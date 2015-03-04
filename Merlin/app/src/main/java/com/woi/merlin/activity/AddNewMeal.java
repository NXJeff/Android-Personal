package com.woi.merlin.activity;

import android.content.ActivityNotFoundException;
import android.content.Entity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.woi.merlin.R;
import com.woi.merlin.enumeration.EntityType;
import com.woi.merlin.model.ImageHolder;
import com.woi.merlin.util.EntityUtil;
import com.woi.merlin.util.MediaUtil;
import com.woi.merlin.util.MyFileContentProvider;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by YeekFeiTan on 2/27/2015.
 */
public class AddNewMeal extends ActionBarActivity {

    private Button photoButton;
    private LinearLayout photosLayout;
    private List<ImageHolder> imageHolders = new ArrayList<>();

    private String entityID;

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

    private void initViewComponents() {
        photosLayout = (LinearLayout) findViewById(R.id.photosLayout);
        photoButton = (Button) findViewById(R.id.add_photo_btn);

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
    }

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

    public String getEntityID() {
        return entityID;
    }

    public void setEntityID(String entityID) {
        this.entityID = entityID;
    }
}
