package com.seven.textedit;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import com.seven.textedit.database.Tables;
import com.seven.textedit.database.Tables.TextContent;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

/**
 * Fragment used for managing interactions for and presentation of a navigation
 * drawer. See the <a href=
 * "https://developer.android.com/design/patterns/navigation-drawer.html#Interaction">
 * design guidelines</a> for a complete explanation of the behaviors implemented
 * here.
 */
public class NavigationDrawerFragment extends Fragment {
	private static final String TAG = NavigationDrawerFragment.class.getSimpleName();

	/**
	 * Remember the position of the selected item.
	 */
	private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";

	/**
	 * Per the design guidelines, you should show the drawer on launch until the
	 * user manually expands it. This shared preference tracks this.
	 */
	private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";

	/**
	 * A pointer to the current callbacks instance (the Activity).
	 */
	private NavigationDrawerCallbacks mCallbacks;

	/**
	 * Helper component that ties the action bar to the navigation drawer.
	 */
	private ActionBarDrawerToggle mDrawerToggle;

	private DrawerLayout mDrawerLayout;
	private ListView mDrawerListView;
	private View mFragmentContainerView;

	private int mCurrentSelectedPosition = 0;
	private boolean mFromSavedInstanceState;
	private boolean mUserLearnedDrawer;
	private ArrayAdapter<String> mAdapter;
	private ArrayList<String> mTextNameList = new ArrayList<String>();
	private String[] mTmpNameList;
	private AlertDialog.Builder addDialogBuilder;
	private DialogInterface.OnClickListener dialogListener;
	private EditText addEditText;
	private TextManager txManager = TextManager.getInstance();
	
	public NavigationDrawerFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Read in the flag indicating whether or not the user has demonstrated
		// awareness of the
		// drawer. See PREF_USER_LEARNED_DRAWER for details.
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
		mUserLearnedDrawer = sp.getBoolean(PREF_USER_LEARNED_DRAWER, false);

		if (savedInstanceState != null) {
			mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
			mFromSavedInstanceState = true;
		}

		// Select either the default item (0) or the last selected item.
		selectItem(mCurrentSelectedPosition);
		
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// Indicate that this fragment would like to influence the set of
		// actions in the action bar.
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mDrawerListView = (ListView) inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
		mDrawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Log.v("TAG", "onItemClick():position"+position);
				selectItem(position);
			}
		});
		
		mAdapter = new ArrayAdapter<String>(getActionBar().getThemedContext(),
				android.R.layout.simple_list_item_activated_1, android.R.id.text1,
				mTextNameList);
		
