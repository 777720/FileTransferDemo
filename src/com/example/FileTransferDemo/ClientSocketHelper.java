package com.example.FileTransferDemo;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by 720 on 2016/4/27.
 */
public class ClientSocketHelper {
    private String TAG="client";
    private String IP;
    private int port;
    private Socket socket=null;
    DataOutputStream out=null;
    DataInputStream in=null;

    public ClientSocketHelper(String ip,int port){
        this.IP=ip;
        this.port=port;
    }
    public void createConnection(){
        try {
            socket=new Socket(IP,port);

        }catch (UnknownHostException e1){
            e1.printStackTrace();
            if(socket!=null){
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        catch (IOException e) {
            e.printStackTrace();
            if(socket!=null){
                try {
                    socket.close();
                } catch (IOException e3) {
                    e3.printStackTrace();
                }
            }
        }

    }
    public void sendMessage(String sendMessage){
        try {
            out = new DataOutputStream(socket.getOutputStream());
            if (sendMessage.equals("Windows")) {
                out.writeByte(0x1);
                out.flush();
                return;
            }
            if (sendMessage.equals("Unix")) {
                out.writeByte(0x2);
                out.flush();
                return;
            }
            if (sendMessage.equals("Linux")) {
                out.writeByte(0x3);
                out.flush();
            } else {
                out.writeUTF(sendMessage);
                out.flush();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        }

    }
    public DataInputStream getMessageStream(){
        try {
            in=new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
            if(in!=null){
                try {
                    in.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return  in;

    }
    public void shutDownConnection(){
        try {
            if(out!=null){
                out.close();;
            }
            if (in!=null){
                in.close();
            }
            if (socket!=null){
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
