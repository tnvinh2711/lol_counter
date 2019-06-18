package com.zinzin.lolcounter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.zinzin.lolcounter.adapter.HeroAdapter;
import com.zinzin.lolcounter.model.ItemHero;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.zinzin.lolcounter.utils.Constant.URL_DOMAIN;

public class MainActivity extends AppCompatActivity {
    private ImageView ivheader;
    private RecyclerView rvHero;
    private EditText edtSearch;
    private HeroAdapter heroAdapter;
    private  ArrayList<ItemHero>  listHero = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.collapsing_toolbar);
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar_layout);
        collapsingToolbarLayout.setTitle("Khắc Chế Tướng LoL");
        collapsingToolbarLayout.setExpandedTitleColor(Color.TRANSPARENT);
        // Set collapsing tool bar image.
        ivheader = findViewById(R.id.iv_header);
        edtSearch = findViewById(R.id.edt_search);
        rvHero = findViewById(R.id.rcv_hero);
        Bundle bundle = getIntent().getExtras();
        if(bundle!= null){
            ArrayList<ItemHero> arrHero = bundle.getParcelableArrayList("data");
            if (arrHero != null) {
                listHero.clear();
                listHero.addAll(arrHero);
            }
        }
        initHeader();
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

    @SuppressLint("StaticFieldLeak")
    private void initRecyclerView() {
        GridLayoutManager adapterManager  = new GridLayoutManager(MainActivity.this, 3);
        rvHero.setLayoutManager(adapterManager);
        heroAdapter = new HeroAdapter(MainActivity.this,listHero);
        rvHero.setAdapter(heroAdapter);
        heroAdapter.setListener(new HeroAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(ItemHero item, int position) {
                Intent intent = new Intent(MainActivity.this,DetailActivity.class);
                intent.putExtra("Url", item.getUrl());
                intent.putExtra("Title", item.getBaseName());
                intent.putExtra("Name", item.getName());
                startActivity(intent);
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    private void initHeader() {
        new AsyncTask<Void, Void, String>() {

            public String doInBackground(Void... params) {
                String title = "";
                Document doc;
                try {
                    doc = Jsoup.connect("https://lienminh.garena.vn/").get();
                    Element newsHeadlines = doc.select("div.default-1-2").first();
                    title = newsHeadlines.select("img").attr("src");

                } catch (IOException e) {
                    e.printStackTrace();
                }
                return title;
            }

            @Override
            protected void onPostExecute(String result) {
                Glide.with(MainActivity.this).load("https://lienminh.garena.vn" + result).optionalCenterCrop().error(R.drawable.header_default).into(ivheader);
            }
        }.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    private void filter(String text) {
        List<ItemHero> heroBase = new ArrayList<>();
        heroBase.addAll(listHero);
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
