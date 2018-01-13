package gomoku;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

import javax.swing.*;

//my GameGUI g = new GameGUI();
//  GameGUI.repaint();
public class GameGUI extends JFrame {
	GameClient gc = null;
	JPanel panel0;
	JPanel panel1;
	private int xclick;
	private int yclick;
	boolean mouseOn = true;
	JButton giveUp;
	public int color;
	int [][] board = new int [15][15];
	String resetmsg = GomokuProtocol.generateResetMessage();
	String giveupmsg = GomokuProtocol.generateGiveupMessage();
	String losemsg = GomokuProtocol.generateLoseMessage();
	String newname;
	String oldname;
	JScrollPane scroll;
	JButton button1;
	JButton button2;
	JButton button3;
//	JButton button4;
	JLabel label;
	JLabel colis;
	JTextField chat;
	JTextField chname;
	JTextArea ta;
	static int AIrow = -1;
	static int AIcol = -1;
	
	public void setXclick(int o)
	{
		xclick = o;
	}
	public void setYclick(int o)
	{
		yclick = o;
	}
	public GameGUI()
	{
		  this.setSize(new Dimension(1000,800));
		  this.setLayout(new BorderLayout());
		  this.setLocationRelativeTo(null);
		  this.setVisible(true);
		  this.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		  this.setResizable(false);  
		  this.setBackground(Color.WHITE);
//		  Container cPane = this.getContentPane();

		  
		  for(int i = 0; i < 15; i++)
			  for(int j = 0; j < 15; j++)
				  board[i][j] = -1;
		  
		  chat = new JTextField(20);
		  chat.setText("Type here");
		  ta = new JTextArea(60, 60);
		  label = new JLabel();
		  colis = new JLabel();
		  chat.setBackground(Color.WHITE);	
		  ta.setBackground(Color.LIGHT_GRAY);
		  ta.setEditable(false);
		  
		  panel0 = new JPanel()
				  {
			public void paint(Graphics g)
			  {
				  super.paint(g);
				  
					chat.setBounds(650,575,300,50);
//					button4.setBounds(750, 675, 200, 50);
					scroll.setBounds(650, 50,300,500);
					button1.setBounds(50, 625, 150,50);
					button2.setBounds(225, 625, 150,50);
					button3.setBounds(400, 625, 150,50);
					chname.setBounds(400,680,150,30);
					label.setBounds(650, 25, 300, 25);
					colis.setBounds(915, 25, 200, 25);
					
						for(int i=0;i<=15;i++)
						{g.drawLine(0, 40*i,15*40,40*i);
						 g.drawLine(40*i,0,40*i,40*15);}
			         for (int row = 0; row < 15; row++)
			             for (int col = 0; col < 15; col++)
			             {
			                if((40*row<xclick &&40*(row+1)>xclick&&40*col<yclick&&40*(col+1)>yclick && board[row][col] == -1))
			                {
//			             	   	
//			                		System.out.println(row + "  " + col);
			             	   	board[row][col] = color;
			             	   	try {
									gc.sendMove(row, col);
//									System.out.println("sending sending sending ");
//									gc.turn = false;
									mouseOn = false;
								} catch (IOException e) {
									e.printStackTrace();
								}
			                }
			               
			                if(board[row][col] == 0)
			                {
			                		g.setColor(Color.white);
			             	   	g.fillOval(row*40+3, col*40+3, 34, 34);
			                }
			                else if(board[row][col] == 1)
			                {
			                		g.setColor(Color.black);
			             	   	g.fillOval(row*40+3, col*40+3, 34, 34);
			                }
			             }			        
			        xclick = 0;
			        yclick = 0;
			        AIrow = -1;
			        AIcol = -1;
				  }
			  
			  };
			  
			button1 = new JButton();
			button2 = new JButton();
			button3 = new JButton();
//			button4 = new JButton();
			button1.setText("GIVE UP");
			button2.setText("RESET GAME");
			button3.setText("Change name");
//			button4.setText("Get Paired");
			label.setText(oldname);
			colis.setText("");
			chname = new JTextField(80);
			chname.setText("Type name here");
			scroll  = new JScrollPane(ta);

//		
			
			panel0.setBackground(Color.LIGHT_GRAY);
			panel0.setSize(602,602);
//			chat.setBounds(650,575,300,50);
//			ta.setBounds(650, 50,300,500);
//			button1.setBounds(50, 625, 150,50);
//			button2.setBounds(225, 625, 150,50);
//			button3.setBounds(400, 625, 150,50);
//			chname.setBounds(400,680,150,30);
//			label.setBounds(650, 25, 300, 25);
//			colis.setBounds(875, 25, 200, 25);
			this.add(panel0,BoxLayout.X_AXIS);
			this.add(button1, BoxLayout.X_AXIS);
			this.add(button2, BoxLayout.X_AXIS);
			this.add(button3, BoxLayout.X_AXIS);
			this.add(chname, BoxLayout.X_AXIS);
			this.add(scroll,BoxLayout.X_AXIS);
			this.add(chat,BoxLayout.X_AXIS);
			this.add(label,BoxLayout.X_AXIS);
			this.add(colis, BoxLayout.X_AXIS);
//			this.add(button4,BoxLayout.X_AXIS);
			
			button1.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					gc.pout.println(losemsg);
					gc.pout.println(giveupmsg);
				}
			});
			button2.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					gc.pout.println(resetmsg);
				}
			});
			button3.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					 newname = chname.getText();
					 label.setText(newname);
					 label.setBounds(650, 25, 300, 25);
					 gc.pout.println(GomokuProtocol.generateChangeNameMessage(oldname, newname));
					 chname.setText("Type name here");
					 				
				}
			});
//			button4.addActionListener(new ActionListener() {
//				
//				@Override
//				public void actionPerformed(ActionEvent e) {
//					 gc.pair();					
//				}
//			});
			chat.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					 String msg = chat.getText();
					 gc.pout.println(GomokuProtocol.generateChatMessage(newname, msg));
					 ta.append(newname+": "+msg + "\n");
					 chat.setText("Type here");
				}
			});
			
		
		  panel0.addMouseListener(new MouseAdapter() {
	          public void mouseClicked(MouseEvent e) {
	        	  if(!mouseOn)
	        		  return;
	        	  int x=e.getX();
	        	  int y=e.getY();
	        	  setXclick(x);
	        	  setYclick(y);
	        	  panel0.repaint();
	        	   }
	      });
		  this.repaint();
		  }

	public static void main(String[] args)
	{
//      GameGUI g = new GameGUI();
	}
}
