package com.example.schoolNeedsOrganizer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;

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
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.InputType;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.exercises.R;
import com.example.schoolNeedsOrganizer.AppSettings.MyNotificationPublisher;
import com.example.schoolNeedsOrganizer.AppSettings.Settings;
import com.example.schoolNeedsOrganizer.SchoolNeeds.HomeWork;
import com.example.schoolNeedsOrganizer.SchoolNeeds.Need;
import com.example.schoolNeedsOrganizer.SchoolNeeds.Test;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.joda.time.DateTime;
import org.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TreeMap;

public class MainScreen extends AppCompatActivity {

    private ConstraintLayout navigation, mainLayout, mainPanel, nameLayout, dateLayout, descriptionLayout, tutor, table;
    private ImageView topBanner, settings, addButton, homeButton, listButton, nothingIsAdded, rightArrow, leftArrow, strelka, tableImage;
    private String[] spinnerItems = {"Тесты", "Домашнее на завтра"};
    private String[] dayOfWeek = {"Понедельник", "Вторник", "Среда", "Четверг", "Пятница", "Суббота", "Воскресенье"};
    private Spinner itemSpinner, weekDay;
    private int weekDayNumber = -1;
    private int screenWidth, screenHeight;
    private long count = 0;
    public static final String APP_PREFERENCES = "mysettings";
    private SharedPreferences mSettings;
    private TextView settingsText, needName, getNeedName, needDate, getNeedDate, description, getDescription, label, hello, tutorial, desc,
            first, second, third, fouth, fith, six, seventh, eight;
    private String animCaller;
    private EditText firstEdit, firstEdit2, firstEdit3, firstEdit4, firstEdit5, firstEdit6, firstEdit7, firstEdit8;
    private static boolean isOpened;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        boolean firstEnterInApp = mSettings.getBoolean("firstEnter", true);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_main_screen);
        findElements();
        getScreenSize();
        startingNeeds();
        viewSettings();
        if (firstEnterInApp) {
            startTutorial();
        } else {
            tutor.setVisibility(View.INVISIBLE);
            hello.setVisibility(View.INVISIBLE);
            tutorial.setVisibility(View.INVISIBLE);
            desc.setVisibility(View.INVISIBLE);
            strelka.setVisibility(View.INVISIBLE);
            playAnimations();
            setOnClickActions();
            formattingMainScreen();
            correctMAinPanelStyle();
            makingTable(-1);
        }
    }

    private void findElements() {
        mainLayout = findViewById(R.id.mainLayout);
        navigation = findViewById(R.id.navigation);
        topBanner = findViewById(R.id.topBanner);
        mainPanel = findViewById(R.id.mainPanel);
        itemSpinner = findViewById(R.id.itemSpinner);
        settings = findViewById(R.id.settings);
        addButton = findViewById(R.id.addButton);
        homeButton = findViewById(R.id.homeButton);
        listButton = findViewById(R.id.listButton);
        nothingIsAdded = findViewById(R.id.nothingIsAdded);
        settingsText = findViewById(R.id.settingsText);
        getNeedName = findViewById(R.id.getNeedName);
        needName = findViewById(R.id.needName);
        needDate = findViewById(R.id.needDate);
        getNeedDate = findViewById(R.id.getNeedDate);
        description = findViewById(R.id.description);
        getDescription = findViewById(R.id.getDescription);
        nameLayout = findViewById(R.id.nameLayout);
        dateLayout = findViewById(R.id.dateLayout);
        descriptionLayout = findViewById(R.id.descriptionLayout);
        rightArrow = findViewById(R.id.rightArrow);
        leftArrow = findViewById(R.id.leftArrow);
        label = findViewById(R.id.label);
        tutor = findViewById(R.id.tutor);
        hello = findViewById(R.id.hello);
        tutorial = findViewById(R.id.tutorial);
        strelka = findViewById(R.id.strelka);
        desc = findViewById(R.id.desc);
        table = findViewById(R.id.table);

        first = findViewById(R.id.first);
        second = findViewById(R.id.second);
        third = findViewById(R.id.third);
        fouth = findViewById(R.id.fouth);
        fith = findViewById(R.id.fith);
        six = findViewById(R.id.six);
        seventh = findViewById(R.id.seventh);
        eight = findViewById(R.id.eight);
        tableImage = findViewById(R.id.tableImage);

        firstEdit = findViewById(R.id.firstEdit);
        firstEdit2 = findViewById(R.id.firstEdit2);
        firstEdit3 = findViewById(R.id.firstEdit3);
        firstEdit4 = findViewById(R.id.firstEdit4);
        firstEdit5 = findViewById(R.id.firstEdit5);
        firstEdit6 = findViewById(R.id.firstEdit6);
        firstEdit7 = findViewById(R.id.firstEdit7);
        firstEdit8 = findViewById(R.id.firstEdit8);
        weekDay = findViewById(R.id.weekDay);
    }

    private void startTutorial() {
        table.setVisibility(View.INVISIBLE);
        hello.setVisibility(View.INVISIBLE);
        tutorial.setVisibility(View.INVISIBLE);
        strelka.setVisibility(View.INVISIBLE);
        desc.setVisibility(View.INVISIBLE);
        Animation appearance = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.appearance_anim);
        playAnimations();
        int[] stage = new int[2];
        tutor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stage[0] == 0) {
                    stage[0]++;
                    hello.setTypeface(StaticUtils.getTypeFace(getApplicationContext()));
                    hello.setTextSize((float) (screenWidth * 0.02));
                    hello.startAnimation(appearance);
                    hello.setVisibility(View.VISIBLE);
                } else if (stage[0] == 1) {
                    stage[0]++;
                    hello.clearAnimation();
                    hello.setVisibility(View.INVISIBLE);
                    tutorial.setTypeface(StaticUtils.getTypeFace(getApplicationContext()));
                    tutorial.setTextSize((float) (screenWidth * 0.02));
                    tutorial.startAnimation(appearance);
                    tutorial.setVisibility(View.VISIBLE);
                } else if (stage[0] == 2) {
                    stage[0]++;
                    tutorial.clearAnimation();
                    tutorial.setVisibility(View.INVISIBLE);
                    desc.setTypeface(StaticUtils.getTypeFace(getApplicationContext()));
                    desc.setTextSize((float) (screenWidth * 0.02));
                    desc.startAnimation(appearance);
                    desc.setVisibility(View.VISIBLE);
                    strelka.startAnimation(appearance);
                    strelka.setVisibility(View.VISIBLE);
                } else if (stage[0] == 3) {
                    Intent intent = new Intent("android.intent.action.ADD");
                    startActivity(intent);
                }
            }
        });

    }

    private void playAnimations() {
        Animation appearance = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.appearance_anim);
        navigation.startAnimation(appearance);
        topBanner.startAnimation(appearance);
        itemSpinner.startAnimation(appearance);
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
        spinnerSettings();
    }

    private void actionBarAndStatusBarSettings() {
        getSupportActionBar().hide();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.main_color));
        }
    }

    private void centralLayoutSettings() {
        android.view.ViewGroup.LayoutParams layoutParams = mainPanel.getLayoutParams();
        layoutParams.height = (int) (screenHeight * 0.75);
        layoutParams.width = (int) (screenWidth * 0.9);
        mainPanel.setLayoutParams(layoutParams);
    }

    private void spinnerSettings() {
        spinnerSettingsTable();
        ArrayAdapter<String> spinnerItemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerItems) {
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTypeface(StaticUtils.getTypeFace(getApplicationContext()));//Typeface for normal view
                ((TextView) v).setTextSize(20);
                return v;
            }

            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                ((TextView) v).setTypeface(StaticUtils.getTypeFace(getApplicationContext()));//Typeface for dropdown
                ((TextView) v).setBackgroundColor(Color.parseColor("#BBfef3da"));
                ((TextView) v).setTextSize(20);
                return v;
            }
        };
        spinnerItemsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        itemSpinner.setAdapter(spinnerItemsAdapter);
    }

    private void spinnerSettingsTable() {
        ArrayAdapter<String> spinnerItemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dayOfWeek) {
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTypeface(StaticUtils.getTypeFace(getApplicationContext()));//Typeface for normal view
                ((TextView) v).setTextSize(20);
                return v;
            }

            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                ((TextView) v).setTypeface(StaticUtils.getTypeFace(getApplicationContext()));//Typeface for dropdown view
                ((TextView) v).setBackgroundColor(Color.parseColor("#BBfef3da"));
                ((TextView) v).setTextSize(20);
                return v;
            }
        };
        spinnerItemsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        weekDay.setAdapter(spinnerItemsAdapter);
    }

    private void setOnClickActions() {
        isOpened = false;
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("android.intent.action.SETTINGS");
                startActivity(intent);
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("android.intent.action.ADD");
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

        itemSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                formattingMainScreen();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        weekDay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TreeMap<Integer, String> treeMap = Settings.getWeekDays();
                StringBuilder builder = new StringBuilder();
                boolean error = false;
                for (int i = 0; i < 8; i++) {
                    EditText text = returnEditText(i);
                    error = text.getText().toString().contains(")");
                    if (!text.getText().toString().equals(""))
                        builder.append(i + 1 + ". " + text.getText() + ") ");
                }
                if (error) {
                    showAlertDialog("Неверные данные!", "Пожалуйста, не используйте симовол - \')\', а так же его открывающийся аналог, для корректной работы. Спасибо :)");
                } else {
                    treeMap.put(weekDayNumber, builder.toString());
                    Settings.setWeekDays(treeMap, getApplicationContext());
                    weekDayNumber = position;
                    makingTable(weekDayNumber);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        leftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String item = itemSpinner.getSelectedItem().toString();
                if (item.equals("Тесты")) {
                    Test.addToCurrentTest(-1);
                    fillMainPanel(true);
                } else {
                    HomeWork.addToCurrentWork(-1);
                    fillMainPanel(false);
                }
            }
        });
        rightArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String item = itemSpinner.getSelectedItem().toString();
                if (item.equals("Тесты")) {
                    Test.addToCurrentTest(1);
                    fillMainPanel(true);
                } else {
                    HomeWork.addToCurrentWork(1);
                    fillMainPanel(false);
                }
            }
        });
        tableImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOpened) {
                    TreeMap<Integer, String> treeMap = Settings.getWeekDays();
                    StringBuilder builder = new StringBuilder();
                    boolean error = false;
                    for (int i = 0; i < 8; i++) {
                        EditText text = returnEditText(i);
                        error = text.getText().toString().contains(")");
                        if (!text.getText().toString().equals(""))
                            builder.append(i + 1 + ". " + text.getText() + ") ");
                    }
                    if (error) {
                        showAlertDialog("Неверные данные!", "Пожалуйста, не используйте симовол - \')\', а так же его открывающийся аналог, для корректной работы. Спасибо :)");
                    } else {
                        treeMap.put(weekDayNumber, builder.toString());
                        Settings.setWeekDays(treeMap, getApplicationContext());
                        Animation appearance = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.table);
                        table.setAnimation(appearance);
                        table.setVisibility(View.INVISIBLE);
                        tableImage.setAlpha((float) 0.7);
                        isOpened = false;
                    }
                } else {
                    makingTable(-1);
                    Animation appearance = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.appearance_anim);
                    table.setVisibility(View.VISIBLE);
                    table.setAnimation(appearance);
                    tableImage.setAlpha((float) 1.0);
                    isOpened = true;
                }
            }
        });
        mainPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOpened) {
                    TreeMap<Integer, String> treeMap = Settings.getWeekDays();
                    StringBuilder builder = new StringBuilder();
                    boolean error = false;
                    for (int i = 0; i < 8; i++) {
                        EditText text = returnEditText(i);
                        error = text.getText().toString().contains(")");
                        if (!text.getText().toString().equals(""))
                            builder.append(i + 1 + ". " + text.getText() + ") ");
                    }
                    if (error) {
                        showAlertDialog("Неверные данные!", "Пожалуйста, не используйте симовол - \')\', а так же его открывающийся аналог, для корректной работы. Спасибо :)");
                    } else {
                        treeMap.put(weekDayNumber, builder.toString());
                        Settings.setWeekDays(treeMap, getApplicationContext());
                        Animation appearance = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.table);
                        table.setAnimation(appearance);
                        table.setVisibility(View.INVISIBLE);
                        tableImage.setAlpha((float) 0.7);
                        isOpened = false;
                    }
                }
            }
        });
        nothingIsAdded.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOpened) {
                    TreeMap<Integer, String> treeMap = Settings.getWeekDays();
                    StringBuilder builder = new StringBuilder();
                    boolean error = false;
                    for (int i = 0; i < 8; i++) {
                        EditText text = returnEditText(i);
                        error = text.getText().toString().contains(")");
                        if (!text.getText().toString().equals(""))
                            builder.append(i + 1 + ". " + text.getText() + ") ");
                    }
                    if (error) {
                        showAlertDialog("Неверные данные!", "Пожалуйста, не используйте симовол - \')\', а так же его открывающийся аналог, для корректной работы. Спасибо :)");
                    } else {
                        treeMap.put(weekDayNumber, builder.toString());
                        Settings.setWeekDays(treeMap, getApplicationContext());
                        Animation appearance = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.table);
                        table.setAnimation(appearance);
                        table.setVisibility(View.INVISIBLE);
                        tableImage.setAlpha((float) 0.7);
                        isOpened = false;
                    }
                }
            }
        });
        mainPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOpened) {
                    TreeMap<Integer, String> treeMap = Settings.getWeekDays();
                    StringBuilder builder = new StringBuilder();
                    boolean error = false;
                    for (int i = 0; i < 8; i++) {
                        EditText text = returnEditText(i);
                        error = text.getText().toString().contains(")");
                        if (!text.getText().toString().equals(""))
                            builder.append(i + 1 + ". " + text.getText() + ") ");
                    }
                    if (error) {
                        showAlertDialog("Неверные данные!", "Пожалуйста, не используйте симовол - \')\', а так же его открывающийся аналог, для корректной работы. Спасибо :)");
                    } else {
                        treeMap.put(weekDayNumber, builder.toString());
                        Settings.setWeekDays(treeMap, getApplicationContext());
                        Animation appearance = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.table);
                        table.setAnimation(appearance);
                        table.setVisibility(View.INVISIBLE);
                        tableImage.setAlpha((float) 0.7);
                        isOpened = false;
                    }
                }
            }
        });
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

    @Override
    public void onBackPressed() {
        if(isOpened){
            TreeMap<Integer, String> treeMap = Settings.getWeekDays();
            StringBuilder builder = new StringBuilder();
            boolean error = false;
            for (int i = 0; i < 8; i++) {
                EditText text = returnEditText(i);
                error = text.getText().toString().contains(")");
                if (!text.getText().toString().equals(""))
                    builder.append(i + 1 + ". " + text.getText() + ") ");
            }
            if (error) {
                showAlertDialog("Неверные данные!", "Пожалуйста, не используйте симовол - \')\', а так же его открывающийся аналог, для корректной работы. Спасибо :)");
            } else {
                treeMap.put(weekDayNumber, builder.toString());
                Settings.setWeekDays(treeMap, getApplicationContext());
                Animation appearance = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.table);
                table.setAnimation(appearance);
                table.setVisibility(View.INVISIBLE);
                tableImage.setAlpha((float) 0.7);
                isOpened = false;
            }
        } else
        new AlertDialog.Builder(this)
                .setTitle("Выйти из приложения?")
                .setMessage("Вы действительно хотите выйти?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        moveTaskToBack(true);
                        android.os.Process.killProcess(android.os.Process.myPid());
                        System.exit(1);
                    }
                })
                .create()
                .show();
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

    @Override
    protected void onStart() {
        super.onStart();
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

    private void formattingMainScreen() {
        table.setVisibility(View.INVISIBLE);
        String item = itemSpinner.getSelectedItem().toString();
        if (item.equals("Тесты")) {
            formattingTestsScreen();
        } else {
            formattingHomeWorkScreen();
        }
    }

    private void formattingTestsScreen() {
        mainPanel.setVisibility(View.INVISIBLE);
        nothingIsAdded.setVisibility(View.INVISIBLE);
        Animation appearance = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.appearance_anim);
        appearance.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (animCaller.equals("Panel")) {
                    mainPanel.setVisibility(View.VISIBLE);
                } else {
                    nothingIsAdded.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        if (Test.getTestTreeSet().size() != 0) {
            nothingIsAdded.setVisibility(View.INVISIBLE);
            label.setText("Тест");
            animCaller = "Panel";
            mainPanel.startAnimation(appearance);
            if (Test.getTestTreeSet().size() < 2) {
                leftArrow.setVisibility(View.INVISIBLE);
                rightArrow.setVisibility(View.INVISIBLE);
            } else {
                leftArrow.setVisibility(View.VISIBLE);
                rightArrow.setVisibility(View.VISIBLE);
            }
            fillMainPanel(true);
        } else {
            animCaller = "Img";
            mainPanel.setVisibility(View.INVISIBLE);
            nothingIsAdded.startAnimation(appearance);
        }
    }

    private void formattingHomeWorkScreen() {
        mainPanel.setVisibility(View.INVISIBLE);
        nothingIsAdded.setVisibility(View.INVISIBLE);
        Animation appearance = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.appearance_anim);
        appearance.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (animCaller.equals("Panel")) {
                    mainPanel.setVisibility(View.VISIBLE);
                } else {
                    nothingIsAdded.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        if (HomeWork.getForTomorrow().size() != 0) {
            TreeMap<String, HomeWork> map = HomeWork.getForTomorrow();
            nothingIsAdded.setVisibility(View.INVISIBLE);
            label.setText("Домашняя работа");
            animCaller = "Panel";
            mainPanel.startAnimation(appearance);
            if (HomeWork.getForTomorrow().size() < 2) {
                leftArrow.setVisibility(View.INVISIBLE);
                rightArrow.setVisibility(View.INVISIBLE);
            }else {
                leftArrow.setVisibility(View.VISIBLE);
                rightArrow.setVisibility(View.VISIBLE);
            }
            fillMainPanel(false);
        } else {
            animCaller = "Img";
            mainPanel.setVisibility(View.INVISIBLE);
            nothingIsAdded.startAnimation(appearance);
        }
        getNeedName.setKeyListener(null);
    }

    private void fillMainPanel(boolean isTest) {
        ArrayList<com.example.schoolNeedsOrganizer.SchoolNeeds.Need> needList = new ArrayList<>();
        Need need;
        if (isTest) {
            TreeMap<String, Test> needs = Test.getTestTreeSet();
            needs.forEach((t, t1) -> needList.add(t1));
            int i = Test.getCurrentTest();
            need = needList.get(Test.getCurrentTest());
        } else {
            TreeMap<String, HomeWork> needs = HomeWork.getForTomorrow();
            needs.forEach((t, t1) -> needList.add(t1));
            need = needList.get(HomeWork.getCurrentWork());
        }
        getNeedName.setText(need.getNeedName());
        LocalDateTime time = LocalDateTime.ofInstant(need.getImplementationDate().getTime().toInstant(), ZoneId.systemDefault());
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.CANADA);
        getNeedDate.setText(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG).format(time));
        getDescription.setText(need.getNeedDescription());
    }

    private void correctMAinPanelStyle() {
        android.view.ViewGroup.LayoutParams layoutParams = getNeedName.getLayoutParams();
        layoutParams.width = (int) (screenWidth * 0.55);
        layoutParams.height = (int) (screenHeight * 0.05);

        needName.setTypeface(StaticUtils.getTypeFace(getApplicationContext()));
        needName.setTextSize((float) (screenWidth * 0.02));

        getNeedName.setTypeface(StaticUtils.getTypeFace(getApplicationContext()));
        getNeedName.setTextSize((float) (screenWidth * 0.02));
        getNeedName.setLayoutParams(layoutParams);
        layoutParams = getNeedName.getLayoutParams();
        layoutParams.width = (int) (screenWidth * 0.55);
        layoutParams.height = (int) (screenHeight * 0.07);
        getNeedName.setLayoutParams(layoutParams);


        needDate.setTypeface(StaticUtils.getTypeFace(getApplicationContext()));
        needDate.setTextSize((float) (screenWidth * 0.02));

        getNeedDate.setTypeface(StaticUtils.getTypeFace(getApplicationContext()));
        getNeedDate.setTextSize((float) (screenWidth * 0.02));
        layoutParams = getNeedDate.getLayoutParams();
        layoutParams.width = (int) (screenWidth * 0.55);
        getNeedDate.setLayoutParams(layoutParams);
        getNeedDate.setInputType(InputType.TYPE_NULL);

        description.setTypeface(StaticUtils.getTypeFace(getApplicationContext()));
        description.setTextSize((float) (screenWidth * 0.02));

        getDescription.setTypeface(StaticUtils.getTypeFace(getApplicationContext()));
        getDescription.setTextSize((float) (screenWidth * 0.02));
        layoutParams = getDescription.getLayoutParams();
        layoutParams.width = (int) (screenWidth * 0.55);
        layoutParams.height = (int) (screenHeight * 0.1);
        getDescription.setLayoutParams(layoutParams);
        getDescription.setMaxLines(6);

        layoutParams = nameLayout.getLayoutParams();
        layoutParams.width = (int) (screenWidth * 0.8);
        layoutParams.height = (int) (screenHeight * 0.1);
        nameLayout.setLayoutParams(layoutParams);

        layoutParams = dateLayout.getLayoutParams();
        layoutParams.width = (int) (screenWidth * 0.8);
        layoutParams.height = (int) (screenHeight * 0.1);
        dateLayout.setLayoutParams(layoutParams);

        layoutParams = descriptionLayout.getLayoutParams();
        layoutParams.width = (int) (screenWidth * 0.8);
        layoutParams.height = (int) (screenHeight * 0.1);
        descriptionLayout.setLayoutParams(layoutParams);

        label.setTypeface(StaticUtils.getTypeFace(getApplicationContext()));
        label.setTextSize((float) (screenWidth * 0.02));

    }

    private void makingTable(int day) {
        TreeMap<Integer, String> table = Settings.getWeekDays();
        DateTime date = new DateTime().now();
        day = day == -1 ? date.getDayOfWeek() : day;
        day = day == 7 ? 0 : day;
        weekDayNumber = day;
        weekDay.setSelection(weekDayNumber);
        String[] tableTasks = table.get(day).split("\\)");
        int i = 0;
        if (tableTasks.length == 1 && tableTasks[i].equals("")) {
            for (int j = 0; j < 8; j++) {
                EditText text = returnEditText(j);
                text.setText("");
            }
        } else {
            for (String s : tableTasks) {
                EditText text = returnEditText(i);
                if (!tableTasks[i].equals("")) {
                    if (!tableTasks[i].equals(i + 1 + "."))
                        text.setText(tableTasks[i].trim().replace(i + 1 + ".", "").replace(")", "").trim());
                }
                i++;
            }
            for (int j = 0; j < 8; j++) {
                EditText text = returnEditText(j);
                text.setTypeface(StaticUtils.getTypeFace(getApplicationContext()));
                text.setTextSize((float) (screenWidth * 0.02));
            }
        }
    }

    private EditText returnEditText(int count) {
        switch (count) {
            case 0:
                return firstEdit;
            case 1:
                return firstEdit2;
            case 2:
                return firstEdit3;
            case 3:
                return firstEdit4;
            case 4:
                return firstEdit5;
            case 5:
                return firstEdit6;
            case 6:
                return firstEdit7;
            default:
                return firstEdit8;
        }
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
}