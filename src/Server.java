/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

/**
 *
 * @author Hai
 */
public class Server {

    public int port;
    public HashSet<ServerMessage> listChat;
    public HashSet<ServerControl> listControl;
    public ArrayList<Room> roomChat;
    public Server(int port) {
        this.port = port;
        listChat = new HashSet<>();
        listControl = new HashSet<>();
        roomChat = new ArrayList<>();
    }

   
    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public Room getRoom(String roomName){
        for(Room room : roomChat){
            if(room.getName().equals(roomName))
               return room;
        }
        return null;
    }
    

    
    public HashSet<ServerControl> getListControl() {
        return listControl;
    }

    public void setListControl(HashSet<ServerControl> listControl) {
        this.listControl = listControl;
    }


    public void run() {      
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            while (true) {
                Socket con = serverSocket.accept();
                DataInputStream dis = new DataInputStream(con.getInputStream());
                DataOutputStream dos = new DataOutputStream(con.getOutputStream());
                String choice = dis.readUTF();
                if(choice.equals("newUser"))
                    insertUser(con,dis,dos);
                if(choice.equals("newChat"))
                    newChat(con,dis,dos);
                if (choice.equals("addUserToRoom")) {
                    addUserToRoom(con,dis,dos);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void insertUser(Socket con,DataInputStream dis,DataOutputStream dos) throws IOException {    
        String userName = dis.readUTF();
        int mark = 0;
        for (ServerControl tmp : listControl) {
            
            if (tmp.getUserName().equals(userName)) {
                mark = 1;
                break;
            }
        }        
        dos.writeInt(mark);
        if (mark == 1) 
            return;
        ServerControl sc = new ServerControl(this, con, userName);
        listControl.add(sc);
        sc.start();
        
    }




    void broadCast(ServerMessage sm,String mess) throws IOException {
        for(ServerMessage tmp : listChat){
            System.out.println(tmp.getSocket().isClosed());
            if(tmp.getSocket().isClosed()){
                listChat.remove(tmp);
                continue;
            }
            if(tmp.getYou().equals(sm.getChatWith()) && tmp.getChatWith().equals(sm.getYou())){
                tmp.getMess(mess);
                break;
            }
        }
        System.out.println("Successfully");
    }

    private void newChat(Socket con, DataInputStream dis, DataOutputStream dos) throws IOException {
        String chatUser = dis.readUTF();
        String you = dis.readUTF();
        ServerMessage serverMessage = new ServerMessage(you, chatUser,this,con);
        listChat.add(serverMessage);
        serverMessage.start();
    }

    public void removeUser(ServerControl tmp) {
        listControl.remove(tmp);
    }

    public void removeChat(ServerMessage aThis) {
        listChat.remove(aThis);       
    }

    

    private void addUserToRoom(Socket con, DataInputStream dis, DataOutputStream dos) throws IOException {
        String userName = dis.readUTF();
        String roomName = dis.readUTF();
        ServerRoomMessage sr = new ServerRoomMessage(userName, roomName, this, con);
        System.out.println(roomName);
        sr.start();
        for(Room room : roomChat){
            if(room.getName().equals(roomName)){
                room.addUser(sr);
                break;
            }
        }
    }

    void broadCastRoom(ServerRoomMessage aThis, String mess, String roomName,String user) throws IOException {
        for(Room room : roomChat){
            if(room.getName().equals(roomName)){
                HashSet<ServerRoomMessage> x = room.getUserInRoom();
                for(ServerRoomMessage romMessage : x){
                    System.out.println(romMessage.getYou());
                    if(user.equals(romMessage.getYou()))
                        continue;
                    romMessage.getMess(mess);
                }
            }
        }
    }

    

    

    
    
    
    
}
