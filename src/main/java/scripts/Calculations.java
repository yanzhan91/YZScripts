package scripts;

public class Calculations {
    private static final int XP0_13 = 1_833;
    private static final int XP13_19 = 3_973 - XP0_13;
    private static final int XP19_55 = 166_636 - XP13_19;
    private static final int XP55_94 = 7_944_614 - XP19_55;

    private static final int STAFF_OF_AIR = 1700;
    private static final int STAFF_OF_EARTH = 1000;
    private static final int STAFF_OF_FIRE = 940;

    private static final int ELEMENT_RUNE = 5;
    private static final int BODY_RUNE = 6;
    private static final int MIND_RUNE = 4;
    private static final int NATURE_RUNE = 253;
    private static final int LAW_RUNE = 182;

    private static final int WIND_STRIKE_PRICE = MIND_RUNE;
    private static final int FIRE_STRIKE_PRICE = MIND_RUNE + 3 * ELEMENT_RUNE;
    private static final int CURSE_PRICE = BODY_RUNE + 2 * ELEMENT_RUNE;
//    private static final int HIGH_ALCH_PRICE = NATURE_RUNE;
    private static final int HIGH_ALCH_PRICE = BODY_RUNE + 2 * ELEMENT_RUNE;

    private static final int WIND_STRIKE_COUNT = (int) Math.ceil(XP0_13 / 5.5);
    private static final int FIRE_STRIKE_COUNT = (int) Math.ceil(XP13_19 / 11.5);
    private static final int CURSE_COUNT = (int) Math.ceil(XP19_55 / 29);
//    private static final int HIGH_ALCH_COUNT = (int) Math.ceil(XP55_94 / 65);
    private static final int HIGH_ALCH_COUNT = (int) Math.ceil(XP55_94 / 29);

    public static void main(String[] args) {
        int money = WIND_STRIKE_PRICE * WIND_STRIKE_COUNT
                + FIRE_STRIKE_PRICE * FIRE_STRIKE_COUNT
                + CURSE_PRICE * CURSE_COUNT
                + HIGH_ALCH_PRICE * HIGH_ALCH_COUNT;

        int time = (WIND_STRIKE_COUNT + FIRE_STRIKE_COUNT + CURSE_COUNT + HIGH_ALCH_COUNT) * 2;

        System.out.println(money);
        System.out.println(time/3600);
    }
}
