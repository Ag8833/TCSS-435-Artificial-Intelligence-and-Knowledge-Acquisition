/* Andrew Gates
 * 4/10/2017
 * TCES 435	
 * Programming Assignment 1
 * 
 * This program will solve the 15 puzzle based on the given input board from a user. It will take in an initial state, 
 * a search method to use, and options for that search method if applicable. Then it will run the corresponding 
 * algorithm that is selected until completion, either finding a solution or returning that the puzzle is not solvable.
 */

import java.util.*;

/**
 * Class that contains the algorithm methods to run the GameBoard object against. This class takes in input from the 
 * command line, and then assigns that to initialState, searchMethod, and options to be used by the algorithms.
 * @author Andrew
 *
 */
public class PA1
{
	static GameBoard myBoard;
	static int numCreated = 0;
	static int numExpanded = 0;
	static int maxFringe = 0;
	
	public static void main(String [ ] args)
	{
		// Splitting the input into initialState, searchMethod, and options
		String[] initialState = args[0].split("");
		String searchMethod = args[1];
		String options = new String();
		if(args.length > 2)
		{
			options = args[2];
		}
		myBoard = new GameBoard(initialState, 0);
		
		if(searchMethod.equals("BFS"))
		{
			BFS();
		}
		else if(searchMethod.equals("DFS"))
		{
			DFS();
		}
		else if(searchMethod.equals("DLS"))
		{
			System.out.println("DONE");
			int depthLimit = Integer.parseInt(options);
			DLS(depthLimit);
		}
		else if(searchMethod.toString().equals("GBFS"))
		{
			if(options.equals("h1"))
			{
				GBFSH1();
			}
			else
			{
				GBFSH2();
			}
		}
		else if(searchMethod.equals("AStar"))
		{
			if(options.equals("h1"))
			{
				ASTARH1();
			}
			else
			{
				ASTARH2();
			}
		}
	}
	
	/**
	 * Method to run the BFS algorithm.
	 */
	public static void BFS()
	{
		String[] currentState = null;
		
		System.out.println("INITIAL");
		myBoard.printGameBoard();
		
		Queue<GameBoard> queuedMoves = new LinkedList<GameBoard>();
		LinkedList<GameBoard> visitedMoves = new LinkedList<GameBoard>();
		
		queuedMoves.add(myBoard);
		maxFringe = queuedMoves.size();
		
		while(queuedMoves.size() > 0)
		{
			GameBoard currentMove = queuedMoves.remove();
			if(!visitedMoves.contains(currentMove))
			{
				numExpanded++;
				if(queuedMoves.size() > maxFringe)
				{
					maxFringe = queuedMoves.size();
				}
				
				visitedMoves.add(currentMove);
				System.out.println("CURRENT");
				currentMove.printGameBoard();
				
				if(currentMove.isSolved() == 1)
				{
					System.out.println("BFS - ");
					System.out.print("depth - ");
					System.out.print(currentMove.depth);
					System.out.print(", numCreated - ");
					System.out.print(numCreated);
					System.out.print(", numExpanded - ");
					System.out.print(numExpanded);
					System.out.print(", maxFringe - ");
					System.out.print(maxFringe);
	
					return;
				}

				//--------------------Right--------------------
				currentState = currentMove.getCurrentState();
				GameBoard moveRightState = new GameBoard(currentState,currentMove.depth + 1); 
				moveRightState = moveRightState.moveRight();
				System.out.println("RIGHT");
				if(moveRightState != null)
				{
					moveRightState.printGameBoard();
					queuedMoves.add(moveRightState);
					numCreated++;
				}
				else
				{
					System.out.println("INVALID MOVE");
					System.out.println();
				}
				//--------------------Right--------------------

				//--------------------Down--------------------
				currentState = currentMove.getCurrentState();
				GameBoard moveDownState = new GameBoard(currentState,currentMove.depth + 1); 
				moveDownState = moveDownState.moveDown();
				System.out.println("DOWN");
				if(moveDownState != null)
				{
					moveDownState.printGameBoard();
					queuedMoves.add(moveDownState);
					numCreated++;
				}
				else
				{
					System.out.println("INVALID MOVE");
					System.out.println();
				}
				//--------------------Down--------------------

				//--------------------Left--------------------
				currentState = currentMove.getCurrentState();
				GameBoard moveLeftState = new GameBoard(currentState,currentMove.depth + 1); 
				moveLeftState = moveLeftState.moveLeft();
				System.out.println("LEFT");
				if(moveLeftState != null)
				{
					moveLeftState.printGameBoard();
					queuedMoves.add(moveLeftState);
					numCreated++;
				}
				else
				{
					System.out.println("INVALID MOVE");
					System.out.println();
				}
				//--------------------Left--------------------

				//--------------------Up--------------------
				currentState = currentMove.getCurrentState();
				GameBoard moveUpState = new GameBoard(currentState,currentMove.depth + 1); 
				moveUpState = moveUpState.moveUp();
				System.out.println("UP");
				if(moveUpState != null)
				{
					moveUpState.printGameBoard();
					queuedMoves.add(moveUpState);
					numCreated++;
				}
				else
				{
					System.out.println("INVALID MOVE");
					System.out.println();
				}
				//--------------------Up--------------------
			}
			else
			{
				System.out.println("VISITED");
			}
		}
		System.out.println("NO SOLUTION FOUND!");
		System.out.print("depth - -1");
		System.out.print(", numCreated - 0");
		System.out.print(", numExpanded - 0");
		System.out.print(", maxFringe - 0");
	}
	
