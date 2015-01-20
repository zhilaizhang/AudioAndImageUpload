package com.example.audiorecord.ContentProvider;

import com.example.audiorecord.model.LeagueModel;

import android.R.integer;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

public class TestProvider extends ContentProvider{

	public static final String TAG = "TestProvider";
	
	private final String DB_NAME = "Leagues.db";
	private final String DB_TABLE = "LeaguesTable";
	private static final String AUTHORITY = "com.example.audiorecordtest";
	private static final String PATH = "items";
	
	private static final int ITEM = 1;
	private static final int ITMES = 2;
	
	private int DB_VERSION = 1;
	
	
	private DBHelper dbHelper = null;
	
	private Context mContext;
	private static  UriMatcher uriMatcher;
	
	
	static{
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(AUTHORITY, PATH, ITEM);
		uriMatcher.addURI(AUTHORITY, PATH, ITMES);
	}
	
	private final String DB_CREATE_STRING = "create table " + DB_TABLE + " (" + LeagueModel.TopicID + " text, " + LeagueModel.BoardID + " text, " +
			LeagueModel.Title + " text, " + LeagueModel.Summary + " text, "  + LeagueModel.IsTop + " text, "  + LeagueModel.IsBest + " text, "  +
			LeagueModel.IsHot + "text,"  + LeagueModel.ReplyNum + "text,"  + LeagueModel.ThumbImg + "text,"  + LeagueModel.RateSum + "text," +
			LeagueModel.LastReplyDate + " text, " + LeagueModel.LastReplyUserID + " text, " + LeagueModel.LastReplyUserName + " text, " + 
			LeagueModel.AudioPath + " text);";
	
	@Override
	public boolean onCreate() {
		mContext = getContext();
		dbHelper = new DBHelper(mContext, DB_NAME, null, DB_VERSION);
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		System.out.println("--test88--query");
		Cursor cursor = null;
		switch (uriMatcher.match(uri)) {
		case ITMES:
			System.out.println("--test88--ITMES");
			cursor = db.query(DB_TABLE, null, null, null, null, null, null);
			System.out.println("--test88--cursor number: " + cursor.getColumnCount());
			while(cursor.moveToNext()){
				System.out.println("--test88--moveToNext: ");
				for(int i = 0; i < cursor.getColumnCount(); i++){
					System.out.println("--test888--i= " + i + " values: " + cursor.getColumnIndex("Title"));
				}
			}
			System.out.println("--test88--cursor: " + cursor);
			break;
		default:
			break;
		}
		return cursor;
	}

	@Override
	public String getType(Uri uri) {
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		System.out.println("--test888--insert");
		if(uriMatcher.match(uri) == ITEM){
			System.out.println("---test88--item:" + ITEM);
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			long id = db.insert(DB_TABLE, null, values);
		}
		return uri;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		return 0;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		return 0;
	}

	private class DBHelper extends SQLiteOpenHelper{

		public DBHelper(Context context, String name, CursorFactory factory, int version) {
			super(context, name, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			System.out.println("--test888---onCreate");
			System.out.println("--test888---DB_CREATE_STRING" + DB_CREATE_STRING);
			db.execSQL(DB_CREATE_STRING);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			
		}
		
	}
}
