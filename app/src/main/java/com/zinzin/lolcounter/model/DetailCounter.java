package com.zinzin.lolcounter.model;

import java.util.ArrayList;
import java.util.List;

public class DetailCounter {
    String url_header;
    String url_ava;
    String name;
    List<String> list_info = new ArrayList<>();
    List<String> list_lane = new ArrayList<>();
    List<String> list_item = new ArrayList<>();
    List<String> list_buff = new ArrayList<>();

    List<ItemHero> itemHeroesCounter = new ArrayList<>();
    List<ItemHero> itemHeroesNerf = new ArrayList<>();

    public DetailCounter() {
    }

    public String getUrl_header() {
        return url_header;
    }

    public void setUrl_header(String url_header) {
        this.url_header = url_header;
    }

    public String getUrl_ava() {
        return url_ava;
    }

    public void setUrl_ava(String url_ava) {
        this.url_ava = url_ava;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getList_info() {
        return list_info;
    }

    public void setList_info(List<String> list_info) {
        this.list_info = list_info;
    }

    public List<String> getList_lane() {
        return list_lane;
    }

    public void setList_lane(List<String> list_lane) {
        this.list_lane = list_lane;
    }

    public List<String> getList_item() {
        return list_item;
    }

    public void setList_item(List<String> list_item) {
        this.list_item = list_item;
    }

    public List<String> getList_buff() {
        return list_buff;
    }

    public void setList_buff(List<String> list_buff) {
        this.list_buff = list_buff;
    }

    public List<ItemHero> getItemHeroesCounter() {
        return itemHeroesCounter;
    }

    public void setItemHeroesCounter(List<ItemHero> itemHeroesCounter) {
        this.itemHeroesCounter = itemHeroesCounter;
    }

    public List<ItemHero> getItemHeroesNerf() {
        return itemHeroesNerf;
    }

    public void setItemHeroesNerf(List<ItemHero> itemHeroesNerf) {
        this.itemHeroesNerf = itemHeroesNerf;
    }
}
