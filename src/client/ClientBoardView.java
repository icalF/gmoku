package client;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * @author gazandic
 */
public class ClientBoardView {
    private String InputUser;
    private int[][] Data;
    private final Object LockBoard = new Object();

    public ClientBoardView() {
        Data = new int[20][20];
    }

    public void setBoardIJ(int i, int j, int s) {
        synchronized (LockBoard) {
            Data[i][j] = s;
        }
    }

    public void printBoard() {
        for (int i = 0; i < Data.length; i++) {
            for (int j = 0; j < Data.length; j++) {
                System.out.print(Data[i][j] + " ");
            }
            System.out.println("");
        }
    }

    public int getBoardIJ(int i, int j) {
        return Data[i][j];
    }

    public void setInput(String s) {
        synchronized (LockBoard) {
            InputUser = s;
        }
    }

    public String getInput() {
        return InputUser;
    }
}
