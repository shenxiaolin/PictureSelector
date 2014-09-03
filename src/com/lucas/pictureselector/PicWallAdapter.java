package com.lucas.pictureselector;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.lucas.pictureselector.BitmapLib.PicZoomOutType;

public class PicWallAdapter extends ArrayAdapter<String> {
    
    // Lru：Least Recently Used 近期最少使用算法
    private LruCache<Integer, Bitmap> picCache;
    
    private Context context;

    public PicWallAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
        
        this.context = context;

        // 获取应用程序最大可用内存
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheSize = maxMemory / 8;
        // 设置图片缓存大小为程序最大可用内存的1/8
        picCache = new LruCache<Integer, Bitmap>(cacheSize) {

            @Override
            protected int sizeOf(Integer key, Bitmap value) {
                // return value.getByteCount();
                return super.sizeOf(key, value);
            }

        };

    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.pic_wall_item, null);  
        }
        
        final ImageView iv = (ImageView) convertView.findViewById(R.id.image);
       
        final String picPath = getItem(position);
        GridView gv = (GridView) parent;
        final int w = gv.getColumnWidth();
        
        Bitmap bitmap = picCache.get(position);
        if(bitmap != null) {
            iv.setImageBitmap(bitmap);            
            return convertView;
        }               
        
        new AsyncTask<Integer, Void, Bitmap> () {

            @Override
            protected Bitmap doInBackground(Integer... params) {        
                InputStream is = null;
                try {
                    is = context.getAssets().open(picPath);
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("", e.getMessage());
                    return null;
                }

                Bitmap bt =  BitmapLib.decodeBitmap(context, is, w, w, PicZoomOutType.DIG_CENTER);
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return bt;
            }

            @Override
            protected void onPostExecute(Bitmap result) {
                if(result != null) {
                    picCache.put(position, result);
                    iv.setImageBitmap(result);
                }
            }
            
        }.execute(position);
        
        return convertView;
    }

}
