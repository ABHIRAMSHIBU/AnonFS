package cryptography;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class ClientKeyManagment{
	PublicKey pu;
	public ClientKeyManagment(PublicKey pu) {
		this.pu = pu;
	}
	public ClientKeyManagment(byte[] publicKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
		KeyFactory kf = KeyFactory.getInstance("RSA");
		this.pu = kf.generatePublic(new X509EncodedKeySpec(publicKey));
	}
	public byte[] encrypt(byte [] message) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
		Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWITHSHA-512ANDMGF1PADDING");
        cipher.init(Cipher.ENCRYPT_MODE, this.pu);
        byte[] cipherText = cipher.doFinal(message) ;
        return cipherText;
	}
	// NOTE Here i have skipped decrypt because its useless
	// TODO Add signing
}
