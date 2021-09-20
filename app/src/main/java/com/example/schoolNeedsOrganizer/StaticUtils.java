package com.example.schoolNeedsOrganizer;

import android.content.Context;
import android.graphics.Typeface;

public class StaticUtils {
    public static Typeface getTypeFace(Context currentContext) {
        Typeface typeface = Typeface.createFromAsset(currentContext.getAssets(),
                "font/font2.ttf");
        return typeface;
    }
}
