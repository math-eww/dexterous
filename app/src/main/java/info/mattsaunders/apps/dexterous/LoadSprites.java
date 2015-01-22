package info.mattsaunders.apps.dexterous;

import android.os.Environment;

import java.io.File;

/**
 * Class to load sprites to Pokemon object list
 */
public class LoadSprites {
    private static String FILEDIR = "/Dexterous/SPRITES/";
    private static String FILE_EXT = ".png";

    public static void loadSprites() {
        File sdcard = Environment.getExternalStorageDirectory();
        File dir = new File(sdcard.getAbsolutePath() + FILEDIR);
        //Loop through pokemon list
        for (Pokemon poke : MainActivity.pokemonList) {
            //Check if sprite file exists
            File file = new File(dir, poke.getStringNumber() + FILE_EXT );
            if (file.exists()) {
                //Set sprite file to pokemon object
                poke.setSprite(file);
            }

        }
        //MainActivity.dexAdapter.notifyDataSetChanged();
    }
}
