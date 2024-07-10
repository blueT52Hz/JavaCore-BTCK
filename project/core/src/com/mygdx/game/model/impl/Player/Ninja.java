package com.mygdx.game.model.impl.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.controller.BoxManager;
import com.mygdx.game.model.Player;
import com.mygdx.game.model.PlayerBullet;
import com.mygdx.game.model.StatusSkinAndArm;
import com.mygdx.game.model.constant.PlayerState;
import com.mygdx.game.model.impl.Bullet.Kunai;
import com.mygdx.game.view.GameMap;

import java.util.Objects;

import static com.mygdx.game.model.constant.Constants.PPM;

public class Ninja extends Player {
    private static Animation glideAnimation;
    private static Animation throwAnimation;
    private static Animation flashAnimation;
    private static Texture waitImg;
    public Kunai kunai;
    public boolean throwed;

    private static Texture[] skins;
    private static Texture[] arms;

    public Ninja() {
        super();
        this.place = 0;
        this.x = 200;
        this.y = 50;
        this.width = 32*3/2;
        this.height = 38*3/2;
        this.ySpeed = -10;
        this.stateTime = 0;
        this.throwed = true;
        this.appear = true;
        this.kunai = new Kunai(x, y);
        this.playerState = PlayerState.IDLE;
        this.navigationArrow = new Sprite(new Texture("Arrow2.png"));
        createBody();
        loadTextures();
        loadAnimation();
    }

    private void loadTextures() {
        skins = new Texture[] {
                new Texture("Entities/ninja/Throw__009.png"),
                new Texture("Entities/ninjagirl/Throw__009.png"),
        };
        arms = new Texture[] {
                new Texture("Bullet/Kunai.png"),
                new Texture("Bullet/Bomb.png"),
                new Texture("Bullet/Bomerang.png"),
                new Texture("Bullet/Dart.png"),
                new Texture("Bullet/FoldingFan.png"),
                new Texture("Bullet/Hammer.png"),
                new Texture("Bullet/Rugby.png"),
                new Texture("Bullet/Shuriken.png"),
                new Texture("Bullet/Shield.png"),

        };
    }

    public void changeSkin(int index) {
        loadAnimation();
        System.out.println("Change");
    }

    public void changeArm(int index) {
        kunai.setTexture(arms[index]);
        if (!Objects.equals(StatusSkinAndArm.armState, "Kunai")){
            kunai.setHeight(40);
        }else kunai.setHeight(8);
    }

    public void draw(SpriteBatch spriteBatch, float gameMapStateTime) {
        if (!isAppear()) return;
        update();
        switch (playerState) {
            case IDLE:
                spriteBatch.draw(waitImg, x - (float) width / 2, y - (float) height / 2, width, height);
                break;
            case GLIDE:
                spriteBatch.draw((Texture) glideAnimation.getKeyFrame(gameMapStateTime, true), x - (float) width / 2, y - (float) height / 2, width, height);
                break;
            case THROW:
                Texture throwImg = (Texture) throwAnimation.getKeyFrame(stateTime, false);
                stateTime += Gdx.graphics.getDeltaTime();
                if (throwImg == null || stateTime >= Gdx.graphics.getDeltaTime() * 30) {
                    stateTime = 0;
                    playerState = PlayerState.GLIDE;
                    throwed = true;
                }
                spriteBatch.draw(throwImg, x - (float) width / 2, y - (float) height / 2, width, height);
                break;
            case FLASH:
                Texture tmp = (Texture) flashAnimation.getKeyFrame(stateTime, true);
                stateTime += Gdx.graphics.getDeltaTime();
                if (tmp == null || stateTime >= Gdx.graphics.getDeltaTime() * 30) {
                    stateTime = 0;
                    playerState = PlayerState.GLIDE;
                    tmp = waitImg;
                }
                spriteBatch.draw(tmp, x - (float) width / 2, y - (float) height / 2, width, height);
                break;
            case DEAD:
                break;
        }
    }

    @Override
    public void update() {
        if(!isAppear()) {
            return;
        }
        if(playerState == PlayerState.GLIDE)    this.body.setLinearVelocity(0, -50f/PPM);
        if(playerState == PlayerState.IDLE)     this.body.setLinearVelocity(0, 0);
        x = body.getPosition().x*PPM;
        y = body.getPosition().y*PPM;
    }

    @Override
    public void loadAnimation() {
        Texture[] throwImg = new Texture[9];
        Texture[] glideImg = new Texture[9];
        Texture[] flashImg = new Texture[14];
        for(int i=0;i<9;++i) {
            throwImg[i] = new Texture("Entities/" + StatusSkinAndArm.skinState + "/" + String.format("Throw__00%d.png", i));
            glideImg[i] = new Texture("Entities/" + StatusSkinAndArm.skinState + "/" +String.format("Glide_00%d.png", i));
            flashImg[i] = new Texture("Entities/" + StatusSkinAndArm.skinState + "/" +String.format("Lightning_%02d.png", i + 1));
        }
        for (int i = 10;  i<= 13; i++) flashImg[i] = new Texture("Entities/" + StatusSkinAndArm.skinState + "/" + String.format("Lightning_%02d.png", i+1));
        throwAnimation = new Animation<>(0.075f, throwImg);
        glideAnimation = new Animation<>(0.1f, glideImg);
        flashAnimation = new Animation<>(0.025f, flashImg);
        waitImg = throwImg[0];
    }

    @Override
    public void createBody() {
        this.body = BoxManager.createBox(x, y, width-15, height, false, GameMap.world, 0);
        this.body.getFixtureList().first().setUserData(this);
    }

    @Override
    public PlayerBullet getPlayerBullet() {
        return kunai;
    }
}