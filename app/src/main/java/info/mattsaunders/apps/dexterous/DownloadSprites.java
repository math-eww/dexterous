package info.mattsaunders.apps.dexterous;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Class to download sprites for pokemon
 */
public class DownloadSprites {

    private static String API_URL = "http://pokeapi.co";
    private static String POKE = "/media/img/";
    private static String FILE_EXT = ".png";
    private static int TOTAL_POKES = 718;
    private static String FILEDIR = "/Dexterous/SPRITES/";


    public static class CallAPI extends AsyncTask<String, String, String> {
        ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            progress = new ProgressDialog(MainActivity.c);
            progress.setMessage("Downloading Pokemon sprites");
            progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progress.setCancelable(false);
            progress.setMax(TOTAL_POKES);
            progress.show();
        }
        @Override
        protected String doInBackground(String... params) {
            //String urlStringEnd = params[0];
            //String command = params[1];
            String resultToDisplay = "";

            for (int i = 1; i <= TOTAL_POKES; i++ ) {
                String urlString = API_URL + POKE + String.valueOf(i) + FILE_EXT;
                HttpURLConnection urlConnection;
                InputStream input;
                Log.i("Executing background API task", "URL is " + urlString);

                // HTTP Get
                try {
                    URL url = new URL(urlString);
                    urlConnection = (HttpURLConnection) url.openConnection();
                    input = urlConnection.getInputStream(); //API result
                    try {
                        File sdcard = Environment.getExternalStorageDirectory();
                        File dir = new File(sdcard.getAbsolutePath() + FILEDIR);
                        dir.mkdir();
                        OutputStream output = new FileOutputStream(new File(dir,i + FILE_EXT));
                        try {
                            byte[] buffer = new byte[2048];
                            int bytesRead = 0;
                            while ((bytesRead = input.read(buffer, 0, buffer.length)) >= 0) {
                                output.write(buffer, 0, bytesRead);
                            }
                        } finally {
                            output.close();
                        }
                    } finally {
                        input.close();
                    }

                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    return e.getMessage();
                }

                //Set progress bar:
                progress.incrementProgressBy(1);
            }

            return resultToDisplay;
        }

        protected void onPostExecute(String result) {
            progress.dismiss();
            Log.i("API call complete", "Result = " + result);
        }
    }

}

