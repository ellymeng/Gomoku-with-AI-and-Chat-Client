package gomoku;

import java.util.ArrayList;

public class State {
	
	static AIClient myClient; // need color and current board
	static AIClient oppClient; // set when paired by server  
	static ArrayList<State> possibleMoves = new ArrayList<State>();
	
//	static int depth;
	static double h; //heuristic
	static boolean isMaximizer;
	static double alpha;
	static double beta;
	static double bestValue;
	static int[] newMove = new int[2];
	static int myColor = convertColorInt();
	
	public State() {}
	
	// adds new piece to current board 
	public State(int row, int col, boolean isMaxi) 
	{
		newMove[0] = row;
		newMove[1] = col;
		isMaximizer = isMaxi;
	}
	
	// 0 = minimizer, 1 = maximizer
	public static int nextAgent() { 
		if (isMaximizer)
			return 0; 
		else
			return 1;
	}
	
	public static boolean isTerminalState(int myColor)
	{
		if (myClient.victory(myColor) || myClient.victory(opponentColor()) || myClient.tie())
		{
			return true;
		} else {
			return false;
		}
	}
	
	public static int convertColorInt() 
	{
		if(myClient.isBlack) { 
			return 1; } 
		else { 
			return 0; }
	}
	
	public static int opponentColor()
	{
		if(myColor == 1) {
			return 0;
		} else {
			return 1;
		}
	}
	
	public static void setHeuristic () 
		{
			myClient.setCoords(); 
			
			for (int row = 0; row < 15; row++)
				for (int col = 0; col < 15; col++)	
						
						if (myClient.victory(myColor)) 
						{
							h = 1.0;
						}
						else if (myClient.victory(opponentColor()))
						{
							h = -1.0;
						}
						else if (myClient.tie())
						{
							h = 0.0;
						}
						else if (myClient.fourChain( opponentColor()))
						{
//							System.out.println("4444444444444444444444444444444");
							for (Chain chain:  myClient.oppChainCoords) 
							{
								if (chain.length == 4) 
								{
									// place piece to seal off opp's 4-row: 
									if (chain.isVertical()) 
									{
				//check if out of bounds:
										if (chain.start.get(0) > 0 && chain.end.get(0) < 14) 
										{
											if ( myClient.board[chain.start.get(0)-1][chain.start.get(1)] ==  myColor
													&&  myClient.board[chain.end.get(0)+1][chain.end.get(1)] ==  myColor) 
											{
												h= 0.9;
											}
										}
									} 
									else // horizontal
									{
				//check if out of bounds:
										if (chain.start.get(1) > 0 && chain.end.get(1) < 14) 
										{
											if ( myClient.board[chain.start.get(0)][chain.start.get(1)-1] ==  myColor
													&&  myClient.board[chain.end.get(0)][chain.end.get(1)+1] ==  myColor) 
											{
												h= 0.9;
											}
										}
									}	
								}	
							}
							if(h== 0)
								h= .9;
						}
						else if ( myClient.threeChain( opponentColor()))
						{
//							System.out.println("333333333333333333");

							for (Chain chain:  myClient.oppChainCoords) 
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
											if (( myClient.board[chain.start.get(0)-2][chain.start.get(1)] ==  opponentColor()
													&&  myClient.board[chain.start.get(0)-1][chain.start.get(1)] ==  myColor))
											{
												h= 0.9;
											}
										}
				//check if out of bounds:
										if (chain.end.get(0) < 13) 
										{
											if(( myClient.board[chain.end.get(0)+2][chain.end.get(1)] ==  opponentColor()
													&&  myClient.board[chain.end.get(0)+1][chain.end.get(1)] ==  myColor))
											{
												h= 0.9;
											}
										}
										
										// place piece to block unsurrounded 3-chain --> prevent check mate
				//check if out of bounds:
										if (chain.start.get(0) > 0 && chain.start.get(0) < 12) 
										{
											if (( myClient.board[chain.start.get(0)-1][chain.start.get(1)] == -1
													&&  myClient.board[chain.start.get(0)+3][chain.start.get(1)] ==  myColor))
												{
													h= 0.9;
												}
										}
				//check if out of bounds:
										if (chain.end.get(0) < 14 && chain.end.get(0) > 2) 
										{
											if ( myClient.board[chain.end.get(0)+1][chain.end.get(1)] == -1
												&&  myClient.board[chain.end.get(0)-3][chain.end.get(1)] ==  myColor)
											{
												h= 0.9;
											}
										}
										
										// not urgent, opp 3-chain is blocked at one end so check mate not imminent
				//check if out of bounds:
										if (chain.start.get(1) > 0 && chain.start.get(1) < 12) 
										{
											if (( myClient.board[chain.start.get(0)-1][chain.start.get(1)] ==  myColor
													&&  myClient.board[chain.start.get(0)+3][chain.start.get(1)] ==  myColor))
												{
													h= 0.5;
												}
										}
				//check if out of bounds:
										if (chain.end.get(1) < 14 && chain.end.get(1) > 2) 
										{
											if ( myClient.board[chain.end.get(0)+1][chain.end.get(1)] ==  myColor
												&&  myClient.board[chain.end.get(0)-3][chain.end.get(1)] ==  myColor)
											{
												h= 0.5;
											}
										}
										
									} 
									else // horizontal
									{
										// place piece between 3-chain and 1-chain --> prevent opp from winning 
				//check if out of bounds:
										if (chain.start.get(1) > 1) 
										{
											if (( myClient.board[chain.start.get(0)][chain.start.get(1)-2] ==  opponentColor()
													&&  myClient.board[chain.start.get(0)][chain.start.get(1)-1] ==  myColor))
											{
												h= 0.9;
											}
										}
				//check if out of bounds:
										if (chain.end.get(1) < 13) 
										{
											if(( myClient.board[chain.end.get(0)][chain.end.get(1)+2] ==  opponentColor()
													&&  myClient.board[chain.end.get(0)][chain.end.get(1)+1] ==  myColor))
											{
												h= 0.9;
											}
										}
										
										// place piece to block unsurrounded 3-chain --> prevent check mate
				//check if out of bounds:
										if (chain.start.get(1) > 0 && chain.start.get(1) < 12) 
										{
										if (( myClient.board[chain.start.get(0)][chain.start.get(1)-1] == -1
												&&  myClient.board[chain.start.get(0)][chain.start.get(1)+3] ==  myColor))
											{
												h= 0.9;
											}
										}
				//check if out of bounds:
										if (chain.end.get(1) < 14 && chain.end.get(1) > 2) 
										{
											if(( myClient.board[chain.end.get(0)][chain.end.get(1)+1] == -1
													&&  myClient.board[chain.end.get(0)][chain.end.get(1)-3] ==  myColor))
											{
												h= 0.9;
											}
										}
										
										// not urgent, opp 3-chain is blocked at one end so check mate not imminent
				//check if out of bounds:
										if (chain.start.get(1) > 0 && chain.start.get(1) < 12) 
										{
											if (( myClient.board[chain.start.get(0)][chain.start.get(1)-1] ==  myColor
													&&  myClient.board[chain.start.get(0)][chain.start.get(1)+3] ==  myColor))
												{
													h= 0.6;
												}	
										}
										//check if out of bounds:
										if (chain.end.get(1) < 14 && chain.end.get(1) > 2) 
										{		
											if(( myClient.board[chain.end.get(0)][chain.end.get(1)+1] ==  myColor
													&&  myClient.board[chain.end.get(0)][chain.end.get(1)-3] ==  myColor))
											{
												h= 0.6;
											}
										}
									}	
								}	
							}
