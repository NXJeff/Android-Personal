package com.woi.merlin.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.woi.merlin.R;
import com.woi.merlin.card.MedicalCard;
import com.woi.merlin.card.MedicalCardHeader;
import com.woi.merlin.enumeration.ReminderType;
import com.woi.merlin.util.GeneralUtil;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.util.ArrayList;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.base.BaseCard;
import it.gmariotti.cardslib.library.view.CardListView;
import it.gmariotti.cardslib.library.view.CardViewNative;

/**
 * Created by YeekFeiTan on 2/26/2015.
 */
public class ReminderFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reminder, container,
                false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initCards();
    }

    private void initCards() {
        ArrayList<Card> cards = new ArrayList<Card>();

        for (int i = 0; i < 50; i++) {
            MedicalCard medicalCard = getMedicalCard();
            cards.add(medicalCard);
        }

        // Provide a custom adapter.
        // It is important to set the viewTypeCount
        // You have to provide in your card the type value with {@link Card#setType(int)} method.
        CardArrayAdapter mCardArrayAdapter = new CardArrayAdapter(getActivity(), cards);
        mCardArrayAdapter.setInnerViewTypeCount(3);

        // An alternative is to write a own CardArrayAdapter
        // MyCardArrayAdapter mCardArrayAdapter = new MyCardArrayAdapter(getActivity(),cards);


        CardListView listView = (CardListView) getActivity().findViewById(R.id.reminder_list_base);
        if (listView != null) {
            listView.setAdapter(mCardArrayAdapter);
        }

    }

    private MedicalCard getMedicalCard() {
        String description = "Paracetamol (acetaminophen) is a pain reliever and a fever reducer. Note: This dose will make you sleepy or dizzy.";
        ReminderType reminderType = ReminderType.MedicalReminder;
        String lastDismissedDate = GeneralUtil.getDateInString(new LocalDate());
        String nextRemindDate = GeneralUtil.getDateInString(new LocalDate());
        String atTime = GeneralUtil.getTimeInString(new LocalTime());
        String title = "22 minutes before taking";
        String subTitle = "Paracetamol";
        int bgColor = R.color.red_500;


        //Create a Card
        MedicalCard card = new MedicalCard(getActivity(), description, reminderType, atTime);
        card.setLastDismissedDate(lastDismissedDate);
        card.setNextRemindDate(nextRemindDate);

        //Create a CardHeader
        MedicalCardHeader header = new MedicalCardHeader(getActivity(), title, subTitle);
        header.setBgColor(bgColor);
        header.setPopupMenu(R.menu.sample1menu, new CardHeader.OnClickCardHeaderPopupMenuListener() {
            @Override
            public void onMenuItemClick(BaseCard card, MenuItem item) {
                Toast.makeText(getActivity(), "Click on " + item.getTitle(), Toast.LENGTH_SHORT).show();
            }
        });
        card.addCardHeader(header);


//        card.addPartialOnClickListener(Card.CLICK_LISTENER_CONTENT_VIEW, new Card.OnCardClickListener() {
//            @Override
//            public void onClick(Card card, View view) {
////                Toast.makeText(getActivity(),"New activity", Toast.LENGTH_LONG).show();
////                testOpenActivity();
//            }
//        });

        //Set shadow elevation
        //Convert dp to float
        float shadowElevation = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 14, getResources().getDisplayMetrics());
        card.setCardElevation(shadowElevation);

        card.setSwipeable(true);

        //Set card in the CardViewNative
//        final CardViewNative cardViewNative = (CardViewNative) getActivity().findViewById(R.id.medical_card_layout);
//        cardViewNative.setCard(card);

        return card;
    }

}
