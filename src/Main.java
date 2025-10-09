import task1.*;
import task2.*;
import java.util.*;

void main() {

    Scanner sc = new Scanner(System.in);




    System.out.println("Selecteaza unul dintre urmatoarele taskuri:\n");
    System.out.println("1. Sarcina 1.1");
    System.out.println("2. Sarcina 1.2");
    int option = sc.nextInt();

    switch (option) {
        case 1 -> {
            task1.main();
        }
        case 2 -> {
            task2.main();
        }
        default -> {
            System.out.println("Ați introdus o opțiune greșită!");
            System.exit(404);
        }
    }
}
