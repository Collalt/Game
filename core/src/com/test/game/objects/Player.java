
package com.test.game.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.codeandweb.physicseditor.PhysicsShapeCache;
import com.test.game.GameScreen;
import com.test.game.ListenerClass;
import com.test.game.info.ObjectInfo;


public class Player {
    private Texture texture;
    private Sprite sprite;
    private Body body;
    PhysicsShapeCache physicsShapeCache;
    private Texture point;

    public Player(World world, float x, float y, float angle) {

        physicsShapeCache = new PhysicsShapeCache("iceberg.xml");

        texture = new Texture("ldyshko.png");
        sprite = new Sprite(texture);
        sprite.setScale(0.05f);
        body = physicsShapeCache.createBody("ldyshko", world, 0.05f, 0.05f);
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

    public void update(Batch batch){
        sprite.setPosition(body.getPosition().x, body.getPosition().y);
        sprite.setOrigin(body.getPosition().x, body.getPosition().y);
        float degrees = (float) Math.toDegrees(body.getAngle());
        sprite.setOrigin(0f,0f);
        sprite.setRotation(degrees);
        sprite.draw(batch);
        //applyFlow();

    }

    public void applyFlow(Vector2 flow){
        body.setLinearVelocity(body.getLinearVelocity().add(flow));
    }

    public void dispose(){
        texture.dispose();
    }


    public Texture getPoint() {
        return point;
    }
}



