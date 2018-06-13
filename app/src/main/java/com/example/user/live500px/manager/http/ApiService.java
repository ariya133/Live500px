package com.example.user.live500px.manager.http;

import com.example.user.live500px.dao.PhotoCollectionDao;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService  {
    @POST("list")
    Call<PhotoCollectionDao> loadPhotoList();

    @POST("list/after/{id}")
    Call<PhotoCollectionDao>loadPhotoAfterId(@Path("id")int id);

    @POST("list/before/{id}")
    Call<PhotoCollectionDao>loadPhotoBeforeId(@Path("id")int id);


}
