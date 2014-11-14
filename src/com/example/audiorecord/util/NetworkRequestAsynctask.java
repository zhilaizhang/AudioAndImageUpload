package com.example.audiorecord.util;


import android.os.AsyncTask;
/**
 * 
 * @author zhangzhilai
 *
 */
public abstract class NetworkRequestAsynctask extends AsyncTask<Object, Integer, String>{

	@Override
	protected String doInBackground(Object... params) {
		
		return networkOperate();
	}
	
	@Override
	protected void onPostExecute(String result) {
		setResult(result);
	}
	
	//进行网络操作
	public abstract String  networkOperate();
	//设置获取的值
	public abstract void setResult(String result);
}

