
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
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
class ServerMessage extends Thread {

    private String you;
    private String chatWith;
    private Server server;
    private Socket socket;
    private DataInputStream dis;
    private DataOutputStream dos;

    public String getYou() {
        return you;
    }

    public ServerMessage(String you, String chatWith, Server server, Socket socket) throws IOException {
        this.you = you;
        this.chatWith = chatWith;
        this.server = server;
        this.socket = socket;
        this.dis = new DataInputStream(socket.getInputStream());
        this.dos = new DataOutputStream(socket.getOutputStream());

    }

    @Override
    public void run() {
        while (true) {
            try {
                String mess = dis.readUTF();
                server.broadCast(this, mess);
            } catch (IOException ex) {
                server.removeChat(this);
                ex.printStackTrace();
                break;
            }

        }
    }

    public Socket getSocket() {
        return socket;
    }

    public String getChatWith() {
        return chatWith;
    }

    public void getMess(String mess) throws IOException {
        dos.writeUTF(mess);
    }

}
