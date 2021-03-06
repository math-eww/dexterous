package info.mattsaunders.apps.dexterous;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter to display Pokedex list
 */
public class DexListAdapter extends BaseAdapter implements Filterable {
    private LayoutInflater mInflater;
    private List<Pokemon> mPokes;
    private Context mContext;
    private ValueFilter valueFilter;
    private List<Pokemon> mPokesFilterList;

    public DexListAdapter(Context context, List<Pokemon> pokes) {
        mInflater = LayoutInflater.from(context);
        mPokes = pokes;
        mContext = context;
        mPokesFilterList = pokes;
    }

    @Override
    public Filter getFilter() {
        if (valueFilter == null) {
            valueFilter = new ValueFilter();
        }
        return valueFilter;
    }

    private class ValueFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if (constraint != null && constraint.length() > 0) {
                ArrayList<Pokemon> filterList = new ArrayList<>();
                for (int i = 0; i < mPokesFilterList.size(); i++) {
                    if ( (mPokesFilterList.get(i).getName().toUpperCase() )
                            .contains(constraint.toString().toUpperCase())) {
                        Pokemon listPoke = mPokesFilterList.get(i);
                        filterList.add(listPoke);
                    }
                }
                results.count = filterList.size();
                results.values = filterList;
            } else {
                results.count = mPokesFilterList.size();
                results.values = mPokesFilterList;
            }
            return results;

        }

        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            mPokes = (ArrayList<Pokemon>) results.values;
            notifyDataSetChanged();
        }

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
        final ViewHolder holder;

        if(convertView == null) {
            String mRowLayout = "pokemon_list_item_relative";
            int resID = mContext.getResources().getIdentifier(mRowLayout, "layout", mContext.getPackageName());
            view = mInflater.inflate(resID, parent, false);

            holder = new ViewHolder();

            holder.number = (TextView)view.findViewById(R.id.pokemonNumber);
            holder.name = (TextView)view.findViewById(R.id.pokemonName);
            holder.types = (TextView)view.findViewById(R.id.pokemonTypes);
            holder.image = (ImageView)view.findViewById(R.id.pokemonImage);
            holder.pokeballTog1 = (ToggleButton)view.findViewById(R.id.toggle);
            holder.pokeballTog2 = (ToggleButton)view.findViewById(R.id.toggle2);
            holder.pokeballTog3 = (ToggleButton)view.findViewById(R.id.toggle3);
            holder.clickView = view.findViewById(R.id.clickView);

            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder)view.getTag();
        }

        final Pokemon poke = mPokes.get(position);
        //Set info from pokemon object to list view item
        holder.number.setText(poke.getThreeDigitStringNumber());
        String name = poke.getName();
        if (name.contains("-")) {
            if (!name.equals("Porygon-z")&&!name.equals("Mime-jr")&&!name.equals("Ho-oh")&&!name.equals("Mr-mime")) {
                name = name.split("-")[0];
            }
        }
        holder.name.setText(name);
        holder.types.setText(poke.getTypeOne() + " " + poke.getTypeTwo());

        //Set sprite if available to image view
        if (poke.isHasSprite()) {
            Bitmap sprite = BitmapFactory.decodeFile(poke.getSpriteFile().getAbsolutePath());
            holder.image.setImageBitmap(sprite);
        }

        //Set toggle to correct state based on stored state in pokemon object
        holder.pokeballTog1.setChecked(poke.getPokeballToggle1());
        holder.pokeballTog2.setChecked(poke.getPokeballToggle2());
        holder.pokeballTog3.setChecked(poke.getPokeballToggle3());

        //Set listener on toggle buttons
        //Toggle 1
        holder.pokeballTog1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.pokeballTog1.isChecked()) {
                    poke.setPokeballToggle1(true);
                    MainActivity.caughtDex++;
                } else {
                    poke.setPokeballToggle1(false);
                    MainActivity.caughtDex--;
                }
            }
        });
        //Toggle 2
        holder.pokeballTog2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.pokeballTog2.isChecked()) {
                    poke.setPokeballToggle2(true);
                    if (!poke.getPokeballToggle1()) {
                        holder.pokeballTog1.setChecked(true);
                        poke.setPokeballToggle1(true);
                        MainActivity.caughtDex++;
                    }
                    MainActivity.livingDex++;

                } else {
                    poke.setPokeballToggle2(false);
                    MainActivity.livingDex--;
                }
            }
        });
        //Toggle 3
        holder.pokeballTog3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.pokeballTog3.isChecked()) {
                    poke.setPokeballToggle3(true);
                } else {
                    poke.setPokeballToggle3(false);
                }
            }
        });

        final int pokePosition = Global.pokemonList.indexOf(poke);
        holder.clickView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,PokeDetailsTabs.class);
                intent.putExtra("pokemon",pokePosition);
                mContext.startActivity(intent);
            }
        });


        return view;
    }

    public void setList(List<Pokemon> newList) {
        mPokes = newList;
        mPokesFilterList = newList;
    }

    private class ViewHolder {
        public View clickView;
        public TextView number, name, types;
        public ImageView image;
        public ToggleButton pokeballTog1, pokeballTog2, pokeballTog3;
    }
}