package com.example.myapplication;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myapplication.Common.CodeReUse;
import com.example.myapplication.Common.Constant;
import com.example.myapplication.Common.HELPER;
import com.example.myapplication.Common.PickerFragment;
import com.example.myapplication.databinding.ActivityMainBinding;

import java.net.URLEncoder;
import java.util.Objects;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    public ActivityMainBinding binding;
    String number = "";
    private ActivityResultLauncher<Intent> resultLauncher;
    PickerFragment pickerFragment;
    String[] strAr1 = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(act, R.layout.activity_main);
        initView();
    }

    public void initView() {
        strAr1 = new String[]{"7359792115", "820002604"};
        hideKeyboard(findViewById(R.id.main_Layout));
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        initResultLauncher();
        number = getIntent().getStringExtra("number");
        binding.cvGallery.setOnClickListener(this);
        binding.shareView.setOnClickListener(this);
        binding.cvSharBtn.setOnClickListener(this);
        binding.cvPdfView.setOnClickListener(this);
    }

    private void pickerView(int actionId, boolean viewMode, Bitmap selectedBitmap, String url) {
        pickerFragment = new PickerFragment(act);
        pickerFragment.setUserNumber(number);
        pickerFragment.setEnableViewMode(viewMode);
        pickerFragment.setActionId(actionId);
        if (actionId == Constant.PICKER_PROFILE) {
            pickerFragment.setCalledFragmentContext(Constant.PROFILE_FRAGMENT);
        }
        if (viewMode) {
            if (selectedBitmap != null)
                pickerFragment.setSelectedBitmapForFullView(selectedBitmap);
            else
                pickerFragment.setImageUrl(url);
        }
        PickerFragment.HandlerImageLoad imageLoad = new PickerFragment.HandlerImageLoad() {
            @Override
            public void onGalleryResult(int flag, Bitmap bitmap) {
                if (flag == Constant.PICKER_PROFILE) {
                    Glide.with(act).clear(binding.galleryImg);
                    binding.galleryImg.setVisibility(View.GONE);
                    binding.galleryImg.setImageBitmap(bitmap);
                }
            }
        };
        pickerFragment.setImageLoad(imageLoad);
        pickerFragment.show(getSupportFragmentManager(), pickerFragment.getTag());
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.cvGallery) {
            pickerView(Constant.PICKER_PROFILE, false, null, "");
        } else if (id == R.id.cvPdfView) {
            if (canAccessStorage()) {
                choosePDF();
            }
        } else if (id == R.id.cvSharBtn) {
            if (!Objects.requireNonNull(binding.textEditText.getText()).toString().trim().isEmpty()) {
                HELPER.print("Number", binding.textEditText.getText().toString());

                // MySMService.handleActionWhatsapp(act, "Hiii", "5", strAr1);
                CodeReUse.shareTextToWhatsapp(act, number, Objects.requireNonNull(binding.textEditText.getText()).toString().trim());
                //shareTextToWhatsapp(number, Objects.requireNonNull(binding.textEditText.getText()).toString().trim());
            } else {
                Toast.makeText(act, "Enter Text", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //select pdf
    private void choosePDF() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf");
        resultLauncher.launch(intent);
    }

    // handle response after select the document
    private void initResultLauncher() {
        resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                Intent data = result.getData();
                if (data != null) {
                    Uri pdfUrl = data.getData();
                    CodeReUse.sendPdfToWhatsApp(act, number, pdfUrl);
                    //sendPdfToWhatsApp(number, pdfUrl);
                }
            }
        });
    }

    //ask for permission
    private boolean canAccessStorage() {
        // check condition
        if (ActivityCompat.checkSelfPermission(act, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(act, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            Toast.makeText(act, "Please Enable permission for accessing pdf files", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    public void shareTextToWhatsapp(String number, String message) {

        boolean isWhatsappInstalled = CodeReUse.whatsappInstalledOrNot(act);
        String toNumber = "";
        try {
            Intent intent;
            if (isWhatsappInstalled) {
                if (number.startsWith("+")) {
                    toNumber = number;
                } else {
                    toNumber = "91" + number;
                }
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://api.whatsapp.com/send?phone=" + toNumber + "&text=" + URLEncoder.encode(message, "UTF-8")));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setPackage("com.whatsapp");
            } else {
                Uri uri = Uri.parse("market://details?id=com.whatsapp");
                intent = new Intent(Intent.ACTION_VIEW, uri);
                Toast.makeText(this, "WhatsApp not Installed", Toast.LENGTH_SHORT).show();
            }
            startActivity(intent);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //send Pdf to Whatsapp
    public void sendPdfToWhatsApp(String number, Uri pdfUrl) {
        boolean isWhatsappInstalled = CodeReUse.whatsappInstalledOrNot(act);
        try {
            if (isWhatsappInstalled) {

                String toNumber = "91" + number;
                Intent sendIntent = new Intent("android.intent.action.MAIN");
                sendIntent.putExtra(Intent.EXTRA_STREAM, pdfUrl);
                sendIntent.putExtra("jid", toNumber + "@s.whatsapp.net");
                sendIntent.putExtra(Intent.EXTRA_TEXT, "");
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.setPackage("com.whatsapp");
                sendIntent.setType("application/pdf");
                sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                sendIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(sendIntent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}