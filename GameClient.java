package gomoku;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

// I think the cs-class server is at 10.120.5.205

public class GameClient extends GomokuProtocol{
	int clientType; //// 0 is AI and 1 is human
	 int port = 8008;//12311;
	 String ip = "localhost";//"10.120.3.147";
	 static Socket sock = null;
	 static BufferedReader bin = null;
	 static PrintWriter pout = null;
	 static boolean isBlack;
	 int isColorSet = -1;
	 static int [][] board = new int [15][15];
	 static boolean isTurn = false;
	 static GameGUI g;
	 String name = "";
	 double isZero;
	 
	 static ArrayList<Chain> chainCoords = new ArrayList<Chain>();
	 ArrayList<Chain> oppChainCoords = new ArrayList<Chain>();


	 static int myColor;
	 
	public int clientType()
	{
		return clientType;
	}
	public void clientType(int c)
	{
		clientType = c;
	}
	public GameClient()
	{

	}
	public static void sendMove(int a, int b) throws IOException
	{
		g.mouseOn = false;
		if(isBlack)
		{
			board[a][b] = 1;
			myColor = 1;
		}
		else
		{
			board[a][b] = 0;
			myColor = 0;
		}
//		g.repaint();
		String m = generatePlayMessage(isBlack, a, b);
		g.repaint();
//		System.out.println(m);
		pout.println(m);
//		pout = new PrintWriter(sock.getOutputStream(), true);
		System.out.println("THIS IS PLAY MSG" + m);
		if(victory(myColor))
		{
			pout.println(generateWinMessage());
			System.out.println("YOU WON!!!!");
		}
//		pout.println(m);
		isTurn = false;
	}
	
	public void setCoords() {
		chainCoords = new ArrayList<Chain>();
		oppChainCoords = new ArrayList<Chain>();
		int player, opponent;
		if(isBlack)
		{
			player = 1;
			opponent =0;
		}
		else
		{
			player = 0;
			opponent = 1;
		}
		fourChain(player, chainCoords);
		threeChain(player, chainCoords);
		twoChain(player, chainCoords);
		
		fourChain(opponent, oppChainCoords);
		threeChain(opponent, oppChainCoords);
		twoChain(opponent, oppChainCoords);
	}

	public static boolean tie () 
	{
		int emptySpaces = 0;
		for (int row = 0; row < 15; row++) {
	        for (int col = 0; col < 15; col++)
	        {
	            if(board[row][col] == -1)
	            		emptySpaces++;
	        } 
		}
		if (emptySpaces == 0)
			return true;
		else 
			return false;
	}

