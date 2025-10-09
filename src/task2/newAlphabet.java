package task2;

import task1.*;

import java.util.Arrays;

public class newAlphabet {
    static char[] ABC2;            // new alphabet
    static char[] ABC = alphabet.ABC;
    static String k2 = "";

    public static char[] newABC(String k) {
        StringBuilder sb = new StringBuilder();
        k = k.toUpperCase();
        for (int i = 0; i < k.length(); i++) {
            char ch = k.charAt(i);
            if (sb.indexOf(String.valueOf(ch)) == -1) {
                sb.append(ch);
            }
        }

        for (int j = 0; j < ABC.length; j++) {
            char baseChar = ABC[j];
            if (sb.indexOf(String.valueOf(baseChar)) == -1) {
                sb.append(baseChar);
            }
        }

        ABC2 = sb.toString().toCharArray();

        return ABC2;
    }

}
