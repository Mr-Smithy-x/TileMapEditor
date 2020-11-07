package com.models;

public class Point {
    int x;
    int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Point setX(int x) {
        this.x = x;
        return this;
    }

    public Point setY(int y) {
        this.y = y;
        return this;
    }

    public long toLong(){
        return toLong(x,y);
    }

    public static long toLong(int position_x, int position_y) {
        long address = position_x;
        address = (address << 12) + position_y;
        return address;
    }

    public static Point fromLong(long position) {
        long position_x = (position >> 12) & 0xfff;
        long position_y = (position) & 0xfff;
        return new Point((int) position_x, (int) position_y);
    }

}
