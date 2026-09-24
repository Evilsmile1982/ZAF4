package com.zaero.diamondclean;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
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

public class MainActivity extends Activity {

    private static final int BG = Color.rgb(15, 13, 13);
    private static final int PANEL = Color.rgb(31, 27, 27);
    private static final int GOLD = Color.rgb(214, 168, 79);
    private static final int TEXT = Color.rgb(245, 240, 232);
    private static final int MUTED = Color.rgb(190, 180, 170);

    private static final String PREFS = "zaero_diamond_clean";
    private static final String KEY_ERRORS = "errors";

    private LinearLayout root;
    private LinearLayout content;

    private final ArrayList<ErrorItem> errors = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loadErrors();
        buildBase();
        showHome();
    }

    private void buildBase() {
        root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(BG);

        setContentView(root);
    }

    private void clearScreen() {
        root.removeAllViews();
    }

    private TextView text(String value, float size, int color) {
        TextView v = new TextView(this);
        v.setText(value);
        v.setTextSize(size);
        v.setTextColor(color);
        v.setPadding(dp(8), dp(6), dp(8), dp(6));
        return v;
    }

    private TextView title(String value) {
        TextView v = text(value, 25, TEXT);
        v.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        v.setGravity(Gravity.CENTER);
        return v;
    }

    private Button button(String value) {
        Button b = new Button(this);
        b.setText(value);
        b.setTextColor(TEXT);
        b.setTextSize(16);
        b.setAllCaps(false);
        b.setBackgroundColor(PANEL);
        return b;
    }

    private LinearLayout card() {
        LinearLayout box = new LinearLayout(this);
        box.setOrientation(LinearLayout.VERTICAL);
        box.setPadding(dp(18), dp(16), dp(18), dp(16));
        box.setBackgroundColor(PANEL);

        LinearLayout.LayoutParams p =
                new LinearLayout.LayoutParams(-1, -2);
        p.setMargins(dp(10), dp(8), dp(10), dp(8));
        box.setLayoutParams(p);

        return box;
    }

    private void addHeader(String pageTitle) {

        LinearLayout header = new LinearLayout(this);
        header.setOrientation(LinearLayout.HORIZONTAL);
        header.setGravity(Gravity.CENTER_VERTICAL);
        header.setPadding(dp(8), dp(8), dp(8), dp(8));

        Button menu = button("☰");
        menu.setTextSize(24);

        LinearLayout.LayoutParams menuParams =
                new LinearLayout.LayoutParams(dp(55), dp(55));

        header.addView(menu, menuParams);

        TextView heading = title(pageTitle);
        LinearLayout.LayoutParams headingParams =
                new LinearLayout.LayoutParams(0, -2, 1);

        header.addView(heading, headingParams);

        TextView brand = text("Z.AERO", 15, GOLD);
        brand.setTypeface(Typeface.DEFAULT, Typeface.BOLD);

        header.addView(brand,
                new LinearLayout.LayoutParams(dp(75), -2));

        root.addView(header);

        menu.setOnClickListener(v -> showMenu());
    }

    private void showHome() {

        clearScreen();
        addHeader("Z.AERO");

        ScrollView scroll = new ScrollView(this);

        content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setPadding(dp(12), dp(18), dp(12), dp(30));

        scroll.addView(content);
        root.addView(scroll,
                new LinearLayout.LayoutParams(-1, 0, 1));

        TextView welcome = text(
                "Willkommen bei",
                21,
                MUTED
        );

        welcome.setGravity(Gravity.CENTER);
        content.addView(welcome);

        TextView machineName = text(
                "Z-Aero Diamond Clean",
                30,
                GOLD
        );

        machineName.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        machineName.setGravity(Gravity.CENTER);

        content.addView(machineName);

        TextView subtitle = text(
                "Ihre Unterstützung für den sicheren " +
                "und effizienten Betrieb der Anlage.",
                16,
                TEXT
        );

        subtitle.setGravity(Gravity.CENTER);
        subtitle.setPadding(dp(20), dp(8), dp(20), dp(18));

        content.addView(subtitle);

        animateWelcome(welcome);
        animateWelcome(machineName);

        LinearLayout machine = card();

        TextView machineIcon = text(
                "◈",
                70,
                GOLD
        );

        machineIcon.setGravity(Gravity.CENTER);

        machine.addView(machineIcon);

        TextView machineText = text(
                "DIAMOND CLEAN\n" +
                "Bedienerinformation",
                18,
                TEXT
        );

        machineText.setGravity(Gravity.CENTER);
        machine.addView(machineText);

        content.addView(machine);

        addHomeCard(
                "▶  Anfahren",
                "Anlage starten",
                v -> showChapter("Anfahren")
        );

        addHomeCard(
                "■  Abstellen",
                "Anlage sicher herunterfahren",
                v -> showChapter("Abstellen")
        );

        addHomeCard(
                "⌕  Fehlersuche",
                "Fehler und Lösungen",
                v -> showErrors()
        );
    }

    private void addHomeCard(
            String heading,
            String description,
            View.OnClickListener listener) {

        LinearLayout box = card();

        TextView h = text(heading, 21, GOLD);
        h.setTypeface(Typeface.DEFAULT, Typeface.BOLD);

        TextView d = text(description, 15, MUTED);

        box.addView(h);
        box.addView(d);

        box.setOnClickListener(listener);

        content.addView(box);
    }

    private void showChapter(String chapter) {

        clearScreen();
        addHeader(chapter);

        ScrollView scroll = new ScrollView(this);

        LinearLayout page = new LinearLayout(this);
        page.setOrientation(LinearLayout.VERTICAL);
        page.setPadding(dp(15), dp(20), dp(15), dp(30));

        scroll.addView(page);

        root.addView(scroll,
                new LinearLayout.LayoutParams(-1, 0, 1));

        TextView heading = text(chapter, 30, GOLD);
        heading.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        heading.setGravity(Gravity.CENTER);

        page.addView(heading);

        String description;

        if (chapter.equals("Anfahren")) {
            description =
                    "Hier wird Schritt für Schritt beschrieben, " +
                    "wie die Anlage gestartet wird.\n\n" +
                    "Die genaue Bedienanleitung kann später " +
                    "hier ergänzt werden.";
        } else {
            description =
                    "Hier wird Schritt für Schritt beschrieben, " +
                    "wie die Anlage sicher abgestellt wird.\n\n" +
                    "Die genaue Bedienanleitung kann später " +
                    "hier ergänzt werden.";
        }

        LinearLayout info = card();

        TextView infoText = text(
                description,
                17,
                TEXT
        );

        infoText.setPadding(dp(8), dp(10), dp(8), dp(10));
        info.addView(infoText);

        page.addView(info);

        Button back = button("← Zur Startseite");

        back.setOnClickListener(v -> showHome());

        page.addView(back,
                new LinearLayout.LayoutParams(-1, dp(60)));
    }

    private void showErrors() {

        clearScreen();
        addHeader("Fehlersuche");

        ScrollView scroll = new ScrollView(this);

        LinearLayout page = new LinearLayout(this);
        page.setOrientation(LinearLayout.VERTICAL);
        page.setPadding(dp(12), dp(15), dp(12), dp(30));

        scroll.addView(page);

        root.addView(scroll,
                new LinearLayout.LayoutParams(-1, 0, 1));

        TextView heading = text(
                "Fehler und Lösungen",
                27,
                GOLD
        );

        heading.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        heading.setGravity(Gravity.CENTER);

        page.addView(heading);

        Button search = button("⌕ Fehler suchen");

        search.setOnClickListener(v -> searchErrors());

        page.addView(search,
                new LinearLayout.LayoutParams(-1, dp(58)));

        Button add = button("＋ Fehler anlegen");

        add.setOnClickListener(v -> addErrorDialog());

        page.addView(add,
                new LinearLayout.LayoutParams(-1, dp(58)));

        if (errors.isEmpty()) {

            LinearLayout empty = card();

            TextView emptyText = text(
                    "Noch keine Fehler gespeichert.\n\n" +
                    "Als Profi können hier später " +
                    "Fehler und Lösungen angelegt werden.",
                    17,
                    MUTED
            );

            emptyText.setGravity(Gravity.CENTER);
            empty.addView(emptyText);

            page.addView(empty);

        } else {

            for (ErrorItem error : errors) {
                addErrorCard(page, error);
            }
        }

        Button back = button("← Zur Startseite");

        back.setOnClickListener(v -> showHome());

        page.addView(back,
                new LinearLayout.LayoutParams(-1, dp(58)));
    }

    private void addErrorCard(
            LinearLayout page,
            ErrorItem error) {

        LinearLayout box = card();

        TextView heading = text(
                error.title,
                20,
                GOLD
        );

        heading.setTypeface(Typeface.DEFAULT, Typeface.BOLD);

        TextView description = text(
                error.description,
                16,
                TEXT
        );

        box.addView(heading);
        box.addView(description);

        if (!error.solution.isEmpty()) {

            TextView solution = text(
                    "Lösung:\n" + error.solution,
                    16,
                    MUTED
            );

            box.addView(solution);
        }

        page.addView(box);
    }

    private void addErrorDialog() {

        LinearLayout form = new LinearLayout(this);
        form.setOrientation(LinearLayout.VERTICAL);
        form.setPadding(dp(20), dp(10), dp(20), dp(5));

        EditText title =
                edit("Fehler / Meldung");

        EditText description =
                edit("Beschreibung");

        EditText solution =
                edit("Lösung");

        form.addView(title);
        form.addView(description);
        form.addView(solution);

        AlertDialog dialog =
                new AlertDialog.Builder(this)
                        .setTitle("Fehler anlegen")
                        .setView(form)
                        .setNegativeButton("Abbrechen", null)
                        .setPositiveButton("Speichern", null)
                        .create();

        dialog.setOnShowListener(v -> {

            dialog.getButton(
                    AlertDialog.BUTTON_POSITIVE
            ).setOnClickListener(x -> {

                String t =
                        title.getText().toString().trim();

                String d =
                        description.getText().toString().trim();

                String s =
                        solution.getText().toString().trim();

                if (t.isEmpty()) {
                    Toast.makeText(
                            this,
                            "Bitte einen Fehler eingeben.",
                            Toast.LENGTH_SHORT
                    ).show();
                    return;
                }

                errors.add(
                        new ErrorItem(t, d, s)
                );

                saveErrors();

                dialog.dismiss();
                showErrors();
            });
        });

        dialog.show();
    }

    private EditText edit(String hint) {

        EditText e = new EditText(this);

        e.setHint(hint);
        e.setTextColor(TEXT);
        e.setHintTextColor(MUTED);
        e.setTextSize(16);
        e.setPadding(
                dp(12),
                dp(8),
                dp(12),
                dp(8)
        );

        return e;
    }

    private void searchErrors() {

        EditText input =
                edit("Suchbegriff");

        new AlertDialog.Builder(this)
                .setTitle("Fehler suchen")
                .setView(input)
                .setNegativeButton(
                        "Abbrechen",
                        null
                )
                .setPositiveButton(
                        "Suchen",
                        (dialog, which) -> {

                            String q =
                                    input.getText()
                                            .toString()
                                            .trim()
                                            .toLowerCase();

                            if (q.isEmpty()) {
                                showErrors();
                                return;
                            }

                            showSearchResults(q);
                        }
                )
                .show();
    }

    private void showSearchResults(String q) {

        clearScreen();
        addHeader("Suchergebnis");

        ScrollView scroll = new ScrollView(this);

        LinearLayout page = new LinearLayout(this);
        page.setOrientation(LinearLayout.VERTICAL);
        page.setPadding(dp(12), dp(15), dp(12), dp(30));

        scroll.addView(page);

        root.addView(scroll,
                new LinearLayout.LayoutParams(-1, 0, 1));

        int found = 0;

        for (ErrorItem error : errors) {

            String all =
                    (error.title + " " +
                     error.description + " " +
                     error.solution)
                            .toLowerCase();

            if (all.contains(q)) {

                addErrorCard(page, error);
                found++;
            }
        }

        if (found == 0) {

            TextView none = text(
                    "Kein passender Fehler gefunden.",
                    18,
                    MUTED
            );

            none.setGravity(Gravity.CENTER);
            page.addView(none);
        }

        Button back = button("← Zur Fehlersuche");

        back.setOnClickListener(v -> showErrors());

        page.addView(back,
                new LinearLayout.LayoutParams(-1, dp(58)));
    }

    private void showMenu() {

        String[] items = {
                "Startseite",
                "Anfahren",
                "Abstellen",
                "Fehlersuche"
       
