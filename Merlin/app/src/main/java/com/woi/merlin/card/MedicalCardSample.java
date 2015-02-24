package com.woi.merlin.card;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.woi.merlin.R;
import com.woi.merlin.enumeration.ReminderType;

import it.gmariotti.cardslib.library.internal.Card;

/**
 * Created by YeekFeiTan on 2/13/2015.
 */
public class MedicalCardSample extends Card {

    String description;
    ReminderType reminderType = ReminderType.MedicalReminder;
    String lastDismissedDate;
    String nextRemindDate;
    String atTime;


    public MedicalCardSample(Context context, int layout, String description) {
        super(context, layout);
        this.description = description;
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
            tvContent.setText(description);

    }
}
