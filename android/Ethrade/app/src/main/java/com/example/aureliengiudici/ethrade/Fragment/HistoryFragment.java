package com.example.aureliengiudici.ethrade.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.aureliengiudici.ethrade.Adapters.CustomAdapter;
import com.example.aureliengiudici.ethrade.Adapters.HistoryAdapter;
import com.example.aureliengiudici.ethrade.Contracts.WalletConfiguration;
import com.example.aureliengiudici.ethrade.Model.HistoryModel;
import com.example.aureliengiudici.ethrade.Model.UserModel;
import com.example.aureliengiudici.ethrade.R;

import java.util.ArrayList;

/**
 * Created by aureliengiudici on 05/04/2018.
 */

public class HistoryFragment extends Fragment {
    private ArrayList<HistoryModel> histoModel;
    private static HistoryAdapter adapter;


    public HistoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ContactFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HistoryFragment newInstance() {
        HistoryFragment fragment = new HistoryFragment();
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

        histoModel= new ArrayList<>();

        histoModel.add(new HistoryModel("John", "test@gmail.com", "15342542354",true));
        histoModel.add(new HistoryModel("Banana Bread", "test2@gmail.com", "2543254325",true));
        histoModel.add(new HistoryModel("Cupcake", "test2@gmail.com", "354325432543254325",true));
        histoModel.add(new HistoryModel("Donut","test2@gmail.com","4543254325345432",true));
        histoModel.add(new HistoryModel("Eclair", "test2@gmail.com", "55342543254325432",false));
        histoModel.add(new HistoryModel("Froyo", "test2@gmail.com", "854325432543254325",false));



        adapter= new HistoryAdapter(histoModel, getContext());

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                HistoryModel historyModel = histoModel.get(position);


                //Snackbar.make(view, userModel.getName()+"\n"+userModel.getAddress() +userModel.getType(), Snackbar.LENGTH_LONG)
                // .setAction("No action", null).show();
            }
        });
        return view;
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

