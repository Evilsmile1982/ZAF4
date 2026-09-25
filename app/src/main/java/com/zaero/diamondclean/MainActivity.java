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

    private static final String MASTER = "Chefe";

    private static final int REQUEST_ERROR_IMAGE = 5001;

    private LinearLayout root;
    private LinearLayout content;

    private boolean profi = false;

    private String editingImagePath = "";
    private ImageView editingImagePreview;

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
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showMenu();
                    }
                }
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

        machine.setAdjustViewBounds(
                true
        );

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
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showChapter("Anfahren");
                    }
                }
        );

        addHomeCard(
                cards,
                "■",
                "Abstellen",
                "Anlage sicher\nherunterfahren",
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showChapter("Abstellen");
                    }
                }
        );

        addHomeCard(
                cards,
                "⌕",
                "Fehlersuche",
                "Fehler und\nLösungen",
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showTroubleshooting();
                    }
                }
        );

        home.addView(
                cards,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(150)
                )
        );

        content.addView(
                home,
                new LinearLayout.LayoutParams(
                        -1,
                        -2
                )
        );

        AnimationSet animation =
                new AnimationSet(true);

        animation.addAnimation(
                new TranslateAnimation(
                        -dp(420),
                        0,
                        0,
                        0
                )
        );

        animation.addAnimation(
                new AlphaAnimation(
                        0.0f,
                        1.0f
                )
        );

        animation.setDuration(1600);
        animation.setStartOffset(250);

        brand.startAnimation(
                animation
        );
    }

    private void addHomeCard(
            LinearLayout cards,
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
                dp(5),
                dp(8),
                dp(5),
                dp(8)
        );

        GradientDrawable background =
                new GradientDrawable();

        background.setColor(PANEL);

        background.setCornerRadius(
                dp(16)
        );

        background.setStroke(
                dp(1),
                Color.rgb(70, 63, 52)
        );

        card.setBackground(
                background
        );

        TextView iconView =
                text(
                        icon,
                        34,
                        GOLD,
                        false
                );

        iconView.setGravity(
                Gravity.CENTER
        );

        card.addView(
                iconView,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(48)
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

        titleView.setMaxLines(2);

        card.addView(
                titleView,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(42)
                )
        );

        TextView subtitleView =
                text(
                        subtitle,
                        12,
                        MUTED,
                        false
                );

        subtitleView.setGravity(
                Gravity.CENTER
        );

        subtitleView.setMaxLines(2);

        card.addView(
                subtitleView,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(40)
                )
        );

        card.setOnClickListener(
                listener
        );

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        0,
                        -1,
                        1
                );

        params.setMargins(
                dp(4),
                0,
                dp(4),
                0
        );

        cards.addView(
                card,
                params
        );
    }

    private void showChapter(
            String chapter
    ) {

        clearContent();

        Button back =
                button("←  Startseite");

        back.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showHome();
                    }
                }
        );

        content.addView(back);

        addSpace(15);

        content.addView(
                text(
                        chapter,
                        28,
                        GOLD,
                        true
                )
        );

        addSpace(10);

        LinearLayout info =
                panel();

        String description;

        if ("Anfahren".equals(chapter)) {

            description =
                    "Hier wird Schritt für Schritt beschrieben, "
                            + "wie die Z-Aero Diamond Clean Anlage "
                            + "gestartet wird.\n\n"
                            + "Die ausführlichen Arbeitsschritte "
                            + "können hier später ergänzt werden.";

        } else {

            description =
                    "Hier wird Schritt für Schritt beschrieben, "
                            + "wie die Z-Aero Diamond Clean Anlage "
                            + "sicher abgestellt wird.\n\n"
                            + "Die ausführlichen Arbeitsschritte "
                            + "können hier später ergänzt werden.";
        }

        TextView body =
                text(
                        description,
                        17,
                        TEXT,
                        false
                );

        body.setPadding(
                dp(18),
                dp(18),
                dp(18),
                dp(18)
        );

        info.addView(body);

        content.addView(info);
    }

    private void showTroubleshooting() {

        clearContent();

        Button back =
                button("←  Startseite");

        back.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showHome();
                    }
                }
        );

        content.addView(back);

        addSpace(15);

        content.addView(
                text(
                        "Fehlersuche",
                        28,
                        GOLD,
                        true
                )
        );

        content.addView(
                text(
                        "Fehler, Ursachen und Lösungen",
                        15,
                        MUTED,
                        false
                )
        );

        addSpace(12);

        Button search =
                button("⌕  Fehler suchen");

        search.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showSearch();
                    }
                }
        );

        content.addView(search);

        if (profi) {

            Button add =
                    button(
                            "＋  Fehler anlegen"
                    );

            add.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            editError(null);
                        }
                    }
            );

            content.addView(add);
        }

        addSpace(15);

        if (errors.isEmpty()) {

            LinearLayout empty =
                    panel();

            TextView emptyText =
                    text(
                            "Noch keine Fehler gespeichert.\n\n"
                                    + "Im Profi-Modus kannst du "
                                    + "Fehler, Lösungen und Bilder "
                                    + "anlegen.",
                            16,
                            MUTED,
                            false
                    );

            emptyText.setGravity(
                    Gravity.CENTER
            );

            emptyText.setPadding(
                    dp(20),
                    dp(35),
                    dp(20),
                    dp(35)
            );

            empty.addView(emptyText);

            content.addView(empty);

            return;
        }

        for (final ErrorItem item : errors) {

            LinearLayout card =
                    panel();

            card.addView(
                    text(
                            item.title,
                            19,
                            GOLD,
                            true
                    )
            );

            addImageToCard(
                    card,
                    item.imagePath
            );

            TextView solution =
                    text(
                            item.solution,
                            15,
                            TEXT,
                            false
                    );

            solution.setPadding(
                    0,
                    dp(10),
                    0,
                    0
            );

            card.addView(solution);

            if (profi) {

                Button edit =
                        smallActionButton(
                                "Bearbeiten"
                        );

                edit.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                editError(item);
                            }
                        }
                );

                card.addView(edit);
            }

            LinearLayout.LayoutParams params =
                    new LinearLayout.LayoutParams(
                            -1,
                            -2
                    );

            params.bottomMargin =
                    dp(12);

            content.addView(
                    card,
                    params
            );
        }
    }

    private void addImageToCard(
            LinearLayout card,
            String path
    ) {

        if (path == null ||
                path.isEmpty()) {
            return;
        }

        File file =
                new File(path);

        if (!file.exists()) {
            return;
        }

        ImageView image =
                new ImageView(this);

        image.setImageURI(
                Uri.fromFile(file)
        );

        image.setScaleType(
                ImageView.ScaleType.CENTER_INSIDE
        );

        image.setAdjustViewBounds(
                true
        );

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        -1,
                        dp(220)
                );

        params.topMargin =
                dp(10);

        card.addView(
                image,
                params
        );
    }

    private void showSearch() {

        final EditText input =
                new EditText(this);

        input.setHint(
                "Fehler suchen..."
        );

        input.setTextColor(TEXT);
        input.setHintTextColor(MUTED);

        new AlertDialog.Builder(this)
                .setTitle(
                        "Fehlersuche"
                )
                .setView(input)
                .setPositiveButton(
                        "Suchen",
                        (dialog, which) -> {

                            showSearchResults(
                                    input.getText()
                                            .toString()
                                            .trim()
                                            .toLowerCase(
                                                    Locale.getDefault()
                                            )
                            );
                        }
                )
                .setNegativeButton(
                        "Abbrechen",
                        null
                )
                .show();
    }

    private void showSearchResults(
            String query
    ) {

        clearContent();

        Button back =
                button(
                        "←  Fehlersuche"
                );

        back.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showTroubleshooting();
                    }
                }
        );

        content.addView(back);

        addSpace(15);

        content.addView(
                text(
                        "Suchergebnisse",
                        25,
                        GOLD,
                        true
                )
        );

        int count = 0;

        for (ErrorItem item : errors) {

            String combined =
                    (
                            item.title
                                    + " "
                                    + item.solution
                    )
                            .toLowerCase(
                                    Locale.getDefault()
                            );

            if (!combined.contains(query)) {
                continue;
            }

            LinearLayout card =
                    panel();

            card.addView(
                    text(
                            item.title,
                            19,
                            GOLD,
                            true
                    )
            );

            addImageToCard(
                    card,
                    item.imagePath
            );

            card.addView(
                    text(
                            item.solution,
                            15,
                            TEXT,
                            false
                    )
            );

            content.addView(card);

            addSpace(10);

            count++;
        }

        if (count == 0) {

            content.addView(
                    text(
                            "Kein passender Fehler gefunden.",
                            16,
                            MUTED,
                            false
                    )
            );
        }
    }

    private void editError(
            final ErrorItem existing
    ) {

        if (!profi) {

            Toast.makeText(
                    this,
                    "Bitte zuerst den Profi-Modus aktivieren.",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        editingImagePath =
                existing == null
                        ? ""
                        : existing.imagePath;

        LinearLayout box =
                new LinearLayout(this);

        box.setOrientation(
                LinearLayout.VERTICAL
        );

        box.setPadding(
                dp(20),
                dp(5),
                dp(20),
                dp(5)
        );

        EditText title =
                new EditText(this);

        title.setHint(
                "Fehler / Meldung"
        );

        title.setTextColor(TEXT);
        title.setHintTextColor(MUTED);

        EditText solution =
                new EditText(this);

        solution.setHint(
                "Ursache und Lösung"
        );

        solution.setTextColor(TEXT);
        solution.setHintTextColor(MUTED);

        solution.setMinLines(5);

        solution.setGravity(
                Gravity.TOP
        );

        if (existing != null) {

            title.setText(
                    existing.title
            );

            solution.setText(
                    existing.solution
            );
        }

        box.addView(title);

        box.addView(
                solution,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(130)
                )
        );

        Button imageButton =
                button(
                        "＋  Bild auswählen"
                );

        box.addView(imageButton);

        editingImagePreview =
                new ImageView(this);

        editingImagePreview.setScaleType(
                ImageView.ScaleType.CENTER_INSIDE
        );

        editingImagePreview.setAdjustViewBounds(
                true
        );

        if (editingImagePath != null &&
                !editingImagePath.isEmpty()) {

            File file =
                    new File(
                            editingImagePath
                    );

            if (file.exists()) {

                editingImagePreview
                        .setImageURI(
                                Uri.fromFile(file)
                        );
            }
        }

        box.addView(
                editingImagePreview,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(180)
                )
        );

        imageButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

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
                }
        );

        AlertDialog.Builder builder =
                new AlertDialog.Builder(this);

        builder.setTitle(
                existing == null
                        ? "Fehler anlegen"
                        : "Fehler bearbeiten"
        );

        builder.setView(box);

        builder.setPositiveButton(
                "Speichern",
                null
        );

        builder.setNegativeButton(
                "Abbrechen",
                null
        );

        if (existing != null) {

            builder.setNeutralButton(
                    "Löschen",
                    null
            );
        }

        final AlertDialog dialog =
                builder.create();

        dialog.setOnShowListener(
                new android.content.DialogInterface.OnShowListener() {

                    @Override
                    public void onShow(
                            android.content.DialogInterface d
                    ) {

                        Button save =
                                dialog.getButton(
                                        AlertDialog.BUTTON_POSITIVE
                                );

                        save.setOnClickListener(
                                new View.OnClickListener() {

                                    @Override
                                    public void onClick(
                                            View v
                                    ) {

                                        String t =
                                                title.getText()
                                                        .toString()
                                                        .trim();

                                        String s =
                                                solution.getText()
                                                        .toString()
                                                        .trim();

                                        if (t.isEmpty()
                                                || s.isEmpty()) {

                                            Toast.makeText(
                                                    MainActivity.this,
                                                    "Bitte Fehler sowie Ursache und Lösung ausfüllen.",
                                                    Toast.LENGTH_SHORT
                                            ).show();

                                            return;
                                        }

                                        if (existing == null) {

                                            errors.add(
                                                    new ErrorItem(
                                                            t,
                                                            s,
                                                            editingImagePath
                                                    )
                                            );

                                        } else {

                                            existing.title =
                                                    t;

                                            existing.solution =
                                                    s;

                                            existing.imagePath =
                                                    editingImagePath;
                                        }

                                        saveErrors();

                                        editingImagePreview =
                                                null;

                                        dialog.dismiss();

                                        showTroubleshooting();
                                    }
                                }
                        );

                        if (existing != null) {

                            Button delete =
                                    dialog.getButton(
                                            AlertDialog.BUTTON_NEUTRAL
                                    );

                            delete.setOnClickListener(
                                    new View.OnClickListener() {

                                        @Override
                                        public void onClick(
                                                View v
                                        ) {

                                            new AlertDialog.Builder(
                                                    MainActivity.this
                                            )
                                                    .setTitle(
                                                            "Fehler löschen?"
                                                    )
                                                    .setMessage(
                                                            "Dieser Fehler wird dauerhaft gelöscht."
                                                    )
                                                    .setPositiveButton(
                                                            "Löschen",
                                                            (dialog2, which) -> {

                                                                errors.remove(
                                                                        existing
                                                                );

                                                                saveErrors();

                                                                dialog.dismiss();

                                                                showTroubleshooting();
                                                            }
                                                    )
                                                    .setNegativeButton(
                                                            "Abbrechen",
                                                            null
                                                    )
                                                    .show();
                                        }
                                    }
                            );
                        }
                    }
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

        if (requestCode != REQUEST_ERROR_IMAGE
                || resultCode != RESULT_OK
                || data == null
                || data.getData() == null) {

            return;
        }

        try {

            Uri source =
                    data.getData();

            File dir =
                    new File(
                            getFilesDir(),
                            "error_images"
                    );

            if (!dir.exists()) {
                dir.mkdirs();
            }

            File target =
                    new File(
                            dir,
                            "error_"
                                    + System.currentTimeMillis()
                                    + ".png"
                    );

            InputStream input =
                    getContentResolver()
                            .openInputStream(source);

            FileOutputStream output =
                    new FileOutputStream(target);

            byte[] buffer =
                    new byte[8192];

            int length;

            while (
                    (length =
                            input.read(buffer)) > 0
            ) {

                output.write(
                        buffer,
                        0,
                        length
                );
            }

            output.close();
            input.close();

            editingImagePath =
                    target.getAbsolutePath();

            if (editingImagePreview != null) {

                editingImagePreview
                        .setImageURI(
                                Uri.fromFile(target)
                        );
            }

            Toast.makeText(
                    this,
                    "Bild ausgewählt.",
                    Toast.LENGTH_SHORT
            ).show();

        } catch (Exception e) {

            Toast.makeText(
                    this,
                    "Bild konnte nicht übernommen werden.",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    private void showMenu() {

        ArrayList<String> items =
                new ArrayList<String>();

        items.add("Startseite");
        items.add("Anfahren");
        items.add("Abstellen");
        items.add("Fehlersuche");

        items.add(
                profi
                        ? "Profi-Modus ausschalten"
                        : "Profi-Modus"
        );

        final String[] menuItems =
                items.toArray(
                        new String[items.size()]
                );

        new AlertDialog.Builder(this)
                .setTitle(
                        "Z-Aero Diamond Clean"
                )
                .setItems(
                        menuItems,
                        (dialog, which) -> {

                            String selected =
                                    menuItems[which];

                            if ("Startseite"
                                    .equals(selected)) {

                                showHome();

                            } else if ("Anfahren"
                                    .equals(selected)) {

                                showChapter(
                                        "Anfahren"
                                );

                            } else if ("Abstellen"
                                    .equals(selected)) {

                                showChapter(
                                        "Abstellen"
                                );

                            } else if ("Fehlersuche"
                                    .equals(selected)) {

                                showTroubleshooting();

                            } else if (
                                    "Profi-Modus"
                                            .equals(selected)
                            ) {

                                requestProfi();

                            } else {

                                profi = false;

                                getSharedPreferences(
                                        PREFS,
                                        MODE_PRIVATE
                                )
                                        .edit()
                                        .putBoolean(
                                                KEY_PROFI,
                                                false
                                        )
                                        .apply();

                                Toast.makeText(
                                        this,
                                        "Profi-Modus ausgeschaltet.",
                                        Toast.LENGTH_SHORT
                                ).show();

                                showHome();
                            }
                        }
                )
                .show();
    }

    private void requestProfi() {

        final EditText input =
                new EditText(this);

        input.setHint(
                "Passwort"
        );

        input.setInputType(
                android.text.InputType.TYPE_CLASS_TEXT
                        | android.text.InputType
                        .TYPE_TEXT_VARIATION_PASSWORD
        );

        input.setTextColor(TEXT);
        input.setHintTextColor(MUTED);

        new AlertDialog.Builder(this)
                .setTitle(
                        "Profi-Modus"
                )
                .setMessage(
                        "Bitte Profi-Passwort eingeben."
                )
                .setView(input)
                .setPositiveButton(
                        "Anmelden",
                        (dialog, which) -> {

                            if (MASTER.equals(
                                    input.getText()
                                            .toString()
                            )) {

                                profi = true;

                                getSharedPreferences(
                                        PREFS,
                                        MODE_PRIVATE
                                )
                                        .edit()
                                        .putBoolean(
                                                KEY_PROFI,
                                                true
                                        )
                                        .apply();

                                Toast.makeText(
                                        this,
                                        "Profi-Modus aktiviert.",
                                        Toast.LENGTH_SHORT
                                ).show();

                                showHome();

                            } else {

                                Toast.makeText(
                                        this,
                                        "Falsches Passwort.",
                                        Toast.LENGTH_SHORT
                                ).show();
                            }
                        }
                )
                .setNegativeButton(
                        "Abbrechen",
                        null
                )
                .show();
    }

    private void saveErrors() {

        try {

            JSONArray array =
                    new JSONArray();

            for (ErrorItem item : errors) {

                JSONObject object =
                        new JSONObject();

                object.put(
                        "title",
                        item.title
                );

                object.put(
                        "solution",
                        item.solution
                );

                object.put(
                        "imagePath",
                        item.imagePath == null
                                ? ""
                                : item.imagePath
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

        SharedPreferences prefs =
                getSharedPreferences(
                        PREFS,
                        MODE_PRIVATE
                );

        String saved =
                prefs.getString(
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

                errors.add(
                        new ErrorItem(
                                object.optString(
                                        "title"
                                ),
                                object.optString(
                                        "solution"
                                ),
                                object.optString(
                                        "imagePath",
                                        ""
                                )
                        )
                );
            }

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

        layout.setBackgroundColor(
                PANEL
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
        button.setBackgroundColor(
                PANEL_LIGHT
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
        button.setBackgroundColor(PANEL);

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
        button.setBackgroundColor(
                PANEL_LIGHT
        );

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        -1,
                        dp(45)
                );

        params.topMargin =
                dp(12);

        button.setLayoutParams(params);

        return button;
    }

    private void addSpace(
            int size
    ) {

        View space =
                new View(this);

        content.addView(
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
        );
    }

    private static class ErrorItem {

        String title;
        String solution;
        String imagePath;

        ErrorItem(
                String title,
                String solution,
                String imagePath
        ) {

            this.title = title;
            this.solution = solution;
            this.imagePath = imagePath;
        }
    }
}
