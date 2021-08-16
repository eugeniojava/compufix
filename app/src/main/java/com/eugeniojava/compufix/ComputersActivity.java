package com.eugeniojava.compufix;

import static android.widget.AbsListView.CHOICE_MODE_SINGLE;
import static com.eugeniojava.compufix.ComputerFormActivity.ACTION;
import static com.eugeniojava.compufix.ComputerFormActivity.CREATE;
import static com.eugeniojava.compufix.ComputerFormActivity.ID;
import static com.eugeniojava.compufix.ComputerFormActivity.UPDATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;

import com.eugeniojava.compufix.dao.ComputerDatabase;
import com.eugeniojava.compufix.model.Computer;
import com.eugeniojava.compufix.model.ComputerAdapter;
import com.eugeniojava.compufix.model.Type;
import com.eugeniojava.compufix.util.AlertDialogUtil;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ComputersActivity extends AppCompatActivity {

    private static final String FILE = "com.eugeniojava.shared.preferences.order-preference";
    private static final String ORDER_BY = "ORDER_BY";
    private static final int ORDER_BY_1 = 1;
    private static final int ORDER_BY_2 = 2;
    private static final int REQUEST_CREATE_COMPUTER = 1;
    private static final int REQUEST_UPDATE_COMPUTER = 2;
    private ListView listViewComputer;
    private ComputerAdapter computerAdapter;
    private List<Computer> computers;
    private int orderByInUse = ORDER_BY_1;
    private final Comparator<Computer> computerComparator = (computer1, computer2) -> {
        switch (orderByInUse) {
            case ORDER_BY_1:
                return computer1.getOwner().compareToIgnoreCase(computer2.getOwner());
            case ORDER_BY_2:
                return 0;
            default:
                return 0;
        }
    };
    private ActionMode actionMode;
    private int selectedPosition = -1;
    private View selectedView;
    private final ActionMode.Callback actionModeCallback = new ActionMode.Callback() {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.list_selected_item_options, menu);

            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        private void delete() {
            computers.remove(selectedPosition);
            computerAdapter.notifyDataSetChanged();
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            int itemId = item.getItemId();

            if (itemId == R.id.menuItemUpdate) {
                Computer computer = (Computer) listViewComputer.getItemAtPosition(selectedPosition);
                callRegisterActivityToUpdate(computer);
                mode.finish();

                return true;
            } else if (itemId == R.id.menuItemDelete) {
                delete();
                mode.finish();

                return true;
            }
            return false;
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

        listViewComputer = findViewById(R.id.listViewItem);
        listViewComputer.setOnItemClickListener((parent, view, position, id) -> {
            Computer computer = (Computer) parent.getItemAtPosition(position);
            callRegisterActivityToUpdate(computer);
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
        readOrderByPreference();
        loadComputers();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if ((requestCode == REQUEST_CREATE_COMPUTER || requestCode == REQUEST_UPDATE_COMPUTER) && resultCode == RESULT_OK) {
            loadComputers();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.computers_options_menu, menu);

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem menuItem;

        switch (orderByInUse) {
            case ORDER_BY_1:
                menuItem = menu.findItem(R.id.menuItemSubmenuOrderBy1);
                break;
            case ORDER_BY_2:
                menuItem = menu.findItem(R.id.menuItemSubmenuOrderBy2);
                break;
            default:
                return false;
        }
        menuItem.setChecked(true);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.menuItemAdd) {
            verifyTypes();

            return true;
        } else if (itemId == R.id.menuItemTypes) {
            callTypesActivity();

            return true;
        } else if (itemId == R.id.menuItemSubmenuOrderBy1) {
            item.setChecked(true);
            saveOrderPreference(ORDER_BY_1);

            return true;
        } else if (itemId == R.id.menuItemSubmenuOrderBy2) {
            item.setChecked(true);
            saveOrderPreference(ORDER_BY_2);

            return true;
        } else if (itemId == R.id.menuItemAbout) {
            callAboutActivity();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadComputers() {
        AsyncTask.execute(() -> {
            ComputerDatabase computerDatabase = ComputerDatabase.getInstance(this);
            computers = computerDatabase.computerDao().findAll();

            runOnUiThread(() -> {
                computerAdapter = new ComputerAdapter(this, computers);
                listViewComputer.setAdapter(computerAdapter);
            });
        });
    }

    private void verifyTypes() {
        AsyncTask.execute(() -> {
            ComputerDatabase computerDatabase = ComputerDatabase.getInstance(this);
            int typesQuantity = computerDatabase.typeDao().countAll();

            if (typesQuantity == 0) {
                runOnUiThread(() -> AlertDialogUtil.showErrorAlertDialog(this,
                        R.string.computers_activity_message_error_no_type_registered));

                return;
            }
            callRegisterActivityToCreate();
        });
    }

    private void readOrderByPreference() {
        orderByInUse = getSharedPreferences(FILE, Context.MODE_PRIVATE).getInt(ORDER_BY, orderByInUse);
    }

    private void saveOrderPreference(int orderBy) {
        SharedPreferences.Editor editor = getSharedPreferences(FILE, Context.MODE_PRIVATE).edit();

        editor.putInt(ORDER_BY, orderBy);
        editor.apply();
        orderByInUse = orderBy;

        orderByPreference();
    }

    private void orderByPreference() {
        if (!computers.isEmpty()) {
            Collections.sort(computers, computerComparator);
            computerAdapter.notifyDataSetChanged();
        }
    }

    private void populateList() {
        ComputerDatabase computerDatabase = ComputerDatabase.getInstance(this);

        computers = computerDatabase.computerDao().findAll();
        computerAdapter = new ComputerAdapter(this, computers);
        listViewComputer.setAdapter(computerAdapter);
        orderByPreference();
    }

    private void callRegisterActivityToCreate() {
        Intent intent = new Intent(this, ComputerFormActivity.class);

        intent.putExtra(ACTION, CREATE);

        startActivityForResult(intent, CREATE);
    }

    private void callRegisterActivityToUpdate(Computer computer) {
        Intent intent = new Intent(this, ComputerFormActivity.class);

        intent.putExtra(ACTION, UPDATE);
        intent.putExtra(ID, computer.getId());

        startActivityForResult(intent, UPDATE);
    }

    private void callTypesActivity() {
        startActivity(new Intent(this, TypesActivity.class));
    }

    private void callAboutActivity() {
        startActivity(new Intent(this, AboutActivity.class));
    }
}
