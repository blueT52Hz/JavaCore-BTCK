package com.mygdx.game.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.mygdx.game.MyGdxGame;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HighscoreScreen implements BaseScreen {
    private static final int BACK_BUTTON_WIDTH = 100;
    private static final int BACK_BUTTON_HEIGHT = 50;
    private static final int BACK_BUTTON_Y = 30;

    private MyGdxGame game;

    private final MyGdxGame game;

    private final Texture highscoreTexture;
    private final Texture tablehighscoreTexture;

    // Khai báo font cho dòng tiêu đề và player
    private final BitmapFont headerFont;
    private final BitmapFont playerFont;

    public HighscoreScreen(MyGdxGame game) {
        super(game);
        this.game = game;
        // Generate BitmapFont from OTF file
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("MinecraftRegular-Bmg3.otf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        // Thiết lập kích thước cho dòng tiêu đề là 24
        parameter.size = 24;
        parameter.color = Color.YELLOW; // Màu vàng cho dòng tiêu đề
        headerFont = generator.generateFont(parameter);

        // Thiết lập kích thước cho dòng player là 19
        parameter.size = 19;
        parameter.color = Color.WHITE; // Màu trắng cho dòng player
        playerFont = generator.generateFont(parameter);

        generator.dispose(); // Don't forget to dispose to avoid memory leaks

        highscoreTexture = new Texture("Button/HighscoreActive.PNG");
        tablehighscoreTexture = new Texture("Button/TableHighscore.PNG");
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
        // Draw the highscore image
        game.batch.draw(highscoreTexture, (float) MyGdxGame.WIDTH / 2 - 120, 550, 240, 60);
        game.batch.draw(tablehighscoreTexture, (float) MyGdxGame.WIDTH / 2 - 180, 150, 360, 370);

        // Load high scores from file
        List<PlayerScore> playerScores = loadHighScores();

        // Sort high scores by score (descending)
        playerScores.sort(Comparator.comparingInt(PlayerScore::getScore).reversed());
        int y = 490;
        // Set font color for headers
        headerFont.setColor(Color.YELLOW);
        headerFont.draw(game.batch, "Rank", 30, y);
        headerFont.draw(game.batch, "Name", 100, y);
        headerFont.draw(game.batch, "Lv", 250, y);
        headerFont.draw(game.batch, "Score", 300, y);


        y = 450;
        int stt = 1;
        
        playerFont.setColor(Color.WHITE);
        for (PlayerScore playerScore : playerScores.subList(0, Math.min(playerScores.size(), 10))) {
            playerFont.draw(game.batch, String.valueOf(stt), 50, y);
            playerFont.draw(game.batch, playerScore.getName(), 100, y);
            playerFont.draw(game.batch, String.valueOf(playerScore.getLevel()), 250, y);
            playerFont.draw(game.batch, String.valueOf(playerScore.getScore()), 300, y);
            y -= 30;
            stt++;
        }

        int x = MyGdxGame.WIDTH / 2 - BACK_BUTTON_WIDTH / 2;
        if (Gdx.input.getX() < x + BACK_BUTTON_WIDTH && Gdx.input.getX() > x && MyGdxGame.HEIGHT - Gdx.input.getY() < BACK_BUTTON_Y + BACK_BUTTON_HEIGHT && MyGdxGame.HEIGHT - Gdx.input.getY() > BACK_BUTTON_Y) {
            game.batch.draw(backButtonActive, x, BACK_BUTTON_Y, BACK_BUTTON_WIDTH, BACK_BUTTON_HEIGHT);
            if (Gdx.input.justTouched()) {
                this.dispose();
                game.setScreen(new MainMenuScreen(game));
            }
        } else {
            game.batch.draw(backButtonInActive, x, BACK_BUTTON_Y, BACK_BUTTON_WIDTH, BACK_BUTTON_HEIGHT);
        }

        game.batch.end();
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void dispose() {
        // Dispose resources
        headerFont.dispose();
        playerFont.dispose();
        highscoreTexture.dispose();
    }

    private List<PlayerScore> loadHighScores() {
        List<PlayerScore> playerScores = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("HighScores.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\s+");
                if (parts.length >= 3) {
                    String name = parts[0];
                    int level = Integer.parseInt(parts[1]);
                    int score = Integer.parseInt(parts[2]);
                    playerScores.add(new PlayerScore(name, level, score));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return playerScores;
    }

    private static class PlayerScore {
        private final String name;
        private final int level;
        private final int score;

        public PlayerScore(String name, int level, int score) {
            this.name = name;
            this.level = level;
            this.score = score;
        }

        public String getName() {
            return name;
        }

        public int getLevel() {
            return level;
        }

        public int getScore() {
            return score;
        }
    }
}
