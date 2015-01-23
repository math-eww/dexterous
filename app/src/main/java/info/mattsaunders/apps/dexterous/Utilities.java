package info.mattsaunders.apps.dexterous;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.Iterator;

/**
 * Utilities - JSON to bundle, bundle to JSON, JSON to file, file to JSON
 */
public class Utilities {

    public static String FILENAME = "test";
    private final static String FILEDIR = "/Dexterous/";
    public static String SETTINGS_FILENAME = "_settings";
    public static String SUBDIR = "";

    public static JSONObject bundleToJsonObject(Bundle bundle) {
        try {
            JSONObject output = new JSONObject();
            for( String key : bundle.keySet() ){
                Object object = bundle.get(key);
                if(object instanceof Integer || object instanceof String)
                    output.put(key, object);
                else
                    throw new RuntimeException("only Integer and String can be extracted");
            }
            return output;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public static Bundle JsonObjectToBundle(JSONObject jsonObject) {
        try {
            Bundle bundle = new Bundle();
            Iterator<?> keys = jsonObject.keys();
            while( keys.hasNext() ){
                String key = (String)keys.next();
                Object object = jsonObject.get(key);
                if(object instanceof String)
                    bundle.putString(key, (String) object);
                else if(object instanceof Integer)
                    bundle.putInt(key, (Integer) object);
                else
                    throw new RuntimeException("only Integer and String can be re-extracted");
            }
            return bundle;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        } catch (NullPointerException e) {
            Log.e("JSON OBJ TO BUNDLE:", "Null pointer exception: missing file?");
        }
        return null;
    }

    public static void writeJsonFile(JSONObject data) {
        try {
            File sdcard = Environment.getExternalStorageDirectory();
            File dir = new File(sdcard.getAbsolutePath() + FILEDIR + SUBDIR);
            dir.mkdir();
            File file = new File(dir, FILENAME);
            FileOutputStream fos = new FileOutputStream(file);
            try {
                fos.write(data.toString().getBytes());
            } catch (Exception ex) {
                Log.e("Failed to save data", data.toString());
                ex.printStackTrace();
            }
            fos.close();
        } catch (Exception ex) {
            Log.e("Failed to open file", FILENAME);
            ex.printStackTrace();
        }
    }

    public static JSONObject readJsonFile() {
        String json;
        try {
            File sdcard = Environment.getExternalStorageDirectory();
            File dir = new File(sdcard.getAbsolutePath() + FILEDIR + SUBDIR);
            File file = new File(dir, FILENAME);
            try {
                FileInputStream fis = new FileInputStream(file);
                InputStreamReader fileRead = new InputStreamReader(fis);
                BufferedReader reader = new BufferedReader(fileRead);
                String str;
                StringBuilder buf = new StringBuilder();
                try {
                    while ((str = reader.readLine()) != null) {
                        buf.append(str);
                    }
                    fis.close();
                    json = buf.toString();
                    return new JSONObject(json);
                } catch (Exception ex) {
                    Log.e("Failed to read file", ex.toString());
                }
            } catch (FileNotFoundException ex) {
                Log.e("Failed to load file: file not found", file.toString());
            }
        } catch (Exception ex) {
            Log.e("Failed to find directory", ex.toString());
        }
        return null;
    }

    public static String readSettingsFile() {
        String data;
        try {
            File sdcard = Environment.getExternalStorageDirectory();
            File dir = new File(sdcard.getAbsolutePath() + FILEDIR);
            File file = new File(dir, SETTINGS_FILENAME);
            try {
                FileInputStream fis = new FileInputStream(file);
                InputStreamReader fileRead = new InputStreamReader(fis);
                BufferedReader reader = new BufferedReader(fileRead);
                String str;
                StringBuilder buf = new StringBuilder();
                try {
                    while ((str = reader.readLine()) != null) {
                        buf.append(str);
                    }
                    fis.close();
                    data = buf.toString();
                    return data;
                } catch (Exception ex) {
                    Log.e("Failed to read file", ex.toString());
                }
            } catch (FileNotFoundException ex) {
                Log.e("Failed to load file: file not found", file.toString());
            }
        } catch (Exception ex) {
            Log.e("Failed to find directory", ex.toString());
        }
        return "";
    }

    public static void writeSettingsFile(String data) {
        try {
            File sdcard = Environment.getExternalStorageDirectory();
            File dir = new File(sdcard.getAbsolutePath() + FILEDIR);
            dir.mkdir();
            File file = new File(dir, SETTINGS_FILENAME);
            FileOutputStream fos = new FileOutputStream(file);
            try {
                fos.write(data.getBytes());
                //Log.i("Successfully wrote JSON to file", data.toString());
            } catch (Exception ex) {
                Log.e("Failed to save pid", data);
                ex.printStackTrace();
            }
            fos.close();
        } catch (Exception ex) {
            Log.e("Failed to open file", SETTINGS_FILENAME);
            ex.printStackTrace();
        }
    }
}
