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

package com.kingcreator11.bossbartimer.Config;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.kingcreator11.bossbartimer.BossBarTimer;
import com.kingcreator11.bossbartimer.BossBarTimerBase;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * Handles all configurable messages for this plugin
 */
public class MessagesHandler extends BossBarTimerBase {

	/**
	 * A map of the configurable messages
	 */
	private Map<String, String> messageMap = new HashMap<>();

	/**
	 * Creates a new message handler instance for this plugin
	 * @param plugin
	 */
	public MessagesHandler(BossBarTimer plugin) {
		super(plugin);

		File configFile = new File(this.plugin.getDataFolder(), "messages.yml");
		if (!configFile.exists())
			this.plugin.saveResource("messages.yml", false);
	}

	/**
	 * Loads the messages from the config file
	 */
	public void loadMessages() {
		File configFile = new File(this.plugin.getDataFolder(), "messages.yml");
		YamlConfiguration config = new YamlConfiguration();
	
		try {
			config.load(configFile);
		} catch (InvalidConfigurationException | IOException e) {
			e.printStackTrace();
			this.plugin.getLogger().severe("Unable to load messages.yml");
		}

		for (String key : config.getKeys(false)) {
			messageMap.put(key, config.getString(key));
		}
	}

	/**
	 * Gets a message given the configuration name
	 * @param key The name of the configuration message
	 * @return The message
	 */
	public String getMessage(String key) {
		String message = messageMap.get(key);
		if (message == null) {
			this.plugin.getLogger().severe("Unable to find message with key "+key+". Please make sure all keys are defined!");
			return null;
		}

		return message.replaceAll("\\\\n", "\n");
	}

	/**
	 * Formats a message
	 * @param key The key of the message
	 * @param replacer The string to replace <var>
	 * @return
	 */
	public String getMessage(String key, String replacer) {
		String message = this.getMessage(key);
		if (message == null) return null;
		return message.replaceAll("\\<var\\>", replacer);
	}

	/**
	 * Sends a message to a receiver and formats the message
	 * @param receiver
	 * @param key
	 * @param replacer
	 */
	public void sendMessage(CommandSender receiver, String key, String replacer) {
		String message = this.getMessage(key, replacer);
		if (message == null) return;
		receiver.sendMessage(message);
	}

	/**
	 * Sends a message to a receiver
	 * @param receiver
	 * @param key
	 */
	public void sendMessage(CommandSender receiver, String key) {
		String message = this.getMessage(key);
		if (message == null) return;
		receiver.sendMessage(message);
	}
}