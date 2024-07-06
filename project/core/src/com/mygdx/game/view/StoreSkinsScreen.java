package com.mygdx.game.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
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
import com.mygdx.game.model.StatusSkinAndArm;
import com.mygdx.game.model.impl.Player.Ninja;
import com.mygdx.game.model.impl.Player.Skin;
import com.mygdx.game.controller.CoinCounter;
import com.mygdx.game.model.PlayerInventory;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StoreSkinsScreen extends BaseScreen {
    private final MyGdxGame game;
    private final Texture coinTexture;
    private final Texture ninjaTexture;
    private final Texture ninjaGirlTexture;
    private final Texture armsTexture;
    private final Texture usingTexture;
    private ArrayList<Skin> skins = new ArrayList<Skin>();
    private BitmapFont coinFont;
    private FreeTypeFontGenerator.FreeTypeFontParameter parameter;
    private FreeTypeFontGenerator fontGenerator;

    public StoreSkinsScreen(MyGdxGame game) {
        super(game);
        this.game = game;
        coinTexture = new Texture(Gdx.files.internal("Coin/Coin(5).png"));
        ninjaTexture = new Texture(Gdx.files.internal("Entities/ninja/Throw__009.png"));
        ninjaGirlTexture = new Texture(Gdx.files.internal("Entities/ninjagirl/Throw__009.png"));
        armsTexture = new Texture(Gdx.files.internal("Button/ArmsButton.png"));
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
        loadSkinsFromJSON("Skins.json");
    }

    private void handleSkinButtonClick(int index) {
        // Xử lý logic khi click vào button skin
        Skin skin = skins.get(index);
        if (CoinCounter.getCoinIngame() >= skin.getPrice() && !skin.isUnlocked()) {
            CoinCounter.updateCoin(-skin.getPrice()); // Trừ tiền
            skin.setUnlocked(true); // Đánh dấu skin đã mở khóa
            PlayerInventory.addItem(skin.getName()); // Thêm skin vào kho
        }
        for (Skin s : skins) {
            s.setEquipped(false);
        }
        skin.setEquipped(true);
        StatusSkinAndArm.skinState = skin.getName();
        MyGdxGame.ninja.changeSkin(index);
    }

    private void loadSkinsFromJSON(String filename) {
        // Đọc các skin từ file JSON
        FileReader reader = null;
        try {
            reader = new FileReader(filename);
            Type classOfT = new TypeToken<ArrayList<Skin>>(){}.getType();
            Gson gson = new Gson();
            skins = gson.fromJson(reader, classOfT);
            for (Skin skin : skins) {
                skin.display(); // Hiển thị thông tin skin (debug)
            }
        } catch (FileNotFoundException e) {
            Logger.getLogger(StoreArmsScreen.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ex) {
                    Logger.getLogger(StoreArmsScreen.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    @Override
    public void render(float delta) {
        // Vẽ màn hình
        Gdx.gl.glClearColor(0.15f, 0.15f, 0.3f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();
        game.batch.draw(new Texture("Button/TableHighscore.PNG"), (float) MyGdxGame.WIDTH / 2 - 180, 180, 360, 360);

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);
        // Vẽ số coin
        parameter.size = 20;
        parameter.color = Color.YELLOW;
        coinFont = fontGenerator.generateFont(parameter);
        game.batch.draw(coinTexture, 300, Gdx.graphics.getHeight() - 40 - 10, 35, 35);
        String coinCount = String.valueOf(CoinCounter.getCoinIngame());
        coinFont.draw(game.batch, coinCount, 355, Gdx.graphics.getHeight() - 25);
        Texture[] skinTextures = {ninjaTexture, ninjaGirlTexture};

        int width = 90;
        int height = 90;
        int radius = 15;
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);

        // Tạo background cho các button skin
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

        ImageButton.ImageButtonStyle armsButtonStyle = new ImageButton.ImageButtonStyle();
        armsButtonStyle.imageUp = new TextureRegionDrawable(new TextureRegion(armsTexture));
        armsButtonStyle.up = whiteBackground;
        ImageButton armsButton = new ImageButton(armsButtonStyle);
        armsButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new StoreArmsScreen(game));
                return true;
            }
        });
        armsButton.setBounds(MyGdxGame.WIDTH - 150, 570, 90, 90);

        stage.addActor(armsButton);
        for (int i = 0; i < skinTextures.length; i++) {
            final int index = i;
            ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
            style.imageUp = new TextureRegionDrawable(new TextureRegion(skinTextures[i]));
            style.up = whiteBackground;
            style.over = darkBackground;
            ImageButton skinButton = new ImageButton(style);
            skinButton.addListener(new InputListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    handleSkinButtonClick(index);
                    return true;
                }
            });
            Table buttonTable = new Table();
            buttonTable.setBackground(whiteBackground);
            buttonTable.add(skinButton).size(70, 70).center();

            table.add(buttonTable).pad(10).size(90, 90);
            if ((i + 1) % 3 == 0) {
                table.row();
            }

            // Thêm hình ảnh khóa nếu skin chưa được mở khóa
            Texture lockTexture = new Texture(Gdx.files.internal("lock.png"));
            if (!skins.get(i).isUnlocked()) {
                Image lockImage = new Image(lockTexture);
                lockImage.setSize(20, 20);
                lockImage.setPosition(50, 50);
                Table lockTable = new Table();
                lockTable.add(lockImage).size(10, 10).top().right().padTop(-5).padRight(-5);
                skinButton.addActor(lockTable);
            }

            // Thêm hình ảnh đang sử dụng nếu skin đã được chọn
            if (skins.get(i).isEquipped()) {
                Image usingImage = new Image(usingTexture);
                usingImage.setSize(15, 15);
                usingImage.setPosition(60, 60);
                skinButton.addActor(usingImage);
            }
        }
        game.batch.end();
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
        coinTexture.dispose();
        ninjaTexture.dispose();
        armsTexture.dispose();
        fontGenerator.dispose();
        ninjaGirlTexture.dispose();
        usingTexture.dispose();
    }
}
