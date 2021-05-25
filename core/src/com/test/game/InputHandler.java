package com.test.game;

import com.test.game.objects.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class InputHandler {

    public void inputHandler(Player player) {

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) player.getBody().applyForceToCenter(60f,0,true);
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) player.getBody().applyForceToCenter(-60f,0,true);
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) player.getBody().applyForceToCenter(0,60f,true);
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) player.getBody().applyForceToCenter(0,-60f,true);


    }

}

