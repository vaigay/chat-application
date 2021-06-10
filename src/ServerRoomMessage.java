
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashSet;
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
public class ServerRoomMessage extends Thread {

    private String you;
    private String roomName;
    private Server server;
    private Socket socket;
    private DataInputStream dis;
    private DataOutputStream dos;

    public ServerRoomMessage(String you, String room, Server server, Socket socket) throws IOException {
        this.you = you;
        this.roomName = room;
        this.server = server;
        this.socket = socket;
        dis = new DataInputStream(socket.getInputStream());
        dos = new DataOutputStream(socket.getOutputStream());
    }

    public void run() {
        listUserInEveryRoom();
        while (true) {       
            try {
                String type = dis.readUTF();
                System.out.println(type);
                if(type.equals("mess")){
                    String mess = dis.readUTF();
                    server.broadCastRoom(this, mess, roomName,you);
                    System.out.println(mess+" "+ roomName);
                }
                if(type.equals("userOutRoom")){
                    String mess = dis.readUTF();
                    server.broadCastRoom(this, mess, roomName,you);
                    userOutRoom();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                break;
            }

        }
    }

    public String getYou() {
        return you;
    }

    public Socket getSocket() {
        return socket;
    }

    public String getRoomName() {
        return roomName;
    }

    public void getMess(String mess) throws IOException {
        dos.writeUTF(mess);
    }

    public void listUserInRoom() throws IOException {
        dos.writeUTF("listUser");
        Room room = server.getRoom(roomName);
        HashSet<ServerRoomMessage> srm = room.getUserInRoom();
        dos.writeInt(srm.size());
        for(ServerRoomMessage tmp : srm){
            dos.writeUTF(tmp.getYou());
        }
    }
    
    public void listUserInEveryRoom(){
        try {
            Room e = server.getRoom(roomName);
            HashSet<ServerRoomMessage> srm = e.getUserInRoom();
            for(ServerRoomMessage tmp : srm){
                tmp.listUserInRoom();
            }
        } catch (IOException ex) {
        }
    }

    private void userOutRoom() {
         Room e = server.getRoom(roomName);
         e.removeUser(this);
         listUserInEveryRoom();
         this.stop();       
    }

}
