package com.woi.merlin.card;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.IconTextView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gc.materialdesign.views.ButtonFlat;
import com.joanzapata.android.iconify.IconDrawable;
import com.woi.merlin.R;
import com.woi.merlin.activity.AddNewReminder;
import com.woi.merlin.enumeration.ReminderType;
import com.woi.merlin.enumeration.RepeatType;
import com.woi.merlin.fragment.ReminderFragment;
import com.woi.merlin.util.GeneralUtil;
import com.woi.merlin.util.ReminderUtil;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import it.gmariotti.cardslib.library.internal.CardExpand;
import merlin.model.raw.Reminder;

/**
 * Created by YeekFeiTan on 3/24/2015.
 */
public class ReminderCardExpand extends CardExpand {

    Reminder reminder = null;
    Fragment fragment = null;


    public ReminderCardExpand(Context context, Fragment fragment, Reminder reminder) {
        super(context, R.layout.reminder_card_expand_layout);
        this.reminder = reminder;
        this.fragment = fragment;
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {

        LinearLayout left = (LinearLayout) parent.findViewById(R.id.reminder_expand_linear_layout_left);
        LinearLayout right = (LinearLayout) parent.findViewById(R.id.reminder_expand_linear_layout_right);
        ButtonFlat editBtn = (ButtonFlat) parent.findViewById(R.id.expand_edit_btn);
        ButtonFlat deleteBtn = (ButtonFlat) parent.findViewById(R.id.expand_delete_btn);
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(fragment.getActivity(), AddNewReminder.class);
                intent.putExtra("id", reminder.getId());
//                if (android.os.Build.VERSION.SDK_INT >= 21) {
//                    IconTextView ictIcon = (IconTextView) view.findViewById(R.id.norm_reminder_icon);
//                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity(), ictIcon, "ictIcon");
//                    startActivityForResult(intent, UPDATE_REMINDER, options.toBundle());
//                } else {
                fragment.startActivityForResult(intent, ReminderFragment.UPDATE_REMINDER);
//            }
            }
        });

        left.removeAllViews();
        right.removeAllViews();


        //Left
        //Note
        if (reminder.getDescription() != null && !reminder.getDescription().isEmpty()) {
            addRow(left, "{fa-align-left}", reminder.getDescription());
        }
        //Reminder Type
        addRow(left, "{fa-bell}", ReminderType.of(reminder.getReminderType()).toString());
        //Repeat Type
        addRow(left, "{fa-repeat}", RepeatType.of(reminder.getRepeatType()).toString());
        //


        //Right side
        //Next Reminder Time
        LocalDate date = new LocalDate(ReminderUtil.getNextReminderTime(reminder));
        LocalTime time = new LocalTime(reminder.getAtTime());
        String nextReminderOccurDate = GeneralUtil.getDateInStringFormatA(date);
        String nextReminderOccurTime = GeneralUtil.getTimeInString(time);

        addRow(right, "{fa-chevron-right}", nextReminderOccurDate + " " + nextReminderOccurTime);

        //Last Reminder Time
        addRow(right, "{fa-chevron-left}", "N/A");
    }

    //Dynamically add view into the linear layout
    public void addRow(LinearLayout layout, String iconId, String text) {
        LinearLayout ll = new LinearLayout(getContext());
        LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        linearLayoutParams.setMargins(0, 10, 0, 10);
        ll.setLayoutParams(linearLayoutParams);
        ll.setOrientation(LinearLayout.HORIZONTAL);

        IconTextView icon = new IconTextView(getContext());
        icon.setText(iconId);

        TextView tv = new TextView(getContext());
        tv.setText(text);
        LinearLayout.LayoutParams textViewParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        textViewParams.setMargins(20, 0, 0, 0);
        tv.setLayoutParams(textViewParams);
        tv.setMaxLines(3);

        ll.addView(icon);
        ll.addView(tv);
        layout.addView(ll);
    }
}
