import java.io.*;
import java.net.*;

/**
 * Main class
 *
 * @author Afrizal
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
   * Prints command line help
   */
  public static void help() {
    System.out.println("arguments:");
    System.out.println(" [serverport]");
    System.out.println();
  }

  /**
   * Entry point
   * @param argv Comand line arguments
   */
  public static void main(String argv[]) 
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
    PrintStream os = null;
    BufferedReader is = null;
    Socket cs = null;
    Board board = new Board();

    try {
      ss = new ServerSocket(serverPort);
    } catch (IOException e) {
      System.out.println("Server failure: " + e);
      System.exit(1);
    }

    while (true) {
      try {
        cs = ss.accept();
        is = new BufferedReader(new InputStreamReader(cs.getInputStream()));
        os = new PrintStream(cs.getOutputStream());

        while (board.players != 0 && board.players == board.readyAll);          // game still running
        (new Thread(new Player(os, is, board))).start();                        // add new player to game
      } catch (IOException e) {
        System.out.println("Connection error :" + e);
        // System.exit(1);
      }
    }
  }
}