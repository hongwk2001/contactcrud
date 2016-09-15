package com.tkprof.servicefusion.contactcrud;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.ContextMenu;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/*
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
*/


public class MainActivity extends ListActivity implements GestureDetector.OnGestureListener,
        GestureDetector.OnDoubleTapListener {

    private DataSource datasource;
    SharedPreferences sharedPref;
    private GestureDetectorCompat mDetector;


    TextToSpeech ttobj;
    Locale locale;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
int _id;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTheme(android.R.style.Theme_Holo_Light);
        setContentView(R.layout.activity_main);


        sharedPref = getSharedPreferences("ReadRepeat", Context.MODE_PRIVATE);

        restorePref();

        datasource = new DataSource(this);
        datasource.open();

        List<ContactRecord> values = datasource.getContacts()  ;

        DataArrayAdapter adapter = new DataArrayAdapter(this, values);
        setListAdapter(adapter);

        registerForContextMenu(this.getListView());

        mDetector = new GestureDetectorCompat(this, this);
        mDetector.setOnDoubleTapListener(this);

        if (locale == null) {
            locale = new Locale(datasource.getLocale());
        };

        ttobj = new TextToSpeech(getApplicationContext(),
                new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int status) {
                        if (status != TextToSpeech.ERROR) {
                            //  ttobj.setLanguage(Locale.getDefault());
                            // ttobj.setLanguage(Locale.KOREAN);
                            ttobj.setLanguage(locale);
                        }
                    }
                });

        ttobj.setOnUtteranceProgressListener(utteranceProgressListener);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    UtteranceProgressListener utteranceProgressListener = new UtteranceProgressListener() {
        @Override
        public void onStart(String utteranceId) {
           Log.d(TAG + ".", "onStart ( utteranceId :" + utteranceId + " ) ");
        }

        @Override
        public void onError(String utteranceId) {
            Log.d(TAG + ".", "onError ( utteranceId :" + utteranceId + " ) ");
        }

        @Override
        public void onDone(String utteranceId) {
            Log.d(TAG + ".", "onDone ( utteranceId :" + utteranceId + " ) ");
        }
    };

    String TAG = "MainActivity";

    private static final int SETTING_ACTIVITY = 10;

	/*@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent;
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	    case R.id.mi_setting:
	    	intent = new Intent(this, SettingActivity.class) ;
	    	startActivityForResult(intent, SETTING_ACTIVITY);
            return true;
	     case R.id.mi_tts:

	    	 ttobj.speak(contents, TextToSpeech.QUEUE_FLUSH, null);

            return true;
        default:
	            return super.onOptionsItemSelected(item);
	    }
	} */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == SETTING_ACTIVITY) {
            if (data.hasExtra("returnKey1") &&
                    data.getExtras().getString("returnKey1").equals("Saved")) {
        /*Toast.makeText(this, data.getExtras().getString("returnKey1"),
            Toast.LENGTH_SHORT).show();*/
                restorePref();
            }
        }
    }

    @Override
    public void onDestroy() {
        savePref();

        if (ttobj != null) {
            ttobj.stop();
            ttobj.shutdown();
        }

        super.onDestroy();
    }

    private void restorePref() {
      /*  curr_line_no = sharedPref.getInt("curr_line_no", 0);
        curr_pum_no = sharedPref.getInt("curr_pum_no", 0);
        curr_phase_no = sharedPref.getInt("curr_phase_no", 0);*/
    }

    int clickCnt = 0;

    private void savePref() {
        SharedPreferences.Editor editor = sharedPref.edit();
     /*   editor.putInt("curr_line_no", curr_line_no);
        editor.putInt("curr_pum_no", curr_pum_no);
        editor.putInt("curr_phase_no", curr_phase_no);*/
        editor.commit();
    }

    public static Intent mServiceIntent;

    protected void speach(String tts, String utteranceId) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, utteranceId);
        ttobj.speak(tts, TextToSpeech.QUEUE_FLUSH, params);
    }

    String l_insOrupd = "";


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Select The Action");
        menu.add(0, v.getId(), 0, "edit");//groupId, itemId, order, title
        menu.add(0, v.getId(), 0, "delete");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item){
        ListView lv = (ListView) this.getListView();

        AdapterView.AdapterContextMenuInfo acmi =  (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        ContactRecord obj = (ContactRecord) lv.getItemAtPosition(acmi.position);


        int _no = obj.getKey() ;

        Log.d("MainActivity. ","position " + acmi.position + " no:" + _no );

        if(item.getTitle()=="delete"){
            Toast.makeText(this, "Code for Delete", Toast.LENGTH_LONG).show();
        }else   if(item.getTitle()=="edit"){
            Intent intent = new Intent(this, ContactDetailActivity.class);
            intent.putExtra("_id",  _no);
            startActivity(intent);
        }else {
            return false;
        }
        return true;
    }

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
      //  ttobj.speak(contents, TextToSpeech.QUEUE_FLUSH, null);
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent motionEvent) {
        return false;
    }

    static final int SWIPE_MIN_DISTANCE = 120;

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                           float velocityY) {

        if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE) {
          //  showContactDetail(tv_pum);
        } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE) {
          //  prev(tv_pum);
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
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
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
                "Main Page", // TODO: Define a title for the content shown.
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
