package com.seven.textedit;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity implements NavigationDrawerFragment.NavigationDrawerCallbacks {

	/**
	 * Fragment managing the behaviors, interactions and presentation of the
	 * navigation drawer.
	 */
	private NavigationDrawerFragment mNavigationDrawerFragment;

	/**
	 * Used to store the last screen title. For use in
	 * {@link #restoreActionBar()}.
	 */
	private CharSequence mTitle;
	
	private TextManager txManager;
	private static final String TAG = MainActivity.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		txManager = TextManager.getInstance();
		txManager.textListInit();
		Log.v(TAG, "onCreate:txManager:"+txManager);
		
		setContentView(R.layout.activity_main);
		//getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
		mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		mTitle = getTitle();
		Button edit = (Button)findViewById(R.id.edit);
		Button capture = (Button)findViewById(R.id.capture);
		Button share = (Button)findViewById(R.id.share);
		Button setting = (Button)findViewById(R.id.setting);
		
		edit.setOnClickListener(new ButtonOnClickListener());
		capture.setOnClickListener(new ButtonOnClickListener());
		share.setOnClickListener(new ButtonOnClickListener());
		setting.setOnClickListener(new ButtonOnClickListener());
		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));
	}
	
	public class ButtonOnClickListener implements View.OnClickListener
	{
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.edit:
				Button edit = (Button)findViewById(R.id.edit);
				if (TextManager.getInstance() != null && TextManager.getInstance().getEditEnable() == false) {
					TextManager.getInstance().showTextFragment(MainActivity.this, TextManager.getInstance().getCurrentPos(),true);
					edit.setText(R.string.action_preview);
				} else {
					TextManager.getInstance().showTextFragment(MainActivity.this, TextManager.getInstance().getCurrentPos(),false);
					edit.setText(R.string.action_edit);
				}
				break;
			case R.id.capture:
				captureScreen(MainActivity.this);
				break;
			case R.id.share:
				Toast.makeText(MainActivity.this, "share", Toast.LENGTH_SHORT).show();
				break;
			case R.id.setting:
				Toast.makeText(MainActivity.this, "setting", Toast.LENGTH_SHORT).show();
				break;
			default :
				break;
			}
			
		}
		
	}

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		// update the main content by show fragments
		Log.v(TAG, "onNavigationDrawerItemSelected:position"+position);
		Log.v(TAG, "onNavigationDrawerItemSelected:txManager"+TextManager.getInstance());
		if (TextManager.getInstance() != null && position >= 0)
			TextManager.getInstance().showTextFragment(MainActivity.this, position, false);
	}

	public void onSectionAttached(int number) {
		switch (number) {
		case 1:
			mTitle = getString(R.string.title_section1);
			break;
		case 2:
			mTitle = getString(R.string.title_section2);
			break;
		case 3:
			mTitle = getString(R.string.title_section3);
			break;
		}
	}

	public void restoreActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!mNavigationDrawerFragment.isDrawerOpen()) {
			// Only show items in the action bar relevant to this screen
			// if the drawer is not showing. Otherwise, let the drawer
			// decide what to show in the action bar.
			getMenuInflater().inflate(R.menu.main, menu);
			restoreActionBar();
			
			return true;
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (item.getItemId() == android.R.id.home) {
			Toast.makeText(this, "android.R.id.home", Toast.LENGTH_SHORT).show();
//			return true;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onNavigationDrawerItemAddText(int position) {
		// TODO Auto-generated method stub
		Toast.makeText(this, "action_add", Toast.LENGTH_SHORT).show();
	}
	
	@Override
	public void onNavigationDrawerItemSaveText(int position) {
		// TODO Auto-generated method stub
		Toast.makeText(this, "save", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onNavigationDrawerItemRemoveText(int position) {
		// TODO Auto-generated method stub
		Toast.makeText(this, "remove", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onNavigationDrawerItemRenameText(int position) {
		// TODO Auto-generated method stub
		Toast.makeText(this, "rename", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onNavigationDrawerItemClearText(int position) {
		// TODO Auto-generated method stub
		Toast.makeText(this, "clear", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onNavigationDrawerItemShareText(int position) {
		// TODO Auto-generated method stub
		Toast.makeText(this, "share", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onNavigationDrawerItemSettingText(int position) {
		// TODO Auto-generated method stub
		Toast.makeText(this, "settings", Toast.LENGTH_SHORT).show();
	}

	/*
	 * captureScreen()
	 * */
	public void captureScreen(Activity activity)
	{
		View v = activity.getWindow().getDecorView();
		v.setDrawingCacheEnabled(true);
		v.buildDrawingCache();
		
		Bitmap srcBitmap = v.getDrawingCache();
		Rect frame = new Rect();
		
		activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
		
		int statusHeight = frame.top;
		Point outSize = new Point();
		
		activity.getWindowManager().getDefaultDisplay().getSize(outSize);
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
                Toast.makeText(activity, "保存成功" + file.getName(), Toast.LENGTH_SHORT).show();
            } else {
            	Toast.makeText(activity, "失败", Toast.LENGTH_SHORT).show();
            }
				
            fos.close();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

	}

}