	public static boolean victory(int player)
	{
	    int vert = 0;
	    int horiz = 0;
	    int rdiag = 0;
	    int ldiag = 0;
	    for (int row = 0; row < 15; row++) {
	        vert = 0;
	        for (int col = 0; col < 15; col++)
	        {
	            if(board[row][col]==player)
	                vert++;
	            else
	                vert = 0;
	            if(vert == 5)
	                return true;
	        }
	    }
	    for (int col = 0; col < 15; col++) {
	        horiz = 0;
	        for (int row = 0; row < 15; row++)
	        {
	            if(board[row][col]==player)
	                horiz++;
	            else
	                horiz = 0;
	            if(horiz == 5)
	                return true;
	        }
	    }
	    for (int row = 0; row < 11; row++)
	    for (int col = 0; col < 11; col++)
	    {
	        if(board[row][col]==player) {
	            rdiag = 1;
	            for(int i = 1; i < 5; i++)
	            {
	                if(board[row+i][col+i]==player)
	                    rdiag++;
	                else
	                    rdiag = 0;
	                if(rdiag == 5)
	                    return true;
	            }
	        }
	    }
	    for (int row = 0; row < 11; row++)
		    for (int col = 0; col < 15; col++)
		    {
		        if(board[row][col]==player && col > 4) {
		            ldiag = 1;
		            for(int i = 1; i < 5; i++)
		            {
		                if(board[row+i][col-i]==player)
		                    ldiag++;
		                else
		                    ldiag = 0;
		                if(ldiag == 5)
		                    return true;
		            }
		        }
		    }
		    for (int row = 0; row < 15; row++)
			    for (int col = 0; col < 11; col++)
			    {
			        if(board[row][col]==player && row > 4) {
			            ldiag = 1;
			            for(int i = 1; i < 5; i++)
			            {
			                if(board[row-i][col+i]==player)
			                    ldiag++;
			                else
			                    ldiag = 0;
			                if(ldiag == 5)
			                    return true;
			            }
			        }
			    }
	    
	    return false;
	    
	}
//	
//	public static boolean victory(int player)
//	{
//	    int vert;
//	    int horiz;
//	    int rdiag;
//	    int ldiag;
//	    	    
//	    for (int col = 0; col < 15; col++) {
//	        vert = 0;
//	        for (int row = 0; row < 15; row++)
//	        {
//	            if(board[row][col]==player)
//	            {
//	            		vert++;
//	            }  
//
//	            if(vert == 5)
//	            {
//	            	g.board = board;
//	            	g.repaint();
//		            return true;
//	            }     
//	        }
//	    }
//	    	    
//	    for (int row = 0; row < 15; row++) {
//	        horiz = 0;
//	        for (int col = 0; col < 15; col++)
//	        {
//	            if(board[row][col]==player)
//	            {
//	            		horiz++;
//	            }
//
//	            if(horiz == 5)
//	            {
//	            	g.board = board;
//	            	g.repaint();
//		            return true;
//	            }
//	        }       
//	    }
//	    	    
//	    for (int row = 0; row < 14; row++) {
//	    		rdiag = 0;
//		    for (int col = 0; col < 14; col++)
//		    {
//		        if(board[row][col]==player && col < 11) 
//		        {
//		            rdiag++;
//		            for(int i = 1; i < 5; i++)
//		            {
//		                if(board[row+i][col+i]==player)
//		                    rdiag++;
//
//		            }
//		        }
//		        if(rdiag == 5)
//		        {
//		        	g.board = board;
//	            	g.repaint();
//		            return true;
//		        }
//		    }
//	    }
//	    	    
//	    for (int row = 0; row < 14; row++) {
//	    		ldiag = 0;
//		    for (int col = 0; col < 15; col++)
//		    {
//		        if (board[row][col]==player && col > 3) 
//		        {
//		            ldiag++;
//		            for (int i = 1; i < 5; i++)
//		            {
//		                if(board[row+i][col-i]==player)
//		                    ldiag++;
//		            }
//		        }
//		        
//		        if(ldiag == 5) 
//		        {
//		        	g.board = board;
//	            	g.repaint();
//		            return true;
//		        }
//		    }
//	    }
//	    return false;
//	}
//	
	public static boolean fourChain (int player, ArrayList<Chain> cc)
	{
	    int vert;
	    int horiz;
	    int rdiag;
	    int ldiag;
	    boolean trigger = false; // so that start coords are only set once in nested for loops
	    
	    Chain chain = new Chain();
	    
	    for (int col = 0; col < 15; col++) {
	        vert = 0;
	        for (int row = 0; row < 15; row++)
	        {
	            if(board[row][col]==player)
	            {
	            		vert++;
	            		if(trigger == false) 
	            		{
	            			ArrayList<Integer> start = new ArrayList<Integer>();
	    		            start.add(row);
	    		            start.add(col);
	    		            chain.start = start;
	    		            trigger = true;
	            		}	
	            }  
//	            else
//	                vert = 0;
	            
	            if(vert == 4)
	            {
	            		chain.length = vert;
	            		ArrayList<Integer> end = new ArrayList<Integer>();
		            end.add(row);
		            end.add(col);
		            chain.end = end;
		            chain.color = player;
		            cc.add(chain);
		            return true;
	            }     
	        }
	    }
	    
	    trigger = false;
	    
	    for (int row = 0; row < 15; row++) {
	        horiz = 0;
	        for (int col = 0; col < 15; col++)
	        {
	            if(board[row][col]==player)
	            {
	            		horiz++;
		            if(trigger == false) 
	        			{
	        				ArrayList<Integer> start = new ArrayList<Integer>();
			            start.add(row);
			            start.add(col);
			            chain.start = start;
			            trigger = true;
	        			}	
	            }
//	            else
//	                horiz = 0;
	            
	            if(horiz == 4)
	            {
		            chain.length = horiz;
	            		ArrayList<Integer> end = new ArrayList<Integer>();
		            end.add(row);
		            end.add(col);
		            chain.end = end;
		            chain.color = player;
		            cc.add(chain);
		            return true;
	            }
	        }       
	    }
	    
	    trigger = false;
	    
	    for (int row = 0; row < 14; row++) {
	    		rdiag = 0;
		    for (int col = 0; col < 14; col++)
		    {
		        if(board[row][col]==player && col < 12) 
		        {
		            rdiag++;
		            if(trigger == false) 
	        			{
	        				ArrayList<Integer> start = new ArrayList<Integer>();
			            start.add(row);
			            start.add(col);
			            chain.start = start;
			            trigger = true;
	        			}	
		            
		            for(int i = 1; i < 4; i++)
		            {
		                if(board[row+i][col+i]==player)
		                    rdiag++;
//		                else
//		                    rdiag = 0;
		            }
		        }
		        if(rdiag == 4)
		        {
			        	chain.length = rdiag;
	            		ArrayList<Integer> end = new ArrayList<Integer>();
		            end.add(row);
		            end.add(col);
		            chain.end = end;
		            chain.color = player;
		            cc.add(chain);
		            return true;
		        }
		    }
	    }
	    
	    trigger = false;
	    
	    for (int row = 0; row < 14; row++) {
	    		ldiag = 0;
		    for (int col = 0; col < 15; col++)
		    {
		        if (board[row][col]==player && col > 2) 
		        {
		            ldiag++;
		            if(trigger == false) 
	        			{
	        				ArrayList<Integer> start = new ArrayList<Integer>();
			            start.add(row);
			            start.add(col);
			            chain.start = start;
			            trigger = true;
	        			}	
		            for (int i = 1; i < 4; i++)
		            {
		                if(board[row+i][col-i]==player)
		                    ldiag++;
//		                else
//		                    ldiag = 0;
		            }
		        }
		        
		        if(ldiag == 4) 
		        {
			        	chain.length = ldiag;
	            		ArrayList<Integer> end = new ArrayList<Integer>();
		            end.add(row);
		            end.add(col);
		            chain.end = end;
		            chain.color = player;
		            cc.add(chain);
		            return true;
		        }
		    }
	    }
	    return false;
	}

