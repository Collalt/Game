package com.test.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.test.game.info.FlowInfo;
import com.test.game.info.ObjectInfo;
import com.test.game.objects.Player;


public class ListenerClass implements ContactListener {
    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {


    }

    @Override
    public void beginContact(Contact contact) {

        Body objectA = contact.getFixtureA().getBody();
        Body objectB = contact.getFixtureB().getBody();


		/*	if (((ObjectInfo) objectA.getUserData()).getType() == "player" || ((ObjectInfo) objectB.getUserData()).getType() == "player")
			{
				isPlayer += 1;
				System.out.println(isPlayer);
			}*/

        if (((ObjectInfo) objectA.getUserData()).getType() == "flow")
            objectB.setLinearVelocity(objectA.getLinearVelocity().add(((FlowInfo) objectA.getUserData()).getFlow()));


        if (((ObjectInfo) objectB.getUserData()).getType() == "flow")
            objectA.setLinearVelocity(objectB.getLinearVelocity().add(((FlowInfo) objectB.getUserData()).getFlow()));
        //objectA.applyForceToCenter(((FlowInfo) objectB.getUserData()).getFlow(), true);
    }

    public void Test(Contact contact, Body objectA, Body objectB){
        Vector2 flow;
        flow = new Vector2();
        if (((ObjectInfo) objectA.getUserData()).getType() == "flow") flow = ((FlowInfo) objectA.getUserData()).getFlow();
        if (((ObjectInfo) objectB.getUserData()).getType() == "flow") flow = ((FlowInfo) objectB.getUserData()).getFlow();
    }

}

