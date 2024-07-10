package com.mygdx.game.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.game.controller.BoxManager;
import com.mygdx.game.view.GameMap;

public class Coin {
    private Animation spinning;
    private Body body;
    private float x, y;
    private int value;
    private final int size;
    private boolean appear;
    public Coin(float x, float y, int level) {
        this.x = x;
        this.y = y;
        this.size = level * 45;
        this.appear = true;
        load();
        setValue(level);
        createBody();
    }
    public void draw(SpriteBatch spriteBatch, float gameMapStateTime) {
        Texture tmp ;
        tmp = (Texture) spinning.getKeyFrame(gameMapStateTime, true);
        spriteBatch.draw(tmp, x, y+ (float) size /2, size, size);
    }

    public void load() {
        Texture[] textures = new Texture[10];
        for(int i=0;i<10;++i) {
            textures[i] = new Texture(String.format("Entities/coin/Coin (%d).png", i+1));
        }
        spinning = new Animation<>(0.12f, textures);
    }

    public Body getBody() {
        return body;
    }

    public void setValue(int level) {
        switch (level) {
            case 1:
                this.value = 1;
                break;
            case 2:
                this.value = 5;
                break;
            case 3:
                this.value = 15;
                break;
        }
    }

    public int getValue() {
        return value;
    }

    public void createBody() {

        this.body = BoxManager.createBox(x+ (float) size /2, y+size, size-20, size-20, true, GameMap.world, 0);
        this.body.getFixtureList().first().setUserData(this);
        this.body.setGravityScale(0);
    }

    public boolean isAppear() {
        return appear;
    }

    public void setAppear(boolean appear) {
        this.appear = appear;
    }
}