/* Andrew Gates
 * 4/27/2017
 * TCES 435	
 * Programming Assignment 2
 * 
 * This program will allow the user to play a 1 on 1 game of Pentago against an AI. The game will allow the user to enter a name,
 * choose which color they want to be (B or W), and choose who goes first. Then it will alternate back and forth between player 
 * turns and AI turns until a winner is found when 5 of the same letter are in a row, or no winner if the board is full.
 */

import java.util.*;

/**
 * Class that contains the initialization of the PentagoBoard object, along with taking in input from 
 * the user for all of the setting related info, such as Player 1 Name, Player 1 Color, and Initial Player.
 * @author Andrew
 *
 */
public class PA2
{	
	public static void main(String [ ] args)
	{
		int winnerFound = 0;
		int pentagoSize = 3;
		String[][] initialBoard = new String[pentagoSize * 2][pentagoSize * 2];
		Scanner scanner = new Scanner(System.in);
		
		//  Initialize PentagoBoard to all "."
		for(int i = 0; i < pentagoSize * 2; i ++)
		{
			for(int j = 0; j < pentagoSize * 2; j++)
			{
				initialBoard[i][j] = ".";
			}
		}
		PentagoBoard gameBoard = new PentagoBoard(initialBoard, 0);
		
		// User input for settings
	    System.out.print("Player 1 Name: ");
	    String playerOneName = scanner.next();
	    System.out.print("Player 2 Name: ");
	    System.out.println(gameBoard.AIName);
	    
	    System.out.print("Player 1 Token Color (B or W): ");
	    String playerOneColor = scanner.next();
	    System.out.print("Player 2 Token Color (B or W): ");
	    gameBoard.setAIColor(playerOneColor, gameBoard);
	    System.out.println(gameBoard.AIColor);
	    
	    System.out.print("Player to Move Next (1 or 2): ");
	    int playerTurn = scanner.nextInt();
	    scanner.nextLine();
	    
	    gameBoard.boardCreator = playerTurn;
	    
	    // Loop to run the game until either a single winner, tie, or draw is found
		while(winnerFound == 0)
		{
			// Human player's turn
			if(playerTurn == 1)
			{
				System.out.println();
				System.out.print(playerOneName);
				System.out.print(" Enter Your Move (B/P BD): ");
			    String playerOneMove = scanner.nextLine();
			    
			    // Loop while that move is invalid
			    while(gameBoard.isMoveValid(playerOneMove) == false)
			    {
			    	System.out.println("INVALID MOVE");
			    	System.out.print(playerOneName);
					System.out.print(" Enter Your Move (B/P BD): ");
				    playerOneMove = scanner.nextLine();
			    }
			    gameBoard.humanGameMove(playerOneMove, playerOneColor);
			    
			    System.out.println();
			    System.out.println("CURRENT BOARD - ");
			    gameBoard.printPentagoBoard();
			    
			    winnerFound = gameBoard.winnerCheck();
			    
				playerTurn = 2;
			}
			
			// AI player's turn
			else
			{
				System.out.println();
				System.out.println("Gaston's Move (B/P BD): ");
				PentagoBoard bestBoard = gameBoard.minimaxGameMove(gameBoard, 2, 3, Integer.MIN_VALUE, Integer.MAX_VALUE);
				
				gameBoard.updatePentagoBoardState(bestBoard.initialState);
				
				System.out.println();
			    System.out.println("CURRENT BOARD - ");
				gameBoard.printPentagoBoard();
				
				winnerFound = gameBoard.winnerCheck();
				
				playerTurn = 1;
			}
		}
		
		// Black is the winner
		if(winnerFound == 1)
		{
			if(playerOneColor.equals("B"))
			{
				System.out.print(playerOneName);
				System.out.println(" - Is The Winner!");
			}
			else
			{
				System.out.print(gameBoard.AIName);
				System.out.println(" - Is The Winner!");
			}
		}
		
		// White is the winner
		else if(winnerFound == 2)
		{
			if(playerOneColor.equals("B"))
			{
				System.out.print(gameBoard.AIName);
				System.out.println(" - Is The Winner!");
			}
			else
			{
				System.out.print(playerOneName);
				System.out.println(" - Is The Winner!");
			}
		}
		
		// Tie, black and white are winners
		else if(winnerFound == 3)
		{
			System.out.println("Tie - Both Players Win!");
		}
		
		// Board full, neither are winners
		else
		{
			System.out.print("Board Full - Neither Player Wins!");
		}
		
		scanner.close();
	}
}

