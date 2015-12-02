public class Player implements Runnable 
{
  Board board;
  PrintStream os;
  BufferedReader is;
  byte id;
  String

  /**
   * Prints command line help
   */
  public void help() {
    System.out.println("arguments:");
    System.out.println(" [serverport]");
    System.out.println();
  }

  /**
   * Command help
   * @param s Stream to print to
   */
  public void cmdHelp(PrintStream s) {
    s.println("ready            - ready for a new match");
    s.println("info             - game/match scores");
    s.println("Nnn..            - make a move ('A2', 'B15', etc)");
    s.println("resign           - resign");
    s.println("bye              - quit game");
    s.println("!                - display board again");
    s.println("?                - get this help text");
  }

  /**
   * Prints game scores/info
   * @param s Stream to print to
   */
  public void info(PrintStream s) {
    // s.println();
    // s.println("GAME SCORE - S: " + here + " C: " + there);

    // if (board != null) {
    //   s.println("CAPTURES   - B: " + board.getScore((byte) 'B') + " W: " + board.getScore((byte) 'W'));
    // } else {
    //   if (hereReady) s.println("server is ready");
    //   if (thereReady) s.println("client is ready");
    // }

    // s.println();
  }

  public Player(PrintStream os, BufferedReader is, byte id, Board board)
  {
    PrintStream os = this.os;
    BufferedReader is = this.is;
    byte id = this.id;
    Board board = this.board;

    do 
    {
      os.println("Are you ready to play? [Y/N]");
      String opt = is.readLine();

      // option validation
      if (!opt.equals("N") && !opt.equals("Y")
        && !opt.equals("n") && !opt.equals("y"))
        os.println("Option invalid. Type only \"Y\" and \"N\"");
    } 
    while (!opt.equals("Y") && !opt.equals("y"));
  }

  /**
   * Thread for player action-response
   */
  public void run() {
    try 
    {
      while (true) 
      {
        line = is.readLine();
        if (line == null) {
          System.out.println("Client "+ (char)id +" disconnected");
          System.exit(0);
        }

        line.toLowerCase();
        if (line.equals("bye")) {
          System.out.println("Client "+ (char)id +" quit");
          os.close();
          System.exit(0);
        }

        if (line.equals("?")) {
          cmdHelp(os);
          continue;
        }

        if (line.equals("!")) {
          if (board == null) 
            System.out.println("There is no match in progress");
          else printBoard(os);
          continue;
        }

        if (line.equals("info")) {
          info(os);
          continue;
        }

        if (line.equals("resign") && board != null) 
        {
          os.println("You resign");
          // System.out.println("!your opponent has resigned");
          board = null;
          ready = 
          here ++;
          info(os);
          info(System.out);
          hereBlack = false;
          continue;
        }

        if (line.length() > 0 && board != null) 
        {
          if (board.player == thereColor) 
          {
            int x = (byte) line.charAt(0) - (byte) 'a';
            int y;
            if (x >= 0 && x < dim) 
            {
              line = line.substring(1);
              try 
              {
                y = Integer.parseInt(line) - 1;
                if (y >= 0 && y < dim) {
                  int r = board.move(new Point(x, y));
                  if (r == -1) os.println("invalid move");
                  if (r == 0 || r == 1) {
                    printBoard(System.out);
                    printBoard(os);
                  }
                  if (r == 1) {
                    os.println("you win!");
                    System.out.println("you lose");
                    board = null;
                    hereReady = false;
                    thereReady = false;
                    there ++;
                    info(os);
                    info(System.out);
                    hereBlack = true;
                  }
                  continue;
                }
              } catch (NumberFormatException e) {
              }
            }
          }
        }

        os.println("?what");
      }
    } catch (Exception e) {
      System.out.println("Error occurred : " + e);
      System.exit(1);
    }
  }

  /**
   * Prints game board
   * @param s Stream to print to
   */
  public static void printBoard(PrintStream s, Byte player) 
  {
    int x, y;

    s.println();
    board.print(s);
    s.println();

    if (player == turn)
      s.println ("It's your turn");
    else
      s.println ("It's your opponent's turn");
  }
}