package io.github.fourlastor.game.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.github.fourlastor.game.GdxGame;

public class LevelScreen extends ScreenAdapter {

    private static final float STEP = 1f / 60f;

    private final GdxGame game;
    private final World world;
    private final Body body;

    private final Stage stage;
    private final Image image;

    public LevelScreen(GdxGame game) {
        this.game = game;
        world = new World(new Vector2(), true);
        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.KinematicBody;
        body = world.createBody(def);
        FixtureDef fixtureDef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(0.5f, 0.5f);
        fixtureDef.shape = shape;
        body.createFixture(fixtureDef);
        shape.dispose();

        stage = new Stage(new ExtendViewport(32, 20));
        image = new Image(new Texture(Gdx.files.internal("images/included/whitePixel.png")));
        stage.addActor(image);
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height);
    }

    private float accumulator = 0f;
    private final Vector2 velocity = new Vector2();

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.DARK_GRAY, true);
        velocity.set(0f,0f);
        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            world.dispose();
            stage.dispose();
            game.goToLevel();
            return;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            velocity.y += 3f;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            velocity.y -= 3f;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            velocity.x -= 3f;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            velocity.x += 3f;
        }
        body.setLinearVelocity(velocity);
        int count = 0;
        accumulator += delta;
        while (accumulator > STEP) {
            world.step(STEP, 6, 2);
            accumulator -= STEP;
            count += 1;
        }
        if (count != 1) {
            System.out.printf("!!! Step ran %d times\n", count);
        }
        Vector2 position = body.getPosition();
        image.setPosition(position.x, position.y, Align.center);
        stage.act();
        stage.draw();
    }
}
