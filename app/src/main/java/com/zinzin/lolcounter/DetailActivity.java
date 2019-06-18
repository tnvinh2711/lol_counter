package com.zinzin.lolcounter;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;

import android.text.Spanned;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.zinzin.lolcounter.adapter.CounterAdapter;
import com.zinzin.lolcounter.adapter.InfoAdapter;
import com.zinzin.lolcounter.adapter.ItemAdapter;
import com.zinzin.lolcounter.model.DetailCounter;
import com.zinzin.lolcounter.model.ItemHero;
import com.zinzin.lolcounter.utils.Utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import static com.zinzin.lolcounter.utils.Constant.URL_DOMAIN;

public class DetailActivity extends AppCompatActivity implements Html.ImageGetter {
    private TextView tvName, tvStrong, tvWeak, tvTips;
    private ImageView ivHeader, ivAva;
    private String name;
    private RecyclerView rvClass, rvLane, rvItem, rvBuff, rvCounter, rvStrong;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getIntent().getStringExtra("Title"));
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.getNavigationIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        initView();
        name = getIntent().getStringExtra("Name");
        parseHtml(getIntent().getStringExtra("Url"));


    }

    private void initView() {
        ivHeader = findViewById(R.id.iv_header);
        ivAva = findViewById(R.id.iv_ava);
        tvName = findViewById(R.id.tv_name);
        tvStrong = findViewById(R.id.tv_strong);
        tvWeak = findViewById(R.id.tv_weak);
        tvTips = findViewById(R.id.tv_tips);
        rvClass = findViewById(R.id.rcv_status);
        rvLane = findViewById(R.id.rcv_lane);
        rvItem = findViewById(R.id.rcv_item);
        rvBuff = findViewById(R.id.rcv_buff);
        rvCounter = findViewById(R.id.rcv_weak);
        rvStrong = findViewById(R.id.rcv_strong);
    }

    @SuppressLint("StaticFieldLeak")
    private void parseHtml(final String url) {
        new AsyncTask<Void, Void, DetailCounter>() {

            public DetailCounter doInBackground(Void... params) {
                DetailCounter detailCounter = new DetailCounter();
                Document doc;
                try {
                    doc = Jsoup.connect(url).get();
                    Element header = doc.getElementsByClass("col-sm-12 col-xs-12 pl0 pr0 mb5").first();
                    Element ava = doc.getElementsByClass("col-sm-7 col-xs-12 lg_pr0").first();
                    Elements listStatus = doc.getElementsByClass("list_type");
                    Elements listLane = doc.getElementsByClass("list_type1");
                    Element nameElement = doc.getElementsByClass("alias").first();
                    Elements item = doc.getElementsByClass("col-sm-12 col-xs-12 box_champ_items pr0").first().select("img");
                    Elements buff = doc.getElementsByClass("col-sm-12 col-xs-12 pr0 pl0 box_summoner mt0").first().select("img");

                    Elements counter = doc.getElementsByClass("col-sm-12 col-xs-12 box_list_champs_c").first().getElementsByClass("col-sm-12 col-xs-12 list_champs_c list_champs_c1");
                    Elements strong = doc.getElementsByClass("col-sm-12 col-xs-12 box_list_champs_c box_list_champs_str").first().getElementsByClass("col-sm-12 col-xs-12 list_champs_c list_champs_c1");

                    Element tips = doc.getElementsByClass("col-sm-12 col-xs-12 mt10 box_counter_text").first();

                    List<ItemHero> listCounter = new ArrayList<>();
                    List<ItemHero> listStrong = new ArrayList<>();


                    List<String> listS = new ArrayList<>();
                    List<String> listL = new ArrayList<>();
                    List<String> listBuff = new ArrayList<>();
                    List<String> listItem = new ArrayList<>();
                    for (Element element : listStatus) {
                        listS.add(element.text());
                    }
                    for (Element element : listLane) {
                        listL.add(element.text());
                    }
                    for (Element element : item) {
                        listItem.add(element.attr("src"));
                    }
                    for (Element element : buff) {
                        listBuff.add(URL_DOMAIN + element.attr("src"));
                    }

                    for (Element element : counter) {
                        List<String> listItemStrong = new ArrayList<>();
                        ItemHero itemHero = new ItemHero();
                        itemHero.setName(element.select("img").attr("title"));
                        itemHero.setUrl(URL_DOMAIN + element.select("img").attr("src"));
                        Elements items = element.getElementsByClass("box_champ_items_c box_champ_items_c1").first().select("img");
                        for (Element itemz : items) {
                            listItemStrong.add(itemz.attr("src"));
                        }
                        itemHero.setItem(listItemStrong);
                        listStrong.add(itemHero);
                        if (listStrong.size() == 5) break;
                    }

                    for (Element element : strong) {
                        List<String> listItemCounter = new ArrayList<>();
                        ItemHero itemHero = new ItemHero();
                        itemHero.setName(element.select("img").attr("title"));
                        itemHero.setUrl(URL_DOMAIN + element.select("img").attr("src"));
                        Elements items = element.getElementsByClass("box_champ_items_c box_champ_items_c1").first().select("img");
                        for (Element itemz : items) {
                            listItemCounter.add(itemz.attr("src"));
                        }
                        itemHero.setItem(listItemCounter);
                        listCounter.add(itemHero);
                        if (listCounter.size() == 5) break;
                    }
                    String tip = tips.html();
                    detailCounter.setTips(tips.html());
                    detailCounter.setItemHeroesCounter(listCounter);
                    detailCounter.setItemHeroesNerf(listStrong);
                    detailCounter.setList_info(listS);
                    detailCounter.setList_lane(listL);
                    detailCounter.setList_item(listItem);
                    detailCounter.setList_buff(listBuff);
                    detailCounter.setUrl_header(URL_DOMAIN + header.select("a").attr("href"));
                    detailCounter.setUrl_ava(URL_DOMAIN + ava.select("img").attr("src"));
                    detailCounter.setName(name + " - " + nameElement.text());
                    detailCounter.setList_item(listItem);
                    detailCounter.setList_buff(listBuff);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                return detailCounter;
            }

            @Override
            protected void onPostExecute(DetailCounter detailCounter) {
                InfoAdapter classAdapter = new InfoAdapter(DetailActivity.this, detailCounter.getList_info(), R.color.color_class);
                InfoAdapter laneAdapter = new InfoAdapter(DetailActivity.this, detailCounter.getList_lane(), R.color.color_lane);
                ItemAdapter itemAdapter = new ItemAdapter(DetailActivity.this, detailCounter.getList_item(), 0);
                ItemAdapter buffAdapter = new ItemAdapter(DetailActivity.this, detailCounter.getList_buff(), 1);
                CounterAdapter counterAdapter = new CounterAdapter(DetailActivity.this, detailCounter.getItemHeroesCounter());
                CounterAdapter strongAdapter = new CounterAdapter(DetailActivity.this, detailCounter.getItemHeroesNerf());

                LinearLayoutManager layoutManager1 = new LinearLayoutManager(DetailActivity.this, LinearLayoutManager.HORIZONTAL, false);
                LinearLayoutManager layoutManager2 = new LinearLayoutManager(DetailActivity.this, LinearLayoutManager.HORIZONTAL, false);
                LinearLayoutManager layoutManager3 = new LinearLayoutManager(DetailActivity.this, LinearLayoutManager.HORIZONTAL, false);
                LinearLayoutManager layoutManager4 = new LinearLayoutManager(DetailActivity.this, LinearLayoutManager.HORIZONTAL, false);
                LinearLayoutManager layoutManager5 = new LinearLayoutManager(DetailActivity.this, LinearLayoutManager.VERTICAL, false);
                LinearLayoutManager layoutManager6 = new LinearLayoutManager(DetailActivity.this, LinearLayoutManager.VERTICAL, false);
                ViewCompat.setNestedScrollingEnabled(rvStrong, false);
                ViewCompat.setNestedScrollingEnabled(rvCounter, false);

                rvClass.setLayoutManager(layoutManager1);
                rvLane.setLayoutManager(layoutManager2);
                rvItem.setLayoutManager(layoutManager3);
                rvBuff.setLayoutManager(layoutManager4);
                rvStrong.setLayoutManager(layoutManager5);
                rvCounter.setLayoutManager(layoutManager6);

                rvClass.setAdapter(classAdapter);
                rvItem.setAdapter(itemAdapter);
                rvBuff.setAdapter(buffAdapter);
                rvLane.setAdapter(laneAdapter);
                rvStrong.setAdapter(strongAdapter);
                rvCounter.setAdapter(counterAdapter);
                Spanned spanned = Html.fromHtml(detailCounter.getTips(), DetailActivity.this, null);
                tvTips.setText(spanned);
                tvStrong.setText(name + " yếu khi đi với");
                tvWeak.setText(name + " mạnh khi đi với");
                tvName.setText(detailCounter.getName());
                ivAva.setPadding(-20, -20, -20, -20);
                RequestOptions requestOptions = new RequestOptions();
                requestOptions.centerCrop();
                Glide.with(DetailActivity.this).load(detailCounter.getUrl_ava()).apply(requestOptions).into(ivAva);
                Glide.with(DetailActivity.this).load(detailCounter.getUrl_header()).error(R.drawable.header_default).into(ivHeader);
            }
        }.execute();
    }

    @Override
    public Drawable getDrawable(String s) {
        LevelListDrawable d = new LevelListDrawable();
        d.addLevel(0, 0, null);
        d.setBounds(10, 10, 110, 110);
        new LoadImage().execute(s, d);
        return d;
    }


    class LoadImage extends AsyncTask<Object, Void, Bitmap> {
        private LevelListDrawable mDrawable;

        @Override
        protected Bitmap doInBackground(Object... params) {
            String source = (String) params[0];
            mDrawable = (LevelListDrawable) params[1];
            try {
                InputStream is = new URL(source).openStream();
                return BitmapFactory.decodeStream(is);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                BitmapDrawable d = new BitmapDrawable(Utils.getRoundedCornerBitmap(DetailActivity.this, bitmap));
                mDrawable.addLevel(1, 1, d);
                //mDrawable.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
                mDrawable.setBounds(10, 10, 110, 110);
                mDrawable.setLevel(1);
                CharSequence t = tvTips.getText();
                tvTips.setText(t);
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}