	/**
	 * Method to run the DFS algorithm.
	 */
	public static void DFS()
	{
		String[] currentState = null;
		
		System.out.println("INITIAL");
		myBoard.printGameBoard();
		
		Stack<GameBoard> queuedMoves = new Stack<GameBoard>();
		LinkedList<GameBoard> visitedMoves = new LinkedList<GameBoard>();
		
		queuedMoves.push(myBoard);
		maxFringe = queuedMoves.size();
		
		while(queuedMoves.size() > 0)
		{
			GameBoard currentMove = queuedMoves.pop();
			if(!visitedMoves.contains(currentMove))
			{
				numExpanded++;
				if(queuedMoves.size() > maxFringe)
				{
					maxFringe = queuedMoves.size();
				}
				
				visitedMoves.add(currentMove);
				System.out.println("CURRENT");
				currentMove.printGameBoard();
				
				if(currentMove.isSolved() == 1)
				{
					System.out.println("DFS - ");
					System.out.print("depth - ");
					System.out.print(currentMove.depth);
					System.out.print(", numCreated - ");
					System.out.print(numCreated);
					System.out.print(", numExpanded - ");
					System.out.print(numExpanded);
					System.out.print(", maxFringe - ");
					System.out.print(maxFringe);
					return;
				}
				
				//--------------------Up--------------------
				currentState = currentMove.getCurrentState();
				GameBoard moveUpState = new GameBoard(currentState,currentMove.depth + 1); 
				moveUpState = moveUpState.moveUp();
				System.out.println("UP");
				if(moveUpState != null)
				{
					moveUpState.printGameBoard();
					if(!queuedMoves.contains(moveUpState) && !visitedMoves.contains(moveUpState))
					{
						queuedMoves.push(moveUpState);
						numCreated++;
					}
				}
				else
				{
					System.out.println("INVALID MOVE");
					System.out.println();
				}
				//--------------------Up--------------------
				
				//--------------------Left--------------------
				currentState = currentMove.getCurrentState();
				GameBoard moveLeftState = new GameBoard(currentState,currentMove.depth + 1); 
				moveLeftState = moveLeftState.moveLeft();
				System.out.println("LEFT");
				if(moveLeftState != null)
				{
					moveLeftState.printGameBoard();
					if(!queuedMoves.contains(moveLeftState) && !visitedMoves.contains(moveLeftState))
					{
						queuedMoves.push(moveLeftState);
						numCreated++;
					}
				}
				else
				{
					System.out.println("INVALID MOVE");
					System.out.println();
				}
				//--------------------Left--------------------
				
				//--------------------Down--------------------
				currentState = currentMove.getCurrentState();
				GameBoard moveDownState = new GameBoard(currentState,currentMove.depth + 1); 
				moveDownState = moveDownState.moveDown();
				System.out.println("DOWN");
				if(moveDownState != null)
				{
					moveDownState.printGameBoard();
					if(!queuedMoves.contains(moveDownState) && !visitedMoves.contains(moveDownState))
					{
						queuedMoves.push(moveDownState);
						numCreated++;
					}
				}
				else
				{
					System.out.println("INVALID MOVE");
					System.out.println();
				}
				//--------------------Down--------------------
				
				//--------------------Right--------------------
				currentState = currentMove.getCurrentState();
				GameBoard moveRightState = new GameBoard(currentState,currentMove.depth + 1); 
				moveRightState = moveRightState.moveRight();
				System.out.println("RIGHT");
				if(moveRightState != null)
				{
					moveRightState.printGameBoard();
					if(!queuedMoves.contains(moveRightState) && !visitedMoves.contains(moveRightState))
					{
						queuedMoves.push(moveRightState);
						numCreated++;
					}
				}
				else
				{
					System.out.println("INVALID MOVE");
					System.out.println();
				}
				//--------------------Right--------------------
			}
			else
			{
				System.out.println("VISITED");
			}
		}
		System.out.println("NO SOLUTION FOUND!");
		System.out.print("depth - -1");
		System.out.print(", numCreated - 0");
		System.out.print(", numExpanded - 0");
		System.out.print(", maxFringe - 0");
	}