//		ArrayList<TextContent> textList = txManager.getTextList();
//		Log.v(TAG, "onCreateView() textList.size():"+textList.size());
//		for (int i=0; i<textList.size(); i++) {
//			mTextNameList.add(i, textList.get(i).mTextName);
//			Log.v(TAG, "onCreateView() textList.size():"+textList.get(i).mTextName);
//		}
		txManager.getTextNameList(mTextNameList);
		mDrawerListView.setAdapter(mAdapter);
		mDrawerListView.setItemChecked(mCurrentSelectedPosition, true);
		return mDrawerListView;
	}

	public boolean isDrawerOpen() {
		return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mFragmentContainerView);
	}

	/**
	 * Users of this fragment must call this method to set up the navigation
	 * drawer interactions.
	 *
	 * @param fragmentId
	 *            The android:id of this fragment in its activity's layout.
	 * @param drawerLayout
	 *            The DrawerLayout containing this fragment's UI.
	 */
	public void setUp(int fragmentId, DrawerLayout drawerLayout) {
		mFragmentContainerView = getActivity().findViewById(fragmentId);
		mDrawerLayout = drawerLayout;

		// set a custom shadow that overlays the main content when the drawer
		// opens
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
		// set up the drawer's list view with items and click listener

		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayShowHomeEnabled(false);

		// ActionBarDrawerToggle ties together the the proper interactions
		// between the navigation drawer and the action bar app icon.
		mDrawerToggle = new ActionBarDrawerToggle(
				getActivity(), /* host Activity */
				mDrawerLayout, /* DrawerLayout object */
				R.drawable.ic_drawer, /*
										 * nav drawer image to replace 'Up' caret
										 */
				R.string.navigation_drawer_open, /*
													 * "open drawer" description
													 * for accessibility
													 */
				R.string.navigation_drawer_close /*
													 * "close drawer" description
													 * for accessibility
													 */
		) {
			@Override
			public void onDrawerClosed(View drawerView) {
				super.onDrawerClosed(drawerView);
				if (!isAdded()) {
					return;
				}

				getActivity().invalidateOptionsMenu(); // calls
														// onPrepareOptionsMenu()
			}

			@Override
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				if (!isAdded()) {
					return;
				}

				if (!mUserLearnedDrawer) {
					// The user manually opened the drawer; store this flag to
					// prevent auto-showing
					// the navigation drawer automatically in the future.
					mUserLearnedDrawer = true;
					SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
					sp.edit().putBoolean(PREF_USER_LEARNED_DRAWER, true).apply();
				}

				getActivity().invalidateOptionsMenu(); // calls
														// onPrepareOptionsMenu()
			}
		};

		// If the user hasn't 'learned' about the drawer, open it to introduce
		// them to the drawer,
		// per the navigation drawer design guidelines.
		if (!mUserLearnedDrawer && !mFromSavedInstanceState) {
			mDrawerLayout.openDrawer(mFragmentContainerView);
		}

		// Defer code dependent on restoration of previous instance state.
		mDrawerLayout.post(new Runnable() {
			@Override
			public void run() {
				mDrawerToggle.syncState();
			}
		});

		mDrawerLayout.setDrawerListener(mDrawerToggle);
	}

	private void selectItem(int position) {
		if (position < 0)
			position = 0;
		mCurrentSelectedPosition = position;
		if (mDrawerListView != null) {
			mDrawerListView.setItemChecked(position, true);
		}
		if (mDrawerLayout != null) {
			mDrawerLayout.closeDrawer(mFragmentContainerView);
		}
		if (mCallbacks != null) {
			mCallbacks.onNavigationDrawerItemSelected(position);
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mCallbacks = (NavigationDrawerCallbacks) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mCallbacks = null;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Forward the new configuration the drawer toggle component.
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// If the drawer is open, show the global app actions in the action bar.
		// See also
		// showGlobalContextActionBar, which controls the top-left area of the
		// action bar.
		Log.v(TAG, "onCreateOptionsMenu():");
		if (mDrawerLayout != null && isDrawerOpen()) {
			inflater.inflate(R.menu.global, menu);
			showGlobalContextActionBar();
		}
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		Log.v(TAG, "item.getItemId():"+item.getItemId());
		
		if (item.getItemId() == R.id.action_add) {
			//add here
			addTextDialog();
			return true;
		}
		if (item.getItemId() == R.id.action_save) {
			Log.v("TAG", "onOptionsItemSelected():mCurrentSelectedPosition"+mCurrentSelectedPosition);
			save(mCurrentSelectedPosition);
			if (mCallbacks != null) {
				mCallbacks.onNavigationDrawerItemSaveText(mCurrentSelectedPosition);
			}
			
			return true;
		}
		if (item.getItemId() == R.id.action_remove) {
			//add here
			removeTextDialog();
			return true;
		}
		if (item.getItemId() == R.id.action_rename) {
			//add here
			renameTextDialog();
			return true;
		}
		if (item.getItemId() == R.id.action_clear) {
			//add here
			clearTextDialog();
			return true;
		}
		if (item.getItemId() == R.id.action_capture) {
			//add here
			captureScreen();
			return true;
		}
		if (item.getItemId() == R.id.action_share) {
			
			if (mCallbacks != null) {
				mCallbacks.onNavigationDrawerItemShareText(mCurrentSelectedPosition);
			}
			return true;
		}
		if (item.getItemId() == R.id.action_settings) {
			
			if (mCallbacks != null) {
				mCallbacks.onNavigationDrawerItemSettingText(mCurrentSelectedPosition);
			}
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	/**
	 * Per the navigation drawer design guidelines, updates the action bar to
	 * show the global app 'context', rather than just what's in the current
	 * screen.
	 */
	private void showGlobalContextActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setTitle(R.string.action_Catalog);
	}

	private ActionBar getActionBar() {
		return getActivity().getActionBar();
	}

	/**
	 * Callbacks interface that all activities using this fragment must
	 * implement.
	 */
	public static interface NavigationDrawerCallbacks {
		/**
		 * Called when an item in the navigation drawer is selected.
		 */
		void onNavigationDrawerItemSelected(int position);
		void onNavigationDrawerItemAddText(int position);
		void onNavigationDrawerItemSaveText(int position);
		void onNavigationDrawerItemRemoveText(int position);
		void onNavigationDrawerItemRenameText(int position);
		void onNavigationDrawerItemClearText(int position);
		void onNavigationDrawerItemShareText(int position);
		void onNavigationDrawerItemSettingText(int position);
	}
	
	/**
	 * addTextDialog()
	 */
	public void addTextDialog()
	{
		//add add_text_dialog
		dialogListener = new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				Log.v(TAG, "addTextDialog()->OnClickListener()->which:"+which);
				switch(which) {
				case Dialog.BUTTON_POSITIVE:
					Log.v(TAG, "dialogListener->addEditText name:"+addEditText.getText().toString());
					addToFirstPos(addEditText.getText().toString());
					selectItem(mCurrentSelectedPosition);
					if (mCallbacks != null) {
						mCallbacks.onNavigationDrawerItemAddText(mCurrentSelectedPosition);
					}
					break;
				case Dialog.BUTTON_NEGATIVE:
					break;
				case Dialog.BUTTON_NEUTRAL:
					break;
				default:
					break;
				}
			}
		};
		addEditText = new EditText(getActivity());
		addEditText.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				// TODO Auto-generated method stub
				return false;
			}
		});
		
		addDialogBuilder = new AlertDialog.Builder(getActivity());
		addDialogBuilder.setTitle(R.string.dialog_input_name);
		addDialogBuilder.setIcon(android.R.drawable.ic_dialog_info);
		addDialogBuilder.setView(addEditText);
		addDialogBuilder.setPositiveButton(R.string.dialog_add, dialogListener); //添加
		addDialogBuilder.setNegativeButton(R.string.dialog_cancel, dialogListener); //取消
