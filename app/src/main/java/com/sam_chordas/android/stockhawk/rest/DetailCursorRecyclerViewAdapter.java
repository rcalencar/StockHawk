package com.sam_chordas.android.stockhawk.rest;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sam_chordas.android.stockhawk.R;

/**
 * Created by rodrigo.alencar on 5/9/16.
 */
public class DetailCursorRecyclerViewAdapter extends RecyclerView.Adapter {

    private Cursor mCursor;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        if(mCursor != null) {
            mCursor.getCount();
        }
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final TextView date;
        public final TextView price;

        public ViewHolder(View itemView) {
            super(itemView);

            date = (TextView) itemView.findViewById(R.id.detail_item_date);
            price = (TextView) itemView.findViewById(R.id.detail_item_quote);
        }
    }
}
