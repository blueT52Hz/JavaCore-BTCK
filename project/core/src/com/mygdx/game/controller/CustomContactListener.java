package com.mygdx.game.controller;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.model.*;
import com.mygdx.game.model.constant.ConstantSound;
import com.mygdx.game.model.constant.EnemyState;
import com.mygdx.game.model.constant.PlayerState;
import com.mygdx.game.model.impl.Bullet.Flame;
import com.mygdx.game.model.impl.Bullet.Kunai;
import com.mygdx.game.model.impl.Player.Ninja;
import com.mygdx.game.view.Brick;
import com.mygdx.game.view.GameMap;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.audio.Music;


public class CustomContactListener implements ContactListener {
    private GameMap gameMap;

    private AudioManager audioManager;
    private Sound   kunaiHitWallSound,
                    coinEarnedSound,
                    enemyDeadSound,
                    fireballShootSound,
                    fireballHitWallSound;
                    // Âm thanh va chạm
    public CustomContactListener(GameMap gameMap) {
        this.gameMap = gameMap;
        this.audioManager = AudioManager.getInstance();
        this.kunaiHitWallSound = ConstantSound.kunaiHitWallSound; // Khởi tạo âm thanh từ ConstantSound
        this.coinEarnedSound = ConstantSound.coinEarnedSound;
        this.enemyDeadSound = ConstantSound.enemyDeadSound;
        this.fireballShootSound = ConstantSound.fireballShootSound;
        this.fireballHitWallSound = ConstantSound.fireballHitWallSound;
    }

    // Xử lý khi bắt đầu va chạm
    @Override
    public void beginContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        if(fixtureA.getUserData() == null || fixtureB.getUserData() == null) return;

        // xử lí khi EnemyBullet đập vào cạnh
        if( (fixtureA.getUserData() instanceof EnemyBullet && fixtureB.getUserData() == "wall") ||
            (fixtureB.getUserData() instanceof EnemyBullet && fixtureA.getUserData() == "wall") ||
            (fixtureA.getUserData() instanceof EnemyBullet && fixtureB.getUserData() == "topWall") ||
            (fixtureB.getUserData() instanceof EnemyBullet && fixtureA.getUserData() == "topWall") ||
            (fixtureA.getUserData() instanceof EnemyBullet && fixtureB.getUserData() == "bottomWall") ||
            (fixtureB.getUserData() instanceof EnemyBullet && fixtureA.getUserData() == "bottomWall")) {

            EnemyBullet enemyBullet = (fixtureA.getUserData() instanceof Bullet) ? (EnemyBullet) fixtureA.getUserData() : (EnemyBullet) fixtureB.getUserData();
            // nếu là đạn của quái và không thể bật tường thì bỏ nó ra khỏi list đạn của quái
            if(!enemyBullet.isCanBounce()) {
                enemyBullet.setAppear(false);
                audioManager.playSound(fireballHitWallSound);
            }
            String s = (fixtureA.getUserData() instanceof Bullet) ? (String) fixtureB.getUserData() : (String) fixtureA.getUserData();
            if(s=="topWall" || s=="bottomWall") enemyBullet.setRotation(360-enemyBullet.getRotation());
            else enemyBullet.setRotation(180-enemyBullet.getRotation());
            enemyBullet.setHandledContact(false);
        }