	/**
	 * Method to run the DLS algorithm.
	 * @param depthLimit Value to cap the depth that the algorithm will search.
	 */
	public static void DLS(int depthLimit)
	{
		String[] currentState = null;
		
		System.out.println("INITIAL");
		myBoard.printGameBoard();
		
		Stack<GameBoard> queuedMoves = new Stack<GameBoard>();
		LinkedList<GameBoard> visitedMoves = new LinkedList<GameBoard>();
		
		queuedMoves.push(myBoard);
		maxFringe = queuedMoves.size();
		
		while(queuedMoves.size() > 0)
		{
			GameBoard currentMove = queuedMoves.pop();
			
			if(currentMove.depth >= depthLimit)
			{
				System.out.println("DEPTH LIMIT REACHED");
			}
			
			else if(!visitedMoves.contains(currentMove))
			{
				numExpanded++;
				if(queuedMoves.size() > maxFringe)
				{
					maxFringe = queuedMoves.size();
				}
				
				visitedMoves.add(currentMove);
				System.out.println("CURRENT");
				currentMove.printGameBoard();
				
				if(currentMove.isSolved() == 1)
				{
					System.out.println("DLS - ");
					System.out.print("depth - ");
					System.out.print(currentMove.depth);
					System.out.print(", numCreated - ");
					System.out.print(numCreated);
					System.out.print(", numExpanded - ");
					System.out.print(numExpanded);
					System.out.print(", maxFringe - ");
					System.out.print(maxFringe);
					return;
				}

				//--------------------Up--------------------
				currentState = currentMove.getCurrentState();
				GameBoard moveUpState = new GameBoard(currentState,currentMove.depth + 1); 
				moveUpState = moveUpState.moveUp();
				System.out.println("UP");
				if(moveUpState != null)
				{
					moveUpState.printGameBoard();
					if(!visitedMoves.contains(moveUpState) && moveUpState.depth <= depthLimit)
					{
						queuedMoves.push(moveUpState);
						numCreated++;
					}
				}
				else
				{
					System.out.println("INVALID MOVE");
					System.out.println();
				}
				//--------------------Up--------------------
				
				//--------------------Left--------------------
				currentState = currentMove.getCurrentState();
				GameBoard moveLeftState = new GameBoard(currentState,currentMove.depth + 1); 
				moveLeftState = moveLeftState.moveLeft();
				System.out.println("LEFT");
				if(moveLeftState != null)
				{
					moveLeftState.printGameBoard();
					if(!visitedMoves.contains(moveLeftState) && moveLeftState.depth <= depthLimit)
					{
						queuedMoves.push(moveLeftState);
						numCreated++;
					}
				}
				else
				{
					System.out.println("INVALID MOVE");
					System.out.println();
				}
				//--------------------Left--------------------
				
				//--------------------Down--------------------
				currentState = currentMove.getCurrentState();
				GameBoard moveDownState = new GameBoard(currentState,currentMove.depth + 1); 
				moveDownState = moveDownState.moveDown();
				System.out.println("DOWN");
				if(moveDownState != null)
				{
					moveDownState.printGameBoard();
					if(!visitedMoves.contains(moveDownState) && moveDownState.depth <= depthLimit)
					{
						queuedMoves.push(moveDownState);
						numCreated++;
					}
				}
				else
				{
					System.out.println("INVALID MOVE");
					System.out.println();
				}
				//--------------------Down--------------------
				
				//--------------------Right--------------------
				currentState = currentMove.getCurrentState();
				GameBoard moveRightState = new GameBoard(currentState,currentMove.depth + 1); 
				moveRightState = moveRightState.moveRight();
				System.out.println("RIGHT");
				if(moveRightState != null)
				{
					moveRightState.printGameBoard();
					if(!visitedMoves.contains(moveRightState) && moveRightState.depth <= depthLimit)
					{
						queuedMoves.push(moveRightState);
						numCreated++;
					}
				}
				else
				{
					System.out.println("INVALID MOVE");
					System.out.println();
				}
				//--------------------Right--------------------
			}
			else
			{
				System.out.println("VISITED");
			}
		}
		System.out.println("NO SOLUTION FOUND!");
		System.out.print("depth - -1");
		System.out.print(", numCreated - 0");
		System.out.print(", numExpanded - 0");
		System.out.print(", maxFringe - 0");
	}

