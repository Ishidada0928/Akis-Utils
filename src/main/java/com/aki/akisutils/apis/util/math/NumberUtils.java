package com.aki.akisutils.apis.util.math;

public class NumberUtils {
    public static double literalCalculateD(double I1, int E) {
        return I1 * (Math.pow(10, E));
    }

    public static double getStringLiteralToDouble(String s) {
        String[] s1 = s.replace("E", "e").split("e");
        return literalCalculateD(Double.parseDouble(s1[0]), Integer.parseInt(s1[1].replace(".000000", "")));
    }

    //桁数取得
    /**
     * 12afafasg4f15.00000245
     * のような物で、Startに入力した場所から桁を数えます。
     * Start and End == "" 空白の場合は、全桁を数えます。
     * Start == "" Endに入力された文字のある桁まで数えます。
     * End == "" Startに入力された文字のある桁まで数えます。
     *
     * */
    public static int GetStringOfDigits(String str, String Start, String End) {
        if(Start.equals("")) {
            if(End.equals("")) {
                return str.length();
            } else {
                int EndIndex = str.indexOf(End);
                return EndIndex > 0 ? EndIndex : str.length();
            }
        } else {
            int startIndex = str.indexOf(Start);
            if(End.equals("")) {
                return str.length() - (startIndex + 1);
            } else {
                int EndIndex = str.indexOf(End, startIndex);
                return EndIndex > 0 ? EndIndex + 1 : str.length();
            }
        }
    }
}
