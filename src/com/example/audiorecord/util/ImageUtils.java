package com.example.audiorecord.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;

import android.R.integer;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

/**
 * 图片的操作类
 * @author zhangzhilai
 *
 */
public class ImageUtils {
	
	public static final String TAG = "ImageUtils";
	
	/**
	 * 获取图片的默认路径
	 * @return
	 */
	public String getImagePath() {
		String path = null;
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			path = Environment.getExternalStorageDirectory().getAbsolutePath();
			path += "/test.png";
		}
		return path;
	}
	
	//根据图片路径进行外观大小压缩
	public Bitmap imageSizeCompress(String imagePath, int width, int height){
		Bitmap bitmap = null;
		
		final BitmapFactory.Options options = new BitmapFactory.Options();
		
		// 第一次解析将inJustDecodeBounds设置为true，只会用来获取图片大小，而不会真正去转换图片为bitmap
		options.inJustDecodeBounds = true;
		
		BitmapFactory.decodeFile(imagePath, options);
		options.inSampleSize = calculateInSampleSize(options, width, height);
		// 第一次解析将inJustDecodeBounds设置为false，真正去转换图片为bitmap
		options.inJustDecodeBounds = false;
		bitmap = BitmapFactory.decodeFile(imagePath, options);
		return bitmap;
	}
	
	//对bitmap图片进行质量压缩
	private  byte[]  imageQualityCompress(Bitmap bitmap){
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		int quality = 100;
		while(baos.toByteArray().length/1024 > 100){ //判断如果图片大于100k,进行压缩避免生成图片
			baos.reset();
			quality = quality - 10;
			bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
		}
		if(bitmap != null && bitmap.isRecycled()){
			bitmap.recycle();
		}
		return baos.toByteArray();
	}

	private byte[] imagePathQualityCompress(String path){
		return ConversionUtils.readFileFromSD(path);
	}
	
	/**
	 * 返回当前图片和目标图片大小的比例用来进行压缩
	 * @param options 当前图片的一些属性
	 * @param targetWidth  目标图片的宽度
	 * @param targetHeight  目标图片的高度
	 * @return
	 */
	private int calculateInSampleSize(BitmapFactory.Options options, int targetWidth, int targetHeight) {
		
		//原图片的宽高
		final int width = options.outWidth;  
		final int height = options.outHeight;
		int inSampleSize = 1;
		
		//获取最大的长度和宽度比例
		if(width > targetWidth || height > targetHeight){
			final int heightRatio = Math.round((float)height / (float)targetHeight);
			final int widthRatio = Math.round((float)width / (float)targetWidth);
			inSampleSize = Math.max(heightRatio, widthRatio);
		}
		return inSampleSize;
	}
	
}
