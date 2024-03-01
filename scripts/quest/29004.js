/*
    This file is part of the HeavenMS MapleStory Server
    Copyleft (L) 2016 - 2019 RonanLana

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation version 3 as published by
    the Free Software Foundation. You may not use, modify or distribute
    this program under any other version of the GNU Affero General Public
    License.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
/*
    Author: Harku Li
    Dsecription: Quest - The One Who's Touched the Sky
    Quest ID: 29004

    This script doesn't work currently.
 */

var status = -1;

function start(mode, type, selection) {
    if (qm.forceStartQuest()) {
        qm.showInfoText("You have earned the <The One Who's Touched the Sky> title. You can receive a Medal from NPC Dalair.");
    }
    qm.dispose();
}

function end(mode, type, selection) {
    if (mode == -1 || (mode == 0 && type > 0)) {
        qm.dispose();
        return;
    }
    
    if (mode == 1) {
        status++;
    } else {
        status--;
    }
    
    if (status == 0) {
        qm.sendNext(
            "Congratulations on earning your honorable #b<The One Who's Touched the Sky>#k title."
            + " I wish you the best of luck in your future endeavors!"
            + " Keep up the good work.\r\n\r\n#fUI/UIWindow.img/QuestIcon/4/0#\r\n #v1142111:# #t1142111# 1"
        );
    } else if (status == 1) {
        if (qm.canHold(1142111)) {
            qm.gainItem(1142111);
            qm.forceCompleteQuest();
        } else {
            qm.dropMessage(1, "Your inventory is full");
        }

        qm.dispose();
    }
}
