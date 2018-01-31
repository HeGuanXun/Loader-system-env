package com.laohe.sign;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hegx
 */
public class SaltProvider extends CryptManager {
	
	

	@Override
	protected byte[] encodeSalt(byte[] bytes) {
		
		algorithm(bytes);
		return bytes;
	}

	@Override
	protected byte[] decodeSalt(byte[] bytes) {
		
		algorithm(bytes);
		return bytes;
	}
	
	private List<Byte> calc(byte m,byte send){
		
		List<Byte> byteList = new ArrayList<>();
		_calc(m, send, byteList);


		if(logger.isDebugEnabled()){
			StringBuilder sb = new StringBuilder("[");

			for (Byte b : byteList) {
				sb.append(b);
				sb.append(",");
			}
			sb.replace(sb.length()-1, sb.length(), "]");

			logger.info("种子："+send+";加盐序列为："+sb.toString());
		}

		if(byteList.size()==0)
			logger.warn("【未生成加盐序列！】");

		return byteList;
	}
	
	private void _calc(byte m,byte send,List<Byte> numList){
		
		if((m-send)<=Byte.MAX_VALUE && (m-send)>=Byte.MIN_VALUE){
			byte result = (byte) (m-send);
			numList.add(result);
			_calc(send,result,numList);
		}else{
			return;
		}
	}
	
	private void algorithm(byte[] bytes){
		
		final byte m = (byte) (Math.max(bytes.length,getSend()) % Math.min(bytes.length,getSend()));
		
		if(bytes.length<0){
			throw new NullPointerException("不可以对空数据进行加密");
		}
		
		//获得反费氏序列
		List<Byte> fsList = calc(m, getSend());
		
		
		for(int i =0;i<bytes.length;i++){
			for(Byte b : fsList)
				bytes[i] ^= b;
		}
		
	}

	private Logger logger = LogManager.getLogger();

}
