package com.test.game.objects;


import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.test.game.info.ObjectInfo;

public class Wall {
    private BodyDef bodyDef;
    private FixtureDef fixtureDef;
    private PolygonShape polygonShape;
    private Body body;

    public Wall(World world, float width, float height, float x, float y) {

        bodyDef = new BodyDef();
        fixtureDef = new FixtureDef();
        polygonShape = new PolygonShape();

        bodyDef.type = BodyDef.BodyType.StaticBody;
        fixtureDef.shape = polygonShape;
        fixtureDef.filter.categoryBits = 0x0004;
        fixtureDef.filter.groupIndex = 1;
        fixtureDef.filter.maskBits = 2;
        polygonShape.setAsBox(width, height);

        body = world.createBody(bodyDef);
        body.createFixture(fixtureDef);
        body.setTransform(x,y,0);
        body.setUserData(new ObjectInfo("wall"));

    }

    public void dispose(){
    }

}

