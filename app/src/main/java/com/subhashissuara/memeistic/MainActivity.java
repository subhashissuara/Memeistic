package com.subhashissuara.memeistic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final int APP_PERMISSION_REQUEST = 1;
    Button openButton, saveButton, shareButton, changeMemeTextColorButton;
    EditText memeTopTextInput, memeBottomTextInput;
    TextView memeTopText, memeBottomText;
    ImageView memeTemplateView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Media Storage Permission
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, APP_PERMISSION_REQUEST);
            } else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, APP_PERMISSION_REQUEST);
            }
        } else {
            // do nothing
        }


        // UI Assignments
        openButton = (Button) findViewById(R.id.open_button);
        saveButton = (Button) findViewById(R.id.save_button);
        shareButton = (Button) findViewById(R.id.share_button);
        changeMemeTextColorButton = (Button) findViewById(R.id.change_meme_text_color_button);

        memeTopTextInput = (EditText) findViewById(R.id.meme_top_text_input);
        memeBottomTextInput = (EditText) findViewById(R.id.meme_bottom_text_input);

        memeTopText = (TextView) findViewById(R.id.meme_top_text);
        memeBottomText = (TextView) findViewById(R.id.meme_bottom_text);

        memeTemplateView = (ImageView) findViewById(R.id.meme_template_view);


        // Disable Save & Share Buttons Initially
        saveButton.setEnabled(false);
        shareButton.setEnabled(false);


        // Button onClick Listeners
        openButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        changeMemeTextColorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentColor = memeTopText.getCurrentTextColor();
                switch (currentColor){
                    case Color.WHITE:
                        memeTopText.setTextColor(Color.BLACK);
                        memeBottomText.setTextColor(Color.BLACK);
                        break;
                    case Color.BLACK:
                    default:
                        memeTopText.setTextColor(Color.WHITE);
                        memeBottomText.setTextColor(Color.WHITE);
                }
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
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode) {
            case APP_PERMISSION_REQUEST: {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                        // do nothing
                    }
                    else {
                        Toast.makeText(MainActivity.this, "Required Permission Not Granted", Toast.LENGTH_LONG).show();
                        finish();
                    }
                    return;
                }
            }
        }
    }
}