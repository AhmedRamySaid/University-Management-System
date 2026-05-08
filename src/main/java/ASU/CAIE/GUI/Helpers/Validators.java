package ASU.CAIE.GUI.Helpers;

import java.util.regex.Pattern;

public class Validators {

    public static boolean isValidEmail(String v) {
        return Pattern.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$", v);
    }

    public static int pwScore(String pw) {
        int s = 0;
        if (pw.length() >= 8)               s++;
        if (pw.length() >= 12)              s++;
        if (pw.matches(".*[A-Z].*"))        s++;
        if (pw.matches(".*[0-9].*"))        s++;
        if (pw.matches(".*[^A-Za-z0-9].*")) s++;
        return s;
    }

    public static String esc(String s) {
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}