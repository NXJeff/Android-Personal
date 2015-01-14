package com.woi.merlin.card;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.woi.merlin.R;
import com.woi.merlin.util.CircleTransform;

import it.gmariotti.cardslib.library.internal.CardHeader;

/**
 * Created by YeekFeiTan on 1/14/2015.
 */
public class Sample1CardHeader extends CardHeader {

    private String url;
    private String username;
    private String subtitle;
    private String privacy;
    private String timestamp;
    // make sure to set Target as strong reference
    private Target loadtarget;

    public Sample1CardHeader(Context context, String userThumbUrl, String username, String subtitle, String privacy, String timestamp) {
        super(context, R.layout.sample1cardinnerheaderlayout);
        this.url = userThumbUrl;
        this.username = username;
        this.subtitle = subtitle;
        this.privacy = privacy;
        this.timestamp = timestamp;
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {

        if (view != null) {
            TextView tvUsername = (TextView) view.findViewById(R.id.tv_username);
            TextView tvSubtitle = (TextView) view.findViewById(R.id.tv_subtitle);
            TextView tvPrivacy = (TextView) view.findViewById(R.id.tv_privacy);
            TextView tvSeparatorForPrivacy = (TextView) view.findViewById(R.id.tv_seperator_privacy);
            TextView tvTimestamp = (TextView) view.findViewById(R.id.tv_timestamp);


            ImageView iv1 = (ImageView) view.findViewById(R.id.iv_user_thumbnail);

            if (tvUsername != null) {
                tvUsername.setText(username);
            }

            if (tvSubtitle != null) {
                tvSubtitle.setText(subtitle);
            }

            if (tvPrivacy != null) {
                tvPrivacy.setText(privacy);
            }

            if (tvTimestamp != null) {
                tvTimestamp.setText(timestamp);
            }


            if (iv1 != null) {
                // Load image into imageView
                Picasso.with(getContext())
                        .load(url).transform(new CircleTransform())
                        .into((ImageView) iv1);
            }
        }
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPrivacy() {
        return privacy;
    }

    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

}
