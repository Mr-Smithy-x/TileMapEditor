package migration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import nyc.vonley.models.Tile;
import nyc.vonley.models.TileSet;

import java.io.*;

public class Migration {


    public static Tile old(long address) {
        long spos_x = (address >> 36) & 0xfff;
        long spos_y = (address >> 24) & 0xfff;
        long stile_w = (address >> 12) & 0xfff;
        long stile_h = (address) & 0xfff;
        return new Tile((int) spos_x, (int) spos_y, (int) stile_w, (int) stile_h, false, false);
    }

    public static long toLong(int position_x, int position_y, int pixel_w, int pixel_h) {
        long address = position_x;
        address = (address << 12) + position_y;
        address = (address << 12) + pixel_w;
        address = (address << 12) + pixel_h;
        return address;
    }


    public static void main(String[] args) throws IOException {
        String directory = System.getProperty("user.dir");
        String maps = String.format("%s/assets/maps/", directory);
        String migration_dir = String.format("%s/migration/", maps);
        File file = new File(maps);
        File[] mapsfiles = file.listFiles();

        File migration = new File(migration_dir);
        if (!migration.exists()) {
            migration.mkdir();
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        for (File f : mapsfiles) {
            BufferedReader br = new BufferedReader(new FileReader(f));
            String stub = null;
            StringBuilder sb = new StringBuilder();
            while ((stub = br.readLine()) != null) {
                sb.append(stub);
            }
            br.close();

            TileSet tileSet = gson.fromJson(sb.toString(), TileSet.class);
            for (long key : tileSet) {
                long value = tileSet.get(key);
                Tile oldTile = old(value);
                tileSet.getTiles().replace(key, oldTile.longValue());
            }

            String newTiles = gson.toJson(tileSet);


            FileWriter fw = new FileWriter(String.format("%s%s", migration_dir, f.getName()));
            fw.write(newTiles);
            fw.flush();
            fw.close();

        }
        ;

    }

}
