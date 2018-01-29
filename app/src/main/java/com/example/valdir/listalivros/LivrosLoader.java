package com.example.valdir.listalivros;

/**
 * Created by VALDIR on 23/01/2018.
 */

import java.util.List;
import android.content.AsyncTaskLoader;
import android.content.Context;

/**
 * Carrega uma lista de earthquakes usando uma AsyncTask para realizar a
 * requisição de rede para a dada URL.
 */
public class LivrosLoader extends AsyncTaskLoader<List<Livro>> {

    //Utilizado para referenciar String na interface principal
    private static Context mContext;


    /** Tag para mensagens de log */
    private static final String LOG_TAG = LivrosLoader.class.getName();

    /** URL da busca */
    private String mUrl;

    /**
     * Constrói um novo {@link LivrosLoader}.
     *
     * @param context O contexto da activity
     * @param url A URL de onde carregaremos dados
     */
    public LivrosLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    /**
     * Está e uma thread de background.
     */
    @Override
    public List<Livro> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        // Realiza requisição de rede, decodifica a resposta, e extrai uma lista de earthquakes.
        List<Livro> Livros = ConsultasUteis.buscarLivros(mUrl);
        return Livros;
    }
}