package com.example.user.live500px.adaptor;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;

import com.example.user.live500px.R;
import com.example.user.live500px.dao.PhotoCollectionDao;
import com.example.user.live500px.dao.PhotoDao;
import com.example.user.live500px.dataType.MutableInteger;
import com.example.user.live500px.manager.PhotoListManager;
import com.example.user.live500px.view.PhotoViewGroup;

public class PhotoListAdaptor extends BaseAdapter {
    PhotoCollectionDao dao;
    MutableInteger lastPositionInteger;

    public PhotoListAdaptor(MutableInteger lastPositionInteger){
        this.lastPositionInteger =lastPositionInteger;
    }

    public void setDao(PhotoCollectionDao dao) {
        this.dao = dao;
    }

    @Override
    public int getCount() {/*
        if(PhotoListManager.getInstance().getDao() == null)
            return 0;
        if (PhotoListManager.getInstance().getDao().getData()==null)
            return 0;
        return PhotoListManager.getInstance().getDao().getData().size();
    */
        if (dao == null)
            return 1;
        if (dao.getData() == null)
            return 1;
        return dao.getData().size() + 1;


    }

    @Override
    public Object getItem(int position) {
        return dao.getData().get(position);
        //PhotoListManager.getInstance().getDao().getData().get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return position==getCount()-1? 1:0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (position == getCount() - 1) {
            //Progress Bar
            ProgressBar item;
            if (convertView != null) {
                item = (ProgressBar) convertView;
            }
            else{
                item =new ProgressBar(parent.getContext());
            }
            return item;
        }

        PhotoViewGroup item;
        if (convertView != null) {
            item = (PhotoViewGroup) convertView;
        } else {
            item = new PhotoViewGroup(parent.getContext());
        }
        PhotoDao dao = (PhotoDao) getItem(position);
        item.setNameText(dao.getCaption());
        item.setDescriptionText(dao.getUserName() + "\n" + dao.getCamera());
        item.setImageUrl(dao.getImageUrl());

        if (position > lastPositionInteger.getValue()) {
            Animation amin = AnimationUtils.loadAnimation(
                    parent.getContext(), R.anim.up_from_bottom);
            item.startAnimation(amin);
            lastPositionInteger.setValue(position);
        }
        return item;
    }

    public void increaseLastPosition(int amount) {
        lastPositionInteger.setValue(
                lastPositionInteger.getValue()+amount);
    }
}
