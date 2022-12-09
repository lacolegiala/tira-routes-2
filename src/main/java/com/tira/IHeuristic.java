package com.tira;

public interface IHeuristic {
    // Define the heuristic interface to calculate the heuristic and the true cost
    Double heuristic(GridNode node1, GridNode node2);
    Double cost(GridNode node1, GridNode node2);
}
