package jwtc.android.chess;

import android.content.SharedPreferences;

public class ColorScheme {
    public static int colorBlack;
    public static int colorWhite;
    public static int colorSelected;
    public static int colorValid;
    public static int colorInvalid;

    public static void init(SharedPreferences prefs) {
        int colorScheme = prefs.getInt("ColorScheme", 0);
        ColorScheme.colorSelected = 0xccf3ed4b;
        ColorScheme.colorInvalid = 0xcccc0000;
        ColorScheme.colorValid = 0xcc00cc00;

        if (colorScheme >= 0 && colorScheme < 6) {
            switch (colorScheme) {
                case 0:
                    // yellow
                    ColorScheme.colorBlack = 0xffdeac5d;
                    ColorScheme.colorWhite = 0xfff9e3c0;
                    ColorScheme.colorSelected = 0xccf3ed4b;
                    break;
                case 1:
                    // blue
                    ColorScheme.colorBlack = 0xff28628b;
                    ColorScheme.colorWhite = 0xff7dbdea;
                    ColorScheme.colorSelected = 0xcc9fdef3;
                    break;
                case 2:
                    // green
                    ColorScheme.colorBlack = 0xff8eb59b;
                    ColorScheme.colorWhite = 0xffcae787;
                    ColorScheme.colorSelected = 0xcc9ff3b4;
                    break;
                case 3:
                    // grey
                    ColorScheme.colorBlack = 0xffc0c0c0;
                    ColorScheme.colorWhite = 0xffffffff;
                    ColorScheme.colorSelected = 0xccf3ed4b;
                    break;
                case 4:
                    // brown
                    ColorScheme.colorBlack = 0xff65390d; //4c2b0a
                    ColorScheme.colorWhite = 0xffb98b4f;
                    ColorScheme.colorSelected = 0xccf3ed4b;
                    break;
                case 5:
                    // red
                    ColorScheme.colorBlack = 0xffff2828;
                    ColorScheme.colorWhite = 0xffffd1d1;
                    ColorScheme.colorSelected = 0xccf3ed4b;
                break;
            }
        } else {
            ColorScheme.colorWhite = prefs.getInt("color2", 0xffdddddd);
            ColorScheme.colorBlack = prefs.getInt("color1", 0xffff0066);
        }
    }
}
