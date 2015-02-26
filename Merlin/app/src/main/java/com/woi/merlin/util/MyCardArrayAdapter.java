package com.woi.merlin.util;

import android.content.Context;

import java.util.List;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;

/**
 * Created by YeekFeiTan on 2/26/2015.
 */
public class MyCardArrayAdapter extends CardArrayAdapter {

    /**
     * Constructor
     *
     * @param context The current context.
     * @param cards   The cards to represent in the ListView.
     */
    public MyCardArrayAdapter(Context context, List<Card> cards) {
        super(context, cards);
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }
}