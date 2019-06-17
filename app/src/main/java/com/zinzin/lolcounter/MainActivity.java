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
import android.util.Log;
import android.view.Menu;
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

public class MainActivity extends AppCompatActivity {
    private ImageView ivheader;
    private RecyclerView rvHero;
    public static String URL_DOMAIN = "https://skmoba.com";

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
        rvHero = findViewById(R.id.rcv_hero);
        initHeader();
        initRecyclerView();
    }
    @SuppressLint("StaticFieldLeak")
    private void initRecyclerView() {
        GridLayoutManager adapterManager  = new GridLayoutManager(MainActivity.this, 3);
        rvHero.setLayoutManager(adapterManager);
        final HeroAdapter heroAdapter = new HeroAdapter(MainActivity.this,new ArrayList<ItemHero>());
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
        new AsyncTask<Void, Void, ArrayList<ItemHero>>() {

            public ArrayList<ItemHero> doInBackground(Void... params) {
                ArrayList<ItemHero>  listHero = new ArrayList<>();
                Document doc;
                try {
                    doc = Jsoup.connect(URL_DOMAIN+"/tuong-khac-che").get();
                    Elements heroList = doc.getElementsByClass("col-lg-2 col-md-2 col-sm-2 col-xs-4 list_champ_home mb15");
                    for(Element element: heroList){
                        ItemHero itemHero = new ItemHero();
                        itemHero.setName(element.select("a").attr("title").replace("Khắc chế ",""));
                        itemHero.setBaseName(element.select("a").attr("title"));
                        itemHero.setUrl_image(URL_DOMAIN+element.select("img").attr("src"));
                        itemHero.setUrl(element.select("a").attr("href"));
                        listHero.add(itemHero);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
                return listHero;
            }

            @Override
            protected void onPostExecute(ArrayList<ItemHero> result) {
                heroAdapter.setList(result);
            }
        }.execute();
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
                Log.e("main", result);
                Glide.with(MainActivity.this).load("https://lienminh.garena.vn" + result).optionalCenterCrop().into(ivheader);
            }
        }.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
}
