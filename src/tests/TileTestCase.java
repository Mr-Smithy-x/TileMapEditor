package tests;

import nyc.vonley.models.Tile;
import org.junit.jupiter.api.Test;

import static nyc.vonley.models.Tile.toLong;

public class TileTestCase {


    @Test
    public void test() {
        int pos_x = 4095;
        int pos_y = 4095;
        int pix_w = 255;
        int pix_h = 255;
        int collison = 1;
        int is_object = 0; //defined is i can be picked upc
        int level = Tile.LEVEL_MID; // 2
        int transition_type = Tile.TRANSITION_TELEPORT; // 2
        int transition_reference = 127;

        long l = Tile.toLong(pos_x, pos_y, pix_w, pix_h, collison, is_object, level, transition_type, transition_reference);
        Tile tile = Tile.create(l);

        System.out.println(l);
        System.out.printf("POS: (%s, %s)\nDIM: (%s, %s)\n" +
                        "Collision: %s, Object: %s\n" +
                        "Level: %s, T_TYPE: %s, T_REF: %s",
                tile.getPositionX(), tile.getPositionY(),
                tile.getPixelW(), tile.getPixelH(),
                tile.isCollision(), tile.isObject(),
                tile.getLevel(), tile.getTransitionType(),
                tile.getTransitionReference());
    }

    @Test
    public void testCanToggleCollisionAndObjectAndRetain() {
        int pos_x = 4095;
        int pos_y = 2004;
        int pix_w = 145;
        int pix_h = 86;
        int collison = 1;
        int level = Tile.LEVEL_GROUND;
        int transition_type = Tile.TRANSITION_NONE;
        int transition_reference = 0;
        int is_object = 0; //defined is i can be picked upc

        long l = Tile.toLong(pos_x, pos_y, pix_w, pix_h, collison, is_object, Tile.LEVEL_GROUND, Tile.TRANSITION_NONE, 0);
        Tile tile = Tile.create(l);


        System.out.printf("pos: (%s,%s) dim: (%s, %s), collide: %s, is_object: %s\n", tile.getPositionX(), tile.getPositionY(), tile.getPixelW(), tile.getPixelH(), tile.isCollision(), tile.isObject());


        assert tile.getPixelH() == pix_h;
        assert tile.getPixelW() == pix_w;
        assert tile.getPositionY() == pos_y;
        assert tile.getPositionX() == pos_x;
        assert (tile.isCollision() ? 1 : 0) == collison;
        assert (tile.isObject() ? 1 : 0) == is_object;

        tile.toggleCollision();
        l = tile.toLong();

        long tcollision = (l >> 2) & 0x1;
        long tobject = l & 0x1;


        System.out.printf("pos: (%s,%s) dim: (%s, %s), collide: %s, is_object: %s\n", tile.getPositionX(), tile.getPositionY(), tile.getPixelW(), tile.getPixelH(), tile.isCollision(), tile.isObject());


        assert tcollision == 0;
        assert tobject == 0;


        tile.setCollision(true);
        tile.setObject(true);
        l = tile.toLong();

        tcollision = (l >> 2) & 0x1;
        tobject = (l & 0x1);


        System.out.printf("pos: (%s,%s) dim: (%s, %s), collide: %s, is_object: %s\n", tile.getPositionX(), tile.getPositionY(), tile.getPixelW(), tile.getPixelH(), tile.isCollision(), tile.isObject());

        long ttile_h = tile.getPixelH();
        long ttile_w = tile.getPixelW();
        long tpos_x = tile.getPositionX();
        long tpos_y = tile.getPositionY();

        assert ttile_h == pix_h;
        assert ttile_w == pix_w;
        assert tpos_y == pos_y;
        assert tpos_x == pos_x;
        assert tcollision == 1;
        assert tobject == 1;
    }

    @Test
    public void testShouldPackAndUnPackBitwiseToCorrespondingValues() {
        int pos_x = 4095;
        int pos_y = 0;
        int pix_w = 1;
        int pix_h = 0;
        int collison = 1;
        int is_object = 0; //defined is i can be picked upc

        long l = toLong(pos_x, pos_y, pix_w, pix_h, collison, is_object, Tile.LEVEL_GROUND, Tile.TRANSITION_NONE, 0);
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

        long l = toLong(pos_x, pos_y, pix_w, pix_h, collison, object, Tile.LEVEL_GROUND, Tile.TRANSITION_NONE, 0);

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
