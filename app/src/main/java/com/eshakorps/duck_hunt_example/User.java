package com.eshakorps.duck_hunt_example;

public class User {
    private String nick;
    private int duck;

    public User() {

    }

    public User(String nick, int duck) {
        this.duck = duck;
        this.nick = nick;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public int getDuck() {
        return duck;
    }

    public void setDuck(int duck) {
        this.duck = duck;
    }
}
