package com.test.game;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactFilter;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Filter;
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
import com.test.game.objects.StreamZone;
import com.test.game.objects.Wall;
//import com.test.game.ListenerClass;
import com.test.game.objects.WindZone;


public class GameScreen implements Screen {
	final TestGame game;
	public Player player;
	private Wall wall;
	private World world;
	private SpriteBatch batch;
	private Texture seaTexture;
	private OrthographicCamera camera;
	private ExtendViewport viewport;
	private Box2DDebugRenderer debugRenderer;
	private InputHandler inputHandler;
	private WindZone wind;
	private StreamZone stream;
	private Listener listenerClass;
	private Filter filterClass;

	public static float SCALE = 0.05f;

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
}


	public GameScreen (final TestGame game) {


		this.game = game;
		Box2D.init();
		world = new World(new Vector2(0 ,0), true);


		listenerClass = new Listener();
		world.setContactListener(listenerClass);
		filterClass = new Filter();
		world.setContactFilter(filterClass);



		player = new Player(world, 10, 10, 0);

		wall = new Wall(world,1920,0, 0,0 );
		wall = new Wall(world, 1920, 0, 0, 50);
		wall = new Wall(world, 0, 1080, 0, 0);
		wall = new Wall(world, 0, 1080, 300, 0);

		/*wind = new WindZone(world,20,20, 20,20, new Vector2(100f,0));
		wind = new WindZone(world,20,20, 80,20, new Vector2(100f,0));
		stream = new StreamZone(world,10,10, 25,25, new Vector2(0,100f));

		stream = new StreamZone(world,2,2, 250,40, new Vector2(-10000000f,0f));*/

		stream = new StreamZone(world,300,50, 0,0, new Vector2(10f,0));
		wind = new WindZone(world,20,20, 50,20, new Vector2(0,50f));
		//Gdx.net.openURI("https://github.com/Collalt/Game");

		camera = new OrthographicCamera();
		viewport = new ExtendViewport(40, 40, camera);
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


	@Override
	public void render (float delta) {

		//ScreenUtils.clear(12f/255, 191f/255, 194f/255, 255);
		ScreenUtils.clear(0, 0, 0, 255);
		camera.position.set(player.getBody().getPosition().x+56*SCALE, player.getBody().getPosition().y+56*SCALE, 0);
		camera.update();
		inputHandler.inputHandler(player, camera);
		game.batch.setProjectionMatrix(camera.combined);

		//System.out.println(player.getForces());

		//System.out.print(wind.getBody().getUserData());
		game.batch.begin();

		//game.batch.draw(seaTexture, 10, 10);
		player.update(game.batch);
		game.font.draw(game.batch,"Vector: " + player.getBody().getLinearVelocity(), 0  , 10 );
		game.font.draw(game.batch,"Sail Angle: " + (float) Math.toDegrees(player.getSail().getAngle()), 0,-5 );
		game.font.draw(game.batch,"Forces: " + player.getForces(), player.getBody().getPosition().x-90, player.getBody().getPosition().y+50);


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


	public class Listener implements ContactListener {
		@Override
		public void endContact(Contact contact) {
			Body objectA = contact.getFixtureA().getBody();
			Body objectB = contact.getFixtureB().getBody();

			if (((ObjectInfo) objectA.getUserData()).getType() == "flow")
				player.deleteForce((((ObjectInfo) objectA.getUserData()).getId()));

			if (((ObjectInfo) objectB.getUserData()).getType() == "flow")
				player.deleteForce((((ObjectInfo) objectB.getUserData()).getId()));		}

		@Override
		public void preSolve(Contact contact, Manifold oldManifold) {
			Body objectA = contact.getFixtureA().getBody();
			Body objectB = contact.getFixtureB().getBody();

			if (((ObjectInfo) objectA.getUserData()).getType() == "sail" & ((ObjectInfo) objectB.getUserData()).getType() == "player")
				contact.setEnabled(false);


			if (((ObjectInfo) objectB.getUserData()).getType() == "sail" & ((ObjectInfo) objectA.getUserData()).getType() == "player")
				contact.setEnabled(false);
		}

		@Override
		public void postSolve(Contact contact, ContactImpulse impulse) {
		}

		@Override
		public void beginContact(Contact contact) {

			Body objectA = contact.getFixtureA().getBody();
			Body objectB = contact.getFixtureB().getBody();

			if (((ObjectInfo) objectA.getUserData()).getType() == "flow")
				player.addForce((((ObjectInfo) objectA.getUserData()).getId()), ((FlowInfo) objectA.getUserData()).getFlow());

			if (((ObjectInfo) objectB.getUserData()).getType() == "flow")
				player.addForce((((ObjectInfo) objectB.getUserData()).getId()), ((FlowInfo) objectB.getUserData()).getFlow());
					}
	}

	public class Filter implements ContactFilter{

		@Override
		public boolean shouldCollide(Fixture fixtureA, Fixture fixtureB) {
			com.badlogic.gdx.physics.box2d.Filter filterA = fixtureA.getFilterData();
			com.badlogic.gdx.physics.box2d.Filter filterB = fixtureB.getFilterData();

			if (filterA == filterB & filterA != null & filterB != null) return false;

			return true;
		}
	}
	public class InputHandler {
		ProcessorClass inputProccessor;

		public void inputHandler(Player player, OrthographicCamera camera) {

			ProcessorClass inputProccessor = new ProcessorClass();
			Gdx.input.setInputProcessor(inputProccessor);

			if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) player.getBody().applyForceToCenter(60f,0,true);
			if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) player.getBody().applyForceToCenter(-60f,0,true);
			if (Gdx.input.isKeyPressed(Input.Keys.UP)) player.getBody().applyForceToCenter(0,60f,true);
			if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) player.getBody().applyForceToCenter(0,-60f,true);

			float sailDeg = (float) Math.toDegrees(player.getSail().getAngle());



			if (Gdx.input.isKeyPressed(Input.Keys.W)) player.getSail().setTransform(player.getSail().getPosition(), (float) (player.getSail().getAngle()+ Math.toRadians(1)));
			if (Gdx.input.isKeyPressed(Input.Keys.S)) player.getSail().setTransform(player.getSail().getPosition(), (float) (player.getSail().getAngle()- Math.toRadians(1)));

		}


		public class ProcessorClass implements InputProcessor {
			@Override
			public boolean keyDown (int keycode) {
				return false;
			}

			@Override
			public boolean keyUp(int i) {
				return false;
			}

			@Override
			public boolean keyTyped(char c) {
				return false;
			}

			@Override
			public boolean touchDown(int i, int i1, int i2, int i3) {
				return false;
			}

			@Override
			public boolean touchUp(int i, int i1, int i2, int i3) {
				return false;
			}

			@Override
			public boolean touchDragged(int i, int i1, int i2) {
				return false;
			}

			@Override
			public boolean mouseMoved(int i, int i1) {
				return false;
			}

			@Override
			public boolean scrolled(float v, float v1) {

				float zoomMax = 0.4f;
				float zoomMin = 10f;
				if (v1<0 && camera.zoom > zoomMax) camera.zoom+=v1*0.055f;
				if (v1>0 && camera.zoom < zoomMin) camera.zoom+=v1*0.055f;
				System.out.println(camera.zoom);

				return false;
			}

		}
	}

}

