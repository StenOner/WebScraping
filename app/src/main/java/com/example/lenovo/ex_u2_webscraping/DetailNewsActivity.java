package com.example.lenovo.ex_u2_webscraping;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DetailNewsActivity extends AppCompatActivity {

    TextView textViewTitle, textViewContent;
    ImageView imageViewPhoto;
    ProgressBar progressBar;

    String title, content;
    Bitmap photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_news);

        textViewTitle=(TextView)findViewById(R.id.txt_title_detail);
        textViewContent=(TextView)findViewById(R.id.txt_content_detail);
        imageViewPhoto=(ImageView)findViewById(R.id.iv_photo_detail);
        progressBar=(ProgressBar)findViewById(R.id.pb_detail);

        int pos=getIntent().getIntExtra("EXTRA_POSITION",0);
        title=MainActivity.noticias.get(pos).titulo;
        photo=MainActivity.noticias.get(pos).photo;
        content="";
        String noticia="Detalle de Noticia " +(pos+1);
        setTitle(noticia);

        String contentUrl=MainActivity.contentUrl.get(pos);
        getNewsContent(contentUrl);

    }
    private void getNewsContent(String contentUrl) {
        new GetNewsContentATask().execute(contentUrl);
    }
    private class GetNewsContentATask extends AsyncTask<String,Void,Void>{

        @Override
        protected Void doInBackground(String... strings) {
            try {
                Document document = Jsoup.connect(strings[0]).get();
                Elements photos = document.select(".image img");
                for(Element photoDetail : photos){
                    String photoStringUrl = photoDetail.attr("src");
                    URL photoUrl = new URL(photoStringUrl);
                    HttpURLConnection connection = (HttpURLConnection) photoUrl.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream input = connection.getInputStream();
                    photo= BitmapFactory.decodeStream(input);
                }
                Elements contenidos = document.select(".news-text-content p");
                for(Element contenido : contenidos) {
                    content += contenido.text() + "\n";
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void voids){
            textViewTitle.setText(title);
            textViewContent.setText(content);
            imageViewPhoto.setImageBitmap(photo);
            progressBar.setVisibility(View.GONE);
        }
    }
}
