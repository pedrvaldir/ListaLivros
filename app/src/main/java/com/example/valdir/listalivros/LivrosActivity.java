package com.example.valdir.listalivros;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

public class LivrosActivity extends AppCompatActivity implements LoaderCallbacks<List<Livro>> {

    private static String REQUISICAO_URL_GOOGLE;

    /**
     * Valor constante para o ID do loader de earthquake. Podemos escolher qualquer inteiro.
     * Isto só importa realmente se você estiver usando múltiplos loaders.
     */
    private static final int LIVROS_LOADER_ID = 1;

    private LivrosAdapter mAdapter;

    /**
     * TextView que é mostrada quando a lista é vazia
     */
    private TextView mTextEstadoVazio;
    private Button mBtnPesquisar;
    private EditText mTextoBusca;
    private View mIndicadorCarregamento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.livros_activity);

        mBtnPesquisar = (Button) findViewById(R.id.btn_pesquisar);
        mTextoBusca = (EditText) findViewById(R.id.edit_pesquisa);
        mIndicadorCarregamento = findViewById(R.id.indicador_carregamento);
        mTextEstadoVazio = (TextView) findViewById(R.id.view_vazia);

        // Encontra uma referencia {@link ListView} no layout
        ListView LivrosListView = (ListView) findViewById(R.id.lista);

        // Crie um novo adaptador que leve uma lista vazia de terremotos como entrada
        mAdapter = new LivrosAdapter(LivrosActivity.this, new ArrayList<Livro>());

        LivrosListView.setEmptyView(mTextEstadoVazio);

        // Defina o adaptador no {@link ListView}
        // então a lista pode ser preenchida na interface do usuário
        LivrosListView.setAdapter(mAdapter);

        final LoaderManager loaderManager = getLoaderManager();

        // Seta o texto quando o app é aberto e será realizada a primeira consulta
        mTextEstadoVazio.setText(R.string.primeira_pesquisa);

        mBtnPesquisar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obter uma referência ao ConnectivityManager para verificar o estado da conectividade de rede
                ConnectivityManager connMgr = (ConnectivityManager)
                        getSystemService(Context.CONNECTIVITY_SERVICE);

                // Obter detalhes sobre a rede de dados padrão atualmente ativa
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

                // Limpa o adapter de dados de livros anteriores
                mAdapter.clear();

                // Se houver uma conexão de rede, obtenha dados
                if (networkInfo != null && networkInfo.isConnected()) {

                    // recebe a endereço da url para realizar a consulta
                    String url = getResources().getString(R.string.url_consulta);

                    // Retira os espaços do editText para consulta
                    REQUISICAO_URL_GOOGLE = url + mTextoBusca.getText().toString().replace(" ", "");

                    // Esconde a informação da tela inicial vazia
                    mTextEstadoVazio.setVisibility(View.GONE);

                    // Exibe o indicador de carregamento porque foi requisitado uma nova pesquisa
                    mIndicadorCarregamento.setVisibility(View.VISIBLE);

                    loaderManager.restartLoader(LIVROS_LOADER_ID, null, LivrosActivity.this);
                } else {
                    //Caso contrário, o erro de exibição
                    //Primeiro, esconda o indicador de carregamento, então a mensagem de erro será visível
                    View loadingIndicator = findViewById(R.id.indicador_carregamento);
                    loadingIndicator.setVisibility(View.GONE);

                    // Atualize o estado vazio sem mensagem de erro de conexão
                    mTextEstadoVazio.setText(R.string.sem_conexao_internet);
                }
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

        // Esconde o indicador de carregamento porque os dados foram carregados
        mIndicadorCarregamento.setVisibility(View.GONE);

        // Se há uma lista válida de {@link livro}s, então os adiciona ao data set do adapter.
        // Isto ativará a atualização da ListView.
        if (livros != null && !livros.isEmpty()) {
            mAdapter.addAll(livros);
        } else {
            //caso não retorne nenhum valor referente a consulta
            mTextEstadoVazio.setVisibility(View.VISIBLE);
            mTextEstadoVazio.setText(R.string.retorno_vazio);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Livro>> loader) {
        // Reinicialização do carregador, para que possamos limpar nossos dados existentes.
        mAdapter.clear();
    }
}