package com.example.myapplication.Utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.text.HtmlCompat;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.databinding.DialogImageViewLayoutBinding;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class Utility {
    private static Dialog dialog;
    private static final String APP_SPECIFIC_FOLDER = "offline/video";


    public static void Log(String act, Object msg) {
        Log.e(act, msg + "");
    }

    public static void printHtmlText(String text, TextView textView) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            textView.setText(Html.fromHtml(text, 0));
            //textView.setText(Html.fromHtml(text, Html.TO_HTML_PARAGRAPH_LINES_INDIVIDUAL));
        } else {
            textView.setText(HtmlCompat.fromHtml(text, 0));
        }
    }

    public static void RemoveError(EditText editText, TextInputLayout textInputLayout) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                editText.setError(null);
                textInputLayout.setError(null);
                textInputLayout.setHelperText("");
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }
        });
    }

    public static String getProgressDisplayLine(long currentBytes, long totalBytes) {
        return getBytesToMBString(currentBytes) + "/" + getBytesToMBString(totalBytes);
    }

    private static String getBytesToMBString(long bytes) {
        return String.format(Locale.ENGLISH, "%.2fMb", bytes / (1024.00 * 1024.00));
    }



    public static String getRootDirPath(Context context) {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            File file = ContextCompat.getExternalFilesDirs(context.getApplicationContext(), null)[0];
            return file.getAbsolutePath();
        } else {
            return context.getApplicationContext().getFilesDir().getAbsolutePath();
        }
    }

    public static void showAlertNew(Activity act, String msg) {
        new AlertDialog.Builder(act)
                .setMessage(msg)
                .setCancelable(true)
                .setPositiveButton("OK", (dialogInterface, i) -> dialogInterface.dismiss())
                .show();
    }


    public static void showAlert(Activity act, String msg, String flag) {
        new AlertDialog.Builder(act)
                .setMessage(msg)
                .setCancelable(true)
                .setPositiveButton("OK", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                })
                .show();
    }

    private static OnImageViewDismiss viewDismiss;

    public static void fullScreenImageViewerBitmap(Activity act, Bitmap imageUrl, OnImageViewDismiss viewDi) {
        viewDismiss = viewDi;
        DialogImageViewLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(act),
                R.layout.dialog_image_view_layout, null, false);
        androidx.appcompat.app.AlertDialog alertDialog;
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(act, R.style.MyAlertDialogStyle);
        builder.setView(binding.getRoot());
        alertDialog = builder.create();
        alertDialog.setContentView(binding.getRoot());

        binding.CloseImg.setOnClickListener(v -> alertDialog.dismiss());

        binding.fullImageViewer3.setImageBitmap(imageUrl);
        binding.fullImageViewer3.setVisibility(View.VISIBLE);
        binding.fullImageViewer3.setVisibility(View.VISIBLE);
        alertDialog.show();
        alertDialog.setOnDismissListener(dialog -> viewDismiss.onPhotoDialogDismiss());
    }

    public static void fullScreenImageViewerURL(Activity act, String imageUrl, OnImageViewDismiss viewDi) {
        viewDismiss = viewDi;
        DialogImageViewLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(act),
                R.layout.dialog_image_view_layout, null, false);
        androidx.appcompat.app.AlertDialog alertDialog;
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(act, R.style.MyAlertDialogStyle);
        builder.setView(binding.getRoot());
        alertDialog = builder.create();
        alertDialog.setContentView(binding.getRoot());

        binding.CloseImg.setOnClickListener(v -> alertDialog.dismiss());
        Glide.with(act).load(imageUrl)
                .placeholder(ContextCompat.getDrawable(act, R.drawable.male_vector))
                .error(ContextCompat.getDrawable(act, R.drawable.male_vector))
                .into(binding.fullImageViewer3);
        binding.fullImageViewer3.setVisibility(View.VISIBLE);
        binding.fullImageViewer3.setVisibility(View.VISIBLE);
        alertDialog.show();
        alertDialog.setOnDismissListener(dialog -> viewDismiss.onPhotoDialogDismiss());
    }

    public static void showAlert(Activity act, String msg) {
        new AlertDialog.Builder(act)
                .setMessage(msg)
                .setCancelable(true)
                .setPositiveButton("OK", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                })
                .show();
    }

    public static void showProgress(Activity act) {

        if (dialog != null && dialog.isShowing())
            return;

        dialog = new Dialog(act);
        dialog.getWindow().setBackgroundDrawableResource(R.color.colorProgressBackground);
        dialog.setContentView(R.layout.progress_bar_layout);
        dialog.setCancelable(false);
        act.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Showing Alert Message
                try {
                    if (dialog != null && !dialog.isShowing())
                        dialog.show();
                } catch (WindowManager.BadTokenException e) {
                    e.printStackTrace();
                } catch (RuntimeException e) {
                    e.printStackTrace();
                }
            }
        });
    }