	public static boolean threeChain (int player, ArrayList<Chain> cc)
	{
	    int vert;
	    int horiz;
	    int rdiag;
	    int ldiag;
	    boolean trigger = false; // so that start coords are only set once in nested for loops
	    
	    Chain chain = new Chain();
	    
	    for (int col = 0; col < 15; col++) {
	        vert = 0;
	        for (int row = 0; row < 15; row++)
	        {
	            if(board[row][col]==player)
	            {
	            		vert++;
	            		if(trigger == false) 
	            		{
	            			ArrayList<Integer> start = new ArrayList<Integer>();
	    		            start.add(row);
	    		            start.add(col);
	    		            chain.start = start;
	    		            trigger = true;
	            		}	
	            }  
//	            else
//	                vert = 0;
	            
	            if(vert == 3)
	            {
	            		chain.length = vert;
	            		ArrayList<Integer> end = new ArrayList<Integer>();
		            end.add(row);
		            end.add(col);
		            chain.end = end;
		            chain.color = player;
		            cc.add(chain);
		            return true;
	            }     
	        }
	    }
	    
	    trigger = false;
	    
	    for (int row = 0; row < 15; row++) {
	        horiz = 0;
	        for (int col = 0; col < 15; col++)
	        {
	            if(board[row][col]==player)
	            {
	            		horiz++;
		            if(trigger == false) 
	        			{
	        				ArrayList<Integer> start = new ArrayList<Integer>();
			            start.add(row);
			            start.add(col);
			            chain.start = start;
			            trigger = true;
	        			}	
	            }
//	            else
//	                horiz = 0;
	            
	            if(horiz == 3)
	            {
		            chain.length = horiz;
	            		ArrayList<Integer> end = new ArrayList<Integer>();
		            end.add(row);
		            end.add(col);
		            chain.end = end;
		            chain.color = player;
		            cc.add(chain);
		            return true;
	            }
	        }       
	    }
	    
	    trigger = false;
	    
	    for (int row = 0; row < 14; row++) {
	    		rdiag = 0;
		    for (int col = 0; col < 14; col++)
		    {
		        if(board[row][col]==player && col < 13) 
		        {
		            rdiag++;
		            if(trigger == false) 
	        			{
	        				ArrayList<Integer> start = new ArrayList<Integer>();
			            start.add(row);
			            start.add(col);
			            chain.start = start;
			            trigger = true;
	        			}	
		            
		            for(int i = 1; i < 3; i++)
		            {
		                if(board[row+i][col+i]==player)
		                    rdiag++;
//		                else
//		                    rdiag = 0;
		            }
		        }
		        if(rdiag == 3)
		        {
			        	chain.length = rdiag;
	            		ArrayList<Integer> end = new ArrayList<Integer>();
		            end.add(row);
		            end.add(col);
		            chain.end = end;
		            chain.color = player;
		            cc.add(chain);
		            return true;
		        }
		    }
	    }
	    
	    trigger = false;
	    
	    for (int row = 0; row < 14; row++) {
	    		ldiag = 0;
		    for (int col = 0; col < 15; col++)
		    {
		        if (board[row][col]==player && col > 1) 
		        {
		            ldiag++;
		            if(trigger == false) 
	        			{
	        				ArrayList<Integer> start = new ArrayList<Integer>();
			            start.add(row);
			            start.add(col);
			            chain.start = start;
			            trigger = true;
	        			}	
		            for (int i = 1; i < 3; i++)
		            {
		                if(board[row+i][col-i]==player)
		                    ldiag++;
//		                else
//		                    ldiag = 0;
		            }
		        }
		        
		        if(ldiag == 3) 
		        {
			        	chain.length = ldiag;
	            		ArrayList<Integer> end = new ArrayList<Integer>();
		            end.add(row);
		            end.add(col);
		            chain.end = end;
		            chain.color = player;
		            cc.add(chain);
		            return true;
		        }
		    }
	    }
	    return false;
	}
	
