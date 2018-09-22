package com.example.lenovo.ex_u2_webscraping;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    static ArrayList<Noticias> noticias;
    static ArrayList<String> contentUrl;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Noticias Peru21: Hoy");

        progressBar=(ProgressBar)findViewById(R.id.pb_main);

        noticias=new ArrayList<>();
        contentUrl=new ArrayList<>();

        getNews();
    }
    private void getNews() {
        new getNewsATask().execute();
    }
    private class getNewsATask extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            try{
                String url = "https://peru21.pe/archivo/peru";
                Document document = Jsoup.connect(url).get();
                Elements titles = document.select(".column-flows .flow-detail .flow-title");
                String[] mTitle = new String[100];
                int count=0;
                for(Element title : titles){
                    mTitle[count]=title.text();
                    count++;
                }
                Elements contenidos = document.select(".column-flows .flow-detail .flow-summary");
                String[] mContenido = new String[100];
                count=0;
                for(Element contenido : contenidos){
                    mContenido[count]=contenido.text();
                    count++;
                }
                Elements contenidosLinks = document.select(".column-flows .flow-detail .flow-title .page-link");
                for(Element contenidosLink : contenidosLinks){
                    String contenidoUrl = "https://peru21.pe";
                    contenidoUrl+=contenidosLink.attr("href");
                    contentUrl.add(contenidoUrl);
                }
                Elements photosLinks = document.select(".column-flows .flow-image picture source");
                ArrayList<Bitmap> bitmaps = new ArrayList<>();
                for(Element photosLink : photosLinks){
                    String photoStringUrl = "https://peru21.pe";
                    photoStringUrl+=photosLink.attr("srcset");
                    URL photoUrl = new URL(photoStringUrl);
                    HttpURLConnection connection = (HttpURLConnection) photoUrl.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream input = connection.getInputStream();
                    bitmaps.add(BitmapFactory.decodeStream(input));
                }
                for(int i=0;i<count;i++){
                    noticias.add(new Noticias(mTitle[i],mContenido[i],
                            bitmaps.get(i)));
                }

            }catch(Exception ex){
                ex.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void voids){
            RecyclerView rv = (RecyclerView)findViewById(R.id.rv);
            rv.setHasFixedSize(true);

            LinearLayoutManager llm = new LinearLayoutManager(getBaseContext());
            rv.setLayoutManager(llm);

            RVAdapter adapter = new RVAdapter(noticias, getBaseContext());
            rv.setAdapter(adapter);

            progressBar.setVisibility(View.GONE);
        }
    }
}
