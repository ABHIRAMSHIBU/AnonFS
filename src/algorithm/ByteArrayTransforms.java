package algorithm;

public class ByteArrayTransforms {
	/**
	 * Converts a byte array to char array
	 * @param in - byte array
	 * @return char array
	 */
	public static char [] toCharArray(byte [] in) {
		char [] out = new char[in.length];
		for(int i=0;i<out.length;i++) {
			out[i]=(char) in[i];
		}
		return out;
	}
	/**
	 * Converts a char array to byte array
	 * @param in - char array
	 * @return byte array
	 */
	public static byte [] toByteArray(char [] in) {
		byte [] out = new byte[in.length];
		for(int i=0;i<out.length;i++) {
			out[i]=(byte) in[i];
		}
		return out;
	}
	/**
	 * Converts a string to byte array
	 * @param in - string
	 * @return byte array
	 */
	public static byte [] toByteArray(String in) {
		char [] _in = in.toCharArray();
		byte [] out = new byte[in.length()];
		for(int i=0;i<out.length;i++) {
			out[i]=(byte) _in[i];
		}
		return out;
	}
	
	public static String toHexString(byte [] in) {
		String hexout = "";
		for(int i=0;i<in.length;i++) {
			try {
				String hex = Integer.toHexString(in[i]);
				if(hex.length()==1) {
					hex = "0"+hex;
				}
				else {
					hex = hex.substring(hex.length()-2,hex.length());
				}
				hexout+=hex;
				System.out.println(hex+" "+in[i]);
			}
			catch (StringIndexOutOfBoundsException e) {
				throw e;
			}
		}
		return hexout;
	}
	
}
