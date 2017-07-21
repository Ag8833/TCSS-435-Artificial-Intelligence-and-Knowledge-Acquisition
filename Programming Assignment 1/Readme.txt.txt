Andrew Gates - Programming Assignment 1

Input = 123456789AB DEFC
BFS: depth - 3, numCreated - 22, numExpanded - 8, maxFringe - 13
DFS: depth - 1, numCreated - 3, numExpanded - 2, maxFringe - 2
DLS, Depth = 5: depth - 3, numCreated - 6, numExpanded - 4, maxFringe - 3
GBFS H1: depth - 3, numCreated - 8, numExpanded - 4, maxFringe - 5
GBFS H2: depth - 3, numCreated - 8, numExpanded - 4, maxFringe - 5
AStar H1: depth - 3, numCreated - 8, numExpanded - 4, maxFringe - 5
AStar H2: depth - 3, numCreated - 8, numExpanded - 4, maxFringe - 5

Input = 12345678 9ABDEFC
BFS: depth - 4, numCreated - 78, numExpanded - 25, maxFringe - 45
DFS: depth - 4, numCreated - 11, numExpanded - 5, maxFringe - 7
DLS, Depth = 10: depth - 4, numCreated - 11, numExpanded - 5, maxFringe - 7
GBFS H1: depth - 4, numCreated - 14, numExpanded - 5, maxFringe - 10
GBFS H2: depth - 4, numCreated - 14, numExpanded - 5, maxFringe - 10
AStar H1: depth - 4, numCreated - 14, numExpanded - 5, maxFringe - 10
AStar H2: depth - 4, numCreated - 14, numExpanded - 5, maxFringe - 10

Time Complexities - 
BFS: O(b^d)
DFS: O(b^m)
DLS: O(b^l) (l is the cutoff depth)
GBFS H1: O(b^m)
GBFS H2: O(b^m)
AStar H1: O(b^d)
AStar H2: O(b^d)