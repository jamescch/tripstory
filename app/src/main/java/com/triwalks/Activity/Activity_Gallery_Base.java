package com.triwalks.Activity;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import com.triwalks.Fragment.Fragment_Gallery;
import com.triwalks.R;

public class Activity_Gallery_Base extends Activity_Navi implements LoaderManager.LoaderCallbacks<Cursor> {
    protected static final int URL_LOADER = 0;
    protected Fragment_Gallery fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void finish() {
        super.finish();
        fragment.removeSelectItems();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderID, Bundle bundle) {
        switch (loaderID) {
            case URL_LOADER:
                final String[] projections = { /* must have _ID which is the key */MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA, MediaStore.Images.Media.LATITUDE, MediaStore.Images.Media.LONGITUDE, MediaStore.Images.Media.DATE_TAKEN};
                // Returns a new CursorLoader
                return new CursorLoader(
                        this.getApplicationContext(),   // Parent activity context
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,        // Table to query
                        projections,     // Projection to return
                        MediaStore.Images.Media.LATITUDE + ">?",            // No selection clause
                        new String[]{"" + 0},            // No selection arguments
                        MediaStore.Images.Media.DATE_TAKEN + " DESC"             // Default sort order
                );
            default:
                // An invalid id was passed in
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        fragment.initExifList(cursor);
        fragment.getImageAdapter().setExifList(fragment.getExifList());
        fragment.getImageAdapter().notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        System.out.println("onLoaderReset");
    }
}