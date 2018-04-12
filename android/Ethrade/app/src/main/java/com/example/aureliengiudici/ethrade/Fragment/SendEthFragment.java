package com.example.aureliengiudici.ethrade.Fragment;


import android.app.AlertDialog;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.aureliengiudici.ethrade.BarCodeManager.QRCodeActivity;
import com.example.aureliengiudici.ethrade.Configuration.ConfigurationManager;
import com.example.aureliengiudici.ethrade.Contracts.WalletConfiguration;
import com.example.aureliengiudici.ethrade.MainActivity;
import com.example.aureliengiudici.ethrade.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.kenai.jffi.Main;


/**
 * Created by aureliengiudici on 02/04/2018.
 */

public class SendEthFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match

    private final String TAG = "SendEthFragment";
    private String address;
    private String value;
    private EditText etAddress;
    private EditText etValue;
    private Button   btSend;
    private Button   btCancel;
    private Button  scan;
    private ConfigurationManager configurationManager;


    public SendEthFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        configurationManager = new ConfigurationManager(getContext());
        address = null;
        value = null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }



    private void InitView(View   view) {
        etAddress = (EditText) view.findViewById(R.id.et_address);
        etValue = (EditText) view.findViewById(R.id.et_value);
        btSend = (Button) view.findViewById(R.id.btn_send_eth);
        btCancel = (Button) view.findViewById(R.id.btn_cancel);
        scan = (Button) view.findViewById(R.id.scan_button);

        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (address == null) {
                    address = etAddress.getText().toString();
                    MainActivity activity = (MainActivity)getActivity();
                    address = (address == null ? activity.getSendAddress() : address);
                    etAddress.setText(address);
                }
                value = etValue.getText().toString();
                if (address != null &&
                        (value != null && value.matches("^-?\\d+(\\.\\d+)?$"))){
                    Log.d(TAG, Long.valueOf(value) + " WERid");

                    MainActivity activity = (MainActivity) getActivity();
                    Log.d(TAG, Long.valueOf(value) + " WERid");
                    if (activity.sendEth(address, Long.valueOf(value))) {
                        displayAlert("Success", "Ether are well send");
                    } else {
                        displayAlert("Error", "Transaction Error");
                    }
                } else {
                   displayAlert("Cannot Send", "Please enter a valid address and a good value");
                }
            }
        });

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etAddress.setText("");
                etValue.setText("");
            }
        });

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator scanIntegrator =  IntentIntegrator.forSupportFragment(SendEthFragment.this);
                scanIntegrator.initiateScan();
                address = configurationManager.getString("sendAddress");
                etAddress.setText(address);
            }
        });
    }


    private void displayAlert(String title, String str) {
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(str);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.dialog_send, container, false);
        this.InitView(view);
        return view;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, resultData);
        if (scanningResult != null) {
            String scanContent = scanningResult.getContents();
            Toast toast = Toast.makeText(getContext(),
                    "Scan data Receive:" + scanContent, Toast.LENGTH_SHORT);
            toast.show();
            address = scanContent;
            configurationManager.putString("sendAddress", address);
        } else {
            Toast toast = Toast.makeText(getContext(),
                    "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}

