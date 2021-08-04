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
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.kingcreator11.bossbartimer.BossBarTimer;
import com.kingcreator11.bossbartimer.BossBarTimerBase;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Handles all boss bar timers storage in this plugin
 */
public class BossBarHandler extends BossBarTimerBase implements Listener {

	/**
	 * Stores a single boss bar
	 */
	public class BossBarConfig {
		public long startTime;
		public long endTime;
		public String title;
		public BossBar bossBar;
	}

	/**
	 * Stores all boss bars currently active
	 */
	private Map<String, BossBarConfig> bossBars = new HashMap<>();

	/**
	 * Runnable for updating boss bars
	 */
	private BukkitRunnable bossUpdatingRunnable = new BukkitRunnable() {

		/**
		 * Updates every tick
		 */
		@Override
		public void run() {
			List<String> toRemove = new ArrayList<>();
			for (String name : bossBars.keySet()) {
				BossBarConfig bar = bossBars.get(name);
				String title = bar.title;
				long timeLeft = bar.endTime - Instant.now().getEpochSecond();
				double progress = (double) timeLeft / (double) (bar.endTime - bar.startTime);

				if (timeLeft > 0) {
					title = title.replaceAll("\\<weeks\\>", String.valueOf(timeLeft / 604800l));
					title = title.replaceAll("\\<days\\>", String.valueOf((timeLeft % 604800l) / 86400l));
					title = title.replaceAll("\\<hours\\>", String.valueOf((timeLeft % 86400l) / 3600l));
					title = title.replaceAll("\\<minutes\\>", String.valueOf((timeLeft % 3600l) / 60l));
					title = title.replaceAll("\\<seconds\\>", String.valueOf(timeLeft % 60l));
	
					bar.bossBar.setTitle(title);
					bar.bossBar.setProgress(progress);
				}
				else {
					toRemove.add(name);
				}
			}

			for (String name : toRemove) {
				BossBarConfig bar = bossBars.get(name);
				bar.bossBar.removeAll();
				bar.bossBar.setVisible(false);
				removeBossBar(name); 
			}
		}
		
	}; 

	/**
	 * Creates a new boss bar handler instance for this plugin
	 * @param plugin
	 */
	public BossBarHandler(BossBarTimer plugin) {
		super(plugin);

		File configFile = new File(this.plugin.getDataFolder(), "bossbars.yml");
		if (!configFile.exists())
			this.plugin.saveResource("bossbars.yml", false);
	}

	/**
	 * Loads the boss bars from the config file
	 */
	public void loadBossBars() {
		// Register events
		this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);

		File configFile = new File(this.plugin.getDataFolder(), "bossbars.yml");
		YamlConfiguration config = new YamlConfiguration();
	
		try {
			config.load(configFile);
		}
		catch (InvalidConfigurationException | IOException e) {
			e.printStackTrace();
			this.plugin.getLogger().severe("Unable to load bossbars.yml");
		}

		for (String key : config.getKeys(false)) {
			BossBarConfig bar = new BossBarConfig();
			bar.title = config.getString(key + ".title");
			bar.startTime = config.getLong(key + ".startTime");
			bar.endTime = config.getLong(key + ".endTime");
			bar.bossBar = Bukkit.createBossBar(bar.title, BarColor.valueOf(config.getString(key + ".color")),
				BarStyle.SEGMENTED_12, new BarFlag[0]);
			bar.bossBar.setVisible(true);
			for (Player player : Bukkit.getOnlinePlayers())
				bar.bossBar.addPlayer(player);
			bossBars.put(key, bar);
		}

		bossUpdatingRunnable.runTaskTimer(this.plugin, 0, 1);
	}

	/**
	 * Gets a boss bar under a name
	 * @param name The name
	 * @return The boss bar
	 */
	public BossBarConfig getBossBar(String name) {
		return this.bossBars.get(name);
	}

	/**
	 * Saves a new boss bar under a name
	 * @param name The name
	 * @param bar The boss bar
	 */
	public void addBossBar(String name, BossBarConfig bar) {
		this.bossBars.put(name, bar);

		File configFile = new File(this.plugin.getDataFolder(), "bossbars.yml");
		YamlConfiguration config = new YamlConfiguration();
		
		try {
			config.load(configFile);
			config.set(name + ".title", bar.title);
			config.set(name + ".startTime", bar.startTime);
			config.set(name + ".endTime", bar.endTime);
			config.set(name + ".color", bar.bossBar.getColor().toString());
			config.save(configFile);

			bar.bossBar.setVisible(true);
			for (Player player : Bukkit.getOnlinePlayers())
				bar.bossBar.addPlayer(player);
		}
		catch (InvalidConfigurationException | IOException e) {
			e.printStackTrace();
			this.plugin.getLogger().severe("Unable to load bossbars.yml");
		}
	}

	/**
	 * Removes a boss bar under a name
	 * @param name the name
	 * @param bar the bar
	 */
	public void removeBossBar(String name) {
		this.bossBars.remove(name);

		File configFile = new File(this.plugin.getDataFolder(), "bossbars.yml");
		YamlConfiguration config = new YamlConfiguration();
		
		try {
			config.load(configFile);
			config.set(name, null);
			config.save(configFile);
		}
		catch (InvalidConfigurationException | IOException e) {
			e.printStackTrace();
			this.plugin.getLogger().severe("Unable to load bossbars.yml");
		}
	}

	/**
	 * Returns all boss bar names
	 * @return All boss bar names
	 */
	public Set<String> getBossBarNames() {
		return this.bossBars.keySet();
	}

	/**
	 * Creates an empty BossBarConfig
	 * @return an empty BossBarConfig
	 */
	public BossBarConfig createEmptyBossBarConfig() {
		return new BossBarConfig();
	}

	// -------------------------------------- Event Handlers --------------------------------------

	/**
	 * Called when a player joins
	 * Loads all active boss bars on them
	 * @param event
	 */
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		if (event.getPlayer() == null) return;
		
		for (BossBarConfig bar : bossBars.values()) {
			bar.bossBar.addPlayer(event.getPlayer());
		}
	}

	/**
	 * Called when a player leaves
	 * Removes all boss bars from them
	 * @param event
	 */
	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent event) {
		if (event.getPlayer() == null) return;
		
		for (BossBarConfig bar : bossBars.values()) {
			bar.bossBar.removePlayer(event.getPlayer());
		}
	}
}