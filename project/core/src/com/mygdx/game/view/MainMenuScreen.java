package com.mygdx.game.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.MyGdxGame;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;


import com.mygdx.game.model.constant.ConstantSound;

public class MainMenuScreen implements Screen {
    private static final int BACK_BUTTON_WIDTH = 40;
    private static final int BACK_BUTTON_HEIGHT = 40;
    private static final int NEWGAME_BUTTON_WIDTH = 320;
    private static final int NEWGAME_BUTTON_HEIGHT = 80;
    private static final int CONTINUE_BUTTON_WIDTH = 240;
    private static final int CONTINUE_BUTTON_HEIGHT = 60;
    private static final int HIGHSCORE_BUTTON_WIDTH = 240;
    private static final int HIGHSCORE_BUTTON_HEIGHT = 60;
    private static final int SETTINGS_BUTTON_WIDTH = 240;
    private static final int SETTINGS_BUTTON_HEIGHT = 60;
    private static final int STORE_BUTTON_WIDTH = 240;
    private static final int STORE_BUTTON_HEIGHT = 60;

    private static final int BACK_BUTTON_Y = 670;
    private static final int NEWGAME_BUTTON_Y = 350;
    private static final int CONTINUE_BUTTON_Y = 255;
    private static final int HIGHSCORE_BUTTON_Y = 180;
    private static final int SETTINGS_BUTTON_Y = 105;
    private static final int STORE_BUTTON_Y = 30;

    private final MyGdxGame game;
    private final Stage stage;
    Texture newgameButtonActive;
    Texture newgameButtonInActive;
    Texture continueButtonActive;
    Texture continueButtonInActive;
    Texture highscoreButtonActive;
    Texture highscoreButtonInActive;
    Texture settingsButtonActive;
    Texture settingsButtonInActive;
    Texture storeButtonActive;
    Texture storeButtonInActive;

    Texture exitButtonActive;
    Texture exitButtonInActive;

    private Music mainBGM;
  
    public MainMenuScreen(MyGdxGame game) {
        this.game = game;
        this.stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        createButtons();
    }