/**
 * Class that contains the Pentago Board itself, split into TL, TR, BL, and BR. This also contains functions for initialization, 
 * flipping the AI color, updating the AI color, checking for a winner, getting the board state, updating the board state, printing
 * the board, and the functions for an AI move, and a human move, and the associated functions that they use.
 * @author Andrew
 *
 */
class PentagoBoard
{
	// Initial State used to backtrack what the state should be from the best move in Minimax
	public String[][]initialState = new String[pentagoSize * 2][pentagoSize * 2];
	
	// States for each portion of the Pentago Board
	public String[][]pentagoBoardTL = new String[pentagoSize][pentagoSize];
	public String[][]pentagoBoardTR = new String[pentagoSize][pentagoSize];
	public String[][]pentagoBoardBL = new String[pentagoSize][pentagoSize];
	public String[][]pentagoBoardBR = new String[pentagoSize][pentagoSize];
	
	// Variables uesd to store info about the Pentago Board
	static int pentagoSize = 3;
	int boardUtility = 0;
	int boardCreator = 0;
	int alpha = Integer.MIN_VALUE;
	int beta = Integer.MAX_VALUE;
	String AIName = "Gaston";
	String AIColor = new String();
	
	/**
	 * Constructor to initialize the PentagoBoard object by loading initialBoard into the corresponding quadrants
	 * @param initialBoard The state to be used for the Pentago Board
	 * @param currentPlayer The current player who is making the board, used in Minimax
	 */
	public PentagoBoard(String[][] initialBoard, int currentPlayer)
	{	
		for(int i = 0; i < pentagoSize * 2; i ++)
		{
			if(i < 3)
			{
				for(int j = 0; j < pentagoSize * 2; j++)
				{
					if(j < 3)
					{
						pentagoBoardTL[i][j] = initialBoard[i][j];
					}
					else
					{
						pentagoBoardTR[i][j - 3] = initialBoard[i][j];
					}
				}
			}
			else
			{
				for(int j = 0; j < pentagoSize * 2; j++)
				{
					if(j < 3)
					{
						pentagoBoardBL[i - 3][j] = initialBoard[i][j];
					}
					else
					{
						pentagoBoardBR[i - 3][j - 3] = initialBoard[i][j];
					}
				}
			}
			this.boardCreator = currentPlayer;
		}
	}
	
	/**
	 * Function to swap the AI color based on the color that is sent in. Used to alternate colors for Minimax.
	 * @param playerOneColor The color of the creator who made the board
	 * @param currentBoard The current PentagoBoard of the creator 
	 */
	public void setAIColor(String playerOneColor, PentagoBoard currentBoard)
	{
		if(playerOneColor.equals("B"))
		{
			currentBoard.AIColor = "W";
		}
		else
		{
			currentBoard.AIColor = "B";
		}
	}
	
	/**
	 * Function to set the AI color based on the color that is sent in.
	 * @param playerOneColor The color of the creator who made the board
	 * @param currentBoard The current PentagoBoard of the creator 
	 */
	public void updateAIColor(String playerOneColor, PentagoBoard currentBoard)
	{
		currentBoard.AIColor = playerOneColor;
	}
	
