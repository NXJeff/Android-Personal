package com.woi.merlin.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by YeekFeiTan on 3/3/2015.
 */
public class MediaUtil {

    private static final String APP_DIRECTORY_NAME = "Merlin";
    private static final String IMAGE_DIRECTORY_NAME = "Images";
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    /**
     * Creating file uri to store image/video
     */
    public static Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /*
     * returning image / video
     */
    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStorageDirectory(),
                APP_DIRECTORY_NAME + File.separator + IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                        + APP_DIRECTORY_NAME + File.separator + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
            Log.i("MediaUtil", mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }

    public static void compressImage(String path) {
        try {
//            InputStream is = new FileInputStream(file);
            BitmapFactory.Options options = new BitmapFactory.Options();
//            options.inSampleSize = 4; //subsampling

            // First decode with inJustDecodeBounds=true to check dimensions
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, options);

            // Calculate inSampleSize
            options.inSampleSize = getInSampleSize(options);

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;

            int compress = 75;
            Bitmap bitmap = BitmapFactory.decodeFile(path, options);
            bitmap.compress(Bitmap.CompressFormat.JPEG, compress, new FileOutputStream(new File(path)));

        } catch (FileNotFoundException fnfe) {
        }
    }

    private static int getInSampleSize(BitmapFactory.Options options) {
        int reqWidth = 1280;
        int reqHeight = 1280;

        return calculateInSampleSize(options, reqWidth, reqHeight);
    }

    private static int calculateInSampleSize(BitmapFactory.Options options,
                                             int reqWidth, int reqHeight) {

        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    /**
     * Convenient method to delete a file
     * @param path
     * @return
     */
    public static boolean deleteFile(String path) {
        File file = new File(path);
        if (file != null) {
            return file.delete();
        }
        return false;
    }

}
