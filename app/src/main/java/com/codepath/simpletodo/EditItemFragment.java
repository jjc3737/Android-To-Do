package com.codepath.simpletodo;

import android.support.v4.app.DialogFragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EditItemFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EditItemFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class EditItemFragment extends DialogFragment implements TextView.OnEditorActionListener{

    private EditText mEditItemText;

    public EditItemFragment() {
        // Required empty public constructor
    }

    public interface EditItemFragmentListener {
        void onFinishEditItem(String inputText);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditItemFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditItemFragment newInstance(String title, String textToEdit) {
        EditItemFragment fragment = new EditItemFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("textToEdit", textToEdit);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_edit_item, container);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mEditItemText = (EditText) view.findViewById(R.id.etEditFragmentTextfield);
        mEditItemText.setOnEditorActionListener(this);
        String title = getArguments().getString("title", "Edit Item");
        getDialog().setTitle(title);
        String textToEdit = getArguments().getString("textToEdit");
        mEditItemText.setText(textToEdit);
        mEditItemText.requestFocus();
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof TextView.OnEditorActionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (EditorInfo.IME_ACTION_DONE == actionId) {
            // Return input text to activity
            EditItemFragmentListener listener = (EditItemFragmentListener) getActivity();
            listener.onFinishEditItem(mEditItemText.getText().toString());
            dismiss();
            return true;
        }

        return false;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */

}
