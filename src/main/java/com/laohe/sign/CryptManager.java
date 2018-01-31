package com.laohe.sign;

import org.keyczar.Crypter;
import org.keyczar.exceptions.KeyczarException;
import org.keyczar.interfaces.KeyczarReader;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Class Description:
 * Copyright:   Copyright (c)
 * Company:     Daren Capital. Co. Ltd.
 * @author:     walnut
 * @version:    1.0
 *
 * Modification History: 初始版本
 * Date:        2014年4月9日
 * Author:   	walnut
 * Version:     1.0
 * Description: Initialize
 *
 */
public abstract class CryptManager {

	/**
	 * 加盐编码
	 * @param bytes 源数据
	 * @return	加盐后数据
	 */
	protected abstract byte[] encodeSalt(byte[] bytes);
	
	/**
	 * 加盐解码
	 * @param bytes 加盐数据
	 * @return	源数据
	 */
	protected abstract byte[] decodeSalt(byte[] bytes);

	/**
	 * 加密
	 * @param bytes 源
	 * @return 加密后数据
	 */
	byte[] encrypt(byte[] bytes){
		
		try {
			
			bytes = encodeSalt(bytes);
			return crypter.encrypt(bytes);
			
		} catch (KeyczarException e) {
			
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * 解密
	 * @param bytes 加密后数据
	 * @return 源数据
	 */
	byte[] decrypt(byte[] bytes){
		
		try {
			
			bytes = crypter.decrypt(bytes);
			return decodeSalt(bytes);
		} catch (KeyczarException e) {
			
			e.printStackTrace();
		}
		
		return null;
	}

	public byte getSend() {
		return send;
	}

	/**
	 * 获得CryptManager实例
	 * @return
	 */
	public static synchronized CryptManager instance(byte send,String location){
		
		String key = send+location;
		if(!instanceMap.containsKey(key)){
			CryptManager CryptManager = new SaltProvider();
			instanceMap.put(key, CryptManager);
			instanceMap.get(key).send = send;
			instanceMap.get(key).location = location;
			instanceMap.get(key).initCrypter();
		}
		return instanceMap.get(key);
	}
	
	private void initCrypter(){
		
		String sysLocation = System.getenv(SYSTEM_LOCATION_PARAM);
		
		try(FileInputStream fis = new FileInputStream(location)) {
			
			//使用系统密钥对业务密钥进行解密
			Crypter sysCrypter = new Crypter(sysLocation);
			byte[] buf = new byte[fis.available()];
			fis.read(buf);
			final String key = new String(sysCrypter.decrypt(buf));
			
			this.crypter = new Crypter(new KeyczarReader() {
				
				@Override
				public String getMetadata() throws KeyczarException {
					// TODO Auto-generated method stub
					return  "{\"name\":\"Test\",\"purpose\":\"DECRYPT_AND_ENCRYPT\","
							+ "\"type\":\"AES\",\"versions\":[{\"exportable\":false,\"status\":\"PRIMARY\",\"versionNumber\":1}],"
							+ "\"encrypted\":false}";
				}
				
				@Override
				public String getKey(int version) throws KeyczarException {
					
					return key;
				}
				
				@Override
				public String getKey() throws KeyczarException {
					
					return key;
				}
			});
			
		} catch (KeyczarException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private Crypter crypter;

	private static final Map<String,CryptManager> instanceMap = new ConcurrentHashMap<>();

	static final String SYSTEM_LOCATION_PARAM = "SYSTEM_LOCATION";

	private String location;
	private byte send;
}
