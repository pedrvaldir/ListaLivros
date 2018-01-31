package com.example.valdir.listalivros;
// Métodos auxiliares relacionados à solicitação e recebimento de dados

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

public final class ConsultasUteis {

    public static final String LOG_TAG = LivrosActivity.class.getName();

    //Uma instancia do objeto ConsultasUteis não será necessária, privado
    private ConsultasUteis() {
    }

    //Realiza a busca de livros de acordo com a url
    public static ArrayList<Livro> buscarLivros(String requisicaoUrl) {

        // Cria um objeto URL
        URL url = criarUrl(requisicaoUrl);

        String respostaJson = null;

        // Execute a solicitação HTTP para o URL e receba uma resposta JSON de volta
        try {
            respostaJson = requisicaoHttp(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Erro ao fechar o fluxo de entrada", e);
        }

        // Extraia campos relevantes da resposta JSON e retorne arraylist {@link Livro}
        ArrayList<Livro> livros = extrairArrayJson(respostaJson);

        // Retornar uma lista de objetos {@link Livro}
        return livros;
    }

    /**
     * Retorna a nova URL fornecido.
     */
    private static URL criarUrl(String stringUrl) {

        URL url = null;

        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Erro com a criação de URL ", e);
        }
        return url;
    }

    /**
     * Faça uma solicitação HTTP para o URL fornecido e devolva um String como a resposta da consulta de livros.
     */
    private static String requisicaoHttp(URL url) throws IOException {

        String respostaJson = "";

        // Se o URL for nulo, volte mais cedo.
        if (url == null) {
            return respostaJson;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milisegundos */);
            urlConnection.setConnectTimeout(15000 /* milisegundos */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Se a solicitação foi bem sucedida (código de resposta 200),
            // então leia o fluxo de entrada e analise a resposta.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                respostaJson = lerStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Código de resposta de erro: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problema ao recuperar os resultados JSON dos livros.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }

        //retorna a String da resposta JSON
        return respostaJson;
    }

    /**
     * Converta o {@link InputStream} em uma String que contém toda a resposta JSON do servidor.
     */
    private static String lerStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Retornar uma lista de Objetos {@link ArrayList<Livro>}, analisando as informações da resposta livroJSON convertida em String.
     */
    private static ArrayList<Livro> extrairArrayJson(String livroJSON) {


        // Se a String JSON estiver vazio ou nulo, volte mais cedo.
        if (TextUtils.isEmpty(livroJSON)) {
            return null;
        }

        try {

            //Crie um ArrayList vazio para que possamos começar a adicionar os livros
            ArrayList<Livro> livros = new ArrayList<>();

            //crie uma lista de objetos livros com os dados correspondentes.
            JSONObject baseJsonResponse = new JSONObject(livroJSON);
            JSONArray livroArray = baseJsonResponse.getJSONArray("items");

            for (int i = 0; i < livroArray.length(); i++) {
                JSONObject livroAtual = livroArray.getJSONObject(i);
                JSONObject informacoes = livroAtual.getJSONObject("volumeInfo");

                String titulo = informacoes.getString("title");

                String autores;
                if (informacoes.has("authors")) {
                    JSONArray authorsArray = informacoes.getJSONArray("authors");
                    autores = formatarAutores(authorsArray);
                } else {
                    autores = "não informado";
                }

                String versao = informacoes.getString("contentVersion");
                String paginas;

                if (informacoes.has("pageCount")) {
                    paginas = informacoes.getString("pageCount");
                } else {
                    paginas = "não informado";
                }

                Livro livro = new Livro(titulo, autores, versao, paginas);
                livros.add(livro);
            }

            // Crie uma lista de Objetos {@link ArrayList<Livro>}
            return new ArrayList<Livro>(livros);

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problema ao analisar os resultados de livros da resposta JSON", e);
        }
        return null;
    }

    /**
     * Retonar a lista String de dados
     */
    public static String formatarAutores(JSONArray listaAutores) throws JSONException {

        String listaAutoresEmString = null;

        if (listaAutores.length() == 0) {
            return null;
        }

        for (int i = 0; i < listaAutores.length(); i++) {
            if (i == 0) {
                listaAutoresEmString = listaAutores.getString(0);
            } else {
                listaAutoresEmString += ", " + listaAutores.getString(i);
            }
        }
        return listaAutoresEmString;
    }
}