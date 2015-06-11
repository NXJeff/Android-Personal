package com.woi.merlin.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.melnykov.fab.FloatingActionButton;
import com.woi.merlin.R;
import com.woi.merlin.activity.AddNewReminder;

import it.gmariotti.cardslib.library.view.listener.SwipeOnScrollListener;

/**
 * Created by YeekFeiTan on 6/11/2015.
 */
public class BillMaintenanceFragment extends Fragment {

    //keep track of intents
    public static final int ADD_NEW_ITEM = 1;
    public static final int UPDATE_ITEM = 2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bill_maintenance, container,
                false);

        initFAB(view);

        return view;
    }

    /** Initialise Floating Action Button **/
    private void initFAB(View view) {
        ListView listView = (ListView) view.findViewById(R.id.bill_main_list);
        listView.setOnScrollListener(
                new SwipeOnScrollListener() {
                    @Override
                    public void onScrollStateChanged(AbsListView view, int scrollState) {
                        //It is very important to call the super method here to preserve built-in functions
                        super.onScrollStateChanged(view, scrollState);
                    }

                    @Override
                    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                        //Do something...
                    }
                }
        );
        final FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab_bill_maintenance_fm);
        fab.attachToListView(listView);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddNewReminder.class);
                startActivityForResult(intent, ADD_NEW_ITEM);
            }
        });
    }

}
