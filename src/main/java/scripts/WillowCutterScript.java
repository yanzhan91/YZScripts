package scripts;

import org.tribot.api.util.abc.ABCUtil;
import org.tribot.api2007.*;
import org.tribot.api2007.Objects;
import org.tribot.api2007.ext.Filters;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;

import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
@ScriptManifest(authors = {"YZ"}, category = "WoodCutting", name = "YZWillowCutterScript")
public class WillowCutterScript extends Script {

    private static final RSTile POSITION = new RSTile(2970, 3192);
    private static final int WILLOW = 1519;
    private static final int WILLOW1 = 1750;
    private static final int WILLOW2 = 1756;

    private Random random = new Random((new Date()).getTime());
    private ABCUtil abcUtil = new ABCUtil();
    private HashSet<Integer> hatchets = Arrays.stream(Constants.IDs.Items.hatchets).boxed().collect(Collectors.toCollection(HashSet::new));

    private int hour = 60 * 60 * 1000;

    @Override
    public void run() {

        long start = System.currentTimeMillis();
        long end = start + 10 * hour;

        try {
            while (System.currentTimeMillis() < end) {
                sleep(3000);
                checkForAxe();
                movePlayerIntoPosition();
                checkInventoryAndDrop();
                if (Player.getAnimation() != -1) {
                    performAntiBan();
                    continue;
                }
                findWillowAndCut();
            }
        } catch (YZScriptException e) {
            System.out.println(e.getMessage());
        }

        Login.logout();
    }

    private void checkForAxe() {
        boolean axeEquipped = Equipment.getItem(Equipment.SLOTS.WEAPON).name != null && Equipment.getItem(Equipment.SLOTS.WEAPON).name.contains("axe");
        boolean axeInInventory = Arrays.stream(Inventory.getAll()).anyMatch(item -> hatchets.contains(item.getID()));
        if (!(axeEquipped || axeInInventory)) {
            throw new YZScriptException("No axe found");
        }
    }

    private void findWillowAndCut() {
        RSObject[] willowTrees = Objects.findNearest(2, Filters.Objects.idEquals(WILLOW1, WILLOW2));
        System.out.println("Number of trees " + willowTrees.length);
        if (willowTrees.length > 0) {
            willowTrees[0].click();
        } else {
            System.out.println("No willow tree available");
        }
    }

    private void checkInventoryAndDrop() {
        if (Inventory.isFull()) {
            if (Inventory.getCount(WILLOW) <= 0) {
                throw new YZScriptException("Inventory full without willows");
            } else {
                Inventory.setDroppingPattern(Inventory.DROPPING_PATTERN.LEFT_TO_RIGHT);
                Inventory.drop(WILLOW);
            }
        }
    }

    private void movePlayerIntoPosition() {
        while (Player.getPosition().distanceTo(POSITION) > 1) {
            Walking.walkTo(POSITION);
            sleep(1000);
        }
    }

    private void performAntiBan() {
        switch(this.random.nextInt(30)) {
            case 1:
                this.abcUtil.moveMouse();
                break;
            case 2:
                this.abcUtil.checkTabs();
                this.sleep(2000L);
                break;
            case 3:
                this.abcUtil.checkXP();
                this.sleep(2000L);
                break;
            case 4:
                this.abcUtil.pickupMouse();
                break;
            case 5:
                this.abcUtil.rightClick();
        }
    }
}
