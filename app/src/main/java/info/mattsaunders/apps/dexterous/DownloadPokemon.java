package info.mattsaunders.apps.dexterous;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Class to call API to download info about Pokemon
 */
public class DownloadPokemon {
    private static String API_URL = "http://pokeapi.co";
    private static String POKE = "/api/v1/pokemon/";
    private static int TOTAL_POKES = 718;

    public static int getTOTAL_POKES() { return TOTAL_POKES; }


    public static class CallAPI extends AsyncTask<String, String, String> {
        ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            progress = new ProgressDialog(MainActivity.c);
            progress.setMessage("Downloading Pokedex info");
            progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progress.setCancelable(false);
            progress.setMax(TOTAL_POKES);
            progress.show();
            Utilities.SUBDIR = "POKEMON/";
        }
        @Override
        protected String doInBackground(String... params) {
            //String urlStringEnd = params[0];
            //String command = params[1];
            String resultToDisplay = "";

            for (int i = 1; i <= TOTAL_POKES; i++ ) {
                String urlString = API_URL + POKE + String.valueOf(i);
                HttpURLConnection urlConnection;
                InputStream input;
                Log.i("Executing background API task", "URL is " + urlString);

                // HTTP Get
                try {
                    URL url = new URL(urlString);
                    urlConnection = (HttpURLConnection) url.openConnection();
                    input = urlConnection.getInputStream(); //API result
                    //Build JSON from result:
                    BufferedReader streamReader = new BufferedReader(new InputStreamReader(input, "UTF-8"));
                    StringBuilder responseStrBuilder = new StringBuilder();

                    String inputStr;
                    while ((inputStr = streamReader.readLine()) != null)
                        responseStrBuilder.append(inputStr);
                    Utilities.FILENAME = String.valueOf(i);
                    Utilities.writeJsonFile(new JSONObject(responseStrBuilder.toString()));

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
            Utilities.SUBDIR = "";
        }
    }

}
