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
	
	/**
	 * Encodes a byte array into string of hexadecimal chars
	 * @param in - Byte array to be transformed
	 * @return
	 */
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
			}
			catch (StringIndexOutOfBoundsException e) {
				throw e;
			}
		}
		return hexout;
	}
	
	/**
	 * Decodes a  HexString into array of bytes
	 * @param in - Hexadecimal String in
	 * @return
	 */
	public static byte [] HexStringToBytes(String in) {
		in = in.strip();
		int length = in.length();
		if(length%2 == 0) {
			// Even
			byte [] bytesout = new byte[length/2];
			int bytesoutindex = 0;
			for(int i=0;i<length;i+=2) {
				String byteString = in.substring(i,i+2);
				byte byteout = 0;
				switch (byteString) {
					case "00":
						byteout= (byte)0x00;
						break;
					case "01":
						byteout= (byte)0x01;
						break;
					case "02":
						byteout= (byte)0x02;
						break;
					case "03":
						byteout= (byte)0x03;
						break;
					case "04":
						byteout= (byte)0x04;
						break;
					case "05":
						byteout= (byte)0x05;
						break;
					case "06":
						byteout= (byte)0x06;
						break;
					case "07":
						byteout= (byte)0x07;
						break;
					case "08":
						byteout= (byte)0x08;
						break;
					case "09":
						byteout= (byte)0x09;
						break;
					case "0a":
						byteout= (byte)0x0a;
						break;
					case "0b":
						byteout= (byte)0x0b;
						break;
					case "0c":
						byteout= (byte)0x0c;
						break;
					case "0d":
						byteout= (byte)0x0d;
						break;
					case "0e":
						byteout= (byte)0x0e;
						break;
					case "0f":
						byteout= (byte)0x0f;
						break;
					case "10":
						byteout= (byte)0x10;
						break;
					case "11":
						byteout= (byte)0x11;
						break;
					case "12":
						byteout= (byte)0x12;
						break;
					case "13":
						byteout= (byte)0x13;
						break;
					case "14":
						byteout= (byte)0x14;
						break;
					case "15":
						byteout= (byte)0x15;
						break;
					case "16":
						byteout= (byte)0x16;
						break;
					case "17":
						byteout= (byte)0x17;
						break;
					case "18":
						byteout= (byte)0x18;
						break;
					case "19":
						byteout= (byte)0x19;
						break;
					case "1a":
						byteout= (byte)0x1a;
						break;
					case "1b":
						byteout= (byte)0x1b;
						break;
					case "1c":
						byteout= (byte)0x1c;
						break;
					case "1d":
						byteout= (byte)0x1d;
						break;
					case "1e":
						byteout= (byte)0x1e;
						break;
					case "1f":
						byteout= (byte)0x1f;
						break;
					case "20":
						byteout= (byte)0x20;
						break;
					case "21":
						byteout= (byte)0x21;
						break;
					case "22":
						byteout= (byte)0x22;
						break;
					case "23":
						byteout= (byte)0x23;
						break;
					case "24":
						byteout= (byte)0x24;
						break;
					case "25":
						byteout= (byte)0x25;
						break;
					case "26":
						byteout= (byte)0x26;
						break;
					case "27":
						byteout= (byte)0x27;
						break;
					case "28":
						byteout= (byte)0x28;
						break;
					case "29":
						byteout= (byte)0x29;
						break;
					case "2a":
						byteout= (byte)0x2a;
						break;
					case "2b":
						byteout= (byte)0x2b;
						break;
					case "2c":
						byteout= (byte)0x2c;
						break;
					case "2d":
						byteout= (byte)0x2d;
						break;
					case "2e":
						byteout= (byte)0x2e;
						break;
					case "2f":
						byteout= (byte)0x2f;
						break;
					case "30":
						byteout= (byte)0x30;
						break;
					case "31":
						byteout= (byte)0x31;
						break;
					case "32":
						byteout= (byte)0x32;
						break;
					case "33":
						byteout= (byte)0x33;
						break;
					case "34":
						byteout= (byte)0x34;
						break;
					case "35":
						byteout= (byte)0x35;
						break;
					case "36":
						byteout= (byte)0x36;
						break;
					case "37":
						byteout= (byte)0x37;
						break;
					case "38":
						byteout= (byte)0x38;
						break;
					case "39":
						byteout= (byte)0x39;
						break;
					case "3a":
						byteout= (byte)0x3a;
						break;
					case "3b":
						byteout= (byte)0x3b;
						break;
					case "3c":
						byteout= (byte)0x3c;
						break;
					case "3d":
						byteout= (byte)0x3d;
						break;
					case "3e":
						byteout= (byte)0x3e;
						break;
					case "3f":
						byteout= (byte)0x3f;
						break;
					case "40":
						byteout= (byte)0x40;
						break;
					case "41":
						byteout= (byte)0x41;
						break;
					case "42":
						byteout= (byte)0x42;
						break;
					case "43":
						byteout= (byte)0x43;
						break;
					case "44":
						byteout= (byte)0x44;
						break;
					case "45":
						byteout= (byte)0x45;
						break;
					case "46":
						byteout= (byte)0x46;
						break;
					case "47":
						byteout= (byte)0x47;
						break;
					case "48":
						byteout= (byte)0x48;
						break;
					case "49":
						byteout= (byte)0x49;
						break;
					case "4a":
						byteout= (byte)0x4a;
						break;
					case "4b":
						byteout= (byte)0x4b;
						break;
					case "4c":
						byteout= (byte)0x4c;
						break;
					case "4d":
						byteout= (byte)0x4d;
						break;
					case "4e":
						byteout= (byte)0x4e;
						break;
					case "4f":
						byteout= (byte)0x4f;
						break;
					case "50":
						byteout= (byte)0x50;
						break;
					case "51":
						byteout= (byte)0x51;
						break;
					case "52":
						byteout= (byte)0x52;
						break;
					case "53":
						byteout= (byte)0x53;
						break;
					case "54":
						byteout= (byte)0x54;
						break;
					case "55":
						byteout= (byte)0x55;
						break;
					case "56":
						byteout= (byte)0x56;
						break;
					case "57":
						byteout= (byte)0x57;
						break;
					case "58":
						byteout= (byte)0x58;
						break;
					case "59":
						byteout= (byte)0x59;
						break;
					case "5a":
						byteout= (byte)0x5a;
						break;
					case "5b":
						byteout= (byte)0x5b;
						break;
					case "5c":
						byteout= (byte)0x5c;
						break;
					case "5d":
						byteout= (byte)0x5d;
						break;
					case "5e":
						byteout= (byte)0x5e;
						break;
					case "5f":
						byteout= (byte)0x5f;
						break;
					case "60":
						byteout= (byte)0x60;
						break;
					case "61":
						byteout= (byte)0x61;
						break;
					case "62":
						byteout= (byte)0x62;
						break;
					case "63":
						byteout= (byte)0x63;
						break;
					case "64":
						byteout= (byte)0x64;
						break;
					case "65":
						byteout= (byte)0x65;
						break;
					case "66":
						byteout= (byte)0x66;
						break;
					case "67":
						byteout= (byte)0x67;
						break;
					case "68":
						byteout= (byte)0x68;
						break;
					case "69":
						byteout= (byte)0x69;
						break;
					case "6a":
						byteout= (byte)0x6a;
						break;
					case "6b":
						byteout= (byte)0x6b;
						break;
					case "6c":
						byteout= (byte)0x6c;
						break;
					case "6d":
						byteout= (byte)0x6d;
						break;
					case "6e":
						byteout= (byte)0x6e;
						break;
					case "6f":
						byteout= (byte)0x6f;
						break;
					case "70":
						byteout= (byte)0x70;
						break;
					case "71":
						byteout= (byte)0x71;
						break;
					case "72":
						byteout= (byte)0x72;
						break;
					case "73":
						byteout= (byte)0x73;
						break;
					case "74":
						byteout= (byte)0x74;
						break;
					case "75":
						byteout= (byte)0x75;
						break;
					case "76":
						byteout= (byte)0x76;
						break;
					case "77":
						byteout= (byte)0x77;
						break;
					case "78":
						byteout= (byte)0x78;
						break;
					case "79":
						byteout= (byte)0x79;
						break;
					case "7a":
						byteout= (byte)0x7a;
						break;
					case "7b":
						byteout= (byte)0x7b;
						break;
					case "7c":
						byteout= (byte)0x7c;
						break;
					case "7d":
						byteout= (byte)0x7d;
						break;
					case "7e":
						byteout= (byte)0x7e;
						break;
					case "7f":
						byteout= (byte)0x7f;
						break;
					case "80":
						byteout= (byte)0x80;
						break;
					case "81":
						byteout= (byte)0x81;
						break;
					case "82":
						byteout= (byte)0x82;
						break;
					case "83":
						byteout= (byte)0x83;
						break;
					case "84":
						byteout= (byte)0x84;
						break;
					case "85":
						byteout= (byte)0x85;
						break;
					case "86":
						byteout= (byte)0x86;
						break;
					case "87":
						byteout= (byte)0x87;
						break;
					case "88":
						byteout= (byte)0x88;
						break;
					case "89":
						byteout= (byte)0x89;
						break;
					case "8a":
						byteout= (byte)0x8a;
						break;
					case "8b":
						byteout= (byte)0x8b;
						break;
					case "8c":
						byteout= (byte)0x8c;
						break;
					case "8d":
						byteout= (byte)0x8d;
						break;
					case "8e":
						byteout= (byte)0x8e;
						break;
					case "8f":
						byteout= (byte)0x8f;
						break;
					case "90":
						byteout= (byte)0x90;
						break;
					case "91":
						byteout= (byte)0x91;
						break;
					case "92":
						byteout= (byte)0x92;
						break;
					case "93":
						byteout= (byte)0x93;
						break;
					case "94":
						byteout= (byte)0x94;
						break;
					case "95":
						byteout= (byte)0x95;
						break;
					case "96":
						byteout= (byte)0x96;
						break;
					case "97":
						byteout= (byte)0x97;
						break;
					case "98":
						byteout= (byte)0x98;
						break;
					case "99":
						byteout= (byte)0x99;
						break;
					case "9a":
						byteout= (byte)0x9a;
						break;
					case "9b":
						byteout= (byte)0x9b;
						break;
					case "9c":
						byteout= (byte)0x9c;
						break;
					case "9d":
						byteout= (byte)0x9d;
						break;
					case "9e":
						byteout= (byte)0x9e;
						break;
					case "9f":
						byteout= (byte)0x9f;
						break;
					case "a0":
						byteout= (byte)0xa0;
						break;
					case "a1":
						byteout= (byte)0xa1;
						break;
					case "a2":
						byteout= (byte)0xa2;
						break;
					case "a3":
						byteout= (byte)0xa3;
						break;
					case "a4":
						byteout= (byte)0xa4;
						break;
					case "a5":
						byteout= (byte)0xa5;
						break;
					case "a6":
						byteout= (byte)0xa6;
						break;
					case "a7":
						byteout= (byte)0xa7;
						break;
					case "a8":
						byteout= (byte)0xa8;
						break;
					case "a9":
						byteout= (byte)0xa9;
						break;
					case "aa":
						byteout= (byte)0xaa;
						break;
					case "ab":
						byteout= (byte)0xab;
						break;
					case "ac":
						byteout= (byte)0xac;
						break;
					case "ad":
						byteout= (byte)0xad;
						break;
					case "ae":
						byteout= (byte)0xae;
						break;
					case "af":
						byteout= (byte)0xaf;
						break;
					case "b0":
						byteout= (byte)0xb0;
						break;
					case "b1":
						byteout= (byte)0xb1;
						break;
					case "b2":
						byteout= (byte)0xb2;
						break;
					case "b3":
						byteout= (byte)0xb3;
						break;
					case "b4":
						byteout= (byte)0xb4;
						break;
					case "b5":
						byteout= (byte)0xb5;
						break;
					case "b6":
						byteout= (byte)0xb6;
						break;
					case "b7":
						byteout= (byte)0xb7;
						break;
					case "b8":
						byteout= (byte)0xb8;
						break;
					case "b9":
						byteout= (byte)0xb9;
						break;
					case "ba":
						byteout= (byte)0xba;
						break;
					case "bb":
						byteout= (byte)0xbb;
						break;
					case "bc":
						byteout= (byte)0xbc;
						break;
					case "bd":
						byteout= (byte)0xbd;
						break;
					case "be":
						byteout= (byte)0xbe;
						break;
					case "bf":
						byteout= (byte)0xbf;
						break;
					case "c0":
						byteout= (byte)0xc0;
						break;
					case "c1":
						byteout= (byte)0xc1;
						break;
					case "c2":
						byteout= (byte)0xc2;
						break;
					case "c3":
						byteout= (byte)0xc3;
						break;
					case "c4":
						byteout= (byte)0xc4;
						break;
					case "c5":
						byteout= (byte)0xc5;
						break;
					case "c6":
						byteout= (byte)0xc6;
						break;
					case "c7":
						byteout= (byte)0xc7;
						break;
					case "c8":
						byteout= (byte)0xc8;
						break;
					case "c9":
						byteout= (byte)0xc9;
						break;
					case "ca":
						byteout= (byte)0xca;
						break;
					case "cb":
						byteout= (byte)0xcb;
						break;
					case "cc":
						byteout= (byte)0xcc;
						break;
					case "cd":
						byteout= (byte)0xcd;
						break;
					case "ce":
						byteout= (byte)0xce;
						break;
					case "cf":
						byteout= (byte)0xcf;
						break;
					case "d0":
						byteout= (byte)0xd0;
						break;
					case "d1":
						byteout= (byte)0xd1;
						break;
					case "d2":
						byteout= (byte)0xd2;
						break;
					case "d3":
						byteout= (byte)0xd3;
						break;
					case "d4":
						byteout= (byte)0xd4;
						break;
					case "d5":
						byteout= (byte)0xd5;
						break;
					case "d6":
						byteout= (byte)0xd6;
						break;
					case "d7":
						byteout= (byte)0xd7;
						break;
					case "d8":
						byteout= (byte)0xd8;
						break;
					case "d9":
						byteout= (byte)0xd9;
						break;
					case "da":
						byteout= (byte)0xda;
						break;
					case "db":
						byteout= (byte)0xdb;
						break;
					case "dc":
						byteout= (byte)0xdc;
						break;
					case "dd":
						byteout= (byte)0xdd;
						break;
					case "de":
						byteout= (byte)0xde;
						break;
					case "df":
						byteout= (byte)0xdf;
						break;
					case "e0":
						byteout= (byte)0xe0;
						break;
					case "e1":
						byteout= (byte)0xe1;
						break;
					case "e2":
						byteout= (byte)0xe2;
						break;
					case "e3":
						byteout= (byte)0xe3;
						break;
					case "e4":
						byteout= (byte)0xe4;
						break;
					case "e5":
						byteout= (byte)0xe5;
						break;
					case "e6":
						byteout= (byte)0xe6;
						break;
					case "e7":
						byteout= (byte)0xe7;
						break;
					case "e8":
						byteout= (byte)0xe8;
						break;
					case "e9":
						byteout= (byte)0xe9;
						break;
					case "ea":
						byteout= (byte)0xea;
						break;
					case "eb":
						byteout= (byte)0xeb;
						break;
					case "ec":
						byteout= (byte)0xec;
						break;
					case "ed":
						byteout= (byte)0xed;
						break;
					case "ee":
						byteout= (byte)0xee;
						break;
					case "ef":
						byteout= (byte)0xef;
						break;
					case "f0":
						byteout= (byte)0xf0;
						break;
					case "f1":
						byteout= (byte)0xf1;
						break;
					case "f2":
						byteout= (byte)0xf2;
						break;
					case "f3":
						byteout= (byte)0xf3;
						break;
					case "f4":
						byteout= (byte)0xf4;
						break;
					case "f5":
						byteout= (byte)0xf5;
						break;
					case "f6":
						byteout= (byte)0xf6;
						break;
					case "f7":
						byteout= (byte)0xf7;
						break;
					case "f8":
						byteout= (byte)0xf8;
						break;
					case "f9":
						byteout= (byte)0xf9;
						break;
					case "fa":
						byteout= (byte)0xfa;
						break;
					case "fb":
						byteout= (byte)0xfb;
						break;
					case "fc":
						byteout= (byte)0xfc;
						break;
					case "fd":
						byteout= (byte)0xfd;
						break;
					case "fe":
						byteout= (byte)0xfe;
						break;
					case "ff":
						byteout= (byte)0xff;
						break;
				}
				bytesout[bytesoutindex] = byteout;
				bytesoutindex++;
			}
			return bytesout;
		}
		else {
			//Odd
			return null; // Error size can never be odd.. because its pair of char as byte.
		}
	}
}
