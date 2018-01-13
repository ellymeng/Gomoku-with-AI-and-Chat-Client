package gomoku;

import java.util.ArrayList;

public class Chain {
	
	int length; 
	int color;
	static ArrayList<Integer> start = new ArrayList<Integer>();
	static ArrayList<Integer> end = new ArrayList<Integer>();

	
	public static boolean isVertical()
	{
		if (start.get(0) == end.get(1)) {
			return true;
		} else { 
			return false; 
		}
		
	}
}
