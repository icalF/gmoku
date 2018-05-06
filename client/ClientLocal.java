package client;

import java.io.PrintWriter;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * @author gazandic
 */
public class ClientLocal extends Thread {
    public PrintWriter out;

    //  public String userInput;
//  public BufferedReader userInputBR ;
//  public final Object Lock = new Object(); 
    public void run() {
//    userInputBR = new BufferedReader(new InputStreamReader(System.in));
//    while(true){ 
//        try {
//             userInput = userInputBR.readLine();
//             
//        } catch (IOException ex) {
//          Logger.getLogger(client.ClientLocal.class.getName()).log(Level.SEVERE, null, ex);
//        }
        if (ClientManager.board.getInput() != null) {
            out.println(ClientManager.board.getInput());
            System.out.println(ClientManager.board.getInput());
            ClientManager.board.setInput(null);
        }


//    }
    }
//  public void setString(String s){
//    synchronized(Lock){
//      userInput = s;
//    }
//  }

}
