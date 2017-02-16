package scripts;

import org.tribot.api2007.Inventory;
import org.tribot.api2007.Objects;
import org.tribot.api2007.Player;
import org.tribot.api2007.Walking;
import org.tribot.api2007.ext.Filters;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;

@SuppressWarnings("unused")
@ScriptManifest(authors = {"YZ"}, category = "WoodCutting", name = "YZWillowCutterScript")
public class WillowCutterScript extends Script {

    private static final RSTile POSITION = new RSTile(2970, 3192);
    private static final int WILLOW = 1519;
    private static final int WILLOW1 = 1750;
    private static final int WILLOW2 = 1756;

    @Override
    public void run() {
        try {
            while (true) {
                movePlayerIntoPosition();
                checkInventoryAndDrop();
                if (Player.getAnimation() != -1) {
                    continue;
                }
                findWillowAndCut();
                sleep(3000);
            }
        } catch (YZScriptException e) {
            System.out.println(e.getMessage());
        }
    }

    private void findWillowAndCut() {
        RSObject[] willowTrees = Objects.findNearest(1, Filters.Objects.idEquals(WILLOW1, WILLOW2));
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
}
