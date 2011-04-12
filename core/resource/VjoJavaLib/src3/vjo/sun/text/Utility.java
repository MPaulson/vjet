package vjo.java.sun.text;

/*jadclipse*/// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
//Jad home page: http://www.kpdus.com/jad.html
//Decompiler options: packimports(3) radix(10) lradix(10) 
//Source File Name:   Utility.java
import java.lang.InternalError;

import vjo.java.lang.* ;
import vjo.java.lang.StringBuffer ;

public final class Utility {

	public Utility() {
	}

	public static final boolean arrayEquals(Object aobj[], Object obj) {
		if (aobj == null)
			return obj == null;
		if (!(obj instanceof Object[])) {
			return false;
		} else {
			Object aobj1[] = (Object[]) (Object[]) obj;
			return aobj.length == aobj1.length
					&& arrayRegionMatches(aobj, 0, aobj1, 0, aobj.length);
		}
	}

	public static final boolean arrayEquals(int ai[], Object obj) {
		if (ai == null)
			return obj == null;
		if (!(obj instanceof int[])) {
			return false;
		} else {
			int ai1[] = (int[]) (int[]) obj;
			return ai.length == ai1.length
					&& arrayRegionMatches(ai, 0, ai1, 0, ai.length);
		}
	}

	public static final boolean arrayEquals(double ad[], Object obj) {
		if (ad == null)
			return obj == null;
		if (!(obj instanceof double[])) {
			return false;
		} else {
			double ad1[] = (double[]) (double[]) obj;
			return ad.length == ad1.length
					&& arrayRegionMatches(ad, 0, ad1, 0, ad.length);
		}
	}

	public static final boolean arrayEquals(Object obj, Object obj1) {
		if (obj == null)
			return obj1 == null;
		if (obj instanceof Object[])
			return arrayEquals((Object[]) (Object[]) obj, obj1);
		if (obj instanceof int[])
			return arrayEquals((int[]) (int[]) obj, obj1);
		if (obj instanceof double[])
			return arrayEquals((int[]) (int[]) obj, obj1);
		else
			return obj.equals(obj1);
	}

	public static final boolean arrayRegionMatches(Object aobj[], int i,
			Object aobj1[], int j, int k) {
		int l = i + k;
		int i1 = j - i;
		for (int j1 = i; j1 < l; j1++)
			if (!arrayEquals(aobj[j1], aobj1[j1 + i1]))
				return false;

		return true;
	}

	public static final boolean arrayRegionMatches(int ai[], int i, int ai1[],
			int j, int k) {
		int l = i + k;
		int i1 = j - i;
		for (int j1 = i; j1 < l; j1++)
			if (ai[j1] != ai1[j1 + i1])
				return false;

		return true;
	}

	public static final boolean arrayRegionMatches(char ac[], int i,
			char ac1[], int j, int k) {
		int l = i + k;
		int i1 = j - i;
		for (int j1 = i; j1 < l; j1++)
			if (ac[j1] != ac1[j1 + i1])
				return false;

		return true;
	}

	public static final boolean arrayRegionMatches(double ad[], int i,
			double ad1[], int j, int k) {
		int l = i + k;
		int i1 = j - i;
		for (int j1 = i; j1 < l; j1++)
			if (ad[j1] != ad1[j1 + i1])
				return false;

		return true;
	}

	public static final boolean objectEquals(Object obj, Object obj1) {
		if (obj == null)
			return obj1 == null;
		else
			return obj.equals(obj1);
	}

	public static final String arrayToRLEString(short aword0[]) {
		StringBuffer stringbuffer = new StringBuffer();
		stringbuffer.append((char) (aword0.length >> 16));
		stringbuffer.append((char) aword0.length);
		short word0 = aword0[0];
		int i = 1;
		for (int j = 1; j < aword0.length; j++) {
			short word1 = aword0[j];
			if (word1 == word0 && i < 65535) {
				i++;
			} else {
				encodeRun(stringbuffer, word0, i);
				word0 = word1;
				i = 1;
			}
		}

		encodeRun(stringbuffer, word0, i);
		return stringbuffer.toString();
	}

	public static final String arrayToRLEString(byte abyte0[]) {
		StringBuffer stringbuffer = new StringBuffer();
		stringbuffer.append((char) (abyte0.length >> 16));
		stringbuffer.append((char) abyte0.length);
		byte byte0 = abyte0[0];
		int i = 1;
		byte abyte1[] = new byte[2];
		for (int j = 1; j < abyte0.length; j++) {
			byte byte1 = abyte0[j];
			if (byte1 == byte0 && i < 255) {
				i++;
			} else {
				encodeRun(stringbuffer, byte0, i, abyte1);
				byte0 = byte1;
				i = 1;
			}
		}

		encodeRun(stringbuffer, byte0, i, abyte1);
		if (abyte1[0] != 0)
			appendEncodedByte(stringbuffer, (byte) 0, abyte1);
		return stringbuffer.toString();
	}

