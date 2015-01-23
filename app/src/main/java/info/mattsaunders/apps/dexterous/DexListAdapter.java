package info.mattsaunders.apps.dexterous;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

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
        final ViewHolder holder;

        if(convertView == null) {
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

            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder)view.getTag();
        }

        final Pokemon poke = mPokes.get(position);
        //Set info from pokemon object to list view item
        holder.number.setText(poke.getStringNumber());
        holder.name.setText(poke.getName());
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
                } else {
                    poke.setPokeballToggle1(false);
                }
            }
        });
        //Toggle 2
        holder.pokeballTog2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.pokeballTog2.isChecked()) {
                    poke.setPokeballToggle2(true);
                } else {
                    poke.setPokeballToggle2(false);
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


        return view;
    }

    public void setList(List<Pokemon> newList) {
        mPokes = newList;
    }

    private class ViewHolder {
        public TextView number, name, types;
        public ImageView image;
        public ToggleButton pokeballTog1, pokeballTog2, pokeballTog3;
    }
}