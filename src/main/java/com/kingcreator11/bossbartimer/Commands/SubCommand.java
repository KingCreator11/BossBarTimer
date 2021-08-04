/*
   Copyright 2021 KingCreator11

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

	   http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/

package com.kingcreator11.bossbartimer.Commands;

import java.util.Arrays;
import java.util.List;

import com.kingcreator11.bossbartimer.BossBarTimer;
import com.kingcreator11.bossbartimer.BossBarTimerBase;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Represents a single sub command
 */
public abstract class SubCommand extends BossBarTimerBase {

	/**
	 * Represents what type of arguments this sub command requires when handling the command
	 */
	protected enum SubCommandType {
		argsList, argString
	}

	/**
	 * Represents what type of sub command this is
	 */
	private SubCommandType subCommandType;

	/**
	 * A list of all required permissions to use the command
	 * A sender must have all the permissions in this list to use the command
	 */
	private String[] requiredPermissions;

	/**
	 * Creates a new sub command
	 * @param plugin A pointer to the plugin instance
	 * @param requiredPermissions The required permissions to use this sub command
	 * @param subCommandType The type of sub command this is
	 */
	public SubCommand(BossBarTimer plugin, String[] requiredPermissions, SubCommandType subCommandType) {
		super(plugin);
		this.requiredPermissions = requiredPermissions;
		this.subCommandType = subCommandType;
	}

	/**
	 * Executes the sub command
	 * @param args The arguments list (Excluding the name of the subcommand itself)
	 * @param sender The sender of the command
	 */
	public void executeCommand(String[] args, CommandSender sender) {
		switch (this.subCommandType) {
			case argString:
				this.executeCommand(sender, String.join(" ", args));
				break;
			case argsList:
				this.executeCommand(sender, args);
				break;
		}
	}

	/**
	 * Checks whether or not a sender has permissions to use this sub command
	 * @param sender The sender to check for
	 * @return
	 */
	public boolean hasPerms(CommandSender sender) {
		for (int i = 0; i < this.requiredPermissions.length; i++)
			if (!sender.hasPermission(this.requiredPermissions[i]))
				return false;

		return true;
	}

	/**
	 * Creates an item intended for a GUI
	 * @param name The name of the item
	 * @param lore The lore for the item
	 * @param material The material for the item
	 * @return The item
	 */
	protected ItemStack createGUIItem(String name, String[] lore, Material material) {
		ItemStack item = new ItemStack(material);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		meta.setLore(Arrays.asList(lore));
		item.setItemMeta(meta);
		return item;
	}

	/**
	 * Override this - This is called when the sub command is used.
	 * This method in specific is called if you set the SubCommandType to argString
	 * @param sender The sender of the command
	 * @param argument The arguments required 
	 */
	protected void executeCommand(CommandSender sender, String argument) {}

	/**
	 * Override this - This is called when the sub command is used.
	 * This method in specific is called if you set the SubCommandType to argsList
	 * @param sender The sender of the command
	 * @param argument The arguments required 
	 */
	protected void executeCommand(CommandSender sender, String[] args) {}

	/**
	 * Override this - This is called when tab completion for this sub command is required.
	 * @param sender The sender of the command
	 * @param args The arguments required
	 * @return The tab completion options
	 */
	public abstract List<String> tabComplete(CommandSender sender, String[] args);
}