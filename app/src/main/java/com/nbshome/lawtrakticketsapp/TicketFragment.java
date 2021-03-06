package com.nbshome.lawtrakticketsapp;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.nbshome.lawtrakticketsapp.enums.Country;
import com.nbshome.lawtrakticketsapp.enums.Ethnicity;
import com.nbshome.lawtrakticketsapp.objects.Person;
import com.nbshome.lawtrakticketsapp.objects.Ticket;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;
import fr.ganfra.materialspinner.MaterialSpinner;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TicketFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TicketFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TicketFragment extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private OnFragmentInteractionListener mListener;

    ArrayAdapter<String> testAdapter;

    public TicketFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TicketFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TicketFragment newInstance(String param1, String param2) {
        TicketFragment fragment = new TicketFragment();
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

    private int getIndex(Spinner spinner, String myString)
    {
        int index = 0;

        for (int i=0;i<spinner.getAdapter().getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                index = i;
                break;
            }
        }
        return index;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        List<Ethnicity> ethnicities =
                new ArrayList<Ethnicity>(EnumSet.allOf(Ethnicity.class));
        ArrayAdapter<Ethnicity> ethnicityAdapter;
        ethnicityAdapter = new ArrayAdapter<Ethnicity>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, ethnicities);
        ethnicityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        List<Country> countries =
                new ArrayList<Country>(EnumSet.allOf(Country.class));
        ArrayAdapter<Country> countryAdapter;
        countryAdapter = new ArrayAdapter<Country>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, countries);
        countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        
		View v         = inflater.inflate(R.layout.fragment_ticket, container, false);
		Button submit  = (Button) v.findViewById(R.id.submit);
        submit.setOnClickListener(this);
		cdl            = (RadioGroup)      v.findViewById(R.id.cdlGroup);
		dlClass        = (MaterialSpinner) v.findViewById(R.id.dlClass);
		dlNum          = (EditText)        v.findViewById(R.id.dlNum);
		fName          = (EditText)        v.findViewById(R.id.firstName);
		mName          = (EditText)        v.findViewById(R.id.middleName);
		lName          = (EditText)        v.findViewById(R.id.lastName);
		weight         = (EditText)        v.findViewById(R.id.weight);
		addr           = (EditText)        v.findViewById(R.id.addr);
		city           = (EditText)        v.findViewById(R.id.city);
		state          = (MaterialSpinner) v.findViewById(R.id.state);
		hair           = (MaterialSpinner) v.findViewById(R.id.hair);
		eyes           = (MaterialSpinner) v.findViewById(R.id.eyes);
		zip            = (EditText)        v.findViewById(R.id.zip);
		country        = (MaterialSpinner) v.findViewById(R.id.country);
		phone          = (EditText)        v.findViewById(R.id.phone);
		ssn            = (EditText)        v.findViewById(R.id.ssn);
		dob            = (EditText)        v.findViewById(R.id.dob);
		race           = (MaterialSpinner) v.findViewById(R.id.race);
		sex            = (MaterialSpinner) v.findViewById(R.id.sex);
		ethnicity      = (MaterialSpinner) v.findViewById(R.id.ethnicity);
		residence      = (MaterialSpinner) v.findViewById(R.id.residence);
		feet           = (EditText)        v.findViewById(R.id.heightFeet);
		inches         = (EditText)        v.findViewById(R.id.heightIn);
		dlStateSpinner = (MaterialSpinner) v.findViewById(R.id.dlStateSpinner);
        RetrieveCsvTask task = new RetrieveCsvTask();
        task.setSpinner(dlClass);
        task.execute("ftp://sitebackups:backmeup~01@nbshome.com/etickets/" + MainActivity.ori + "/dr_lic_cla.csv");
        RetrieveCsvTask task2 = new RetrieveCsvTask();
        task2.setSpinner(state);
        task2.execute("ftp://sitebackups:backmeup~01@nbshome.com/etickets/" + MainActivity.ori + "/state.csv");
        RetrieveCsvTask task7 = new RetrieveCsvTask();
        task7.setSpinner(hair);
        task7.execute("ftp://sitebackups:backmeup~01@nbshome.com/etickets/" + MainActivity.ori + "/hair.csv");
        RetrieveCsvTask task8 = new RetrieveCsvTask();
        task8.setSpinner(eyes);
        task8.execute("ftp://sitebackups:backmeup~01@nbshome.com/etickets/" + MainActivity.ori + "/eyes.csv");
        country.setAdapter(countryAdapter);
        RetrieveCsvTask task3 = new RetrieveCsvTask();
        task3.setSpinner(race);
        task3.execute("ftp://sitebackups:backmeup~01@nbshome.com/etickets/" + MainActivity.ori + "/race.csv");
        RetrieveCsvTask task4 = new RetrieveCsvTask();
        task4.setSpinner(sex);
        task4.execute("ftp://sitebackups:backmeup~01@nbshome.com/etickets/" + MainActivity.ori + "/sex.csv");
        ethnicity.setAdapter(ethnicityAdapter);
        RetrieveCsvTask task6 = new RetrieveCsvTask();
        task6.setSpinner(residence);
        task6.execute("ftp://sitebackups:backmeup~01@nbshome.com/etickets/" + MainActivity.ori + "/res.csv");
        RetrieveCsvTask task5 = new RetrieveCsvTask();
        task5.setSpinner(dlStateSpinner);
        task5.execute("ftp://sitebackups:backmeup~01@nbshome.com/etickets/" + MainActivity.ori + "/state.csv");


        if (!MainActivity.firstName.equals("")) {

            dlNum.setText(MainActivity.dlNum);
            country.setSelection(countryAdapter.getPosition(Country.US)+1);
            race.setSelection(0);
            ethnicity.setSelection(0);
            residence.setSelection(0);
            fName.setText(MainActivity.firstName);
            mName.setText(MainActivity.middleName);
            lName.setText(MainActivity.lastName);
            weight.setText(MainActivity.weight);
            addr.setText(MainActivity.addr);
            city.setText(MainActivity.city);
            zip.setText(MainActivity.zip);
            dob.setText(MainActivity.dob);
            feet.setText(MainActivity.height.substring(0, MainActivity.height.lastIndexOf('-')));
            inches.setText(MainActivity.height.substring(MainActivity.height.lastIndexOf('-') + 1));
        }

        return v;
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

    private EditText fName, mName, lName, addr, city, zip, phone, ssn, dob,
            feet, inches, weight, dlNum;
    private MaterialSpinner state;
    private Spinner dlStateSpinner, race, sex, ethnicity, residence, country, hair, eyes, dlClass;
    private RadioGroup cdl;

    @Override
    public void onClick(View v)
    {
        if(v.getId() == R.id.submit) {
			Person violator = new Person();
			boolean flag    = false;

            try {
                if (!fName.getText().toString().equals("")) {
                    violator.setfName(fName.getText().toString());
                } else
                {
					Context context  = getContext();
					CharSequence err = "A First Name must be selected";
					int duration     = Toast.LENGTH_SHORT;
					
					Toast toast      = Toast.makeText(context, err, duration);
                    toast.show();
                    flag = true;
                }

                violator.setmName(mName.getText().toString());

                if (!lName.getText().toString().equals("")) {
                    violator.setlName(lName.getText().toString());
                } else {
					Context context  = getContext();
					CharSequence err = "A Last Name must be selected";
					int duration     = Toast.LENGTH_SHORT;
					
					Toast toast      = Toast.makeText(context, err, duration);
                    toast.show();
                    flag = true;
                }

                if (!addr.getText().toString().equals("")) {
                    violator.setAddr(addr.getText().toString());
                } else {
					Context context  = getContext();
					CharSequence err = "An Address must be selected";
					int duration     = Toast.LENGTH_SHORT;
					
					Toast toast      = Toast.makeText(context, err, duration);
                    toast.show();
                    flag = true;
                }

                if (country.getSelectedItem() == Country.US) {
                    if (!city.getText().toString().equals("")) {
                        violator.setCity(city.getText().toString());
                    } else {
						Context context  = getContext();
						CharSequence err = "A City must be selected";
						int duration     = Toast.LENGTH_SHORT;
						
						Toast toast      = Toast.makeText(context, err, duration);
                        toast.show();
                        flag = true;
                    }

                    try {
                        violator.setState(state.getSelectedItem().toString());
                    } catch(Exception ex)
                    {
						Context context  = getContext();
						CharSequence err = "A State must be selected";
						int duration     = Toast.LENGTH_SHORT;
						
						Toast toast      = Toast.makeText(context, err, duration);
                        toast.show();
                        flag = true;
                    }

                    violator.setZip(zip.getText().toString());
                }

                violator.setPhone(phone.getText().toString());
                violator.setSsn(ssn.getText().toString());
                violator.setHeight(feet.getText() + "-" + inches.getText());

                if (!dob.getText().toString().equals("")) {
                    violator.setDob(dob.getText().toString());
                } else {
					Context context  = getContext();
					CharSequence err = "A Birth Date must be selected";
					int duration     = Toast.LENGTH_SHORT;
					
					Toast toast      = Toast.makeText(context, err, duration);
                    toast.show();
                    flag = true;
                }
                if (!dlNum.getText().toString().equals("")) {
                    violator.setDlNum(dlNum.getText().toString());
                } else {
					Context context  = getContext();
					CharSequence err = "A Drivers License Number must be selected";
					int duration     = Toast.LENGTH_SHORT;
					
					Toast toast      = Toast.makeText(context, err, duration);
                    toast.show();
                    flag = true;
                }
                try {
                    if (Integer.parseInt(weight.getText().toString())<998) {
                        violator.setWeight(weight.getText().toString());
                    } else {
						Context context  = getContext();
						CharSequence err = "Check Weight";
						int duration     = Toast.LENGTH_SHORT;
						
						Toast toast      = Toast.makeText(context, err, duration);
                        toast.show();
                        flag = true;
                    }
                } catch (Exception e) {

                }
                if(!dlStateSpinner.getSelectedItem().toString().equals("Drivers License State")) {
                    violator.setDlState(dlStateSpinner.getSelectedItem().toString());
                } else
                {
					Context context  = getContext();
					CharSequence err = "A Drivers License State must be selected";
					int duration     = Toast.LENGTH_SHORT;
					
					Toast toast      = Toast.makeText(context, err, duration);
                    toast.show();
                    flag = true;
                }
                if(!dlClass.getSelectedItem().toString().equals("Drivers License Number")) {
                    violator.setDlClass(dlClass.getSelectedItem().toString());
                } else {
					Context context  = getContext();
					CharSequence err = "A Drivers License Class must be selected";
					int duration     = Toast.LENGTH_SHORT;
					
					Toast toast      = Toast.makeText(context, err, duration);
                    toast.show();
                    flag = true;
                }
                if(!race.getSelectedItem().toString().equals("Race")) {
                    violator.setRace(race.getSelectedItem().toString());
                } else {
                    Context context = getContext();
                    CharSequence err = "A Race must be selected";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, err, duration);
                    toast.show();
                    flag = true;
                }
                if(!sex.getSelectedItem().toString().equals("Sex")) {
                    violator.setSex(sex.getSelectedItem().toString());
                    violator.setDob(MainActivity.dob);
                } else {
					Context context  = getContext();
					CharSequence err = "A Sex must be selected";
					int duration     = Toast.LENGTH_SHORT;
					
					Toast toast      = Toast.makeText(context, err, duration);
                    toast.show();
                    flag = true;
                }
                try {
                    violator.setEthnicity((Ethnicity) ethnicity.getSelectedItem());
                } catch (Exception e) {
                    violator.setEthnicity(Ethnicity.U);
                }
                if(!residence.getSelectedItem().toString().equals("Residence")) {
                    violator.setResidence(residence.getSelectedItem().toString());
                } else {
                    violator.setResidence("Unknown");
                }

                try {
                    violator.setCountry((Country) country.getSelectedItem());
                } catch (Exception e) {
					Context context  = getContext();
					CharSequence err = "A Country must be selected";
					int duration     = Toast.LENGTH_SHORT;
					
					Toast toast      = Toast.makeText(context, err, duration);
                    toast.show();
                    flag = true;
                }

                if(!hair.getSelectedItem().toString().equals("Hair Color")) {
                    violator.setHair(hair.getSelectedItem().toString());
                } else {
                    violator.setHair("XXX");
                }
                if(!eyes.getSelectedItem().toString().equals("Eye Color")) {
                    violator.setEyes(eyes.getSelectedItem().toString());
                } else {
                    violator.setEyes("XXX");
                }
                violator.setCdl(cdl.getCheckedRadioButtonId() == R.id.cdlYes);
            } catch (NumberFormatException e) {
				Context context  = getContext();
				CharSequence err = "Something Went Wrong";
				int duration     = Toast.LENGTH_SHORT;
				
				Toast toast      = Toast.makeText(context, err, duration);
                toast.show();
                flag = true;
            }
            if(!flag) {
                violator.getTickets().add(new Ticket());
                MainActivity.violators.add(violator);
                FragmentManager fragmentManager = getFragmentManager();
                Fragment frag = null;
                Class fragmentClass = Registration.class;

                try {
                    frag = (Fragment) fragmentClass.newInstance();
                    MainActivity.frags.add(frag);
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

                getActivity().setTitle("Registration");

            }
        }

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


    class RetrieveCsvTask extends AsyncTask<String, Void, List<String[]>> {
        private Exception exception;
        private Spinner spin;

        public void setSpinner(Spinner spin)
        {
            this.spin = spin;
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
            ArrayList stuff = new ArrayList();
            for(int i = 1; i<list.size(); ++i)
            {
                //if(spin.getId() != R.id.race && spin.getId() != R.id.residence)
                    stuff.add(list.get(i)[0]);
                //else
                   // stuff.add(list.get(i)[1]);
            }

            testAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item, stuff);

            spin.setAdapter(testAdapter);
            try {
                if (spin.getId() == R.id.dlClass) {
                    dlClass.setSelection(getIndex(dlClass, MainActivity.dlClass) + 1);
                } else if (spin.getId() == R.id.state) {
                    state.setSelection(getIndex(state, MainActivity.state) + 1);
                } else if (spin.getId() == R.id.dlStateSpinner) {
                    dlStateSpinner.setSelection(getIndex(dlStateSpinner, MainActivity.state) + 1);
                } else if (spin.getId() == R.id.sex) {
                    sex.setSelection(getIndex(sex, MainActivity.sex) + 1);
                }
            } catch(Exception ex) {



            }


        }
    }

}