	/**
	 * Method to run the GBFS algorithm with heuristic 1 (Misplaced Tiles).
	 */
	public static void GBFSH1()
	{
		String[] currentState = null;
		System.out.println("INITIAL");
		myBoard.printGameBoard();
		
		Comparator<GameBoard> comparator = new Comparator<GameBoard>() 
		{
	  		@Override
	  		public int compare(GameBoard a, GameBoard b) 
	  		{
	  			return (a.getMisplacedTiles() - b.getMisplacedTiles());
	  		}
	  	};
	  	
		PriorityQueue<GameBoard> queuedMoves = new PriorityQueue<GameBoard>(10,comparator);
		LinkedList<GameBoard> visitedMoves = new LinkedList<GameBoard>();
		
		queuedMoves.add(myBoard);
		maxFringe = queuedMoves.size();
		
		while(queuedMoves.size() > 0)
		{
			GameBoard currentMove = queuedMoves.remove();
			if(!visitedMoves.contains(currentMove))
			{
				numExpanded++;
				if(queuedMoves.size() > maxFringe)
				{
					maxFringe = queuedMoves.size();
				}
				
				visitedMoves.add(currentMove);
				System.out.println("CURRENT");
				currentMove.printGameBoard();
				
				if(currentMove.isSolved() == 1)
				{
					System.out.println("GBFS H1 - ");
					System.out.print("depth - ");
					System.out.print(currentMove.depth);
					System.out.print(", numCreated - ");
					System.out.print(numCreated);
					System.out.print(", numExpanded - ");
					System.out.print(numExpanded);
					System.out.print(", maxFringe - ");
					System.out.print(maxFringe);
					return;
				}

				//--------------------Right--------------------
				currentState = currentMove.getCurrentState();
				GameBoard moveRightState = new GameBoard(currentState,currentMove.depth + 1); 
				moveRightState = moveRightState.moveRight();
				System.out.println("RIGHT");
				if(moveRightState != null)
				{
					moveRightState.printGameBoard();
					queuedMoves.add(moveRightState);
					numCreated++;
				}
				else
				{
					System.out.println("INVALID MOVE");
					System.out.println();
				}
				//--------------------Right--------------------

				//--------------------Down--------------------
				currentState = currentMove.getCurrentState();
				GameBoard moveDownState = new GameBoard(currentState,currentMove.depth + 1); 
				moveDownState = moveDownState.moveDown();
				System.out.println("DOWN");
				if(moveDownState != null)
				{
					moveDownState.printGameBoard();
					queuedMoves.add(moveDownState);
					numCreated++;
				}
				else
				{
					System.out.println("INVALID MOVE");
					System.out.println();
				}
				//--------------------Down--------------------

				//--------------------Left--------------------
				currentState = currentMove.getCurrentState();
				GameBoard moveLeftState = new GameBoard(currentState,currentMove.depth + 1); 
				moveLeftState = moveLeftState.moveLeft();
				System.out.println("LEFT");
				if(moveLeftState != null)
				{
					moveLeftState.printGameBoard();
					queuedMoves.add(moveLeftState);
					numCreated++;
				}
				else
				{
					System.out.println("INVALID MOVE");
					System.out.println();
				}
				//--------------------Left--------------------

				//--------------------Up--------------------
				currentState = currentMove.getCurrentState();
				GameBoard moveUpState = new GameBoard(currentState,currentMove.depth + 1); 
				moveUpState = moveUpState.moveUp();
				System.out.println("UP");
				if(moveUpState != null)
				{
					moveUpState.printGameBoard();
					queuedMoves.add(moveUpState);
					numCreated++;
				}
				else
				{
					System.out.println("INVALID MOVE");
					System.out.println();
				}
				//--------------------Up--------------------
			}
			else
			{
				System.out.println("VISITED");
			}
		}
		System.out.println("NO SOLUTION FOUND!");
		System.out.print("depth - -1");
		System.out.print(", numCreated - 0");
		System.out.print(", numExpanded - 0");
		System.out.print(", maxFringe - 0");
	}

	/**
	 * Method to run the GBFS algorithm with heuristic 2 (Manhattan Distance).
	 */
	public static void GBFSH2()
	{
		String[] currentState = null;
		System.out.println("INITIAL");
		myBoard.printGameBoard();
		
		Comparator<GameBoard> comparator = new Comparator<GameBoard>() 
		{
	  		@Override
	  		public int compare(GameBoard a, GameBoard b) 
	  		{
	  			return (a.getManhattanSum() - b.getManhattanSum());
	  		}
	  	};
	  	
		PriorityQueue<GameBoard> queuedMoves = new PriorityQueue<GameBoard>(10,comparator);
		LinkedList<GameBoard> visitedMoves = new LinkedList<GameBoard>();
		
		queuedMoves.add(myBoard);
		maxFringe = queuedMoves.size();
		
		while(queuedMoves.size() > 0)
		{
			GameBoard currentMove = queuedMoves.remove();
			if(!visitedMoves.contains(currentMove))
			{
				numExpanded++;
				if(queuedMoves.size() > maxFringe)
				{
					maxFringe = queuedMoves.size();
				}
				
				visitedMoves.add(currentMove);
				System.out.println("CURRENT");
				currentMove.printGameBoard();
				
				if(currentMove.isSolved() == 1)
				{
					System.out.println("GBFS H2 - ");
					System.out.print("depth - ");
					System.out.print(currentMove.depth);
					System.out.print(", numCreated - ");
					System.out.print(numCreated);
					System.out.print(", numExpanded - ");
					System.out.print(numExpanded);
					System.out.print(", maxFringe - ");
					System.out.print(maxFringe);
					return;
				}

				//--------------------Right--------------------
				currentState = currentMove.getCurrentState();
				GameBoard moveRightState = new GameBoard(currentState,currentMove.depth + 1); 
				moveRightState = moveRightState.moveRight();
				System.out.println("RIGHT");
				if(moveRightState != null)
				{
					moveRightState.printGameBoard();
					queuedMoves.add(moveRightState);
					numCreated++;
				}
				else
				{
					System.out.println("INVALID MOVE");
					System.out.println();
				}
				//--------------------Right--------------------

				//--------------------Down--------------------
				currentState = currentMove.getCurrentState();
				GameBoard moveDownState = new GameBoard(currentState,currentMove.depth + 1); 
				moveDownState = moveDownState.moveDown();
				System.out.println("DOWN");
				if(moveDownState != null)
				{
					moveDownState.printGameBoard();
					queuedMoves.add(moveDownState);
					numCreated++;
				}
				else
				{
					System.out.println("INVALID MOVE");
					System.out.println();
				}
				//--------------------Down--------------------

				//--------------------Left--------------------
				currentState = currentMove.getCurrentState();
				GameBoard moveLeftState = new GameBoard(currentState,currentMove.depth + 1); 
				moveLeftState = moveLeftState.moveLeft();
				System.out.println("LEFT");
				if(moveLeftState != null)
				{
					moveLeftState.printGameBoard();
					queuedMoves.add(moveLeftState);
					numCreated++;
				}
				else
				{
					System.out.println("INVALID MOVE");
					System.out.println();
				}
				//--------------------Left--------------------

				//--------------------Up--------------------
				currentState = currentMove.getCurrentState();
				GameBoard moveUpState = new GameBoard(currentState,currentMove.depth + 1); 
				moveUpState = moveUpState.moveUp();
				System.out.println("UP");
				if(moveUpState != null)
				{
					moveUpState.printGameBoard();
					queuedMoves.add(moveUpState);
					numCreated++;
				}
				else
				{
					System.out.println("INVALID MOVE");
					System.out.println();
				}
				//--------------------Up--------------------
			}
			else
			{
				System.out.println("VISITED");
			}
		}
		System.out.println("NO SOLUTION FOUND!");
		System.out.print("depth - -1");
		System.out.print(", numCreated - 0");
		System.out.print(", numExpanded - 0");
		System.out.print(", maxFringe - 0");
	}

