package com.triwalks.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.triwalks.Activity.Activity_Gallery_Base;
import com.triwalks.Activity.Activity_Gallery_Single;
import com.triwalks.Activity.Activity_Gallery_Tripline;
import com.triwalks.Common.Lib.ImageAdapter_Base;
import com.triwalks.Common.Lib.ImageAdapter_Single;
import com.triwalks.Common.Lib.ImageAdapter_Tripline;
import com.triwalks.Common.Lib.ImageCache;
import com.triwalks.Common.Lib.ImageLoader;
import com.triwalks.R;

public class Fragment_Gallery extends Fragment_Inner implements GridView.OnItemClickListener{
    protected static GridView gridView;
    protected static final String IMAGE_CACHE_DIR = ".thumbs";
    protected ImageLoader mImageLoader;
    protected ImageAdapter_Base mImageAdapter;
    protected int imageViewLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        if(mImageLoader == null) {
            mImageLoader = new ImageLoader(getFragView().getContext());
            ImageCache.ImageCacheParams cacheParams =
                    new ImageCache.ImageCacheParams(getActivity(), IMAGE_CACHE_DIR);
            cacheParams.setMemCacheSizePercent(0.25f); // Set memory cache to 25% of app memory
            mImageLoader.addImageCache(getFragmentManager(), cacheParams);
        }

        if(mImageAdapter == null) {
            if(this instanceof Activity_Gallery_Single.Frag_Gallery_Single){
                mImageAdapter = (ImageAdapter_Base) new ImageAdapter_Single(getFragView().getContext(), getExifList(), mImageLoader, imageViewLayout);
            }
            else if(this instanceof Activity_Gallery_Tripline.Frag_Gallery_Tripline){
                mImageAdapter = (ImageAdapter_Base) new ImageAdapter_Tripline(getFragView().getContext(), getExifList(), mImageLoader, imageViewLayout);
            }
        }

        gridView = (GridView)getFragView().findViewById(R.id.gridview);
        gridView.setAdapter(mImageAdapter);
        gridView.setOnItemClickListener(this);

        return getFragView();
    }

    public GridView getGridView(){
        return gridView;
    }

    public void setImageViewLayout(int layout){
        this.imageViewLayout = layout;
    }

    public void removeSelectItems(){
        mImageAdapter.unSelect();
    }

    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {}

    public ImageAdapter_Base getImageAdapter(){
        return mImageAdapter;
    }
}