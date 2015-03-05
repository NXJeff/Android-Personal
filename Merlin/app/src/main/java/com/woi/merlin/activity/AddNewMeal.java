package com.woi.merlin.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Entity;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.IconTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.ThemeSingleton;
import com.woi.merlin.R;
import com.woi.merlin.component.ColorPickerDialog;
import com.woi.merlin.component.DatePickerFragment;
import com.woi.merlin.component.TimePickerFragment;
import com.woi.merlin.enumeration.EntityType;
import com.woi.merlin.enumeration.StatusType;
import com.woi.merlin.model.ImageHolder;
import com.woi.merlin.model.Meal;
import com.woi.merlin.util.DbUtil;
import com.woi.merlin.util.EditTextValidator;
import com.woi.merlin.util.EntityUtil;
import com.woi.merlin.util.GeneralUtil;
import com.woi.merlin.util.MediaUtil;
import com.woi.merlin.util.MyFileContentProvider;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import merlin.model.raw.BaseMealDao;
import merlin.model.raw.BaseReminderDao;
import merlin.model.raw.DaoSession;

/**
 * Created by YeekFeiTan on 2/27/2015.
 */
public class AddNewMeal extends ActionBarActivity {

    TextView mealDatePicker, mealTimePicker, colorPicker;
    LocalDate date;
    LocalTime time;
    private Button photoButton;
    private LinearLayout photosLayout;
    private List<ImageHolder> imageHolders = new ArrayList<>();
    IconTextView colorIconView;
    private Meal meal;
    EditText subjectET, remarkET;

    private String entityID;
    int selectedColor;

    //keep track of camera capture intent
    final int CAMERA_CAPTURE = 1;
    //keep track of cropping intent
    final int PIC_CROP = 2;
    //captured picture uri
    private Uri fileUri;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_meal_activity_layout);

        initViewComponents();
        initEntityID();
