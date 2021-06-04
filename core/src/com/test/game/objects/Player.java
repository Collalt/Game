
package com.test.game.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.ArrayMap;
import com.codeandweb.physicseditor.PhysicsShapeCache;
import com.test.game.info.ObjectInfo;

import static com.test.game.GameScreen.SCALE;


public class Player {
    private Texture texture;
    private Sprite sprite;
    private Body body;
    PhysicsShapeCache physicsShapeCache;
    private Texture point;
    private ArrayMap<String, Vector2> forces;

    public Player(World world, float x, float y, float angle) {

        physicsShapeCache = new PhysicsShapeCache("iceberg.xml");

        texture = new Texture("ldyshko.png");
        sprite = new Sprite(texture);
        sprite.setScale(SCALE);
        forces = new ArrayMap<String, Vector2>();
        body = physicsShapeCache.createBody("ldyshko", world, SCALE, SCALE);
        body.setTransform(x, y, angle);
        point = new Texture("iceberg.png");
        body.setUserData(new ObjectInfo("player"));

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
        for (Vector2 vector : forces.values())
        {
            body.applyForceToCenter(vector, true);
        }
    }

    public void update(Batch batch){

        sprite.setPosition(body.getPosition().x, body.getPosition().y);
        sprite.setOrigin(body.getPosition().x, body.getPosition().y);
        float degrees = (float) Math.toDegrees(body.getAngle());
        sprite.setOrigin(0f,0f);
        sprite.setRotation(degrees);
        sprite.draw(batch);
        applyForces();

    }

    public ArrayMap<String, Vector2> getForces() {
        return forces;
    }

    public void dispose(){
        texture.dispose();
    }




}