    private void createButtons() {
        mainBGM = ConstantSound.mainBGM;
        if (mainBGM != null && !mainBGM.isPlaying()) {
            mainBGM.setLooping(true);
            mainBGM.setVolume(1.0f); // Điều chỉnh âm lượng theo ý muốn
            mainBGM.play();
        }
        Texture newgameButtonActive = new Texture("Button/NewgameActive.PNG");
        Texture newgameButtonInActive = new Texture("Button/NewgameInactive.PNG");
        Texture continueButtonActive = new Texture("Button/ContinueActive.PNG");
        Texture continueButtonInActive = new Texture("Button/ContinueInactive.PNG");
        Texture highscoreButtonActive = new Texture("Button/HighscoreActive.PNG");
        Texture highscoreButtonInActive = new Texture("Button/HighscoreInactive.PNG");
        Texture settingsButtonActive = new Texture("Button/SettingsActive.PNG");
        Texture settingsButtonInActive = new Texture("Button/SettingsInactive.PNG");
        Texture storeButtonActive = new Texture("Button/StoreActive.PNG");
        Texture storeButtonInActive = new Texture("Button/StoreInactive.PNG");

        //newGameButton
        ImageButton newGameButton = createButton(newgameButtonActive, newgameButtonInActive);
        newGameButton.setPosition((float) MyGdxGame.WIDTH /2 - (float) NEWGAME_BUTTON_WIDTH /2, NEWGAME_BUTTON_Y);
        newGameButton.setSize(NEWGAME_BUTTON_WIDTH, NEWGAME_BUTTON_HEIGHT);
        newGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new EnterNameScreen(game));
            }
        });

        //continueButton
        ImageButton continueButton = createButton(continueButtonActive, continueButtonInActive);
        continueButton.setPosition((float) MyGdxGame.WIDTH /2 - (float) CONTINUE_BUTTON_WIDTH /2, CONTINUE_BUTTON_Y);
        continueButton.setSize(CONTINUE_BUTTON_WIDTH, CONTINUE_BUTTON_HEIGHT);
        continueButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Xử lý tiếp tục trò chơi
            }
        });

        //leaderBoardButton
        ImageButton highscoreButton = createButton(highscoreButtonActive, highscoreButtonInActive);
        highscoreButton.setPosition((float) MyGdxGame.WIDTH /2 - (float) HIGHSCORE_BUTTON_WIDTH /2, HIGHSCORE_BUTTON_Y);
        highscoreButton.setSize(HIGHSCORE_BUTTON_WIDTH, HIGHSCORE_BUTTON_HEIGHT);
        highscoreButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new HighscoreScreen(game));
            }
        });

        //settingsButton
        ImageButton settingsButton = createButton(settingsButtonActive, settingsButtonInActive);
        settingsButton.setPosition((float) MyGdxGame.WIDTH /2 - (float) SETTINGS_BUTTON_WIDTH /2, SETTINGS_BUTTON_Y);
        settingsButton.setSize(SETTINGS_BUTTON_WIDTH, SETTINGS_BUTTON_HEIGHT);
        settingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Xử lý cài đặt
            }
        });

        //storeButton
        ImageButton storeButton = createButton(storeButtonActive, storeButtonInActive);
        storeButton.setPosition((float) MyGdxGame.WIDTH /2 - (float) STORE_BUTTON_WIDTH /2, STORE_BUTTON_Y);
        storeButton.setSize(STORE_BUTTON_WIDTH, STORE_BUTTON_HEIGHT);
        storeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new StoreArmsScreen(game));
            }
        });

        stage.addActor(newGameButton);
        stage.addActor(continueButton);
        stage.addActor(highscoreButton);
        stage.addActor(settingsButton);
        stage.addActor(storeButton);
    }

    private ImageButton createButton(Texture activeTexture, Texture inactiveTexture) {
        TextureRegionDrawable activeDrawable = new TextureRegionDrawable(activeTexture);
        TextureRegionDrawable inactiveDrawable = new TextureRegionDrawable(inactiveTexture);

        return new ImageButton(inactiveDrawable, activeDrawable);
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float v) {
        Gdx.gl.glClearColor(0.15f, 0.15f, 0.3f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();

        game.batch.begin();

        // newgame button
        int x = MyGdxGame.WIDTH / 2 - NEWGAME_BUTTON_WIDTH / 2;
        if (Gdx.input.getX() < x + NEWGAME_BUTTON_WIDTH && Gdx.input.getX() > x && MyGdxGame.HEIGHT - Gdx.input.getY() < NEWGAME_BUTTON_Y + NEWGAME_BUTTON_HEIGHT && MyGdxGame.HEIGHT - Gdx.input.getY() > NEWGAME_BUTTON_Y) {
            game.batch.draw(newgameButtonActive, x, NEWGAME_BUTTON_Y, NEWGAME_BUTTON_WIDTH, NEWGAME_BUTTON_HEIGHT);
            if (Gdx.input.isTouched()) {
                stopAndDisposeBGM();
                this.dispose();
                game.setScreen(new EnterNameScreen(game));
            }
        } else {
            game.batch.draw(newgameButtonInActive, x, NEWGAME_BUTTON_Y, NEWGAME_BUTTON_WIDTH, NEWGAME_BUTTON_HEIGHT);
        }

        // continue button
        x = MyGdxGame.WIDTH / 2 - CONTINUE_BUTTON_WIDTH / 2;
        if (Gdx.input.getX() < x + CONTINUE_BUTTON_WIDTH && Gdx.input.getX() > x && MyGdxGame.HEIGHT - Gdx.input.getY() < CONTINUE_BUTTON_Y + CONTINUE_BUTTON_HEIGHT && MyGdxGame.HEIGHT - Gdx.input.getY() > CONTINUE_BUTTON_Y) {
            game.batch.draw(continueButtonActive, x, CONTINUE_BUTTON_Y, CONTINUE_BUTTON_WIDTH, CONTINUE_BUTTON_HEIGHT);
            if (Gdx.input.isTouched()) {
                stopAndDisposeBGM();
                // Do something, for example: game.setScreen(new ContinueGameScreen(game));
            }
        } else {
            game.batch.draw(continueButtonInActive, x, CONTINUE_BUTTON_Y, CONTINUE_BUTTON_WIDTH, CONTINUE_BUTTON_HEIGHT);
        }

        // leaderboard button
        x = MyGdxGame.WIDTH / 2 - HIGHSCORE_BUTTON_WIDTH / 2;
        if (Gdx.input.getX() < x + HIGHSCORE_BUTTON_WIDTH && Gdx.input.getX() > x && MyGdxGame.HEIGHT - Gdx.input.getY() < HIGHSCORE_BUTTON_Y + HIGHSCORE_BUTTON_HEIGHT && MyGdxGame.HEIGHT - Gdx.input.getY() > HIGHSCORE_BUTTON_Y) {
            game.batch.draw(highscoreButtonActive, x, HIGHSCORE_BUTTON_Y, HIGHSCORE_BUTTON_WIDTH, HIGHSCORE_BUTTON_HEIGHT);
            if (Gdx.input.isTouched()) {
                this.dispose();
                game.setScreen(new HighscoreScreen(game));
            }
        } else {
            game.batch.draw(highscoreButtonInActive, x, HIGHSCORE_BUTTON_Y, HIGHSCORE_BUTTON_WIDTH, HIGHSCORE_BUTTON_HEIGHT);
        }

        // settings button
        x = MyGdxGame.WIDTH / 2 - SETTINGS_BUTTON_WIDTH / 2;
        if (Gdx.input.getX() < x + SETTINGS_BUTTON_WIDTH && Gdx.input.getX() > x && MyGdxGame.HEIGHT - Gdx.input.getY() < SETTINGS_BUTTON_Y + SETTINGS_BUTTON_HEIGHT && MyGdxGame.HEIGHT - Gdx.input.getY() > SETTINGS_BUTTON_Y) {
            game.batch.draw(settingsButtonActive, x, SETTINGS_BUTTON_Y, SETTINGS_BUTTON_WIDTH, SETTINGS_BUTTON_HEIGHT);
            if (Gdx.input.isTouched()) {
                this.dispose();
                game.setScreen(new SettingsScreen(game));
            }
        } else {
            game.batch.draw(settingsButtonInActive, x, SETTINGS_BUTTON_Y, SETTINGS_BUTTON_WIDTH, SETTINGS_BUTTON_HEIGHT);
        }

        // store button
        x = MyGdxGame.WIDTH / 2 - STORE_BUTTON_WIDTH / 2;
        if (Gdx.input.getX() < x + STORE_BUTTON_WIDTH && Gdx.input.getX() > x && MyGdxGame.HEIGHT - Gdx.input.getY() < STORE_BUTTON_Y + STORE_BUTTON_HEIGHT && MyGdxGame.HEIGHT - Gdx.input.getY() > STORE_BUTTON_Y) {
            game.batch.draw(storeButtonActive, x, STORE_BUTTON_Y, STORE_BUTTON_WIDTH, STORE_BUTTON_HEIGHT);
            if (Gdx.input.isTouched()) {
                // Do something
            }
        } else {
            game.batch.draw(storeButtonInActive, x, STORE_BUTTON_Y, STORE_BUTTON_WIDTH, STORE_BUTTON_HEIGHT);
        }

        game.batch.end();
    }

    @Override
    public void dispose() {
        stage.dispose();
        newgameButtonActive.dispose();
        newgameButtonInActive.dispose();
        continueButtonActive.dispose();
        continueButtonInActive.dispose();
        highscoreButtonActive.dispose();
        highscoreButtonInActive.dispose();
        settingsButtonActive.dispose();
        settingsButtonInActive.dispose();
        storeButtonActive.dispose();
        storeButtonInActive.dispose();
        exitButtonActive.dispose();
        exitButtonInActive.dispose();
    }

    private void stopAndDisposeBGM() {
        if (mainBGM != null) {
            mainBGM.stop();
            mainBGM.dispose();
        }
    }
  
    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
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
}