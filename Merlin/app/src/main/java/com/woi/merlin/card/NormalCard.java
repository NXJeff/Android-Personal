package com.woi.merlin.card;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.IconTextView;
import android.widget.TextView;

import com.woi.merlin.R;
import com.woi.merlin.util.ReminderUtil;

import it.gmariotti.cardslib.library.internal.Card;
import merlin.model.raw.Reminder;

/**
 * Created by YeekFeiTan on 3/23/2015.
 */
public class NormalCard extends Card {

    Reminder reminder;

    public NormalCard(Context context, Reminder reminder) {
        super(context, R.layout.norm_reminder_content);
        this.reminder = reminder;
    }


    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {

        if (view != null) {
            TextView tvTitle = (TextView) parent.findViewById(R.id.normal_title);
            TextView tvUpcoming = (TextView) parent.findViewById(R.id.normal_upcoming_date);
            IconTextView ictIcon  = (IconTextView) parent.findViewById(R.id.norm_reminder_icon);

            if (tvTitle != null)
                tvTitle.setText(reminder.getSubject());

            if (tvUpcoming != null) {
                tvUpcoming.setText(ReminderUtil.getReadableRemainingDate(reminder));
            }

            if(ictIcon != null) {
                ictIcon.setTextColor(reminder.getColor());
            }
        }
    }

    public Reminder getReminder() {
        return reminder;
    }

    public void setReminder(Reminder reminder) {
        this.reminder = reminder;
    }
}
