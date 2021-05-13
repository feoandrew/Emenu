package com.example.emenuapp;

import android.content.Context;
import android.os.NetworkOnMainThreadException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class NetWork {
    NetWork(String host, int hostport) {
        adress = host;
        port = hostport;

        temp = new byte[1024];

    }



    private static Socket Client;
    private static InputStream in;
    private static BufferedWriter out;
    public String LastServerAnswer = " ";
    public int LastFileSize = 0;
    private boolean Connected = false;
    private String adress;
    private int port;
    private Context cont;
    private byte[] temp;


    //private boolean send = false;
   private boolean NetBusy = false;
    private boolean ConnectedUpdated = false;
    private int ReqvestType;
    private String Message;


    private Thread NET;

    private Thread NETIO;
    public void GetContext(Context context) {
        cont = context;
    }
    public boolean check()
    {
        return Client.isConnected();
    }
    public boolean StartNetwork() {
        ConnectedUpdated = false;
        NET = new Thread(() -> {
            try {
                StartNet();
            } catch (IOException | InterruptedException e) {
                ConnectedUpdated = true;
            }
        });
        NET.start();
        while (!ConnectedUpdated) {

        }
        return Connected;
    }

    private void StartNet() throws IOException, InterruptedException {

        Client = new Socket(adress, port);


        if (Client.isConnected()) {

            in = Client.getInputStream();
            out = new BufferedWriter(new OutputStreamWriter(Client.getOutputStream(),"windows-1251"));
            while (in.available() > 0) {
                in.skip(1);
            }

            while (Client.isConnected()) {
                Connected = true;
                ConnectedUpdated = true;
                /*if (send) {
                    switch (ReqvestType) {
                        case 1:
                            Send(Message);
                            send = false;
                            break;
                        case 2:
                            Load(Message, cont);
                            send = false;
                            break;

                        default:
                            break;


                    }
                }*/


            }

        }
        Connected = false;
        ConnectedUpdated = true;
    }


    private String Send(String content) throws IOException {
        String answer = "";

        if (Client.isConnected()) {

            out.write(content + "\n\0");

            out.flush();
            BufferedReader rd = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            answer = rd.readLine();


            LastServerAnswer = answer;

            return answer;
        }
        return "NOT CONNECTED";

    }




    private boolean Load(String FILENAME, Context fileContext) throws IOException, InterruptedException {

        String answer = "";

        answer = Send("DOWNLOAD\n"+FILENAME);
        LastServerAnswer = answer;
        if (answer.compareTo("SENDING") == 0) {




            AtomicInteger len = new AtomicInteger();
            BufferedReader tmp = new BufferedReader(new InputStreamReader(in, "UTF-8"));
          //  int a = in.available();
            String s=tmp.readLine();
           int FILE_SIZE = Integer.parseInt(s);
           out.write("READY\n\0");
           out.flush();

           /* for (int i = 0; i < 4; i++) {
               int ByteNum = 0;
                //Text.setText(FILE_SIZE+" ");

               ByteNum = (int) temp[i] & 0xff;

                FILE_SIZE = FILE_SIZE + ByteNum * (int) Math.pow(2, (8 * i));

            }*/
            LastFileSize = FILE_SIZE;




            Thread Runtime;
            int Remain = FILE_SIZE;
            OutputStream Fout = fileContext.openFileOutput(FILENAME, 0);
            while (Client.isConnected() && (Remain>0 || in.available()>0)) {
                Thread.sleep(1);
                    len.set(0);
                    AtomicBoolean flag=new AtomicBoolean();

                    Runtime=new Thread(()-> {
                        try {
                            len.set(in.read(temp, 0, 1024));

                            flag.set(true);
                        } catch (IOException e) {

                        }
                    });
                    flag.set(false);


                    Runtime.start();
                    Runtime.join(10000);


                    if(!flag.get()) {
                        Runtime.interrupt();
                        LastServerAnswer="RELOAD";
                        break;
                    }


                    if(Remain>1024) {
                        Fout.write(temp, 0, 1024);
                    }
                    else
                    {
                        if(Remain>0)
                        Fout.write(temp, 0, Remain);
                    }



                Fout.flush();
                Remain-=len.get();
            }
            if(!Client.isConnected())
            {
                LastServerAnswer="NOT CONNECTED";
            }
          //  temp = new byte[FILE_SIZE];
         //  out.write("READY");
        //   out.flush();
         //   in.read(temp, 0, FILE_SIZE);
          //  Fout.write(temp, 0, FILE_SIZE);
            Fout.close();
            while (in.available() > 0) {
                in.read();
            }



            return true;
        } else {

            return false;
        }

    }

//////////////////////////////////////////////////////////////////////////////////////////////////////////
    public String Auntification(String Name, int Password) throws InterruptedException {
        String s = SendReqvest("LOGIN\n"+Name);
        if(s.compareTo("0")!=0)
        {
            if(s.compareTo("PASSWORD")==0) {
                s = SendReqvest("PASSWORD\n" + Password);

                if (s.compareTo("PASSWORD CORRECT") == 0) {
                        return s;
                }

            }

        }
        return s;

    }

    public String SendReqvest(String reqvest, String content) throws InterruptedException {

            NETIO=new Thread(()-> {
                try {
                    Send(reqvest+"\n"+content);
                } catch (IOException e) {

                }

            });
            NETIO.start();
            NETIO.join();
            String s = LastServerAnswer;
            NetBusy = false;
            return s;

        }


    public String SendReqvest(String reqvest) throws InterruptedException {
        NETIO=new Thread(()-> {
            try {
                Send(reqvest);
            } catch (IOException e) {
                return ;
            }

        });
        NETIO.start();
        NETIO.join();
        String s = LastServerAnswer;
        NetBusy = false;
        return s;
    }
    public String SendPosition(String Name, int count) throws InterruptedException {
      /*  if (!NetBusy && Connected) {
            ReqvestType = 1;
            Message = Name+"\n"+count;

            NetBusy = true;
            send = true;
            while (send) {
            }
            String s = LastServerAnswer;
            NetBusy = false;
            return s;

        }
        return "0";*/
        NETIO=new Thread(()-> {
            try {
                Send(Name+"\n"+count);
            } catch (IOException e) {
                return ;
            }

        });
        NETIO.start();
        NETIO.join();
        String s = LastServerAnswer;
        NetBusy = false;
        return s;
    }
    public String SendOrder(Order order) throws InterruptedException {

        int size=order.getSize();

        if(SendReqvest("ORDER",""+size).compareTo("ACCEPTING")==0)
        {
            for (int i=0; i<size; i++)
            {
               if(SendPosition(order.getPosition(i).getName(), order.getPosition(i).getCount()).compareTo("OK")!=0)
               {
                   break;
               }

            }

            return SendPosition("TOTALSUM", order.getTotalSum());
        }
        return LastServerAnswer;
    }

    public String Download(String FILENAME) throws InterruptedException {
        NETIO=new Thread(()-> {
            try {
                Load(FILENAME, cont);
            } catch (IOException | InterruptedException e) {
                return ;
            }

        });
        NETIO.start();
        NETIO.join();
        String s = LastServerAnswer;
        NetBusy = false;
        return s;

    }


    public void Destruct() throws IOException {

        try {


            if (Connected) {
                Client.close();
                in.close();
                out.close();
            }
        } catch (NetworkOnMainThreadException e) {

        }
    }




}