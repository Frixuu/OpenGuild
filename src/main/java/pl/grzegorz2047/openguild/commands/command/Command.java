/*
 * Copyright 2014
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

package pl.grzegorz2047.openguild.commands.command;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

public abstract class Command {
    private String permission;
    private List<String> aliases;

    public Command(String[] aliases) {
        this.aliases = Arrays.asList(aliases);
    }

    public abstract void execute(CommandSender sender, String[] args) throws CommandException;

    public String getPermission() {
        return permission;
    }

    protected String getTitle(String title) {
        String label = ChatColor.GRAY + "------------------" + ChatColor.DARK_GRAY + "[" + ChatColor.RESET;
        String label2 = ChatColor.DARK_GRAY + "]" + ChatColor.GRAY + "------------------" + ChatColor.RESET;
        return label + " " + title + ChatColor.RESET + " " + label2;
    }

    public boolean hasPermission() {
        return permission != null;
    }

    public abstract int minArgs();

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public List<String> getAliases() {
        return aliases;
    }
}
