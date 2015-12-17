package com.seven.textedit.database;

import com.seven.textedit.database.Tables.TextTable;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper{
	private final static String TAG = DatabaseHelper.class.getSimpleName();
	private Context context;
	private final static String DATABASE_NAME = "textTables.db";
	private final static int TABLE_VERSION = 1;
	
	public static String textTable = "Create table if not exists " + TextTable.mTableName + "(" + TextTable.mId
            + " integer primary key autoincrement," + TextTable.mIndex + " integer, " + TextTable.mNewIndex
            + " integer, " + TextTable.mTextName + " text, " + TextTable.mTextContentUri + " text, " + TextTable.mAudioUri
            + " text, " + TextTable.mVideoUri + " text, " + TextTable.mShared + " text);";
//	public DatabaseHelper(Context context, String name, CursorFactory factory, int version) {
//		super(context, name, factory, version);
//		// TODO Auto-generated constructor stub
//	}
	//在SQLiteOpenHelper的子类当中，必须有该构造函数
	public DatabaseHelper(Context context, String name, CursorFactory factory, int version)
	{
		super(context, DATABASE_NAME, null, TABLE_VERSION);
		this.context = context;
	}
	
	public DatabaseHelper(Context context, String name, int version)
	{
		this(context, name, null, version);
	}
	
	public DatabaseHelper(Context context)
	{
		this(context, DATABASE_NAME, TABLE_VERSION);
	}

	@Override
	public String getDatabaseName() {
		// TODO Auto-generated method stub
		return super.getDatabaseName();
	}

//	@Override
//	public void setWriteAheadLoggingEnabled(boolean enabled) {
//		// TODO Auto-generated method stub
//		super.setWriteAheadLoggingEnabled(enabled);
//	}

	@Override
	public SQLiteDatabase getWritableDatabase() {
		// TODO Auto-generated method stub
		return super.getWritableDatabase();
	}

	@Override
	public SQLiteDatabase getReadableDatabase() {
		// TODO Auto-generated method stub
		return super.getReadableDatabase();
	}

	@Override
	public synchronized void close() {
		// TODO Auto-generated method stub
		super.close();
		Log.v(TAG, "close a Database");
	}

//	@Override
//	public void onConfigure(SQLiteDatabase db) {
//		// TODO Auto-generated method stub
//		super.onConfigure(db);
//	}
	
	//onCreate(SQLiteDatabase db)该函数是在第一次创建数据库的时候执行，实际上是在第一次得到SQLiteDatabase对象的时候才调用
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		Log.v(TAG, "Create a Database");
		createTable(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		Log.v(TAG, "Upgrade a Database");
	}

	@Override
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		super.onDowngrade(db, oldVersion, newVersion);
		Log.v(TAG, "Downgrade a Database");
	}

	@Override
	public void onOpen(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		super.onOpen(db);
		Log.v(TAG, "Open a Database");
	}

	public void createTable(SQLiteDatabase db)
	{
		db.execSQL(textTable);
	}
}