	/**
	 * Method to run the AStar algorithm with heuristic 1 (Misplaced Tiles).
	 */
	public static void ASTARH1()
	{
		String[] currentState = null;
		System.out.println("INITIAL");
		myBoard.printGameBoard();
		
		Comparator<GameBoard> comparator = new Comparator<GameBoard>() 
		{
	  		@Override
	  		public int compare(GameBoard a, GameBoard b) 
	  		{
	  			return ((a.getMisplacedTiles()+a.depth) - (b.getMisplacedTiles() + b.depth));
	  		}
	  	};
	  	
		PriorityQueue<GameBoard> queuedMoves = new PriorityQueue<GameBoard>(10,comparator);
		LinkedList<GameBoard> visitedMoves = new LinkedList<GameBoard>();
		
		queuedMoves.add(myBoard);
		maxFringe = queuedMoves.size();
		
		while(queuedMoves.size() > 0)
		{
			GameBoard currentMove = queuedMoves.remove();
			if(!visitedMoves.contains(currentMove))
			{
				numExpanded++;
				if(queuedMoves.size() > maxFringe)
				{
					maxFringe = queuedMoves.size();
				}
				
				visitedMoves.add(currentMove);
				System.out.println("CURRENT");
				currentMove.printGameBoard();
				
				if(currentMove.isSolved() == 1)
				{
					System.out.println("ASTAR H1 - ");
					System.out.print("depth - ");
					System.out.print(currentMove.depth);
					System.out.print(", numCreated - ");
					System.out.print(numCreated);
					System.out.print(", numExpanded - ");
					System.out.print(numExpanded);
					System.out.print(", maxFringe - ");
					System.out.print(maxFringe);
					return;
				}

				//--------------------Right--------------------
				currentState = currentMove.getCurrentState();
				GameBoard moveRightState = new GameBoard(currentState,currentMove.depth + 1); 
				moveRightState = moveRightState.moveRight();
				System.out.println("RIGHT");
				if(moveRightState != null)
				{
					moveRightState.printGameBoard();
					queuedMoves.add(moveRightState);
					numCreated++;
				}
				else
				{
					System.out.println("INVALID MOVE");
					System.out.println();
				}
				//--------------------Right--------------------

				//--------------------Down--------------------
				currentState = currentMove.getCurrentState();
				GameBoard moveDownState = new GameBoard(currentState,currentMove.depth + 1); 
				moveDownState = moveDownState.moveDown();
				System.out.println("DOWN");
				if(moveDownState != null)
				{
					moveDownState.printGameBoard();
					queuedMoves.add(moveDownState);
					numCreated++;
				}
				else
				{
					System.out.println("INVALID MOVE");
					System.out.println();
				}
				//--------------------Down--------------------

				//--------------------Left--------------------
				currentState = currentMove.getCurrentState();
				GameBoard moveLeftState = new GameBoard(currentState,currentMove.depth + 1); 
				moveLeftState = moveLeftState.moveLeft();
				System.out.println("LEFT");
				if(moveLeftState != null)
				{
					moveLeftState.printGameBoard();
					queuedMoves.add(moveLeftState);
					numCreated++;
				}
				else
				{
					System.out.println("INVALID MOVE");
					System.out.println();
				}
				//--------------------Left--------------------

				//--------------------Up--------------------
				currentState = currentMove.getCurrentState();
				GameBoard moveUpState = new GameBoard(currentState,currentMove.depth + 1); 
				moveUpState = moveUpState.moveUp();
				System.out.println("UP");
				if(moveUpState != null)
				{
					moveUpState.printGameBoard();
					queuedMoves.add(moveUpState);
					numCreated++;
				}
				else
				{
					System.out.println("INVALID MOVE");
					System.out.println();
				}
				//--------------------Up--------------------
			}
			else
			{
				System.out.println("VISITED");
			}
		}
		System.out.println("NO SOLUTION FOUND!");
		System.out.print("depth - -1");
		System.out.print(", numCreated - 0");
		System.out.print(", numExpanded - 0");
		System.out.print(", maxFringe - 0");
	}

