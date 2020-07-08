package com.appnewspaper.utils.network;

import com.appnewspaper.utils.network.exceptions.AuthenticationError;
import com.appnewspaper.utils.network.exceptions.ServerCommunicationError;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ServiceCallUtils {

    public static String parseHttpStreamResult(HttpURLConnection connection) throws UnsupportedEncodingException, IOException {
        String res = "";
        BufferedReader br = new BufferedReader(new InputStreamReader(
                connection.getInputStream(),"utf-8"));
        String line = null;
        while ((line = br.readLine()) != null) {
            res += line + "\n";

        }
        br.close();
        return res;
    }

    @SuppressWarnings("unchecked")
    public static int readRestResultFromInsert(String res) throws ParseException, ServerCommunicationError {
        Object o = JSONValue.parseWithException(res);
        if (o instanceof JSONObject){
            JSONObject jsonResult = (JSONObject) JSONValue.parseWithException(res);
            Set<String> keys = jsonResult.keySet();
            if (keys.contains("id"))
                return Integer.parseInt((String) jsonResult.get("id"));
            else{
                throw new ServerCommunicationError("Error: No id in json returned");
            }
        }else{
            throw new ServerCommunicationError("Error: No json returned");
        }
    }

    @SuppressWarnings("unchecked")
    public static JSONObject readRestResultFromGetObject(String res) throws ParseException, ServerCommunicationError {
        Object o = JSONValue.parseWithException(res);
        if (o instanceof JSONObject){
            JSONObject jsonResult = (JSONObject) JSONValue.parseWithException(res);
            return jsonResult;
        }else{
            throw new ServerCommunicationError("Error: No json returned");
        }
    }

    public static List<JSONObject> readRestResultFromList(String res) throws AuthenticationError {
        List<JSONObject> result = new ArrayList<JSONObject>() ;
        try {
            Object o = JSONValue.parseWithException(res);
            if (o instanceof JSONObject){
                JSONObject jsonResult = (JSONObject) JSONValue.parseWithException(res);
                @SuppressWarnings("unchecked")
                Set<Object> keys = jsonResult.keySet();
                for (Object keyRow : keys) {
                    JSONObject jsonObj = (JSONObject)jsonResult.get(keyRow);
                    result.add(jsonObj);
                }
            } else if (o instanceof JSONArray){
                JSONArray jsonArray = (JSONArray) JSONValue.parseWithException(res);
                for (Object row : jsonArray) {
                    JSONObject jsonObj = (JSONObject)row;
                    result.add(jsonObj);
                }
            } else{
                throw new AuthenticationError("Result is not an Json Array nor Object");
            }
        } catch (ParseException e) {
            throw new AuthenticationError(e.getMessage());
        }
        return result;
    }

    public static JSONObject readRestResultFromSingle(String res) throws ParseException {
        return (JSONObject) JSONValue.parseWithException(res);
    }

    public static void writeJSONParams(HttpURLConnection connection, JSONObject json) throws IOException{
        // Send POST output.

        DataOutputStream wr = new DataOutputStream(connection.getOutputStream ());
        wr.writeBytes(json.toJSONString());//(URLEncoder.encode(json.toJSONString(),"UTF-8"));
        wr.flush();
        wr.close();
    }


}
