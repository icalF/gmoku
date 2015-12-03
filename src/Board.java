import java.io.*;
import java.util.*;

/**
 * Represents a Gobang game (board and score)
 */
public class Board {
  byte[] data;
  final static int w = 20;
  final static int h = 20;
  public int lastX = -1;
  public int lastY = -1;
  static final int dr[] = {-1,-1,0,1,1,1,0,-1};
  static final int dc[] = {0,-1,-1,-1,0,1,1,1};

  /**
   * Next player.
   */
  public byte turn = 'A';

  /**
   * Board changed by player turn
   * Bitmask flag (if true then certain player has not update the board)
   */
  public int changed = 0;

  /**
   * Is all player ready
   * Bitmask status
   */
  public int readyAll = 0;

  /**
   * Player list
   * Bitmask status
   */
  public int players = 0;

  /**
   * Constructor
   */
  public Board() {
    data = new byte[h * w];
  }

  /**
   * Reset board
   */
  public void reset() {
    Arrays.fill(data, (byte)0);
    readyAll = 0;
    turn = next(turn);
  }

  /**
   * Gets a cell
   * @param p Point
   * @return Cell value
   */
  public byte getCell(Point p) {
    return data[p.y * w + p.x];
  }

  /**
   * Gets a cell
   * @param x X coordinate
   * @param y Y coordinate
   * @return Cell value
   */
  public byte getCell(int x, int y) {
    return data[y * w + x];
  }

  /**
   * Sets a cell
   * @param p Point
   * @param v Value
   */
  public void setCell(Point p, byte v) {
    data[p.y * w + p.x] = v;
  }

  /**
   * Sets a cell
   * @param x X coordinate
   * @param y Y coordinate
   * @param v Value
   */
  public void setCell(int x, int y, byte v) {
    data[y * w + x] = v;
  }

  /**
   * Moves Point in direction.
   * @param p Point
   * @param d Direction (0-7, 0 is north, positive rotation (1 is NW and so on))
   * @return False if the Point slips off the edge of the board (always true when wraparound is active)
   */
  public boolean advance(Point p, int d) {
    p.y += dc[d];
    p.x += dr[d];

    if (p.y < 0) return false;
    if (p.y == h) return false;
    if (p.x < 0) return false;
    if (p.x == w) return false;
    return true;
  }

  /**
   * Counts the length of the streak of same values on the board in a direction
   * @param p Point of origin
   * @param dir Direction
   * @return Array of two integers - first is the length of the streak, second is the color that ends the streak (-1 if edge)
   */
  public int[] streak(Point p, int dir) {
    byte i, c = getCell(p);
    int limit = w;
    int l = 0;
    while(true) {
      if (!advance(p, dir))
        return new int[] {l, -1};
      if ((i = getCell(p)) != c)
        return new int[] {l, i};
      l ++;
      if (l == limit)
        return new int[] {l, c};
    }
  }

  /**
   * Prints a representation of the board on stdout
   * @param s Output stream
   */
  public void print(PrintStream s) {
    int x, y;
    for (y = 0; y < h; y ++) {
      for (x = 0; x < w; x ++) {
        s.print (" ");
        int stone = getCell(x, y);
        if (stone < (byte)'A' || stone > (byte)'T')
          s.print(".");
        else
          s.print(stone);
      }
      s.println();
    }
  }

  /**
   * Remove existing player
   */
  public void removePlayer(byte id) {
    players &= ~(1 << (id - (byte)'A'));
    readyAll &= ~(1 << (id - (byte)'A'));
  }

  /**
   * Add new player
   * @return byte new player's id
   */
  public byte addPlayer() {
    int i = 0;
    while (((players >>> i) & 1) == 1) { i++; if (i == 20) i = 0; }
    return (byte)(i + 'A');
  }

  /**
   * Returns the color of the next player
   * @return The color of the next player
   */
  public byte next(byte c) {
    int i = c - (byte)'A';
    while (((players >>> i) & 1) != 1) { i++; if (i == 20) i = 0; }
    return (byte)(i + 'A');
  }

  /**
   * Makes a move
   * @param p Point
   * @return -1 if the move is illegal, > 0 if the move wins the game
   */
  public int move(Point p) {
    if (getCell(p) != 0) return -1;
    setCell(p, turn);

    lastX = p.x;
    lastY = p.y;

    for (int d = 0; d < 4; d ++) {
      int s = 0;
      int st[] = streak(p.copy(), d);
      s = st[0];
      st = streak(p.copy(), d + 4);
      s += st[0];
      if (s > 3) {
        reset();
        return turn;
      }
    }

    turn = next(turn);
    return 0;
  }
}
