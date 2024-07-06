package com.mygdx.game.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.model.*;
import com.mygdx.game.model.impl.Enemy.Medusa;
import com.mygdx.game.model.impl.Player.Ninja;
import com.mygdx.game.view.Brick;
import com.mygdx.game.view.GameMap;

import java.util.ArrayList;
import java.util.Random;

import static com.mygdx.game.model.constant.Constants.PPM;

public class LevelManager {
    public static LevelManager instance;
    public int currentLevel=0;
    public int maxLevel=0;
    private boolean isGoToNextLevel, isGoToPreLevel;
    public ArrayList<ArrayList<Brick>> bricks;
    public ArrayList<ArrayList<Enemy>> enemies;
    private final ArrayList<ArrayList<Coin>> coins;
    public Ninja player;
    private final Texture startMapImage = new Texture("tiles/mapStart.png");
    private final Texture mapImage = new Texture("tiles/map.png");
    public LevelManager(){
        bricks = new ArrayList<>();
        enemies = new ArrayList<>();
        coins = new ArrayList<>();
        coins.add(new ArrayList<>());
        player = MyGdxGame.ninja;
        currentLevel = 0;
        maxLevel = 0;
        isGoToNextLevel = false;
        isGoToPreLevel = false;
        spawnNormalLevel();
    }
    public static LevelManager getInstance() {
        if(instance==null) instance = new LevelManager();
        return instance;
    }
    public void drawMap(SpriteBatch spriteBatch) {
        Texture img = mapImage;
        if(currentLevel==0) img = startMapImage;
        spriteBatch.draw(img, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }
    public void spawnNormalLevel() {
        coins.add(new ArrayList<Coin>());
        ArrayList<Brick> tmp = new ArrayList<>();
        ArrayList<Enemy> tmp1 = new ArrayList<>();
        tmp.add(new Brick(new Random(System.currentTimeMillis()).nextInt(18)+1, 31, 16));
        tmp.add(new Brick(new Random(System.currentTimeMillis()).nextInt(18)+1, 21, 16));
        tmp.add(new Brick(new Random(System.currentTimeMillis()).nextInt(18)+1, 10, 16));
        for (Brick brick : tmp) {
            tmp1.add(new Medusa(brick.getX()+new Random(System.currentTimeMillis()).nextInt(brick.getWidth()), brick.getY() + brick.getHeight(), 1, brick, player));
        }
        enemies.add(tmp1);
        bricks.add(tmp);
    }

    public void spawnHardLevel() {
        coins.add(new ArrayList<Coin>());
        ArrayList<Brick> tmp = new ArrayList<>();
        ArrayList<Enemy> tmp1 = new ArrayList<>();
        tmp.add(new Brick(new Random(System.currentTimeMillis()).nextInt(18)+1, 15, 20));
        for (Brick brick : tmp) {
            tmp1.add(new Medusa(brick.getX() + new Random(System.currentTimeMillis()).nextInt(brick.getWidth()), brick.getY() + brick.getHeight(), 2, brick, player));
        }
        enemies.add(tmp1);
        bricks.add(tmp);
    }

    public void update() {
        Array<Body> bodies = new Array<>();
        GameMap.world.getBodies(bodies);
        for (Body body : bodies) {
            if(!body.getFixtureList().isEmpty()) System.out.println(body.getFixtureList().first().getUserData());
        }


        System.out.println(GameMap.world.getBodyCount());
        if(isGoToNextLevel) nextLevel();
        else if(isGoToPreLevel) preLevel();
        ArrayList<Enemy> enemiesRemove = new ArrayList<>();
        ArrayList<Body> bodiesRemove = new ArrayList<>();
        for(Enemy enemy : enemies.get(currentLevel)) {
            if(enemy.isDead()) {
                coins.get(currentLevel).add(new Coin(enemy.getX(), enemy.getY(), enemy.getLevel()));
                enemiesRemove.add(enemy);
                bodiesRemove.add(enemy.getBody());
                bodiesRemove.addAll(enemy.getEnemyBulletsBodies());
                enemy.getEnemyBulletsBodies().clear();
            }
        }
        enemies.get(currentLevel).removeAll(enemiesRemove);

        ArrayList<Coin> coinsRemove = new ArrayList<>();
        for (Coin coin : coins.get(currentLevel)) {
            if (!coin.isAppear()) {
                coinsRemove.add(coin);
                bodiesRemove.add(coin.getBody());
            }
        }
        coins.get(currentLevel).removeAll(coinsRemove);

        for(Body body : bodiesRemove) GameMap.world.destroyBody(body);


    }

    public void nextLevel() {
        System.out.println("nextLevel");
        Array<Body> bodies = new Array<>();
        GameMap.world.getBodies(bodies);
        for (Body body : bodies) {
            if(!body.getFixtureList().isEmpty() && (body.getFixtureList().first().getUserData() instanceof Player ||
                                                    body.getFixtureList().first().getUserData() instanceof PlayerBullet||
                                                    body.getFixtureList().first().getUserData() == "wall" ||
                                                    body.getFixtureList().first().getUserData() == "bottomWall" ||
                                                    body.getFixtureList().first().getUserData() == "topWall")) continue;
            GameMap.destroyBody(body);
        }

        this.currentLevel++;
        if(this.currentLevel>this.maxLevel) {
            maxLevel = currentLevel;
            if(currentLevel%10==0 && currentLevel!=0) spawnHardLevel();
            else spawnNormalLevel();
        } else {
            for (Brick brick : bricks.get(currentLevel)) brick.createBody();
            for (Coin coin : coins.get(currentLevel)) coin.createBody();
        }


        player.getBody().setTransform(1000,1000, 0);
        player.getPlayerBullet().getBody().setTransform(player.getPlayerBullet().getBody().getPosition().x, 30/PPM, player.getPlayerBullet().getBody().getAngle());
        player.setAppear(false);

        isGoToNextLevel = false;
    }
    public void preLevel() {
        System.out.println("preLevel.");
        Array<Body> bodies = new Array<>();
        GameMap.world.getBodies(bodies);
        for (Body body : bodies) {
            if(!body.getFixtureList().isEmpty() &&
                    (body.getFixtureList().first().getUserData() instanceof Player ||
                    body.getFixtureList().first().getUserData() instanceof PlayerBullet||
                    body.getFixtureList().first().getUserData() == "wall" ||
                    body.getFixtureList().first().getUserData() == "bottomWall" ||
                    body.getFixtureList().first().getUserData() == "topWall")) continue;
            GameMap.destroyBody(body);
        }

        currentLevel--;
        if(currentLevel<0) currentLevel=0;

        for (Enemy enemy : enemies.get(currentLevel)) {
            enemy.createBody();
        }

        for (Coin coin : coins.get(currentLevel)) coin.createBody();

        for (Brick brick : bricks.get(currentLevel)) brick.createBody();

        player.getBody().setTransform(1000,1000, 0);
        player.getPlayerBullet().getBody().setTransform(player.getPlayerBullet().getBody().getPosition().x, 650/PPM, player.getPlayerBullet().getBody().getAngle());
        player.setAppear(false);

        isGoToPreLevel = false;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(int currentLevel) {
        this.currentLevel = currentLevel;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public Player getPlayer() {
        return player;
    }
    public ArrayList<ArrayList<Coin>> getCoins() {
        return coins;
    }

    public void setGoToNextLevel(boolean goToNextLevel) {
        isGoToNextLevel = goToNextLevel;
    }

    public void setGoToPreLevel(boolean goToPreLevel) {
        isGoToPreLevel = goToPreLevel;
    }
}