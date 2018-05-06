package org.gomokuton.actor;

import org.gomokuton.adt.Board;

import java.io.BufferedReader;
import java.io.PrintStream;

/**
 * Created by icalF on 1/29/2018.
 */
public class Bot extends Player {
    public Bot(PrintStream os, BufferedReader is, Board board) {
        super(os, is, board);
    }
}
