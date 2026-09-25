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

    private static final int RED = Color.rgb(220, 30, 35);
    private static final int GREEN = Color.rgb(30, 220, 70);

    private static final String PREFS = "zaero_data";
    private static final String KEY_ERRORS = "errors";
    private static final String KEY_PROFI = "profi";

    private static final String MASTER = "C1B2A3Z";

    private static final int REQUEST_ERROR_IMAGE = 5001;

    private LinearLayout root;
    private LinearLayout content;

    private boolean profi = false;

    private int editingIndex = -1;

    private final List<ErrorItem> errors =
            new ArrayList<ErrorItem>();

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
                        profi ? "PROFI" : "BENUTZER",
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

        home.addView(
                brand,
                new LinearLayout.LayoutParams(
                        -1,
                        -2
                )
        );

        AnimationSet animation =
                new AnimationSet(true);

        TranslateAnimation slide =
                new TranslateAnimation(
                        -dp(280),
                        0,
                        0,
                        0
                );

        slide.setDuration(850);

        AlphaAnimation fade =
                new AlphaAnimation(
                        0f,
                        1f
                );

        fade.setDuration(850);

        animation.addAnimation(slide);
        animation.addAnimation(fade);

        brand.startAnimation(animation);

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
                        -1,
                        -2
                )
        );

        content.addView(
                home,
                new LinearLayout.LayoutParams(
                        -1,
                        -2
                )
        );
    }

    private void addHomeCard(
            LinearLayout parent,
            String icon,
            String title,
            String subtitle,
            View.OnClickListener listener
    ) {

        LinearLayout card =
                new LinearLayout(this);

        card.setOrientation(
                LinearLayout.VERTICAL
        );

        card.setGravity(
                Gravity.CENTER
        );

        card.setPadding(
                dp(8),
                dp(10),
                dp(8),
                dp(10)
        );

        card.setBackground(
                round(
                        PANEL,
                        dp(12),
                        GOLD
                )
        );

        TextView iconView =
                text(
                        icon,
                        25,
                        GOLD,
                        true
                );

        iconView.setGravity(
                Gravity.CENTER
        );

        card.addView(
                iconView,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(34)
                )
        );

        TextView titleView =
                text(
                        title,
                        16,
                        TEXT,
                        true
                );

        titleView.setGravity(
                Gravity.CENTER
        );

        card.addView(
                titleView,
                new LinearLayout.LayoutParams(
                        -1,
                        -2
                )
        );

        TextView subtitleView =
                text(
                        subtitle,
                        11,
                        MUTED,
                        false
                );

        subtitleView.setGravity(
                Gravity.CENTER
        );

        card.addView(
                subtitleView,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(34)
                )
        );

        card.setOnClickListener(
                listener
        );

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        0,
                        dp(112),
                        1
                );

        params.setMargins(
                dp(4),
                0,
                dp(4),
                0
        );

        parent.addView(
                card,
                params
        );
    }

    private void showChapter(
            String chapter
    ) {

        clearContent();

        content.setGravity(
                Gravity.TOP |
                        Gravity.CENTER_HORIZONTAL
        );

        TextView heading =
                text(
                        chapter,
                        28,
                        GOLD,
                        true
                );

        heading.setGravity(
                Gravity.CENTER
        );

        content.addView(
                heading,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(60)
                )
        );

        LinearLayout panel =
                panel();

        String message;

        if (chapter.equals("Anfahren")) {

            message =
                    "Hier wird beschrieben, wie die Anlage sicher gestartet wird.\n\n"
                    + "Die genauen Arbeitsschritte können hier später ergänzt werden.";

        } else {

            message =
                    "Hier wird beschrieben, wie die Anlage sicher heruntergefahren wird.\n\n"
                    + "Die genauen Arbeitsschritte können hier später ergänzt werden.";
        }

        TextView body =
                text(
                        message,
                        16,
                        TEXT,
                        false
                );

        body.setGravity(
                Gravity.TOP
        );

        panel.addView(
                body,
                new LinearLayout.LayoutParams(
                        -1,
                        -2
                )
        );

        content.addView(
                panel,
                new LinearLayout.LayoutParams(
                        -1,
                        -2
                )
        );

        Button back =
                button("← Zurück");

        back.setOnClickListener(
                v -> showHome()
        );

        content.addView(back);
    }

    private void showTroubleshooting() {

        clearContent();

        content.setGravity(
                Gravity.TOP |
                        Gravity.CENTER_HORIZONTAL
        );

        TextView heading =
                text(
                        "Fehlersuche",
                        28,
                        GOLD,
                        true
                );

        heading.setGravity(
                Gravity.CENTER
        );

        content.addView(
                heading,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(60)
                )
        );

        Button search =
                button("⌕ Fehler suchen");

        search.setOnClickListener(
                v -> showSearch()
        );

        content.addView(search);

        if (errors.isEmpty()) {

            TextView empty =
                    text(
                            "Noch keine Fehler hinterlegt.",
                            16,
                            MUTED,
                            false
                    );

            empty.setGravity(
                    Gravity.CENTER
            );

            content.addView(
                    empty,
                    new LinearLayout.LayoutParams(
                            -1,
                            dp(70)
                    )
            );

        } else {

            for (
                    int i = 0;
                    i < errors.size();
                    i++
            ) {

                final int index = i;

                ErrorItem item =
                        errors.get(i);

                LinearLayout card =
                        panel();

                /*
                 * ROTE FEHLERMELDUNG
                 */

                LinearLayout errorBox =
                        borderedBox(RED);

                TextView errorTitle =
                        text(
                                item.title,
                                20,
                                GOLD,
                                true
                        );

                errorTitle.setGravity(
                        Gravity.CENTER
                );

                errorBox.addView(
                        errorTitle,
                        new LinearLayout.LayoutParams(
                                -1,
                                dp(58)
                        )
                );

                card.addView(
                        errorBox,
                        new LinearLayout.LayoutParams(
                                -1,
                                -2
                        )
                );

                /*
                 * URSACHE
                 */

                TextView causeLabel =
                        text(
                                "URSACHE:",
                                14,
                                GOLD,
                                true
                        );

                causeLabel.setPadding(
                        0,
                        dp(12),
                        0,
                        dp(2)
                );

                card.addView(
                        causeLabel
                );

                TextView cause =
                        text(
                                item.cause,
                                15,
                                TEXT,
                                false
                        );

                card.addView(
                        cause
                );

                /*
                 * GRÜNE LÖSUNG
                 */

                LinearLayout solutionBox =
                        borderedBox(GREEN);

                TextView solutionLabel =
                        text(
                                "LÖSUNG:",
                                14,
                                GREEN,
                                true
                        );

                solutionLabel.setPadding(
                        dp(10),
                        dp(8),
                        dp(10),
                        dp(2)
                );

                solutionBox.addView(
                        solutionLabel,
                        new LinearLayout.LayoutParams(
                                -1,
                                -2
                        )
                );

                TextView solution =
                        text(
                                item.solution,
                                15,
                                TEXT,
                                false
                        );

                solution.setPadding(
                        dp(10),
                        dp(2),
                        dp(10),
                        dp(10)
                );

                solutionBox.addView(
                        solution,
                        new LinearLayout.LayoutParams(
                                -1,
                                -2
                        )
                );

                LinearLayout.LayoutParams solutionParams =
                        new LinearLayout.LayoutParams(
                                -1,
                                -2
                        );

                solutionParams.topMargin =
                        dp(12);

                card.addView(
                        solutionBox,
                       
