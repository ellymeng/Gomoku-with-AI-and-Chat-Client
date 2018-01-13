package gomoku;


public class HumanClient extends GameClient {
	
	int choice;
	int row; // set from gameGUI
	int col; //set from gameGUI
	boolean closed = false;
	String nickname = " ";
		
	public HumanClient()
	{
		super();
		clientType = 1;

		g = new GameGUI();
		g.mouseOn = false;
		g.gc = this;
		board = g.board;
	}
	
	public GameGUI getGameGUI()
	{
		return g;
	}
	
	public int getChoice()
	{
		return choice;
	}


	    	   
	public static void main(String []args) 
	{

	}
}
