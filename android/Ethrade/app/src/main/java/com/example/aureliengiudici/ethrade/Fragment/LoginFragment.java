package com.example.aureliengiudici.ethrade.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.aureliengiudici.ethrade.Contracts.WalletConfiguration;
import com.example.aureliengiudici.ethrade.Model.UserModel;
import com.example.aureliengiudici.ethrade.NetworkManager.IRequestInterface;
import com.example.aureliengiudici.ethrade.NetworkManager.ServerRequest;
import com.example.aureliengiudici.ethrade.NetworkManager.ServerResponse;
import com.example.aureliengiudici.ethrade.R;
import com.example.aureliengiudici.ethrade.Utils.Constants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class LoginFragment extends Fragment implements View.OnClickListener{

    private AppCompatButton btn_login;
    private EditText et_email,et_password;
    private TextView tv_register;
    private SharedPreferences pref;
    private WalletConfiguration walletConfiguration;

    public void setWalletConfiguration(WalletConfiguration walletConfiguration) {
        this.walletConfiguration = walletConfiguration;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_login,container,false);
        initViews(view);
        return view;
    }

    private void initViews(View view){

        pref = getActivity().getPreferences(0);

        btn_login = (AppCompatButton)view.findViewById(R.id.btn_login);
        tv_register = (TextView)view.findViewById(R.id.tv_register);
        et_email = (EditText)view.findViewById(R.id.et_email);
        et_password = (EditText)view.findViewById(R.id.et_password);

        btn_login.setOnClickListener(this);
        tv_register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_register:
                goToRegister();
                break;
            case R.id.btn_login:
                String email = et_email.getText().toString();
                String password = et_password.getText().toString();
                if(!email.isEmpty() && !password.isEmpty()) {
                    loginProcess(email,password);
                } else {
                    Snackbar.make(getView(), "Fields are empty !", Snackbar.LENGTH_LONG).show();
                }
                break;
        }
    }

    private void loginProcess(String email,String password){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        IRequestInterface requestInterface = retrofit.create(IRequestInterface.class);
        UserModel user = new UserModel(email, password);
        ServerRequest request = new ServerRequest(Constants.LOGIN_OPERATION, user);
        Call<ServerResponse> response = requestInterface.operation(request);

        response.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {

                ServerResponse resp = response.body();
                Snackbar.make(getView(), resp.getMessage(), Snackbar.LENGTH_LONG).show();

                if(resp.getResult().equals(Constants.SUCCESS)){
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putBoolean(Constants.IS_LOGGED_IN,true);
                    editor.putString("name", resp.getUser().getName());
                    //all the thing that the api need
                    editor.apply();
                    displayAlert("Success", "You're now login");
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                Log.d(Constants.TAG,"failed");
                Snackbar.make(getView(), t.getLocalizedMessage(), Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private AlertDialog displayAlert(String title, String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
        return alertDialog;
    }


    private void goToRegister(){
        Fragment register = new RegisterFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_frame,register);
        ft.commit();
    }
}