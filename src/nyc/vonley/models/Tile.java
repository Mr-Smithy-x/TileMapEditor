package nyc.vonley.models;

public class Tile extends Number implements Comparable<Long> {

    int position_x; // range 0-4095
    int position_y;// range 0-4095
    int pixel_w;// range 0-255
    int pixel_h;// range 0-255
    boolean collision;// range 0-1
    boolean object;// range 0-1


    public static boolean isCollisionTile(long address){
        return (address >> 2 & 0x1) == 1;
    }

    public static boolean isObjectTile(long address){
        return (address & 0x1) == 1;
    }

    public static long toggleCollision(long address) {
        long collison = (address >> 2 & 0x1);
        if (collison == 0) {
            address += 1 << 2; // +4
        } else {
            address -= 1 << 2; // -4
        }
        return address;
    }

    public static long setCollision(long address, boolean collides) {
        long collision = (address >> 2 & 0x1);
        if (collision == 0) { //currently no collision
            address += (collides ? 1 << 2 : 0); //if true add collision, +4
        } else { //currently collision
            address -= (!collides ? 1 << 2 : 0); //if false minus collision, -4
        }
        return address;
    }

    public static long toggleObject(long address) {
        long is_object = (address & 0x1);
        if (is_object == 0) {
            address += 1;
        } else {
            address -= 1;
        }
        return address;
    }

    public static long toggleObject(long address, boolean is_object) {
        long object = (address & 0x1);
        if (object == 0) { //currently no collision
            address += (is_object ? 1 : 0); //if true add collision
        } else { //currently collision
            address -= (!is_object ? 1 : 0); //if false minus collision
        }
        return address;
    }


    public Tile(int position_x, int position_y, int pixel_w, int pixel_h, boolean collides, boolean object) {
        this.position_x = position_x;
        this.position_y = position_y;
        this.pixel_w = pixel_w;
        this.pixel_h = pixel_h;
        this.collision = collides;
        this.object = object;
    }


    public int getPixelH() {
        return pixel_h;
    }

    public int getPixelW() {
        return pixel_w;
    }

    public int getPositionX() {
        return position_x;
    }

    public int getPositionY() {
        return position_y;
    }

    public static Tile create(long address) {

        long spos_x = (address >> 32) & 0xfff;
        long spos_y = (address >> 20) & 0xfff;
        long stile_w = (address >> 12) & 0xff;
        long stile_h = (address >> 4) & 0xff;
        long scollision = (address >> 2) & 0x1;
        long sobject = (address & 0x1);

        return new Tile((int) spos_x, (int) spos_y, (int) stile_w, (int) stile_h, scollision == 1, sobject == 1);
    }


    public static long toLong(int position_x, int position_y, int pixel_w, int pixel_h, boolean collision, boolean is_object) {
        return toLong(position_x,position_y, pixel_w, pixel_h, collision ? 1 : 0, is_object ? 1 : 0);
    }


    public static long toLong(int position_x, int position_y, int pixel_w, int pixel_h, int collision, int is_object) {
        long address = position_x;
        address = (address << 12) + position_y;
        address = (address << 8) + pixel_w;
        address = (address << 8) + pixel_h;
        address = (address << 2) + collision;
        address = (address << 2) + is_object;
        return address;
    }


    public boolean isObject() {
        return object;
    }

    public boolean isCollision() {
        return collision;
    }

    public void setCollision(boolean collision) {
        this.collision = collision;
    }

    public void setObject(boolean object) {
        this.object = object;
    }

    public void toggleObject() {
        this.object = !this.object;
    }

    public void toggleCollision() {
        this.collision = !this.collision;
    }

    public long toLong() {
        return toLong(position_x, position_y, pixel_w, pixel_h, collision ? 1 : 0, object ? 1 : 0);
    }

    @Override
    public int intValue() {
        return (int) longValue();
    }

    @Override
    public long longValue() {
        return toLong();
    }

    @Override
    public float floatValue() {
        return longValue();
    }

    @Override
    public double doubleValue() {
        return longValue();
    }

    @Override
    public int compareTo(Long o) {
        return Long.compare(toLong(), o);
    }
}
