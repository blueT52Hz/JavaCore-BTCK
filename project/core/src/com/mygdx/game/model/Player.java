package com.mygdx.game.model;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.mygdx.game.model.constant.PlayerState;

public abstract class Player extends Entity {
    protected float xSpeed;
    protected float ySpeed;
    protected PlayerState playerState;
    protected boolean appear;
    protected PlayerBullet playerBullet;
    public Sprite navigationArrow;

    public void setxSpeed(float xSpeed) {
        this.xSpeed = xSpeed;
    }

    public void setySpeed(float ySpeed) {
        this.ySpeed = ySpeed;
    }

    public void setPlayerState(PlayerState playerState) {
        this.playerState = playerState;
    }

    public float getxSpeed() {
        return xSpeed;
    }

    public float getySpeed() {
        return ySpeed;
    }

    public PlayerState getPlayerState() {
        return playerState;
    }

    public boolean isAppear() {
        return appear;
    }

    public void setAppear(boolean appear) {
        this.appear = appear;
    }

    @Override
    public void createBody(){
        super.createBody();
        this.body.getFixtureList().first().setUserData(this);
    }

    public PlayerBullet getPlayerBullet() {
        return playerBullet;
    }
}
