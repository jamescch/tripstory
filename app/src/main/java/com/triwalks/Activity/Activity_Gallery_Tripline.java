package com.triwalks.Activity;

import android.content.Context;
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

import java.util.ArrayList;

public class Activity_Gallery_Tripline extends Activity_Gallery_Base {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setToolbarTitle("Select Trip Start/End Photo");
        fragment = (Fragment_Gallery) new Frag_Gallery_Tripline();
        fragment.setFragLayout(R.layout.frag_layout_gallery);
        displayFragment(fragment);
        getSupportLoaderManager().initLoader(URL_LOADER, null, this);
    }

    public static class Frag_Gallery_Tripline extends Fragment_Gallery {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            setImageViewLayout(R.layout.multi_item_photo);
            super.onCreateView(inflater, container, savedInstanceState);
            getFragView().findViewById(R.id.btn_select_photos).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ArrayList<Integer> items = mImageAdapter.getCheckedItems();
                    if(items == null) {
                        Toast.makeText(getFragView().getContext(), "Please choose Start/End photo", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("imageUrls", items);
                    startActivity(
                            CommonFunction.gotoIntent_WithObject(getFragView().getContext(), Activity_CreateTripline.class, bundle)
                    );
                }
            });
            return getFragView();
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
            ImageView imageView = (ImageView)v.findViewById(R.id.imageView);
            final LinearLayout flight = (LinearLayout) v.findViewById(R.id.img_select_flight);
            final LinearLayout home = (LinearLayout) v.findViewById(R.id.img_select_home);
            Context mContext = getFragView().getContext();
            final int[] range = mImageAdapter.getRange();
            // prevent select same photo
            if(range[0] == (int) imageView.getTag() || range[1] == (int) imageView.getTag()){
                return;
            }
            // limit only two picture
            else if(range[0] > -1 && range[1] > -1) {
                Toast.makeText(mContext, "Please unselect Start or End photo", Toast.LENGTH_SHORT).show();
                System.out.println("S:" + range[0] + " E:" + range[1]);
                return;
            }
            // timestamp check
            else if(range[0] > -1 && (range[0] < (int)imageView.getTag())){
                Toast.makeText(mContext, "End photo cannot early than Start one", Toast.LENGTH_SHORT).show();
                return;
            }
            // start
            else if(range[0] < 0) {
                System.out.println("start");
                range[0] = (int) imageView.getTag();
                mImageAdapter.getSparseBooleanArray().put((Integer) imageView.getTag(), true);
                flight.setVisibility(View.VISIBLE);
            }
            // end -> make sure that start was selected
            else if(range[1] < 0 && range[0] > -1) {
                range[1] = (int) imageView.getTag();
                mImageAdapter.getSparseBooleanArray().put((Integer) imageView.getTag(), true);
                home.setVisibility(View.VISIBLE);
            }
        }
    }
}
