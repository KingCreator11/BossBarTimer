# BossBarTimer

## Simple Description
A simple plugin for allowing the creation of boss bars which have timers/countdowns.

## Commands and Permissions
All the commands and permissions are listed below. Anywhere where `/bbt` is used, `/bossbartimer` may also be used.

| Command | Permission | Description |
|---------|------------|-------------|
| `/bbt` | `bossbartimer.usage` | The main plugin command, brings up a nice little summary. All other sub commands require this permission to be used. |
| `/bbt help` | `bossbartimer.usage` | The help command for this plugin, shows a nice description of each command. |
| `/bbt begin <duration> <name> <color> <text>` | `bossbartimer.begin` | Begins a boss bar timer with a duration formatted in `<weeks>w<days>d<hours>h<minutes>m<seconds>s`. All of the measurements are optional but there must be at least one present. Name must be an alphanumeric name. Color can be blue, green, pink, purple, red, white, or yellow. Text refers to the actual text of the boss bar. You can format this using any of the time indicators placed in the duration which will be replaced. An example would be `/bbt begin 1h expboost blue &6&l2x &c&lExp Boost! &6&l<minutes> &c&lminutes and &6&l<seconds> &c&lseconds left!`. Boss bars are persistent throughout restarts and are not lag dependent. |
| `/bbt end <name>` | `bossbartimer.end` | Ends the boss bar named `<name>` |

## Config


## License
```
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
```