package com.example.audiorecord.util;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * json字符串转换hashtable，Cursor
 * 
 * @author zhangzhilai
 * 
 */
public class JsonConvertUtils {

	public static final String RESULT_STATUS = "status";
	public static final String RESULT_MESSAGE = "msg";
	public static final String RESULT_CONTENT = "data";

	// 将json字符串转换为hashtable
	public static Hashtable<Object, Object> parseContent(String jsonString) {

		Hashtable<Object, Object> hsHashtable = new Hashtable<>();
		if (jsonString == null || "".equals(jsonString.trim())) {
			return null;
		}

		try {
			JSONObject jsonObject = new JSONObject(jsonString);
			hsHashtable.put(RESULT_STATUS, stringToValue(jsonObject, RESULT_STATUS));
			hsHashtable.put(RESULT_MESSAGE, stringToValue(jsonObject, RESULT_MESSAGE));
			if (jsonObject.has(RESULT_CONTENT)) {
				hsHashtable.put(RESULT_CONTENT, parserJSONObject(jsonObject.getJSONObject(RESULT_CONTENT)));
			}

		} catch (Exception e) {
			// TODO: handle exception
		}

		return hsHashtable;
	}

	// 将jsonobject转换成hashtable
	@SuppressWarnings("unchecked")
	public static Hashtable<Object, Object> parserJSONObject(JSONObject contentObject) {
    	Hashtable<Object, Object> hashtable = new Hashtable<>();
    	try {
			for(Iterator<String> iterator = contentObject.keys(); iterator.hasNext();){
				String key = (String)iterator.next();
				Object valueObject = contentObject.get(key);
				if(valueObject instanceof JSONObject){
					hashtable.put(key, parserJSONObject((JSONObject)valueObject));
				} else if(valueObject instanceof JSONArray){
					hashtable.put(key, parserJSONArray((JSONArray)valueObject));
				} else {
					hashtable.put(key, "" + valueObject);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return hashtable;
	}

	//将jsonarray转换成list
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static ArrayList parserJSONArray(JSONArray contentArray) {
		ArrayList aList = new ArrayList();
		try {
			for (int i = 0; i < contentArray.length(); i++) {
				Object object = contentArray.get(i);
				if (object instanceof JSONObject) {
					aList.add(parserJSONObject((JSONObject) object));
				} else if (object instanceof JSONArray) {
					aList.add(parserJSONArray((JSONArray) object));
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return aList;
	}

	// 根据键名获取对应的值
	public static String stringToValue(JSONObject jsonObject, String key) {

		try {
			if (jsonObject == null || key == null || "".equals(key.trim())) {
				return null;
			}
			return "" + jsonObject.get(key);
		} catch (Exception e) {
			return null;
		}
	}

	//获取json中内容的数组
	@SuppressWarnings("rawtypes")
	public static ArrayList getContentList(String jsonString){
		Hashtable<Object, Object> hashtable = parseContent(jsonString);
		Hashtable<Object, Object> hashtableData = (Hashtable)hashtable.get(RESULT_CONTENT);
		hashtableData.get(RESULT_CONTENT);
		ArrayList<Hashtable> arrayList = (ArrayList<Hashtable>)(hashtableData.get(RESULT_CONTENT));
		return (ArrayList) hashtableData.get(RESULT_CONTENT);
	}
	
	//将list转换成ContentValues
	public static ContentValues[] listToContentValues(ArrayList list){
		ContentValues[] conValues = new ContentValues[list.size()];
		for(int i = 0; i < list.size(); i++){
			Object object = list.get(i);
			ContentValues contentValues = new ContentValues();
			if(object instanceof Hashtable){
				Hashtable hashtable = (Hashtable)object;
				Enumeration enumeration = hashtable.keys();
				while (enumeration.hasMoreElements()) {
					String key = (String) enumeration.nextElement();
					String value = (String)hashtable.get(key);
					contentValues.put(key, value);
				}
			}
			conValues[i] = contentValues;
		}
		return conValues;
	}
	
}
