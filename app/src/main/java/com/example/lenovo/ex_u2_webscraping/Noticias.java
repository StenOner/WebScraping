package com.example.lenovo.ex_u2_webscraping;

import android.graphics.Bitmap;

public class Noticias {
    String titulo;
    String contenido;
    Bitmap photo;

    public Noticias(String titulo, String contenido, Bitmap photo) {
        this.titulo = titulo;
        this.contenido = contenido;
        this.photo = photo;
    }
}