//        initActionBar();

    }

    private void initEntityID() {
        if (entityID == null) {
            entityID = EntityUtil.generateEntityUniqueID(this, EntityType.MEAL);
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

    private void initViewComponents() {
        mealDatePicker = (TextView) findViewById(R.id.newMealDatePickerET);
        mealTimePicker = (TextView) findViewById(R.id.newMealTimePickerET);
        colorPicker = (TextView) findViewById(R.id.addNewMealColorPickerET);
        colorIconView = (IconTextView) findViewById(R.id.addNewMealColorIconView);
        remarkET = (EditText) findViewById(R.id.addNewMealRemarkET);
        subjectET = (EditText) findViewById(R.id.addNewMealSubjectET);

        photosLayout = (LinearLayout) findViewById(R.id.addNewMealPhotosLayout);
        photoButton = (Button) findViewById(R.id.addNewMealAddPhotoBtn);

        date = new LocalDate();
        time = new LocalTime();

        mealDatePicker.setText(GeneralUtil.getDateInString(date));
        mealDatePicker.setText(GeneralUtil.getTimeInString(time));

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

        mealDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToDateDatePicker();
            }
        });
        mealTimePicker.setOnClickListener(new View.OnClickListener() {
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

        applyDefaultColorToActivity();

    }

    public void showToDateDatePicker() {
        DatePickerFragment date = new DatePickerFragment();
        /**
         * Set Up Current Date Into dialog
         */
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", this.date.getYear());
        args.putInt("month", this.date.getMonthOfYear() - 1);
        args.putInt("day", this.date.getDayOfMonth());
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
        args.putInt("hour", time.getHourOfDay());
        args.putInt("minutes", time.getMinuteOfHour());
        date.setArguments(args);
        /**
         * Set Call back to capture selected date
         */
        date.setCallBack(callbackOnAtTime);
        date.show(this.getFragmentManager(), "At Time");
    }

    //Initialize Callbacks
    DatePickerDialog.OnDateSetListener callbackOnToDate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            date = new LocalDate(year, monthOfYear + 1, dayOfMonth);
            mealDatePicker.setText(GeneralUtil.getDateInString(date));
        }
    };

    TimePickerDialog.OnTimeSetListener callbackOnAtTime = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            time = new LocalTime(hourOfDay, minute);
            mealTimePicker.setText(GeneralUtil.getTimeInString(time));
        }
    };

    /**
     * Handle user returning from both capturing and cropping the image
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            //user is returning from capturing an image using the camera
            if (requestCode == CAMERA_CAPTURE) {
                if (resultCode == RESULT_OK) {
                    //get the Uri for the captured image
//                Bundle extras = data.getExtras();
//                    File out = new File(getFilesDir(), "newImage.jpg");

//                    if (!out.exists()) {
//                        Toast.makeText(getBaseContext(), "Error while capturing image", Toast.LENGTH_LONG).show();
//                        return;
//                    }
                    addNewPhoto();
                }
            }
            //user is returning from cropping the image
            else if (requestCode == PIC_CROP) {
            }
        }
    }

    private void addNewPhoto() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;
        final Bitmap imageBitmap = BitmapFactory.decodeFile(fileUri.getPath(),
                options);
        ImageView imageView = new ImageView(this);
        imageView.setImageBitmap(imageBitmap);
        imageView.setMaxHeight(600);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(10, 10, 10, 10);
        imageView.setLayoutParams(params);

        imageView.setTag(fileUri.getPath());
        imageView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final String path = (String) v.getTag();
                        final View view = v;

                        new MaterialDialog.Builder(AddNewMeal.this)
                                .title("Remove")
                                .content("Are you sure you want to remove this photo?")
                                .positiveText("Remove, right away.")
                                .negativeText("No, hold on")
                                .callback(new MaterialDialog.ButtonCallback() {
                                    @Override
                                    public void onPositive(MaterialDialog dialog) {
                                        view.setVisibility(View.GONE);
                                        removeImageHolder(path);
                                    }

                                    @Override
                                    public void onNegative(MaterialDialog dialog) {
                                        dialog.dismiss();
                                    }
                                })
                                .show();
                    }
                }
        );
        photosLayout.addView(imageView);

        //Create ImageHolder
        ImageHolder ih = new ImageHolder();
        ih.setEntityId(entityID);
        ih.setPath(fileUri.getPath());
        imageHolders.add(ih);
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
                Log.d("AddNewMeal", "Removed " + imageHolder.getPath());
            }
        }
    }

    private void removeImageHolder(ImageHolder imageHolder) {
        if (imageHolder.getId() != null) {
            //remove
        }
    }

    /**
     * Here we store the file url as it will be null after returning from camera
     * app
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save file url in bundle as it will be null on scren orientation
        // changes
        outState.putParcelable("file_uri", fileUri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // get the file url
        fileUri = savedInstanceState.getParcelable("file_uri");
    }

    /**
     * Helper method to carry out crop operation
     */
    private void performCrop() {
        //take care of exceptions
        try {
            //call the standard crop action intent (the user device may not support it)
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            //indicate image type and Uri
            cropIntent.setDataAndType(fileUri, "image/*");
            //set crop properties
            cropIntent.putExtra("crop", "true");
            //indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            //indicate output X and Y
            cropIntent.putExtra("outputX", 256);
            cropIntent.putExtra("outputY", 256);
            //retrieve data on return
            cropIntent.putExtra("return-data", true);
            //start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, PIC_CROP);
        }
        //respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException anfe) {
            //display an error message
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    /**
     * Color components
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

    /**
     * Color changer methods
     */
    private void applyDefaultColorToActivity() {
        applyDefaultColorToActivity(13);
    }

    private void applyDefaultColorToActivity(int colorPosition) {
        TypedArray ca = getResources().obtainTypedArray(R.array.colors);
        TypedArray cna = getResources().obtainTypedArray(R.array.colorsName);
        selectedColor = ca.getColor(colorPosition, 0);
        int darker = ColorPickerDialog.shiftColor(selectedColor);
        String colorName = cna.getString(colorPosition);
        applyColorToActivity(selectedColor, darker, colorName);
    }

    private void applyColorToActivity(int color, int darker, String colorName) {
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(color));
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
                        finish();
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

        //Construct
        meal = new Meal();
        meal.setStatus(StatusType.Active.getValue());
        meal.setSubject(subjectET.getText().toString());
        meal.setColor(selectedColor);
        meal.setEntityId(entityID);



        meal.setDescription(remarkET.getText().toString());

        return true;
    }


    /**
     * Saving methods
     */
    public void saveToDatabase() {

        DaoSession daoSession = DbUtil.setupDatabase(this);
        BaseMealDao mealDao = daoSession.getBaseMealDao();
        mealDao.insert(meal);
        finish();
    }

    public void onSave() {
        if (validateReminderValues()) {
            new Thread(new Runnable() {
                public void run() {
                    saveToDatabase();
                }
            }).start();
        }
    }

    public String getEntityID() {
        return entityID;
    }

    public void setEntityID(String entityID) {
        this.entityID = entityID;
    }


}
