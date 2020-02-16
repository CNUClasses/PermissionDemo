package com.example.permissiondemo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import com.google.android.material.snackbar.Snackbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    //all the permissions we need (although we are only using the camera)
    //this demonstrates how to ask for a bunch of permissions at one time
    private static final String[] PERMISSIONS={Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA};
    private static final int PERMS_REQ_CODE = 200;

    private View mLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLayout = findViewById(R.id.main_layout);
    }

    public void startCamera(View view) {
        showCameraPreview();
    }

    public void startCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivity(intent);
    }

    private void showCameraPreview() {
        //TODO verify that app has permission to use camera
        //do we have needed permissions? if not
        if (!verifyPermissions())
            return;

        startCamera();
    }
    /**
     * Verify that the specific list of permisions requested have been granted, otherwise ask for
     * these permissions.  Note this is coarse in that I assumme I need them all
     */
    private boolean verifyPermissions() {

        //loop through all permissions seeing if they are ALL granted
        //iff ALL granted then return true
        boolean allGranted = true;
        for (String permission:PERMISSIONS){
            //a single false causes allGranted to be false
            allGranted = allGranted && (ActivityCompat.checkSelfPermission(this, permission ) == PackageManager.PERMISSION_GRANTED);
        }

        if (!allGranted) {
            //OH NO!, missing some permissions, offer rationale if needed
            for (String permission : PERMISSIONS) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                    Snackbar.make(findViewById(android.R.id.content),
                            permission+" WE GOTTA HAVE IT!", Snackbar.LENGTH_LONG).show();
                }
            }

            //Okay now finally ask for them
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(PERMISSIONS, PERMS_REQ_CODE);
            }
        }

        //and return false until they are granted
        return allGranted;
    }

    /***
     * callback from requestPermissions
     * @param permsRequestCode  user defined code passed to requestpermissions used to identify what callback is coming in
     * @param permissions       list of permissions requested
     * @param grantResults      //results of those requests
     */
    @Override
    public void onRequestPermissionsResult(int permsRequestCode, String[] permissions, int[] grantResults) {

        boolean allGranted = false;
        switch (permsRequestCode) {
            case PERMS_REQ_CODE:
                for (int result: grantResults){
                    allGranted = allGranted&&(result== PackageManager.PERMISSION_GRANTED);
                }
                break;
        }

        if (allGranted)
            //TODO do your work here
            startCamera();
    }

}