	public static boolean twoChain (int player, ArrayList<Chain> cc)
	{
	    int vert;
	    int horiz;
	    int rdiag;
	    int ldiag;
	    boolean trigger = false; // so that start coords are only set once in nested for loops
	    
	    Chain chain = new Chain();
	    
	    for (int col = 0; col < 15; col++) {
	        vert = 0;
	        for (int row = 0; row < 15; row++)
	        {
	            if(board[row][col]==player)
	            {
	            		vert++;
	            		if(trigger == false) 
	            		{
	            			ArrayList<Integer> start = new ArrayList<Integer>();
	    		            start.add(row);
	    		            start.add(col);
	    		            chain.start = start;
	    		            trigger = true;
	            		}	
	            }  
//	            else
//	                vert = 0;
	            
	            if(vert == 2)
	            {
	            		chain.length = vert;
	            		ArrayList<Integer> end = new ArrayList<Integer>();
		            end.add(row);
		            end.add(col);
		            chain.end = end;
		            chain.color = player;
		            cc.add(chain);
		            return true;
	            }     
	        }
	    }
	    
	    trigger = false;
	    
	    for (int row = 0; row < 15; row++) {
	        horiz = 0;
	        for (int col = 0; col < 15; col++)
	        {
	            if(board[row][col]==player)
	            {
	            		horiz++;
		            if(trigger == false) 
	        			{
	        				ArrayList<Integer> start = new ArrayList<Integer>();
			            start.add(row);
			            start.add(col);
			            chain.start = start;
			            trigger = true;
	        			}	
	            }
//	            else
//	                horiz = 0;
	            
	            if(horiz == 2)
	            {
		            chain.length = horiz;
	            		ArrayList<Integer> end = new ArrayList<Integer>();
		            end.add(row);
		            end.add(col);
		            chain.end = end;
		            chain.color = player;
		            cc.add(chain);
		            return true;
	            }
	        }       
	    }
	    
	    trigger = false;
	    
	    for (int row = 0; row < 14; row++) {
	    		rdiag = 0;
		    for (int col = 0; col < 14; col++)
		    {
		        if(board[row][col]==player && col < 14) 
		        {
		            rdiag++;
		            if(trigger == false) 
	        			{
	        				ArrayList<Integer> start = new ArrayList<Integer>();
			            start.add(row);
			            start.add(col);
			            chain.start = start;
			            trigger = true;
	        			}	
		            
		            for(int i = 1; i < 2; i++)
		            {
		                if(board[row+i][col+i]==player)
		                    rdiag++;
//		                else
//		                    rdiag = 0;
		            }
		        }
		        if(rdiag == 2)
		        {
			        	chain.length = rdiag;
	            		ArrayList<Integer> end = new ArrayList<Integer>();
		            end.add(row);
		            end.add(col);
		            chain.end = end;
		            chain.color = player;
		            cc.add(chain);
		            return true;
		        }
		    }
	    }
	    
	    trigger = false;
	    
	    for (int row = 0; row < 14; row++) {
	    		ldiag = 0;
		    for (int col = 0; col < 15; col++)
		    {
		        if (board[row][col]==player && col > 0) 
		        {
		            ldiag++;
		            if(trigger == false) 
	        			{
	        				ArrayList<Integer> start = new ArrayList<Integer>();
			            start.add(row);
			            start.add(col);
			            chain.start = start;
			            trigger = true;
	        			}	
		            for (int i = 1; i < 2; i++)
		            {
		                if(board[row+i][col-i]==player)
		                    ldiag++;
//		                else
//		                    ldiag = 0;
		            }
		        }
		        
		        if(ldiag == 2) 
		        {
			        	chain.length = ldiag;
	            		ArrayList<Integer> end = new ArrayList<Integer>();
		            end.add(row);
		            end.add(col);
		            chain.end = end;
		            chain.color = player;
		            cc.add(chain);
		            return true;
		        }
		    }
	    }
	    
//	    for (int row = 0; row < 15; row++)
//		    for (int col = 0; col < 14; col++)
//		    {
//		        if(board[row][col]==player && row > 1) {
//		            ldiag = 1;
//		            if(trigger == false) 
//		        			{
//		        				ArrayList<Integer> start = new ArrayList<Integer>();
//				            start.add(row);
//				            start.add(col);
//				            chain.start = start;
//				            trigger = true;
//		        			}	
//		            for(int i = 1; i < 2; i++)
//		            {
//		                if(board[row-i][col+i]==player)
//		                    ldiag++;
//		                else
//		                    ldiag = 0;
//		            }
//		        }
//		        if(ldiag == 2) 
//		        {
//			        	chain.length = ldiag;
//	            		ArrayList<Integer> end = new ArrayList<Integer>();
//		            end.add(row);
//		            end.add(col);
//		            chain.end = end;
//		            chain.color = player;
//		            cc.add(chain);
//		            return true;
//		        }
//		    }
		    return false;
	}
	
