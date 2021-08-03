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

import org.bukkit.plugin.java.JavaPlugin;

/**
 * The main boss bar timer class, represents the plugin
 */
public class BossBarTimer extends JavaPlugin {

	/**
	 * Is called when the plugin is activated
	 */
	@Override
	public void onEnable() {
		this.getLogger().info("Hello World!");
	}

	/**
	 * Is called when the plugin is disabled
	 */
	@Override
	public void onDisable() {
		
	}
}
