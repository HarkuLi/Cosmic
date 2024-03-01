/*
This file is part of the OdinMS Maple Story Server
Copyright (C) 2008 Patrick Huy <patrick.huy@frz.cc>
Matthias Butz <matze@odinms.de>
Jan Christian Meyer <vimes@odinms.de>

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
package scripting.map;

import client.Character.DelayedQuestUpdate;
import client.Client;
import client.QuestStatus;
import constants.id.MapId;
import scripting.AbstractPlayerInteraction;
import server.quest.Quest;
import tools.PacketCreator;

public class MapScriptMethods extends AbstractPlayerInteraction {

    private final String rewardstring = " title has been rewarded. Please see NPC Dalair to receive your Medal.";

    public MapScriptMethods(Client c) {
        super(c);
    }

    public void displayCygnusIntro() {
        switch (c.getPlayer().getMapId()) {
            case MapId.CYGNUS_INTRO_LEAD -> {
                lockUI();
                c.sendPacket(PacketCreator.showIntro("Effect/Direction.img/cygnusJobTutorial/Scene0"));
            }
            case MapId.CYGNUS_INTRO_WARRIOR -> c.sendPacket(PacketCreator.showIntro("Effect/Direction.img/cygnusJobTutorial/Scene1"));
            case MapId.CYGNUS_INTRO_BOWMAN -> c.sendPacket(PacketCreator.showIntro("Effect/Direction.img/cygnusJobTutorial/Scene2"));
            case MapId.CYGNUS_INTRO_MAGE -> c.sendPacket(PacketCreator.showIntro("Effect/Direction.img/cygnusJobTutorial/Scene3"));
            case MapId.CYGNUS_INTRO_PIRATE -> c.sendPacket(PacketCreator.showIntro("Effect/Direction.img/cygnusJobTutorial/Scene4"));
            case MapId.CYGNUS_INTRO_THIEF -> c.sendPacket(PacketCreator.showIntro("Effect/Direction.img/cygnusJobTutorial/Scene5"));
            case MapId.CYGNUS_INTRO_CONCLUSION -> {
                lockUI();
                c.sendPacket(PacketCreator.showIntro("Effect/Direction.img/cygnusJobTutorial/Scene6"));
            }
        }
    }

    public void displayAranIntro() {
        switch (c.getPlayer().getMapId()) {
            case MapId.ARAN_TUTO_1 -> {
                lockUI();
                c.sendPacket(PacketCreator.showIntro("Effect/Direction1.img/aranTutorial/Scene0"));
            }
            case MapId.ARAN_TUTO_2 -> c.sendPacket(PacketCreator.showIntro("Effect/Direction1.img/aranTutorial/Scene1" + c.getPlayer().getGender()));
            case MapId.ARAN_TUTO_3 -> c.sendPacket(PacketCreator.showIntro("Effect/Direction1.img/aranTutorial/Scene2" + c.getPlayer().getGender()));
            case MapId.ARAN_TUTO_4 -> c.sendPacket(PacketCreator.showIntro("Effect/Direction1.img/aranTutorial/Scene3"));
            case MapId.ARAN_POLEARM -> {
                lockUI();
                c.sendPacket(PacketCreator.showIntro("Effect/Direction1.img/aranTutorial/HandedPoleArm" + c.getPlayer().getGender()));
            }
        }
    }

    public void startExplorerExperience() {
        switch (c.getPlayer().getMapId()) {
        case 1020100: //Swordman
            c.sendPacket(PacketCreator.showIntro("Effect/Direction3.img/swordman/Scene" + c.getPlayer().getGender()));
            break;
        case 1020200: //Magician
            c.sendPacket(PacketCreator.showIntro("Effect/Direction3.img/magician/Scene" + c.getPlayer().getGender()));
            break;
        case 1020300: //Archer
            c.sendPacket(PacketCreator.showIntro("Effect/Direction3.img/archer/Scene" + c.getPlayer().getGender()));
            break;
        case 1020400: //Rogue
            c.sendPacket(PacketCreator.showIntro("Effect/Direction3.img/rogue/Scene" + c.getPlayer().getGender()));
            break;
        case 1020500: //Pirate
            c.sendPacket(PacketCreator.showIntro("Effect/Direction3.img/pirate/Scene" + c.getPlayer().getGender()));
            break;
        }
    }

    public void goAdventure() {
        lockUI();
        c.sendPacket(PacketCreator.showIntro("Effect/Direction3.img/goAdventure/Scene" + c.getPlayer().getGender()));
    }

    public void goLith() {
        lockUI();
        c.sendPacket(PacketCreator.showIntro("Effect/Direction3.img/goLith/Scene" + c.getPlayer().getGender()));
    }

    public void explorerQuest(short questid, String questName) {
        Quest quest = Quest.getInstance(questid);
        if (isQuestCompleted(questid)) {
            return;
        }
        
        if (!isQuestStarted(questid)) {
            if (!quest.forceStart(getPlayer(), 9000066)) {
                return;
            }
        }
        QuestStatus qs = getPlayer().getQuest(quest);
        if (!qs.addMedalMap(getPlayer().getMapId())) {
            return;
        }
        String status = Integer.toString(qs.getMedalProgress());
        String infoex = qs.getInfoEx(0);

        // explorer quests all have an infoex/infonumber requirement that points to another quest
        // THAT quest's progress needs to be updated for Quest.canComplete() to return true
        getPlayer().setQuestProgress(quest.getId(), (int)quest.getInfoNumber(qs.getStatus()), status);

        StringBuilder smp = new StringBuilder();
        StringBuilder etm = new StringBuilder();
        if (status.equals(infoex)) {
            etm.append("Earned the ").append(questName).append(" title!");
            smp.append("You have earned the <").append(questName).append(">").append(rewardstring);
            getPlayer().sendPacket(PacketCreator.getShowQuestCompletion(quest.getId()));
        } else {
            getPlayer().sendPacket(PacketCreator.earnTitleMessage(status + "/" + infoex + " regions explored."));
            etm.append("Trying for the ").append(questName).append(" title.");
            smp.append("You made progress on the ").append(questName).append(" title. ").append(status).append("/").append(infoex);
        }
        getPlayer().sendPacket(PacketCreator.earnTitleMessage(etm.toString()));
        showInfoText(smp.toString());
    }

    public void touchTheSky() {
        int questId = 29004;
        if (isQuestCompleted(questId)) {
            return;
        }

        Quest quest = Quest.getInstance(questId);
        if (!isQuestStarted(questId)) {
            if (!quest.forceStart(getPlayer(), 9000066)) {
                return;
            }
        }

        QuestStatus qs = getPlayer().getQuest(quest);

        int goalProgress = Integer.valueOf(qs.getInfoEx(0));
        int medalId = 1142111;

        if (qs.getMedalProgress() + 1 == goalProgress && !getPlayer().canHold(medalId)) {
            showInfoText(
                "You will be awarded the \"The One Who's Touched the Sky\" title after stepping on this place, but your inventory is full."
                + " Please clean your inventory and try again later."
            );
            return;
        }

        if (!qs.addMedalMap(getPlayer().getMapId())) {
            return;
        }

        String status = Integer.toString(qs.getMedalProgress());
        getPlayer().announceUpdateQuest(DelayedQuestUpdate.UPDATE, qs, true);
        getPlayer().sendPacket(PacketCreator.earnTitleMessage(status + "/5 Completed"));
        getPlayer().sendPacket(PacketCreator.earnTitleMessage("The One Who's Touched the Sky title in progress."));

        if (qs.getMedalProgress() == goalProgress) {
            getPlayer().sendPacket(PacketCreator.getShowQuestCompletion(quest.getId()));
            gainItem(medalId);
            forceCompleteQuest(questId);
            showInfoText("The One Who's Touched the Sky title has been rewarded. Besides, the quest has been auto-completed.");
        } else {
            showInfoText("The One Who's Touched the Sky title in progress. " + status + "/5 Completed");
        }
    }
}
