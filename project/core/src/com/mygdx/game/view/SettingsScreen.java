package com.mygdx.game.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.controller.AudioManager;

public class SettingsScreen implements Screen {
    private MyGdxGame game;
    private Texture MusicButtonOn, MusicButtonOff;
    private Texture SFXButtonOn, SFXButtonOff;
    private Texture backButtonActive, backButtonInActive;

    public static final int MUSIC_BUTTON_WIDTH = 100;
    public static final int SFX_BUTTON_WIDTH = 100;
    public static final int BACK_BUTTON_WIDTH = 150;
    public static final int BACK_BUTTON_HEIGHT = 75;
    public static final int MUSIC_BUTTON_Y = 450;
    public static final int SFX_BUTTON_Y = 450;
    public static final int BACK_BUTTON_Y = 70;

    public SettingsScreen(MyGdxGame game) {
        this.game = game;
        MusicButtonOn = new Texture("Button/MusicButtonOn.png");
        MusicButtonOff = new Texture("Button/MusicButtonOff.png");
        SFXButtonOn = new Texture("Button/SFXButtonOn.png");
        SFXButtonOff = new Texture("Button/SFXButtonOff.png");
        backButtonActive = new Texture("Button/BackActive.PNG");
        backButtonInActive = new Texture("Button/BackInactive.PNG");
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.15f, 0.15f, 0.3f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();

        // Vẽ nút Back
        int x = MyGdxGame.WIDTH / 2 - BACK_BUTTON_WIDTH / 2;
        if (Gdx.input.getX() < x + BACK_BUTTON_WIDTH && Gdx.input.getX() > x && MyGdxGame.HEIGHT - Gdx.input.getY() < BACK_BUTTON_Y + BACK_BUTTON_HEIGHT && MyGdxGame.HEIGHT - Gdx.input.getY() > BACK_BUTTON_Y) {
            game.batch.draw(backButtonActive, x, BACK_BUTTON_Y, BACK_BUTTON_WIDTH, BACK_BUTTON_HEIGHT);
            if (Gdx.input.justTouched()) {
                game.setScreen(new MainMenuScreen(game));
            }
        } else {
            game.batch.draw(backButtonInActive, x, BACK_BUTTON_Y, BACK_BUTTON_WIDTH, BACK_BUTTON_HEIGHT);
        }

        // Vẽ nút Music
        x = MyGdxGame.WIDTH / 2 - MUSIC_BUTTON_WIDTH / 2 - 100;
        if (Gdx.input.getX() < x + MUSIC_BUTTON_WIDTH && Gdx.input.getX() > x && MyGdxGame.HEIGHT - Gdx.input.getY() < MUSIC_BUTTON_Y + MUSIC_BUTTON_WIDTH && MyGdxGame.HEIGHT - Gdx.input.getY() > MUSIC_BUTTON_Y) {
            game.batch.draw(AudioManager.getInstance().isMusicOn() ? MusicButtonOn : MusicButtonOff, x, MUSIC_BUTTON_Y, MUSIC_BUTTON_WIDTH, MUSIC_BUTTON_WIDTH);
            if (Gdx.input.justTouched()) {
                AudioManager.getInstance().toggleMusic();
            }
        } else {
            game.batch.draw(AudioManager.getInstance().isMusicOn() ? MusicButtonOn : MusicButtonOff, x, MUSIC_BUTTON_Y, MUSIC_BUTTON_WIDTH, MUSIC_BUTTON_WIDTH);
        }

        // Vẽ nút SFX
        x = MyGdxGame.WIDTH / 2 - SFX_BUTTON_WIDTH / 2 + 100;
        if (Gdx.input.getX() < x + SFX_BUTTON_WIDTH && Gdx.input.getX() > x && MyGdxGame.HEIGHT - Gdx.input.getY() < SFX_BUTTON_Y + SFX_BUTTON_WIDTH && MyGdxGame.HEIGHT - Gdx.input.getY() > SFX_BUTTON_Y) {
            game.batch.draw(AudioManager.getInstance().isSFXOn() ? SFXButtonOn : SFXButtonOff, x, SFX_BUTTON_Y, SFX_BUTTON_WIDTH, SFX_BUTTON_WIDTH);
            if (Gdx.input.justTouched()) {
                AudioManager.getInstance().toggleSFX();
            }
        } else {
            game.batch.draw(AudioManager.getInstance().isSFXOn() ? SFXButtonOn : SFXButtonOff, x, SFX_BUTTON_Y, SFX_BUTTON_WIDTH, SFX_BUTTON_WIDTH);
        }

        game.batch.end();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        MusicButtonOn.dispose();
        MusicButtonOff.dispose();
        SFXButtonOn.dispose();
        SFXButtonOff.dispose();
        backButtonActive.dispose();
        backButtonInActive.dispose();
    }
}