	/**
	 * Method to run the AStar algorithm with heuristic 2 (Manhattan Distance).
	 */
	public static void ASTARH2()
	{
		String[] currentState = null;
		System.out.println("INITIAL");
		myBoard.printGameBoard();
		
		Comparator<GameBoard> comparator = new Comparator<GameBoard>() 
		{
	  		@Override
	  		public int compare(GameBoard a, GameBoard b) 
	  		{
	  			return ((a.getManhattanSum()+a.depth) - (b.getManhattanSum() + b.depth));
	  		}
	  	};
	  	
		PriorityQueue<GameBoard> queuedMoves = new PriorityQueue<GameBoard>(10,comparator);
		LinkedList<GameBoard> visitedMoves = new LinkedList<GameBoard>();
		
		queuedMoves.add(myBoard);
		maxFringe = queuedMoves.size();
		
		while(queuedMoves.size() > 0)
		{
			GameBoard currentMove = queuedMoves.remove();
			if(!visitedMoves.contains(currentMove))
			{
				numExpanded++;
				if(queuedMoves.size() > maxFringe)
				{
					maxFringe = queuedMoves.size();
				}
				
				visitedMoves.add(currentMove);
				System.out.println("CURRENT");
				currentMove.printGameBoard();
				
				if(currentMove.isSolved() == 1)
				{
					System.out.println("ASTAR H2 - ");
					System.out.print("depth - ");
					System.out.print(currentMove.depth);
					System.out.print(", numCreated - ");
					System.out.print(numCreated);
					System.out.print(", numExpanded - ");
					System.out.print(numExpanded);
					System.out.print(", maxFringe - ");
					System.out.print(maxFringe);
					return;
				}

				//--------------------Right--------------------
				currentState = currentMove.getCurrentState();
				GameBoard moveRightState = new GameBoard(currentState,currentMove.depth + 1); 
				moveRightState = moveRightState.moveRight();
				System.out.println("RIGHT");
				if(moveRightState != null)
				{
					moveRightState.printGameBoard();
					queuedMoves.add(moveRightState);
					numCreated++;
				}
				else
				{
					System.out.println("INVALID MOVE");
					System.out.println();
				}
				//--------------------Right--------------------

				//--------------------Down--------------------
				currentState = currentMove.getCurrentState();
				GameBoard moveDownState = new GameBoard(currentState,currentMove.depth + 1); 
				moveDownState = moveDownState.moveDown();
				System.out.println("DOWN");
				if(moveDownState != null)
				{
					moveDownState.printGameBoard();
					queuedMoves.add(moveDownState);
					numCreated++;
				}
				else
				{
					System.out.println("INVALID MOVE");
					System.out.println();
				}
				//--------------------Down--------------------

				//--------------------Left--------------------
				currentState = currentMove.getCurrentState();
				GameBoard moveLeftState = new GameBoard(currentState,currentMove.depth + 1); 
				moveLeftState = moveLeftState.moveLeft();
				System.out.println("LEFT");
				if(moveLeftState != null)
				{
					moveLeftState.printGameBoard();
					queuedMoves.add(moveLeftState);
					numCreated++;
				}
				else
				{
					System.out.println("INVALID MOVE");
					System.out.println();
				}
				//--------------------Left--------------------

				//--------------------Up--------------------
				currentState = currentMove.getCurrentState();
				GameBoard moveUpState = new GameBoard(currentState,currentMove.depth + 1); 
				moveUpState = moveUpState.moveUp();
				System.out.println("UP");
				if(moveUpState != null)
				{
					moveUpState.printGameBoard();
					queuedMoves.add(moveUpState);
					numCreated++;
				}
				else
				{
					System.out.println("INVALID MOVE");
					System.out.println();
				}
				//--------------------Up--------------------
			}
			else
			{
				System.out.println("VISITED");
			}
		}
		System.out.println("NO SOLUTION FOUND!");
		System.out.print("depth - -1");
		System.out.print(", numCreated - 0");
		System.out.print(", numExpanded - 0");
		System.out.print(", maxFringe - 0");
	}
}

/**
 * Class that contains the GameBoard object. This class contains a subclass with the location of the blank tile.
 * As well as functions to get the current state, get the misplaced tiles, get the Manhattan sum, check if the 
 * board is solved, and move the blank piece right, down, left and up.
 * @author Andrew
 *
 */
