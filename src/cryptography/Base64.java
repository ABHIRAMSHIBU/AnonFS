package cryptography;

public class Base64 {
	
	/**
	 * Encodes data into base64 format
	 * @param datain
	 * @return a char array of encoded data
	 */
	public static char [] encode(byte [] datain) {
		return algorithm.ByteArrayTransforms.toCharArray(java.util.Base64.getEncoder().encode(datain));
	}
	
	/**
	 * Decodes data from base64 format
	 * @param datain
	 * @return a byte array of decoded data
	 */
	public static byte [] decode(char [] datain) {
		return java.util.Base64.getDecoder().decode(algorithm.ByteArrayTransforms.toByteArray(datain));
	}
}
