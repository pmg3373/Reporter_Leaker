
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.Random;
import edu.rit.util.Hex;
import static edu.rit.util.Hex.toByteArray;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Sends encrypted messages from one host and port to another
 * @author Pat
 */
public class Leaker {
    private static byte[] keyCharSet;
    public static void main(String[] args) {
        
        if(args.length != 6){
            usage();
            System.exit(-1);
        }
        
        //Ports and Hosts
        String reporterHost = args[0];
        int reporterPort = Integer.parseInt(args[1]);
        String leakerHost = args[2];
        int leakerPort = Integer.parseInt(args[3]);
        
        //The Key
        String key = args[4];
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
        
        //Message
        String message = args[5];
        
        try{
            DatagramSocket leaker = 
                    new DatagramSocket(new InetSocketAddress(leakerHost, leakerPort));
            ReporterProxy proxy = new ReporterProxy(leaker, reporterHost, reporterPort, keyCharSet);
            proxy.sendMessage(message);
        }
        catch(Exception e){
            System.out.println("ERROR: Unable to establish connection with reporter");
            System.exit(-2);
        }
        
    }

    /**
     * Usage message for this program
     */
    private static void usage() {
        System.out.println("Leaker <rhost> <rport> <lhost> <lport> <key> <message>");
    }
}