class GameBoard
{
	/**
	 * The BlankPosition class contains the coordinates of the blank piece located inside the GameBoard object.
	 * @author Andrew
	 *
	 */
	public class BlankPosition
	{
		public int x;
		public int y;
		
		/**
		 * Constructor to initialize blank piece location.
		 * @param x X-coordinate of blank piece.
		 * @param y Y-coordinate of blank piece.
		 */
		public BlankPosition(int x, int y)
		{
			this.x = x;
			this.y = y;
		}
	}
	
	// Initialize the gameBoardSize, depth, gameBoard string, and currentBlankPosition
	public static final int gameBoardSize = 4;
	public int depth;
	public String[][]gameBoard = new String[gameBoardSize][gameBoardSize];
	BlankPosition currentBlankPosition = new BlankPosition(0,0);
	
	// Initialize finished arrays to check solved state against
	public static String[][]gameBoardSolvedOne = new String[gameBoardSize][gameBoardSize];
	public static String[][]gameBoardSolvedTwo = new String[gameBoardSize][gameBoardSize];
	String[]solvedStateOne = {"1","2","3","4","5","6","7","8","9","A","B","C","D","E","F"," "};
	String[]solvedStateTwo = {"1","2","3","4","5","6","7","8","9","A","B","C","D","F","E"," "};
	
	/**
	 * Constructor to initialize GameBoard object, as well as the solved states.
	 * @param inputState String sent in to initialize GameBoard object to.
	 * @param inputDepth Depth to be used for AStar algorithms.
	 */
	public GameBoard(String[] inputState, int inputDepth)
	{	
		depth = inputDepth;
		int k = 0;
		for(int i = 0; i < gameBoardSize; i ++)
		{
			for(int j = 0; j < gameBoardSize; j++)
			{
				gameBoard[i][j] = inputState[k];
				if(inputState[k].equals(" "))
				{
					currentBlankPosition.x = i;
					currentBlankPosition.y = j;
				}
				gameBoardSolvedOne[i][j] = solvedStateOne[k];
				gameBoardSolvedTwo[i][j] = solvedStateTwo[k];
				k++;
			}
		}
	}
	
	/**
	 * Method to return the current state of the GameBoard object.
	 * @return Returns a String[] that contains the current state of the board.
	 */
	public String[] getCurrentState()
	{
		String[]currentState = {"0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0"};
		int k = 0;
		for(int i = 0; i < gameBoardSize; i++)
		{
			for(int j = 0; j < gameBoardSize; j++)
			{
				currentState[k] = this.gameBoard[i][j];
				k++;
			}
		}
		return currentState;
	}
	
	/**
	 * Method to be used for h1 heuristics to determine the amount of misplaced tiles in a GameBoard.
	 * @return Returns the amount of misplaced tiles.
	 */
	public int getMisplacedTiles()
	{
		int misplaced = 0;
		for(int i = 0; i < gameBoardSize; i ++)
		{
			for(int j = 0; j < gameBoardSize; j++)
			{
				if(!gameBoard[i][j].equals(gameBoardSolvedOne[i][j]))
				{
					misplaced++;
				}
			}
		}
		return misplaced;
	}
	
	/**
	 * Method to be used for h2 heuristics to determine the sum of distances from tiles to their solved location.
	 * @return Returns the total sum of distances from tiles to their solved locations.
	 */
	public int getManhattanSum()
	{
		int manhattanDistanceSum = 0;
		int value = 0;
	    for (int i = 0; i < gameBoardSize; i++)
	    {
	        for (int j = 0; j < gameBoardSize; j++) 
	        {
	        	if(this.gameBoard[i][j].equals(" "))
	        	{
	        		value = 0;
	        	}
	        	else if(this.gameBoard[i][j].equals("A"))
	        	{
	        		value = 10;
	        	}
	        	else if(this.gameBoard[i][j].equals("B"))
	        	{
	        		value = 11;
	        	}
	        	else if(this.gameBoard[i][j].equals("C"))
	        	{
	        		value = 12;
	        	}
	        	else if(this.gameBoard[i][j].equals("D"))
	        	{
	        		value = 13;
	        	}
	        	else if(this.gameBoard[i][j].equals("E"))
	        	{
	        		value = 14;
	        	}
	        	else if(this.gameBoard[i][j].equals("F"))
	        	{
	        		value = 15;
	        	}
	        	else
	        	{
	        		value = Integer.parseInt(this.gameBoard[i][j]);
	        	}
	        	
				if (value != 0) 
	            {
	                int targetX = (value - 1) / gameBoardSize;
	                int targetY = (value - 1) % gameBoardSize;
	                int dx = i - targetX;
	                int dy = j - targetY; 
	                manhattanDistanceSum += Math.abs(dx) + Math.abs(dy); 
	            } 
	        }
	    }
	    return(manhattanDistanceSum);
	}
	
