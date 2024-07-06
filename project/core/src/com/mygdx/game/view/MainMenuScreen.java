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
    public MainMenuScreen(MyGdxGame game) {
        this.game = game;
        this.stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        createButtons();
    }

    private void createButtons() {
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
    public void render(float delta) {
        Gdx.gl.glClearColor(0.15f, 0.15f, 0.3f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
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

    @Override
    public void dispose() {
        stage.dispose();
    }
}