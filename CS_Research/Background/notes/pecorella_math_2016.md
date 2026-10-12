# Math of Idle Games

* Estimate progression patterns without playing
* Balance multiple elements and keep them interesting
* Balancing a game with huge numbers
* Covers Growth, Cost, Prestige, and Generators
* Primary Currency
    * The main number being incremented
* Generator
    * Produce Primary Currency
* Primary Exchange Currency
    * Intermediate currency from Generator to Primary
    * Exchange rate can be changed independently of Primary currency
* Multipliers
    * Modifiers to income generation
    * Used to improve cost to income ratio
* Prestige
    * Resets currencies in exchange for another currency
    * Can also provide more Multipliers
* "At the most basic level, idle games are a seesaw between production rate and costs"
* Generally costs grow exponentially while production grows polynomically
```
$cost_{next} = cost_{base} \times (rate_{growth})^{owned}$
$production_{total} = (production_{base} \times owned) \times multipliers$
```
Given that $n^x$ when n > 1 will eventually exceed $x^k$
* Order of functions
    * Constant: 2
    * Logarithmic : Log(x)
    * Sublinear : Sqrt(x)
    * Linear : X
    * Superlinear: X Log(x)
    * Polynomial: x^2
    * Superpolynomial : x^log(x)
    * Exponential: 2^x
    * Superexponential: 2^(x*2)
    * Doubly Exponential: 2 ^ (x ^ x)
* "Prestige allows the player to get farther through the cost curve before falling behind"
* Choices come from multiple generators
* Optimal is best cost/income ratio at every purchase opportunity
* ![Interesting Progression](InterestingProgression.png)

* Part 2
* Part 1 describes many generators generating a single income type
* Part 2 describes gen 2 producing gen 1
* Given enough generators, income grows toward e^x, which is exponential
* Separating total owned and total purchased keeps purchasing early generators valuable

* Part 3
* Prestige resets most things and give a big bonus
* 2 Kinds, All-time and Since-Reset
* Example presetige formulas
    * $p={\sqrt{1 + 8 \times{c^M/10^12} - 1}}/2$
    * $p=150 * \sqrt{cl/10^15}$
    * $p=\root{3, cL/10^12}$
* Creating bumpy graphs (prestige should rapidly return to where the last prestige was and grow from there)
* Time between prestiges can also vary