	public static final String arrayToRLEString(char ac[]) {
		StringBuffer stringbuffer = new StringBuffer();
		stringbuffer.append((char) (ac.length >> 16));
		stringbuffer.append((char) ac.length);
		char c = ac[0];
		int i = 1;
		for (int j = 1; j < ac.length; j++) {
			char c1 = ac[j];
			if (c1 == c && i < 65535) {
				i++;
			} else {
				encodeRun(stringbuffer, (short) c, i);
				c = c1;
				i = 1;
			}
		}

		encodeRun(stringbuffer, (short) c, i);
		return stringbuffer.toString();
	}

	public static final String arrayToRLEString(int ai[]) {
		StringBuffer stringbuffer = new StringBuffer();
		appendInt(stringbuffer, ai.length);
		int i = ai[0];
		int j = 1;
		for (int k = 1; k < ai.length; k++) {
			int l = ai[k];
			if (l == i && j < 65535) {
				j++;
			} else {
				encodeRun(stringbuffer, i, j);
				i = l;
				j = 1;
			}
		}

		encodeRun(stringbuffer, i, j);
		return stringbuffer.toString();
	}

	private static final void encodeRun(StringBuffer stringbuffer, short word0,
			int i) {
		if (i < 4) {
			for (int j = 0; j < i; j++) {
				if (word0 == 42405)
					stringbuffer.append('\uA5A5');
				stringbuffer.append((char) word0);
			}

		} else {
			if (i == 42405) {
				if (word0 == 42405)
					stringbuffer.append('\uA5A5');
				stringbuffer.append((char) word0);
				i--;
			}
			stringbuffer.append('\uA5A5');
			stringbuffer.append((char) i);
			stringbuffer.append((char) word0);
		}
	}

	private static final void encodeRun(StringBuffer stringbuffer, byte byte0,
			int i, byte abyte0[]) {
		if (i < 4) {
			for (int j = 0; j < i; j++) {
				if (byte0 == -91)
					appendEncodedByte(stringbuffer, (byte) -91, abyte0);
				appendEncodedByte(stringbuffer, byte0, abyte0);
			}

		} else {
			if (i == -91) {
				if (byte0 == -91)
					appendEncodedByte(stringbuffer, (byte) -91, abyte0);
				appendEncodedByte(stringbuffer, byte0, abyte0);
				i--;
			}
			appendEncodedByte(stringbuffer, (byte) -91, abyte0);
			appendEncodedByte(stringbuffer, (byte) i, abyte0);
			appendEncodedByte(stringbuffer, byte0, abyte0);
		}
	}

	private static final void encodeRun(StringBuffer stringbuffer, int i, int j) {
		if (j < 4) {
			for (int k = 0; k < j; k++) {
				if (i == 42405)
					appendInt(stringbuffer, i);
				appendInt(stringbuffer, i);
			}

		} else {
			if (j == 42405) {
				if (i == 42405)
					appendInt(stringbuffer, 42405);
				appendInt(stringbuffer, i);
				j--;
			}
			appendInt(stringbuffer, 42405);
			appendInt(stringbuffer, j);
			appendInt(stringbuffer, i);
		}
	}

	private static final void appendInt(StringBuffer stringbuffer, int i) {
		stringbuffer.append((char) (i >>> 16));
		stringbuffer.append((char) (i & 65535));
	}

	private static final void appendEncodedByte(StringBuffer stringbuffer,
			byte byte0, byte abyte0[]) {
		if (abyte0[0] != 0) {
			char c = (char) (abyte0[1] << 8 | byte0 & 255);
			stringbuffer.append(c);
			abyte0[0] = 0;
		} else {
			abyte0[0] = 1;
			abyte0[1] = byte0;
		}
	}

	public static final short[] RLEStringToShortArray(String s) {
		int i = s.charAt(0) << 16 | s.charAt(1);
		short aword0[] = new short[i];
		int j = 0;
		for (int k = 2; k < s.length(); k++) {
			char c = s.charAt(k);
			if (c == '\uA5A5') {
				c = s.charAt(++k);
				if (c == '\uA5A5') {
					aword0[j++] = (short) c;
					continue;
				}
				char c1 = c;
				short word0 = (short) s.charAt(++k);
				for (int l = 0; l < c1; l++)
					aword0[j++] = word0;

			} else {
				aword0[j++] = (short) c;
			}
		}

		if (j != i)
			throw new InternalError("Bad run-length encoded short array");
		else
			return aword0;
	}

