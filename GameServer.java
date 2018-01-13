package gomoku;

import java.io.*;
//import java.net.*;
import java.util.ArrayList;
import java.util.Random;
import java.net.ServerSocket;
import java.net.Socket;


public class GameServer extends GomokuProtocol
{
   private static int PORT = 8008;
   private static ArrayList<String> names = new ArrayList<>();
   private static ArrayList<PrintWriter> writers = new ArrayList<>();
   private static ArrayList<Pairs> pairs = new ArrayList<>();
   protected static String server_IP;
   static boolean gameEnd = false;
   	
   public static void main( String[] args ) throws Exception //throws IOException
   {
	   
//	   if(args.length != 2)
//	   {
//		   System.err.println("Not enough arguments");
//		   System.exit(1);
//	   }
//	    server_IP = args[0];
//	   PORT = Integer.parseInt(args[1]);
	   
	   new GameServer();
   }
   
   GameServer() throws Exception{
	   System.out.println("GameServer stated running.");
	   
	   ServerSocket sock = new ServerSocket(PORT);
	   try {
		   while(true) {
			   new ThreadClass(sock.accept()).start();
		   }
	   }
	   finally {
		   sock.close();
	   }
   }
   
   public static void colorAssign(Pairs p1, Pairs p2) {
	   Random random = new Random();
	   int tmp = random.nextInt(2);
	   p1.color = tmp;
	   if(tmp == 1)
		   p2.color = 0;
	   else
		   p2.color = 1;	   
   }
   public static void pairClients() {
	   ArrayList<Pairs> idles = new ArrayList<>();
	   Pairs p1 = null;
	   ArrayList<Pairs> idleAIs = new ArrayList<>();
	   Pairs p2 = null;
	   PrintWriter pout1;
	   int pos = 0;
//	   System.out.println("Pairing");
	   
	   Random random = new Random();
//	   int i = random.nextInt(pairs.size());
	   
//	   for(int j = i; j < pairs.size(); j++)
	   for(Pairs p : pairs)
	   {
//		   Pairs p = pairs.get(i);
		   if(p.partner == null) 
		   {
//			   if (p1 == null)
//			   {
//				   p1 = p;
//				   idles.add(p);
////				   System.out.println(p1.partner);
////			   }
//			   else 
//			   {
				   if( p.isAI)
				   {
//					   p2 = p;
//					   System.out.println("These are AIs being paired!");
					   idleAIs.add(p);
				   }
				   else
				   {
//					   p2 = p;
//					   System.out.println("These are Humans being paired!");
					   idles.add(p);
				   }
//			   }
		   }
	   }
	   if(idles.size() < idleAIs.size())
		   idles = idleAIs;
	   if(idles.size() != 0)
	   {
		   for(int i = 0; i < idles.size(); i++)
		   {
			   if(idles.get(pos).count > idles.get(i).count)
			   {
				   pos = i;
			   }
		   }
		   p1 = idles.get(pos);
		   idles.remove(pos);
		   pos = 0;
	   }
	   if(idles.size() != 0)
	   {
		   for(int i = 0; i < idles.size(); i++)
		   {
			   if(idles.get(pos).count > idles.get(i).count)
			   {
				   pos = i;
			   }
		   }
		   p2 = idles.get(pos);
	   }
//	   System.out.println("after pairing");
	   if(p2 != null)
	   {
		   p1.setPartner(p2.name);
		   p2.setPartner(p1.name);
		   pos = names.indexOf(p1.name);
		   pout1 = writers.get(pos);
		   colorAssign(p1, p2);
		   pos = names.indexOf(p1.name);
//		   PrintWriter pout1 = writers.get(pos);
		   pos = names.indexOf(p2.name);
		   PrintWriter pout2 = writers.get(pos);

		   if(p1.color == 1)
		   {
			   pout1.println(generateSetBlackColorMessage());
			   pout2.println(generateSetWhiteColorMessage());
			   p1.count++;
			   p2.count++;
			   
		   }
		   else
		   {
			   pout2.println(generateSetBlackColorMessage());
			   pout1.println(generateSetWhiteColorMessage());
			   p1.count++;
			   p2.count++;
		   }
//		   pout1.println("Your opponent is " + p2.name);
//		   pout2.println("Your opponent is "+ p1.name);
		   for (PrintWriter p : writers)
			   p.println(p1.name +" and " + p2.name + " are playing eachother");
	   }
//	   else if(p1!= null) {
//		   pos = names.indexOf(p1.name);
//		   pout1 = writers.get(pos);
//		   pout1.println("not enough idle players to make pair");
//	   }
   }
   
