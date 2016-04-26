
/* ====================================================================
 * Copyright (c) 2014 Alpha Cephei Inc.  All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY ALPHA CEPHEI INC. ``AS IS'' AND
 * ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL CARNEGIE MELLON UNIVERSITY
 * NOR ITS EMPLOYEES BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * ====================================================================
 */

package edu.cmu.pocketsphinx.demo;

import static android.widget.Toast.makeText;
import static edu.cmu.pocketsphinx.SpeechRecognizerSetup.defaultSetup;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import edu.cmu.pocketsphinx.Assets;
import edu.cmu.pocketsphinx.Hypothesis;
import edu.cmu.pocketsphinx.RecognitionListener;
import edu.cmu.pocketsphinx.SpeechRecognizer;

public class PocketSphinxActivity extends Activity implements
        RecognitionListener {
		
    /* Named searches allow to quickly reconfigure the decoder */
    private static final String KWS_SEARCH = "wakeup";
    private static final String MENU_SEARCH = "menu";
    private static final String CALL_SEARCH = "dial";
    private static final String TURN_SEARCH = "turn";
    private static final String SWITCH_SEARCH = "switch";
    private static final String OPEN_SEARCH = "open";
    private static final String CLOSE_SEARCH = "close";
    private static final String DIR_SEARCH = "direction";
    private static final String MUSIC_SEARCH = "music";
    private static String prevCom = "";
    private static String curCom = "";
    private static int count = 0;
    /* Keyword we are looking for to activate menu */
    private static final String KEYPHRASE = "start voice";
    private static String url = "http://192.168.0.3:8080";
    private Handler aHandler ;
    private SpeechRecognizer recognizer;
    private HashMap<String, Integer> captions;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);

        // Prepare the data for UI
        captions = new HashMap<String, Integer>();
        captions.put(KWS_SEARCH, R.string.kws_caption);
        captions.put(TURN_SEARCH, R.string.turn_caption);
        captions.put(CALL_SEARCH, R.string.call_caption);
        captions.put(SWITCH_SEARCH, R.string.switch_caption);
        captions.put(MENU_SEARCH, R.string.menu_caption);
        captions.put(OPEN_SEARCH, R.string.open_caption);
        captions.put(CLOSE_SEARCH, R.string.close_caption);
        captions.put(DIR_SEARCH, R.string.dir_caption);
        captions.put(MUSIC_SEARCH, R.string.music_caption);

        setContentView(R.layout.main);
        ((TextView) findViewById(R.id.caption_text))
                .setText("Preparing the recognizer");

        // Recognizer initialization is a time-consuming and it involves IO,
        // so we execute it in async task

        new AsyncTask<Void, Void, Exception>() {
            @Override
            protected Exception doInBackground(Void... params) {
                try {

                    Assets assets = new Assets(PocketSphinxActivity.this);
                    File assetDir = assets.syncAssets();
                    setupRecognizer(assetDir);
                } catch (IOException e) {
                    return e;
                }
                return null;
            }

            @Override
            protected void onPostExecute(Exception result) {
                if (result != null) {
                    ((TextView) findViewById(R.id.caption_text))
                            .setText("Failed to init recognizer " + result);
                } else {
                    switchSearch(KWS_SEARCH);
                }
            }
        }.execute();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        recognizer.cancel();
        recognizer.shutdown();
    }
    
    /**
     * In partial result we get quick updates about current hypothesis. In
     * keyword spotting mode we can react here, in other modes we need to wait
     * for final result in onResult.
     */
    @Override
    public void onPartialResult(Hypothesis hypothesis) {
        if (hypothesis == null)
    	    return;
        System.out.println("onPartialResult");
        String text = hypothesis.getHypstr();

        android.util.Log.w(this.getClass().getSimpleName(), "onPartialResult" + text);
        if (text.equals(KEYPHRASE)) {
            switchSearch(MENU_SEARCH);
        }
        else if (text.equals(CALL_SEARCH))
            switchSearch(CALL_SEARCH);
        else if (text.equals(TURN_SEARCH)) {
            switchSearch(TURN_SEARCH);
            prevCom=new String(text);
        }
        else if (text.equals(OPEN_SEARCH)) {
            switchSearch(OPEN_SEARCH);
            prevCom=new String(text);
        }
        else if (text.equals(CLOSE_SEARCH)) {
            switchSearch(CLOSE_SEARCH);
            prevCom=new String(text);
        }
        else if (text.equals(DIR_SEARCH)) {
            switchSearch(DIR_SEARCH);
            prevCom=new String(text);
        }
        else if (text.equals(MUSIC_SEARCH)) {
            switchSearch(MUSIC_SEARCH);
            prevCom=new String(text);
        }
        else
            ((TextView
                    ) findViewById(R.id.result_text)).setText(text);
        curCom = text;
    }

    /**
     * This callback is called when we stop the recognizer.
     */

    public void callFromSkype(String cmd, String text, String name){
        try {
            aHandler = new Handler();
            android.util.Log.w(this.getClass().getSimpleName(), "Constructing URL" +
                    " " + url + " message ");
            JsonRPCClientViaThread names = new JsonRPCClientViaThread(new URL(url),
                    aHandler, this, cmd, "[ \"" + name + "\" ]");
            names.start();
        } catch (Exception ex) {
            android.util.Log.w(this.getClass().getSimpleName(), "Exception constructing URL" +
                    " " + url + " message " + ex.getMessage());

        }
        makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResult(Hypothesis hypothesis) {
        ((TextView) findViewById(R.id.result_text)).setText("");
        String name = null;
        if (hypothesis != null) {
            String text = hypothesis.getHypstr();
            System.out.println("onResult");
            android.util.Log.w(this.getClass().getSimpleName(), "onResult" + text);
            if(text.equals("tom") || text.equals("kevin") || text.equals("john") ) {
                if(text.equals("tom"))
                    name = "harishmalik108";
                else
                    name = "live:dhawal9035";
                String cmd = "callFromSkype";
                callFromSkype(cmd,text,name);
            }
            else if(text.equals("music") || text.equals("bluetooth") || text.equals("maps")){
                if(text.equals("music")){
                    android.util.Log.w(this.getClass().getSimpleName(),"command operated is " + prevCom + " "+ text);
                }
                else if(text.equals("maps")){
                    android.util.Log.w(this.getClass().getSimpleName(),"command operated is " + prevCom + " "+ text);
                }
                else if(text.equals("bluetooth"))
                {
                    android.util.Log.w(this.getClass().getSimpleName(),"command operated is " + prevCom + " "+ text);
                }
            }
            if(text.equals("play") || text.equals("stop") || text.equals("pause") ) {
                android.util.Log.w(this.getClass().getSimpleName(),"command operated is " + prevCom + " "+ text);
            }
            else if((text.contains("to"))){
                String[] str = text.split(" ");
                String source = str[0];
                String destination = str[2];
                String sourceCity = text.substring(0, text.indexOf(" to ") + 1);
                String destinationCity = text.substring(5 + text.indexOf(" of "), text.length());
                android.util.Log.w(this.getClass().getSimpleName(), "source - " + source + "destination " + destination);
                android.util.Log.w(this.getClass().getSimpleName(), "source - " + sourceCity + "destination " + destinationCity);
            }
            }

    }

    @Override
    public void onBeginningOfSpeech() {
    }

    /**
     * We stop recognizer here to get a final result
     */
    @Override
    public void onEndOfSpeech() {
        count++;
        System.out.println(count + "count" );
        if (count != 5){
            count= 0;
            switchSearch(KWS_SEARCH);
        }

        System.out.println("onEnd of Speech");
    }

    public void startListen(View view)
    {
        TextView t = (TextView) findViewById(R.id.curCom);
        String searchName = (String) t.getText();
        android.util.Log.w(this.getClass().getSimpleName(),"start listening by button");
        if (searchName.equals(KWS_SEARCH))
            recognizer.startListening(searchName);
        else
            recognizer.startListening(searchName, 20000);
    }
    private void switchSearch(String searchName) {
        recognizer.stop();
        
        // If we are not spotting, start listening with timeout (10000 ms or 10 seconds).
        System.out.println("Switch Searth");
        String caption = getResources().getString(captions.get(searchName));
        ((TextView) findViewById(R.id.caption_text)).setText(caption);
        TextView t = (TextView) findViewById(R.id.curCom);
        t.setText(searchName);
    }

    private void setupRecognizer(File assetsDir) throws IOException {
        // The recognizer can be configured to perform multiple searches
        // of different kind and switch between them
        
        recognizer = defaultSetup()
                .setAcousticModel(new File(assetsDir, "en-us-ptm"))
                .setDictionary(new File(assetsDir, "cmudict-en-us.dict"))
                
                // To disable logging of raw audio comment out this call (takes a lot of space on the device)
                .setRawLogDir(assetsDir)
                
                // Threshold to tune for keyphrase to balance between false alarms and misses
                .setKeywordThreshold(1e-45f)
                
                // Use context-independent phonetic search, context-dependent is too slow for mobile
                .setBoolean("-allphone_ci", true)
                
                .getRecognizer();
        recognizer.addListener(this);

        /** In your application you might not need to add all those searches.
         * They are added here for demonstration. You can leave just one.
         */

        // Create keyword-activation search.
        recognizer.addKeyphraseSearch(KWS_SEARCH, KEYPHRASE);
        
        // Create grammar-based search for selection between demos
        File menuGrammar = new File(assetsDir, "menu.gram");
        recognizer.addGrammarSearch(MENU_SEARCH, menuGrammar);

        // Create grammar-based search for digit recognition
        File digitsGrammar = new File(assetsDir, "digits.gram");
        recognizer.addGrammarSearch(CALL_SEARCH, digitsGrammar);

        File openGrammar = new File(assetsDir, "open.gram");
        recognizer.addGrammarSearch(OPEN_SEARCH, openGrammar);

        File closeGrammar = new File(assetsDir, "close.gram");
        recognizer.addGrammarSearch(CLOSE_SEARCH, closeGrammar);

        File dirGrammar = new File(assetsDir, "dir.gram");
        recognizer.addGrammarSearch(DIR_SEARCH, dirGrammar);

        File musicGrammar = new File(assetsDir, "music.gram");
        recognizer.addGrammarSearch(MUSIC_SEARCH, musicGrammar);

        /*
        // Create language model search
        File languageModel = new File(assetsDir, "weather.dmp");
        recognizer.addNgramSearch(SWITCH_SEARCH, languageModel);
        */
        // Phonetic search
        File phoneticModel = new File(assetsDir, "turn.gram");
        recognizer.addGrammarSearch(TURN_SEARCH, phoneticModel);
    }

    @Override
    public void onError(Exception error) {
        ((TextView) findViewById(R.id.caption_text)).setText(error.getMessage());
    }

    @Override
    public void onTimeout() {
        switchSearch(KWS_SEARCH);
    }
}
