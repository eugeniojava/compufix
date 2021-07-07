package com.eugeniojava.compufix;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ListagemActivity extends AppCompatActivity {

    private ListView listViewComputadores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listagem);

        listViewComputadores = findViewById(R.id.listViewComputadores);

        popularLista();

        listViewComputadores.setOnItemClickListener((parent, view, position, id) -> {
            Computador computador = (Computador) listViewComputadores.getItemAtPosition(position);

            Toast.makeText(getApplicationContext(),
                    getString(R.string.foi_clicado, computador.getProprietario()),
                    Toast.LENGTH_SHORT).show();
        });
    }

    private void popularLista() {
        int[] urgentes = getResources().getIntArray(R.array.urgentes);
        String[] tipos = getResources().getStringArray(R.array.tipos);
        String[] clientes = getResources().getStringArray(R.array.clientes);
        String[] proprietarios = getResources().getStringArray(R.array.proprietarios);

        ArrayList<Computador> computadores = new ArrayList<>();

        for (int i = 0; i < urgentes.length; i++) {
            computadores.add(new Computador(
                    this,
                    urgentes[i] == 1,
                    tipos[i],
                    clientes[i],
                    proprietarios[i]));
        }

        ComputadorAdapter computadorAdapter = new ComputadorAdapter(this, computadores);

        listViewComputadores.setAdapter(computadorAdapter);
    }
}
