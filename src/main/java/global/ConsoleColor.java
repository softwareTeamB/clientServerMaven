package global;

/**
 * Console color klasse
 *
 * @author michel
 */
public class ConsoleColor {

    // Reset
    private static final String RESET = "\033[0m";  // Text Reset

    // Regular Colors
    private static final String BLACK = "\033[0;30m";   // BLACK
    private static final String RED = "\033[0;31m";     // RED
    private static final String GREEN = "\033[0;32m";   // GREEN
    private static final String YELLOW = "\033[0;33m";  // YELLOW
    private static final String BLUE = "\033[0;34m";    // BLUE
    private static final String PURPLE = "\033[0;35m";  // PURPLE
    private static final String CYAN = "\033[0;36m";    // CYAN
    private static final String WHITE = "\033[0;37m";   // WHITE

    // Bold
    private static final String BLACK_BOLD = "\033[1;30m";  // BLACK
    private static final String RED_BOLD = "\033[1;31m";    // RED
    private static final String GREEN_BOLD = "\033[1;32m";  // GREEN
    private static final String YELLOW_BOLD = "\033[1;33m"; // YELLOW
    private static final String BLUE_BOLD = "\033[1;34m";   // BLUE
    private static final String PURPLE_BOLD = "\033[1;35m"; // PURPLE
    private static final String CYAN_BOLD = "\033[1;36m";   // CYAN
    private static final String WHITE_BOLD = "\033[1;37m";  // WHITE

    // Underline
    private static final String BLACK_UNDERLINED = "\033[4;30m";  // BLACK
    private static final String RED_UNDERLINED = "\033[4;31m";    // RED
    private static final String GREEN_UNDERLINED = "\033[4;32m";  // GREEN
    private static final String YELLOW_UNDERLINED = "\033[4;33m"; // YELLOW
    private static final String BLUE_UNDERLINED = "\033[4;34m";   // BLUE
    private static final String PURPLE_UNDERLINED = "\033[4;35m"; // PURPLE
    private static final String CYAN_UNDERLINED = "\033[4;36m";   // CYAN
    private static final String WHITE_UNDERLINED = "\033[4;37m";  // WHITE

    // Background
    private static final String BLACK_BACKGROUND = "\033[40m";  // BLACK
    private static final String RED_BACKGROUND = "\033[41m";    // RED
    private static final String GREEN_BACKGROUND = "\033[42m";  // GREEN
    private static final String YELLOW_BACKGROUND = "\033[43m"; // YELLOW
    private static final String BLUE_BACKGROUND = "\033[44m";   // BLUE
    private static final String PURPLE_BACKGROUND = "\033[45m"; // PURPLE
    private static final String CYAN_BACKGROUND = "\033[46m";   // CYAN
    private static final String WHITE_BACKGROUND = "\033[47m";  // WHITE

    // High Intensity
    public static final String BLACK_BRIGHT = "\033[0;90m";  // BLACK
    public static final String RED_BRIGHT = "\033[0;91m";    // RED
    public static final String GREEN_BRIGHT = "\033[0;92m";  // GREEN
    public static final String YELLOW_BRIGHT = "\033[0;93m"; // YELLOW
    public static final String BLUE_BRIGHT = "\033[0;94m";   // BLUE
    public static final String PURPLE_BRIGHT = "\033[0;95m"; // PURPLE
    public static final String CYAN_BRIGHT = "\033[0;96m";   // CYAN
    public static final String WHITE_BRIGHT = "\033[0;97m";  // WHITE

    // Bold High Intensity
    private static final String BLACK_BOLD_BRIGHT = "\033[1;90m"; // BLACK
    private static final String RED_BOLD_BRIGHT = "\033[1;91m";   // RED
    private static final String GREEN_BOLD_BRIGHT = "\033[1;92m"; // GREEN
    private static final String YELLOW_BOLD_BRIGHT = "\033[1;93m";// YELLOW
    private static final String BLUE_BOLD_BRIGHT = "\033[1;94m";  // BLUE
    private static final String PURPLE_BOLD_BRIGHT = "\033[1;95m";// PURPLE
    private static final String CYAN_BOLD_BRIGHT = "\033[1;96m";  // CYAN
    private static final String WHITE_BOLD_BRIGHT = "\033[1;97m"; // WHITE

    // High Intensity backgrounds
    private static final String BLACK_BACKGROUND_BRIGHT = "\033[0;100m";// BLACK
    private static final String RED_BACKGROUND_BRIGHT = "\033[0;101m";// RED
    private static final String GREEN_BACKGROUND_BRIGHT = "\033[0;102m";// GREEN
    private static final String YELLOW_BACKGROUND_BRIGHT = "\033[0;103m";// YELLOW
    private static final String BLUE_BACKGROUND_BRIGHT = "\033[0;104m";// BLUE
    private static final String PURPLE_BACKGROUND_BRIGHT = "\033[0;105m"; // PURPLE
    private static final String CYAN_BACKGROUND_BRIGHT = "\033[0;106m";  // CYAN
    private static final String WHITE_BACKGROUND_BRIGHT = "\033[0;107m";   // WHITE

    /**
     * Methoden om out bericht uit te printen
     *
     * @param bericht out bericht
     */
    public static void out(Object bericht) {

        System.out.println(BLUE + "[" + Datum.datum() + "] [INFO] " + RESET + bericht);
    }
    
    /**
     * Methoden om een tabel uit te printen in 2 tabellen
     * @param format formate 
     * @param bericht bericht
     * @param bericht2 bericht
     */
    public static void outTable(String format, Object bericht, Object bericht2){
        System.out.printf(format, BLUE + "[" + Datum.datum() + "] [INFO] " + 
                RESET + bericht,  bericht2);
    }

    /**
     * Methoden om error bericht uit te printen
     *
     * @param bericht out bericht
     */
    public static void error(Object bericht) {
        System.out.println(RED + "[" + Datum.datum() + "] [ERROR] " + RESET + bericht);
    }

    /**
     * Methoden om error bericht uit te printen
     *
     * @param bericht out bericht
     */
    public static void err(Object bericht) {
        System.out.println(RED + "[" + Datum.datum() + "] [ERROR] " + RESET + bericht);
    }

    /**
     * Methoden om warn bericht uit te printen
     *
     * @param bericht out bericht
     */
    public static void warn(Object bericht) {
        System.out.println(YELLOW + "[" + Datum.datum() + "] [WARN] " + RESET + bericht);
    }
}
