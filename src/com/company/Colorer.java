package com.company;

/**
 * Created by Badtoasters on 5/12/2017.
 */

public class Colorer {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREY = "\u001B[37m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_LIGHT_BLUE = "\u001B[94m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public static final String ANSI_RED_BACKGROUND = "\u001B[41m";

    /**
     *
     * @param number number to be colored
     * @return the colored string of the integer
     */
    public static String color(int number) {
        switch (number) {
            case 1:
                return ANSI_BLUE + number + ANSI_RESET;
            case 2:
                return ANSI_GREEN + number + ANSI_RESET;
            case 3:
                return ANSI_RED + number + ANSI_RESET;
            case 4:
                return ANSI_PURPLE + number + ANSI_RESET;
            case 5:
                return ANSI_YELLOW + number + ANSI_RESET;
            case 6:
                return ANSI_CYAN + number + ANSI_RESET;
            case 7:
                return ANSI_LIGHT_BLUE + number + ANSI_RESET;
            case 8:
                return ANSI_GREY + number + ANSI_RESET;
            default:
                return ""+number;
        }
    }

    /**
     *
     * @param item number to be highlighted
     * @return the highlighted string representation of the string
     */
    public static String highlight(String item) {
        return ANSI_RED_BACKGROUND + ANSI_WHITE + item + ANSI_RESET;
    }
}
