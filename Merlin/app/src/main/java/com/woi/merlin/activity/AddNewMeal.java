package com.woi.merlin.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.woi.merlin.R;
import com.woi.merlin.util.MyFileContentProvider;

import java.io.File;

/**
 * Created by YeekFeiTan on 2/27/2015.
 */
public class AddNewMeal extends ActionBarActivity {

    Button photoButton;
    LinearLayout photosLayout;

    //keep track of camera capture intent
    final int CAMERA_CAPTURE = 1;
    //keep track of cropping intent
    final int PIC_CROP = 2;
    //captured picture uri
    private Uri picUri;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_meal_activity_layout);


        initViewComponents();
//        initActionBar();
//        initRepeatSpinner();
//        initCustomRepeatMode();
//        initReminderType();

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
                    //use standard intent to capture an image
//                    Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    //we will handle the returned data in onActivityResult
//                    startActivityForResult(captureIntent, CAMERA_CAPTURE);

                    Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    i.putExtra(MediaStore.EXTRA_OUTPUT, MyFileContentProvider.CONTENT_URI);
                    startActivityForResult(i, CAMERA_CAPTURE);

                } catch (ActivityNotFoundException anfe) {
                    //display an error message
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
            if(requestCode == CAMERA_CAPTURE){
                //get the Uri for the captured image
//                Bundle extras = data.getExtras();
                File out = new File(getFilesDir(), "newImage.jpg");

                if(!out.exists()) {
                    Toast.makeText(getBaseContext(),"Error while capturing image", Toast.LENGTH_LONG).show();
                    return;
                }


//                Bitmap imageBitmap = (Bitmap) extras.get("data");
                Bitmap imageBitmap = BitmapFactory.decodeFile(out.getAbsolutePath());
                ImageView imageView =  new ImageView(this);
                imageView.setImageBitmap(imageBitmap);
                imageView.setMaxHeight(600);
                photosLayout.addView(imageView);

                picUri = data.getData();
                //carry out the crop operation
//                performCrop();
            }
            //user is returning from cropping the image
            else if(requestCode == PIC_CROP){
                //get the returned data
//                Bundle extras = data.getExtras();
//                //get the cropped bitmap
//                Bitmap thePic = extras.getParcelable("data");
//                //retrieve a reference to the ImageView
//                ImageView picView = (ImageView)findViewById(R.id.picture);
//                ImageView imageView = (ImageView)
//                //display the returned cropped image
//                picView.setImageBitmap(thePic);
            }
        }
    }

    /**
     * Helper method to carry out crop operation
     */
    private void performCrop(){
        //take care of exceptions
        try {
            //call the standard crop action intent (the user device may not support it)
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            //indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image/*");
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
        catch(ActivityNotFoundException anfe){
            //display an error message
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