//		addDialogBuilder.setNeutralButton(R.string.dialog_ignore, dialogListener);  //忽略
		addDialogBuilder.create().show();
	}
	
	/**
	 * removeTextDialog()
	 */
	public void removeTextDialog()
	{
		dialogListener = new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				Log.v(TAG, "removeTextDialog()->OnClickListener()->which:"+which);
				switch(which) {
				case Dialog.BUTTON_POSITIVE:
					delete(mCurrentSelectedPosition);
					selectItem(mCurrentSelectedPosition-1);
					if (mCallbacks != null) {
						mCallbacks.onNavigationDrawerItemRemoveText(mCurrentSelectedPosition);
					}
					break;
				case Dialog.BUTTON_NEGATIVE:
					break;
				default:
					break;
				}
			}
		};
		
		addDialogBuilder = new AlertDialog.Builder(getActivity());
		addDialogBuilder.setTitle(R.string.dialog_delete_text);
		addDialogBuilder.setIcon(R.drawable.prompt_img);
		addDialogBuilder.setMessage(R.string.dialog_if_delete);
		addDialogBuilder.setPositiveButton(R.string.dialog_delete, dialogListener); //删除
		addDialogBuilder.setNegativeButton(R.string.dialog_cancel, dialogListener); //取消
		addDialogBuilder.create().show();
	}
	
	/**
	 * renameTextDialog()
	 */
	public void renameTextDialog()
	{
		dialogListener = new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				Log.v(TAG, "renameTextDialog()->OnClickListener()->which:"+which);
				switch(which) {
				case Dialog.BUTTON_POSITIVE:
					rename(mCurrentSelectedPosition, addEditText.getText().toString());
					selectItem(mCurrentSelectedPosition);
					if (mCallbacks != null) {
						mCallbacks.onNavigationDrawerItemRenameText(mCurrentSelectedPosition);
					}
					break;
				case Dialog.BUTTON_NEGATIVE:
					break;
				default:
					break;
				}
			}
		};
		addEditText = new EditText(getActivity());
		addEditText.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				// TODO Auto-generated method stub
				return false;
			}
		});
		
		addEditText.setText(txManager.getTextName(txManager.getTextList().get(mCurrentSelectedPosition)));
		
		addDialogBuilder = new AlertDialog.Builder(getActivity());
		addDialogBuilder.setTitle(R.string.dialog_rename_text);
		addDialogBuilder.setIcon(android.R.drawable.ic_dialog_info);
		addDialogBuilder.setView(addEditText);
		addDialogBuilder.setPositiveButton(R.string.dialog_modify, dialogListener); //修改
		addDialogBuilder.setNegativeButton(R.string.dialog_cancel, dialogListener); //取消
		addDialogBuilder.create().show();
	}
	
	/**
	 * clearTextDialog()
	 */
	public void clearTextDialog()
	{
		dialogListener = new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				Log.v(TAG, "clearTextDialog()->OnClickListener()->which:"+which);
				switch(which) {
				case Dialog.BUTTON_POSITIVE:
					clear(mCurrentSelectedPosition);
					selectItem(mCurrentSelectedPosition);
					if (mCallbacks != null) {
						mCallbacks.onNavigationDrawerItemClearText(mCurrentSelectedPosition);
					}
					break;
				case Dialog.BUTTON_NEGATIVE:
					break;
				default:
					break;
				}
			}
		};
		
		addDialogBuilder = new AlertDialog.Builder(getActivity());
		addDialogBuilder.setTitle(R.string.dialog_clear_text);
		addDialogBuilder.setIcon(R.drawable.prompt_img);
		addDialogBuilder.setMessage(R.string.dialog_if_clear);
		addDialogBuilder.setPositiveButton(R.string.dialog_clear, dialogListener); //清除
		addDialogBuilder.setNegativeButton(R.string.dialog_cancel, dialogListener); //取消
		addDialogBuilder.create().show();
	}
	
	/**
	 * addToFirstPos(String name)
	 */
	public void addToFirstPos(String name)
	{
		String path = txManager.getTextPath(name);
		TextContent textContent = new Tables().new TextContent(0, 0, 
				name,path, "", "", false);
		Log.v(TAG, "NavigationDrawerFragment-> addToLast() textContent.mId:"+textContent.mId);
		Log.v(TAG, "NavigationDrawerFragment-> addToLast() textContent.mIndex:"+textContent.mIndex);
		Log.v(TAG, "NavigationDrawerFragment-> addToLast() textContent.mNewIndex:"+textContent.mNewIndex);
		Log.v(TAG, "NavigationDrawerFragment-> addToLast() textContent.mTableName:"+textContent.mTableName);
		Log.v(TAG, "NavigationDrawerFragment-> addToLast() textContent.mTextContentUri:"+textContent.mTextContentUri);
		txManager.addText(0, textContent);
		txManager.setTextCount(txManager.getTextCount()+1);
//		txManager.addTextNameList(0, mTextNameList, name);
		mTextNameList.add(0, name);
		mAdapter.notifyDataSetChanged();
		mCurrentSelectedPosition = 0;
	}
	/**
	 * addToLast(String name)
	 */
	public void addToLast(String name)
	{
		String path = txManager.getTextPath(name);
		TextContent textContent = new Tables().new TextContent(mTextNameList.size(), mTextNameList.size(), 
																name,path, "", "", false);
		txManager.addText(mTextNameList.size(), textContent);
		txManager.setTextCount(txManager.getTextCount()+1);
		mTextNameList.add(name);
		mAdapter.notifyDataSetChanged();
	}
	
	/**
	 * save(int position)
	 */
	public void save(int position)
	{
		TextFragment mCurrentFragment = (TextFragment)txManager.getTextFragment(0+"");
		if (mCurrentFragment != null) {
			txManager.saveTextContent(txManager.getTextList().get(position), (String)mCurrentFragment.getEditTextView().getText().toString());
			setToFirstPos(mCurrentSelectedPosition);
		}
		selectItem(mCurrentSelectedPosition);
	}
	
	/**
	 * delete(int position)
	 */
	public void delete(int position)
	{
		txManager.deleteText(position, txManager.getTextList().get(position));
		txManager.setTextCount(txManager.getTextCount()-1);
		if (position >= 0 && position <= mTextNameList.size())
			mTextNameList.remove(position);
		mAdapter.notifyDataSetChanged();
	}
	
	/**
	 * rename(int position, String name)
	 */
	public void rename(int position, String name)
	{
		txManager.setTextName(txManager.getTextList().get(position), name);
		mTextNameList.set(position, name);
		setToFirstPos(mCurrentSelectedPosition);
	}
	
	/**
	 * clear(int position)
	 */
	public void clear(int position)
	{
		TextFragment mCurrentFragment = (TextFragment)txManager.getTextFragment(0+"");
		if (mCurrentFragment != null) {
			mCurrentFragment.getEditTextView().setText("");
			txManager.saveTextContent(txManager.getTextList().get(position), (String)mCurrentFragment.getEditTextView().getText().toString());
			setToFirstPos(mCurrentSelectedPosition);
		}
	}
	
	/**
	 * setToFirstPos(int pos)
	 */
	public void setToFirstPos(int pos)
	{
		TextContent tmp = txManager.getTextList().get(pos);
		txManager.getTextList().remove(pos);
		txManager.getTextList().add(0, tmp);
		txManager.updateTextListIndex(0, pos);
		
		String str = mTextNameList.get(pos);
		mTextNameList.remove(pos);
		mTextNameList.add(0, str);
		mAdapter.notifyDataSetChanged();
		
		mCurrentSelectedPosition = 0;
	}
	
	/**
	 * exchangePos()
	 */
	public void exchangePos(int pos1, int pos2)
	{
		TextContent tmp = txManager.getTextList().get(pos1);
		TextContent text = txManager.getTextList().get(pos2);
		txManager.getTextList().set(pos1, text);
		txManager.getTextList().set(pos2, tmp);
		txManager.updateTextListIndex(pos1, pos2);
	}
	
	/*
	 * captureScreen()
	 * */
	public void captureScreen()
	{
		View v = getActivity().getWindow().getDecorView();
		v.setDrawingCacheEnabled(true);
		v.buildDrawingCache();
		
		Bitmap srcBitmap = v.getDrawingCache();
		Rect frame = new Rect();
		
		getActivity().getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
		
		int statusHeight = frame.top;
		Point outSize = new Point();
		
		getActivity().getWindowManager().getDefaultDisplay().getSize(outSize);
		int width = outSize.x;
		int height = outSize.y;
		
		Bitmap desBitmap = Bitmap.createBitmap(srcBitmap, 0, statusHeight, width, height-statusHeight);
		
		v.destroyDrawingCache();
		
		FileOutputStream fos = null;
		    
        try {
            File file = File.createTempFile("capture", ".jpg",
                    new File("/sdcard"));

            fos = new FileOutputStream(file);

            if (null != fos) {
                // 第五步 ：将屏幕图像保存到sd卡的根目录
            	desBitmap.compress(Bitmap.CompressFormat.PNG, 90, fos);
                fos.flush();
                Toast.makeText(getActivity(), "保存成功" + file.getName(), Toast.LENGTH_SHORT).show();
            } else {
            	Toast.makeText(getActivity(), "失败", Toast.LENGTH_SHORT).show();
            }
				
            fos.close();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

	}
}
