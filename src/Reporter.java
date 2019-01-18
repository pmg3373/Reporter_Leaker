
import static edu.rit.util.Hex.toByteArray;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Reads encrypted messages sent on a specified host and port and writes them
 * @author Pat
 */
public class Reporter {
    private static DatagramSocket mailbox;
    private static byte[] keyCharSet;
    public static void main(String[] args) {
        if(args.length != 3){
            usage();
            System.exit(-1);
        }
        
        String host = args[0];
        int port = Integer.parseInt(args[1]);
        String key = args[2];
        if(key.length() != 32){
            System.out.println("Invalid Key");
            System.exit(-1);
        }
        try{
            keyCharSet = toByteArray(key);
        }
        catch(Exception e){
            System.out.println("Invalid Key");
            System.exit(-1);
        }
        
        ReporterModel model = new ReporterModel();
        try{
            mailbox = new DatagramSocket(new InetSocketAddress(host, port));
        }
        catch(Exception e){
            System.out.println("Error Establishing Mailbox");
            System.exit(-1);
        }
        LeakerProxy proxy = new LeakerProxy(mailbox, keyCharSet);
        proxy.setListener(model);
        proxy.start();
    }

    /**
     * Usage Message for this program
     */
    private static void usage() {
        System.out.println("Reporter <rhost> <rport> <key>");
    }
    
}
