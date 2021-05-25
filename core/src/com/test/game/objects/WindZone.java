package com.test.game.objects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.test.game.info.FlowInfo;

public class WindZone {
    private Vector2 wind;
    private BodyDef bodyDef;
    private FixtureDef fixtureDef;
    private PolygonShape polygonShape;
    private Body body;


    public Vector2 getWind() {
        return wind;
    }

    public Body getBody() {
        return body;
    }

    public WindZone(World world, float width, float height, float x, float y, Vector2 wind){
        bodyDef = new BodyDef();
        fixtureDef = new FixtureDef();
        polygonShape = new PolygonShape();


        bodyDef.type = BodyDef.BodyType.StaticBody;
        fixtureDef.shape = polygonShape;
        fixtureDef.isSensor = true;
        polygonShape.setAsBox(width, height);

        body = world.createBody(bodyDef);
        //((FlowBody) body).setFlow(wind);
        body.createFixture(fixtureDef);
        body.setTransform(x,y,0);
        body.setUserData(new FlowInfo("flow", wind));
    }
}

