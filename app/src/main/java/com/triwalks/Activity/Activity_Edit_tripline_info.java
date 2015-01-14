package com.triwalks.Activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.triwalks.Common.CustomTextWatcher;
import com.triwalks.Common.Lib.ImageLoader;
import com.triwalks.Fragment.Fragment_CustomMap;
import com.triwalks.R;

import java.io.File;
import java.util.ArrayList;


public class Activity_Edit_tripline_info extends Activity_Navi {
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setToolbarTitle("TripSpot");
        //getSupportActionBar().hide();
        Frag_user_info fragment = new Frag_user_info();
        fragment.setFragLayout(R.layout.frag_layout_single_tripline_info);

        displayFragment(fragment);
    }

    public static class Frag_user_info extends Fragment_CustomMap {
        private Uri mImageUri;
        File tmpPhoto;
        private EditText photo_title;
        private boolean isShot = false;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            super.onCreateView(inflater, container, savedInstanceState);
            // initialize
            getFragView().findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tmpPhoto.delete();
                    getActivity().finish();
                }
            });

            ((ScrollView)getFragView().findViewById(R.id.horizontalScrollView1)).setSmoothScrollingEnabled(true);
            ((TextView)getFragView().findViewById(R.id.textView1)).setMovementMethod(new ScrollingMovementMethod());

            if(getActivity().getIntent().getExtras() != null){
                int checkItem = getActivity().getIntent().getExtras().getInt("imageUrl");
                ImageView iv = (ImageView)getFragView().findViewById(R.id.photo);
                iv.setImageBitmap(ImageLoader.decodeTripLineBitmapFromPath(getExifList().get(checkItem).getPath()));
            } else if(!isShot) {
                Intent intent_camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //startActivityForResult(intent_camera, 0);

                try {
                    // place where to store camera taken picture
                    tmpPhoto = this.createTemporaryFile("picture", ".jpg");
                    tmpPhoto.delete();
                    mImageUri = Uri.fromFile(tmpPhoto);
                } catch (Exception e) {
                    ;//Toast.makeText(getFragView().getContext(), "Please check SD card! Image shot is impossible!", Toast.LENGTH_SHORT);
                }

                isShot = true;
                intent_camera.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
                //start camera intent
                startActivityForResult(intent_camera, 0);
            }

            photo_title = (EditText)getFragView().findViewById(R.id.trip_photo_title);
            photo_title.addTextChangedListener(new CustomTextWatcher(getFragView().getContext(), 20, photo_title));

            return getFragView();
        }

        private File createTemporaryFile(String part, String ext) throws Exception {
            File tempDir= Environment.getExternalStorageDirectory();
            tempDir=new File(tempDir.getAbsolutePath()+"/.temp/");
            if(!tempDir.exists()) {
                tempDir.mkdir();
            }
            return File.createTempFile(part, ext, tempDir);
        }

        public void grabImage(ImageView imageView){
            getActivity().getContentResolver().notifyChange(mImageUri, null);
            ContentResolver cr = getActivity().getContentResolver();
            Bitmap bitmap;
            try {
                bitmap = android.provider.MediaStore.Images.Media.getBitmap(cr, mImageUri);
                imageView.setImageBitmap(bitmap);
            }
            catch (Exception e) {
                ;
            }
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data){
            if (resultCode == RESULT_OK)
                this.grabImage((ImageView)getFragView().findViewById(R.id.photo));
            else
                getActivity().finish();
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
