package com.mygdx.game.SpeechRecognizer;

/**
 * Created by Viktor on 2016-04-27.
 */
public interface SpeechControl {

    public abstract void processVoiceCommands(String... voiceCommands);
    // Executed when voice command found

    public void restartListeningService();
    // Used to keep recognition activated
}
