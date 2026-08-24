package com.android.support.components;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.text.Html;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.support.Menu;

import org.lsposed.lsparanoid.Obfuscate;

@Obfuscate
public class ICategory {

    private final Context context;
    private final Typeface typeface;

    public ICategory(Context context, Typeface typeface) {
        this.context = context;
        this.typeface = typeface;
    }

    public void add(LinearLayout mContent, String text) {

        GradientDrawable categoryDrawable = new GradientDrawable();
        categoryDrawable.setCornerRadius(Colors.RADIUS_WIDGET);
        categoryDrawable.setColor(Colors.MENU_FEATURE_BG_COLOR);
        categoryDrawable.setStroke(1, Colors.CARD_STROKE);
        // accent left-edge bar effect via two-tone gradient
        categoryDrawable.setOrientation(android.graphics.drawable.GradientDrawable.Orientation.LEFT_RIGHT);
        categoryDrawable.setColors(new int[]{Colors.accentStart(), Colors.MENU_FEATURE_BG_COLOR});
        float[] radii = new float[8];
        java.util.Arrays.fill(radii, Colors.RADIUS_WIDGET * 2f); // px
        categoryDrawable.setCornerRadii(radii);

        TextView textView = new TextView(context);
        textView.setBackground(categoryDrawable);
        textView.setText(Html.fromHtml(text));
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Colors.TEXT_COLOR_2);
        textView.setTypeface(typeface, Typeface.BOLD);
        textView.setPadding(0, 12, 0, 12);
        SpartanAnim.bounce(textView);
        mContent.addView(textView);
    }
}
