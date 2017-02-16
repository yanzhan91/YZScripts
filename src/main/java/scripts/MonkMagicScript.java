package scripts;

import org.tribot.api.input.Mouse;
import org.tribot.api.util.abc.ABCUtil;
import org.tribot.api2007.*;
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.types.RSNPC;
import org.tribot.api2007.types.RSTile;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;

import java.util.Date;
import java.util.Random;

@SuppressWarnings("unused")
@ScriptManifest(authors = {"YZ"}, category = "Magic", name = "YZMonkMagicScript")
public class MonkMagicScript extends Script {

    private static final int MIND_RUNE = 558;
    private static final int AIR_RUNE = 556;
    private static final int FIRE_RUNE = 554;
    private static final int STAFF_OF_AIR = 1381;
    private static final int STAFF_OF_FIRE = 1387;
    private static final int BODY_RUNE = 559;
    private static final int WATER_RUNE = 555;
    private static final int EARTH_RUNE = 557;
    private static final int STAFF_OF_WATER = 1383;
    private static final int STAFF_OF_EARTH = 1385;
    private static final int MONK_OF_ZAMORAK = 2886;
    private static final int CURSE_LEVEL = 19;
    private static final int FIRE_STRIKE_LEVEL = 13;
    private static final int WIND_STRIKE_LEVEL = 0;

    private RSNPC monkOfZamorak = null;
    private Random random = new Random(new Date().getTime());
    private ABCUtil abcUtil = new ABCUtil();

    @Override
    public void run() {
        Mouse.setSpeed(150);
        try {
            while (true) {
                if (!Player.isMoving()) {
                    verifyMonkIsNearby();
                    verifyPlayerInRightPosition();
                    verifyArmorIsEquipped();

                    performAntiBan();
                    selectSpell();
                }
                sleep(random.nextInt(1000) + 1500);
            }
        } catch (YZScriptException mme) {
            System.out.println(mme.getMessage());
        }
    }

    private void verifyMonkIsNearby() {
        RSNPC[] npcs = NPCs.findNearest(MONK_OF_ZAMORAK);
        if (npcs.length < 1) {
            throw new YZScriptException("No Monk of Zamorak nearby. Please move to varrok castle near the Monk of Zamorak.");
        }
        monkOfZamorak = npcs[0];
    }

    private void verifyPlayerInRightPosition() {
        while (Player.getPosition().distanceTo(new RSTile(3214,3476)) > 2) {
            Walking.walkTo(new RSTile(3214,3476));
            sleep(1000);
        }
    }

    private void verifyArmorIsEquipped() {
        if (Equipment.getItem(Equipment.SLOTS.BODY) == null
                || Equipment.getItem(Equipment.SLOTS.HELMET) == null
                || Equipment.getItem(Equipment.SLOTS.LEGS) == null
                || Equipment.getItem(Equipment.SLOTS.SHIELD) == null) {
            throw new YZScriptException("Wearing armor set is strongly recommended. Exiting...");
        }
    }

    private void performAntiBan() {
        switch (random.nextInt(20)) {
            case 1:
                abcUtil.moveMouse();
                break;
            case 2:
                abcUtil.checkTabs();
                sleep(2000);
                break;
            case 3:
                abcUtil.checkXP();
                sleep(2000);
                break;
            case 4:
                abcUtil.pickupMouse();
                break;
            case 5:
                abcUtil.rightClick();
                break;
            default:
                break;
        }
    }

    private void selectSpell() {
        int magicLevel = Skills.getActualLevel(Skills.SKILLS.MAGIC);

        if (magicLevel >= CURSE_LEVEL) {
            castCurse();
        } else if (magicLevel >= FIRE_STRIKE_LEVEL) {
            caseFireStrike();
        } else if (magicLevel >= WIND_STRIKE_LEVEL) {
            caseWindStrike();
        } else {
            throw new YZScriptException("Internal Error: Magic Level is " + magicLevel);
        }
    }

