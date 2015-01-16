package com.example.audiorecord.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * 图片操作工具类
 * @author zhangzhilai
 *
 */
public class ImageUtils {
	
	public static Bitmap getBitmapFromFile(String filePath){
		if(TextUtils.isEmpty(filePath)){
			return null;
		}
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 3;
		Bitmap bmpBitmap = BitmapFactory.decodeFile(filePath, options);
		return bmpBitmap;
	}
	
	
}
