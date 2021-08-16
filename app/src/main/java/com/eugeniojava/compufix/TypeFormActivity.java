package com.eugeniojava.compufix;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.eugeniojava.compufix.dao.ComputerDatabase;
import com.eugeniojava.compufix.model.Type;
import com.eugeniojava.compufix.util.AlertDialogUtil;

public class TypeFormActivity extends AppCompatActivity {

    public static final String ACTION = "ACTION";
    public static final String ID = "ID";
    public static final int CREATE = 1;
    public static final int UPDATE = 2;
    private EditText editTextDescription;
    private int mode;
    private Type type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_form);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        editTextDescription = findViewById(R.id.editTextDescription);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mode = bundle.getInt(ACTION, CREATE);
        } else {
            mode = CREATE;
        }
        if (mode == CREATE) {
            setTitle(R.string.type_form_activity_title_create);

            type = new Type("");
        } else {
            setTitle(R.string.type_form_activity_title_update);

            AsyncTask.execute(() -> {
                int id = bundle.getInt(ID);
                ComputerDatabase computerDatabase = ComputerDatabase.getInstance(this);
                type = computerDatabase.typeDao().findById(id);

                runOnUiThread(() -> editTextDescription.setText(type.getDescription()));
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_item_options_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.menuItemSave) {
            save();

            return true;
        } else if (itemId == R.id.menuItemClear) {
            clear();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void save() {
        String description = AlertDialogUtil.validateEditText(this, editTextDescription,
                R.string.type_form_activity_message_description_empty);
        if (description != null) {
            AsyncTask.execute(() -> {
                ComputerDatabase computerDatabase = ComputerDatabase.getInstance(this);
                int countByDescription = computerDatabase.typeDao().countBy(description);

                if (mode == CREATE) {
                    if (countByDescription > 0) {
                        runOnUiThread(() -> AlertDialogUtil.showErrorAlertDialog(this,
                                R.string.type_form_activity_message_description_already_registered));

                        return;
                    }
                    type.setDescription(description);
                    computerDatabase.typeDao().create(type);
                } else {
                    if (!description.equals(type.getDescription())) {
                        if (countByDescription > 0) {
                            runOnUiThread(() -> AlertDialogUtil.showErrorAlertDialog(this,
                                    R.string.type_form_activity_message_description_already_registered));

                            return;
                        }
                        type.setDescription(description);
                        computerDatabase.typeDao().update(type);
                    }
                }
                setResult(RESULT_OK);
                finish();
            });
        }
    }

    private void clear() {
        editTextDescription.setText(null);
        editTextDescription.requestFocus();
        Toast.makeText(this, getString(R.string.type_form_activity_message_form_cleared), Toast.LENGTH_SHORT).show();
    }
}
