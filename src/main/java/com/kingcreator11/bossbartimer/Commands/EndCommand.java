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
import java.util.List;

import com.kingcreator11.bossbartimer.BossBarTimer;
import com.kingcreator11.bossbartimer.Config.BossBarHandler.BossBarConfig;

import org.bukkit.command.CommandSender;

/**
 * Represents the sub command
 */
public class EndCommand extends SubCommand {
	
	/**
	 * Creates a new sub command handler
	 * @param plugin Plugin pointer
	 */
	public EndCommand(BossBarTimer plugin) {
		super(plugin, new String[] {"bossbartimer.end"}, SubCommandType.argString);
	}

	/**
	 * Called to tab complete this sub command
	 * @param sender The command sender
	 * @param args The arguments (excluding the argument for this subcommand)
	 * @return The tab completion options
	 */
	@Override
	public List<String> tabComplete(CommandSender sender, String[] args) {
		return new ArrayList<>(this.plugin.bossBarHandler.getBossBarNames());
	}
	
	/**
	 * Executes the command
	 */
	@Override
	protected void executeCommand(CommandSender sender, String argument) {
		if (argument.isEmpty()) {
			this.plugin.messagesHandler.sendMessage(sender, "endInvalidArgument");
			return;
		}

		BossBarConfig bar = this.plugin.bossBarHandler.getBossBar(argument);

		if (bar == null) {
			this.plugin.messagesHandler.sendMessage(sender, "endInvalidArgument");
			return;
		}

		bar.bossBar.removeAll();
		bar.bossBar.setVisible(false);

		this.plugin.bossBarHandler.removeBossBar(argument);

		this.plugin.messagesHandler.sendMessage(sender, "endSuccess");
		return;
	}
}
