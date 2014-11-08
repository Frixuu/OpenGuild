/*
 * The MIT License
 *
 * Copyright 2014 Grzegorz.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package pl.grzegorz2047.openguild2047.managers;

import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import pl.grzegorz2047.openguild2047.OpenGuild;
import pl.grzegorz2047.openguild2047.SimpleGuild;

/**
 *
 * @author Grzegorz
 */
public class TagManager {
    
    private OpenGuild plugin;
   
    private static Scoreboard sc;//Mozna trzymac w pamieci tej klasy, zeby nie bawic sie tym za bardzo.
    
    public TagManager(OpenGuild plugin){
        this.plugin = plugin;
        
        if(!TagManager.isInitialised()){//Kiedy trzeba to mozna zainicjowac scoreboard np. przy onEnable()
            sc = Bukkit.getScoreboardManager().getNewScoreboard();
        }
    }
    /*
      Uzycie tagow z teamow z Bukkit API zamiast NametagEdit API.
      Potrzeba metoda do rejestracji, do czyszczenia teamow przy wylaczaniu pluginow
      oraz dodawaniu/usuwaniu gracza z teamtagu.
    
      A ponizej przykladowa metoda rejestracji teamtagu
    */
    public static Scoreboard getScoreboard(){
        return TagManager.sc;
    }
    public void updateBoard(){
            for(Player p : Bukkit.getOnlinePlayers()){
                p.setScoreboard(TagManager.getScoreboard());
            }
    }
    
    private boolean registerTeamTag(SimpleGuild sg){
        if(!TagManager.isInitialised()){
            System.out.println("Scoreboard nie jest zainicjowany!");
            return false;
        }
        String tag = sg.getTag().toUpperCase();
        if(sc.getTeam(tag)== null){
            Team teamtag = sc.registerNewTeam(tag);
            teamtag.setPrefix(com.github.grzegorz2047.openguild.OpenGuild.getGuildManager().getNicknameTag().replace("{TAG}", sg.getTag().toUpperCase()));
            teamtag.setDisplayName(com.github.grzegorz2047.openguild.OpenGuild.getGuildManager().getNicknameTag().replace("{TAG}", sg.getTag().toUpperCase()));
            for(UUID uuid : sg.getMembers()){
                teamtag.addPlayer(Bukkit.getOfflinePlayer(uuid));
            }
            updateBoard();
            return true;
        }
        updateBoard();
        return false;
    }
    
    public boolean setTag(UUID player){
        if(TagManager.isInitialised()){
            if(plugin.getGuildHelper().hasGuild(player)){
                //System.out.println("gracz w gildii");
                SimpleGuild g =plugin.getGuildHelper().getPlayerGuild(player);
                Team t = sc.getTeam(g.getTag().toUpperCase());
                if(t == null){
                    //System.out.println("Brak team pref");
                    registerTeamTag(g);
                    return true;
                }else{
                    //System.out.println("gracz w gildii team pref istnieje");
                    if(!t.getPlayers().contains(Bukkit.getOfflinePlayer(player))){
                        t.addPlayer(Bukkit.getOfflinePlayer(player));
                        updateBoard();
                        return true;
                    }else{
                        updateBoard();
                        return true;
                    }
                }
            }else{
                Bukkit.getPlayer(player).setScoreboard(TagManager.getScoreboard());
            }
        }else{
            //System.out.println("Nie zainicjalizowano tag managera");
        }
        return false;
    }
    public boolean removeTag(UUID player){
        if(TagManager.isInitialised()){
            if(plugin.getGuildHelper().hasGuild(player)){
                SimpleGuild g = plugin.getGuildHelper().getPlayerGuild(player);
                Team t = sc.getTeam(g.getTag().toUpperCase());
                if(t == null){
                    updateBoard();
                    return true;
                }else{
                    if(t.getPlayers().contains(Bukkit.getOfflinePlayer(player))){
                        t.removePlayer(Bukkit.getOfflinePlayer(player));
                        updateBoard();
                        return true;
                    }else{
                        updateBoard();
                        return true;// moze tak
                    }
                }
            }
        }
        return false;
    }
    
    public static boolean isInitialised(){
        return sc!= null; 
    }
    
}
