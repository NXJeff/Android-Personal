package com.woi.merlin.fragment;

import android.app.Fragment;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.woi.merlin.R;

import it.gmariotti.cardslib.library.Constants;
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
        Resources r = getResources();
        //Convert dp to float
        float shadowElevation = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 14, r.getDisplayMetrics());
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
                switch (motionEvent.getAction()){
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


}
