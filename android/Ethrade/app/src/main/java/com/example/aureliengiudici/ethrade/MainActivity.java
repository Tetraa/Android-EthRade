package com.example.aureliengiudici.ethrade;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.aureliengiudici.ethrade.BarCodeManager.QRCodeActivity;
import com.example.aureliengiudici.ethrade.Configuration.ConfigurationManager;
import com.example.aureliengiudici.ethrade.Fragment.ContactFragment;
import com.example.aureliengiudici.ethrade.Fragment.HistoryFragment;
import com.example.aureliengiudici.ethrade.Fragment.LoginFragment;
import com.example.aureliengiudici.ethrade.Fragment.SendEthFragment;
import com.example.aureliengiudici.ethrade.Fragment.SettingsFragment;
import com.example.aureliengiudici.ethrade.Fragment.WalletFragment;
import com.example.aureliengiudici.ethrade.Contracts.WalletConfiguration;
import com.example.aureliengiudici.ethrade.Utils.FileDialog;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


import java.io.File;
import java.math.BigInteger;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private ConfigurationManager configurationManager;
    private Context context;

    private static final int READ_REQUEST_CODE = 42;
    private static final int REQUEST_CODE_ASK_PERMISSIONS = 99;

    private WalletConfiguration config;
    private String walletFilePath;
    private String address;
    private String balance;
    private String sendAddress;

    private EditText password;
    private final int LOGIN = 0;
    private final int CONTACT = 1;
    private final int SEND = 2;
    private final int SETTINGS = 3;
    private final int HISTO = 4;


    /**
     * init
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();
        sendAddress = null;
        configurationManager = new ConfigurationManager(context);
        this.initViews();
        Dialog passwordDialog = this.onCreateDialogPassword();
        passwordDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        for(int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
                SpannableString spanString = new SpannableString(menu.getItem(i).getTitle().toString());
                int end = spanString.length();
                spanString.setSpan(new RelativeSizeSpan(0.2f), 0, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                item.setTitle(spanString);
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.wallet_menu:
                this.initWalletFragment();
                break;
            case R.id.contact_menu:
                this.initFragment(CONTACT);
                break;
            case R.id.settings_menu:
                this.initFragment(SETTINGS);
                break;
            case R.id.send_eth_menu:
                this.initFragment(SEND);
                break;
            case R.id.login_menu:
                this.initFragment(LOGIN);
                break;
            case R.id.histo_menu:
                this.initFragment(HISTO);
                break;
        }
        return false;
    }

    private void initViews() {
        final Button createWallet = (Button) findViewById(R.id.create_wallet);
        Button importWallet = (Button) findViewById(R.id.import_wallet);

        createWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeView();
                createWalletFile();
            }
        });

        importWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeView();
                checkWalletFile();

            }
        });
    }

        public Dialog onCreateDialogPassword() {
            LayoutInflater inflater = this.getLayoutInflater();
            View view = inflater.inflate(R.layout.dialog_password, null);
            password = (EditText)view.findViewById(R.id.dl_password);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(view)
                    .setMessage("Please enter your password to create or open you wallet !")
                    .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            if (password.getText().toString().equals("")) return;
                            configurationManager.putString("password", password.getText().toString());
                            Log.d("Password", configurationManager.getString("password"));
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            return builder.create();
        }

    private void changeView() {
        View layout = findViewById(R.id.activity_main);
        ViewGroup parent = (ViewGroup) layout.getParent();
        parent.removeView(layout);
        View yourProfileView = getLayoutInflater().inflate(R.layout.fragment_basic, parent, false);
        parent.addView(yourProfileView);
    }

    /**
     * Wallet
     */

    private void createWalletFile() {
        this.config = new WalletConfiguration(context);
        if (!config.start(configurationManager.getString("password")))
            this.retryDialog();
        this.reloadWalletParam(config);
        this.initWalletFragment();
    }

    void retryDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Please retry to put the good password !")
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        recreate();
                    }
                });
        // Create the AlertDialog object and return it
        AlertDialog dialog =  builder.create();
        dialog.show();
    }

    private void openWalletFile(String path) {
        File file = new File(path);
        Log.i(TAG, "File uri: " + file.getPath());
        Log.i(TAG, file.getName());
        if (checkPerms()) {
            WalletConfiguration config = new WalletConfiguration(context);
            configurationManager.putString("file", file.toString());
            if (!config.start(this.configurationManager.getString("password"), file)) {
                this.retryDialog();
                return;
            }
            this.reloadWalletParam(config);
            this.initWalletFragment();
        }
    }

    private void reloadWalletParam(WalletConfiguration config) {
        address = config.getAddress();
        configurationManager.putString("address", address);
        Log.d("Password", configurationManager.getString("password"));
        Log.d("Address", configurationManager.getString("address"));
        balance = config.getBalance(address).toString();
        configurationManager.putString("balance", balance);
    }

    public String getBalance() {
        if (configurationManager == null) {
            configurationManager = new ConfigurationManager(getContext());
        }
        return config.getBalance(configurationManager.getString("balance")).toString();
    }

    public String getSendAddress() {
        return sendAddress;
    }

    public Boolean sendEth(String _sendAddress, long value) {
        try {
            config = new WalletConfiguration(getContext());
                if (configurationManager == null) {
                    configurationManager = new ConfigurationManager(getContext());
                }
                _sendAddress = configurationManager.getString("sendAddress");
                address = configurationManager.getString("address");
                Log.d("File", configurationManager.getString("file"));
                String pass = configurationManager.getString("password");
                //config.getWallet(pass, new File(configurationManager.getString("file")));
             if (!config.sendEther(_sendAddress, address, BigInteger.valueOf(value), pass)) return false;
             balance = this.config.getBalance(address).toString();
             return true;
        } catch (Exception e) {
            Log.d(TAG, "Send eth error");
        }
        return false;
    }

    public String getAddress() {
        if (configurationManager == null)
            configurationManager = new ConfigurationManager(getContext());
        return configurationManager.getString("address");
    }

    public void checkWalletFile() {
        if (Build.VERSION.SDK_INT >= 19) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"text/*"});
            startActivityForResult(intent, READ_REQUEST_CODE);
        }
        else {
            FileDialog  fileDialog;
            File mPath = new File(Environment.getExternalStorageDirectory().toString());
            fileDialog = new FileDialog(this, mPath, ".json");
            fileDialog.addFileListener(new FileDialog.FileSelectedListener() {
                public void fileSelected(File file) {
                    Log.d(getClass().getName(), "selected file " + file.toString());
                    walletFilePath = file.toString();
                    openWalletFile(file.toString());
                }
            });
            fileDialog.showDialog();
        }
    }

    private Context getContext() {
        return this.context;
    }


    /**
     *  Fragment / Intent
     */
    private void initWalletFragment(){
       Fragment fragment = new WalletFragment();

        Bundle bundle = new Bundle();
        bundle.putString("address", address);
        bundle.putString("balance", balance);
        android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        fragment.setArguments(bundle);
        ft.replace(R.id.fragment_frame, fragment);
        ft.commit();
    }

    private void initFragment(int fragmentId){
        Fragment fragment;

        switch (fragmentId) {
            case LOGIN:
                 fragment = new LoginFragment();
                break;
            case SEND:
                fragment = new SendEthFragment();
                break;
            case CONTACT:
                fragment = new ContactFragment();
                break;
            case SETTINGS:
                fragment = new SettingsFragment();
                break;
            case HISTO:
                fragment = new HistoryFragment();
                break;
             default:
                return;
        }
        android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_frame, fragment);
        ft.commit();
    }

    private void initRegisterFragment() {

    }


    private void StartQRCodeIntent() {
        Intent myIntent = new Intent(this.getContext(), QRCodeActivity.class);
        startActivityForResult(myIntent, 0);
    }


    private void ContactFragment() {
        Fragment fragment = new ContactFragment();
        android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_frame, fragment);
        ft.commit();
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, resultData);
        if (scanningResult != null) {
            String scanContent = scanningResult.getContents();
            Toast toast = Toast.makeText(getContext(),
                    "Scan data Receive:" + scanContent, Toast.LENGTH_SHORT);
            toast.show();
            sendAddress = scanContent;
            configurationManager.putString("sendAddress", sendAddress);
        }else{
            Toast toast = Toast.makeText(getContext(),
                    "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }

        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {

            Uri uri = null;
            if (resultData != null) {
                uri = resultData.getData();
                Log.i(TAG, "Uri: " + uri.getPath());
                this.walletFilePath = uri.getPath();
                this.openWalletFile(uri.getPath());
            }
        }
    }

    /**
     * Perms
     */
    private Boolean checkPerms() {
        if (Build.VERSION.SDK_INT <= 23) {
            return checkOldPermissions();
        } return checkPermissions();
    }

    private boolean checkOldPermissions() {
        String[] perms = new String[] {
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.INTERNET,
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
        };
        int permission;
        for (String perm : perms){
            permission = PermissionChecker.checkSelfPermission(this, perm);
            if (!(permission == PermissionChecker.PERMISSION_GRANTED)) {
                return false;
            }
        }
        System.out.println("Ethrade permission granded");
        return true;
    }

    /**
     * App need permissions to work
     */
    private boolean checkPermissions() {
        // Check permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.ACCESS_NETWORK_STATE,
                            Manifest.permission.INTERNET,
                            Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                    },
                    REQUEST_CODE_ASK_PERMISSIONS);
            return false;
        } else {
            System.out.println("Adotmob permissions are OKOKOK");
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    System.out.println("Ethrade permission granded");
                } else {
                    // force user to accept
                    Toast.makeText(this, "You have to accept the permissions", Toast.LENGTH_LONG).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
