package com.example.prashant_tripathi.chatterbox;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

/**
 * Created by Prashant_Tripathi on 28-09-2016.
 */
public class Splash extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        new MyTask().execute();
    }
    private class MyTask extends AsyncTask<Void,Void,Void> {
        ProgressDialog progress;
        SharedPreferences share;
        String user,pass;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress=new ProgressDialog(Splash.this);
            progress.setTitle("Loading ...");
            progress.setMessage("Please Wait ...");
            progress.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            share=getSharedPreferences("Prashant",MODE_PRIVATE);
            user=share.getString("user","");
            pass=share.getString("pass","");
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progress.dismiss();
            if(user.equals("")&&pass.equals("")){
                Intent i=new Intent(Splash.this,Login.class);
                startActivity(i);
                finish();
            }
            else{
                Intent i=new Intent(Splash.this,ChatRoom.class);
                i.putExtra("Username",user);
                startActivity(i);
                finish();
            }
        }
    }
}
