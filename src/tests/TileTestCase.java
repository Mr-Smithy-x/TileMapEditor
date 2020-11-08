package tests;

import org.junit.jupiter.api.Test;

import static nyc.vonley.models.Tile.toLong;

public class TileTestCase {





    public static long toggleCollision(long address) {
        long collison = (address >> 2 & 0x1);
        if (collison == 0) {
            address += 1 << 2; // +4
        } else {
            address -= 1 << 2; // -4
        }
        return address;
    }

    public static long toggleCollision(long address, boolean collides) {
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




    @Test
    public void testCanToggleCollisionAndObjectAndRetain() {
        int pos_x = 4095;
        int pos_y = 2004;
        int pix_w = 145;
        int pix_h = 86;
        int collison = 1;
        int is_object = 0; //defined is i can be picked upc

        long l = toLong(pos_x, pos_y, pix_w, pix_h, collison, is_object);
        long spos_x     =       (l >> 32)   & 0xfff;
        long spos_y     =       (l >> 20)   & 0xfff;
        long stile_w    =       (l >> 12)   & 0xff;
        long stile_h    =       (l >> 4)    & 0xff;
        long scollision =       (l >> 2)    & 0x1;
        long sobject    =       (l & 0x1);

        System.out.printf("pos: (%s,%s) dim: (%s, %s), collide: %s, is_object: %s\n", spos_x, spos_y, stile_w, stile_h, scollision, sobject);


        assert stile_h == pix_h;
        assert stile_w == pix_w;
        assert spos_y == pos_y;
        assert spos_x == pos_x;
        assert scollision == collison;
        assert sobject == is_object;

        l = toggleCollision(l);

        spos_x     =       (l >> 32)   & 0xfff;
        spos_y     =       (l >> 20)   & 0xfff;
        stile_w    =       (l >> 12)   & 0xff;
        stile_h    =       (l >> 4)    & 0xff;
        scollision =       (l >> 2)    & 0x1;
        sobject    =       (l & 0x1);


        System.out.printf("pos: (%s,%s) dim: (%s, %s), collide: %s, is_object: %s\n", spos_x, spos_y, stile_w, stile_h, scollision, sobject);


        assert stile_h == pix_h;
        assert stile_w == pix_w;
        assert spos_y == pos_y;
        assert spos_x == pos_x;
        assert scollision == 0;
        assert sobject == is_object;



        l = toggleCollision(l, true);
        l = toggleObject(l, true);

        spos_x     =       (l >> 32)   & 0xfff;
        spos_y     =       (l >> 20)   & 0xfff;
        stile_w    =       (l >> 12)   & 0xff; 
        stile_h    =       (l >> 4)    & 0xff; 
        scollision =       (l >> 2)    & 0x1;  
        sobject    =       (l & 0x1);

        System.out.printf("pos: (%s,%s) dim: (%s, %s), collide: %s, is_object: %s\n", spos_x, spos_y, stile_w, stile_h, scollision, sobject);

        assert stile_h == pix_h;
        assert stile_w == pix_w;
        assert spos_y == pos_y;
        assert spos_x == pos_x;
        assert scollision == 1;
        assert sobject == 1;
    }

    @Test
    public void testShouldPackAndUnPackBitwiseToCorrespondingValues() {
        int pos_x = 4095;
        int pos_y = 0;
        int pix_w = 1;
        int pix_h = 0;
        int collison = 1;
        int is_object = 0; //defined is i can be picked upc

        long l = toLong(pos_x, pos_y, pix_w, pix_h, collison, is_object);
        long spos_x = (l >> 32) & 0xfff;
        long spos_y = (l >> 20) & 0xfff;
        long stile_w = (l >> 12) & 0xff;
        long stile_h = (l >> 4) & 0xff;
        long scollision = (l >> 2) & 0x1;
        long sobject = (l & 0x1);


        assert stile_h == pix_h;
        assert stile_w == pix_w;
        assert spos_y == pos_y;
        assert spos_x == pos_x;
        assert scollision == collison;
        assert sobject == is_object;
    }

    @Test
    public void testShouldNotEqualPastRangeValues() {
        int pos_x = 4096; //max is 4096 Range = 0-4095
        int pos_y = 4096; //max is 4096 Range = 0-4095
        int pix_w = 256; //max is 256 Range = 0-255
        int pix_h = 256; //max is 256 Range = 0-255
        int collison = 2; //max is 1 Range = 0-1
        int object = 2; //max is 1 Range = 0-1

        long l = toLong(pos_x, pos_y, pix_w, pix_h, collison, object);

        long spos_x = (l >> 32) & 0xfff;
        long spos_y = (l >> 20) & 0xfff;
        long stile_w = (l >> 12) & 0xff;
        long stile_h = (l >> 4) & 0xff;
        long scollison = (l >> 2) & 0x1;
        long sobject = l & 0x1;

        assert spos_x != pos_x;
        assert spos_y != pos_y;
        assert stile_w != pix_w;
        assert stile_h != pix_h;
        assert scollison != collison;
        assert sobject != object;
    }

}
