package com.mygdx.game.SpeechRecognizer;

import android.media.AudioManager;

/**
 * Created by Viktor on 2016-05-23.
 */
public class AudioHandler {
    private AudioManager audioManager;
    private int volume;

    public static AudioHandler instance = null;

    public static AudioHandler getInstance(AudioManager audioManager) {
        if(instance == null) {
            instance = new AudioHandler(audioManager);
        }
        return instance;
    }
    public static AudioHandler getInstance2() {
        return instance;
    }

    public AudioHandler(AudioManager audioManager) {
        this.audioManager = audioManager;
        volume = 0;
    }
    public void enableAudio() {
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);
    }
    public void disableAudio() {
        if(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) != 0) {
            volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
        }
    }


}
