package com.example.coursecron;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    int position,positionVeng;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView usersList = findViewById(R.id.valuteList);
        TextView value = findViewById(R.id.tv_value);

        value.setText("Загрузка...");
        new Thread(new Runnable() {
            public void run() {
                try{
                    String content = download("http://www.cbr.ru/scripts/XML_daily.asp");
                    usersList.post(new Runnable() {
                        public void run() {
                            ValuteXmlParser parser = new ValuteXmlParser();
                            if(parser.parse(content))
                            {
                                ArrayAdapter<Valute> adapter = new ArrayAdapter(getBaseContext(),
                                        android.R.layout.simple_list_item_1, parser.getValutes());
                                usersList.setAdapter(adapter);
                                checkList(parser.getValutes());
                                value.setText(String.valueOf(priceNorv(parser.getValutes())));
                            }
                        }
                    });
                }
                catch (IOException ex){
                    value.post(new Runnable() {
                        public void run() {
                            value.setText("Ошибка: " + ex.getMessage());
                        }
                    });
                }
            }
        }).start();
    }

    private String download(String urlPath) throws IOException{
        StringBuilder xmlResult = new StringBuilder();
        BufferedReader reader = null;
        InputStream stream = null;
        HttpURLConnection connection = null;
        try {
            URL url = new URL(urlPath);
            connection = (HttpURLConnection) url.openConnection();
            stream = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(stream));
            String line;
            while ((line=reader.readLine()) != null) {
                xmlResult.append(line);
            }
            return xmlResult.toString();
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (stream != null) {
                stream.close();
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
    public void checkList (ArrayList<Valute> valutes){
        for (int i = 0; i < valutes.size() ; i++){
            if (valutes.get(i).getNumCode().equals("578")){
                position = i;
            }
            if (valutes.get(i).getNumCode().equals("348")){
                positionVeng = i;
            }
        }
    }
    public float priceNorv (ArrayList<Valute> valutes){
        String nominalNorvStr = valutes.get(position).getName();
        String nominalVengStr = valutes.get(positionVeng).getName();
        String valueNorvStr = valutes.get(position).getAge();
        valueNorvStr = valueNorvStr.replace(",",".");
        String valueVengStr = valutes.get(positionVeng).getAge();
        valueVengStr = valueVengStr.replace(",",".");
        int nominalNorv = Integer.parseInt(nominalNorvStr);
        float valueNorv = Float.parseFloat(valueNorvStr);
        int nominalVeng = Integer.parseInt(nominalVengStr);
        float valueVeng = Float.parseFloat(valueVengStr);
        float oneNorv = valueNorv/nominalNorv;
        float oneVeng = valueVeng/nominalVeng;
        float priceOneNorvAtVeng = oneNorv/oneVeng;
        return priceOneNorvAtVeng;
    }
}