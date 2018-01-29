package com.example.valdir.listalivros;

import android.view.View;
import android.widget.TextView;

/**
 * Created by VALDIR on 21/01/2018.
 */

public class ViewHolder {
    final TextView tituloLivro;
    final TextView autorLivro;
    final TextView versaoLivro;
    final TextView quantPagLivro;

    public ViewHolder(View view){
        tituloLivro = (TextView) view.findViewById(R.id.titulo);
        autorLivro = (TextView) view.findViewById(R.id.autor);
        versaoLivro = (TextView) view.findViewById(R.id.versao);
        quantPagLivro = (TextView) view.findViewById(R.id.quant_paginas);
    }
}
