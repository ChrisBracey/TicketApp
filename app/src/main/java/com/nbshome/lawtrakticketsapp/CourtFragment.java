package com.nbshome.lawtrakticketsapp;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CourtFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CourtFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CourtFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    public EditText courtDate, courtTime, courtType, trialOffId, trialOffName, judgeId, judgeName;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public CourtFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CourtFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CourtFragment newInstance(String param1, String param2) {
        CourtFragment fragment = new CourtFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View v         = inflater.inflate(R.layout.fragment_court, container, false);
        courtDate = (EditText) v.findViewById(R.id.courtDate);
        courtTime = (EditText) v.findViewById(R.id.courtTime);
        courtType = (EditText) v.findViewById(R.id.courtType);
        trialOffId = (EditText) v.findViewById(R.id.trialOffId);
        trialOffName = (EditText) v.findViewById(R.id.trialOffName);
        judgeId = (EditText) v.findViewById(R.id.judgeID);
        judgeName = (EditText) v.findViewById(R.id.judgeName);

        RetrieveCsvTask task = new RetrieveCsvTask();
        task.setThings(judgeName, judgeId, courtType);
        task.execute("ftp://sitebackups:backmeup~01@nbshome.com/etickets/"+ MainActivity.ori + "/myagency.csv");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_court, container, false);
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


    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.submitCourt) {

        }

    }


    String cName, jID, jName;

    class RetrieveCsvTask extends AsyncTask<String, Void, List<String[]>> {
        private Exception exception;
        EditText judgeName, judgeId, courtType;

        public void setThings(EditText judgeName, EditText judgeId, EditText courtType) {
            this.judgeName = judgeName;
            this.judgeId = judgeId;
            this.courtType = courtType;
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
            try {
                jName = list.get(1)[list.get(1).length - 4];
                jID = list.get(1)[list.get(1).length - 5];
                cName = list.get(1)[list.get(1).length - 12];
                judgeName = (EditText) getActivity().findViewById(R.id.judgeName);
                judgeId = (EditText) getActivity().findViewById(R.id.judgeID);
                courtType = (EditText) getActivity().findViewById(R.id.courtType);
                

                judgeName.setText(jName);
                judgeId.setText(jID);
                courtType.setText(cName);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
    }


}
