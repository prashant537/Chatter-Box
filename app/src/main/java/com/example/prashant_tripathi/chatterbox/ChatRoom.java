package com.example.prashant_tripathi.chatterbox;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Prashant_Tripathi on 28-09-2016.
 */
public class ChatRoom extends AppCompatActivity {
    private ListView lv;
    private String JSON_STRING;
    SharedPreferences share;
    String usr,receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatroom);
        Bundle b=getIntent().getExtras();
        usr=b.getString("Username");
        lv=(ListView)findViewById(R.id.chatroom_all_users);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i=new Intent(ChatRoom.this,ChatHead.class);
                HashMap<String,String> map=(HashMap)parent.getItemAtPosition(position);
                receiver=map.get(Config.TAG_RECEIVER).toString();
                i.putExtra("Sender",usr);
                i.putExtra("Receiver",receiver);
                // System.out.println("Hello "+receiver+usr);
                startActivity(i);
            }
        });

        GetJson();
    }


    private void ShowAllUsers(){
        JSONObject jsonObject=null;
        ArrayList<HashMap<String,String>> list=new ArrayList<HashMap<String, String>>();
        try{
            jsonObject=new JSONObject(JSON_STRING);
            JSONArray result=jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);

            for(int i=0;i<result.length();i++){
                String stream=null;
                JSONObject jo=result.getJSONObject(i);
                String name=jo.getString(Config.TAG_NAME);
                String ss=jo.getString(Config.TAG_STREAM);
                if(ss.equals("MECHANICAL ENGINEERING")){
                    stream="ME";
                }
                else if(ss.equals("ELECTRICAL ENGINEERING")){
                    stream="EE";
                }
                else if(ss.equals("CIVIL ENGINEERING")){
                    stream="CE";
                }
                else if(ss.equals("ELECTRONICS & COMMUNICATION ENGINEERING")){
                    stream="ECE";
                }
                else if(ss.equals("COMPUTER SCIENCE ENGINEERING")){
                    stream="CSE";
                }
                else if(ss.equals("INFORMATION TECHNOLOGY")){
                    stream="IT";
                }
                String roll=jo.getString(Config.TAG_ROLL);
                String rec=jo.getString(Config.TAG_RECEIVER);
                HashMap<String,String> all=new HashMap<>();
                all.put(Config.TAG_NAME,name);
                all.put(Config.TAG_STREAM, stream);
                all.put(Config.TAG_ROLL,roll);
                all.put(Config.TAG_RECEIVER,rec);
                list.add(all);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }

        ListAdapter adapter=new SimpleAdapter(ChatRoom.this,list,R.layout.chatroomlayout,new String[]{Config.TAG_NAME,Config.TAG_STREAM,Config.TAG_ROLL},new int[]{R.id.chatroom_name,R.id.chatroom_stream,R.id.chatroom_roll});
        lv.setAdapter(adapter);
    }


    private void GetJson(){
        class ReceiveJson extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading=ProgressDialog.show(ChatRoom.this,"Populating Chatroom ...","Please Wait ...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING=s;
                ShowAllUsers();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh=new RequestHandler();
                String s=rh.SendGetRequestParam(Config.URL_ALL_USERS,usr );
                return s;
            }
        }

        ReceiveJson rj=new ReceiveJson();
        rj.execute();
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
