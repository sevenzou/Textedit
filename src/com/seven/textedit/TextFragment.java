package com.seven.textedit;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class TextFragment extends BaseTextFragment {
	
	/**
	 * The fragment argument representing the section number for this
	 * fragment.
	 */
	private static final String ARG_SECTION_NUMBER = "section_number";
	static final Object syncObject = new Object();
	private static TextFragment fragment = null;
	private TextManager txManager = TextManager.getInstance();
	private int curPos;
	private int prePos;
	private int textCount;
	/**
	 * Returns a new instance of this fragment for the given section number.
	 */
	public static TextFragment newInstance(int sectionNumber) {
//		if (fragment == null) {
//			synchronized (syncObject) {
//				if (fragment == null)
					fragment = new TextFragment();
//			}
//		}
//		Bundle args = new Bundle();
//		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
//		fragment.setArguments(args);
		return fragment;
	}

	public TextFragment() {
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		Log.v("TAG", "TextFragment->onAttach():activity:"+activity);
//		((MainActivity) activity).onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Log.v("TAG", "TextFragment->onAttach():savedInstanceState:"+savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_main, container, false);
//		editText = (EditText) rootView.findViewById(R.id.section_label);
		Log.v("TAG", "TextFragment->onCreateView() - entry");
		setEditTextView((EditText) rootView.findViewById(R.id.section_label));
		getEditTextView().setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				// TODO Auto-generated method stub
				Log.v("TAG", "onEditorAction() - actionId:"+actionId);
				switch (actionId) {
				case EditorInfo.IME_ACTION_UNSPECIFIED:
					Log.v("TAG", "onEditorAction() - IME_ACTION_UNSPECIFIED");
//					Editable et = getEditTextView().getText();
//					getEditTextView().setSelection(3);
//					Selection.setSelection(et, et.length());
//					Selection.moveDown(et, v.getLayout());
					break;
				case EditorInfo.IME_ACTION_DONE: 
					break;
				case EditorInfo.IME_ACTION_NEXT:
					break;
				default:
					break;
				}
				String text = v.getText().toString();
				Log.v("TAG", "onEditorAction() - text:"+text);
				return false;
			}
			
		});
		this.curPos = txManager.getCurrentPos();
		Log.v("TAG", "TextFragment->onCreateView():curPos:"+curPos);
		if (txManager.getTextList().size() > 0) {
			Log.v("TAG", "TextFragment->onCreateView():txManager.getTextList().get(curPos):"+txManager.getTextList().get(curPos));
			String text = txManager.getTextContent(curPos, txManager.getTextList().get(curPos));
			Log.v("TAG", "TextFragment->onCreateView():text:"+text);
			getEditTextView().setText(text);
			if (text.length() > 0)
				getEditTextView().setSelection(text.length()-1); //设置光标位置在文件尾
		}
		
		return rootView;
	}
	
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Log.v("TAG", "TextFragment->onStart()");
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.v("TAG", "TextFragment->onResume()");
//		this.curPos = txManager.getCurrentPos();
//		String text = txManager.getTextContent(curPos);
//		Log.v("TAG", "TextFragment->onResume():text:"+text);
//		getEditTextView().setText(text);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		Log.v("TAG", "TextFragment->onSaveInstanceState():outState:"+outState);
	}
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.v("TAG", "TextFragment->onPause()");
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.v("TAG", "TextFragment->onDestroy()");
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		// TODO Auto-generated method stub
		super.onHiddenChanged(hidden);
		Log.v("TAG", "TextFragment->onHiddenChanged():hidden:"+hidden);
		String text = "";
		this.curPos = txManager.getCurrentPos();
		this.prePos = txManager.getPrePos();
		this.textCount = txManager.getTextCount();
		Log.v("TAG", "TextFragment->onHiddenChanged():curPos:"+curPos);
		Log.v("TAG", "TextFragment->onHiddenChanged():prePos:"+prePos);
		Log.v("TAG", "TextFragment->onHiddenChanged():textCount:"+textCount);
		if (curPos <= -1 || textCount == 0) {
			getEditTextView().setText(text);
			return;
		} else if (curPos >= 0) {
			if (hidden == false) {
				text = txManager.getTextContent(curPos, txManager.getTextList().get(curPos));
				getEditTextView().setText(text);
				Log.v("TAG", "TextFragment->onHiddenChanged():text.length():"+text.length());
				if (text.length() > 0)
					getEditTextView().setSelection(text.length()-1); //设置光标位置在文件尾
			}
		}
		
		Log.v("TAG", "TextFragment->onHiddenChanged():text:"+text);
	}

}
