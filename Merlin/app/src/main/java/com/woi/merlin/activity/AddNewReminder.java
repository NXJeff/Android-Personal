package com.woi.merlin.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.IconTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.ThemeSingleton;
import com.woi.merlin.R;
import com.woi.merlin.component.ColorPickerDialog;
import com.woi.merlin.component.DatePickerFragment;
import com.woi.merlin.component.ImageViewActivity;
import com.woi.merlin.component.TimePickerFragment;
import com.woi.merlin.enumeration.CustomRepeatMode;
import com.woi.merlin.enumeration.EntityType;
import com.woi.merlin.enumeration.ReminderType;
import com.woi.merlin.enumeration.RepeatType;
import com.woi.merlin.enumeration.StatusType;
import com.woi.merlin.util.DbUtil;
import com.woi.merlin.util.EditTextValidator;
import com.woi.merlin.util.EntityUtil;
import com.woi.merlin.util.GeneralUtil;
import com.woi.merlin.util.MediaUtil;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import merlin.model.raw.DaoSession;
import merlin.model.raw.ImageHolder;
import merlin.model.raw.ImageHolderDao;
import merlin.model.raw.Reminder;
import merlin.model.raw.ReminderDao;

/**
 * Created by Jeffery on 2/2/2015.
 */
public class AddNewReminder extends ActionBarActivity {

    public final static String NEW_REMINDER_ID = "NEW_REMINDER_ID";
    //keep track of intents
    final int CAMERA_CAPTURE = 1;
    final int PIC_CROP = 2;
    //captured picture uri
    private Uri fileUri;

    TextView fromDatePicker, toDatePicker, atTimePicker, colorPicker;
    LocalDate fromDate, toDate;
    LocalTime atTime;
    Spinner repeatSpinner, customRepeatModeSpinner, reminderTypeSpinner;
    IconTextView colorIconView;
    int selectedColor;
    String entityID;
    private Button photoButton;
    private LinearLayout photosLayout;

    RepeatType repeatType = RepeatType.DONOTREPEAT;
    ReminderType reminderType = ReminderType.Normal;
    CustomRepeatMode customRepeatMode = CustomRepeatMode.FOREVER;

    EditText subjectET, repeatEveryNDayET, dosesInTotalET, dosesPerDayET, remarkET;

