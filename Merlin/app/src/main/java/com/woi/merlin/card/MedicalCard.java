package com.woi.merlin.card;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.woi.merlin.R;
import com.woi.merlin.enumeration.ReminderType;

import it.gmariotti.cardslib.library.internal.Card;

/**
 * Created by YeekFeiTan on 2/13/2015.
 */
public class MedicalCard extends Card {

    String description;
    ReminderType reminderType = ReminderType.MedicalReminder;
    String lastDismissedDate;
    String nextRemindDate;
    String atTime;
    Long reminderId;


    public MedicalCard(Context context, Long reminderId, String description, ReminderType reminderType, String atTime) {
        super(context, R.layout.medical_card_content);
        this.description = description;
        this.reminderType = reminderType;
        this.atTime = atTime;
        this.reminderId  = reminderId;
        init();
    }

    /**
     * Init
     */
    private void init() {
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {

        if (view != null) {
//            TextView tvContent = (TextView) parent.findViewById(R.id.tv_content);
            TextView tvRemark = (TextView) parent.findViewById(R.id.medical_remark);
            TextView tvCategory = (TextView) parent.findViewById(R.id.medical_category);
            TextView tvLastDissed = (TextView) parent.findViewById(R.id.medical_last_dismissed_time);
            TextView tvNextRemind = (TextView) parent.findViewById(R.id.medical_next_remind_time);


            if (tvRemark != null)
                tvRemark.setText(description);

            if (tvCategory != null)
                tvCategory.setText(reminderType.toString());

            if (tvLastDissed != null)
                tvLastDissed.setText(lastDismissedDate);

            if (tvNextRemind != null)
                tvNextRemind.setText(nextRemindDate);
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ReminderType getReminderType() {
        return reminderType;
    }

    public void setReminderType(ReminderType reminderType) {
        this.reminderType = reminderType;
    }

    public String getLastDismissedDate() {
        return lastDismissedDate;
    }

    public void setLastDismissedDate(String lastDismissedDate) {
        this.lastDismissedDate = lastDismissedDate;
    }

    public String getNextRemindDate() {
        return nextRemindDate;
    }

    public void setNextRemindDate(String nextRemindDate) {
        this.nextRemindDate = nextRemindDate;
    }

    public String getAtTime() {
        return atTime;
    }

    public void setAtTime(String atTime) {
        this.atTime = atTime;
    }

    public Long getReminderId() {
        return reminderId;
    }

    public void setReminderId(Long reminderId) {
        this.reminderId = reminderId;
    }
}
