package com.example.myapplication.Common;

import static android.provider.Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.CallLog;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.multidex.BuildConfig;

import com.example.myapplication.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CodeReUse {

    public static final int CAMERA_INTENT = 101;
    public static final int GALLERY_INTENT = 102;
    public static final int ASK_PERMISSSION = 103;
    public static final int SELECT_VIDEO_GALLERY = 104;
    public static final int SELECT_VIDEO_CAMERA = 105;
    public static final int PDF_INTENT = 106;

    public static boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;

        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&*+=])(?=\\S+$).{4,}$";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }

    public static void slideEnter(Activity act) {
        act.overridePendingTransition(
                R.anim.slide_from_right,
                R.anim.slide_to_left
        );
    }

    public void RequestPermission(Activity thisActivity, String[] Permission, int Code) {
        if (ContextCompat.checkSelfPermission(thisActivity, Permission[0]) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(thisActivity, new String[]{Manifest.permission.READ_CALL_LOG}, 1);

        } else {
        }
    }

    private void getCallLogs(Activity act) {

        String[] projection = new String[]{
                CallLog.Calls.CACHED_NAME,
                CallLog.Calls.NUMBER,
                CallLog.Calls.TYPE,
                CallLog.Calls.DATE
        };
        String sortOrder = android.provider.CallLog.Calls.DATE + " DESC";
        Cursor managedCursor = act.getContentResolver().query(CallLog.Calls.CONTENT_URI, projection, null, null, sortOrder);
        if (managedCursor.getCount() > 0) {
            while (managedCursor.moveToNext()) {
                String name = managedCursor.getString(0);
                String numbers = managedCursor.getString(1);
            }
            managedCursor.close();
        }
    }

    private void getLastNumber(Activity act) {

        //this help you to get recent call
        Uri contacts = CallLog.Calls.CONTENT_URI;
        Cursor managedCursor = act.getContentResolver().query(contacts, null, null,
                null, android.provider.CallLog.Calls.DATE + " DESC limit 1;");
        int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
        int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
        int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
        int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);

        StringBuffer sb = new StringBuffer();
        managedCursor.moveToNext();
        String phNumber = managedCursor.getString(number);
        String callType = managedCursor.getString(type);
        String callDate = managedCursor.getString(date);
        String callDayTime = new Date(Long.valueOf(callDate)).toString();
        int callDuration = managedCursor.getInt(duration);
        managedCursor.close();

        int dircode = Integer.parseInt(callType);
        sb.append("Phone Number:--- " + phNumber + " ,Call Date:--- " + callDayTime + " ,Call duration in sec :--- " + callDuration);
        sb.append("\n----------------------------------");
        Log.e("calllogs", "getLastNumber: " + "Phone Number:--- " + phNumber + " ,Call Date:--- " + callDayTime + " ,Call duration in sec :--- " + callDuration);
    }

    public static void makeStatusBarTransparent(Activity act) {

        act.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        act.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            act.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        } else {
            act.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        act.getWindow().setStatusBarColor(act.getResources().getColor(R.color.Transperent));
        act.getWindow().getDecorView().setSystemUiVisibility(act.getWindow().getDecorView().getSystemUiVisibility());
    }

    private boolean requestAgain(Activity act) {

        if (ActivityCompat.checkSelfPermission(act, Arrays.toString(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE})) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(act, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, CodeReUse.ASK_PERMISSSION);
            return false;
        } else {
            return true;
        }
    }

    public static boolean whatsappInstalledOrNot(Activity activity) {
        PackageManager pm = activity.getPackageManager();
        boolean app_installed = false;
        try {
            pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            Toast.makeText(activity, "Whatsapp have not been installed.", Toast.LENGTH_SHORT).show();
            app_installed = false;
        }
        return app_installed;
    }

    public static void shareTextToWhatsapp(Activity act, String number, String message) {

        boolean isWhatsappInstalled = CodeReUse.whatsappInstalledOrNot(act);
        String toNumber = "";
        try {
            Intent intent;
            if (isWhatsappInstalled) {
//                if (number.startsWith("+")) {
//                    toNumber = number;
//                } else {
//                    toNumber = "91" + number;
//                }
                HELPER.print("Number", number);
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://api.whatsapp.com/send?phone=" + number + "&text=" + URLEncoder.encode(message, "UTF-8")));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setPackage("com.whatsapp");
            } else {
                Uri uri = Uri.parse("market://details?id=com.whatsapp");
                intent = new Intent(Intent.ACTION_VIEW, uri);
                Toast.makeText(act, "WhatsApp not Installed", Toast.LENGTH_SHORT).show();
            }
            act.startActivity(intent);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Permissions Check
    public static void verifyStoragePermission(Activity act, int RequestPermissionCode) {
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
                act.startActivity(intent);
            }
        } else {
            ActivityCompat.requestPermissions(act, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_CALL_LOG, Manifest.permission.READ_CONTACTS}, RequestPermissionCode);
        }
    }


    //send Pdf to Whatsapp
    public static void sendPdfToWhatsApp(Activity act, String number, Uri pdfUrl) {
        boolean isWhatsappInstalled = CodeReUse.whatsappInstalledOrNot(act);
        try {
            Intent sendIntent;
            if (isWhatsappInstalled) {

               // String toNumber = "91" + number;
                sendIntent = new Intent("android.intent.action.MAIN");
                sendIntent.putExtra(Intent.EXTRA_STREAM, pdfUrl);
                sendIntent.putExtra("jid", number + "@s.whatsapp.net");
                sendIntent.putExtra(Intent.EXTRA_TEXT, "");
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.setPackage("com.whatsapp");
                sendIntent.setType("application/pdf");
                sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                sendIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            } else {
                Uri uri = Uri.parse("market://details?id=com.whatsapp");
                sendIntent = new Intent(Intent.ACTION_VIEW, uri);
                Toast.makeText(act, "WhatsApp not Installed", Toast.LENGTH_SHORT).show();
            }
            act.startActivity(sendIntent);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showSnackBar(Activity act, View view, String message) {
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




    public static String getRealPathFromURIPath(Uri contentURI, Activity activity) {
        Cursor cursor = activity.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }

    public static String getRealPathFromURI(Activity activity, Uri contentURI) {
        String thePath = null;
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = activity.getContentResolver().query(contentURI, filePathColumn, null, null, null);
        if (cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            thePath = cursor.getString(columnIndex);
        }
        cursor.close();
        return thePath;
    }

    public static File createFileFromBitmap(Activity act, String fileName, Bitmap bitmap) {
        File file;
        file = new File(act.getCacheDir(), System.currentTimeMillis() + fileName);
        try {
            file.createNewFile();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bos);
            byte[] bitmapdata = bos.toByteArray();
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    //check is email is valid or not
    public static boolean isEmailValid(String email) {
        String expression = "^[\\w.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    //check is contact valid or not
    public static boolean isContactValid(String mobileNumber) {
        if (mobileNumber == null) {
            return false;
        } else if (mobileNumber.isEmpty()) {
            return false;
        } else if (mobileNumber.length() < 10) {
            return false;
        } else return mobileNumber.length() <= 10;
    }

    public static void hideKeyboard(Activity activity, View view) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);

        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    public static void setTimePickerDialog(Activity act, TextInputEditText editText) {

        TimePickerDialog picker;
        final Calendar cldr = Calendar.getInstance();
        int hour = cldr.get(Calendar.HOUR_OF_DAY);
        int minutes = cldr.get(Calendar.MINUTE);
        // time picker dialog
        picker = new TimePickerDialog(act,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                        int hour = sHour;
                        String timeSet;
                        if (hour > 12) {
                            hour -= 12;
                            timeSet = "PM";
                        } else if (hour == 0) {
                            hour += 12;
                            timeSet = "AM";
                        } else if (hour == 12) {
                            timeSet = "PM";
                        } else {
                            timeSet = "AM";
                        }

                        String min;
                        if (sMinute < 10)
                            min = "0" + sMinute;
                        else
                            min = String.valueOf(sMinute);

                        String aTime = String.valueOf(hour) + ':' + min + " " + timeSet;
                        Log.e("SELECT_TIME", aTime);
                        editText.setText(aTime);


                    }
                }, hour, minutes, false);

        picker.show();


    }

    public static void datePicker(Activity act, TextInputEditText editText) {

        DatePickerDialog picker;
        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);

        picker = new DatePickerDialog(act,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = (month + 1);
                        String dateStr = "";
                        String monthStr = "";
                        if (dayOfMonth < 10) {
                            dateStr = "0" + dayOfMonth;
                        } else {
                            dateStr = String.valueOf(dayOfMonth);
                        }

                        if (month < 10) {
                            monthStr = "0" + month;
                        } else {
                            monthStr = String.valueOf(month);
                        }
                        editText.setText(dateStr + "-" + monthStr + "-" + year);
                        Log.e("SELECT_DATE", dateStr + "-" + monthStr + "-" + year);
                    }

                }, year, month, day);

        picker.getDatePicker().setCalendarViewShown(false);
        picker.getDatePicker().setSpinnersShown(true);

        picker.show();
    }


    public static void activityBackPress(Activity act) {
        act.finish();
        //act.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void setWhiteNavigationBar(@NonNull Dialog dialog, Activity act) {
        Window window = dialog.getWindow();
        if (window != null) {
            DisplayMetrics metrics = new DisplayMetrics();
            window.getWindowManager().getDefaultDisplay().getMetrics(metrics);
            GradientDrawable dimDrawable = new GradientDrawable();
            // ...customize your dim effect here
            GradientDrawable navigationBarDrawable = new GradientDrawable();
            navigationBarDrawable.setShape(GradientDrawable.RECTANGLE);
            navigationBarDrawable.setColor(act.getColor(R.color.colorGray));
            // navigationBarDrawable.setTint(act.getColor(R.color.WhiteColor));
            Drawable[] layers = {dimDrawable, navigationBarDrawable};
            LayerDrawable windowBackground = new LayerDrawable(layers);
            windowBackground.setLayerInsetTop(1, metrics.heightPixels);
            window.setBackgroundDrawable(windowBackground);
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

    //get each word capitalize
    public static String capitalizeString(String str) {
        String[] words = str.split("\\s");
        StringBuilder capitalizeWord = new StringBuilder();
        for (String w : words) {
            String first = w.substring(0, 1);
            String afterfirst = w.substring(1);
            capitalizeWord.append(first.toUpperCase()).append(afterfirst).append(" ");
        }
        return capitalizeWord.toString().trim();
    }

    public static String getDateFromDateTime(boolean wantDate, String dateTimeStr) {
        //true  return date
        //false return time
        try {
            //DateFormat f = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
            DateFormat f = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date d = f.parse(dateTimeStr);
            DateFormat date = new SimpleDateFormat("dd-MM-yyyy");
            DateFormat time = new SimpleDateFormat("hh:mm:ss a");


            if (wantDate)
                return date.format(d);
            else
                return time.format(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static Uri getUri(Activity act, File file) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return FileProvider.getUriForFile(act, BuildConfig.APPLICATION_ID + ".fileProvider", file);
        } else {
            return Uri.fromFile(file);
        }
    }

    public static Intent makeCall(Activity act, String contactNumber) {
        Intent returnDate = new Intent();
        returnDate.putExtra("mobileNumber", contactNumber);
        Intent intent = new Intent(Intent.ACTION_CALL);

        intent.setData(Uri.parse("tel:" + contactNumber));
        if (ActivityCompat.checkSelfPermission(act, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            returnDate.putExtra("returnCode", -1);
            return returnDate;
        } else {
            returnDate.putExtra("returnCode", 1);
        }
        act.startActivity(intent);
        return returnDate;
    }

    public static String getFilenameFromURL(String url) {
        if (url == null) {
            return "";
        }
        try {
            URL resource = new URL(url);
            String host = resource.getHost();
            if (host.length() > 0 && url.endsWith(host)) {
                // handle ...example.com
                return "";
            }
        } catch (MalformedURLException e) {
            return "";
        }

        int startIndex = url.lastIndexOf('/') + 1;
        int length = url.length();

        // find end index for ?
        int lastQMPos = url.lastIndexOf('?');
        if (lastQMPos == -1) {
            lastQMPos = length;
        }

        // find end index for #
        int lastHashPos = url.lastIndexOf('#');
        if (lastHashPos == -1) {
            lastHashPos = length;
        }

        // calculate the end index
        int endIndex = Math.min(lastQMPos, lastHashPos);
        return url.substring(startIndex, endIndex);
    }

}
