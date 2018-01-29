package com.example.valdir.listalivros;

import android.content.Context;
import android.os.Bundle;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Loader;

import java.util.ArrayList;
import java.util.List;

public class LivrosActivity extends  AppCompatActivity implements LoaderCallbacks<List<Livro>> {

    private static String REQUISICAO_URL_GOOGLE;


    /**
     * Valor constante para o ID do loader de earthquake. Podemos escolher qualquer inteiro.
     * Isto só importa realmente se você estiver usando múltiplos loaders.
     */
    private static final int LIVROS_LOADER_ID = 1;

    private LivrosAdapter mAdapter;

    /** TextView que é mostrada quando a lista é vazia */
    private TextView mEmptyStateTextView;

    private Button mBtnPesquisar;
    private EditText mTextoBusca;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.livros_activity);


        /**
         * URL para consulta do conjunto de dados na google API books
         *

        LivroAsyncTask task = new LivroAsyncTask();
        task.execute(REQUISICAO_URL_GOOGLE);
         */

        mBtnPesquisar = (Button) findViewById(R.id.btn_pesquisar);
        mTextoBusca = (EditText) findViewById(R.id.edit_pesquisa);

        // Find a reference to the {@link ListView} in the layout
        ListView LivrosListView = (ListView) findViewById(R.id.lista);

        // Create a new adapter that takes an empty list of earthquakes as input
        mAdapter = new LivrosAdapter(LivrosActivity.this, new ArrayList<Livro>());

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        LivrosListView.setEmptyView(mEmptyStateTextView);

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        LivrosListView.setAdapter(mAdapter);

        final LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(LIVROS_LOADER_ID, null, LivrosActivity.this);

        mBtnPesquisar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.googleapis.com/books/v1/volumes?q=search+";
                REQUISICAO_URL_GOOGLE = url + mTextoBusca.getText().toString().replace(" ", "");
                Log.v("Texte", mTextoBusca.getText().toString());

                loaderManager.restartLoader(LIVROS_LOADER_ID, null, LivrosActivity.this);

            }
        });
    }

    @Override
    public Loader<List<Livro>> onCreateLoader(int i, Bundle bundle) {
        // Criar um novo loader para a dada URL

        return new LivrosLoader(this, REQUISICAO_URL_GOOGLE);
    }

    @Override
    public void onLoadFinished(Loader<List<Livro>> loader, List<Livro> livros) {

        mEmptyStateTextView.setText(R.string.sem_livros);
        // Limpa o adapter de dados de livros anteriores
        mAdapter.clear();

        // Se há uma lista válida de {@link livro}s, então os adiciona ao data set do adapter.
        // Isto ativará a atualização da ListView.
        if (livros != null && !livros.isEmpty()) {
            mAdapter.addAll(livros);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Livro>> loader) {
        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();
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
    }
}