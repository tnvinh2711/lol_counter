package com.zinzin.lolcounter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.snackbar.Snackbar;
import com.zinzin.lolcounter.adapter.HeroAdapter;
import com.zinzin.lolcounter.model.ItemHero;
import com.zinzin.lolcounter.utils.Preference;
import com.zinzin.lolcounter.utils.Utils;
import com.zinzin.lolcounter.utils.WelcomeDialog;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ImageView ivheader;
    private RecyclerView rvHero;
    private EditText edtSearch;
    private AdView mAdView;
    private boolean isLoadAd;
    private boolean isLoadClickAd;
    private int clickitem = 0;
    private WelcomeDialog welcomeDialog;
    private InterstitialAd mInterstitialAd, mInterstitialAdClick;
    private HeroAdapter heroAdapter;
    private ArrayList<ItemHero> listHero = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ivheader = findViewById(R.id.iv_header);
        edtSearch = findViewById(R.id.edt_search);
        rvHero = findViewById(R.id.rcv_hero);
        MobileAds.initialize(this, "ca-app-pub-5796098881172039~8728680952");
        mAdView = findViewById(R.id.adView);
        mAdView.setVisibility(View.GONE);
        setUpDialog();
        loadAd();
        setUpToolBar();
        setUpSwipeView();
        setUpView();
    }

    private void setUpSwipeView() {
        mSwipeRefreshLayout = findViewById(R.id.swipeView);
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
    }

    private void setUpToolBar() {
        Toolbar toolbar = findViewById(R.id.collapsing_toolbar);
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar_layout);
        collapsingToolbarLayout.setTitle("Khắc Chế Tướng LoL");
        collapsingToolbarLayout.setExpandedTitleColor(Color.TRANSPARENT);
    }

    private void setUpDialog() {
        welcomeDialog = new WelcomeDialog(this, new WelcomeDialog.DialogCallBack() {
            @Override
            public void onClickOpen() {
                if(isLoadClickAd) {
                    mInterstitialAdClick.show();
                    isLoadClickAd = false;
                } else {
                    Toast.makeText(MainActivity.this, "Không có quảng cáo, xin vui lòng mở sau", Toast.LENGTH_SHORT).show();
                }
            }
        });
        welcomeDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    private void loadAd() {
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAdClick = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-5796098881172039/1013433344");
        mInterstitialAdClick.setAdUnitId("ca-app-pub-5796098881172039/6292065785");
        mInterstitialAdClick.loadAd(new AdRequest.Builder().build());

        if (Preference.getBoolean(this, "firstrun", true)) {
            mInterstitialAd.loadAd(new AdRequest.Builder().build());
        } else {
            long timeOld = Preference.getLong(this, "Time", 0);
            long timeNew = System.currentTimeMillis();
            if (timeOld != 0 && timeNew - timeOld >= 86400000) {
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            } else {
                if (!Preference.getBoolean(MainActivity.this, "LoadAds", false)) {
                    mInterstitialAd.loadAd(new AdRequest.Builder().build());
                }
            }
        }
        mAdView.loadAd(new AdRequest.Builder().build());
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                mAdView.setVisibility(View.VISIBLE);
                mAdView.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.slide_up));
            }

            @Override
            public void onAdOpened() {
                mAdView.setVisibility(View.GONE);
                mAdView.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.slide_down));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mAdView.loadAd(new AdRequest.Builder().build());
                    }
                }, 1800000);
            }
        });
        mInterstitialAdClick.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                isLoadClickAd = true;
            }

            @Override
            public void onAdClosed() {
                mInterstitialAdClick.loadAd(new AdRequest.Builder().build());
            }
            @Override
            public void onAdFailedToLoad(int i) {
                mInterstitialAdClick.loadAd(new AdRequest.Builder().build());
            }
        });
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                isLoadAd = true;
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                Preference.getBoolean(MainActivity.this, "LoadAds", false);
                isLoadAd = false;
            }

        });
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
                clickitem++;
                if(isLoadAd && clickitem > 3){
                    Preference.save(MainActivity.this, "firstrun", false);
                    Preference.save(MainActivity.this, "Time", System.currentTimeMillis());
                    Preference.save(MainActivity.this, "LoadAds", true);
                    mInterstitialAd.show();
                    isLoadAd = false;
                } else {
                    Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                    intent.putExtra("Url", item.getUrl());
                    intent.putExtra("Title", item.getBaseName());
                    intent.putExtra("Name", item.getName());
                    startActivity(intent);
                }
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.icon_info) {
            showDialogAds();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void showDialogAds() {
        welcomeDialog.show();
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
