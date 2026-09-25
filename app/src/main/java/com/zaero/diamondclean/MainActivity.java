package com.zaero.diamondclean;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ScaleGestureDetector;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
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
import java.util.Locale;

public class MainActivity extends Activity {

    private static final int GOLD = Color.rgb(214, 168, 79);
    private static final int BG = Color.rgb(12, 12, 14);
    private static final int PANEL = Color.rgb(28, 27, 30);
    private static final int PANEL2 = Color.rgb(40, 38, 42);
    private static final int TEXT = Color.rgb(245, 242, 235);
    private static final int MUTED = Color.rgb(180, 175, 168);
    private static final int RED = Color.rgb(220, 30, 35);
    private static final int GREEN = Color.rgb(30, 220, 70);

    private static final String PREFS = "zaero_data";
    private static final String ERRORS = "errors";
    private static final String PROFI = "profi";
    private static final String ANFAHREN_TEXT = "anfahren_text";
    private static final String ABSTELLEN_TEXT = "abstellen_text";
    private static final String ANFAHREN_IMAGES = "anfahren_images";
    private static final String ABSTELLEN_IMAGES = "abstellen_images";
    private static final String MASTER = "C1B2A3Z";

    private static final int IMAGE_REQUEST = 5001;
    private static final int CHAPTER_IMAGE_REQUEST = 5002;

    private LinearLayout root;
    private LinearLayout content;
    private EditText searchField;
    private LinearLayout searchResults;

    private boolean profi = false;
    private int editingIndex = -1;
    private String editingChapter = null;

    private final ArrayList<ErrorItem> errorList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        profi = getSharedPreferences(PREFS, MODE_PRIVATE)
                .getBoolean(PROFI, false);

