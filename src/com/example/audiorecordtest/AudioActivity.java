package com.example.audiorecordtest;

import java.util.Timer;
import java.util.TimerTask;

import com.example.audiorecord.util.ContextStrategy;
import com.example.audiorecord.util.ConversionUtils;
import com.example.audiorecord.util.HttpPostRequest;
import com.example.audiorecord.util.LogUtil;
import com.example.audiorecord.util.NetworkRequestAsynctask;
import com.example.audiorecord.util.RecordUtils;

import android.text.TextUtils;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class AudioActivity extends Activity {

	private Button mRecordStartButton;
	private Button mRecordEndButton;
	private Button mRecordPlayButton;
	private Button mRecordStopButton;
	private Button mRecordDeleteButton;
	private Button mRecordUploadButton;
	private Context mContext;

	private ContextStrategy contextStrategy;
	
	private String mUrl;
	private byte[] mUploadByte;
	
	private long mTimeStart;
	private long mTimeEnd;

	private TextView mRecordStatusTextView;
	private TextView mRecordTestTextView;
	private Timer mRecordTimer;
	private TimerTask mRecordTask;

	private final int TIME_NUM = 0;
	private int i = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_audio);
		findViews();
		initData();
		setListeners();
	}

	private void findViews() {
		mRecordStartButton = (Button) findViewById(R.id.record_start_button);
		mRecordEndButton = (Button) findViewById(R.id.record_end_button);
		mRecordPlayButton = (Button) findViewById(R.id.record_play_button);
		mRecordStopButton = (Button) findViewById(R.id.record_stop_button);
		mRecordDeleteButton = (Button) findViewById(R.id.record_delete_button);
		mRecordUploadButton = (Button) findViewById(R.id.record_upload_button);
		
		mRecordStatusTextView = (TextView) findViewById(R.id.record_status_textview);
		mRecordTestTextView = (TextView) findViewById(R.id.record_test_textview);
		
	}

	private void initData() {
		mContext = this;
		mTimeStart = 0;
		mTimeEnd = 0;
	}

	private void setListeners() {
		mRecordStartButton.setOnClickListener(onClickListener);
		mRecordEndButton.setOnClickListener(onClickListener);
		mRecordPlayButton.setOnClickListener(onClickListener);
		mRecordStopButton.setOnClickListener(onClickListener);
		mRecordDeleteButton.setOnClickListener(onClickListener);
		mRecordUploadButton.setOnClickListener(onClickListener);
	}

	OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.record_start_button:
				mRecordStatusTextView.setText("开始录音");
				startRecord();
				break;
			case R.id.record_end_button:
				mRecordStatusTextView.setText("结束录音");
				RecordUtils.stopRecording();
				break;
			case R.id.record_play_button:
				mRecordStatusTextView.setText("开始播放");
				RecordUtils.startPlay(AudioActivity.this);
				break;
			case R.id.record_stop_button:
				mRecordStatusTextView.setText("停止播放");
				RecordUtils.stopPlay();
				break;
			case R.id.record_delete_button:
				mRecordStatusTextView.setText("删除音频");
				RecordUtils.deleteRecord();
				break;
			case R.id.record_upload_button:
				mRecordStatusTextView.setText("上传音频");
				uploadRecord();
				break;
			}
		}

	};

	private void startRecord() {
		mRecordTimer = new Timer();
		mRecordTask = new TimerTask() {

			@Override
			public void run() {
				mHandler.sendEmptyMessage(TIME_NUM);
			}
		};
		mRecordTimer.schedule(mRecordTask, 1000);
		if (!RecordUtils.startToRecord()) {
			if (TextUtils.isEmpty(RecordUtils.getRecordPath())) {
				Toast.makeText(mContext, "录音失败,无法检测到SD卡", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(mContext, "录音失败", Toast.LENGTH_SHORT).show();
			}
		}
	}

	private void uploadRecord(){
		int userID = 23114732;
		mUrl = "http://st.hjapi.com/Topic/UploadAudio";
		String audioPath = RecordUtils.getRecordPath();
		String suffix = ".amr";
		String token = "UThXNzwn351Gr70%2B6gNyO0a5RIr7mjzuSKqDSRGmfVE%3D";
		mUploadByte = ConversionUtils.generateRecordByte(userID,audioPath,suffix,token);
		HttpPostRequest httpPostRequest = new HttpPostRequest();
		contextStrategy = new ContextStrategy(httpPostRequest);
		networkRequestAsynctask.execute();
		
	}
	
	NetworkRequestAsynctask  networkRequestAsynctask = new NetworkRequestAsynctask() {
		
		@Override
		public String networkOperate() {
			return contextStrategy.networkRequestStrategy(mUrl, mUploadByte);
		}
		
		//网络返回后的数据处理
		@Override
		public void setResult(String result) {
			System.out.println("--test--result:" + result);
			LogUtil.d("test", "result" + result);
		}
		
	};
	
	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Bundle bundle = msg.getData();
			switch (msg.what) {
			case 0:
				mRecordTestTextView.setText("----test--handler-" + i++);
				break;
			}
		}

	};

	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
