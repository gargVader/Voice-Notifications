package com.example.notificationstutorial;

import android.app.Application;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.util.Locale;

import static com.example.notificationstutorial.MainActivity.TAG;

public class AudioPlayer {

    private static AudioPlayer instance;
    private static TextToSpeech TTS = null;
    private Application application;

    public static AudioPlayer getInstance() {
        if (instance == null) {
            instance = new AudioPlayer();
        }
        return instance;
    }

    private AudioPlayer() {

    }

    public void prepareAudioPlayer(Application application) {
        this.application = application;
        TTS = new TextToSpeech(application, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    if (safelySetLanguage("en")) {
                        TTS.setSpeechRate(0.9f);
                    } else {
                        Log.e(TAG, "onInit: Language could not be set");
                    }
                } else {
                    Log.e(TAG, "onInit: Error in initialising TTS status=" + status);
                }
            }
        });
    }

    private boolean safelySetLanguage(String ISO) {
        int result = TTS.setLanguage(Locale.forLanguageTag(ISO));
        if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
            Log.e(TAG, "safelySetLanguage: Language not supported");
            return false;
        } else {
            return true;
        }
    }

    public void play(String text) {
        Log.d(TAG, "play: " + text);
        TTS.stop();
        playHelper(text);
    }

    private void playHelper(String text) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            TTS.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        } else {
            TTS.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }
    }


}
