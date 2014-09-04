package com.lucas.pictureselector;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.lucas.pictureselector.PictureSelector.OnPicSelectedListener;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        final ImageView iv = (ImageView) findViewById(R.id.image);
        
        findViewById(R.id.begin).setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                PictureSelector.open(MainActivity.this, new OnPicSelectedListener() {
                    
                    @Override
                    public void onSelected(Bitmap bitmap) {
                        iv.setImageBitmap(bitmap);
                    }
                });
            }
        });
    }
}
