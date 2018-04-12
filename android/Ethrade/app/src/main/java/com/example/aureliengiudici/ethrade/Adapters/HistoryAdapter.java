package com.example.aureliengiudici.ethrade.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.aureliengiudici.ethrade.Model.HistoryModel;
import com.example.aureliengiudici.ethrade.R;

import java.util.ArrayList;

/**
 * Created by aureliengiudici on 05/04/2018.
 */

public class HistoryAdapter extends ArrayAdapter<HistoryModel> {

    private ArrayList<HistoryModel> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView txtDate;
        TextView txtAddress;
        TextView txtHisto;
    }

    public HistoryAdapter(ArrayList<HistoryModel> data, Context context) {
        super(context, R.layout.row_item, data);
        this.dataSet = data;
        this.mContext = context;

    }


    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        HistoryModel historyModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item_histo, parent, false);
            viewHolder.txtAddress = (TextView) convertView.findViewById(R.id.et_address);
            viewHolder.txtDate = (TextView) convertView.findViewById(R.id.date);
            viewHolder.txtHisto = (TextView) convertView.findViewById(R.id.tv_histo);

            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;

        //viewHolder.txtDate.setText(historyModel.getDate());
        //viewHolder.txtAddress.setText(historyModel.getAddress());
        //viewHolder.txtHisto.setText(historyModel.toString());
        // Return the completed view to render on screen
        return convertView;
    }
}

