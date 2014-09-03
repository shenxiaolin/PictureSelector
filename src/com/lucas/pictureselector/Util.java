package com.lucas.pictureselector;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Point;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

public class Util {   
        
    private static int screenWidth = -1;
    private static int screenHeight = -1;
    
    public static int getScreenWidth(Context context) {
        if(screenWidth != -1) {
            return screenWidth;
        }
        
        Point size = new Point();
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getSize(size);
        
        screenWidth = size.x;
        return screenWidth;
    }
    
    public static int getScreenHeight(Context context) {
        if(screenHeight != -1) {
            return screenHeight;
        }
        
        Point size = new Point();
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getSize(size);
        
        screenHeight = size.y;
        return screenHeight;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     * @param context
     * @param dpValue
     * @return
     */
    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
        
    //    Resources res = context.getResources();
    //    DisplayMetrics dm = res.getDisplayMetrics();
    //    return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, dm);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
    
    public static String getCurrDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        return format.format(new Date());
    }
    
    public static String getCurrTimeNoSecond() {
        SimpleDateFormat format = new SimpleDateFormat("hh:mm", Locale.CHINA);
        return format.format(new Date());
    }
    
    public static String getCurrDateTimeNoSecond() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm", Locale.CHINA);
        return format.format(new Date());
    }
    
    public static String getCurrDateTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.CHINA);
        return format.format(new Date());
    }
    
    public static long parseDateTime(String dateTime) throws ParseException {
        return new SimpleDateFormat("yyyy-MM-dd hh:mm", Locale.CHINA).parse(dateTime).getTime();
    }
    
    // 比较传入的两个日期时间的大小。
    // 参数需为 yyyy-MM-dd hh:mm 形式
    public static int dateTimeCmp(String dt1, String dt2) {
        SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        
        Date begin = null;
        try {
            begin = dfs.parse(dt1);
        } catch (ParseException e) {
            throw new IllegalArgumentException("第一个参数格式不合法");
        }

        Date end = null;
        try {
            end = dfs.parse(dt2);
        } catch (ParseException e) {
            throw new IllegalArgumentException("第二个参数格式不合法");
        }
        
        return begin.compareTo(end);
    }
    
    public static int dateTimeCmp(String dt) {
        // 只传一个参数，默认和当前时间比
        return dateTimeCmp(dt, new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA).format(new Date()));
    }
    
    // 计算传入的两个日期时间的差。
    // 参数需为 yyyy-MM-dd hh:mm 形式
    public static String calDateTimeDiff(String dt1, String dt2) {
        SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        
        Date begin = null;
        try {
            begin = dfs.parse(dt1);
        } catch (ParseException e) {
            throw new IllegalArgumentException("第一个参数格式不合法");
        }

        Date end = null;
        try {
            end = dfs.parse(dt2);
        } catch (ParseException e) {
            throw new IllegalArgumentException("第二个参数格式不合法");
        }

        long secondDiff = (end.getTime() - begin.getTime()) / 1000; // 除以1000是为了转换成秒
        secondDiff = Math.abs(secondDiff);  

        long day = secondDiff / (24 * 3600);
        long hour = secondDiff % (24 * 3600) / 3600;
        long minute = secondDiff % 3600 / 60;
                
        StringBuilder builder = new StringBuilder();
        if(day > 0) {
            builder.append(day);
            builder.append("天");
        }
        if(hour > 0) {
            builder.append(hour);
            builder.append("小时");
        }
        if(minute > 0) {
            builder.append(minute);
            builder.append("分钟");
        }
        
        return builder.toString();
    }
    
    public static String calDateTimeDiff(String dt) {
     // 只传一个参数，默认和当前时间计算差值
        return calDateTimeDiff(dt, new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA).format(new Date()));
    }
    
    public static String getUniqueFileName() {
        // 试试   UUID.randomUUID();
        
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhhmmss", Locale.CHINA);
        return format.format(new Date());
    }
    
    /*
     *  v: parent view of TextViews
     *  ids: TextView's id in v
     *  names: column names
     */
    public static void setTVTextByCursor(View v, int[] ids, Cursor c, String[] names) {
        for(int i = 0; i < ids.length; i++) {
            ((TextView)(v.findViewById(ids[i]))).setText(c.getString(c.getColumnIndex(names[i])));
        }
    }
    
    /*
     *  v: parent view of TextViews
     *  id: TextView's id in v
     */    
    public static String getTVText(View v, int id) {
        return ((TextView) v.findViewById(id)).getText().toString();
    }
    
    public static void setTVText(View v, int id, CharSequence text) {
        ((TextView) v.findViewById(id)).setText(text);
    }
    
    public static File getFileInSdcardByName(Context context, String fileName, boolean createIfNoeExists) {
        String state = Environment.getExternalStorageState();
        if(!state.equals(Environment.MEDIA_MOUNTED)) {
            Log.e("MBM", "SD卡没有插入或没有写权限");
            Toast.makeText(context, "SD卡没有插入或没有写权限", Toast.LENGTH_LONG).show();
            return null;
        }

        File file = new File(Environment.getExternalStorageDirectory(), "HappyMemo");

        if(!file.exists()) {
            if(!file.mkdirs()) {
                Log.e("MBM", "在SD卡中创建文件夹失败");
                Toast.makeText(context, "在SD卡中创建文件夹失败", Toast.LENGTH_LONG).show();
                return null;
            }
        }
        
        File f =  new File(file, fileName);
        if(!f.exists()) {
            if(!createIfNoeExists) {
                return null;
            }

            try {
                f.createNewFile();
            } catch (IOException e) {
                Log.e("", "fffff   " + e.getMessage());
            }

        }
        
        return f;
    }
    
    public static void deleteFileInSdcardByName(Context context, String fileName) {
        File f = getFileInSdcardByName(context, fileName, false);
        if(f != null) {
            f.delete();
        }
    }
}
