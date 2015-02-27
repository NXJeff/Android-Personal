package com.woi.merlin.card;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.woi.merlin.R;

import it.gmariotti.cardslib.library.internal.Card;

/**
 * Created by YeekFeiTan on 2/27/2015.
 */
public class ColorCard extends Card {

    protected String mTitle;
    protected int count;

    public ColorCard(Context context) {
        this(context, R.layout.color_card_inner_layout);
    }

    public ColorCard(Context context, int innerLayout) {
        super(context, innerLayout);
        init();
    }

    private void init() {
        //Add ClickListener
//        setOnClickListener(new OnCardClickListener() {
//            @Override
//            public void onClick(Card card, View view) {
//                Toast.makeText(getContext(), "Click Listener card="+count, Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {

        //Retrieve elements
        TextView title = (TextView) parent.findViewById(R.id.carddemo_card_color_inner_simple_title);

        if (title != null)
            title.setText(mTitle);

    }


    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

}