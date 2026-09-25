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

                TextView title =
                        text(
                                item.title,
                                18,
                                GOLD,
                                true
                        );

                card.addView(
                        title,
                        new LinearLayout.LayoutParams(
                                -1,
                                -2
                        )
                );

                TextView causeLabel =
                        text(
                                "URSACHE:",
                                14,
                                GOLD,
                                true
                        );

                causeLabel.setPadding(
                        0,
                        dp(8),
                        0,
                        dp(2)
                );

                card.addView(
                        causeLabel,
                        new LinearLayout.LayoutParams(
                                -1,
                                -2
                        )
                );

                TextView cause =
                        text(
                                item.cause,
                                15,
                                TEXT,
                                false
                        );

                card.addView(
                        cause,
                        new LinearLayout.LayoutParams(
                                -1,
                                -2
                        )
                );

                TextView solutionLabel =
                        text(
                                "LÖSUNG:",
                                14,
                                GOLD,
                                true
                        );

                solutionLabel.setPadding(
                        0,
                        dp(10),
                        0,
                        dp(2)
                );

                card.addView(
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

                card.addView(
                        solution,
                        new LinearLayout.LayoutParams(
                                -1,
                                -2
                        )
                );

                addImagesToCard(
                        card,
                        item.imagePaths
                );

                if (profi) {

                    Button edit =
                            smallActionButton(
                                    "✎ Bearbeiten"
                            );

                    edit.setOnClickListener(
                            v -> showEditError(index)
                    );

                    card.addView(edit);
                }

                content.addView(
                        card,
                        new LinearLayout.LayoutParams(
                                -1,
                                -2
                        )
                );

                addSpaceToLayout(
                        content,
                        8
                );
            }
        }

        if (profi) {

            Button add =
                    button(
                            "+ Fehler hinzufügen"
                    );

            add.setOnClickListener(
                    v -> showEditError(-1)
            );

            content.addView(add);
        }

        Button back =
                button("← Zurück");

        back.setOnClickListener(
                v -> showHome()
        );

        content.addView(back);
    }

    private void showSearch() {

        final EditText input =
                new EditText(this);

        input.setHint(
                "Mindestens 3 Buchstaben"
        );

        input.setTextColor(TEXT);

        input.setHintTextColor(MUTED);

        input.setSingleLine(true);

        AlertDialog dialog =
                new AlertDialog.Builder(this)
                        .setTitle("Fehler suchen")
                        .setView(input)
                        .setNegativeButton(
                                "Abbrechen",
                                null
                        )
                        .setPositiveButton(
                                "Suchen",
                                null
                        )
                        .create();

        dialog.setOnShowListener(
                d -> {

                    Button searchButton =
                            dialog.getButton(
                                    AlertDialog.BUTTON_POSITIVE
                            );

                    searchButton.setEnabled(false);

                    input.addTextChangedListener(
                            new TextWatcher() {

                                public void beforeTextChanged(
                                        CharSequence s,
                                        int start,
                                        int count,
                                        int after
                                ) {
                                }

                                public void onTextChanged(
                                        CharSequence s,
                                        int start,
                                        int before,
                                        int count
                                ) {

                                    searchButton.setEnabled(
                                            s.toString()
                                                    .trim()
                                                    .length() >= 3
                                    );
                                }

                                public void afterTextChanged(
                                        Editable e
                                ) {
                                }
                            }
                    );

                    searchButton.setOnClickListener(
                            v -> {

                                String q =
                                        input.getText()
                                                .toString()
                                                .trim()
                                                .toLowerCase(
                                                        Locale.GERMAN
                                                );

                                dialog.dismiss();

                                showSearchResults(q);
                            }
                    );
                }
        );

        dialog.show();
    }

    private void showSearchResults(
            String q
    ) {

        clearContent();

        content.setGravity(
                Gravity.TOP |
                        Gravity.CENTER_HORIZONTAL
        );

        TextView heading =
                text(
                        "Suchergebnisse",
                        26,
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
                        dp(58)
                )
        );

        boolean found = false;

        for (
                ErrorItem item : errors
        ) {

            String all =
                    (
                            item.title
                                    + " "
                                    + item.cause
                                    + " "
                                    + item.solution
                    )
                            .toLowerCase(
                                    Locale.GERMAN
                            );

            if (all.contains(q)) {

                found = true;

                LinearLayout card =
                        panel();

                card.addView(
                        text(
                                item.title,
                                18,
                                GOLD,
                                true
                        )
                );

                TextView causeLabel =
                        text(
                                "URSACHE:",
                                14,
                                GOLD,
                                true
                        );

                causeLabel.setPadding(
                        0,
                        dp(8),
                        0,
                        dp(2)
                );

                card.addView(causeLabel);

                card.addView(
                        text(
                                item.cause,
                                15,
                                TEXT,
                                false
                        )
                );

                TextView solutionLabel =
                        text(
                                "LÖSUNG:",
                                14,
                                GOLD,
                                true
                        );

                solutionLabel.setPadding(
                        0,
                        dp(10),
                        0,
                        dp(2)
                );

                card.addView(solutionLabel);

                card.addView(
                        text(
                                item.solution,
                                15,
                                TEXT,
                                false
                        )
                );

                addImagesToCard(
                        card,
                        item.imagePaths
                );

                content.addView(
                        card,
                        new LinearLayout.LayoutParams(
                                -1,
                                -2
                        )
                );

                addSpaceToLayout(
                        content,
                        8
                );
            }
        }

        if (!found) {

            TextView none =
                    text(
                            "Kein passender Fehler gefunden.",
                            16,
                            MUTED,
                            false
                    );

            none.setGravity(
                    Gravity.CENTER
            );

            content.addView(
                    none,
                    new LinearLayout.LayoutParams(
                            -1,
                            dp(70)
                    )
            );
        }

        Button back =
                button("← Zurück");

        back.setOnClickListener(
                v -> showTroubleshooting()
        );

        content.addView(back);
    }

    private void showEditError(
            int index
    ) {

        editingIndex = index;

        LinearLayout box =
                new LinearLayout(this);

        box.setOrientation(
                LinearLayout.VERTICAL
        );

        box.setPadding(
                dp(8),
                0,
                dp(8),
                0
        );

        EditText title =
                edit(
                        "Fehlermeldung / Fehler"
                );

        EditText cause =
                edit("Ursache");

        EditText solution =
                edit("Lösung");

        box.addView(title);
        box.addView(cause);
        box.addView(solution);

        ErrorItem existing = null;

        if (index >= 0) {

            existing =
                    errors.get(index);

            title.setText(
                    existing.title
            );

            cause.setText(
                    existing.cause
            );

            solution.setText(
                    existing.solution
            );
        }

        LinearLayout imageBox =
                new LinearLayout(this);

        imageBox.setOrientation(
                LinearLayout.VERTICAL
        );

        box.addView(
                imageBox,
                new LinearLayout.LayoutParams(
                        -1,
                        -2
                )
        );

        if (existing != null) {

            for (
                    String path :
                    existing.imagePaths
            ) {

                addEditableImage(
                        imageBox,
                        path
                );
            }
        }

        Button addImage =
                smallActionButton(
                        "+ Bild hinzufügen"
                );

        addImage.setOnClickListener(
                v -> {

                    String t =
                            title.getText()
                                    .toString()
                                    .trim();

                    String c =
                            cause.getText()
                                    .toString()
                                    .trim();

                    String s =
                            solution.getText()
                                    .toString()
                                    .trim();

                    if (
                            t.isEmpty()
                                    || c.isEmpty()
                                    || s.isEmpty()
                    ) {

                        Toast.makeText(
                                this,
                                "Bitte zuerst Fehlermeldung, Ursache und Lösung ausfüllen.",
                                Toast.LENGTH_SHORT
                        ).show();

                        return;
                    }

                    if (editingIndex < 0) {

                        errors.add(
                                new ErrorItem(
                                        t,
                                        c,
                                        s
                                )
                        );

                        editingIndex =
                                errors.size() - 1;

                    } else {

                        errors.get(
                                editingIndex
                        ).title = t;

                        errors.get(
                                editingIndex
                        ).cause = c;

                        errors.get(
                                editingIndex
                        ).solution = s;
                    }

                    saveErrors();

                    Intent intent =
                            new Intent(
                                    Intent.ACTION_OPEN_DOCUMENT
                            );

                    intent.addCategory(
                            Intent.CATEGORY_OPENABLE
                    );

                    intent.setType(
                            "image/*"
                    );

                    startActivityForResult(
                            intent,
                            REQUEST_ERROR_IMAGE
                    );
                }
        );

        box.addView(addImage);

        AlertDialog dialog =
                new AlertDialog.Builder(this)
                        .setTitle(
                                index >= 0
                                        ? "Fehler bearbeiten"
                                        : "Fehler hinzufügen"
                        )
                        .setView(box)
                        .setNegativeButton(
                                "Abbrechen",
                                null
                        )
                        .setPositiveButton(
                                "Speichern",
                                null
                        )
                        .create();

        dialog.setOnShowListener(
                d -> {

                    dialog.getButton(
                            AlertDialog.BUTTON_POSITIVE
                    ).setOnClickListener(
                            v -> {

                                String t =
                                        title.getText()
                                                .toString()
                                                .trim();

                                String c =
                                        cause.getText()
                                                .toString()
                                                .trim();

                                String s =
                                        solution.getText()
                                                .toString()
                                                .trim();

                                if (
                                        t.isEmpty()
                                                || c.isEmpty()
                                                || s.isEmpty()
                                ) {

                                    Toast.makeText(
                                            this,
                                            "Alle drei Felder müssen ausgefüllt sein.",
                                            Toast.LENGTH_SHORT
                                    ).show();

                                    return;
                                }

                                if (index < 0) {

                                    errors.add(
                                            new ErrorItem(
                                                    t,
                                                    c,
                                                    s
                                            )
                                    );

                                } else {

                                    errors.get(index).title =
                                            t;

                                    errors.get(index).cause =
                                            c;

                                    errors.get(index).solution =
                                            s;
                                }

                                saveErrors();

                                dialog.dismiss();

                                showTroubleshooting();
                            }
                    );
                }
        );

        dialog.show();
    }

    @Override
    protected void onActivityResult(
            int requestCode,
            int resultCode,
            Intent data
    ) {

        super.onActivityResult(
                requestCode,
                resultCode,
                data
        );

        if (
                requestCode
                        == REQUEST_ERROR_IMAGE
                        && resultCode == RESULT_OK
                        && data != null
                        && data.getData() != null
                        && editingIndex >= 0
                        && editingIndex < errors.size()
        ) {

            String path =
                    copyImage(
                            data.getData()
                    );

            if (path != null) {

                errors.get(
                        editingIndex
                ).imagePaths.add(path);

                saveErrors();

                Toast.makeText(
                        this,
                        "Bild hinzugefügt.",
                        Toast.LENGTH_SHORT
                ).show();
            }

            showEditError(
                    editingIndex
            );
        }
    }

    private void addEditableImage(
            LinearLayout parent,
            String path
    ) {

        LinearLayout row =
                new LinearLayout(this);

        row.setOrientation(
                LinearLayout.VERTICAL
        );

        row.setPadding(
                0,
                dp(8),
                0,
                dp(4)
        );

        ImageView image =
                new ImageView(this);

        image.setImageURI(
                Uri.fromFile(
                        new File(path)
                )
        );

        image.setScaleType(
                ImageView.ScaleType.CENTER_INSIDE
        );

        image.setAdjustViewBounds(true);

        row.addView(
                image,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(150)
                )
        );

        Button remove =
                smallActionButton(
                        "Bild entfernen"
                );

        remove.setOnClickListener(
                v -> {

                    if (
                            editingIndex >= 0
                                    && editingIndex < errors.size()
                    ) {

                        errors.get(
                                editingIndex
                        ).imagePaths.remove(
                                path
                        );

                        saveErrors();

                        showEditError(
                                editingIndex
                        );
                    }
                }
        );

        row.addView(remove);

        parent.addView(
                row,
                new LinearLayout.LayoutParams(
                        -1,
                        -2
                )
        );
    }

    private void addImagesToCard(
            LinearLayout card,
            List<String> paths
    ) {

        for (
                String path :
                paths
        ) {

            if (
                    path == null
                            || path.isEmpty()
            ) {
                continue;
            }

            File file =
                    new File(path);

            if (!file.exists()) {
                continue;
            }

            ImageView image =
                    new ImageView(this);

            image.setImageURI(
                    Uri.fromFile(file)
            );

            image.setScaleType(
                    ImageView.ScaleType.CENTER_INSIDE
            );

            image.setAdjustViewBounds(true);

            LinearLayout.LayoutParams params =
                    new LinearLayout.LayoutParams(
                            -1,
                            dp(170)
                    );

            params.topMargin =
                    dp(8);

            card.addView(
                    image,
                    params
            );
        }
    }

    private String copyImage(
            Uri uri
    ) {

        try {

            File dir =
                    new File(
                            getFilesDir(),
                            "error_images"
                    );

            if (
                    !dir.exists()
                            && !dir.mkdirs()
            ) {
                return null;
            }

            File file =
                    new File(
                            dir,
                            "img_"
                                    + System.currentTimeMillis()
                                    + ".jpg"
                    );

            InputStream input =
                    getContentResolver()
                            .openInputStream(uri);

            FileOutputStream output =
                    new FileOutputStream(file);

            byte[] buffer =
                    new byte[8192];

            int length;

            while (
                    (length =
                            input.read(buffer))
                            != -1
            ) {

                output.write(
                        buffer,
                        0,
                        length
                );
            }

            input.close();
            output.close();

            return file.getAbsolutePath();

        } catch (Exception e) {

            Toast.makeText(
                    this,
                    "Bild konnte nicht übernommen werden.",
                    Toast.LENGTH_SHORT
            ).show();

            return null;
        }
    }

    private void showMenu() {

        String[] items;

        if (profi) {

            items =
                    new String[]{
                            "Startseite",
                            "Fehlersuche",
                            "Profi-Modus ausschalten"
                    };

        } else {

            items =
                    new String[]{
                            "Startseite",
                            "Fehlersuche",
                            "Profi-Modus"
                    };
        }

        new AlertDialog.Builder(this)
                .setTitle("Z.AERO")
                .setItems(
                        items,
                        (dialog, which) -> {

                            if (which == 0) {

                                showHome();

                            } else if (which == 1) {

                                showTroubleshooting();

                            } else {

                                if (profi) {

                                    profi = false;

                                    saveProfi();

                                    buildBase();

                                    showHome();

                                } else {

                                    askPassword();
                                }
                            }
                        }
                )
                .show();
    }

    private void askPassword() {

        EditText input =
                new EditText(this);

        input.setInputType(
                InputType.TYPE_CLASS_TEXT
                        | InputType.TYPE_TEXT_VARIATION_PASSWORD
        );

        input.setTextColor(TEXT);

        input.setHint("Passwort");

        new AlertDialog.Builder(this)
                .setTitle("Profi-Modus")
                .setMessage("Passwort eingeben")
                .setView(input)
                .setNegativeButton(
                        "Abbrechen",
                        null
                )
                .setPositiveButton(
                        "OK",
                        (dialog, which) -> {

                            if (
                                    MASTER.equals(
                                            input.getText()
                                                    .toString()
                                    )
                            ) {

                                profi = true;

                                saveProfi();

                                buildBase();

                                showHome();

                                Toast.makeText(
                                        this,
                                        "Profi-Modus aktiviert.",
                                        Toast.LENGTH_SHORT
                                ).show();

                            } else {

                                Toast.makeText(
                                        this,
                                        "Falsches Passwort.",
                                        Toast.LENGTH_SHORT
                                ).show();
                            }
                        }
                )
                .show();
    }

    private void saveProfi() {

        getSharedPreferences(
                PREFS,
                MODE_PRIVATE
        )
                .edit()
                .putBoolean(
                        KEY_PROFI,
                        profi
                )
                .apply();
    }

    private void saveErrors() {

        try {

            JSONArray array =
                    new JSONArray();

            for (
                    ErrorItem item :
                    errors
            ) {

                JSONObject object =
                        new JSONObject();

                object.put(
                        "title",
                        item.title
                );

                object.put(
                        "cause",
                        item.cause
                );

                object.put(
                        "solution",
                        item.solution
                );

                JSONArray images =
                        new JSONArray();

                for (
                        String path :
                        item.imagePaths
                ) {

                    images.put(path);
                }

                object.put(
                        "imagePaths",
                        images
                );

                array.put(object);
            }

            getSharedPreferences(
                    PREFS,
                    MODE_PRIVATE
            )
                    .edit()
                    .putString(
                            KEY_ERRORS,
                            array.toString()
                    )
                    .apply();

        } catch (Exception e) {

            Toast.makeText(
                    this,
                    "Fehler beim Speichern.",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    private void loadErrors() {

        errors.clear();

        String saved =
                getSharedPreferences(
                        PREFS,
                        MODE_PRIVATE
                )
                        .getString(
                                KEY_ERRORS,
                                ""
                        );

        if (saved.isEmpty()) {
            return;
        }

        try {

            JSONArray array =
                    new JSONArray(saved);

            for (
                    int i = 0;
                    i < array.length();
                    i++
            ) {

                JSONObject object =
                        array.getJSONObject(i);

                ErrorItem item =
                        new ErrorItem(
                                object.optString(
                                        "title",
                                        ""
                                ),
                                object.optString(
                                        "cause",
                                        "Nicht angegeben"
                                ),
                                object.optString(
                                        "solution",
                                        ""
                                )
                        );

                JSONArray images =
                        object.optJSONArray(
                                "imagePaths"
                        );

                if (images != null) {

                    for (
                            int j = 0;
                            j < images.length();
                            j++
                    ) {

                        String path =
                                images.optString(
                                        j,
                                        ""
                                );

                        if (!path.isEmpty()) {

                            item.imagePaths.add(
                                    path
                            );
                        }
                    }

                } else {

                    String oldPath =
                            object.optString(
                                    "imagePath",
                                    ""
                            );

                    if (!oldPath.isEmpty()) {

                        item.imagePaths.add(
                                oldPath
                        );
                    }
                }

                boolean duplicate = false;

                for (
                        ErrorItem existing :
                        errors
                ) {

                    if (
                            existing.title.equals(
                                    item.title
                            )
                                    && existing.cause.equals(
                                    item.cause
                            )
                                    && existing.solution.equals(
                                    item.solution
                            )
                    ) {

                        duplicate = true;
                        break;
                    }
                }

                if (!duplicate) {

                    errors.add(item);
                }
            }

            saveErrors();

        } catch (Exception e) {

            Toast.makeText(
                    this,
                    "Gespeicherte Fehler konnten nicht geladen werden.",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    private void clearContent() {

        if (content != null) {

            content.removeAllViews();

            content.setGravity(
                    Gravity.TOP |
                            Gravity.CENTER_HORIZONTAL
            );
        }
    }

    private TextView text(
            String value,
            float size,
            int color,
            boolean bold
    ) {

        TextView view =
                new TextView(this);

        view.setText(value);

        view.setTextSize(size);

        view.setTextColor(color);

        if (bold) {

            view.setTypeface(
                    Typeface.DEFAULT,
                    Typeface.BOLD
            );
        }

        view.setGravity(
                Gravity.CENTER_VERTICAL
        );

        return view;
    }

    private EditText edit(
            String hint
    ) {

        EditText edit =
                new EditText(this);

        edit.setHint(hint);

        edit.setHintTextColor(MUTED);

        edit.setTextColor(TEXT);

        edit.setTextSize(15);

        edit.setPadding(
                dp(10),
                dp(8),
                dp(10),
                dp(8)
        );

        return edit;
    }

    private LinearLayout panel() {

        LinearLayout layout =
                new LinearLayout(this);

        layout.setOrientation(
                LinearLayout.VERTICAL
        );

        layout.setPadding(
                dp(16),
                dp(16),
                dp(16),
                dp(16)
        );

        layout.setBackground(
                round(
                        PANEL,
                        dp(12),
                        Color.TRANSPARENT
                )
        );

        return layout;
    }

    private Button button(
            String label
    ) {

        Button button =
                new Button(this);

        button.setText(label);

        button.setTextColor(TEXT);

        button.setTextSize(15);

        button.setAllCaps(false);

        button.setBackground(
                round(
                        PANEL_LIGHT,
                        dp(10),
                        Color.TRANSPARENT
                )
        );

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        -1,
                        dp(52)
                );

        params.bottomMargin =
                dp(8);

        button.setLayoutParams(params);

        return button;
    }

    private Button smallButton(
            String label
    ) {

        Button button =
                new Button(this);

        button.setText(label);

        button.setTextColor(GOLD);

        button.setTextSize(20);

        button.setAllCaps(false);

        button.setBackground(
                round(
                        PANEL,
                        dp(10),
                        Color.TRANSPARENT
                )
        );

        return button;
    }

    private Button smallActionButton(
            String label
    ) {

        Button button =
                new Button(this);

        button.setText(label);

        button.setTextColor(GOLD);

        button.setTextSize(13);

        button.setAllCaps(false);

        button.setBackground(
                round(
                        PANEL_LIGHT,
                        dp(10),
                        Color.TRANSPARENT
                )
        );

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        -1,
                        dp(45)
                );

        params.topMargin =
                dp(10);

        button.setLayoutParams(params);

        return button;
    }

    private GradientDrawable round(
            int color,
            int radius,
            int strokeColor
    ) {

        GradientDrawable drawable =
                new GradientDrawable();

        drawable.setColor(color);

        drawable.setCornerRadius(radius);

        if (
                strokeColor
                        != Color.TRANSPARENT
        ) {

            drawable.setStroke(
                    dp(1),
                    strokeColor
            );
        }

        return drawable;
    }

    private void addSpaceToLayout(
            LinearLayout layout,
            int size
    ) {

        View space =
                new View(this);

        layout.addView(
                space,
                new LinearLayout.LayoutParams(
                        1,
                        dp(size)
                )
        );
    }

    private int dp(
            int value
    ) {

        return (int) (
                value *
                        getResources()
                                .getDisplayMetrics()
                                .density
                        + 0.5f
        );
    }

    private static class ErrorItem {

        String title;
        String cause;
        String solution;

        List<String> imagePaths =
                new ArrayList<String>();

        ErrorItem(
                String title,
                String cause,
                String solution
        ) {

            this.title = title;
            this.cause = cause;
            this.solution = solution;
        }
    }
}
