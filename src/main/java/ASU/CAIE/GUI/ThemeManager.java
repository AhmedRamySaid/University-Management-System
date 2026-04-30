package ASU.CAIE.GUI;

public class ThemeManager {

    // ── State ───────────────────────────────────────────────────────────────
    private static boolean dark = false;

    // ── Light palette ────────────────────────────────────────────────────────
    private static final String L_BG      = "#ffffff";
    private static final String L_BG2     = "#f5f5f5";
    private static final String L_BORDER2 = "#b0b0b0";
    private static final String L_TEXT    = "#111111";
    private static final String L_TEXT2   = "#555555";
    private static final String L_TEXT3   = "#888888";

    // ── Dark palette ─────────────────────────────────────────────────────────
    private static final String D_BG      = "#1a1a1a";
    private static final String D_BG2     = "#252525";
    private static final String D_BORDER2 = "#4a4a4a";
    private static final String D_TEXT    = "#f0f0f0";
    private static final String D_TEXT2   = "#aaaaaa";
    private static final String D_TEXT3   = "#666666";

    public static boolean isDark()      { return dark; }
    public static void    toggle()      { dark = !dark; }

    public static String bg()      { return dark ? D_BG      : L_BG; }
    public static String bg2()     { return dark ? D_BG2     : L_BG2; }
    public static String border2() { return dark ? D_BORDER2 : L_BORDER2; }
    public static String text()    { return dark ? D_TEXT    : L_TEXT; }
    public static String text2()   { return dark ? D_TEXT2   : L_TEXT2; }
    public static String text3()   { return dark ? D_TEXT3   : L_TEXT3; }
}