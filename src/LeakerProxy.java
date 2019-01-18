
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.charset.Charset;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Pat
 */
public class LeakerProxy {
    DatagramSocket mailbox;
    LeakerListener listener;
    byte[] key;
    
    public LeakerProxy(DatagramSocket mailbox, byte[] key){
        this.mailbox = mailbox;
        this.key = key;
    }
    
    public void setListener(LeakerListener listener){
        this.listener = listener;
    }
    
    public void start(){
        new ReaderThread().start();
    }
    
    private class ReaderThread extends Thread{
        public void run(){
            while(true){
                try{
                    byte[] payload = new byte[1024];
                    DatagramPacket incoming = new DatagramPacket(payload, payload.length);
                    mailbox.receive(incoming);
                    DataInputStream stream = new DataInputStream(
                            new ByteArrayInputStream(payload, 0, incoming.getLength()));
                    if(incoming.getLength() > 15){
                        byte[] nonce = new byte[16];
                        for (int i = 0; i < 16; i++) {
                            nonce[i] = stream.readByte();
                        }
                        stream.read(payload);
                        byte[] encBytes = new byte[incoming.getLength() - 16];
                        for(int i = 0; i < encBytes.length; i++){
                            encBytes[i] = payload[i];
                        }
                        try{
                            listener.setKey(key, nonce);
                            byte[] decBytes = listener.decrypt(encBytes);
                            String message = new String(decBytes, Charset.forName("UTF-8"));
                            System.out.println("" + message);
                        }
                        catch(Exception e){

                        }
                    }
                }
                catch(Exception e){
                    System.out.println("Error reading messages, aborting");
                    System.exit(-2);
                }
            }
        }
    }
}
