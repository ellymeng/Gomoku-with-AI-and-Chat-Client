package gomoku;

import java.awt.Color;
import java.io.IOException;
import java.io.PrintWriter;

public class AIClient extends GameClient{
	
	int choice;
	static int myRow; 
	static int myCol; 
	boolean closed = false;
	String nickname = " ";
	static State root;

	public static void main(String[] args)
	{
		AIClient a = new AIClient();
	}
	
	public AIClient()
	{
		super();
		clientType = 0;
		g = new GameGUI();
		g.mouseOn = false;
		g.gc = this;
		board = g.board;
		root.myClient = this;
		System.out.println("instantiated AIClient");
//		startPlay();
	}
	
	public static int initialBoard()
	{
		int filledSpaces = 0;
		for (int col = 0; col < 15; col++) 
	        for (int row = 0; row < 15; row++)
	        {
		        	if(board[row][col] == 1 || board[row][col] == 0)
		        		filledSpaces++;
	        }
		
		return filledSpaces; 
	}
	
// **************************************************************************************************
// **************************************************************************************************
	public static void startPlay() 
	{
		System.out.println("**********************new move***************************************");
		System.out.println("inside startPlay");
		System.out.println("isBlack = " + isBlack); 
		System.out.println("initialBoard returns " + initialBoard()); // ERROR: ALWAYS 2
		if (isBlack && initialBoard() == 0) // make first move of game
		{
			int row;
			int col;
			row = col = 7;
			System.out.println("making first move at col = 7, row = 7");
			root = new State(row, col, true); // isMaximizer = true
			root.newMove[0] = row;
			root.newMove[1] = col;
//			root.myClient = this;
			System.out.println("instantiated root state");
			root.alpha = -9999;
			root.beta = 9999;
			root.myColor = 1;
			board[row][col] = root.myColor;
			g.board = board;
//			g.AIrow = row;
//			g.AIcol = col;
			try {
				sendMove(row, col);
			} catch (IOException e) { e.printStackTrace(); }
		} 
		else if (initialBoard() == 1) // place first piece on right/left of opponent's
		{
			for (int col = 0; col < 15; col++) 
			{ 
		        for (int row = 0; row < 15; row++)
		        {
			        	if(board[row][col] == root.opponentColor())
		            {
		            		if (col+1 < 15 && board[row][col+1] == -1)
		            		{
		            			
//		            			System.out.println("placing white piece beside black piece");
		            			col++;
		            			board[row][col] = root.myColor;
		            			g.board = board;
		            			root = new State(row,col, true);
		            			root.newMove[0] = row;
		            			root.newMove[1] = col;
		            			root.myColor = 0;
		            			System.out.println("making first move at " + row + ", " + col);
		            			try {
		            				sendMove(row, col);
		            			} catch (IOException e) { e.printStackTrace(); }
		            		}	
		            		else if (col-1 >=0 && board[row][col-1] == -1)
		            		{
//		            			System.out.println("placing white piece beside black piece");
		            			col--;
		            			board[row][col] = root.myColor;
//		            			g.AIrow = row;
//		            			g.AIcol = col-1;
		            			g.board = board;
		            			root = new State(row, col, true);
		            			root.newMove[0] = row;
		            			root.newMove[1] = col;
		            			root.myColor = 0;
		            			System.out.println("making first move at " + row + ", " + col);
		            			try {
		            				sendMove(row, col);
		            			} catch (IOException e) { e.printStackTrace(); }
		            		}	
		            }
			    }
			}
		}
		else 
		{
//			System.out.println("inside minimax-substitute");
//			if (!root.isTerminalState(root.myColor))
//			{
				State nextMove = root;

				while(board[myRow][myCol] != -1 && !root.isTerminalState(root.myColor))
				{
				nextMove = getBestMove(nextMove);
				myRow = nextMove.newMove[0];
				myCol = nextMove.newMove[1];
				}
				
//				System.out.println("generating next best move: " + myRow + ", " + myCol);
				board[myRow][myCol] = root.myColor;
				g.board = board;
				System.out.println("Heuristic for this is " + nextMove.h);
				
				try { 
					sendMove(myRow, myCol); } 
				catch (IOException e) { 
					e.printStackTrace(); }
//			}
			
			
//			System.out.println("about to execute minimax");
//			double goodnessScore = minimax(root, -9999, 9999); // state, alpha, beta
//			for (State child : root.possibleMoves)
//			{
//				if (child.h == goodnessScore)
//				{
////					g.AIrow = child.newMove[0];
////					g.AIcol = child.newMove[1];
//					row = child.newMove[0];
//					col = child.newMove[1];
//					board[row][col] = child.myColor; 
//					g.board = board;
//					try {
//						sendMove(row, col);
//					} catch (IOException e) { e.printStackTrace(); }
//				}
//			}
			
		}
		g.repaint();
	}

