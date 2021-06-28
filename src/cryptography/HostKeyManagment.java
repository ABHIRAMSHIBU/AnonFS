package cryptography;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
public class HostKeyManagment{
	KeyPair keys;
	PublicKey pu;
	PrivateKey ps;
	public KeyPair createKey() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(4096);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        this.keys = keyPair;
        this.pu = keyPair.getPublic();
        this.ps = keyPair.getPrivate();
        return keyPair;
	}
	public void writeKeyPair() throws IOException {
		byte privkey[] = this.ps.getEncoded();
		byte pubkey[] = this.pu.getEncoded();
		System.out.println(keys.getPrivate().getFormat());
		System.out.println(keys.getPublic().getFormat());
		FileOutputStream privkeyFile = new FileOutputStream("hostkey");
		FileOutputStream pubkeyFile = new FileOutputStream("hostkey.pub");
		privkeyFile.write(privkey);
		pubkeyFile.write(pubkey);
		privkeyFile.close();
		pubkeyFile.close();
		
	}
	public KeyPair readKeyPair() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException{
		FileInputStream privkeyFile = new FileInputStream("hostkey");
		FileInputStream pubkeyFile = new FileInputStream("hostkey.pub");
		byte privkey[]=privkeyFile.readAllBytes();
		byte pubkey[]=pubkeyFile.readAllBytes();
		privkeyFile.close();
		pubkeyFile.close();
		KeyFactory kf = KeyFactory.getInstance("RSA");
		PrivateKey ps = kf.generatePrivate(new PKCS8EncodedKeySpec(privkey));
		PublicKey pu = kf.generatePublic(new X509EncodedKeySpec(pubkey));
		this.pu = pu;
		this.ps = ps;
		KeyPair kp = new KeyPair(pu, ps);
		this.keys = kp;
		return kp;
	}
	public PublicKey getPublicKey() {
		return this.pu;
	}
	public byte[] encrypt(byte [] message) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWITHSHA-512ANDMGF1PADDING");
        cipher.init(Cipher.ENCRYPT_MODE, this.pu);
        byte[] cipherText = cipher.doFinal(message) ;
        return cipherText;
	}
	public byte[] decrypt(byte [] message) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWITHSHA-512ANDMGF1PADDING");
        cipher.init(Cipher.DECRYPT_MODE, this.ps);
        byte[] cipherText = cipher.doFinal(message) ;
        return cipherText;
	}
	// TODO Add signing
}