	/**
	 * Function to check whether a winner was found or not.
	 * @return Returns 1 if B is the winner, 2 if W is the winner, 3 if there is a tie, and 4 if there is a draw
	 */
	public int winnerCheck()
	{
		int winnerFound = 0;
		int fullBoardCounter = 0;
		int consecutiveBCounter = 0;
		int consecutiveWCounter = 0;
		int pentagoResize = 0;
		int startingI = 0;
		int startingJ = 0;
		String[][] pentagoBoardFull = new String[pentagoSize * 2][pentagoSize * 2];
		
		// Load individual boards into a full board for easier traversal, while checking to see how many positions are filled
		for(int i = 0; i < pentagoSize * 2; i ++)
		{
			if(i < 3)
			{
				for(int j = 0; j < pentagoSize * 2; j++)
				{
					if(j < 3)
					{
						pentagoBoardFull[i][j] = pentagoBoardTL[i][j];
						if(pentagoBoardFull[i][j].equals("b") || pentagoBoardFull[i][j].equals("w"))
						{
							fullBoardCounter++;
						}
					}
					else
					{
						pentagoBoardFull[i][j] = pentagoBoardTR[i][j - 3];
						if(pentagoBoardFull[i][j].equals("b") || pentagoBoardFull[i][j].equals("w"))
						{
							fullBoardCounter++;
						}
					}
				}
			}
			else
			{
				for(int j = 0; j < pentagoSize * 2; j++)
				{
					if(j < 3)
					{
						pentagoBoardFull[i][j] = pentagoBoardBL[i - 3][j];
						if(pentagoBoardFull[i][j].equals("b") || pentagoBoardFull[i][j].equals("w"))
						{
							fullBoardCounter++;
						}
					}
					else
					{
						pentagoBoardFull[i][j] = pentagoBoardBR[i - 3][j - 3];
						if(pentagoBoardFull[i][j].equals("b") || pentagoBoardFull[i][j].equals("w"))
						{
							fullBoardCounter++;
						}
					}
				}
			}
		}
		
		// If the board is full, return 4 which is no winner
		if(fullBoardCounter == 36)
		{
			return 4;
		}
		
		// Checks horizontal victories
		for(int i = 0; i < pentagoSize * 2; i ++)
		{
			for(int j = 0; j < pentagoSize * 2; j++)
			{
				if(pentagoBoardFull[i][j].equals("b"))
				{
					consecutiveBCounter++;
					consecutiveWCounter = 0;
				}
				else if(pentagoBoardFull[i][j].equals("w"))
				{
					consecutiveWCounter++;
					consecutiveBCounter = 0;
				}
				else
				{
					consecutiveBCounter = 0;
					consecutiveWCounter = 0;
				}
				
				// If there are 5 consecutive B's
				if(consecutiveBCounter == 5)
				{
					// If there are 5 consecutive W's already then it is a draw
					if(winnerFound == 2)
					{
						winnerFound = 3;
					}
					else if(winnerFound == 0)
					{
						winnerFound = 1;
					}
				}
				// If there are 5 consecutive W's
				else if(consecutiveWCounter == 5)
				{
					// If there are 5 consecutive B's already then it is a draw
					if(winnerFound == 1)
					{
						winnerFound = 3;
					}
					else if(winnerFound == 0)
					{
						winnerFound = 2;
					}
				}
			}
			consecutiveWCounter = 0;
			consecutiveBCounter = 0;
		}
		
		// Checks vertical victories
		for(int j = 0; j < pentagoSize * 2; j ++)
		{
			for(int i = 0; i < pentagoSize * 2; i++)
			{
				if(pentagoBoardFull[i][j].equals("b"))
				{
					consecutiveBCounter++;
					consecutiveWCounter = 0;
				}
				else if(pentagoBoardFull[i][j].equals("w"))
				{
					consecutiveWCounter++;
					consecutiveBCounter = 0;
				}
				else
				{
					consecutiveBCounter = 0;
					consecutiveWCounter = 0;
				}
						
				// If there are 5 consecutive B's
				if(consecutiveBCounter == 5)
				{
					// If there are 5 consecutive W's already then it is a draw
					if(winnerFound == 2)
					{
						winnerFound = 3;
					}
					else if(winnerFound == 0)
					{
						winnerFound = 1;
					}
				}
				// If there are 5 consecutive W's
				else if(consecutiveWCounter == 5)
				{
					// If there are 5 consecutive B's already then it is a draw
					if(winnerFound == 1)
					{
						winnerFound = 3;
					}
					else if(winnerFound == 0)
					{
						winnerFound = 2;
					}
				}
			}
			consecutiveWCounter = 0;
			consecutiveBCounter = 0;
		}

		// Checks top left to bottom right diagonal victories, then top right to bottom left diagonal victories
		for(int k = 0; k < pentagoSize * 2; k ++)
		{
			// Check to see where the starting I and J values should be, and if it should be 5 or 6 indices 
			if(k == 0)
			{
				startingI = 1;
				startingJ = 0;
				pentagoResize = 1;
			}
			else if(k == 1)
			{
				startingI = 0;
				startingJ = 0;
				pentagoResize = 0;
			}
			else if(k == 2)
			{
				startingI = 0;
				startingJ = 1;
				pentagoResize = 1;
			}
			else if(k == 3)
			{
				startingI = 1;
				startingJ = 5;
				pentagoResize = 1;
			}
			else if(k == 4)
			{
				startingI = 0;
				startingJ = 5;
				pentagoResize = 0;
			}
			else
			{
				startingI = 0;
				startingJ = 4;
				pentagoResize = 1;
			}
			
			// Checking top left to bottom right diagonal victories
			if(k < 3)
			{
				for(int i = 0; i < (pentagoSize * 2) - pentagoResize; i++)
				{
					if(pentagoBoardFull[startingI + i][startingJ + i].equals("b"))
					{
						consecutiveBCounter++;
						consecutiveWCounter = 0;
					}
					else if(pentagoBoardFull[startingI + i][startingJ + i].equals("w"))
					{
						consecutiveWCounter++;
						consecutiveBCounter = 0;
					}
					else
					{
						consecutiveBCounter = 0;
						consecutiveWCounter = 0;
					}

					// If there are 5 consecutive B's
					if(consecutiveBCounter == 5)
					{
						// If there are 5 consecutive W's already then it is a draw
						if(winnerFound == 2)
						{
							winnerFound = 3;
						}
						else if(winnerFound == 0)
						{
							winnerFound = 1;
						}
					}
					// If there are 5 consecutive W's
					else if(consecutiveWCounter == 5)
					{
						// If there are 5 consecutive B's already then it is a draw
						if(winnerFound == 1)
						{
							winnerFound = 3;
						}
						else if(winnerFound == 0)
						{
							winnerFound = 2;
						}
					}
				}
			}	
			
			// Checking top right to bottom left diagonal victories
			else
			{
				for(int i = 0; i < (pentagoSize * 2) - pentagoResize; i++)
				{
					if(pentagoBoardFull[startingI + i][startingJ - i].equals("b"))
					{
						consecutiveBCounter++;
						consecutiveWCounter = 0;
					}
					else if(pentagoBoardFull[startingI + i][startingJ - i].equals("w"))
					{
						consecutiveWCounter++;
						consecutiveBCounter = 0;
					}
					else
					{
						consecutiveBCounter = 0;
						consecutiveWCounter = 0;
					}

					// If there are 5 consecutive B's
					if(consecutiveBCounter == 5)
					{
						// If there are 5 consecutive W's already then it is a draw
						if(winnerFound == 2)
						{
							winnerFound = 3;
						}
						else if(winnerFound == 0)
						{
							winnerFound = 1;
						}
					}
					// If there are 5 consecutive W's
					else if(consecutiveWCounter == 5)
					{
						// If there are 5 consecutive B's already then it is a draw
						if(winnerFound == 1)
						{
							winnerFound = 3;
						}
						else if(winnerFound == 0)
						{
							winnerFound = 2;
						}
					}
				}
			}	
			consecutiveWCounter = 0;
			consecutiveBCounter = 0;
		}
		return winnerFound;
	}
	
