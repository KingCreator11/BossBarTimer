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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kingcreator11.bossbartimer.BossBarTimer;
import com.kingcreator11.bossbartimer.BossBarTimerBase;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

/**
 * Handles the basic commands of /bbt
 */
public class CommandHandler extends BossBarTimerBase implements CommandExecutor, TabCompleter {

	/**
	 * The map of sub commands
	 */
	private Map<String, SubCommand> subCommandMap = new HashMap<>();

	/**
	 * Creates a new command handler
	 * @param plugin Pointer to plugin instance
	 */
	public CommandHandler(BossBarTimer plugin) {
		super(plugin);
	}

	/**
	 * Adds a sub command to the command handler
	 * @param subCommand The sub command to add
	 * @param commandHandler The handler for the sub command
	 */
	public void addSubCommand(String subCommand, SubCommand commandHandler) {
		subCommandMap.put(subCommand, commandHandler);
	}

	/**
	 * Removes a sub command to the command handler
	 * @param subCommand The sub command to remove
	 */
	public void removeSubCommand(String subCommand, SubCommand commandHandler) {
		subCommandMap.remove(subCommand);
	}

	/**
	 * Called when tab completion for commands is required
	 */
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		if (args.length > 1) {
			SubCommand subCommand = subCommandMap.get(args[0]);

			if (subCommand == null) {
				return Arrays.asList("Unknown Sub-Command");
			}
	
			if (!subCommand.hasPerms(sender)) {
				return Arrays.asList("Insufficient Privileges");
			}

			return subCommand.tabComplete(sender, Arrays.copyOfRange(args, 1, args.length));
		}

		List<String> completions = new ArrayList<>();

		String searchKey = args.length > 0 ? args[0] : "";

		for (String subCommand : this.subCommandMap.keySet())
			if (subCommand.contains(searchKey) && this.subCommandMap.get(subCommand).hasPerms(sender))
				completions.add(subCommand);

		return completions;
	}

	/**
	 * Called when a command is used
	 */
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length == 0) {
			String message = "§c>---------- §6Boss Bar Timer ----------<\n" +
				"§2Version: §a1.00\n" +
				"§2Author: §aKingCreator11\n" +
				"§2Description: §aA simple plugin for allowing the creation of boss bars which have timers/countdowns.\n" +
				"§c>----------------------------------<";
			sender.sendMessage(message);
			return true;
		}

		SubCommand subCommand = subCommandMap.get(args[0]);

		if (subCommand == null) {
			this.plugin.messagesHandler.sendMessage(sender, "commandNotFound");
			return true;
		}

		if (!subCommand.hasPerms(sender)) {
			this.plugin.messagesHandler.sendMessage(sender, "insufficientPrivileges");
			return true;
		}

		try {
			subCommand.executeCommand(args.length > 1 ? Arrays.copyOfRange(args, 1, args.length) : new String[]{}, sender);
		}
		catch (Exception e) {
			this.plugin.messagesHandler.sendMessage(sender, "unknownCommandError");
			e.printStackTrace();
		}
		
		return true;
	}
}