package com.mygdx.game.view;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.MyGdxGame;

public class EnterNameScreen implements Screen {
    private MyGdxGame game;
    private Stage stage;
    private TextField nameInput;
    private Texture background;
    private BitmapFont font;
    private Texture submitButtonActive;
    private Texture submitButtonInactive;

    private static final int SUBMIT_BUTTON_WIDTH = 110;
    private static final int SUBMIT_BUTTON_HEIGHT = 50;
    private static final int SUBMIT_BUTTON_Y = 280;

    public EnterNameScreen(MyGdxGame game) {
        super(game);
        background = new Texture("Button/TableHighscore.png");
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("MinecraftRegular-Bmg3.otf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 24;
        font = generator.generateFont(parameter);
        generator.dispose();

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        Label nameLabel = new Label("Enter your name:", labelStyle);
        nameLabel.setPosition(Gdx.graphics.getWidth() / 2f - 100, Gdx.graphics.getHeight() / 2f + 50);
        stage.addActor(nameLabel);

        TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();
        textFieldStyle.font = font;
        textFieldStyle.fontColor = Color.BLACK;

        Texture whiteTexture = new Texture("white.png");
        textFieldStyle.background = new TextureRegionDrawable(new TextureRegion(whiteTexture));

        textFieldStyle.cursor = new TextureRegionDrawable(new Texture(Gdx.files.internal("cursor.png")));
        textFieldStyle.cursor.setMinWidth(1f);
        textFieldStyle.cursor.setMinHeight(20f);
        textFieldStyle.cursor.setLeftWidth(1f);

        nameInput = new TextField("", textFieldStyle);
        nameInput.setSize(200, 40);
        nameInput.setPosition(Gdx.graphics.getWidth() / 2f - 100, Gdx.graphics.getHeight() / 2f);
        nameInput.setAlignment(Align.center);
        nameInput.setMaxLength(7);
        stage.addActor(nameInput);

        submitButtonActive = new Texture("Button/EnterActive.PNG");
        submitButtonInactive = new Texture("Button/EnterInactive.PNG");
    }

    @Override
    protected void onBackButtonPressed() {
        game.setScreen(new MainMenuScreen(game));
    }
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.15f, 0.15f, 0.3f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();
        float backgroundX = (float) (Gdx.graphics.getWidth() - 360) / 2;
        float backgroundY = (float) (Gdx.graphics.getHeight() - 200) / 2;
        game.batch.draw(background, backgroundX, backgroundY, 360, 200);

        int x = Gdx.graphics.getWidth() / 2 - SUBMIT_BUTTON_WIDTH / 2;
        if (Gdx.input.getX() < x + SUBMIT_BUTTON_WIDTH && Gdx.input.getX() > x && Gdx.graphics.getHeight() - Gdx.input.getY() < SUBMIT_BUTTON_Y + SUBMIT_BUTTON_HEIGHT && Gdx.graphics.getHeight() - Gdx.input.getY() > SUBMIT_BUTTON_Y) {
            game.batch.draw(submitButtonActive, x, SUBMIT_BUTTON_Y, SUBMIT_BUTTON_WIDTH, SUBMIT_BUTTON_HEIGHT);
            if (Gdx.input.isTouched()) {
                String playerName = nameInput.getText();
                game.setScreen(new MainGameScreenTest(game, playerName));
            }
        } else {
            game.batch.draw(submitButtonInactive, x, SUBMIT_BUTTON_Y, SUBMIT_BUTTON_WIDTH, SUBMIT_BUTTON_HEIGHT);
        }
        game.batch.end();

        stage.act(delta);
        stage.draw();
    }
}
