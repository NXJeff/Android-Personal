package com.woi.merlin.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.Toast;

import com.woi.merlin.R;
import com.woi.merlin.activity.AddNewReminder;
import com.woi.merlin.card.Sample1Card;
import com.woi.merlin.card.Sample1CardHeader;

import java.util.ArrayList;

import it.gmariotti.cardslib.library.Constants;
import it.gmariotti.cardslib.library.cards.actions.BaseSupplementalAction;
import it.gmariotti.cardslib.library.cards.actions.IconSupplementalAction;
import it.gmariotti.cardslib.library.cards.actions.TextSupplementalAction;
import it.gmariotti.cardslib.library.cards.material.MaterialLargeImageCard;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.CardThumbnail;
import it.gmariotti.cardslib.library.internal.base.BaseCard;
import it.gmariotti.cardslib.library.view.CardViewNative;

/**
 * Created by YeekFeiTan on 1/7/2015.
 */
public class FragmentCardSample1 extends Fragment {
    public static final int SIMULATED_REFRESH_LENGTH = 3000;
    protected ScrollView mScrollView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sample_1, container,
                false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mScrollView = (ScrollView) getActivity().findViewById(R.id.card_scrollview);
        initCards();
    }


    private void initCards() {
        init_standard_header_with_overflow_button();
        init_custom_header_inner_layout();
        init_material_largeimage_text();
        init_material_largeimage_icon();
    }

    private void init_standard_header_with_overflow_button() {
        //Create a Card
        Card card = new Card(getActivity());

        //Create a CardHeader
        CardHeader header = new CardHeader(getActivity());

        //Set the header title
        header.setTitle("Card Design");

        //Add a popup menu. This method set OverFlow button to visible
        header.setPopupMenu(R.menu.sample1menu, new CardHeader.OnClickCardHeaderPopupMenuListener() {
            @Override
            public void onMenuItemClick(BaseCard card, MenuItem item) {
                Toast.makeText(getActivity(), "Click on " + item.getTitle(), Toast.LENGTH_SHORT).show();
            }
        });
        card.addCardHeader(header);

        //Set shadow elevation
        //Convert dp to float
        float shadowElevation = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 14, getResources().getDisplayMetrics());
        card.setCardElevation(shadowElevation);

        //Create thumbnail
        CardThumbnail thumb = new CardThumbnail(getActivity());

        //Set URL resource
        thumb.setUrlResource("https://lh5.googleusercontent.com/-N8bz9q4Kz0I/AAAAAAAAAAI/AAAAAAAAAAs/Icl2bQMyK7c/s265-c-k-no/photo.jpg");

        //Error Resource ID
        thumb.setErrorResource(R.drawable.ic_error_loadingorangesmall);

        //Add thumbnail to a card
        card.addCardThumbnail(thumb);

        //Set card in the CardViewNative
        final CardViewNative cardViewNative = (CardViewNative) getActivity().findViewById(R.id.cardView1);
        cardViewNative.setCard(card);

        cardViewNative.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (Build.VERSION.SDK_INT >= Constants.API_L) {
                            cardViewNative.animate().setDuration(100).scaleX(1.1f).scaleY(1.1f).translationZ(10);
                        } else {
                            cardViewNative.animate().setDuration(100).scaleX(1.1f).scaleY(1.1f);
                        }
                        return true;
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:
                        if (Build.VERSION.SDK_INT >= Constants.API_L) {
                            cardViewNative.animate().setDuration(100).scaleX(1).scaleY(1).translationZ(0);
                        } else {
                            cardViewNative.animate().setDuration(100).scaleX(1).scaleY(1);
                        }
                        return true;
                }
                return false;
            }
        });
    }

    public void init_custom_header_inner_layout() {


        String username_thumbnail_url = "https://lh5.googleusercontent.com/-N8bz9q4Kz0I/AAAAAAAAAAI/AAAAAAAAAAs/Icl2bQMyK7c/s265-c-k-no/photo.jpg";
        String username = "Jason Yap";
        String subtitle = "has commented on the video.";
        String privacy = "Custom";
        String timestamp = "35 minutes ago";
        String contentText = "Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Vestibulum interdum elit lectus, id pharetra mi facilisis a. Suspendisse et nisi et dolor pulvinar feugiat. Vivamus elementum sodales lacinia. In hac habitasse platea dictumst. Pellentesque mollis tortor at augue cursus, et vehicula dui pellentesque. Aenean at tempus nibh. Nulla sollicitudin lorem non nunc tempor tincidunt.";

        //Create a Card
        Sample1Card card = new Sample1Card(getActivity(), R.layout.sample1cardlayout, contentText);
        //Create a CardHeader
        Sample1CardHeader header = new Sample1CardHeader(getActivity(), username_thumbnail_url, username, subtitle, privacy, timestamp);
        //Add Header to card
        card.addCardHeader(header);

        card.addPartialOnClickListener(Card.CLICK_LISTENER_CONTENT_VIEW, new Card.OnCardClickListener() {
            @Override
            public void onClick(Card card, View view) {
//                Toast.makeText(getActivity(),"New activity", Toast.LENGTH_LONG).show();
                testOpenActivity();
            }
        });

        //Set shadow elevation
        //Convert dp to float
        float shadowElevation = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 14, getResources().getDisplayMetrics());
        card.setCardElevation(shadowElevation);

        //Set card in the CardViewNative
        CardViewNative cardViewNative = (CardViewNative) getActivity().findViewById(R.id.sample1_carddemo_header_layout);
        cardViewNative.setCard(card);
    }

    /**
     * Builds a Material Card with Large Image and Text
     */
    private void init_material_largeimage_text() {

        ArrayList<BaseSupplementalAction> actions = new ArrayList<BaseSupplementalAction>();

        // Set supplemental actions
        TextSupplementalAction t1 = new TextSupplementalAction(getActivity(), R.id.text1);
        t1.setOnActionClickListener(new BaseSupplementalAction.OnActionClickListener() {
            @Override
            public void onClick(Card card, View view) {
                Toast.makeText(getActivity(), " Click on Text SHARE ", Toast.LENGTH_SHORT).show();
            }
        });
        actions.add(t1);

        TextSupplementalAction t2 = new TextSupplementalAction(getActivity(), R.id.text2);
        t2.setOnActionClickListener(new BaseSupplementalAction.OnActionClickListener() {
            @Override
            public void onClick(Card card, View view) {
                Toast.makeText(getActivity(), " Click on Text LEARN ", Toast.LENGTH_SHORT).show();
            }
        });
        actions.add(t2);

        //Create a Card, set the title over the image and set the thumbnail
        MaterialLargeImageCard card =
                MaterialLargeImageCard.with(getActivity())
                        .setTextOverImage("Italian Beaches")
                        .setTitle("This is my favorite local beach")
                        .setSubTitle("A wonderful place")
                        .useDrawableId(R.drawable.card_bg_1)
                        .setupSupplementalActions(R.layout.sample_material_card_supl_layout_text, actions)
                        .build();

        card.setOnClickListener(new Card.OnCardClickListener() {
            @Override
            public void onClick(Card card, View view) {
                Toast.makeText(getActivity(), " Click on ActionArea ", Toast.LENGTH_SHORT).show();
            }
        });

        //Set card in the CardViewNative
        CardViewNative cardView = (CardViewNative) getActivity().findViewById(R.id.carddemo_largeimage_text);
        cardView.setCard(card);
    }

    /**
     * Builds a Material Card with Large and small icons as supplemental actions
     */
    private void init_material_largeimage_icon() {

        // Set supplemental actions as icon
        ArrayList<BaseSupplementalAction> actions = new ArrayList<BaseSupplementalAction>();
        IconSupplementalAction t1 = new IconSupplementalAction(getActivity(), R.id.ic1);
        t1.setOnActionClickListener(new BaseSupplementalAction.OnActionClickListener() {
            @Override
            public void onClick(Card card, View view) {
                Toast.makeText(getActivity(), " Click on Text SHARE ", Toast.LENGTH_SHORT).show();
            }
        });
        actions.add(t1);

        IconSupplementalAction t2 = new IconSupplementalAction(getActivity(), R.id.ic2);
        t2.setOnActionClickListener(new BaseSupplementalAction.OnActionClickListener() {
            @Override
            public void onClick(Card card, View view) {
                Toast.makeText(getActivity(), " Click on Text LEARN ", Toast.LENGTH_SHORT).show();
            }
        });
        actions.add(t2);

        MaterialLargeImageCard card =
                MaterialLargeImageCard.with(getActivity())
                        .setTextOverImage("Italian Beaches")
                        .useDrawableId(R.drawable.card_bg_1)
                        .setupSupplementalActions(R.layout.sample_material_card_supl_icon_layout, actions)
                        .build();

        card.setOnClickListener(new Card.OnCardClickListener() {
            @Override
            public void onClick(Card card, View view) {
//                Toast.makeText(getActivity(), " Click on ActionArea ", Toast.LENGTH_SHORT).show();
            }
        });

        //Set card in the CardViewNative
        CardViewNative cardView = (CardViewNative) getActivity().findViewById(R.id.carddemo_largeimage);
        cardView.setCard(card);
    }

    public void testOpenActivity() {
        Intent intent = new Intent(getActivity(), AddNewReminder.class);
//        intent.putExtra(EXTRA_MESSAGE, message);

        startActivity(intent);
    }


}
