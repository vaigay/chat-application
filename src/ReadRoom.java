
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
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
public class ReadRoom extends Thread{
    private Socket socket;
    private DataInputStream dis;
    private JTextArea jtextMess;
    private JTextArea jtextUser;

    public ReadRoom(Socket socket, DataInputStream dis, JTextArea jtextMess, JTextArea jtextUser) {
        this.socket = socket;
        this.dis = dis;
        this.jtextMess = jtextMess;
        this.jtextUser = jtextUser;
    }
    
    
    
    
    @Override
    public void run(){    
        while(true){
            try {
                if(socket.isClosed())
                    break;
                String type = dis.readUTF();
                System.out.println(type);
                if(type.equals("listUser")){
                    jtextUser.setText("");
                    int size = dis.readInt();
                    for(int i=0;i<size;i++){
                        String user = dis.readUTF();
                        jtextUser.append(user);
                        jtextUser.append("\n");
                    }
                }
                else{                   
                    jtextMess.append(type+"\n");
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                break;
            }
        }
    }
}
