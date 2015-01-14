package com.triwalks.Activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.triwalks.Common.CommonFunction;
import com.triwalks.Fragment.Fragment_Gallery;
import com.triwalks.R;

public class Activity_Gallery_Single extends Activity_Gallery_Base {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        setToolbarTitle(bundle.getString("title"));
        fragment = (Fragment_Gallery) new Frag_Gallery_Single();
        fragment.setFragLayout(R.layout.frag_layout_gallery_single);
        displayFragment(fragment);
        getSupportLoaderManager().initLoader(URL_LOADER, null, this);
    }

    public static class Frag_Gallery_Single extends Fragment_Gallery {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            setImageViewLayout(R.layout.single_item_photo);
            super.onCreateView(inflater, container, savedInstanceState);
            getFragView().findViewById(R.id.btn_select_photos).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Integer item = mImageAdapter.getSingleCheckedItem();
                    if(item == null) {
                        Toast.makeText(getFragView().getContext(), "Please choose one photo", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Bundle bundle = new Bundle();
                    bundle.putInt("imageUrl", item);
                    startActivity(
                        CommonFunction.gotoIntent_WithObject(getFragView().getContext(), Activity_Edit_tripline_info.class, bundle)
                    );
                }
            });

            getFragView().findViewById(R.id.btn_shot).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(
                        CommonFunction.gotoIntent(getActivity(), Activity_Edit_tripline_info.class)
                    );
                }
            });

            return getFragView();
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
            ImageView imageView = (ImageView)v.findViewById(R.id.imageView);
            final LinearLayout check = (LinearLayout) v.findViewById(R.id.img_select_check);
            final int[] range = mImageAdapter.getRange();

            if(range[0] > -1 && range[0] != imageView.getTag()) {
                Toast.makeText(getFragView().getContext(), "Please unselect previous photo", Toast.LENGTH_SHORT).show();
                return;
            }
            else if(range[0] == -1) {
                mImageAdapter.getSparseBooleanArray().put((Integer)imageView.getTag(), true);
                check.setVisibility(View.VISIBLE);
                range[0] = (int) imageView.getTag();
            }
        }
    }
}
