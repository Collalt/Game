package com.test.game.info;

import java.util.UUID;

public class ObjectInfo {
    private String type;
    private String id;

    public ObjectInfo(String type){
       setType(type);
       id = UUID.randomUUID().toString();

    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }
}

