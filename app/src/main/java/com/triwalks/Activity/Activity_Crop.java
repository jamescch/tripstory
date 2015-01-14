package com.triwalks.Activity;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.triwalks.Common.CommonFunction;
import com.triwalks.Common.Lib.ImageLoader;
import com.triwalks.Cropper.CropImageView;
import com.triwalks.R;

import java.io.File;

public class Activity_Crop extends Activity {
    private static Bitmap avatar;
    final static String APP_PATH_SD_CARD = "/triwalks/.thumbnails";
    final static String AVATAR_FILE_NAME = "avatar.png";
    final static String TARGET_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + APP_PATH_SD_CARD;
    final static String AVATAR_FILE_PATH = TARGET_PATH + File.separator + AVATAR_FILE_NAME;

    // Static final constants
    private static final int DEFAULT_ASPECT_RATIO_VALUES = 10;
    private static final int ROTATE_NINETY_DEGREES = 90;
    private static final String ASPECT_RATIO_X = "ASPECT_RATIO_X";
    private static final String ASPECT_RATIO_Y = "ASPECT_RATIO_Y";
    private static final int ON_TOUCH = 1;

    // Instance variables
    private int mAspectRatioX = DEFAULT_ASPECT_RATIO_VALUES;
    private int mAspectRatioY = DEFAULT_ASPECT_RATIO_VALUES;

    Bitmap croppedImage;

    // Saves the state upon rotating the screen/restarting the activity
    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putInt(ASPECT_RATIO_X, mAspectRatioX);
        bundle.putInt(ASPECT_RATIO_Y, mAspectRatioY);
    }

    // Restores the state upon rotating the screen/restarting the activity
    @Override
    protected void onRestoreInstanceState(Bundle bundle) {
        super.onRestoreInstanceState(bundle);
        mAspectRatioX = bundle.getInt(ASPECT_RATIO_X);
        mAspectRatioY = bundle.getInt(ASPECT_RATIO_Y);
    }

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_crop);

        // Sets fonts for all
        // ViewGroup root = (ViewGroup) findViewById(R.id.mylayout);
        // setFont(root, mFont);

        // Initialize components of the app
        final CropImageView cropImageView = (CropImageView) findViewById(R.id.CropImageView);
        cropImageView.setImageResource(R.drawable.beach);
        Spinner showGuidelinesSpin = (Spinner) findViewById(R.id.showGuidelinesSpin);

        // Set initial spinner value
        showGuidelinesSpin.setSelection(ON_TOUCH);

        //Set AspectRatio fixed for circular selection
        cropImageView.setFixedAspectRatio(true);

        // Sets initial aspect ratio to 10/10
        cropImageView.setAspectRatio(DEFAULT_ASPECT_RATIO_VALUES, DEFAULT_ASPECT_RATIO_VALUES);

        //Sets the rotate button
        final Button rotateButton = (Button) findViewById(R.id.Button_rotate);
        rotateButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                cropImageView.rotateImage(ROTATE_NINETY_DEGREES);
            }
        });

        // Sets up the Spinner
        showGuidelinesSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                cropImageView.setGuidelines(i);
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });

        final Button cropButton = (Button) findViewById(R.id.Button_crop);
        final Activity activity = this;
        cropButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                avatar = cropImageView.getCroppedCircleImage();
                CommonFunction.returnIntent_WithObject(activity, new Bundle());
                finish();
            }
        });

    }

    public static Bitmap getAvatar(){
        // implement singleton pattern
        if(avatar == null){
            synchronized (Activity_Crop.class){
                if(avatar == null){
                    Bitmap bitmap = ImageLoader.decodeTripLineBitmapFromPath(AVATAR_FILE_PATH);
                    avatar = bitmap;
                    return bitmap;
                }
            }
        }
        return avatar;
    }

    public void setFont(ViewGroup group, Typeface font) {
        int count = group.getChildCount();
        View v;
        for (int i = 0; i < count; i++) {
            v = group.getChildAt(i);
            if (v instanceof TextView || v instanceof EditText || v instanceof Button) {
                ((TextView) v).setTypeface(font);
            } else if (v instanceof ViewGroup)
                setFont((ViewGroup) v, font);
        }
    }
}