	public static boolean fourChain (int player)
	{
	    int vert;
	    int horiz;
	    int rdiag;
	    int ldiag;
	    boolean trigger = false; // so that start coords are only set once in nested for loops
	    
	    Chain chain = new Chain();
	    
	    for (int col = 0; col < 15; col++) {
	        vert = 0;
	        for (int row = 0; row < 15; row++)
	        {
	            if(board[row][col]==player)
	            {
	            		vert++;
	            		if(trigger == false) 
	            		{
	            			ArrayList<Integer> start = new ArrayList<Integer>();
	    		            start.add(row);
	    		            start.add(col);
	    		            chain.start = start;
	    		            trigger = true;
	            		}	
	            }  
//	            else
//	                vert = 0;
	            
	            if(vert == 4)
	            {
	            		chain.length = vert;
	            		ArrayList<Integer> end = new ArrayList<Integer>();
		            end.add(row);
		            end.add(col);
		            chain.end = end;
		            chain.color = player;
//		            cc.add(chain);
		            return true;
	            }     
	        }
	    }
	    
	    trigger = false;
	    
	    for (int row = 0; row < 15; row++) {
	        horiz = 0;
	        for (int col = 0; col < 15; col++)
	        {
	            if(board[row][col]==player)
	            {
	            		horiz++;
		            if(trigger == false) 
	        			{
	        				ArrayList<Integer> start = new ArrayList<Integer>();
			            start.add(row);
			            start.add(col);
			            chain.start = start;
			            trigger = true;
	        			}	
	            }
//	            else
//	                horiz = 0;
	            
	            if(horiz == 4)
	            {
		            chain.length = horiz;
	            		ArrayList<Integer> end = new ArrayList<Integer>();
		            end.add(row);
		            end.add(col);
		            chain.end = end;
		            chain.color = player;
//		            cc.add(chain);
		            return true;
	            }
	        }       
	    }
	    
	    trigger = false;
	    
	    for (int row = 0; row < 14; row++) {
	    		rdiag = 0;
		    for (int col = 0; col < 14; col++)
		    {
		        if(board[row][col]==player && col < 12) 
		        {
		            rdiag++;
		            if(trigger == false) 
	        			{
	        				ArrayList<Integer> start = new ArrayList<Integer>();
			            start.add(row);
			            start.add(col);
			            chain.start = start;
			            trigger = true;
	        			}	
		            
		            for(int i = 1; i < 4; i++)
		            {
		                if(board[row+i][col+i]==player)
		                    rdiag++;
//		                else
//		                    rdiag = 0;
		            }
		        }
		        if(rdiag == 4)
		        {
			        	chain.length = rdiag;
	            		ArrayList<Integer> end = new ArrayList<Integer>();
		            end.add(row);
		            end.add(col);
		            chain.end = end;
		            chain.color = player;
//		            cc.add(chain);
		            return true;
		        }
		    }
	    }
	    
	    trigger = false;
	    
	    for (int row = 0; row < 14; row++) {
	    		ldiag = 0;
		    for (int col = 0; col < 15; col++)
		    {
		        if (board[row][col]==player && col > 2) 
		        {
		            ldiag++;
		            if(trigger == false) 
	        			{
	        				ArrayList<Integer> start = new ArrayList<Integer>();
			            start.add(row);
			            start.add(col);
			            chain.start = start;
			            trigger = true;
	        			}	
		            for (int i = 1; i < 4; i++)
		            {
		                if(board[row+i][col-i]==player)
		                    ldiag++;
//		                else
//		                    ldiag = 0;
		            }
		        }
		        
		        if(ldiag == 4) 
		        {
			        	chain.length = ldiag;
	            		ArrayList<Integer> end = new ArrayList<Integer>();
		            end.add(row);
		            end.add(col);
		            chain.end = end;
		            chain.color = player;
//		            cc.add(chain);
		            return true;
		        }
		    }
	    }
	    return false;
	}