        // xử lý khi playerBullet đập vào cạnh
        if(     (fixtureA.getUserData() instanceof PlayerBullet && fixtureB.getUserData() == "wall") || (fixtureB.getUserData() instanceof PlayerBullet && fixtureA.getUserData() == "wall") ||
                (fixtureA.getUserData() instanceof PlayerBullet && fixtureB.getUserData() == "topWall") || (fixtureB.getUserData() instanceof PlayerBullet && fixtureA.getUserData() == "topWall") ||
                (fixtureA.getUserData() instanceof PlayerBullet && fixtureB.getUserData() == "bottomWall") || (fixtureB.getUserData() instanceof PlayerBullet && fixtureA.getUserData() == "bottomWall")) {
            PlayerBullet playerBullet = (fixtureA.getUserData() instanceof Bullet) ? (PlayerBullet) fixtureA.getUserData() : (PlayerBullet) fixtureB.getUserData();
            String s = (fixtureA.getUserData() instanceof PlayerBullet) ? (String) fixtureB.getUserData() : (String) fixtureA.getUserData();
            if(s=="topWall") {
                if(gameMap.getLevelManager().enemies.get(gameMap.getLevelManager().getCurrentLevel()).isEmpty()) gameMap.getLevelManager().setGoToNextLevel(true);
                else {
                    playerBullet.setRotation(360-playerBullet.getRotation());
                    playerBullet.setHandledContact(false);
                    audioManager.playSound(kunaiHitWallSound);

                }
            } else if(s=="bottomWall") {
                if(gameMap.getLevelManager().enemies.get(gameMap.getLevelManager().getCurrentLevel()).isEmpty() && gameMap.getLevelManager().getCurrentLevel() != 0) {
                    gameMap.getLevelManager().setGoToPreLevel(true);
                } else {
                    playerBullet.setRotation(360-playerBullet.getRotation());
                    playerBullet.setHandledContact(false);
                    audioManager.playSound(kunaiHitWallSound);

                }
            } else {
                playerBullet.setRotation(180-playerBullet.getRotation());
                playerBullet.setHandledContact(false);
                audioManager.playSound(kunaiHitWallSound);

            }
        }

        // xử lí khi playerBullet đập vào brick
        if((fixtureA.getUserData() instanceof PlayerBullet && fixtureB.getUserData() instanceof Brick|| fixtureA.getUserData() instanceof Brick && fixtureA.getUserData() instanceof PlayerBullet)) {
            PlayerBullet playerBullet = (fixtureA.getUserData() instanceof PlayerBullet) ? (PlayerBullet) fixtureA.getUserData() : (PlayerBullet) fixtureB.getUserData();
            playerBullet.setRotation(360 - playerBullet.getRotation());
            playerBullet.setHandledContact(false);
            audioManager.playSound(kunaiHitWallSound);

        }

        // xử lí khi player trúng enemyBullet ==> gameover
        if((fixtureA.getUserData() instanceof EnemyBullet && fixtureB.getUserData() instanceof Player || fixtureA.getUserData() instanceof Player && fixtureB.getUserData() instanceof EnemyBullet)) {
            System.out.println("Nhân vật trúng đạn");
            EnemyBullet bullet = (fixtureA.getUserData() instanceof EnemyBullet) ? (EnemyBullet) fixtureA.getUserData() : (EnemyBullet) fixtureB.getUserData();
            Player player = (fixtureA.getUserData() instanceof EnemyBullet) ? (Player) fixtureB.getUserData() : (Player) fixtureA.getUserData();
            if (player.getPlayerState() == PlayerState.IDLE || player.getPlayerState() == PlayerState.GLIDE || player.getPlayerState() == PlayerState.THROW)
                player.setPlayerState(PlayerState.DEAD);
            if(bullet instanceof EnemyBullet && !bullet.isCanBounce()) {
                bullet.setAppear(false);
//                System.out.println("Remove EnemyBullet");
            }
        }


        // xử lí khi enemyBullet trúng playerBullet
        if((fixtureA.getUserData() instanceof PlayerBullet && fixtureB.getUserData() instanceof EnemyBullet || fixtureA.getUserData() instanceof EnemyBullet && fixtureB.getUserData() instanceof PlayerBullet)) {
            EnemyBullet enemyBullet = (fixtureA.getUserData() instanceof EnemyBullet) ? (EnemyBullet) fixtureA.getUserData() : (EnemyBullet) fixtureB.getUserData();
            enemyBullet.setAppear(false);
            audioManager.playSound(fireballHitWallSound);
            audioManager.playSound(kunaiHitWallSound);
        }


        if((fixtureA.getUserData() instanceof PlayerBullet && fixtureB.getUserData() instanceof Enemy || fixtureA.getUserData() instanceof Enemy && fixtureB.getUserData() instanceof PlayerBullet)) {
//            System.out.println("Quái trúng đạn");
            PlayerBullet playerBullet = (fixtureA.getUserData() instanceof Enemy) ? (PlayerBullet) fixtureB.getUserData() : (PlayerBullet)fixtureA.getUserData();
            Enemy enemy = (fixtureA.getUserData() instanceof Enemy) ? (Enemy) fixtureA.getUserData() : (Enemy) fixtureB.getUserData();
            if(enemy.getEnemyState() != EnemyState.DEAD) {
                enemy.setEnemyState(EnemyState.DEAD);
                enemy.setStateTime(0);
                switch (enemy.getLevel()) {
                    case 1:
                        GameMap.playerScore.addScore(1);
                        break;
                    case 2:
                        GameMap.playerScore.addScore(5);
                        break;
                    case 3:
                        GameMap.playerScore.addScore(15);
                        break;
                }
            }
            audioManager.playSound(kunaiHitWallSound);
            audioManager.playSound(enemyDeadSound);
        }

