package com.seven.textedit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.seven.textedit.database.DatabaseManager;
import com.seven.textedit.database.Tables;
import com.seven.textedit.database.Tables.TextContent;
import com.seven.textedit.filestream.FileStreamManager;
import com.seven.textedit.utils.StrUtil;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.util.Log;
import android.view.View;

public class TextManager {
	
	private static TextManager txManager = null;
	public final static String TAG = TextManager.class.getSimpleName();
	static final Object syncObject = new Object();
	private Context context = BaseApplication.getInstance();
	private int currentPos;
	private int prePos;
	private TextFragment tmCurFragment;
	private TextFragment tmPreFragment;
	private static Map<String, BaseTextFragment> txFragmentMap = new HashMap<String, BaseTextFragment>();
	private static List<String> txContentList = new ArrayList<String>();
//	private static SharedPreferences sp = null;
	private String SP_TEXT_CONTENT = "TextContent";
	private String SP_TEXT_NAME = "TextName";
	private String SP_TEXT_COUNT = "TextCount";
	private int textCount;
	private DatabaseManager dBManager;
	private FileStreamManager fileManager;
	private ArrayList<TextContent> textList = new ArrayList<TextContent>();
	
	public static TextManager getInstance()
	{
		if (txManager == null) {
			synchronized (syncObject) {
				if (txManager == null)
					txManager = new TextManager();
			}
		}
		return txManager;
	}
	
	private TextManager() {
//		this.context = context;
		textCount = 0;
		currentPos = -1;
		prePos = -1;
		tmCurFragment = null;
		tmPreFragment = null;
		Log.v(TAG, "TextManager()!!!");
	}
	
	public int textListInit()
	{
		textCount = 3;
		dBManager = DatabaseManager.getInstance(context);
		fileManager = FileStreamManager.getInstance(context);
		
		if (getTextListFromDB() != null) {
			return 0;
		} else {
			getTextListForTest();
		}
		
		return 0;
	}
	
	public ArrayList<TextContent> getTextList()
	{
		return textList;
	}
	
	public int getCurrentPos()
	{
		return currentPos;
	}
	
	public void setCurrentPos(int pos)
	{
		this.currentPos = pos;
	}
	
	public int getPrePos()
	{
		return prePos;
	}
	
	public void setPrePos(int prePos)
	{
		this.prePos = prePos;
	}
	
	/**
	 * begin text manager
	 */
	public int getTextCount()
	{
		return textCount;
	}
	
	public void setTextCount(int count)
	{
		textCount = count;
	}
	
	public String getTextPath(String name)
	{
		String time = StrUtil.getCurrentStrTime("yyyy-MM-dd-HH-mm-ss-SSS");
		String _name = name + "-" + time;
		String path = fileManager.getFilePath() + _name + ".txt";
		return path;
	}
	
	public String getTextName(TextContent textContent)
	{
		String textName = null;
//		Map<String,String> whereMap = new HashMap<String,String>();
//		whereMap.put(Tables.TextTable.mIndex, textContent.mIndex+"");
//		List<Map<String,String>> list = dBManager.queryData(textContent.mTableName, whereMap, null, true);
//		if (list != null && list.size() > 0) {
//			return textName = list.get(0).get(Tables.TextTable.mTextName);
//		}
		textName = textContent.mTextName;
		return textName;
	}
	
	public void setTextName(TextContent textContent, String name)
	{
		Map<String,String> whereMap = new HashMap<String,String>();
		Map<String, Object> map = new HashMap<String,Object>();
		map.put(Tables.TextTable.mTextName, name);
		whereMap.put(Tables.TextTable.mIndex, textContent.mIndex+"");
		synchronized (this) {
			dBManager.updateData(textContent.mTableName, map, whereMap);
//			SharedPreferences sp = context.getSharedPreferences(SP_TEXT_CONTENT+index, context.MODE_PRIVATE);
//			sp.edit().remove(index+"").commit();
		}
	}
	
	public int addText(int index, TextContent textContent)
	{
		synchronized (this) {
			Log.v("TAG", "addText() -- textContent.mTextContentUri£º"+textContent.mTextContentUri);
			getTextList().add(index, textContent);
			addTextContent(textContent, "");
			updateTextListIndex(0, txManager.getTextList().size());
			//need to add func to get the mid of the new textContent
			textContent.mId = getTextContentmId(textContent);
		}
		return 0;
	}
	
