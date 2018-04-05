package com.example.aureliengiudici.ethrade.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aureliengiudici.ethrade.Model.UserModel;
import com.example.aureliengiudici.ethrade.R;

import java.util.ArrayList;

/**
 * Created by aureliengiudici on 23/03/2018.
 */

public class CustomAdapter extends ArrayAdapter<UserModel> implements View.OnClickListener{

    private ArrayList<UserModel> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView txtName;
        TextView txtType;
        TextView txtAddress;
    }

    public CustomAdapter(ArrayList<UserModel> data, Context context) {
        super(context, R.layout.row_item, data);
        this.dataSet = data;
        this.mContext=context;

    }

    @Override
    public void onClick(View v) {

        int position=(Integer) v.getTag();
        Object object= getItem(position);
        UserModel userModel=(UserModel)object;

        switch (v.getId())
        {
            case R.id.tv_address:
                if (userModel.getType() != "contact")
                this.dialogCreation();
        }
    }

    private void dialogCreation() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this.mContext);
        alertDialogBuilder.setTitle("AlertDialog Title");
        alertDialogBuilder
                .setMessage("Some Alert Dialog message.")
                .setCancelable(false)
                .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        Toast.makeText(mContext, "Add as contact ", Toast.LENGTH_SHORT).show();

                    }
                })
                .setNegativeButton("CANCEL",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(mContext, "CANCEL button click ", Toast.LENGTH_SHORT).show();

                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        UserModel userModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item, parent, false);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.name);
            viewHolder.txtType = (TextView) convertView.findViewById(R.id.type);
            viewHolder.txtAddress = (TextView) convertView.findViewById(R.id.tv_address);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;

        viewHolder.txtName.setText(userModel.getName());
        viewHolder.txtType.setText(userModel.getType());
        viewHolder.txtAddress.setText(userModel.getAddress());
        // Return the completed view to render on screen
        return convertView;
    }
}