	/**
	 * Function to return the PentagoBoard state as 2d array.
	 * @return The PentagoBoard represented as a 2d array
	 */
	
	public String[][] getPentagoBoardState()
	{
		String[][] currentBoard = new String[pentagoSize * 2][pentagoSize * 2];
		for(int i = 0; i < pentagoSize * 2; i ++)
		{
			if(i < 3)
			{
				for(int j = 0; j < pentagoSize * 2; j++)
				{
					if(j < 3)
					{
						currentBoard[i][j] = pentagoBoardTL[i][j];
					}
					else
					{
						currentBoard[i][j] = pentagoBoardTR[i][j - 3];
					}
				}
			}
			else
			{
				for(int j = 0; j < pentagoSize * 2; j++)
				{
					if(j < 3)
					{
						currentBoard[i][j] = pentagoBoardBL[i - 3][j];
					}
					else
					{
						currentBoard[i][j] = pentagoBoardBR[i - 3][j - 3];
					}
				}
			}
		}
		return currentBoard;
	}
	
	/**
	 * Function to change the associated PentagoBoard state to the 2d array that is sent in.
	 * @param currentBoard The state to change the current PentagoBoard state to
	 */
	
	public void updatePentagoBoardState(String[][] currentBoard)
	{
		for(int i = 0; i < pentagoSize * 2; i ++)
		{
			if(i < 3)
			{
				for(int j = 0; j < pentagoSize * 2; j++)
				{
					if(j < 3)
					{
						pentagoBoardTL[i][j] = currentBoard[i][j];
					}
					else
					{
						pentagoBoardTR[i][j - 3] = currentBoard[i][j];
					}
				}
			}
			else
			{
				for(int j = 0; j < pentagoSize * 2; j++)
				{
					if(j < 3)
					{
						pentagoBoardBL[i - 3][j] = currentBoard[i][j];
					}
					else
					{
						pentagoBoardBR[i - 3][j - 3] = currentBoard[i][j];
					}
				}
			}
		}
	}
	
