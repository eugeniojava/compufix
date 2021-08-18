package com.eugeniojava.compufix;

import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.eugeniojava.compufix.dao.ComputerDatabase;
import com.eugeniojava.compufix.model.Computer;
import com.eugeniojava.compufix.model.Type;
import com.eugeniojava.compufix.util.AlertDialogUtil;
import com.eugeniojava.compufix.util.DateUtil;

import java.util.Calendar;
import java.util.List;

public class ComputerFormActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    public static final String ACTION = "ACTION";
    public static final String ID = "ID";
    public static final int CREATE = 1;
    public static final int UPDATE = 2;
    private int action;
    private EditText editTextOwner;
    private EditText editTextModel;
    private EditText editTextManufacturer;
    private EditText editTextDescription;
    private Spinner spinnerType;
    private List<Type> types;
    private RadioGroup radioGroupCustomerType;
    private CheckBox checkBoxUrgent;
    private EditText editTextCompletionForecast;
    private Calendar calendarCompletionForecast;
    private Computer computer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_computer_form);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        editTextOwner = findViewById(R.id.editTextOwner);
        editTextModel = findViewById(R.id.editTextModel);
        editTextManufacturer = findViewById(R.id.editTextManufacturer);
        editTextDescription = findViewById(R.id.editTextDescription);
        spinnerType = findViewById(R.id.spinnerType);
        loadTypes();
        radioGroupCustomerType = findViewById(R.id.radioGroupCustomer);
        checkBoxUrgent = findViewById(R.id.checkBoxUrgent);
        calendarCompletionForecast = Calendar.getInstance();
        editTextCompletionForecast = findViewById(R.id.editTextCompletionForecast);
        editTextCompletionForecast.setFocusable(false);
        editTextCompletionForecast.setOnClickListener(view -> new DatePickerDialog(
                this,
                R.style.CustomDatePickerDialogTheme,
                this,
                calendarCompletionForecast.get(Calendar.YEAR),
                calendarCompletionForecast.get(Calendar.MONTH),
                calendarCompletionForecast.get(Calendar.DAY_OF_MONTH))
                .show()
        );

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            action = bundle.getInt(ACTION, CREATE);

            if (action == CREATE) {
                setTitle(getString(R.string.computer_form_activity_title_create_computer));

                computer = new Computer("", "", "", "", "");
                editTextOwner.requestFocus();
            } else {
                setTitle(getString(R.string.computer_form_activity_title_update_computer));

                AsyncTask.execute(() -> {
                    int id = bundle.getInt(ID);
                    ComputerDatabase computerDatabase = ComputerDatabase.getInstance(this);
                    computer = computerDatabase.computerDao().findById(id);

                    runOnUiThread(() -> {
                        editTextOwner.setText(computer.getOwner());
                        editTextModel.setText(computer.getModel());
                        editTextManufacturer.setText(computer.getManufacturer());
                        editTextDescription.setText(computer.getDescription());
                        spinnerType.setSelection(typePosition(computer.getTypeId()));
                        radioGroupCustomerType.check(getCustomerTypeId(computer.getCustomerType()));
                        checkBoxUrgent.setChecked(computer.isUrgent());
                        calendarCompletionForecast.setTime(computer.getCompletionForecast());
                        editTextCompletionForecast.setText(DateUtil.formatDate(this,
                                computer.getCompletionForecast()));
                        editTextOwner.requestFocus();
                        editTextOwner.setSelection(editTextOwner.getText().length());
                    });
                });
            }
        }
    }

    @Override
    public void onBackPressed() {
        cancel();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_item_options_menu, menu);

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

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        calendarCompletionForecast.set(year, month, dayOfMonth);
        editTextCompletionForecast.setText(DateUtil.formatDate(this, calendarCompletionForecast.getTime()));
    }

    private void save() {
        String owner = AlertDialogUtil.validateEditText(this, editTextOwner,
                R.string.computer_form_activity_message_error_owner);
        if (owner == null) {
            return;
        }

        String model = AlertDialogUtil.validateEditText(this, editTextModel,
                R.string.computer_form_activity_message_error_model);
        if (model == null) {
            return;
        }

        String manufacturer = AlertDialogUtil.validateEditText(this, editTextManufacturer,
                R.string.computer_form_activity_message_error_manufacturer);
        if (manufacturer == null) {
            return;
        }

        String description = AlertDialogUtil.validateEditText(this, editTextDescription,
                R.string.computer_form_activity_message_error_description);
        if (description == null) {
            return;
        }

        Type type = (Type) spinnerType.getSelectedItem();
        if (type == null) {
            return;
        }

        if (!AlertDialogUtil.validateRadioGroup(this, radioGroupCustomerType,
                R.string.computer_form_activity_message_error_customer)) {
            return;
        }
        String customerType = getCustomer();

        boolean urgent = checkBoxUrgent.isChecked();

        String completionForecastDate = AlertDialogUtil.validateEditText(this, editTextCompletionForecast,
                R.string.computer_form_activity_message_error_completion_forecast);
        if (completionForecastDate == null) {
            return;
        }

        computer.setOwner(owner);
        computer.setModel(model);
        computer.setManufacturer(manufacturer);
        computer.setDescription(description);
        computer.setTypeId(type.getId());
        computer.setCustomerType(customerType);
        computer.setUrgent(urgent);
        computer.setCompletionForecast(calendarCompletionForecast.getTime());

        AsyncTask.execute(() -> {
            ComputerDatabase computerDatabase = ComputerDatabase.getInstance(this);

            if (action == CREATE) {
                computerDatabase.computerDao().create(computer);
            } else {
                computerDatabase.computerDao().update(computer);
            }

            setResult(RESULT_OK);
            finish();
        });
    }

    private void clear() {
        editTextOwner.setText(null);
        editTextModel.setText(null);
        editTextManufacturer.setText(null);
        editTextDescription.setText(null);
        spinnerType.setSelection(0);
        radioGroupCustomerType.clearCheck();
        checkBoxUrgent.setChecked(false);
        editTextCompletionForecast.setText(null);
        editTextOwner.requestFocus();

        Toast.makeText(this, R.string.computer_form_activity_message_form_cleared, Toast.LENGTH_SHORT).show();
    }

    private void cancel() {
        setResult(RESULT_CANCELED);
        finish();
    }

    private void loadTypes() {
        AsyncTask.execute(() -> {
            ComputerDatabase computerDatabase = ComputerDatabase.getInstance(this);
            types = computerDatabase.typeDao().findAll();

            runOnUiThread(() -> {
                ArrayAdapter<Type> arrayAdapterType =
                        new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, types);
                spinnerType.setAdapter(arrayAdapterType);
            });
        });
    }

    private int typePosition(int typeId) {
        for (int i = 0; i < types.size(); i++) {
            if (types.get(i).getId() == typeId) {
                return i;
            }
        }
        return -1;
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
}
