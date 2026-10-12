# Using Evolutionary Algorithms to Target Complexity Levels in Game Economies

* Economies are in most games, and vary in complexity
* "Using Evolutionary Algorithms to craft targeted complexity graphs to model GEs"
* Resource based economies exist in many genres of games, but prominent in resource management games
* Designing and adjusting economies is complex and error-prone
* Resources can be produced or collected actively
* Resources can be traded for other resources
* Sources, Drains, Converters, Traders
    * Sources produce resources
    * Drains destroy them
    * Converters convert from one to another
    * Traders swap resource ownership
* Managing resources is directly tied to difficulty
* More difficult GEs can lead to shortages or over-abundance
* "Games try to provide progression in difficulty and challenge"
* Balancing between boredom and frustration
* Games can vary in their cognitive, emotional, physical, and social complexity - Bowman
* Using EAs is common in other aspects of games
* Representing GEs as graphs
    * Two kinds of nodes: products and producers
    * Edges are Farms (no inputs), and Factories (consume inputs)
* Complexity Fitness Function defined recursively
    * Weights outputs differently than inputs
    * Farms complexity are the output per minute
    * Factories are Iw + Ow + Csum
        * Iw = Cin * (i - 1)
        * Csum is the sum of the complexity leading to the node
    