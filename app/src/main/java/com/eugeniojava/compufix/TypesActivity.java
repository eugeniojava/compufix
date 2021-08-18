package com.eugeniojava.compufix;

import static android.widget.AbsListView.CHOICE_MODE_SINGLE;
import static com.eugeniojava.compufix.TypeFormActivity.ACTION;
import static com.eugeniojava.compufix.TypeFormActivity.CREATE;
import static com.eugeniojava.compufix.TypeFormActivity.ID;
import static com.eugeniojava.compufix.TypeFormActivity.UPDATE;
import static com.eugeniojava.compufix.model.Type.typeComparator;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;

import com.eugeniojava.compufix.dao.ComputerDatabase;
import com.eugeniojava.compufix.model.Type;
import com.eugeniojava.compufix.util.AlertDialogUtil;

import java.util.Collections;
import java.util.List;

public class TypesActivity extends AppCompatActivity {

    private static final int REQUEST_CREATE_TYPE = 1;
    private static final int REQUEST_UPDATE_TYPE = 2;
    private ListView listViewType;
    private ActionMode actionMode;
    private int selectedPosition = -1;
    private View selectedView;
    private ArrayAdapter<Type> arrayAdapterType;
    private List<Type> types;
    private final ActionMode.Callback callback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.list_selected_item_options, menu);

            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            Type type = (Type) listViewType.getItemAtPosition(selectedPosition);
            int itemId = item.getItemId();

            if (itemId == R.id.menuItemUpdate) {
                callTypeFormActivityToUpdate(type);
                mode.finish();

                return true;
            } else if (itemId == R.id.menuItemDelete) {
                callTypeDeletionValidation(type);
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
            listViewType.setEnabled(true);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        setTitle(R.string.types_activity_title);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        listViewType = findViewById(R.id.listViewItem);
        listViewType.setOnItemClickListener((parent, view, position, id) -> {
            Type type = (Type) parent.getItemAtPosition(position);
            callTypeFormActivityToUpdate(type);
        });
        listViewType.setChoiceMode(CHOICE_MODE_SINGLE);
        listViewType.setOnItemLongClickListener((parent, view, position, id) -> {
            if (actionMode == null) {
                selectedPosition = position;
                view.setBackgroundColor(Color.LTGRAY);
                selectedView = view;
                listViewType.setEnabled(false);
                actionMode = startSupportActionMode(callback);

                return true;
            }
            return false;
        });
        loadTypes();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if ((requestCode == REQUEST_CREATE_TYPE || requestCode == REQUEST_UPDATE_TYPE) && resultCode == RESULT_OK) {
            loadTypes();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.types_options_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.menuItemAdd) {
            callTypeFormActivityToCreate();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadTypes() {
        AsyncTask.execute(() -> {
            ComputerDatabase computerDatabase = ComputerDatabase.getInstance(this);
            types = computerDatabase.typeDao().findAll();

            runOnUiThread(() -> {
                arrayAdapterType = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, types);
                Collections.sort(types, typeComparator);
                listViewType.setAdapter(arrayAdapterType);
            });
        });
    }

    private void callTypeFormActivityToCreate() {
        Intent intent = new Intent(this, TypeFormActivity.class);

        intent.putExtra(ACTION, CREATE);

        startActivityForResult(intent, REQUEST_CREATE_TYPE);
    }

    private void callTypeFormActivityToUpdate(Type type) {
        Intent intent = new Intent(this, TypeFormActivity.class);

        intent.putExtra(ACTION, UPDATE);
        intent.putExtra(ID, type.getId());

        startActivityForResult(intent, REQUEST_UPDATE_TYPE);
    }

    private void callTypeDeletion(Type type) {
        String confirmationMessage = getString(R.string.types_activity_message_confirmation_deletion,
                type.getDescription());

        DialogInterface.OnClickListener onClickListener = (dialog, which) -> {
            if (which == DialogInterface.BUTTON_POSITIVE) {
                AsyncTask.execute(() -> {
                    ComputerDatabase computerDatabase = ComputerDatabase.getInstance(this);
                    computerDatabase.typeDao().delete(type);

                    runOnUiThread(() -> {
                        types.remove(type);
                        arrayAdapterType.notifyDataSetChanged();
                    });
                });
            }
        };

        AlertDialogUtil.showConfirmationAlertDialog(this, confirmationMessage, onClickListener);
    }

    private void callTypeDeletionValidation(Type type) {
        AsyncTask.execute(() -> {
            ComputerDatabase computerDatabase = ComputerDatabase.getInstance(this);
            int computersByTypeId = computerDatabase.computerDao().countByTypeId(type.getId());

            if (computersByTypeId > 0) {
                runOnUiThread(() -> AlertDialogUtil.showErrorAlertDialog(this,
                        R.string.types_activity_message_error_type_in_use));

                return;
            }
            runOnUiThread(() -> callTypeDeletion(type));
        });
    }
}
