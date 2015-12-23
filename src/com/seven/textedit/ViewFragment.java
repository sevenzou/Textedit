package com.seven.textedit;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ViewFragment extends BaseTextFragment {

	private static final String TAG = ViewFragment.class.getSimpleName();
	static final Object syncObject = new Object();
	private static ViewFragment fragment = null;
	private TextManager txManager = TextManager.getInstance();
	private int curPos;
	private int prePos;
	private int textCount;
	
	public static ViewFragment newInstance() {
//		if (fragment == null) {
//			synchronized (syncObject) {
//				if (fragment == null)
					fragment = new ViewFragment();
//			}
//		}
		return fragment;
	}

	public ViewFragment() {
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Log.v(TAG, "ViewFragment->onCreateView() - entry");
		super.onCreateView(inflater, container, savedInstanceState);
		View rootView = inflater.inflate(R.layout.fragment_view, container, false);
		setNameView((TextView) rootView.findViewById(R.id.textViewName));
		setTextView((TextView) rootView.findViewById(R.id.textView));
		if (txManager.getTextList().size() > 0) {
			this.curPos = txManager.getCurrentPos();
			Log.v(TAG, "ViewFragment->onCreateView():curPos:"+curPos);
			Log.v(TAG, "ViewFragment->onCreateView():txManager.getTextList().get(curPos):"+txManager.getTextList().get(curPos));
			setTextFromList(curPos);
		}
		
		return rootView;
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	@Override
	public void onHiddenChanged(boolean hidden) {
		// TODO Auto-generated method stub
		super.onHiddenChanged(hidden);
		Log.v(TAG, "TextFragment->onHiddenChanged():hidden:"+hidden);
		String text = "";
		this.curPos = txManager.getCurrentPos();
		this.prePos = txManager.getPrePos();
		this.textCount = txManager.getTextCount();
		Log.v(TAG, "ViewFragment->onHiddenChanged():curPos:"+curPos);
		Log.v(TAG, "ViewFragment->onHiddenChanged():prePos:"+prePos);
		Log.v(TAG, "ViewFragment->onHiddenChanged():textCount:"+textCount);
		if (curPos <= -1 || textCount == 0) {
			setText(text, text);
			return;
		} else if (curPos >= 0) {
			if (hidden == false) {
				setTextFromList(curPos);
			}
		}
		
		Log.v(TAG, "ViewFragment->onHiddenChanged():text:"+text);
	}

	public void setText(String name, String text)
	{
		getNameView().setText(name);
		getTextView().setText(text);
		getTextView().setMovementMethod(ScrollingMovementMethod.getInstance());
	}
	
	public void setTextFromList(int pos)
	{
		String textName = txManager.getTextName(txManager.getTextList().get(pos));
		String textContent = txManager.getTextContent(pos, txManager.getTextList().get(pos));
		Log.v(TAG, "ViewFragment->setText():textName:"+textName);
		Log.v(TAG, "ViewFragment->setText():textContent:"+textContent);
		Log.v(TAG, "ViewFragment->setText():text.length():"+textContent.length());
		setText(textName, textContent);
	}

}
