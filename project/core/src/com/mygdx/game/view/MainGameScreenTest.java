package com.mygdx.game.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.controller.BoxManager;
import com.mygdx.game.controller.CoinCounter;
import com.mygdx.game.controller.CustomContactListener;
import com.mygdx.game.controller.MouseHandler;
import com.mygdx.game.model.constant.PlayerState;
import com.mygdx.game.model.impl.Player.Ninja;
import static com.mygdx.game.model.constant.Constants.PPM;
import com.mygdx.game.controller.*;
import com.mygdx.game.model.Coin;
import com.mygdx.game.model.constant.ConstantSound;
import com.mygdx.game.model.constant.PlayerState;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import java.util.ArrayList;


public class MainGameScreenTest implements Screen {
    private final float SCALE  = 2.0f;
    private Box2DDebugRenderer b2dr;
    private OrthographicCamera camera;
    MyGdxGame game;
    GameMap gameMap;
    MouseHandler mouseHandler;
    public static Ninja ninja;
    CustomContactListener contactListener;
    private BitmapFont lvFont, completeFont, continuanceFont, nameFont, coinFont, scoreFont;
    private final FreeTypeFontGenerator fontGenerator;
    private Texture tableTexture;
    private Texture coinTexture;
    private GlyphLayout layout;
    private boolean levelComplete;
    private final String playerName;
    private Body platform;

    private AudioManager audioManager;
    private Music BossFightBGM, DefeatedBGM, NormalFightBGM;
    private Sound kunaiThrowSound, ninjaDeadSound, playerTeleportSound;
    private boolean hasPlayedTeleportSound = false;
    private boolean hasPlayedKunaiThrowedSound = false;
    private boolean hasPlayedNinjaDeadSound = false;

    public MainGameScreenTest(MyGdxGame game, String playerName) {
        this.game = game;
        this.playerName = playerName;
        this.fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("MinecraftRegular-Bmg3.otf"));

        this.audioManager = AudioManager.getInstance();
        NormalFightBGM = ConstantSound.normalFightBGM;
        BossFightBGM = ConstantSound.bossFightBGM;
        DefeatedBGM = ConstantSound.defeatedBGM;

