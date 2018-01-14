package com.example.valdir.listalivros;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by VALDIR on 14/01/2018.
 */

public class LivrosAdapter extends ArrayAdapter<Livro> {

    /**
     * Constroi um novo {@link LivrosAdapter}.
     *
     * @param context do app
     * @param livros  é a lista de livros, é a fonte de dados que alimenta o adaptador
     */
    public LivrosAdapter(Context context, List<Livro> livros) {
        super(context, 0, livros);
    }

    /**
     * Retorna uma exibição de um item da lista sobre informações do livro  na posição dada na lista.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Verifique se existe uma exibição de item de lista existente (chamada convertView) que podemos reutilizar,
        // Caso contrário, se convertView for nulo, infle um novo layout de item de lista.
        View listaItemView = convertView;
        if (listaItemView == null) {
            listaItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.item_lista_livros, parent, false);
        }

        // Encontre o livro na posição indicada da lista.
        Livro livroAtual = getItem(position);

        // Encontre o TextView com a ID titulo
        TextView tituloLivro = (TextView) listaItemView.findViewById(R.id.titulo);
        // Mostre o titulo do livro atual nesta TextView
        tituloLivro.setText(livroAtual.getTitulo());

        //Encontre o TextView com a ID autor
        TextView autorLivro = (TextView) listaItemView.findViewById(R.id.autor);
        // Mostre o autor do livro atual nesta TextView
        autorLivro.setText(livroAtual.getAutor());

        //Encontre o TextView com a ID data
        TextView dataLivro = (TextView) listaItemView.findViewById(R.id.data);
        // Mostre a data do livro atual nesta TextView
        dataLivro.setText(livroAtual.getData());

        //Encontre o TextView com a ID quantPag
        TextView quantPagLivro = (TextView) listaItemView.findViewById(R.id.quant_paginas);
        // Mostre a quantidade de páginas do livro atual nesta TextView
        quantPagLivro.setText(livroAtual.getQuantPag());

        //Retonar o item da lista em questão para ser exibido
        return listaItemView;
    }
}
