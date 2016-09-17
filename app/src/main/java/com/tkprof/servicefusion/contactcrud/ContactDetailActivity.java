package com.tkprof.servicefusion.contactcrud;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.Locale;
import java.util.Map;

public class ContactDetailActivity extends Activity
        implements GestureDetector.OnGestureListener,
        GestureDetector.OnDoubleTapListener {

    private DataSource datasource;
    SharedPreferences sharedPref;
    private GestureDetectorCompat mDetector;

    EditText e_first_name, e_last_name, e_date_of_birth, e_zip_code;

    ToggleButton tgb_start;
    Button  b_edit, b_new;

    int _id;
    String first_name, last_name, date_of_birth, zip_code ;
    boolean isIdReceivedWhenOpen = false;
    Map<Integer, String> pums;
    TextToSpeech ttobj;
    Locale locale;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTheme(android.R.style.Theme_Holo_Light);
        setContentView(R.layout.contact_detail);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            _id = extras.getInt("_id");
            isIdReceivedWhenOpen = true;
        }

        sharedPref = getSharedPreferences("contactcrud", Context.MODE_PRIVATE);

        e_first_name = (EditText) findViewById(R.id.e_first_name);
        e_last_name  = (EditText) findViewById(R.id.e_last_name);
        e_date_of_birth = (EditText) findViewById(R.id.e_date_of_birth);
        e_zip_code  = (EditText) findViewById(R.id.e_zip_code);

        b_edit = (Button) findViewById(R.id.b_edit);
        b_new = (Button) findViewById(R.id.b_new);

        if (isIdReceivedWhenOpen) {

            datasource = new DataSource(this);
            datasource.open();
            if (locale == null) {
                locale = new Locale(datasource.getLocale());
            }
            ;

            showContactDetail();
        }

        mDetector = new GestureDetectorCompat(this, this);
        mDetector.setOnDoubleTapListener(this);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    UtteranceProgressListener utteranceProgressListener = new UtteranceProgressListener() {

        @Override
        public void onStart(String utteranceId) {
            Log.d(TAG + ".utterancePressListener", "onStart ( utteranceId :" + utteranceId + " ) ");
        }

        @Override
        public void onError(String utteranceId) {
            Log.d(TAG + ".utteranceProgrstener", "onError ( utteranceId :" + utteranceId + " ) ");
        }

        @Override
        public void onDone(String utteranceId) {
            Log.d(TAG + ".utteranceProgressLner", "onDone ( utteranceId :" + utteranceId + " ) ");
            //Looping
            if (tgb_start.isChecked()) {
               // start();
            }
        }
    };


    String TAG = "MainActivity";

    public void showContactDetail() {

        ContactRecord row1 = datasource.getContact(_id);

        _id = row1.getKey();   // id
        first_name    = row1.getFirstName();  //  no
        last_name     = row1.getLastName();  //
        date_of_birth = row1.getDateOfBirth();  //
        zip_code      = row1.getZipCode();  //

        e_first_name.setText(first_name);
        e_last_name.setText(last_name);
        e_date_of_birth.setText(date_of_birth);
        e_zip_code.setText(zip_code);

    }

    private static final int SETTING_ACTIVITY = 10;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == SETTING_ACTIVITY) {
            if (data.hasExtra("returnKey1") &&
                    data.getExtras().getString("returnKey1").equals("Saved")) {
        /*Toast.makeText(this, data.getExtras().getString("returnKey1"),
            Toast.LENGTH_SHORT).show();*/

            }
        }
    }

    @Override
    public void onDestroy() {
      //  savePref();

        if (ttobj != null) {
            ttobj.stop();
            ttobj.shutdown();
        }


        super.onDestroy();
    }


    int clickCnt = 0;

    public void lineNo(View view) {
        ++clickCnt;

        // Log.i("credit", "cnt" + clickCnt);
        if (clickCnt < 5) return;

        Toast.makeText(this, "popup dialog for Line_no only for me"
                , Toast.LENGTH_LONG).show();
		/*
		String text =  getString(R.string.credit);
		Intent i = new Intent(CreditActivity.class);
		startActivity(i);*/
    }


    public static Intent mServiceIntent;

    CharSequence[] pumNames;
    Integer[] pumkeys;

/*
    CountDownTimer ct;
    CountDownTimer ct_remain;
    int file_line_cnt;

    // Default value for user to just start without setup
    Double interval_sec;

    boolean isStarted = false;
*/

/*
    public void pause() {
        if (ct != null) {
            ct.cancel();
            ct = null;
        }
        if (ct_remain != null) {
            ct_remain.cancel();
            ct_remain = null;
        }
        isStarted = false;  // release
    }*/

    String l_insOrupd = "";

    //TODO
    public void onClickSave(View view) {

        if( isIdReceivedWhenOpen ) {

            datasource.updateContact(_id,
                    e_first_name.getText().toString(),
                    e_last_name.getText().toString(),
                    e_date_of_birth.getText().toString(),
                    e_zip_code.getText().toString());
        } else {
            if ( e_first_name == null ) {
                Log.d("onclickSave", "e_first_name is NULL" ) ;
            }
            datasource.insertContact(
                    e_first_name.getText().toString(),
                    e_last_name.getText().toString(),
                    e_date_of_birth.getText().toString(),
                    e_zip_code.getText().toString());

            datasource.LAST_ROW_NO = datasource.getLastLineNo();
        }
    }

    // let both be able to delete
    public void onClickDel(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure to Delete?")
                .setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener);
        builder.show();
    }

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    //Yes button clicked
                    datasource.deleteLine(_id);

                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    //No button clicked
                    break;
            }
        }
    };

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.mDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        Log.i("onDouble tap", "tap");
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        Log.i("onDouble tap", "tap Event");

        return false;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        // TODO Auto-generated method stub

        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                            float distanceY) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        // TODO Auto-generated method stub

    }

    static final int SWIPE_MIN_DISTANCE = 120;
    static final int SWIPE_MAX_OFF_PATH = 250;
    static final int SWIPE_THRESHOLD_VELOCITY = 200;

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                           float velocityY) {

        if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE) {
         //TODO   next(_id);
        } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE) {
          // TODO  prev(_id);
        }
        return false;
        //return super.onFling(e1, e2, velocityX, velocityY);
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "ContactDetail Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse(""),
                //Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.tkprof.servicefusion.contactcrud/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "ContactDetail Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.tkprof.servicefusion.contactcrud/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