	// ***********************************************************************
	// *********************** USE IF MINIMAX FAILS **************************
	// ***********************************************************************
			
	public static State getBestMove(State state) {

		generatePossibleMoves(state); // already sets isMaximizer, color, and nextMove values 

		if(state.possibleMoves.size() == 0)
			return null;

		State bestState = state.possibleMoves.get(0);
		double bestScore = getMoveScore(state.possibleMoves.remove(0)); // remove() pops first item & shifts the rest left one index
			
		for(State successor : state.possibleMoves) {
				
			double score = getMoveScore(successor);
				System.out.println("scores here is " + score);
			if(score > bestScore) {
				bestState = successor;
				bestScore = score;
			}
		}
		System.out.println("Final score here is " + bestScore);

		bestState.h = bestScore;
		return bestState;
	}

	// heuristic 
	public static double getMoveScore(State state) 
	{
		double choice = 0;
		if (victory(state.myColor)) 
			choice = 1;
		else if (victory(state.opponentColor()))
			choice = -1;
		else if (state.myClient.tie())
			choice = 0.0;
		else if (state.myClient.fourChain(state.opponentColor()))
		{
//			System.out.println("4444444444444444444444444444444");
			for (Chain chain: state.myClient.oppChainCoords) 
			{
				if (chain.length == 4) 
				{
					// place piece to seal off opp's 4-row: 
					if (chain.isVertical()) 
					{
//check if out of bounds:
						if (chain.start.get(0) > 0 && chain.end.get(0) < 14) 
						{
							if (state.myClient.board[chain.start.get(0)-1][chain.start.get(1)] == state.myColor
									&& state.myClient.board[chain.end.get(0)+1][chain.end.get(1)] == state.myColor) 
							{
								choice = 0.9;
							}
						}
					} 
					else // horizontal
					{
//check if out of bounds:
						if (chain.start.get(1) > 0 && chain.end.get(1) < 14) 
						{
							if (state.myClient.board[chain.start.get(0)][chain.start.get(1)-1] == state.myColor
									&& state.myClient.board[chain.end.get(0)][chain.end.get(1)+1] == state.myColor) 
							{
								choice = 0.9;
							}
						}
					}	
				}	
			}
			if(choice == 0)
				choice = .9;
		}
		else if (state.myClient.threeChain(state.opponentColor()))
		{
//			System.out.println("333333333333333333");

			for (Chain chain: state.myClient.oppChainCoords) 
			{
				if (chain.length == 3) 
				{
					// place piece to block opponent's VERTICAL 3-chain: 
					if (chain.isVertical()) 
					{
						// place piece between 3-chain and 1-chain --> prevent opp from winning 
//check if out of bounds:
						if (chain.start.get(0) > 1) 
						{
							if ((state.myClient.board[chain.start.get(0)-2][chain.start.get(1)] == state.opponentColor()
									&& state.myClient.board[chain.start.get(0)-1][chain.start.get(1)] == state.myColor))
							{
								choice = 0.9;
							}
						}
//check if out of bounds:
						if (chain.end.get(0) < 13) 
						{
							if((state.myClient.board[chain.end.get(0)+2][chain.end.get(1)] == state.opponentColor()
									&& state.myClient.board[chain.end.get(0)+1][chain.end.get(1)] == state.myColor))
							{
								choice = 0.9;
							}
						}
						
						// place piece to block unsurrounded 3-chain --> prevent check mate
//check if out of bounds:
						if (chain.start.get(0) > 0 && chain.start.get(0) < 12) 
						{
							if ((state.myClient.board[chain.start.get(0)-1][chain.start.get(1)] == -1
									&& state.myClient.board[chain.start.get(0)+3][chain.start.get(1)] == state.myColor))
								{
									choice = 0.9;
								}
						}
//check if out of bounds:
						if (chain.end.get(0) < 14 && chain.end.get(0) > 2) 
						{
							if (state.myClient.board[chain.end.get(0)+1][chain.end.get(1)] == -1
								&& state.myClient.board[chain.end.get(0)-3][chain.end.get(1)] == state.myColor)
							{
								choice = 0.9;
							}
						}
						
						// not urgent, opp 3-chain is blocked at one end so check mate not imminent
//check if out of bounds:
						if (chain.start.get(1) > 0 && chain.start.get(1) < 12) 
						{
							if ((state.myClient.board[chain.start.get(0)-1][chain.start.get(1)] == state.myColor
									&& state.myClient.board[chain.start.get(0)+3][chain.start.get(1)] == state.myColor))
								{
									choice = 0.5;
								}
						}
//check if out of bounds:
						if (chain.end.get(1) < 14 && chain.end.get(1) > 2) 
						{
							if (state.myClient.board[chain.end.get(0)+1][chain.end.get(1)] == state.myColor
								&& state.myClient.board[chain.end.get(0)-3][chain.end.get(1)] == state.myColor)
							{
								choice = 0.5;
							}
						}
						
					} 
					else // horizontal
					{
						// place piece between 3-chain and 1-chain --> prevent opp from winning 
//check if out of bounds:
						if (chain.start.get(1) > 1) 
						{
							if ((state.myClient.board[chain.start.get(0)][chain.start.get(1)-2] == state.opponentColor()
									&& state.myClient.board[chain.start.get(0)][chain.start.get(1)-1] == state.myColor))
							{
								choice = 0.9;
							}
						}
//check if out of bounds:
						if (chain.end.get(1) < 13) 
						{
							if((state.myClient.board[chain.end.get(0)][chain.end.get(1)+2] == state.opponentColor()
									&& state.myClient.board[chain.end.get(0)][chain.end.get(1)+1] == state.myColor))
							{
								choice = 0.9;
							}
						}
						
						// place piece to block unsurrounded 3-chain --> prevent check mate
//check if out of bounds:
						if (chain.start.get(1) > 0 && chain.start.get(1) < 12) 
						{
						if ((state.myClient.board[chain.start.get(0)][chain.start.get(1)-1] == -1
								&& state.myClient.board[chain.start.get(0)][chain.start.get(1)+3] == state.myColor))
							{
								choice = 0.9;
							}
						}
//check if out of bounds:
						if (chain.end.get(1) < 14 && chain.end.get(1) > 2) 
						{
							if((state.myClient.board[chain.end.get(0)][chain.end.get(1)+1] == -1
									&& state.myClient.board[chain.end.get(0)][chain.end.get(1)-3] == state.myColor))
							{
								choice = 0.9;
							}
						}
						
						// not urgent, opp 3-chain is blocked at one end so check mate not imminent
//check if out of bounds:
						if (chain.start.get(1) > 0 && chain.start.get(1) < 12) 
						{
							if ((state.myClient.board[chain.start.get(0)][chain.start.get(1)-1] == state.myColor
									&& state.myClient.board[chain.start.get(0)][chain.start.get(1)+3] == state.myColor))
								{
									choice = 0.6;
								}	
						}
						//check if out of bounds:
						if (chain.end.get(1) < 14 && chain.end.get(1) > 2) 
						{		
							if((state.myClient.board[chain.end.get(0)][chain.end.get(1)+1] == state.myColor
									&& state.myClient.board[chain.end.get(0)][chain.end.get(1)-3] == state.myColor))
							{
								choice = 0.6;
							}
						}
					}	
				}	
			}
//			if(choice ==0)
//				choice = .9;
		}
		else if (state.myClient.twoChain(state.opponentColor()))
		{
//			System.out.println("222222222222222222222222222222");

			for (Chain chain: state.myClient.oppChainCoords) 
			{
				if (chain.length == 2)
				{
					if (chain.isVertical())
					{
//check if out of bounds:
						if (chain.start.get(0) > 2) 
						{
						// place piece in middle of two opp 2-chains --> prevent opp from winning
							if((state.myClient.board[chain.start.get(0)-3][chain.start.get(1)] == state.opponentColor()
									&& state.myClient.board[chain.start.get(0)-2][chain.start.get(1)] == state.opponentColor()
									&& state.myClient.board[chain.start.get(0)-1][chain.start.get(1)] == myColor)) 
							{
								choice = 0.9;
							}
						}	
//check if out of bounds:
						if (chain.end.get(0) < 12) 
						{
							if((state.myClient.board[chain.end.get(0)+3][chain.end.get(1)] == state.opponentColor()
									&& state.myClient.board[chain.end.get(0)+2][chain.end.get(1)] == state.opponentColor()
									&& state.myClient.board[chain.end.get(0)+1][chain.end.get(1)] == myColor))
							{
								choice = 0.9;
							}
						}
//check if out of bounds:
						if (chain.start.get(0) > 2 && chain.start.get(0) < 13) 
						{
							// place piece between unsurrounded 2-chain _ 1-chain --> prevent check mate
							if ((state.myClient.board[chain.start.get(0)-3][chain.start.get(1)] == -1
									&& state.myClient.board[chain.start.get(0)-2][chain.start.get(1)] == state.opponentColor()
									&& state.myClient.board[chain.start.get(0)-1][chain.start.get(1)] == myColor
									&& state.myClient.board[chain.start.get(0)+2][chain.start.get(1)] == -1))
							{
								choice = 0.9;
							}
						}
//check if out of bounds:
						if (chain.end.get(0) > 1 && chain.end.get(0) < 12) 
						{
							if ((state.myClient.board[chain.end.get(0)+3][chain.end.get(1)] == -1
									&& state.myClient.board[chain.end.get(0)+2][chain.end.get(1)] == state.opponentColor()
									&& state.myClient.board[chain.end.get(0)+1][chain.end.get(1)] == myColor)
									&& state.myClient.board[chain.end.get(0)-2][chain.end.get(1)] == -1)
							{
								choice = 0.9;
							}
						}
//check if out of bounds:										
						if (chain.start.get(0) > 2 && chain.start.get(0) < 13) 
						{
						// place piece to seal off blocked 2-chain _ 1-chain, no risk of check mate
							if ((state.myClient.board[chain.start.get(0)-3][chain.start.get(1)] == myColor
									&& state.myClient.board[chain.start.get(0)-2][chain.start.get(1)] == state.opponentColor()
									&& state.myClient.board[chain.start.get(0)-1][chain.start.get(1)] == -1
									&& state.myClient.board[chain.start.get(0)+2][chain.start.get(1)] == myColor))
							{
								choice = 0.5;
							}
						}
//check if out of bounds:										
						if (chain.end.get(0) > 1 && chain.end.get(0) < 12) 
						{		
							if((state.myClient.board[chain.end.get(0)+3][chain.end.get(1)] == myColor
								&& state.myClient.board[chain.end.get(0)+2][chain.end.get(1)] == state.opponentColor()
								&& state.myClient.board[chain.end.get(0)+1][chain.end.get(1)] == -1
								&& state.myClient.board[chain.end.get(0)-2][chain.end.get(1)] == myColor))
							{
								choice = 0.5;
							}
					}
					}	
					else //horizontal
					{
//check if out of bounds:										
						if (chain.start.get(1) > 2) 
						{												
						// place piece in middle of two opp 2-chains --> prevent opp from winning
							if((state.myClient.board[chain.start.get(0)][chain.start.get(1)-3] == state.opponentColor()
									&& state.myClient.board[chain.start.get(0)][chain.start.get(1)-2] == state.opponentColor()
									&& state.myClient.board[chain.start.get(0)][chain.start.get(1)-1] == myColor))  
							{
								choice = 0.9;
							}
						}
//check if out of bounds:										
						if (chain.end.get(1) < 12) 
						{
							if((state.myClient.board[chain.end.get(0)][chain.end.get(1)+3] == state.opponentColor()
								&& state.myClient.board[chain.end.get(0)][chain.end.get(1)+2] == state.opponentColor()
								&& state.myClient.board[chain.end.get(0)][chain.end.get(1)+1] == myColor))
							{
								choice = 0.9;
							}
						}
						
						// place piece between unsurrounded 2-chain _ 1-chain --> prevent check mate
//check if out of bounds:								
						if (chain.start.get(1) > 2 && chain.start.get(1) < 13) 
						{
							if ((state.myClient.board[chain.start.get(0)][chain.start.get(1)-3] == -1
									&& state.myClient.board[chain.start.get(0)][chain.start.get(1)-2] == state.opponentColor()
									&& state.myClient.board[chain.start.get(0)][chain.start.get(1)-1] == myColor
									&& state.myClient.board[chain.start.get(0)][chain.start.get(1)+2] == -1))
							{
								choice = 0.9;
							}
						}
						// check if out of bounds:
						if (chain.end.get(1) < 12 && chain.end.get(1) > 1) 
						{
							if((state.myClient.board[chain.end.get(0)][chain.end.get(1)+3] == -1
								&& state.myClient.board[chain.end.get(0)][chain.end.get(1)+2] == state.opponentColor()
								&& state.myClient.board[chain.end.get(0)][chain.end.get(1)+1] == myColor
								&& state.myClient.board[chain.end.get(0)][chain.end.get(1)-2] == -1))
							{
								choice = 0.9;
							}
						}
//check if out of bounds:								
						if (chain.start.get(1) > 2 && chain.start.get(1) < 13) 
						{
						// place piece to seal off blocked 2-chain _ 1-chain, no risk of check mate
							if ((state.myClient.board[chain.start.get(0)][chain.start.get(1)-3] == myColor
									&& state.myClient.board[chain.start.get(0)][chain.start.get(1)-2] == state.opponentColor()
									&& state.myClient.board[chain.start.get(0)][chain.start.get(1)-1] == -1
									&& state.myClient.board[chain.start.get(0)][chain.start.get(1)+2] == myColor))
							{
								choice = 0.5;
							}
						}
						// check if out of bounds:								
						if (chain.end.get(1) < 12 && chain.end.get(1) > 1)
							if((state.myClient.board[chain.end.get(0)][chain.end.get(1)+3] == myColor
								&& state.myClient.board[chain.end.get(0)][chain.end.get(1)+2] == state.opponentColor()
								&& state.myClient.board[chain.end.get(0)][chain.end.get(1)+1] == -1
								&& state.myClient.board[chain.end.get(0)][chain.end.get(1)-2] == myColor))
						{
							choice = 0.5;
						}
					}
			}
				
			}
//			if(choice ==0)
//				choice = .9;
		}
		// *********** if no extreme cases present, grow your own chain ***************
		// a totally unblocked 4-chain is more favorable than a 4-chain with one side blocked

		else if (state.myClient.fourChain(state.myColor)) {
			choice = 0.4;		
		}
		else if (state.myClient.threeChain(state.myColor)) {
			choice = 0.3;
		}
		else if (state.myClient.twoChain(state.myColor)) {
			choice = 0.2;
		}
		else { 
			choice = 0.1; // does not grow row or prevent opponent from gaining advantage	
		}
//		return
//			(getLongestPlayerRow(state) -
//			getLongestOpponentRow(state)) * .2;
		return choice;
	}

