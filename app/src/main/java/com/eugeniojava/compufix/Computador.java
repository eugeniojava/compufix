package com.eugeniojava.compufix;

import android.content.Context;

public class Computador {

    private final String urgente;
    private final String tipo;
    private final String cliente;
    private final String proprietario;

    public Computador(
            Context context, boolean urgente, String tipo, String cliente, String proprietario) {
        this.urgente = urgente ?
                context.getString(R.string.urgente_sim) :
                context.getString(R.string.urgente_nao);
        this.tipo = tipo;
        this.cliente = cliente;
        this.proprietario = proprietario;
    }

    public String getUrgente() {
        return urgente;
    }

    public String getTipo() {
        return tipo;
    }

    public String getCliente() {
        return cliente;
    }

    public String getProprietario() {
        return proprietario;
    }
}
