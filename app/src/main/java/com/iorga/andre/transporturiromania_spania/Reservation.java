package com.iorga.andre.transporturiromania_spania;

/**
 * Created by Andrei on 07.12.2016.
 */

public class Reservation {
    String name;
    String location;
    String phone;
    String id;
    Boolean hasBeenCalled = false;

    public Reservation(String name, String location, String phone, String id, String hasBeenCalled) {
        this.name = name;
        this.location = location;
        this.phone = phone;
        this.id = id;
        if(hasBeenCalled.equals("1")){
            this.hasBeenCalled = true;
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getHasBeenCalled() {
        return hasBeenCalled;
    }

    public void setHasBeenCalled(String hasBeenCalled) {
        if(hasBeenCalled.equals("1")){
            this.hasBeenCalled = true;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