        if( fixtureA.getUserData() instanceof Coin && fixtureB.getUserData() instanceof PlayerBullet || fixtureA.getUserData() instanceof PlayerBullet && fixtureB.getUserData() instanceof Coin ||
            fixtureA.getUserData() instanceof Coin && fixtureB.getUserData() instanceof Player       || fixtureA.getUserData() instanceof Player && fixtureB.getUserData() instanceof Coin) {

            Coin coin = (fixtureA.getUserData() instanceof Coin) ? (Coin) fixtureA.getUserData() : (Coin) fixtureB.getUserData();
            CoinCounter.addCoinIngame(coin.getValue());
            coin.setAppear(false);
            audioManager.playSound(coinEarnedSound);
        }

    }

    @Override
    public void endContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();
        if(fixtureA.getUserData() == null || fixtureB.getUserData() == null) return;

        if (    (fixtureA.getUserData() instanceof Player && fixtureB.getUserData() == "bottomWall")  || (fixtureB.getUserData() instanceof Player && fixtureA.getUserData() == "bottomWall") ||
                (fixtureA.getUserData() instanceof Player && fixtureB.getUserData() instanceof Brick)     || (fixtureB.getUserData() instanceof Player && fixtureA.getUserData() instanceof Brick)) {
            Player player = (fixtureA.getUserData() instanceof Player) ? (Player) fixtureA.getUserData() : (Player) fixtureB.getUserData();
            player.setPlayerState(PlayerState.GLIDE);
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold manifold) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();
        if(fixtureA.getUserData() == null || fixtureB.getUserData() == null) return;

        if (    (fixtureA.getUserData() instanceof Player && fixtureB.getUserData() == "bottomWall")  || (fixtureB.getUserData() instanceof Player && fixtureA.getUserData() == "bottomWall") ||
                (fixtureA.getUserData() instanceof Player && fixtureB.getUserData() instanceof Brick)     || (fixtureB.getUserData() instanceof Player && fixtureA.getUserData() instanceof Brick)) {
            Player player = (fixtureA.getUserData() instanceof Player) ? (Player) fixtureA.getUserData() : (Player) fixtureB.getUserData();
            player.setPlayerState(PlayerState.IDLE);
        }

        if( fixtureA.getUserData() instanceof Coin || fixtureB.getUserData() instanceof Coin) {
            contact.setEnabled(false);
        }

        if (    (fixtureA.getUserData() instanceof Player && fixtureB.getUserData() instanceof PlayerBullet) || (fixtureA.getUserData() instanceof PlayerBullet && fixtureB.getUserData() instanceof Player)||
                (fixtureA.getUserData() instanceof Enemy && fixtureB.getUserData() instanceof EnemyBullet)   || (fixtureA.getUserData() instanceof EnemyBullet && fixtureB.getUserData() instanceof Enemy)  ||
                (fixtureA.getUserData() instanceof EnemyBullet && fixtureB.getUserData() instanceof EnemyBullet)   ||
                (fixtureA.getUserData() instanceof Brick && fixtureB.getUserData() instanceof EnemyBullet)   || (fixtureA.getUserData() instanceof EnemyBullet && fixtureB.getUserData() instanceof Brick)) {
            // Vô hiệu hóa va chạm đạn đồng minh
            contact.setEnabled(false);
        }

        if((fixtureA.getUserData() instanceof PlayerBullet && fixtureB.getUserData() instanceof Enemy || fixtureA.getUserData() instanceof Enemy && fixtureB.getUserData() instanceof PlayerBullet)) {
            contact.setEnabled(false);

        }

        if(fixtureA.getUserData() instanceof Enemy && fixtureB.getUserData() instanceof Brick || fixtureA.getUserData() instanceof Brick && fixtureB.getUserData() instanceof Enemy) {
            contact.setEnabled(false);
        }


    }
    @Override
    public void postSolve(Contact contact, ContactImpulse contactImpulse) {

    }
}

