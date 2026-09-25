package com.zaero.diamondclean;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends Activity {

    private static final int GOLD = Color.rgb(214, 168, 79);
    private static final int BG = Color.rgb(12, 12, 14);
    private static final int PANEL = Color.rgb(28, 27, 30);
    private static final int PANEL_LIGHT = Color.rgb(40, 38, 42);
    private static final int TEXT = Color.rgb(245, 242, 235);
    private static final int MUTED = Color.rgb(180, 175, 168);

    private static final String PREFS = "zaero_data";
    private static final String KEY_ERRORS = "errors";
    private static final String KEY_PROFI = "profi";

    private static final String MASTER = "C1B2A3Z";

    private static final int REQUEST_ERROR_IMAGE = 5001;

    private LinearLayout root;
    private LinearLayout content;

    private boolean profi = false;

    private final List<ErrorItem> errors =
            new ArrayList<ErrorItem>();

    private final List<String> editingImagePaths =
            new ArrayList<String>();

    private LinearLayout editingImageContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs =
                getSharedPreferences(
                        PREFS,
                        MODE_PRIVATE
                );

        profi = prefs.getBoolean(
                KEY_PROFI,
                false
        );

        loadErrors();

        buildBase();

        showHome();
    }

    private void buildBase() {

        root = new LinearLayout(this);

        root.setOrientation(
                LinearLayout.VERTICAL
        );

        root.setBackgroundColor(BG);

        setContentView(root);

        createHeader();

        ScrollView scroll =
                new ScrollView(this);

        scroll.setFillViewport(true);

        scroll.setBackgroundColor(BG);

        content =
                new LinearLayout(this);

        content.setOrientation(
                LinearLayout.VERTICAL
        );

        content.setGravity(
                Gravity.TOP |
                        Gravity.CENTER_HORIZONTAL
        );

        content.setPadding(
                dp(18),
                dp(18),
                dp(18),
                dp(30)
        );

        scroll.addView(
                content,
                new ScrollView.LayoutParams(
                        -1,
                        -1
                )
        );

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

        LinearLayout header =
                new LinearLayout(this);

        header.setOrientation(
                LinearLayout.HORIZONTAL
        );

        header.setGravity(
                Gravity.CENTER_VERTICAL
        );

        header.setPadding(
                dp(12),
                dp(8),
                dp(12),
                dp(8)
        );

        header.setBackgroundColor(
                Color.rgb(18, 17, 19)
        );

        Button menu =
                smallButton("☰");

        menu.setOnClickListener(
                v -> showMenu()
        );

        header.addView(
                menu,
                new LinearLayout.LayoutParams(
                        dp(52),
                        dp(52)
                )
        );

        TextView title =
                text(
                        "Z.AERO",
                        21,
                        GOLD,
                        true
                );

        title.setGravity(
                Gravity.CENTER
        );

        header.addView(
                title,
                new LinearLayout.LayoutParams(
                        0,
                        dp(52),
                        1
                )
        );

        TextView user =
                text(
                        profi
                                ? "PROFI"
                                : "BENUTZER",
                        12,
                        TEXT,
                        false
                );

        user.setGravity(
                Gravity.CENTER
        );

        header.addView(
                user,
                new LinearLayout.LayoutParams(
                        dp(82),
                        dp(52)
                )
        );

        root.addView(
                header,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(68)
                )
        );
    }

    private void showHome() {

        clearContent();

        content.setGravity(
                Gravity.CENTER
        );

        LinearLayout home =
                new LinearLayout(this);

        home.setOrientation(
                LinearLayout.VERTICAL
        );

        home.setGravity(
                Gravity.CENTER_HORIZONTAL
        );

        home.setPadding(
                dp(4),
                dp(8),
                dp(4),
                dp(8)
        );

        TextView welcome =
                text(
                        "Willkommen bei",
                        22,
                        GOLD,
                        true
                );

        welcome.setGravity(
                Gravity.CENTER
        );

        home.addView(
                welcome,
                new LinearLayout.LayoutParams(
                        -1,
                        -2
                )
        );

        TextView brand =
                text(
                        "Z-Aero Diamond Clean",
                        30,
                        TEXT,
                        true
                );

        brand.setGravity(
                Gravity.CENTER
        );

        brand.setPadding(
                0,
                dp(3),
                0,
                dp(6)
        );

        home.addView(
                brand,
                new LinearLayout.LayoutParams(
                        -1,
                        -2
                )
        );

        TextView subtitle =
                text(
                        "Ihre Unterstützung für den sicheren und effizienten Betrieb der Anlage.",
                        15,
                        MUTED,
                        false
                );

        subtitle.setGravity(
                Gravity.CENTER
        );

        subtitle.setPadding(
                dp(8),
                0,
                dp(8),
                dp(10)
        );

        home.addView(
                subtitle,
                new LinearLayout.LayoutParams(
                        -1,
                        -2
                )
        );

        ImageView machine =
                new ImageView(this);

        machine.setImageResource(
                R.drawable.machine
        );

        machine.setScaleType(
                ImageView.ScaleType.CENTER_INSIDE
        );

        machine.setAdjustViewBounds(true);

        machine.setContentDescription(
                "Z-Aero Diamond Clean Anlage"
        );

        LinearLayout.LayoutParams imageParams =
                new LinearLayout.LayoutParams(
                        -1,
                        dp(205)
                );

        imageParams.topMargin =
                dp(2);

        imageParams.bottomMargin =
                dp(12);

        home.addView(
                machine,
                imageParams
        );

        LinearLayout cards =
                new LinearLayout(this);

        cards.setOrientation(
                LinearLayout.HORIZONTAL
        );

        cards.setGravity(
                Gravity.CENTER
        );

        addHomeCard(
                cards,
                "▶",
                "Anfahren",
                "Anlage starten",
                v -> showChapter("Anfahren")
        );

        addHomeCard(
                cards,
                "■",
                "Abstellen",
                "Anlage sicher\nherunterfahren",
                v -> showChapter("Abstellen")
        );

        addHomeCard(
                cards,
                "⌕",
                "Fehlersuche",
                "Fehler und\nLösungen",
                v -> showTroubleshooting()
        );

        home.addView(
                cards,
                new LinearLayout.LayoutParams(
                        -
