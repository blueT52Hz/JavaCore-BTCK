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

public abstract class BaseScreen implements Screen {
    protected final MyGdxGame game;
    protected final Stage stage;
    private static final int BACK_BUTTON_WIDTH = 40;
    private static final int BACK_BUTTON_HEIGHT = 40;
    private static final int BACK_BUTTON_Y = 670;

    public BaseScreen(MyGdxGame game) {
        this.game = game;
        this.stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        Texture backButtonActive = new Texture("Button/Back.PNG");
        ImageButton backButton = new ImageButton(new TextureRegionDrawable(backButtonActive));
        backButton.setPosition(15, BACK_BUTTON_Y);
        backButton.setSize(BACK_BUTTON_WIDTH, BACK_BUTTON_HEIGHT);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                onBackButtonPressed();
            }
        });

        stage.addActor(backButton);
    }

    protected abstract void onBackButtonPressed();

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
