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
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.exercises.R;
import com.example.schoolNeedsOrganizer.SchoolNeeds.HomeWork;
import com.example.schoolNeedsOrganizer.SchoolNeeds.Need;
import com.example.schoolNeedsOrganizer.SchoolNeeds.Test;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

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
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TreeMap;

public class AddScreen extends AppCompatActivity {

    private ConstraintLayout navigation, mainPanel, nameLayout, dateLayout, descriptionLayout, buttonLayout, tutor;
    private ImageView topBanner, settings, addButton, homeButton, listButton, strelka2;
    private int screenWidth, screenHeight;
    private TextView settingsText, needName, getNeedName, needDate, getNeedDate, description, getDescription, addTutorial;
    private Button saveButton;
    private RadioGroup group;
    private RadioButton testButton, homworkButton;
    private Calendar selectedDate;
    public static final String APP_PREFERENCES = "mysettings";
    private SharedPreferences mSettings;
    private boolean edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        boolean firstEnterInApp = mSettings.getBoolean("firstEnter", true);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            edit = extras.containsKey("Type");
        } else {
            edit = false;
        }
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_screen);
        findElements();
        getScreenSize();
        viewSettings();
        correctMAinPanelStyle();
        if (firstEnterInApp) {
            startTutorial();
        } else {
            tutor.setVisibility(View.INVISIBLE);
            strelka2.setVisibility(View.INVISIBLE);
            addTutorial.setVisibility(View.INVISIBLE);
            playAnimations();
            setOnClickActions();
            if (edit) {
                Need need;
                String type = intent.getStringExtra("NeedType");
                String name = intent.getStringExtra("Need");
                if (type.equals("T")) {
                    need = Test.getTestTreeSet().get(name);
                    Test.delete(name);
                    testButton.setChecked(true);
                } else {
                    need = HomeWork.getHWTreeSet().get(name);
                    HomeWork.delete(name);
                    homworkButton.setChecked(true);
                }
                try {
                    saveStatement();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                getNeedName.setText(need.getNeedName());
                selectedDate = need.getImplementationDate();
                LocalDateTime time = LocalDateTime.ofInstant(need.getImplementationDate().getTime().toInstant(), ZoneId.systemDefault());
                getNeedDate.setText(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG).format(time));
                getDescription.setText(need.getNeedDescription());
            }
        }
    }

    private void startTutorial() {
        addTutorial.setVisibility(View.INVISIBLE);
        strelka2.setVisibility(View.INVISIBLE);
        Animation appearance = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.appearance_anim);
        playAnimations();
        int[] stage = new int[2];
        tutor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stage[0] == 0) {
                    stage[0]++;
                    addTutorial.setTypeface(StaticUtils.getTypeFace(getApplicationContext()));
                    addTutorial.setTextSize((float) (screenWidth * 0.02));
                    addTutorial.startAnimation(appearance);
                    addTutorial.setVisibility(View.VISIBLE);
                    strelka2.startAnimation(appearance);
                    strelka2.setVisibility(View.VISIBLE);
                } else if (stage[0] == 1) {
                    Intent intent = new Intent("android.intent.action.LIST");
                    startActivity(intent);
                }
            }
        });

    }

    private void findElements() {
        settingsText = findViewById(R.id.settingsText);
        navigation = findViewById(R.id.navigation);
        topBanner = findViewById(R.id.topBanner);
        settings = findViewById(R.id.settings);
        addButton = findViewById(R.id.addButton);
        homeButton = findViewById(R.id.homeButton);
        listButton = findViewById(R.id.listButton);
        mainPanel = findViewById(R.id.mainPanel);
        getNeedName = findViewById(R.id.getNeedName);
        needName = findViewById(R.id.needName);
        needDate = findViewById(R.id.needDate);
        getNeedDate = findViewById(R.id.getNeedDate);
        saveButton = findViewById(R.id.saveButton);
        description = findViewById(R.id.description);
        getDescription = findViewById(R.id.getDescription);
        group = findViewById(R.id.group);
        homworkButton = findViewById(R.id.homworkButton);
        testButton = findViewById(R.id.testButton);
        nameLayout = findViewById(R.id.nameLayout);
        dateLayout = findViewById(R.id.dateLayout);
        descriptionLayout = findViewById(R.id.descriptionLayout);
        buttonLayout = findViewById(R.id.buttonLayout);
        tutor = findViewById(R.id.tutor);
        strelka2 = findViewById(R.id.strelka2);
        addTutorial = findViewById(R.id.addTutorial);
    }

    private void playAnimations() {
        Animation appearance = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.appearance_anim);
        navigation.startAnimation(appearance);
        topBanner.startAnimation(appearance);
        mainPanel.startAnimation(appearance);
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
        setStyles();
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

    private void setFont() {
        settingsText.setTypeface(StaticUtils.getTypeFace(getApplicationContext()));
        settingsText.setTextSize((float) (screenWidth * 0.02));
    }

    private void setStyles() {
        /*android.view.ViewGroup.LayoutParams layoutParams = navigation.getLayoutParams();
        layoutParams.width = (int) (screenWidth * 0.8);
        navigation.setLayoutParams(layoutParams);*/
    }

    private void setOnClickActions() {
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("android.intent.action.SETTINGS");
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
        getNeedDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                                String date = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                                getNeedDate.setText(date);
                                selectedDate = GregorianCalendar.getInstance();
                                selectedDate.set(year, monthOfYear, dayOfMonth);
                            }
                        },
                        now.get(Calendar.YEAR), // Initial year selection
                        now.get(Calendar.MONTH), // Initial month selection
                        now.get(Calendar.DAY_OF_MONTH) // Inital day selection
                );
                dpd.setVersion(DatePickerDialog.Version.VERSION_2);
                dpd.setAccentColor(getResources().getColor(R.color.main_color));
                dpd.setTitle("Выберите дату");
                dpd.setMinDate(now);
                dpd.show(getSupportFragmentManager(), "Datepickerdialog");
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNeed();
            }
        });
        getNeedDate.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                return true;
            }
        });
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
        layoutParams.height = (int) (screenHeight * 0.06);
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

        layoutParams = buttonLayout.getLayoutParams();
        layoutParams.width = (int) (screenWidth * 0.8);
        layoutParams.height = (int) (screenHeight * 0.1);
        buttonLayout.setLayoutParams(layoutParams);


    }

    private void saveNeed() {
        String name = getNeedName.getText().toString();
        String description = getDescription.getText().toString();
        String date = getNeedDate.getText().toString();
        if ((name.length() == 0) || (description.length() == 0) || (date.length() == 0)) {
            if (name.length() == 0) {
                showAlertDialog("Неверные данные!", "Введите коректно название предмета!");
            } else {
                if (date.length() == 0) {
                    showAlertDialog("Неверные данные!", "Введите коректно дату!");
                } else showAlertDialog("Неверные данные!", "Введите коректно задание!");
            }
        } else {
            if ((!homworkButton.isChecked()) && (!testButton.isChecked())) {
                showAlertDialog("Неверные данные!", "Выберите тип задания!");
            } else {
                boolean test = testButton.isChecked();
                Need need = new HomeWork();
                if (test) {
                    need = new Test();
                }
                need.setNeedName(name);
                need.setImplementationDate(selectedDate);
                need.setNeedDescription(description);
                need.setStatus("Активно");
                if (test) {
                    if (Test.getTestTreeSet().containsKey(name)) {
                        showAlertDialog("Ошибка!", "Тест с таким именем уже существует. Пожалуйста, видоизмените его :)");
                    } else {
                        Test.setTestTreeSet((Test) need, need.getNeedName());
                        Intent intent = new Intent("android.intent.action.SCREEN");
                        onPause();
                        startActivity(intent);
                    }
                } else {
                    if (HomeWork.getHWTreeSet().containsKey(name)) {
                        showAlertDialog("Ошибка!", "Домашняя работа с таким именем уже существует. Пожалуйста, видоизмените его :)");
                    } else {
                        HomeWork.setHWTreeSet((HomeWork) need, need.getNeedName());
                        Intent intent = new Intent("android.intent.action.SCREEN");
                        onPause();
                        startActivity(intent);
                    }
                }
            }
        }
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
    public void onBackPressed() {
        if (edit) {
            new AlertDialog.Builder(this)
                    .setTitle("Сохранение")
                    .setMessage("Вы не сохранили изменения. Если вы продолжите, задача будет удалена. Продолжить?")
                    .setNegativeButton(android.R.string.no, null)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            Intent intent = new Intent("android.intent.action.SCREEN");
                            startActivity(intent);
                        }
                    })
                    .create()
                    .show();
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