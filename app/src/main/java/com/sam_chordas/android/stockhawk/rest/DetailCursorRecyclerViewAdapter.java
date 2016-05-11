package com.sam_chordas.android.stockhawk.rest;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.data.QuoteColumns;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import lecho.lib.hellocharts.util.ChartUtils;

/**
 * Created by rodrigo.alencar on 5/9/16.
 */
public class DetailCursorRecyclerViewAdapter extends CursorRecyclerViewAdapterAbstract<DetailCursorRecyclerViewAdapter.ViewHolder> {

    private Context context;

    public DetailCursorRecyclerViewAdapter(Context context, Cursor cursor) {
        super(context, cursor);
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_quote_recycler_view_item, parent, false);
        ViewHolder vh = new ViewHolder(itemView);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Cursor cursor) {
        int isUp = cursor.getInt(cursor.getColumnIndex(QuoteColumns.ISUP));
        long time = Long.parseLong(cursor.getString(cursor.getColumnIndex(QuoteColumns.CREATED)));
        Date date = new Date();
        date.setTime(time);
        SimpleDateFormat simpleDateFormat = (SimpleDateFormat) SimpleDateFormat.getDateTimeInstance();
        String dt = simpleDateFormat.format(date);

        viewHolder.date.setText(dt);
        viewHolder.price.setText(cursor.getString(cursor.getColumnIndex(QuoteColumns.BIDPRICE)));
        viewHolder.price.setTextColor(ChartUtils.COLOR_BLUE);
        viewHolder.percentage.setText(cursor.getString(cursor.getColumnIndex(QuoteColumns.PERCENT_CHANGE)));
        if(isUp == 1) {
            viewHolder.percentage.setTextColor(context.getResources().getColor(android.R.color.holo_green_light));
        } else {
            viewHolder.percentage.setTextColor(context.getResources().getColor(android.R.color.holo_red_light));
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView date;
        public final TextView price;
        public final TextView percentage;

        public ViewHolder(View itemView) {
            super(itemView);

            date = (TextView) itemView.findViewById(R.id.detail_item_date);
            price = (TextView) itemView.findViewById(R.id.detail_item_quote);
            percentage = (TextView) itemView.findViewById(R.id.detail_item_percentage);
        }
    }
}
