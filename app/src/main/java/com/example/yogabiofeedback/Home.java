package com.example.yogabiofeedback;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Home extends AppCompatActivity{
    Button abt,displayBtn;
    String currentImagePath=null;
    private static final int IMAGE_REQUEST=1;
    private static final int PERMISSION_REQUEST_CODE = 200;
    ImageView capture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        abt=findViewById(R.id.aboutid2);
        //abt.setOnClickListener(this);

        //Camera permission
        if (!checkPermission()) {
            requestPermission();
        }

        //Display result button
        displayBtn=findViewById(R.id.disbtn);
        displayBtn.setVisibility(View.INVISIBLE);
        displayBtn.setActivated(false);
        displayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentImagePath != null && !currentImagePath.isEmpty()) {
                    Toast.makeText(Home.this, currentImagePath, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Home.this, Result.class);
                    intent.putExtra("image_paths", currentImagePath);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(Home.this, "No image captured", Toast.LENGTH_SHORT).show();
                }
            }
        });




        capture=findViewById(R.id.capture);
        capture.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();




            Intent cameraIntent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            if(cameraIntent.resolveActivity(getPackageManager())!=null)
            {
                File imageFile=null;
                try {
                    imageFile=getImageFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if(imageFile!=null)
                {
                    Uri imageUri= FileProvider.getUriForFile(Home.this,"com.example.yogabiofeedback.fileprovider",imageFile);
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                    startActivityForResult(cameraIntent,IMAGE_REQUEST);
                }
            }
        }
    });


    }
    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);

        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);
        int result2 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);

        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE, CAMERA,READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);

    }


    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {

                    boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean cameraAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;


                    // Snackbar.make(view, "Permission Denied, You cannot access location data and camera.", Snackbar.LENGTH_LONG).show();

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE)) {
                            showMessageOKCancel("You need to allow access to both the permissions",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA, READ_EXTERNAL_STORAGE},
                                                        PERMISSION_REQUEST_CODE);
                                            }
                                        }
                                    });
                            return;
                        }
                    }


                }


                break;
        }
    }


    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(Home.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }


    private File getImageFile() throws IOException
    {
        String timeStamp=new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageName="jpg_"+timeStamp+"_";
        File storageDir=getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File imageFile=File.createTempFile(imageName,".jpg",storageDir);
        currentImagePath=imageFile.getAbsolutePath();
        return imageFile;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_CANCELED){
            currentImagePath=null;
        }
        else if(resultCode==RESULT_OK){
            displayBtn.setActivated(true);
            displayBtn.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        displayBtn.setActivated(false);
        displayBtn.setVisibility(View.INVISIBLE);
    }




}
