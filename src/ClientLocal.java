
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
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
//          Logger.getLogger(ClientLocal.class.getName()).log(Level.SEVERE, null, ex);
//        }
        if(Klien.board.getInput()!=null){
          out.println(Klien.board.getInput());
          System.out.println(Klien.board.getInput());
          Klien.board.setInput(null);
        }
        
        
        
//    }
  }
//  public void setString(String s){
//    synchronized(Lock){
//      userInput = s;
//    }
//  }
  
}
