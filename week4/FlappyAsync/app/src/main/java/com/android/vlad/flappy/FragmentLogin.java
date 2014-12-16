package com.android.vlad.flappy;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class FragmentLogin extends Fragment {
    private EditText mEditUserName;
    private EditText mEditEmail;
    private RadioGroup mGroupUniversity;
    private OnLoginButtonClickListener mOnLoginButtonClickListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mOnLoginButtonClickListener = (OnLoginButtonClickListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnLoginButtonClickListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mEditUserName = (EditText) getActivity().findViewById(R.id.edit_user_name);
        mEditEmail = (EditText) getActivity().findViewById(R.id.edit_email);
        mGroupUniversity = (RadioGroup) getActivity().findViewById(R.id.group_university);

        Button buttonLogin = (Button) getActivity().findViewById(R.id.button_login);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isDataValid()) {
                    Toast.makeText(getActivity(), "Missing input data! Please fill/check all fields!", Toast.LENGTH_LONG).show();
                    return;
                }

                // Used to hide the software keyboard if needed
                if (getActivity().getCurrentFocus() != null) {
                    InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                }

                RadioButton radioButton = (RadioButton) mGroupUniversity.findViewById(mGroupUniversity.getCheckedRadioButtonId());
                Player player = new Player(mEditUserName.getText().toString(), mEditEmail.getText().toString(), radioButton.getText().toString());
                mOnLoginButtonClickListener.onLoginButtonClick(player);
            }
        });
    }

    private boolean isDataValid() {
        return !mEditUserName.getText().toString().isEmpty()
                && !mEditEmail.getText().toString().isEmpty()
                && mGroupUniversity.getCheckedRadioButtonId() != -1;
    }

    public interface OnLoginButtonClickListener {
        public void onLoginButtonClick(Player player);
    }
}
