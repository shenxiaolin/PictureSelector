package com.lucas.pictureselector;

import java.io.IOException;
import java.util.Arrays;

import android.app.Activity;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class PicWallActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic_wall);
        
        final String[] picPathArr;
        AssetManager am = getAssets();
        try {
            picPathArr = am.list("belle");
        } catch (IOException e) {
            e.printStackTrace();
            return ;
        }
        
        for(int i = 0; i < picPathArr.length; i++) {
            picPathArr[i] = "belle/" + picPathArr[i];
        }

        GridView picWall = (GridView) findViewById(R.id.pic_wall);        
        
        picWall.setAdapter(new PicWallAdapter(this, R.layout.pic_wall_item, Arrays.asList(picPathArr)));
        
        picWall.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GalleryActivity.start(PicWallActivity.this, picPathArr, position);
            }
        });
    }

}






