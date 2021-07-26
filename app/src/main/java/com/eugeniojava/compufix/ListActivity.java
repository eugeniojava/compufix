package com.eugeniojava.compufix;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;

import java.util.ArrayList;

import static android.widget.AbsListView.CHOICE_MODE_SINGLE;
import static com.eugeniojava.compufix.RegisterActivity.ACTION;
import static com.eugeniojava.compufix.RegisterActivity.CREATE;
import static com.eugeniojava.compufix.RegisterActivity.CUSTOMER_TYPE;
import static com.eugeniojava.compufix.RegisterActivity.DESCRIPTION;
import static com.eugeniojava.compufix.RegisterActivity.MANUFACTURER;
import static com.eugeniojava.compufix.RegisterActivity.MODEL;
import static com.eugeniojava.compufix.RegisterActivity.OWNER;
import static com.eugeniojava.compufix.RegisterActivity.TYPE;
import static com.eugeniojava.compufix.RegisterActivity.UPDATE;
import static com.eugeniojava.compufix.RegisterActivity.URGENT;

public class ListActivity extends AppCompatActivity {

    private ListView listViewComputer;
    private ArrayList<Computer> computers;
    private ComputerAdapter computerAdapter;
    private ActionMode actionMode;
    private int selectedPosition = -1;
    private View selectedView;
    private final ActionMode.Callback actionModeCallback = new ActionMode.Callback() {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.list_selected_item, menu);

            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menuItemUpdate:
                    callRegisterActivityToUpdate();
                    mode.finish();
                    return true;
                case R.id.menuItemDelete:
                    delete();
                    mode.finish();
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            if (selectedView != null) {
                selectedView.setBackgroundColor(Color.TRANSPARENT);
            }
            actionMode = null;
            selectedView = null;
            listViewComputer.setEnabled(true);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        listViewComputer = findViewById(R.id.listViewComputer);
        listViewComputer.setOnItemClickListener((parent, view, position, id) -> {
            selectedPosition = position;
            callRegisterActivityToUpdate();
        });
        listViewComputer.setChoiceMode(CHOICE_MODE_SINGLE);
        listViewComputer.setOnItemLongClickListener((parent, view, position, id) -> {
            if (actionMode != null) {
                return false;
            }
            selectedPosition = position;
            view.setBackgroundColor(Color.LTGRAY);
            selectedView = view;
            listViewComputer.setEnabled(false);
            actionMode = startSupportActionMode(actionModeCallback);

            return true;
        });

        populateList();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            Bundle bundle = data.getExtras();
            String owner = bundle.getString(OWNER);
            String model = bundle.getString(MODEL);
            String manufacturer = bundle.getString(MANUFACTURER);
            String description = bundle.getString(DESCRIPTION);
            String type = bundle.getString(TYPE);
            String customerType = bundle.getString(CUSTOMER_TYPE);
            boolean urgent = bundle.getBoolean(URGENT);

            if (requestCode == UPDATE) {
                Computer computer = computers.get(selectedPosition);
                computer.setOwner(owner);
                computer.setModel(model);
                computer.setManufacturer(manufacturer);
                computer.setDescription(description);
                computer.setType(type);
                computer.setCustomerType(customerType);
                computer.setUrgent(urgent);
                selectedPosition = -1;
            } else {
                computers.add(new Computer(owner, model, manufacturer, description, type, customerType, urgent));
            }
            computerAdapter.notifyDataSetChanged();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.list_options_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuItemAdd:
                callRegisterActivityToCreate();
                return true;
            case R.id.menuItemAbout:
                callAboutActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void populateList() {
        computers = new ArrayList<>();
        computerAdapter = new ComputerAdapter(this, computers);
        listViewComputer.setAdapter(computerAdapter);
    }

    public void callRegisterActivityToCreate() {
        Intent intent = new Intent(this, RegisterActivity.class);

        intent.putExtra(ACTION, CREATE);

        startActivityForResult(intent, CREATE);
    }

    private void callRegisterActivityToUpdate() {
        Computer computer = computers.get(selectedPosition);
        Intent intent = new Intent(this, RegisterActivity.class);

        intent.putExtra(ACTION, UPDATE);
        intent.putExtra(OWNER, computer.getOwner());
        intent.putExtra(MODEL, computer.getModel());
        intent.putExtra(MANUFACTURER, computer.getManufacturer());
        intent.putExtra(DESCRIPTION, computer.getDescription());
        intent.putExtra(TYPE, computer.getType());
        intent.putExtra(CUSTOMER_TYPE, computer.getCustomerType());
        intent.putExtra(URGENT, computer.isUrgent());

        startActivityForResult(intent, UPDATE);
    }

    private void delete() {
        computers.remove(selectedPosition);
        computerAdapter.notifyDataSetChanged();
    }

    public void callAboutActivity() {
        startActivity(new Intent(this, AboutActivity.class));
    }
}
