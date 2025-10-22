# Lucrare de laborator 1 — Cifrul lui Cezar (Java)

> Proiect Java pentru implementarea **Cezar** (Task 1) și **Cezar + permutare** (Task 2), fără a expune soluția în README. Acest fișier descrie arhitectura, regulile de intrare/ieșire, modul de rulare și testare.

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
> Notă: denumirile exacte ale fișierelor pot diferi; păstrați pachetele `task1` și `task2`.

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
> Dacă clasele au pachete (`package task1;` etc.), asigurați-vă că folosiți calea și numele complet al clasei la rulare (ex: `java -cp out task1.task1`).

---

## Reguli de intrare & validare
- **Cheia numerică `k`**: întreg în **[1..25]**. Alte valori sunt respinse cu mesaj clar.
- **Cuvânt-cheie `k2` (Task 2)**: **doar litere A–Z/a–z**, **fără spații**, **lungime ≥ 7**. Se elimină duplicatele în ordinea apariției.
- **Mesaj/Criptogramă**: doar litere A–Z/a–z. Se va transforma la **MAJUSCULE** și se vor **elimina spațiile** înainte de procesare.
- **Mapare alfabet**: se utilizează **tabloul propriu** de litere (A..Z) pentru indexare 0..25. **NU** se folosesc codificările ASCII/Unicode pentru calcule.

---

## Descriere la nivel înalt (fără cod)
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

## Testare recomandată
- **Valori limită pentru `k`**: 1 și 25; cazuri invalide: 0, 26, negative, non-numeric.
- **Validare `k2`**: prea scurt, conține non-litere, conține duplicate → alfabetul final nu are duplicate și are 26 litere.
- **Round-trip**:
    - `Decrypt(Encrypt(M, k), k) == Normalize(M)`
    - `Decrypt(Encrypt(M, k, k2), k, k2) == Normalize(M)`
- **Normalizare**: texte cu litere mici, spații, newline.
- **Robustețe**: mesaje cu litere la capete ale alfabetului (X, Y, Z) pentru a verifica „wrap-around”.

Puteți include fișiere în `tests/` (ex: `ex1.txt`, `ex2.txt`) și un script simplu de rulare.

---

## Troubleshooting
- **Nu pornește programul din `task1.task1`**: metoda `main` trebuie să fie `public static void main(String[] args)`. Dacă folosiți un „launcher” în `Main.java`, acesta trebuie să apeleze metodele potrivite.
- **Clase duplicate**: evitați duplicarea fișierelor (ex: `Decrypt.java` de două ori). Mențineți câte **o** clasă per responsabilitate.
- **Chei în afara domeniului**: validați `k` înainte de a continua; afișați mesaj și reporniți promptul.
- **Caractere ignorate**: dacă apar caractere non-alfabetice, semnalați și cereți input valid.
- **Wrap-around**: verificați calculele pentru cazurile `i + k >= 26` (encrypt) și `i - k < 0` (decrypt).

---

## Interacțiune CLI (exemplu fără rezultat)
```
Alege modul:
1) Task 1 — Cezar
2) Task 2 — Cezar + permutare
> 2

Cheie numerică (1–25): 3
Cuvânt-cheie (doar litere, ≥ 7): CRYPTOGRAPHY

Operație:
1) Criptare
2) Decriptare
> 1

Mesaj: BRUTE FORCE ATTACK
Rezultat: [generat de program]
```

---

## Checklist înainte de predare
- [ ] Validare completă pentru `k` și `k2` cu mesaje clare.
- [ ] Normalizare text (UPPERCASE + fără spații) înainte de procesare.
- [ ] Generarea alfabetului permutat **fără duplicate** (26 litere).
- [ ] Round-trip tests: encrypt → decrypt revine la mesajul normalizat.
- [ ] `README.md` explică **cum se rulează**, **ce fișiere există** și **ce teste s-au făcut**.
- [ ] Nicio scurgere a cheilor în loguri/print-uri inutile.

---

## Integritate academică
Acest proiect este destinat uzului educațional. Implementarea trebuie să fie a autorului. Evitați copierea necreditată și documentați deciziile tehnice în `docs/`.

---

## Autor
**Student:** Bujor Alexandru  
**Grupa:** FAF-231
**Disciplina:** Criptografie și Securitate Informațională  
**Lucrarea:** Nr. 1 — Cifrul lui Cezar
