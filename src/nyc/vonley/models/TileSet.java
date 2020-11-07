package nyc.vonley.models;

import java.util.HashMap;
import java.util.Map;

/**
 * {
 * "image": "bg_kit_3.png",
 * "tile_width": 32,
 * "tile_height": 32,
 * "tile": {
 * "0": "131104",
 * "32": "-536739808",
 * "64": "-3221094368",
 * "96": "-3757965280",
 * "128": "-4294836192",
 * "131072": "-2199023124448",
 * "131104": "-2199559995360",
 * "131136": "-2202244349920",
 * "131168": "-2202781220832",
 * "131200": "-2203318091744",
 * "262208": "-4401267605472",
 * "262240": "-4401804476384",
 * "262272": "-4402341347296",
 * "393344": "-6601364602848",
 * "393312": "-6600827731936",
 * "393280": "-6600290861024"
 * }
 * }
 */
public class TileSet {
    String map_image;
    LongMap tiles;
    int tile_width;
    int tile_height;

    public String getMapImage() {
        return map_image;
    }

    public void setMapImage(String map_image) {
        this.map_image = map_image;
    }

    public int getTileWidth() {
        return tile_width;
    }

    public void setTileWidth(int tile_width) {
        this.tile_width = tile_width;
    }

    public int getTileHeight() {
        return tile_height;
    }

    public void setTileHeight(int tile_height) {
        this.tile_height = tile_height;
    }

    public LongMap getTiles() {
        return tiles;
    }

    public void put(Long key, Long value) {
        tiles.put(key, value);
    }

    public void remove(Long key) {
        tiles.remove(key);
    }

    public void setTiles(LongMap tiles) {
        this.tiles = tiles;
    }

    public static class LongMap extends HashMap<Long, Long> {
    }
}


