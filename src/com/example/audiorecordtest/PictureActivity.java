package com.example.audiorecordtest;

import java.io.File;

import com.example.audiorecord.util.ConfigUtils;
import com.example.audiorecord.util.ImageUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.util.Log;
import android.view.View;

public class PictureActivity extends Activity implements View.OnClickListener {

	public static final String TAG = "PictureActivity";
	
	public final int REQUESTCODE_PIC = 101;  //拍照获取图片
	public final int REQUESTCODE_ALBUM = 102; //从相册获取
	
	private Context mContext;
	private Button mGetImageButton;
	private Button mUploadImageButton;

	private ImageView mDisplayImageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_picture);
		mContext = PictureActivity.this;
		findViews();
		setListeners();
	}

	private void findViews() {
		mGetImageButton = (Button) findViewById(R.id.btn_get_image);
		mUploadImageButton = (Button) findViewById(R.id.btn_upload_image);
		mDisplayImageView = (ImageView) findViewById(R.id.img_display);
	}

	private void setListeners() {
		mGetImageButton.setOnClickListener(this);
		mUploadImageButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_get_image:
			getImage();
			break;
		case R.id.btn_upload_image:

			break;
		default:
			break;
		}
	}

	private void getImage() {
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setTitle(R.string.picture_select);
		builder.setItems(R.array.picture_get_type, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = null;
				String tempImagePath;
				switch (which) {
				case 0:
					intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					tempImagePath = ConfigUtils.generateTempImage();
					intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(tempImagePath)));
					startActivityForResult(intent, REQUESTCODE_PIC);
					break;
				case 1:
					intent = new Intent();
					if(Build.VERSION.SDK_INT < 19){
						intent.setAction(Intent.ACTION_GET_CONTENT);
					} else {
						intent.setAction(Intent.ACTION_PICK);
					}
					intent.setType("image/*");
					startActivityForResult(intent, REQUESTCODE_ALBUM);
//					Toast.makeText(mContext, "" + Environment.getExternalStorageDirectory().getAbsolutePath(), Toast.LENGTH_SHORT).show();
					break;
				default:
					break;
				}
			}
		});
		builder.show();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Bitmap bitmap = null;
		Toast.makeText(mContext, "requestCode:" + requestCode, Toast.LENGTH_SHORT).show();
		if(requestCode == REQUESTCODE_PIC){
//			Uri uri = data.getData();
//			Bundle bundle = data.getExtras();
//			Log.d(TAG, "uri: " + uri);
//			bitmap = (Bitmap)bundle.get("data");
			bitmap = ImageUtils.getBitmapFromFile(ConfigUtils.APP_PATH + ConfigUtils.TEMP_IMAGE_NAME);
			mDisplayImageView.setImageBitmap(bitmap);
//			Toast.makeText(mContext, "Uri:" + uri, Toast.LENGTH_SHORT).show();
		} else if(requestCode == REQUESTCODE_ALBUM) {
			if(resultCode == RESULT_OK){
				Uri uri = data.getData();
				ContentResolver cResolver = getContentResolver();
				try {
					if(bitmap != null){
						bitmap.recycle();
					}
					bitmap = BitmapFactory.decodeStream(cResolver.openInputStream(uri));
				} catch (Exception e) {
				}
				mDisplayImageView.setImageBitmap(bitmap);
			}
		}
	}
}
