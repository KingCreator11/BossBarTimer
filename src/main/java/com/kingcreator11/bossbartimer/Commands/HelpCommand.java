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
import java.util.List;

import com.kingcreator11.bossbartimer.BossBarTimer;

import org.bukkit.command.CommandSender;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;

/**
 * Represents the sub command
 */
public class HelpCommand extends SubCommand {

	/**
	 * A helper class to provide the info for a single command
	 */
	private class CommandInfo {

		/**
		 * How the command is to be used
		 */
		public String usage;

		/**
		 * The description of the command
		 */
		public String description;

		/**
		 * The permission required to use this command
		 */
		public String permission = "bossbartimer.usage";

		/**
		 * Creates a new command info instance
		 * @param usage How the command is to be used
		 * @param description The description of the command
		 */
		public CommandInfo(String usage, String description) {
			this.usage = usage;
			this.description = description;
		}

		/**
		 * Creates a new command info instance
		 * @param usage How the command is to be used
		 * @param description The description of the command
		 * @param permission The permission required to use this command
		 */
		public CommandInfo(String usage, String description, String permission) {
			this.usage = usage;
			this.description = description;
			this.permission = permission;
		}
	}

	/**
	 * The command information for every sub command in the plugin
	 */
	private CommandInfo[] commandInfo = {
		new CommandInfo(
			this.plugin.messagesHandler.getMessage("helpHelpUsage"),
			this.plugin.messagesHandler.getMessage("helpHelpDescription")
		),
		new CommandInfo(
			this.plugin.messagesHandler.getMessage("helpBeginUsage"),
			this.plugin.messagesHandler.getMessage("helpBeginDescription"),
			"bossbartimer.begin"
		),
		new CommandInfo(
			this.plugin.messagesHandler.getMessage("helpEndUsage"),
			this.plugin.messagesHandler.getMessage("helpEndDescription"),
			"bossbartimer.end"
		)
	};

	/**
	 * Creates a new sub command handler
	 * @param plugin Plugin pointer
	 */
	public HelpCommand(BossBarTimer plugin) {
		super(plugin, new String[] {"bossbartimer.usage"}, SubCommandType.argString);
	}

	/**
	 * Called to tab complete this sub command
	 * @param sender The command sender
	 * @param args The arguments (excluding the argument for this subcommand)
	 * @return The tab completion options
	 */
	@Override
	public List<String> tabComplete(CommandSender sender, String[] args) {
		return Arrays.asList("<page>");
	}
	
	/**
	 * Executes the command
	 */
	@Override
	protected void executeCommand(CommandSender sender, String argument) {
		String message = this.plugin.messagesHandler.getMessage("helpHeader")+"\n";
		int page = 1;
		List<CommandInfo> infoSubset = new ArrayList<>();

		for (int i = 0; i < commandInfo.length; i++)
			if (sender.hasPermission(commandInfo[i].permission))
				infoSubset.add(commandInfo[i]);

		int numPages = (int) Math.ceil((double) infoSubset.size() / 5.0);

		try {
			page = Integer.parseInt(argument);
			if (page > numPages) page = numPages;
			if (page < 1) page = 1;
		}
		catch (NumberFormatException e) {
			// Do nothing - just give the first page of the help
		}

		for (int i = (page-1)*5; i < (page*5 > infoSubset.size() ? infoSubset.size() : page*5); i++) {
			CommandInfo cmdInfo = infoSubset.get(i);
			String template = this.plugin.messagesHandler.getMessage("helpTemplate");
			template = template.replaceAll("\\<usage\\>", cmdInfo.usage);
			template = template.replaceAll("\\<description\\>", cmdInfo.description);
			message += template + "\n";
		}

		String footer = this.plugin.messagesHandler.getMessage("helpFooter");
		footer = footer.replaceAll("\\<page\\>", String.valueOf(page));
		footer = footer.replaceAll("\\<max\\>", String.valueOf(numPages));
		message += footer;
		sender.sendMessage(message);

		if (page > 1 || page < numPages) {
			TextComponent textComponent = new TextComponent();
			if (page > 1) {
				textComponent.addExtra("§6<---- §cPrevious Page");
				textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
					new Text("Click on this to go to the previous help page!")));
				textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/bbt help "+(page - 1)));
			}
			if (page > 1 && page < numPages)
				textComponent.addExtra(" ======== ");
			if (page < numPages) {
				textComponent.addExtra("§cNext Page §6---->");
				textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
					new Text("Click on this to go to the next help page!")));
				textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/bbt help "+(page + 1)));
			}

			sender.spigot().sendMessage(textComponent);
		}
	}
}