	/**
	 * Function to print out the Pentago Board.
	 */
	
	public void printPentagoBoard()
	{
		System.out.println("+---+---+");
		for(int i = 0; i < pentagoSize * 2; i ++)
		{
			if(i == 3)
			{
				System.out.println("+---+---+");
			}
			if(i < 3)
			{
				for(int j = 0; j < pentagoSize * 2; j++)
				{
					if(j == 0 || j == 3)
					{
						System.out.print("|");
					}
					if(j < 3)
					{
						System.out.print(pentagoBoardTL[i][j]);
					}
					else
					{
						System.out.print(pentagoBoardTR[i][j - 3]);
					}
				}
				System.out.print("|");
				System.out.println();
			}
			else
			{
				for(int j = 0; j < pentagoSize * 2; j++)
				{
					if(j == 0 || j == 3)
					{
						System.out.print("|");
					}
					if(j < 3)
					{
						System.out.print(pentagoBoardBL[i - 3][j]);
					}
					else
					{
						System.out.print(pentagoBoardBR[i - 3][j - 3]);
					}
				}
				System.out.print("|");
				System.out.println();
			}
		}
		System.out.println("+---+---+");
	}
	
	/**
	 * Function to get the current utility value of the board. Used in Minimax to determine the best/worst moves
	 * @param currentPlayer	The current player of the board to evaluate
	 * @param currentBoard The board to evaluate the utility value of
	 * @return The summation of the total utility value of the board
	 */

	public int getUtility(int currentPlayer, PentagoBoard currentBoard)
	{
		int pentagoResize = 0;
		int startingI = 0;
		int startingJ = 0;
		int utilitySum = 0;
		int consecutiveCounter = 0;
		String currentColor = new String();
		
		// Get the correct color to search for
		if(currentPlayer == 2)
		{
			currentColor = currentBoard.AIColor.toLowerCase();
		}
		else
		{
			if(this.AIColor.equals("B"))
			{
				currentColor = "w";
			}
			else
			{
				currentColor = "b";
			}
		}
		
		// Load individual boards into a full board for easier traversal
		String[][] pentagoBoardFull = currentBoard.getPentagoBoardState();
		
		// Checks horizontal victories
		for(int i = 0; i < pentagoSize * 2; i ++)
		{
			for(int j = 0; j < ((pentagoSize * 2) - 1); j++)
			{
				if(pentagoBoardFull[i][j].equals(currentColor) && pentagoBoardFull[i][j + 1].equals(currentColor))
				{
					utilitySum += consecutiveCounter + 1;
					consecutiveCounter++;
				}
				else
				{
					consecutiveCounter = 0;
				}
			}
		}
		
		// Checks vertical victories
		consecutiveCounter = 0;
		for(int j = 0; j < pentagoSize * 2; j ++)
		{
			for(int i = 0; i < ((pentagoSize * 2) - 1); i++)
			{
				if(pentagoBoardFull[i][j].equals(currentColor.toLowerCase()) && pentagoBoardFull[i + 1][j].equals(currentColor.toLowerCase()))
				{
					utilitySum += consecutiveCounter + 1;
					consecutiveCounter++;
				}
				else
				{
					consecutiveCounter = 0;
				}
			}
		}
		
		// Checks top left to bottom right diagonal victories, then top right to bottom left diagonal victories
		for(int k = 0; k < (pentagoSize * 2); k ++)
		{
			// Check to see where the starting I and J values should be, and if it should be 5 or 6 indices 
			if(k == 0)
			{
				startingI = 1;
				startingJ = 0;
				pentagoResize = 1;
			}
			else if(k == 1)
			{
				startingI = 0;
				startingJ = 0;
				pentagoResize = 0;
			}
			else if(k == 2)
			{
				startingI = 0;
				startingJ = 1;
				pentagoResize = 1;
			}
			else if(k == 3)
			{
				consecutiveCounter = 0;
				startingI = 1;
				startingJ = 5;
				pentagoResize = 1;
			}
			else if(k == 4)
			{
				startingI = 0;
				startingJ = 5;
				pentagoResize = 0;
			}
			else
			{
				startingI = 0;
				startingJ = 4;
				pentagoResize = 1;
			}
			
			// Checking top left to bottom right diagonal victories
			if(k < 3)
			{
				for(int i = 0; i < (((pentagoSize * 2) - pentagoResize) - 1); i++)
				{
					if(pentagoBoardFull[startingI + i][startingJ + i].equals(currentColor) && pentagoBoardFull[startingI + i + 1][startingJ + i + 1].equals(currentColor))
					{
						utilitySum += consecutiveCounter + 1;
						consecutiveCounter++;
					}
					else
					{
						consecutiveCounter = 0;
					}
				}
			}	
			
			// Checking top right to bottom left diagonal victories
			else
			{
				for(int i = 0; i < (((pentagoSize * 2) - pentagoResize) - 1); i++)
				{
					if(pentagoBoardFull[startingI + i][startingJ - i].equals(currentColor) && pentagoBoardFull[startingI + i + 1][startingJ - i - 1].equals(currentColor))
					{
						utilitySum += consecutiveCounter + 1;
						consecutiveCounter++;
					}
					else
					{
						consecutiveCounter = 0;
					}
				}
			}	
		}
		currentBoard.boardUtility = utilitySum;
		return utilitySum;
	}
	