	public static boolean threeChain (int player)
	{
	    int vert;
	    int horiz;
	    int rdiag;
	    int ldiag;
	    boolean trigger = false; // so that start coords are only set once in nested for loops
	    
	    Chain chain = new Chain();
	    
	    for (int col = 0; col < 15; col++) {
	        vert = 0;
	        for (int row = 0; row < 15; row++)
	        {
	            if(board[row][col]==player)
	            {
	            		vert++;
	            		if(trigger == false) 
	            		{
	            			ArrayList<Integer> start = new ArrayList<Integer>();
	    		            start.add(row);
	    		            start.add(col);
	    		            chain.start = start;
	    		            trigger = true;
	            		}	
	            }  
//	            else
//	                vert = 0;
	            
	            if(vert == 3)
	            {
	            		chain.length = vert;
	            		ArrayList<Integer> end = new ArrayList<Integer>();
		            end.add(row);
		            end.add(col);
		            chain.end = end;
		            chain.color = player;
//		            cc.add(chain);
		            return true;
	            }     
	        }
	    }
	    
	    trigger = false;
	    
	    for (int row = 0; row < 15; row++) {
	        horiz = 0;
	        for (int col = 0; col < 15; col++)
	        {
	            if(board[row][col]==player)
	            {
	            		horiz++;
		            if(trigger == false) 
	        			{
	        				ArrayList<Integer> start = new ArrayList<Integer>();
			            start.add(row);
			            start.add(col);
			            chain.start = start;
			            trigger = true;
	        			}	
	            }
//	            else
//	                horiz = 0;
	            
	            if(horiz == 3)
	            {
		            chain.length = horiz;
	            		ArrayList<Integer> end = new ArrayList<Integer>();
		            end.add(row);
		            end.add(col);
		            chain.end = end;
		            chain.color = player;
//		            cc.add(chain);
		            return true;
	            }
	        }       
	    }
	    
	    trigger = false;
	    
	    for (int row = 0; row < 14; row++) {
	    		rdiag = 0;
		    for (int col = 0; col < 14; col++)
		    {
		        if(board[row][col]==player && col < 13) 
		        {
		            rdiag++;
		            if(trigger == false) 
	        			{
	        				ArrayList<Integer> start = new ArrayList<Integer>();
			            start.add(row);
			            start.add(col);
			            chain.start = start;
			            trigger = true;
	        			}	
		            
		            for(int i = 1; i < 3; i++)
		            {
		                if(board[row+i][col+i]==player)
		                    rdiag++;
//		                else
//		                    rdiag = 0;
		            }
		        }
		        if(rdiag == 3)
		        {
			        	chain.length = rdiag;
	            		ArrayList<Integer> end = new ArrayList<Integer>();
		            end.add(row);
		            end.add(col);
		            chain.end = end;
		            chain.color = player;
//		            cc.add(chain);
		            return true;
		        }
		    }
	    }
	    
	    trigger = false;
	    
	    for (int row = 0; row < 14; row++) {
	    		ldiag = 0;
		    for (int col = 0; col < 15; col++)
		    {
		        if (board[row][col]==player && col > 1) 
		        {
		            ldiag++;
		            if(trigger == false) 
	        			{
	        				ArrayList<Integer> start = new ArrayList<Integer>();
			            start.add(row);
			            start.add(col);
			            chain.start = start;
			            trigger = true;
	        			}	
		            for (int i = 1; i < 3; i++)
		            {
		                if(board[row+i][col-i]==player)
		                    ldiag++;
//		                else
//		                    ldiag = 0;
		            }
		        }
		        
		        if(ldiag == 3) 
		        {
			        	chain.length = ldiag;
	            		ArrayList<Integer> end = new ArrayList<Integer>();
		            end.add(row);
		            end.add(col);
		            chain.end = end;
		            chain.color = player;
//		            cc.add(chain);
		            return true;
		        }
		    }
	    }
	    return false;
	}
	
