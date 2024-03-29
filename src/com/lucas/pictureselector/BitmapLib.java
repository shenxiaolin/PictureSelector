package com.lucas.pictureselector;

import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class BitmapLib {

    // 指定经过BitmapFactory.Options.inSampleSize缩小后的图片，仍然比需要的图片大时的处理类型
    enum PicZoomOutType {
        DIG_CENTER,  // 从中间挖一块
        ZOOM_OUT,    // 按比例缩小，调用 Bitmap.createScaledBitmap
        FILL;        // 填充
        /*
         * 不直接调用调用 Bitmap.createScaledBitmap 缩小到需要的尺寸，
         * 而是先通过BitmapFactory.Options.inSampleSize缩小
         * 是为了防止过大的图片造成OOM
         */
    }
    
    public static Bitmap decodeBitmap(Context context, String picPath, int w, int h, PicZoomOutType zoomOutType) {
        if(w < 0 || h < 0) {
            throw new IllegalArgumentException("w, h必须都大于0");
        }

        int screenWidth = Util.getScreenWidth(context);
        int screenHeight = Util.getScreenHeight(context);
        
        // 防止图片过大，显示不出来
        if (w > screenWidth) { // 宽度的最大值
            w = screenWidth;
        }

        if (h > screenHeight) { // 高度的最大值
            h = screenHeight;
        } 
        
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(picPath, options);

        // 取两者中小的一个， 保证缩小后的图片比需要的图片大
        options.inSampleSize = Math.min(options.outWidth / w, options.outHeight / h);
        options.inJustDecodeBounds = false;
        
        Bitmap bitmap = BitmapFactory.decodeFile(picPath, options);
        if(bitmap == null) {
            return null;
        }
        
        int btW = bitmap.getWidth();
        int btH = bitmap.getHeight();
        
        if(btW <= w && btH <= h) {
            return bitmap;
        }

        switch (zoomOutType) {
        case DIG_CENTER:
            int x = btW - w;
            int y = btH - h;
            
            int finalW = 0;
            int finalH = 0;
            if(x <= 0) {
                x = 0;
                finalW = btW;
            } else {
                x /= 2;
                finalW = w;
            }
            
            if (y <= 0) {
                y = 0;
                finalH = btH;
            } else {
                y /= 2;
                finalH = h;
            }

            /*
             * Bitmap.createBitmap函数要求： 
             * x + width must be <= bitmap.width()
             * y + height must be <= bitmap.height()
             */
            return Bitmap.createBitmap(bitmap, x, y, finalW, finalH);
        case ZOOM_OUT:            
            if(btW > w) {
                // 宽度上压缩了多少，高度上也要同样压缩多少
                float ratio = w / (float)btW;
                return Bitmap.createScaledBitmap(bitmap, w, (int)(btH * ratio), false);
            }
            break;
        case FILL:
            float ratio = w / (float)btW;
            return Bitmap.createScaledBitmap(bitmap, w, (int)(btH * ratio), false);
        default:
            break;
        }

        return null;       
    }
    
    public static Bitmap decodeBitmap(Context context, InputStream is, int w, int h, PicZoomOutType zoomOutType) {     
        if(w < 0 || h < 0) {
            throw new IllegalArgumentException("w, h必须都大于0");
        }

        int screenWidth = Util.getScreenWidth(context);
        int screenHeight = Util.getScreenHeight(context);
        
        // 防止图片过大，显示不出来
        if (w > screenWidth) { // 宽度的最大值
            w = screenWidth;
        }

        if (h > screenHeight) { // 高度的最大值
            h = screenHeight;
        } 
        
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        BitmapFactory.decodeStream(is, null, options);

        // 取两者中小的一个， 保证缩小后的图片比需要的图片大
        options.inSampleSize = Math.min(options.outWidth / w, options.outHeight / h);
        options.inJustDecodeBounds = false;
        
        Bitmap bitmap = BitmapFactory.decodeStream(is, null, options);
        if(bitmap == null) {
            return null;
        }
        
        int btW = bitmap.getWidth();
        int btH = bitmap.getHeight();        

        switch (zoomOutType) {
        case DIG_CENTER:
            if(btW <= w && btH <= h) {
                return bitmap;
            }
            
            int x = btW - w;
            int y = btH - h;
            
            int finalW = 0;
            int finalH = 0;
            if(x <= 0) {
                x = 0;
                finalW = btW;
            } else {
                x /= 2;
                finalW = w;
            }
            
            if (y <= 0) {
                y = 0;
                finalH = btH;
            } else {
                y /= 2;
                finalH = h;
            }

            /*
             * Bitmap.createBitmap函数要求： 
             * x + width must be <= bitmap.width()
             * y + height must be <= bitmap.height()
             */
            return Bitmap.createBitmap(bitmap, x, y, finalW, finalH);
        case ZOOM_OUT:   
            if(btW <= w && btH <= h) {
                return bitmap;
            }
            if(btW > w) {
                // 宽度上压缩了多少，高度上也要同样压缩多少
                float ratio = w / (float)btW;
                return Bitmap.createScaledBitmap(bitmap, w, (int)(btH * ratio), false);
            }
            break;
        case FILL:
            float ratio = w / (float)btW;
            return Bitmap.createScaledBitmap(bitmap, w, (int)(btH * ratio), false);
        default:
            break;
        }

        return null;       
    }
}
