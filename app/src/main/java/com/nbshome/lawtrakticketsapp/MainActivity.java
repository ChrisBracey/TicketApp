package com.nbshome.lawtrakticketsapp;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import com.nbshome.lawtrakticketsapp.barcode.BarcodeCaptureActivity;
import com.nbshome.lawtrakticketsapp.objects.Person;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import au.com.bytecode.opencsv.CSVReader;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener,
        PersonFragment.OnFragmentInteractionListener, MainContentFragment.OnFragmentInteractionListener,
        TicketFragment.OnFragmentInteractionListener, Registration.OnFragmentInteractionListener,
        CourtFragment.OnFragmentInteractionListener{

    public static ArrayList<Person> violators = new ArrayList<>();

    boolean licScanned = false, regScanned = false;

    private static final int RC_BARCODE_CAPTURE = 9001;
    private static final String TAG = "BarcodeMain";

    public static String output, parsedOutput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);

            setTitle("New Ticket");
            setContentView(R.layout.activity_main);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            findViewById(R.id.read_barcode).setOnClickListener(this);
            findViewById(R.id.skip).setOnClickListener(this);
            findViewById(R.id.submitToken).setOnClickListener(this);

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.setDrawerListener(toggle);
            toggle.syncState();
            RetrieveCsvTask task = new RetrieveCsvTask();
            task.execute("ftp://sitebackups:backmeup~01@nbshome.com/etickets/us/myagency.csv");

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);


        } catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    boolean skipLic = false, skipReg = false;


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
            if(token.getText().toString().equals(this.token))
            {
                Button btn = (Button) findViewById(R.id.read_barcode);
                Button btn2 = (Button) findViewById(R.id.skip);
                btn.setEnabled(true);
                btn2.setEnabled(true);
                findViewById(R.id.submitToken).setEnabled(false);
                findViewById(R.id.token).setEnabled(false);
                findViewById(R.id.tokenLayout).setVisibility(View.INVISIBLE);
            } else {
                TextInputLayout err = (TextInputLayout) findViewById(R.id.tokenTextLayout);
                err.setError("Wrong Token Value");
            }
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

    public void clearVars() {
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

 String token = "";
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

        protected void onPostExecute(List<String[]> list)
        {
            ArrayList stuff = new ArrayList();
            token = list.get(1)[list.get(1).length-1];

        }
    }

}
