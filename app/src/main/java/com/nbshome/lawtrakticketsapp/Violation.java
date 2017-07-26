package com.nbshome.lawtrakticketsapp;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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
public class Violation extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

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
    Spinner tc;
    Button submit, skip;
    TextInputLayout descLayout, offLayout, pointsLayout;
    EditText descBox, offBox, pointsBox;


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

        skip.setOnClickListener(this);
        submit.setOnClickListener(this);
        tc        = (MaterialSpinner) v.findViewById(R.id.trafficCode);
        RetrieveCsvTask task = new RetrieveCsvTask();
        task.setSpinner(tc);
        task.execute("ftp://sitebackups:backmeup~01@nbshome.com/etickets/" + MainActivity.ori + "/traffic.csv");

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

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.submitViolation) {
            RetrieveCsvTask task = new RetrieveCsvTask();
            Log.d("STUFF", tc.getSelectedItem().toString().substring(0,2));
            task.setFlag(true, tc.getSelectedItem().toString().substring(0,2));
            task.execute("ftp://sitebackups:backmeup~01@nbshome.com/etickets/" + MainActivity.ori + "/traffic.csv");
            skip.setVisibility(View.INVISIBLE);
            descLayout.setVisibility(View.VISIBLE);
            offLayout.setVisibility(View.VISIBLE);
            pointsLayout.setVisibility(View.VISIBLE);

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
    ArrayList codes;
    String desc;

    class RetrieveCsvTask extends AsyncTask<String, Void, List<String[]>> {
        private Exception exception;
        private Spinner spin;
        private boolean flag = false;
        private String code;
        public void setSpinner(Spinner spin)
        {
            this.spin = spin;
        }
        public void setFlag(boolean flag, String code) {
            this.flag = flag;
            this.code = code;
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
            if(!flag) {
                codes = new ArrayList();
                for (int i = 1; i < list.size(); ++i) {
                    String temp = list.get(i)[0] + " " + list.get(i)[1];
                    Log.d("Stuff", temp);
                    codes.add(temp);
                }
                spin.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, codes));
            } else {
                for(int i =1; i<list.size(); ++i) {
                    if(list.get(i)[0].equals(code)) {
                        descBox.setText(list.get(i)[1]);
                        offBox.setText(list.get(i)[2]);
                        pointsBox.setText(list.get(i)[5]);
                        offBox.setInputType(InputType.TYPE_NULL);
                        pointsBox.setInputType(InputType.TYPE_NULL);
                        break;
                    }
                }
            }

        }
    }
}
