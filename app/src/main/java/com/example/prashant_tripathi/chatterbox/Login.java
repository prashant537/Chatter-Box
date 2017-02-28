package com.example.prashant_tripathi.chatterbox;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Prashant_Tripathi on 28-09-2016.
 */
public class Login extends Activity {
    EditText login_username, login_password;
    Button login_register, login_login;
    TextView login_forgot;
    CheckBox login_remember;
    Bundle b;
    private String uname,pwd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.login);
        Login_Initialization();
    }

    /*INITIALIZATION*/
    private void Login_Initialization() {
        login_username=(EditText)findViewById(R.id.login_username);
        login_password=(EditText)findViewById(R.id.login_password);
        login_register=(Button)findViewById(R.id.login_register);
        login_login=(Button)findViewById(R.id.login_login);
        login_forgot=(TextView)findViewById(R.id.login_forgot);
        login_remember=(CheckBox)findViewById(R.id.login_remember);

        /*login_forgot.setOnTouchListener(new View.OnTouchListener() {
            @Override
           public boolean onTouch(View v, MotionEvent event) {
                Intent i=new Intent(Intent.ACTION_SEND);
                i.setData(Uri.parse("mailto :"));
                i.putExtra(Intent.EXTRA_EMAIL, "abc@gmail.com");
                i.putExtra(Intent.EXTRA_TEXT,"Hello");
                i.setType("message/rfc822");
                startActivity(Intent.createChooser(i,"Choose An App..."));

                return true;
            }
        });*/

    }


    /*BUTTON ACTIONS*/
    public void ButtonClickedLogin(View v){
        if(v.getId()==R.id.login_login){
            if(NonEmpty()){
                uname=login_username.getText().toString().trim();
                pwd=login_password.getText().toString().trim();
    /*VALIDATION CHECKING*/
                CheckUser();
            }
        }
        else if(v.getId()==R.id.login_register){
            Intent i=new Intent(Login.this,Register.class);
            startActivity(i);
        }
    }


    /*ERROR GENERATION*/
    private boolean NonEmpty(){
        if(login_username.getText().toString().equals("")||login_password.getText().toString().equals("")) {
            AlertDialog.Builder alert=new AlertDialog.Builder(Login.this);
            alert.setMessage("Username or Password Field is Empty");
            alert.setTitle("Login Denied !!!");
            alert.setIcon(R.drawable.login_empty);
            alert.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            alert.show();
            return false;
        }
        return true;
    }


    /*VALIDATION CHECKING*/
    private void CheckUser(){
        class CheckValidation extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading=ProgressDialog.show(Login.this,"Logging In ...","Please Wait ...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
    /*CHECKING FETCHED RESULTS*/
                CheckUserResult(s);
            }

            /*SENDING REQUEST TO FETCH USER DETAILS*/
            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh=new RequestHandler();
                String s=rh.SendGetRequestParam(Config.URL_GET_USER,uname.trim());
                return s;
            }
        }

        CheckValidation cv=new CheckValidation();
        cv.execute();
    }

    /*CHECKING FETCHED RESULTS*/
    private void CheckUserResult(String json){
        try{
            JSONObject jsonObject=new JSONObject(json);
            JSONArray result=jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);
            JSONObject c=result.getJSONObject(0);
            String name=c.getString(Config.TAG_NAME);
            String usr=c.getString(Config.TAG_USERNAME);
            String pass=c.getString(Config.TAG_PASSWORD);
            if(pass.equals(pwd)){
                if(login_remember.isChecked()){
                    SharedPreferences share=getSharedPreferences("Prashant",MODE_PRIVATE);
                    SharedPreferences.Editor ed=share.edit();
                    ed.putString("user", uname);
                    ed.putString("pass", pwd);
                    ed.commit();
                }
                Intent i=new Intent(Login.this,ChatRoom.class);
                i.putExtra("Username",usr);
                startActivity(i);
                finish();
            }
            else {
                AlertDialog.Builder alert=new AlertDialog.Builder(Login.this);
                alert.setMessage("Invalid Username or Password");
                alert.setTitle("Login Denied !!!");
                alert.setIcon(R.drawable.login_denied);
                alert.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alert.show();
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }
}

//Notification Icon
//Action bar
//Search bar    (secondary)
//Sending Email (secondary)