	/**
	 * Method to check if the GameBoard object is solved or not by checking the solved states.
	 * @return Returns 0 if it is not solved, and 1 if it is solved.
	 */
	public int isSolved()
	{
		for(int i = 0; i < gameBoardSize; i ++)
		{
			for(int j = 0; j < gameBoardSize; j++)
			{
				if(!gameBoard[i][j].equals(gameBoardSolvedOne[i][j]))
				{
					if(!gameBoard[i][j].equals(gameBoardSolvedTwo[i][j]))
					{
						return 0;
					}
				}
			}
		}
		return 1;
	}
	
	/**
	 * Method to be used for the Comparator object in GBFS and AStar
	 */
	@Override
	public int hashCode() 
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.deepHashCode(gameBoard);
		return result;
	}

	/**
	 * Method to be used for the Comparator object in GBFS and AStar
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GameBoard other = (GameBoard) obj;
		if (!Arrays.deepEquals(gameBoard, other.gameBoard))
			return false;
		return true;
	}
	
	/**
	 * Prints the current GameBoard object.
	 */
	public void printGameBoard()
	{
		System.out.println("CURRENT GAME BOARD -");
		for(int i = 0; i < gameBoardSize; i ++)
		{
			for(int j = 0; j < gameBoardSize; j++)
			{
				System.out.print(this.gameBoard[i][j]);	
			}
			System.out.println();
		}
		System.out.println();
	}
	
	/**
	 * Prints the current GameBoard object's blank position.
	 */
	public void printBlankPosition()
	{
		System.out.print("X - ");
		System.out.print(this.currentBlankPosition.x);
		System.out.print(", Y - ");
		System.out.println(this.currentBlankPosition.y);
	}
	
	/**
	 * Takes the current GameBoard object's blank position and swaps it with the tile to the right if possible.
	 * @return Returns an updated GameBoard object if successful, or null if not successful.
	 */
	public GameBoard moveRight()
	{
		GameBoard updatedBoard = this;
		int xValue = updatedBoard.currentBlankPosition.x;
		int yValue = updatedBoard.currentBlankPosition.y;
		
		if((yValue + 1) <= (gameBoardSize - 1))
		{
			String blankPiece = updatedBoard.gameBoard[xValue][yValue];
			String swapPiece = updatedBoard.gameBoard[xValue][yValue + 1];
			updatedBoard.gameBoard[xValue][yValue] = swapPiece;
			updatedBoard.gameBoard[xValue][yValue + 1] = blankPiece;
			updatedBoard.currentBlankPosition.y = yValue + 1;
			
			return updatedBoard;
		}
		else
		{
			return null;
		}
	}
	
	/**
	 * Takes the current GameBoard object's blank position and swaps it with the tile below it if possible.
	 * @return Returns an updated GameBoard object if successful, or null if not successful.
	 */
	public GameBoard moveDown()
	{
		GameBoard updatedBoard = this;
		int xValue = updatedBoard.currentBlankPosition.x;
		int yValue = updatedBoard.currentBlankPosition.y;
		
		if((xValue + 1) <= (gameBoardSize - 1))
		{
			String blankPiece = updatedBoard.gameBoard[xValue][yValue];
			String swapPiece = updatedBoard.gameBoard[xValue + 1][yValue];
			updatedBoard.gameBoard[xValue][yValue] = swapPiece;
			updatedBoard.gameBoard[xValue + 1][yValue] = blankPiece;
			updatedBoard.currentBlankPosition.x = xValue + 1;
			
			return updatedBoard;
		}
		else
		{
			return null;
		}
	}
	
	/**
	 * Takes the current GameBoard object's blank position and swaps it with the tile to the left if possible.
	 * @return Returns an updated GameBoard object if successful, or null if not successful.
	 */
	public GameBoard moveLeft()
	{
		GameBoard updatedBoard = this;
		int xValue = updatedBoard.currentBlankPosition.x;
		int yValue = updatedBoard.currentBlankPosition.y;
		
		if((yValue - 1) >= 0)
		{
			String blankPiece = updatedBoard.gameBoard[xValue][yValue];
			String swapPiece = updatedBoard.gameBoard[xValue][yValue - 1];
			updatedBoard.gameBoard[xValue][yValue] = swapPiece;
			updatedBoard.gameBoard[xValue][yValue - 1] = blankPiece;
			updatedBoard.currentBlankPosition.y = yValue - 1;
			
			return updatedBoard;
		}
		else
		{
			return null;
		}
	}
	
	/**
	 * Takes the current GameBoard object's blank position and swaps it with the tile above it if possible.
	 * @return Returns an updated GameBoard object if successful, or null if not successful.
	 */
	public GameBoard moveUp()
	{
		GameBoard updatedBoard = this;
		int xValue = updatedBoard.currentBlankPosition.x;
		int yValue = updatedBoard.currentBlankPosition.y;
		
		if((xValue - 1) >= 0)
		{
			String blankPiece = updatedBoard.gameBoard[xValue][yValue];
			String swapPiece = updatedBoard.gameBoard[xValue - 1][yValue];
			updatedBoard.gameBoard[xValue][yValue] = swapPiece;
			updatedBoard.gameBoard[xValue - 1][yValue] = blankPiece;
			updatedBoard.currentBlankPosition.x = xValue - 1;
			
			return updatedBoard;
		}
		else
		{
			return null;
		}
	}
}