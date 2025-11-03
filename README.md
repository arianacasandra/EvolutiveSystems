# EvolutiveSystems
# Laborator 1 – Sisteme Evolutive: Algoritmi Genetici (JGAP)

## Scop
Familiarizarea cu principiile algoritmilor genetici prin folosirea bibliotecii **JGAP** și implementarea a două probleme de optimizare:
- Problema monedelor (capitolul 5 din suportul de laborator)
- Problema rucsacului (capitolul 6)

---

## Exercițiul 1 – Operatorii genetici și selecția

### Încrucișarea (Crossover)
- Este procesul prin care doi părinți (soluții existente) sunt combinați pentru a genera unul sau mai mulți urmași.  
- Scopul este **combinarea genelor bune** din indivizi diferiți pentru a obține soluții mai performante.  
- Exemple:
  - *One-point crossover*: se alege un punct de tăiere; prima parte vine de la un părinte, a doua de la celălalt.
  - *Two-point crossover*: se aleg două puncte; secțiunea dintre ele se schimbă între părinți.
  - *Uniform crossover*: fiecare genă are o probabilitate de 50% să fie moștenită de la oricare părinte.

### Mutația (Mutation)
- Mutația introduce diversitate aleatorie în populație.
- Constă în **modificarea aleatorie a uneia sau mai multor gene** ale unui individ (ex: schimbarea unei valori din 0 în 1).  
- Rolul său este să prevină convergența prematură și să exploreze regiuni noi ale spațiului de căutare.

### Operatorii de selecție
Selecția determină **care indivizi vor fi părinți** pentru următoarea generație.  
Cele mai folosite metode:

1. **Selecția de tip ruletă (Roulette Wheel Selection)**  
   - Fiecare individ primește o „felie” dintr-o ruletă proporțională cu valoarea fitness-ului său.  
   - Indivizii cu fitness mai mare au șanse mai mari de a fi selectați.  
   - Permite păstrarea diversității, dar uneori indivizii slabi pot fi selectați.

2. **Selecția de tip turneu (Tournament Selection)**  
   - Se aleg aleator un grup mic de indivizi (ex: 3–5).  
   - Câștigătorul turneului (cel cu fitness cel mai bun) este selectat.  
   - Este simplă și eficientă, controlând presiunea selecției prin dimensiunea turneului.

3. **Alte variante**  
   - *Rank Selection*: indivizii sunt sortați după fitness și primesc probabilități proporționale cu rangul.  
   - *Elitism*: păstrează automat cei mai buni indivizi în generația următoare.  
   - *Stochastic Universal Sampling*: o versiune mai echilibrată a ruletei.

---

## Exercițiul 2 – Observarea convergenței pentru problema monedelor

- Programul „ConstraintExample” a fost rulat cu următorii parametri:
  - Populație: 80 indivizi
  - Generații: 100
  - Evaluator: `DeltaFitnessEvaluator` (fitness mic = mai bun)
  - Funcția de fitness: diferența față de suma dorită + penalizare pentru numărul de monede

### Observații
- Soluția s-a stabilizat în jurul generației **≈ 38–40**.  
- Cea mai bună soluție obținută:
  - **6 quarters, 1 dime, 4 nickels, 0 pennies → total 1.80$**
- **Număr de evaluări:** `100 generații × 80 indivizi = 8000 evaluări`  
- Rulările multiple produc rezultate ușor diferite (datorită naturii aleatoare).  
- Soluția tinde să se repete de la o generație la alta după convergență (elita e păstrată).

---

## Exercițiul 3 – Experimentarea parametrilor algoritmului

Au fost testați mai mulți factori:
| Parametru | Valori testate | Efect observat |
|------------|----------------|----------------|
| **Dimensiunea populației** | 40, 80, 120 | Populații mici → convergență rapidă dar soluții slabe; populații mari → diversitate mai mare, dar mai lent. |
| **Număr generații** | 50, 100, 200 | Mai multe generații permit o rafinare mai bună a soluției, dar crește timpul. |
| **Factor de penalizare (α)** | 100, 300, 500 | Un α mai mare prioritizează precizia sumei în locul numărului mic de monede. |

### Concluzii
- Parametrii controlează echilibrul între **viteză și calitatea soluției**.  
- Ajustarea corectă a mărimii populației și a factorului de penalizare duce la o convergență mai stabilă.

---

## Exercițiul 4 – Problema rucsacului

###  Specificație
- n = 10 obiecte, capacitate V = 30  
- vol(i) = i, val(i) = i²  
- Cromozom: 10 gene `BooleanGene` (1 = inclus, 0 = exclus)  
- Evaluator: `DeltaFitnessEvaluator` + fitness pozitiv (1000 - totalValue)  

### Rezultat obținut
```
items: 0 0 1 0 0 0 0 1 1 1
vol = 30 / 30
value = 254
fitness = 746.00
```
Soluția optimă: **obiectele 3, 8, 9, 10** → volum = 30, valoare = 254 ✅

### Concluzii
- Algoritmul a convergent rapid (~10 generații).  
- Fitness-ul a rămas constant după atingerea soluției optime.  
- Număr de evaluări: `150 × 120 = 18.000`.  
- Soluția respectă constrângerea de volum exact și maximizează valoarea totală.

---

## Exercițiul 5 – Extensia problemei rucsacului (max. 5 obiecte / tip)

### Modificări implementate
- Tipul genelor: `IntegerGene(conf, 0, 5)` în loc de `BooleanGene`.  
- Funcția de fitness a fost ajustată pentru a ține cont de `count_i` (numărul de obiecte de tip i).  
- Penalizare aplicată dacă `totalVolume > 30`.  

### Exemplu de rezultat
```
vol = 30 / 30
value = 278
chromosome: [1, 2, 0, 0, 1, 0, 0, 0, 1, 1]
```
- Volumul rămâne în limită, valoarea totală crește.  
- Algoritmul are un spațiu de căutare mai mare, deci convergența e puțin mai lentă.  

### Concluzii
- GA poate fi extins ușor pentru versiuni mai generale ale problemei.  
- Mutația și crossover-ul trebuie ajustate pentru a evita stagnarea.  
- Soluțiile finale respectă constrângerile și oferă valori mari, demonstrând flexibilitatea JGAP.

---

## Concluzie generală
- Am implementat și rulat **ambele probleme**: monede și rucsac.  
- Am analizat **convergența, parametrii și performanța** algoritmului genetic.  
- GA-ul a atins soluții optime și stabile.  
- Extensia demonstrează capacitatea algoritmilor genetici de a aborda probleme combinatorice mai complexe.

---

## Atașamente
- `ConstraintExample.java`  
- `SampleFitnessFunction.java`  
- `KnapsackExample.java`  
- `KnapsackFitnessFunction.java`  
