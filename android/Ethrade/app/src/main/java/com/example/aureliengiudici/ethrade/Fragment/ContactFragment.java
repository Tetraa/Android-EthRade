package com.example.aureliengiudici.ethrade.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.aureliengiudici.ethrade.Adapters.CustomAdapter;
import com.example.aureliengiudici.ethrade.Contracts.WalletConfiguration;
import com.example.aureliengiudici.ethrade.Model.UserModel;
import com.example.aureliengiudici.ethrade.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link ContactFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContactFragment extends Fragment {
    private ArrayList<UserModel> userModels;
    private static CustomAdapter adapter;
    private WalletConfiguration walletConfiguration;


    public ContactFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ContactFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ContactFragment newInstance() {
        ContactFragment fragment = new ContactFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact, container, false);

        ListView listView = (ListView) view.findViewById(R.id.list);

        userModels= new ArrayList<>();

        userModels.add(new UserModel("John", "test@gmail.com", "15342542354",true));
        userModels.add(new UserModel("Banana Bread", "test2@gmail.com", "2543254325",true));
        userModels.add(new UserModel("Cupcake", "test2@gmail.com", "354325432543254325",true));
        userModels.add(new UserModel("Donut","test2@gmail.com","4543254325345432",true));
        userModels.add(new UserModel("Eclair", "test2@gmail.com", "55342543254325432",false));
        userModels.add(new UserModel("Froyo", "test2@gmail.com", "854325432543254325",false));
        userModels.add(new UserModel("Gingerbread", "test2@gmail.com", "954235432543254325423",false));
        userModels.add(new UserModel("Honeycomb","test2@gmail.com","1153425432524354235423234",false));
        userModels.add(new UserModel("Honeycomb","test2@gmail.com","1153425432524354235423234",false));
        userModels.add(new UserModel("Honeycomb","test2@gmail.com","1153425432524354235423234",false));
        userModels.add(new UserModel("Honeycomb","test2@gmail.com","1153425432524354235423234",false));
        userModels.add(new UserModel("Honeycomb","test2@gmail.com","1153425432524354235423234",false));


        adapter= new CustomAdapter(userModels, getContext());

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                UserModel userModel = userModels.get(position);


                //Snackbar.make(view, userModel.getName()+"\n"+userModel.getAddress() +userModel.getType(), Snackbar.LENGTH_LONG)
                       // .setAction("No action", null).show();
            }
        });
        return view;
    }



    public void setWalletConfiguration(WalletConfiguration walletConfiguration) {
        this.walletConfiguration = walletConfiguration;
    }

    /*
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    } */
}
