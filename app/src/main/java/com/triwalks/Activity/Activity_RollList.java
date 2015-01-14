package com.triwalks.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.nhaarman.listviewanimations.appearance.AnimationAdapter;
import com.nhaarman.listviewanimations.appearance.simple.AlphaInAnimationAdapter;
import com.triwalks.Fragment.Fragment_Inner;
import com.triwalks.Common.MyListAdapter;
import com.triwalks.R;
import com.triwalks.Common.Serial_check_list;

public class Activity_RollList extends Activity_Navi {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setToolbarTitle("Camera Roll");
        // Inflate user main page fragment
        Frag_Roll_List fragment = new Frag_Roll_List();
        // must call setFragLayout right after the initialization
        fragment.setFragLayout(R.layout.frag_layout_roll_list);
        displayFragment(fragment);
    }

    public static class Frag_Roll_List extends Fragment_Inner {
        private AnimationAdapter mAnimAdapter;
        private BaseAdapter mAdapter;
        private ListView mListView;
        private Serial_check_list list;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            super.onCreateView(inflater, container, savedInstanceState);

            list = new Serial_check_list();
            mListView = (ListView) getFragView().findViewById(R.id.activity_mylist_listview);
            mAdapter = new MyListAdapter(getActivity().getApplicationContext(), list);
            setAlphaAdapter();

            getFragView().findViewById(R.id.check).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("checkList", list);
                    intent.setClass(getActivity(), Activity_Memories.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });

            return getFragView();
        }

        private void setAlphaAdapter() {
            if (!(mAnimAdapter instanceof AlphaInAnimationAdapter)) {
                mAnimAdapter = new AlphaInAnimationAdapter(mAdapter);
                mAnimAdapter.setAbsListView(mListView);
                mListView.setAdapter(mAnimAdapter);
                mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            }
        }
    }
}
