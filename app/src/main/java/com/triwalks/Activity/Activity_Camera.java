package com.triwalks.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.triwalks.Fragment.Fragment_Inner;
import com.triwalks.R;

public class Activity_Camera extends Activity_Navi {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setToolbarTitle("Select Photos");
        // Inflate user main page fragment
        Frag_Camera fragment = new Frag_Camera();
        // must call setFragLayout right after the initialization
        fragment.setFragLayout(R.layout.frag_layout_camera);
        displayFragment(fragment);
    }

    public static class Frag_Camera extends Fragment_Inner {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            super.onCreateView(inflater, container, savedInstanceState);

            Intent intent_camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // 0 is request code
            startActivityForResult(intent_camera, 0);

            return getFragView();
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data){
            super.onActivityResult(requestCode, resultCode, data);
            ImageView iv = (ImageView)getFragView().findViewById(R.id.imagecaptured);
            if (resultCode == RESULT_OK){
                Bundle extras = data.getExtras();
                Bitmap bmp = (Bitmap) extras.get("data");
                iv.setImageBitmap(bmp);
            }
        }
    }
}
