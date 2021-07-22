package com.learning.test;

import org.json.simple.JSONObject;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import java.io.IOException;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import org.json.simple.parser.JSONParser;

public class CRUD_ops {
    OkHttpClient client = new OkHttpClient();
    // code request code here
    String doGetRequest(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    // post request code here

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    // test data
    String bowlingJson(String player1, String player2) {
        return "{'deva':{'HIGH_SCORE',"
                + "'name':'Bowling',"
                + "'round':4,"
                + "'lastSaved':1367702411696,"
                + "'dateStarted':1367702378785,"
                + "'players':["
                + "{'name':'" + player1 + "','history':[10,8,6,7,8],'color':-13388315,'total':39},"
                + "{'name':'" + player2 + "','history':[6,10,5,10,10],'color':-48060,'total':41}"
                + "]}";
    }

    String doPostRequest(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }
    String doDelRequest(String url, String json) throws IOException {
        //RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .delete()
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    String doPutRequest(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .put(body)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public static void main(String[] args) throws IOException {

        // issue the Get request
        CRUD_ops example = new CRUD_ops();
        String getResponse = example.doGetRequest("https://test-76a97-default-rtdb.asia-southeast1.firebasedatabase.app/test-76a97-default-rtdb/users.json");
        System.out.println(getResponse);

       // JSONObject json = (JSONObject)new JSONParser().parse("{\"application\":\"admin\",\"keytype\":\"PRODUCTION\",\"callbackUrl\":\"qwerewqr;ewqrwerq;qwerqwerq\",\"authorizedDomains\":\"ALL\",\"validityTime\":\"3600000\",\"retryAfterFailure\":true}");
        String putobj = "{\r\n" + " \"firstName\" : \"Ram\",\r\n" + " \"lastName\" : \"Fadatare\",\r\n" + " \"emailId\" : \"ram@gmail.com\"\r\n" + "}";

        String postResponse = example.doPostRequest("https://test-76a97-default-rtdb.asia-southeast1.firebasedatabase.app/test-76a97-default-rtdb/users.json", putobj);

        System.out.println(postResponse);

        String delres = example.doDelRequest("https://test-76a97-default-rtdb.asia-southeast1.firebasedatabase.app/test-76a97-default-rtdb/users/-MeSxGwCOhpBXZ3AP-V_.json","") ;

        System.out.println(delres);

        String putobj1 = "{\r\n" + " \"firstName\" : \"Deva\"\r\n" + "}";
        String putres = example.doPutRequest("https://test-76a97-default-rtdb.asia-southeast1.firebasedatabase.app/test-76a97-default-rtdb/users/-MeTHKgXcEO2YX9eScGa.json",putobj1) ;
        System.out.println(putres);
    }
}
