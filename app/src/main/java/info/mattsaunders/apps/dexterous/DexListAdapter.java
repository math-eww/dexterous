package info.mattsaunders.apps.dexterous;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Adapter to display Pokedex list
 */
public class DexListAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<Pokemon> mPokes;
    private String mRowLayout = "pokemon_list_item";
    private Context mContext;

    public DexListAdapter(Context context, List<Pokemon> pokes) {
        mInflater = LayoutInflater.from(context);
        mPokes = pokes;
        mContext = context;
    }

    @Override
    public int getCount() {
        return mPokes.size();
    }

    @Override
    public Object getItem(int position) {
        return mPokes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        ViewHolder holder;

        if(convertView == null) {
            int resID = mContext.getResources().getIdentifier(mRowLayout, "layout", mContext.getPackageName());
            view = mInflater.inflate(resID, parent, false);

            holder = new ViewHolder();

            holder.number = (TextView)view.findViewById(R.id.pokemonNumber);
            holder.name = (TextView)view.findViewById(R.id.pokemonName);
            holder.types = (TextView)view.findViewById(R.id.pokemonTypes);
            holder.image = (ImageView)view.findViewById(R.id.pokemonImage);

            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder)view.getTag();
        }

        Pokemon poke = mPokes.get(position);
        //Set info from pokemon object to listview item
        holder.number.setText(poke.getStringNumber());
        holder.name.setText(poke.getName());
        holder.types.setText(poke.getTypeOne() + " " + poke.getTypeTwo());

        return view;
    }

    private class ViewHolder {
        public TextView number, name, types;
        public ImageView image;
    }
}