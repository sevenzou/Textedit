package com.seven.textedit.filestream;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import android.content.Context;
import android.util.Log;

public class FileStreamManager {
	private String TAG = FileStreamManager.class.getSimpleName();
	private static final Object syncObject = new Object();
	private static FileStreamManager fileStreamManager = null;
	private String path = null;
	private Context context;
	
	public static FileStreamManager getInstance(Context context)
	{
		if (fileStreamManager == null) {
			synchronized(syncObject) {
				if (fileStreamManager == null) {
					fileStreamManager = new FileStreamManager(context);
				}
			}
		}
		return fileStreamManager;
	}
	
	private FileStreamManager(Context context)
	{
		this.context = context;
	}
	
	public int createFile(String path)
	{
		File file = new File(path);
		if (!file.exists()) {
			Log.v(TAG, "createFile() the file:"+path+" isnot exist");
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.v(TAG, "createFile() the file:"+path+" create fail");
			}
		} else {
			Log.v(TAG, "createFile() the file:"+path+" is exist");
		}
		return -1;
	}
	
	public String readFile(String path)
	{
//		FileOutputStream out;
		String content = "";
		File file = new File(path);
		if (!file.isDirectory()) {
			try {
				FileInputStream in = new FileInputStream(file);
				if (in != null) {
					InputStreamReader inputReader = new InputStreamReader(in);
					BufferedReader bufferReader = new BufferedReader(inputReader);
					String line;
					while((line = bufferReader.readLine()) != null) {
						content += line + "\n";
					}
					in.close();
				}
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} else {
			Log.v(TAG, "readFile() the file:"+path+" isnot exist");
		}
		Log.v("TAG", "readFile() -- content£º"+content);
		return content;
	}
	
	public int writeFile(String path, String text)
	{
		if (text == null)
			return -1;
		Log.v(TAG, "writeFile() the file:"+path+" £»text£º"+ text);
		byte[] buffer = text.getBytes();
		File file = new File(path);
		if (file.exists()) {
			try {
				FileOutputStream out = new FileOutputStream(file);
				out.write(buffer);
				out.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			Log.v(TAG, "writeFile() the file:"+path+" isnot exist");
			return -1;
		}
		return 0;
	}
	
	public int renameFile(String localFilePath, String rename)
	{
		File localFile = new File(localFilePath);
        localFile.renameTo(new File(localFile.getAbsolutePath(), rename));
		return -1;
	}
	
	public String getFilePath()
	{
		return context.getFilesDir().getPath() + "/";
	}
	
	public boolean deleteFile(String path)
	{
		File file = new File(path);
		return file.delete();
	}
	
	
	/*
	 * getFileSize()
	 * get a single file size
	 * 
	 * */
	public long getFileSize(File file) throws Exception
	{
		long size = 0;
		if (file.exists()) {
			FileInputStream in = new FileInputStream(file);
			size = in.available();
		} else {
			file.createNewFile();
		}
		return size;
	}
	
	/*
	 * getDirFileSize()
	 * get folder file size
	 * */
	public long getDirFileSize(File file) throws Exception
	{
		long size = 0;
		File filelist[] = file.listFiles();
		for (int i=0; i < filelist.length; i++) {
			if (filelist[i].isDirectory()) {
				size += getDirFileSize(filelist[i]);
			} else {
				size +=  getFileSize(filelist[i]);
			}
		}
		return size;
	}

}
