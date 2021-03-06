/*
 * Copyright 2014 Aleksander.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package pl.grzegorz2047.openguild.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import pl.grzegorz2047.openguild.antilogout.AntiLogoutManager;
import pl.grzegorz2047.openguild.database.SQLHandler;
import pl.grzegorz2047.openguild.guilds.Guilds;
import pl.grzegorz2047.openguild.managers.MsgManager;
import pl.grzegorz2047.openguild.metadata.PlayerMetadataController;
import pl.grzegorz2047.openguild.ranking.EloRanking;
import pl.grzegorz2047.openguild.ranking.RankDifference;

import java.util.UUID;

/**
 * @author Aleksander
 */
public class PlayerDeathListener implements Listener {

    private final SQLHandler sqlHandler;
    private final AntiLogoutManager antiLogoutManager;
    private final Guilds guilds;

    public PlayerDeathListener(SQLHandler sqlHandler, AntiLogoutManager antiLogoutManager, Guilds guilds) {
        this.sqlHandler = sqlHandler;
        this.antiLogoutManager = antiLogoutManager;
        this.guilds = guilds;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        Player lost = e.getEntity();
        Player killer = lost.getKiller();
        String lostName = lost.getName();
        antiLogoutManager.removePlayerFromFight(lostName);
        sqlHandler.addDeath(lost);
        if (killer == null) {
            return;
        }
        sqlHandler.addKill(killer);
        String killerName = killer.getName();
        antiLogoutManager.removePlayerFromFight(killerName);

        RankDifference rankDifference = getRankDifference(lost, killer);
        updatePlayerEloData(lost, killer, rankDifference);
    }

    private RankDifference getRankDifference(Player lost, Player killer) {
        PlayerMetadataController.PlayerMetaDataColumn eloColumn = PlayerMetadataController.PlayerMetaDataColumn.ELO;
        String eloColumnName = eloColumn.name();
        int lostOldPoints = lost.getMetadata(eloColumnName).get(0).asInt();
        int killerOldPoints = killer.getMetadata(eloColumnName).get(0).asInt();
        return EloRanking.recountEloFight(lostOldPoints, killerOldPoints);
    }

    private void updatePlayerEloData(Player lost, Player killer, RankDifference rankDifference) {

        Bukkit.broadcastMessage(MsgManager.get("killerkilledvictim").
                replace("{VICTIM}", lost.getName()).
                replace("{KILLER}", killer.getName()).
                replace("{VICTIMLOSE}", -rankDifference.getLostDifference() + "").
                replace("{KILLEREARN}", -rankDifference.getWinDifference() + ""));

        UUID killerUniqueId = killer.getUniqueId();
        UUID lostUniqueId = lost.getUniqueId();
        sqlHandler.updatePlayersElo(killerUniqueId, (int) rankDifference.getWinNewPoints(), lostUniqueId, (int) rankDifference.getLostNewPoints());
        //set last kill to winner w metadata i potem jak zginie ten sam nie nalicza elo
        guilds.updatePlayersElo(killer, lost, rankDifference);
    }


}
