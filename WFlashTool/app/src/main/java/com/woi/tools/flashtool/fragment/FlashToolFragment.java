package com.woi.tools.flashtool.fragment;

import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.afollestad.materialdialogs.folderselector.FolderChooserDialog;
import com.woi.tools.MainActivity;
import com.woi.tools.R;

import java.io.File;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by YeekFeiTan on 4/28/2015.
 */
public class FlashToolFragment extends Fragment {

    private Toast mToast;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.flash_tool_fragment, container,
                false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.cloneInitDFolderPath)
    public void showFlashToolFragment() {
        ((MainActivity)getActivity()).showFolderChooser("/sdcard/");
    }

    @OnClick(R.id.applyValue)
    public void test(View view) {
        Log.d("test","apply Value");
    }



}
