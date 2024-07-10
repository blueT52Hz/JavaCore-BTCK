package com.mygdx.game.controller;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.mygdx.game.model.constant.ConstantSound;

public class AudioManager {

    private static AudioManager instance = null;
    private boolean isMusicOn = true;
    private boolean isSFXOn = true;

    private AudioManager() {
        // Private constructor to prevent instantiation
    }

    public static AudioManager getInstance() {
        if (instance == null) {
            instance = new AudioManager();
        }
        return instance;
    }

    public boolean isMusicOn() {
        return isMusicOn;
    }

    public void toggleMusic() {
        isMusicOn = !isMusicOn;
        float musicvolume = isMusicOn ? 1.0f : 0.0f;

        if (ConstantSound.bossFightBGM != null) ConstantSound.bossFightBGM.setVolume(musicvolume);
        if (ConstantSound.defeatedBGM != null) ConstantSound.defeatedBGM.setVolume(musicvolume);
        if (ConstantSound.mainBGM != null) ConstantSound.mainBGM.setVolume(musicvolume);
        if (ConstantSound.normalFightBGM != null) ConstantSound.normalFightBGM.setVolume(musicvolume);
    }

    public boolean isSFXOn() {
        return isSFXOn;
    }

    public void toggleSFX() {
        isSFXOn = !isSFXOn;

        // Thiết lập âm lượng cho từng âm thanh SFX
        float sfxVolume = isSFXOn ? 1.0f : 0.0f;

        // Danh sách các âm thanh SFX cần xử lý
        Sound[] sfxSounds = {
                ConstantSound.coinEarnedSound,
                ConstantSound.fireballShootSound,
                ConstantSound.fireballHitWallSound,
                ConstantSound.enemyDeadSound,
                ConstantSound.ninjaDeadSound,
                ConstantSound.playerTeleportSound,
                ConstantSound.kunaiThrowSound,
                ConstantSound.kunaiHitWallSound
        };

        // Thiết lập âm lượng cho từng âm thanh SFX
        for (Sound sound : sfxSounds) {
            if (sound != null) {
                sound.setVolume(sound.play(), sfxVolume); // Thiết lập âm lượng
            }
        }
    }



    public void playSound(Sound sound) {
        if (isSFXOn && sound != null) {
            sound.play();
        }
    }

    public void playMusic(Music music) {
        if (isMusicOn && music != null) {
            music.setLooping(true);
            music.play();
        }
    }

    public void stopMusic(Music music) {
        if (music != null) {
            music.stop();
        }
    }

    public void dispose() {
        // Dispose music resources
        ConstantSound.dispose();
    }
}