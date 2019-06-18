package com.zinzin.lolcounter.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class ItemHero implements Parcelable {
    String name;
    String baseName;
    String url;
    String url_image;
    List<String> item = new ArrayList<>();

    public ItemHero(String name, String baseName, String url, String url_image) {
        this.name = name;
        this.baseName = baseName;
        this.url = url;
        this.url_image = url_image;
    }

    public ItemHero(Parcel in) {
        name = in.readString();
        baseName = in.readString();
        url = in.readString();
        url_image = in.readString();
        in.readStringList(item);
    }

    public ItemHero() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl_image() {
        return url_image;
    }

    public void setUrl_image(String url_image) {
        this.url_image = url_image;
    }

    public String getBaseName() {
        return baseName;
    }

    public void setBaseName(String baseName) {
        this.baseName = baseName;
    }

    public List<String> getItem() {
        return item;
    }

    public void setItem(List<String> item) {
        this.item = item;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(baseName);
        dest.writeString(url);
        dest.writeString(url_image);
        dest.writeStringList(item);
    }

    public static final Parcelable.Creator<ItemHero> CREATOR = new Parcelable.Creator<ItemHero>() {
        public ItemHero createFromParcel(Parcel in) {
            return new ItemHero(in);
        }

        public ItemHero[] newArray(int size) {
            return new ItemHero[size];
        }
    };
}
