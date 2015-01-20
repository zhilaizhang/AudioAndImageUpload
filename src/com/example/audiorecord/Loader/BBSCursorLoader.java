package com.example.audiorecord.Loader;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

public class BBSCursorLoader extends BaseCursorLoaders{

	public BBSCursorLoader(Context context) {
		super(context);
	}
	
	public BBSCursorLoader(Context context, Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		super(context, uri, projection, selection, selectionArgs, sortOrder);
	}

	@Override
	public String networkOperate() {
		return null;
	}

	@Override
	public Cursor jsonToCursor(String jsonString) {
		return null;
	}


}
