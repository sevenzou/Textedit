package com.seven.textedit.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Locale;

import com.seven.textedit.BaseApplication;

import android.R;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Paint;
import android.text.format.Time;
import android.util.Log;

public class StrUtil
{
    final static String TAG = StrUtil.class.getSimpleName();

    public static boolean isNullOrEmpty(String value)
    {
        return (value == null) || (value.length() == 0) || (value.equals("null"));
    }

    public static String[] concat(String[] a, String[] b)
    {
        String[] c = new String[a.length + b.length];
        System.arraycopy(a, 0, c, 0, a.length);
        System.arraycopy(b, 0, c, a.length, b.length);
        return c;
    }

    public static String getCurrentStrTime(String format)
    {
    	//format like "hh:mm:ss" / "yyyy-MM-dd-HH-mm-ss"
    	SimpleDateFormat sDateFormat = new SimpleDateFormat(format, Locale.getDefault());
        String time = sDateFormat.format(new java.util.Date());
    	return time;
    }
    
    public static int getCurrentTime()
    {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("hh:mm:ss", Locale.getDefault());
        String time = sDateFormat.format(new java.util.Date());

        String[] myTime = time.split(":");
        int hour = StrUtil.parseInt(myTime[0]);
        int min = StrUtil.parseInt(myTime[1]);
        int sec = StrUtil.parseInt(myTime[2]);
        int totalSec = hour * 3600 + min * 60 + sec;
        return totalSec;
    }

    public static int sec2min(int sec)
    {
        return sec / 60;
    }

    public static String getHourMinStr(Time time)
    {
        if (time != null)
        {
            return String.format("%02d", time.hour) + ":" + String.format("%02d", time.minute);
        }
        else
        {
            return "00:00";
        }
    }

    public static void parseTime(final String strHourMin, final String todayDate, Time time, boolean bNow)
    {
        if (time == null)
        {
            time = new Time();
            time.setToNow();
        }

        //存入日期参数
        if (isNullOrEmpty(todayDate))
        {
            if (time != null && bNow)
            {
                time.setToNow();
            }
        }
        else
        {
            if (-1 != todayDate.indexOf("-"))
            {
                if (bNow) // 先把年月日设置好，然后再修改小时和分钟
                {
                    time.setToNow();
                }
                String date[] = todayDate.split("-");
                if (date.length > 0)
                {
                    time.year = StrUtil.parseInt(date[0]);
                    if (date.length > 1)
                    {
                        time.month = StrUtil.parseInt(date[1]) + 1;
                        if (date.length > 2)
                        {
                            time.monthDay = StrUtil.parseInt(date[2]);
                        }
                        else
                        {
                            time.setToNow();
                        }
                    }
                    else
                    {
                        time.setToNow();
                    }
                }
                else
                {
                    time.setToNow();
                }
            }
        }

        //存入时间参数
        if (isNullOrEmpty(strHourMin))
        {
            if (time != null && bNow)
            {
                time.setToNow();
            }
        }
        else
        {
            if (-1 != strHourMin.indexOf(":"))
            {
                if (bNow) // 先把年月日设置好，然后再修改小时和分钟
                {
                    time.setToNow();
                }
                String timer[] = strHourMin.split(":");
                if (timer.length > 0)
                {
                    time.hour = StrUtil.parseInt(timer[0]);
                    if (timer.length > 1)
                    {
                        time.minute = StrUtil.parseInt(timer[1]);
                    }
                    else
                    {
                        time.minute = 0;
                    }
                    time.second = 0;
                }
                else
                {
                    time.setToNow();
                }
            }
        }
    }

    public static void parseHourMin(final String strHourMin, Time time, boolean bNow)
    {
        if (time == null)
        {
            time = new Time();
            time.setToNow();
        }
        if (isNullOrEmpty(strHourMin))
        {
            if (time != null && bNow)
            {
                time.setToNow();
            }
        }
        else
        {
            if (-1 != strHourMin.indexOf(":"))
            {
                if (bNow) // 先把年月日设置好，然后再修改小时和分钟
                {
                    time.setToNow();
                }
                time.hour = StrUtil.parseInt(strHourMin.substring(0, strHourMin.indexOf(":")));
                time.minute = StrUtil.parseInt(strHourMin.substring(strHourMin.indexOf(":") + 1, strHourMin.length()));
                time.second = 0;
            }
        }
    }

    public static String trimUrlTail(String url)
    {
        if (isNullOrEmpty(url))
        {
            return url;
        }
        int i = url.indexOf("?");
        if (i != -1)
        {
            return url.substring(0, i);
        }
        return url;
    }

    public static boolean equalAndNotNull(final String a, final String b)
    {
        if (isNullOrEmpty(a) || isNullOrEmpty(b))
        {
            return false;
        }
        if (a.equals(b))
        {
            return true;
        }
        return false;
    }