	public static boolean twoChain (int player)
	{
	    int vert;
	    int horiz;
	    int rdiag;
	    int ldiag;
	    boolean trigger = false; // so that start coords are only set once in nested for loops
	    
	    Chain chain = new Chain();
	    
	    for (int col = 0; col < 15; col++) {
	        vert = 0;
	        for (int row = 0; row < 15; row++)
	        {
	            if(board[row][col]==player)
	            {
	            		vert++;
	            		if(trigger == false) 
	            		{
//	            			ArrayList<Integer> start = new ArrayList<Integer>();
//	    		            start.add(row);
//	    		            start.add(col);
//	    		            chain.start = start;
	    		            trigger = true;
	            		}	
	            }  
//	            else
//	                vert = 0;
	            
	            if(vert == 2)
	            {
//	            		chain.length = vert;
//	            		ArrayList<Integer> end = new ArrayList<Integer>();
//		            end.add(row);
//		            end.add(col);
//		            chain.end = end;
//		            chain.color = player;
//		            cc.add(chain);
		            return true;
	            }     
	        }
	    }
	    
	    trigger = false;
	    
	    for (int row = 0; row < 15; row++) {
	        horiz = 0;
	        for (int col = 0; col < 15; col++)
	        {
	            if(board[row][col]==player)
	            {
	            		horiz++;
		            if(trigger == false) 
	        			{
//	        				ArrayList<Integer> start = new ArrayList<Integer>();
//			            start.add(row);
//			            start.add(col);
//			            chain.start = start;
			            trigger = true;
	        			}	
	            }
//	            else
//	                horiz = 0;
	            
	            if(horiz == 2)
	            {
//		            chain.length = horiz;
//	            		ArrayList<Integer> end = new ArrayList<Integer>();
//		            end.add(row);
//		            end.add(col);
//		            chain.end = end;
//		            chain.color = player;
//		            cc.add(chain);
		            return true;
	            }
	        }       
	    }
	    
	    trigger = false;
	    
	    for (int row = 0; row < 14; row++) {
	    		rdiag = 0;
		    for (int col = 0; col < 14; col++)
		    {
		        if(board[row][col]==player && col < 14) 
		        {
		            rdiag++;
		            if(trigger == false) 
	        			{
//	        				ArrayList<Integer> start = new ArrayList<Integer>();
//			            start.add(row);
//			            start.add(col);
//			            chain.start = start;
			            trigger = true;
	        			}	
		            
		            for(int i = 1; i < 2; i++)
		            {
		                if(board[row+i][col+i]==player)
		                    rdiag++;
//		                else
//		                    rdiag = 0;
		            }
		        }
		        if(rdiag == 2)
		        {
//			        	chain.length = rdiag;
//	            		ArrayList<Integer> end = new ArrayList<Integer>();
//		            end.add(row);
//		            end.add(col);
//		            chain.end = end;
//		            chain.color = player;
//		            cc.add(chain);
		            return true;
		        }
		    }
	    }
	    
	    trigger = false;
	    
	    for (int row = 0; row < 14; row++) {
	    		ldiag = 0;
		    for (int col = 0; col < 15; col++)
		    {
		        if (board[row][col]==player && col > 0) 
		        {
		            ldiag++;
		            if(trigger == false) 
	        			{
//	        				ArrayList<Integer> start = new ArrayList<Integer>();
//			            start.add(row);
//			            start.add(col);
//			            chain.start = start;
			            trigger = true;
	        			}	
		            for (int i = 1; i < 2; i++)
		            {
		                if(board[row+i][col-i]==player)
		                    ldiag++;
//		                else
//		                    ldiag = 0;
		            }
		        }
		        
		        if(ldiag == 2) 
		        {
//			        	chain.length = ldiag;
//	            		ArrayList<Integer> end = new ArrayList<Integer>();
//		            end.add(row);
//		            end.add(col);
//		            chain.end = end;
//		            chain.color = player;
//		            cc.add(chain);
		            return true;
		        }
		    }
	    }
	    
//	    for (int row = 0; row < 15; row++)
//		    for (int col = 0; col < 14; col++)
//		    {
//		        if(board[row][col]==player && row > 1) {
//		            ldiag = 1;
//		            if(trigger == false) 
//		        			{
//		        				ArrayList<Integer> start = new ArrayList<Integer>();
//				            start.add(row);
//				            start.add(col);
//				            chain.start = start;
//				            trigger = true;
//		        			}	
//		            for(int i = 1; i < 2; i++)
//		            {
//		                if(board[row-i][col+i]==player)
//		                    ldiag++;
//		                else
//		                    ldiag = 0;
//		            }
//		        }
//		        if(ldiag == 2) 
//		        {
//			        	chain.length = ldiag;
//	            		ArrayList<Integer> end = new ArrayList<Integer>();
//		            end.add(row);
//		            end.add(col);
//		            chain.end = end;
//		            chain.color = player;
//		            cc.add(chain);
//		            return true;
//		        }
//		    }
		    return false;
	}
	
	
	
