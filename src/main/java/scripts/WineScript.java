package scripts;

import org.tribot.api.input.Mouse;
import org.tribot.api2007.*;
import org.tribot.api2007.types.RSTile;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;

@ScriptManifest(authors = {"YZ"}, category = "Money making", name = "YZWineScript")
public class WineScript extends Script {

    public static String FALADOR_TELEPORT = "Falador Teleport";
    public static int WATER_RUNE =  555;
    public static int LAW_RUNE = 563;
    public static int STAFF_OF_AIR = 1381;
    public static int MIST_BATTLESTAFF = 20730;

    private RSTile[] bankToChaosTemple = new RSTile[] {
        new RSTile(2946, 3368, 0),
        new RSTile(2946, 3368, 0),
        new RSTile(2956, 3382, 0),
        new RSTile(2966, 3396, 0),
        new RSTile(2960, 3412, 0),
        new RSTile(2950, 3424, 0),
        new RSTile(2948, 3440, 0),
        new RSTile(2944, 3456, 0),
        new RSTile(2944, 3472, 0),
        new RSTile(2943, 3488, 0),
        new RSTile(2941, 3504, 0),
        new RSTile(2934, 3516, 0),
        new RSTile(2931, 3515, 0)
    };

    private RSTile[] chaosTempleToBank = null;
    private RSTile wineStandingTile = new RSTile(2931,3515);
    private RSTile bankStandingTile = new RSTile(2946,3368);

    @Override
    public void run() {
        Mouse.setSpeed(200);
        boolean start = true;
        while (start) {
            if (Player.isMoving()) {
                turnRunOn();
                sleep(100);
            }
            if (Inventory.isFull()) {
                bank();
            }
            if (Player.getPosition().distanceTo(bankStandingTile) < 2) {
                walkToChaosTemple();
            } else if (Player.getPosition().distanceTo(wineStandingTile) < 2) {
                performWineGrab();
            }
        }
    }

    private void performWineGrab() {}


    private void turnRunOn() {
        if (Game.getRunEnergy() > 30 && !Game.isRunOn()) {
            Mouse.click(0,0, 1);
        }
    }

    private void bank() {
        goToFalador();
        restackInventory();
    }

    private void restackInventory() {
        Banking.openBankBanker();
        sleep(100);
        Banking.depositAllExcept(LAW_RUNE, WATER_RUNE);
        int lawRuneCount = Inventory.getCount(LAW_RUNE);
        if (lawRuneCount < 50) {
            boolean ableToWithdraw = Banking.withdraw(100 - lawRuneCount, LAW_RUNE);
        }
        int waterRuneCount = Inventory.getCount(WATER_RUNE);
        if (!Equipment.isEquipped(MIST_BATTLESTAFF) && waterRuneCount < 50) {
            boolean ableToWithDraw = Banking.withdraw(50 - waterRuneCount, WATER_RUNE);
        }
        Banking.close();
    }

    private void goToFalador() {
        if (!teleToFalador()) {
            walkToFalador();
        }
    }

    private void walkToChaosTemple() {
        Walking.walkPath(bankToChaosTemple);
    }

    private boolean teleToFalador() {
        GameTab.open(GameTab.TABS.MAGIC);
        return Magic.selectSpell(FALADOR_TELEPORT);
    }

    private void walkToFalador() {
        Walking.walkPath(getChaosTempleToBankPath());
    }

    private RSTile[] getChaosTempleToBankPath() {
        if (chaosTempleToBank != null) {
            return chaosTempleToBank;
        }
        chaosTempleToBank = new RSTile[bankToChaosTemple.length];
        System.arraycopy(bankToChaosTemple, 0, chaosTempleToBank, 0, bankToChaosTemple.length);

        return chaosTempleToBank;
    }
}
