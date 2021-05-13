package com.example.emenuapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class ChoiceActivity extends AppCompatActivity {

    private TextView Title, Description;
    private ImageView Image;
    private TextView Counter;
    private int Count=1;
    private Meal selected;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getIntent().getExtras();
        String Filename = arguments.get("Filename").toString();
         selected = ReadMealFromFile(Filename);
        setContentView(R.layout.activity_choice);
        Title=findViewById(R.id.Title2);
        Description=findViewById(R.id.Descr);
        Image=findViewById(R.id.imageView2);
        Counter=findViewById(R.id.textView6);
        Counter.setText(""+Count);
        Title.setText(selected.getName());
        Description.setText(selected.getDescription()+"\nЦена "+selected.getCost());
        Image.setImageDrawable( selected.getImageDrawable());
        ActionBar act = getSupportActionBar();
        if (act != null) {
            act.hide();
        }




    }

    public void BackButton(View v)
    {
        Intent intent = new Intent();
        intent.putExtra("name", selected.getName());
        intent.putExtra("count", 0);
        setResult(RESULT_OK, intent);

        this.finish();

    }
    public void onMinus(View v)
    {
        if(Count>1)
        Count--;
        Counter.setText(""+Count);

    }
    public void onPlus(View v)
    {
        if(Count<100)
        Count++;
        Counter.setText(""+Count);
    }
    public  void onAdd(View v)
    {
        Intent intent = new Intent();
        intent.putExtra("name", selected.getName());
        intent.putExtra("count", Count);
        intent.putExtra("cost", selected.getCost());
        setResult(RESULT_OK, intent);

        this.finish();
    }
    @Override
    public void onBackPressed() {


    }




    private Meal ReadMealFromFile(String Filename)
    {
        try {


            BufferedReader reader = new BufferedReader(new InputStreamReader(openFileInput(Filename)));
            String s=reader.readLine();
            if(s.compareTo("MEAL")==0)
            {
                String Name=" ";
                int Cost=0;
                String Description=" ";
                String Img;
                Name=reader.readLine();
                Cost=Integer.parseInt(reader.readLine());
                Description=reader.readLine();
                Img=reader.readLine();
                reader.close();
                Drawable Draw = BitmapDrawable.createFromPath(getFilesDir().getPath() + "/" + Img);
                return  new Meal(Name, Cost, Description, Draw, Filename+".txt");




            }

        }
        catch (FileNotFoundException e)
        {} catch (IOException e) {

        }
        return  new Meal("Еrror", 0, "Еrror", null," ");
    }
}