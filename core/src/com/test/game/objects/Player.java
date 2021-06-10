
package com.test.game.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.utils.ArrayMap;
import com.codeandweb.physicseditor.PhysicsShapeCache;
import com.test.game.info.ObjectInfo;

import static com.test.game.GameScreen.SCALE;


public class Player {
    private Texture texture;
    private Sprite sprite;
    private Body body;
    private Body sail;
    private BodyDef sailBd;
    private FixtureDef sailFd;
    private PolygonShape polygonShape;
    private Texture sailTexture;
    private Sprite sailSprite;
    private Texture penguinTexture;
    private Sprite penguinSprite;
    PhysicsShapeCache physicsShapeCache;
    private ArrayMap<String, Vector2> forces;

    public Player(World world, float x, float y, float angle) {


        physicsShapeCache = new PhysicsShapeCache("iceberg.xml");
        texture = new Texture("ldyshko.png");
        sprite = new Sprite(texture);
        sprite.setScale(SCALE);
        body = physicsShapeCache.createBody("ldyshko", world, SCALE, SCALE);
        body.setTransform(x, y, angle);

        MassData dataMass = new MassData();
        dataMass.mass = 10;
        dataMass.I = -0.01f;
        dataMass.center.set(sprite.getWidth()/2, sprite.getHeight()/2);

        //body.setMassData(dataMass);
        body.setUserData(new ObjectInfo("player"));

        sailTexture = new Texture("parusus.png");
        sailSprite = new Sprite(sailTexture);
        sailSprite.setScale(SCALE);
        sailBd = new BodyDef();
        sailBd.type = BodyDef.BodyType.DynamicBody;
        sail = world.createBody(sailBd);

        sailFd = new FixtureDef();
        sailFd.filter.categoryBits = 1;
        polygonShape = new PolygonShape();
        sailFd.shape = polygonShape;


        polygonShape.setAsBox(sailTexture.getWidth()/2*SCALE, sailTexture.getHeight()/2*SCALE);
        sail.createFixture(sailFd);


        float radians = (float) Math.toRadians(90);

        sail.setTransform(x+(56*SCALE), y+(56*SCALE), radians);
        sail.setUserData(new ObjectInfo("sail"));

        RevoluteJointDef jointDef = new RevoluteJointDef();


        jointDef.bodyA = body;
        jointDef.bodyB = sail;

        jointDef.initialize(jointDef.bodyA, jointDef.bodyB, new Vector2(x+(56*SCALE), y+(56*SCALE)));

        RevoluteJoint joint = (RevoluteJoint) world.createJoint(jointDef);

        forces = new ArrayMap<String, Vector2>();

        penguinTexture = new Texture("penguinus.png");
        penguinSprite = new Sprite(sailTexture);
        penguinSprite.setScale(SCALE);

    }

    public Texture getTexture() {
        return texture;
    }

    public Body getBody (){
        return body;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void addForce(String id, Vector2 force){
        if (forces.get(id) == null) forces.put(id, force);
    }

    public void deleteForce(String id){
        if (forces.get(id) != null) forces.removeKey(id);
    }

    public void applyForces(){
        float sailDegrees = (float) Math.toDegrees(sail.getAngle());

        //Wind power on sail F = 0,1· S · v2 · f(i) i = deg
        //vector.x/10 vector.y/10, s = 1, v = 10, f(i) = 2sin//(1+sin2i)
        for (Vector2 vector : forces.values())
        {
            float i = (float) ((2 * Math.sin(Math.toRadians(sailDegrees)))/(1+Math.toRadians(2*sailDegrees)));
            if (sailDegrees<0) sailDegrees*=(-1);
            if (sailDegrees>180) sailDegrees-=180;
            vector.angleDeg(new Vector2(0f,0f));
          //  System.out.println(sailDegrees);
            float sin = (float) Math.sin(Math.toDegrees(i));
           // System.out.println(sin);
            //System.out.println(Math.sin(vector.angleDeg(new Vector2(0f,0f))));
            //System.out.println(sailDegrees);
            body.applyForceToCenter(vector.x * sin, vector.y * sin , true);
        }
    }

    public void update(Batch batch){
        float playerDegrees = (float) Math.toDegrees(body.getAngle());
        float sailDegrees = (float) Math.toDegrees(sail.getAngle());

        sprite.setPosition(body.getPosition().x , body.getPosition().y);
        //sprite.setOrigin(body.getPosition().x, body.getPosition().y);
        sprite.setOrigin(0f,0f);
        sprite.setRotation(playerDegrees);


        sailSprite.setPosition(sail.getPosition().x, sail.getPosition().y);
        sailSprite.setOriginBasedPosition(sail.getPosition().x, sail.getPosition().y);
        sailSprite.setOrigin(sailSprite.getWidth()/2,sailSprite.getHeight()/2);
        sailSprite.setRotation(sailDegrees);

        penguinSprite.setPosition(body.getPosition().x , body.getPosition().y);
        penguinSprite.setOrigin(penguinSprite.getWidth()/2,penguinSprite.getHeight()/2);


        sprite.draw(batch);
        sailSprite.draw(batch);
        penguinSprite.draw(batch);

        applyForces();

    }

    public ArrayMap<String, Vector2> getForces() {
        return forces;
    }

    public Body getSail() {
        return sail;
    }

    public void dispose(){
        texture.dispose();
    }




}



