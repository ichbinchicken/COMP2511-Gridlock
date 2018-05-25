import java.io.*;
import java.net.*;
import java.util.*;

/**
 * @author Sam Dodds
 *
 * Networking Module for Gridlock - Contains all Sockets, Comms for 2 player multiplayer
 */
public class NetworkModule implements Runnable {

    // Local and Remote Details
    private InetAddress remoteAddr;
    private int remotePort;
    private InetAddress localAddr;
    private int localPort;

    private ServerSocket servSock;
    private Socket socket;

    boolean amIHost;

    private InputStream input;
    private BufferedReader reader;
    private OutputStream output;
    private PrintWriter writer;

   // NetworkController controller;
    GameEngine engine;


    private Thread t;


    /**
     * Constructor to create Network Module
     * @param controller Network controller to communicate with game engine
     */

    public NetworkModule(GameEngine engine) {
        this.engine = engine;
    }


    /**
     * Set up Network module for hosting game
     *
     * @param port Port number to listen for incoming connections
     * @throws Exception
     */
    public void hostGame(int port) {
        try {
          servSock = new ServerSocket(port);

           this.localAddr = this.socket.getLocalAddress();
           this.localPort = this.socket.getLocalPort();
        } catch (IOException e) {
          e.printStackTrace();
        }
    }

    /**
     * Blocks until a game join request is detected - then initializes the connection
     */
    public void waitForJoin() {
        try {
			socket = servSock.accept();
		} catch (IOException e) {
			e.printStackTrace();
		}

        try {
			input = socket.getInputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
        reader = new BufferedReader(new InputStreamReader(input));

        try {
			output = socket.getOutputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
        writer = new PrintWriter(output, true);

        this.localAddr = this.socket.getLocalAddress();
        this.localPort = this.socket.getLocalPort();

        this.startRecv();

    }

    /**
     * Connects to a remote host Game
     *
     * @param remoteAddr IP address of game to connect to
     * @param remotePort Port of Game to connect to
     * @throws Exception
     */
    public void joinGame(InetAddress remoteAddr, int remotePort) {
        try {
         Socket socket = new Socket(remoteAddr, remotePort);

         input = socket.getInputStream();
         reader = new BufferedReader(new InputStreamReader(input));
         output = socket.getOutputStream();
         writer = new PrintWriter(output, true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.startRecv();

    }

    /**
     * Sends a start Game message to the other player, with a starting gameboard
     * @param newBoard new Board state to send
     */
    public void startGame(GameBoard newBoard) {
        writer.println("S " + newBoard.toString());
    }

    /**
     * Sends a new move to the other player
     * @param newBoard new Board State to send
     */
    public void makeMove(GameBoard newBoard) {
        writer.println("M " + newBoard.toString());
    }

    /**
     * Sends a game won message to the other player
     */
    public void winGame() {
        try {
            writer.println("W");
            socket.close();

        } catch (Exception e) {

        }
        t.interrupt();
    }

    /**
     * Starts the reciever message thread
     */
    public void startRecv() {
        if (t == null) {
            t = new Thread (this, "recvThread");
            t.start();
        }
    }

    /**
     * Looping message recieve function
     */
    public void run() {
        String message=null;

        while (true) {
            if (Thread.interrupted()) {
                return;
            }
			try {
				message = reader.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}    // reads a line of text

            Scanner sc = new Scanner(message);

            String command = sc.next();

            if (command == "S") {
                //engine.startGame(sc.nextLine());

            } else if (command == "M") {
               // engine.move(sc.nextLine());

            } else if (command == "W") {
                try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
               // engine.lose();
                return;
            }
        }
    }

    /**
     * Returns the IP address that the Host is listening on as a String
     * @return Host IP address
     */
    public String getLocalAddr() {
        return localAddr.toString();
    }

}



