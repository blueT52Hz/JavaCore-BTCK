package com.mygdx.game.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;

import static com.mygdx.game.model.constant.Constants.PPM;

public abstract class Bullet extends Sprite {
    public float x;
    public float y;
    public float speed;
    public float xSpeed;
    public float ySpeed;
    protected int width;
    protected int height;
    public Body body;
    protected boolean appear;
    protected boolean canBounce = false;
    protected boolean handledContact;
    public Bullet(Texture texture) {
        super(texture);
    }
    protected abstract void update();
    protected abstract void updateRotation(float xWeapon, float yWeapon);
    public void updateBodyPosition() {
        this.body.setTransform(body.getPosition(), getRotation() * MathUtils.degreesToRadians);
        handledContact = true;
    }
    public void setHandledContact(boolean handledContact) {
        this.handledContact = handledContact;
    }

    public void setAppear(boolean appear) {
        this.appear = appear;
    }

    public boolean isHandledContact() {
        return handledContact;
    }

    public boolean isCanBounce() {
        return canBounce;
    }

    public boolean isAppear() {
        return appear;
    }

    public float getySpeed() {
        return ySpeed;
    }

    public void setySpeed(float ySpeed) {
        this.ySpeed = ySpeed;
    }

    public Body getBody() {
        return body;
    }
    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public void setRotation(float rotation) {
        rotation %= 360;
        if(rotation<0) rotation += 360;
        super.setRotation(rotation);
    }
}