        kunaiThrowSound = ConstantSound.kunaiThrowSound;
        playerTeleportSound = ConstantSound.playerTeleportSound;
        ninjaDeadSound = ConstantSound.ninjaDeadSound;
    }

    @Override
    public void show () {
        gameMap = new GameMap();
        ninja = MyGdxGame.ninja;

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, w, h);
        contactListener = new CustomContactListener(this.gameMap);
        GameMap.world.setContactListener(contactListener);
        b2dr = new Box2DDebugRenderer();

        // tạo box cho 3 góc trái phải trên dưới
        gameMap.bottomWall = BoxManager.createBox(200, 0-16, 400, 32, true, GameMap.world, 0);
        gameMap.bottomWall.getFixtureList().first().setUserData("bottomWall");
        gameMap.topWall = BoxManager.createBox(200, 720+16, 400, 32, true, GameMap.world, 0);
        gameMap.topWall.getFixtureList().first().setUserData("topWall");
        gameMap.leftWall = BoxManager.createBox(8, (float) 720 /2, 16, 720, true, GameMap.world, 0);
        gameMap.leftWall.getFixtureList().first().setUserData("wall");
        gameMap.rightWall = BoxManager.createBox(400-8, (float) 720 /2, 16, 720, true, GameMap.world, 0);
        gameMap.rightWall.getFixtureList().first().setUserData("wall");

        mouseHandler = new MouseHandler();
        Gdx.input.setInputProcessor(mouseHandler);

        layout = new GlyphLayout();
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 50;
        parameter.color = Color.RED;
        completeFont = fontGenerator.generateFont(parameter);

        parameter.size = 20;
        parameter.color = Color.WHITE;
        continuanceFont = fontGenerator.generateFont(parameter);

        parameter.size = 50;
        parameter.color = Color.WHITE;
        lvFont = fontGenerator.generateFont(parameter);

        parameter.size = 20;
        parameter.color = Color.WHITE;
        nameFont = fontGenerator.generateFont(parameter);

        parameter.size = 20;
        parameter.color = Color.YELLOW;
        coinFont = fontGenerator.generateFont(parameter);

        parameter.size = 20;
        parameter.color = Color.WHITE;
        scoreFont = fontGenerator.generateFont(parameter);

        tableTexture = new Texture(Gdx.files.internal("Button/TableHighscore.PNG"));
        coinTexture = new Texture(Gdx.files.internal("Coin/Coin(5).png"));

        Gdx.app.log("Font", "Font generated successfully.");
    }

    @Override
    public void render (float deltaTime) {
        update(Gdx.graphics.getDeltaTime());
        Gdx.gl.glClearColor(0.15f, 0.15f, 0.3f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Test sinh level
        if(Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            gameMap.getLevelManager().nextLevel();
            System.out.println(gameMap.getLevelManager().currentLevel + " " + gameMap.getLevelManager().maxLevel);
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            gameMap.getLevelManager().preLevel();
            System.out.println(gameMap.getLevelManager().currentLevel + " " + gameMap.getLevelManager().maxLevel);
        }

        // Kiểm tra và cập nhật âm thanh cho từng trường hợp level
        int currentLevel = gameMap.getLevelManager().currentLevel;
        if (currentLevel % 10 == 0 && currentLevel!= 0) {
            // Level chia hết cho 10 (ví dụ: level 10, 20, 30,...)
            if (BossFightBGM != null && !BossFightBGM.isPlaying()) {
                audioManager.playMusic(BossFightBGM);
            }
            if (NormalFightBGM != null && NormalFightBGM.isPlaying()) {
                audioManager.stopMusic(NormalFightBGM);
            }
        } else {
            // Các level còn lại
            if (NormalFightBGM != null && !NormalFightBGM.isPlaying()) {
//                NormalFightBGM.setVolume(1.0f);
                audioManager.playMusic(NormalFightBGM);
            }
            if (BossFightBGM != null && BossFightBGM.isPlaying()) {
                audioManager.stopMusic(BossFightBGM);
            }
        }

        game.batch.begin();
        gameMap.draw(game.batch);

        // vẽ UI
        String levelText = "LV " + gameMap.getLevelManager().currentLevel;
        layout.setText(lvFont, levelText);
        float textWidth = layout.width;
        float x = (Gdx.graphics.getWidth() - textWidth) / 2;
        float y = gameMap.bottomWall.getPosition().y * PPM + 130;

        lvFont.setColor(1, 1, 1, 0.1f);
        lvFont.draw(game.batch, levelText, x, y);

        // Vẽ tên người chơi
        nameFont.draw(game.batch, "Name: " + playerName, 20, Gdx.graphics.getHeight() - 10);

        // Vẽ hình ảnh coin và số lượng coin
        game.batch.draw(coinTexture, 12, Gdx.graphics.getHeight() - 40 - 26, 35, 35);
        String coinCount = String.valueOf(CoinCounter.getCoinIngame());
        coinFont.draw(game.batch, coinCount, 50, Gdx.graphics.getHeight() - 40);

        scoreFont.draw(game.batch, "Score: " + Integer.toString(GameMap.playerScore.getScore()), Gdx.graphics.getWidth() - 100, Gdx.graphics.getHeight() - 10);
        // end vẽ UI

        ninja.kunai.update();

        if (ninja.kunai.isAppear()) ninja.kunai.draw(game.batch);
        if(mouseHandler.isDrag()) {
            ninja.kunai.body.setTransform(ninja.getBody().getPosition(), 90);
            ninja.navigationArrow.setOriginCenter();
            ninja.navigationArrow.setBounds(ninja.getBody().getPosition().x*PPM - 75, ninja.getBody().getPosition().y*PPM - 10, 150, 20);
            ninja.navigationArrow.setRotation(ninja.kunai.getRotation());
            ninja.navigationArrow.draw(game.batch);
            ninja.kunai.updateRotation();
            ninja.kunai.setAppear(true);
            ninja.throwed = false;
        }

        // xử lí khi nhấn xuống
        if (ninja.getPlayerState() != PlayerState.DEAD && mouseHandler.isTouchDown()) {
            if (!hasPlayedTeleportSound) {
                audioManager.playSound(playerTeleportSound);
                hasPlayedTeleportSound = true; // Đánh dấu là đã phát âm thanh
            }
            ninja.setAppear(true);
            ninja.getBody().setTransform(ninja.kunai.body.getPosition(), 0);
            ninja.setPlayerState(PlayerState.FLASH);
            ninja.setPlace(gameMap.getLevelManager().currentLevel);

            ninja.kunai.setAppear(false);
            ninja.kunai.body.setLinearVelocity(0, 0);
        } else {
            hasPlayedTeleportSound = false; // Reset lại biến khi không còn thực hiện hành động
        }

        // xử lí khi không làm gì
        if(!mouseHandler.isDrag() && !mouseHandler.isTouchDown()) {
            // nếu kunai đang bay
            if(ninja.kunai.isAppear())  {
                if (!hasPlayedKunaiThrowedSound) {
                    hasPlayedKunaiThrowedSound = true;
                    audioManager.playSound(kunaiThrowSound);
                }
                ninja.kunai.updateSpeed();
            }
            else {
                ninja.kunai.body.setTransform(ninja.getBody().getPosition(), 90);
            }
            if(!ninja.throwed) ninja.setPlayerState(PlayerState.THROW);
        }
        else {
            hasPlayedKunaiThrowedSound = false;
        }
        if(ninja.getPlace() == gameMap.getLevelManager().currentLevel) ninja.draw(game.batch, gameMap.getStateTime());


        if (ninja.getPlayerState() == PlayerState.DEAD) {


                if (NormalFightBGM != null && NormalFightBGM.isPlaying()) {
                    NormalFightBGM.stop();
                }
                if (BossFightBGM != null && BossFightBGM.isPlaying()) {
                    BossFightBGM.stop();
                }
            if (!hasPlayedNinjaDeadSound) {
                audioManager.playSound(ninjaDeadSound);
                hasPlayedNinjaDeadSound = true; // Đánh dấu là đã phát âm thanh
            }
                if (DefeatedBGM != null && !DefeatedBGM.isPlaying()) {
                    DefeatedBGM.play();
                }
            Array<Body> bodies = new Array<>();
            GameMap.world.getBodies(bodies);
            System.out.println(bodies.size);
            for (Body body : bodies) {
                GameMap.destroyBody(body);
            }

            GameMap.playerScore.saveScore(playerName);
            CoinCounter.updateCoin(CoinCounter.getCoinIngame());
            CoinCounter.resetCoinIngame();
            game.batch.draw(tableTexture, (float) MyGdxGame.WIDTH / 2 - 180, 200, 360, 300);

            String completeText = "YOU LOSE";
            layout.setText(completeFont, completeText);
            float completeTextWidth = layout.width;
            float completeTextHeight = layout.height;
            float completeTextX = (Gdx.graphics.getWidth() - completeTextWidth) / 2;
            float completeTextY = (Gdx.graphics.getHeight() + completeTextHeight) / 2 + 50;
            completeFont.draw(game.batch, completeText, completeTextX, completeTextY);

            String continueText = "Tap to continue";
            layout.setText(continuanceFont, continueText);
            float continueTextWidth = layout.width;
            float continueTextHeight = layout.height;
            float continueTextX = (Gdx.graphics.getWidth() - continueTextWidth) / 2;
            float continueTextY = (Gdx.graphics.getHeight() - continueTextHeight) / 2 - 30;
            taptocontinueFont.draw(game.batch, continueText, continueTextX, continueTextY);

            if(Gdx.input.justTouched()) {
                if (DefeatedBGM != null && DefeatedBGM.isPlaying()) {
                    DefeatedBGM.stop();
                }
                //đang lỗi
                this.dispose();
                game.setScreen(new MainMenuScreen(game));
            }
            else {
                continuanceFont.draw(game.batch, continueText, continueTextX, continueTextY);
            }
        }


        game.batch.end();
//        b2dr.render(GameMap.world, camera.combined.scl(PPM));
    }
    @Override
    public void resize (int width, int height) {
        camera.setToOrtho(false, width, height);
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
    public void dispose() {
        if (b2dr != null) {
            b2dr.dispose();
        }
        if (GameMap.world != null) {
            GameMap.world.dispose();
        }
        if (game != null && game.batch != null) {
            game.batch.dispose();
        }
        if (fontGenerator != null) {
            fontGenerator.dispose();
        }
        if (lvFont != null) {
            lvFont.dispose();
        }
        if (completeFont != null) {
            completeFont.dispose();
        }
        if (taptocontinueFont != null) {
            taptocontinueFont.dispose();
        }
        if (nameFont != null) {
            nameFont.dispose();
        }
        if (coinFont != null) {
            coinFont.dispose();
        }
        if (scoreFont != null) {
            scoreFont.dispose();
        }
        if (tableTexture != null) {
            tableTexture.dispose();
        }
        if (coinTexture != null) {
            coinTexture.dispose();
        }
        if (BossFightBGM != null) {
            BossFightBGM.dispose();
        }
        if (DefeatedBGM != null) {
            DefeatedBGM.dispose();
        }
        if (NormalFightBGM != null) {
            NormalFightBGM.dispose();
        }
        if (kunaiThrowSound != null) {
            kunaiThrowSound.dispose();
        }
        if (ninjaDeadSound != null) {
            ninjaDeadSound.dispose();
        }
        if (playerTeleportSound != null) {
            playerTeleportSound.dispose();
        }
    }

    public void cameraUpdate(float delta) {
        Vector3 position = camera.position;
        position.x = (float) Gdx.graphics.getWidth() /2;
        position.y = (float) Gdx.graphics.getHeight() /2;
        camera.position.set(position);
        camera.update();
    }

    public void update(float delta) {
        GameMap.world.step(1 / 60f, 6, 2);
        cameraUpdate(delta);
        game.batch.setProjectionMatrix(camera.combined);
    }
}