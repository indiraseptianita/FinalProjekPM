package com.example.finalprojekpm.menu;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.finalprojekpm.R;
import com.example.finalprojekpm.adapter.MovAdapter;
import com.example.finalprojekpm.api.ApiClient;
import com.example.finalprojekpm.api.ApiInterface;
import com.example.finalprojekpm.modelMov.ResponseMov;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class Favorite extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public Favorite() {

    }

    public static Favorite newInstance(String param1, String param2) {
        Favorite fragment = new Favorite();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    String APIKEY = "92e614bd264d164515d45076380f67d5";
    String lang = "en-US";
    String category = "now_playing";
    int PAGE = 1;
    List <ResponseMov> list = new ArrayList <ResponseMov>();
    MovAdapter adapter;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    TextView tv_status;
    RecyclerView rv_favorite;

    String language;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        GridLayoutManager grid = new GridLayoutManager(getContext(), 2);


        rv_favorite = view.findViewById(R.id.rv_favorite);
        rv_favorite.setHasFixedSize(true);
        rv_favorite.setLayoutManager(grid);

        preferences = getActivity().getSharedPreferences("FavoriteMovie", Context.MODE_PRIVATE);
        editor = preferences.edit();

        language = preferences.getString("lang", "en");

        tv_status = view.findViewById(R.id.tv_status);
        rv_favorite = view.findViewById(R.id.rv_favorite);

        if (preferences.getString("favorite", "").equals("")) {
            tv_status.setVisibility(View.VISIBLE);
            tv_status.setText(language.equals("en") ? "Favorite Movie Not Found" : "Film Favorite Tidak Ditemukan");
            rv_favorite.setVisibility(View.GONE);
        } else {
            rv_favorite.setVisibility(View.VISIBLE);
            tv_status.setVisibility(View.GONE);
        }
        return view;
    }

    //    @Override
    public void onResume() {
        if (preferences.getString("favorite", "").equals("")) {
            tv_status.setVisibility(View.VISIBLE);
            tv_status.setText(language.equals("en") ? "Favorite Movie Not Found" : "Film Favorite Tidak Ditemukan");
            rv_favorite.setVisibility(View.GONE);
        } else {
            rv_favorite.setVisibility(View.VISIBLE);
            tv_status.setVisibility(View.GONE);


            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            String[] allFav = preferences.getString("favorite", "").split(" ");
            System.out.println("On Resume All Fav : " + preferences.getString("favorite", "") + " End");

            list.clear();
            for (int i = 0; i < allFav.length; i++) {

                Call <ResponseMov> call = apiInterface.getMovieById(allFav[i], APIKEY);
                int x = i;
                call.enqueue(new Callback <ResponseMov>() {
                    @Override
                    public void onResponse(Call <ResponseMov> call, retrofit2.Response <ResponseMov> response) {
//                        list.clear();
                        list.add(response.body());
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(Call <ResponseMov> call, Throwable t) {

                    }
                });
            }
            adapter = new MovAdapter(getContext(), list);
            rv_favorite.setAdapter(adapter);
        }
        super.onResume();
    }
}
