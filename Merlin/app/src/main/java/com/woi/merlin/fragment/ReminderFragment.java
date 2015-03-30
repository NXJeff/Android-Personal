package com.woi.merlin.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;
import com.nhaarman.listviewanimations.appearance.AnimationAdapter;
import com.nhaarman.listviewanimations.appearance.simple.AlphaInAnimationAdapter;
import com.woi.merlin.R;
import com.woi.merlin.activity.AddNewReminder;
import com.woi.merlin.card.MedicalCard;
import com.woi.merlin.card.MedicalCardHeader;
import com.woi.merlin.card.NormalCard;
import com.woi.merlin.card.ReminderCardExpand;
import com.woi.merlin.enumeration.ReminderType;
import com.woi.merlin.enumeration.StatusType;
import com.woi.merlin.util.DbUtil;
import com.woi.merlin.util.GeneralUtil;
import com.woi.merlin.util.ReminderUtil;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.dao.query.Query;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.base.BaseCard;
import it.gmariotti.cardslib.library.view.CardListView;
import it.gmariotti.cardslib.library.view.listener.SwipeOnScrollListener;
import merlin.model.raw.DaoSession;
import merlin.model.raw.Reminder;
import merlin.model.raw.ReminderDao;
import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;

/**
 * Created by YeekFeiTan on 2/26/2015.
 */
public class ReminderFragment extends Fragment implements OnRefreshListener {

    //keep track of intents
    final int ADD_NEW_REMINDER = 1;
    final int UPDATE_REMINDER = 2;
    final int DELETE_REMINDER = 3;