   /////////////////////// Pairs Class //////////////////////////////
   public static class Pairs{
	   private String name;
	   private String partner = null;
	   public boolean isAI = false;
	   public int color;
	   public int count;
	   public Pairs(String p1) {
		  setName(p1);
	   }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPartner() {
		return partner;
	}

	public void setPartner(String partner) {
		this.partner = partner;
	}

	   
	   

   }
   ////////////////////////////// THREAD CLASS ///////////////////////////////
   
   public static class ThreadClass extends Thread {
	   
	   private String name;
	   private Socket socket;
	   private BufferedReader bin;
	   private PrintWriter pout;
	   private Pairs newP;
	   
	   public ThreadClass(Socket socket) {
		   this.socket = socket;
	   }

	   public boolean isAimsg(String m) {
		   return m.startsWith("----this is AI");
	   }
	   public void run() 
	   {
		   try {
			   System.out.println("this is running");
			   name = "Anonymous" + (pairs.size() + 1);
			   bin = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			   pout = new PrintWriter(socket.getOutputStream(), true);
  
			   synchronized (names)
			   {
				   newP = new Pairs(name);
				   pairs.add(newP);
				   if(name.contains("AI"))
					   newP.isAI = true;
				   names.add(name);
				   pout.println(generateChangeNameMessage("", name));
//				   pout.println("SETNAME " + name);
				   pout.println("Welcome to game "+ name+"!");
				   writers.add(pout);		   
//				   pairClients();
				   
			   }
			   
			   while(true)//names != null)
			   {				   
				   String msg = bin.readLine();
				   if (msg == null){
					   continue;
					   }
				   else if(isAimsg(msg)) {		
					   int pos = names.indexOf(name);					   
					   String c = name.substring(9);
					   String newn = "AI" + c;					   
					   Pairs temp = new Pairs(newn);
					   temp.isAI = true;
		            	names.remove(name);			            	
		            	pairs.remove(pos);
		            	names.add(pos, newn);
		            	pairs.add(pos, temp);
		            	pout.println(generateChangeNameMessage(name, newn));
		            	name = newn;
//		            	pout.println("SETNAME " + newn);
		            	pout.println("Oh you're an AI changing name to "+newn);
				   }
				   else if(isPlayMessage(msg))
				   {
			            int pos = names.indexOf(name);
			            String part = pairs.get(pos).partner;
			            pos = names.indexOf(part);
//			            System.out.println(name);
//			            System.out.println(pos);
			            pout = writers.get(pos);
			            pout.println(msg);			   
				   }
				   else if(isChangeNameMessage(msg))
				   {	
					   String[] detail = getChangeNameDetail(msg);
			            // black is 1 and white is 0
					   
					   int pos = names.indexOf(detail[0]);
					   pout = writers.get(pos);
//			            System.out.println("old name is " + detail[0]);
//			            System.out.println("new name is " + detail[1]);
			            if(names.contains(detail[1]))
			            {
//			            	pout.println("SETNAME " + detail[0]);
			            	pout.println(generateChangeNameMessage(detail[0], detail[1]));
			            	pout.println(detail[1] + " Already taken setting to first");
			            }
//			            else if(detail[1].startsWith("AI")) {
//			            	Pairs temp = new Pairs(detail[1]);
//			            	names.remove(name);			            	
//			            	pairs.remove(pos);
//			            	names.add(pos, detail[1]);
//			            	pairs.add(pos, temp);
//			            	name = detail[1];
//			            	pout.println("SETNAME " + detail[1]);
//			            }
			            else
			            {
			            	
			            	String tmp = pairs.get(pos).partner;
			            	Pairs temp = new Pairs(detail[1]);			            	
			            	temp.setPartner(tmp);
			            	names.remove(name);			            	
			            	pairs.remove(pos);
			            	names.add(pos, detail[1]);
			            	pairs.add(pos, temp);
			            	if(tmp != null)
			            	{
			            		pos = names.indexOf(tmp);
			            		pairs.get(pos).setPartner(detail[1]);
			            	}
			            	name = detail[1];
//			            	pout.println("SETNAME " + detail[1]);
			            	pout.println(generateChangeNameMessage(detail[0], detail[1]));
			            	pout.println("Your nickname has been changed from "+name+ " to " + detail[1]);
			            }
				   }
				   else if (isChatMessage(msg)) {
					   String[] detail = getChatDetail(msg);
			            // black is 1 and white is 0
//			            System.out.println("sender is " + detail[0]);
//			            System.out.println("chat message is " + detail[1]);
			            int n1 = names.indexOf(detail[0]);
			            Pairs prs = pairs.get(n1);
			            String str = prs.getPartner();
			            if(str != null)
			            { 
			            	n1 = names.indexOf(str);
			            	PrintWriter wr1 = writers.get(n1);
			            	wr1.println(detail[0] + ": " + detail[1]);
			            	}
				   }
				   else if(isWinMessage(msg))
				   {
					   int n1 = names.indexOf(name);
			            PrintWriter wr1 = writers.get(n1);
			            Pairs prs = pairs.get(n1);
			            String str = prs.getPartner();
			            
			            int n2 = names.indexOf(str);
			            Pairs prs2 = pairs.get(n2);
			            PrintWriter wr2 = writers.get(n2);
			            wr2.println(generateLoseMessage());
			            wr1.println(msg);
//			            wr1.println(generateResetMessage());
//			            wr2.println(generateResetMessage());
			            for (PrintWriter p : writers)
			            	p.println(name + " is victorious!");
			            wr1.println("Game ended. Returning to idle state");
			            wr2.println("Game ended. Returning to idle state");
			            prs.setPartner(null);
			            prs2.setPartner(null);
			            gameEnd = true;
//					   System.out.println("WINNER");
				   }
				   else if (isLoseMessage(msg))
				   {
					   int n1 = names.indexOf(name);
			            PrintWriter wr1 = writers.get(n1);
			            Pairs prs = pairs.get(n1);
			            String str = prs.getPartner();
			            
			            int n2 = names.indexOf(str);
			            Pairs prs2 = pairs.get(n2);
			            PrintWriter wr2 = writers.get(n2);
			            wr2.println(generateWinMessage());
			            wr1.println(msg);
//			            wr1.println(generateResetMessage());
//			            wr2.println(generateResetMessage());
			            for (PrintWriter p : writers)
			            	p.println(str + " is victorious!");
			            wr1.println("Game ended. Returning to idle state");
			            wr2.println("Game ended. Returning to idle state");
			            prs.setPartner(null);
			            prs2.setPartner(null);
			            gameEnd = true;
//					   System.out.println("Loser");
				   }
				   else if(isGiveupMessage(msg)) {
					   for (PrintWriter p : writers)
			            	p.println(name + " has given up.");
//					   System.out.println("GIVE UP");
				   }
				   else if(isSetBlackColorMessage(msg)) {
//					   System.out.println("is set black color message");
				   }
				   else if(isSetWhiteColorMessage(msg)) {
//					   System.out.println("is set white color message");
				   }
				   else if (isResetMessage(msg))
				   {
					   int n1 = names.indexOf(name);
					   PrintWriter wr2 = writers.get(n1);
			            Pairs prs = pairs.get(n1);
			            String str = prs.getPartner();
			            n1 = names.indexOf(str);
			            PrintWriter wr1 = writers.get(n1);
			            wr1.println(msg);
			            wr2.println(msg);
//					   System.out.println("RESET GAME");
				   }
				   else {
					   for (PrintWriter writer : writers) {
						   writer.println(msg);
					   }

				   }
				   if(!gameEnd)
					   pairClients();
//				   else
//				   {
//					   if (name != null) {
//		                    names.remove(name);
//		                    System.out.println("remove " + name);
//		                }
//		                if (pout != null) {
//		                    writers.remove(pout);
//		                    System.out.println("remove writer for " + name);
//		                }
//		                if (newP != null ) {
//		                	pairs.remove(newP);
//		                    System.out.println("remove pair for " + name);
//
//		                }
//		                try {
//		                    System.out.println("remove socket for " + name);
//		                    socket.close();
//		                } catch (IOException e) {
//		                }
//		                break;
//				   }
			   }
		   }
		   catch (IOException e) {
				e.printStackTrace();
			} finally {
                // This client is going down!  Remove its name and its print
                // writer from the sets, and close its socket.
                if (name != null) {
                    names.remove(name);
//                    System.out.println("remove " + name);
                }
                if (pout != null) {
                    writers.remove(pout);
//                    System.out.println("remove writer for " + name);
                }
                if (newP != null ) {
                	pairs.remove(newP);
//                    System.out.println("remove pair for " + name);

                }
                try {
//                    System.out.println("remove socket for " + name);
                    socket.close();
                } catch (IOException e) {
                }
            }
        }
    }
}