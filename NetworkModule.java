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
    private NetworkController controller;

    private Thread t;


    /**
     * Constructor to create Network Module
     * @param controller Network controller to communicate with game engine
     */
    public NetworkModule(GameEngine controller) {
        this.controller = controller;
    }


    /**
     * Set up Network module for hosting game
     *
     * @param port Port number to listen for incoming connections
     * @throws Exception
     */
    public void hostGame(int port) throws Exception {
         servSock = new ServerSocket(port);

          this.localAddr = this.socket.getLocalAddress();
          this.localPort = this.socket.getLocalPort();
    }

    /**
     * Blocks until a game join request is detected - then initializes the connection
     */
    public void waitForJoin() {
        socket = servSock.accept();

        input = socket.getInputStream();
        reader = new BufferedReader(new InputStreamReader(input));

        output = socket.getOutputStream();
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
    public void joinGame(InetAddress remoteAddr, int remotePort) throws Exception {
         Socket socket = new Socket(remoteAddr, remotePort);

         input = socket.getInputStream();
         reader = new BufferedReader(new InputStreamReader(input));
         output = socket.getOutputStream();
         writer = new PrintWriter(output, true);

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

        while (true) {
            if (Thread.interrupted()) {
                return;
            }
            String message = reader.readLine();    // reads a line of text

            Scanner sc = new Scanner(message);

            String command = sc.next();

            if (command == "S") {
                controller.startGame(sc.nextLine());

            } else if (command == "M") {
                controller.move(sc.nextLine());

            } else if (command == "W") {
                socket.close();
                controller.lose();
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