    public static Long parseLong(final String string)
    {
        try
        {
            return Long.valueOf(string);
        }
        catch (NumberFormatException e)
        {
            Log.e(TAG, "parseLong: NumberFormatException string = " + string);
            //LogUtil.handleUncaughtException(BaseApplication.getInstance(), e);
            return 0L;
        }
        catch (Exception e)
        {
            Log.e(TAG, "parseLong: Exception string = " + string);
            //LogUtil.handleUncaughtException(BaseApplication.getInstance(), e);
            return 0L;
        }
    }

    public static int parseInt(final String string)
    {
        try
        {
            return Integer.parseInt(string);
        }
        catch (NumberFormatException e)
        {
            Log.e(TAG, "parseInt: NumberFormatException string = " + string);
            //LogUtil.handleUncaughtException(BaseApplication.getInstance(), e);
            return 0;
        }
        catch (Exception e)
        {
            Log.e(TAG, "parseInt: Exception string = " + string);
            //LogUtil.handleUncaughtException(BaseApplication.getInstance(), e);
            return 0;
        }
    }

    public static int parseInt(final String string, final int radix)
    {
        try
        {
            return Integer.parseInt(string, radix);
        }
        catch (NumberFormatException e)
        {
            Log.e(TAG, "parseInt: NumberFormatException string = " + string + " radix = " + radix);
            //LogUtil.handleUncaughtException(BaseApplication.getInstance(), e);
            return 0;
        }
        catch (Exception e)
        {
            Log.e(TAG, "parseInt: Exception string = " + string + " radix = " + radix);
            //LogUtil.handleUncaughtException(BaseApplication.getInstance(), e);
            return 0;
        }
    }

//    public static void putShareIntValue(String key, int value)
//    {
//        putShareIntValue(AppData.PREF_SHARE_NAME, key, value);
//    }

