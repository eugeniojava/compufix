package com.eugeniojava.compufix;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity {

    public static final String ACTION = "ACTION";
    public static final String OWNER = "OWNER";
    public static final String MODEL = "MODEL";
    public static final String MANUFACTURER = "MANUFACTURER";
    public static final String DESCRIPTION = "DESCRIPTION";
    public static final String TYPE = "TYPE";
    public static final String CUSTOMER_TYPE = "CUSTOMER_TYPE";
    public static final String URGENT = "URGENT";
    public static final int CREATE = 1;
    public static final int UPDATE = 2;
    private EditText editTextOwner;
    private EditText editTextModel;
    private EditText editTextManufacturer;
    private EditText editTextDescription;
    private Spinner spinnerType;
    private RadioGroup radioGroupCustomerType;
    private CheckBox checkBoxUrgent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        editTextOwner = findViewById(R.id.editTextOwner);
        editTextModel = findViewById(R.id.editTextModel);
        editTextManufacturer = findViewById(R.id.editTextManufacturer);
        editTextDescription = findViewById(R.id.editTextDescription);
        spinnerType = findViewById(R.id.spinnerType);
        populateSpinner();
        radioGroupCustomerType = findViewById(R.id.radioGroupCustomer);
        checkBoxUrgent = findViewById(R.id.checkBoxUrgent);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            if (bundle.getInt(ACTION, CREATE) == CREATE) {
                setTitle(getString(R.string.register_activity_title_register_computer));
            } else {
                editTextOwner.setText(bundle.getString(OWNER));
                editTextModel.setText(bundle.getString(MODEL));
                editTextManufacturer.setText(bundle.getString(MANUFACTURER));
                editTextDescription.setText(bundle.getString(DESCRIPTION));
                spinnerType.setSelection(getType(bundle.getString(TYPE)));
                radioGroupCustomerType.check(getCustomerTypeId(bundle.getString(CUSTOMER_TYPE)));
                checkBoxUrgent.setChecked(bundle.getBoolean(URGENT));
                setTitle(getString(R.string.register_activity_title_update_computer));
            }
        }
    }

    @Override
    public void onBackPressed() {
        cancel();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.computer_options, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == android.R.id.home) {
            cancel();

            return true;
        } else if (itemId == R.id.menuItemSave) {
            save();

            return true;
        } else if (itemId == R.id.menuItemClear) {
            clear();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void save() {
        String owner = editTextOwner.getText().toString();
        String model = editTextModel.getText().toString();
        String manufacturer = editTextManufacturer.getText().toString();
        String description = editTextDescription.getText().toString();
        String type = (String) spinnerType.getSelectedItem();
        String customerType = getCustomer();
        boolean urgent = checkBoxUrgent.isChecked();

        if (validateField(this, editTextOwner, R.string.register_activity_message_error_owner)
                || validateField(this, editTextModel, R.string.register_activity_message_error_model)
                || validateField(this, editTextManufacturer, R.string.register_activity_message_error_manufacturer)
                || validateField(this, editTextDescription, R.string.register_activity_message_error_description)
                || !validateCustomer()) {
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(OWNER, owner);
        intent.putExtra(MODEL, model);
        intent.putExtra(MANUFACTURER, manufacturer);
        intent.putExtra(DESCRIPTION, description);
        intent.putExtra(TYPE, type);
        intent.putExtra(CUSTOMER_TYPE, customerType);
        intent.putExtra(URGENT, urgent);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void cancel() {
        setResult(Activity.RESULT_CANCELED);
        finish();
    }

    private void clear() {
        editTextOwner.setText(null);
        editTextModel.setText(null);
        editTextManufacturer.setText(null);
        editTextDescription.setText(null);
        spinnerType.setSelection(0);
        radioGroupCustomerType.clearCheck();
        checkBoxUrgent.setChecked(false);
        editTextOwner.requestFocus();
        Toast.makeText(this, R.string.register_activity_message_success_clear, Toast.LENGTH_SHORT).show();
    }

    private void populateSpinner() {
        ArrayList<String> arrayList = new ArrayList<>();

        arrayList.add(getString(R.string.register_activity_type_1));
        arrayList.add(getString(R.string.register_activity_type_2));
        arrayList.add(getString(R.string.register_activity_type_3));

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, arrayList);

        spinnerType.setAdapter(arrayAdapter);
    }

    private int getType(String type) {
        for (int i = 0; i < spinnerType.getAdapter().getCount(); i++) {
            if (spinnerType.getAdapter().getItem(i).equals(type)) {
                return i;
            }
        }
        return 0;
    }

    private String getCustomer() {
        int checkedRadioButtonId = radioGroupCustomerType.getCheckedRadioButtonId();

        if (checkedRadioButtonId == R.id.radioButtonPhysicalPerson) {
            return getString(R.string.register_activity_customer_physical_person);
        } else if (checkedRadioButtonId == R.id.radioButtonLegalPerson) {
            return getString(R.string.register_activity_customer_legal_person);
        } else if (checkedRadioButtonId == R.id.radioButtonNotRegistered) {
            return getString(R.string.register_activity_customer_unregistered);
        }
        return getString(R.string.register_activity_customer_not_specified);
    }

    private int getCustomerTypeId(String customerType) {
        if (customerType.equals(getString(R.string.register_activity_customer_physical_person))) {
            return R.id.radioButtonPhysicalPerson;
        } else if (customerType.equals(getString(R.string.register_activity_customer_legal_person))) {
            return R.id.radioButtonLegalPerson;
        } else if (customerType.equals(getString(R.string.register_activity_customer_unregistered))) {
            return R.id.radioButtonNotRegistered;
        }
        return -1;
    }

    private boolean validateField(Context context, EditText editText, int errorMessage) {
        String fieldContent = editText.getText().toString();

        if (fieldContent.trim().isEmpty()) {
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
            editText.requestFocus();

            return true;
        }
        return false;
    }

    private boolean validateCustomer() {
        if (radioGroupCustomerType.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, R.string.register_activity_message_error_customer, Toast.LENGTH_SHORT).show();

            return false;
        }
        return true;
    }
}
