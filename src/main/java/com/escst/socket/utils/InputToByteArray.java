package com.escst.socket.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.CharBuffer;

public class InputToByteArray {
	private static String temp; 
	private final static String SOAP_BEGIN = "&"; 
    private final static String SOAP_END = "$";
	/**
	 * 将InputStream 转换成字节数组
	 * @param in
	 * @return
	 * @throws Exception
	 */
	public static byte[] toByteArray(InputStream in,int len) throws Exception {
        byte[] buffer=new byte[len];
        for (int i = 0; i < len; i++) {
        	buffer[i] = (byte) in.read();
		}
        return buffer;
    }
	
	public static String toByteArray(InputStream input) throws ClassNotFoundException, IOException{
		Reader reader = new InputStreamReader(input);
		CharBuffer charBuffer = CharBuffer.allocate(8192); 
		int readIndex = -1;  
		temp = "";
		while ((readIndex = reader.read(charBuffer)) != -1) { 
            charBuffer.flip(); 
            temp += charBuffer.toString(); 
            if (temp.indexOf(SOAP_BEGIN) != -1 
                    && temp.indexOf(SOAP_END) != -1) { 
            	System.out.println("接收到的数据："+temp);
            	
            	break; 
            } else if (temp.indexOf(SOAP_BEGIN) != -1) { 
                // 包含开始，但不包含 
                temp = temp.substring(temp.indexOf(SOAP_BEGIN)); 
            }    
            if (temp.length() > 1024 * 16) { 
                break; 
            } 
        }  
//		ByteArrayOutputStream output = new ByteArrayOutputStream();
//		byte[] buffer = new byte[1024];
//        try{
//	        int n = 0;
//	        while (-1 != (n = input.read(buffer))) {
//	            output.write(buffer, 0, n);
//	        }
//        }catch(Exception e){
//        	e.printStackTrace();
//        }
        return temp;
    }
}
