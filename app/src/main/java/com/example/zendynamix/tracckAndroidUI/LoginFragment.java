package com.example.zendynamix.tracckAndroidUI;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class LoginFragment extends Fragment {
    private static final String LOG = "Login activity>>>>";
    private final String EXTRA = "extra";
    private final String NUMBER = "number";
    private AutoCompleteTextView editTextCountry;
    private EditText editTextNumber;
    private Button buttonSubmit;
    private int phoneNumLength;
    private CallBacks callbacks;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    ItemData itemData = new ItemData();

    @Override

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        callbacks = (CallBacks) activity;
    }

    public interface CallBacks {
        void onSubmitButton(String num);
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_page, container, false);
        editTextCountry = (AutoCompleteTextView) view.findViewById(R.id.autocomplete_country);
        editTextNumber = (EditText) view.findViewById(R.id.enter_phone);
        editTextNumber.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                phoneNumLength = editable.length();
            }
        });
        buttonSubmit = (Button) view.findViewById(R.id.submit);
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number = editTextNumber.getText().toString();
                if (phoneNumLength < 9 || phoneNumLength > 9) {
                    Toast toast = Toast.makeText(getActivity(), "Check Number", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();
                    ;
                } else {
                    callbacks.onSubmitButton(number);
                }
                // getActivity().finish();
            }
        });
        String[] countries = getResources().getStringArray(R.array.countries_array);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, countries);
        editTextCountry.setAdapter(arrayAdapter);
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callbacks = null;
    }
}
