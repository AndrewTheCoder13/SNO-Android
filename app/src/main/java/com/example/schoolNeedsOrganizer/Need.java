package com.example.schoolNeedsOrganizer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.Display;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.exercises.R;
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

public class Need extends AppCompatActivity {

    private ConstraintLayout navigation;
    private ConstraintLayout mainPanel;
    private ConstraintLayout nameLayout;
    private ConstraintLayout dateLayout;
    private ConstraintLayout descriptionLayout;
    private ImageView topBanner, homeButton;
    private ImageView settings;
    private ImageView addButton;
    private ImageView listButton, leftArrow, rightArrow;
    private int screenWidth, screenHeight;
    public static final String APP_PREFERENCES = "mysettings";
    private SharedPreferences mSettings;
    private CheckBox ready;
        private TextView settingsText, needName, getNeedName, needDate, getNeedDate, description, getDescription, label;
    private String animCaller;
    private Spinner itemSpinner;
    private com.example.schoolNeedsOrganizer.SchoolNeeds.Need need;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        String action = intent.getStringExtra("Type");
        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_need);
        findElements();
        playAnimations();
        getScreenSize();
        viewSettings();
        setOnClickActions();
        correctMAinPanelStyle();
        makeMainPanel();
    }

    private void findElements() {
        ConstraintLayout mainLayout = findViewById(R.id.mainLayout);
        navigation = findViewById(R.id.navigation);
        topBanner = findViewById(R.id.topBanner);
        mainPanel = findViewById(R.id.mainPanel);
        settings = findViewById(R.id.settings);
        addButton = findViewById(R.id.addButton);
        homeButton = findViewById(R.id.homeButton);
        listButton = findViewById(R.id.listButton);
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
        itemSpinner = findViewById(R.id.itemSpinner);
        label = findViewById(R.id.label);
        ready = findViewById(R.id.ready);
    }

    private void playAnimations() {
        Animation appearance = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.appearance_anim);
        navigation.startAnimation(appearance);
        topBanner.startAnimation(appearance);
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
    }

    private void actionBarAndStatusBarSettings() {
        getSupportActionBar().hide();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.main_color));
        }
    }

    private void centralLayoutSettings() {
        android.view.ViewGroup.LayoutParams layoutParams = mainPanel.getLayoutParams();
        layoutParams.height = (int) (screenHeight * 0.65);
        layoutParams.width = (int) (screenWidth * 0.9);
        mainPanel.setLayoutParams(layoutParams);
    }


    private void setOnClickActions() {
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
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("android.intent.action.SCREEN");
                startActivity(intent);
            }
        });
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
            Intent intent = getIntent();
            need.setEnded(ready.isChecked());
            String action = intent.getStringExtra("Type");
            if (action.equals("T")) {
                Test.setTestTreeSet((Test) need, need.getNeedName());
            } else {
               HomeWork.setHWTreeSet((HomeWork) need, need.getNeedName());
            }
            com.example.schoolNeedsOrganizer.AppSettings.Settings.saveSettings(getApplicationContext());
            saveStatement();
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.onPause();
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

    private void makeMainPanel() {
        mainPanel.setVisibility(View.VISIBLE);
        leftArrow.setVisibility(View.INVISIBLE);
        rightArrow.setVisibility(View.INVISIBLE);
        itemSpinner.setVisibility(View.INVISIBLE);
        Intent intent = getIntent();
        String action = intent.getStringExtra("Type");
        String name = intent.getStringExtra("Name");
        com.example.schoolNeedsOrganizer.SchoolNeeds.Need need;
        if (action.equals("T")) {
            TreeMap<String, Test> needs = Test.getTestTreeSet();
            need = needs.get(name);
        } else {
            TreeMap<String, HomeWork> needs = HomeWork.getHWTreeSet();
            need = needs.get(name);
        }
        this.need = need;
        ready.setChecked(need.isEnded());
        getNeedName.setText(need.getNeedName());
        LocalDateTime time = LocalDateTime.ofInstant(need.getImplementationDate().getTime().toInstant(), ZoneId.systemDefault());
        getNeedDate.setText(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG).format(time));
        getDescription.setText(need.getNeedDescription());
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

