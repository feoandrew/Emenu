package com.example.emenuapp;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageButton;
import android.content.Context;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

class Category {
    private String Name;
    private ArrayList<Meal> meals;

    private int Size;

    Category(String name)
    {
        Name =name;
        meals=new ArrayList<Meal>();

        Size=0;
    }
    public void put(Meal meal){

            meals.add(meal);
            Size++;
    }
    public int GetSize(){
        return Size;
    }
    public String getName()
    {
        return Name;
    }
    public Meal getMeal(int i){
        return meals.get(i);
    }


}

class Meal {
    Meal(String name, int cost, String Descr, Drawable image, String TxtFilename)
    {
        Name=name;
        Cost=cost;
        Description= Descr;

        Image=image;
        FileName=TxtFilename;

    }
    private String Name;
    private int Cost;
    private String Description;

    private String FileName;
    public String getName()
    {
        return Name;
    }
    public int getCost()
    {
        return Cost;
    }
    private Drawable Image;
    public String getDescription()
    {
        return Description;

    }

    public String getFileName()
    {

        return FileName;

    }
    public Drawable getImageDrawable()
    {
        return Image;
    }



}

public class Menu {



    private ArrayList<Category> categories;
    private Context fileContext;
    private int Size=0;


    Menu(Context context)
    {
        fileContext = context;
        categories = new ArrayList<Category>();
    }
    public int GetSize()
    {
        return Size;
    }
    private Meal ReadMealFromFile(String Filename)
    {
        try {


            BufferedReader reader = new BufferedReader(new InputStreamReader(fileContext.openFileInput(Filename+".txt")));
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
                Drawable Draw = BitmapDrawable.createFromPath(fileContext.getFilesDir().getPath() + "/" + Img);
                return  new Meal(Name, Cost, Description, Draw, Filename+".txt");




            }

        }
        catch (FileNotFoundException e)
        {} catch (IOException e) {

        }
        return  new Meal("Еrror", 0, "Еrror", null, " ");
    }

    public boolean Build(String configFilename)
    {
        BufferedReader br;
        try {
            boolean end=false;
            String s;
            br=new BufferedReader(new InputStreamReader(fileContext.openFileInput(configFilename),"windows-1251"));
            int CatID=-1;
            int Cost=0;
            String Description=" ";
            s=br.readLine();
            if(s.compareTo("MENU")==0)
            while(!end)
            {

                s=br.readLine();
                char []flag=new char[1];
                s.getChars(0, 1, flag, 0);
                if(flag[0]=='/')
                {

                    char[]name=new char[s.length()-1];
                    s.getChars(1, s.length(), name, 0);

                    s=new String(name);
                    if(s.compareTo("END")!=0) {
                        CatID++;
                        Size++;
                        categories.add(new Category(s));
                    }
                    else
                    {
                        end=true;
                    }

                }
                else {



                    if(CatID>=0)
                   categories.get(CatID).put(ReadMealFromFile(s));
                }


            }
            br.close();
            return true;
        }
        catch (FileNotFoundException e)
        {
            return false;
        } catch (IOException e) {
            return false;
        }

    }
    public Category GetCategory(int i)
    {
        return categories.get(i);
    }

}
