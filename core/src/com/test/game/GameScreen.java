package com.test.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.test.game.info.FlowInfo;
import com.test.game.info.ObjectInfo;
import com.test.game.objects.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.physics.box2d.World;
import com.test.game.objects.Wall;
import com.test.game.ListenerClass;
import com.test.game.objects.WindZone;


public class GameScreen implements Screen {
	final TestGame game;
	private Player player;
	private Wall wall;
	private World world;
	private SpriteBatch batch;
	private Texture seaTexture;
	private OrthographicCamera camera;
	private ExtendViewport viewport;
	private Box2DDebugRenderer debugRenderer;
	private InputHandler inputHandler;
	private WindZone wind;
	private ListenerClass listenerClass;

	static final float SCALE = 0.05f;
	static final float STEP_TIME = 1f / 60f;
	static final int VELOCITY_ITERATIONS = 6;
	static final int POSITION_ITERATIONS = 2;

	float accumulator = 0;

private void stepWorld() {
	float delta = Gdx.graphics.getDeltaTime();

	accumulator += Math.min(delta, 0.25f);

	if (accumulator >= STEP_TIME) {
		accumulator -= STEP_TIME;

		world.step(STEP_TIME, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
	}
	//applyFlow(listenerClass.Test());
}


	public GameScreen (final TestGame game) {


		this.game = game;
		Box2D.init();
		world = new World(new Vector2(0 ,0), true);

		listenerClass = new ListenerClass();
		world.setContactListener(listenerClass);


		player = new Player(world, 10, 10, 0);

		wall = new Wall(world,1920,0, 0,0 );
		wall = new Wall(world, 1920, 0, 0, 50);
		wall = new Wall(world, 0, 1080, 0, 0);
		wall = new Wall(world, 0, 1080, 300, 0);

		wind = new WindZone(world,20,20, 20,20, new Vector2(1f,0));
		wind = new WindZone(world,20,20, 80,20, new Vector2(1f,0));

		camera = new OrthographicCamera();
		viewport = new ExtendViewport(100, 100, camera);
		debugRenderer = new Box2DDebugRenderer(true,true,true,true,true,true);
		camera.setToOrtho(false, 1920, 1080);

		batch = new SpriteBatch();
		inputHandler = new InputHandler();

		seaTexture = new Texture(Gdx.files.internal("SeaTexture.jpg"));


	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height, true);
		batch.setProjectionMatrix(camera.combined);
	}

	public void applyFlow(Vector2 flow){
		player.getBody().setLinearVelocity(player.getBody().getLinearVelocity().add(flow));
	}

	@Override
	public void render (float delta) {

		//ScreenUtils.clear(12f/255, 191f/255, 194f/255, 255);
		ScreenUtils.clear(0, 0, 0, 255);
		camera.position.set(player.getBody().getPosition().x, player.getBody().getPosition().y, 0);
		camera.update();
		inputHandler.inputHandler(player);
		game.batch.setProjectionMatrix(camera.combined);

		//System.out.print(wind.getBody().getUserData());
		game.batch.begin();

		//game.batch.draw(seaTexture, 0, 0);
		player.update(game.batch);
		game.font.draw(game.batch,"Vector: " + player.getBody().getLinearVelocity(), 0  , 0 );

		game.batch.end();
		debugRenderer.render(world, camera.combined);
		stepWorld();
	}


	@Override
	public void show() {

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
	public void dispose () {
		batch.dispose();
		seaTexture.dispose();
		player.dispose();
		world.dispose();
		debugRenderer.dispose();
	}
}
