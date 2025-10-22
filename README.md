# Lucrare de laborator 1 — Cifrul lui Cezar (Java)

---

## Obiective
- **Task 1**: Cifrul Cezar clasic cu cheie numerică `k ∈ [1..25]` pe alfabetul englez A–Z, cu mapare proprie (fără ASCII/Unicode).
- **Task 2**: Cifrul Cezar aplicat peste un **alfabet permutat** generat dintr-un cuvânt-cheie `k2` (numai litere, lungime ≥ 7), urmat de restul literelor din alfabet fără duplicate.
- Operații: **Encrypt** și **Decrypt** pentru ambele task-uri.
- Normalizare intrare: **majuscule + eliminare spații** înainte de procesare.
- Validare strictă: chei & text doar în domeniul permis; mesaje de eroare explicite.

---

## Structura proiectului (actuală)
```
Cryptografie_Securitate/
├─ src/
│  ├─ task1/
│  │  ├─ alphabet.java     # Alfabetul de bază (A..Z) și maparea 0..25
│  │  ├─ Encrypt.java      # Criptare Caesar simplu
│  │  ├─ Decrypt.java      # Decriptare Caesar simplu
│  │  └─ task1.java        # Flux CLI pentru Task 1
│  ├─ task2/
│  │  ├─ newAlphabet.java  # Generarea alfabetului permutat din k2
│  │  ├─ encrypt2.java     # Criptare Caesar peste alfabetul permutat
│  │  └─ task2.java        # Flux CLI pentru Task 2
│  └─ Main.java            # Meniul principal (alegere Task 1 / Task 2)
├─ README.md               # Acest fișier
└─ .gitignore
```

---

## Rulare
### 1) Din IntelliJ IDEA
- Deschide proiectul → click dreapta pe `Main.java` → **Run 'Main'**.
- Alternativ, rulați direct `task1.task1` sau `task2.task2` pentru a intra direct în fluxul fiecărui task.

### 2) Din linie de comandă (JDK 17+)
În directorul proiectului:
```bash
# Compilare în folderul out/
javac -d out -sourcepath src src/Main.java src/task1/*.java src/task2/*.java

# Rulare (folosește numele pachetului dacă există)
java -cp out Main
```

---

## Reguli de intrare & validare
- **Cheia numerică `k`**: întreg în **[1..25]**. Alte valori sunt respinse cu mesaj clar.
- **Cuvânt-cheie `k2` (Task 2)**: **doar litere A–Z/a–z**, **fără spații**, **lungime ≥ 7**. Se elimină duplicatele în ordinea apariției.
- **Mesaj/Criptogramă**: doar litere A–Z/a–z. Se va transforma la **MAJUSCULE** și se vor **elimina spațiile** înainte de procesare.
- **Mapare alfabet**: se utilizează **tabloul propriu** de litere (A..Z) pentru indexare 0..25. **NU** se folosesc codificările ASCII/Unicode pentru calcule.

---

## Descriere 
### Task 1 — Cezar simplu
- Criptare: `c = (x + k) mod 26`
- Decriptare: `m = (y - k + 26) mod 26`
- `x`, `y` sunt indecșii 0..25 obținuți din alfabetul definit de proiect.

### Task 2 — Cezar + permutare
1. **Generează alfabetul permutat** din `k2`:
    - Adaugă literele din `k2` (fără duplicate) → apoi restul literelor A..Z în ordine naturală (fără duplicare).
2. **Aplică** deplasarea Caesar (cu `k`) **peste alfabetul permutat**.
3. Criptare/decriptare funcționează analog Task 1, dar **indexarea** se face în **alfabetul permutat**.

---



## Rezultate
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

```
Selecteaza unul dintre urmatoarele taskuri:

1. Sarcina 1.1
2. Sarcina 1.2
1
Introdu cheia pentru Cryptare/Decryptare: 3

Algoritmul Cesar. Selectează una dintre opțiuni:
1. Criptare
2. Decriptare
2
Introdu textul care trebuie decriptat: QHZ
NEW

```

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


## Autor
**Student:** Bujor Alexandru  
**Grupa:** FAF-231
**Disciplina:** Criptografie și Securitate Informațională  
**Lucrarea:** Nr. 1 — Cifrul lui Cezar