	public static int getLongestPlayerRow(State state) 
	{
		int rowLength = 0;
		
		if (fourChain(state.myColor))
			rowLength = 4;
		else if (threeChain(state.myColor))
			rowLength = 3;
		else if (twoChain(state.myColor))
			rowLength = 2;
		else
			rowLength = 1;
		
		return rowLength;
	}

	public static int getLongestOpponentRow(State state) 
		{
			int rowLength = 0;
			
			if (fourChain(state.opponentColor()))
				rowLength = 4;
			else if (threeChain(state.opponentColor()))
				rowLength = 3;
			else if (twoChain(state.opponentColor()))
				rowLength = 2;
			else
				rowLength = 1;
			
			return rowLength;	
		}	
	
	// **************************************************************************************************
	// **************************************************************************************************

	// generate new child state for each empty adjacent space on board:
	public static void generatePossibleMoves (State state) 
	{
//		System.out.println("inside generatePossibleMoves()");
		for (int i = 0; i < 15; i++)
		{
			for (int j = 0; j < 15; j++)
			{
				if (board[i][j] == state.myColor)
				{
//					System.out.println("found a piece to grow");
					for (int ii = myRow-1; ii < myRow+2; ii++)
					{
						for (int jj = myCol-1; jj < myCol+2; jj++)
							if (ii-1 >= 0 && ii+1 <= 14 && jj-1 >= 0 && jj+1 <= 14) // discount spaces outside board boundaries
							{
								if (board[ii][jj] == -1)
								{
//									System.out.println("generated new child state");
									boolean isChildMaximizer = true;
									if (state.isMaximizer)
										isChildMaximizer = false;
									State child = new State(ii,jj,isChildMaximizer);
									if (isBlack)
										state.myColor = 1;
									else
										state.myColor = 0;
									child.newMove[0] = ii;
									child.newMove[1] = jj;
//									System.out.println("setting child state's heuristic");
//									child.setHeuristic(); // DOES THE HEAVY-LIFTING
									child.myClient.getMoveScore(child);
//									System.out.println("appending child state to list of possible moves");
									state.possibleMoves.add(child);
								}
							}
					}
				}
			}
		}
	}
	
