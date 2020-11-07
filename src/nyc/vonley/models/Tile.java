package nyc.vonley.models;

public class Tile {

    int position_x;
    int position_y;
    int pixel_w;
    int pixel_h;

    public Tile(int position_x, int position_y, int pixel_w, int pixel_h) {
        this.position_x = position_x;
        this.position_y = position_y;
        this.pixel_w = pixel_w;
        this.pixel_h = pixel_h;
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
        long spos_x = (address >> 36) & 0xfff;
        long spos_y = (address >> 24) & 0xfff;
        long stile_w = (address >> 12) & 0xfff;
        long stile_h = (address) & 0xfff;
        return new Tile((int) spos_x, (int) spos_y, (int) stile_w, (int) stile_h);
    }

    public static long toLong(int position_x, int position_y, int pixel_w, int pixel_h) {
        long address = position_x;
        System.out.println(address);
        address = (address << 12) + position_y;

        System.out.println(address);
        address = (address << 12) + pixel_w;

        System.out.println(address);
        address = (address << 12) + pixel_h;
        System.out.println(address);
        return address;
    }

    public long toLong() {
        return toLong(position_x, position_y, pixel_w, pixel_h);
    }


}