	public int deleteText(int index, TextContent textContent)
	{
		synchronized (this) {
			Log.v("TAG", "deleteText() -- textContent.mTextContentUri£º"+textContent.mTextContentUri);
			getTextList().remove(index);
			deleteTextContent(textContent);
			updateTextListIndex(currentPos-1, txManager.getTextList().size());
		}
		return 0;
	}
	
	public String getTextContent(int index, TextContent textContent)
	{
//		String textContentUri = null;
//		Map<String,String> whereMap = new HashMap<String,String>();
//		if (textContent == null)
//			return null;
//		whereMap.put(Tables.TextTable.mIndex, textContent.mIndex+"");
//		List<Map<String,String>> list = dBManager.queryData(textContent.mTableName, whereMap, "", true);
//		
//		if (list != null && list.size() > 0) {
//			textContentUri = list.get(0).get(Tables.TextTable.mTextContentUri);
//			Log.v("TAG", "TextManager->getTextContent():textContentUri:"+textContentUri);
//			return fileManager.readFile(textContentUri);
//		}
		String textContentUri = textContent.mTextContentUri;
		Log.v("TAG", "TextManager->getTextContent():textContentUri:"+textContentUri);
		return fileManager.readFile(textContentUri);
	}
	
	public int getTextContentmId(TextContent textContent)
	{
		int mId = 0;
		Log.v("TAG", "getTextContentmId()-> entry!");
		Map<String,String> whereMap = new HashMap<String,String>();
		if (textContent == null)
			return mId;
		whereMap.put(Tables.TextTable.mIndex, textContent.mIndex+"");
		List<Map<String,String>> list = dBManager.queryData(textContent.mTableName, whereMap, "", true);
		if (list != null && list.size() > 0) {
			mId = StrUtil.parseInt( list.get(0).get(Tables.TextTable.mId) );
			Log.v("TAG", "getTextContentmId()-> mId:"+mId);
		}
		
		return mId;
	}
	
	public int addTextContent(TextContent textContent, String content)
	{
		synchronized (this) {
			Log.v("TAG", "saveTextContent() -- textContent.mTextContentUri£º"+textContent.mTextContentUri);
			addTextToDB(textContent);
			fileManager.createFile(textContent.mTextContentUri);
			fileManager.writeFile(textContent.mTextContentUri, content);
		}
		return 0;
	}
	
	public int saveTextContent(TextContent textContent, String content)
	{
		synchronized (this) {
			Log.v("TAG", "saveTextContent() -- textContent.mTextContentUri£º"+textContent.mTextContentUri);
			Map<String, String> whereMap = new HashMap<String, String>();
			whereMap.put(Tables.TextTable.mIndex, textContent.mIndex+"");
			updateTextToDB(textContent, whereMap);
			fileManager.createFile(textContent.mTextContentUri);
			fileManager.writeFile(textContent.mTextContentUri, content);
		}
		
		return 0;
	}
	
	public int addTextToDB(TextContent textContent)
	{
		//dBManager.insertData(textContent.mTableName, textContent);
		dBManager.replaceData(textContent.mTableName, textContent);
		return 0;
	}
	
	public int deleteTextContent(TextContent textContent)
	{
		synchronized (this) {
			Log.v("TAG", "saveTextContent() -- textContent.mTextContentUri£º"+textContent.mTextContentUri);
			deleteTextFromDB(textContent);
			fileManager.deleteFile(textContent.mTextContentUri);
		}
		return 0;
	}
	
	public int deleteTextFromDB(TextContent textContent)
	{
		dBManager.deleteData(textContent.mTableName, textContent.mId);
		return 0;
	}
	
	public int updateTextToDB(TextContent textContent, Map<String, String> whereMap)
	{
		synchronized (this) {
			Log.v("TAG", "updateTextToDB() -- textContent.mId£º"+textContent.mId);
			Log.v("TAG", "updateTextToDB() -- textContent.mIndex£º"+textContent.mIndex);
			Log.v("TAG", "updateTextToDB() -- textContent.mNewIndex£º"+textContent.mNewIndex);
			Log.v("TAG", "updateTextToDB() -- textContent.mTextName£º"+textContent.mTextName);
			Log.v("TAG", "updateTextToDB() -- textContent.mTextContentUri£º"+textContent.mTextContentUri);
			
			dBManager.updateData(textContent.mTableName, textContent, whereMap); //mTextName mTextContentUri can not update?
		}
		
		return 0;
	}
	
