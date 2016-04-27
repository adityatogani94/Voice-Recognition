package com.inferno;


import org.json.JSONObject;
import org.json.JSONArray;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class CollectionSkeleton extends Object {

    SRSCollection sLib;

    public CollectionSkeleton (SRSCollection sLib){
        this.sLib = sLib;
    }

    public String callMethod(String request){
        JSONObject result = new JSONObject();
        try{
            JSONObject theCall = new JSONObject(request);
            String method = theCall.getString("method");
            int id = theCall.getInt("id");
            JSONArray params = null;
            if(!theCall.isNull("params")){
                params = theCall.getJSONArray("params");
            }
            result.put("id",id);
            result.put("jsonrpc","2.0");
            if(method.equals("callFromSkype")){
                String command = params.getString(0);
                boolean executed = sLib.callFromSkype(command);
                result.put("result",executed);
            }else if(method.equals("bluetoothon")){
                boolean executed = sLib.bluetoothon();
                result.put("result",executed);
            }else if(method.equals("bluetoothoff")){
                boolean executed = sLib.bluetoothoff();
                result.put("result",executed);
            }else if(method.equals("getCarStatus")) {
                boolean executed = sLib.getCarStatus();
                result.put("result", executed);
            }
            else if(method.equals("openMapsAndDirections")){
                String command = params.getString(0);
                boolean executed = sLib.openMapsAndDirections(command);
                result.put("result",executed);
            }
            else if(method.equals("getFuelStations")){
                boolean executed = sLib.getFuelStations();
                result.put("result",executed);
            }
            else if(method.equals("getRestaurants")){
                boolean executed = sLib.getRestaurants();
                result.put("result",executed);
            }
            else if(method.equals("playMusic")){
                boolean executed = sLib.playMusic();
                result.put("result",executed);
            }
            else if(method.equals("closeMusic")){
                boolean executed = sLib.closeMusic();
                result.put("result",executed);
            }
            else if(method.equals("getHotels")){
                boolean executed = sLib.getHotels();
                result.put("result",executed);
            }
            else if(method.equals("connected")){
                boolean executed = sLib.connected();
                result.put("result",executed);
            }


        }catch(Exception ex){
            System.out.println("exception in callMethod: "+ex.getMessage());
        }
        System.out.println("returning: "+result.toString());
        return "HTTP/1.0 200 Data follows\nServer:localhost:8080\nContent-Type:text/plain\nContent-Length:"+(result.toString()).length()+"\n\n"+result.toString();
    }
}

