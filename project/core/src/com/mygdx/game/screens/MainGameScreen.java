package com.mygdx.game.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.entities.Kunai;
import com.mygdx.game.entities.MainCharacter;
import com.mygdx.game.entities.NavigationArrow;
import com.badlogic.gdx.math.MathUtils;

public class MainGameScreen implements Screen {
    SpriteBatch batch;
    Texture texture;
    Sprite sprite, spriteNinja, spriteKunai;
    float MOUSE_X;
    float MOUSE_Y;
    public MainCharacter ninja;
    float stateTime; // thời gian set cho animation
    boolean started;// trạng thái game để set cho phần thả chuột đầu
    MyGdxGame game;

    public MainGameScreen(MyGdxGame game) {
        this.game = game;
        ninja = new MainCharacter();
        started = false;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        MainCharacter.load();
        sprite = new Sprite(MainCharacter.navigationImg);
        spriteNinja = new Sprite(MainCharacter.waitImg);
        spriteKunai = new Sprite(MainCharacter.kunaiImg);
    }

    @Override
    public void render(float deltaTime) {
        Gdx.gl.glClearColor(0.15f, 0.15f, 0.3f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();

        if (Gdx.input.isTouched()) {
            started = true;
            MOUSE_X = Gdx.input.getX();
            MOUSE_Y = Gdx.graphics.getHeight() - Gdx.input.getY();
            ninja.navigationArrow.update(ninja.kunai.x, ninja.kunai.y, MOUSE_X, MOUSE_Y);
            ninja.kunai.rotation = ninja.navigationArrow.rotation;
            sprite.setOriginCenter();
            sprite.setRotation(ninja.navigationArrow.rotation);// set góc quay cho thanh điều hướng
            sprite.setBounds(ninja.kunai.x - 35, ninja.kunai.y - 26, NavigationArrow.WIDTH, NavigationArrow.HEIGHT);
            sprite.draw(batch);
            ninja.x = ninja.kunai.x;
            ninja.y = ninja.kunai.y;
            ninja.speed = 0;
            ninja.kunai.xSpeed = 0;
            ninja.kunai.ySpeed = 0;
            ninja.kunai.speedChanged = false;
            ninja.throwed = false;
            ninja.update(deltaTime);
            batch.draw((Texture) MainCharacter.glideAnimation.getKeyFrame(ninja.stateTime, true), ninja.x-MainCharacter.WIDTH/2, ninja.y - MainCharacter.HEIGHT/2, MainCharacter.WIDTH, MainCharacter.HEIGHT);

        } else if (started) {
            if (!ninja.throwed)
            {
                ninja.stateTime += deltaTime;
                batch.draw((Texture) MainCharacter.throwAnimation.getKeyFrame(ninja.stateTime, false), ninja.x - MainCharacter.WIDTH/2, ninja.y-MainCharacter.HEIGHT/2, MainCharacter.WIDTH, MainCharacter.HEIGHT);
                if(ninja.stateTime >= 30 * deltaTime) {
                    ninja.throwed = true;
                    ninja.stateTime = 0f;
                }
            }else batch.draw((Texture) MainCharacter.glideAnimation.getKeyFrame(ninja.stateTime, true), ninja.x-MainCharacter.WIDTH/2, ninja.y - MainCharacter.HEIGHT/2, MainCharacter.WIDTH, MainCharacter.HEIGHT);

            if (!ninja.kunai.speedChanged) {
                ninja.kunai.speedChanged = true;
                ninja.kunai.updateSpeedStartFly(MOUSE_X, MOUSE_Y);
            }
            ninja.kunai.updateCollision();
            ninja.kunai.updatePosition(deltaTime);
            ninja.update(deltaTime);
            spriteKunai.setOriginCenter();
            spriteKunai.setRotation(ninja.kunai.rotation);
            spriteKunai.setBounds(ninja.kunai.x - 4, ninja.kunai.y - 20, Kunai.WIDTH, Kunai.HEIGHT);
            spriteKunai.draw(batch);
        }
        batch.end();
    }

    @Override
    public void resize(int i, int i1) {

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
    public void dispose () {
        batch.dispose();
        texture.dispose();
    }
}
