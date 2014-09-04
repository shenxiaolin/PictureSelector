package com.lucas.pictureselector;

import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class BitmapLib {

    // ָ������BitmapFactory.Options.inSampleSize��С���ͼƬ����Ȼ����Ҫ��ͼƬ��ʱ�Ĵ�������
    enum PicZoomOutType {
        DIG_CENTER,  // ���м���һ��
        ZOOM_OUT,    // ��������С������ Bitmap.createScaledBitmap
        FILL;        // ���
        /*
         * ��ֱ�ӵ��õ��� Bitmap.createScaledBitmap ��С����Ҫ�ĳߴ磬
         * ������ͨ��BitmapFactory.Options.inSampleSize��С
         * ��Ϊ�˷�ֹ�����ͼƬ���OOM
         */
    }
    
    public static Bitmap decodeBitmap(Context context, String picPath, int w, int h, PicZoomOutType zoomOutType) {
        if(w < 0 || h < 0) {
            throw new IllegalArgumentException("w, h���붼����0");
        }

        int screenWidth = Util.getScreenWidth(context);
        int screenHeight = Util.getScreenHeight(context);
        
        // ��ֹͼƬ������ʾ������
        if (w > screenWidth) { // ��ȵ����ֵ
            w = screenWidth;
        }

        if (h > screenHeight) { // �߶ȵ����ֵ
            h = screenHeight;
        } 
        
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(picPath, options);

        // ȡ������С��һ���� ��֤��С���ͼƬ����Ҫ��ͼƬ��
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
             * Bitmap.createBitmap����Ҫ�� 
             * x + width must be <= bitmap.width()
             * y + height must be <= bitmap.height()
             */
            return Bitmap.createBitmap(bitmap, x, y, finalW, finalH);
        case ZOOM_OUT:            
            if(btW > w) {
                // �����ѹ���˶��٣��߶���ҲҪͬ��ѹ������
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
            throw new IllegalArgumentException("w, h���붼����0");
        }

        int screenWidth = Util.getScreenWidth(context);
        int screenHeight = Util.getScreenHeight(context);
        
        // ��ֹͼƬ������ʾ������
        if (w > screenWidth) { // ��ȵ����ֵ
            w = screenWidth;
        }

        if (h > screenHeight) { // �߶ȵ����ֵ
            h = screenHeight;
        } 
        
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        BitmapFactory.decodeStream(is, null, options);

        // ȡ������С��һ���� ��֤��С���ͼƬ����Ҫ��ͼƬ��
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
             * Bitmap.createBitmap����Ҫ�� 
             * x + width must be <= bitmap.width()
             * y + height must be <= bitmap.height()
             */
            return Bitmap.createBitmap(bitmap, x, y, finalW, finalH);
        case ZOOM_OUT:   
            if(btW <= w && btH <= h) {
                return bitmap;
            }
            if(btW > w) {
                // �����ѹ���˶��٣��߶���ҲҪͬ��ѹ������
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
