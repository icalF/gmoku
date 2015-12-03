import java.io.*;
import java.net.*;

/**
 * Main class
 */
public class Main {
  /**
   * Server port
   * default = 1200
   */
  static int serverPort = 2015;

  /**
   * Game board (null when no match is in progress)
   */
  public Board board = null;

  /**
   * Board dimension
   */
  public int dim;

  /**
   * Client socket output stream
   */
  public PrintStream[] os;

  /**
   * Client socket input stream
   */
  public PrintStream[] os;

  /**
   * Client number
   */
  public int clientNum;

  /**
   * Initialize game
   */
  public void gameInit() 
  {
    ready = 0;
    board = new Board();
  }

  /**
   * Prints command line help
   */
  public void help() {
    System.out.println("arguments:");
    System.out.println(" [serverport]");
    System.out.println();
  }

  /**
   * Entry point
   * @param argv Comand line arguments
   */
  public void main(String argv[]) 
  {
    if (argv.length > 1) {
      help();
      System.exit(1);
    }

    if (argv.length == 1)
      try {
        serverPort = Integer.parseInt(argv[0]);
        if (serverPort < 1 || serverPort > 65535) {
          System.out.println("Invalid server port number (must be about 1-65535)");
          System.exit(1);
        }
      } catch (NumberFormatException e) {
        System.out.println("Wrong number format");
        System.exit(1);
      }

    ServerSocket ss = null;
    String line;
    BufferedReader is = null;
    Socket cs = null;

    try {
      ss = new ServerSocket(serverPort);
    } catch (IOException e) {
      System.out.println("Server failure: " + e);
      System.exit(1);
    }

    System.out.print((char) 27 + "[2J" + (char) 27 + "[H");
    System.out.println("!waiting for client");
    try {
      cs = ss.accept();
      is = new BufferedReader(new InputStreamReader(cs.getInputStream()));
      os = new PrintStream(cs.getOutputStream());
    } catch (IOException e) {
      System.out.println("foiled! " + e);
      System.exit(1);
    }

    System.out.println("!client is connected");
    System.out.println("Type ? for help");
    System.out.println();

    os.print((char) 27 + "[2J" + (char) 27 + "[H");
    os.println("Welcome to a game of Gobang");
    os.println("Type ? for help");
    os.print("The board is " + dim + "x" + dim);
    if (w) os.print(" wraparound");
    os.println();
    os.println();

    gameInit();

    new Local().start();
  }
}