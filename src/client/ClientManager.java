package client;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * @author gazandic
 */
public class ClientManager {
    public String userInput;
    public static ClientBoardView board;
    public Socket socket;
    final static String host = "localhost";
    public int portNumber;
    public boolean connected;
    public static int sizeboard = 20;
    public String chatNow = null;
    public boolean chatNowB = false;
    private boolean you;
    private String winorlose;
    public ClientLocal local;
    public final Object klienLock = new Object();


    public void start() throws IOException {
        System.out.println("Creating socket to '" + host + "' on port " + portNumber);
        board = new ClientBoardView();
        while (true) {
            socket = new Socket(host, portNumber);
            connected = true;
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            initLokal();
            System.out.println(br.readLine());
            String s;
            s = br.readLine();
            setWinorlose("jahat");
            while (s != null) {
                checkingandprint(s);
                s = br.readLine();
            }
        }
    }

    public void checkingandprint(String s) {
        if (s.contains("It's your t")) {
            setYou(true);
        }
        if (s.contains("It's your o")) {
            setYou(false);
        }
        if (s.contains("win")) {
            setWinorlose("lose");
        }
        if (s.contains("lose")) {
            setWinorlose("lose");
        }
        setBoard(s);
//           printBoard(s);
        System.out.println(s);
    }

    public String getWinorlose() {
        return winorlose;
    }

    public void setWinorlose(String s) {
        synchronized (klienLock) {
            winorlose = s;
        }
    }

    public void setYou(boolean b) {
        synchronized (klienLock) {
            you = b;
        }
    }

    public boolean getYou() {
        return you;
    }

    public void printBoard(String s) {
        if (s.contains("It's your ")) {
            board.printBoard();
        }
    }

    public void finalLokal() {
        local.interrupt();
    }

    public void initLokal() throws IOException {
        local = new ClientLocal();
        local.out = new PrintWriter(socket.getOutputStream(), true);
    }

    public void setBoard(String s) {
        for (int h = 0; h <= sizeboard - 1; h++) {
            if (s.contains(" " + h + " ")) {
                String sa = "";
                if (String.valueOf(h).length() == 1) {
                    sa = s.substring(3);
                } else if (String.valueOf(h).length() == 2) {
                    sa = s.substring(4);
                }
                int i = 0;
                while (i <= ((sizeboard - 1) * 2)) {
                    if (sa.toLowerCase().charAt(i) == '.') {
                        board.setBoardIJ((h), (i / 2), 0);
                    }
                    int stone = (byte) sa.charAt(i);
                    if (stone >= (byte) 'A' && stone <= (byte) 'T') {
                        stone -= 64;
                        System.out.println(stone);
                        board.setBoardIJ((h), (i / 2), stone);
                    }

                    i += 2;
                }
            }
        }
    }

}

