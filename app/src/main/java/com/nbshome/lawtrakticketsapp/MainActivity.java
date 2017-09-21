package com.nbshome.lawtrakticketsapp;

import android.app.ActionBar;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.text.InputFilter;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.fitness.data.Goal;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.text.Line;
import com.nbshome.lawtrakticketsapp.barcode.BarcodeCaptureActivity;
import com.nbshome.lawtrakticketsapp.enums.Country;
import com.nbshome.lawtrakticketsapp.enums.Ethnicity;
import com.nbshome.lawtrakticketsapp.objects.Person;
import com.nbshome.lawtrakticketsapp.objects.Ticket;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import au.com.bytecode.opencsv.CSVReader;

import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener,
        PersonFragment.OnFragmentInteractionListener, MainContentFragment.OnFragmentInteractionListener,
        TicketFragment.OnFragmentInteractionListener, Registration.OnFragmentInteractionListener,
        CourtFragment.OnFragmentInteractionListener, Violation.OnFragmentInteractionListener{

    public static ArrayList<Person> violators = new ArrayList<>();

    boolean licScanned = false, regScanned = false;

    private static final int RC_BARCODE_CAPTURE = 9001;
    private static final String TAG = "BarcodeMain";
    private static final String CONFIG = "Config";
    public boolean flag = false;

    public static String output, parsedOutput;
    public static ArrayList<Person> people;


    public static Object readCachedFile (Context context, String key) throws IOException, ClassNotFoundException {
        FileInputStream fis = context.openFileInput (key);
        ObjectInputStream ois = new ObjectInputStream (fis);
        Object object = ois.readObject ();
        return object;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            SharedPreferences settings = getSharedPreferences(CONFIG, 0);
                Set set = settings.getStringSet("Array", null);

            user = settings.getString("User", null);

                violators = new ArrayList<>();
                try {
                   violators = (ArrayList<Person>) readCachedFile(MainActivity.this, "Array");

                    Log.d("People", violators.get(0).getfName());
                } catch(Exception e)
                {
                    Log.d("Failed", "At Main");
                }

            super.onCreate(savedInstanceState);
            people = new ArrayList<>();
            for(int i = 0; i<violators.size(); ++i)
            {
                people.add(violators.get(i));
            }
            deleteFile("Array");
            for(int i = 0; i<violators.size(); ++i) {
                long calc = System.currentTimeMillis() - violators.get(i).getKillTime();
                Log.d(violators.get(i).getfName(), Long.toString(calc));
                if(System.currentTimeMillis() - violators.get(i).getKillTime() >= 86400000)
                {
                    violators.remove(i);
                    people.remove(i);

                }
            }
            try {
                createCachedFile(this.getApplicationContext() , "Array", violators);
//                    editor.putString("Array", ObjectSerializer.serialize(MainActivity.violators));
            } catch(Exception e)
            {
                e.printStackTrace();
                Log.d("Failed", "At Vio");
            }

           /* Person person1 = new Person("John", "", "Doe", "", "123 streetname", "Florence", "29505", "123456789", "d", "12/16/1994", "600", "200",
                                        "SC", "SC", "BLK", "BRO", "M", false, "W", Country.US, "1234567890", "123121234", Ethnicity.N, "S", new ArrayList<Ticket>());
            Person person2 = new Person("Billy", "", "Bob", "", "123 streetname", "Florence", "29505", "123456789", "d", "12/16/1994", "600", "200",
                    "SC", "SC", "BLK", "BRO", "M", false, "W", Country.US, "1234567890", "123121234", Ethnicity.N, "S", new ArrayList<Ticket>());
            Person person3 = new Person("Jane", "", "Doe", "", "123 streetname", "Florence", "29505", "123456789", "d", "12/16/1994", "600", "200",
                    "SC", "SC", "BLK", "BRO", "M", false, "W", Country.US, "1234567890", "123121234", Ethnicity.N, "S", new ArrayList<Ticket>());

            people = new ArrayList<Person>();
            people.add(person1);
            people.add(person2);
            people.add(person3);*/

            setTitle("New Ticket");
            setContentView(R.layout.activity_main);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            findViewById(R.id.read_barcode).setOnClickListener(this);
            findViewById(R.id.skip).setOnClickListener(this);
            findViewById(R.id.submitToken).setOnClickListener(this);
            findViewById(R.id.changeOri).setOnClickListener(this);
            findViewById(R.id.createNew).setOnClickListener(this);


            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.setDrawerListener(toggle);
            toggle.syncState();
            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);
            ori = settings.getString("ORI", "");
            Log.d(TAG, ori);
            if(ori.equals(""))
            {
                EditText text = (EditText) findViewById(R.id.token);
                text.setHint("ORI");

                text.setFilters(new InputFilter[] {
                        new InputFilter.LengthFilter(9)
                });
                flag = true;
            } else {
                EditText text = (EditText) findViewById(R.id.token);
                text.setText(settings.getString("Token", null));
                text.setHint("Token");
                EditText userBox = (EditText) findViewById(R.id.user);
                userBox.setVisibility(View.VISIBLE);
                userBox.setText(settings.getString("User", null));
                RetrieveCsvTask task = new RetrieveCsvTask();
                task.execute("ftp://sitebackups:backmeup~01@nbshome.com/etickets/"+ ori + "/myagency.csv");
                //Thread.sleep(1000);
                Log.d("Token", settings.getString("Token", null));
                Log.d("User", settings.getString("User", null));
                if(!text.getText().equals("") && !userBox.getText().equals("")) {
                    Button btn = (Button) findViewById(R.id.submitToken);
                    btn.performClick();
                }
            }

        } catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    static boolean skipLic = false, skipReg = false;
    public static String ori;

    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {}
    }
    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if(dir!= null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }

    public static void createCachedFile (Context context, String key, ArrayList<Person> vios) throws IOException {

        String tempFile = null;
        for (Person person : vios) {
            FileOutputStream fos = context.openFileOutput (key, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject (vios);
            oos.close ();
            fos.close ();

        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.read_barcode) {
            // launch barcode activity.
            Intent intent = new Intent(this, BarcodeCaptureActivity.class);
            intent.putExtra(BarcodeCaptureActivity.AutoFocus, true);
            intent.putExtra(BarcodeCaptureActivity.UseFlash, false);

            startActivityForResult(intent, RC_BARCODE_CAPTURE);
        } else if(v.getId() == R.id.skip)
        {
            Button btn = (Button) findViewById(R.id.read_barcode);
            if(!skipLic) {
                btn.setText("Scan Registration");
                skipLic = true;
            } else
            {
                FragmentManager fragmentManager = getSupportFragmentManager();
                Fragment frag = null;
                Class fragmentClass = TicketFragment.class;
                skipReg = false;
                skipLic = false;
                btn.setText("Scan License");
                Button btn2 = (Button) findViewById(R.id.skip);
                btn2.setText("Skip");
                btn2.setVisibility(View.INVISIBLE);
                findViewById(R.id.read_barcode).setEnabled(false);
                findViewById(R.id.read_barcode).setVisibility(View.INVISIBLE);

                try {
                    frag = (Fragment) fragmentClass.newInstance();
                    frags.add(frag);
                } catch(Exception e)
                {
                    e.printStackTrace();
                }

                try {
                    fragmentManager.beginTransaction().replace(R.id.flContent, frag).commit();
                } catch(Exception e)
                {
                    e.printStackTrace();
                }

                setTitle("Violator");

            }
        } else if(v.getId() == R.id.submitToken)
        {
            EditText token = (EditText) findViewById(R.id.token);
            SharedPreferences settings = getSharedPreferences(CONFIG, 0);
            SharedPreferences.Editor editor = settings.edit();
            if(flag)
            {
                if(!token.getText().toString().equals("")) {
                    editor.putString("ORI", token.getText().toString());
                    editor.commit();
                    ori = settings.getString("ORI", "");
                    try {
                        RetrieveCsvTask task = new RetrieveCsvTask();
                        task.execute("ftp://sitebackups:backmeup~01@nbshome.com/etickets/" + ori + "/myagency.csv");
                        token.setFilters(new InputFilter[] {
                                new InputFilter.LengthFilter(6)
                        });
                        findViewById(R.id.userText).setVisibility(View.VISIBLE);
                    } catch (Exception ex)
                    {

                        editor.putString("ORI", "");
                        editor.commit();
                        CharSequence err = "Please Enter a valid ORI";
                        int duration     = Toast.LENGTH_SHORT;

                        Toast toast      = Toast.makeText(this, err, duration);
                        toast.show();
                        flag = true;
                    }

                    TextInputLayout err = (TextInputLayout) findViewById(R.id.tokenTextLayout);
                    err.setError("");
                } else {
                    TextInputLayout err = (TextInputLayout) findViewById(R.id.tokenTextLayout);
                    err.setError("You must enter an ORI");
                }
            }
             else if(token.getText().toString().equals(this.token))
            {
                EditText userText = (EditText) findViewById(R.id.user);
                user = userText.getText().toString();
                RetrieveCsvTask task = new RetrieveCsvTask();
                task.setUser(user);
                task.setView(v);
                task.execute("ftp://sitebackups:backmeup~01@nbshome.com/etickets/" + ori + "/logon.csv");
                editor.putString("Token", null);
                editor.putString("User", null);
                editor.commit();

            } else {
                TextInputLayout err = (TextInputLayout) findViewById(R.id.tokenTextLayout);
                err.setError("Wrong Token Value. Please Try again.");
            }
        } else if(v.getId() == R.id.changeOri) {
            EditText text = (EditText) findViewById(R.id.token);
            text.setHint("ORI");
            findViewById(R.id.userText).setVisibility(View.GONE);
            SharedPreferences settings = getSharedPreferences(CONFIG, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("ORI", "");
            editor.commit();
            text.setFilters(new InputFilter[] {
                    new InputFilter.LengthFilter(9)
            });
            flag = true;
        } else if(v.getId() == R.id.createNew) {
            Button btn = (Button) findViewById(R.id.read_barcode);
            Button btn2 = (Button) findViewById(R.id.skip);
            btn.setEnabled(true);
            btn2.setEnabled(true);
            findViewById(R.id.tokenLayout).setVisibility(View.INVISIBLE);
            findViewById(R.id.userText).setVisibility(View.GONE);
        }


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static ArrayList<Fragment> frags = new ArrayList();

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        FragmentManager fragmentManager = getSupportFragmentManager();
        int id = item.getItemId();
        Fragment frag = null;
        Class fragmentClass = null;

        if (id == R.id.nav_camera) {
            /*Intent intent = new Intent(this, BarcodeCaptureActivity.class);
            intent.putExtra(BarcodeCaptureActivity.AutoFocus, true);
            intent.putExtra(BarcodeCaptureActivity.UseFlash, false);

            startActivityForResult(intent, RC_BARCODE_CAPTURE);*/
            skipLic = false;
            skipReg = false;
            setTitle("New Ticket");
            clearVars();
            findViewById(R.id.read_barcode).setEnabled(true);
            findViewById(R.id.read_barcode).setVisibility(View.VISIBLE);
            findViewById(R.id.skip).setVisibility(View.VISIBLE);
            Button btn = (Button) findViewById(R.id.skip);
            btn.setText("Skip");
            btn = (Button) findViewById(R.id.read_barcode);
            btn.setText("Scan License");
            for(Fragment fragment : frags)
            {
                fragmentManager.beginTransaction().remove(fragment).commit();
            }
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_person) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        } else
            fragmentClass = null;
        try {
            frag = (Fragment) fragmentClass.newInstance();
            frags.add(frag);
        } catch(Exception e)
        {
            e.printStackTrace();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        try {
            fragmentManager.beginTransaction().replace(R.id.flContent, frag).commit();
        } catch(Exception e)
        {
            e.printStackTrace();
        }
        item.setChecked(true);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // Restore UI state from the savedInstanceState.
        // This bundle has also been passed to onCreate.
        output = savedInstanceState.getString("Output");
        parsedOutput = savedInstanceState.getString("ParsedOutput");
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.

        savedInstanceState.putString("Output", output);
        savedInstanceState.putString("ParsedOutput", parsedOutput);

        super.onSaveInstanceState(savedInstanceState);
    }


    public static String expDate = "", lastName = "", middleName = "", firstName = "",issuedDate = "", dob = "", sex = "",
           height = "", addr = "", city = "", state = "", zip = "", dlNum = "", dlClass = "", weight = "";

    public static String regExpDate = "", regPlateNum = "", regFirstName = "", regLastName = "", regFullName = "", regAddr = "",
           regCity = "", regState = "", regZip = "", regVin = "", regMake = "", regYear = "", regBody="", regDecal = "";

    public static String courtDate, courtTime, courtType, trialOffId, trialOffName, judgeId, judgeName;

    public static void clearVars() {
        expDate = "";
		lastName = "";
		middleName = "";
		firstName = "";
        issuedDate = "";
		dob = "";
		sex = "";
        height = "";
		addr = "";
		city = "";
		state = "";
		zip = "";
		dlNum = "";
		dlClass = "";
		weight = "";
        regExpDate = "";
		regPlateNum = "";
		regFirstName = "";
		regLastName = "";
		regFullName = "";
		regAddr = "";
        regCity = "";
		regState = "";
		regZip = "";
		regVin = "";
		regMake = "";
		regYear = "";
		regBody = "";
		regDecal = "";
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_BARCODE_CAPTURE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
                    parsedOutput = "";

                    output = barcode.rawValue;
                    boolean temp = false;
                    boolean stateTemp = false;

                    Scanner scan = new Scanner(output);

                    int startIndex;

                    while (scan.hasNextLine()) {
                        String line = scan.nextLine();
                        if(!temp && !skipLic) {
                            if (line.contains("DBC1")) {
                                sex = "M";

                                temp = true;
                            } else {
                                sex = "F";
                            }
                        }
                        if(line.contains("DAJ"))
                        {
                            state = line.substring(3).trim();

                        }
                        if(!skipLic || scan.hasNextLine()) {
                            //region License Stuff
                            if (line.contains("DBA")) {
                                startIndex = line.indexOf("DBA");
                                licScanned = true;
                                skipLic = true;
                                Button btn = (Button) findViewById(R.id.read_barcode);
                                btn.setText("Scan Registration");
                                expDate = line.substring(startIndex + 3).trim();
                            } else if (line.contains("DAA")) {
                                startIndex = line.indexOf("DAA");
                                line = line.substring(startIndex + 3);
                                boolean one = false, two = false, three = false;
                                firstName = "";
                                middleName = "";
                                lastName = "";
                                for (int i = 0; i < line.length(); ++i) {
                                    char ch = line.charAt(i);
                                    if (ch != ',' && ch != '\n') {
                                        if (!one && !two && !three)
                                            lastName += ch;
                                        else if (one && !two && !three)
                                            firstName += ch;
                                        else if (one && two && !three)
                                            middleName += ch;
                                    } else {
                                        if (!one && !two && !three) {
                                            one = true;
                                        } else if (one && !two && !three) {
                                            two = true;
                                        } else if (one && two && !three) {
                                            three = true;
                                        }
                                    }

                                }

                            } else if (line.contains("DCS")) {
                                startIndex = line.indexOf("DCS");
                                lastName = line.substring(startIndex + 3).trim();
                            } else if (line.contains("DAC")) {
                                startIndex = line.indexOf("DAC");
                                firstName = line.substring(startIndex + 3).trim();
                            } else if (line.contains("DAD")) {
                                startIndex = line.indexOf("DAD");
                                middleName = line.substring(startIndex + 3).trim();
                            } else if (line.contains("DBD")) {
                                startIndex = line.indexOf("DBD");
                                issuedDate = line.substring(startIndex + 3).trim();
                            } else if (line.contains("DBB")) {
                                startIndex = line.indexOf("DBB");
                                dob = line.substring(startIndex + 3).trim();
                            }else if (line.contains("DBC")) {
                                startIndex = line.indexOf("DBC");
                                temp = true;
                                if (line.substring(startIndex + 3).trim().equals("1"))
                                    sex = "M";
                                else
                                    sex = "F";

                            } else if (line.contains("DAU")) {
                                startIndex = line.indexOf("DAU");
                                String height = line.substring(startIndex + 3).trim();
                                int h = Integer.parseInt(height.substring(0, 3));
                                int feet = h / 12;
                                int in = h % 12;
                                if (feet >= 9) {
                                    feet = Integer.parseInt(height.substring(0, 1));
                                    in = Integer.parseInt(height.substring(1, 3));
                                }

                                this.height = feet + "-" + in;
                            } else if (line.contains("DAG")) {
                                startIndex = line.indexOf("DAG");
                                addr = line.substring(startIndex + 3).trim();
                            } else if (line.contains("DAI")) {
                                startIndex = line.indexOf("DAI");
                                city = line.substring(startIndex + 3).trim();
                            } else if (line.contains("DAJ")) {
                                startIndex = line.indexOf("DAJ");
                                state = line.substring(startIndex + 3).trim();
                            } else if (line.contains("DAK")) {
                                startIndex = line.indexOf("DAK");
                                zip = line.substring(startIndex + 3).trim();
                            } else if (line.contains("DAQ")) {
                                startIndex = line.indexOf("DAQ");
                                dlNum = line.substring(startIndex + 3).trim();
                            } else if (line.contains("DCA") || line.contains("DAR")) {
                                if (line.contains("DCA")) {
                                    startIndex = line.indexOf("DCA");
                                    dlClass = line.substring(startIndex + 3).trim();
                                } else if (line.contains("DAR")) {
                                    startIndex = line.indexOf("DAR");
                                    dlClass = line.substring(startIndex + 3).trim();
                                }
                            } else if (line.contains("DAW")) {
                                startIndex = line.indexOf("DAW");
                                weight = line.substring(startIndex + 3).trim();
                            }
                            //endregion

                        }
                        if((!skipReg || scan.hasNextLine()) && skipLic) {
                            //region Registration Stuff

                            if (!licScanned) {

                                if (line.contains("RAG")) {
                                    regScanned = true;
                                    skipLic = true;
                                    //skipReg = true;
                                    Button btn = (Button) findViewById(R.id.skip);
                                    btn.setText("Next");
                                    regExpDate = line.substring(3).trim();
                                } else if (line.contains("RAM")) {
                                    regPlateNum = line.substring(3).trim();
                                } else if (line.contains("RBD")) {
                                    regLastName = line.substring(3).trim();
                                } else if (line.contains("RBE")) {
                                    regFirstName = line.substring(3).trim();
                                    if(regFullName.equals(""))
                                        regFullName = regFirstName + " " + regLastName;
                                } else if (line.contains("BBC")) {
                                    regFullName = line.substring(3).trim();
                                } else if (line.contains("RBI")) {
                                    regAddr = line.substring(3).trim();
                                } else if (line.contains("RBK")) {
                                    regCity = line.substring(3).trim();
                                } else if (line.contains("RBL")) {
                                    regState = line.substring(3).trim();
                                } else if (line.contains("RBM")) {
                                    regZip = line.substring(3).trim();
                                } else if (line.contains("VAD")) {
                                    regVin = line.substring(3).trim();
                                } else if (line.contains("VAK")) {
                                    regMake = line.substring(3).trim();
                                } else if (line.contains("VAL")) {
                                    regYear = line.substring(3).trim();
                                } else if (line.contains("VAO")) {
                                    regBody = line.substring(3).trim();
                                } else if (line.contains("RBT")) {
                                    regDecal = line.substring(3).trim();
                                }


                            }


                            //endregion
                        }

                    }


                    if(licScanned)
                    {
                        licScanned = false;
                    } else if(regScanned)
                    {
                        regScanned = false;
                    }

                    Log.d(TAG, "Barcode read: " + barcode.rawValue);
                } else {
                    Log.d(TAG, "No barcode captured, intent data is null");
                }
            } else {
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

 public static String token = "";
    public static  String user, officerId, offName, rank, badgeNum, stateid;
    public static String courtName, courtAddress, courtCity, courtZip, courtState;
    boolean badUsername = false;
    class RetrieveCsvTask extends AsyncTask<String, Void, List<String[]>> {
        private Exception exception;

        @Override
        protected List<String[]> doInBackground(String... urlStr) {

            String[] next = {};
            List<String[]> list = new ArrayList<String[]>();
            try {
                URL url = new URL(urlStr[0]);
                BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
                CSVReader reader = new CSVReader(in);
                for(;;)
                {
                    next = reader.readNext();
                    if(next!= null) {
                        list.add(next);
                    }
                    else {
                        break;
                    }
                }

                return list;
            } catch (Exception ex)
            {
                this.exception = ex;
                return null;
            }
        }

        String user = "NothingHasBeenEntered";
        View v;

        public void setUser(String user) {
            this.user = user;

        }

        public void setView(View v) {
            this.v = v;
        }

        protected void onPostExecute(List<String[]> list)
        {
                ArrayList stuff = new ArrayList();
            if(!user.equals("NothingHasBeenEntered"))
            {
                    for (int i = 1; i < list.size(); ++i) {
                        if (user.toLowerCase().equals(list.get(i)[0].toLowerCase())) {
                            badUsername = false;
                            user = list.get(i)[0];
                            officerId = list.get(i)[1];
                            offName = list.get(i)[2];
                            rank = list.get(i)[3];
                            badgeNum = list.get(i)[4];
                            stateid = list.get(i)[5];

                            Button btn = (Button) findViewById(R.id.read_barcode);
                            Button btn2 = (Button) findViewById(R.id.skip);
                            btn.setEnabled(false);
                            btn2.setEnabled(false);

                            for (int j = 0; j < people.size(); ++j) {
                                CardView card = new CardView(getApplicationContext());
                                CardView.LayoutParams params = new CardView.LayoutParams(
                                        CardView.LayoutParams.MATCH_PARENT,
                                        CardView.LayoutParams.WRAP_CONTENT
                                );
                                final float scale = getResources().getDisplayMetrics().density;

                                params.setMargins(8, 8, 8, 8);
                                params.height = (int) (200 * scale);
                                card.setLayoutParams(params);
                                card.setRadius((int) (10 * scale));
                                card.setCardBackgroundColor(Color.parseColor("#33b5e5"));
                                card.setElevation((int) (4 * scale));
                                TextView tv = new TextView(getApplicationContext());
                                tv.setLayoutParams(params);
                                tv.setText(people.get(j).getfName() + " " + people.get(j).getlName());
                                tv.setTextAppearance(R.style.TextAppearance_AppCompat_Headline);
                                tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                tv.setTextColor(Color.WHITE);
                                card.addView(tv);


                                TextView tv2 = new TextView(getApplicationContext());
                                tv2.setLayoutParams(params);
                                final int temp = j;
                                tv2.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        String url = "ftp://sitebackups:backmeup~01@nbshome.com/etickets/"  + ori + "/printouts/" + people.get(temp).getTicketNumber() + ".pdf";
                                        String uri = "googlechrome://navigate?url=" + url;
                                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));

                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        intent.setPackage("com.android.chrome");
                                        try {
                                            getApplicationContext().startActivity(intent);
                                        } catch (ActivityNotFoundException ex) {
                                            // Chrome browser presumably not installed so allow user to choose instead
                                            intent.setPackage(null);
                                            getApplicationContext().startActivity(intent);
                                        }
                                    }
                                });
                                String text = people.get(j).getTicketNumber();
                                tv2.setText("\n\n\n" + text);
                                tv2.setTextAppearance(R.style.TextAppearance_AppCompat_Headline);
                                tv2.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                tv2.setTextColor(Color.WHITE);
                                tv2.setPaintFlags(tv2.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);

                                TextView tv3 = new TextView(getApplicationContext());
                                tv3.setLayoutParams(params);
                                tv3.setText("\n\n\n\n\n\n\n" + people.get(j).getTickets().get(0).getViolation().getViolation());
                                tv3.setTextColor(Color.WHITE);
                                tv3.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

                                card.addView(tv2);
                                card.addView(tv3);
                                LinearLayout cards = (LinearLayout) findViewById(R.id.cards);
                                cards.addView(card, 0);
                            }

                            findViewById(R.id.submitToken).setVisibility(View.GONE);
                            findViewById(R.id.tokenTextLayout).setVisibility(View.GONE);
                            findViewById(R.id.userText).setVisibility(View.GONE);
                            findViewById(R.id.createNew).setVisibility(View.VISIBLE);
                            findViewById(R.id.changeOri).setVisibility(View.GONE);

                            InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                            //findViewById(R.id.tokenLayout).setVisibility(View.INVISIBLE);
                            return;
                        } else {
                            badUsername = true;
                            CharSequence err = "Please Enter a Valid Username";
                            int duration = Toast.LENGTH_SHORT;
                            Toast toast = Toast.makeText(getApplicationContext(), err, duration);
                            toast.show();
                            return;
                        }
                    }

            } else {
                try {
                    token = list.get(1)[list.get(1).length - 1];

                    courtName = list.get(1)[9];
                    courtAddress = list.get(1)[10];
                    courtCity = list.get(1)[11];
                    courtState = list.get(1)[12];
                    courtZip = list.get(1)[13];
                    courtType = list.get(1)[8];
                    courtTime = list.get(1
                    )[19];
                    flag = false;
                    EditText token = (EditText) findViewById(R.id.token);
                    token.setText("");
                    token.setHint("Token");
                    EditText user = (EditText) findViewById(R.id.user);
                    user.setVisibility(View.VISIBLE);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    CharSequence err = "Please Enter a valid ORI";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(getApplicationContext(), err, duration);
                    toast.show();
                    flag = true;
                }
            }

        }
    }

}
