package com.laohe.sign;

import org.apache.commons.lang3.StringUtils;
import org.keyczar.exceptions.Base64DecodingException;
import org.keyczar.util.Base64Coder;

import java.io.*;

/**
 * @author hegx
 */

public enum SecurityUtils {
	
	/** 业务加密工具*/
	BUSINESS,
	
	/** 配置加密工具*/
	CONFIG;
	
	/**
	 * 加密一个字符串
	 * @param text 源字符串
	 * @return 加密后的字符串
	 */
	public String cryptText(String text){
		
		if(StringUtils.isEmpty(text))
		{
			return null;
		}

		
		CryptManager manager = getManager();
		byte[] bytes = null;
		try {
			bytes = manager.encrypt(text.getBytes("utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return Base64Coder.encodeWebSafe(bytes);
	}
	
	/**
	 * 解密一个字符串
	 * @param text 密文
	 * @return 解密后的字符串
	 */
	public String deCryptText(String text){
		
		if(StringUtils.isEmpty(text))
			return null;
		
		byte[] bytes = null;
		try {
			bytes = Base64Coder.decodeWebSafe(text);
		} catch (Base64DecodingException e) {
			e.printStackTrace();
		}
		CryptManager manager = getManager();
		
		try {
			return new String(manager.decrypt(bytes),"utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 获得字的MD5的加盐后Base64
	 * @param text
	 * @return 32位16进制字符串
	 */
	public String getSign(String text){
		
		if(StringUtils.isEmpty(text))
			return null;
		CryptManager manager = getManager();
		
		return Base64Coder.encodeWebSafe(manager.encodeSalt(MD5Utils.getMD5Sign(text).getBytes()));
	}
	
	
	/**
	 * 加密一个文件，返回加密后的byte数组
	 * @param file 一个不超过10M的文件
	 * @return 加密后的byte类型数组
	 */
	public byte[] cryptFile(File file){
		
		return invoke(file,ENCRYPT);
	}
	
	/**
	 * 使用加密后的密文作为输出流的源
	 * @param file 一个不超过10M的待解密文件文件
	 * @param os 一个可以输出密文的输出流
	 * @throws IOException 
	 */
	public void cryptFile(File file,OutputStream os) throws IOException{
		
		byte[] bytes = cryptFile(file);
		
		os.write(bytes);
	}
	
	
	/**
	 * 解密一个文件，返回字节数组
	 * @param file
	 * @return 解密后的字节数组
	 */
	public byte[] deCryptFile(File file){
		
		return invoke(file,DECRYPT);
	}
	
	/**
	 * 使用解密后的文作为输出流的源
	 * @param file 一个需要解密的文件
	 * @param os 一个可以输出原文的输出流
	 * @throws IOException 
	 */
	public void deCryptFile(File file,OutputStream os) throws IOException{
		
		byte[] bytes = deCryptFile(file);
		
		os.write(bytes);
	}
	
	/**
	 * 获得文件的MD5签名
	 * @param file
	 * @return 32位16进制字符串
	 */
	public static String getFileMD5SignBy(File file){
		
		return MD5Utils.getFileMD5Sign(file);
	}
	
	/**
	 * 获得文件的MD5签名
	 * @param fileName 文件名
	 * @return 32位16进制字符串
	 */
	public static String getFileMD5SignBy(String fileName){
		
		return MD5Utils.getFileMD5Sign(fileName);
	}
	
	
	private byte[] invoke(File file,int opp){
		
		CryptManager manager = getManager();
		
		try(FileInputStream fis = new FileInputStream(file)){
			
			byte[] bytes = new byte[fis.available()];
			fis.read(bytes);
			
			if(opp == ENCRYPT)
				bytes = manager.encrypt(bytes);
			else
				bytes = manager.decrypt(bytes);
			
			return bytes;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private CryptManager getManager(){
		
		byte send = 0;
		String location = null;
		
		switch(this){
		
		case BUSINESS:
			
			send = Byte.parseByte(System.getenv(BUSINESS_SEND_PARAM));
			location = System.getenv(SECURITY_LOACTION_PARAM)+"business";
			break;
			
		case CONFIG:
			
			send = Byte.parseByte(System.getenv(CONFIG_SEND_PARAM));
			location = System.getenv(SECURITY_LOACTION_PARAM)+"config";
			break;
		default:
			break;
		} 
		
		return CryptManager.instance(send, location);
	}
	
	
	private static final String BUSINESS_SEND_PARAM = "BUSINESS_SEND";
	private static final String CONFIG_SEND_PARAM = "CONFIG_SEND";
	
	private static final String SECURITY_LOACTION_PARAM = "SECURITY_LOCATION";
	
	private static final int ENCRYPT = 1;
	private static final int DECRYPT = 2;
}
