
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Hai
 */
class ServerControl extends Thread {
    
    private String userName;
    private Server server;
    private Socket socket;
    
    public Socket getSocket() {
        return socket;
    }
    private DataInputStream dis;
    private DataOutputStream dos;
    
    public ServerControl(Server server, Socket socket, String userName) throws IOException {
        this.userName = userName;
        this.server = server;
        this.socket = socket;
        this.dis = new DataInputStream(socket.getInputStream());
        this.dos = new DataOutputStream(socket.getOutputStream());
        
    }
    
    public String getUserName() {
        return userName;
    }
    
    @Override
    public void run() {
        System.out.println(socket.isConnected());
        System.out.println(server);
        try {
            
            while (true) {
                if (socket.isClosed()) {
                    break;
                }
                String choice = dis.readUTF();
                if (choice.equals("sendListRoom")) {
                    sendListRoom();
                }
                if (choice.equals("sendListUser")) {
                    sendListUser();
                }
                if (choice.equals("newRoom")) {
                    newRoom();
                }
                if (choice.equals("checkUser")) {
                    checkUser();
                }
                if (choice.equals("close")) {
                    close();
                }             
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            
        }
    }
    
    public Object[] toObject() {
        return new Object[]{this.userName};
    }
    
    private void sendListUser() throws IOException {
        String userName = dis.readUTF();        
        for (ServerControl tmp : this.server.getListControl()) {
            if (tmp.getSocket().isClosed()) {
                System.out.println("Remove");
                server.removeUser(tmp);
                continue;
            } else if (!tmp.getUserName().equals(userName)) {
                dos.writeUTF(tmp.getUserName());
            }
        }
        dos.writeUTF("endUser");
        System.out.println("thanh cong gui");
        
    }
    
    private void checkUser() throws IOException {
        String user = dis.readUTF();
        System.out.println("user");
        for (ServerControl tmp : server.getListControl()) {
            if (tmp.getUserName().equals(user)) {
                if (tmp.getSocket().isClosed()) {                    
                    server.removeUser(tmp);
                    dos.writeInt(0);
                } else {
                    dos.writeInt(1);
                }
                break;
            }
        }
    }
    
    private void close() throws IOException {
        server.removeUser(this);
        socket.close();
    }
    
    private void newRoom() throws IOException {
        String roomName = dis.readUTF();
        int mark = 0;
        for (Room room : server.roomChat) {
            if (room.getName().equals(roomName)) {
                mark = 1;
                break;
            }
        }
        dos.writeInt(mark);
        if (mark == 1) {
            return;
        }
        Room room = new Room(roomName);
        server.roomChat.add(room);
        System.out.println("Tao phong thanh cong: " + room.getName());
    }
    
    private void sendListRoom() throws IOException {
        for (Room room : server.roomChat) {
            dos.writeUTF(room.getName());
            dos.writeInt(room.numberUserInRoom());
        }
        dos.writeUTF("endRoom");
        dos.writeInt(-1);
        System.out.println("Gui thong tin");
    }

    

    
    
}
