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

/**
 * The main boss bar timer base class
 * All other classes in the plugin must inherit from this
 */
public class BossBarTimerBase {
	
	/**
	 * A pointer to the plugin instance this class refers to
	 */
	protected BossBarTimer plugin;

	/**
	 * Creates a new boss bar timer base instance
	 * @param plugin
	 */
	public BossBarTimerBase(BossBarTimer plugin) {
		this.plugin = plugin;
	}
}
