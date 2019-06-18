package com.zinzin.lolcounter;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.wang.avi.AVLoadingIndicatorView;
import com.zinzin.lolcounter.model.ItemHero;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import static com.zinzin.lolcounter.utils.Constant.URL_DOMAIN;

public class SplashActivity extends AppCompatActivity {
    private AVLoadingIndicatorView loading;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        loading = findViewById(R.id.loading);
        YoYo.with(Techniques.Tada)
                .onEnd(new YoYo.AnimatorCallback() {
                    @Override
                    public void call(Animator animator) {
                        loadData();
                    }
                })
                .duration(1000)
                .repeat(1)
                .playOn(findViewById(R.id.iv_logo));
    }

    @SuppressLint("StaticFieldLeak")
    private void loadData() {
        new AsyncTask<Void, Void, ArrayList<ItemHero>>() {

            public ArrayList<ItemHero> doInBackground(Void... params) {
                ArrayList<ItemHero> listHero = new ArrayList<>();
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
                loading.hide();
                Intent intent = new Intent(SplashActivity.this,MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("data", result);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        }.execute();
    }
}
