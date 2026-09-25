package com.zaero.diamondclean;

import android.app.*;
import android.content.*;
import android.graphics.*;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.*;
import android.text.InputType;
import android.view.*;
import android.view.animation.*;
import android.widget.*;

import org.json.*;

import java.io.*;
import java.util.*;

public class MainActivity extends Activity {

    private static final int GOLD = Color.rgb(214,168,79);
    private static final int BG = Color.rgb(12,12,14);
    private static final int PANEL = Color.rgb(28,27,30);
    private static final int PANEL2 = Color.rgb(40,38,42);
    private static final int TEXT = Color.rgb(245,242,235);
    private static final int MUTED = Color.rgb(180,175,168);
    private static final int RED = Color.rgb(220,30,35);
    private static final int GREEN = Color.rgb(30,220,70);

    private static final String PREFS = "zaero_data";
    private static final String ERRORS = "errors";
    private static final String PROFI = "profi";
    private static final String MASTER = "C1B2A3Z";
    private static final int IMAGE_REQUEST = 5001;

    private LinearLayout root, content;
    private boolean profi;
    private int editingIndex = -1;

    private final ArrayList<ErrorItem> errorList =
            new ArrayList<>();

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);

        profi = getSharedPreferences(PREFS, MODE_PRIVATE)
                .getBoolean(PROFI, false);

        loadErrors();
        buildBase();
        showHome();
    }

    private int dp(int n) {
        return (int)(n * getResources().getDisplayMetrics().density + .5f);
    }

    private TextView tv(
            String s,
            float size,
            int color,
            boolean bold) {

        TextView v = new TextView(this);
        v.setText(s);
        v.setTextSize(size);
        v.setTextColor(color);
        v.setGravity(Gravity.CENTER_VERTICAL);

        if (bold)
            v.setTypeface(Typeface.DEFAULT, Typeface.BOLD);

        return v;
    }

    private GradientDrawable bg(
            int color,
            int radius,
            int stroke) {

        GradientDrawable g = new GradientDrawable();
        g.setColor(color);
        g.setCornerRadius(dp(radius));

        if (stroke != Color.TRANSPARENT)
            g.setStroke(dp(1), stroke);

        return g;
    }

    private Button btn(String s) {

        Button b = new Button(this);
        b.setText(s);
        b.setTextColor(TEXT);
        b.setTextSize(15);
        b.setAllCaps(false);
        b.setBackground(bg(PANEL2,10,Color.TRANSPARENT));

        LinearLayout.LayoutParams p =
                new LinearLayout.LayoutParams(-1,dp(52));

        p.bottomMargin = dp(8);
        b.setLayoutParams(p);

        return b;
    }

    private Button smallBtn(String s) {

        Button b = new Button(this);
        b.setText(s);
        b.setTextColor(GOLD);
        b.setTextSize(20);
        b.setAllCaps(false);
        b.setBackground(bg(PANEL,10,Color.TRANSPARENT));

        return b;
    }

    private LinearLayout panel() {

        LinearLayout p = new LinearLayout(this);
        p.setOrientation(LinearLayout.VERTICAL);
        p.setPadding(dp(16),dp(16),dp(16),dp(16));
        p.setBackground(bg(PANEL,12,Color.TRANSPARENT));

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
        header.setPadding(dp(12),dp(8),dp(12),dp(8));
        header.setBackgroundColor(Color.rgb(18,17,19));

        Button menu = smallBtn("☰");
        menu.setOnClickListener(v -> showMenu());

        header.addView(menu,
                new LinearLayout.LayoutParams(dp(52),dp(52)));

        TextView title = tv("Z.AERO",21,GOLD,true);
        title.setGravity(Gravity.CENTER);

        header.addView(title,
                new LinearLayout.LayoutParams(
                        0,dp(52),1));

        TextView user =
                tv(profi ? "PROFI" : "BENUTZER",
                        12,TEXT,false);

        user.setGravity(Gravity.CENTER);

        header.addView(user,
                new LinearLayout.LayoutParams(
                        dp(82),dp(52)));

        root.addView(header,
                new LinearLayout.LayoutParams(
                        -1,dp(68)));

        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(true);
        scroll.setBackgroundColor(BG);

        content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setGravity(Gravity.CENTER_HORIZONTAL);
        content.setPadding(dp(18),dp(18),dp(18),dp(30));

        scroll.addView(content,
                new ScrollView.LayoutParams(-1,-1));

        root.addView(scroll,
                new LinearLayout.LayoutParams(
                        -1,0,1));
    }

    private void clear() {
        content.removeAllViews();
    }

    private void showHome() {

        clear();
        content.setGravity(Gravity.CENTER);

        LinearLayout home = new LinearLayout(this);
        home.setOrientation(LinearLayout.VERTICAL);
        home.setGravity(Gravity.CENTER_HORIZONTAL);

        TextView welcome =
                tv("Willkommen bei",22,GOLD,true);
        welcome.setGravity(Gravity.CENTER);

        home.addView(welcome,
                new LinearLayout.LayoutParams(-1,-2));

        TextView brand =
                tv("Z-Aero Diamond Clean",30,TEXT,true);
        brand.setGravity(Gravity.CENTER);

        home.addView(brand,
                new LinearLayout.LayoutParams(-1,-2));

        AnimationSet set = new AnimationSet(true);

        TranslateAnimation slide =
                new TranslateAnimation(
                        -dp(280),0,0,0);

        slide.setDuration(850);

        AlphaAnimation fade =
                new AlphaAnimation(0f,1f);

        fade.setDuration(850);

        set.addAnimation(slide);
        set.addAnimation(fade);
        brand.startAnimation(set);

        TextView sub =
                tv("Ihre Unterstützung für den sicheren und effizienten Betrieb der Anlage.",
                        15,MUTED,false);

        sub.setGravity(Gravity.CENTER);
        sub.setPadding(dp(8),0,dp(8),dp(10));

        home.addView(sub,
                new LinearLayout.LayoutParams(-1,-2));

        ImageView machine = new ImageView(this);
        machine.setImageResource(R.drawable.machine);
        machine.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        machine.setAdjustViewBounds(true);

        LinearLayout.LayoutParams ip =
                new LinearLayout.LayoutParams(-1,dp(205));

        ip.bottomMargin = dp(12);

        home.addView(machine,ip);

        LinearLayout cards = new LinearLayout(this);
        cards.setOrientation(LinearLayout.HORIZONTAL);
        cards.setGravity(Gravity.CENTER);

        addHomeCard(cards,"▶","Anfahren",
                "Anlage starten",
                v -> showChapter("Anfahren"));

        addHomeCard(cards,"■","Abstellen",
                "Anlage sicher\nherunterfahren",
                v -> showChapter("Abstellen"));

        addHomeCard(cards,"⌕","Fehlersuche",
                "Fehler und\nLösungen",
                v -> showTroubleshooting());

        home.addView(cards,
                new LinearLayout.LayoutParams(-1,-2));

        content.addView(home,
                new LinearLayout.LayoutParams(-1,-2));
    }

    private void addHomeCard(
            LinearLayout parent,
            String icon,
            String title,
            String sub,
            View.OnClickListener click) {

        LinearLayout c = new LinearLayout(this);
        c.setOrientation(LinearLayout.VERTICAL);
        c.setGravity(Gravity.CENTER);
        c.setPadding(dp(8),dp(10),dp(8),dp(10));
        c.setBackground(bg(PANEL,12,GOLD));
        c.setOnClickListener(click);

        TextView i = tv(icon,25,GOLD,true);
        i.setGravity(Gravity.CENTER);

        c.addView(i,
                new LinearLayout.LayoutParams(-1,dp(34)));

        TextView t = tv(title,16,TEXT,true);
        t.setGravity(Gravity.CENTER);

        c.addView(t,
                new LinearLayout.LayoutParams(-1,-2));

        TextView s = tv(sub,11,MUTED,false);
        s.setGravity(Gravity.CENTER);

        c.addView(s,
                new LinearLayout.LayoutParams(-1,dp(34)));

        LinearLayout.LayoutParams p =
                new LinearLayout.LayoutParams(
                        0,dp(112),1);

        p.setMargins(dp(4),0,dp(4),0);

        parent.addView(c,p);
    }

    private void showChapter(String chapter) {

        clear();

        TextView h = tv(chapter,28,GOLD,true);
        h.setGravity(Gravity.CENTER);

        content.addView(h,
                new LinearLayout.LayoutParams(-1,dp(60)));

        LinearLayout p = panel();

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

        p.addView(tv(text,16,TEXT,false));

        content.addView(p,
                new LinearLayout.LayoutParams(-1,-2));

        Button back = btn("← Zurück");
        back.setOnClickListener(v -> showHome());
        content.addView(back);
    }

    private LinearLayout borderBox(int color) {

        LinearLayout b = new LinearLayout(this);
        b.setOrientation(LinearLayout.VERTICAL);
        b.setBackground(bg(Color.BLACK,8,color));
        b.setPadding(dp(4),dp(4),dp(4),dp(4));

        return b;
    }

    private void showTroubleshooting() {

        clear();

        TextView h = tv("Fehlersuche",28,GOLD,true);
        h.setGravity(Gravity.CENTER);

        content.addView(h,
                new LinearLayout.LayoutParams(-1,dp(60)));

        Button search = btn("⌕ Fehler suchen");
        search.setOnClickListener(v -> showSearch());
        content.addView(search);

        if (errorList.isEmpty()) {

            TextView empty =
                    tv("Noch keine Fehler hinterlegt.",
                            16,MUTED,false);

            empty.setGravity(Gravity.CENTER);

            content.addView(empty,
                    new LinearLayout.LayoutParams(
                            -1,dp(70)));

        } else {

            for (int i=0;i<errorList.size();i++) {

                final int index = i;
                ErrorItem item = errorList.get(i);

                LinearLayout card = panel();

                // ROTER FEHLERBEREICH
                LinearLayout errorBox =
                        borderBox(RED);

                TextView errorTitle =
                        tv(item.title,20,GOLD,true);

                errorTitle.setGravity(Gravity.CENTER);

                errorBox.addView(errorTitle,
                        new LinearLayout.LayoutParams(
                                -1,dp(58)));

                card.addView(errorBox);

                // URSACHE
                TextView causeLabel =
                        tv("URSACHE:",14,GOLD,true);

                causeLabel.setPadding(
                        0,dp(12),0,dp(2));

                card.addView(causeLabel);

                card.addView(
                        tv(item.cause,15,TEXT,false));

                // GRÜNER LÖSUNGSBEREICH
                LinearLayout solutionBox =
                        borderBox(GREEN);

                TextView solutionLabel =
                        tv("LÖSUNG:",14,GREEN,true);

                solutionLabel.setPadding(
                        dp(10),dp(8),dp(10),dp(2));

                solutionBox.addView(solutionLabel);

                TextView solution =
                        tv(item.solution,15,TEXT,false);

                solution.setPadding(
                        dp(10),dp(2),dp(10),dp(10));

                solutionBox.addView(solution);

                LinearLayout.LayoutParams sp =
                        new LinearLayout.LayoutParams(-1,-2);

                sp.topMargin = dp(12);

                card.addView(solutionBox,sp);

                // BILDER
                addImages(card,item.images);

                if (profi) {

                    Button edit =
                            btn("✎ Bearbeiten");

                    edit.setOnClickListener(
                            v -> showEditError(index));

                    card.addView(edit);
                }

                content.addView(card,
                        new LinearLayout.LayoutParams(
                                -1,-2));

                addSpace(8);
            }
        }

        if (profi) {

            Button add =
                    btn("+ Fehler hinzufügen");

            add.setOnClickListener(
                    v -> showEditError(-1));

            content.addView(add);
        }

        Button back = btn("← Zurück");
        back.setOnClickListener(v -> showHome());
        content.addView(back);
    }

    private void addImages(
            LinearLayout card,
            ArrayList<String> images) {

        for (String path : images) {

            File f = new File(path);

            if (!f.exists())
                continue;

            ImageView image = new ImageView(this);
            image.setImageURI(Uri.fromFile(f));
            image.setScaleType(
                    ImageView.ScaleType.CENTER_INSIDE);
            image.setAdjustViewBounds(true);

            LinearLayout.LayoutParams p =
                    new LinearLayout.LayoutParams(
                            -1,dp(170));

            p.topMargin = dp(8);

            card.addView(image,p);
        }
    }

    private void showSearch() {

        final EditText input = new EditText(this);

        input.setHint("Mindestens 3 Buchstaben");
        input.setSingleLine(true);
        input.setTextColor(TEXT);
        input.setHintTextColor(MUTED);

        AlertDialog d =
                new AlertDialog.Builder(this)
                        .setTitle("Fehler suchen")
                        .setView(input)
                        .setNegativeButton(
                                "Abbrechen",null)
                        .setPositiveButton(
                                "Suchen",null)
                        .create();

        d.setOnShowListener(x -> {

            Button search =
                    d.getButton(
                            AlertDialog.BUTTON_POSITIVE);

            search.setEnabled(false);

            input.addTextChangedListener(
                    new TextWatcher() {

                        public void beforeTextChanged(
                                CharSequence s,
                                int a,int b,int c) {}

                        public void onTextChanged(
                                CharSequence s,
                                int a,int b,int c) {

                            search.setEnabled(
                                    s.toString()
                                            .trim()
                                            .length() >= 3);
                        }

                        public void afterTextChanged(
                                Editable e) {}
                    });

            search.setOnClickListener(v -> {

                String q =
                        input.getText()
                                .toString()
                                .trim()
                                .toLowerCase(Locale.GERMAN);

                d.dismiss();
                showSearchResults(q);
            });
        });

        d.show();
    }

    private void showSearchResults(String q) {

        clear();

        TextView h =
                tv("Suchergebnisse",26,GOLD,true);

        h.setGravity(Gravity.CENTER);

        content.addView(h,
                new LinearLayout.LayoutParams(-1,dp(58)));

        boolean found = false;

        for (ErrorItem item : errorList) {

            String all =
                    (item.title+" "+
                     item.cause+" "+
                     item.solution)
                    .toLowerCase(Locale.GERMAN);

            if (!all.contains(q))
                continue;

            found = true;

            LinearLayout card = panel();

            LinearLayout errorBox =
                    borderBox(RED);

            TextView errorTitle =
                    tv(item.title,20,GOLD,true);

            errorTitle.setGravity(Gravity.CENTER);

            errorBox.addView(errorTitle,
                    new LinearLayout.LayoutParams(
                            -1,dp(58)));

            card.addView(errorBox);

            TextView causeLabel =
                    tv("URSACHE:",14,GOLD,true);

            causeLabel.setPadding(
                    0,dp(12),0,dp(2));

            card.addView(causeLabel);

            card.addView(
                    tv(item.cause,15,TEXT,false));

            LinearLayout solutionBox =
                    borderBox(GREEN);

            TextView solutionLabel =
                    tv("LÖSUNG:",14,GREEN,true);

            solutionLabel.setPadding(
                    dp(10),dp(8),dp(10),dp(2));

            solutionBox.addView(solutionLabel);

            TextView solution =
                    tv(item.solution,15,TEXT,false);

            solution.setPadding(
                    dp(10),dp(2),dp(10),dp(10));

            solutionBox.addView(solution);

            LinearLayout.LayoutParams sp =
                    new LinearLayout.LayoutParams(-1,-2);

            sp.topMargin = dp(12);

            card.addView(solutionBox,sp);

            addImages(card,item.images);

            content.addView(card,
                    new LinearLayout.LayoutParams(
                            -1,-2));

            addSpace(8);
        }

        if (!found) {

            TextView none =
                    tv("Kein passender Fehler gefunden.",
                            16,MUTED,false);

            none.setGravity(Gravity.CENTER);

            content.addView(none,
                    new LinearLayout.LayoutParams(
                            -1,dp(70)));
        }

        Button back = btn("← Zurück");
        back.setOnClickListener(
                v -> showTroubleshooting());

        content.addView(back);
    }

    private void showEditError(int index) {

        editingIndex = index;

        LinearLayout box =
                new LinearLayout(this);

        box.setOrientation(
                LinearLayout.VERTICAL);

        EditText title =
                edit("Fehlermeldung / Fehler");

        EditText cause =
                edit("Ursache");

        EditText solution =
                edit("Lösung");

        box.addView(title);
        box.addView(cause);
        box.addView(solution);

        if (index >= 0) {

            ErrorItem e = errorList.get(index);

            title.setText(e.title);
            cause.setText(e.cause);
            solution.setText(e.solution);
        }

        if (index >= 0) {

            for (String path :
                    errorList.get(index).images) {

                addEditImage(box,path);
            }
        }

        Button addImage =
                btn("+ Bild hinzufügen");

        addImage.setOnClickListener(v -> {

            String t =
                    title.getText().toString().trim();

            String c =
                    cause.getText().toString().trim();

            String s =
                    solution.getText().toString().trim();

            if (t.isEmpty() ||
                c.isEmpty() ||
                s.isEmpty()) {

                Toast.makeText(
                        this,
                        "Bitte alle drei Felder ausfüllen.",
                        Toast.LENGTH_SHORT).show();

                return;
            }

            if (editingIndex < 0) {

                errorList.add(
                        new ErrorItem(t,c,s));

                editingIndex =
                        errorList.size()-1;

            } else {

                ErrorItem e =
                        errorList.get(editingIndex);

                e.title = t;
                e.cause = c;
                e.solution = s;
            }

            saveErrors();

            Intent intent =
                    new Intent(
                            Intent.ACTION_OPEN_DOCUMENT);

            intent.addCategory(
                    Intent.CATEGORY_OPENABLE);

            intent.setType("image/*");

            startActivityForResult(
                    intent,IMAGE_REQUEST);
        });

        box.addView(addImage);

        AlertDialog d =
                new AlertDialog.Builder(this)
                        .setTitle(
                                index >= 0
                                        ? "Fehler bearbeiten"
                                        : "Fehler hinzufügen")
                        .setView(box)
                        .setNegativeButton(
                                "Abbrechen",null)
                        .setPositiveButton(
                                "Speichern",null)
                        .create();

        d.setOnShowListener(x -> {

            d.getButton(
                    AlertDialog.BUTTON_POSITIVE)
                    .setOnClickListener(v -> {

                        String t =
                                title.getText()
                                        .toString().trim();

                        String c =
                                cause.getText()
                                        .toString().trim();

                        String s =
                                solution.getText()
                                        .toString().trim();

                        if (t.isEmpty() ||
                            c.isEmpty() ||
                            s.isEmpty()) {

                            Toast.makeText(
                                    this,
                                    "Alle drei Felder müssen ausgefüllt sein.",
                                    Toast.LENGTH_SHORT).show();

                            return;
                        }

                        if (index < 0) {

                            errorList.add(
                                    new ErrorItem(
                                            t,c,s));

                        } else {

                            ErrorItem e =
                                    errorList.get(index);

                            e.title = t;
                            e.cause = c;
                            e.solution = s;
                        }

                        removeDuplicates();
                        saveErrors();

                        d.dismiss();
                        showTroubleshooting();
                    });
        });

        d.show();
    }

    private EditText edit(String hint) {

        EditText e = new EditText(this);
        e.setHint(hint);
        e.setHintTextColor(MUTED);
        e.setTextColor(TEXT);
        e.setTextSize(15);

        return e;
    }

    private void addEditImage(
            LinearLayout parent,
            String path) {

        ImageView image = new ImageView(this);
        image.setImageURI(
                Uri.fromFile(new File(path)));
        image.setScaleType(
                ImageView.ScaleType.CENTER_INSIDE);
        image.setAdjustViewBounds(true);

        parent.addView(image,
                new LinearLayout.LayoutParams(
                        -1,dp(150)));

        Button remove =
                btn("Bild entfernen");

        remove.setOnClickListener(v -> {

            if (editingIndex >= 0) {

                errorList.get(editingIndex)
                        .images.remove(path);

                saveErrors();
                showEditError(editingIndex);
            }
        });

        parent.addView(remove);
    }

    @Override
    protected void onActivityResult(
            int requestCode,
            int resultCode,
            Intent data) {

        super.onActivityResult(
                requestCode,resultCode,data);

        if (
                requestCode == IMAGE_REQUEST &&
                resultCode == RESULT_OK &&
                data != null &&
                data.getData() != null &&
                editingIndex >= 0
        ) {

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

    private String copyImage(Uri uri) {

        try {

            File dir =
                    new File(
                            getFilesDir(),
                            "error_images");

            if (!dir.exists())
                dir.mkdirs();

            File file =
                    new File(
                            dir,
                            "img_"+
                            System.currentTimeMillis()+
                            ".jpg");

            InputStream in =
                    getContentResolver()
                            .openInputStream(uri);

            FileOutputStream out =
                    new FileOutputStream(file);

            byte[] buffer = new byte[8192];
            int n;

            while ((n=in.read(buffer))!=-1)
                out.write(buffer,0,n);

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

            items = new String[]{
                    "Startseite",
                    "Fehlersuche",
                    "Profi-Modus ausschalten"};

        } else {

            items = new String[]{
                    "Startseite",
                    "Fehlersuche",
                    "Profi-Modus"};
        }

        new AlertDialog.Builder(this)
                .setTitle("Z.AERO")
                .setItems(items,(d,w) -> {

                    if (w == 0) {

                        showHome();

                    } else if (w == 1) {

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
                .setNegativeButton(
                        "Abbrechen",null)
                .setPositiveButton(
                        "OK",(d,w) -> {

                            if (MASTER.equals(
                                    input.getText()
                                            .toString())) {

                                profi = true;
                                saveProfi();
                                buildBase();
                                showHome();

                            } else {

                                Toast.makeText(
                                        this,
                                        "Falsches Passwort.",
                                        Toast.LENGTH_SHORT)
                                        .show();
                            }
                        })
                .show();
    }

    private void saveProfi() {

        getSharedPreferences(
                PREFS,MODE_PRIVATE)
                .edit()
                .putBoolean(PROFI,profi)
                .apply();
    }

    private void saveErrors() {

        try {

            JSONArray a = new JSONArray();

            for (ErrorItem e : errorList) {

                JSONObject o = new JSONObject();

                o.put("title",e.title);
                o.put("cause",e.cause);
                o.put("solution",e.solution);

                JSONArray imgs = new JSONArray();

                for (String path : e.images)
                    imgs.put(path);

                o.put("imagePaths",imgs);

                a.put(o);
            }

            getSharedPreferences(
                    PREFS,MODE_PRIVATE)
                    .edit()
                    .putString(
                            ERRORS,a.toString())
                    .apply();

        } catch (Exception ignored) {}
    }

    private void loadErrors() {

        errorList.clear();

        String saved =
                getSharedPreferences(
                        PREFS,MODE_PRIVATE)
                        .getString(ERRORS,"");

        if (saved.isEmpty())
            return;

        try {

            JSONArray a =
                    new JSONArray(saved);

            for (int i=0;i<a.length();i++) {

                JSONObject o =
                        a.getJSONObject(i);

                ErrorItem e =
                        new ErrorItem(
                                o.optString("title",""),
                                o.optString(
                                        "cause",
                                        "Nicht angegeben"),
                                o.optString(
                                        "solution",""));

                JSONArray imgs =
                        o.optJSONArray("imagePaths");

                if (imgs == null)
                    imgs = o.optJSONArray("images");

                if (imgs != null) {

                    for (int j=0;j<imgs.length();j++) {

                        String path =
                                imgs.optString(j,"");

                        if (!path.isEmpty())
                            e.images.add(path);
                    }
                }

                String old =
                        o.optString("imagePath","");

                if (!old.isEmpty() &&
                    !e.images.contains(old)) {

                    e.images.add(old);
                }

                errorList.add(e);
            }

            removeDuplicates();

        } catch (Exception ignored) {}
    }

    private void removeDuplicates() {

        for (int i=errorList.size()-1;i>=0;i--) {

            ErrorItem current =
                    errorList.get(i);

            for (int j=0;j<i;j++) {

                ErrorItem old =
                        errorList.get(j);

                if (
                        old.title.equals(current.title) &&
                        old.cause.equals(current.cause) &&
                        old.solution.equals(current.solution)
                ) {

                    for (String image :
                            current.images) {

                        if (!old.images.contains(image))
                            old.images.add(image);
                    }

                    errorList.remove(i);
                    break;
                }
            }
        }
    }

    private void addSpace(int size) {

        View v = new View(this);

        content.addView(v,
                new LinearLayout.LayoutParams(
                        1,dp(size)));
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
                String solution) {

            this.title = title;
            this.cause = cause;
            this.solution = solution;
        }
    }
}