//    public static boolean isNetworkAvailable(Context context) {
//        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
//        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
//    }

    public interface OnImageViewDismiss {
        void onPhotoDialogDismiss();
    }

    public static void dismissProgress() {
        try {
            if (dialog != null && dialog.isShowing())
                dialog.dismiss();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }


    public static void showSnackBar(View view, Activity act, String message) {
        Snackbar snackbar = Snackbar
                .make(view, message, Snackbar.LENGTH_LONG)
                .setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
        snackbar.setActionTextColor(ContextCompat.getColor(act, R.color.colorWhite));
        snackbar.show();
    }

    public static boolean compareDate(String examStartDate) {
        Date c = Calendar.getInstance().getTime();

        @SuppressLint("SimpleDateFormat") SimpleDateFormat fmt = new SimpleDateFormat("dd-MM-yyyy");
        String currDateStr = fmt.format(c);
        //  Log.e("CurrentData ", currDateStr);
        try {

            Date currentDate = fmt.parse(currDateStr);
            Date examDate = fmt.parse(examStartDate);
            assert currentDate != null;
            if (currentDate.compareTo(examDate) == 0) {
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static int compareTime(String examTime, String duration) {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat fmt = new SimpleDateFormat("hh:mm a");
        String currDateStr = fmt.format(c);

        try {
            //  Log.e("currDateStr", currDateStr);
            Date currentDate = fmt.parse(currDateStr);
            Date examStartTime = fmt.parse(examTime);
            //Log.e("examStartTime", examTime);
            //add duration
            Calendar cal = Calendar.getInstance();
            assert examStartTime != null;
            cal.setTime(examStartTime);
            cal.add(Calendar.MINUTE, Integer.parseInt(duration));
            String newExamEndTime = fmt.format(cal.getTime());
            //Log.e("End Time", newExamEndTime);
            Date examEndDate = fmt.parse(newExamEndTime);

            //Log.e("End Time", String.valueOf(currentDate.compareTo(examStartTime) >= 0));
            //Log.e("End Time", String.valueOf(examEndDate.compareTo(currentDate) < 0));
            //Log.e("End Time", String.valueOf(currentDate.compareTo(examStartTime) <= 0));
            //Log.e("End Time", String.valueOf(currentDate.compareTo(examEndDate) >= 0));
            assert currentDate != null;
            if (currentDate.compareTo(examStartTime) >= 0 && currentDate.compareTo(examEndDate) < 0) {
                return 1;
            } else if (currentDate.compareTo(examStartTime) <= 0) {
                return 2;
            } else if (currentDate.compareTo(examEndDate) >= 0) {
                return 3;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }


    public static void rateApp(Activity act) {
        Uri uri = Uri.parse("market://details?id=com.app.bespoke");// + act.getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);

        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            act.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            act.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=com.app.bespoke")));//+ act.getPackageName())));
        }
    }

    public static String convertFirstUpperWord(String str) {
        String[] strArr = str.split(" ");
        if (strArr != null && strArr.length != 0) {
            String returnedStr = "";
            for (int i = 0; i < strArr.length; i++) {
                strArr[i] = convertFirstUpperPerChar(strArr[i]);
                if (!returnedStr.isEmpty())
                    returnedStr = returnedStr + " " + strArr[i];
                else
                    returnedStr = returnedStr + strArr[i];
            }
            return returnedStr;
        } else
            return convertFirstUpper(str);

    }

    public static String convertFirstUpperPerChar(String str) {
        char[] arrayChar = str.toCharArray();
        if (arrayChar != null && arrayChar.length != 0) {
            String returnedStr = "";
            for (int i = 0; i < arrayChar.length; i++) {
                if (i == 0)
                    arrayChar[i] = Character.toUpperCase(arrayChar[i]);
                else
                    arrayChar[i] = Character.toUpperCase(arrayChar[i]);
                // arrayChar[i] = Character.toLowerCase(arrayChar[i]);

                returnedStr = returnedStr + arrayChar[i];
            }
            return returnedStr;
        }
        //Log("FirstLetter", str.substring(0, 1) + "    " + str.substring(1));
        if (str.length() != 0)
            return str.substring(0, 1).toUpperCase() + str.substring(1);

        return str;
    }

    public static String convertFirstUpper(String str) {

        if (str == null || str.isEmpty()) {
            return str;
        }
        //Log("FirstLetter", str.substring(0, 1) + "    " + str.substring(1));
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public static String createAppSpecificFolder(Activity act) {
        String rootPath = null;
        try {
            File directory = act.getDir("offline", Context.MODE_PRIVATE);
            rootPath = directory + "/video/";
            File root = new File(rootPath);
            if (!root.exists()) {
                root.mkdirs();
            }

            File f = new File(act.getFilesDir(), "mttext");
            f.createNewFile();

            FileOutputStream out = new FileOutputStream(f);
            //Log.e("PATH:  ", f.getAbsolutePath());
            out.flush();
            out.close();
            //Log.e("PATH:  ", rootPath);
            //Log.e("PATH:  ", Environment.getDataDirectory().getPath());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rootPath;
    }


    public static void shareTextUrl(Activity act, String str1) {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add data to the intent, the receiving app will decide
        // what to do with it.
        share.putExtra(Intent.EXTRA_SUBJECT, "The Prelims Master");
        //share.putExtra(Intent.EXTRA_TEXT, str1+"See on !!! \n\nhttps://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID);
        share.putExtra(Intent.EXTRA_TEXT, str1 + "\n\nhttps://play.google.com/store/apps/details?id=in.startv.hotstar&hl=en");
        act.startActivity(Intent.createChooser(share, "Share link!"));
    }

    public static void shareVideoUrl(Activity act, String str1) {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add data to the intent, the receiving app will decide
        // what to do with it.
        share.putExtra(Intent.EXTRA_SUBJECT, "The Prelims Master");
        //share.putExtra(Intent.EXTRA_TEXT, str1+"See on !!! \n\nhttps://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID);
        share.putExtra(Intent.EXTRA_TEXT, str1);
        act.startActivity(Intent.createChooser(share, "Share link!"));
    }


    // Can be triggered by a view event such as a button press
    public static void onShareItem(ImageView ivImage, Activity act, String str1) {
        // Get access to bitmap image from view

        // Get access to the URI for the bitmap
        Bitmap bmp;
        bmp = ((BitmapDrawable) ivImage.getDrawable()).getBitmap();


        Uri bmpUri = getBitmapFromDrawable(act, bmp);
        if (bmpUri != null) {
            // Construct a ShareIntent with link to image
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "The Prelims Master");
            shareIntent.putExtra(Intent.EXTRA_TEXT, str1);

            shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
            shareIntent.setType("image/*");
            // Launch sharing dialog for image
            act.startActivity(Intent.createChooser(shareIntent, "Share Image"));
        } else {
        }
    }

   /* // Returns the URI path to the Bitmap displayed in specified ImageView
    public static Uri getLocalBitmapUri(Activity act, ImageView imageView) {
        // Extract Bitmap from ImageView drawable
        Drawable drawable = imageView.getDrawable();
        Bitmap bmp = null;
        if (drawable instanceof BitmapDrawable){
            bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        } else {
            return null;
        }
        // Store image to default external storage directory
        Uri bmpUri = null;
        try {

            File file =  new File(act.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            // **Warning:** This will fail for API >= 24, use a FileProvider as shown below instead.
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }*/

    // Method when launching drawable within Glide
    public static Uri getBitmapFromDrawable(Activity act, Bitmap bmp) {

        // Store image to default external storage directory
        Uri bmpUri = null;
        try {

            File file = new File(act.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();


            bmpUri = FileProvider.getUriForFile(act, "com.app.thePrelimMaster.fileProvider", file);  // use this version for API >= 24

            // **Note:** For API < 24, you may use bmpUri = Uri.fromFile(file);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }


    public static void printHtmlTextBase(String text, TextView textView) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            textView.setText(Html.fromHtml(text, Html.TO_HTML_PARAGRAPH_LINES_INDIVIDUAL));
        } else {
            textView.setText(HtmlCompat.fromHtml(text, 0));
        }
    }

    public static void printHtmlTextAppend(String text, TextView textView) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            textView.append(Html.fromHtml(text, Html.TO_HTML_PARAGRAPH_LINES_INDIVIDUAL));
        } else {
            textView.append(HtmlCompat.fromHtml(text, 0));
        }
    }


}
