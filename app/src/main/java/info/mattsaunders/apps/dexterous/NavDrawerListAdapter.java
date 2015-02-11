package info.mattsaunders.apps.dexterous;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;

/**
 * Custom list adapter for navigation drawer menu list:
 */
public class NavDrawerListAdapter extends ArrayAdapter<String> {

    String[] itemStringList;

    public NavDrawerListAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public NavDrawerListAdapter(Context context, int resource, String[] items) {
        super(context, resource, items);
        this.itemStringList = items;
    }

    public void setList(String[] newList) {
        itemStringList = newList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {

            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.nav_dawer_list_item, null);

        }

        TextView listName = (TextView) v.findViewById(R.id.navDrawerListName);
        TextView listPercentage = (TextView) v.findViewById(R.id.navDrawerPercentage);
        ImageView pokeBallIcon = (ImageView) v.findViewById(R.id.navDrawerPokeballIcon);
        TextView listCount = (TextView) v.findViewById(R.id.navDrawerListCount);

        listName.setTextColor(Color.BLACK);
        listPercentage.setTextColor(Color.BLACK);
        listCount.setTextColor(Color.BLACK);

        int percentageCalcTop;
        int percentageCalcBottom;
        float displayPercetage;
        DecimalFormat df = new DecimalFormat("#.##");

        switch (position) {
            case 0:
                listPercentage.setText("");
                listCount.setText("");
                break;
            case 1:
                percentageCalcBottom = MainActivity.getTotalPokes();
                percentageCalcTop = MainActivity.getTotalPokes() - MainActivity.getCaughtDex(); //number of missing pokemon from pokedex
                displayPercetage = (float) ((float)percentageCalcTop / (float)percentageCalcBottom) * 100;
                listPercentage.setText(String.valueOf(df.format(displayPercetage)) + "%");
                pokeBallIcon.setImageResource(R.drawable.pokeball_deselect);
                listCount.setText(String.valueOf(percentageCalcTop) + " / " + String.valueOf(percentageCalcBottom));
                break;
            case 2:
                percentageCalcBottom = MainActivity.getTotalPokes();
                percentageCalcTop = MainActivity.getCaughtDex();
                displayPercetage = (float) ((float)percentageCalcTop / (float)percentageCalcBottom) * 100;
                listPercentage.setText(String.valueOf(df.format(displayPercetage)) + "%");
                pokeBallIcon.setImageResource(R.drawable.pokeball);
                listCount.setText(String.valueOf(percentageCalcTop) + " / " + String.valueOf(percentageCalcBottom));
                break;
            case 3:
                percentageCalcBottom = MainActivity.getTotalPokes();
                percentageCalcTop = MainActivity.getTotalPokes() - MainActivity.getLivingDex(); //number of missing pokemon from living dex
                displayPercetage = (float) ((float)percentageCalcTop / (float)percentageCalcBottom) * 100;
                listPercentage.setText(String.valueOf(df.format(displayPercetage)) + "%");
                pokeBallIcon.setImageResource(R.drawable.premierball_deselect);
                listCount.setText(String.valueOf(percentageCalcTop) + " / " + String.valueOf(percentageCalcBottom));
                break;
            case 4:
                percentageCalcBottom = MainActivity.getTotalPokes();
                percentageCalcTop = MainActivity.getLivingDex();
                displayPercetage = (float) ((float)percentageCalcTop / (float)percentageCalcBottom) * 100;
                listPercentage.setText(String.valueOf(df.format(displayPercetage)) + "%");
                pokeBallIcon.setImageResource(R.drawable.premierball);
                listCount.setText(String.valueOf(percentageCalcTop) + " / " + String.valueOf(percentageCalcBottom));
                break;
            case 5:
                pokeBallIcon.setImageResource(R.drawable.cherishball);
                listPercentage.setText("");
                listCount.setText("");
                break;
        }

        listName.setText(itemStringList[position]);


        return v;

    }
}

