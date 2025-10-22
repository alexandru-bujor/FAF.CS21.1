# Lucrare de laborator 1 — Cifrul lui Cezar (Java)

---

## Descriere detaliată a soluției

### 1. Arhitectura generală

Soluția este organizată modular, pe două pachete:
- `task1` — implementează **cifrul Cezar clasic**, bazat pe deplasarea alfabetului.
- `task2` — implementează **cifrul Cezar cu permutare**, adăugând o cheie secundară ce modifică ordinea alfabetului.

Fiecare pachet conține clase specializate:
- `alphabet` — definește alfabetul de bază `A–Z` ca un tablou `char[]`.
- `Encrypt` și `Decrypt` — conțin logica efectivă pentru criptare/decriptare.
- `task1` și `task2` — coordonează interacțiunea cu utilizatorul (citire chei, opțiune, text).
- `newAlphabet` — generează alfabetul permutat din cuvântul-cheie introdus.

---

### 2. Logica algoritmului Cezar (Task 1)

Algoritmul pornește de la alfabetul standard:
```
A B C D E F G H I J K L M N O P Q R S T U V W X Y Z
```

#### Criptare:
Pentru fiecare caracter `ch` din textul de intrare:
1. Se transformă în majusculă.
2. Se caută poziția sa `i` în vectorul `ABC`.
3. Se deplasează spre dreapta cu valoarea cheii `k`:
   ```
   noua_poz = (i + k) mod 26
   ```
4. Se extrage litera de la poziția `noua_poz` și se concatenează în rezultatul criptat.

#### Decriptare:
Procedura inversă:
```
noua_poz = (i - k + 26) mod 26
```
Se adaugă 26 pentru a evita indici negativi.

Exemplu:
```
Text: NEW
Cheie: 3
Rezultat criptare: QHZ
Rezultat decriptare: NEW
```

---

### 3. Validare și normalizare

Înainte de orice procesare:
- Textul este convertit în majuscule (`toUpperCase()`).
- Spațiile sunt eliminate.
- Cheia `k` este verificată să fie între `1` și `25`.
- Dacă intrarea nu respectă aceste reguli, programul afișează mesaje de eroare și solicită introducerea unei valori corecte.

Această validare se face în `task1.java` prin intermediul clasei `Scanner`, care verifică tipul de date și repornește promptul în caz de eroare.

---

### 4. Cifrul Cezar cu permutare (Task 2)

Această versiune adaugă o cheie secundară `k2` (un cuvânt-cheie format doar din litere).  
Scopul este să genereze un **alfabet personalizat**, după următorul algoritm:

#### a) Generarea alfabetului nou (`newAlphabet.java`)
1. Se preiau literele din `k2`, se transformă în majuscule.
2. Se elimină duplicatele păstrând ordinea apariției.
3. Se adaugă restul literelor alfabetului `A–Z` care nu apar în `k2`.
4. Rezultatul este un nou vector `ABC2` cu 26 caractere unice.

Exemplu:
```
k2 = TEST
Alfabet nou = T E S A B C D F G H I J K L M N O P Q R U V W X Y Z
```

#### b) Aplicarea cifrului Cezar
După generarea alfabetului permutat, criptarea și decriptarea funcționează identic cu Task 1,  
doar că în locul alfabetului `ABC` se folosește `ABC2`.

Exemplu:
```
Text: NEW
k1 = 3
k2 = TEST
Rezultat criptare: QBZ
Rezultat decriptare: NEW
```

---

### 5. Fluxul principal (`Main.java`)
Clasa `Main` acționează ca un meniu principal:
- Afișează opțiunile disponibile („Sarcina 1.1” și „Sarcina 1.2”).
- Direcționează execuția către metoda `task1.main()` sau `task2.main()`.
- Asigură controlul centralizat al rulării programului.

---

### 6. Exemple de rulare

#### Criptare simplă:
```
Selecteaza unul dintre urmatoarele taskuri:
1. Sarcina 1.1
2. Sarcina 1.2
1
Introdu cheia pentru Cryptare/Decryptare: 3
Algoritmul Cesar. Selectează una dintre opțiuni:
1. Criptare
2. Decriptare
1
Introdu textul care trebuie criptat: NEW
QHZ
```

#### Decriptare:
```
Introdu textul care trebuie decriptat: QHZ
NEW
```

#### Cezar cu permutare:
```
Selecteaza unul dintre urmatoarele taskuri:
1. Sarcina 1.1
2. Sarcina 1.2
2
Criptarea algoritmului cesar utilizand doua chei.
Introdu prima cheie: 3
Introdu a doua cheie: TEST
Textul care trebuie criptat: NEW
QBZ
```

---

### 7. Concluzii

Implementarea demonstrează funcționarea corectă a:
- **cifrului Cezar clasic** și
- **versiunii extinse cu permutare**.

Prin utilizarea propriei mapări alfabetice și a validărilor stricte, programul respectă cerințele teoretice ale laboratorului.  
Extensia cu permutare crește spațiul de chei posibile și reduce vulnerabilitatea la atacul exhaustiv, dar rămâne sensibil la analiza frecvenței.

---

**Student:** Bujor Alexandru  
**Grupa:** FAF-231  
**Disciplina:** Criptografie și Securitate Informațională  
**Lucrarea:** Nr. 1 — Cifrul lui Cezar
