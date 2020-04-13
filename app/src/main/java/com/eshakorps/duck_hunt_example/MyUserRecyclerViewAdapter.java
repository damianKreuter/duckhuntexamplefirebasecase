package com.eshakorps.duck_hunt_example;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class MyUserRecyclerViewAdapter extends RecyclerView.Adapter<MyUserRecyclerViewAdapter.ViewHolder> {

    private final List<User> mValues;

    public MyUserRecyclerViewAdapter(List<User> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        int pos = 1;
        holder.txtposition.setText(pos + "2");
        holder.txtducks.setText(String.valueOf(mValues.get(position).getDuck()));
        holder.txtNick.setText(mValues.get(position).getNick());
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView txtposition;
        public final TextView txtducks;
        public final TextView txtNick;
        public User mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            txtposition = (TextView) view.findViewById(R.id.textViewPosition);
            txtducks = (TextView) view.findViewById(R.id.textViewPuntaje);
            txtNick = (TextView) view.findViewById(R.id.textViewUser);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + txtNick.getText() + "'";
        }
    }
}
