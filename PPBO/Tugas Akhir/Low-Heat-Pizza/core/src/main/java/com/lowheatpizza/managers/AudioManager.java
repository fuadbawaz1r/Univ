package com.lowheatpizza.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;

public class AudioManager {
    private Music currentMusic;
    private String currentTrackPath;
    private boolean enabled = true;
    private final com.badlogic.gdx.utils.ObjectMap<String, com.badlogic.gdx.audio.Sound> cachedSounds = new com.badlogic.gdx.utils.ObjectMap<>();

    public void playLoop(String internalPath, float volume) {
        if (!enabled) {
            return;
        }
        if (internalPath.equals(currentTrackPath) && currentMusic != null) {
            if (!currentMusic.isPlaying()) {
                currentMusic.play();
            }
            currentMusic.setVolume(volume);
            return;
        }

        stopCurrent();
        FileHandle file = Gdx.files.internal(internalPath);
        if (!file.exists()) {
            Gdx.app.log("AudioManager", "Missing audio file: " + internalPath);
            return;
        }

        try {
            currentMusic = Gdx.audio.newMusic(file);
            currentMusic.setLooping(true);
            currentMusic.setVolume(volume);
            currentMusic.play();
            currentTrackPath = internalPath;
        } catch (Exception e) {
            Gdx.app.error("AudioManager", "Error playing music: " + internalPath, e);
            currentMusic = null;
            currentTrackPath = null;
        }
    }

    public void playSound(String internalPath, float volume) {
        if (!enabled) {
            return;
        }
        com.badlogic.gdx.audio.Sound sound = cachedSounds.get(internalPath);
        if (sound == null) {
            FileHandle file = Gdx.files.internal(internalPath);
            if (!file.exists()) {
                Gdx.app.log("AudioManager", "Missing sound file: " + internalPath);
                return;
            }
            try {
                sound = Gdx.audio.newSound(file);
                cachedSounds.put(internalPath, sound);
            } catch (Exception e) {
                Gdx.app.error("AudioManager", "Error loading sound: " + internalPath, e);
                return;
            }
        }
        sound.play(volume);
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (!enabled) {
            stopCurrent();
        } else if (currentMusic != null) {
            currentMusic.play();
        }
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void dispose() {
        stopCurrent();
        for (com.badlogic.gdx.audio.Sound sound : cachedSounds.values()) {
            sound.dispose();
        }
        cachedSounds.clear();
    }

    private void stopCurrent() {
        if (currentMusic != null) {
            currentMusic.stop();
            currentMusic.dispose();
            currentMusic = null;
        }
        currentTrackPath = null;
    }
}

