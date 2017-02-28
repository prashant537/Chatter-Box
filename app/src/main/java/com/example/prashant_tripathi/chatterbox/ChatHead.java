package com.example.prashant_tripathi.chatterbox;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Prashant_Tripathi on 28-09-2016.
 */
public class ChatHead extends AppCompatActivity {
    String sender,receiver,snd,rec,msg;
    private ListView messages,all_messages;
    EditText chathead_message;
    SharedPreferences share;
    private String JSON_STRING;
    private ListAdapter adapter1,adapter2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatlayout);
        chathead_message=(EditText)findViewById(R.id.chat_message);
        messages=(ListView)findViewById(R.id.chathead_messages);
        all_messages=(ListView)findViewById(R.id.chathead_messages);
        Bundle b=getIntent().getExtras();
        sender=b.getString("Sender");
        receiver=b.getString("Receiver");
        System.out.println("Hello"+sender+receiver);
        LoadMessages load=new LoadMessages();
        load.execute();
        messages.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder delete=new AlertDialog.Builder(ChatHead.this);
                delete.setTitle("Delete Message");
                delete.setMessage("Are You Sure Want To Delete This Message ?");
                delete.setNegativeButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Delete Message from Chat and then call load.execute()
                    }
                });
                delete.setPositiveButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                delete.show();
                return false;
            }
        });
         /*   @Override
            public boolean onLongClick(View v) {
                final AlertDialog.Builder delete=new AlertDialog.Builder(ChatHead.this);
                delete.setTitle("Delete Message");
                delete.setMessage("Are You Sure Want To Delete This Message ?");
                delete.setNegativeButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                delete.setPositiveButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                delete.show();
                return false;
            }
        });*/

    }

    class LoadMessages extends AsyncTask<Void,Void,String> {
        ProgressDialog loading;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading=ProgressDialog.show(ChatHead.this,"Loading Messages ...","Please Wait ...",false,false);
        }
        //Start
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            loading.dismiss();
            JSON_STRING=s;
            ShowMessages();
        }

        @Override
        protected String doInBackground(Void... params) {
            RequestHandler rh=new RequestHandler();
            String s=rh.SendGetRequestMessage(Config.URL_LOAD_MESSAGES, sender, receiver);
            return s;
        }
    }

    private void ShowMessages(){
        JSONObject jsonObject=null;
        ArrayList<HashMap<String,String>> listmessages=new ArrayList<HashMap<String, String>>();
        try{
            jsonObject=new JSONObject(JSON_STRING);
            JSONArray result=jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);

            for(int i=0;i<result.length();i++){
                //   String stream=null;
                JSONObject jo=result.getJSONObject(i);
                msg=jo.getString(Config.TAG_MESSAGE);
                rec=jo.getString(Config.TAG_RECEIVER);
                snd=jo.getString(Config.TAG_SENDER);
                //
                System.out.println(snd+rec);
                if(snd==receiver) {
                    adapter1 = new SimpleAdapter(ChatHead.this,listmessages, R.layout.rightmessage, new String[]{Config.TAG_MESSAGE}, new int[]{R.id.chat_message_right});
                    messages.setAdapter(adapter1);
                }
                else {
                    adapter2 = new SimpleAdapter(ChatHead.this,listmessages, R.layout.leftmessage, new String[]{Config.TAG_MESSAGE}, new int[]{R.id.chat_message_left});
                    all_messages.setAdapter(adapter2);
                }

                HashMap<String,String> all=new HashMap<>();
                all.put(Config.TAG_MESSAGE, msg);
                listmessages.add(all);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }


    }
    //End
    public void ButtonClickedSendMessage(View v){
        if(chathead_message.getText().toString().equals("")){
            Toast.makeText(ChatHead.this, "Write something ...", Toast.LENGTH_LONG).show();
        }

        else {
            final String message=chathead_message.getText().toString();
            //  System.out.println(message+receiver);
                 /*final String roll=register_roll.getText().toString().trim();
                 final String stream=register_stream.getText().toString().trim();
                 Button b=(Button)findViewById(register_gender.getCheckedRadioButtonId());
                 final String gender=b.getText().toString().trim();
                 final String email=register_email.getText().toString().trim();
                 final String user=register_username.getText().toString().trim();
                 final String pass=register_pass.getText().toString().trim();*/


    /*CALLING WEB SERVICES*/
            class SendMessage extends AsyncTask<Void,Void,String> {
                ProgressDialog loading;

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    loading=ProgressDialog.show(ChatHead.this,"Sending Message ...","Please Wait ...",false,false);
                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    loading.dismiss();
                    if(s.equals("Message Sent")){
                        chathead_message.setText("");
                        Toast.makeText(ChatHead.this,"Message Sent",Toast.LENGTH_LONG).show();
                        LoadMessages load=new LoadMessages();
                        load.execute();

                    }
                    else if(s.equals("Could Not Send Message")){
                        Toast.makeText(ChatHead.this,"Message Could Not Be Sent",Toast.LENGTH_LONG).show();
                    }
                }



                /*PUSHING DATA INTO HASHMAP AND THEN TRANSPORTING IT*/
                @Override
                protected String doInBackground(Void... v) {
                    HashMap<String,String> msg=new HashMap<>();
                    msg.put(Config.KEY_SENDER_NAME,sender);
                    msg.put(Config.KEY_RECEIVER_NAME,receiver);
                    msg.put(Config.KEY_MESSAGE,message);
                    RequestHandler rh=new RequestHandler();
                    String res=rh.SendPostRequest(Config.URL_MESSAGE,msg);
                    return res;
                }
            }

            SendMessage s=new SendMessage();
            s.execute();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.chatroom_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.chatroom_logout:
                share=getSharedPreferences("Prashant",MODE_PRIVATE);
                SharedPreferences.Editor ed=share.edit();
                ed.remove("user");
                ed.remove("pass");
                ed.commit();
                Intent i=new Intent(getApplicationContext(),Login.class);
                startActivity(i);
                finish();
                break;
        }
        return true;
    }
}
