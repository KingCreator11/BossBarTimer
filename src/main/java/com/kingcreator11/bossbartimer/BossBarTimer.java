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

package com.kingcreator11.bossbartimer;

import com.kingcreator11.bossbartimer.Commands.*;
import com.kingcreator11.bossbartimer.Config.BossBarHandler;
import com.kingcreator11.bossbartimer.Config.MessagesHandler;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * The main boss bar timer class, represents the plugin
 */
public class BossBarTimer extends JavaPlugin {

	/**
	 * The messages handler for this plugin instance
	 */
	public MessagesHandler messagesHandler = new MessagesHandler(this);

	/**
	 * The boss bar handler for this plugin instance
	 */
	public BossBarHandler bossBarHandler = new BossBarHandler(this);

	/**
	 * The command handler for this plugin instance
	 */
	public CommandHandler commandHandler = new CommandHandler(this);

	/**
	 * Is called when the plugin is activated
	 */
	@Override
	public void onEnable() {
		
		// Load messages
		messagesHandler.loadMessages();

		// Load boss bars
		bossBarHandler.loadBossBars();

		// Commands
		this.getCommand("bbt").setExecutor(commandHandler);
		this.getCommand("bossbartimer").setExecutor(commandHandler);

		this.commandHandler.addSubCommand("help", new HelpCommand(this));
		this.commandHandler.addSubCommand("begin", new BeginCommand(this));
		this.commandHandler.addSubCommand("end", new EndCommand(this));
	}

	/**
	 * Is called when the plugin is disabled
	 */
	@Override
	public void onDisable() {
		
	}
}