	// with alpha-beta pruning
	public static double minimax (State state, double alpha, double beta) {
		System.out.println("inside MINIMAX");
		 if (state.isTerminalState(state.myColor)) // win, lose, tie
		 {
			 System.out.println("reached terminal state, returning " + state.h);
			 return state.h;
		 }
		else if (state.nextAgent() == 0) // 0 = minimizer
		{
//			System.out.println("executing minValue");
			return minValue(state, alpha, beta); // should return 3
		} 
		else // next agent = 1 =  maximizer
		{
//			System.out.println("executing maxValue");
			return maxValue(state, alpha, beta);
		}
	}
		
	public static double maxValue (State state, double alpha, double beta) 
	{
		System.out.println("inside maxValue");
		state.h = -999999999;
		generatePossibleMoves(state);
		System.out.println("possible moves: " + state.possibleMoves.size());
		for (State successor: state.possibleMoves) 
		{
			state.h = Math.max(state.h,minimax(successor,alpha,beta));
			System.out.println("check pruning");
			if(beta <= state.h) // ***PRUNE
				return state.h;
			alpha = Math.max(state.h,alpha);
		}
		return state.h;
	}
	
	public static double minValue (State state, double alpha, double beta) 
	{
//		System.out.println("inside minValue");
		state.h = 999999999;
		generatePossibleMoves(state);
//		System.out.println("possible moves: " + state.possibleMoves.size());
		for (State successor: state.possibleMoves) 
		{
			state.h = Math.min(state.h,minimax(successor,alpha,beta));
//			System.out.println("check pruning");
			if (alpha >= state.h) // ***PRUNE 
				return state.h;
			beta = Math.min(state.h,beta);
		}
		return state.h;
	}	

	}