        loadErrors();
        buildBase();
        showHome();
    }

    private int dp(int value) {
        return (int) (value * getResources().getDisplayMetrics().density + 0.5f);
    }

    private TextView tv(String text, float size, int color, boolean bold) {
        TextView v = new TextView(this);
        v.setText(text);
        v.setTextSize(size);
        v.setTextColor(color);
        v.setGravity(Gravity.CENTER_VERTICAL);
        if (bold) {
            v.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        }
        return v;
    }

    private GradientDrawable bg(int color, int radius, int stroke) {
        GradientDrawable g = new GradientDrawable();
        g.setColor(color);
        g.setCornerRadius(dp(radius));
        if (stroke != Color.TRANSPARENT) {
            g.setStroke(dp(1), stroke);
        }
        return g;
    }

    private Button btn(String text) {
        Button b = new Button(this);
        b.setText(text);
        b.setTextColor(TEXT);
        b.setTextSize(15);
        b.setAllCaps(false);
        b.setBackground(bg(PANEL2, 10, Color.TRANSPARENT));

        LinearLayout.LayoutParams p =
                new LinearLayout.LayoutParams(-1, dp(52));
        p.bottomMargin = dp(8);
        b.setLayoutParams(p);
        return b;
    }

    private Button smallBtn(String text) {
        Button b = new Button(this);
        b.setText(text);
        b.setTextColor(GOLD);
        b.setTextSize(20);
        b.setAllCaps(false);
        b.setBackground(bg(PANEL, 10, Color.TRANSPARENT));
        return b;
    }

    private LinearLayout panel() {
        LinearLayout p = new LinearLayout(this);
        p.setOrientation(LinearLayout.VERTICAL);
        p.setPadding(dp(16), dp(16), dp(16), dp(16));
        p.setBackground(bg(PANEL, 12, Color.TRANSPARENT));
        return p;
    }

    private void buildBase() {
        root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(BG);
        setContentView(root);

        LinearLayout header = new LinearLayout(this);
        header.setOrientation(LinearLayout.HORIZONTAL);
        header.setGravity(Gravity.CENTER_VERTICAL);
        header.setPadding(dp(12), dp(8), dp(12), dp(8));
        header.setBackgroundColor(Color.rgb(18, 17, 19));

        Button menu = smallBtn("☰");
        menu.setOnClickListener(v -> showMenu());

        header.addView(menu,
                new LinearLayout.LayoutParams(dp(52), dp(52)));

        TextView title = tv("Z.AERO", 21, GOLD, true);
        title.setGravity(Gravity.CENTER);

        header.addView(title,
                new LinearLayout.LayoutParams(0, dp(52), 1));

        TextView user = tv(profi ? "PROFI" : "BENUTZER",
                12, TEXT, false);
        user.setGravity(Gravity.CENTER);

        header.addView(user,
                new LinearLayout.LayoutParams(dp(82), dp(52)));

        root.addView(header,
                new LinearLayout.LayoutParams(-1, dp(68)));

        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(true);
        scroll.setBackgroundColor(BG);

        content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setGravity(Gravity.CENTER_HORIZONTAL);
        content.setPadding(dp(18), dp(18), dp(18), dp(30));

        scroll.addView(content,
                new ScrollView.LayoutParams(-1, -1));

        root.addView(scroll,
                new LinearLayout.LayoutParams(-1, 0, 1));
    }

    private void clear() {
        content.removeAllViews();
        searchField = null;
        searchResults = null;
    }

    private void showHome() {
        clear();
        content.setGravity(Gravity.CENTER);

        LinearLayout home = new LinearLayout(this);
        home.setOrientation(LinearLayout.VERTICAL);
        home.setGravity(Gravity.CENTER_HORIZONTAL);

        TextView welcome = tv("Willkommen bei", 22, GOLD, true);
        welcome.setGravity(Gravity.CENTER);

        home.addView(welcome,
                new LinearLayout.LayoutParams(-1, -2));

        FrameLayout brandWrap = new FrameLayout(this);

        TextView brand = tv("Z-Aero Diamond Clean ◆", 30, TEXT, true);
        brand.setGravity(Gravity.CENTER);
        brandWrap.addView(brand,
                new FrameLayout.LayoutParams(-1, -2));

        // Einfahr-Animation: 2,0 Sekunden.
        TranslateAnimation brandSlide = new TranslateAnimation(
                -dp(280), 0, 0, 0);
        brandSlide.setDuration(2000);

        AlphaAnimation brandFade = new AlphaAnimation(0f, 1f);
        brandFade.setDuration(2000);

        AnimationSet brandSet = new AnimationSet(true);
        brandSet.addAnimation(brandSlide);
        brandSet.addAnimation(brandFade);
        brand.startAnimation(brandSet);

        home.addView(brandWrap,
                new LinearLayout.LayoutParams(-1, dp(48)));

        TextView subtitle = tv(
                "Ihre Unterstützung für den sicheren und effizienten Betrieb der Anlage.",
                15, MUTED, false);
        subtitle.setGravity(Gravity.CENTER);
        subtitle.setPadding(dp(8), 0, dp(8), dp(10));

        home.addView(subtitle,
                new LinearLayout.LayoutParams(-1, -2));

        ImageView machine = new ImageView(this);
        machine.setImageResource(R.drawable.machine);
        machine.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        machine.setAdjustViewBounds(true);

        LinearLayout.LayoutParams imageParams =
                new LinearLayout.LayoutParams(-1, dp(205));
        imageParams.bottomMargin = dp(12);

        home.addView(machine, imageParams);

        LinearLayout cards = new LinearLayout(this);
        cards.setOrientation(LinearLayout.HORIZONTAL);
        cards.setGravity(Gravity.CENTER);

        addHomeCard(cards, "▶", "Anfahren",
                "Anlage starten",
                v -> showChapter("Anfahren"));

        addHomeCard(cards, "■", "Abstellen",
                "Anlage sicher\nherunterfahren",
                v -> showChapter("Abstellen"));

        addHomeCard(cards, "⌕", "Fehlersuche",
                "Fehler und\nLösungen",
                v -> showTroubleshooting());

        home.addView(cards,
                new LinearLayout.LayoutParams(-1, -2));

        // Die drei Startkarten fahren gleichzeitig von rechts nach links ein.
        TranslateAnimation cardsSlide = new TranslateAnimation(
                dp(420), 0, 0, 0);
        cardsSlide.setDuration(2000);

        AlphaAnimation cardsFade = new AlphaAnimation(0f, 1f);
        cardsFade.setDuration(2000);

        AnimationSet cardsSet = new AnimationSet(true);
        cardsSet.addAnimation(cardsSlide);
        cardsSet.addAnimation(cardsFade);
        cards.startAnimation(cardsSet);

        content.addView(home,
                new LinearLayout.LayoutParams(-1, -2));
    }

    private void addHomeCard(
            LinearLayout parent,
            String icon,
            String title,
            String subtitle,
            View.OnClickListener listener) {

        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setGravity(Gravity.CENTER);
        card.setPadding(dp(8), dp(10), dp(8), dp(10));
        card.setBackground(bg(PANEL, 12, GOLD));
        card.setOnClickListener(listener);

        TextView iconView = tv(icon, 25, GOLD, true);
        iconView.setGravity(Gravity.CENTER);

        card.addView(iconView,
                new LinearLayout.LayoutParams(-1, dp(34)));

        TextView titleView = tv(title, 16, TEXT, true);
        titleView.setGravity(Gravity.CENTER);

        card.addView(titleView,
                new LinearLayout.LayoutParams(-1, -2));

        TextView subtitleView = tv(subtitle, 11, MUTED, false);
        subtitleView.setGravity(Gravity.CENTER);

        card.addView(subtitleView,
                new LinearLayout.LayoutParams(-1, dp(34)));

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(0, dp(112), 1);

        params.setMargins(dp(4), 0, dp(4), 0);
        parent.addView(card, params);
    }

    private void showChapter(String chapter) {
        clear();

        content.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);

        TextView heading = tv(chapter, 28, GOLD, true);
        heading.setGravity(Gravity.CENTER);

        content.addView(heading,
                new LinearLayout.LayoutParams(-1, dp(60)));

        String text;

        if (chapter.equals("Anfahren")) {
            text = getSharedPreferences(PREFS, MODE_PRIVATE)
                    .getString(
                            ANFAHREN_TEXT,
                            "Hier kannst du später die genaue Anleitung zum sicheren Anfahren der Anlage eintragen.");
        } else {
            text = getSharedPreferences(PREFS, MODE_PRIVATE)
                    .getString(
                            ABSTELLEN_TEXT,
                            "Hier kannst du später die genaue Anleitung zum sicheren Abstellen der Anlage eintragen.");
        }

        LinearLayout box = panel();

        TextView textView = tv(text, 17, TEXT, false);
        textView.setPadding(dp(6), dp(6), dp(6), dp(6));

        box.addView(textView,
                new LinearLayout.LayoutParams(-1, -2));

        content.addView(box,
                new LinearLayout.LayoutParams(-1, -2));

        ArrayList<String> images = loadChapterImages(chapter);

        if (!images.isEmpty()) {
            addSpace(12);

            TextView photoHeading = tv("Fotos", 20, GOLD, true);
            photoHeading.setGravity(Gravity.CENTER);

            content.addView(photoHeading,
                    new LinearLayout.LayoutParams(-1, dp(44)));

            for (String path : images) {
                File file = new File(path);
                if (!file.exists()) {
                    continue;
                }

                ImageView image = new ImageView(this);
                image.setImageURI(Uri.fromFile(file));
                image.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                image.setAdjustViewBounds(true);
                image.setOnClickListener(v -> showZoomImage(file));

                LinearLayout.LayoutParams imageParams =
                        new LinearLayout.LayoutParams(-1, dp(220));
                imageParams.bottomMargin = dp(10);

                content.addView(image, imageParams);
            }
        }

        if (profi) {
            addSpace(12);

            Button edit = btn("✎ " + chapter + " bearbeiten");
            edit.setOnClickListener(v -> editChapter(chapter));
            content.addView(edit);
        }

        addSpace(8);

        Button back = btn("← Zurück");
        back.setOnClickListener(v -> showHome());
        content.addView(back);
    }

    private void editChapter(String chapter) {
        editingChapter = chapter;

        String oldText;

        if (chapter.equals("Anfahren")) {
            oldText = getSharedPreferences(PREFS, MODE_PRIVATE)
                    .getString(
                            ANFAHREN_TEXT,
                            "Hier kannst du später die genaue Anleitung zum sicheren Anfahren der Anlage eintragen.");
        } else {
            oldText = getSharedPreferences(PREFS, MODE_PRIVATE)
                    .getString(
                            ABSTELLEN_TEXT,
                            "Hier kannst du später die genaue Anleitung zum sicheren Abstellen der Anlage eintragen.");
        }

        LinearLayout box = new LinearLayout(this);
        box.setOrientation(LinearLayout.VERTICAL);
        box.setPadding(dp(8), dp(4), dp(8), dp(4));

        EditText input = new EditText(this);
        input.setText(oldText);
        input.setTextColor(TEXT);
        input.setTextSize(16);
        input.setGravity(Gravity.TOP);
        input.setSingleLine(false);
        input.setMinLines(8);
        input.setPadding(dp(12), dp(12), dp(12), dp(12));
        input.setBackground(bg(PANEL2, 10, GOLD));

        box.addView(input,
                new LinearLayout.LayoutParams(-1, dp(190)));

        addSpaceTo(box, 10);

        TextView photoTitle =
                tv("Fotos zu " + chapter, 18, GOLD, true);

        box.addView(photoTitle,
                new LinearLayout.LayoutParams(-1, dp(40)));

        ArrayList<String> images = loadChapterImages(chapter);

        for (String path : images) {
            addChapterEditImage(box, chapter, path);
        }

        Button addImage = btn("+ Foto hinzufügen");

        addImage.setOnClickListener(v -> {
            String newText = input.getText().toString().trim();

            if (newText.isEmpty()) {
                Toast.makeText(this,
                        "Der Text darf nicht leer sein.",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            saveChapterText(chapter, newText);

            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");

            startActivityForResult(intent, CHAPTER_IMAGE_REQUEST);
        });

        box.addView(addImage);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(chapter + " bearbeiten")
                .setView(box)
                .setNegativeButton("Abbrechen", null)
                .setPositiveButton("Speichern", null)
                .create();

        dialog.setOnShowListener(d -> {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                    .setOnClickListener(v -> {
                        String newText =
                                input.getText().toString().trim();

                        if (newText.isEmpty()) {
                            Toast.makeText(this,
                                    "Der Text darf nicht leer sein.",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }

                        saveChapterText(chapter, newText);
                        dialog.dismiss();
                        editingChapter = null;
                        showChapter(chapter);
                    });
        });

        dialog.show();
    }

    private void saveChapterText(String chapter, String text) {
        String key = chapter.equals("Anfahren")
                ? ANFAHREN_TEXT
                : ABSTELLEN_TEXT;

        getSharedPreferences(PREFS, MODE_PRIVATE)
                .edit()
                .putString(key, text)
                .apply();
    }

    private ArrayList<String> loadChapterImages(String chapter) {
        ArrayList<String> result = new ArrayList<>();

        String key = chapter.equals("Anfahren")
                ? ANFAHREN_IMAGES
                : ABSTELLEN_IMAGES;

        String saved = getSharedPreferences(PREFS, MODE_PRIVATE)
                .getString(key, "");

        if (saved.isEmpty()) {
            return result;
        }

        try {
            JSONArray array = new JSONArray(saved);

            for (int i = 0; i < array.length(); i++) {
                String path = array.optString(i, "");

                if (!path.isEmpty() && !result.contains(path)) {
                    result.add(path);
                }
            }
        } catch (Exception ignored) {
        }

        return result;
    }

    private void saveChapterImages(
            String chapter,
            ArrayList<String> images) {

        String key = chapter.equals("Anfahren")
                ? ANFAHREN_IMAGES
                : ABSTELLEN_IMAGES;

        JSONArray array = new JSONArray();

        for (String path : images) {
            array.put(path);
        }

        getSharedPreferences(PREFS, MODE_PRIVATE)
                .edit()
                .putString(key, array.toString())
                .apply();
    }

    private void addChapterEditImage(
            LinearLayout parent,
            String chapter,
            String path) {

        File file = new File(path);

        if (!file.exists()) {
            return;
        }

        ImageView image = new ImageView(this);
        image.setImageURI(Uri.fromFile(file));
        image.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        image.setAdjustViewBounds(true);
        image.setOnClickListener(v -> showZoomImage(file));

        parent.addView(image,
                new LinearLayout.LayoutParams(-1, dp(150)));

        Button remove = btn("Foto entfernen");

        remove.setOnClickListener(v -> {
            ArrayList<String> images =
                    loadChapterImages(chapter);

            images.remove(path);
            saveChapterImages(chapter, images);
            editChapter(chapter);
        });

        parent.addView(remove);
    }

    private void showTroubleshooting() {
        clear();

        content.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);

        TextView heading = tv("Fehlersuche", 28, GOLD, true);
        heading.setGravity(Gravity.CENTER);

        content.addView(heading,
                new LinearLayout.LayoutParams(-1, dp(60)));

        searchField = new EditText(this);
        searchField.setHint("Fehler suchen...");
        searchField.setHintTextColor(MUTED);
        searchField.setTextColor(TEXT);
        searchField.setTextSize(16);
        searchField.setSingleLine(true);
        searchField.setPadding(dp(14), 0, dp(14), 0);
        searchField.setBackground(bg(PANEL2, 10, GOLD));

        content.addView(searchField,
                new LinearLayout.LayoutParams(-1, dp(52)));

        searchResults = new LinearLayout(this);
        searchResults.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams resultsParams =
                new LinearLayout.LayoutParams(-1, -2);
        resultsParams.topMargin = dp(12);

        content.addView(searchResults, resultsParams);

        searchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(
                    CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(
                    CharSequence s, int start, int before, int count) {
                updateSearchResults(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        updateSearchResults("");

        if (profi) {
            addSpace(12);

            Button add = btn("+ Fehler hinzufügen");
            add.setOnClickListener(v -> showEditError(-1));
            content.addView(add);
        }

        addSpace(8);

        Button back = btn("← Zurück");
        back.setOnClickListener(v -> showHome());
        content.addView(back);
    }

    private void updateSearchResults(String query) {
        if (searchResults == null) {
            return;
        }

        searchResults.removeAllViews();

        String q = query.trim().toLowerCase(Locale.GERMAN);

        if (q.length() > 0 && q.length() < 3) {
            TextView hint =
                    tv("Bitte mindestens 3 Zeichen eingeben.",
                            14, MUTED, false);
            hint.setGravity(Gravity.CENTER);
            searchResults.addView(hint,
                    new LinearLayout.LayoutParams(-1, dp(50)));
            return;
        }

        int count = 0;

        for (int i = 0; i < errorList.size(); i++) {
            ErrorItem item = errorList.get(i);

            boolean match = q.isEmpty()
                    || item.title.toLowerCase(Locale.GERMAN).contains(q)
                    || item.cause.toLowerCase(Locale.GERMAN).contains(q)
                    || item.solution.toLowerCase(Locale.GERMAN).contains(q);

            if (!match) {
                continue;
            }

            addErrorResult(i, item);
            count++;
        }

        if (count == 0) {
            TextView empty =
                    tv(q.isEmpty()
                                    ? "Noch keine Fehler hinterlegt."
                                    : "Kein passender Fehler gefunden.",
                            15, MUTED, false);
            empty.setGravity(Gravity.CENTER);
            searchResults.addView(empty,
                    new LinearLayout.LayoutParams(-1, dp(60)));
        }
    }

    private void addErrorResult(int index, ErrorItem item) {
        LinearLayout card = panel();

        // FEHLER: fett und Gold, der Fehlerbereich ist rot umrandet.
        LinearLayout errorBox = new LinearLayout(this);
        errorBox.setOrientation(LinearLayout.VERTICAL);
        errorBox.setPadding(dp(12), dp(10), dp(12), dp(10));
        errorBox.setBackground(bg(PANEL2, 10, RED));

        TextView errorLabel = tv("FEHLER", 14, GOLD, true);
        errorBox.addView(errorLabel,
                new LinearLayout.LayoutParams(-1, dp(26)));

        TextView title = tv(item.title, 20, GOLD, true);
        errorBox.addView(title,
                new LinearLayout.LayoutParams(-1, -2));

        card.addView(errorBox,
                new LinearLayout.LayoutParams(-1, -2));

        addSpaceTo(card, 10);

        TextView causeLabel =
                tv("Ursache", 13, RED, true);
        card.addView(causeLabel,
                new LinearLayout.LayoutParams(-1, dp(28)));

        TextView cause = tv(item.cause, 15, TEXT, false);
        cause.setPadding(0, 0, 0, dp(8));
        card.addView(cause,
                new LinearLayout.LayoutParams(-1, -2));

        // LÖSUNG: fett und grün, der Lösungsbereich ist grün umrandet.
        LinearLayout solutionBox = new LinearLayout(this);
        solutionBox.setOrientation(LinearLayout.VERTICAL);
        solutionBox.setPadding(dp(12), dp(10), dp(12), dp(10));
        solutionBox.setBackground(bg(PANEL2, 10, GREEN));

        TextView solutionLabel = tv("LÖSUNG", 14, GREEN, true);
        solutionBox.addView(solutionLabel,
                new LinearLayout.LayoutParams(-1, dp(26)));

        TextView solution = tv(item.solution, 15, GREEN, true);
        solutionBox.addView(solution,
                new LinearLayout.LayoutParams(-1, -2));

        card.addView(solutionBox,
                new LinearLayout.LayoutParams(-1, -2));

        if (!item.images.isEmpty()) {
            addSpaceTo(card, 8);

            for (String path : item.images) {
                File file = new File(path);

                if (!file.exists()) {
                    continue;
                }

                ImageView image = new ImageView(this);
                image.setImageURI(Uri.fromFile(file));
                image.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                image.setAdjustViewBounds(true);
                image.setOnClickListener(v -> showZoomImage(file));

                card.addView(image,
                        new LinearLayout.LayoutParams(-1, dp(150)));
            }
        }

        if (profi) {
            Button edit = btn("✎ Bearbeiten");
            edit.setOnClickListener(v -> showEditError(index));
            card.addView(edit);
        }

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(-1, -2);
        params.bottomMargin = dp(10);

        searchResults.addView(card, params);
    }

    private void showEditError(int index) {
        editingIndex = index;

        String oldTitle = "";
        String oldCause = "";
        String oldSolution = "";

        if (index >= 0 && index < errorList.size()) {
            ErrorItem item = errorList.get(index);
            oldTitle = item.title;
            oldCause = item.cause;
            oldSolution = item.solution;
        }

        LinearLayout box = new LinearLayout(this);
        box.setOrientation(LinearLayout.VERTICAL);
        box.setPadding(dp(8), dp(4), dp(8), dp(4));

        EditText title = edit("Fehler / Titel");
        title.setText(oldTitle);

        EditText cause = edit("Ursache");
        cause.setText(oldCause);

        EditText solution = edit("Lösung");
        solution.setText(oldSolution);
        solution.setMinLines(4);
        solution.setGravity(Gravity.TOP);

        box.addView(title,
                new LinearLayout.LayoutParams(-1, dp(52)));
        box.addView(cause,
                new LinearLayout.LayoutParams(-1, dp(52)));
        box.addView(solution,
                new LinearLayout.LayoutParams(-1, dp(120)));

        if (index >= 0) {
            TextView imageTitle =
                    tv("Vorhandene Bilder", 17, GOLD, true);

            box.addView(imageTitle,
                    new LinearLayout.LayoutParams(-1, dp(40)));

            for (String path : errorList.get(index).images) {
                addEditImage(box, path);
            }
        }

        Button addImage = btn("+ Bild hinzufügen");

        addImage.setOnClickListener(v -> {
            String t = title.getText().toString().trim();
            String c = cause.getText().toString().trim();
            String s = solution.getText().toString().trim();

            if (t.isEmpty() || c.isEmpty() || s.isEmpty()) {
                Toast.makeText(this,
                        "Bitte alle drei Felder ausfüllen.",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            if (editingIndex < 0) {
                errorList.add(new ErrorItem(t, c, s));
                editingIndex = errorList.size() - 1;
            } else {
                ErrorItem item = errorList.get(editingIndex);
                item.title = t;
                item.cause = c;
                item.solution = s;
            }

            saveErrors();

            Intent intent =
                    new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");

            startActivityForResult(intent, IMAGE_REQUEST);
        });

        box.addView(addImage);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(index >= 0
                        ? "Fehler bearbeiten"
                        : "Fehler hinzufügen")
                .setView(box)
                .setNegativeButton("Abbrechen", null)
                .setPositiveButton("Speichern", null)
                .create();

        dialog.setOnShowListener(d -> {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                    .setOnClickListener(v -> {

                        String t = title.getText().toString().trim();
                        String c = cause.getText().toString().trim();
                        String s = solution.getText().toString().trim();

                        if (t.isEmpty() || c.isEmpty() || s.isEmpty()) {
                            Toast.makeText(this,
                                    "Alle drei Felder müssen ausgefüllt sein.",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (index < 0) {
                            errorList.add(new ErrorItem(t, c, s));
                        } else {
                            ErrorItem item = errorList.get(index);
                            item.title = t;
                            item.cause = c;
                            item.solution = s;
                        }

                        removeDuplicates();
                        saveErrors();

                        dialog.dismiss();
                        editingIndex = -1;
                        showTroubleshooting();
                    });
        });

        dialog.show();
    }

    private EditText edit(String hint) {
        EditText e = new EditText(this);
        e.setHint(hint);
        e.setHintTextColor(MUTED);
        e.setTextColor(TEXT);
        e.setTextSize(15);
        e.setBackground(bg(PANEL2, 8, Color.TRANSPARENT));
        e.setPadding(dp(12), 0, dp(12), 0);
        return e;
    }

    private void addEditImage(
            LinearLayout parent,
            String path) {

        File file = new File(path);

        if (!file.exists()) {
            return;
        }

        ImageView image = new ImageView(this);
        image.setImageURI(Uri.fromFile(file));
        image.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        image.setAdjustViewBounds(true);
        image.setOnClickListener(v -> showZoomImage(file));

        parent.addView(image,
                new LinearLayout.LayoutParams(-1, dp(150)));

        Button remove = btn("Bild entfernen");

        remove.setOnClickListener(v -> {
            if (editingIndex >= 0 &&
                    editingIndex < errorList.size()) {

                errorList.get(editingIndex).images.remove(path);
                saveErrors();
                showEditError(editingIndex);
            }
        });

        parent.addView(remove);
    }

    private void showZoomImage(File file) {
        if (!file.exists()) {
            return;
        }

        Dialog dialog = new Dialog(this);
        dialog.setTitle("Bild");

        ImageView image = new ImageView(this);
        image.setImageURI(Uri.fromFile(file));
        image.setBackgroundColor(Color.BLACK);
        image.setScaleType(ImageView.ScaleType.MATRIX);

        Matrix matrix = new Matrix();
        image.setImageMatrix(matrix);

        image.setOnTouchListener(
                new ZoomTouchListener(image, matrix));

        dialog.setContentView(image);

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(
                    bg(Color.BLACK, 0, Color.TRANSPARENT));
        }

        dialog.show();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setLayout(
                    dp(360), dp(520));
        }
    }

    private static class ZoomTouchListener
            implements View.OnTouchListener {

        private final ImageView image;
        private final Matrix matrix;
        private final ScaleGestureDetector scaleDetector;

        private float lastX;
        private float lastY;
        private boolean dragging = false;

        ZoomTouchListener(ImageView image, Matrix matrix) {
            this.image = image;
            this.matrix = matrix;

            scaleDetector =
                    new ScaleGestureDetector(
                            image.getContext(),
                            new ScaleGestureDetector.SimpleOnScaleGestureListener() {
                                @Override
                                public boolean onScale(
                                        ScaleGestureDetector detector) {

                                    float scale =
                                            detector.getScaleFactor();

                                    matrix.postScale(
                                            scale,
                                            scale,
                                            detector.getFocusX(),
                                            detector.getFocusY());

                                    image.setImageMatrix(matrix);
                                    return true;
                                }
                            });
        }

        @Override
        public boolean onTouch(
                View v,
                MotionEvent event) {

            scaleDetector.onTouchEvent(event);

            switch (event.getActionMasked()) {

                case MotionEvent.ACTION_DOWN:
                    lastX = event.getX();
                    lastY = event.getY();
                    dragging = true;
                    break;

                case MotionEvent.ACTION_MOVE:
                    if (dragging &&
                            event.getPointerCount() == 1) {

                        float dx = event.getX() - lastX;
                        float dy = event.getY() - lastY;

                        matrix.postTranslate(dx, dy);
                        image.setImageMatrix(matrix);

                        lastX = event.getX();
                        lastY = event.getY();
                    }
                    break;

                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    dragging = false;
                    break;
            }

            return true;
        }
    }

    @Override
    protected void onActivityResult(
            int requestCode,
            int resultCode,
            Intent data) {

        super.onActivityResult(
                requestCode,
                resultCode,
                data);

        if (requestCode == CHAPTER_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null
                && editingChapter != null) {

            String chapter = editingChapter;

            String path =
                    copyChapterImage(data.getData());

            if (path != null) {
                ArrayList<String> images =
                        loadChapterImages(chapter);

                images.add(path);

                saveChapterImages(
                        chapter,
                        images);
            }

            editingChapter = null;
            editChapter(chapter);
            return;
        }

        if (requestCode == IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null
                && editingIndex >= 0
                && editingIndex < errorList.size()) {

            String path =
                    copyImage(data.getData());

            if (path != null) {
                errorList.get(editingIndex)
                        .images.add(path);

                saveErrors();
            }

            showEditError(editingIndex);
        }
    }

    private String copyChapterImage(Uri uri) {
        return copyImageToFolder(uri, "chapter_images",
                "chapter_");
    }

    private String copyImage(Uri uri) {
        return copyImageToFolder(uri, "error_images", "img_");
    }

    private String copyImageToFolder(
            Uri uri,
            String folderName,
            String prefix) {

        try {
            File dir = new File(getFilesDir(), folderName);

            if (!dir.exists()) {
                dir.mkdirs();
            }

            File file = new File(
                    dir,
                    prefix + System.currentTimeMillis() + ".jpg");

            InputStream in =
                    getContentResolver().openInputStream(uri);

            if (in == null) {
                return null;
            }

            FileOutputStream out =
                    new FileOutputStream(file);

            byte[] buffer = new byte[8192];
            int length;

            while ((length = in.read(buffer)) != -1) {
                out.write(buffer, 0, length);
            }

            in.close();
            out.close();

            return file.getAbsolutePath();

        } catch (Exception e) {
            Toast.makeText(
                    this,
                    "Bild konnte nicht übernommen werden.",
                    Toast.LENGTH_SHORT).show();

            return null;
        }
    }

    private void showMenu() {
        String[] items;

        if (profi) {
            items = new String[] {
                    "Startseite",
                    "Fehlersuche",
                    "Profi-Modus ausschalten"
            };
        } else {
            items = new String[] {
                    "Startseite",
                    "Fehlersuche",
                    "Profi-Modus"
            };
        }

        new AlertDialog.Builder(this)
                .setTitle("Z.AERO")
                .setItems(items, (dialog, which) -> {

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
                })
                .show();
    }

    private void askPassword() {
        EditText input = new EditText(this);
        input.setHint("Passwort");
        input.setTextColor(TEXT);
        input.setInputType(
                InputType.TYPE_CLASS_TEXT |
                        InputType.TYPE_TEXT_VARIATION_PASSWORD);

        new AlertDialog.Builder(this)
                .setTitle("Profi-Modus")
                .setMessage("Passwort eingeben")
                .setView(input)
                .setNegativeButton("Abbrechen", null)
                .setPositiveButton("OK", (dialog, which) -> {

                    if (MASTER.equals(
                            input.getText().toString())) {

                        profi = true;
                        saveProfi();
                        buildBase();
                        showHome();

                    } else {
                        Toast.makeText(
                                this,
                                "Falsches Passwort.",
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .show();
    }

    private void saveProfi() {
        getSharedPreferences(PREFS, MODE_PRIVATE)
                .edit()
                .putBoolean(PROFI, profi)
                .apply();
    }

    private void saveErrors() {
        try {
            JSONArray array = new JSONArray();

            for (ErrorItem item : errorList) {
                JSONObject object = new JSONObject();

                object.put("title", item.title);
                object.put("cause", item.cause);
                object.put("solution", item.solution);

                JSONArray images = new JSONArray();

                for (String path : item.images) {
                    images.put(path);
                }

                object.put("imagePaths", images);
                array.put(object);
            }

            getSharedPreferences(PREFS, MODE_PRIVATE)
                    .edit()
                    .putString(ERRORS, array.toString())
                    .apply();

        } catch (Exception ignored) {
        }
    }

    private void loadErrors() {
        errorList.clear();

        String saved =
                getSharedPreferences(PREFS, MODE_PRIVATE)
                        .getString(ERRORS, "");

        if (saved.isEmpty()) {
            return;
        }

        try {
            JSONArray array = new JSONArray(saved);

            for (int i = 0; i < array.length(); i++) {
                JSONObject object =
                        array.getJSONObject(i);

                ErrorItem item = new ErrorItem(
                        object.optString("title", ""),
                        object.optString(
                                "cause",
                                "Nicht angegeben"),
                        object.optString("solution", ""));

                JSONArray images =
                        object.optJSONArray("imagePaths");

                if (images == null) {
                    images = object.optJSONArray("images");
                }

                if (images != null) {
                    for (int j = 0; j < images.length(); j++) {
                        String path =
                                images.optString(j, "");

                        if (!path.isEmpty()) {
                            item.images.add(path);
                        }
                    }
                }

                String oldImage =
                        object.optString("imagePath", "");

                if (!oldImage.isEmpty()
                        && !item.images.contains(oldImage)) {
                    item.images.add(oldImage);
                }

                errorList.add(item);
            }

            removeDuplicates();

        } catch (Exception ignored) {
        }
    }

    private void removeDuplicates() {
        for (int i = errorList.size() - 1; i >= 0; i--) {

            ErrorItem current = errorList.get(i);

            for (int j = 0; j < i; j++) {

                ErrorItem old = errorList.get(j);

                if (old.title.equals(current.title)
                        && old.cause.equals(current.cause)
                        && old.solution.equals(current.solution)) {

                    for (String image : current.images) {
                        if (!old.images.contains(image)) {
                            old.images.add(image);
                        }
                    }

                    errorList.remove(i);
                    break;
                }
            }
        }
    }

    private void addSpace(int size) {
        addSpaceTo(content, size);
    }

    private void addSpaceTo(
            LinearLayout layout,
            int size) {

        View space = new View(this);

        layout.addView(
                space,
                new LinearLayout.LayoutParams(
                        1, dp(size)));
    }

    private static class ErrorItem {
        String title;
        String cause;
        String solution;
        ArrayList<String> images = new ArrayList<>();

        ErrorItem(
                String title,
                String cause,
                String solution) {

            this.title = title;
            this.cause = cause;
            this.solution = solution;
        }
    }
}
