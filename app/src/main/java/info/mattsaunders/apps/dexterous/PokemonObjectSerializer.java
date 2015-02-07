package info.mattsaunders.apps.dexterous;

import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;

/**
 * Save and Load pokemon objects as serialized data
 */
public class PokemonObjectSerializer {
    private final static String FILEDIR = "/Dexterous/PokemonObjects/";
    public static String FILENAME = "poke";


    public static Pokemon readSerializedPokemon(String filename){
        ObjectInputStream input;
        filename = FILENAME + filename + ".srl";
        File sdcard = Environment.getExternalStorageDirectory();
        File dir = new File(sdcard.getAbsolutePath() + FILEDIR);
        File file = new File(dir, filename);

        try {
            input = new ObjectInputStream(new FileInputStream(file));
            Pokemon poke = (Pokemon) input.readObject();
            input.close();
            return poke;
        } catch (StreamCorruptedException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void writeSerializedPokemon(Pokemon poke){
        String filename = FILENAME + poke.getNumber() + ".srl";
        File sdcard = Environment.getExternalStorageDirectory();
        File dir = new File(sdcard.getAbsolutePath() + FILEDIR);
        dir.mkdir();
        File file = new File(dir, filename);

        ObjectOutput out = null;

        try {
            out = new ObjectOutputStream(new FileOutputStream(file));
            out.writeObject(poke);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
