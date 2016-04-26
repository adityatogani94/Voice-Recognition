package edu.cmu.pocketsphinx.demo;

import org.json.JSONObject;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import android.os.Handler;
import android.util.Log;
import org.json.JSONObject;

/**
 * Created by Prafull on 4/25/2016.
 */
public class JsonRPCClientViaThread extends Thread{
    private final Map<String, String> headers;
    private URL url;
    private String method;
    private String requestData;
    private Handler handler;
    private PocketSphinxActivity parent;

    public JsonRPCClientViaThread(URL url, Handler handler, PocketSphinxActivity parent, String method, String parmsArray) {
        this.url = url;
        this.parent = parent;
        this.method = method;
        this.handler = handler;
        this.headers = new HashMap<String, String>();
        Log.d("url", String.valueOf(url));
        requestData = "{ \"jsonrpc\":\"2.0\", \"method\":\""+method+"\", \"params\":"+parmsArray+
                ",\"id\":3}";
    }

    public void run(){
        try {
            String respData = this.post(url, headers, requestData);
            android.util.Log.d(this.getClass().getSimpleName(),"Result of JsonRPC request: "+respData);
            if(method.equals("getGenreBasedMovieList")){

            }else if(method.equals("get")){

            }
            else if(method.equals("remove")){
                JSONObject jo = new JSONObject(respData);
                if(jo.toString() == "true"){
                    Log.d("Delete operation:", "true");
                }
                else {
                    Log.d("Delete operation:", "false");
                }
            }
            else if(method.equals("add")){
                JSONObject jo = new JSONObject(respData);
                if(jo.toString() == "true"){
                    Log.d("Add operation:", "true");
                }
                else {
                    Log.d("Add operation:", "false");
                }
            }
        }catch (Exception ex){


        }
    }

    public void setHeader(String key, String value) {
        this.headers.put(key, value);
    }

    public String call(String requestData) throws Exception {
        String respData = post(url, headers, requestData);
        return respData;
    }

    private String post(URL url, Map<String, String> headers, String data) throws Exception {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                connection.addRequestProperty(entry.getKey(), entry.getValue());
            }
        }
        connection.addRequestProperty("Accept-Encoding", "gzip");
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.connect();
        OutputStream out = null;
        try {
            out = connection.getOutputStream();
            out.write(data.getBytes());
            out.flush();
            out.close();
            int statusCode = connection.getResponseCode();
            if (statusCode != HttpURLConnection.HTTP_OK) {
                throw new Exception(
                        "Unexpected status from post: " + statusCode);
            }
        } finally {
            if (out != null) {
                out.close();
            }
        }
        String responseEncoding = connection.getHeaderField("Content-Encoding");
        responseEncoding = (responseEncoding == null ? "" : responseEncoding.trim());
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        InputStream in = connection.getInputStream();
        try {
            in = connection.getInputStream();
            if ("gzip".equalsIgnoreCase(responseEncoding)) {
                in = new GZIPInputStream(in);
            }
            in = new BufferedInputStream(in);
            byte[] buff = new byte[1024];
            int n;
            while ((n = in.read(buff)) > 0) {
                bos.write(buff, 0, n);
            }
            bos.flush();
            bos.close();
        } finally {
            if (in != null) {
                in.close();
            }
        }
        android.util.Log.d(this.getClass().getSimpleName(), "json rpc request via http returned string " + bos.toString());
        return bos.toString();
    }

}
