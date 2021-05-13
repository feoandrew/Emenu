package com.example.emenuapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class NetworkActivity extends AppCompatActivity {
    public static NetWork NETWORK;
    String Name;
    String IPadress;
    String ManifestFile="Manifest.txt";
    int port;
    int code;
    TextView Text;
    Thread NETPROCESSING;
    void WriteText(TextView text, String content) throws InterruptedException {
        runOnUiThread(() -> text.setText(content));
        Thread.sleep(100);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NETPROCESSING = new Thread(()-> {

                    try {
                        WriteText(Text, "Подключение к серверу");
                        Thread.sleep(3000);

                        if (ReadConfigfile("Config.txt")) {
                            NETWORK = new NetWork(IPadress, port);

                            if (NETWORK.StartNetwork()) {
                                NETWORK.GetContext(getApplicationContext());

                                String answer;
                                answer = NETWORK.Auntification(Name, code);
                                WriteText(Text, "Загрузка файлов");
                                if (answer.compareTo("PASSWORD CORRECT") == 0) {

                                    do {
                                        answer = NETWORK.Download(ManifestFile);
                                    }while (answer.compareTo("RELOAD")==0);

                                    if (answer.compareTo("SENDING") == 0) {

                                    } else {
                                        WriteText(Text, answer);
                                    }
                                    BufferedReader rd = new BufferedReader(new InputStreamReader(openFileInput(ManifestFile),"windows-1251"));
                                    boolean Success=true;
                                    ArrayList<String> Manifest = new ArrayList<String>(0);
                                    if (rd.readLine().compareTo("Manifest") == 0) {


                                        String filename = rd.readLine();
                                        while (filename.compareTo("/Updates") != 0)
                                        {


                                                    File H=new File(getFilesDir().getPath()+"/"+filename);
                                                    if(!H.exists()) {
                                                        do {
                                                            answer = NETWORK.Download(filename);
                                                        }while (answer.compareTo("RELOAD")==0);
                                                        if (answer.compareTo("SENDING") == 0) {
                                                            WriteText(Text, filename + " Downloaded Successfuly");
                                                            Manifest.add(filename);


                                                        } else {
                                                            Success = false;
                                                            WriteText(Text, answer + filename);
                                                            break;
                                                        }
                                                    }
                                                    else
                                                    {
                                                        Manifest.add(filename);

                                                    }
                                            filename = rd.readLine();




                                        }
                                        filename = rd.readLine();
                                        while (filename.compareTo("/END") != 0 && Success) {


                                            answer = NETWORK.Download(filename);
                                            if (answer.compareTo("SENDING") == 0) {
                                                WriteText(Text, filename + " Downloaded Successfuly");
                                                filename = rd.readLine();
                                                Success = true;
                                            } else {
                                                Success = false;
                                                WriteText(Text, answer + filename);
                                                break;
                                            }
                                        }
                                        rd.close();



                                    }

                                       if(Success)
                                       {
                                           WriteText(Text, "Download Success");
                                           File dir = getFilesDir(); //path указывает на директорию
                                           File[] arrFiles = dir.listFiles();
                                           for (File F:arrFiles) {
                                               boolean Garbage=true;
                                               if(F.getName().compareTo("Config.txt")==0)
                                                   continue;
                                               for (String s: Manifest) {

                                                   if(F.getName().compareTo(s)==0)
                                                   {
                                                       Garbage=false;
                                                       break;
                                                   }

                                               }
                                               if(Garbage)
                                               {
                                                   F.delete();
                                               }



                                           }
                                           runOnUiThread(()-> {
                                               Intent intent = new Intent(this, MenuActivity.class);
                                           startActivity(intent);
                                           });
                                       }


                                } else {
                                    WriteText(Text, answer);
                                }
                                

                                if(!NETWORK.check())
                                {
                                    WriteText(Text, "Cоединение Разорвано");
                                }
                            } else {
                                WriteText(Text, "FAILED TO CONNECT TO: \n" + IPadress + " : " + port + "\n Проверьте подключение к сети");
                            }
                        }
                    }
                    catch (InterruptedException | IOException e )
                    {
                        try {
                            WriteText(Text, "ЕRROR");
                        } catch (InterruptedException interruptedException) {
                            interruptedException.printStackTrace();
                        }
                        try {
                            NETWORK.Destruct();
                        } catch (IOException ioException) {

                        }
                    }
                });


       




    }
    @Override
    protected void onStart()
    {
        super.onStart();



            setContentView(R.layout.activity_network);



        Text = findViewById(R.id.textView5);
        Text.setText("Подготовка к загрузке");


    }
    @Override
    protected void onResume()
    {
        super.onResume();
        NETPROCESSING.start();



    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        try {
             NETWORK.Destruct();

              } catch (IOException e) {

              }
    }
}