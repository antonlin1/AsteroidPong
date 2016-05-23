package com.mygdx.game.SpeechRecognizer;

import android.media.AudioManager;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.util.Log;

import com.mygdx.game.AndroidLauncher;

import java.util.ArrayList;

/**
 * Created by Viktor on 2016-04-27.
 */
public class SpeechListener implements RecognitionListener {

    public static SpeechListener instance = null;

    SpeechControl listener;

    private AudioHandler audioHandler;

    public static SpeechListener getInstance() {
        if(instance == null) {
            instance = new SpeechListener();
        }
        return instance;
    }
    private SpeechListener() {
        audioHandler = AudioHandler.getInstance2();

    }

    public void setListener(SpeechControl listener) {
        this.listener = listener;
    }

    public void processVoiceCommands(String... voiceCommands) {
        listener.processVoiceCommands(voiceCommands);
    }

    @Override
    public void onResults(Bundle results) {
        ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        String[] commands = new String[matches.size()];
        for(String command : matches) {
            Log.d("prelTAG", command);
        }
        commands = matches.toArray(commands);
        processVoiceCommands(commands);
    }

    @Override
    public void onReadyForSpeech(Bundle params) {
        audioHandler.enableAudio();
        Log.d("prelTAG", "onReadyForSpeech");
    }

    @Override
    public void onBeginningOfSpeech() {
        Log.d("prelTAG", "onBeginningOfSpeech");
    }

    @Override
    public void onRmsChanged(float rmsdB) {

    }

    @Override
    public void onBufferReceived(byte[] buffer) {

    }

    @Override
    public void onEndOfSpeech() {

    }

    @Override
    public void onError(int error) {
        Log.d("prelTAG", "error " + error);
        if(listener != null) {
            listener.restartListeningService();
            audioHandler.disableAudio();
        }
    }


    @Override
    public void onPartialResults(Bundle partialResults) {

    }

    @Override
    public void onEvent(int eventType, Bundle params) {

    }


}