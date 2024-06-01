package com.mygdx.game.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.controller.LevelManager;
import com.mygdx.game.model.Coin;
import com.mygdx.game.model.Enemy;


public class GameMap extends Matrix4 {
    public static World world = new World(new Vector2(0, -1f), false);
    private final LevelManager levelManager;
    private float stateTime;
    public Body leftWall, rightWall, bottomWall, topWall;
    public GameMap() {
        stateTime = 0;
        levelManager = LevelManager.getInstance();
    }
    public void draw(SpriteBatch spriteBatch) {
        stateTime += Gdx.graphics.getDeltaTime();
        levelManager.update();
        drawBackground(spriteBatch);
        drawBricks(spriteBatch);
        drawEnemies(spriteBatch);
    }
    public void drawCoins(SpriteBatch spriteBatch) {
        for(Coin coin : levelManager.getCoins().get(levelManager.getCurrentLevel())) {
            coin.draw(spriteBatch, stateTime);
        }
    }

    public void drawBricks(SpriteBatch spriteBatch) {
        for(Brick brick : levelManager.bricks.get(levelManager.getCurrentLevel())) {
            brick.draw(spriteBatch);
        }
    }

    public void drawEnemies(SpriteBatch spriteBatch) {
        for(Enemy enemy : levelManager.enemies.get(levelManager.currentLevel)) {
            enemy.draw(spriteBatch, stateTime);
        }
    }

    public void drawBackground(SpriteBatch spriteBatch) {
        levelManager.drawMap(spriteBatch);
    }

    public LevelManager getLevelManager() {
        return levelManager;
    }

    public float getStateTime() {
        return stateTime;
    }

    // Thêm phương thức kiểm tra tất cả quái vật đã bị tiêu diệt
    public boolean allEnemiesDefeated() {
        for (Enemy enemy : levelManager.enemies.get(levelManager.currentLevel)) {
            if (!enemy.isDead()) {
                return false;
            }
        }
        return true;
    }
}
