package com.example.valdir.listalivros;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;

public class LivrosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.livros_activity);


        // Cria uma lista falsa de livros
        ArrayList<Livro> livros = new ArrayList<>();

        livros.add(new Livro("titulo", "autor", "data", "123"));
        livros.add(new Livro("titulo", "autor", "data", "123"));
        livros.add(new Livro("titulo", "autor", "data", "123"));
        livros.add(new Livro("titulo", "autor", "data", "123"));
        livros.add(new Livro("titulo", "autor", "data", "123"));
        livros.add(new Livro("titulo", "autor", "data", "123"));
        livros.add(new Livro("titulo", "autor", "data", "123"));
        livros.add(new Livro("titulo", "autor", "data", "123"));
        livros.add(new Livro("titulo", "autor", "data", "123"));
        livros.add(new Livro("titulo", "autor", "data", "123"));
        livros.add(new Livro("titulo", "autor", "data", "123"));
        livros.add(new Livro("titulo", "autor", "data", "123"));
        livros.add(new Livro("titulo", "autor", "data", "123"));
        livros.add(new Livro("titulo", "autor", "data", "123"));
        livros.add(new Livro("titulo", "autor", "data", "123"));
        livros.add(new Livro("titulo", "autor", "data", "123"));
        livros.add(new Livro("titulo", "autor", "data", "123"));
        livros.add(new Livro("titulo", "autor", "data", "123"));

        // declara o Adapter e define o Array que irá alimentar
        LivrosAdapter livrosAdapter = new LivrosAdapter(this, livros);

        // Procura uma referência para o {@link ListView} no Layout livros_activity.xml
        ListView livrosListView = (ListView) findViewById(R.id.lista);

        // referencia a lista que o adapter irá popular
        livrosListView.setAdapter(livrosAdapter);

    }
}