import java.util.Scanner;

public class Main {

    // Tabelul de permutare initiala (IP)
    private static final int[] IP = {
            58, 50, 42, 34, 26, 18, 10, 2,
            60, 52, 44, 36, 28, 20, 12, 4,
            62, 54, 46, 38, 30, 22, 14, 6,
            64, 56, 48, 40, 32, 24, 16, 8,
            57, 49, 41, 33, 25, 17,  9, 1,
            59, 51, 43, 35, 27, 19, 11, 3,
            61, 53, 45, 37, 29, 21, 13, 5,
            63, 55, 47, 39, 31, 23, 15, 7
    };

    // Transformă un caracter în binar pe 8 biți
    private static String toBinary8(char c) {
        return String.format("%8s", Integer.toBinaryString(c)).replace(' ', '0');
    }

    // Aplică permutarea IP șirului de 64 biți
    private static String applyIP(String bits64) {
        StringBuilder out = new StringBuilder();
        for (int index : IP) {
            out.append(bits64.charAt(index - 1));
        }
        return out.toString();
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.println("=== DES – Calcularea lui L1 ===");
        System.out.println("Introdu un mesaj de EXACT 8 caractere:");

        String msg = sc.nextLine();
        if (msg.length() != 8) {
            System.out.println("Eroare: mesajul trebuie sa contina exact 8 caractere.");
            return;
        }

        // 1. Convertirea mesajului în 64 biți
        StringBuilder bits64 = new StringBuilder();
        for (char c : msg.toCharArray()) {
            bits64.append(toBinary8(c));
        }

        System.out.println("\nMesajul in binar (64 biti):");
        System.out.println(bits64);

        // 2. Afișăm tabelul IP
        System.out.println("\nTabelul IP (Initial Permutation):");
        for (int i = 0; i < IP.length; i++) {
            System.out.printf("%2d ", IP[i]);
            if ((i + 1) % 8 == 0) System.out.println();
        }

        // 3. Aplic IP
        String afterIP = applyIP(bits64.toString());

        System.out.println("\nDupa aplicarea IP (64 biti):");
        System.out.println(afterIP);

        // 4. L0 si R0
        String L0 = afterIP.substring(0, 32);
        String R0 = afterIP.substring(32);

        System.out.println("\nL0 (primii 32 biti):");
        System.out.println(L0);

        System.out.println("\nR0 (ultimii 32 biti):");
        System.out.println(R0);

        // 5. L1 = R0
        System.out.println("\n=== REZULTAT ===");
        System.out.println("L1 = R0 = ");
        System.out.println(R0);
    }
}
