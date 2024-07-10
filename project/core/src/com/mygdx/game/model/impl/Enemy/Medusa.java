package com.mygdx.game.model.impl.Enemy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.TimeUtils;
import com.mygdx.game.controller.AudioManager;
import com.mygdx.game.controller.BoxManager;
import com.mygdx.game.model.Coin;
import com.mygdx.game.model.EnemyBullet;
import com.mygdx.game.model.Player;
import com.mygdx.game.model.constant.ConstantSound;
import com.mygdx.game.model.constant.EnemyState;
import com.mygdx.game.model.impl.Bullet.Flame;
import com.mygdx.game.view.Brick;
import com.mygdx.game.model.Enemy;
import com.mygdx.game.view.GameMap;
import org.w3c.dom.Text;

import java.util.ArrayList;

import static com.mygdx.game.model.constant.Constants.PPM;

public class Medusa extends Enemy {
    private AudioManager audioManager;
    private Sound fireballShootSound;
    private long lastTimeAttack;
    public Medusa(float x, float y, int level, Brick brick, Player target) {
        super(level, brick, target);
        setEnemyTilePath();
        setHeight(level*80);
        setWidth(level*80);
        loadAnimation();
        this.x = x;
        this.y = y-20*level;
        System.out.println("x = " + x + " y = " + y);
        this.stateTime = 0;
        this.dead = false;

        this.lastTimeAttack = TimeUtils.millis();
        this.enemyState = EnemyState.MOVE;
        this.speed = 10 + this.brick.getxSpeed();
        createBody();
        System.out.println("xPos = " + body.getPosition().x*PPM + " yPos = " + body.getPosition().y*PPM);

        this.audioManager = AudioManager.getInstance();
        this.fireballShootSound = ConstantSound.fireballShootSound;

    }

    @Override
    public void createBody() {
        this.body = BoxManager.createBox(x, y, width-40*level, height-40*level, false, GameMap.world, 0);
        this.body.getFixtureList().first().setUserData(this);
        this.body.setGravityScale(0);
    }

    @Override
    protected void setEnemyTilePath() {}

    @Override
    public void draw(SpriteBatch spriteBatch, float gameMapStateTime) {
        update();
        for (EnemyBullet enemyBullet : enemyBullets) {
            enemyBullet.draw(spriteBatch);
        }

        switch (enemyState) {
            case MOVE:
                if(this.speed>0) spriteBatch.draw((Texture) moveRightAnimation.getKeyFrame(gameMapStateTime, true), x, y, this.width, this.height);
                if (this.speed<=0) spriteBatch.draw((Texture) moveLeftAnimation.getKeyFrame(gameMapStateTime, true), x, y, this.width, this.height);
                break;
            case DEAD:
                Texture deadImg = (this.speed>=0) ? (Texture) deathRightAnimation.getKeyFrame(this.stateTime, false) : (Texture) deathLeftAnimation.getKeyFrame(this.stateTime, false);
                this.stateTime += Gdx.graphics.getDeltaTime();
                this.lastTimeAttack = TimeUtils.millis();
                if(deadImg==null || this.stateTime >= 50 * Gdx.graphics.getDeltaTime()) {
                    this.stateTime = 0;
                    this.dead = true;
                    return;
                }
                spriteBatch.draw(deadImg, x, y, this.width, this.height);
                break;
            case ATTACK:
                Texture attackImg = (this.speed>=0) ? (Texture) attackRightAnimation.getKeyFrame(this.stateTime, false) : (Texture) attackLeftAnimation.getKeyFrame(this.stateTime, false);
                this.stateTime += Gdx.graphics.getDeltaTime();
                if(attackImg==null || this.stateTime >= 70 * Gdx.graphics.getDeltaTime()) {
                    this.stateTime = 0;
                    spawnEnemyBullet();

                    this.enemyState = EnemyState.MOVE;
                }
                if(this.speed>0) spriteBatch.draw(attackImg, x, y, this.width, this.height);
                if (this.speed<=0) spriteBatch.draw(attackImg, x, y, this.width, this.height);
                break;
        }

    }



    @Override
    public void update() {
        ArrayList<EnemyBullet> enemyBulletsRemove = new ArrayList<>();
        for(EnemyBullet enemyBullet : enemyBullets) {
            if(enemyBullet.isAppear()) enemyBullet.update();
            else enemyBulletsRemove.add(enemyBullet);
        }
        for (EnemyBullet enemyBullet : enemyBulletsRemove) {
            GameMap.destroyBody(enemyBullet.getBody());
            this.enemyBulletsBodies.remove(enemyBullet.getBody());
            this.enemyBullets.remove(enemyBullet);
        }
        if(TimeUtils.millis() - lastTimeAttack > 3000/level && enemyState != EnemyState.DEAD) {
            enemyState = EnemyState.ATTACK;
        }

        if(enemyState == EnemyState.MOVE) {
            x += speed * Gdx.graphics.getDeltaTime();
            if(this.x<=this.brick.getX()-20*level) {
                this.x=this.brick.getX()-20*level;
                speed = -speed;
            }
            if(this.x>=this.brick.getX()+this.brick.getWidth()+20*level-this.width) {
                x = this.brick.getX()+this.brick.getWidth()+20*level-this.width;
                speed = -speed;
            }
        }
        body.setTransform((x+width/2)/PPM, (y+height/2)/PPM, 0);
    }

    public void spawnEnemyBullet() {
        lastTimeAttack = TimeUtils.millis();
        if(!target.isAppear()) return;
        audioManager.playSound(fireballShootSound);
        Flame flame = new Flame(x+width/2, y+height/2, this);
        flame.updateRotation(target.getX(), target.getY());
        enemyBullets.add(flame);
        enemyBulletsBodies.add(flame.getBody());
    }

    @Override
    public void loadAnimation() {
        Texture[] moveRightImgs = new Texture[4];
        Texture[] moveLeftImgs = new Texture[4];
        Texture[] deathRightImgs = new Texture[6];
        Texture[] deathLeftImgs = new Texture[6];
        Texture[] attackRightImgs = new Texture[6];
        Texture[] attackLeftImgs = new Texture[6];
        for(int i=1;i<=4;++i) {
            moveRightImgs[i-1] = new Texture(String.format("entities/medusa/MoveRight%d.png", i));
            moveLeftImgs[i-1] = new Texture(String.format("entities/medusa/MoveLeft%d.png", i));
        }
        for(int i=1;i<=6;++i) {
            deathRightImgs[i-1] = new Texture(String.format("entities/medusa/DeathRight%d.png", i));
            deathLeftImgs[i-1] = new Texture(String.format("entities/medusa/DeathLeft%d.png", i));
            attackRightImgs[i-1] = new Texture(String.format("entities/medusa/AttackRight%d.png", i));
            attackLeftImgs[i-1] = new Texture(String.format("entities/medusa/AttackLeft%d.png", i));
        }
        this.moveRightAnimation = new Animation<>(0.05f, moveRightImgs);
        this.moveLeftAnimation = new Animation<>(0.05f, moveLeftImgs);
        this.deathRightAnimation = new Animation<>(0.2f, deathRightImgs);
        this.deathLeftAnimation = new Animation<>(0.2f, deathLeftImgs);
        this.attackRightAnimation = new Animation<>(0.2f, attackRightImgs);
        this.attackLeftAnimation = new Animation<>(0.2f, attackLeftImgs);
    }
}