package com.mygdx.game.model;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.game.model.constant.ConstantSound;
import com.mygdx.game.model.constant.EnemyState;
import com.mygdx.game.view.Brick;

import java.util.ArrayList;

public abstract class Enemy extends Entity{
    protected float speed;
    protected EnemyState enemyState;
    protected Animation moveRightAnimation;
    protected Animation moveLeftAnimation;
    protected Animation deathRightAnimation;
    protected Animation deathLeftAnimation;
    protected Animation attackRightAnimation;
    protected Animation attackLeftAnimation;

    protected Brick brick;
    protected ArrayList<EnemyBullet> enemyBullets;
    protected ArrayList<Body> enemyBulletsBodies;
    protected Player target;
    protected int level;
    public Enemy(int level, Brick brick, Player target) {
        enemyBullets = new ArrayList<>();
        enemyBulletsBodies = new ArrayList<>();
        this.level = level;
        this.brick = brick;
        this.target = target;
    }

    public ArrayList<Body> getEnemyBulletsBodies() {
        return enemyBulletsBodies;
    }

    public ArrayList<EnemyBullet> getEnemyBullets() {
        return enemyBullets;
    }

    public Brick getBrick() {
        return brick;
    }

    protected abstract void setEnemyTilePath();

    public void setEnemyState(EnemyState enemyState) {
        this.enemyState = enemyState;
    }

    public EnemyState getEnemyState() {
        return enemyState;
    }

    public int getLevel() {
        return level;
    }

    public Player getTarget() {
        return target;
    }

    @Override
    public boolean isDead() {
        return super.isDead();
    }
}
