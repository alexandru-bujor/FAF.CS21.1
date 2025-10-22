package task1;
import java.util.Scanner;

public class task1 {
    public static void main() {
        Scanner sc = new Scanner(System.in);

        System.out.print("Introdu cheia pentru Cryptare/Decryptare: ");
        while (!sc.hasNextInt()) {
            System.out.println("Cheia trebuie să fie un număr!");
            sc.next();
        }
        int key = sc.nextInt();
        sc.nextLine();

        System.out.println("\nAlgoritmul Cesar. Selectează una dintre opțiuni:");
        System.out.println("1. Criptare");
        System.out.println("2. Decriptare");

        while (!sc.hasNextInt()) {
            System.out.println("Introdu o opțiune validă (1 sau 2):");
            sc.next();
        }
        int option = sc.nextInt();
        sc.nextLine();

        switch (option) {
            case 1 -> {
                System.out.print("Introdu textul care trebuie criptat: ");
                String ch = sc.nextLine();
                Encrypt.encrypt(ch, key, alphabet.ABC);
            }
            case 2 -> {
                System.out.print("Introdu textul care trebuie decriptat: ");
                String ch = sc.nextLine();
                Decrypt.decrypt(ch, key, alphabet.ABC);
            }
            default -> {
                System.out.println("Ați introdus o opțiune greșită!");
                System.exit(404);
            }
        }

        sc.close();
    }
}
