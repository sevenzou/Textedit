package com.seven.textedit.database;

public class Tables {
	public class TextTable {
		public final static String mTableName = "textTable";
		public final static String mId = "_id";
		public final static String mIndex = "_index";
		public final static String mNewIndex = "_newIndex";
		public final static String mTextName = "textName";
		public final static String mTextContentUri = "TextContentUri";
		public final static String mAudioUri = "audioUri";
		public final static String mVideoUri = "videoUri";
		public final static String mShared = "shared";
	}
	
	public class TextContent {
		public String mTableName = "textTable";
		public int mId;
		public int mIndex;
		public int mNewIndex;
		public String mTextName;
		public String mTextContentUri;
		public String mAudioUri;
		public String mVideoUri;
		public boolean mShared;
		
		public TextContent(int id, int index, int newIndex, String textName, String textContentUri, String audioUri,
							String videoUri, boolean shared)
		{
			mId = id;
			mIndex = index;
			mNewIndex = newIndex;
			mTextName = textName;
			mTextContentUri = textContentUri;
			mAudioUri = audioUri;
			mVideoUri = videoUri;
			mShared = shared;
		}
		
		public TextContent(int index, int newIndex, String textName, String textContentUri, String audioUri,
							String videoUri, boolean shared)
		{
			this(0, index, newIndex, textName, textContentUri, audioUri, videoUri, shared);
		}
	}
}
