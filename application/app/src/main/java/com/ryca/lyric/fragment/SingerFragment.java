package com.ryca.lyric.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ryca.lyric.R;
import com.ryca.lyric.Utils.MyDividerItemDecoration;
import com.ryca.lyric.acitivity.MainActivity;
import com.ryca.lyric.adapter.ContactsAdapter;
import com.ryca.lyric.api.Api;
import com.ryca.lyric.api.ServiceGenerator;
import com.ryca.lyric.model.DatumSinger;
import com.ryca.lyric.model.Singer;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SingerFragment extends Fragment implements ContactsAdapter.ContactsAdapterListener {
    private static final int PAGE_START = 1;
    private View view;
    private RecyclerView recyclerView;
    private List<Singer> contactList;
    private ContactsAdapter mAdapter;
    private Activity mActivity;
    private int currentPage = PAGE_START;
    private boolean loading = true;
    private int pastVisiblesItems, visibleItemCount, totalItemCount;

    public SingerFragment() {
    }

    public SingerFragment(Activity activity) {
        this.mActivity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_singer, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        contactList = new ArrayList<>();
        LinearLayoutManager mLayoutManager =
                new LinearLayoutManager(mActivity.getBaseContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(mActivity.getBaseContext(),
                DividerItemDecoration.VERTICAL, 36));
        mAdapter = new ContactsAdapter(mActivity.getBaseContext(), contactList, this);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    visibleItemCount = mLayoutManager.getChildCount();
                    totalItemCount = mLayoutManager.getItemCount();
                    pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();

                    if (loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            ++currentPage;
                            loadNextPage();
                        }
                    }
                }
            }
        });
        loadFirstPage();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = new SearchView(((MainActivity) mActivity)
                .getSupportActionBar().getThemedContext());
        MenuItemCompat.setShowAsAction(item,
                MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW |
                        MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
        MenuItemCompat.setActionView(item, searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        searchView.setOnClickListener(v -> {
                }
        );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void loadNextPage() {
        Api service = null;
        try {
            service = ServiceGenerator.createService(Api.class);
        } catch (KeyManagementException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        service.getSinger(currentPage).enqueue(new Callback<DatumSinger>() {
            @Override
            public void onResponse(Call<DatumSinger> call,
                                   Response<DatumSinger> response) {
                DatumSinger results = response.body();
                if (results != null) {
                    mAdapter.addAll(results.getData());
                } else loading = false;
            }

            @Override
            public void onFailure(Call<DatumSinger> call, Throwable t) {
            }
        });
    }

    private void loadFirstPage() {
        Api service = null;
        try {
            service = ServiceGenerator.createService(Api.class);
        } catch (KeyManagementException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        Call<DatumSinger> call = service.getSinger(currentPage);
        call.enqueue(new Callback<DatumSinger>() {
            @Override
            public void onResponse(Call<DatumSinger> call, Response<DatumSinger> response) {

                DatumSinger results = response.body();
                if (results != null) {
                    mAdapter.addAll(results.getData());
                } else loading = false;
            }

            @Override
            public void onFailure(Call<DatumSinger> call, Throwable t) {
            }
        });
    }

    @Override
    public void onContactSelected(Singer contact) {
        if (contact.getCount()>0) {
            AlbumsFragment fragment = new AlbumsFragment(mActivity, contact.getId(), contact.getName());
            getFragmentManager().beginTransaction().
                    replace(R.id.frameLayout, fragment).
                    addToBackStack("singer").commit();
        }else
            Toasty.success(mActivity.getBaseContext(), getString(R.string.add_album), Toasty.LENGTH_LONG).show();
    }
}
