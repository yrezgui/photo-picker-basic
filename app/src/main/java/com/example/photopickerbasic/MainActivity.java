package com.example.photopickerbasic;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import static android.os.ext.SdkExtensions.getExtensionVersion;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {
    final int PHOTO_PICKER_SINGLE_SELECT = 42;
    final int PHOTO_PICKER_MULTIPLE_SELECT = 43;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.button).setOnClickListener(this::onClick);
    }

    private void onClick(View view) {
        if(!isPhotoPickerAvailable()) {
            Snackbar.make(view, "Photo Picker isn't available, sorry", Snackbar.LENGTH_LONG).show();
        }

        EditText editText = findViewById(R.id.maxItemsLimit);
        int maxItemsLimit = Integer.parseInt(editText.getText().toString());

        Intent intent = new Intent(MediaStore.ACTION_PICK_IMAGES);

        if(maxItemsLimit == 1) {
            startActivityForResult(intent, PHOTO_PICKER_SINGLE_SELECT);
        } else {
            intent.putExtra(MediaStore.EXTRA_PICK_IMAGES_MAX, maxItemsLimit);
            startActivityForResult(intent, PHOTO_PICKER_MULTIPLE_SELECT);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK) {
            // Handle error
            return;
        }

        switch(requestCode) {
            case PHOTO_PICKER_SINGLE_SELECT:
                // Get photo picker response for single select.
                Uri uri = data.getData();
                Log.d("PHOTO_PICKER_SINGLE_SELECT", uri.toString());
                return;

            case PHOTO_PICKER_MULTIPLE_SELECT:
                // Get photo picker response for multi select
                for (int i = 0; i < data.getClipData().getItemCount(); i++) {
                    Uri currentUri = data.getClipData().getItemAt(i).getUri();
                    Log.d("PHOTO_PICKER_MULTIPLE_SELECT", currentUri.toString());
                }
        }
    }

    @SuppressLint("NewApi")
    private boolean isPhotoPickerAvailable() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return true;
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            return getExtensionVersion(Build.VERSION_CODES.R) >= 2;
        } else {
            return false;
        }
    }
}