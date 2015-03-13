package com.woi.merlin.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.nfc.tech.NfcBarcode;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.woi.merlin.R;
import com.woi.merlin.activity.AddNewMeal;
import com.woi.merlin.activity.AddNewReminder;
import com.woi.merlin.card.ColorCard;

import java.util.ArrayList;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.view.CardListView;

/**
 * Created by YeekFeiTan on 2/27/2015.
 */
public class MealFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meal, container,
                false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }


}
