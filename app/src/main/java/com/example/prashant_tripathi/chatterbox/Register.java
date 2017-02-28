package com.example.prashant_tripathi.chatterbox;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.HashMap;

/**
 * Created by Prashant_Tripathi on 28-09-2016.
 */
public class Register extends Activity {


    private String stream[]={"MECHANICAL ENGINEERING","ELECTRICAL ENGINEERING","CIVIL ENGINEERING","ELECTRONICS & COMMUNICATION ENGINEERING","COMPUTER SCIENCE ENGINEERING","INFORMATION TECHNOLOGY"};
    Button register_clear,register_register;
    EditText register_name,register_roll,register_email,register_username,register_pass,register_confirm;
    AutoCompleteTextView register_stream;
    RadioGroup register_gender;
    RadioButton register_male;
    int note_id=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        Register_Initialization();
    }

    private void Register_Initialization(){
    /*INITIALIZATION*/
        register_name=(EditText)findViewById(R.id.register_name);
        register_roll=(EditText)findViewById(R.id.register_roll);
        register_stream=(AutoCompleteTextView)findViewById(R.id.register_stream);
        register_gender=(RadioGroup)findViewById(R.id.register_gender);
        register_email=(EditText)findViewById(R.id.register_email);
        register_username=(EditText)findViewById(R.id.register_username);
        register_pass=(EditText)findViewById(R.id.register_pass);
        register_male=(RadioButton)findViewById(R.id.register_male);
        register_confirm=(EditText)findViewById(R.id.register_confirm);
        register_clear=(Button)findViewById(R.id.register_clear);
        register_register=(Button)findViewById(R.id.register_register);

    /*AUTO-COMPLETE VALUES SETTING*/
        register_stream.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,stream));
        register_stream.setThreshold(2);
    }


    /*VALIDATION*/
    private boolean Validation(){
        if(register_name.getText().toString().equals("")){
            register_name.setError("ENTER A VALID NAME");
            register_name.requestFocus();
            return false;
        }
        else if(register_roll.getText().toString().length()!=7||register_roll.getText().toString().equals("")){
            register_roll.setError("ENTER A VALID ROLL NUMBER");
            register_roll.requestFocus();
            return false;
        }
        else if(!(register_stream.getText().toString().equals("MECHANICAL ENGINEERING"))&&!(register_stream.getText().toString().equals("ELECTRICAL ENGINEERING"))&&!(register_stream.getText().toString().equals("CIVIL ENGINEERING"))&&!(register_stream.getText().toString().equals("COMPUTER SCIENCE ENGINEERING"))&&!(register_stream.getText().toString().equals("ELECTRONICS & COMMUNICATION ENGINEERING"))&&!(register_stream.getText().toString().equals("INFORMATION TECHNOLOGY"))){
            register_stream.setError("ENTER A VALID STREAM");
            register_stream.requestFocus();
            return false;
        }
        else if(register_email.getText().toString().equals("")||(!register_email.getText().toString().endsWith(".com"))||(!register_email.getText().toString().contains("@"))) {
            register_email.setError("ENTER A VALID E-MAIL ID");
            register_email.requestFocus();
            return false;
        }
        else if(register_username.getText().toString().equals("")){
            register_username.setError("ENTER A VALID USERNAME");
            register_username.requestFocus();
            return false;
        }
        else if(register_pass.getText().toString().equals("")) {
            register_pass.setError("PASSWORD REQUIRED");
            register_pass.requestFocus();
            return false;
        }
        else if(!(register_confirm.getText().toString().equals(register_pass.getText().toString()))){
            register_pass.setError("PASSWORDS DID NOT MATCH");
            register_pass.setText("");
            register_pass.requestFocus();
            register_confirm.setText("");
            return false;
        }
        return true;
    }



    /*BUTTON ACTIONS*/
    public void ButtonClickedRegister(View v){

        if(v.getId()==R.id.register_clear){
            register_name.setText("");
            register_roll.setText("");
            register_stream.setText("");
            register_email.setText("");
            register_username.setText("");
            register_pass.setText("");
            register_confirm.setText("");
            register_name.requestFocus();
            register_gender.check(R.id.register_male);
        }

        else  if(v.getId()==R.id.register_register){
            if(Validation()){
                AlertDialog.Builder alrt=new AlertDialog.Builder(Register.this);
                alrt.setMessage("Sure Want To Register ?");
                alrt.setNegativeButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        InsertUser();
                    }
                });
                alrt.setPositiveButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alrt.show();
            }
        }
    }


    /*INSERTION OPERATION*/
    private void InsertUser(){
        final String nm=register_name.getText().toString().trim();
        final String roll=register_roll.getText().toString().trim();
        final String stream=register_stream.getText().toString().trim();
        Button b=(Button)findViewById(register_gender.getCheckedRadioButtonId());
        final String gender=b.getText().toString().trim();
        final String email=register_email.getText().toString().trim();
        final String user=register_username.getText().toString().trim();
        final String pass=register_pass.getText().toString().trim();


    /*CALLING WEB SERVICES*/
        class Insert extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading=ProgressDialog.show(Register.this,"Registering ...","Please Wait ...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();

                NotificationCompat.Builder builder=new NotificationCompat.Builder(Register.this);

                if(s.equals("Registration Successful")){
                    builder.setSmallIcon(R.drawable.btn_check_on_holo_light);
                    builder.setContentTitle("Congratulations !!!");
                    builder.setContentText("Registration Successful ");
                    NotificationManager notif=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                    notif.notify(note_id, builder.build());
                    Intent i=new Intent(Register.this,Login.class);
                    startActivity(i);
                    finish();
                }
                else if(s.equals("Could Not Register")){
                    builder.setSmallIcon(R.drawable.btn_close_selected);
                    builder.setContentTitle("Registration Failed !!!");
                    builder.setContentText("Username Already Exists ...");
                    NotificationManager notif=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                    notif.notify(note_id, builder.build());
                }
            }



            /*PUSHING DATA INTO HASHMAP AND THEN TRANSPORTING IT*/
            @Override
            protected String doInBackground(Void... v) {
                HashMap<String,String> params=new HashMap<>();
                params.put(Config.KEY_USER_NAME,nm);
                params.put(Config.KEY_USER_ROLL,roll);
                params.put(Config.KEY_USER_STREAM,stream);
                params.put(Config.KEY_USER_GENDER,gender);
                params.put(Config.KEY_USER_EMAIL,email);
                params.put(Config.KEY_USER_USERNAME,user);
                params.put(Config.KEY_USER_PASSWORD,pass);

                RequestHandler rh=new RequestHandler();
                String res=rh.SendPostRequest(Config.URL_INSERT,params);
                return res;
            }
        }

        Insert i=new Insert();
        i.execute();
    }
}
