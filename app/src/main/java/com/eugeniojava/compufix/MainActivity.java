package com.eugeniojava.compufix;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText editTextProprietario;
    private EditText editTextModelo;
    private EditText editTextFabricante;
    private EditText editTextDescricao;
    private Spinner spinnerTipo;
    private RadioGroup radioGroupCliente;
    private CheckBox checkBoxUrgente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextProprietario = findViewById(R.id.editTextProprietario);
        editTextModelo = findViewById(R.id.editTextModelo);
        editTextFabricante = findViewById(R.id.editTextFabricante);
        editTextDescricao = findViewById(R.id.editTextDescricao);
        spinnerTipo = findViewById(R.id.spinnerTipo);
        popularSpinner();
        radioGroupCliente = findViewById(R.id.radioGroupCliente);
        checkBoxUrgente = findViewById(R.id.checkBoxUrgente);
    }

    private void popularSpinner() {
        ArrayList<String> lista = new ArrayList<>();

        lista.add(getString(R.string.tipo_formatacao));
        lista.add(getString(R.string.tipo_manutencao));
        lista.add(getString(R.string.tipo_reparo));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, lista);

        spinnerTipo.setAdapter(adapter);
    }

    public void limpar(View view) {
        editTextProprietario.setText(null);
        editTextModelo.setText(null);
        editTextFabricante.setText(null);
        editTextDescricao.setText(null);
        radioGroupCliente.clearCheck();
        checkBoxUrgente.setSelected(false);

        editTextProprietario.requestFocus();

        Toast.makeText(this, R.string.mensagem_sucesso_limpar, Toast.LENGTH_SHORT).show();
    }

    public void cadastrar(View view) {
        String proprietario = editTextProprietario.getText().toString();
        String modelo = editTextModelo.getText().toString();
        String fabricante = editTextFabricante.getText().toString();
        String descricao = editTextDescricao.getText().toString();
        String tipo = (String) spinnerTipo.getSelectedItem();
        String cliente = obterCliente();
        String urgente = checkBoxUrgente.isChecked() ?
                getString(R.string.urgente_sim) :
                getString(R.string.urgente_nao);

        if (!validarCampo(this, editTextProprietario, R.string.mensagem_erro_proprietario)
                || !validarCampo(this, editTextModelo, R.string.mensagem_erro_modelo)
                || !validarCampo(this, editTextFabricante, R.string.mensagem_erro_fabricante)
                || !validarCampo(this, editTextDescricao, R.string.mensagem_erro_descricao)
                || !validarCliente(this, cliente, R.string.mensagem_erro_cliente)) {
            return;
        }

        String mensagemCadastro = getString(
                R.string.mensagem_sucesso,
                proprietario,
                modelo,
                fabricante,
                descricao,
                tipo,
                cliente,
                urgente);
        Toast.makeText(this, mensagemCadastro, Toast.LENGTH_LONG).show();
    }

    private String obterCliente() {
        switch (radioGroupCliente.getCheckedRadioButtonId()) {
            case R.id.radioButtonPessoaFisica:
                return getString(R.string.cliente_pessoa_fisica);
            case R.id.radioButtonPessoaJuridica:
                return getString(R.string.cliente_pessoa_juridica);
            case R.id.radioButtonSemCadastro:
                return getString(R.string.cliente_sem_cadastro);
            default:
                return "Cliente não especificado";
        }
    }

    private boolean validarCampo(Context context, EditText editText, int mensagemErro) {
        String stringCampo = editText.getText().toString();

        if (stringCampo == null || stringCampo.trim().isEmpty()) {
            Toast.makeText(context, mensagemErro, Toast.LENGTH_SHORT).show();
            editText.requestFocus();

            return false;
        }

        return true;
    }

    private boolean validarCliente(Context context, String cliente, int mensagemErro) {
        if (cliente.equals("Cliente não especificado")) {
            Toast.makeText(context, mensagemErro, Toast.LENGTH_SHORT).show();

            return false;
        }

        return true;
    }
}
