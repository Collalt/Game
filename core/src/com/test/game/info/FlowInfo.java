package com.test.game.info;

import com.badlogic.gdx.math.Vector2;

public class FlowInfo extends ObjectInfo {
    private Vector2 flow;

    public FlowInfo(String type, Vector2 flow){
        super(type);
        setFlow(flow);
    }

    public Vector2 getFlow() {
        return flow;
    }

    public void setFlow(Vector2 flow) {
        this.flow = flow;
    }
}
