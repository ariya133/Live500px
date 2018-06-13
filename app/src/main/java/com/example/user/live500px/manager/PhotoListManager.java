package com.example.user.live500px.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.user.live500px.Contextor;
import com.example.user.live500px.dao.PhotoCollectionDao;
import com.example.user.live500px.dao.PhotoDao;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;


public class PhotoListManager {

    private Context mContext;
    private PhotoCollectionDao dao;

    public PhotoCollectionDao getDao() {
        return dao;
    }

    public void setDao(PhotoCollectionDao dao) {
        this.dao = dao;
        saveCache();
    }

    public void insertDaoATopitem(PhotoCollectionDao newDao) {
        if (dao == null)
            dao = new PhotoCollectionDao();
        if (dao.getData() == null)
            dao.setData(new ArrayList<PhotoDao>());
        dao.getData().addAll(0, newDao.getData());
        saveCache();

    }

    public void appendAtBottomDaoATopitem(PhotoCollectionDao newDao) {
        if (dao == null)
            dao = new PhotoCollectionDao();
        if (dao.getData() == null)
            dao.setData(new ArrayList<PhotoDao>());
        dao.getData().addAll(dao.getData().size(), newDao.getData());
        saveCache();

    }

    public PhotoListManager() {
        mContext = Contextor.getInstance().getContext();
        loadCache();
    }

    public int getMinimumId() {
        if (dao == null) {
            return 0;
        }
        if (dao.getData() == null) {
            return 0;
        }
        if (dao.getData().size() == 0) {
            return 0;
        }
        int minId = dao.getData().get(0).getId();
        for (int i = 1; i < dao.getData().size(); i++) {
            minId = Math.min(minId, dao.getData().get(i).getId());
        }
        return minId;
    }

    public int getMaximumId() {
        if (dao == null) {
            return 0;
        }
        if (dao.getData() == null) {
            return 0;
        }
        if (dao.getData().size() == 0) {
            return 0;
        }
        int maxId = dao.getData().get(0).getId();
        for (int i = 1; i < dao.getData().size(); i++) {
            maxId = Math.max(maxId, dao.getData().get(i).getId());
        }
        return maxId;
    }

    public int getCount() {
        if (dao == null) return 0;
        if (dao.getData() == null) return 0;
        return dao.getData().size();
    }

    public Bundle onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("dao", dao);
        return bundle;

    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        dao = savedInstanceState.getParcelable("dao");
    }

    private void saveCache() {

        PhotoCollectionDao cacheDao = new PhotoCollectionDao();
        if (dao != null && dao.getData() != null){


            cacheDao.setData( new ArrayList<PhotoDao>(dao.getData().subList(0,
                    Math.min(20,dao.getData().size())))
                    );
        }
        String json =new Gson().toJson(cacheDao);

        SharedPreferences prefs = mContext.getSharedPreferences(
                "photos",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.apply();

    }

    private void loadCache() {
        SharedPreferences prefs = mContext.getSharedPreferences(
                "photos",
                Context.MODE_PRIVATE);
        String json =prefs.getString("json",null);
        if (json==null){
            return;
        }
        dao = new Gson().fromJson(json,PhotoCollectionDao.class);

    }
}
