package com.zaero.diamondclean;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.SharedPreferences;
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

import org.json.JSONArray;
import org.json.JSONObject;

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

    private static final String PREFS = "zaero_data";
    private static final String KEY_ERRORS = "errors";

    private static final String MASTER = "Chefe";

    private LinearLayout root;
    private LinearLayout content;

    private boolean profi = false;

    private final List<ErrorItem> errors = new ArrayList<ErrorItem>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loadErrors();
        buildBase();
        showHome();
    }

    // ============================================================
    // GRUNDLAYOUT
    // ============================================================

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
        content.setPadding(
                dp(18),
                dp(18),
                dp(18),
                dp(30)
        );

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
        header.setPadding(
                dp(12),
                dp(8),
                dp(12),
                dp(8)
        );
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
                new LinearLayout.LayoutParams(
                        dp(52),
                        dp(52)
                )
        );

        TextView title = new TextView(this);
        title.setText("Z.AERO");
        title.setTextColor(GOLD);
        title.setTextSize(21);
        title.setTypeface(Typeface.DEFAULT_BOLD);
        title.setGravity(Gravity.CENTER);

        header.addView(
                title,
                new LinearLayout.LayoutParams(
                        0,
                        dp(52),
                        1
                )
        );

        TextView user = new TextView(this);
        user.setText(profi ? "PROFI" : "BENUTZER");
        user.setTextColor(TEXT);
        user.setTextSize(12);
        user.setGravity(Gravity.CENTER);

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

    // ============================================================
    // STARTSEITE
    // ============================================================

    private void showHome() {

        clearContent();

        TextView welcome = text(
                "Willkommen bei",
                22,
                GOLD,
                true
        );

        welcome.setGravity(Gravity.CENTER);

        content.addView(
                welcome,
                new LinearLayout.LayoutParams(
                        -1,
                        -2
                )
        );

        TextView brand = text(
                "Z-Aero Diamond Clean",
                30,
                TEXT,
                true
        );

        brand.setGravity(Gravity.CENTER);
        brand.setPadding(
                0,
                dp(5),
                0,
                dp(8)
        );

        content.addView(brand);

        TextView subtitle = text(
                "Ihre Unterstützung für den sicheren und effizienten Betrieb der Anlage.",
                15,
                MUTED,
                false
        );

        subtitle.setGravity(Gravity.CENTER);
        subtitle.setPadding(
                dp(10),
                0,
                dp(10),
                dp(20)
        );

        content.addView(subtitle);

        // --------------------------------------------------------
        // Maschinenbereich
        // --------------------------------------------------------

        LinearLayout machinePanel = panel();

        machinePanel.setGravity(Gravity.CENTER);
        machinePanel.setPadding(
                dp(15),
                dp(20),
                dp(15),
                dp(20)
        );

        TextView machine = text(
                "Z-AERO\n◆ DIAMOND CLEAN ◆",
                25,
                GOLD,
                true
        );

        machine.setGravity(Gravity.CENTER);
        machine.setPadding(
                dp(10),
                dp(35),
                dp(10),
                dp(35)
        );

        machinePanel.addView(
                machine,
                new LinearLayout.LayoutParams(
                        -1,
                        -2
                )
        );

        content.addView(
                machinePanel,
                new LinearLayout.LayoutParams(
                        -1,
                        -2
                )
        );

        addSpace(18);

        // --------------------------------------------------------
        // ANFAHREN
        // --------------------------------------------------------

        addHomeCard(
                "▶  Anfahren",
                "Anlage starten",
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showChapter("Anfahren");
                    }
                }
        );

        // --------------------------------------------------------
        // ABSTELLEN
        // --------------------------------------------------------

        addHomeCard(
                "■  Abstellen",
                "Anlage sicher herunterfahren",
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showChapter("Abstellen");
                    }
                }
        );

        // --------------------------------------------------------
        // FEHLERSUCHE
        // --------------------------------------------------------

        addHomeCard(
                "⌕  Fehlersuche",
                "Fehler und Lösungen",
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showTroubleshooting();
                    }
                }
        );

        // Animation von links nach rechts
        Animation animation = new TranslateAnimation(
                -dp(350),
                0,
                0,
                0
        );

        animation.setDuration(700);
        animation.setStartOffset(150);

        welcome.startAnimation(animation);
    }

    private void addHomeCard(
            String title,
            String subtitle,
            View.OnClickListener listener
    ) {

        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setGravity(Gravity.CENTER_VERTICAL);
        card.setPadding(
                dp(20),
                dp(18),
                dp(20),
                dp(18)
        );
        card.setBackgroundColor(PANEL);

        TextView titleView = text(
                title,
                20,
                GOLD,
                true
        );

        TextView subtitleView = text(
                subtitle,
                14,
                MUTED,
                false
        );

        card.addView(titleView);

        LinearLayout.LayoutParams subParams =
                new LinearLayout.LayoutParams(
                        -1,
                        -2
                );

        subParams.topMargin = dp(5);

        card.addView(
                subtitleView,
                subParams
        );

        card.setOnClickListener(listener);

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        -1,
                        -2
                );

        params.bottomMargin = dp(12);

        content.addView(card, params);
    }

    // ============================================================
    // ANFAHREN / ABSTELLEN
    // ============================================================

    private void showChapter(String chapter) {

        clearContent();

        Button back = button("←  Startseite");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHome();
            }
        });

        content.addView(back);

        addSpace(15);

        TextView heading = text(
                chapter,
                28,
                GOLD,
                true
        );

        content.addView(heading);

        addSpace(10);

        LinearLayout info = panel();

        String description;

        if ("Anfahren".equals(chapter)) {
            description =
                    "Hier wird Schritt für Schritt beschrieben, " +
                    "wie die Z-Aero Diamond Clean Anlage gestartet wird.\n\n" +
                    "Die ausführlichen Arbeitsschritte können später " +
                    "hier ergänzt werden.";
        } else {
            description =
                    "Hier wird Schritt für Schritt beschrieben, " +
                    "wie die Z-Aero Diamond Clean Anlage sicher " +
                    "abgestellt wird.\n\n" +
                    "Die ausführlichen Arbeitsschritte können später " +
                    "hier ergänzt werden.";
        }

        TextView body = text(
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

    // ============================================================
    // FEHLERSUCHE
    // ============================================================

    private void showTroubleshooting() {

        clearContent();

        Button back = button("←  Startseite");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHome();
            }
        });

        content.addView(back);

        addSpace(15);

        TextView heading = text(
                "Fehlersuche",
                28,
                GOLD,
                true
        );

        content.addView(heading);

        TextView info = text(
                "Fehler, Ursachen und Lösungen",
                15,
                MUTED,
                false
        );

        content.addView(info);

        addSpace(12);

        Button search = button("⌕  Fehler suchen");

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSearch();
            }
        });

        content.addView(search);

        if (profi) {

            Button add = button("＋  Fehler anlegen");

            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editError(null);
                }
            });

            content.addView(add);
        }

        addSpace(15);

        if (errors.isEmpty()) {

            LinearLayout empty = panel();

            TextView emptyText = text(
                    "Noch keine Fehler gespeichert.\n\n" +
                    "Als Profi kannst du hier Fehler und Lösungen anlegen.",
                    16,
                    MUTED,
                    false
            );

            emptyText.setGravity(Gravity.CENTER);
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

            LinearLayout card = panel();

            TextView title = text(
                    item.title,
                    19,
                    GOLD,
                    true
            );

            card.addView(title);

            TextView solution = text(
                    item.solution,
                    15,
                    TEXT,
                    false
            );

            solution.setPadding(
                    0,
                    dp(8),
                    0,
                    0
            );

            card.addView(solution);

            if (profi) {

                Button edit = smallActionButton("Bearbeiten");

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

            params.bottomMargin = dp(12);

            content.addView(card, params);
        }
    }

    // ============================================================
    // SUCHE
    // ============================================================

    private void showSearch() {

        final EditText input = new EditText(this);

        input.setHint("Fehler suchen...");
        input.setTextColor(TEXT);
        input.setHintTextColor(MUTED);

        LinearLayout box = new LinearLayout(this);
        box.setPadding(
                dp(20),
                dp(5),
                dp(20),
                dp(5)
        );

        box.addView(
                input,
                new LinearLayout.LayoutParams(
                        -1,
                        -2
                )
        );

        new AlertDialog.Builder(this)
                .setTitle("Fehlersuche")
                .setView(box)
                .setPositiveButton(
                        "Suchen",
                        (dialog, which) -> {

                            String query =
                                    input.getText()
                                            .toString()
                                            .trim()
                                            .toLowerCase(
                                                    Locale.getDefault()
                                            );

                            showSearchResults(query);
                        }
                )
                .setNegativeButton(
                        "Abbrechen",
                        null
                )
                .show();
    }

    private void showSearchResults(String query) {

        clearContent();

        Button back = button("←  Fehlersuche");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTroubleshooting();
            }
        });

        content.addView(back);

        addSpace(15);

        TextView heading = text(
                "Suchergebnisse",
                25,
                GOLD,
                true
        );

        content.addView(heading);

        int count = 0;

        for (ErrorItem item : errors) {

            String combined =
                    (item.title + " " + item.solution)
                            .toLowerCase(
                                    Locale.getDefault()
                            );

            if (combined.contains(query)) {

                LinearLayout card = panel();

                card.addView(
                        text(
                                item.title,
                                19,
                                GOLD,
                                true
                        )
                );

                TextView solution = text(
                        item.solution,
                        15,
                        TEXT,
                        false
                );

                solution.setPadding(
                        0,
                        dp(8),
                        0,
                        0
                );

                card.addView(solution);

                content.addView(
                        card,
                        new LinearLayout.LayoutParams(
                                -1,
                                -2
                        )
                );

                addSpace(10);

                count++;
            }
        }

        if (count == 0) {

            TextView none = text(
                    "Kein passender Fehler gefunden.",
                    16,
                    MUTED,
                    false
            );

            content.addView(none);
        }
    }

    // ============================================================
    // FEHLER ANLEGEN / BEARBEITEN
    // ============================================================

    private void editError(final ErrorItem existing) {

        if (!profi) {
            Toast.makeText(
                    this,
                    "Nur im Profi-Modus möglich.",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        LinearLayout box = new LinearLayout(this);
        box.setOrientation(LinearLayout.VERTICAL);
        box.setPadding(
                dp(20),
                dp(5),
                dp(20),
                dp(5)
        );

        EditText title = new EditText(this);
        title.setHint("Fehler / Meldung");
        title.setTextColor(TEXT);
        title.setHintTextColor(MUTED);

        EditText solution = new EditText(this);
        solution.setHint("Ursache und Lösung");
        solution.setTextColor(TEXT);
        solution.setHintTextColor(MUTED);
        solution.setMinLines(5);
        solution.setGravity(Gravity.TOP);

        if (existing != null) {
            title.setText(existing.title);
            solution.setText(existing.solution);
        }

        box.addView(title);

        box.addView(
                solution,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(130)
                )
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

        final AlertDialog dialog = builder.create();

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
                                    public void onClick(View v) {

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
                                                    "Bitte beide Felder ausfüllen.",
                                                    Toast.LENGTH_SHORT
                                            ).show();

                                            return;
                                        }

                                        if (existing == null) {

                                            errors.add(
                                                    new ErrorItem(
                                                            t,
                                                            s
                                                    )
                                            );

                                        } else {

                                            existing.title = t;
                                            existing.solution = s;
                                        }

                                        saveErrors();
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
                                        public void onClick(View v) {

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

    // ============================================================
    // MENÜ
    // ============================================================

    private void showMenu() {

        final ArrayList<String> items =
                new ArrayList<String>();

        items.add("Startseite");
        items.add("Anfahren");
        items.add("Abstellen");
        items.add("Fehlersuche");
        items.add(profi
                ? "Profi-Modus ausschalten"
                : "Profi-Modus");

        final String[] menuItems =
                items.toArray(new String[items.size()]);

        new AlertDialog.Builder(this)
                .setTitle("Z-Aero Diamond Clean")
                .setItems(
                        menuItems,
                        (dialog, which) -> {

                            String selected =
                                    menuItems[which];

                            if ("Startseite".equals(selected)) {

                                showHome();

                            } else if ("Anfahren".equals(selected)) {

                                showChapter("Anfahren");

                            } else if ("Abstellen".equals(selected)) {

                                showChapter("Abstellen");

                            } else if ("Fehlersuche".equals(selected)) {

                                showTroubleshooting();

                            } else if (
                                    "Profi-Modus".equals(selected)
                            ) {

                                requestProfi();

                            } else if (
                                    "Profi-Modus ausschalten"
                                            .equals(selected)
                            ) {

                                profi = false;

                                Toast.makeText(
                                        this,
                                        "Profi-Modus ausgeschaltet.",
                                        Toast.LENGTH_SHORT
                                ).show();

                                recreate();
                            }
                        }
                )
                .show();
    }

    // ============================================================
    // PROFI-MODUS
    // ============================================================

    private void requestProfi() {

        final EditText input = new EditText(this);

        input.setHint("Passwort");
        input.setInputType(
                android.text.InputType.TYPE_CLASS_TEXT
                        | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
        );

        input.setTextColor(TEXT);
        input.setHintTextColor(MUTED);

        new AlertDialog.Builder(this)
                .setTitle("Profi-Modus")
                .setMessage(
                        "Bitte Profi-Passwort eingeben."
                )
                .setView(input)
                .setPositiveButton(
                        "Anmelden",
                        (dialog, which) -> {

                            if (MASTER.equals(
                                    input.getText().toString()
                            )) {

                                profi = true;

                                Toast.makeText(
                                        this,
                                        "Profi-Modus aktiviert.",
                                        Toast.LENGTH_SHORT
                                ).show();

                                recreate();

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

    // ============================================================
    // DATENSPEICHERUNG
    // ============================================================

    private void saveErrors() {

        try {

            JSONArray array = new JSONArray();

            for (ErrorItem item : errors) {

                JSONObject object = new JSONObject();

                object.put(
                        "title",
                        item.title
                );

                object.put(
                        "solution",
                        item.solution
                );

                array.put(object);
            }

            SharedPreferences prefs =
                    getSharedPreferences(
                            PREFS,
                            MODE_PRIVATE
                    );

            prefs.edit()
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

            for (int i = 0;
                    i < array.length();
                    i++) {

                JSONObject object =
                        array.getJSONObject(i);

                errors.add(
                        new ErrorItem(
                                object.optString("title"),
                                object.optString("solution")
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

    // ============================================================
    // UI-HILFSMETHODEN
    // ============================================================

    private void clearContent() {

        if (content != null) {
            content.removeAllViews();
        }
    }

    private TextView text(
            String value,
            float size,
            int color,
            boolean bold
    ) {

        TextView view = new TextView(this);

        view.setText(value);
        view.setTextSize(size);
        view.setTextColor(color);

        if (bold) {
            view.setTypeface(
                    Typeface.DEFAULT,
                    Typeface.BOLD
            );
        }

        view.setGravity(Gravity.CENTER_VERTICAL);

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

        layout.setBackgroundColor(PANEL);

        return layout;
    }

    private Button button(String label) {

        Button button = new Button(this);

        button.setText(label);
        button.setTextColor(TEXT);
        button.setTextSize(15);
        button.setAllCaps(false);
        button.setBackgroundColor(PANEL_LIGHT);

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        -1,
                        dp(52)
                );

        params.bottomMargin = dp(8);

        button.setLayoutParams(params);

        return button;
    }

    private Button smallButton(String label) {

        Button button = new Button(this);

        button.setText(label);
        button.setTextColor(GOLD);
        button.setTextSize(20);
        button.setAllCaps(false);
        button.setBackgroundColor(PANEL);

        return button;
    }

    private Button smallActionButton(String label) {

        Button button = new Button(this);

        button.setText(label);
        button.setTextColor(GOLD);
        button.setTextSize(13);
        button.setAllCaps(false);
        button.setBackgroundColor(PANEL_LIGHT);

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        -1,
                        dp(45)
                );

        params.topMargin = dp(12);

        button.setLayoutParams(params);

        return button;
    }

    private void addSpace(int size) {

        View space = new View(this);

        content.addView(
                space,
                new LinearLayout.LayoutParams(
                        1,
                        dp(size)
                )
        );
    }

    private int dp(int value) {

        return (int) (
                value
                        * getResources()
                        .getDisplayMetrics()
                        .density
        );
    }

    // ============================================================
    // FEHLERDATEN
    // ============================================================

    private static class ErrorItem {

        String title;
        String solution;

        ErrorItem(
                String title,
                String solution
        ) {

            this.title = title;
            this.solution = solution;
        }
    }
}
