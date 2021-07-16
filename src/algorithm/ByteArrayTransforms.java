package algorithm;

public class ByteArrayTransforms {
	public static char [] toCharArray(byte [] in) {
		char [] out = new char[in.length];
		for(int i=0;i<out.length;i++) {
			out[i]=(char) in[i];
		}
		return out;
	}
	public static byte [] toByteArray(char [] in) {
		byte [] out = new byte[in.length];
		for(int i=0;i<out.length;i++) {
			out[i]=(byte) in[i];
		}
		return out;
	}
	public static byte [] toByteArray(String in) {
		char [] _in = in.toCharArray();
		byte [] out = new byte[in.length()];
		for(int i=0;i<out.length;i++) {
			out[i]=(byte) _in[i];
		}
		return out;
	}
}
