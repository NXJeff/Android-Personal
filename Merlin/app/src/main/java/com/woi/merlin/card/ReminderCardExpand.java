package com.woi.merlin.card;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.IconTextView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.joanzapata.android.iconify.IconDrawable;
import com.woi.merlin.R;

import it.gmariotti.cardslib.library.internal.CardExpand;
import merlin.model.raw.Reminder;

/**
 * Created by YeekFeiTan on 3/24/2015.
 */
public class ReminderCardExpand extends CardExpand {

    Reminder reminder = null;


    public ReminderCardExpand(Context context, Reminder reminder) {
        super(context, R.layout.reminder_card_expand_layout);
        this.reminder = reminder;
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {

        LinearLayout left = (LinearLayout) parent.findViewById(R.id.reminder_expand_linear_layout_left);
        LinearLayout right = (LinearLayout) parent.findViewById(R.id.reminder_expand_linear_layout_right);
        left.removeAllViews();
        right.removeAllViews();

    }

    public void addRow(LinearLayout layout, String iconId, String text) {
        LinearLayout ll = new LinearLayout(getContext());
        ll.setOrientation(LinearLayout.HORIZONTAL);

        IconTextView icon = new IconTextView(getContext());
        icon.setText(iconId);

        TextView tv = new TextView(getContext());
        tv.setText(text);

        ll.addView(icon);
        ll.addView(tv);
        layout.addView(ll);
    }

}
