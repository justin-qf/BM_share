package com.example.myapplication;

import static android.provider.Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextPaint;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.myapplication.Common.CodeReUse;
import com.example.myapplication.Common.Constant;
import com.example.myapplication.adapter.TabFragmentAdapter;
import com.example.myapplication.databinding.ActivityLoginBinding;
import com.example.myapplication.fragments.ContactFragment;
import com.example.myapplication.fragments.RecentListFragment;

import java.util.Arrays;
import java.util.Objects;
import java.util.Observable;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    public ActivityLoginBinding binding;
    Animation LefToRightAnimation;
    Animation RightToLefTAnimation;
    private int indicatorWidth;
    private RecentListFragment recentListFragment;
    private ContactFragment contactFragment;
    // Static RequestPermissionCode VALUE
    public static final int RequestPermissionCode = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(act, R.layout.activity_login);
        hideKeyboard(findViewById(R.id.main_layout));
        init();
    }

    public void init() {
        setTextPaints();
        recentListFragment = new RecentListFragment();
        contactFragment = new ContactFragment();
        //verifyStoragePermission();
        CodeReUse.verifyStoragePermission(act, RequestPermissionCode);
        TabFragmentAdapter adapter = new TabFragmentAdapter(getSupportFragmentManager());
        adapter.addFragment(recentListFragment, "RecentList");
        adapter.addFragment(contactFragment, "ContactList");
        binding.viewPager.setAdapter(adapter);
        binding.tab.setupWithViewPager(binding.viewPager);
        binding.viewPager.setOffscreenPageLimit(3);
        binding.loginBtn.setOnClickListener(this);

        //Determine indicator width at runtime
        binding.tab.post(new Runnable() {
            @Override
            public void run() {
                indicatorWidth = binding.tab.getWidth() / binding.tab.getTabCount();
                FrameLayout.LayoutParams indicatorParams = (FrameLayout.LayoutParams) binding.indicator.getLayoutParams();
                indicatorParams.width = indicatorWidth - 10;
                binding.indicator.setLayoutParams(indicatorParams);
            }
        });
        binding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int i, float positionOffset, int positionOffsetPx) {
                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) binding.indicator.getLayoutParams();
                float translationOffset = (positionOffset + i) * indicatorWidth;
                params.leftMargin = (int) translationOffset;
                binding.indicator.setLayoutParams(params);
            }

            @Override
            public void onPageSelected(int i) {
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });

    }

    public void setTextPaints() {
        TextPaint paint = binding.titleTxt.getPaint();
        float width = paint.measureText(binding.titleTxt.getText().toString().trim());
        Shader textShader = new LinearGradient(0, 0, width, binding.titleTxt.getTextSize(),
                new int[]{ContextCompat.getColor(act, R.color.colorthird), ContextCompat.getColor(act, R.color.colorPrimary),}, null, Shader.TileMode.CLAMP);
        binding.titleTxt.getPaint().setShader(textShader);
        Animation blinkingText = AnimationUtils.loadAnimation(this, R.anim.blinking_view);
        binding.titleTxt.startAnimation(blinkingText);

        LefToRightAnimation = AnimationUtils.loadAnimation(this, R.anim.left_slide);
        RightToLefTAnimation = AnimationUtils.loadAnimation(this, R.anim.right_slide);
        binding.loginBtn.startAnimation(LefToRightAnimation);
        binding.userNumberTextLayout.startAnimation(RightToLefTAnimation);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == binding.loginBtn.getId()) {

            if (Objects.requireNonNull(binding.mobileNumberEditText.getText()).toString().trim().length() > 9) {
                Intent intent = new Intent(act, MainActivity.class);
                intent.putExtra("number", binding.mobileNumberEditText.getText().toString().trim());
                startActivity(intent);
                CodeReUse.slideEnter(act);

            } else if (binding.mobileNumberEditText.getText().toString().trim().isEmpty()) {
                Toast.makeText(act, "Enter Number", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(act, "Enter Valid Number", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void update(Observable observable, Object data) {
        super.update(observable, data);

        if (shareApp.getObserver().getValue() == Constant.PIC_FROM_CONTACT_LIST) {
            binding.mobileNumberEditText.setText(shareApp.getObserver().getData());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case RequestPermissionCode: {

                if (grantResults.length > 0) {
                    boolean cameraGrant = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeStorageGrant = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean readStorageGrant = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    boolean READ_CALL_LOG = grantResults[3] == PackageManager.PERMISSION_GRANTED;
                    boolean READ_CONTACTS = grantResults[4] == PackageManager.PERMISSION_GRANTED;

                    if (cameraGrant) {
                        shareApp.getObserver().setValue(Constant.IS_FIRST_TIME_LOAD);
                    }
                } else {
                    Toast.makeText(act, "You need to allow permission for better performance", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    //Permissions Check
    public void verifyStoragePermission() {
        int permission = ActivityCompat.checkSelfPermission(act, Arrays.toString(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_CALL_LOG, Manifest.permission.READ_CONTACTS}));

        // Surrounded with if statement for Android R to get access of complete file.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager() && permission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(act, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_CALL_LOG, Manifest.permission.READ_CONTACTS}, RequestPermissionCode);
                //Abruptly we will ask for permission once the application is launched for sake demo.
                Intent intent = new Intent();
                intent.setAction(ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                Uri uri = Uri.fromParts("package", act.getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        } else {
            ActivityCompat.requestPermissions(act, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_CALL_LOG, Manifest.permission.READ_CONTACTS}, RequestPermissionCode);
        }
    }

//        if (!isAccessibilityOn(act)) {
//            Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);
//
//        }


//    public boolean isAccessibilityOn(Activity act) {
//        Settings.Secure.putString(getContentResolver(), Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES, act.getPackageName() + "/" + LoginActivity.class);
//        Settings.Secure.putString(getContentResolver(), Settings.Secure.ACCESSIBILITY_ENABLED, "1");
//        return true;
//    }

}