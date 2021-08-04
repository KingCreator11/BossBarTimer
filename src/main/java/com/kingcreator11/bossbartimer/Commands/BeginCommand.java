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

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.kingcreator11.bossbartimer.BossBarTimer;
import com.kingcreator11.bossbartimer.Config.BossBarHandler.BossBarConfig;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.command.CommandSender;

/**
 * Represents the sub command
 */
public class BeginCommand extends SubCommand {
	
	/**
	 * Creates a new sub command handler
	 * @param plugin Plugin pointer
	 */
	public BeginCommand(BossBarTimer plugin) {
		super(plugin, new String[] {"bossbartimer.begin"}, SubCommandType.argsList);
	}

	/**
	 * Called to tab complete this sub command
	 * @param sender The command sender
	 * @param args The arguments (excluding the argument for this subcommand)
	 * @return The tab completion options
	 */
	@Override
	public List<String> tabComplete(CommandSender sender, String[] args) {
		if (args.length < 2) {
			return Arrays.asList("<Duration>");
		}
		if (args.length < 3) {
			return Arrays.asList("<Name>");
		}
		if (args.length < 4) {
			return Arrays.asList("blue", "green", "pink", "purple", "red", "white", "yellow");
		}
		
		return Arrays.asList("<Text/Title>");
	}

	/**
	 * Parses a single time past string
	 * @param time A time string in the format of AwBdChDmEs with A-E as numerical values
	 * @return The time past as a date
	 */
	private long parseTimePast(String time) {
		long timePast = 0;
		long[] timeModifiers = {60 * 60 * 24 * 7, 60 * 60 * 24, 60 * 60, 60, 1};
		char[] identifiers = {'w', 'd', 'h', 'm', 's'};

		for (int i = 0; i < identifiers.length; i++) {
			Pattern pattern = Pattern.compile("[0-9]*"+identifiers[i]);
			List<String> list = new ArrayList<String>();
			Matcher m = pattern.matcher(time);
			while (m.find()) {
				list.add(m.group());
			}

			if (list.size() > 1) return -1;
			if (list.size() == 0) continue;
			
			try {
				timePast += timeModifiers[i] * Long.parseLong(list.get(0).substring(0, list.get(0).length() - 1));
			}
			catch (NumberFormatException e) {
				return -1;
			}
		}

		return timePast;
	}
	
	/**
	 * Executes the command
	 */
	@Override
	protected void executeCommand(CommandSender sender, String[] args) {

		// Create variables for everything
		BossBarConfig bar = this.plugin.bossBarHandler.createEmptyBossBarConfig();
		BarColor color = null;
		String name = null;
		bar.startTime = Instant.now().getEpochSecond();

		// Check that there are the correct amount of arguments
		if (args.length < 4) {
			this.plugin.messagesHandler.sendMessage(sender, "beginInvalidArgument");
			return;
		}

		// Argument 1 - duration
		long duration = parseTimePast(args[0]);

		if (duration == -1 || duration == 0) {
			this.plugin.messagesHandler.sendMessage(sender, "beginInvalidArgument");
			return;
		}
		
		bar.endTime = bar.startTime + duration;
		
		// Argument 2 - name
		name = args[1];
		if (!name.matches("^[a-zA-Z0-9]*$")) {
			this.plugin.messagesHandler.sendMessage(sender, "beginInvalidArgument");
			return;
		}

		for (String existingName : this.plugin.bossBarHandler.getBossBarNames()) {
			if (name.equals(existingName)) {
				this.plugin.messagesHandler.sendMessage(sender, "beginInvalidArgument");
				return;
			}
		}
		
		// Argument 3 - color
		try {
			color = BarColor.valueOf(args[2].toUpperCase());
		}
		catch (IllegalArgumentException e) {
			this.plugin.messagesHandler.sendMessage(sender, "beginInvalidArgument");
			return;
		}

		// Argument 4 - text
		bar.title = String.join(" ", Arrays.copyOfRange(args, 3, args.length)).replaceAll("&", "§");

		// Create boss bar
		bar.bossBar = Bukkit.createBossBar(bar.title, color, BarStyle.SEGMENTED_12, new BarFlag[0]);

		this.plugin.bossBarHandler.addBossBar(name, bar);
		this.plugin.messagesHandler.sendMessage(sender, "beginSuccess");
		return;
	}
}