package com.github.braisdom.funcsql.apt;

final class Utils {

    private static String A2Z = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static String a2z = "abcdefghijklmnopqrstuvwxyz";

    private static boolean isInA2Z(char c) {
        return (A2Z.indexOf(c) != -1)?true:false;
    }

    private static boolean isIna2z(char c) {
        return (a2z.indexOf(c) != -1)?true:false;
    }


    public static String underscore(String phase) {
        if (phase == null || "".equals(phase)) return phase;

        phase = phase.replace('-', '_');
        StringBuilder sb = new StringBuilder();
        int total = phase.length();
        for (int i = 0; i < total; i++)	{
            char c = phase.charAt(i);
            if (i == 0) {
                if (isInA2Z(c)) {
                    sb.append(("" + c).toLowerCase());
                }
                else {
                    sb.append(c);
                }
            }
            else {
                if (isInA2Z(c)) {
                    if (isIna2z(phase.charAt(i-1))) {
                        sb.append(("_" + c).toLowerCase());
                    }
                    else {
                        sb.append(("" + c).toLowerCase());
                    }
                }
                else {
                    sb.append(c);
                }
            }
        }
        return sb.toString();
    }
}
