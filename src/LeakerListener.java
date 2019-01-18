/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Pat
 */
public interface LeakerListener {
    
    /**
     * Sets the key for RC4 operations
     * @param key
     * @param nonce 
     */
    public void setKey(byte[] key, byte[] nonce);
    
    /**
     * Decrypts an array of bytes using rc4
     * Assumes an RC4 key has been set up
     * @param encrypted The encrypted byte array
     * @return The original message as a byte array
     */
    public byte[] decrypt(byte[] encrypted);
    /**
     * Encrypts an array of bytes using RC4
     * Assumes an RC4 key has been set up
     * @param message The message to encrypt as a byte array
     * @return The encrypted message
     */
    public byte[] encrypt(byte[] message);
}