	/**
	 * Function for the AI to use to determine it's best move. This function will recursively load in all associated moves with the current 
	 * Pentago Board, while alternating which player's turn it is. It will then combine the utility values of each child node, whether it be
	 * from the Human or from the AI, and then based on the best available move it will chose to put it's piece in that location. It will also
	 * use Alpha Beta Pruning to chop off unnecessary nodes that do not need to be evaluated to increase runtime.
	 * @param currentBoard	The Pentago Board to be evaluated
	 * @param currentPlayer	The owner of the Pentago Board that is being evaluated
	 * @param depth	The current depth of the Pentago Board
	 * @param alpha	The current alpha value to be used for Alpha Beta Pruning
	 * @param beta The current beta value to be used for Alpha Beta Pruning
	 * @return
	 */
	
	public PentagoBoard minimaxGameMove(PentagoBoard currentBoard, int currentPlayer, int depth, int alpha, int beta)
	{
		PentagoBoard bestBoard = currentBoard;
		
		if(depth == 0)
		{
			return currentBoard;
		}
		
		LinkedList<PentagoBoard> possibleMoves = new LinkedList<PentagoBoard>();
		
		String[][]currentState = this.getPentagoBoardState();
		
		// Traverse through all possible Pentago Boards that can be generated from the current board
		for(int i = 0; i < pentagoSize * 2; i++)
		{
			for(int j = 0; j < pentagoSize * 2; j++)
			{
				// Refresh the current state by reassigning it to the current Pentago Board
				currentState = currentBoard.getPentagoBoardState();
				// If the index is "." then that means it is a valid move
				if(currentState[i][j].equals("."))
				{
					// Assign that index to the corresponding letter of the current player
					if(currentPlayer == 1)
					{
						if(this.AIColor.toLowerCase().equals("b"))
						{
							currentState[i][j] = "w";
						}
						else
						{
							currentState[i][j] = "b";
						}
					}
					else
					{
						currentState[i][j] = currentBoard.AIColor.toLowerCase();
					}
					
					// Loop 4 times, each time taking the current index and rotating each quadrant left and right, for a total of 8 moves
					for(int k = 1; k < 5; k++)
					{
						// Create two near PentagoBoard objects from the current state and current player
						PentagoBoard newBoardL = new PentagoBoard(currentState, currentPlayer);
						PentagoBoard newBoardR = new PentagoBoard(currentState, currentPlayer);
						// Rotate the two boards left and right by getting which quadrant needs to be rotated, and then calling the corresponding function
						newBoardL.rotateBoardLeft(newBoardL.getGameBlock(k));
						newBoardR.rotateBoardRight(newBoardR.getGameBlock(k));
						// Swap the AI color to the alternate player's color
						newBoardL.updateAIColor(currentBoard.AIColor, newBoardL);
						newBoardR.updateAIColor(currentBoard.AIColor, newBoardR);
						// Get the utility of each move so that it can be stored and evaluated later
						newBoardL.getUtility(currentPlayer, newBoardL);
						newBoardR.getUtility(currentPlayer, newBoardR);
						// Add the boards to the list to be evaluated later
						possibleMoves.add(newBoardL);
						possibleMoves.add(newBoardR);
					}
				}
			}
		}
		
		// Evaluate every move that was generated by the loops above
		for(int i = 0; i < possibleMoves.size(); i++)
		{
			// If the current player of the board is the AI
			if(currentPlayer == 2)
			{
				// Remove a board that was generated
				PentagoBoard boardToEvaluate = possibleMoves.remove();
				
				// Recursively call the Minimax function with a decreasing depth, and the alternate player for the board
				bestBoard = minimaxGameMove(boardToEvaluate, 1, (depth - 1), boardToEvaluate.alpha, boardToEvaluate.beta);
				
				// If the board that was returned has a better utility than the current alpha, reassign alpha to that utility
				if(bestBoard.boardUtility > currentBoard.alpha)
				{
					bestBoard.alpha = bestBoard.boardUtility;
					
					// If the depth is lower than the initial board then keep track of the previous move
					if(depth < 3)
					{
						bestBoard.initialState = currentBoard.getPentagoBoardState();
					}
				}
				
				// If alpha and beta cross then break out of evaluating these moves
				if(bestBoard.alpha >= bestBoard.beta)
				{
					break;
				}
			}
			
			// If the current player of the board is the human
			else
			{
				// Remove a board that was generated
				PentagoBoard boardToEvaluate = possibleMoves.remove();
				
				// Recursively call the Minimax function with a decreasing depth, and the alternate player for the board
				bestBoard = minimaxGameMove(boardToEvaluate, 2, (depth - 1), boardToEvaluate.alpha, boardToEvaluate.beta);
				
				// If the board that was returned has a better utility than the current beta, reassign beta to that utility
				if(bestBoard.boardUtility < currentBoard.beta)
				{
					bestBoard.beta = bestBoard.boardUtility;
					
					// If the depth is lower than the initial board then keep track of the previous move
					if(depth < 3)
					{
						bestBoard.initialState = currentBoard.getPentagoBoardState();
					}
				}
				
				// If alpha and beta cross then break out of evaluating these moves
				if(bestBoard.alpha >= bestBoard.beta)
				{
					break;
				}
			}
		}
		
		// Return the PentagoBoard object of the best move that was found
		return bestBoard;
	}
	
