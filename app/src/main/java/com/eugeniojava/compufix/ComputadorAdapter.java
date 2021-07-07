package com.eugeniojava.compufix;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class ComputadorAdapter extends BaseAdapter {

    private final Context context;
    private final List<Computador> computadores;

    public ComputadorAdapter(Context context, List<Computador> computadores) {
        this.context = context;
        this.computadores = computadores;
    }

    @Override
    public int getCount() {
        return computadores.size();
    }

    @Override
    public Object getItem(int position) {
        return computadores.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ComputadorHolder computadorHolder;

        if (convertView == null) {
            LayoutInflater layoutInflater =
                    (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(
                    R.layout.list_view_computadores, parent, false);

            computadorHolder = new ComputadorHolder();
            computadorHolder.textViewUrgente = convertView.findViewById(R.id.textViewUrgente);
            computadorHolder.textViewTipo = convertView.findViewById(R.id.textViewTipo);
            computadorHolder.textViewCliente = convertView.findViewById(R.id.textViewCliente);
            computadorHolder.textViewProprietario =
                    convertView.findViewById(R.id.textViewProprietario);

            convertView.setTag(computadorHolder);
        } else {
            computadorHolder = (ComputadorHolder) convertView.getTag();
        }

        computadorHolder.textViewUrgente.setText(
                context.getString(
                        R.string.prefixo_urgente, computadores.get(position).getUrgente()));
        computadorHolder.textViewTipo.setText(
                context.getString(
                        R.string.prefixo_tipo, computadores.get(position).getTipo()));
        computadorHolder.textViewCliente.setText(
                context.getString(
                        R.string.prefixo_cliente, computadores.get(position).getCliente()));
        computadorHolder.textViewProprietario.setText(
                context.getString(
                        R.string.prefixo_proprietario,
                        computadores.get(position).getProprietario()));

        return convertView;
    }

    private static class ComputadorHolder {
        public TextView textViewUrgente;
        public TextView textViewTipo;
        public TextView textViewCliente;
        public TextView textViewProprietario;
    }
}
