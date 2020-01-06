package com.lauzy.freedom.lyricview.fragment;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
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

import com.lauzy.freedom.lyricview.acitivity.MainActivity;
import com.lauzy.freedom.lyricview.adapter.ContactsAdapter;
import com.lauzy.freedom.lyricview.Utils.MyDividerItemDecoration;
import com.lauzy.freedom.lyricview.R;
import com.lauzy.freedom.lyricview.model.Contact;

import java.util.ArrayList;
import java.util.List;

public class SingerFragment extends Fragment implements ContactsAdapter.ContactsAdapterListener {
    private View view;
    private RecyclerView recyclerView;
    private List<Contact> contactList;
    private ContactsAdapter mAdapter;
    private Activity mActivity;
    private SearchView searchView;

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
        for (int i = 1; i < 100; ++i) {
            Contact c = new Contact();
            c.setId(i);
            c.setName("نام:‌ " + i);
            c.setPhone("+98-" + (135484 * i));
            c.setImage("https://persianblog.ir/upload/blog/5bdaf0de1579b-7490974.jpg");
            contactList.add(c);
        }
        mAdapter = new ContactsAdapter(mActivity.getBaseContext(), contactList, this);
//
//        // white background notification bar
//        whiteNotificationBar(recyclerView);
//
        RecyclerView.LayoutManager mLayoutManager =
                new LinearLayoutManager(mActivity.getBaseContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(mActivity.getBaseContext(),
                DividerItemDecoration.VERTICAL, 36));
        recyclerView.setAdapter(mAdapter);

        return view;
    }

    private void whiteNotificationBar(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int flags = view.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
            mActivity.getWindow().setStatusBarColor(Color.WHITE);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = new SearchView(((MainActivity) mActivity).getSupportActionBar().getThemedContext());
        MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
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

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//
//
//
////       inflater.inflate(R.menu.menu_main, menu);
////
////        // Associate searchable configuration with the SearchView
////        SearchManager searchManager = (SearchManager) mActivity.
////                getBaseContext().
////                getSystemService(Context.SEARCH_SERVICE);
////        searchView = (SearchView) menu.findItem(R.id.action_search)
////                .getActionView();
////        searchView.setSearchableInfo(searchManager
////                .getSearchableInfo(mActivity.getComponentName()));
////        searchView.setMaxWidth(Integer.MAX_VALUE);
////
////        // listening to search query text change
////        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
////            @Override
////            public boolean onQueryTextSubmit(String query) {
////                // filter recycler view when query submitted
////                mAdapter.getFilter().filter(query);
////                return false;
////            }
////
////            @Override
////            public boolean onQueryTextChange(String query) {
////                // filter recycler view when text is changed
////                mAdapter.getFilter().filter(query);
////                return false;
////            }
////        });
//    }
//

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onContactSelected(Contact contact) {
        AlbumsFragment fragment = new AlbumsFragment(mActivity, contact.getId());

        getFragmentManager().beginTransaction().
                replace(R.id.frameLayout, fragment).
                addToBackStack("singer").commit();
    }
}