    DaoSession daoSession = null;
    ReminderDao reminderDao = null;
    CardArrayAdapter mCardArrayAdapter;
    List<Card> cards;
    PullToRefreshLayout mPullToRefreshLayout = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reminder, container,
                false);

        // Retrieve the PullToRefreshLayout from the content view
        mPullToRefreshLayout = (PullToRefreshLayout) view.findViewById(R.id.carddemo_extra_ptr_layout);

        // Now setup the PullToRefreshLayout
        ActionBarPullToRefresh.from(getActivity())
                // Mark All Children as pullable
                .allChildrenArePullable()
                        // Set the OnRefreshListener
                .listener(this)
                        // Finally commit the setup to our PullToRefreshLayout
                .setup(mPullToRefreshLayout);

        initFAB(view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initDAOs();
        initCards();
    }

    //Initialise all the DAOs
    public void initDAOs() {
        daoSession = DbUtil.setupDatabase(getActivity());
        reminderDao = daoSession.getReminderDao();
    }

    //Initialise Floating Action Button
    private void initFAB(View view) {
        ListView listView = (ListView) view.findViewById(R.id.reminder_list_base);
        listView.setOnScrollListener(
                new SwipeOnScrollListener() {
                    @Override
                    public void onScrollStateChanged(AbsListView view, int scrollState) {
                        //It is very important to call the super method here to preserve built-in functions
                        super.onScrollStateChanged(view, scrollState);
                    }

                    @Override
                    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                        //Do something...
                    }
                }
        );
        final FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab_reminder_fm);
        fab.attachToListView(listView);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddNewReminder.class);
                startActivityForResult(intent, ADD_NEW_REMINDER);
            }
        });
    }

    private void initCards() {

//        for (int i = 0; i < 50; i++) {
//            MedicalCard medicalCard = getMedicalCard();
//            cards.add(medicalCard);
//        }
        cards = getAllCards();
        // Provide a custom adapter.
        // It is important to set the viewTypeCount
        // You have to provide in your card the type value with {@link Card#setType(int)} method.
        mCardArrayAdapter = new CardArrayAdapter(getActivity(), cards);
        mCardArrayAdapter.setInnerViewTypeCount(3);

        // An alternative is to write a own CardArrayAdapter
        // MyCardArrayAdapter mCardArrayAdapter = new MyCardArrayAdapter(getActivity(),cards);

        CardListView listView = (CardListView) getActivity().findViewById(R.id.reminder_list_base);
        if (listView != null) {
//            listView.setAdapter(mCardArrayAdapter);
            AnimationAdapter animCardArrayAdapter = new AlphaInAnimationAdapter(mCardArrayAdapter);
            animCardArrayAdapter.setAbsListView(listView);
            listView.setExternalAdapter(animCardArrayAdapter,mCardArrayAdapter);

        }



    }

    /**
     * Handle user returning from intent
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultIntent) {
        if (resultCode == getActivity().RESULT_OK) {
            //user is returning from capturing an image using the camera
            if (requestCode == ADD_NEW_REMINDER) {
                Long newId = resultIntent.getLongExtra(AddNewReminder.NEW_REMINDER_ID, 0l);
                if (newId != 0l) {
                    Reminder r = (Reminder) reminderDao.load(newId);
                    if (r != null) {
                        cards.add(getNormalCard(r));
                        mCardArrayAdapter.notifyDataSetChanged();
                    }
                }
            } else if (requestCode == UPDATE_REMINDER) {
                Long id = resultIntent.getLongExtra(AddNewReminder.NEW_REMINDER_ID, 0l);
                if (id != 0l) {
                    Reminder r = (Reminder) reminderDao.load(id);
                    if (r != null) {
                        initCards();
                        mCardArrayAdapter.notifyDataSetChanged();
                    }
                }
            }
            //user is returning from cropping the image
        }
    }

    private List<Card> getAllCards() {
        DaoSession daoSession = DbUtil.setupDatabase(getActivity());
        ReminderDao reminderDao = daoSession.getReminderDao();
        Query query = reminderDao.queryBuilder().where(ReminderDao.Properties.Status.eq(StatusType.of(StatusType.Active.toString()))).build();
        List<Reminder> reminders = query.list();
        List<Card> cards = new ArrayList<>();
        if (reminders != null) {
            for (Reminder r : reminders) {
                //Seperate by ReminderType
                if (r.getReminderType().equals(ReminderType.Normal.getValue())) {
                    cards.add(getCard(r));
                } else if (r.getReminderType().equals(ReminderType.MedicalReminder)) {

                } else if (r.getReminderType().equals(ReminderType.LoveCalendar)) {

                }
            }
        }

        return cards;
    }

    private Card getCard(Reminder reminder) {

        NormalCard card = new NormalCard(getActivity(), reminder);

        CardHeader header = new CardHeader(getActivity());

        card.addPartialOnClickListener(Card.CLICK_LISTENER_CONTENT_VIEW, new Card.OnCardClickListener() {
            @Override
            public void onClick(Card card, View view) {
                NormalCard normalCard = (NormalCard) card;
                Intent intent = new Intent(getActivity(), AddNewReminder.class);
                intent.putExtra("id", normalCard.getReminder().getId());
                startActivityForResult(intent, UPDATE_REMINDER);
            }
        });


//        header.setButtonOverflowVisible(true);
        //Add the listener
        header.setPopupMenuListener(new CardHeader.OnClickCardHeaderPopupMenuListener() {
            @Override
            public void onMenuItemClick(BaseCard card, MenuItem item) {
                Toast.makeText(getActivity(), "Click on " + item.getTitle() + "-" + ((Card) card).getCardHeader().getTitle(), Toast.LENGTH_SHORT).show();
            }
        });

        //Add a PopupMenuPrepareListener to add dynamically a menu entry
        header.setPopupMenuPrepareListener(new CardHeader.OnPrepareCardHeaderPopupMenuListener() {
            @Override
            public boolean onPreparePopupMenu(BaseCard card, PopupMenu popupMenu) {
                popupMenu.getMenu().add("Dynamic item");
                return true;
            }
        });

        card.addCardHeader(header);

        header.setButtonExpandVisible(true);

        //This provide a simple (and useless) expand area
        ReminderCardExpand expand = new ReminderCardExpand(getActivity(), reminder);

        //Set inner title in Expand Area
        expand.setTitle("Test");

        //Add expand to a card
        card.addCardExpand(expand);

        return card;
    }

    private Card getNormalCard(Reminder reminder) {

        //Create a Card
        MedicalCard card = new MedicalCard(getActivity(), reminder.getId(), reminder.getDescription(), ReminderType.Normal, GeneralUtil.getTimeInString(new LocalTime(reminder.getAtTime())));
//        card.setLastDismissedDate(lastDismissedDate);
//        card.setNextRemindDate(nextRemindDate);

        DateTime nextDate = ReminderUtil.getNextReminderTime(reminder);


        //Create a CardHeader
        String title = "";
        if (nextDate == null) {
            title = "Expired";
        } else {
            title = ReminderUtil.getReadableRemainingDate(nextDate);
        }

        MedicalCardHeader header = new MedicalCardHeader(getActivity(), title, reminder.getSubject());
        header.setBgColor(reminder.getColor());
        header.setPopupMenu(R.menu.sample1menu, new CardHeader.OnClickCardHeaderPopupMenuListener() {
            @Override
            public void onMenuItemClick(BaseCard card, MenuItem item) {
                Toast.makeText(getActivity(), "Click on " + item.getTitle(), Toast.LENGTH_SHORT).show();
            }
        });
        card.addCardHeader(header);

        //Set shadow elevation
        //Convert dp to float
//        float shadowElevation = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 14, getResources().getDisplayMetrics());
//        card.setCardElevation(shadowElevation);

        card.setSwipeable(true);

        card.addPartialOnClickListener(Card.CLICK_LISTENER_CONTENT_VIEW, new Card.OnCardClickListener() {
            @Override
            public void onClick(Card card, View view) {
                MedicalCard medicalCard = (MedicalCard) card;
                Intent intent = new Intent(getActivity(), AddNewReminder.class);
                intent.putExtra("id", medicalCard.getReminderId());
                startActivityForResult(intent, UPDATE_REMINDER);
            }
        });

        return card;

    }

    private MedicalCard getMedicalCard(Reminder reminder) {
        String description = "Paracetamol (acetaminophen) is a pain reliever and a fever reducer. Note: This dose will make you sleepy or dizzy.";
        ReminderType reminderType = ReminderType.MedicalReminder;
        String lastDismissedDate = GeneralUtil.getDateInString(new LocalDate());
        String nextRemindDate = GeneralUtil.getDateInString(new LocalDate());
        String atTime = GeneralUtil.getTimeInString(new LocalTime());
        String title = "22 minutes before taking";
        String subTitle = "Paracetamol";
        int bgColor = R.color.red_500;


        //Create a Card
        MedicalCard card = new MedicalCard(getActivity(), reminder.getId(), description, reminderType, atTime);
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

        //Set shadow elevation
        //Convert dp to float
        float shadowElevation = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 14, getResources().getDisplayMetrics());
        card.setCardElevation(shadowElevation);

        card.setSwipeable(true);

        return card;
    }

    @Override
    public void onRefreshStarted(View view) {
        /**
         * Simulate Refresh with 4 seconds sleep
         */
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                super.onPostExecute(result);

                // Notify PullToRefreshAttacher that the refresh has finished
                mPullToRefreshLayout.setRefreshComplete();
            }
        }.execute();
    }


