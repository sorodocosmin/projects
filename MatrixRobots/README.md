# Matrix Robots
This project involves designing and implementing a simulation of robot exploration in a map represented as an n x n square matrix. The goal is for multiple robots to explore all the cells of the matrix, starting from random positions. The robots can move in any direction within the map but cannot occupy the same cell simultaneously.\
The exploration is simulated using threads, with synchronization mechanisms in place for token extraction and cell visits. The implementation includes commands to control the robots' movements, such as starting, pausing, or resuming individual robots or all of them. Additionally, an algorithm is needed to ensure systematic exploration of the map, guaranteeing the termination of the exploration process(see [here](https://github.com/sorodocosmin/projects/blob/main/MatrixRobots/Lab7-Week7-GUI/model_neighbours.png) a simple representation of the algorithm)
#### you can check the requirements image to see more details about the project aand how it is implemented
---
### you can see a short demo [here](https://www.youtube.com/watch?v=6wZenV5ZNSo)
---
Screenshot took from the demo:
![image](https://github.com/sorodocosmin/projects/assets/61987774/c860bf2d-44e9-483e-ac1a-f7575657907f)
