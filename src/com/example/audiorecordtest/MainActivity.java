package com.example.audiorecordtest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity implements View.OnClickListener {

	public static final String TAG = "WelcomeActivity";

	private Button mToAudioButton;
	private Button mToPictureButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		findViews();
		setListeners();
	}

	private void findViews() {
		mToAudioButton = (Button) findViewById(R.id.btn_audio);
		mToPictureButton = (Button) findViewById(R.id.btn_picture);
	}

	private void setListeners() {
		mToAudioButton.setOnClickListener(this);
		mToPictureButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.btn_audio:
			intent.setClass(MainActivity.this, AudioActivity.class);
			break;
		case R.id.btn_picture:
			intent.setClass(MainActivity.this, PictureActivity.class);
			break;
		default:
			break;
		}
		startActivity(intent);
	}

}
