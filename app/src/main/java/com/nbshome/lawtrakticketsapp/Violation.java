package com.nbshome.lawtrakticketsapp;

import android.*;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.SyncStateContract;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.nbshome.lawtrakticketsapp.enums.Country;
import com.nbshome.lawtrakticketsapp.objects.Person;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.security.Permission;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.RunnableFuture;

import au.com.bytecode.opencsv.CSVReader;
import fr.ganfra.materialspinner.MaterialSpinner;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Violation.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Violation#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Violation extends Fragment implements View.OnClickListener, LocationListener, AdapterView.OnItemSelectedListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    String code = "";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Violation() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Violation.
     */
    // TODO: Rename and change types and number of parameters
    public static Violation newInstance(String param1, String param2) {
        Violation fragment = new Violation();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    Spinner tc, cdrSpinner, statuteSpinner, date, roadType;
    Button submit, skip;
    TextView locHeader, vioHeader;
    TextInputLayout descLayout, offLayout, pointsLayout, cdrLayout, fineLayout,locLayout, cityLayout,
            zoneLayout, roadNumLayout, latLayout, longLayout, baLayout, dateOfArrestLayout, timeOfArrestLayout,
            timeOfVioLayout, dateOfVioLayout, speedLayout;
    EditText descBox, offBox, pointsBox, cdrBox, fineBox,locBox, cityBox, zoneBox, roadNumBox, latBox,
            longBox, baBox, dateOfArrestBox, timeofArrestBox, timeOfVioBox, dateOfVioBox, speedBox;
    CheckBox refused, blow0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v         = inflater.inflate(R.layout.fragment_violation, container, false);
        submit  = (Button) v.findViewById(R.id.submitViolation);
        skip = (Button) v.findViewById(R.id.skipTC);
        descLayout = (TextInputLayout) v.findViewById(R.id.descText);
        descBox = (EditText) v.findViewById(R.id.desc);
        offLayout = (TextInputLayout) v.findViewById(R.id.offText);
        offBox = (EditText) v.findViewById(R.id.off);
        pointsLayout = (TextInputLayout) v.findViewById(R.id.pointsText);
        pointsBox = (EditText) v.findViewById(R.id.points);
        cdrLayout = (TextInputLayout) v.findViewById(R.id.cdrText);
        cdrBox = (EditText) v.findViewById(R.id.cdr);
        cdrSpinner = (MaterialSpinner) v.findViewById(R.id.cdrCode);
        statuteSpinner = (MaterialSpinner) v.findViewById(R.id.offenseCode);
        fineLayout = (TextInputLayout) v.findViewById(R.id.fineText);
        fineBox = (EditText) v.findViewById(R.id.fine);
        date = (MaterialSpinner) v.findViewById(R.id.date);
        vioHeader = (TextView) v.findViewById(R.id.violationHeader);
        dateOfArrestLayout = (TextInputLayout)v.findViewById(R.id.dateOfArrestText);
        dateOfArrestBox = (EditText) v.findViewById(R.id.dateOfArrest);

        timeOfArrestLayout = (TextInputLayout) v.findViewById(R.id.timeOfArrestText) ;
        timeofArrestBox = (EditText) v.findViewById(R.id.timeOfArrest);

        timeOfVioLayout = (TextInputLayout) v.findViewById(R.id.timeOfVioText);
        timeOfVioBox = (EditText) v.findViewById(R.id.timeOfVio);

        dateOfVioLayout = (TextInputLayout) v.findViewById(R.id.dateOfVioText);
        dateOfVioBox = (EditText) v.findViewById(R.id.dateOfVio);

        speedLayout = (TextInputLayout) v.findViewById(R.id.speedText);
        speedBox = (EditText) v.findViewById(R.id.speed);

        dateOfVioBox.addTextChangedListener(new TextWatcher() {

            private String current = "";
            private String ddmmyyyy = "MMDDYYYY";
            private Calendar cal = Calendar.getInstance();

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals(current)) {
                    String clean = s.toString().replaceAll("[^\\d.]", "");
                    String cleanC = current.replaceAll("[^\\d.]", "");

                    int cl = clean.length();
                    int sel = cl;
                    for (int i = 2; i <= cl && i < 6; i += 2) {
                        sel++;
                    }
                    //Fix for pressing delete next to a forward slash
                    if (clean.equals(cleanC)) sel--;

                    if (clean.length() < 8){
                        clean = clean + ddmmyyyy.substring(clean.length());
                    }else{
                        //This part makes sure that when we finish entering numbers
                        //the date is correct, fixing it otherwise
                        int mon  = Integer.parseInt(clean.substring(0,2));
                        int day  = Integer.parseInt(clean.substring(2,4));
                        int year = Integer.parseInt(clean.substring(4,8));

                        if(mon > 12) mon = 12;
                        cal.set(Calendar.MONTH, mon-1);
                        year = (year<1900)?1900:(year>2100)?2100:year;
                        cal.set(Calendar.YEAR, year);
                        // ^ first set year for the line below to work correctly
                        //with leap years - otherwise, date e.g. 29/02/2012
                        //would be automatically corrected to 28/02/2012

                        day = (day > cal.getActualMaximum(Calendar.DATE))? cal.getActualMaximum(Calendar.DATE):day;
                        clean = String.format("%02d%02d%02d",mon, day, year);
                    }

                    clean = String.format("%s/%s/%s", clean.substring(0, 2),
                            clean.substring(2, 4),
                            clean.substring(4, 8));

                    sel = sel < 0 ? 0 : sel;
                    current = clean;
                    dateOfVioBox.setText(current);
                    dateOfVioBox.setSelection(sel < current.length() ? sel : current.length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        dateOfArrestBox.addTextChangedListener(new TextWatcher() {

            private String current = "";
            private String ddmmyyyy = "MMDDYYYY";
            private Calendar cal = Calendar.getInstance();

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals(current)) {
                    String clean = s.toString().replaceAll("[^\\d.]", "");
                    String cleanC = current.replaceAll("[^\\d.]", "");

                    int cl = clean.length();
                    int sel = cl;
                    for (int i = 2; i <= cl && i < 6; i += 2) {
                        sel++;
                    }
                    //Fix for pressing delete next to a forward slash
                    if (clean.equals(cleanC)) sel--;

                    if (clean.length() < 8){
                        clean = clean + ddmmyyyy.substring(clean.length());
                    }else{
                        //This part makes sure that when we finish entering numbers
                        //the date is correct, fixing it otherwise
                        int mon  = Integer.parseInt(clean.substring(0,2));
                        int day  = Integer.parseInt(clean.substring(2,4));
                        int year = Integer.parseInt(clean.substring(4,8));

                        if(mon > 12) mon = 12;
                        cal.set(Calendar.MONTH, mon-1);
                        year = (year<1900)?1900:(year>2100)?2100:year;
                        cal.set(Calendar.YEAR, year);
                        // ^ first set year for the line below to work correctly
                        //with leap years - otherwise, date e.g. 29/02/2012
                        //would be automatically corrected to 28/02/2012

                        day = (day > cal.getActualMaximum(Calendar.DATE))? cal.getActualMaximum(Calendar.DATE):day;
                        clean = String.format("%02d%02d%02d",mon, day, year);
                    }

                    clean = String.format("%s/%s/%s", clean.substring(0, 2),
                            clean.substring(2, 4),
                            clean.substring(4, 8));

                    sel = sel < 0 ? 0 : sel;
                    current = clean;
                    dateOfArrestBox.setText(current);
                    dateOfArrestBox.setSelection(sel < current.length() ? sel : current.length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        locHeader = (TextView) v.findViewById(R.id.locHeader);

        locLayout = (TextInputLayout) v.findViewById(R.id.locText);
        locBox = (EditText) v.findViewById(R.id.loc);

        cityLayout = (TextInputLayout) v.findViewById(R.id.locCityText);
        cityBox = (EditText) v.findViewById(R.id.locCity);

        zoneLayout = (TextInputLayout) v.findViewById(R.id.locZoneText);
        zoneBox = (EditText) v.findViewById(R.id.locZone);

        roadType = (MaterialSpinner) v.findViewById(R.id.roadType);
        roadType.setOnItemSelectedListener(this);

        roadNumLayout = (TextInputLayout) v.findViewById(R.id.locRoadNumText);
        roadNumBox = (EditText) v.findViewById(R.id.locRoadNum);

        latLayout = (TextInputLayout) v.findViewById(R.id.latText);
        latBox = (EditText) v.findViewById(R.id.lat);

        longLayout = (TextInputLayout) v.findViewById(R.id.longText);
        longBox = (EditText) v.findViewById(R.id.locLong);

        baLayout = (TextInputLayout) v.findViewById(R.id.baLevelText);
        baBox = (EditText) v.findViewById(R.id.baLevel);
        refused = (CheckBox) v.findViewById(R.id.baRefused);
        blow0 = (CheckBox) v.findViewById(R.id.ba0);


        doaLayout = (TextInputLayout) v.findViewById(R.id.dateOfArrestText);
        doa = (EditText) v.findViewById(R.id.dateOfArrest);

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        if(checkLocationPermission()) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }


        skip.setOnClickListener(this);
        submit.setOnClickListener(this);
        tc        = (MaterialSpinner) v.findViewById(R.id.trafficCode);
        RetrieveCsvTask task = new RetrieveCsvTask();
        task.setSpinner(tc);
        task.execute("ftp://sitebackups:backmeup~01@nbshome.com/etickets/" + MainActivity.ori + "/traffic.csv");


        RetrieveCsvTask task1 = new RetrieveCsvTask();
        task1.setSpinner(date);
        task1.isDate(true);
        task1.execute("ftp://sitebackups:backmeup~01@nbshome.com/etickets/" + MainActivity.ori + "/trialdatelist.csv");


        RetrieveCsvTask task2 = new RetrieveCsvTask();
        task2.setSpinner(roadType);
        task2.isRoadType(true);
        task2.execute("ftp://sitebackups:backmeup~01@nbshome.com/etickets/" + MainActivity.ori + "/roadtype.csv");

        return v;
    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission. ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return false;
        } else {
            return true;
        }
    }
LocationManager locationManager;
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                    }
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(getActivity(), "Permission denied to read Location", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    boolean skippedTC = false, skippedCDR = false, flag = false, otherFlag = false;

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

    CheckBox courtReq, ROA, insVer, vehSearched;

    private static final String CONFIG = "Config";

    private TextInputLayout doaLayout;
    private EditText doa;
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.submitViolation) {
            if (flag && !otherFlag) {
                baLayout.setVisibility(View.GONE);
                blow0.setVisibility(View.GONE);
                refused.setVisibility(View.GONE);
                date.setVisibility(View.VISIBLE);
                fineLayout.setVisibility(View.VISIBLE);
                descLayout.setVisibility(View.GONE);
                offLayout.setVisibility(View.GONE);
                pointsLayout.setVisibility(View.GONE);
                cdrLayout.setVisibility(View.GONE);
                cdrSpinner.setVisibility(View.GONE);
                statuteSpinner.setVisibility(View.GONE);
                locLayout.setVisibility(View.VISIBLE);
                cityLayout.setVisibility(View.VISIBLE);
                zoneLayout.setVisibility(View.VISIBLE);
                roadType.setVisibility(View.VISIBLE);
                locHeader.setVisibility(View.VISIBLE);
                latLayout.setVisibility(View.VISIBLE);
                longLayout.setVisibility(View.VISIBLE);
                doaLayout.setVisibility(View.VISIBLE);
                timeOfArrestLayout.setVisibility(View.VISIBLE);
                timeOfVioLayout.setVisibility(View.VISIBLE);
                dateOfVioLayout.setVisibility(View.VISIBLE);

                vioHeader.setText("Court Information");
                courtReq = (CheckBox)getActivity().findViewById(R.id.courtReq);
                courtReq.setVisibility(View.VISIBLE);
                ROA = (CheckBox)getActivity().findViewById(R.id.acc);
                ROA.setVisibility(View.VISIBLE);
                insVer = (CheckBox)getActivity().findViewById(R.id.insVer);
                insVer.setVisibility(View.VISIBLE);
                vehSearched = (CheckBox)getActivity().findViewById(R.id.vehSearched);
                vehSearched.setVisibility(View.VISIBLE);



                otherFlag = true;

                tc.setVisibility(View.GONE);
            } else if (otherFlag)
            {
                com.nbshome.lawtrakticketsapp.objects.Violation vio = new com.nbshome.lawtrakticketsapp.objects.Violation();
                vio.setCourtAppearance(((CheckBox)getActivity().findViewById(R.id.courtReq)).isChecked());
                vio.setInsuranceVer(((CheckBox)getActivity().findViewById(R.id.insVer)).isChecked());
                vio.setVehSearched(((CheckBox)getActivity().findViewById(R.id.vehSearched)).isChecked());
                vio.setResultOfAcc(((CheckBox)getActivity().findViewById(R.id.acc)).isChecked());
                vio.setPoints(pointsBox.getText().toString());
                vio.setRoadType(roadType.getSelectedItem().toString());
                vio.setVioLat(latBox.getText().toString());
                vio.setViolationCity(cityBox.getText().toString());
                vio.setViolationLocation(locBox.getText().toString());
                vio.setVioLong(longBox.getText().toString());
                vio.setZone(zoneBox.getText().toString());
                vio.setViolation(descBox.getText().toString());
                vio.setRoadNum(roadNumBox.getText().toString());
                vio.setViolationSectionNo(section);
                vio.setTimeOfArrest(timeofArrestBox.getText().toString());
                vio.setViolationTime(timeOfVioBox.getText().toString());
                vio.setViolationDate(dateOfVioBox.getText().toString());
                vio.setPostActSpeed(speedBox.getText().toString());




                final String f_name = new SimpleDateFormat("yyyyMMddhhmm").format(new Date());
                MainActivity.violators.get(MainActivity.violators.size()-1).setTicketNumber(f_name);
                MainActivity.courtDate = date.getSelectedItem().toString();

                MainActivity.violators.get(MainActivity.violators.size() - 1).getTickets().get
                        (MainActivity.violators.get(MainActivity.violators.size() - 1).getTickets().size() - 1).setViolation(vio);

                SharedPreferences prefs = getActivity().getSharedPreferences("Config", 0);
                SharedPreferences.Editor editor = prefs.edit();
                try {
                        createCachedFile(getContext(), "Array", MainActivity.violators);
//                    editor.putString("Array", ObjectSerializer.serialize(MainActivity.violators));
                } catch(Exception e)
                {
                    e.printStackTrace();
                    Log.d("Failed", "At Vio");
                }
                editor.commit();
                try {
                    File root = getContext().getFilesDir();
                    if (root.canWrite()) {
                        File dir = new File(root.getAbsoluteFile() + "/ftp");
                        dir.mkdirs();
                        final File f = new File(dir, f_name + ".xml");
                        FileWriter fw = new FileWriter(f);
                        BufferedWriter out = new BufferedWriter(fw);
                        MainActivity.violators.get(MainActivity.violators.size()-1).getTickets().get(MainActivity.violators.get(
                                MainActivity.violators.size()-1).getTickets().size() - 1)
                                .getViolation().setBALevel(baBox.getText().toString());
                        Log.d("FIle Name", f_name);
                        out.write("<VIOLATOR>\r\n" +
                                "\t<Status>A" + "</Status>\r\n" +
                                "\t<TicketNum>" + f_name +"</TicketNum>\r\n" +
                                "\t<CaseNum>" + "</CaseNum>\r\n" +
                                "\t<FirstName>" + MainActivity.violators.get(MainActivity.violators.size()-1).getfName() + "</FirstName>\r\n" +
                                "\t<MiddleName>" + MainActivity.violators.get(MainActivity.violators.size()-1).getmName() + "</MiddleName>\r\n" +
                                "\t<LastName>" + MainActivity.violators.get(MainActivity.violators.size()-1).getlName() + "</LastName>\r\n" +
                                "\t<Suffix>" + MainActivity.violators.get(MainActivity.violators.size()-1).getSuffix() + "</Suffix>\r\n" +
                                "\t<Address>" + MainActivity.violators.get(MainActivity.violators.size()-1).getAddr() + "</Address>\r\n" +
                                "\t<City>" + MainActivity.violators.get(MainActivity.violators.size()-1).getCity() + "</City>\r\n" +
                                "\t<Zip>" + MainActivity.violators.get(MainActivity.violators.size()-1).getZip() + "</Zip>\r\n" +
                                "\t<State>" + MainActivity.violators.get(MainActivity.violators.size()-1).getDlState() + "</State>\r\n" +
                                "\t<DLNum>" + MainActivity.violators.get(MainActivity.violators.size()-1).getDlNum() + "</DLNum>\r\n" +
                                "\t<DLClass>" + MainActivity.violators.get(MainActivity.violators.size()-1).getDlClass() + "</DLClass>\r\n" +
                                "\t<DOB>" + MainActivity.violators.get(MainActivity.violators.size()-1).getDob() + "</DOB>\r\n" +
                                "\t<Height>" + MainActivity.violators.get(MainActivity.violators.size()-1).getHeight() + "</Height>\r\n" +
                                "\t<Weight>" + MainActivity.violators.get(MainActivity.violators.size()-1).getWeight() + "</Weight>\r\n" +
                                "\t<Hair>" + MainActivity.violators.get(MainActivity.violators.size()-1).getHair() + "</Hair>\r\n" +
                                "\t<Eyes>" + MainActivity.violators.get(MainActivity.violators.size()-1).getEyes() + "</Eyes>\r\n" +
                                "\t<Sex>" + MainActivity.violators.get(MainActivity.violators.size()-1).getSex() + "</Sex>\r\n" +
                                "\t<CDL>" + MainActivity.violators.get(MainActivity.violators.size()-1).isCdl() + "</CDL>\r\n" +
                                "\t<Race>" + MainActivity.violators.get(MainActivity.violators.size()-1).getRace() + "</Race>\r\n" +
                                "\t<Country>" + MainActivity.violators.get(MainActivity.violators.size()-1).getCountry().name() + "</Country>\r\n" +
                                "\t<Phone>" + MainActivity.violators.get(MainActivity.violators.size()-1).getPhone() + "</Phone>\r\n" +
                                "\t<SSN>" + MainActivity.violators.get(MainActivity.violators.size()-1).getSsn() + "</SSN>\r\n" +
                                "\t<Ethnicity>" + MainActivity.violators.get(MainActivity.violators.size()-1).getEthnicity().name() + "</Ethnicity>\r\n" +
                                "\t<Residence>" + MainActivity.violators.get(MainActivity.violators.size()-1).getResidence() + "</Residence>\r\n" +
                                "\t<REGISTRATION>\r\n" +
                                "\t\t<FirstName>" + MainActivity.violators.get(MainActivity.violators.size()-1).getTickets().get(MainActivity.violators.get(
                                        MainActivity.violators.size()-1).getTickets().size() - 1)
                                        .getOwner().getfName() + "</FirstName>\r\n" +
                                "\t\t<LastName>"  + MainActivity.violators.get(MainActivity.violators.size()-1).getTickets().get(MainActivity.violators.get(
                                MainActivity.violators.size()-1).getTickets().size() - 1)
                                .getOwner().getlName() + "</LastName>\r\n" +
                                "\t\t<FullName>"  + MainActivity.violators.get(MainActivity.violators.size()-1).getTickets().get(MainActivity.violators.get(
                                MainActivity.violators.size()-1).getTickets().size() - 1)
                                .getOwner().getFullName() + "</FullName>\r\n" +
                                "\t\t<Address>"  + MainActivity.violators.get(MainActivity.violators.size()-1).getTickets().get(MainActivity.violators.get(
                                MainActivity.violators.size()-1).getTickets().size() - 1)
                                .getOwner().getAddr() + "</Address>\r\n" +
                                "\t\t<City>"  + MainActivity.violators.get(MainActivity.violators.size()-1).getTickets().get(MainActivity.violators.get(
                                MainActivity.violators.size()-1).getTickets().size() - 1)
                                .getOwner().getCity() + "</City>\r\n" +
                                "\t\t<Zip>"  + MainActivity.violators.get(MainActivity.violators.size()-1).getTickets().get(MainActivity.violators.get(
                                MainActivity.violators.size()-1).getTickets().size() - 1)
                                .getOwner().getZip() + "</Zip>\r\n" +
                                "\t\t<State>"  + MainActivity.violators.get(MainActivity.violators.size()-1).getTickets().get(MainActivity.violators.get(
                                MainActivity.violators.size()-1).getTickets().size() - 1)
                                .getOwner().getState() + "</State>\r\n" +
                                "\t</REGISTRATION>\r\n" +
                                "\t<VEHICLE>\r\n" +
                                "\t\t<Year>" + MainActivity.violators.get(MainActivity.violators.size()-1).getTickets().get(MainActivity.violators.get(
                                MainActivity.violators.size()-1).getTickets().size() - 1)
                                .getOwner().getVehicle().getYear() + "</Year>\r\n" +
                                "\t\t<Make>"  + MainActivity.violators.get(MainActivity.violators.size()-1).getTickets().get(MainActivity.violators.get(
                                MainActivity.violators.size()-1).getTickets().size() - 1)
                                .getOwner().getVehicle().getMake() + "</Make>\r\n" +
                                "\t\t<Type>"  + MainActivity.violators.get(MainActivity.violators.size()-1).getTickets().get(MainActivity.violators.get(
                                MainActivity.violators.size()-1).getTickets().size() - 1)
                                .getOwner().getVehicle().getType() + "</Type>\r\n" +
                                "\t\t<PlateNum>"  + MainActivity.violators.get(MainActivity.violators.size()-1).getTickets().get(MainActivity.violators.get(
                                MainActivity.violators.size()-1).getTickets().size() - 1)
                                .getOwner().getVehicle().getPlateNum() + "</PlateNum>\r\n" +
                                "\t\t<PlateState>"  + MainActivity.violators.get(MainActivity.violators.size()-1).getTickets().get(MainActivity.violators.get(
                                MainActivity.violators.size()-1).getTickets().size() - 1)
                                .getOwner().getVehicle().getPlateState() + "</PlateState>\r\n" +
                                "\t</VEHICLE>\r\n" +
                                "\t<VIOLATION>\r\n" +
                                "\t\t<TrafficCode>"+ code + "</TrafficCode>\r\n" +
                                "\t\t<ViolationSectionNum>"  + MainActivity.violators.get(MainActivity.violators.size()-1).getTickets().get(MainActivity.violators.get(
                                MainActivity.violators.size()-1).getTickets().size() - 1)
                                .getViolation().getViolationSectionNo() + "</ViolationSectionNum>\r\n" +
                                "\t\t<ViolationDescription>" + longDesc + "</ViolationDescription>\r\n" +
                                "\t\t<ViolationShort>" + desc + "</ViolationShort>\r\n" +
                                "\t\t<ViolationDate>" + MainActivity.violators.get(MainActivity.violators.size()-1).getTickets().get(MainActivity.violators.get(
                                MainActivity.violators.size()-1).getTickets().size() - 1)
                                .getViolation().getViolationDate() + "</ViolationDate>\r\n" +
                                "\t\t<ViolationTime>" + MainActivity.violators.get(MainActivity.violators.size()-1).getTickets().get(MainActivity.violators.get(
                                MainActivity.violators.size()-1).getTickets().size() - 1)
                                .getViolation().getViolationTime() + "</ViolationTime>\r\n" +
                                "\t\t<PostedActSpeed>"  + MainActivity.violators.get(MainActivity.violators.size()-1).getTickets().get(MainActivity.violators.get(
                                MainActivity.violators.size()-1).getTickets().size() - 1)
                                .getViolation().getPostActSpeed() + "</PostedActSpeed>\r\n" +
                                "\t\t<Points>" + MainActivity.violators.get(MainActivity.violators.size()-1).getTickets().get(MainActivity.violators.get(
                                MainActivity.violators.size()-1).getTickets().size() - 1)
                                .getViolation().getPoints() + "</Points>\r\n" +
                                "\t\t<BALevel>" + MainActivity.violators.get(MainActivity.violators.size()-1).getTickets().get(MainActivity.violators.get(
                                MainActivity.violators.size()-1).getTickets().size() - 1)
                                .getViolation().getBALevel() + "</BALevel>\r\n" +
                                "\t\t<Blow0>" + blow0.isChecked() + "</Blow0>\r\n" +
                                "\t\t<BARefused>" + refused.isChecked() + "</BARefused>\r\n" +
                                "\t\t<Location>"  + MainActivity.violators.get(MainActivity.violators.size()-1).getTickets().get(MainActivity.violators.get(
                                MainActivity.violators.size()-1).getTickets().size() - 1)
                                .getViolation().getViolationLocation() + "</Location>\r\n" +
                                "\t\t<Zone>" + MainActivity.violators.get(MainActivity.violators.size()-1).getTickets().get(MainActivity.violators.get(
                                MainActivity.violators.size()-1).getTickets().size() - 1)
                                .getViolation().getZone() + "</Zone>\r\n" +
                                "\t\t<RoadType>" + MainActivity.violators.get(MainActivity.violators.size()-1).getTickets().get(MainActivity.violators.get(
                                MainActivity.violators.size()-1).getTickets().size() - 1)
                                .getViolation().getRoadType() + "</RoadType>\r\n" +
                                "\t\t<RoadNum>" + MainActivity.violators.get(MainActivity.violators.size()-1).getTickets().get(MainActivity.violators.get(
                                MainActivity.violators.size()-1).getTickets().size() - 1)
                                .getViolation().getRoadNum() + "</RoadNum>\r\n" +
                                "\t\t<County>" + MainActivity.violators.get(MainActivity.violators.size()-1).getTickets().get(MainActivity.violators.get(
                                MainActivity.violators.size()-1).getTickets().size() - 1)
                                .getViolation().getCounty() + "</County>\r\n" +
                                "\t\t<Lat>" + MainActivity.violators.get(MainActivity.violators.size()-1).getTickets().get(MainActivity.violators.get(
                                MainActivity.violators.size()-1).getTickets().size() - 1)
                                .getViolation().getVioLat() + "</Lat>\r\n" +
                                "\t\t<Long>" + MainActivity.violators.get(MainActivity.violators.size()-1).getTickets().get(MainActivity.violators.get(
                                MainActivity.violators.size()-1).getTickets().size() - 1)
                                .getViolation().getVioLong() + "</Long>\r\n" +
                                "\t\t<City>" + MainActivity.violators.get(MainActivity.violators.size()-1).getTickets().get(MainActivity.violators.get(
                                MainActivity.violators.size()-1).getTickets().size() - 1)
                                .getViolation().getViolationCity() + "</City>\r\n" +
                                "\t\t<CourtAppearance>" + MainActivity.violators.get(MainActivity.violators.size()-1).getTickets().get(MainActivity.violators.get(
                                MainActivity.violators.size()-1).getTickets().size() - 1)
                                .getViolation().isCourtAppearance() + "</CourtAppearance>\r\n" +
                                "\t\t<ResultOfAcc>" + MainActivity.violators.get(MainActivity.violators.size()-1).getTickets().get(MainActivity.violators.get(
                                MainActivity.violators.size()-1).getTickets().size() - 1)
                                .getViolation().isResultOfAcc() + "</ResultOfAcc>\r\n" +
                                "\t\t<InsuranceVer>" + MainActivity.violators.get(MainActivity.violators.size()-1).getTickets().get(MainActivity.violators.get(
                                MainActivity.violators.size()-1).getTickets().size() - 1)
                                .getViolation().isInsuranceVer() + "</InsuranceVer>\r\n" +
                                "\t\t<VehSearched>" + MainActivity.violators.get(MainActivity.violators.size()-1).getTickets().get(MainActivity.violators.get(
                                MainActivity.violators.size()-1).getTickets().size() - 1)
                                .getViolation().isVehSearched() + "</VehSearched>\r\n" +
                                "\t</VIOLATION>\r\n" +
                                "\t<OFFICER>\r\n" +
                                "\t\t<Name>" + MainActivity.rank + " " + MainActivity.offName + "</Name>\r\n" +
                                "\t\t<IDNum>" + MainActivity.stateid + "</IDNum>\r\n" +
                                "\t\t<BailDeposited>" + "</BailDeposited>\r\n" +
                                "\t\t<ArrestDate>" + doa.getText().toString()+ "</ArrestDate>\r\n" +
                                "\t\t<ArrestTime>"+ MainActivity.violators.get(MainActivity.violators.size()-1).getTickets().get(MainActivity.violators.get(
                                MainActivity.violators.size()-1).getTickets().size() - 1)
                                .getViolation().getTimeOfArrest() + "</ArrestTime>\r\n" +
                                "\t\t<BailJail>" + "</BailJail>\r\n" +
                                "\t\t<ViolationDate>" + "</ViolationDate>\r\n" +
                                "\t\t<BondAmount>" + "</BondAmount>\r\n" +
                                "\t</OFFICER>\r\n" +
                                "\t<COURT>\r\n" +
                                "\t\t<Court>" + MainActivity.courtName + "</Court>\r\n" +
                                "\t\t<Address>" + MainActivity.courtAddress + "</Address>\r\n" +
                                "\t\t<City>" + MainActivity.courtCity + "</City>\r\n" +
                                "\t\t<ZIP>" + MainActivity.courtZip + "</ZIP>\r\n" +
                                "\t\t<State>" + MainActivity.courtState + "</State>\r\n" +
                                "\t\t<Date>" + MainActivity.courtDate + "</Date>\r\n" +
                                "\t\t<Time>" + MainActivity.courtTime + "</Time>\r\n" +
                                "\t</COURT>\r\n" +
                                "</VIOLATOR>");
                        out.close();

                        Log.d("File", "Made it to file create");
                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    URL url = new URL("ftp://sitebackups:backmeup~01@nbshome.com/etickets/" + MainActivity.ori + "/printouts/" + f_name + ".xml");
                                    URLConnection conn = url.openConnection();
                                    OutputStream outputStream = conn.getOutputStream();
                                    FileInputStream inputStream = new FileInputStream(f.getAbsolutePath());



                                    byte[] buffer = new byte[4096];
                                    int bytesRead;
                                    while((bytesRead = inputStream.read(buffer)) != -1) {
                                        outputStream.write(buffer, 0, bytesRead);
                                    }


                                    inputStream.close();
                                    outputStream.close();
                                    f.delete();
                                    Log.d("File", "Uploaded");
                                    SharedPreferences settings = getActivity().getSharedPreferences(CONFIG, 0);
                                    SharedPreferences.Editor editor = settings.edit();
                                    editor.putString("Token", MainActivity.token);
                                    editor.putString("User", MainActivity.user);
                                    editor.commit();

                                    Intent i = getActivity().getBaseContext().getPackageManager()
                                            .getLaunchIntentForPackage( getActivity().getBaseContext().getPackageName() );
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(i);

                                } catch ( Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        thread.start();



                    }

                } catch(Exception e) {
                        Log.d("File", "Failed at file create");
                    e.printStackTrace();
                }
            }
            else {
                if (!skippedTC) {
                    code = tc.getSelectedItem().toString();
                    RetrieveCsvTask task = new RetrieveCsvTask();
                    Log.d("STUFF", tc.getSelectedItem().toString().substring(0, 2));
                    task.setFlag(true, tc.getSelectedItem().toString().substring(0, 2));
                    task.execute("ftp://sitebackups:backmeup~01@nbshome.com/etickets/" + MainActivity.ori + "/traffic.csv");
                    skip.setVisibility(View.INVISIBLE);
                    descLayout.setVisibility(View.VISIBLE);
                    offLayout.setVisibility(View.VISIBLE);
                    pointsLayout.setVisibility(View.VISIBLE);
                    cdrLayout.setVisibility(View.VISIBLE);
                    tc.setVisibility(View.GONE);

                    flag = true;
                } else if (!skippedCDR) {
                    RetrieveCsvTask task = new RetrieveCsvTask();
                    task.setOtherFlag(true, cdrSpinner.getSelectedItem().toString().split(" ")[0]);
                    task.setSpinner(cdrSpinner);
                    task.execute("ftp://sitebackups:backmeup~01@nbshome.com/etickets/" + MainActivity.ori + "/cdrs.csv");

                    skip.setVisibility(View.INVISIBLE);
                    descLayout.setVisibility(View.VISIBLE);
                    offLayout.setVisibility(View.VISIBLE);
                    pointsLayout.setVisibility(View.VISIBLE);
                    cdrLayout.setVisibility(View.VISIBLE);
                    cdrSpinner.setVisibility(View.GONE);

                    flag = true;
                } else {
                    RetrieveCsvTask task = new RetrieveCsvTask();
                    Log.d("MADEIT", "Made it");
                    task.setOtherOtherFlag(true, statuteSpinner.getSelectedItem().toString());
                    task.setSpinner(statuteSpinner);
                    task.execute("ftp://sitebackups:backmeup~01@nbshome.com/etickets/" + MainActivity.ori + "/statutes.csv");
                    skip.setVisibility(View.INVISIBLE);
                    descLayout.setVisibility(View.VISIBLE);
                    offLayout.setVisibility(View.VISIBLE);
                    pointsLayout.setVisibility(View.VISIBLE);
                    cdrLayout.setVisibility(View.VISIBLE);
                    statuteSpinner.setVisibility(View.GONE);
                    flag = true;
                }
            }

        } else if(v.getId() == R.id.skipTC) {
            if(skippedTC)
            {
                skippedCDR = true;
                cdrSpinner.setVisibility(View.GONE);
                statuteSpinner.setVisibility(View.VISIBLE);
                RetrieveCsvTask task = new RetrieveCsvTask();
                task.setOtherOtherFlag(true, "");
                task.setSpinner(statuteSpinner);
                task.execute("ftp://sitebackups:backmeup~01@nbshome.com/etickets/" + MainActivity.ori + "/statutes.csv");

            } else {
                tc.setVisibility(View.GONE);
                cdrSpinner.setVisibility(View.VISIBLE);
                RetrieveCsvTask task = new RetrieveCsvTask();
                task.setOtherFlag(true, "");
                task.setSpinner(cdrSpinner);
                task.execute("ftp://sitebackups:backmeup~01@nbshome.com/etickets/" + MainActivity.ori + "/cdrs.csv");
                skippedTC = true;
            }
        }
    }

    double locLat = 0, locLong = 0;
    boolean locFlag = false;

    @Override
    public void onLocationChanged(Location location) {
        locLat = location.getLatitude();
        locLong = location.getLongitude();
        if((locLat != 0 || locLong !=0) && !locFlag)
        {
            latBox.setText(Location.convert(locLat, Location.FORMAT_SECONDS));
            longBox.setText(Location.convert(locLong, Location.FORMAT_SECONDS));
            locFlag = true;
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
    int currentItem = 0;

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.d("Road Type Selected", roadType.getItemAtPosition(position).toString());
        if(currentItem == position)
        {
            return;
        }
       if(roadType.getItemAtPosition(position).toString().equals("I") || roadType.getItemAtPosition(position).toString().equals("SC") || roadType.getItemAtPosition(position).toString().equals("SEC") || roadType.getItemAtPosition(position).toString().equals("US"))
       {
           roadNumLayout.setVisibility(View.VISIBLE);
           currentItem = position;
       } else {
           roadNumLayout.setVisibility(View.GONE);
           currentItem = position;
       }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    ArrayAdapter<String> testAdapter;
    ArrayList codes;
    String desc, longDesc;
public static String section = "";

    class RetrieveCsvTask extends AsyncTask<String, Void, List<String[]>> {
        private Exception exception;
        private Spinner spin;
        private boolean flag = false, otherFlag = false, otherOtherFlag = false, date = false, roadType = false;
        private String code;
        public void setSpinner(Spinner spin)
        {
            this.spin = spin;
        }
        public void setFlag(boolean flag, String code) {
            this.flag = flag;
            this.code = code;
        }

        public void setOtherFlag(boolean otherFlag, String code)
        {
            this.otherFlag = otherFlag;
            this.code = code;
        }

        public void setOtherOtherFlag(boolean otherOtherFlag, String code)
        {
            this.otherOtherFlag = otherOtherFlag;
            this.code = code;
        }

        public void isDate(boolean date) {
            this.date = date;
        }

        public void isRoadType(boolean roadType) {
            this.roadType = roadType;
        }

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
            if(date)
            {
                ArrayList dates = new ArrayList();
                for(int i = 1; i<list.size(); ++i)
                {
                    dates.add(list.get(i)[0]);
                }

                spin.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, dates));
            }
            else if(roadType)
            {
                ArrayList type = new ArrayList();
                for(int i = 1; i<list.size(); ++i)
                {
                    type.add(list.get(i)[0]);
                }

                spin.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, type));
            }
            else {
                if (!flag && !otherFlag && !otherOtherFlag) {
                    codes = new ArrayList();
                    for (int i = 1; i < list.size(); ++i) {
                        String temp = list.get(i)[0] + " " + list.get(i)[1];
                        Log.d("Stuff", temp);
                        codes.add(temp);
                    }
                    spin.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, codes));
                } else if (flag) {
                    for (int i = 1; i < list.size(); ++i) {
                        if (list.get(i)[0].equals(code)) {
                            if(list.get(i)[2].contains("56-05-2930") || list.get(i)[2].contains("56-05-2933") || list.get(i)[2].contains("56-05-2945"))
                            {
                                baLayout.setVisibility(View.VISIBLE);
                                refused.setVisibility(View.VISIBLE);
                                blow0.setVisibility(View.VISIBLE);
                            }

                            if(list.get(i)[2].contains("56-05-1520"))
                            {
                                speedLayout.setVisibility(View.VISIBLE);
                            }
                            descBox.setText(list.get(i)[1]);
                            longDesc = list.get(i)[4];
                            desc = list.get(i)[1];
                            offBox.setText(list.get(i)[2]);
                            pointsBox.setText(list.get(i)[5]);
                            cdrBox.setText(list.get(i)[3]);
                            offBox.setInputType(InputType.TYPE_NULL);
                            pointsBox.setInputType(InputType.TYPE_NULL);
                            cdrBox.setInputType(InputType.TYPE_NULL);
                            section = list.get(i)[3] + " / " + list.get(i)[2];
                            break;
                        }
                    }
                } else if (otherFlag && code.equals("")) {
                    codes = new ArrayList();
                    for (int i = 1; i < list.size(); ++i) {
                        String temp = list.get(i)[1] + " " + list.get(i)[2];

                        codes.add(temp);
                    }
                    spin.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, codes));
                } else if (otherFlag && !code.equals("")) {
                    for (int i = 1; i < list.size(); ++i) {
                        if (list.get(i)[1].equals(code)) {
                            if(list.get(i)[0].contains("56-05-2930") || list.get(i)[0].contains("56-05-2933") || list.get(i)[0].contains("56-05-2945"))
                            {
                                baLayout.setVisibility(View.VISIBLE);
                                refused.setVisibility(View.VISIBLE);
                                blow0.setVisibility(View.VISIBLE);
                            }
                            descBox.setText(list.get(i)[2]);
                            offBox.setText(list.get(i)[0]);
                            pointsBox.setText(list.get(i)[4]);
                            cdrBox.setText(list.get(i)[1]);
                            offBox.setInputType(InputType.TYPE_NULL);
                            pointsBox.setInputType(InputType.TYPE_NULL);
                            cdrBox.setInputType(InputType.TYPE_NULL);

                            break;
                        }
                    }
                } else if (otherOtherFlag && code.equals("")) {
                    codes = new ArrayList();
                    for (int i = 1; i < list.size(); ++i) {
                        String temp = list.get(i)[0] + " " + list.get(i)[2];

                        codes.add(temp);
                    }
                    spin.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, codes));

                } else if (otherOtherFlag && !code.equals("")) {
                    for (int i = 1; i < list.size(); ++i) {
                        Log.d("STUFF", list.get(i)[0] + " " + list.get(i)[2]);
                        if ((list.get(i)[0] + " " + list.get(i)[2]).equals(code)) {
                            if(list.get(i)[0].contains("56-05-2930") || list.get(i)[0].contains("56-05-2933") || list.get(i)[0].contains("56-05-2945"))
                            {
                                baLayout.setVisibility(View.VISIBLE);
                                refused.setVisibility(View.VISIBLE);
                                blow0.setVisibility(View.VISIBLE);
                            }
                            descBox.setText(list.get(i)[2]);
                            offBox.setText(list.get(i)[0]);
                            pointsBox.setText(list.get(i)[4]);
                            cdrBox.setText(list.get(i)[1]);
                            offBox.setInputType(InputType.TYPE_NULL);
                            pointsBox.setInputType(InputType.TYPE_NULL);
                            cdrBox.setInputType(InputType.TYPE_NULL);
                            break;
                        }
                    }
                }
            }

        }
    }
}
