package tests;

import nyc.vonley.models.Tile;
import org.junit.jupiter.api.Test;

import static nyc.vonley.models.Tile.*;

public class TileTestCase {


    @Test
    public void testChangeLevel(){
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

        assert pos_x == tile.getPositionX();
        assert pos_y == tile.getPositionY();
        assert pix_w == tile.getPixelW();
        assert pix_h == tile.getPixelH();
        assert collison == tile.getCollision();
        assert is_object == tile.getObject();
        assert level == tile.getLevel();
        assert transition_type == tile.getTransitionType();
        assert transition_reference == tile.getTransitionReference();


        l = setLevel(l, LEVEL_GROUND);
        tile = Tile.create(l);


        assert pos_x == tile.getPositionX();
        assert pos_y == tile.getPositionY();
        assert pix_w == tile.getPixelW();
        assert pix_h == tile.getPixelH();
        assert collison == tile.getCollision();
        assert is_object == tile.getObject();
        assert LEVEL_GROUND == tile.getLevel();
        assert transition_type == tile.getTransitionType();
        assert transition_reference == tile.getTransitionReference();

        l = setLevel(l, LEVEL_SKY);
        tile = Tile.create(l);


        assert pos_x == tile.getPositionX();
        assert pos_y == tile.getPositionY();
        assert pix_w == tile.getPixelW();
        assert pix_h == tile.getPixelH();
        assert collison == tile.getCollision();
        assert is_object == tile.getObject();
        assert LEVEL_SKY == tile.getLevel();
        assert transition_type == tile.getTransitionType();
        assert transition_reference == tile.getTransitionReference();



    }

    @Test
    public void testBitmanipulation() {
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

        assert pos_x == tile.getPositionX();
        assert pos_y == tile.getPositionY();
        assert pix_w == tile.getPixelW();
        assert pix_h == tile.getPixelH();
        assert collison == tile.getCollision();
        assert is_object == tile.getObject();
        assert level == tile.getLevel();
        assert transition_type == tile.getTransitionType();
        assert transition_reference == tile.getTransitionReference();

        System.out.printf("OLD Value: %s\n", l);
        l = Tile.setCollision(l, false);
        System.out.printf("New value: %s\n", l);

        tile = Tile.create(l);

        assert tile.getPositionX() == pos_x;
        assert tile.getPositionY() == pos_y;
        assert tile.getPixelW() == pix_w;
        assert tile.getPixelH() == pix_h;
        assert tile.getCollision() == 0;
        assert tile.getObject() == is_object;
        assert tile.getLevel() == level;
        assert tile.getTransitionType() == transition_type;
        assert tile.getTransitionReference() == transition_reference;

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
    public void testShouldNotEqualPastRangeValues() {
        int pos_x = 4096; //max is 4096 Range = 0-4095
        int pos_y = 4096; //max is 4096 Range = 0-4095
        int pix_w = 256; //max is 256 Range = 0-255
        int pix_h = 256; //max is 256 Range = 0-255
        int collison = 2; //max is 1 Range = 0-1
        int object = 2; //max is 1 Range = 0-1
        int level = 3;
        int transition = 3;
        int reference = 128;

        long l = toLong(pos_x, pos_y, pix_w, pix_h, collison, object, level, transition, reference);
        Tile tile = Tile.create(l);


        assert tile.getPositionX() != pos_x;
        assert tile.getPositionY() != pos_y;
        assert tile.getPixelW() != pix_w;
        assert tile.getPixelH() != pix_h;
        assert tile.getCollision() != 0;
        assert tile.getObject() != object;
        assert tile.getLevel() != level;
        assert tile.getTransitionType() != transition;
        assert tile.getTransitionReference() != reference;

    }

}
