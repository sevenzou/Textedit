package com.seven.textedit;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

public class BaseTextFragment extends Fragment {

	private Activity activity = null;
	private int containerId = 0;
	private TextView viewName;
	private TextView viewText;
	private EditText editName;
	private EditText editText;
	
	public void setContainerId(int containerId)
	{
		this.containerId = containerId;
	}
	
	public int getContainerId()
	{
		return this.containerId;
	}
	
	public void setNameView(TextView name)
	{
		this.viewName = name;
	}
	
	public void setTextView(TextView text)
	{
		this.viewText = text;
	}
	
	public void setEditNameView(EditText et)
	{
		this.editName = et;
	}
	
	public void setEditTextView(EditText et)
	{
		this.editText = et;
	}
	
	public TextView getNameView()
	{
		return this.viewName;
	}
	
	public TextView getTextView()
	{
		return this.viewText;
	}
	
	public EditText getEditNameView()
	{
		return this.editName;
	}
	
	public EditText getEditTextView()
	{
		return this.editText;
	}
	
	public void saveEditText()
	{
		String text = getEditText();
		getEditTextView().setText(text);
	}
	
	public String getEditText()
	{
		EditText editText = (EditText)getEditTextView();
		return editText.getText().toString();
	}
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		this.activity = activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return super.onCreateView(inflater, container, savedInstanceState);
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

}