//    private MedicalCard getMedicalCard() {
//        String description = "Paracetamol (acetaminophen) is a pain reliever and a fever reducer. Note: This dose will make you sleepy or dizzy.";
//        ReminderType reminderType = ReminderType.MedicalReminder;
//        String lastDismissedDate = GeneralUtil.getDateInString(new LocalDate());
//        String nextRemindDate = GeneralUtil.getDateInString(new LocalDate());
//        String atTime = GeneralUtil.getTimeInString(new LocalTime());
//        String title = "22 minutes before taking";
//        String subTitle = "Paracetamol";
//        int bgColor = R.color.red_500;
//
//
//        //Create a Card
//        MedicalCard card = new MedicalCard(getActivity(), description, reminderType, atTime);
//        card.setLastDismissedDate(lastDismissedDate);
//        card.setNextRemindDate(nextRemindDate);
//
//        //Create a CardHeader
//        MedicalCardHeader header = new MedicalCardHeader(getActivity(), title, subTitle);
//        header.setBgColor(bgColor);
//        header.setPopupMenu(R.menu.sample1menu, new CardHeader.OnClickCardHeaderPopupMenuListener() {
//            @Override
//            public void onMenuItemClick(BaseCard card, MenuItem item) {
//                Toast.makeText(getActivity(), "Click on " + item.getTitle(), Toast.LENGTH_SHORT).show();
//            }
//        });
//        card.addCardHeader(header);
//
//
////        card.addPartialOnClickListener(Card.CLICK_LISTENER_CONTENT_VIEW, new Card.OnCardClickListener() {
////            @Override
////            public void onClick(Card card, View view) {
//////                Toast.makeText(getActivity(),"New activity", Toast.LENGTH_LONG).show();
//////                testOpenActivity();
////            }
////        });
//
//        //Set shadow elevation
//        //Convert dp to float
//        float shadowElevation = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 14, getResources().getDisplayMetrics());
//        card.setCardElevation(shadowElevation);
//
//        card.setSwipeable(true);
//
//        //Set card in the CardViewNative
////        final CardViewNative cardViewNative = (CardViewNative) getActivity().findViewById(R.id.medical_card_layout);
////        cardViewNative.setCard(card);
//
//        return card;
//    }

}
