package info.mattsaunders.apps.dexterous;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * adapter to display a Pokemon's moves
 */
public class MoveListAdapter extends BaseAdapter implements Filterable {
    private LayoutInflater mInflater;
    private List<Bundle> mMoves;
    private Context mContext;
    private ValueFilter valueFilter;
    private List<Bundle> mMovesFilterList;

    public MoveListAdapter(Context context, List<Bundle> moves) {
        mInflater = LayoutInflater.from(context);
        mMoves = moves;
        mContext = context;
        mMovesFilterList = moves;
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
                ArrayList<Bundle> filterList = new ArrayList<Bundle>();
                for (int i = 0; i < mMovesFilterList.size(); i++) {
                    if ( (mMovesFilterList.get(i).getString("name").toUpperCase() )
                            .contains(constraint.toString().toUpperCase())) {
                        Bundle listMove = mMovesFilterList.get(i);
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
            mMoves = (ArrayList<Bundle>) results.values;
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
            holder.type = (TextView)view.findViewById(R.id.moveLearnType);

            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder)view.getTag();
        }

        final Bundle move = mMoves.get(position);
        //Set info from move bundle to list view item
        String name = move.getString("name");
        String learnType = move.getString("learn");
        holder.name.setText(name);
        holder.type.setText(learnType);

        holder.level.setText("");
        if (move.containsKey("level")) {
            holder.level.setText(String.valueOf(move.getInt("level")));
        }


        return view;
    }

    public void setList(List<Bundle> newList) {
        mMoves = newList;
        mMovesFilterList = newList;
    }

    private class ViewHolder {
        public TextView name, level, type;
    }
}
