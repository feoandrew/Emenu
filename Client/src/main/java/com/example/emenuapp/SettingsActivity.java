package com.example.emenuapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Instrumentation;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SettingsActivity extends AppCompatActivity {

    String Name;
    String IPadress;
    int port;
    int code;
    private EditText InputName, InputAdress, InputPort, InputCode;


    boolean CheckIP(String adress, int port)
    {
         String IPRegex="^\\d{1,3}\\.+\\d{1,3}\\.+\\d{1,3}\\.+\\d{1,3}$";
        String IPpart="\\d{1,3}";
         Pattern pattern;
        pattern=Pattern.compile(IPRegex);
        boolean DataCorrect = true;
        Matcher matcher = pattern.matcher(IPadress);
        if(!matcher.find())
        {
            InputAdress.setTextColor(0xFFFD0000);
            DataCorrect=false;
        }
        else
        {
            pattern=Pattern.compile(IPpart);
            matcher = pattern.matcher(IPadress);
            while (matcher.find())
            {
              int part = Integer.parseInt(IPadress.substring(matcher.start(), matcher.end()));
              if(part<0 || part>255)
              {
                  InputAdress.setTextColor(0xFFFD0000);
                  DataCorrect=false;
                  break;
              }
            }
           if(DataCorrect)InputAdress.setTextColor(0xFF000000);

        }
        port = Integer.parseInt(InputPort.getText().toString());
        if(port<1024 || port>49000)
        {
            InputPort.setTextColor(0xFFFD0000);
            DataCorrect=false;
        }
        else
        {
            InputPort.setTextColor(0xFF000000);
        }
        return DataCorrect;
    }


    boolean ReadConfigfile(String FILENAME)
    {

        boolean rez=false;
        try {

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    openFileInput(FILENAME)));


            String input=br.readLine();
            if(input.compareTo("Config")==0)
            {
                Name=br.readLine();
                IPadress=br.readLine();
               input = br.readLine();
               port = Integer.parseInt(input);
                input = br.readLine();
               code = Integer.parseInt(input);
                rez=true;



            }

            br.close();



        } catch (IOException e) {
            rez = false;
        }
        finally
        {
            return rez;
        }

    }
    void WriteConfigFile(String FILENAME)
    {
        try {

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                    openFileOutput(FILENAME, 0)));

            bw.write("Config\n");
            bw.write(Name+"\n");
            bw.write(IPadress+"\n");
            bw.write(port+"\n");
            bw.write(code+"\n");
            bw.flush();

            bw.close();
            }





         catch (IOException e) {

        }

    }

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        InputName=findViewById(R.id.Name);
        InputAdress=findViewById(R.id.Ipinput);
        InputPort=findViewById(R.id.inputPort);
        InputCode=findViewById(R.id.Password);



    }

    @Override
    protected void onResume()
    {

        super.onResume();
        if(ReadConfigfile("Config.txt"))
        {
            InputName.setText(Name);
            InputAdress.setText(IPadress);
            InputPort.setText(""+port);
            InputCode.setText(""+code);
        }

    }
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }

    public void onClickApply(View view)
    {

        Name=InputName.getText().toString();
       IPadress = InputAdress.getText().toString();

       port = Integer.parseInt(InputPort.getText().toString());

        code = Integer.parseInt(InputCode.getText().toString());
        if(CheckIP(IPadress, port))WriteConfigFile("Config.txt");


    }
    public void onClickExit(View view)
    {
        this.finish();



    }
}