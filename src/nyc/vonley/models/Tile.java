package nyc.vonley.models;

/**
 * Tile:
 * Position X, Y
 * Pixel W,H
 * Collision: True or False
 * Object: True or False (Something you can pickup?)
 * TransitionType: 0 - no transition, 1 - teleport, 2 - to map
 * TransitionReference:
 * Level - 0, 1, 2 (This is the height of the object) // Player will be on level 0, Tall Trees level 1, Buildings Level 2
 * Levels are use in such a fashion where 0 will be drawn first, 1 would be drawn after 0 and 2 will be drawn after 1
 * It makes a layering system.
 */
public class Tile extends Number implements Comparable<Long> {

    public final static int LEVEL_GROUND = 0, LEVEL_MID = 1, LEVEL_SKY = 2;
    public final static int TRANSITION_NONE = 0, TRANSITION_TELEPORT = 1, TRANSITION_ROOM = 2;


    int position_x; // range 0-4095; 0x7FF
    int position_y;// range 0-4095; 0x7FF
    int pixel_w;// range 0-255; 0xFF
    int pixel_h;// range 0-255; //0xFF
    int transition_reference = 0; // Range; 0-127 // 0x7F
    int transition_type = 0; // Range 0-3; 0x3;
    int level = 0; //Range 0-3; //0x3
    boolean collision;// range 0-1; 0x1
    boolean object;// range 0-1; 0x1

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

    public Tile(int position_x, int position_y, int pixel_w, int pixel_h, boolean collides, boolean object) {
        this(position_x, position_y, pixel_w, pixel_h, collides, object, LEVEL_GROUND, TRANSITION_NONE, 0);
    }

    public Tile(int position_x, int position_y, int pixel_w, int pixel_h, boolean collides, boolean object, int level, int transition_type, int transition_reference) {
        this.position_x = position_x;
        this.position_y = position_y;
        this.pixel_w = pixel_w;
        this.pixel_h = pixel_h;
        this.collision = collides;
        this.object = object;
        this.level = level;
        this.transition_type = transition_type;
        this.transition_reference = transition_reference;
    }


    //region Static Functions
    //region Conversation Functions
    public static long toLong(int position_x, int position_y, int pixel_w, int pixel_h, boolean collision, boolean is_object) {
        return toLong(position_x, position_y, pixel_w, pixel_h, collision ? 1 : 0, is_object ? 1 : 0, LEVEL_GROUND, TRANSITION_NONE, 0);
    }

    public static long toLong(int position_x, int position_y, int pixel_w, int pixel_h, int collision, int is_object, int level, int transition_type, int transition_reference) {
        long address = position_x;
        address = (address << 12) + position_y;
        address = (address << 8) + pixel_w;
        address = (address << 8) + pixel_h;
        address = (address << 7) + transition_reference;
        address = (address << 2) + transition_type;
        address = (address << 2) + level;
        address = (address << 1) + collision;
        address = (address << 1) + is_object;
        return address;
    }

    public static Tile create(long address) {
        long spos_x = (address >> 41) & 0xfff;
        long spos_y = (address >> 29) & 0xfff;
        long stile_w = (address >> 21) & 0xff;
        long stile_h = (address >> 13) & 0xff;
        long transition_reference = (address >> 7) & 0x7F; // Range; 0-127 // 0x7F
        long transition_type = (address >> 4) & 0x3; // Range 0-2; 0x2;
        long level = (address >> 2) & 0x3; //Range 0-2; //0x2
        long scollision = (address >> 1) & 0x1;
        long sobject = (address & 0x1);
        return new Tile((int) spos_x, (int) spos_y, (int) stile_w, (int) stile_h, scollision == 1, sobject == 1, (int) level, (int) transition_type, (int) transition_reference);
    }

    public static Tile createOld(long address) {
        long spos_x = (address >> 32) & 0xfff;
        long spos_y = (address >> 20) & 0xfff;
        long stile_w = (address >> 12) & 0xff;
        long stile_h = (address >> 4) & 0xff;
        long scollision = (address >> 2) & 0x1;
        long sobject = (address & 0x1);
        return new Tile((int) spos_x, (int) spos_y, (int) stile_w, (int) stile_h, scollision == 1, sobject == 1);
    }
    //endregion

    //region bit retrievals
    public static long getPositionX(long address) {
        return (address >> 41) & 0xfff;
    }

    public static long getPositionY(long address) {
        return (address >> 29) & 0xfff;
    }

    public static long getTileWidth(long address) {
        return (address >> 21) & 0xff;
    }

    public static long getTileHeight(long address) {
        return (address >> 13) & 0xff;
    }

    public static long getTransitionReference(long address) {
        return (address >> 7) & 0x7F;
    }

    public static long getTransitionType(long address) {
        return (address >> 4) & 0x3;
    }

    public static long getLevel(long address) {
        return (address >> 2) & 0x3;
    }

    public static long getCollision(long address) {
        return (address >> 1) & 0x1;
    }

    public static long getObject(long address) {
        return (address) & 0x1;
    }
    //endregion

    //region boolean checks
    public static boolean isCollisionTile(long address) {
        return getCollision(address) == 1;
    }

    public static boolean isObjectTile(long address) {
        return getObject(address) == 1;
    }
    //endregion

    //region bit manipulations

    public static long setCollision(long address, boolean collides) {
        long collision = (address >> 1) & 0x1;
        if (collision == 0) { //currently no collision
            address += (collides ? 1 << 1 : 0); //if true add collision, +2
        } else { //currently collision
            address -= (!collides ? 1 << 1 : 0); //if false minus collision, -2
        }
        return address;
    }

    public static long setIsObject(long address, boolean is_object) {
        long object = (address & 0x1);
        if (object == 0) { //currently no collision
            address += (is_object ? 1 : 0); //if true add collision
        } else { //currently collision
            address -= (!is_object ? 1 : 0); //if false minus collision
        }
        return address;
    }

    //endregion


    //region Toggles
    public static long toggleCollision(long address) {
        long collision = (address >> 1) & 0x1;
        if (collision == 0) {
            address += 1 << 1; // +2
        } else {
            address -= 1 << 1; // -2
        }
        return address;
    }

    public static long toggleObject(long address) {
        long object = (address & 0x1);
        if (object == 0) {
            address += 1;
        } else {
            address -= 1;
        }
        return address;
    }
    //endregion Toggles

    //endregion

    //region Class Function
    public boolean isObject() {
        return object;
    }

    public int getTransitionReference() {
        return transition_reference;
    }

    public int getTransitionType() {
        return transition_type;
    }

    public int getLevel() {
        return level;
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
        return toLong(position_x, position_y, pixel_w, pixel_h, collision ? 1 : 0, object ? 1 : 0, level, transition_type, transition_reference);
    }
    //endregion

    //region Number Overrides
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

    public int getCollision() {
        return collision ? 1 : 0;
    }

    public int getObject() {
        return object ? 1 : 0;
    }
    //endregion
}
