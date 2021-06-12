package com.example.finalprojekpm.menu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalprojekpm.R;
import com.example.finalprojekpm.adapter.TVShowAdapater;
import com.example.finalprojekpm.api.ApiClient;
import com.example.finalprojekpm.api.ApiInterface;
import com.example.finalprojekpm.modelTVShow.ResponseTVShow;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TvShow extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TvShow() {

    }
    public static com.example.finalprojekpm.menu.TvShow newInstance(String param1, String param2) {
        com.example.finalprojekpm.menu.TvShow fragment = new com.example.finalprojekpm.menu.TvShow();
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

    RecyclerView rv_content;
    private TVShowAdapater adapter;
    String APIKEY = "92e614bd264d164515d45076380f67d5";
    String lang = "en-US";
    String category = "airing_today";
    int PAGE = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_now_playing, container, false);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        GridLayoutManager grid = new GridLayoutManager(getContext(), 2);

        rv_content = view.findViewById(R.id.rv_now_playing);
        rv_content.setHasFixedSize(true);
        rv_content.setLayoutManager(grid);

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseTVShow> call = apiInterface.getTVShow(category, APIKEY, lang, PAGE);

        call.enqueue(new Callback<ResponseTVShow>() {
            @Override
            public void onResponse(Call<ResponseTVShow> call, Response<ResponseTVShow> response) {
                List <com.example.finalprojekpm.modelTVShow.ResultTVShow> mlist = response.body().getResultTVShows();
                adapter = new TVShowAdapater(getContext(), mlist);
                rv_content.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<ResponseTVShow> call, Throwable t) {

            }
        });

        return view;

    }
}