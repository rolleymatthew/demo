package com.wy.mutilthreadbug;

/**
 * Created by yunwang on 2021/6/30 17:44
 */
public class JEntry {
    private int state;
    private int dubble;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getDubble() {
        return dubble;
    }

    public void setDubble(int dubble) {
        this.dubble = dubble;
    }
}
