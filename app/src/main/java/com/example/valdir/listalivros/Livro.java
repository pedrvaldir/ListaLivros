package com.example.valdir.listalivros;

/**
 * um objeto {@link Livro} contem informações referente a um Livro.
 */
public class Livro {

    /** Título do livro */
    private String mTitulo;

    /** Autor do Livro */
    private String mAutor;

    /** versao da publicacao do livro*/
    private String mVersao;

    /** Quantidade de páginas que o livro contém */
    private String mQuantPag;

    /**
     * Contrutor de um novo objeto {@link Livro}.
     *
     * @param titulo é o nome do livro
     * @param autor é o responsável/Criador do Livro
     * @param versao se refere a versao que o livro foi publicado
     * @param quantPaginas é a quantidade de páginas que o livro contém
     */
    public Livro(String titulo, String autor, String versao, String quantPaginas) {
        this.mTitulo = titulo;
        this.mAutor = autor;
        this.mVersao = versao;
        this.mQuantPag = quantPaginas;
    }

    /**
     * Retorna o título do livro
     */
    public String getTitulo() {
        return mTitulo;
    }

    /**
     * Retorna o autor do livro
     */
    public String getAutor() {
        return mAutor;
    }

    /**
     * Retorna a versao da publicacao do livro
     */
    public String getVersao() {
        return mVersao;
    }

    /**
     * Retorna a quantidade de páginas que contém o livro
     */
    public String getQuantPag() {
        return mQuantPag;
    }
}