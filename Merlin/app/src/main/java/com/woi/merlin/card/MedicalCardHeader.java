package com.woi.merlin.card;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.woi.merlin.R;

import it.gmariotti.cardslib.library.internal.CardHeader;

/**
 * Created by YeekFeiTan on 2/13/2015.
 */
public class MedicalCardHeader extends CardHeader {

    private String title;
    private String subTitle;
    private int bgColor;

    public MedicalCardHeader(Context context, String title, String subTitle) {
        super(context, R.layout.medical_card_header);
        this.title = title;
        this.subTitle = subTitle;
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {

        if (view != null) {
            TextView tvTitle = (TextView) view.findViewById(R.id.card_main_inner_topcolored_title);
            TextView tvSubTitle = (TextView) view.findViewById(R.id.card_main_inner_topcolored_subtitle);
            LinearLayout headerLayout = (LinearLayout) view.findViewById(R.id.card_main_inner_topcolored_layout);

            if(tvTitle!=null) {
                tvTitle.setText(title);
            }

            if(tvSubTitle!=null) {
                tvSubTitle.setText(subTitle);
            }

            if(headerLayout!=null) {
                headerLayout.setBackgroundResource(bgColor);
            }
        }
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public int getBgColor() {
        return bgColor;
    }

    public void setBgColor(int bgColor) {
        this.bgColor = bgColor;
    }
}
