package com.zinzin.lolcounter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.snackbar.Snackbar;
import com.zinzin.lolcounter.adapter.HeroAdapter;
import com.zinzin.lolcounter.model.ItemHero;
import com.zinzin.lolcounter.utils.Utils;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ImageView ivheader;
    private RecyclerView rvHero;
    private EditText edtSearch;
    private HeroAdapter heroAdapter;
    private ArrayList<ItemHero> listHero = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.collapsing_toolbar);
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar_layout);
        collapsingToolbarLayout.setTitle("Khắc Chế Tướng LoL");
        collapsingToolbarLayout.setExpandedTitleColor(Color.TRANSPARENT);
        mSwipeRefreshLayout = findViewById(R.id.swipeView);
        ivheader = findViewById(R.id.iv_header);
        edtSearch = findViewById(R.id.edt_search);
        rvHero = findViewById(R.id.rcv_hero);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (Utils.isNetworkConnected(MainActivity.this)) {
                    setUpView();
                } else {
                    if (mSwipeRefreshLayout.isRefreshing()) {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                    final Snackbar snackbar = Snackbar.make(rvHero, "Không có kết nối internet", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }

            }
        });
        setUpView();
    }

    private void setUpView() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            ArrayList<ItemHero> arrHero = bundle.getParcelableArrayList("data");
            if (arrHero != null) {
                listHero.clear();
                listHero.addAll(arrHero);
            }
            initHeader(bundle.getString("urlHeader"));
        }
        initRecyclerView();
        initEdt();
    }

    private void initEdt() {
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }
        });
    }

    private void initRecyclerView() {
        GridLayoutManager adapterManager = new GridLayoutManager(MainActivity.this, 3);
        rvHero.setLayoutManager(adapterManager);
        heroAdapter = new HeroAdapter(MainActivity.this, listHero);
        rvHero.setAdapter(heroAdapter);
        heroAdapter.setListener(new HeroAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(ItemHero item, int position) {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra("Url", item.getUrl());
                intent.putExtra("Title", item.getBaseName());
                intent.putExtra("Name", item.getName());
                startActivity(intent);
            }
        });
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    private void initHeader(String result) {
        Glide.with(MainActivity.this).load("https://lienminh.garena.vn" + result).optionalCenterCrop().error(R.drawable.header_default).into(ivheader);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    private void filter(String text) {
        List<ItemHero> heroBase = new ArrayList<>(listHero);
        List<ItemHero> listUnitsFilter = new ArrayList();
        for (ItemHero units : heroBase) {
            //filter theo tên
            if (units.getName().toLowerCase().contains(text.toLowerCase())) {
                listUnitsFilter.add(units);
            }
        }
        if (text.equals("")) {
            listUnitsFilter.clear();
            heroAdapter.updateList(heroBase);
        } else {
            heroAdapter.updateList(listUnitsFilter);
        }
    }

}
