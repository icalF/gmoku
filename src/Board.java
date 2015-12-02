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
  public byte player = 'A';

  /**
   * Is game over.
   */
  public boolean gameOver = false;

  /**
   * Score each player
   */
  public int[] score;

  /**
   * Constructor
   */
  public Board() {
    score = new int[20];
    data = new byte[h * w];
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
   * Get score (number of captured stones so far)
   * @param p Player ('A' - 'T')
   */
  public int getScore(byte p) {
    int playerId = p - 'A';
    if (playerId < 0 || playerId > 19)
      return -1;
    return score[playerId];
  }

  /**
   * Set score
   * @param p Player ('A' - 'T')
   * @param s Score
   */
  void setScore(byte p, int s) {
    int playerId = p - 'A';
    if (playerId < 0 || playerId > 19)
      return;
    score[playerId] = s;
  }

  /**
   * Increments score (by 2)
   * @param p Player ('A' - 'T')
   */
  void incScore(byte p) {
    int playerId = p - 'A';
    score[playerId] += 2;
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
   * Returns the color of the next player
   * @return The color of the next player
   */
  public byte next(byte c) {
    return (byte)((c - 'A' + 1) % 20 + 'A');
  }

  /**
   * Makes a move
   * @param p Point
   * @return -1 if the move is illegal, 1 if the move wins the game
   */
  public int move(Point p) {
    if (gameOver) return -1;
    if (getCell(p) != 0) return -1;
    setCell(p, player);

    lastX = p.x;
    lastY = p.y;

    for (int d = 0; d < 8; d ++) {
      Point q = p.copy();
      if (!advance(q, d)) continue;
      if (getCell(q) != player) {
        int st[] = streak(q.copy(), d);
        if (st[0] == 1 && st[1] == player) {
          setCell(q, (byte) 0);
          advance(q, d);
          setCell(q, (byte) 0);
          incScore(player);
        }
      }
    }

    if (getScore(player) >= 10) {
      gameOver = true;
      return 1;
    }

    for (int d = 0; d < 4; d ++) {
      int s = 0;
      int st[] = streak(p.copy(), d);
      s = st[0];
      st = streak(p.copy(), d + 4);
      s += st[0];
      if (s > 3) {
        gameOver = true;
        return 1;
      }
    }

    player = next(player);

    return 0;
  }
}
