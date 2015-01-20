package com.example.audiorecord.util;

import java.io.File;

import android.os.Environment;
import android.renderscript.Element;

public class ConfigUtils {
	public static final String ROOT_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
	public static final String APP_PATH = ROOT_PATH + File.separator + "AUDIO_Record" + File.separator + "images" + File.separator;
	public static final String TEMP_IMAGE_NAME = "tempImage";
	
	public static boolean generateImagePath(String path){
		if(path == null || path.equals("")){
			return false;
		}
		if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
			File file = new File(path);
			if(!file.exists()){
				file.mkdirs();
				return true;
			} else {
				return true;
			}
		} else {
			return false;
		}
	}
	
	public static String generateTempImage(){
		return APP_PATH + TEMP_IMAGE_NAME + ".jpg";
	}
	
}	