//							if(h==0)
//								h= .9;
						}
						else if ( myClient.twoChain( opponentColor()))
						{
//							System.out.println("222222222222222222222222222222");

							for (Chain chain:  myClient.oppChainCoords) 
							{
								if (chain.length == 2)
								{
									if (chain.isVertical())
									{
				//check if out of bounds:
										if (chain.start.get(0) > 2) 
										{
										// place piece in middle of two opp 2-chains --> prevent opp from winning
											if(( myClient.board[chain.start.get(0)-3][chain.start.get(1)] ==  opponentColor()
													&&  myClient.board[chain.start.get(0)-2][chain.start.get(1)] ==  opponentColor()
													&&  myClient.board[chain.start.get(0)-1][chain.start.get(1)] == myColor)) 
											{
												h= 0.9;
											}
										}	
				//check if out of bounds:
										if (chain.end.get(0) < 12) 
										{
											if(( myClient.board[chain.end.get(0)+3][chain.end.get(1)] ==  opponentColor()
													&&  myClient.board[chain.end.get(0)+2][chain.end.get(1)] ==  opponentColor()
													&&  myClient.board[chain.end.get(0)+1][chain.end.get(1)] == myColor))
											{
												h= 0.9;
											}
										}
				//check if out of bounds:
										if (chain.start.get(0) > 2 && chain.start.get(0) < 13) 
										{
											// place piece between unsurrounded 2-chain _ 1-chain --> prevent check mate
											if (( myClient.board[chain.start.get(0)-3][chain.start.get(1)] == -1
													&&  myClient.board[chain.start.get(0)-2][chain.start.get(1)] ==  opponentColor()
													&&  myClient.board[chain.start.get(0)-1][chain.start.get(1)] == myColor
													&&  myClient.board[chain.start.get(0)+2][chain.start.get(1)] == -1))
											{
												h= 0.9;
											}
										}
				//check if out of bounds:
										if (chain.end.get(0) > 1 && chain.end.get(0) < 12) 
										{
											if (( myClient.board[chain.end.get(0)+3][chain.end.get(1)] == -1
													&&  myClient.board[chain.end.get(0)+2][chain.end.get(1)] ==  opponentColor()
													&&  myClient.board[chain.end.get(0)+1][chain.end.get(1)] == myColor)
													&&  myClient.board[chain.end.get(0)-2][chain.end.get(1)] == -1)
											{
												h= 0.9;
											}
										}
				//check if out of bounds:										
										if (chain.start.get(0) > 2 && chain.start.get(0) < 13) 
										{
										// place piece to seal off blocked 2-chain _ 1-chain, no risk of check mate
											if (( myClient.board[chain.start.get(0)-3][chain.start.get(1)] == myColor
													&&  myClient.board[chain.start.get(0)-2][chain.start.get(1)] ==  opponentColor()
													&&  myClient.board[chain.start.get(0)-1][chain.start.get(1)] == -1
													&&  myClient.board[chain.start.get(0)+2][chain.start.get(1)] == myColor))
											{
												h= 0.5;
											}
										}
				//check if out of bounds:										
										if (chain.end.get(0) > 1 && chain.end.get(0) < 12) 
										{		
											if(( myClient.board[chain.end.get(0)+3][chain.end.get(1)] == myColor
												&&  myClient.board[chain.end.get(0)+2][chain.end.get(1)] ==  opponentColor()
												&&  myClient.board[chain.end.get(0)+1][chain.end.get(1)] == -1
												&&  myClient.board[chain.end.get(0)-2][chain.end.get(1)] == myColor))
											{
												h= 0.5;
											}
									}
									}	
									else //horizontal
									{
				//check if out of bounds:										
										if (chain.start.get(1) > 2) 
										{												
										// place piece in middle of two opp 2-chains --> prevent opp from winning
											if(( myClient.board[chain.start.get(0)][chain.start.get(1)-3] ==  opponentColor()
													&&  myClient.board[chain.start.get(0)][chain.start.get(1)-2] ==  opponentColor()
													&&  myClient.board[chain.start.get(0)][chain.start.get(1)-1] == myColor))  
											{
												h= 0.9;
											}
										}
				//check if out of bounds:										
										if (chain.end.get(1) < 12) 
										{
											if(( myClient.board[chain.end.get(0)][chain.end.get(1)+3] ==  opponentColor()
												&&  myClient.board[chain.end.get(0)][chain.end.get(1)+2] ==  opponentColor()
												&&  myClient.board[chain.end.get(0)][chain.end.get(1)+1] == myColor))
											{
												h= 0.9;
											}
										}
										
										// place piece between unsurrounded 2-chain _ 1-chain --> prevent check mate
				//check if out of bounds:								
										if (chain.start.get(1) > 2 && chain.start.get(1) < 13) 
										{
											if (( myClient.board[chain.start.get(0)][chain.start.get(1)-3] == -1
													&&  myClient.board[chain.start.get(0)][chain.start.get(1)-2] ==  opponentColor()
													&&  myClient.board[chain.start.get(0)][chain.start.get(1)-1] == myColor
													&&  myClient.board[chain.start.get(0)][chain.start.get(1)+2] == -1))
											{
												h= 0.9;
											}
										}
										// check if out of bounds:
										if (chain.end.get(1) < 12 && chain.end.get(1) > 1) 
										{
											if(( myClient.board[chain.end.get(0)][chain.end.get(1)+3] == -1
												&&  myClient.board[chain.end.get(0)][chain.end.get(1)+2] ==  opponentColor()
												&&  myClient.board[chain.end.get(0)][chain.end.get(1)+1] == myColor
												&&  myClient.board[chain.end.get(0)][chain.end.get(1)-2] == -1))
											{
												h= 0.9;
											}
										}
				//check if out of bounds:								
										if (chain.start.get(1) > 2 && chain.start.get(1) < 13) 
										{
										// place piece to seal off blocked 2-chain _ 1-chain, no risk of check mate
											if (( myClient.board[chain.start.get(0)][chain.start.get(1)-3] == myColor
													&&  myClient.board[chain.start.get(0)][chain.start.get(1)-2] ==  opponentColor()
													&&  myClient.board[chain.start.get(0)][chain.start.get(1)-1] == -1
													&&  myClient.board[chain.start.get(0)][chain.start.get(1)+2] == myColor))
											{
												h= 0.5;
											}
										}
										// check if out of bounds:								
										if (chain.end.get(1) < 12 && chain.end.get(1) > 1)
											if(( myClient.board[chain.end.get(0)][chain.end.get(1)+3] == myColor
												&&  myClient.board[chain.end.get(0)][chain.end.get(1)+2] ==  opponentColor()
												&&  myClient.board[chain.end.get(0)][chain.end.get(1)+1] == -1
												&&  myClient.board[chain.end.get(0)][chain.end.get(1)-2] == myColor))
										{
											h= 0.5;
										}
									}
							}
								
							}
//							if(h==0)
//								h= .9;
						}
						// *********** if no extreme cases present, grow your own chain ***************
						// a totally unblocked 4-chain is more favorable than a 4-chain with one side blocked

						else if ( myClient.fourChain( myColor)) {
							h= 0.4;		
						}
						else if ( myClient.threeChain( myColor)) {
							h= 0.3;
						}
						else if ( myClient.twoChain( myColor)) {
							h= 0.2;
						}
						else { 
							h= 0.1; // does not grow row or prevent opponent from gaining advantage	
						}
		}
}


	
	