	/**
	 * Function to be called by the Human player. Based on the currentMove String it will 
	 * insert the Human player's piece into that location and rotate the desired block.
	 * @param currentMove The desired move that was selected by the Human player
	 * @param playerColor The color of the Human player
	 */
	
	public void humanGameMove(String currentMove, String playerColor)
	{
		// Split the currentMove String into an array to allow for indexing of the values
		String[] currentMoveArray = currentMove.split("");
		int block = Integer.parseInt(currentMoveArray[0]);
		int position = Integer.parseInt(currentMoveArray[2]);
		int blockRotation = Integer.parseInt(currentMoveArray[4]);
		String blockRotationDirection = currentMoveArray[5];
		
		int positionCounter = 1;
		
		//For loop to find the position, and then adds that letter to the corresponding block
		for(int i = 0; i < pentagoSize; i ++)
		{
			for(int j = 0; j < pentagoSize; j++)
			{
				if(positionCounter == position)
				{
					if(block == 1)
					{
						pentagoBoardTL[i][j] = playerColor.toLowerCase();
					}
					else if(block == 2)
					{
						pentagoBoardTR[i][j] = playerColor.toLowerCase();
					}
					else if(block == 3)
					{
						pentagoBoardBL[i][j] = playerColor.toLowerCase();
					}
					else
					{
						pentagoBoardBR[i][j] = playerColor.toLowerCase();
					}
				}
				positionCounter++;
			}
		}
		
		// Based on the number of the block and the letter that was sent, call getGameBlock function on that number 
		// to get the correct block to rotate, and then call the corresponding rotational direction on that quadrant
		if(blockRotationDirection.equals("r") || blockRotationDirection.equals("R"))
		{
			this.rotateBoardRight(this.getGameBlock(blockRotation));
		}
		else if(blockRotationDirection.equals("l") || blockRotationDirection.equals("L"))
		{
			this.rotateBoardLeft(this.getGameBlock(blockRotation));
		}
	}
	
