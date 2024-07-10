package com.mygdx.game.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.controller.CoinCounter;
import com.mygdx.game.model.PlayerInventory;
import com.mygdx.game.model.StatusSkinAndArm;
import com.mygdx.game.model.impl.Bullet.Weapon;
import com.badlogic.gdx.graphics.Pixmap;
import com.mygdx.game.model.impl.Player.Ninja;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StoreArmsScreen extends BaseScreen {
    private final MyGdxGame game;
    private final Texture coinTexture;
    private final Texture kunaiTexture;
    private final Texture bombTexture;
    private final Texture bomerangTexture;
    private final Texture dartTexture;
    private final Texture foldingFanTexture;
    private final Texture hammerTexture;
    private final Texture shurikenTexture;
    private final Texture shieldTexture;
    private final Texture rugbyTexture;
    private final Texture skinsTexture;
    private final Texture usingTexture;
    private Ninja player;

    ArrayList<Weapon> weapons = new ArrayList<Weapon>();
    private BitmapFont coinFont;
    private FreeTypeFontGenerator.FreeTypeFontParameter parameter;
    private FreeTypeFontGenerator fontGenerator;

    public StoreArmsScreen(MyGdxGame game) {
        super(game);
        this.game = game;
        coinTexture = new Texture(Gdx.files.internal("Coin/Coin(5).png"));
        kunaiTexture = new Texture(Gdx.files.internal("Bullet/Kunai.png"));
        bombTexture = new Texture(Gdx.files.internal("Bullet/Bomb.png"));
        shieldTexture = new Texture(Gdx.files.internal("Bullet/Shield.png"));
        rugbyTexture = new Texture(Gdx.files.internal("Bullet/Rugby.png"));
        hammerTexture = new Texture(Gdx.files.internal("Bullet/Hammer.png"));
        bomerangTexture = new Texture(Gdx.files.internal("Bullet/Bomerang.png"));
        foldingFanTexture = new Texture(Gdx.files.internal("Bullet/FoldingFan.png"));
        shurikenTexture = new Texture(Gdx.files.internal("Bullet/Shuriken.png"));
        dartTexture = new Texture(Gdx.files.internal("Bullet/Dart.png"));
        skinsTexture = new Texture(Gdx.files.internal("Button/SkinsButton.png"));
        usingTexture = new Texture(Gdx.files.internal("using.png"));
        this.fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("MinecraftRegular-Bmg3.otf"));
        this.parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
    }

    @Override
    protected void onBackButtonPressed() {
        game.setScreen(new MainMenuScreen(game));
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        loadWeaponsFromJSON("Weapons.json");
    }

    private void handleWeaponButtonClick(int index) {
        Weapon weapon = weapons.get(index);
        if (CoinCounter.getCoinIngame() >= weapon.getPrice() && !weapon.isUnlocked()) {
            CoinCounter.updateCoin(-weapon.getPrice());
            weapon.setUnlocked(true);
            PlayerInventory.addItem(weapon.getName());
        }
        for (Weapon w : weapons) {
            w.setEquipped(false);
        }
        weapon.setEquipped(true);
        StatusSkinAndArm.armState = weapon.getName();
        MyGdxGame.ninja.changeArm(index);
    }

    private void loadWeaponsFromJSON(String filename) {
        FileReader reader = null;
        try {
            reader = new FileReader(filename);
            Type classOfT = new TypeToken<ArrayList<Weapon>>(){}.getType();
            Gson gson = new Gson();
            weapons = gson.fromJson(reader, classOfT);
            for (Weapon weapon : weapons) {
                weapon.display();
            }
        } catch (FileNotFoundException e) {
            Logger.getLogger(StoreArmsScreen.class.getName()).log(Level.SEVERE,null, e);
        } finally {
            if (reader != null){
                try{
                    reader.close();
                }catch (IOException ex){
                    Logger.getLogger(StoreArmsScreen.class.getName()).log(Level.SEVERE,null, ex);
                }
            }
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.15f, 0.15f, 0.3f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();
        game.batch.draw( new Texture("Button/TableHighscore.PNG"), (float) MyGdxGame.WIDTH / 2 - 180, 180, 360, 360);
        parameter.size = 20;
        parameter.color = Color.YELLOW;
        coinFont = fontGenerator.generateFont(parameter);
        game.batch.draw(coinTexture, 300, Gdx.graphics.getHeight() - 40 - 10, 35, 35);
        String coinCount = String.valueOf(CoinCounter.getCoinIngame());
        coinFont.draw(game.batch, coinCount, 355, Gdx.graphics.getHeight() - 25);
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);
        Texture[] weaponTextures = {kunaiTexture, bombTexture, bomerangTexture, dartTexture, foldingFanTexture, hammerTexture, shurikenTexture, shieldTexture, rugbyTexture};

        int width = 90;
        int height = 90;
        int radius = 15;
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);

        pixmap.setColor(Color.CLEAR);
        pixmap.fill();
        pixmap.setColor(Color.WHITE);
        pixmap.fillCircle(radius, radius, radius);
        pixmap.fillCircle(width - radius, radius, radius);
        pixmap.fillCircle(radius, height - radius, radius);
        pixmap.fillCircle(width - radius, height - radius, radius);
        pixmap.fillRectangle(radius, 0, width - 2 * radius, height);
        pixmap.fillRectangle(0, radius, width, height - 2 * radius);

        Texture whiteTexture = new Texture(pixmap);
        TextureRegionDrawable whiteBackground = new TextureRegionDrawable(new TextureRegion(whiteTexture));

        pixmap.setColor(Color.CLEAR);
        pixmap.fill();
        pixmap.setColor(0.85f, 0.85f, 0.85f, 1f);
        pixmap.fillCircle(radius, radius, radius);
        pixmap.fillCircle(width - radius, radius, radius);
        pixmap.fillCircle(radius, height - radius, radius);
        pixmap.fillCircle(width - radius, height - radius, radius);
        pixmap.fillRectangle(radius, 0, width - 2 * radius, height);
        pixmap.fillRectangle(0, radius, width, height - 2 * radius);

        Texture darkTexture = new Texture(pixmap);
        pixmap.dispose();
        TextureRegionDrawable darkBackground = new TextureRegionDrawable(new TextureRegion(darkTexture));

        ImageButton.ImageButtonStyle skinsButtonStyle = new ImageButton.ImageButtonStyle();
        skinsButtonStyle.imageUp = new TextureRegionDrawable(new TextureRegion(skinsTexture));
        skinsButtonStyle.up = whiteBackground;
        ImageButton skinsButton = new ImageButton(skinsButtonStyle);
        skinsButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new StoreSkinsScreen(game));
                return true;
            }
        });
        skinsButton.setBounds(MyGdxGame.WIDTH - 150, 570 , 90, 90);

        stage.addActor(skinsButton);

        for (int i = 0; i < weaponTextures.length; i++) {
            final int index = i;
            ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
            style.imageUp = new TextureRegionDrawable(new TextureRegion(weaponTextures[i]));
            style.up = whiteBackground;
            style.over = darkBackground;
            ImageButton weaponButton = new ImageButton(style);
            weaponButton.addListener(new InputListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    handleWeaponButtonClick(index);
                    return true;
                }
            });
            Table buttonTable = new Table();
            buttonTable.setBackground(whiteBackground);
            buttonTable.add(weaponButton).size(70, 70).center();

            table.add(buttonTable).pad(10).size(90, 90);
            if ((i + 1) % 3 == 0) {
                table.row();
            }

            Texture lockTexture = new Texture(Gdx.files.internal("lock.png"));

            if (!weapons.get(i).isUnlocked()) {
                // Thêm biểu tượng khóa
                Image lockImage = new Image(lockTexture);
                lockImage.setSize(20, 20);
                lockImage.setPosition(50, 50);
                Table lockTable = new Table();
                lockTable.add(lockImage).size(10, 10).top().right().padTop(-5).padRight(-5);
                weaponButton.addActor(lockTable);
            }
            if (weapons.get(i).isEquipped()) {
                Image usingImage = new Image(usingTexture);
                usingImage.setSize(15, 15);
                usingImage.setPosition(60, 60);
                weaponButton.addActor(usingImage);
            }
        }
        game.batch.end();
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }


    @Override
    public void dispose() {
        stage.dispose();
        coinTexture.dispose();
        kunaiTexture.dispose();
        bombTexture.dispose();
        shieldTexture.dispose();
        rugbyTexture.dispose();
        hammerTexture.dispose();
        bomerangTexture.dispose();
        foldingFanTexture.dispose();
        shurikenTexture.dispose();
        dartTexture.dispose();
    }
}
