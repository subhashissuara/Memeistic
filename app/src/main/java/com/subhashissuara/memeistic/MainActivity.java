package com.subhashissuara.memeistic;

import androidx.annotation.ContentView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private static final int APP_PERMISSION_CODE = 10;

    Button openButton, saveButton, shareButton, changeMemeTextColorButton, fontSizeButton, retryButton;
    EditText memeTopTextInput, memeBottomTextInput;
    TextView memeTopText, memeBottomText;
    ImageView memeTemplateView;

    String currentMemeFile = "";
    float dX, dY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // UI Assignments
        openButton = (Button) findViewById(R.id.open_button);
        saveButton = (Button) findViewById(R.id.save_button);
        shareButton = (Button) findViewById(R.id.share_button);
        changeMemeTextColorButton = (Button) findViewById(R.id.change_meme_text_color_button);
        fontSizeButton = (Button) findViewById(R.id.font_size_button);
        retryButton = (Button) findViewById(R.id.retry_button);

        memeTopTextInput = (EditText) findViewById(R.id.meme_top_text_input);
        memeBottomTextInput = (EditText) findViewById(R.id.meme_bottom_text_input);

        memeTopText = (TextView) findViewById(R.id.meme_top_text);
        memeBottomText = (TextView) findViewById(R.id.meme_bottom_text);

        memeTemplateView = (ImageView) findViewById(R.id.meme_template_view);


        // Disable Buttons Initially
        saveButton.setEnabled(false);
        shareButton.setEnabled(false);
        changeMemeTextColorButton.setEnabled(false);
        fontSizeButton.setEnabled(false);
        retryButton.setEnabled(false);


        // Button onClick Listeners
        openButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkAndRequestPermissions(MainActivity.this)){
                    openImage(MainActivity.this);
                }
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View memeLayout = findViewById(R.id.meme_template_layout);
                Bitmap memeSS = takeScreenshot(memeLayout);
                currentMemeFile = System.currentTimeMillis() + "_meme.png";
                saveImage(memeSS, currentMemeFile);
                shareButton.setEnabled(true);
            }
        });

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareImage(currentMemeFile);
            }
        });

        changeMemeTextColorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final CharSequence[] optionsMenu = {"Black", "White"};
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Choose Font Color");
                builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.setItems(optionsMenu, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (optionsMenu[i].equals("Black")){
                            memeTopText.setTextColor(Color.BLACK);
                            memeBottomText.setTextColor(Color.BLACK);
                        }
                        if (optionsMenu[i].equals("White")){
                            memeTopText.setTextColor(Color.WHITE);
                            memeBottomText.setTextColor(Color.WHITE);
                        }
                    }
                });
                builder.show();
            }
        });

        fontSizeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Choose Font Size");
                final NumberPicker fontPicker = new NumberPicker(MainActivity.this);
                fontPicker.setMaxValue(100);
                fontPicker.setMinValue(0);
                builder.setView(fontPicker);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        int fontSize = fontPicker.getValue();
                        memeTopText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, fontSize);
                        memeBottomText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, fontSize);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int whichButton) {
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
            }
        });

        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainActivity = new Intent(MainActivity.this, MainActivity.class);
                startActivity(mainActivity);
                finish();
            }
        });

        // Text onChange Listeners
        memeTopTextInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Do Nothing
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Do Nothing
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.toString().length() > 0) {
                    memeTopText.setText(editable.toString());
                }
            }
        });

        memeBottomTextInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Do Nothing
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Do Nothing
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.toString().length() > 0) {
                    memeBottomText.setText(editable.toString());
                }
            }
        });

        // Make TextViews Movable
        memeTopText.setOnTouchListener(mOnTouchListener);
        memeBottomText.setOnTouchListener(mOnTouchListener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == APP_PERMISSION_CODE) {
                if (ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(),
                            "App Requires Access to Camara.", Toast.LENGTH_SHORT)
                            .show();
                } else if (ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(),
                            "App Requires Access to Your Storage.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    openImage(MainActivity.this);
                }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 11:
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        memeTemplateView.setImageBitmap(selectedImage);
                    }
                    break;
                case 12:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null) {
                            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();
                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String picturePath = cursor.getString(columnIndex);
                                memeTemplateView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                cursor.close();
                            }
                        }
                    }
                    break;
            }
            saveButton.setEnabled(true);
            changeMemeTextColorButton.setEnabled(true);
            fontSizeButton.setEnabled(true);
            retryButton.setEnabled(true);
            memeTemplateView.setBackgroundColor(getResources().getColor(R.color.black));
        }
    }

    View.OnTouchListener mOnTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    dX = view.getX() - event.getRawX();
                    dY = view.getY() - event.getRawY();
                    break;

                case MotionEvent.ACTION_MOVE:
                    view.animate()
                            .x(event.getRawX() + dX)
                            .y(event.getRawY() + dY)
                            .setDuration(0)
                            .start();
                    break;
                default:
                    return false;
            }
            return true;
        }
    };

    // Check & Request Permission
    public static boolean checkAndRequestPermissions(final Activity context) {
        int storagePermission = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int cameraPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA);
        List<String> listOfPermissionsNeeded = new ArrayList<>();

        if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
            listOfPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (storagePermission != PackageManager.PERMISSION_GRANTED) {
            listOfPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (!listOfPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(context,
                    listOfPermissionsNeeded.toArray(new String[listOfPermissionsNeeded.size()]),
                    APP_PERMISSION_CODE
            );
            return false;
        }
        return true;
    }

    private void openImage(Context context){
        final CharSequence[] optionsMenu = {"Take Photo", "Choose from Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose Image Source");
        builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setItems(optionsMenu, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(optionsMenu[i].equals("Take Photo")){
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 11);
                }
                if(optionsMenu[i].equals("Choose from Gallery")){
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto , 12);
                }
            }
        });
        builder.show();
    }

    public static Bitmap takeScreenshot(View view) {
        view.setDrawingCacheEnabled(true);
        Bitmap memeImage = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);
        return memeImage;
    }

    public void saveImage(Bitmap memeImage, String filename) {
        OutputStream os;
        try {
            ContentResolver contentResolver = getContentResolver();
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, filename);
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/*");
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + File.separator + "Memeistic");
            Uri imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            os = contentResolver.openOutputStream(Objects.requireNonNull(imageUri));
            memeImage.compress(Bitmap.CompressFormat.PNG, 100, os);
            Objects.requireNonNull(imageUri);

            Toast.makeText(MainActivity.this, "Meme Saved!", Toast.LENGTH_SHORT).show();
        } catch (Exception e){
            Toast.makeText(MainActivity.this, "Error While Saving Meme!", Toast.LENGTH_SHORT).show();
        }
    }

    private void shareImage(String filename) {
        String[] projection = {
                MediaStore.MediaColumns.DISPLAY_NAME,
                MediaStore.MediaColumns.RELATIVE_PATH
        };
        Uri uris = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = getContentResolver().query(
                uris, projection, null, null, null);
        Uri image = null;
        while (cursor.moveToNext()) {
            if (String.valueOf(cursor.getString(cursor.getColumnIndex(
                    MediaStore.Images.Media.DISPLAY_NAME))).equals(filename)) {
                image = Uri.parse(String.valueOf(cursor.getString(cursor.getColumnIndex(
                        MediaStore.Images.Media.RELATIVE_PATH))) + File.separator + filename);
            }
        }
        Intent share = new Intent();
        share.setAction(Intent.ACTION_SEND);
        share.setType("image/*");
        share.putExtra(Intent.EXTRA_STREAM, image);

        try {
            startActivity(Intent.createChooser(share, "Share Via"));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(MainActivity.this, "No Sharing App Found!", Toast.LENGTH_SHORT).show();
        }

    }
}