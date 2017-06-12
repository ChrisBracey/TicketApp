package com.nbshome.lawtrakticketsapp;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.nbshome.lawtrakticketsapp.enums.State;
import com.nbshome.lawtrakticketsapp.enums.VehicleType;
import com.nbshome.lawtrakticketsapp.objects.Person;
import com.nbshome.lawtrakticketsapp.objects.Vehicle;

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
 * {@link Registration.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Registration#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Registration extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Registration() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Registration.
     */
    // TODO: Rename and change types and number of parameters
    public static Registration newInstance(String param1, String param2) {
        Registration fragment = new Registration();
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

    public EditText ownerFullName, ownerAddr, ownerCity, ownerZip, vehicleYear, plateNum;
    public Spinner ownerState, plateState, vehType,  vehicleMake;
    public List vehList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_registration, container, false);

        Button btn = (Button) v.findViewById(R.id.submitReg);
        btn.setOnClickListener(this);


        List<State> states =
                new ArrayList<State>(EnumSet.allOf(State.class));
        ArrayAdapter<State> adapter;
        adapter = new ArrayAdapter<State>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, states);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        List<VehicleType> types =
                new ArrayList<VehicleType>(EnumSet.allOf(VehicleType.class));
        ArrayAdapter<VehicleType> typeAdapter;
        typeAdapter = new ArrayAdapter<VehicleType>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, types);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        vehicleMake = (MaterialSpinner) v.findViewById(R.id.vehicleMake);

        RetrieveCsvTask task = new RetrieveCsvTask();
        task.setSpinner(vehicleMake);
        task.execute("http://www.nbshome.com/updates/ticketvalidations/validation_vehmake.csv");



        ownerFullName = (EditText) v.findViewById(R.id.ownerFullName);
        ownerAddr = (EditText) v.findViewById(R.id.ownerAddr);
        ownerCity = (EditText) v.findViewById(R.id.ownerCity);
        ownerState = (MaterialSpinner) v.findViewById(R.id.ownerState);
        ownerZip = (EditText) v.findViewById(R.id.ownerZip);
        vehicleMake = (MaterialSpinner) v.findViewById(R.id.vehicleMake);
        vehicleYear = (EditText) v.findViewById(R.id.vehicleYear);
        plateNum = (EditText) v.findViewById(R.id.plateNum);
        plateState = (MaterialSpinner) v.findViewById(R.id.plateState);
        vehType = (MaterialSpinner) v.findViewById(R.id.vehType);

        vehType.setAdapter(typeAdapter);
        ownerState.setAdapter(adapter);
        plateState.setAdapter(adapter);

        if (!MainActivity.regFullName.equals("")) {

            ownerFullName.setText(MainActivity.regFullName);
            ownerAddr.setText(MainActivity.regAddr);
            ownerCity.setText(MainActivity.regCity);
            ownerState.setSelection(adapter.getPosition(State.valueOf(MainActivity.regState)) + 1);
            ownerZip.setText(MainActivity.regZip);


            vehicleYear.setText(MainActivity.regYear);
            plateNum.setText(MainActivity.regPlateNum);
            plateState.setSelection(adapter.getPosition(State.valueOf(MainActivity.regState)) + 1);



        }


        return v;
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

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.submitReg)
        {
            boolean flag = false;
            if(ownerFullName.getText().toString().equals(""))
            {

                Context context = getContext();
                CharSequence err = "Name must be selected";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, err, duration);
                toast.show();
                flag = true;
            }

            if(ownerAddr.getText().toString().equals(""))
            {
                Context context = getContext();
                CharSequence err = "Address must be selected";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, err, duration);
                toast.show();
                flag = true;
            }

            if(ownerCity.getText().toString().equals(""))
            {
                Context context = getContext();
                CharSequence err = "City must be selected";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, err, duration);
                toast.show();
                flag = true;
            }

            if(ownerZip.getText().toString().equals(""))
            {
                Context context = getContext();
                CharSequence err = "ZIP must be selected";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, err, duration);
                toast.show();
                flag = true;
            }

            try {
                State temp = (State) ownerState.getSelectedItem();
            } catch(Exception ex)
            {
                Context context = getContext();
                CharSequence err = "Owner State must be selected";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, err, duration);
                toast.show();
                flag = true;
            }

            if(vehicleMake.getSelectedItemPosition() == 0)
            {
                Context context = getContext();
                CharSequence err = "Make must be selected";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, err, duration);
                toast.show();
                flag = true;
            }

            if(vehicleYear.getText().toString().equals(""))
            {
                Context context = getContext();
                CharSequence err = "Vehicle Year must be selected";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, err, duration);
                toast.show();
                flag = true;
            }

            if(plateNum.getText().toString().equals(""))
            {
                Context context = getContext();
                CharSequence err = "License Plate Number must be selected";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, err, duration);
                toast.show();
                flag = true;
            }

            try {
                State temp = (State) plateState.getSelectedItem();
            } catch(Exception ex)
            {
                Context context = getContext();
                CharSequence err = "License Plate State must be selected";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, err, duration);
                toast.show();
                flag = true;
            }

            try {
                VehicleType temp = (VehicleType) vehType.getSelectedItem();
            } catch(Exception ex)
            {
                Context context = getContext();
                CharSequence err = "Vehicle Type must be selected";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, err, duration);
                toast.show();
                flag = true;
            }

            if(!flag) {
                Person owner = new Person(ownerFullName.getText().toString(),
                        ownerAddr.getText().toString(), ownerCity.getText().toString(),
                        ownerZip.getText().toString(), (State) ownerState.getSelectedItem());

                owner.setVehicle(new Vehicle(vehicleMake.getSelectedItem().toString(),
                        vehicleYear.getText().toString(), plateNum.getText().toString(),
                        (State) plateState.getSelectedItem(), (VehicleType) vehType.getSelectedItem()));

                MainActivity.violators.get(MainActivity.violators.size() - 1).getTickets().get
                        (MainActivity.violators.get(MainActivity.violators.size() - 1).getTickets().size() - 1).setOwner(owner);

                FragmentManager fragmentManager = getFragmentManager();
                Fragment frag = null;
                Class fragmentClass = CourtFragment.class;

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

                getActivity().setTitle("Trial Court Information");
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

    ArrayAdapter<String> testAdapter;

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
            vehList = new ArrayList();
            for(int i = 1; i<list.size(); ++i)
            {
                stuff.add(list.get(i)[0]);
                vehList.add(list.get(i)[0]);
            }

            testAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item, stuff);

            spin.setAdapter(testAdapter);

            if(!MainActivity.regMake.equals(""))
                spin.setSelection(getIndex(vehicleMake, MainActivity.regMake) + 1);

        }
    }

}
