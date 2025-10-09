package task2;
import task1.*;
import java.util.*;

public class encrypt2 {
    static char[] ABC2;            // new alphabet
    static Encrypt enc = new Encrypt();
    static String k2 = "";
    static String ch;
    static int k;


    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Give me the first key: ");
        k = sc.nextInt(); sc.nextLine();
        System.out.print("Give me the second key: ");
        k2 = sc.nextLine();
        System.out.print("Give me the text to encrypt: ");
        ch = sc.nextLine();
        Encrypt.encrypt(ch, k, newAlphabet.newABC(k2));

    }
}
