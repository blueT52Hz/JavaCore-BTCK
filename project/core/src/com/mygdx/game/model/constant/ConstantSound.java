package com.mygdx.game.model.constant;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.audio.Music;

public class ConstantSound {
    public static Music bossFightBGM;
    public static Music defeatedBGM;
    public static Music mainBGM;
    public static Music normalFightBGM;

    public static Sound coinEarnedSound;
    public static Sound fireballShootSound;
    public static Sound fireballHitWallSound;
    public static Sound enemyDeadSound;
    public static Sound ninjaDeadSound;
    public static Sound playerTeleportSound;
    public static Sound kunaiThrowSound;
    public static Sound kunaiHitWallSound;

    // Load all sound resources
    public static void load() {
        bossFightBGM = Gdx.audio.newMusic(Gdx.files.internal(Constants.BOSS_FIGHT_BGM_PATH));
        defeatedBGM = Gdx.audio.newMusic(Gdx.files.internal(Constants.DEFEATED_BGM_PATH));
        mainBGM = Gdx.audio.newMusic(Gdx.files.internal(Constants.MAIN_BGM_PATH));
        normalFightBGM = Gdx.audio.newMusic(Gdx.files.internal(Constants.NORMAL_FIGHT_BGM_PATH));

        coinEarnedSound = Gdx.audio.newSound(Gdx.files.internal(Constants.COIN_EARNED_SFX_PATH));
        fireballShootSound = Gdx.audio.newSound(Gdx.files.internal(Constants.FIREBALL_SHOOT_SFX_PATH));
        fireballHitWallSound = Gdx.audio.newSound(Gdx.files.internal(Constants.FIREBALL_HIT_SFX_PATH));
        enemyDeadSound = Gdx.audio.newSound(Gdx.files.internal(Constants.ENEMY_DEAD_SFX_PATH));
        ninjaDeadSound = Gdx.audio.newSound(Gdx.files.internal(Constants.NINJA_DEAD_SFX_PATH));
        playerTeleportSound = Gdx.audio.newSound(Gdx.files.internal(Constants.NINJA_FLASH_SFX_PATH));
        kunaiThrowSound = Gdx.audio.newSound(Gdx.files.internal(Constants.KUNAI_THROW_SFX_PATH));
        kunaiHitWallSound = Gdx.audio.newSound(Gdx.files.internal(Constants.KUNAI_HIT_SFX_PATH));
    }

    // Dispose all sound resources when they are no longer needed
    public static void dispose() {
        bossFightBGM.dispose();
        defeatedBGM.dispose();
        mainBGM.dispose();
        normalFightBGM.dispose();
        coinEarnedSound.dispose();
        fireballShootSound.dispose();
        fireballHitWallSound.dispose();
        enemyDeadSound.dispose();
        ninjaDeadSound.dispose();
        playerTeleportSound.dispose();
        kunaiThrowSound.dispose();
        kunaiHitWallSound.dispose();
    }
}
