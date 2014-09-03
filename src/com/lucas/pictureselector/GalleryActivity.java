package com.lucas.pictureselector;

import java.io.IOException;
import java.io.InputStream;

import com.lucas.pictureselector.BitmapLib.PicZoomOutType;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class GalleryActivity extends Activity {

    public static void start(Context context, String[] picPathArray, int beginPos) {
        Intent intent = new Intent(context, GalleryActivity.class);
        intent.putExtra("picPathArray", picPathArray);
        intent.putExtra("beginPos", beginPos);
        context.startActivity(intent);
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        
        final String[] picPathArray = getIntent().getExtras().getStringArray("picPathArray");
        int beginPos = getIntent().getExtras().getInt("beginPos");
        
        final TextView rateTv = (TextView) findViewById(R.id.rate);
        
        ViewPager vp = (ViewPager) findViewById(R.id.pic_pager);
        vp.setAdapter(new PagerAdapter() {

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                Context context = GalleryActivity.this;
                
                InputStream is = null;
                try {
                    is = context.getAssets().open(picPathArray[position]);
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("", e.getMessage());
                    return null;
                }

                
                Bitmap bitmap = BitmapLib.decodeBitmap(context, is, 10500, 15000, PicZoomOutType.FILL);
                
                try {
                    is.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                
                View view = LayoutInflater.from(context).inflate(R.layout.gallery_item, null);  
                ImageView iv = (ImageView) view.findViewById(R.id.image);
                iv.setImageBitmap(bitmap);

                container.addView(view);
                return view;
            }

            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                return arg0 == arg1;  
            }
            
            @Override
            public int getCount() {
                return picPathArray.length;
            }
            
            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                View view = (View) object;  
                container.removeView(view);  
            }
        });
        
        vp.setOnPageChangeListener(new OnPageChangeListener() {
            
            @Override
            public void onPageSelected(int currentPage) {
                rateTv.setText((currentPage + 1) + "/" + picPathArray.length);  
            }
            
            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) { }
            
            @Override
            public void onPageScrollStateChanged(int arg0) { }
        });
        
        rateTv.setText((beginPos + 1) + "/" + picPathArray.length);  
        vp.setCurrentItem(beginPos);
    }

}
