package com.example.audiorecord.util;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
/**
 * 本类主要功能有录音，播放
 * @author zhangzhilai
 *
 */
public class RecordUtils {
	
	public static final String TAG = "RecordUtils";
	
	public static MediaRecorder mRecorder;
	public static MediaPlayer mPlayer;
	//获得录音的存放地址
	public static String getRecordPath() {
		String mFileName = null;
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
			mFileName += "/Record.amr";
		}
		return mFileName;
	}
	//开始录音
	public static boolean startToRecord() {
		if(TextUtils.isEmpty(getRecordPath())){
			return false;
		}
		stopRecording();
		mRecorder = new MediaRecorder();
		mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		mRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);

		String fileName = getRecordPath();
		if (TextUtils.isEmpty(fileName)) {
			return false;
		}
		mRecorder.setOutputFile(fileName);
		mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

		try {
			mRecorder.prepare();
		} catch (IOException e) {
			Log.e("record", "prepare() failed");
			return false;
		}
		LogUtil.d(TAG, "startToRecord");
		mRecorder.start();
		return true;
	}
	//停止录音
	public static void stopRecording() {
		if (mRecorder != null) {
			try {
				mRecorder.stop();
				mRecorder.release();
				mRecorder = null;
			} catch (IllegalStateException e) {
				e.printStackTrace();
			}
		}
		LogUtil.d(TAG, "stopRecording");
	}
	
	//开始播放
	public static void startPlay(final Activity activity) {
		if(mPlayer==null){
			mPlayer = new MediaPlayer();
		}
		mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
			
			@Override
			public void onCompletion(MediaPlayer mp) {
				LogUtil.d(TAG, "播放完啦");
			}
		});
		String fileName = getRecordPath();
		try {
			if (!TextUtils.isEmpty(fileName)&&!mPlayer.isPlaying()) {
				mPlayer.reset();
				mPlayer.setDataSource(fileName);
				mPlayer.prepare();
				mPlayer.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
			Log.e("record", "prepare() failed");
		}
		LogUtil.d(TAG, "startPlay");
	}
	//判断是否在播放
	public static boolean isPlay(){
		if (mPlayer != null) { 
			return mPlayer.isPlaying();
		}
		return false;
	}
	
	//停止播放
	public static void stopPlay() {
		LogUtil.d(TAG, "stopPlay");
		if (mPlayer != null) { 
        	try {
				mPlayer.stop();  
				mPlayer.release();   
				mPlayer = null;
			} catch (IllegalStateException e) {
				e.printStackTrace();
			}   
        }   
	}
	
	/**
	 * 删除音频
	 */
	public static void deleteRecord() {
		LogUtil.d(TAG, "deleteRecord");
		String path = getRecordPath();
		if(!TextUtils.isEmpty(path)){
			File file = new File(getRecordPath());
			if (file.exists()) {
				file.delete();
			}
		}
	}
	
	public static void uploadAudio() {
		
	}
}
