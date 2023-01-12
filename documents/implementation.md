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

It seems to depend on the length and direction of the path whether A* or JPS performs better.
The heuristic values also hold a significance, at least with long paths: with a long path,
both A* and JPS found a shorter route using 1 and 1 for the heuristic values, as opposed to
1 and square root of 1. It is backed up by both the tests and by visual inspection, when giving
similar values in the UI.

Usually, it seems that JPS finds shorter routes when the given path is short, and A* finds shorter
routes when the path is long. However, the heuristic values can change this.