	public static void main(String []args) throws Exception
	{
		GameClient gc = new GameClient();
		gc.clientType(-1);
		JFrame frame = new JFrame();
		Object[] options = {"AI",
                "Human"};
		gc.clientType(JOptionPane.showOptionDialog(frame,
		"What type of game do you want to play?",
		"Gomoku",
		JOptionPane.YES_NO_CANCEL_OPTION,
		JOptionPane.QUESTION_MESSAGE,
		null,
		options,
		options[1]));
		frame.setResizable(false);  
		frame.setVisible(true);
		
		if(gc.clientType() == 1)
		{
			frame.dispose();
			gc = null;
//			System.out.println("human");
			HumanClient h = new HumanClient();
			h.run();
		}
		else if(gc.clientType() ==0)
		{
			frame.dispose();
//			System.out.println("ai");
			AIClient a = new AIClient();
			a.run();
		}
	}
//	public static void startPlay() {}
	
		public void run() throws Exception {
//			System.out.println("workingworking");
//			System.out.println(this.clientType);
	       sock = new Socket(ip, port);
	       bin = new BufferedReader(new InputStreamReader(sock.getInputStream()));
	       pout = new PrintWriter(sock.getOutputStream(), true);
	       if(clientType == 0)
	       {
	    	   pout.println("----this is AI");
	       }
	       else 
	    	   pout.println("");
	       while(true) {
//	    	   if(this.clientType == 1)
//	    	   {
//	    		   System.out.println(turn);
//	    		   g.mouseOn = turn;
//	    	   }
	    	   
			String msg = bin.readLine();
			if(msg == null)
				continue;	
			else if (isPlayMessage(msg)) {
	 		   System.out.println(msg);
	           int[] detail = getPlayDetail(msg);
	           board[detail[1]][detail[2]] = detail[0];
	           g.board = board;
    		   g.mouseOn = true;
	           g.repaint();
	           isTurn = true;
//	           turn = true;
//	 		   nickname = msg.substring(8);
//	 		   label.setText(nickname);
	 	   } else if(isResetMessage(msg)) {
//	 		   System.out.println(generateResetMessage());
				JOptionPane.showMessageDialog(null, "Resetting Game", "ALERT", JOptionPane.INFORMATION_MESSAGE);
	 		   this.reset();
//	 		   closed = true;
	 	   }else if (isChangeNameMessage(msg)) {
			   String[] detail = getChangeNameDetail(msg);
	 		   g.oldname = detail[0];
	 		   g.newname = detail[1];
	 		   name = detail[1];
	 		   g.label.setText(detail[1]);
	 		   
	 	   } else if(isGiveupMessage(msg)) {
//	 		   System.out.println(generateGiveupMessage());
	 	   } else if(isChatMessage(msg)) {
	           String[] detail = getChatDetail(msg);
//	           System.out.println(detail);
	 	   }else if (isLoseMessage(msg)) {
				JOptionPane.showMessageDialog(null, "You have Lost", "ALERT", JOptionPane.INFORMATION_MESSAGE);
//				reset();
//				close();
				g.mouseOn = false;
	 	   }else if (isWinMessage(msg)) {
				JOptionPane.showMessageDialog(null, "You have won", "ALERT", JOptionPane.INFORMATION_MESSAGE);
//				reset();
//				close();
				g.mouseOn = false;
	 	   }else if (msg.startsWith("SETNAME")) {
//	 		  name = msg.substring(8);	
//	 		  System.out.println("NAME is " + name);
	 		  
	 	   }else if(isSetBlackColorMessage(msg)) {
	 		   this.isBlack = true;
	 		   g.color = 1;
	 		   g.colis.setText("Black");
	 		   g.mouseOn = true;
	 		   isColorSet = 1;
	 		   isTurn = true;
	 		   } else if (isSetWhiteColorMessage(msg)) {
	 		   this.isBlack = false;
	 		   g.color = 0;
	 		   isColorSet = 1;
	 		   g.colis.setText("White");
	           } else {
	    		   g.ta.append(msg + "\n");
	 	   }
			if(this.clientType == 0)
				g.mouseOn = false;

		       if(this.clientType ==0 && isColorSet != -1 && isTurn)
	    	   {
	    		   AIClient.startPlay();
//	    		   break;
//	    		   System.out.println("hahaha");
	    	   }
	       }
		}
		public void reset() {
			for(int i = 0; i < 15; i++)
				for(int j = 0; j < 15; j++)
					board[i][j] = -1;
			g.setXclick(0);
			g.setYclick(0);
			g.board = board;
			g.repaint();
		}
		public void pair() {
			pout.println("**STARTPAIRING**");
		}
		public void close() throws IOException {
			bin.close();
			pout.close();
			sock.close();
//			System.exit(0);
		}
}
	 	   
