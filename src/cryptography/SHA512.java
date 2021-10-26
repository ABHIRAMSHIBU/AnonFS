package cryptography;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA512 {
	public static byte[] digest(byte [] in) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("SHA-512");
		byte[] messageDigest = md.digest(in);
		return messageDigest;
	}
	public static boolean verify(byte [] in, byte [] checksum) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("SHA-512");
		byte[] messageDigest = md.digest(in);
		if(messageDigest.length != checksum.length) {
			return false;
		}
		else {
			for(int i=0; i<messageDigest.length; i++) {
				if(messageDigest[i] != checksum[i]) {
					return false;
				}
			}
		}
		return true;
	}
}
