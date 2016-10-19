package com.escst.socket.utils;

import java.io.ByteArrayOutputStream;

public class HexToString {
	/* 
	* 将16进制数字解码成字符串,适用于所有字符（包括中文） 
	*/  
	private static String hexString = "0123456789ABCDEF"; 
	
	public static String decode(String bytes) {  
		/* 
		* 16进制数字字符集 
		*/  
	   ByteArrayOutputStream baos = new ByteArrayOutputStream(bytes.length() / 2);  
	   System.out.println(bytes.length());
	   // 将每2位16进制整数组装成一个字节  
	   for (int i = 0; i < bytes.length(); i += 2) 
	   {
	      baos.write((hexString.indexOf(bytes.charAt(i)) << 4 | hexString.indexOf(bytes.charAt(i + 1))));  
	   }
	   return new String(baos.toByteArray());  
	} 
	
	public static String toStringHex(String s) {
		byte[] baKeyword = new byte[s.length() / 2];
		for (int i = 0; i < baKeyword.length; i++) {
			try {
				
				String num = s.substring(i * 2, i * 2 + 2);
				if(!num.isEmpty()){
				baKeyword[i] = (byte) (0xff & Integer.parseInt(num, 16));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		try {
			s = new String(baKeyword);// UTF-16le:Not
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return s;
	}
	
	/**
	 * 16进制转2进制
	 * 
	 * @param hexString
	 * @return
	 */
	public static String hexStrTobinaryStr(String hexString) {
		if (hexString == null || hexString.length() % 2 != 0)
			return null;
		String bString = "", tmp;
		for (int i = 0; i < hexString.length(); i++) {
			tmp = "0000" + Integer.toBinaryString(Integer.parseInt(hexString.substring(i, i + 1), 16));
			bString += tmp.substring(tmp.length() - 4);
		}
		return bString;
	}
	
	public static void main(String[] args) {
		String str = "26 30 36 2D 30 30 5F 30 30 2D 20 2D 30 30 2D 30";
		str = str.replace(" ", "");
		System.out.println(toStringHex(str));
	}
}
