package com.bcklup.ibotanymo;

/**
 * Created by gians on 11/02/2018.
 */

public class Plant {
    private int id;
    private String name;
    private int type;
    private int storeType;
    private byte[] image;
    private String guide;

    public Plant(int id, String name, int type, int storeType, byte[] image, String guide) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.storeType = storeType;
        this.image = image;
        this.guide = guide;
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

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getGuide() {
        return guide;
    }

    public void setGuide(String guide) {
        this.guide = guide;
    }
}
