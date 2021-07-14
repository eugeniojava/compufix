package com.eugeniojava.compufix;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ListagemActivity extends AppCompatActivity {

    public static final int CADASTRAR = 1;
    private static final ArrayList<Computador> computadores = new ArrayList<>();
    private ComputadorAdapter computadorAdapter;
    private ListView listViewComputadores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listagem);

        listViewComputadores = findViewById(R.id.listViewComputadores);
//        popularLista();
        computadorAdapter = new ComputadorAdapter(this, computadores);
        listViewComputadores.setAdapter(computadorAdapter);
        listViewComputadores.setOnItemClickListener((parent, view, position, id) -> {
            Computador computador = (Computador) listViewComputadores.getItemAtPosition(position);

            Toast.makeText(getApplicationContext(),
                    getString(R.string.foi_clicado, computador.getProprietario()),
                    Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CADASTRAR && resultCode == Activity.RESULT_OK) {
            Bundle bundle = data.getExtras();

            if (bundle != null) {
                boolean urgente = bundle.getBoolean(MainActivity.URGENTE);
                String tipo = bundle.getString(MainActivity.TIPO);
                String cliente = bundle.getString(MainActivity.CLIENTE);
                String proprietario = bundle.getString(MainActivity.PROPRIETARIO);

                computadores.add(new Computador(this, urgente, tipo, cliente, proprietario));
                computadorAdapter.notifyDataSetChanged();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

//    private void popularLista() {
//        int[] urgentes = getResources().getIntArray(R.array.urgentes);
//        String[] tipos = getResources().getStringArray(R.array.tipos);
//        String[] clientes = getResources().getStringArray(R.array.clientes);
//        String[] proprietarios = getResources().getStringArray(R.array.proprietarios);
//
//        ArrayList<Computador> computadores = new ArrayList<>();
//
//        for (int i = 0; i < urgentes.length; i++) {
//            computadores.add(new Computador(
//                    this,
//                    urgentes[i] == 1,
//                    tipos[i],
//                    clientes[i],
//                    proprietarios[i]));
//        }
//
//        ComputadorAdapter computadorAdapter = new ComputadorAdapter(this, computadores);
//
//        listViewComputadores.setAdapter(computadorAdapter);
//    }

    public void chamarDadosAutoria(View view) {
        startActivity(new Intent(this, DadosAutoriaActivity.class));
    }

    public void chamarCadastro(View view) {
        Intent intent = new Intent(this, MainActivity.class);

        intent.putExtra(MainActivity.ACAO, CADASTRAR);

        startActivityForResult(intent, CADASTRAR);
    }
}
