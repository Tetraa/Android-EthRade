package com.example.aureliengiudici.ethrade.Fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.aureliengiudici.ethrade.BarCodeManager.BarcodeEncoder;
import com.example.aureliengiudici.ethrade.Contracts.WalletConfiguration;
import com.example.aureliengiudici.ethrade.MainActivity;
import com.example.aureliengiudici.ethrade.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * create an instance of this fragment.
 */
public class WalletFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match

    private final String TAG = "WalletFragment";
    private ImageView qrCode;
    private String address;
    private String balance;
    private TextView tvAdress;
    private TextView tvBalance;
    private WalletConfiguration walletConfiguration;


    public WalletFragment() {
        // Required empty public constructor

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Bundle args = getArguments();

        address =  args.getString("address");
        balance = args.getString("balance") + " eth";
        if (address == null) {
            MainActivity activity = (MainActivity) getActivity();
            address = activity.getAddress();
        }
            MainActivity activity = (MainActivity) getActivity();
            balance = activity.getBalance();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Do something that differs the Activity's menu here
        super.onCreateOptionsMenu(menu, inflater);
    }

    public void setWalletConfiguration(WalletConfiguration walletConfiguration) {
        this.walletConfiguration = walletConfiguration;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_wallet, container, false);
        qrCode  = (ImageView) view.findViewById(R.id.qr_code);
        tvAdress = (TextView) view.findViewById(R.id.tv_address);
        tvBalance = (TextView) view.findViewById(R.id.tv_wallet_value);
       try {
            tvAdress.setText(balance);
            tvBalance.setText(address);
            generateQrCode();
        } catch (Exception e) {
            Log.d(TAG, e.toString());
        }
        return view;
    }


    public void generateQrCode() {
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(address, BarcodeFormat.QR_CODE,300,300);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            qrCode.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }
}
