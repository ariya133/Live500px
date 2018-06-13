package com.example.user.live500px.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.user.live500px.Contextor;
import com.example.user.live500px.R;
import com.example.user.live500px.activity.MoreInfoActivity;
import com.example.user.live500px.dao.PhotoCollectionDao;
import com.example.user.live500px.dao.PhotoDao;
import com.example.user.live500px.dataType.MutableInteger;
import com.example.user.live500px.manager.HttpManager;
import com.example.user.live500px.manager.PhotoListManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.example.user.live500px.adaptor.PhotoListAdaptor;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class MainFragment extends Fragment {
    //Variable

    public interface FragmentListener{
        void  onPhotoItemClicked(PhotoDao dao);
    }
    ListView listView;
    PhotoListAdaptor listAdaptor;
    SwipeRefreshLayout swipeRefreshLayout;
    PhotoListManager photoListManager;
    Button btnNewPhoto;
    MutableInteger lastPositionInteger;
    boolean isLoadingMore = false;

    //Function
    public MainFragment() {
        super();
    }

    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        initInstances(rootView);
        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        if (savedInstanceState != null)
            onRestoreInstanceState(savedInstanceState); //Restore instance state
    }

    private void init() {
        lastPositionInteger = new MutableInteger(-1);
        photoListManager = new PhotoListManager();
    }

    private void initInstances(View rootView) {
        // Init 'View' instance(s) with rootView.findViewById here
        btnNewPhoto = (Button) rootView.findViewById(R.id.btnNewPhoto);
        listView = (ListView) rootView.findViewById(R.id.listView);
        listAdaptor = new PhotoListAdaptor(lastPositionInteger);
        listAdaptor.setDao(photoListManager.getDao());
        listView.setAdapter(listAdaptor);
        btnNewPhoto.setOnClickListener(buttonClickListener);
        swipeRefreshLayout = (SwipeRefreshLayout) rootView
                .findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(
                pullToRefreshListener);
        listView.setOnScrollListener(listViewScrollListener);

        listView.setOnItemClickListener(listViewItemClickListener);
        refreshData();
    }

    private void refreshData() {
        if (photoListManager.getCount() == 0) reloadData();
        else reloadDataNewer();
    }

    private void reloadDataNewer() {
        int maxId = photoListManager.getMaximumId();
        Call<PhotoCollectionDao> call = HttpManager.
                getInstance().getService().loadPhotoAfterId(maxId);
        call.enqueue(new PhotoListLoadCallback(PhotoListLoadCallback.MODE_RELOAD_NEWER));
    }

    private void loadMoreData() {
        if (isLoadingMore)
            return;
        isLoadingMore = true;
        int minId = photoListManager.getMinimumId();
        Call<PhotoCollectionDao> call = HttpManager.
                getInstance().getService().loadPhotoAfterId(minId);
        call.enqueue(new PhotoListLoadCallback(PhotoListLoadCallback.MODE_LOAD_MORE));
    }

    private void reloadData() {
        Call<PhotoCollectionDao> call = HttpManager.getInstance().getService().loadPhotoList();
        call.enqueue(new PhotoListLoadCallback(PhotoListLoadCallback.MODE_RELOAD));
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    /*
     * Save Instance State Here
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save Instance State here
        outState.putBundle("PhotoListManager",
                photoListManager.onSaveInstanceState());
        outState.putBundle("lastPositionInteger",
                lastPositionInteger.onSaveInstanceState());
    }

    private void onRestoreInstanceState(Bundle saveInstanceState) {
        //restore instance state here
        photoListManager.onRestoreInstanceState(
                saveInstanceState.getBundle("PhotoListManager")
        );
        lastPositionInteger.onRestoreInstanceState(
                saveInstanceState.getBundle("lastPositionInteger")
        );
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void showButtonNewPhotos() {
        Animation anim = AnimationUtils.loadAnimation(
                Contextor.getInstance().getContext(),
                R.anim.zoom_fade_in
        );
        btnNewPhoto.setVisibility(View.VISIBLE);
    }

    private void hideButtonNewPhoto() {
        Animation animation = AnimationUtils.loadAnimation(
                Contextor.getInstance().getContext(),
                R.anim.zoom_fade_out
        );
        btnNewPhoto.setVisibility(View.GONE);
    }

    private void showToast(String text) {
        Toast.makeText(Contextor.getInstance().getContext(),
                text,
                Toast.LENGTH_SHORT).show();
    }

    //*************************************
    //Listener Zone
    //*************************************
    View.OnClickListener buttonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == btnNewPhoto) {
                listView.smoothScrollToPosition(0);
                hideButtonNewPhoto();
            }
        }
    };
    AbsListView.OnScrollListener listViewScrollListener =
            new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                }

                @Override
                public void onScroll(AbsListView view,
                                     int firstVisibleItem,
                                     int visibleItemCount,
                                     int totalItemCount) {
                    if (view == listView) {
                        swipeRefreshLayout.setEnabled(firstVisibleItem == 0);
                        if (firstVisibleItem + visibleItemCount >= totalItemCount) {
                            if (photoListManager.getCount() > 0) {
                                // load more
                                loadMoreData();
                            }
                        }
                    }
                }
            };
    SwipeRefreshLayout.OnRefreshListener pullToRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            refreshData();
        }
    };
    AdapterView.OnItemClickListener listViewItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            if(position< photoListManager.getCount()){
                PhotoDao dao = photoListManager.getDao().getData().get(position);
                FragmentListener listener =(FragmentListener) getActivity();
                listener.onPhotoItemClicked(dao);
            }
        }
    };

    // ***********************************
    //InnerClass
    // ***********************************
    class PhotoListLoadCallback implements Callback<PhotoCollectionDao> {
        public static final int MODE_RELOAD = 1;
        public static final int MODE_RELOAD_NEWER = 2;
        public static final int MODE_LOAD_MORE = 3;
        int mode;

        public PhotoListLoadCallback(int mode) {
            this.mode = mode;
        }

        @Override
        public void onResponse(Call<PhotoCollectionDao> call, Response<PhotoCollectionDao> response) {
            swipeRefreshLayout.setRefreshing(false);
            if (response.isSuccessful()) {
                PhotoCollectionDao dao = response.body();
                //PhotoListManager.getInstance().setDao(dao);
                int firstVisitPosition = listView.getFirstVisiblePosition();
                View c = listView.getChildAt(0);
                int top = c == null ? 0 : c.getTop();

                if (mode == MODE_RELOAD_NEWER)
                    photoListManager.insertDaoATopitem(dao);
                else if (mode == MODE_LOAD_MORE) {
                    photoListManager.appendAtBottomDaoATopitem(dao);

                } else {
                    photoListManager.setDao(dao);

                }
                clearLoadingMoreFlagCapable(mode);
                listAdaptor.setDao(photoListManager.getDao());
                listAdaptor.notifyDataSetChanged();
                if (mode == MODE_RELOAD_NEWER) {
                    //maintain
                    int additionalSize =
                            (dao != null && dao.getData() != null) ? dao.getData().size() : 0;
                    listAdaptor.increaseLastPosition(additionalSize);
                    listView.setSelectionFromTop(
                            firstVisitPosition + additionalSize,
                            top);
                    if (additionalSize > 0)
                        showButtonNewPhotos();
                } else {

                }
                showToast("Load Completed");
            } else {
                //handle
                clearLoadingMoreFlagCapable(mode);
                try {
                    showToast(response.errorBody().string());

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onFailure(Call<PhotoCollectionDao> call, Throwable t) {
            swipeRefreshLayout.setRefreshing(false);
            //TODO loading more
            clearLoadingMoreFlagCapable(mode);
            showToast(t.toString());
        }

        private void clearLoadingMoreFlagCapable(int mode) {
            if (mode == MODE_LOAD_MORE)
                isLoadingMore = false;
        }
    }
}