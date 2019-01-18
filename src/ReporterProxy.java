
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.Random;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Pat
 */
public class ReporterProxy implements LeakerListener {
    String reporterHost;
    int reporterPort;
    DatagramSocket leaker;
    RC4 encryptor;
    byte[] key;
    public ReporterProxy(DatagramSocket leaker, String reporterHost, int reporterPort, byte[] key){
        this.leaker = leaker;
        this.reporterPort = reporterPort;
        this.reporterHost = reporterHost;
        this.key = key;
        encryptor = new RC4();
    }

     /**
     * Sets the key for RC4 operations
     * @param key
     * @param nonce 
     */
    public void setKey(byte[] key, byte[] nonce) {
        encryptor.setKey(key, nonce);
    }

    /**
     * Decrypts an array of bytes using rc4
     * Assumes an RC4 key has been set up
     * @param encrypted The encrypted byte array
     * @return The original message as a byte array
     */
    public byte[] decrypt(byte[] encrypted) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

     /**
     * Encrypts an array of bytes using RC4
     * Assumes an RC4 key has been set up
     * @param message The message to encrypt as a byte array
     * @return The encrypted message
     */
    public byte[] encrypt(byte[] message) {
        byte[] encBytes = new byte[message.length];
        for(int i = 0; i < encBytes.length; i++){
            encBytes[i] = (byte)encryptor.decrypt(message[i]);
        }
        return encBytes;
    }
    
    /**
     * Randomly generates a 16 bit nonce for RC4
     * @return The nonce
     */
    private byte[] pickNonce(){
        byte[] nonce = new byte[16];
        Random gen = new Random();
        gen.nextBytes(nonce);
        return nonce;
    }
    
    /**
     * Encrypts and sends a message to the host and port set in this object
     * @param message The message to send
     */
    public void sendMessage(String message){
        try{
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            DataOutputStream stream = new DataOutputStream(out);
            byte[] nonce = pickNonce();
            encryptor.setKey(key, nonce);
            byte[] charSet = message.getBytes(Charset.forName("UTF-8"));
            byte[] encryptCharSet = encrypt(charSet);
            stream.write(nonce);
            stream.write(encryptCharSet);
            byte[] messageFull = out.toByteArray();
            leaker.send(new DatagramPacket(messageFull, messageFull.length, 
                    new InetSocketAddress(reporterHost, reporterPort)));
        }
        catch(Exception e){
            System.out.println("Error Writing Message");
            System.exit(-1);
        }
    }
}
