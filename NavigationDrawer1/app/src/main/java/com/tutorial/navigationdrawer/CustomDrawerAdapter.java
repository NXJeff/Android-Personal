package com.tutorial.navigationdrawer;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.tutorial.navigation.spinner.CustomSpinnerAdapter;
import com.tutorial.navigation.spinner.SpinnerItem;

public class CustomDrawerAdapter extends ArrayAdapter<DrawerItem> {

    Context context;
    List<DrawerItem> drawerItemList;
    int layoutResID;

    public CustomDrawerAdapter(Context context, int layoutResourceID,
                               List<DrawerItem> listItems) {
        super(context, layoutResourceID, listItems);
        this.context = context;
        this.drawerItemList = listItems;
        this.layoutResID = layoutResourceID;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        DrawerItemHolder drawerHolder;
        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            drawerHolder = new DrawerItemHolder();

            view = inflater.inflate(layoutResID, parent, false);
            drawerHolder.ItemName = (TextView) view
                    .findViewById(R.id.drawer_itemName);
            drawerHolder.icon = (ImageView) view.findViewById(R.id.drawer_icon);

            drawerHolder.spinner = (Spinner) view
                    .findViewById(R.id.drawerSpinner);

            drawerHolder.title = (TextView) view.findViewById(R.id.drawerTitle);

            drawerHolder.headerLayout = (LinearLayout) view
                    .findViewById(R.id.headerLayout);
            drawerHolder.itemLayout = (LinearLayout) view
                    .findViewById(R.id.itemLayout);
            drawerHolder.spinnerLayout = (LinearLayout) view
                    .findViewById(R.id.spinnerLayout);

//            drawerHolder.imageLayout = (LinearLayout) view
//                    .findViewById(R.id.imageLayout);
//
//            drawerHolder.image = (ImageView) view.findViewById(R.id.drawer_image);

            view.setTag(drawerHolder);

        } else {
            drawerHolder = (DrawerItemHolder) view.getTag();
        }

        DrawerItem dItem = (DrawerItem) this.drawerItemList.get(position);

        if (dItem.getType() == DrawerItemType.Image) {
            drawerHolder.headerLayout.setVisibility(LinearLayout.INVISIBLE);
            drawerHolder.itemLayout.setVisibility(LinearLayout.INVISIBLE);
            drawerHolder.spinnerLayout.setVisibility(LinearLayout.INVISIBLE);
//            drawerHolder.imageLayout.setVisibility(LinearLayout.VISIBLE);




//            ImageView headerImageView = new ImageView(getContext());
//            headerImageView.setLayoutParams(new android.view.ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 200));
//            headerImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//            headerImageView.setImageResource(R.drawable.navdrawer_cover);

//            view.setBackgroundResource(R.drawable.navdrawer_cover);
//            view.setLayoutParams(new android.view.ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 200));


//            view = headerImageView;
//            view.setTag(drawerHolder);

        } else if (dItem.getType() == DrawerItemType.Spinner) {

            drawerHolder.headerLayout.setVisibility(LinearLayout.INVISIBLE);
            drawerHolder.itemLayout.setVisibility(LinearLayout.INVISIBLE);
            drawerHolder.spinnerLayout.setVisibility(LinearLayout.VISIBLE);
//            drawerHolder.imageLayout.setVisibility(LinearLayout.INVISIBLE);

            List<SpinnerItem> userList = new ArrayList<SpinnerItem>();

            userList.add(new SpinnerItem(R.drawable.user1, "Ahamed Ishak",
                    "ishakgmail.com"));

            userList.add(new SpinnerItem(R.drawable.user2, "Brain Jekob",
                    "brain.jgmail.com"));

            CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(context,
                    R.layout.custom_spinner_item, userList);

            drawerHolder.spinner.setAdapter(adapter);

            drawerHolder.spinner
                    .setOnItemSelectedListener(new OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(AdapterView<?> arg0,
                                                   View arg1, int arg2, long arg3) {

                            Toast.makeText(context, "User Changed",
                                    Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> arg0) {
                            // TODO Auto-generated method stub

                        }
                    });

        } else if (dItem.getType() == DrawerItemType.Text) {
            drawerHolder.headerLayout.setVisibility(LinearLayout.VISIBLE);
            drawerHolder.itemLayout.setVisibility(LinearLayout.INVISIBLE);
            drawerHolder.spinnerLayout.setVisibility(LinearLayout.INVISIBLE);
//            drawerHolder.imageLayout.setVisibility(LinearLayout.INVISIBLE);
            drawerHolder.title.setText(dItem.getTitle());

        } else if (dItem.getType() == DrawerItemType.TextWithImage) {

            drawerHolder.headerLayout.setVisibility(LinearLayout.INVISIBLE);
            drawerHolder.spinnerLayout.setVisibility(LinearLayout.INVISIBLE);
            drawerHolder.itemLayout.setVisibility(LinearLayout.VISIBLE);
//            drawerHolder.imageLayout.setVisibility(LinearLayout.INVISIBLE);

            drawerHolder.icon.setImageDrawable(view.getResources().getDrawable(
                    dItem.getImgResID()));
            drawerHolder.ItemName.setText(dItem.getItemName());

        }

        return view;
    }

    private static class DrawerItemHolder {
        TextView ItemName, title;
        ImageView icon, image;
        LinearLayout headerLayout, itemLayout, spinnerLayout;
        Spinner spinner;
    }
}