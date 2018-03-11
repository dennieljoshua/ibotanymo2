package com.bcklup.ibotanymo;

/**
 * Created by gians on 11/02/2018.
 */

public class Plant {
    private int id;
    private String name;
    private int type;
    private int storeType;
    private String guide;
    private int kind;

    public Plant(int id, String name, int type, int storeType,String guide, int kind) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.storeType = storeType;
        this.guide = guide;
        this.kind = kind;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getStoreType() {
        return storeType;
    }

    public void setStoreType(int storeType) {
        this.storeType = storeType;
    }

    public String getGuide() {
        return guide;
    }

    public void setGuide(String guide) {
        this.guide = guide;
    }

    public int getKind() {
        return kind;
    }

    public void setKind(int kind) {
        this.kind= kind;
    }
}
