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
package com.github.grzegorz2047.openguild.event.guild;

import com.github.grzegorz2047.openguild.Guild;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 *
 * @author Aleksander
 */
public class GuildDisbandEvent extends GuildEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancel;
    
    public GuildDisbandEvent(Guild guild) {
        super(guild);
    }
    
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    
    @Override
    public boolean isCancelled() {
        return cancel;
    }
    
    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }
}
