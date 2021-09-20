package com.example.schoolNeedsOrganizer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.drawable.IconCompat;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.exercises.R;
import com.example.schoolNeedsOrganizer.AppSettings.MyNotificationPublisher;
import com.example.schoolNeedsOrganizer.SchoolNeeds.HomeWork;
import com.example.schoolNeedsOrganizer.SchoolNeeds.Test;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TreeMap;

public class Settings extends AppCompatActivity {

    private ConstraintLayout navigation, tutor;
    private ImageView topBanner, settings, addButton, homeButton, listButton, strelka4;
    private ScrollView mainScroll;
    private int screenWidth, screenHeight;
    private TextView settingsText, notificationSwitchText, notificationTimeText, setText, thanks;
    public static final String APP_PREFERENCES = "mysettings";
    private SharedPreferences mSettings;
    private Switch notificationControl;
    private EditText editTextTime;
    private LinearLayout notificationTimeLayout;
    private Button saveButton3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        boolean firstEnterInApp = mSettings.getBoolean("firstEnter", true);
        findElements();
        getScreenSize();
        viewSettings();
        getSettings();
        if (firstEnterInApp) {
            startTutorial();
            SharedPreferences.Editor editor = mSettings.edit();
            editor.putBoolean("firstEnter", false);
            editor.apply();
        } else {
            tutor.setVisibility(View.INVISIBLE);
            setText.setVisibility(View.INVISIBLE);
            strelka4.setVisibility(View.INVISIBLE);
            thanks.setVisibility(View.INVISIBLE);
            playAnimations();
            setOnClickActions();
        }
    }


    private void startTutorial() {
        thanks.setVisibility(View.INVISIBLE);
        strelka4.setVisibility(View.INVISIBLE);
        setText.setVisibility(View.INVISIBLE);
        Animation appearance = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.appearance_anim);
        playAnimations();
        int[] stage = new int[2];
        tutor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stage[0] == 0) {
                    stage[0]++;
                    setText.setTypeface(StaticUtils.getTypeFace(getApplicationContext()));
                    setText.setTextSize((float) (screenWidth * 0.02));
                    setText.startAnimation(appearance);
                    setText.setVisibility(View.VISIBLE);
                    strelka4.startAnimation(appearance);
                    strelka4.setVisibility(View.VISIBLE);
                } else if (stage[0] == 1) {
                    stage[0]++;
                    strelka4.clearAnimation();
                    setText.clearAnimation();
                    strelka4.setVisibility(View.INVISIBLE);
                    setText.setVisibility(View.INVISIBLE);
                    thanks.setTypeface(StaticUtils.getTypeFace(getApplicationContext()));
                    thanks.setTextSize((float) (screenWidth * 0.02));
                    thanks.startAnimation(appearance);
                    thanks.setVisibility(View.VISIBLE);
                } else if (stage[0] == 2) {
                    Intent intent = new Intent("android.intent.action.SCREEN");
                    startActivity(intent);
                }
            }
        });

    }

    private void findElements() {
        settingsText = findViewById(R.id.settingsText);
        navigation = findViewById(R.id.navigation);
        topBanner = findViewById(R.id.topBanner);
        mainScroll = findViewById(R.id.mainPanel);
        settings = findViewById(R.id.settings);
        addButton = findViewById(R.id.addButton);
        homeButton = findViewById(R.id.homeButton);
        listButton = findViewById(R.id.listButton);
        notificationTimeText = findViewById(R.id.notificationTimeText);
        notificationSwitchText = findViewById(R.id.notificationSwitchText);
        notificationControl = findViewById(R.id.notificationControl);
        editTextTime = findViewById(R.id.editTextTime);
        notificationTimeLayout = findViewById(R.id.notificationTimeLayout);
        saveButton3 = findViewById(R.id.saveButton3);
        setText = findViewById(R.id.setText);
        tutor = findViewById(R.id.tutor);
        strelka4 = findViewById(R.id.strelka4);
        thanks = findViewById(R.id.thanks);
    }

    private void playAnimations() {
        Animation appearance = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.appearance_anim);
        navigation.startAnimation(appearance);
        topBanner.startAnimation(appearance);
        mainScroll.startAnimation(appearance);
        settings.startAnimation(appearance);
    }

    private void getScreenSize() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenHeight = size.y;
        screenWidth = size.x;
    }

    private void viewSettings() {
        actionBarAndStatusBarSettings();
        centralLayoutSettings();
        setFont();
    }

    private void actionBarAndStatusBarSettings() {
        getSupportActionBar().hide();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.main_color));
        }
    }

    private void centralLayoutSettings() {
        android.view.ViewGroup.LayoutParams layoutParams = mainScroll.getLayoutParams();
        layoutParams.height = (int) (screenHeight * 0.75);
        mainScroll.setLayoutParams(layoutParams);
    }

    private void setFont() {
        settingsText.setTypeface(StaticUtils.getTypeFace(getApplicationContext()));
        settingsText.setTextSize((float) (screenWidth * 0.02));
        notificationTimeText.setTypeface(StaticUtils.getTypeFace(getApplicationContext()));
        notificationTimeText.setTextSize((float) (screenWidth * 0.02));
        notificationSwitchText.setTypeface(StaticUtils.getTypeFace(getApplicationContext()));
        notificationSwitchText.setTextSize((float) (screenWidth * 0.02));
    }

    private void setOnClickActions() {
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("android.intent.action.ADD");
                startActivity(intent);
            }
        });
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("android.intent.action.SCREEN");
                startActivity(intent);
            }
        });
        listButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("android.intent.action.LIST");
                startActivity(intent);
            }
        });
        notificationControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                com.example.schoolNeedsOrganizer.AppSettings.Settings.setNotificationsStatus(notificationControl.isChecked(), getApplicationContext());
                if (!notificationControl.isChecked()) {
                    notificationTimeLayout.setAlpha((float) 0.5);
                    editTextTime.setClickable(false);
                    editTextTime.setCursorVisible(false);
                    editTextTime.setFocusable(false);
                    editTextTime.setFocusableInTouchMode(false);
                } else {
                    notificationTimeLayout.setAlpha((float) 1);
                    editTextTime.setClickable(true);
                    editTextTime.setCursorVisible(true);
                    editTextTime.setFocusable(true);
                    editTextTime.setFocusableInTouchMode(true);
                }
            }
        });
        saveButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                com.example.schoolNeedsOrganizer.AppSettings.Settings.setNotificationsStatus(notificationControl.isChecked(), getApplicationContext());
                if (notificationControl.isChecked()) {
                    String time = editTextTime.getText().toString();
                    int hourSetter = Integer.valueOf(time.split(":")[0]);
                    if ((hourSetter > 24) || (hourSetter < 0)) {
                        showAlertDialog("Неверные данные!", "Неправильно введено время!");
                    } else {
                        int minuteSetter = Integer.valueOf(time.split(":")[1]);
                        if ((minuteSetter > 60) || (minuteSetter < 0)) {
                            showAlertDialog("Неверные данные!", "Неправильно введено время!");
                        } else {
                            com.example.schoolNeedsOrganizer.AppSettings.Settings.setTime(editTextTime.getText().toString(), getApplicationContext());
                            scheduleNotification(127);
                            Toast toast = Toast.makeText(getApplicationContext(), "Настройки сохранены", Toast.LENGTH_LONG);
                            toast.show();
                        }
                    }
                }
            }
        });
    }

    public void scheduleNotification(int notificationId) {
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        String time = com.example.schoolNeedsOrganizer.AppSettings.Settings.getTime();
        int hourSetter = Integer.valueOf(time.split(":")[0]);
        int minuteSetter = Integer.valueOf(time.split(":")[1]);
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        if ((hour > hourSetter) || (hour == hourSetter && calendar.get(Calendar.MINUTE) >= minuteSetter)) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        calendar.set(Calendar.HOUR_OF_DAY, hourSetter);
        calendar.set(Calendar.MINUTE, minuteSetter);
        calendar.set(Calendar.SECOND, 0);
        AlarmManager mAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("SNO", "SchoolNeedsOrganize",
                    NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("SchoolNeedsOrganizer");
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            channel.enableVibration(true);
            notificationManager.createNotificationChannel(channel);
        }
        Notification.Builder builder = new Notification.Builder(getApplicationContext(), "SNO");
        Intent intent = new Intent("android.intent.action.SCREEN");
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.icon, 10)
                .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.icon))
                .setTicker("Привет! Проверь уведомление!")
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setBadgeIconType(Icon.CONTENTS_FILE_DESCRIPTOR)
                .setContentText("Не забудь выполнить все запланированные на сегодня дела!")
                .setContentTitle("Напоминание");
        Date date = calendar.getTime();
        System.out.println(date.toString());
        Notification notification = builder.build();
        Intent nIntent = new Intent(getApplicationContext(), MyNotificationPublisher.class);
        nIntent.putExtra(MyNotificationPublisher.NOTIFICATION_ID, notificationId);
        nIntent.putExtra(MyNotificationPublisher.NOTIFICATION, notification);
        PendingIntent pendingIntentSecond = PendingIntent.getBroadcast(getApplicationContext(), 0, nIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mAlarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntentSecond);
    }


    private void showAlertDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(true);
        builder.setPositiveButton(
                "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }

    private void saveStatement() throws IOException {
        JSONArray array = new JSONArray();
        TreeMap<String, HomeWork> homeWorkTreeMap = HomeWork.getHWTreeSet();
        for (String key : homeWorkTreeMap.keySet()) {
            HomeWork work = homeWorkTreeMap.get(key);

            org.json.simple.JSONObject need = new org.json.simple.JSONObject();
            String[] preDate = work.getImplementationDate().getTime().toString().split(" ");
            Date date = work.getImplementationDate().getTime();
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            String date2 = preDate[2] + "." + (cal.get(Calendar.MONTH) + 1) + "." + preDate[5];
            need.put("test", false);
            need.put("importance", work.isImportant());
            need.put("date", date2);
            need.put("description", work.getNeedDescription().trim());
            need.put("name", work.getNeedName().trim());
            need.put("end", work.isEnded());
            array.add(need);

        }
        TreeMap<String, Test> testTreeMap = Test.getTestTreeSet();
        for (String key : testTreeMap.keySet()) {
            org.json.simple.JSONObject need = new org.json.simple.JSONObject();
            Test work = testTreeMap.get(key);
            String[] preDate = work.getImplementationDate().getTime().toString().split(" ");
            Date date = work.getImplementationDate().getTime();
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            String date2 = preDate[2] + "." + (cal.get(Calendar.MONTH) + 1) + "." + preDate[5];
            need.put("test", true);
            need.put("importance", work.isImportant());
            need.put("date", date2);
            need.put("description", work.getNeedDescription().trim());
            need.put("name", work.getNeedName().trim());
            need.put("end", work.isEnded());
            array.add(need);
        }
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonElement je = JsonParser.parseString(array.toJSONString());
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString("NeedArray", gson.toJson(je));
        editor.apply();
    }

    private void getSettings() {
        boolean nStatus = com.example.schoolNeedsOrganizer.AppSettings.Settings.isNotificationsStatus();
        String time = com.example.schoolNeedsOrganizer.AppSettings.Settings.getTime();
        if (nStatus) {
            notificationControl.setChecked(true);
            editTextTime.setText(time);
        } else {
            editTextTime.setText(time);
            notificationControl.setChecked(false);
            notificationTimeLayout.setAlpha((float) 0.5);
            editTextTime.setClickable(false);
            editTextTime.setCursorVisible(false);
            editTextTime.setFocusable(false);
            editTextTime.setFocusableInTouchMode(false);
        }
    }

    @Override
    protected void onPause() {
        try {
            saveStatement();
            com.example.schoolNeedsOrganizer.AppSettings.Settings.saveSettings(getApplicationContext());
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startingNeeds();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        startingNeeds();
    }

    private void startingNeeds() {
        HomeWork.maidTreeMap();
        Test.maidTreeMap();
        String needsForAdd = mSettings.getString("NeedArray", "");
        try {
            intializationOfNeeds(needsForAdd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        HomeWork.addHomeWorkForTomorrow();
    }

    private static void intializationOfNeeds(String needs) throws ParseException, JSONException {

        try {
            JSONParser jsonParser = new JSONParser();
            JSONArray jsonArray = (JSONArray) jsonParser.parse(needs);
            jsonArray.forEach(need -> {
                try {

                    JSONObject needObject = (JSONObject) need;
                    boolean test = (boolean) needObject.get("test");
                    if (test) {
                        String name = (String) needObject.get("name");
                        String description = (String) needObject.get("description");
                        String date = (String) needObject.get("date");
                        boolean end = (boolean) needObject.get("end");
                        addToTest(name, description, date, end);
                    } else {
                        String name = (String) needObject.get("name");
                        String description = (String) needObject.get("description");
                        String date = (String) needObject.get("date");
                        boolean importance = (boolean) needObject.get("importance");
                        boolean end = (boolean) needObject.get("end");
                        addHomeWork(name, description, date, importance, end);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {

        }
    }

    private static void addToTest(String name, String description, String date, boolean end) {
        Test test = new Test();
        test.setNeedName(name);
        test.setEnded(end);
        test.setNeedDescription(description.trim());
        String[] elementsOfDate = date.trim().split("\\.");
        int mounth = Integer.parseInt(fitsZiro(elementsOfDate[1])) - 1;
        int day = Integer.parseInt(elementsOfDate[0]);
        int year = Integer.parseInt(elementsOfDate[2]);
        Calendar cal = new GregorianCalendar(year, mounth, day);
        test.setImplementationDate(cal);
        String status = getStatus(cal);
        test.setStatus(status);
        test.setImportant(true);
        Test.setTestTreeSet(test, name);
    }

    private static void addHomeWork(String name, String description, String date, boolean importance, boolean end) {
        HomeWork homeWork = new HomeWork();
        homeWork.setNeedName(name);
        homeWork.setEnded(end);
        homeWork.setNeedDescription(description.trim());
        String[] elementsOfDate = date.trim().split("\\.");
        int mounth = Integer.parseInt(fitsZiro(elementsOfDate[1])) - 1;
        String day1 = fitsZiro(elementsOfDate[0]);
        int day = Integer.parseInt(day1);
        int year = Integer.parseInt(elementsOfDate[2]);
        Calendar cal = new GregorianCalendar(year, mounth, day);
        homeWork.setImplementationDate(cal);
        homeWork.setImportant(importance);
        String status = getStatus(cal);
        homeWork.setStatus(status);
        HomeWork.setHWTreeSet(homeWork, name);
    }
    private static String fitsZiro(String s) {
        int i = s.indexOf("0") + 1;
        if (i == 1) {
            return s.substring(1);
        } else return s;
    }

    private static String getStatus(Calendar cal) {
        Calendar calendar = Calendar.getInstance();
        if (cal.before(calendar)) {
            return "Просрочено";
        } else {
            return "Активно";
        }
    }
}