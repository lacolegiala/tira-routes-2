# Implementation

## Structure

MapFileReader together with MapGrid is essential in creating the map from a file that contains characters presenting obstacles and free spaces.
Enum NodeType reveals whether a node is free or blocked.

OctileHeader is used to determine whether the map file has the correct kind of header.


MapGrid uses Direction and GridNode to get the needed information about nodes. GridNodeComparator is needed to compare
neighboring nodes.


MapView creates the visual presentation.


DiagonalHeuristic is used to get the heuristic for the routing algorithms.
AStar and JumpPointSearch perform the path finding algorithms.
MapApplication starts the app.


## Performance

Both A* and JPS find paths that are equally short, however they require different heuristic values for this.
A* is somewhat slower.

When comparing to the optimal lengths given for the maps, it seems that both algorithms work well with finding short paths.
With long paths, the routes found by the algorithms are shorter than the optimal lengths.