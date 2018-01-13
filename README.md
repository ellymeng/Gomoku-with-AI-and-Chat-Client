# Gomoku with AI Mode and Chat Client

This game is a client-server program that exchanges gameplay data, gameboard updates, and chat messages via a TCP/IP network. Users can play in either human or AI mode. In human mode, the user gets paired with another user who joins the server. In AI mode, the user's AI gets paired with another user's AI who joins the server. The AI next-best-move algorithm was written from scratch using a zero-sum game (minimax) model. 



<b>USER MANUAL<b>

<I>IP address: 10.120.3.147 || port number: 12311 ||
How to connect to our service: java <clientName> 10.120.3.147 12311</I>

Instruction of how to play in the human mode

**note: if you have just played in AI vs. AI mode, close and reopen the server before executing steps 2-5 below.  
1.	Run the GameServer
2.	Add a player pair by running the GameClient twice 
a.	Each time, when prompted to choose human or AI in the popup box, choose human 
3.	To change name type name where it says “type name here” and press change name
4.	To reset the game, press reset button
 .	Resetting means you will still be facing the same opponent just on a cleared board
5.	To give up current game, press give up button
 .	You will be forfeiting and will gain a loss
6.	To talk to opponent type in “type here” and press enter
 

Instruction of how to play in the AI mode

**note: if you have just played in human vs. human mode, close and reopen the server before executing steps 2-5 below.  
1.	Run the GameServer
2.	Add a player pair by running the GameClient twice
a.	Each time, when prompted to choose human or AI in the popup box, choose AI 
3.	The name of the AI is set automatically upon connecting to the server
4.	Let game run until a terminal state is reached and the server displays a popup box with the appropriate update (either one of the AI wins and the other loses, or there is a tie)
5.	Upon exiting the terminal popup box, the game ends and returns to an idle state




