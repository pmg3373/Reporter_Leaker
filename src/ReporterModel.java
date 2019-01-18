
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
public class ReporterModel implements LeakerListener {
    
    RC4 decryptor;
    
    public ReporterModel(){
        decryptor = new RC4();
    }
    
     /**
     * Sets the key for RC4 operations
     * @param key
     * @param nonce 
     */
    public void setKey(byte[] key, byte[] nonce){
        try{
            decryptor.setKey(key, nonce);
        }
        catch(Exception e){
            System.out.println("Key or nonce is invalid");
        }
    }
    
     /**
     * Decrypts an array of bytes using rc4
     * Assumes an RC4 key has been set up
     * @param encrypted The encrypted byte array
     * @return The original message as a byte array
     */
    public byte[] decrypt(byte[] encrypted){
        byte[] decBytes = new byte[encrypted.length];
        for(int i = 0; i < encrypted.length; i++){
            decBytes[i] = (byte)decryptor.decrypt(encrypted[i]);
        }
        return decBytes;
    }
    
    /**
     * Encrypts an array of bytes using RC4
     * Assumes an RC4 key has been set up
     * @param message The message to encrypt as a byte array
     * @return The encrypted message
     */
    public byte[] encrypt(byte[] message){
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