	public static final byte[] RLEStringToByteArray(String s) {
		int i = s.charAt(0) << 16 | s.charAt(1);
		byte abyte0[] = new byte[i];
		boolean flag = true;
		char c = '\0';
		int j = 0;
		int k = 0;
		int l = 2;
		int i1 = 0;
		do
			if (i1 < i) {
				byte byte0;
				if (flag) {
					c = s.charAt(l++);
					byte0 = (byte) (c >> 8);
					flag = false;
				} else {
					byte0 = (byte) (c & 255);
					flag = true;
				}
				switch (j) {
				case 0: // '\0'
					if (byte0 == -91)
						j = 1;
					else
						abyte0[i1++] = byte0;
					break;

				case 1: // '\001'
					if (byte0 == -91) {
						abyte0[i1++] = -91;
						j = 0;
					} else {
						k = byte0;
						if (k < 0)
							k += 256;
						j = 2;
					}
					break;

				case 2: // '\002'
					for (int j1 = 0; j1 < k; j1++)
						abyte0[i1++] = byte0;

					j = 0;
					break;
				}
			} else {
				if (j != 0)
					throw new InternalError("Bad run-length encoded byte array");
				if (l != s.length())
					throw new InternalError(
							"Excess data in RLE byte array string");
				else
					return abyte0;
			}
		while (true);
	}

	public static final char[] RLEStringToCharArray(String s) {
		int i = s.charAt(0) << 16 | s.charAt(1);
		char ac[] = new char[i];
		int j = 0;
		for (int k = 2; k < s.length(); k++) {
			char c = s.charAt(k);
			if (c == '\uA5A5') {
				c = s.charAt(++k);
				if (c == '\uA5A5') {
					ac[j++] = c;
					continue;
				}
				char c1 = c;
				char c2 = s.charAt(++k);
				for (int l = 0; l < c1; l++)
					ac[j++] = c2;

			} else {
				ac[j++] = c;
			}
		}

		if (j != i)
			throw new InternalError("Bad run-length encoded short array");
		else
			return ac;
	}

	public static final int[] RLEStringToIntArray(String s) {
		int i = getInt(s, 0);
		int ai[] = new int[i];
		int j = 0;
		int k = 1;
		int l;
		for (l = s.length() / 2; j < i && k < l;) {
			int i1 = getInt(s, k++);
			if (i1 == 42405) {
				i1 = getInt(s, k++);
				if (i1 == 42405) {
					ai[j++] = i1;
				} else {
					int j1 = i1;
					int k1 = getInt(s, k++);
					int l1 = 0;
					while (l1 < j1) {
						ai[j++] = k1;
						l1++;
					}
				}
			} else {
				ai[j++] = i1;
			}
		}

		if (j != i || k != l)
			throw new InternalError("Bad run-length encoded int array");
		else
			return ai;
	}

	public static final String formatForSource(String s) {
		StringBuffer stringbuffer = new StringBuffer();
		for (int i = 0; i < s.length(); stringbuffer.append('"')) {
			if (i > 0)
				stringbuffer.append("+\n");
			stringbuffer.append("        \"");
			for (int j = 11; i < s.length() && j < 80;) {
				char c = s.charAt(i++);
				if (c < ' ' || c == '"') {
					stringbuffer.append('\\');
					stringbuffer.append(HEX_DIGIT[(c & 448) >> 6]);
					stringbuffer.append(HEX_DIGIT[(c & 56) >> 3]);
					stringbuffer.append(HEX_DIGIT[c & 7]);
					j += 4;
				} else if (c <= '~') {
					stringbuffer.append(c);
					j++;
				} else {
					stringbuffer.append("\\u");
					stringbuffer.append(HEX_DIGIT[(c & 61440) >> 12]);
					stringbuffer.append(HEX_DIGIT[(c & 3840) >> 8]);
					stringbuffer.append(HEX_DIGIT[(c & 240) >> 4]);
					stringbuffer.append(HEX_DIGIT[c & 15]);
					j += 6;
				}
			}

		}

		return stringbuffer.toString();
	}

	public static final String hex(char c) {
		StringBuffer stringbuffer = new StringBuffer();
		return hex(c, stringbuffer).toString();
	}

	public static final StringBuffer hex(String s, StringBuffer stringbuffer) {
		if (s != null && stringbuffer != null) {
			int i = s.length();
			int j = 0;
			hex(s.charAt(j), stringbuffer);
			while (j < i) {
				stringbuffer.append(',');
				hex(s.charAt(j++), stringbuffer);
			}
		}
		return stringbuffer;
	}

	public static final String hex(String s) {
		StringBuffer stringbuffer = new StringBuffer();
		hex(s, stringbuffer);
		return stringbuffer.toString();
	}

	public static final String hex(StringBuffer stringbuffer) {
		return hex(stringbuffer.toString());
	}

	public static final StringBuffer hex(char c, StringBuffer stringbuffer) {
		for (int i = 12; i >= 0; i -= 4)
			stringbuffer.append(HEX_DIGIT[(byte) (c >> i & 15)]);

		return stringbuffer;
	}

	static final int getInt(String s, int i) {
		return s.charAt(2 * i) << 16 | s.charAt(2 * i + 1);
	}

	static final char ESCAPE = 42405;

	static final byte ESCAPE_BYTE = -91;

	static final char HEX_DIGIT[] = { '0', '1', '2', '3', '4', '5', '6', '7',
			'8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

}


/*
	DECOMPILATION REPORT

	Decompiled from: C:\opt\java5-ibm-2007-12-13\jre\lib\core.jar
	Total time: 32 ms
	Jad reported messages/errors:
	Exit status: 0
	Caught exceptions:
*/
