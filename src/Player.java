import java.io.*;

public class Player implements Runnable
{
  Board board;
  PrintStream os;
  BufferedReader is;
  byte id;

  public Player(PrintStream os, BufferedReader is, byte id, Board board)
  {
    this.os = os;
    this.is = is;
    this.id = id;
    this.board = board;
    board.readyAll |= (1 << (id - (byte)'A'));
  }

  /**
   * Thread for player action-response
   */
  public void run() {
    String line;
    try 
    {
      while (true)
      {
        boolean playNow = false;

        while (!playNow) {
          do { line = is.readLine(); } while (line.length() < 1);
          if (line.equals("ready"))
          {
            board.readyAll ^= (1 << (id - (byte)'A'));
            playNow = true;
          }
          os.println(id);
        }

        while (playNow)
        {
          line = is.readLine();
          if (line.length() > 0)
          {
            if (board.turn == id)           // check turn
            {
              int x = (byte) line.charAt(0) - (byte) 'a';
              int y = (byte) line.charAt(1) - (byte) 'a';
              if ((x >= 0 && x < board.w) && (y >= 0 && y < board.w))
              {
                int r = board.move(new Point(x, y));
                if (r == -1)
                  os.println("Invalid move");
                else if (r == 0 || r == 1) 
                  printBoard(os);
                else {
                  os.println(r == id ? "Win" : "Lose");
                  playNow = false;
                }
                continue;
              }
            }
          }
        }
      }
    } catch (NullPointerException e) {
    } catch (IOException e) {
    } catch (RuntimeException e) {
      System.out.println("Error occurred : " + e);
      System.exit(1);
    }
  }

  /**
   * Prints game board
   * @param s Stream to print to
   */
  public void printBoard(PrintStream s) 
  {
    int x, y;

    s.println();
    board.print(s);
    s.println();

    if (id == board.turn)
      s.println ("It's your turn");
    else
      s.println ("It's your opponent's turn");
  }
}