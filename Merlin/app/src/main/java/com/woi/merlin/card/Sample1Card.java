package com.woi.merlin.card;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.woi.merlin.R;

import it.gmariotti.cardslib.library.internal.Card;

/**
 * Created by YeekFeiTan on 1/14/2015.
 */
public class Sample1Card extends Card {

    String contentText;

    public Sample1Card(Context context, int layout, String contentText) {
        super(context, layout);
        this.contentText = contentText;
//        init();
    }

    /**
     * Init
     */
    private void init() {

        //No Header

        //Set a OnClickListener listener
//        setOnClickListener(new OnCardClickListener() {
//            @Override
//            public void onClick(Card card, View view) {
//                Toast.makeText(getContext(), "Click Listener card=", Toast.LENGTH_LONG).show();
//            }
//        });
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {

        TextView tvContent = (TextView) parent.findViewById(R.id.tv_content);

        if (tvContent != null)
            tvContent.setText(contentText);

    }
}