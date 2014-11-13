package com.example.audiorecord.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Base64;

/**
 * 转换工具类，将字段转换为hashtable，将hashtable转换为json字符串等
 * 将文件转换为字节数组等
 * @author zhangzhilai
 *
 */
public class ConversionUtils {
	
	/**
	 * 将字段转换成hashtable，方便转换成json字符串(这个不通用)
	 * @param userID
	 * @param suffix
	 * @param token
	 * @return
	 */
	public static Hashtable<String, Serializable> paramsToHashtable(int userID, String suffix, String token) {
		Hashtable<String, Serializable> htable = new Hashtable<String, Serializable>();
		htable.put("UserID", userID);
		htable.put("FileExt", suffix);
		htable.put("Token", token);
//		htable.put("AudioData", byteAudio);
		return htable;
	}
	
	/**
	 * 将hashtable转换为json字符串
	 * @param hashtable
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static String  hashTableToJsonString(Hashtable<String, Serializable> hashtable) {
		if(hashtable == null){
			return "{}";
		}
		
		Enumeration<String> enumerationKeys = hashtable.keys();
		Boolean alreadyAddParma = false;
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("{");
		while (enumerationKeys.hasMoreElements()) {
			if(alreadyAddParma){
				stringBuilder.append(",");
			}
			String key = (String) enumerationKeys.nextElement();
			
			Object valuesObject =  hashtable.get(key);
			if( valuesObject instanceof Hashtable<?, ?>){
				stringBuilder.append("\"" + key + "\":" + hashTableToJsonString((Hashtable<String, Serializable>) valuesObject));
			} else if(valuesObject instanceof List){
				stringBuilder.append("\"" + key + "\":" + listToJsonString((List<?>)valuesObject));
			} else {
				stringBuilder.append("\"" + key + "\":" + fomatValue(valuesObject));
			}
			alreadyAddParma = true;
		}
		stringBuilder.append("}");
		return stringBuilder.toString();
	}
	
	/**
	 * 将list转换为json字符串
	 * @param list
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static String listToJsonString(List list) {
		if(list == null || list.size() == 0){
			return "[]";
		}
		Boolean alreadyAddParam = false;
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("[");
		for(Object object : list){
			if(alreadyAddParam){
				stringBuilder.append(",");
			}
			stringBuilder.append(fomatValue(object));
			alreadyAddParam = true;
		}
		stringBuilder.append("]");
		return stringBuilder.toString();
	}
	
	/**
	 * 对字符串和对象进行格式化到字符串中
	 * @param object
	 * @return
	 */
	public static String fomatValue(Object object){
		if (object == null) {
			return "\"\"";
		}
		StringBuilder stringBuilder = new StringBuilder();
		if(object instanceof String){
			stringBuilder.append("\"" + String.valueOf(object) + "\"");
		} else if(object instanceof Integer || object instanceof Long || object instanceof Boolean){
			stringBuilder.append("" + object + "");
		} else {
			stringBuilder.append("\"" + object + "\"");
		}
		return stringBuilder.toString();
	}
	
	/**
	 * 音频转码和相关参数上传
	 * @param userID  用户id
	 * @param bytes   音频字节数组
	 * @param suffix  数组名
	 * @param token   token
	 * @return
	 */
	public static byte[] generateRecordByte(int userID, String audioPath, String suffix, String token){
		String jsonPath = getUploadRecordJsonPath();
		byte[] jsonByte = null;
		if(!TextUtils.isEmpty(jsonPath)){
			File file = new File(jsonPath);
			if(file.exists()){
				file.delete();
			}
			try {
				file.createNewFile();
			} catch (Exception e) {
				// TODO: handle exception
			}
			byte[] byteAudio = readFileFromSD(audioPath);
			String jsonNotAudioString = hashTableToJsonString(paramsToHashtable(userID,suffix,token));
			StringBuffer stringBuffer = new StringBuffer(jsonNotAudioString);
			stringBuffer.deleteCharAt(stringBuffer.length()-1);
			stringBuffer.append(",\"AudioData\":\"" );
			String paramStart = stringBuffer.toString();
			String paramEnd = "\"}";
			writeEnd(jsonPath,paramStart);
			writeEnd(jsonPath,byteAudio);
			writeEnd(jsonPath,paramEnd);
			jsonByte = readFileFromSD(jsonPath);
		}
		return jsonByte;
	}
	
	
	//获取上传音频json字符串文件存放的路径
		public static String getUploadRecordJsonPath() {
			String mFileName = null;
			if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
				mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
				mFileName += "/uploadRecord.txt";
			}
			return mFileName;
		}
		
	/**
	 * 将字符串添加文件的末尾
	 * @param file
	 * @param content
	 */
		public static void writeEnd(String file, String content) {
			try {
				RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
				long fileLength = randomAccessFile.length();
				randomAccessFile.seek(fileLength);
				randomAccessFile.writeBytes(content);
				randomAccessFile.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		/**
		 * 将字节数组存入到文件中
		 * @param fileName文件路径
		 * @param content 字节数组
		 */
		public static void writeEnd(String fileName, byte[] content) {
			try {
				// 打开一个随机访问文件流，按读写方式
				RandomAccessFile randomFile = new RandomAccessFile(fileName, "rw");
				// 文件长度，字节数
				long fileLength = randomFile.length();
				// 将写文件指针移到文件尾。
				randomFile.seek(fileLength);
				String contentString = Base64.encodeToString(content,
						Base64.DEFAULT);
				randomFile.writeBytes(contentString);
				randomFile.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
	/**
	 * 从sd卡读取字节数组
	 * @param filePath  文件路径
	 * @return
	 */
	public static byte[] readFileFromSD(String filePath){
		File file = new File(filePath);
		byte[] data = null;
		if(!file.exists()){
			return null;
		}
		
		InputStream inputStream = null;
		try {
			inputStream = new BufferedInputStream(new FileInputStream(file));
			data = new byte[inputStream.available()];
			inputStream.read(data);
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			try {
				if(inputStream != null){
					inputStream.close();
				}
			} catch (Exception e2) {
				// TODO: handle exception
			}
			
		}
		return data;
	}
	
	/**
	 * 将流转换为字符串
	 * @return
	 */
	public static String covertStreamToString(InputStream inputStream){
		String result = null;
		
		
		return result;
	}
}
