package com.example.audiorecord.util;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;

/**
 * 经过实践操作发现这个有问题，因为当外部调用getRequestResult这个方法时，里面异步的asynctask还在执行网络操作时，方法就已经返回值了，
 * 所以是失败的方案 实现网络请求接口() httppost请求
 * 
 * @author zhangzhilai
 * 
 */
public class HttpPostRequest implements INetworkRequestStrategy {

	private String mUrl;
	private final String mRequestFail = "fail";

	private byte[] mContenBody;

	/**
	 * 获取传递进来的参数，进行网络请求返回字符串
	 */
	public String getRequestResult(String url, byte[] contentBody) {
		mUrl = url;
		mContenBody = contentBody;
		return networkOperate();
	}

	public String getRequestResult(String url, String contentBody){
		mUrl = url;
		try {
			mContenBody = contentBody.getBytes("UTF8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return mRequestFail;
		}
		return networkOperate();
	}
	
	public String networkOperate() {
		String postResult = null;
		InputStream inputStream = null;
		try {
			HttpPost httpPost = new HttpPost(mUrl);
			ByteArrayEntity byteArrayEntity = new ByteArrayEntity(mContenBody);
			byteArrayEntity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
			httpPost.setEntity(byteArrayEntity);

			HttpClient httpClient = new DefaultHttpClient();
			httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 10 * 1000);// 加入超时时间一共20秒
			httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 10 * 1000);
			HttpResponse httpResponse = httpClient.execute(httpPost);

			HttpEntity httpEntity = httpResponse.getEntity();
			if (httpEntity != null) {
				inputStream = httpEntity.getContent();
				postResult = IOUtils.toString(inputStream);
			}
		} catch (Exception e) {
			return mRequestFail;
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (Exception e) {
				return mRequestFail;
			}
		}
		LogUtil.d("","postResult" + postResult);
		return postResult;
	}

}
