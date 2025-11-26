import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {

    // Alfabetul românesc de 31 litere (toate majuscule)
    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZĂÂÎȘȚ";

    // Mapare literă -> index și index -> literă
    private static final Map<Character, Integer> charToIndex = new HashMap<>();
    private static final Map<Integer, Character> indexToChar = new HashMap<>();

    static {
        for (int i = 0; i < ALPHABET.length(); i++) {
            char c = ALPHABET.charAt(i);
            charToIndex.put(c, i);
            indexToChar.put(i, c);
        }
    }

    // Verifică dacă textul conține doar litere permise
    private static boolean isValid(String text) {
        for (char c : text.toCharArray()) {
            if (!charToIndex.containsKey(c)) {
                return false;
            }
        }
        return true;
    }

    // Scoate spațiile și transformă în majuscule
    private static String preprocess(String text) {
        return text.replaceAll("\\s+", "").toUpperCase();
    }

    // Criptare Vigenere
    private static String encrypt(String message, String key) {
        StringBuilder result = new StringBuilder();
        int mLen = message.length();
        int kLen = key.length();

        for (int i = 0; i < mLen; i++) {
            int mIndex = charToIndex.get(message.charAt(i));
            int kIndex = charToIndex.get(key.charAt(i % kLen));
            int cIndex = (mIndex + kIndex) % 31;
            result.append(indexToChar.get(cIndex));
        }
        return result.toString();
    }

    // Decriptare Vigenere
    private static String decrypt(String cipher, String key) {
        StringBuilder result = new StringBuilder();
        int cLen = cipher.length();
        int kLen = key.length();

        for (int i = 0; i < cLen; i++) {
            int cIndex = charToIndex.get(cipher.charAt(i));
            int kIndex = charToIndex.get(key.charAt(i % kLen));
            int mIndex = (cIndex - kIndex + 31) % 31;
            result.append(indexToChar.get(mIndex));
        }
        return result.toString();
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Cifrul Vigenere (alfabet romanesc, 31 litere)");
        System.out.println("1 - Criptare");
        System.out.println("2 - Decriptare");
        System.out.print("Alege optiunea: ");
        int op = sc.nextInt();
        sc.nextLine(); // consumă \n

        if (op != 1 && op != 2) {
            System.out.println("Eroare: trebuie 1 sau 2.");
            return;
        }

        // Citire cheie
        System.out.print("Introdu cheia (minim 7 caractere): ");
        String key = preprocess(sc.nextLine());

        if (key.length() < 7) {
            System.out.println("Eroare: cheia este prea scurta (minim 7).");
            return;
        }
        if (!isValid(key)) {
            System.out.println("Eroare: cheia poate contine doar litere A–Z, Ă, Â, Î, Ș, Ț.");
            return;
        }

        if (op == 1) {
            // Criptare
            System.out.print("Introdu mesajul: ");
            String msg = preprocess(sc.nextLine());

            if (!isValid(msg)) {
                System.out.println("Eroare: mesajul poate contine doar litere A–Z, Ă, Â, Î, Ș, Ț.");
                return;
            }

            String cipher = encrypt(msg, key);
            System.out.println("Criptograma:");
            System.out.println(cipher);

        } else {
            // Decriptare
            System.out.print("Introdu criptograma: ");
            String cipher = preprocess(sc.nextLine());

            if (!isValid(cipher)) {
                System.out.println("Eroare: criptograma poate contine doar litere A–Z, Ă, Â, Î, Ș, Ț.");
                return;
            }

            String message = decrypt(cipher, key);
            System.out.println("Mesaj decriptat (fara spatii):");
            System.out.println(message);
            System.out.println("Spatiile se adauga manual.");
        }
    }
}
