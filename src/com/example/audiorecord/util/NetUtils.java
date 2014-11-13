package com.example.audiorecord.util;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Base64;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * 用来执行网络请求操作
 * 
 * @author zhangzhilai
 * 
 */
public class NetUtils {

	public static final String TAG = "NetUtils";
	private static byte[] mContentBody;
	private static String mUrl;
	private static Context mContext;

	public static String postHttp(Context context, String url,
			byte[] contentBody) {
		String postResult = null;
		mUrl = url;
		mContext = context;
		mContentBody = contentBody;
		 PostAsynctask postAsynctask = new PostAsynctask();
		 postAsynctask.execute();


		return postResult;
	}

	public static class PostAsynctask extends
			AsyncTask<Integer, Integer, String> {

		@Override
		protected String doInBackground(Integer... params) {
			String postResult = null;
			InputStream inputStream = null;
			try {
				HttpPost httpPost = new HttpPost(mUrl);
				ByteArrayEntity byteArrayEntity = new ByteArrayEntity(
						mContentBody);
				byteArrayEntity.setContentEncoding(new BasicHeader(
						HTTP.CONTENT_TYPE, "application/json"));
				httpPost.setEntity(byteArrayEntity);

				HttpClient httpClient = new DefaultHttpClient();
				httpClient.getParams().setParameter(
						CoreConnectionPNames.CONNECTION_TIMEOUT, 10 * 1000);// 加入超时时间一共20秒
				httpClient.getParams().setParameter(
						CoreConnectionPNames.SO_TIMEOUT, 10 * 1000);
				HttpResponse httpResponse = httpClient.execute(httpPost);
				HttpEntity httpEntity = httpResponse.getEntity();

				if (httpEntity != null) {
					inputStream = httpEntity.getContent();
					postResult = IOUtils.toString(inputStream);
				}

			} catch (Exception e) {
				// TODO: handle exception
			}

			return postResult;
		}

	}

	// 这个使用AsyncHttpClient进行网络操作。。。但还没进行验证
	public static String postAsycHttp(Context context, String url,
			byte[] contentBody) {
		AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		String result = null;
		byte[] byteAudio = ConversionUtils.readFileFromSD(RecordUtils
				.getRecordPath());
		ByteArrayEntity byteArrayEntity = new ByteArrayEntity(byteAudio);
		InputStream inputStream = null;
		try {
			asyncHttpClient.post(context, url, byteArrayEntity,
					"application/json", new AsyncHttpResponseHandler() {

						@Override
						public void onFailure(int arg0, Header[] arg1,
								byte[] arg2, Throwable arg3) {
							LogUtil.d(TAG, "onFailure" + arg0);
						}

						@Override
						public void onSuccess(int arg0, Header[] arg1,
								byte[] arg2) {
							LogUtil.d(TAG, "onSuccess" + arg0);
						}

					});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
