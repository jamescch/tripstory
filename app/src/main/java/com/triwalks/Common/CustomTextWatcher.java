package com.triwalks.Common;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;

public class CustomTextWatcher implements TextWatcher {
    private boolean OUT_OF_BOUND = false;
    private int MAX_LENGTH;
    private EditText targetText;
    private Context context;

    public CustomTextWatcher(Context context, int length, EditText text){
        this.MAX_LENGTH = length;
        this.targetText = text;
        this.context = context;
    }

    public void afterTextChanged(Editable s) {
        if(OUT_OF_BOUND){
            int editStart = targetText.getSelectionStart();
            targetText.removeTextChangedListener(this);
            while(getLength(s.toString()) >= MAX_LENGTH){
                s.delete(editStart - 2, editStart);
                editStart -= 2;
            }
            targetText.setText(s);
            targetText.setSelection(editStart);
            targetText.addTextChangedListener(this);
            OUT_OF_BOUND = false;
            Toast.makeText(context, s.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        ;
    }

    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if(getLength(s.toString()) > MAX_LENGTH)
            OUT_OF_BOUND = true;
    }

    private int getLength(String s){
        float length = 0;

        for(int i=0; i<s.length(); i++){
            if(isCN( s.substring(s.length() - i)) || (s.charAt(s.length() - i - 1 ) <=90 && s.charAt(s.length() - i -1) >= 65))
                length += 1.7;
            else {
                length += 1;
            }
        }
        return Math.round(length);
    }

    public boolean isCN(String str) {
        try {
            byte[] bytes = str.getBytes("UTF-8");
            if (bytes.length == str.length()) {
                return false;
            } else {
                return true;
            }
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }
}
