# Gomoku
- This project involves creating a server-client implementation of the Gomoku (Five in a row) game, allowing two remote players to join and play against each other. 
- _The server component_, implemented in the ...-Server project, manages the game and acts as a mediator between the players. It creates a server socket to listen for incoming connections from clients. The server receives commands from clients, such as creating a game, joining a game, and submitting moves. It ensures fair gameplay and enforces the rules of the game. Additionally, the server implements time control (blitz) where each player is given a specified amount of time to make their moves. If a player's time runs out, they lose the game.
- _The client component_, implemented in the ...-Client project, allows players to connect to the server and interact with the game. Players can join existing games, create new games, and submit their moves. The client provides a user-friendly interface, allowing players to input commands and receive game updates. The game continues until one player achieves five pieces in a row or until a player's time runs out.

#### those 2 requirements images contain more details about the project
---
### you can see a short demo [here](https://www.youtube.com/watch?v=gSM3RZZtqhM)
---
Screenshot took from demo:
![image](https://github.com/sorodocosmin/projects/assets/61987774/2d0ed958-e929-4732-aafe-1c21e0d53ffd)

