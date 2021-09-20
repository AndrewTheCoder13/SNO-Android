package com.example.schoolNeedsOrganizer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Space;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.exercises.R;
import com.example.schoolNeedsOrganizer.SchoolNeeds.HomeWork;
import com.example.schoolNeedsOrganizer.SchoolNeeds.Need;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class FullLust extends AppCompatActivity {

    private ConstraintLayout navigation, tutor;
    private ImageView topBanner, settings, addButton, homeButton, listButton, nothingIsAdded2, strelka3;
    private ScrollView mainScroll;
    private String[] spinnerItems = {"Полный список дел", "Выполненные", "Просроченные", "Активные"};
    private Spinner itemSpinner2;
    private int screenWidth, screenHeight;
    private TextView settingsText, fullText;
    private LinearLayout mainLayout;
    public static final String APP_PREFERENCES = "mysettings";
    private SharedPreferences mSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_lust);
        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        boolean firstEnterInApp = mSettings.getBoolean("firstEnter", true);
        findElements();
        getScreenSize();
        viewSettings();
        if (!firstEnterInApp) {
            tutor.setVisibility(View.INVISIBLE);
            strelka3.setVisibility(View.INVISIBLE);
            fullText.setVisibility(View.INVISIBLE);
            spinnerSettings();
            playAnimations();
            setOnClickActions();
            addNeeds();
        } else {
            startTutorial();
        }
    }
    private void spinnerSettings() {
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
        itemSpinner2.setAdapter(spinnerItemsAdapter);
    }

    private void startTutorial() {
        fullText.setVisibility(View.INVISIBLE);
        strelka3.setVisibility(View.INVISIBLE);
        Animation appearance = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.appearance_anim);
        playAnimations();
        int[] stage = new int[2];
        tutor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stage[0] == 0) {
                    stage[0]++;
                    fullText.setTypeface(StaticUtils.getTypeFace(getApplicationContext()));
                    fullText.setTextSize((float) (screenWidth * 0.02));
                    fullText.startAnimation(appearance);
                    fullText.setVisibility(View.VISIBLE);
                    strelka3.startAnimation(appearance);
                    strelka3.setVisibility(View.VISIBLE);
                }  else if (stage[0] == 1) {
                    Intent intent = new Intent("android.intent.action.SETTINGS");
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
        nothingIsAdded2 = findViewById(R.id.nothingIsAdded2);
        mainLayout = findViewById(R.id.mainLayout);
        strelka3 = findViewById(R.id.strelka3);
        tutor = findViewById(R.id.tutor);
        fullText = findViewById(R.id.fullText);
        itemSpinner2 = findViewById(R.id.itemSpinner2);
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
        setImageVisible();
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

    private void setOnClickActions() {
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("android.intent.action.SETTINGS");
                startActivity(intent);
            }
        });
        itemSpinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                addNeeds();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("android.intent.action.SCREEN");
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
    }

    private void setImageVisible() {
        if (!((HomeWork.getHWTreeSet().size() == 0) && (Test.getTestTreeSet().size() == 0))) {
            nothingIsAdded2.setVisibility(View.INVISIBLE);
            mainLayout.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
            mainLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        } else {
            Animation appearance = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.appearance_anim);
            nothingIsAdded2.startAnimation(appearance);
        }
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

    private void addNeeds() {
        mainLayout.removeAllViews();
        ArrayList<Need> needList = new ArrayList<>();
        TreeMap<String, Test> tests = Test.getTestTreeSet();
        tests.forEach((t, t1) -> needList.add(t1));
        String item = itemSpinner2.getSelectedItem().toString();
        Need[] list = new Need[0];
        List<Need> why;
        switch (item){
            case "Просроченные": why = (needList.stream().filter(t -> !t.getStatus().equals("Активно")).collect(Collectors.toList()));
                list = new Need[why.size()];
                for(int i =0; i < why.size(); i++){list[i] = why.get(i);}break;

            case "Выполненные": why = needList.stream().filter(t -> t.isEnded()).collect(Collectors.toList());
                list = new Need[why.size()];
                                for(int i =0; i < why.size(); i++){list[i] = why.get(i);};break;
            case "Активные":  why = needList.stream().filter(t -> ((!t.isEnded()) && (t.getStatus().equals("Активно")))).collect(Collectors.toList());
                list = new Need[why.size()];
                                for(int i =0; i < why.size(); i++){list[i] = why.get(i);}break;
            default: why = needList;
                list = new Need[why.size()];
                for(int i =0; i < why.size(); i++){list[i] = why.get(i);};break;
        }
        ArrayList<Need> needList2 = new ArrayList<>();
        tests.forEach((t, t1) -> needList2.add(t1));
        for(String test : tests.keySet()){
            int index = find(list, tests.get(test));
            if(index == -1){
                needList2.remove(tests.get(test));
            }
        }
        needList2.forEach(n -> {
            LinearLayout layout = new LinearLayout(mainLayout.getContext());
            layout.setBackgroundResource(R.drawable.layout_bg);
            layout.setOrientation(LinearLayout.HORIZONTAL);
            layout.setGravity(Gravity.CENTER_VERTICAL);
            android.view.ViewGroup.LayoutParams layoutParams = new LinearLayout.LayoutParams((int) (screenWidth * 0.75), 100);

            layout.setLayoutParams(layoutParams);

            TextView textView = new TextView(this);
            if (n.getNeedName().length() > 4) {
                textView.setText(n.getNeedName().substring(0, 4) + "...");
            } else {
                textView.setText(n.getNeedName());
            }
            textView.setTypeface(StaticUtils.getTypeFace(getApplicationContext()));
            textView.setTextSize((float) (screenWidth * 0.02));
            textView.setTextColor(Color.BLACK);
            textView.setId(findElement(false, n));

            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent("android.intent.action.NEED");
                    intent.putExtra("Type", "T");
                    intent.putExtra("Name", n.getNeedName());
                    startActivity(intent);
                }
            });
            Space space = new Space(layout.getContext());
            space.setMinimumWidth(100);
            layout.addView(space, 0);
            layout.addView(textView, 1);
            space = new Space(layout.getContext());
            space.setMinimumWidth((int) (screenWidth * 0.15));
            layout.addView(space, 2);

            ImageView edit = new ImageView(layout.getContext());
            edit.setImageResource(R.drawable.edit);
            layoutParams = new LinearLayout.LayoutParams(100, 100);
            edit.setLayoutParams(layoutParams);
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent("android.intent.action.ADD");
                    intent.putExtra("Type", "Edit");
                    intent.putExtra("Need", n.getNeedName());
                    intent.putExtra("NeedType", "T");
                    startActivity(intent);
                }
            });
            layout.addView(edit, 3);

            space = new Space(layout.getContext());
            space.setMinimumWidth(100);
            layout.addView(space, 4);

            ImageView delete = new ImageView(layout.getContext());
            delete.setImageResource(R.drawable.delete);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(mainLayout.getContext())
                            .setTitle("Удаление задачи")
                            .setMessage("Удалить задачу?")
                            .setNegativeButton(android.R.string.no, null)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                    Test.getTestTreeSet().remove(n.getNeedName());
                                    mainLayout.removeView(layout);
                                }
                            })
                            .create()
                            .show();
                }
            });
            layoutParams = new LinearLayout.LayoutParams(100, 100);

            delete.setLayoutParams(layoutParams);
            layout.addView(delete, 5);
            mainLayout.addView(layout);
        });

        needList2.clear();
        TreeMap<String, HomeWork> hms = HomeWork.getHWTreeSet();
        hms.forEach((t, t1) -> needList.add(t1));

        list = new Need[0];
        switch (item){
            case "Просроченные": why = (needList.stream().filter(t -> !t.getStatus().equals("Активно")).collect(Collectors.toList()));
                list = new Need[why.size()];
                for(int i =0; i < why.size(); i++){list[i] = why.get(i);}break;
            case "Выполненные": why = needList.stream().filter(t -> t.isEnded()).collect(Collectors.toList());
                list = new Need[why.size()];
                for(int i =0; i < why.size(); i++){list[i] = why.get(i);};break;
            case "Активные":  why = needList.stream().filter(t -> ((!t.isEnded()) && (t.getStatus().equals("Активно")))).collect(Collectors.toList());
                list = new Need[why.size()];
                for(int i =0; i < why.size(); i++){list[i] = why.get(i);}break;
            default: why = needList;
                list = new Need[why.size()];
                for(int i =0; i < why.size(); i++){list[i] = why.get(i);};break;

        }
        hms.forEach((t, t1) -> needList2.add(t1));
        for(String test : hms.keySet()){
            int index = find(list, hms.get(test));
            if(index == -1){
                needList2.remove(hms.get(test));
            }
        }

        needList2.forEach(n -> {
            LinearLayout layout = new LinearLayout(mainLayout.getContext());
            layout.setBackgroundResource(R.drawable.layout_bg);
            layout.setOrientation(LinearLayout.HORIZONTAL);
            layout.setGravity(Gravity.CENTER_VERTICAL);
            android.view.ViewGroup.LayoutParams layoutParams = new LinearLayout.LayoutParams((int) (screenWidth * 0.75), 100);

            layout.setLayoutParams(layoutParams);

            TextView textView = new TextView(this);
            if (n.getNeedName().length() > 4) {
                textView.setText(n.getNeedName().substring(0, 4) + "...");
            } else {
                textView.setText(n.getNeedName());
            }
            textView.setTypeface(StaticUtils.getTypeFace(getApplicationContext()));
            textView.setTextSize((float) (screenWidth * 0.02));
            textView.setTextColor(Color.BLACK);
            textView.setId(findElement(false, n));


            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent("android.intent.action.NEED");
                    intent.putExtra("Type", "H");
                    intent.putExtra("Name", n.getNeedName());
                    startActivity(intent);
                }
            });

            Space space = new Space(layout.getContext());
            space.setMinimumWidth(100);
            layout.addView(space, 0);
            layout.addView(textView, 1);
            space = new Space(layout.getContext());
            space.setMinimumWidth((int) (screenWidth * 0.15));
            layout.addView(space, 2);

            ImageView edit = new ImageView(layout.getContext());
            edit.setImageResource(R.drawable.edit);
            layoutParams = new LinearLayout.LayoutParams(100, 100);
            edit.setLayoutParams(layoutParams);
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent("android.intent.action.ADD");
                    intent.putExtra("Type", "Edit");
                    intent.putExtra("Need", n.getNeedName());
                    intent.putExtra("NeedType", "HW");
                    startActivity(intent);
                }
            });
            layout.addView(edit, 3);

            space = new Space(layout.getContext());
            space.setMinimumWidth(100);
            layout.addView(space, 4);

            ImageView delete = new ImageView(layout.getContext());
            delete.setImageResource(R.drawable.delete);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(mainLayout.getContext())
                            .setTitle("Удаление задачи")
                            .setMessage("Удалить задачу?")
                            .setNegativeButton(android.R.string.no, null)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                    HomeWork.delete(n);
                                    mainLayout.removeView(layout);
                                }
                            })
                            .create()
                            .show();
                }
            });
            layoutParams = new LinearLayout.LayoutParams(100, 100);

            delete.setLayoutParams(layoutParams);
            layout.addView(delete, 5);
            mainLayout.addView(layout);
        });

    }

    private int find(Need[] map, Need element){
        int pos = -1;
        for(int i = 0; i < map.length; i++){
            if(element.equals(map[i])){
                return i;
            }
        }
        return pos;
    }

    private int findElement(boolean isTest, Need need) {
        int position = 0;
        if (isTest) {
            TreeMap<String, Test> tests = Test.getTestTreeSet();
            for (String name : tests.keySet()) {
                if (need.getNeedName().equals(name)) {
                    return position;
                }
                position++;
            }
        } else {
            TreeMap<String, HomeWork> tests = HomeWork.getHWTreeSet();
            for (String name : tests.keySet()) {
                if (need.getNeedName().equals(name)) {
                    return position;
                }
                position++;
            }
        }
        return -1;
    }

    @Override
    protected void onResume() {
        super.onResume();
        startingNeeds();
        addNeeds();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        startingNeeds();
        addNeeds();
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