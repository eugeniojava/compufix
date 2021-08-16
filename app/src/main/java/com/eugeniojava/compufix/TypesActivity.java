package com.eugeniojava.compufix;

import static com.eugeniojava.compufix.ComputerFormActivity.ID;
import static com.eugeniojava.compufix.ComputerFormActivity.UPDATE;
import static com.eugeniojava.compufix.TypeFormActivity.ACTION;
import static com.eugeniojava.compufix.TypeFormActivity.CREATE;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.eugeniojava.compufix.dao.ComputerDatabase;
import com.eugeniojava.compufix.model.Type;

import java.util.List;

public class TypesActivity extends AppCompatActivity {

    private static final int REQUEST_CREATE_TYPE = 1;
    private static final int REQUEST_UPDATE_TYPE = 2;
    private ListView listViewType;
    private ArrayAdapter<Type> arrayAdapterType;
    private List<Type> types;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        listViewType = findViewById(R.id.listViewItem);
        listViewType.setOnItemClickListener((parent, view, position, id) -> {
            Type type = (Type) parent.getItemAtPosition(position);
            callTypeActivityToUpdate(type);
        });
        //TODO: add menu
        setTitle(R.string.types_activity_title);
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
            callTypeActivityToCreate();

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
                listViewType.setAdapter(arrayAdapterType);
            });
        });
    }

    private void callTypeActivityToCreate() {
        Intent intent = new Intent(this, TypeFormActivity.class);

        intent.putExtra(ACTION, CREATE);

        startActivityForResult(intent, REQUEST_CREATE_TYPE);
    }

    private void callTypeActivityToUpdate(Type type) {
        Intent intent = new Intent(this, TypeFormActivity.class);

        intent.putExtra(ACTION, UPDATE);
        intent.putExtra(ID, type.getId());

        startActivityForResult(intent, REQUEST_UPDATE_TYPE);
    }
}
