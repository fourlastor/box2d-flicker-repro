package io.github.fourlastor.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import io.github.fourlastor.game.level.LevelScreen;

public class GdxGame extends Game {

    private Screen pendingScreen = null;

    public GdxGame() {
    }

    @Override
    public void create() {
        goToLevel();
    }

    public void goToLevel() {
        pendingScreen = new LevelScreen(this);
    }

    @Override
    public void render() {
        if (pendingScreen != null) {
            setScreen(pendingScreen);
            pendingScreen = null;
        }
        super.render();
    }

    public static GdxGame createGame() {
        return new GdxGame();
    }

}
