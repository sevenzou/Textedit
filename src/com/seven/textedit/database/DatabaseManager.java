package com.seven.textedit.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.seven.textedit.database.Tables.TextContent;
import com.seven.textedit.database.Tables.TextTable;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DatabaseManager {
	public final static String TAG = DatabaseManager.class.getSimpleName();
	private DatabaseHelper dbHelper;
	private SQLiteDatabase sqliteDb;
	private static DatabaseManager dbMg = null;
	static final Object sycnObject = new Object();
	private Context context = null;
	private boolean dataBaseReadOnly = false;
	
	public static DatabaseManager getInstance(Context ct)
	{
		if (dbMg == null) {
			synchronized (sycnObject) {
				if (dbMg == null) {
					dbMg = new DatabaseManager(ct);
				}
			}
		}
		return dbMg;
	}
	
	private DatabaseManager(Context context)
	{
		this.context = context;
		dbHelper = new DatabaseHelper(context);
		try {
			sqliteDb = dbHelper.getWritableDatabase();
			dataBaseReadOnly = false;
		} catch(Exception e) {
			sqliteDb = dbHelper.getReadableDatabase();
			dataBaseReadOnly = true;
		}
	}
	
	//add()for test
	public void add(List<TextTable> textTables)
	{
		sqliteDb.beginTransaction();
		try {
			for(TextTable textTable : textTables)
				sqliteDb.execSQL("insert into "+textTable.mTableName+"Values(?,?,?,?)", new Object[]{});
			sqliteDb.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sqliteDb.endTransaction();
		}
		
	}
	
	/**
	 * insertData()
	 */
	public void insertData(String tableName, Map<String, Object> map)
	{
		sqliteDb.beginTransaction();
		try {
			ContentValues cv = new ContentValues();
			if (map.size() > 0) {
				int i = 0;
				for (Map.Entry<String, Object> entry : map.entrySet()) {
					if (entry.getValue() instanceof String) {
						cv.put(entry.getKey(), String.valueOf(entry.getValue()));
					} else if (entry.getValue() instanceof Integer) {
						cv.put(entry.getKey(), (Integer)entry.getValue());
					}
				}
			}
			sqliteDb.insert(tableName, null, cv);
			sqliteDb.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sqliteDb.endTransaction();
		}
	}
	
	public void insertData(String tableName, TextContent textConetent)
	{
		sqliteDb.beginTransaction();
		try {
			ContentValues cv = new ContentValues();
			cv.put(TextTable.mIndex, textConetent.mIndex);
			cv.put(TextTable.mNewIndex, textConetent.mNewIndex);
			cv.put(TextTable.mTextName, textConetent.mTextName);
			cv.put(TextTable.mTextContentUri, textConetent.mTextContentUri);
			cv.put(TextTable.mAudioUri, textConetent.mAudioUri);
			cv.put(TextTable.mVideoUri, textConetent.mVideoUri);
			cv.put(TextTable.mShared, textConetent.mShared);
			sqliteDb.insert(tableName, null, cv);
			sqliteDb.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sqliteDb.endTransaction();
		}
	}
	
	/**
	 * replaceData()
	 */
	public void replaceData(String tableName, TextContent textConetent)
	{
		sqliteDb.beginTransaction();
		try {
			ContentValues cv = new ContentValues();
			cv.put(TextTable.mIndex, textConetent.mIndex);
			cv.put(TextTable.mNewIndex, textConetent.mNewIndex);
			cv.put(TextTable.mTextName, textConetent.mTextName);
			cv.put(TextTable.mTextContentUri, textConetent.mTextContentUri);
			cv.put(TextTable.mAudioUri, textConetent.mAudioUri);
			cv.put(TextTable.mVideoUri, textConetent.mVideoUri);
			cv.put(TextTable.mShared, textConetent.mShared);
			sqliteDb.replace(tableName, null, cv);
			sqliteDb.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sqliteDb.endTransaction();
		}
	}
	
	/**
	 * updateData()
	 */
	public void updateData(String tableName, Map<String, Object> map, Map<String, String> whereMap)
	{
		sqliteDb.beginTransaction();
		try {
			ContentValues cv = new ContentValues();
			String whereClause = "1=1";
			String[] whereArgs = new String[whereMap.size()];
			if (whereMap.size() > 0) {
				int i = 0;
				for (Map.Entry<String, String> entry : whereMap.entrySet()) {
					whereClause += " AND " + entry.getKey() + "=?";
					whereArgs[i++] = entry.getValue();
				}
			}
			if (map.size() > 0) {
				int i = 0;
				for (Map.Entry<String, Object> entry : map.entrySet()) {
					if (entry.getValue() instanceof String) {
						cv.put(entry.getKey(), String.valueOf(entry.getValue()));
					} else if (entry.getValue() instanceof Integer) {
						cv.put(entry.getKey(), (Integer)entry.getValue());
					}
				}
			}
			sqliteDb.update(tableName, cv, whereClause, whereArgs);
			sqliteDb.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sqliteDb.endTransaction();
		}
	}
	
	public void updateData(String tableName, TextContent textConetent, Map<String, String> whereMap)
	{
		sqliteDb.beginTransaction();
		try {
			ContentValues cv = new ContentValues();
			cv.put(TextTable.mIndex, textConetent.mIndex);
			cv.put(TextTable.mNewIndex, textConetent.mNewIndex);
			cv.put(TextTable.mTextName, textConetent.mTextName);
			cv.put(TextTable.mTextContentUri, textConetent.mTextContentUri);
			cv.put(TextTable.mAudioUri, textConetent.mAudioUri);
			cv.put(TextTable.mVideoUri, textConetent.mVideoUri);
			cv.put(TextTable.mShared, textConetent.mShared);
//			String whereClause = TextTable.mIndex+" = ?";
//			String[] whereArgs = {String.valueOf(_id)};
			String whereClause = "1=1";
			String[] whereArgs = new String[whereMap.size()];
			if (whereMap.size() > 0) {
				int i = 0;
				for (Map.Entry<String, String> entry : whereMap.entrySet()) {
					whereClause += " AND " + entry.getKey() + "=?";
					whereArgs[i++] = entry.getValue();
				}
			}
			
			sqliteDb.update(tableName, cv, whereClause, whereArgs);
			sqliteDb.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sqliteDb.endTransaction();
		}
	}
	
	/**
	 * deleteData()
	 */
	public void deleteData(String tableName, Map<String, String> whereMap)
	{
		sqliteDb.beginTransaction();
		try {
			String whereClause = "1=1";
			String[] whereArgs = new String[whereMap.size()];
			if (whereMap.size() > 0) {
				int i = 0;
//				List<Object> list = new ArrayList<Object>();
				for (Map.Entry<String, String> entry : whereMap.entrySet()) {
					whereClause += "AND" + entry.getKey() + "=?";
					whereArgs[i++] = entry.getValue();
				}
//				list.add(whereClause);
//				list.add(whereArgs);
			}
			sqliteDb.delete(tableName, whereClause, whereArgs);
			sqliteDb.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sqliteDb.endTransaction();
		}
	}
	
	public void deleteData(String tableName, int mId)
	{
		sqliteDb.beginTransaction();
		try {
			String whereClause = TextTable.mId+" = ?";
			String[] whereArgs = {String.valueOf(mId)};
			
			sqliteDb.delete(tableName, whereClause, whereArgs);
			sqliteDb.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sqliteDb.endTransaction();
		}
	}
	
	public void deleteData(String tableName)
	{
		sqliteDb.beginTransaction();
		try {
			sqliteDb.delete(tableName, "1=1", new String[]{});
			sqliteDb.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sqliteDb.endTransaction();
		}
	}
	
	/**
	 * queryData()
	 */
	public List<Map<String,String>> queryData(String tableName, Map<String, String> whereMap, String orderBy, boolean order)
	{
		List<Map<String,String>> result = new ArrayList<Map<String,String>>();
		sqliteDb.beginTransaction();
		try {
			Cursor cursor = null;
			boolean distinct = true;
			String[] columns = null;
			String selection = "1=1";
	        String[] selectionArgs = new String[whereMap.size()];;
	        String groupBy = null;
	        String having = null;
	        String limit = null;
	        String orderStr = null;
	        if (!orderBy.equals("")) {
	        	orderBy += order ? " asc" : " desc";//asc/desc:ÉýÐò/½µÐò
	        }
	        
	        if (whereMap.size() > 0) {
	        	int i = 0;
	        	for (Map.Entry<String, String> entry : whereMap.entrySet()) {
	        		selection += " AND " + entry.getKey() + " =? ";
	        		selectionArgs[i++] = entry.getValue();
				}
	        }
	        
			cursor = sqliteDb.query(distinct, tableName, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
			while (cursor.moveToNext())
	        {
	            Map<String, String> map = new HashMap<String, String>();
	            for (int i = 0; i < cursor.getColumnCount(); i++)
	            {
	                String name = cursor.getColumnName(i);
	                if (cursor.getType(i) == Cursor.FIELD_TYPE_STRING)
	                {
	                	map.put(name, cursor.getString(i));
	                }
	                else if (cursor.getType(i) == Cursor.FIELD_TYPE_INTEGER)
	                {
	                	map.put(name, cursor.getInt(i) + "");
	                }
	            }
	            result.add(map);
	        }
	        cursor.close();
	        sqliteDb.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sqliteDb.endTransaction();
		}
        return result;
	}
	
	public List<Map<String,String>> query(String tableName, String orderBy, boolean order)
	{
		List<Map<String,String>> result = new ArrayList<Map<String,String>>();
		sqliteDb.beginTransaction();
		try {
			Cursor cursor = null;
			boolean distinct = true;
			String[] columns = null;
			String selection = null;
	        String[] selectionArgs = null;
	        String groupBy = null;
	        String having = null;
	        String limit = null;
	        
	        if (!orderBy.equals("")) {
	        	orderBy += order ? " asc" : " desc";//asc/desc:ÉýÐò/½µÐò
	        }
	        cursor = sqliteDb.query(distinct, tableName, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
	        while (cursor.moveToNext())
	        {
	            Map<String, String> map = new HashMap<String, String>();
	            for (int i = 0; i < cursor.getColumnCount(); i++)
	            {
	                String name = cursor.getColumnName(i);
	                if (cursor.getType(i) == Cursor.FIELD_TYPE_STRING)
	                {
	                	map.put(name, cursor.getString(i));
	                }
	                else if (cursor.getType(i) == Cursor.FIELD_TYPE_INTEGER)
	                {
	                	map.put(name, cursor.getInt(i) + "");
	                }
	                Log.v(TAG, "query() - map.get("+name+"):"+map.get(name));
	            }
	            if (map.size() > 0)
	            	result.add(map);
	            Log.v(TAG, "query() - map.size():"+map.size());
	        }
	        cursor.close();
	        sqliteDb.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sqliteDb.endTransaction();
		}
        return result;
	}
	
	public boolean getDataBaseReadOnly()
	{
		return dataBaseReadOnly;
	}
	
	public void closeDB()
	{
		sqliteDb.beginTransaction();
		try {
			sqliteDb.close();
			sqliteDb.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sqliteDb.endTransaction();
		}
	}
	
}
