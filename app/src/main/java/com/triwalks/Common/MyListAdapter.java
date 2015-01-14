/*
 * Copyright 2014 Niek Haarman
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.triwalks.Common;

import android.content.Context;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nhaarman.listviewanimations.ArrayAdapter;
import com.triwalks.R;

public class MyListAdapter extends ArrayAdapter<String> {
    private Serial_check_list x;
    private final Context mContext;
    private int size;

    public MyListAdapter(final Context context, Serial_check_list l) {
        size = TripLineInfo.getInstance().getNumberOfTripLines();
        mContext = context;
        x = l;
        for (int i = 0; i < size; i++) {
            add(mContext.getString(R.string.row_number, i));
        }
    }

    @Override
    public long getItemId(final int position) {
        return getItem(position).hashCode();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        TextView view = (TextView) convertView;
        if (view == null) {
            view = (TextView) LayoutInflater.from(mContext).inflate(R.layout.list_row, parent, false);
        }

        view.setText(getItem(position));
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Vibrator v = (Vibrator) parent.getContext().getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(30);
                if(x.contains(position)) {
                    for(int i=0; i<x.size(); i++){
                        if(x.get(i) == position) {
                            x.remove(i);
                            view.setBackgroundColor(mContext.getResources().getColor(R.color.text_primary_inverse));
                            ((TextView)view).setTextColor(mContext.getResources().getColor(R.color.text_primary));
                            break;
                        }
                    }
                }
                else {
                    x.add(position);
                    view.setBackgroundColor(mContext.getResources().getColor(R.color.text_primary));
                    ((TextView)view).setTextColor(mContext.getResources().getColor(R.color.text_primary_inverse));
                }
            }
        });

        if(x.contains(position)) {
            view.setBackgroundColor(mContext.getResources().getColor(R.color.text_primary));
            view.setTextColor(mContext.getResources().getColor(R.color.text_primary_inverse));
        }
        else {
            view.setBackgroundColor(mContext.getResources().getColor(R.color.text_primary_inverse));
            view.setTextColor(mContext.getResources().getColor(R.color.text_primary));
        }
        return view;
    }
}