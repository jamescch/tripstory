package com.triwalks.Common.Lib;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import android.util.LruCache;
import android.widget.ImageView;

import com.triwalks.R;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class ImageLoader {
    private Context context;
    private LruCache<String, Bitmap> mMemoryCache;
    protected Resources mResources;
    private ImageCache mImageCache;

    public ImageLoader(Context context){
        this.context = context;
        mResources = context.getResources();

        /** Caching */
        // Get max available VM memory, exceeding this amount will throw an
        // OutOfMemory exception. Stored in kilobytes as LruCache takes an
        // int in its constructor.
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        // Use 1/8th of the available memory for this memory cache.
        final int cacheSize = maxMemory / 4;

        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                return bitmap.getByteCount() / 1024;
            }
        };
    }

    public void addImageCache(FragmentManager fragmentManager, ImageCache.ImageCacheParams cacheParams) {
        mImageCache = ImageCache.getInstance(fragmentManager, cacheParams);
        new CacheAsyncTask().execute();
    }

    public void loadBitmap(String path, ImageView imageView) {
        //caching
        final String imageKey = path;

        final BitmapDrawable bitmap = mImageCache.getBitmapFromMemCache(imageKey);
        if (bitmap != null) {
            imageView.setImageDrawable(bitmap);
        } else {
            if(cancelPotentialWork(path, imageView)) { // Without this, it works slower
                BitmapWorkerTask task = new BitmapWorkerTask(path, imageView);
                Bitmap mPlaceHolderBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.transparent_dark_background);
                final AsyncDrawable asyncDrawable =
                        new AsyncDrawable(context.getResources(), mPlaceHolderBitmap, task);
                imageView.setImageDrawable(asyncDrawable);
                task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, path);
            }
        }

    }

    public boolean cancelPotentialWork(String data, ImageView imageView) {
        final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);

        if (bitmapWorkerTask != null) {
            final String bitmapData = bitmapWorkerTask.path;
            // If bitmapData is not yet set or it differs from the new data
            if (bitmapData == null || !bitmapData.equals(data) ) {
                // Cancel previous task
                bitmapWorkerTask.cancel(true);
            } else {
                // The same work is already in progress
                return false;
            }
        }
        // No task associated with the ImageView, or an existing task was cancelled
        return true;
    }

    public BitmapWorkerTask getBitmapWorkerTask(ImageView imageView) {
        if (imageView != null) {
            final Drawable drawable = imageView.getDrawable();
            if (drawable instanceof AsyncDrawable) {
                final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
                return asyncDrawable.getBitmapWorkerTask();
            }
        }
        return null;
    }
    // return a bitmap whose size is near requested size but not smaller than
    public static Bitmap decodeSampledBitmapFromPath(String path, int reqWidth, int reqHeight) {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // Store dimensions info into options object
        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
        Bitmap scaledBitmap;
        // We crop the longer side of the image to it's center
        if(bitmap.getWidth() > bitmap.getHeight()){
            scaledBitmap = Bitmap.createBitmap(
                    bitmap, (bitmap.getWidth()-bitmap.getHeight())/2, 0, bitmap.getHeight(), bitmap.getHeight());
        }else{
            scaledBitmap = Bitmap.createBitmap(
                    bitmap, 0, (bitmap.getHeight()-bitmap.getWidth())/2, bitmap.getWidth(), bitmap.getWidth());
        }
        return scaledBitmap;
    }

    // return a bitmap whose size is near requested size but not smaller than
    public static Bitmap decodeTripLineBitmapFromPath(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        options.inSampleSize = calculateInSampleSize(options, options.outWidth/2, options.outHeight/2);
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
        Bitmap scaledBitmap;
        if(bitmap.getWidth() > bitmap.getHeight()){
            scaledBitmap = Bitmap.createBitmap(
                    bitmap, (bitmap.getWidth()-bitmap.getHeight())/2, 0, bitmap.getHeight(), bitmap.getHeight());
        }else{
            scaledBitmap = Bitmap.createBitmap(
                    bitmap, 0, (bitmap.getHeight()-bitmap.getWidth())/2, bitmap.getWidth(), bitmap.getWidth());
        }
        return scaledBitmap;
    }

    public static Bitmap decodeSampledBitmapFromDescriptor(
            FileDescriptor fileDescriptor, int reqWidth, int reqHeight, ImageCache cache) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        // If we're running on Honeycomb or newer, try to use inBitmap
        if (Utils.hasHoneycomb()) {
            addInBitmapOptions(options, cache);
        }

        return BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options);
    }

    public static Bitmap decodeSampleBitmapFromStream(InputStream in, int reqWidth, int reqHeight){
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        // copy input stream because it will be empty after first decodeStream
        // Fake code simulating the copy
        // You can generally do better with nio if you need...
        // And please, unlike me, do something about the Exceptions :D
        byte[] buffer = new byte[1024];
        int len;
        try {
            while ((len = in.read(buffer)) > -1) {
                baos.write(buffer, 0, len);
            }
            baos.flush();
        }catch(IOException e){
            e.printStackTrace();
        }
        // Open new InputStreams using the recorded bytes
        // Can be repeated as many times as you wish
        InputStream is1 = new ByteArrayInputStream(baos.toByteArray());
        InputStream is2 = new ByteArrayInputStream(baos.toByteArray());

        BitmapFactory.decodeStream(is1, null, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeStream(is2, null, options);
    }

    private static void addInBitmapOptions(BitmapFactory.Options options, ImageCache cache) {
        // inBitmap only works with mutable bitmaps, so force the decoder to
        // return mutable bitmaps.
        options.inMutable = true;

        if (cache != null) {
            // Try to find a bitmap to use for inBitmap.
            Bitmap inBitmap = cache.getBitmapFromReusableSet(options);

            if (inBitmap != null) {
                // If a suitable bitmap has been found, set it as the value of
                // inBitmap.
                options.inBitmap = inBitmap;
            }
        }
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    /** Inner Class */

    private class AsyncDrawable extends BitmapDrawable {
        private final WeakReference<BitmapWorkerTask> bitmapWorkerTaskReference;

        public AsyncDrawable(Resources res, Bitmap bitmap,
                             BitmapWorkerTask bitmapWorkerTask) {
            super(res, bitmap);
            bitmapWorkerTaskReference =
                    new WeakReference<BitmapWorkerTask>(bitmapWorkerTask);
        }

        public BitmapWorkerTask getBitmapWorkerTask() {
            return bitmapWorkerTaskReference.get();
        }
    }

    private class BitmapWorkerTask extends AsyncTask<String, Void, BitmapDrawable> {
        private final WeakReference<ImageView> imageViewReference;
        public String path;

        public BitmapWorkerTask(String path, ImageView imageView) {
            // Use a WeakReference to ensure the ImageView can be garbage collected
            imageViewReference = new WeakReference<ImageView>(imageView);
            this.path = path;
        }

        // Decode image in background.
        @Override
        protected BitmapDrawable doInBackground(String... params) {
            Bitmap bitmap = null;
            BitmapDrawable drawable = null;
            if (mImageCache != null && !isCancelled() && getAttachedImageView() != null) {
                bitmap = mImageCache.getBitmapFromDiskCache(path);
            }
            if(bitmap == null && !isCancelled() && getAttachedImageView() != null) {
                bitmap = decodeSampledBitmapFromPath(path, 100, 100);
            }
            if(bitmap != null) {
                if (Utils.hasHoneycomb()) {
                    // Running on Honeycomb or newer, so wrap in a standard BitmapDrawable
                    drawable = new BitmapDrawable(mResources, bitmap);
                } else {
                    // Running on Gingerbread or older, so wrap in a RecyclingBitmapDrawable
                    // which will recycle automagically
                    drawable = new RecyclingBitmapDrawable(mResources, bitmap);
                }

                if(mImageCache != null) {
                    mImageCache.addBitmapToCache(path, drawable);
                }
            }
            return drawable;
        }

        // Once complete, see if ImageView is still around and set bitmap.
        @Override
        protected void onPostExecute(BitmapDrawable value) {
            if (isCancelled()) {
                value = null;
            }
            final ImageView imageView = getAttachedImageView();
            if (value != null && imageView != null) {
                imageView.setImageDrawable(value);
            }
        }

        private ImageView getAttachedImageView() {
            final ImageView imageView = imageViewReference.get();
            final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);

            if (this == bitmapWorkerTask) {
                return imageView;
            }
            return null;
        }
    }

    protected class CacheAsyncTask extends AsyncTask<Object, Void, Void> {
        @Override
        protected Void doInBackground(Object... params) {
            if(mImageCache != null){
                mImageCache.initDiskCache();
            }
            return null;
        }
    }
}
