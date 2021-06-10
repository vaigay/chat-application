/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
/**
 *
 * @author Hai
 */
public class Room implements Serializable{
    private String name;
    private HashSet<ServerRoomMessage> userInRoom;

    public Room( String name) {
        this.name = name;
        this.userInRoom = new HashSet<ServerRoomMessage>();
    }
    
    public int numberUserInRoom(){
        return userInRoom.size();
    }
    
    public void addUser(ServerRoomMessage e){
        this.userInRoom.add(e);
    }

    public HashSet<ServerRoomMessage> getUserInRoom() {
        return userInRoom;
    }

    public String getName() {
        return name;
    }
    
    public void removeUser(ServerRoomMessage srm){
        userInRoom.remove(srm);
    }
}
