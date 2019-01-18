/**
 * Class RC4 implements Rivest's RC4 stream cipher.
 *
 * @author  Alan Kaminsky
 * @version 18-Apr-2017
 */
public class RC4
	{
	private byte[] S = new byte [256];
	private int x;
	private int y;

	/**
	 * Set the key and the nonce for this stream cipher. The keystream generator
	 * is initialized, such that successive calls to <TT>encrypt()</TT> or
	 * <TT>decrypt()</TT> will encrypt or decrypt a series of plaintext or
	 * ciphertext bytes.
	 *
	 * @param  key    Key; must be a 16-byte array.
	 * @param  nonce  Nonce; must be a 16-byte array.
	 *
	 * @exception  IllegalArgumentException
	 *     (unchecked exception) Thrown if <TT>key</TT> is not a 16-byte array.
	 *     Thrown if <TT>nonce</TT> is not a 16-byte array.
	 */
	public void setKey
		(byte[] key,
		 byte[] nonce)
		{
		if (key.length != 16)
			throw new IllegalArgumentException
				("RC4.setKey(): key must be 16 bytes");
		if (nonce.length != 16)
			throw new IllegalArgumentException
				("RC4.setKey(): nonce must be 16 bytes");

		byte[] keyNonce = new byte [32];
		System.arraycopy (key, 0, keyNonce, 0, 16);
		System.arraycopy (nonce, 0, keyNonce, 16, 16);

		for (int i = 0; i <= 255; ++ i)
			S[i] = (byte)i;
		int j = 0;
		for (int i = 0; i <= 255; ++ i)
			{
			j = (j + S[i] + keyNonce[i & 31]) & 255;
			swap (i, j);
			}
		x = 0;
		y = 0;
		}

	/**
	 * Encrypt the given byte. Only the least significant 8 bits of <TT>b</TT>
	 * are used. The ciphertext byte is returned as a value from 0 to 255.
	 *
	 * @param  b  Plaintext byte.
	 *
	 * @return  Ciphertext byte.
	 */
	public int encrypt
		(int b)
		{
		return encryptOrDecrypt (b);
		}

	/**
	 * Decrypt the given byte. Only the least significant 8 bits of <TT>b</TT>
	 * are used. The plaintext byte is returned as a value from 0 to 255.
	 *
	 * @param  b  Ciphertext byte.
	 *
	 * @return  Plaintext byte.
	 */
	public int decrypt
		(int b)
		{
		return encryptOrDecrypt (b);
		}

	/**
	 * Encrypt or decrypt the given byte. Only the least significant 8 bits of
	 * <TT>b</TT> are used. If <TT>b</TT> is a plaintext byte, the ciphertext
	 * byte is returned as a value from 0 to 255. If <TT>b</TT> is a ciphertext
	 * byte, the plaintext byte is returned as a value from 0 to 255.
	 *
	 * @param  b  Plaintext byte (if encrypting), ciphertext byte (if
	 *            decrypting).
	 *
	 * @return  Ciphertext byte (if encrypting), plaintext byte (if decrypting).
	 */
	private int encryptOrDecrypt
		(int b)
		{
		x = (x + 1) & 255;
		y = (y + S[x]) & 255;
		swap (x, y);
		return (S[(S[x] + S[y]) & 255] ^ b) & 255;
		}

	/**
	 * Swap S[i] with S[j].
	 */
	private void swap
		(int i,
		 int j)
		{
		byte t = S[i];
		S[i] = S[j];
		S[j] = t;
		}
	}
