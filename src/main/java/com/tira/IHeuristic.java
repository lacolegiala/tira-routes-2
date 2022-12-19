package com.tira;

public interface IHeuristic {
    // Define the heuristic interface to calculate the heuristic and the true cost
    Double heuristic(GridNode n1, GridNode n2);
    Double cost(GridNode n1, GridNode n2);
}
