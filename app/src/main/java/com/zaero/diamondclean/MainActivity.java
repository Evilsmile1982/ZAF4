package com.zaero.diamondclean;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends Activity {

    private static final int GOLD = Color.rgb(214, 168, 79);
    private static final int GOLD_DARK = Color.rgb(145, 108, 35);
    private static final int BG = Color.rgb(12, 12, 14);
    private static final int PANEL = Color.rgb(28, 27, 30);
    private static final int PANEL_LIGHT = Color.rgb(40, 38, 42);
    private static final int TEXT = Color.rgb(245, 242, 235);
    private static final int MUTED = Color.rgb(180, 175, 168);

    private LinearLayout root;
    private LinearLayout content;

    private final List<ErrorItem> errors = new ArrayList<ErrorItem>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loadErrors();
        buildBase();
        showHome();
    }

    // ------------------------------------------------------------
    // GRUNDLAYOUT
    // ------------------------------------------------------------

    private void buildBase() {

        root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(BG);

        setContentView(root);

        createHeader();

        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(true);
        scroll.setBackgroundColor(BG);

        content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setPadding(dp(18), dp(18), dp(18), dp(30));

        scroll.addView(content);

        root.addView(
                scroll,
                new LinearLayout.LayoutParams(
                        -1,
                        0,
                        1
                )
        );
    }

    private void createHeader() {

        LinearLayout header = new LinearLayout(this);
        header.setOrientation(LinearLayout.HORIZONTAL);
        header.setGravity(Gravity.CENTER_VERTICAL);
        header.setPadding(dp(12), dp(8), dp(12), dp(8));
        header.setBackgroundColor(Color.rgb(18, 17, 19));

        Button menu = smallButton("☰");
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMenu();
            }
        });

        header.addView(
                menu,
                new LinearLayout.LayoutParams(dp(52), dp(52))
        );

        TextView title = new TextView(this);
        title.setText("Z.AERO");
        title.setTextColor(GOLD);
        title.setTextSize(21);
        title.setTypeface(Typeface.DEFAULT_BOLD);
        title.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams titleParams =
                new LinearLayout.LayoutParams(0, dp(52), 1);

        header.addView(title, titleParams);

        TextView user = new TextView(this);
        user.setText("BENUTZER");
        user.setTextColor(TEXT);
        user.setTextSize(12);
        user.setGravity(Gravity.CENTER);

        header.addView(
                user,
                new LinearLayout.LayoutParams(dp(82), dp(52))
        );

        root.addView(
                header,
                new LinearLayout.LayoutParams(-1, dp(68))
        );
    }

    // ------------------------------------------------------------
    // STARTSEITE
    // ------------------------------------------------------------

    private void showHome() {

        clearContent();

        TextView welcome = text(
                "
