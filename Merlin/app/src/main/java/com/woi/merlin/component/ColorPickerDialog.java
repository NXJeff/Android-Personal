package com.woi.merlin.component;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import com.afollestad.materialdialogs.MaterialDialog;
import com.woi.merlin.R;

/**
 * Created by YeekFeiTan on 2/4/2015.
 */
public class ColorPickerDialog extends DialogFragment implements View.OnClickListener {

    private Callback mCallback;
    private int[] mColors;
    private String[] mColorsName;

    @Override
    public void onClick(View v) {
        if (v.getTag() != null) {
            Integer index = (Integer) v.getTag();
            mCallback.onColorSelection(index, mColors[index], shiftColor(mColors[index]), mColorsName[index]);
            dismiss();
        }
    }

    public static interface Callback {
        void onColorSelection(int index, int color, int darker, String colorName);
    }

    public ColorPickerDialog() {
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        MaterialDialog dialog = new MaterialDialog.Builder(getActivity())
                .title("Color Chooser")
                .autoDismiss(false)
                .customView(R.layout.dialog_color_picker, false)
                .build();

        final TypedArray ta = getActivity().getResources().obtainTypedArray(R.array.colors);
        mColors = new int[ta.length()];
        for (int i = 0; i < ta.length(); i++)
            mColors[i] = ta.getColor(i, 0);

        final TypedArray colorNameTa = getActivity().getResources().obtainTypedArray(R.array.colorsName);
        mColorsName = new String[colorNameTa.length()];
        for (int i = 0; i < colorNameTa.length(); i++)
            mColorsName[i] = colorNameTa.getString(i);

        colorNameTa.recycle();
        ta.recycle();
        final GridLayout list = (GridLayout) dialog.getCustomView().findViewById(R.id.grid);
        final int preselect = getArguments().getInt("preselect", -1);

        for (int i = 0; i < list.getChildCount(); i++) {
            FrameLayout child = (FrameLayout) list.getChildAt(i);
            child.setTag(i);
            child.setOnClickListener(this);
            child.getChildAt(0).setVisibility(preselect == i ? View.VISIBLE : View.GONE);

            Drawable selector = createSelector(mColors[i]);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                int[][] states = new int[][]{
                        new int[]{-android.R.attr.state_pressed},
                        new int[]{android.R.attr.state_pressed}
                };
                int[] colors = new int[]{
                        shiftColor(mColors[i]),
                        mColors[i]
                };
                ColorStateList rippleColors = new ColorStateList(states, colors);
                setBackgroundCompat(child, new RippleDrawable(rippleColors, selector, null));
            } else {
                setBackgroundCompat(child, selector);
            }


        }
        return dialog;
    }

    private void setBackgroundCompat(View view, Drawable d) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            view.setBackground(d);
        else view.setBackgroundDrawable(d);
    }

    private int shiftColor(int color) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] *= 0.9f; // value component
        return Color.HSVToColor(hsv);
    }

    private Drawable createSelector(int color) {
        ShapeDrawable coloredCircle = new ShapeDrawable(new OvalShape());
        coloredCircle.getPaint().setColor(color);
        ShapeDrawable darkerCircle = new ShapeDrawable(new OvalShape());
        darkerCircle.getPaint().setColor(shiftColor(color));

        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{-android.R.attr.state_pressed}, coloredCircle);
        stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, darkerCircle);
        return stateListDrawable;
    }

    public void show(Activity context, int preselect, Callback callback) {
        mCallback = callback;
        Bundle args = new Bundle();
        args.putInt("preselect", preselect);
        setArguments(args);
        show(context.getFragmentManager(), "COLOR_SELECTOR");
    }
}