    Reminder reminder;
    private List<ImageHolder> imageHolders = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_reminder_activity_layout);

        initActionBar();
        initViewComponents();
        initRepeatSpinner();
        initCustomRepeatMode();
        initReminderType();
        initEntityID();
        handleEditMode();

    }

    private void handleEditMode() {
        Long id = getIntent().getLongExtra("id", 0l);
        if (id != 0l) {
            DaoSession daoSession = DbUtil.setupDatabase(this);
            ReminderDao reminderDao = daoSession.getReminderDao();
            ImageHolderDao imageHolderDao = daoSession.getImageHolderDao();
            reminder = reminderDao.load(id);
            entityID = reminder.getEntityId();
            imageHolders = imageHolderDao.queryBuilder().where(ImageHolderDao.Properties.EntityId.eq(entityID)).build().list();
            setValueFromReminder();
        }
    }

    /**
     * Convenient method to set value to GUI from reminder
     *
     * @param
     */
    private void setValueFromReminder() {
        subjectET.setText(reminder.getSubject());
        reminderType = ReminderType.of(reminder.getReminderType());
        populateReminderType();
        fromDate = new LocalDate(reminder.getFromDate());
        toDate = new LocalDate(reminder.getToDate());
        atTime = new LocalTime(reminder.getAtTime());
        refreshDateTextLabels();
        selectedColor = reminder.getColor();
        applyColorToActivity(selectedColor);
        repeatType = RepeatType.of(reminder.getRepeatType());
        populateRepeatSpinnerValue();
        customRepeatMode = CustomRepeatMode.of(reminder.getCustomRepeatMode());
        populateCustomRepeatSpinnerValue();

        if (reminder.getRepeatEveryNDay() != null) {
            repeatEveryNDayET.setText(reminder.getRepeatEveryNDay().toString());
        }
        if (reminder.getDosesPerDay() != null) {
            dosesPerDayET.setText(reminder.getDosesPerDay().toString());
        }
        if (reminder.getDosesInTotal() != null) {
            dosesInTotalET.setText(reminder.getDosesInTotal().toString());
        }
        remarkET.setText(reminder.getDescription());

        if (!imageHolders.isEmpty()) {
            for (ImageHolder ih : imageHolders) {
                addNewPhoto(ih.getPath());
            }
        }
    }

    /**
     * Handle user returning from both capturing and cropping the image
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            //user is returning from capturing an image using the camera
            if (requestCode == CAMERA_CAPTURE) {
                if (resultCode == RESULT_OK) {
                    MediaUtil.compressImage(fileUri.getPath());
                    addNewPhoto(fileUri.getPath());
                    //Create ImageHolder
                    ImageHolder ih = new ImageHolder();
                    ih.setEntityId(entityID);
                    ih.setPath(fileUri.getPath());
                    imageHolders.add(ih);
                }
            }
            //user is returning from cropping the image
            else if (requestCode == PIC_CROP) {
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_new_reminder_action_bar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Log.d("itemId", getResources().getResourceName(item.getItemId()));
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.homeAsUp:
            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.menu_save:
                onSave();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return super.onOptionsItemSelected(item);
    }

    //**INIT**//
    private void initEntityID() {
        if (entityID == null) {
            entityID = EntityUtil.generateEntityUniqueID(this, EntityType.REMINDER);
        }
    }

    private void initActionBar() {
        // enable ActionBar app icon to behave as action to toggle nav drawer
        // use action bar here
//        getSupportActionBar().setIcon(new IconDrawable(this, Iconify.IconValue.fa_bell).colorRes(R.color.grey_white_1000).actionBarSize());
        getSupportActionBar().setTitle("New Event");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void initViewComponents() {
        fromDatePicker = (TextView) findViewById(R.id.fromDatePickerET);
        toDatePicker = (TextView) findViewById(R.id.toDatePickerET);
        atTimePicker = (TextView) findViewById(R.id.atTimePickerET);
        repeatSpinner = (Spinner) findViewById(R.id.repeatSpinner);
        customRepeatModeSpinner = (Spinner) findViewById(R.id.customRepeatMode);
        reminderTypeSpinner = (Spinner) findViewById(R.id.reminderTypeSpinner);
        colorPicker = (TextView) findViewById(R.id.colorPickerET);
        colorIconView = (IconTextView) findViewById(R.id.colorIconView);
        fromDate = new LocalDate();
        toDate = new LocalDate();
        atTime = new LocalTime();

        repeatEveryNDayET = (EditText) findViewById(R.id.repeatEveryNDayET);
        dosesInTotalET = (EditText) findViewById(R.id.dosesInTotalET);
        dosesPerDayET = (EditText) findViewById(R.id.dosesPerDayET);
        remarkET = (EditText) findViewById(R.id.remarkET);
        subjectET = (EditText) findViewById(R.id.subjectET);
        photosLayout = (LinearLayout) findViewById(R.id.addNewReminderPhotosLayout);
        photoButton = (Button) findViewById(R.id.addNewReminderAddPhotoBtn);

        refreshDateTextLabels();

        fromDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFromDateDatePicker();
            }
        });

        toDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToDateDatePicker();
            }
        });

        atTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAtTimePicker();
            }
        });

        colorPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomColorChooser();
            }
        });

        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    fileUri = MediaUtil.getOutputMediaFileUri(MediaUtil.MEDIA_TYPE_IMAGE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                    // start the image capture Intent
                    startActivityForResult(intent, CAMERA_CAPTURE);

                } catch (ActivityNotFoundException anfe) {
                    String errorMessage = "Whoops - your device doesn't support capturing images!";
                    Toast toast = Toast.makeText(getBaseContext(), errorMessage, Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        applyDefaultColorToActivity();
    }

    /**
     * Refresh the textview that show the date
     */
    private void refreshDateTextLabels() {
        fromDatePicker.setText(GeneralUtil.getDateInString(fromDate));
        toDatePicker.setText(GeneralUtil.getDateInString(toDate));
        atTimePicker.setText(GeneralUtil.getTimeInString(atTime));
    }


    public void showFromDateDatePicker() {
        DatePickerFragment date = new DatePickerFragment();
        /**
         * Set Up Current Date Into dialog
         */
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", fromDate.getYear());
        args.putInt("month", fromDate.getMonthOfYear() - 1);
        args.putInt("day", fromDate.getDayOfMonth());
        date.setArguments(args);
        /**
         * Set Call back to capture selected date
         */
        date.setCallBack(callbackOnFromDate);
        date.show(this.getFragmentManager(), "From Date");
    }

    public void showToDateDatePicker() {
        DatePickerFragment date = new DatePickerFragment();
        /**
         * Set Up Current Date Into dialog
         */
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", toDate.getYear());
        args.putInt("month", toDate.getMonthOfYear() - 1);
        args.putInt("day", toDate.getDayOfMonth());
        date.setArguments(args);
        /**
         * Set Call back to capture selected date
         */
        date.setCallBack(callbackOnToDate);
        date.show(this.getFragmentManager(), "To Date");
    }

    public void showAtTimePicker() {
        TimePickerFragment date = new TimePickerFragment();
        /**
         * Set Up Current Date Into dialog
         */
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("hour", atTime.getHourOfDay());
        args.putInt("minutes", atTime.getMinuteOfHour());
        date.setArguments(args);
        /**
         * Set Call back to capture selected date
         */
        date.setCallBack(callbackOnAtTime);
        date.show(this.getFragmentManager(), "At Time");
    }

    //Initialize Callbacks
    DatePickerDialog.OnDateSetListener callbackOnFromDate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            fromDate = new LocalDate(year, monthOfYear + 1, dayOfMonth);
            fromDatePicker.setText(GeneralUtil.getDateInString(fromDate));
        }
    };

    DatePickerDialog.OnDateSetListener callbackOnToDate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            toDate = new LocalDate(year, monthOfYear + 1, dayOfMonth);
            toDatePicker.setText(GeneralUtil.getDateInString(toDate));
        }
    };

    TimePickerDialog.OnTimeSetListener callbackOnAtTime = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            atTime = new LocalTime(hourOfDay, minute);
            atTimePicker.setText(GeneralUtil.getTimeInString(atTime));
        }
    };

    /**
     * repeatSpinner
     */
    private void initRepeatSpinner() {

        ArrayAdapter<RepeatType> dataAdapter = new ArrayAdapter<RepeatType>(this,
                android.R.layout.simple_list_item_1, RepeatType.values());
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        repeatSpinner.setAdapter(dataAdapter);

        repeatSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                repeatType = (RepeatType) repeatSpinner.getSelectedItem();
                renderRepeatCustomView();
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                //Do nothing
            }
        });
    }

    //Convenient method to populate value into repeat spinner
    private void populateRepeatSpinnerValue() {
        repeatSpinner.setSelection(((ArrayAdapter<RepeatType>) repeatSpinner.getAdapter()).getPosition(repeatType));
    }

    //Convenient method to populate value into custom Repeat spinner
    private void populateCustomRepeatSpinnerValue() {
        customRepeatModeSpinner.setSelection(((ArrayAdapter<CustomRepeatMode>) customRepeatModeSpinner.getAdapter()).getPosition(customRepeatMode));
    }

    /**
     * customRepeatModeSpinner
     */
    private void initCustomRepeatMode() {

        ArrayAdapter<CustomRepeatMode> dataAdapter = new ArrayAdapter<CustomRepeatMode>(this,
                android.R.layout.simple_spinner_item, CustomRepeatMode.values());
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        customRepeatModeSpinner.setAdapter(dataAdapter);

        customRepeatModeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                customRepeatMode = (CustomRepeatMode) customRepeatModeSpinner.getSelectedItem();
                LinearLayout dosesInTotalLayout = (LinearLayout) findViewById(R.id.dosesInTotalLayout);
                LinearLayout toDatePickerLayout = (LinearLayout) findViewById(R.id.toDatePickerLayout);

                if (customRepeatMode.equals(CustomRepeatMode.FOREVER)) {
                    toDatePickerLayout.setVisibility(View.GONE);
                    dosesInTotalLayout.setVisibility(View.GONE);

                } else if (customRepeatMode.equals(CustomRepeatMode.UNTIL)) {
                    toDatePickerLayout.setVisibility(View.VISIBLE);
                    dosesInTotalLayout.setVisibility(View.GONE);
                } else if (customRepeatMode.equals(CustomRepeatMode.EVENTS)) {
                    toDatePickerLayout.setVisibility(View.GONE);
                    if (reminderType.equals(ReminderType.MedicalReminder))
                        dosesInTotalLayout.setVisibility(View.VISIBLE);
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                //Do nothing
            }
        });
    }

    private void initReminderType() {

        ArrayAdapter<ReminderType> dataAdapter = new ArrayAdapter<ReminderType>(this,
                android.R.layout.simple_spinner_item, ReminderType.values());

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        reminderTypeSpinner.setAdapter(dataAdapter);
        reminderTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                reminderType = (ReminderType) reminderTypeSpinner.getSelectedItem();
                applyColorBasedOnReminderType();
                renderRepeatCustomView();
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                //Do nothing
            }
        });
    }

    /**
     * Conditions and render repeatCustomView layout.
     */
    private void renderRepeatCustomView() {
        LinearLayout repeatCustomView = (LinearLayout) findViewById(R.id.repeatCustomView);
        LinearLayout dosePerDayOptionLayout = (LinearLayout) findViewById(R.id.dosePerDayOptionLayout);

        if (repeatType.equals(RepeatType.CUSTOM)) {
            repeatCustomView.setVisibility(View.VISIBLE);
        } else {
            repeatCustomView.setVisibility(View.GONE);
        }

        if (repeatType.equals(RepeatType.DONOTREPEAT)) {
            dosePerDayOptionLayout.setVisibility(View.GONE);
        } else {
            if (reminderType.equals(ReminderType.MedicalReminder))
                dosePerDayOptionLayout.setVisibility(View.VISIBLE);
            else
                dosePerDayOptionLayout.setVisibility(View.GONE);
        }
    }


    private void applyColorBasedOnReminderType() {
        if (reminder == null) {
            if (reminderType.equals(ReminderType.Normal)) {
                applySelectedColorToActivity(13);
            } else if (reminderType.equals(ReminderType.MedicalReminder)) {
                applySelectedColorToActivity(5);
            } else if (reminderType.equals(ReminderType.LoveCalendar)) {
                applySelectedColorToActivity(20);
            }
        }
    }

    private void populateReminderType() {
        reminderTypeSpinner.setSelection(((ArrayAdapter<ReminderType>) reminderTypeSpinner.getAdapter()).getPosition(reminderType));
    }

    private void addNewPhoto(String filePath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 6;
        final Bitmap imageBitmap = BitmapFactory.decodeFile(filePath, options);
        ImageView imageView = new ImageView(this);
        imageView.setImageBitmap(imageBitmap);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(10, 10, 10, 10);
        imageView.setLayoutParams(params);


        imageView.setTag(filePath);
        imageView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showImageViewer(v.getTag().toString());
                    }
                }
        );
        photosLayout.addView(imageView);
    }

    private void showImageViewer(String path) {
        List<String> images = new ArrayList<>();
        boolean found = false;
        int position = 0;
        for (ImageHolder ih : imageHolders) {
            if (ih.getPath().equals(path)) {
                position = images.size();
            }
            images.add(ih.getPath());
        }
        Intent intent = new Intent(this, ImageViewActivity.class);
        intent.putExtra("Images", images.toArray(new String[images.size()]));
        intent.putExtra("openPosition", position);
        startActivity(intent);
    }

    private void removeImageHolder(String path) {
        if (!imageHolders.isEmpty()) {
            ImageHolder imageHolder = null;
            for (ImageHolder ih : imageHolders) {
                if (ih.getPath().equals(path)) {
                    imageHolder = ih;
                }
            }

            if (imageHolder != null) {
                removeImageHolder(imageHolder);
                imageHolders.remove(imageHolder);
            }
        }
    }

    private void removeImageHolder(ImageHolder imageHolder) {
        if (imageHolder.getId() != null) {
            DaoSession daoSession = DbUtil.setupDatabase(this);
            ImageHolderDao imageHolderDao = daoSession.getImageHolderDao();
            imageHolderDao.delete(imageHolder);

            MediaUtil.deleteFile(imageHolder.getPath());
        }
    }

    /**
     * Color changer methods
     */

    static int selectedColorIndex = -1;

    private void showCustomColorChooser() {
        new ColorPickerDialog().show(this, selectedColorIndex, new ColorPickerDialog.Callback() {
            @Override
            public void onColorSelection(int index, int color, int darker, String colorName) {
                selectedColorIndex = index;
                applyColorToActivity(color, darker, colorName);
            }
        });
    }

    private void applyDefaultColorToActivity() {
        applySelectedColorToActivity(13);
    }

    private void applySelectedColorToActivity(int colorPosition) {
        TypedArray ca = getResources().obtainTypedArray(R.array.colors);
        TypedArray cna = getResources().obtainTypedArray(R.array.colorsName);
        selectedColor = ca.getColor(colorPosition, 0);
        int darker = ColorPickerDialog.shiftColor(selectedColor);
        String colorName = cna.getString(colorPosition);
        applyColorToActivity(selectedColor, darker, colorName);
    }

    private void applyColorToActivity(int colorId) {
        TypedArray ca = getResources().obtainTypedArray(R.array.colors);
        int position = 0;
        for (int i = 0; i < ca.length(); i++) {
            if (ca.getColor(i, 0) == colorId) {
                position = i;
                break;
            }
        }

        applySelectedColorToActivity(position);
    }

    private void applyColorToActivity(int color, int darker, String colorName) {
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(color));
        selectedColor = color;
        colorIconView.setTextColor(color);
        colorPicker.setText(colorName);
        ThemeSingleton.get().positiveColor = color;
        ThemeSingleton.get().neutralColor = color;
        ThemeSingleton.get().negativeColor = color;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            getWindow().setStatusBarColor(darker);
    }

    /**
     * Override default behaviour
     */
    @Override
    public void onBackPressed() {

        new MaterialDialog.Builder(this)
                .title("Cancel")
                .content("Are you sure you want to discard this event?")
                .positiveText("Keep Editing")
                .negativeText("Discard")
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        dialog.dismiss();
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        if (android.os.Build.VERSION.SDK_INT >= 21) {
                            finishAfterTransition();
                        } else {
                            finish();
                        }
                    }
                })
                .show();
    }

    /**
     * Validations
     *
     * @return
     */
    public boolean validateReminderValues() {

        if (!EditTextValidator.hasText(subjectET)) {
            return false;
        }

        /* Repeat Conditions */
        if (reminderType.equals(ReminderType.Normal)) {
            //TODO: cause now nothing to do
        } else if (reminderType.equals(ReminderType.MedicalReminder)) {
            if (repeatType.equals(RepeatType.CUSTOM)) {
                //TODO
            } else if (!repeatType.equals(RepeatType.DONOTREPEAT)) {
                if (!EditTextValidator.hasText(dosesPerDayET)) {
                    return false;
                }
            }
        }

        //Construct
        if (reminder == null) {
            reminder = new Reminder();
        }
        reminder.setStatus(StatusType.Active.getValue());
        reminder.setEnabled(Boolean.TRUE);
        reminder.setSubject(subjectET.getText().toString());
        reminder.setReminderType(reminderType.getValue());
        reminder.setFromDate(fromDate.toDate());
        reminder.setToDate(toDate.toDate());
        reminder.setAtTime(atTime.toDateTimeToday().toDate());
        reminder.setColor(selectedColor);
        reminder.setRepeatType(repeatType.getValue());
        reminder.setCustomRepeatMode(customRepeatMode.getValue());
        reminder.setEntityId(entityID);

        if (!repeatEveryNDayET.getText().toString().isEmpty())
            reminder.setRepeatEveryNDay(Integer.parseInt(repeatEveryNDayET.getText().toString()));
        if (!dosesPerDayET.getText().toString().isEmpty())
            reminder.setDosesPerDay(Integer.parseInt(dosesPerDayET.getText().toString()));
        if (!dosesInTotalET.getText().toString().isEmpty())
            reminder.setDosesInTotal(Integer.parseInt(dosesInTotalET.getText().toString()));

        reminder.setDescription(remarkET.getText().toString());

        return true;
    }

    /**
     * Saving methods
     */
    public void saveToDatabase() {

        DaoSession daoSession = DbUtil.setupDatabase(this);
        ImageHolderDao imageHolderDao = daoSession.getImageHolderDao();
        ReminderDao reminderDao = daoSession.getReminderDao();
        if (reminder.getId() == null) {
            reminderDao.insert(reminder);
        } else {
            reminderDao.update(reminder);
        }

        if (!imageHolders.isEmpty()) {
            for (ImageHolder imageHolder : imageHolders) {
                if (imageHolder.getId() == null) {
                    imageHolderDao.insert(imageHolder);
                } else {
                    imageHolderDao.update(imageHolder);
                }
            }
        }

        Intent resultIntent = new Intent();
        resultIntent.putExtra(NEW_REMINDER_ID, reminder.getId());
        setResult(Activity.RESULT_OK, resultIntent);
//        if (android.os.Build.VERSION.SDK_INT >= 21) {
//            finishAfterTransition();
//        } else {
            finish();
//        }
    }

    public void onSave() {
        if (validateReminderValues()) {
            runOnUiThread(
                    new Runnable() {
                        public void run() {
                            saveToDatabase();
                        }
                    }

            );
        }

    }
}
