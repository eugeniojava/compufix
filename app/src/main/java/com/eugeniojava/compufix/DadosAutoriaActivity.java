package com.eugeniojava.compufix;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DadosAutoriaActivity extends AppCompatActivity {

    private TextView textViewNome;
    private TextView textViewCurso;
    private TextView textViewEmail;
    private TextView textViewDescricao;
    private TextView textViewNomeUtfpr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dados_autoria);
        setTitle(getString(R.string.dados_autoria_titulo));
        textViewNome = findViewById(R.id.textViewNome);
        textViewCurso = findViewById(R.id.textViewCurso);
        textViewEmail = findViewById(R.id.textViewEmail);
        textViewDescricao = findViewById(R.id.textViewDescricao);
        textViewNomeUtfpr = findViewById(R.id.textViewNomeUtfpr);

        textViewNome.setText(getString(R.string.dados_autoria_nome));
        textViewCurso.setText(getString(R.string.dados_autoria_curso));
        textViewEmail.setText(getString(R.string.dados_autoria_email));
        textViewDescricao.setText(getString(R.string.dados_autoria_descricao));
        textViewNomeUtfpr.setText(getString(R.string.dados_autoria_nome_utfpr));
    }

    public void voltar(View view) {
        finish();
    }
}
