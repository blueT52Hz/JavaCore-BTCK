package com.mygdx.game.model.impl.Bullet;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.mygdx.game.controller.BoxManager;
import com.mygdx.game.model.PlayerBullet;
import com.mygdx.game.model.StatusSkinAndArm;
import com.mygdx.game.view.GameMap;

import static com.mygdx.game.model.constant.Constants.PPM;

public class Kunai extends PlayerBullet {

    public Kunai(float x, float y) {
        super(new Texture("Bullet/" + StatusSkinAndArm.armState + ".png"));
        this.width=40;
        this.height=8;

        this.body = BoxManager.createBox(x, y, width, height, false, GameMap.world, 0);
        this.body.getFixtureList().first().setUserData(this);

    }

    public void setAppear(boolean appear) {
        this.appear = appear;
    }

    public boolean isAppear() {
        return appear;
    }

//     điều chỉnh hướng theo tọa độ target
    @Override
    public void updateRotation(float xWeapon, float yWeapon) {}

    // điều chỉnh hướng theo chuột
    public void updateRotation() {
        float xWeapon = body.getPosition().x*PPM;
        float yWeapon = body.getPosition().y*PPM;
        float MOUSE_X = Gdx.input.getX();
        float MOUSE_Y = Gdx.graphics.getHeight() - Gdx.input.getY();
        float rotation = (float) Math.toDegrees(MathUtils.acos((float) ((double) (MOUSE_X - xWeapon) / (Math.sqrt((MOUSE_Y - yWeapon) * (MOUSE_Y - yWeapon) + (MOUSE_X - xWeapon) * (MOUSE_X - xWeapon))))));
        if (MOUSE_Y < yWeapon) rotation = 360 - rotation;
        setRotation(rotation);
        body.setTransform(body.getPosition(), rotation * MathUtils.degreesToRadians);
        updateSpeed();
    }

    @Override
    public void update() {
        if(!isHandledContact()) {
            updateBodyPosition();
            updateSpeed();
        }
        x = body.getPosition().x*PPM;
        y = body.getPosition().y*PPM;
        setBounds(x-20, y-4, width, height);
        setOriginCenter();
    }

    public void updateSpeed() {
        xSpeed = speed *  (float) Math.cos(Math.toRadians(getRotation()));
        ySpeed = speed * (float) Math.sin(Math.toRadians(getRotation()));
        body.setLinearVelocity(xSpeed/PPM, ySpeed/PPM);
    }
}