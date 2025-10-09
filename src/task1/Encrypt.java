package task1;

public class Encrypt {


    public static void encrypt(String ch, int k, char[] ABC){
        String a = "";
        ch = ch.toUpperCase();
        for (int j = 0; j < ch.length(); j++) {
            char chNow = ch.charAt(j);
            for (int i = 0; i < ABC.length; i++) {
                if (chNow == ABC[i]) {
                    if (i+k>=ABC.length) {
                        a=a+ABC[0+(i+k - ABC.length)];
                    } else
                    {
                        a = a + ABC[i + k];
                    }
                }
            }
        }
        System.out.print(a);
    }

}
