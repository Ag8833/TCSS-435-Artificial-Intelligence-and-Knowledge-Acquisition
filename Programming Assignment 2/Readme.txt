Minimax Algorithm (b is branching factor, d is depth) - 

Number of nodes expanded = 816
Depth level for look-ahead = 3
Time complexity = O(b^d) -> (Worst Case) 288^3 = 23,887,872
Space complexity = O(bd) -> (Worst Case) 288*3 = 864

Number of nodes expanded = 1072
Depth level for look-ahead = 4
Time complexity = O(b^d) -> (Worst Case) 288^4 = 6,879,707,136
Space complexity = O(bd) -> (Worst Case) 288*4 = 1,152

Minimax with Alpha Beta Pruning Algorithm (b is branching factor, d is depth) - 

Number of nodes expanded = 132
Depth level for look-ahead = 3
Time complexity = O(b^(d/2)) -> (Worst Case) 288^(4/2) = 4,887
Space complexity = O(bd) -> (Worst Case) 288*3 = 864

Number of nodes expanded = 128
Depth level for look-ahead = 4
Time complexity = O(b^(d/2)) -> (Worst Case) 288^4 = 82,944
Space complexity = O(bd) -> (Worst Case) 288*4 = 1,152