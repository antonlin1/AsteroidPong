package com.mygdx.game.SpeechRecognizer;

import com.badlogic.gdx.backends.android.AndroidApplication;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;


/**
 * Created by Viktor on 2016-04-27.
 */


// Make this class a service?
public abstract class ListenerActivity extends AndroidApplication implements SpeechControl{

    protected SpeechRecognizer sr;
    protected Context context;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void startListening() {

        initSpeech();
        Intent recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getApplication().getPackageName());
        sr.startListening(recognizerIntent);
    }

    protected void stopListening() {
        if(sr != null) {
            sr.stopListening();
            sr.cancel();
            sr.destroy();
        }
        sr = null;
    }
    protected void initSpeech() {
        if(sr == null) {
            sr = SpeechRecognizer.createSpeechRecognizer(this);

        }
        sr.setRecognitionListener(SpeechListener.getInstance());
    }
    public void finish() {
        if(sr != null) {
            stopListening();
        }
        sr = null;
        super.finish();
    }
    protected void onStop() {
        if(sr != null) {
            stopListening();
        }
        sr = null;
        super.onStop();
    }
    protected void onDestroy() {
        if(sr != null) {
            stopListening();
        }
        sr = null;
        super.onDestroy();
    }

    protected void onPause() {
        if(sr != null) {
            stopListening();
        }
        sr = null;
        super.onPause();
    }

    public abstract void processVoiceCommands(String... voiceCommands);

    public void restartListeningService() {
        stopListening();
        startListening();
    }
}