    public static void putShareIntValue(String pref_name, String key, int value)
    {
        Log.d(TAG, "putShareIntValue is in value = " + value);
        try
        {
            SharedPreferences sharedPreferences = BaseApplication.getInstance().getSharedPreferences(pref_name,
                    Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(key, value);
            editor.commit();
        }
        catch (Exception e)
        {
            Log.e(TAG, "putShareIntValue key = " + key + " value = " + value);
            e.printStackTrace();
        }
    }

//    public static void putShareLongValue(String key, long value)
//    {
//        putShareLongValue(AppData.PREF_SHARE_NAME, key, value);
//    }

    public static void putShareLongValue(String pref_name, String key, long value)
    {
        Log.d(TAG, "putShareLongValue is in value = " + value);
        try
        {
            SharedPreferences sharedPreferences = BaseApplication.getInstance().getSharedPreferences(pref_name,
                    Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putLong(key, value);
            editor.commit();
        }
        catch (Exception e)
        {
            Log.e(TAG, "putShareLongValue key = " + key + " value = " + value);
            e.printStackTrace();
        }
    }

//    public static void putShareBooleanValue(String key, boolean value)
//    {
//        putShareBooleanValue(AppData.PREF_SHARE_NAME, key, value);
//    }

    public static void putShareBooleanValue(String pref_name, String key, boolean value)
    {
        Log.d(TAG, "putShareBooleanValue is in value = " + value);
        try
        {
            SharedPreferences sharedPreferences = BaseApplication.getInstance().getSharedPreferences(pref_name,
                    Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(key, value);
            editor.commit();
        }
        catch (Exception e)
        {
            Log.e(TAG, "putShareBooleanValue key = " + key + " value = " + value);
            e.printStackTrace();
        }
    }

//    public static void putShareStringValue(String key, String value)
//    {
//        putShareStringValue(AppData.PREF_SHARE_NAME, key, value);
//    }

    public static void putShareStringValue(String pref_name, String key, String value)
    {
        Log.d(TAG, "putShareStringValue is in value = " + value);
        try
        {
            SharedPreferences sharedPreferences = BaseApplication.getInstance().getSharedPreferences(pref_name,
                    Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(key, value);
            editor.commit();
        }
        catch (Exception e)
        {
            Log.e(TAG, "putShareStringValue key = " + key + " value = " + value);
            e.printStackTrace();
        }
    }

    public static int getShareIntValue(String pref_name, String key, int value)
    {
        Log.d(TAG, "getShareIntValue is in value = " + value);
        int ret = 0;
        try
        {
            SharedPreferences sharedPreferences = BaseApplication.getInstance().getSharedPreferences(pref_name,
                    Context.MODE_PRIVATE);
            ret = sharedPreferences.getInt(key, value);
        }
        catch (Exception e)
        {
            Log.e(TAG, "getShareIntValue key = " + key + " value = " + value);
            e.printStackTrace();
            ret = 0;
        }
        return ret;
    }

    public static long getShareLongValue(String pref_name, String key, long value)
    {
        Log.d(TAG, "getShareLongValue is in value = " + value);
        long ret = 0L;
        try
        {
            SharedPreferences sharedPreferences = BaseApplication.getInstance().getSharedPreferences(pref_name,
                    Context.MODE_PRIVATE);
            ret = sharedPreferences.getLong(key, value);
        }
        catch (Exception e)
        {
            Log.e(TAG, "getShareLongValue key = " + key + " value = " + value);
            e.printStackTrace();
            ret = 0L;
        }
        return ret;
    }

    public static boolean getShareBooleanValue(String pref_name, String key, boolean value)
    {
        Log.d(TAG, "getShareBooleanValue is in value = " + value);
        boolean ret = false;
        try
        {
            SharedPreferences sharedPreferences = BaseApplication.getInstance().getSharedPreferences(pref_name,
                    Context.MODE_PRIVATE);
            ret = sharedPreferences.getBoolean(key, value);
        }
        catch (Exception e)
        {
            Log.e(TAG, "getShareBooleanValue key = " + key + " value = " + value);
            e.printStackTrace();
            ret = false;
        }
        return ret;
    }

    public static String getShareStringValue(String pref_name, String key, String value)
    {
        Log.d(TAG, "getShareStringValue is in value = " + value);
        String ret = "";
        try
        {
            SharedPreferences sharedPreferences = BaseApplication.getInstance().getSharedPreferences(pref_name,
                    Context.MODE_PRIVATE);
            ret = sharedPreferences.getString(key, value);
        }
        catch (Exception e)
        {
            Log.e(TAG, "getShareStringValue key = " + key + " value = " + value);
            e.printStackTrace();
            ret = "";
        }
        return ret;
    }

    public static void removeShareValue(String pref_name, String key)
    {
        Log.d(TAG, "clearShareValue is in key = " + key);
        try
        {
            SharedPreferences sharedPreferences = BaseApplication.getInstance().getSharedPreferences(pref_name,
                    Context.MODE_PRIVATE);
            Editor editor = sharedPreferences.edit();
            editor.remove(key);
            editor.commit();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.e(TAG, "clearShareValue Exception");
        }
    }

    public static String getCurrentVersionName()
    {
        String currentVersion = "0.0.0.0";
        try
        {
            String pName = BaseApplication.getInstance().getPackageName();
            PackageInfo pinfo;
            pinfo = BaseApplication.getInstance().getPackageManager()
                    .getPackageInfo(pName, PackageManager.GET_CONFIGURATIONS);
            currentVersion = pinfo == null ? "0.0.0.0" : pinfo.versionName;
        }
        catch (NameNotFoundException e)
        {
            e.printStackTrace();
            currentVersion = "0.0.0.0";
        }
        return currentVersion;
    }
    
    public static String getCurrentPackageName()
    {
        String currentVersion = null;
        try
        {
            String pName = BaseApplication.getInstance().getPackageName();
            PackageInfo pinfo;
            pinfo = BaseApplication.getInstance().getPackageManager()
                    .getPackageInfo(pName, PackageManager.GET_CONFIGURATIONS);
            currentVersion = pinfo.packageName;
        }
        catch (NameNotFoundException e)
        {
            e.printStackTrace();
        }
        return currentVersion;
    }

    public static boolean excuteCommand(String command)
    {
        boolean excuteSuccess = false;
        Runtime r = Runtime.getRuntime();
        Process p;
        try
        {
            p = r.exec(command);
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String inline;
            while ((inline = br.readLine()) != null)
            {
                System.out.println(inline);
            }
            br.close();
            excuteSuccess = true;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            Log.e(TAG, "excuteCommand IOException");
            excuteSuccess = false;
        }
        return excuteSuccess;
    }

    public static float getNumFromStr(Context mContex, String str)
    {
        //        int bytesLength = str.getBytes().length;
        //        int sLength = str.length();
        //        int hasNum = bytesLength - sLength;
        //        if (hasNum == 0)
        //        {
        //            Log.i(TAG, "字符串中不含有汉字。");
        //        }
        //        else if (hasNum > 0)
        //        {
        //            Log.i(TAG, "含有  " + hasNum + "  个汉字。");
        //        }
        //        return hasNum/2;
        //
        Paint paint = new Paint();
        float layoutWidth = 5;//mContex.getResources().getDimension(R.dimen.banner_second_party_width);
        float size = paint.measureText(str.toString());
        //Log.d(TAG,"layoutWidth = " + layoutWidth);
        //Log.d(TAG,"size = " + size);
        return size / layoutWidth;
    }
}
