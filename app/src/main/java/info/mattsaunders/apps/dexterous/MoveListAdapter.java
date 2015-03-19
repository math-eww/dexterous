package info.mattsaunders.apps.dexterous;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * adapter to display a Pokemon's moves
 */
public class MoveListAdapter extends BaseAdapter implements Filterable {
    private LayoutInflater mInflater;
    private List<Move> mMoves;
    private Context mContext;
    private ValueFilter valueFilter;
    private List<Move> mMovesFilterList;

    private boolean mIsTeamBuilder;

    public MoveListAdapter(Context context, List<Move> moves, boolean isTeamBuilder) {
        mInflater = LayoutInflater.from(context);
        mMoves = moves;
        mContext = context;
        mMovesFilterList = moves;
        mIsTeamBuilder = isTeamBuilder;
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
                ArrayList<Move> filterList = new ArrayList<>();
                for (int i = 0; i < mMovesFilterList.size(); i++) {
                    if ( (mMovesFilterList.get(i).getMoveName().toUpperCase() )
                            .contains(constraint.toString().toUpperCase())) {
                        Move listMove = mMovesFilterList.get(i);
                        filterList.add(listMove);
                    }
                }
                results.count = filterList.size();
                results.values = filterList;
            } else {
                results.count = mMovesFilterList.size();
                results.values = mMovesFilterList;
            }
            return results;

        }

        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            mMoves = (ArrayList<Move>) results.values;
            notifyDataSetChanged();
        }

    }

    @Override
    public int getCount() {
        return mMoves.size();
    }

    @Override
    public Object getItem(int position) {
        return mMoves.get(position);
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
            String mRowLayout = "move_list_item";
            int resID = mContext.getResources().getIdentifier(mRowLayout, "layout", mContext.getPackageName());
            view = mInflater.inflate(resID, parent, false);

            holder = new ViewHolder();
            holder.level = (TextView)view.findViewById(R.id.moveLearnLevel);
            holder.name = (TextView)view.findViewById(R.id.moveName);
            holder.details = (TextView)view.findViewById(R.id.moveDetails);
            holder.type = (ImageView)view.findViewById(R.id.moveType);
            holder.category = (ImageView)view.findViewById(R.id.moveCategory);

            holder.container = (LinearLayout)view.findViewById(R.id.container);

            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder)view.getTag();
        }

        final Move move = mMoves.get(position);
        Bundle basicMoveDetails = Global.db.getBasicMoveDetailsFromId(move.getResource());
        //Set info from move & bundle to list view item
        String name = move.getMoveName();
        String moveType = move.getType();
        //Set Name
        holder.name.setText(name);
        //Set Type
        switch (moveType) {
            case "bug":
                holder.type.setImageResource(R.drawable.typebug);
                break;
            case "dark":
                holder.type.setImageResource(R.drawable.typedark);
                break;
            case "dragon":
                holder.type.setImageResource(R.drawable.typedragon);
                break;
            case "electric":
                holder.type.setImageResource(R.drawable.typeelectric);
                break;
            case "fairy":
                holder.type.setImageResource(R.drawable.typefairy);
                break;
            case "fighting":
                holder.type.setImageResource(R.drawable.typefighting);
                break;
            case "fire":
                holder.type.setImageResource(R.drawable.typefire);
                break;
            case "flying":
                holder.type.setImageResource(R.drawable.typeflying);
                break;
            case "ghost":
                holder.type.setImageResource(R.drawable.typeghost);
                break;
            case "grass":
                holder.type.setImageResource(R.drawable.typegrass);
                break;
            case "ground":
                holder.type.setImageResource(R.drawable.typeground);
                break;
            case "ice":
                holder.type.setImageResource(R.drawable.typeice);
                break;
            case "normal":
                holder.type.setImageResource(R.drawable.typenormal);
                break;
            case "poison":
                holder.type.setImageResource(R.drawable.typepoison);
                break;
            case "psychic":
                holder.type.setImageResource(R.drawable.typepsychic);
                break;
            case "rock":
                holder.type.setImageResource(R.drawable.typerock);
                break;
            case "steel":
                holder.type.setImageResource(R.drawable.typesteel);
                break;
            case "water":
                holder.type.setImageResource(R.drawable.typewater);
                break;
        }
        //Set Category
        boolean status = false;
        switch (basicMoveDetails.getString("damageType")) {
            case "status":
                holder.category.setImageResource(R.drawable.movetypestatus);
                status = true;
                break;
            case "physical":
                holder.category.setImageResource(R.drawable.movetypephysical);
                break;
            case "special":
                holder.category.setImageResource(R.drawable.movetypespecial);
                break;
        }
        //Set power and accuracy details
        holder.details.setText("");
        if (!status) {
            String power = String.valueOf(basicMoveDetails.getInt("power"));
            if (power.equals("0")) power = "--";
            String accuracy = String.valueOf(basicMoveDetails.getInt("accuracy"));
            if (accuracy.equals("0")) accuracy = "--";
            holder.details.setText("Power: " + power + " | Acc.: " + accuracy);
        }

        holder.level.setText("");
        if (move.getLevel() != 0 && !mIsTeamBuilder) {
            holder.level.setText(String.valueOf(move.getLevel()));
        }

        if (!mIsTeamBuilder) {
            holder.container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showMoveDetailsDialog(move);
                }
            });
        }

        return view;
    }
/*
    public void setList(List<Move> newList) {
        mMoves = newList;
        mMovesFilterList = newList;
    }
*/
    private class ViewHolder {
        public TextView name, level, details;
        public ImageView type, category;
        public LinearLayout container;
    }

    private void showMoveDetailsDialog(Move move) {
        Activity activity = (Activity) mContext;
        FragmentManager fm = activity.getFragmentManager();
        MoveDialogFragment moveDialogFragment = new MoveDialogFragment();
        Bundle moveResource = new Bundle();
        moveResource.putInt("resource",move.getResource());
        moveDialogFragment.setArguments(moveResource);
        moveDialogFragment.show(fm,"moveDialog");
    }
}
