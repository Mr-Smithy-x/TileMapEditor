package tests;

import org.junit.jupiter.api.Test;

public class TileTestCase {


    public static long toLong(int position_x, int position_y, int pixel_w, int pixel_h, int collison) {
        long address = position_x;
        address = (address << 12) + position_y;
        address = (address << 8) + pixel_w;
        address = (address << 8) + pixel_h;
        address = (address << 2) + collison;
        return address;
    }

    public static long toggleCollision(long address) {
        long collison = (address & 0x1);
        if (collison == 0) {
            address += 1;
        } else {
            address -= 1;
        }
        return address;
    }

    public static long toggleCollision(long address, boolean collision) {
        long c = (address & 0x1);
        if (c == 0) { //currently no collision
            address += (collision ? 1 : 0); //if true add collision
        } else { //currently collision
            address -= (!collision ? 1 : 0); //if false minus collision
        }
        return address;
    }

    @Test
    public void testCanToggleCollisionAndRetain() {
        int pos_x = 4095;
        int pos_y = 0;
        int pix_w = 1;
        int pix_h = 0;
        int collison = 1;

        long l = toLong(pos_x, pos_y, pix_w, pix_h, collison);
        long spos_x = (l >> 30) & 0xfff;
        long spos_y = (l >> 18) & 0xfff;
        long stile_w = (l >> 10) & 0xff;
        long stile_h = (l >> 2) & 0xff;
        long bool = l & 0x1;


        assert stile_h == pix_h;
        assert stile_w == pix_w;
        assert spos_y == pos_y;
        assert spos_x == pos_x;
        assert bool == collison;

        l = toggleCollision(l);

        spos_x = (l >> 30) & 0xfff;
        spos_y = (l >> 18) & 0xfff;
        stile_w = (l >> 10) & 0xff;
        stile_h = (l >> 2) & 0xff;
        bool = l & 0x1;


        assert stile_h == pix_h;
        assert stile_w == pix_w;
        assert spos_y == pos_y;
        assert spos_x == pos_x;
        assert bool == 0;

        l = toggleCollision(l, true);

        spos_x = (l >> 30) & 0xfff;
        spos_y = (l >> 18) & 0xfff;
        stile_w = (l >> 10) & 0xff;
        stile_h = (l >> 2) & 0xff;
        bool = l & 0x1;


        assert stile_h == pix_h;
        assert stile_w == pix_w;
        assert spos_y == pos_y;
        assert spos_x == pos_x;
        assert bool == 1;
    }

    @Test
    public void testShouldPackAndUnPackBitwiseToCorrespondingValues() {
        int pos_x = 4095;
        int pos_y = 0;
        int pix_w = 1;
        int pix_h = 0;
        int collison = 1;

        long l = toLong(pos_x, pos_y, pix_w, pix_h, collison);
        long spos_x = (l >> 30) & 0xfff;
        long spos_y = (l >> 18) & 0xfff;
        long stile_w = (l >> 10) & 0xff;
        long stile_h = (l >> 2) & 0xff;
        long bool = l & 0x1;


        assert stile_h == pix_h;
        assert stile_w == pix_w;
        assert spos_y == pos_y;
        assert spos_x == pos_x;
        assert bool == collison;
    }

    @Test
    public void testShouldNotEqualPastRangeValues() {
        int pos_x = 4096; //max is 4096 Range = 0-4095
        int pos_y = 4096; //max is 4096 Range = 0-4095
        int pix_w = 256; //max is 256 Range = 0-255
        int pix_h = 256; //max is 256 Range = 0-255
        int collison = 2; //max is 1 Range = 0-1

        long l = toLong(pos_x, pos_y, pix_w, pix_h, collison);

        long spos_x = (l >> 30) & 0xfff;
        long spos_y = (l >> 18) & 0xfff;
        long stile_w = (l >> 10) & 0xff;
        long stile_h = (l >> 2) & 0xff;
        long bool = l & 0x1;

        assert spos_x != pos_x;
        assert spos_y != pos_y;
        assert stile_w != pix_w;
        assert stile_h != pix_h;
        assert bool != collison;
    }

}
