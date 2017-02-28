package com.example.prashant_tripathi.chatterbox;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Prashant_Tripathi on 28-09-2016.
 */
public class RequestHandler {
    public String SendPostRequest(String requestURL,HashMap<String,String> postDataParams){
        URL url;
        StringBuilder sb=new StringBuilder();
        try{
            url=new URL(requestURL);

            HttpURLConnection conn=(HttpURLConnection)url.openConnection();

            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream os=conn.getOutputStream();

            BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));
            writer.write(GetPostDataString(postDataParams));

            writer.flush();
            writer.close();
            os.close();
            int responseCode=conn.getResponseCode();

            if(responseCode==HttpURLConnection.HTTP_OK){
                BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                sb=new StringBuilder();
                String response;
                while ((response = br.readLine()) != null) {
                    sb.append(response);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return sb.toString();
    }


    public String SendGetRequest(String requestURL){
        StringBuilder sb=new StringBuilder();
        try{
            URL url=new URL(requestURL);
            HttpURLConnection con=(HttpURLConnection)url.openConnection();
            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(con.getInputStream()));

            String s;
            while((s=bufferedReader.readLine())!=null){
                sb.append(s+"\n");
            }
        }catch (Exception e){
        }
        return sb.toString();
    }

    //Load Messages
    public String SendGetRequestMessage(String requestURL,String sender,String receiver){
        StringBuilder sb=new StringBuilder();
        try{
            URL url=new URL(requestURL+"%27"+sender+"%27"+"&receiver=%27"+receiver+"%27");
            HttpURLConnection con=(HttpURLConnection)url.openConnection();
            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(con.getInputStream()));

            String s;
            while((s=bufferedReader.readLine())!=null){
                sb.append(s+"\n");
            }
        }catch (Exception e){
        }
        return sb.toString();
    }
//

    public String SendGetRequestParam(String requestURL,String user ){
        StringBuilder sb=new StringBuilder();
        try{
            URL url=new URL(requestURL+"%27"+user+"%27");
            HttpURLConnection con=(HttpURLConnection)url.openConnection();
            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(con.getInputStream()));

            String s;
            while((s=bufferedReader.readLine())!=null){
                sb.append(s+"\n");
            }
        }catch (Exception e){
        }
        return sb.toString();
    }

    private  String GetPostDataString(HashMap<String,String> params) throws UnsupportedEncodingException {
        StringBuilder result=new StringBuilder();
        boolean first=true;
        for(Map.Entry<String,String> entry:params.entrySet()){
            if(first)
                first=false;
            else
                result.append("&");
            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(),"UTF-8"));
        }
        return result.toString();
    }
}
