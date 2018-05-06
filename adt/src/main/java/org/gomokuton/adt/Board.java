package org.gomokuton.adt;

import java.io.*;
import java.util.*;

/**
 * Represents a Gobang game (adt)
 *
 * @author Afrizal
 */
public class Board {
    public static final int WIDTH = 19;
    public static final int HEIGHT = 19;

    final int dr[] = {-1, -1, 0, 1, 1, 1, 0, -1};
    final int dc[] = {0, -1, -1, -1, 0, 1, 1, 1};

    byte[] data;

    public final Object playerLock;               // lock for all player state

    /**
     * Next player.
     */
    public byte turn = 'A';

    /**
     * adt.Board changed by player turn
     * Bitmask flag (if true then certain player has not update the adt)
     */
    public int changed = 0;

    /**
     * Is all player ready
     * Bitmask status
     */
    public int readyAll = 0;

    /**
     * actor.Player list
     * Bitmask status
     */
    public int players = 0;

    /**
     * Constructor
     */
    public Board() {
        playerLock = new Object();
        data = new byte[HEIGHT * WIDTH];
    }

    /**
     * Reset adt
     */
    public void reset() {
        Arrays.fill(data, (byte) 0);
        synchronized (playerLock) {
            readyAll = 0;
        }
        turn = next(turn);
    }

    /**
     * Gets a cell
     *
     * @param p adt.Point
     * @return Cell value
     */
    public byte getCell(Point p) {
        return data[p.y * WIDTH + p.x];
    }

    /**
     * Gets a cell
     *
     * @param x X coordinate
     * @param y Y coordinate
     * @return Cell value
     */
    public byte getCell(int x, int y) {
        return data[y * WIDTH + x];
    }

    /**
     * Sets a cell
     *
     * @param p adt.Point
     * @param v Value
     */
    public void setCell(Point p, byte v) {
        data[p.y * WIDTH + p.x] = v;
    }

    /**
     * Sets a cell
     *
     * @param x X coordinate
     * @param y Y coordinate
     * @param v Value
     */
    public void setCell(int x, int y, byte v) {
        data[y * WIDTH + x] = v;
    }

    /**
     * Moves adt.Point in direction.
     *
     * @param p adt.Point
     * @param d Direction (0-7, 0 is north, positive rotation (1 is NW and so on))
     * @return False if the adt.Point slips off the edge of the adt (always true when wraparound is active)
     */
    public boolean advance(Point p, int d) {
        p.y += dc[d];
        p.x += dr[d];

        if (p.y < 0) return false;
        if (p.y == HEIGHT) return false;
        if (p.x < 0) return false;
        if (p.x == WIDTH) return false;
        return true;
    }

    /**
     * Counts the length of the streak of same values on the adt in a direction
     *
     * @param p   adt.Point of origin
     * @param dir Direction
     * @return Array of two integers - first is the length of the streak, second is the color that ends the streak (-1 if edge)
     */
    public int[] streak(Point p, int dir) {
        byte i, c = getCell(p);
        int limit = WIDTH;
        int l = 0;
        while (true) {
            if (!advance(p, dir))
                return new int[]{l, -1};
            if ((i = getCell(p)) != c)
                return new int[]{l, i};
            l++;
            if (l == limit)
                return new int[]{l, c};
        }
    }

    /**
     * Prints a representation of the adt on stdout
     *
     * @param s Output stream
     */
    public void print(PrintStream s) {
        int x, y;
        for (y = 0; y < HEIGHT; y++) {
            s.print(" " + y);
            for (x = 0; x < WIDTH; x++) {
                s.print(" ");
                int stone = getCell(x, y);
                if (stone < (byte) 'A' || stone > (byte) 'T')
                    s.print(".");
                else
                    s.print((char) stone);
            }
            s.println();
        }
    }

    /**
     * Remove existing player
     */
    public void removePlayer(byte id) {
        synchronized (playerLock) {
            players &= ~(1 << (id - (byte) 'A'));
            readyAll &= ~(1 << (id - (byte) 'A'));
        }
    }

    /**
     * Add new player
     *
     * @return byte new player's id
     */
    public byte addPlayer() {
        int i = 0;
        synchronized (playerLock) {
            while (i < 20 && ((players >>> i) & 1) == 1) i++;
            players |= (1 << i);
        }
        if (i == 20) return -1;
        return (byte) (i + 'A');
    }

    /**
     * Returns the color of the next player
     *
     * @return The color of the next player
     */
    public byte next(byte c) {
        int i = c - (byte) 'A';
        do {
            i++;
            if (i == 20) i = 0;
        } while (((players >>> i) & 1) != 1);
        return (byte) (i + 'A');
    }

    /**
     * Makes a move
     *
     * @param p adt.Point
     * @return -1 if the move is illegal, > 0 if the move wins the game
     */
    public int move(Point p) {
        if (getCell(p) != 0) return -1;
        setCell(p, turn);

        for (int d = 0; d < 4; d++) {
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