	/**
	 * Function to check if the current move that was defined by the Human player is valid or not
	 * @param currentMove The current move that was defined by the Human player
	 * @return Returns True if the move is valid, and false otherwise
	 */
	
	public boolean isMoveValid(String currentMove)
	{
		// Split the currentMove String into an array to allow for indexing of the values
		String[] currentMoveArray = currentMove.split("");
		int block = Integer.parseInt(currentMoveArray[0]);
		int position = Integer.parseInt(currentMoveArray[2]);
				
		int positionCounter = 1;
		
		//For loop to find the position, and then adds that letter to the corresponding block
		for(int i = 0; i < pentagoSize; i ++)
		{
			for(int j = 0; j < pentagoSize; j++)
			{
				if(positionCounter == position)
				{
					if(block == 1)
					{
						if(pentagoBoardTL[i][j].equals("b") || pentagoBoardTL[i][j].equals("w"))
						{
							return false;
						}
					}
					else if(block == 2)
					{
						if(pentagoBoardTR[i][j].equals("b") || pentagoBoardTR[i][j].equals("w"))
						{
							return false;
						}
					}
					else if(block == 3)
					{
						if(pentagoBoardBL[i][j].equals("b") || pentagoBoardBL[i][j].equals("w"))
						{
							return false;
						}
					}
					else
					{
						if(pentagoBoardBR[i][j].equals("b") || pentagoBoardBR[i][j].equals("w"))
						{
							return false;
						}
					}
				}
				positionCounter++;
			}
		}
		return true;
	}
	
	/**
	 * Function to return the corresponding quadrant based on what value was sent in.
	 * @param currentBlock The value of the quadrant to return (1-4)
	 * @return The 2d String Array quadrant (TL, TR, BL, or BR)
	 */
	
	public String[][] getGameBlock(int currentBlock)
	{
		if(currentBlock == 1)
		{
			return pentagoBoardTL;
		}
		else if(currentBlock == 2)
		{
			return pentagoBoardTR;
		}
		else if(currentBlock == 3)
		{
			return pentagoBoardBL;
		}
		else if(currentBlock == 4)
		{
			return pentagoBoardBR;
		}
		else
		{
			return null;
		}
	}
	
	/**
	 * Function to rotate the board right.
	 * @param currentBoard The board to be rotated
	 * @return Returns the rotated version of the board that was sent in
	 */
	
	public String[][] rotateBoardRight(String[][] currentBoard)
	{
		String tempValue = "";
		for (int i = 0; i < pentagoSize/2; i++)
		{
            for (int j = 0; j < (pentagoSize-2*i-1); j++) 
            {
            	tempValue = currentBoard[i][i + j];
                currentBoard[i][i + j] = currentBoard[pentagoSize - i - j - 1][i];
                currentBoard[pentagoSize - i - j - 1][i] = currentBoard[pentagoSize - i - 1][pentagoSize - i - j - 1];
                currentBoard[pentagoSize - i - 1][pentagoSize - i - j - 1] = currentBoard[i + j][pentagoSize - i - 1];
                currentBoard[i + j][pentagoSize - i - 1] = tempValue;
            }
		}
		return currentBoard;
	}
	
	/**
	 * Function to rotate the board left.
	 * @param currentBoard The board to be rotated
	 * @return Returns the rotated version of the board that was sent in
	 */
	
	public String[][] rotateBoardLeft(String[][] currentBoard)
	{
		String tempValue = "";
		for (int i = 0; i < pentagoSize/2; i ++)
		{
	        for (int j = i; j < (pentagoSize-i-1); j ++)
	        {
	        		tempValue = currentBoard[i][j];
	        		currentBoard[i][j] = currentBoard[j][pentagoSize-i-1];
	        		currentBoard[j][pentagoSize-i-1] = currentBoard[pentagoSize-i-1][pentagoSize-j-1];
	        		currentBoard[pentagoSize-i-1][pentagoSize-j-1] = currentBoard[pentagoSize-j-1][i];
	        		currentBoard[pentagoSize-j-1][i] = tempValue;
	        }
		}
		return currentBoard;
	}
}