package com.zaero.diamondclean;

import android.app.Activity;
import android.app.AlertDialog;
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
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
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
    private static final String MASTER = "C1B2A3Z";

    private static final int IMAGE_REQUEST = 5001;

    private LinearLayout root;
    private LinearLayout content;

    private boolean profi = false;
    private int editingIndex = -1;

    private EditText searchField;
    private LinearLayout searchResults;

    private final ArrayList<ErrorItem> errorList =
            new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        profi = getSharedPreferences(
                PREFS,
                MODE_PRIVATE
        ).getBoolean(
                PROFI,
                false
        );

        loadErrors();

        buildBase();
        showHome();
    }

    private int dp(int value) {
        return (int) (
                value *
                        getResources()
                                .getDisplayMetrics()
                                .density
                        + 0.5f
        );
    }

    private TextView tv(
            String text,
            float size,
            int color,
            boolean bold
    ) {

        TextView v =
                new TextView(this);

        v.setText(text);
        v.setTextSize(size);
        v.setTextColor(color);
        v.setGravity(Gravity.CENTER_VERTICAL);

        if (bold) {
            v.setTypeface(
                    Typeface.DEFAULT,
                    Typeface.BOLD
            );
        }

        return v;
    }

    private GradientDrawable bg(
            int color,
            int radius,
            int stroke
    ) {

        GradientDrawable g =
                new GradientDrawable();

        g.setColor(color);
        g.setCornerRadius(dp(radius));

        if (stroke != Color.TRANSPARENT) {
            g.setStroke(
                    dp(1),
                    stroke
            );
        }

        return g;
    }

    private Button btn(String text) {

        Button b =
                new Button(this);

        b.setText(text);
        b.setTextColor(TEXT);
        b.setTextSize(15);
        b.setAllCaps(false);
        b.setBackground(
                bg(
                        PANEL2,
                        10,
                        Color.TRANSPARENT
                )
        );

        LinearLayout.LayoutParams p =
                new LinearLayout.LayoutParams(
                        -1,
                        dp(52)
                );

        p.bottomMargin = dp(8);

        b.setLayoutParams(p);

        return b;
    }

    private Button smallBtn(String text) {

        Button b =
                new Button(this);

        b.setText(text);
        b.setTextColor(GOLD);
        b.setTextSize(20);
        b.setAllCaps(false);
        b.setBackground(
                bg(
                        PANEL,
                        10,
                        Color.TRANSPARENT
                )
        );

        return b;
    }

    private LinearLayout panel() {

        LinearLayout p =
                new LinearLayout(this);

        p.setOrientation(
                LinearLayout.VERTICAL
        );

        p.setPadding(
                dp(16),
                dp(16),
                dp(16),
                dp(16)
        );

        p.setBackground(
                bg(
                        PANEL,
                        12,
                        Color.TRANSPARENT
                )
        );

        return p;
    }

    private void buildBase() {

        root =
                new LinearLayout(this);

        root.setOrientation(
                LinearLayout.VERTICAL
        );

        root.setBackgroundColor(BG);

        setContentView(root);

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
                smallBtn("☰");

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
                tv(
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
                tv(
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

    private void clear() {

        content.removeAllViews();

        searchField = null;
        searchResults = null;
    }

    private void showHome() {

        clear();

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

        TextView welcome =
                tv(
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
                tv(
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

        AnimationSet set =
                new AnimationSet(true);

        set.addAnimation(slide);
        set.addAnimation(fade);

        brand.startAnimation(set);

        TextView subtitle =
                tv(
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
                bg(
                        PANEL,
                        12,
                        GOLD
                )
        );

        card.setOnClickListener(listener);

        TextView iconView =
                tv(
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
                tv(
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
                tv(
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

        clear();

        content.setGravity(
                Gravity.TOP |
                        Gravity.CENTER_HORIZONTAL
        );

        TextView heading =
                tv(
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

        LinearLayout p =
                panel();

        String text;

        if (chapter.equals("Anfahren")) {

            text =
                    "Hier wird beschrieben, wie die Anlage sicher gestartet wird.\n\n"
                            + "Die genauen Arbeitsschritte können hier später ergänzt werden.";

        } else {

            text =
                    "Hier wird beschrieben, wie die Anlage sicher heruntergefahren wird.\n\n"
                            + "Die genauen Arbeitsschritte können hier später ergänzt werden.";
        }

        p.addView(
                tv(
                        text,
                        16,
                        TEXT,
                        false
                )
        );

        content.addView(
                p,
                new LinearLayout.LayoutParams(
                        -1,
                        -2
                )
        );

        Button back =
                btn("← Zurück");

        back.setOnClickListener(
                v -> showHome()
        );

        content.addView(back);
    }

    /*
     * FEHLERSUCHE
     *
     * Beim Öffnen werden KEINE Fehler angezeigt.
     * Erst ab 3 Buchstaben werden Treffer eingeblendet.
     */
    private void showTroubleshooting() {

        clear();

        content.setGravity(
                Gravity.TOP |
                        Gravity.CENTER_HORIZONTAL
        );

        TextView heading =
                tv(
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

        searchField =
                new EditText(this);

        searchField.setHint(
                "⌕ Fehler suchen"
        );

        searchField.setTextColor(TEXT);
        searchField.setHintTextColor(TEXT);
        searchField.setTextSize(17);
        searchField.setSingleLine(true);

        searchField.setPadding(
                dp(18),
                dp(8),
                dp(18),
                dp(8)
        );

        searchField.setBackground(
                bg(
                        PANEL2,
                        14,
                        Color.TRANSPARENT
                )
        );

        content.addView(
                searchField,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(60)
                )
        );

        addSpace(12);

        searchResults =
                new LinearLayout(this);

        searchResults.setOrientation(
                LinearLayout.VERTICAL
        );

        content.addView(
                searchResults,
                new LinearLayout.LayoutParams(
                        -1,
                        -2
                )
        );

        searchField.addTextChangedListener(
                new TextWatcher() {

                    @Override
                    public void beforeTextChanged(
                            CharSequence s,
                            int start,
                            int count,
                            int after
                    ) {
                    }

                    @Override
                    public void onTextChanged(
                            CharSequence s,
                            int start,
                            int before,
                            int count
                    ) {

                        updateSearchResults(
                                s.toString()
                        );
                    }

                    @Override
                    public void afterTextChanged(
                            Editable s
                    ) {
                    }
                }
        );

        if (profi) {

            addSpace(12);

            Button add =
                    btn(
                            "+ Fehler hinzufügen"
                    );

            add.setOnClickListener(
                    v -> showEditError(-1)
            );

            content.addView(add);
        }

        addSpace(8);

        Button back =
                btn("← Zurück");

        back.setOnClickListener(
                v -> showHome()
        );

        content.addView(back);
    }

    private void updateSearchResults(
            String input
    ) {

        if (searchResults == null)
            return;

        searchResults.removeAllViews();

        String q =
                input
                        .trim()
                        .toLowerCase(
                                Locale.GERMAN
                        );

        /*
         * Unter 3 Buchstaben:
         * KEINE Fehler anzeigen.
         */
        if (q.length() < 3) {
            return;
        }

        boolean found = false;

        for (int i = 0;
             i < errorList.size();
             i++) {

            ErrorItem item =
                    errorList.get(i);

            String all =
                    (
                            item.title
                                    + " "
                                    + item.cause
                                    + " "
                                    + item.solution
                    ).toLowerCase(
                            Locale.GERMAN
                    );

            if (!all.contains(q))
                continue;

            found = true;

            addErrorCard(
                    searchResults,
                    item,
                    i
            );

            addSpaceTo(
                    searchResults,
                    8
            );
        }

        if (!found) {

            TextView none =
                    tv(
                            "Kein passender Fehler gefunden.",
                            16,
                            MUTED,
                            false
                    );

            none.setGravity(
                    Gravity.CENTER
            );

            searchResults.addView(
                    none,
                    new LinearLayout.LayoutParams(
                            -1,
                            dp(70)
                    )
            );
        }
    }

    private void addErrorCard(
            LinearLayout parent,
            ErrorItem item,
            int index
    ) {

        LinearLayout card =
                panel();

        /*
         * ROTER FEHLERBEREICH
         */
        LinearLayout errorBox =
                borderBox(RED);

        TextView errorTitle =
                tv(
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

        card.addView(errorBox);

        /*
         * URSACHE
         */
        TextView causeLabel =
                tv(
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

        card.addView(causeLabel);

        TextView cause =
                tv(
                        item.cause,
                        15,
                        TEXT,
                        false
                );

        card.addView(cause);

        /*
         * GRÜNER LÖSUNGSBEREICH
         */
        LinearLayout solutionBox =
                borderBox(GREEN);

        TextView solutionLabel =
                tv(
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
                solutionLabel
        );

        TextView solution =
                tv(
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
                solution
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
                solutionParams
        );

        /*
         * BILDER
         */
        addImages(
                card,
                item.images
        );

        /*
         * BEARBEITEN
         */
        if (profi) {

            Button edit =
                    btn("✎ Bearbeiten");

            edit.setOnClickListener(
                    v -> showEditError(index)
            );

            card.addView(edit);
        }

        parent.addView(
                card,
                new LinearLayout.LayoutParams(
                        -1,
                        -2
                )
        );
    }

    private LinearLayout borderBox(
            int color
    ) {

        LinearLayout box =
                new LinearLayout(this);

        box.setOrientation(
                LinearLayout.VERTICAL
        );

        box.setPadding(
                dp(4),
                dp(4),
                dp(4),
                dp(4)
        );

        box.setBackground(
                bg(
                        Color.BLACK,
                        8,
                        color
                )
        );

        return box;
    }

    /*
     * Bilder anzeigen
     *
     * Doppeltipp auf Bild:
     * Großansicht mit Zoom.
     */
    private void addImages(
            LinearLayout card,
            ArrayList<String> images
    ) {

        for (String path : images) {

            if (path == null ||
                    path.isEmpty()) {
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

            image.setOnTouchListener(
                    new View.OnTouchListener() {

                        private long firstTapTime = 0;

                        @Override
                        public boolean onTouch(
                                View v,
                                MotionEvent event
                        ) {

                            if (
                                    event.getAction()
                                            == MotionEvent.ACTION_UP
                            ) {

                                long now =
                                        System.currentTimeMillis();

                                if (
                                        now - firstTapTime
                                                < 350
                                ) {

                                    showZoomImage(
                                            file
                                    );

                                    firstTapTime = 0;

                                } else {

                                    firstTapTime = now;
                                }
                            }

                            return true;
                        }
                    }
            );

            LinearLayout.LayoutParams p =
                    new LinearLayout.LayoutParams(
                            -1,
                            dp(170)
                    );

            p.topMargin = dp(8);

            card.addView(
                    image,
                    p
            );
        }
    }

    /*
     * GROßANSICHT + ZOOM
     */
    private void showZoomImage(
            File file
    ) {

        final Dialog dialog =
                new Dialog(this);

        dialog.getWindow();

        LinearLayout background =
                new LinearLayout(this);

        background.setOrientation(
                LinearLayout.VERTICAL
        );

        background.setGravity(
                Gravity.CENTER
        );

        background.setBackgroundColor(
                Color.BLACK
        );

        ImageView image =
                new ImageView(this);

        image.setImageURI(
                Uri.fromFile(file)
        );

        image.setScaleType(
                ImageView.ScaleType.MATRIX
        );

        background.addView(
                image,
                new LinearLayout.LayoutParams(
                        -1,
                        0,
                        1
                )
        );

        Button close =
                new Button(this);

        close.setText("Schließen");
        close.setTextColor(TEXT);
        close.setAllCaps(false);
        close.setBackground(
                bg(
                        PANEL2,
                        10,
                        Color.TRANSPARENT
                )
        );

        background.addView(
                close,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(55)
                )
        );

        close.setOnClickListener(
                v -> dialog.dismiss()
        );

        dialog.setContentView(
                background
        );

        if (dialog.getWindow() != null) {

            dialog.getWindow()
                    .setBackgroundDrawable(
                            bg(
                                    Color.BLACK,
                                    0,
                                    Color.TRANSPARENT
                            )
                    );

            dialog.getWindow()
                    .setLayout(
                            -1,
                            -1
                    );
        }

        dialog.show();

        if (dialog.getWindow() != null) {

            dialog.getWindow()
                    .setLayout(
                            -1,
                            -1
                    );
        }

        ZoomTouchListener zoom =
                new ZoomTouchListener(
                        image
                );

        image.setOnTouchListener(
                zoom
        );
    }

    /*
     * Zoom-Steuerung
     */
    private static class ZoomTouchListener
            implements View.OnTouchListener {

        private final ImageView image;

        private final Matrix matrix =
                new Matrix();

        private final ScaleGestureDetector
                scaleDetector;

        private float lastX;
        private float lastY;

        private boolean moving = false;

        ZoomTouchListener(
                ImageView image
        ) {

            this.image = image;

            scaleDetector =
                    new ScaleGestureDetector(
                            image.getContext(),
                            new ScaleGestureDetector
                                    .SimpleOnScaleGestureListener() {

                                @Override
                                public boolean
                                onScale(
                                        ScaleGestureDetector detector
                                ) {

                                    float scale =
                                            detector
                                                    .getScaleFactor();

                                    matrix.postScale(
                                            scale,
                                            scale,
                                            detector.getFocusX(),
                                            detector.getFocusY()
                                    );

                                    image.setImageMatrix(
                                            matrix
                                    );

                                    return true;
                                }
                            }
                    );
        }

        @Override
        public boolean onTouch(
                View v,
                MotionEvent event
        ) {

            scaleDetector
                    .onTouchEvent(event);

            switch (
                    event.getActionMasked()
            ) {

                case MotionEvent.ACTION_DOWN:

                    lastX = event.getX();
                    lastY = event.getY();

                    moving = true;

                    return true;

                case MotionEvent.ACTION_MOVE:

                    if (
                            moving &&
                                    event.getPointerCount()
                                            == 1
                    ) {

                        float dx =
                                event.getX()
                                        - lastX;

                        float dy =
                                event.getY()
                                        - lastY;

                        matrix.postTranslate(
                                dx,
                                dy
                        );

                        image.setImageMatrix(
                                matrix
                        );

                        lastX = event.getX();
                        lastY = event.getY();
                    }

                    return true;

                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:

                    moving = false;

                    return true;
            }

            return true;
        }
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

        if (index >= 0) {

            ErrorItem item =
                    errorList.get(index);

            title.setText(
                    item.title
            );

            cause.setText(
                    item.cause
            );

            solution.setText(
                    item.solution
            );

            for (
                    String path :
                    item.images
            ) {

                addEditImage(
                        box,
                        path
                );
            }
        }

        Button addImage =
                btn(
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
                                "Bitte alle drei Felder ausfüllen.",
                                Toast.LENGTH_SHORT
                        ).show();

                        return;
                    }

                    if (editingIndex < 0) {

                        errorList.add(
                                new ErrorItem(
                                        t,
                                        c,
                                        s
                                )
                        );

                        editingIndex =
                                errorList.size() - 1;

                    } else {

                        ErrorItem item =
                                errorList.get(
                                        editingIndex
                                );

                        item.title = t;
                        item.cause = c;
                        item.solution = s;
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
                            IMAGE_REQUEST
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

                                    errorList.add(
                                            new ErrorItem(
                                                    t,
                                                    c,
                                                    s
                                            )
                                    );

                                } else {

                                    ErrorItem item =
                                            errorList.get(index);

                                    item.title = t;
                                    item.cause = c;
                                    item.solution = s;
                                }

                                removeDuplicates();
                                saveErrors();

                                dialog.dismiss();

                                showTroubleshooting();
                            }
                    );
                }
        );

        dialog.show();
    }

    private EditText edit(
            String hint
    ) {

        EditText e =
                new EditText(this);

        e.setHint(hint);
        e.setHintTextColor(MUTED);
        e.setTextColor(TEXT);
        e.setTextSize(15);

        return e;
    }

    private void addEditImage(
            LinearLayout parent,
            String path
    ) {

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

        parent.addView(
                image,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(150)
                )
        );

        Button remove =
                btn("Bild entfernen");

        remove.setOnClickListener(
                v -> {

                    if (
                            editingIndex >= 0
                                    && editingIndex
                                    < errorList.size()
                    ) {

                        errorList.get(
                                editingIndex
                        ).images.remove(path);

                        saveErrors();

                        showEditError(
                                editingIndex
                        );
                    }
                }
        );

        parent.addView(remove);
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
                requestCode == IMAGE_REQUEST
                        && resultCode == RESULT_OK
                        && data != null
                        && data.getData() != null
                        && editingIndex >= 0
                        && editingIndex < errorList.size()
        ) {

            String path =
                    copyImage(
                            data.getData()
                    );

            if (path != null) {

                errorList.get(
                        editingIndex
                ).images.add(path);

                saveErrors();
            }

            showEditError(
                    editingIndex
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

            if (!dir.exists()) {
                dir.mkdirs();
            }

            File file =
                    new File(
                            dir,
                            "img_"
                                    + System.currentTimeMillis()
                                    + ".jpg"
                    );

            InputStream in =
                    getContentResolver()
                            .openInputStream(uri);

            FileOutputStream out =
                    new FileOutputStream(file);

            byte[] buffer =
                    new byte[8192];

            int length;

            while (
                    (length =
                            in.read(buffer))
                            != -1
            ) {

                out.write(
                        buffer,
                        0,
                        length
                );
            }

            in.close();
            out.close();

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

        input.setHint("Passwort");
        input.setTextColor(TEXT);

        input.setInputType(
                InputType.TYPE_CLASS_TEXT
                        | InputType.TYPE_TEXT_VARIATION_PASSWORD
        );

        new AlertDialog.Builder(this)
                .setTitle("Profi-Modus")
                .setMessage(
                        "Passwort eingeben"
                )
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
                        PROFI,
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
                    errorList
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
                        item.images
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
                            ERRORS,
                            array.toString()
                    )
                    .apply();

        } catch (Exception ignored) {
        }
    }

    private void loadErrors() {

        errorList.clear();

        String saved =
                getSharedPreferences(
                        PREFS,
                        MODE_PRIVATE
                )
                        .getString(
                                ERRORS,
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

                if (images == null) {

                    images =
                            object.optJSONArray(
                                    "images"
                            );
                }

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

                            item.images.add(
                                    path
                            );
                        }
                    }
                }

                String oldImage =
                        object.optString(
                                "imagePath",
                                ""
                        );

                if (
                        !oldImage.isEmpty()
                                && !item.images.contains(
                                oldImage
                        )
                ) {

                    item.images.add(
                            oldImage
                    );
                }

                errorList.add(item);
            }

            removeDuplicates();

        } catch (Exception ignored) {
        }
    }

    private void removeDuplicates() {

        for (
                int i = errorList.size() - 1;
                i >= 0;
                i--
        ) {

            ErrorItem current =
                    errorList.get(i);

            for (
                    int j = 0;
                    j < i;
                    j++
            ) {

                ErrorItem old =
                        errorList.get(j);

                if (
                        old.title.equals(
                                current.title
                        )
                                && old.cause.equals(
                                current.cause
                        )
                                && old.solution.equals(
                                current.solution
                        )
                ) {

                    for (
                            String image :
                            current.images
                    ) {

                        if (
                                !old.images.contains(
                                        image
                                )
                        ) {

                            old.images.add(
                                    image
                            );
                        }
                    }

                    errorList.remove(i);

                    break;
                }
            }
        }
    }

    private void addSpace(
            int size
    ) {

        addSpaceTo(
                content,
                size
        );
    }

    private void addSpaceTo(
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

    private static class ErrorItem {

        String title;
        String cause;
        String solution;

        ArrayList<String> images =
                new ArrayList<>();

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
