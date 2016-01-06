package tools.woi.com.woitools.archive.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.folderselector.FolderChooserDialog;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tools.woi.com.woitools.R;
import tools.woi.com.woitools.archive.domain.ArchiveItem;
import tools.woi.com.woitools.archive.domain.ArchiveMode;
import tools.woi.com.woitools.archive.domain.DayOrMonthMode;

/**
 * Created by YeekFeiTan on 1/5/2016.
 */
public class AddArchiveActivity extends AppCompatActivity implements FolderChooserDialog.FolderCallback {

    @Bind(R.id.llOlderThan)
    LinearLayout olderThanLinearLayout;

    @Bind(R.id.llMoreThan)
    LinearLayout moreThanLinearLayout;

    @Bind(R.id.archive_mode_spinner)
    Spinner archiveModeSpinner;

    @Bind(R.id.dayOrMonthSpinner)
    Spinner dayOrMonthSpinner;

    @Bind(R.id.etOlderThanNumber)
    EditText etOlderThanNumber;

    @Bind(R.id.etMoreThanNumber)
    EditText etMoreThanNumber;

    @Bind(R.id.archive_name)
    EditText etArchiveName;

    @Bind(R.id.archive_path)
    TextView tvArchivePath;

    //Data
    ArchiveItem archiveItem;
    ArchiveMode archiveMode;
    DayOrMonthMode olderThanOption;
    String sourcePath;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // inflate menu from xml
        if (archiveItem != null) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.activity_new_archive, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.action_delete:
                MaterialDialog dialog = new MaterialDialog.Builder(this)
                        .title(R.string.delete_archive)
                        .content(R.string.confirmation_delete)
                        .positiveText(R.string.delete)
                        .negativeText(R.string.cancel)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                deleteArchive();
                            }
                        })
                        .show();
                break;
        }

        return true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_archive);
        ButterKnife.bind(this);

        initArchiveModeSpinner();
        initDayOrMonthSpinner();
        handleEditMode();
    }

    private void handleEditMode() {
        Long id = getIntent().getLongExtra("id", 0l);
        if (id != 0l) {
            archiveItem = ArchiveItem.findById(ArchiveItem.class, id);
            etArchiveName.setText(archiveItem.getName());
            sourcePath = archiveItem.getSourcePath();
            tvArchivePath.setText(sourcePath);
            archiveMode = archiveItem.getArchieveMode();
            archiveModeSpinner.setSelection(((ArrayAdapter<ArchiveMode>) archiveModeSpinner.getAdapter()).getPosition(archiveMode));
            if (archiveItem.getDayOrMonthMode() != null) {
                dayOrMonthSpinner.setSelection(((ArrayAdapter<DayOrMonthMode>) dayOrMonthSpinner.getAdapter()).getPosition(olderThanOption));
            }
            if (archiveItem.getDayOrMonthNumber() != 0) {
                etOlderThanNumber.setText(String.valueOf(archiveItem.getDayOrMonthNumber()));
            }

            if (archiveItem.getMaxFilesNo() != 0) {
                etMoreThanNumber.setText(String.valueOf(archiveItem.getMaxFilesNo()));
            }

        }
    }

    private void initArchiveModeSpinner() {
        ArrayAdapter<ArchiveMode> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_single_choice, ArchiveMode.values());

        archiveModeSpinner.setAdapter(dataAdapter);
        archiveModeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                archiveMode = (ArchiveMode) archiveModeSpinner.getSelectedItem();
                if (archiveMode.equals(ArchiveMode.OlderThan)) {
                    olderThanLinearLayout.setVisibility(View.VISIBLE);
                    moreThanLinearLayout.setVisibility(View.GONE);
                } else {
                    olderThanLinearLayout.setVisibility(View.GONE);
                    moreThanLinearLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void initDayOrMonthSpinner() {
        ArrayAdapter<DayOrMonthMode> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_single_choice, DayOrMonthMode.values());

        dayOrMonthSpinner.setAdapter(dataAdapter);
        dayOrMonthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                olderThanOption = (DayOrMonthMode) dayOrMonthSpinner.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @OnClick(R.id.archive_path)
    public void showFolderChooser() {
        new FolderChooserDialog.Builder(this)
                .chooseButton(R.string.md_choose_label)  // changes label of the choose button
                .initialPath(Environment.getExternalStorageDirectory().getAbsolutePath())  // changes initial path, defaults to external storage directory
                .show();
    }

    private boolean validateBeforeSave() {
        return true;
    }


    @OnClick(R.id.bSave)
    public void onSaveButtonClick(View view) {
        if (validateBeforeSave()) {

            if (archiveItem == null) {
                archiveItem = new ArchiveItem();
            }
            archiveItem.setName(etArchiveName.getText().toString());
            archiveItem.setSourcePath(sourcePath);
            archiveItem.setArchieveMode(archiveMode);
            if (archiveItem.getArchieveMode().equals(ArchiveMode.OlderThan)) {
                archiveItem.setDayOrMonthNumber(Integer.parseInt(etOlderThanNumber.getText().toString()));
                archiveItem.setDayOrMonthMode(olderThanOption);
                archiveItem.setMaxFilesNo(0);
            } else {
                archiveItem.setDayOrMonthNumber(0);
                archiveItem.setMaxFilesNo(Integer.parseInt(etMoreThanNumber.getText().toString()));
                archiveItem.setDayOrMonthMode(null);
            }

            archiveItem.save();

            Intent returnIntent = new Intent();
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        }
    }

    private void deleteArchive() {
        archiveItem.delete();
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    @Override
    public void onFolderSelection(File folder) {
        sourcePath = folder.getAbsolutePath();
        tvArchivePath.setText(sourcePath);
    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }
}