	public void addTextNameList(int index, ArrayList<String> list, String name)
	{
		list.add(index, name);
	}
	
	public void getTextNameList(ArrayList<String> list)
	{
		ArrayList<TextContent> textList = txManager.getTextList();
		list.clear();
		Log.v(TAG, "getTextNameList() textList.size():"+textList.size());
		for (int i=0; i<textList.size(); i++) {
			list.add(i, textList.get(i).mTextName);
			Log.v(TAG, "getTextNameList() textList.size():"+textList.get(i).mTextName);
		}
	}
	
	public ArrayList<TextContent> updateTextListIndex(int pos1, int pos2)
	{
		ArrayList<TextContent> textList = txManager.getTextList();
		int minPos = pos1 > pos2 ? pos2 :pos1;
		int maxPos = pos1 > pos2 ? pos1 :pos2;
		minPos = minPos < 0 ? 0 : minPos;
		maxPos = textList.size() > maxPos ? textList.size() : maxPos;
		for (int i = minPos; i < maxPos; i++) {
			Map<String, String> whereMap = new HashMap<String, String>();
			whereMap.put(Tables.TextTable.mId, textList.get(i).mId+"");
			textList.get(i).mIndex = i;
			textList.get(i).mNewIndex = i;
			updateTextToDB(textList.get(i), whereMap);
			Log.v(TAG, "updateTextListIndex() textList.get("+i+").mIndex:"+textList.get(i).mIndex);
		}
		return textList;
	}
	
	public ArrayList<TextContent> getTextListFromDB()
	{
		List<Map<String,String>> list = dBManager.query(Tables.TextTable.mTableName, "", true);
		Log.v(TAG, "getTextListFromDB() - list.size():"+list.size());
		if (list.size() > 0) {
			getTextList().clear();
			
			for (Map<String,String> map : list) {
				TextContent textContent = new Tables().new TextContent(StrUtil.parseInt(map.get(Tables.TextTable.mId)),
						StrUtil.parseInt(map.get(Tables.TextTable.mIndex)),StrUtil.parseInt(map.get(Tables.TextTable.mNewIndex)),
						map.get(Tables.TextTable.mTextName),map.get(Tables.TextTable.mTextContentUri),"","",false);
				
				getTextList().add(textContent);
			}
			textCount = list.size();
			
//			Collections.sort(txManager.getTextList(), new SortBymIndex());
			Collections.sort(getTextList(),new Comparator<TextContent>() {
				@Override
				public int compare(TextContent lhs, TextContent rhs) {
					// TODO Auto-generated method stub
					TextContent l = (TextContent)lhs;
					TextContent r = (TextContent)rhs;
					return l.mIndex - r.mIndex;
				}
				
			});
			
			for (TextContent textContent : txManager.getTextList()) {
				Log.v(TAG, "getTextListFromDB() - textContent.mTableName:"+textContent.mTableName);
				Log.v(TAG, "getTextListFromDB() - textContent.mId:"+textContent.mId);
				Log.v(TAG, "getTextListFromDB() - textContent.mIndex:"+textContent.mIndex);
				Log.v(TAG, "getTextListFromDB() - textContent.mNewIndex:"+textContent.mNewIndex);
				Log.v(TAG, "getTextListFromDB() - textContent.mTextName:"+textContent.mTextName);
				Log.v(TAG, "getTextListFromDB() - textContent.mTextContentUri:"+textContent.mTextContentUri);
			}
			return getTextList();
		}
		return null;
	}
	
	class SortBymIndex implements Comparator
	{
		@Override
		public int compare(Object lhs, Object rhs) {
			// TODO Auto-generated method stub
			TextContent l = (TextContent)lhs;
			TextContent r = (TextContent)rhs;
			Log.v(TAG, "SortBymIndex() - l.mIndex:"+l.mIndex);
			Log.v(TAG, "SortBymIndex() - r.mIndex:"+r.mIndex);
			return l.mIndex - r.mIndex;
		}
		
	}
	
