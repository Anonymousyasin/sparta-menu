package com.android.support.components;

import android.graphics.Color;

import org.lsposed.lsparanoid.Obfuscate;

/**
 * Sparta Menu glassmorphism palette.
 * All UI colors flow through here — change presets to retheme instantly.
 */
@Obfuscate
public class Colors {

    // ── Accent presets (index into ACCENTS) ──
    public static final String[][] ACCENTS = {
        {"Sparta Purple", "#8B6CFF", "#4F9DFF"},
        {"Cyber Blue",    "#4F9DFF", "#38D2FF"},
        {"Toxic Green",   "#2DD48F", "#A3E635"},
        {"Blood Red",     "#FB4A5F", "#FF8A5C"},
        {"Gold",          "#FFB627", "#FFE156"},
        {"Mono",          "#B0BEC5", "#78909C"},
    };
    public static int accentIndex = 0;

    private static int c1 = Color.parseColor(ACCENTS[0][1]);
    private static int c2 = Color.parseColor(ACCENTS[0][2]);

    /** Apply an accent preset by index (clamped). */
    public static void setAccent(int idx) {
        accentIndex = Math.max(0, Math.min(idx, ACCENTS.length - 1));
        c1 = Color.parseColor(ACCENTS[accentIndex][1]);
        c2 = Color.parseColor(ACCENTS[accentIndex][2]);
        refresh();
    }

    public static int accentStart() { return c1; }
    public static int accentEnd()   { return c2; }

    /** Blend of the two accent colors (0.0–1.0). */
    public static int accentBlend(float t) {
        t = Math.max(0f, Math.min(1f, t));
        return Color.rgb(
            (int)(Color.red(c1)   * (1 - t) + Color.red(c2)   * t),
            (int)(Color.green(c1) * (1 - t) + Color.green(c2) * t),
            (int)(Color.blue(c1)  * (1 - t) + Color.blue(c2)  * t));
    }

    // ── Glass surfaces ──
    public static int MENU_BG_COLOR          = Color.parseColor("#E60D1220");
    public static int MENU_FEATURE_BG_COLOR  = Color.parseColor("#99151C30");
    public static int OtherBG                = Color.parseColor("#B3151C30");
    public static int CARD_STROKE            = Color.parseColor("#33FFFFFF");

    // ── Text ──
    public static int TEXT_COLOR   = Color.parseColor("#EEF2FF");
    public static int TEXT_COLOR_2 = Color.parseColor("#B8C1DE");
    public static String NumberTxtColor = "#B8C1DE";

    // ── Widgets (set in refresh()) ──
    public static int ToggleON;
    public static int ToggleOFF;
    public static int BtnON;
    public static int BtnOFF;
    public static int CategoryBG;
    public static int SliderColor;
    public static int SliderProgressColor;
    public static int CheckBoxColor;
    public static int RadioColor;
    public static int SpinnerColor;
    public static int BTN_COLOR;

    // ── Geometry ──
    public static float RADIUS_MENU    = 22f;  // dp-ish px applied by Menu
    public static float RADIUS_WIDGET  = 14f;

    /** Recompute widget colors from the active accent. */
    public static void refresh() {
        int a = accentStart();
        ToggleON             = a;
        BtnON                = a;
        CategoryBG           = a;
        SliderProgressColor  = a;
        CheckBoxColor        = a;
        RadioColor           = a;
        SpinnerColor         = a;
        BTN_COLOR            = a;
        SliderColor          = Color.parseColor("#26FFFFFF");      // track: translucent white
        ToggleOFF            = Color.parseColor("#33FFFFFF");      // off state: dim glass
        BtnOFF               = Color.parseColor("#33151C30");
    }
}
