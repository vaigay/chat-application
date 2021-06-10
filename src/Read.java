
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextArea;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Hai
 */
public class Read extends Thread{
    private Socket socket;
    private DataInputStream dis;
    private JTextArea jtext;
    private DataOutputStream dos;
    public Read(Socket socket, DataInputStream dis,JTextArea jt) {
        this.socket = socket;
        this.dis = dis;
        jtext = jt;
        
        this.dos = dos;
        
    }
    
    
    
    @Override
    public void run(){    
        while(true){
            try {              
                if(socket.isClosed())
                    break;
                String x = dis.readUTF();
                jtext.append(x + "\n");
            } catch (IOException ex) {
                ex.printStackTrace();
                break;
            }
        }
    }
}
