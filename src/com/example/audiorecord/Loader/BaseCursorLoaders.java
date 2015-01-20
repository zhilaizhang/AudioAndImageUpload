package com.example.audiorecord.Loader;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.content.CursorLoader;

public abstract class BaseCursorLoaders extends CursorLoader{

	public BaseCursorLoaders(Context context) {
		super(context);
	}
	
	public BaseCursorLoaders(Context context, Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		super(context, uri, projection, selection, selectionArgs, sortOrder);
	}

	@Override
	protected void onStartLoading() {
		super.onStartLoading();
		forceLoad();
	}
	
	@Override
	public Cursor loadInBackground() {
		System.out.println("test88 loadInBackground");
		return jsonToCursor(networkOperate());
	}
	//执行网络请求
	public abstract String networkOperate();
	
	//将网络请求的json数据转化Cursor
	public abstract Cursor jsonToCursor(String jsonString);
	
}
