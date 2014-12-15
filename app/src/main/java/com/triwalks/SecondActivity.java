package com.triwalks;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;

import lib.image.ImageLoader;


public class SecondActivity extends NaviActivity {
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    static GridView gridView;

    ArrayList<String> imageUrls;
    private ImageLoader mImageLoader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // the code following will automatically create options animation
        /*mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        mDrawerLayout.setScrimColor(getResources().getColor(R.color.navdrawer_fading_background));
        mDrawerLayout.setBackgroundColor(getResources().getColor(R.color.white));
        // 實作 drawer toggle 並放入 toolbar
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, this.getToolbar(), R.string.drawer_open, R.string.drawer_close);
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);*/

        setToolbarTitle("WELCOME");

        // Inflate user main page fragment
        Frag_user_info fragment = new Frag_user_info();

        // Must set
        fragment.setFragLayout(R.layout.frag_layout_user_info);
        displayFragment(fragment);

//        MaterialDialog ask = new MaterialDialog.Builder(this)
//                .title("MAIN ACTIVITY")
//                .content("Let Google help apps determine location. This means sending anonymous location data to Google, even when no apps are running.")
//                .positiveText("Agree")
//                .negativeText("Disagree")
//                .show();

        //What columns do we want to check
        //MediaStore.Images.Media.DATA is the path of the image
        final String[] columns = { MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID };
        final String orderBy = MediaStore.Images.Media.DATE_TAKEN;
        //Each image has many columns of info. To save time we specify the name of the column we want.
        Cursor imagecursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null,
                null, orderBy + " DESC");

        imageUrls = new ArrayList<String>();
        //Get the paths of all photos
        for (int j = 0; j < imagecursor.getCount(); j++) {
            imagecursor.moveToPosition(j);
            //Get the number of the column based on its name
            int dataColumnIndex = imagecursor.getColumnIndex(MediaStore.Images.Media.DATA);
            String filePath = imagecursor.getString(dataColumnIndex);
            imageUrls.add(filePath);

//            System.out.println("=====> Array path => "+imageUrls.get(j));
        }
        imagecursor.close();

        mImageLoader = new ImageLoader(getApplicationContext());


    }

    protected void onResume(){
        super.onResume();
        //gridView is null until onCreate finished
        gridView.setAdapter(new ImageAdapter(this, imageUrls));
    }

    public float[] getGeotag(String path) {
        //Get geotag using ExifInterface class
        try {
            ExifInterface imgInfo = new ExifInterface(path);
            float[] geotag = new float[2];
            imgInfo.getLatLong(geotag);
            System.out.println("Latitude: " + geotag[0] + " Longitude: "+geotag[1]);
            return geotag;
        }catch(Exception e){
            System.out.println("Exception: Exifinterface fails");
            return null;
        }
    }

    private Bitmap overlay(Bitmap bmp1, Bitmap bmp2) {
        Bitmap bmOverlay = Bitmap.createBitmap(bmp1.getWidth(), bmp1.getHeight(), bmp1.getConfig());
        Canvas canvas = new Canvas(bmOverlay);
        canvas.drawBitmap(bmp1, new Matrix(), null);
        canvas.drawBitmap(bmp2, 15, 15, null);//15 is the width of the frame
        return bmOverlay;
    }

    public class ImageAdapter extends BaseAdapter {

        ArrayList<String> mList;
        LayoutInflater mInflater;
        Context mContext;
        SparseBooleanArray mSparseBooleanArray;

        public ImageAdapter(Context context, ArrayList<String> imageList) {
            // TODO Auto-generated constructor stub
            mContext = context;
            mInflater = LayoutInflater.from(mContext);
            mSparseBooleanArray = new SparseBooleanArray();
            this.mList = imageList;

        }

        public ArrayList<String> getCheckedItems() {
            ArrayList<String> mTempArry = new ArrayList<String>();

            for (int i = 0; i < mList.size(); i++) {
                if (mSparseBooleanArray.get(i)) {
                    mTempArry.add(mList.get(i));
                }
            }

            return mTempArry;
        }

        @Override
        //Must be given at first
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.item_photo, null);
            }

            CheckBox mCheckBox = (CheckBox) convertView.findViewById(R.id.checkBox1);
            final ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView);

//            imageView.setImageBitmap(SecondActivity.this.decodeSampledBitmapFromPath(imageUrls.get(position), 100, 100));
            mImageLoader.loadBitmap(imageUrls.get(position), imageView);

//            imageLoader.displayImage("file://"+imageUrls.get(position), imageView, options, new SimpleImageLoadingListener() {
//                @Override
//                public void onLoadingComplete(Bitmap loadedImage) {
//                    Animation anim = AnimationUtils.loadAnimation(MultiPhotoSelectActivity.this, R.anim.fade_in);
//                    imageView.setAnimation(anim);
//                    anim.start();
//                }
//            });

            mCheckBox.setTag(position);
            mCheckBox.setChecked(mSparseBooleanArray.get(position));
            mCheckBox.setOnCheckedChangeListener(mCheckedChangeListener);

            return convertView;
        }

        CompoundButton.OnCheckedChangeListener mCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                mSparseBooleanArray.put((Integer) buttonView.getTag(), isChecked);
            }
        };

    }

    public static class Frag_user_info extends InnerFragment {


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            super.onCreateView(inflater, container, savedInstanceState);
            // initialize
            getFragView().findViewById(R.id.pink_icon).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().finish();
                    /* if want to create new activity
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), MainActivity.class);
                    startActivity(intent);
                    getActivity().finish();*/
                }
            });

            gridView = (GridView)v.findViewById(R.id.gridview);
            if(gridView != null){
                System.out.println("gridView is not null");
            }


            return getFragView();
        }

//        public GridView getGridView(){
//            return this.gridView;
//        }
    }
}