	public int getTextListForTest()
	{
		for(int i=0; i<textCount; i++) {
			String name = "Text" + i;
			String path = this.getTextPath(name);
			Log.v(TAG, "getTextListForTest() - path:"+path);
			TextContent textContent = new Tables().new TextContent(i, i, name, path, "", "", false);
			addText(i, textContent);
			Log.v(TAG, "getTextListForTest() - textContent.mTableName:"+textContent.mTableName);
			Log.v(TAG, "getTextListForTest() - textContent.mId:"+textContent.mId);
			Log.v(TAG, "getTextListForTest() - textContent.mIndex:"+textContent.mIndex);
			Log.v(TAG, "getTextListForTest() - textContent.mNewIndex:"+textContent.mNewIndex);
			Log.v(TAG, "getTextListForTest() - textContent.mTextName:"+textContent.mTextName);
			Log.v(TAG, "getTextListForTest() - textContent.mTextContentUri:"+textContent.mTextContentUri);
		}
		
		return 0;
	} 
	
	/**
	 * begin textFragment manager
	 */
	public BaseTextFragment getTextFragment(String key)
	{
		return txFragmentMap.get(key);
	}
	
	public void addTextFragment(String key, BaseTextFragment btf)
	{
		txFragmentMap.put(key, btf);
	}
	
	public void removeTextFragment(String key)
	{
		txFragmentMap.remove(key);
	}
	
	public int showTextFragment(Activity activity, int index)
	{
		Log.v("TAG", "showTextFragment()-->entry!  index£º"+index);
		FragmentManager fragmentManager = activity.getFragmentManager();
		View v = activity.getWindow().getDecorView();
		setPrePos(getCurrentPos());
		setCurrentPos(index);
		fragmentManager.beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		//just one text fragment:0
		boolean oneTextFragment = true;
		if (oneTextFragment) {
			if (tmCurFragment == null) {
				tmCurFragment = TextFragment.newInstance(index);
				addTextFragment(0+"", tmCurFragment);
			}
			if (getPrePos() != -1) {
				tmPreFragment = (TextFragment)getTextFragment(0+"");
				hideTextFragment(activity, 0);
			}
			
		} else {
			tmCurFragment = (TextFragment)getTextFragment(index+"");
			Log.v("TAG", "showTextFragment():getTextFragment:"+tmCurFragment);
			
			if (tmCurFragment == null) {
				tmCurFragment = TextFragment.newInstance(index + 1);
				addTextFragment(index+"", tmCurFragment);
			}
			
			if (getPrePos() != -1) {
				tmPreFragment = (TextFragment)getTextFragment(getPrePos()+"");
				hideTextFragment(activity, getPrePos());
			}
		}
		
		if (tmCurFragment.isAdded()) {
			Log.v("TAG", "showTextFragment():tmFragment.isAdded():"+tmCurFragment.isAdded());
			fragmentManager.beginTransaction().show(tmCurFragment).commit();
			fragmentManager.executePendingTransactions(); //execute immediately
		} else {
//			fragmentManager.beginTransaction().addToBackStack(null);
			fragmentManager.beginTransaction().add(R.id.container, tmCurFragment).commit();
		}
		
		return 0;
	}
	
	public int hideTextFragment(Activity activity, int index)
	{
		Log.v("TAG", "hideTextFragment():index:"+index);
		FragmentManager fragmentManager = activity.getFragmentManager();
		TextFragment tmFragment = (TextFragment)getTextFragment(index+"");
		Log.v("TAG", "hideTextFragment():tmFragment:"+tmFragment);
		if (tmFragment != null)
			fragmentManager.beginTransaction().hide(tmFragment).commit();
//		hideContainer(v, tmPreFragment.getContainerId());
		return 0;
	}
	
	public int hideContainer(View v, int container)
	{
		if (v == null)
			return -1;
		Log.v("TAG", "hideFragment():container:"+container);
		View view = v.findViewById(container);
		if (view != null) {
			Log.v("TAG", "hideFragment():View.GONE:"+View.GONE);
			view.setVisibility(View.GONE);
		}
		return 0;
	}
}











