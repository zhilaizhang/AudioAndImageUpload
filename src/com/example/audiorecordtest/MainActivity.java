package com.example.audiorecordtest;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Timer;
import java.util.TimerTask;

import com.example.audiorecord.Loader.BBSCursorLoader;
import com.example.audiorecord.util.ContextStrategy;
import com.example.audiorecord.util.ConversionUtils;
import com.example.audiorecord.util.HttpPostRequest;
import com.example.audiorecord.util.JsonConvertUtils;
import com.example.audiorecord.util.LogUtil;
import com.example.audiorecord.util.NetworkRequestAsynctask;
import com.example.audiorecord.util.RecordUtils;

import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.R.integer;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
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
import android.database.Cursor;

public class MainActivity extends ActionBarActivity implements LoaderManager.LoaderCallbacks<Cursor> {

	private Button mRecordStartButton;
	private Button mRecordEndButton;
	private Button mRecordPlayButton;
	private Button mRecordStopButton;
	private Button mRecordDeleteButton;
	private Button mRecordUploadButton;
	private Button mGetListButton;
	private Context mContext;

	private ContextStrategy contextStrategy;

	private String mUrl;
	private String requestString;
	private byte[] mUploadByte;

	private long mTimeStart;
	private long mTimeEnd;

	private TextView mRecordStatusTextView;
	private TextView mRecordTestTextView;
	private Timer mRecordTimer;
	private TimerTask mRecordTask;

	LoaderManager loaderManager = null;

	private final Uri URI = Uri.parse("content://com.example.audiorecordtest/items");

	private final int TIME_NUM = 0;
	private int i = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		findViews();
		initData();
		setListeners();
		mUrl = "http://st.hjapi.com/topic/TopicListByLeagueIDType";
		int UserID = 0;
		long LeagueID = 136413213;
		int CurrentPage = 1;
		int TopicAttrType = 2;
		int PageSize = 4;
		Hashtable<String, Serializable> postTable = ConversionUtils.generateLanguage(LeagueID, CurrentPage, PageSize, UserID, TopicAttrType);
		requestString = ConversionUtils.hashTableToJsonString(postTable);
		loaderManager = getSupportLoaderManager();
		loaderManager.initLoader(0, null, this);
	}

	private void findViews() {
		mRecordStartButton = (Button) findViewById(R.id.record_start_button);
		mRecordEndButton = (Button) findViewById(R.id.record_end_button);
		mRecordPlayButton = (Button) findViewById(R.id.record_play_button);
		mRecordStopButton = (Button) findViewById(R.id.record_stop_button);
		mRecordDeleteButton = (Button) findViewById(R.id.record_delete_button);
		mRecordUploadButton = (Button) findViewById(R.id.record_upload_button);
		mGetListButton = (Button) findViewById(R.id.getlist_button);

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
		mGetListButton.setOnClickListener(onClickListener);
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
				RecordUtils.startPlay(MainActivity.this);
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
			case R.id.getlist_button:
				mRecordStatusTextView.setText("获取列表");
				getList();
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

	private void uploadRecord() {
		String audioPath = RecordUtils.getRecordPath();
		String suffix = ".amr";
		mUrl = "http://st.hjapi.com/Topic/UploadAudio";
		uploadFiles(suffix, audioPath);

	}

	private void uploadImage() {
		String audioPath = RecordUtils.getRecordPath();
		String suffix = ".png";
		mUrl = "http://beta.st.hjapi.com/Topic/UploadImg";
		uploadFiles(suffix, audioPath);
	}

	private void uploadFiles(String suffix, String filePath) {
		int userID = 23114732;
		String token = "UThXNzwn351Gr70%2B6gNyO0a5RIr7mjzuSKqDSRGmfVE%3D";
		mUploadByte = ConversionUtils.generateRecordByte(userID, filePath, suffix, token);
		HttpPostRequest httpPostRequest = new HttpPostRequest();
		contextStrategy = new ContextStrategy(httpPostRequest);
		networkRequestAsynctask.execute();

	}

	BBSCursorLoader bbsCursorLoader;

	private void getList() {
			Cursor cursor = getContentResolver().query(URI, null, null, null, null);
			while(cursor.moveToNext()){
				for(int i = 0; i < cursor.getColumnCount(); i++){
					System.out.println("--test888--i= " + i + " values: " + cursor.getString(i) );
				}
			}
	}

	NetworkRequestAsynctask networkRequestAsynctask = new NetworkRequestAsynctask() {

		@Override
		public String networkOperate() {
			return contextStrategy.networkRequestStrategy(mUrl, mUploadByte);
		}

		// 网络返回后的数据处理
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

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		System.out.println("test8888 onCreateLoader");
		bbsCursorLoader = new BBSCursorLoader(mContext) {
			public String networkOperate() {
				LogUtil.d("test", "test8888 networkOperate");
				System.out.println("test8888 networkOperate");
				HttpPostRequest httpPostRequest = new HttpPostRequest();
				contextStrategy = new ContextStrategy(httpPostRequest);
				return contextStrategy.networkRequestStrategy(mUrl, requestString);
			}

			public Cursor jsonToCursor(String jsonString) {
				LogUtil.d("test", "test8888 jsonToCursor");
				System.out.println("test8888 jsonString" + jsonString);
				ArrayList arrylist = JsonConvertUtils.getContentList(jsonString);
				System.out.println("test8888 arrylist" + arrylist);
				ContentValues[] contentValues = JsonConvertUtils.listToContentValues(arrylist);
				System.out.println("test8888 contentValues" + contentValues);
//				long no = getContentResolver().insert(URI, contentValues);
				long no = getContentResolver().bulkInsert(URI, contentValues);
				System.out.println("test888  no:" + no);
				return null;
			}
		};
		return bbsCursorLoader;
	}


	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		// TODO Auto-generated method stub
		
	}

}
