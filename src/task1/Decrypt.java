package task1;

public class Decrypt {

    public static void decrypt(String ch, int k, char[] ABC){
        String a = "";
        ch = ch.toUpperCase();
        for (int j = 0; j < ch.length(); j++) {
            char chNow = ch.charAt(j);
            for (int i = 0; i < ABC.length; i++) {
                if (chNow == ABC[i]) {
                    if (i-k<0) {
                        a=a+ABC[0+(ABC.length+i-k)];
                    } else
                    {
                        a = a + ABC[i - k];
                    }
                }
            }
        }
        System.out.print(a);
    }

}
