package com.woi.merlin.component;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Environment;
import android.app.DialogFragment;
import android.app.Fragment;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.woi.merlin.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by YeekFeiTan on 4/28/2015.
 */
public class FolderChooserDialog extends DialogFragment implements MaterialDialog.ListCallback {

    private File parentFolder;
    private File[] parentContents;
    private boolean canGoUp = true;
    private FolderSelectCallback mCallback;
    private boolean isFile = false;
    private String fileChooserId;

    private final MaterialDialog.ButtonCallback mButtonCallback = new MaterialDialog.ButtonCallback() {
        @Override
        public void onPositive(MaterialDialog materialDialog) {
            if (parentFolder.isFile() && !isFile) {
                return;
            }
            materialDialog.dismiss();
            mCallback.onFolderSelection(fileChooserId, parentFolder);
        }

        @Override
        public void onNegative(MaterialDialog materialDialog) {
            materialDialog.dismiss();
        }
    };

    public interface FolderSelectCallback {
        void onFolderSelection(String id, File folder);
    }


    public FolderChooserDialog() {

        parentFolder = Environment.getExternalStorageDirectory();

    }

    String[] getContentsArray() {
        String[] results = new String[parentContents.length + (canGoUp ? 1 : 0)];
        if (canGoUp) results[0] = "...";
        for (int i = 0; i < parentContents.length; i++)
            results[canGoUp ? i + 1 : i] = parentContents[i].getName();
        return results;
    }

    File[] listFiles() {
        if (!parentFolder.isDirectory()) {
            return new File[0];
        }
        File[] contents = parentFolder.listFiles();
        List<File> results = new ArrayList<>();
        for (File fi : contents) {
            if (fi.isFile() && !isFile) {
                continue;
            }
//            if (fi.isDirectory())
            results.add(fi);
        }
        Collections.sort(results, new FolderSorter());
        return results.toArray(new File[results.size()]);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new MaterialDialog.Builder(getActivity())
                .title(parentFolder.getAbsolutePath())
                .items(getContentsArray())
                .itemsCallback(this)
                .callback(mButtonCallback)
                .autoDismiss(false)
                .positiveText(R.string.choose)
                .negativeText(android.R.string.cancel)
                .build();
    }

    @Override
    public void onSelection(MaterialDialog materialDialog, View view, int i, CharSequence s) {

        if (parentFolder.isDirectory()) {
            if (canGoUp && i == 0) {
                parentFolder = parentFolder.getParentFile();
                canGoUp = parentFolder.getParent() != null;
            } else {
                parentFolder = parentContents[canGoUp ? i - 1 : i];
                canGoUp = true;
            }
            parentContents = listFiles();
        } else {
            if (canGoUp && i == 0) {
                parentFolder = parentFolder.getParentFile();
                canGoUp = parentFolder.getParent() != null;
                parentContents = listFiles();
            }
        }
        MaterialDialog dialog = (MaterialDialog) getDialog();
        dialog.setTitle(parentFolder.getAbsolutePath());
        dialog.setItems(getContentsArray());
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    public void show(Fragment context) {
        if (getArguments() != null) {
            isFile = getArguments().getBoolean("isFile");
            fileChooserId = getArguments().getString("id");

            if(getArguments().get("FileURI") != null) {
                String fileURI = getArguments().getString("FileURI");
                File file = new File(fileURI);
                if (file != null) {
                    parentFolder = file;
                }
            }


        }
        parentContents = listFiles();
        show(context.getFragmentManager(), "FOLDER_SELECTOR");
        mCallback = (FolderSelectCallback) context;
    }

    private static class FolderSorter implements Comparator<File> {
        @Override
        public int compare(File lhs, File rhs) {

            if (lhs.isFile() && rhs.isDirectory()) {
                return 1;
            } else if (lhs.isDirectory() && rhs.isFile()) {
                return -1;
            }

            return lhs.getName().compareTo(rhs.getName());
        }
    }
}