    private void castCurse() {
        verifyCurseRunesInInventory();
        GameTab.open(GameTab.TABS.MAGIC);
        if (!Magic.selectSpell("Curse")) {
            System.out.println("Curse spell cast Unsuccessful");
        } else{
            sleep(random.nextInt(500) + 1000);
            monkOfZamorak.click();
        }
    }

    private void verifyCurseRunesInInventory() {
        int bodyRuneCount = Inventory.getCount(BODY_RUNE);
        int waterRuneCount = Inventory.getCount(WATER_RUNE);
        int earthRuneCount = Inventory.getCount(EARTH_RUNE);

        boolean staffEquipped = false;

        if (bodyRuneCount < 1) {
            throw new YZScriptException("Body Rune count " + bodyRuneCount + " for Curse");
        }

        if (waterRuneCount < 2 && !Equipment.isEquipped(STAFF_OF_WATER)) {
            if (!(staffEquipped = equipItem(STAFF_OF_WATER))) {
                throw new YZScriptException("Water Rune count " + waterRuneCount + " for Curse");
            }
        }

        if (earthRuneCount < 3 && !Equipment.isEquipped(STAFF_OF_EARTH)) {
            if (staffEquipped || !equipItem(STAFF_OF_EARTH)) {
                throw new YZScriptException("Earth Rune count " + earthRuneCount + " for Curse");
            }
        }
    }

    private void caseFireStrike() {
        verifyFireStrikeRunesInInventory();
        GameTab.open(GameTab.TABS.MAGIC);
        if (!Magic.selectSpell("Fire Strike")) {
            System.out.println("Fire Strike spell cast Unsuccessful");
        } else {
            sleep(random.nextInt(500) + 1000);
            monkOfZamorak.click();
        }
    }

    private void verifyFireStrikeRunesInInventory() {
        int mindRuneCount = Inventory.getCount(MIND_RUNE);
        int airRuneCount = Inventory.getCount(AIR_RUNE);
        int fireRuneCount = Inventory.getCount(FIRE_RUNE);

        boolean staffEquipped = false;

        if (mindRuneCount < 1) {
            throw new YZScriptException("Mind Rune count " + mindRuneCount + " for Fire Strike");
        }

        if (airRuneCount < 2 && !Equipment.isEquipped(STAFF_OF_AIR)) {
            if (!(staffEquipped = equipItem(STAFF_OF_AIR))) {
                throw new YZScriptException("Air Rune count " + airRuneCount + " for Fire Strike");
            }
        }

        if (fireRuneCount < 3 && !Equipment.isEquipped(STAFF_OF_FIRE)) {
            if (staffEquipped || !equipItem(STAFF_OF_FIRE)) {
                throw new YZScriptException("Fire Rune count " + fireRuneCount + " for Fire Strike");
            }
        }
    }

    private void caseWindStrike() {
        verifyWindStrikeRunesInInventory();
        GameTab.open(GameTab.TABS.MAGIC);
        if (!Magic.selectSpell("Wind Strike")) {
            System.out.println("Wind Strike spell cast Unsuccessful");
        } else {
            sleep(random.nextInt(500) + 1000);
            monkOfZamorak.click();
        }
    }

    private void verifyWindStrikeRunesInInventory() {
        int mindRuneCount = Inventory.getCount(MIND_RUNE);
        int airRuneCount = Inventory.getCount(AIR_RUNE);

        if (mindRuneCount < 1) {
            throw new YZScriptException("Mind Rune count " + mindRuneCount + " for Wind Strike");
        }

        if (airRuneCount < 1 && !Equipment.isEquipped(STAFF_OF_AIR)) {
            if (!equipItem(STAFF_OF_AIR)) {
                throw new YZScriptException("Air Rune count " + airRuneCount + " for Wind Strike");
            }
        }
    }

    private boolean equipItem(int id) {
        RSItem[] items = Inventory.find(id);
        if (items.length <= 0) {
            return false;
        }
        GameTab.open(GameTab.TABS.INVENTORY);
        return items[0].click();
    }
}


