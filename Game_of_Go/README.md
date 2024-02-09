# Game of go
### A minimalistic Game of GO gui
### you can see a short demo [here](https://www.youtube.com/watch?v=lmkNlhE0IsE)

<details>
<summary>More info :</summary>
    
[Game of GO](https://en.wikipedia.org/wiki/Go_(game))
```
Available arguments for this app:
    Optional:
    1. "-s <options-1>" which comes from size
    2. "-to <options-2>" which comes from type of opponent
<options-1>: the size of the board, it needs to be a positive integer, usually it is 9, 13 or 19
<options-2>: the type of opponent, it can be one of the following:
    1. "h" - human
    2. "c" - computer
If no optional arguments are given, the default values are:
    1. "-s=9"
    2. "-to=h"
Note1 : The game is finished if and only if both players pass their turn.
Note2 : The final score doesn't take into account a komi (https://en.wikipedia.org/wiki/Komi_(Go)) as it might
depend on the rules that are used (Chinese, Japanese, etc.)
Note3 : see more details about the scoring rules (https://senseis.xmp.net/?TrompTaylorRules#toc3)
also called Tromp-Taylor rules or here (https://tromp.github.io/go.html)
Note4 : After the game is finished the little squares show the territory for each player.
Note5 : It will show that a player won if captured stones + territory is greater that the other player's.
Note6 : In case of equality, the second player will be declared the winner. 
Note7 : When playing with the computer, the first one to move will be randomly chosen.
```
</details>

## How to run it ?
```bash
python .\main.py
```
You can see more details by running 
```bash
python .\main.py --help
```
