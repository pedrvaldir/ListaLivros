package com.example.valdir.listalivros;

import android.os.Bundle;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;

public class LivrosActivity extends AppCompatActivity {

    private static final String REQUISICAO_URL_GOOGLE =
            "https://www.googleapis.com/books/v1/volumes?q=search+android";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.livros_activity);

        /**
         * URL para consulta do conjunto de dados na google API books
         */



        LivroAsyncTask task = new LivroAsyncTask();
        task.execute(REQUISICAO_URL_GOOGLE);

    }

    private class LivroAsyncTask extends AsyncTask<String, Void, ArrayList<Livro>> {

        /**
         * Esse método é chamado por uma thread em segundo plano, então, podemos executar
         * operações mais lentas como conexões de rede
         * Não é correto atualizar a UI usando uma operação em segundo plano, então, apenas
         * retornamos {@link ArrayList<Livro>} como resultado.
         */
        @Override
        protected ArrayList<Livro> doInBackground(String... urls) {

            // Não execute o pedido se não houver URLs ou se for nula
            if (urls.length < 1 || urls[0] == null) {
                return null;
            }

            // Execute a solicitação HTTP para dados de Livros e processe a resposta.
            ArrayList<Livro> result = ConsultasUteis.buscarLivros(urls[0]);

            //retorna o ArrayList de livro
            return result;
        }

        /**
         * Esse método é chamado na UI principal depois que o trabalho em segundo plano foi executado.
         * Está correto modificar a UI com esse método. Pegamos o objeto {@link ArrayList<Livro>} (que
         * foi retornado do doInBackground()) e atualizamos a tela.
         */
        @Override
        protected void onPostExecute(ArrayList<Livro> resultado) {

            // Se não houver resultados, não faça nada.
            if (resultado == null) {
                return;
            }
            atualizaUi(resultado);
        }
    }

    private void atualizaUi(ArrayList<Livro> ListaLivros) {

        final LivrosAdapter livrosAdapter = new LivrosAdapter(this, ListaLivros);

        // Encontre uma referência ao {@link ListView} no layout
        ListView livrosListView = (ListView) findViewById(R.id.lista);


        //Seta o adapter na {@link livrosListView}
        // para que a lista possa ser populada na interface do usuário
        livrosListView.setAdapter(livrosAdapter);

        /** - Caso necessite usar a opcao clicar na lista para abrir informações referente ao livro
         *    livrosListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
         *    @Override
         *    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
         *
         *       // Achar o terremoto atual que foi clicado
         *       Livro livroAtual = livrosAdapter.getItem(position);
         *
         *       // Converte o URL String em um objeto URI (para passar no construtor de Intent)
         *       Uri livroUri = Uri.parse(livroAtual.getUrl());
         *
         *       // Cria um novo intent para visualizar a URI do earthquake
         *       Intent websiteIntent = new Intent(Intent.ACTION_VIEW, earthquakeUri);
         *
         *       // Envia o intent para lançar uma nova activity
         *       startActivity(websiteIntent);
         *
         *       }
         *   }
         *);
         */
    }
}