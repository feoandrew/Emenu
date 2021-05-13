package com.example.emenuapp;

import android.annotation.SuppressLint;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;

import android.view.View;

import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;


public class MenuActivity extends AppCompatActivity implements View.OnClickListener {
    private Menu mainmenu;
    private View mContentView;
    private LinearLayout Line1;
    private LinearLayout Line2;
    private ArrayList<Integer> ButtonsId;
    private int MenuLevel=0;
    private int CurrentCat=0;
    public static Order UserOrder;
    private  Button basket;


    ActionBar act;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if ( savedInstanceState == null )   // приложение запущено впервые
        {
            CurrentCat = 0;
            MenuLevel=0;
        }
        else // приложение восстановлено из памяти
        {

            MenuLevel = savedInstanceState.getInt("Menu Level");
            CurrentCat = savedInstanceState.getInt("Category");
        }
        ButtonsId=new ArrayList<>();
        UserOrder=new Order();
        setContentView(R.layout.activity_menu);
        mContentView = findViewById(R.id.constr);
        mContentView.setOnClickListener(view -> onTouch());
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        act = getSupportActionBar();

        if (act != null) {
            act.hide();
        }
        mainmenu = new Menu(getApplicationContext());
        mainmenu.Build("Menu.txt");
        basket=findViewById(R.id.basketbutton);


        Line1 = findViewById(R.id.linearLayout5);
        Line2 = findViewById(R.id.linear);
        if(MenuLevel==0) {
            DisplayMenu(mainmenu);
        }
        else
        {
            DisplayCategory(mainmenu.GetCategory(CurrentCat));
        }





    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("Menu Level", MenuLevel);
        outState.putInt("Category", CurrentCat);



    }


    public void onTouch() {
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        ActionBar act = getSupportActionBar();
        if (act != null) {
            act.hide();
        }

    }

    @Override
    public void onBackPressed() {


    }


    public void onClick(View v)
    {
        if(ButtonsId.contains(v.getId()))
        {


            if(MenuLevel==0) {
                CurrentCat=ButtonsId.indexOf(v.getId());
                DisplayCategory(mainmenu.GetCategory(ButtonsId.indexOf(v.getId())));
                MenuLevel = 1;

            }
            else if(MenuLevel==1)
            {
                Intent intent = new Intent(this, ChoiceActivity.class);
                intent.putExtra("Filename", mainmenu.GetCategory(CurrentCat).getMeal(ButtonsId.indexOf(v.getId())).getFileName());
                startActivityForResult(intent, 1);
            }

        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode , requestCode,data);

        if(requestCode==1) {
            if (data == null) {
                return;
            }
            String name = data.getStringExtra("name");
            int count = data.getIntExtra("count", 0);
            int cost = data.getIntExtra("cost", 0);
            if(count>0)
            UserOrder.put(name, count, cost);
        }
        else if(requestCode==2)
        {
            if(data.getBooleanExtra("NewOrder", false))
            {
                UserOrder=new Order();
                DisplayMenu(mainmenu);
                MenuLevel = 0;
            }
        }
        basket.setText("Корзина("+UserOrder.getSize()+")");
        
    }
    public void BackButton(View v)
    {

            if(MenuLevel==1) {
                DisplayMenu(mainmenu);
                MenuLevel = 0;

            }


    }
    public void onOfficiantButton(View v) throws InterruptedException {
        NetworkActivity.NETWORK.SendReqvest("OFFICIANT");
    }
    public void onBasketButton(View v)
    {
        Intent intent = new Intent(this, BasketActivity.class);

        startActivityForResult(intent, 2);
    }


















    private void DisplayMealIn(Meal meal, LinearLayout layout) {
        ImageButton btn = new ImageButton(this);
        btn.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 250));
        btn.setImageDrawable(meal.getImageDrawable());
        btn.setScaleType(ImageView.ScaleType.FIT_XY);
        btn.setId(View.generateViewId());
        btn.setBackgroundColor(0xFF000000);
        btn.setOnClickListener(this);
        ButtonsId.add(btn.getId());
        layout.addView(btn);
        TextView Text = new TextView(this);
        Text.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 130));
        Text.setTextColor(0xFF000000);
        Text.setGravity(Gravity.CENTER_HORIZONTAL);
        Text.setTextSize(16);
        Text.setText(meal.getName()+"\nЦена: "+meal.getCost());
        layout.addView(Text);

    }
    private void DisplayCategoryIcon(Category cat, LinearLayout layout) {
        ImageButton btn = new ImageButton(this);
        btn.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 250));
        if(cat.GetSize()>0)
            btn.setImageDrawable(cat.getMeal(0).getImageDrawable());
            btn.setScaleType(ImageView.ScaleType.FIT_XY);
            btn.setId(View.generateViewId());
            btn.setOnClickListener(this);
            ButtonsId.add(btn.getId());
            btn.setBackgroundColor(0xFF000000);
            layout.addView(btn);



        TextView Text = new TextView(this);
        Text.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 75));
        Text.setTextColor(0xFF000000);

        Text.setText(cat.getName());
        Text.setGravity(Gravity.CENTER_HORIZONTAL);
        Text.setTextSize(18);
        layout.addView(Text);

    }
    private void DisplayCategory(Category cat) {
        boolean ok=true;
        int index;
        TextView Title=findViewById(R.id.CatName);
        Title.setText(cat.getName());
        Line1.removeAllViews();
        Line2.removeAllViews();
        ButtonsId.clear();
        for(index=0; index<cat.GetSize(); index++)
             {
                if((index % 2)==0) {
                    DisplayMealIn(cat.getMeal(index), Line1);
                }
                else
                {
                    DisplayMealIn(cat.getMeal(index), Line2);
                }

            }


        }
    private void DisplayMenu(Menu menu) {

        int index;
        Line1.removeAllViews();
        Line2.removeAllViews();
        ButtonsId.clear();
        TextView Title=findViewById(R.id.CatName);
        Title.setText("Категории");
        for(index=0; index<menu.GetSize(); index++)
        {
            if((index % 2)==0) {
                DisplayCategoryIcon(menu.GetCategory(index), Line1);
            }
            else
            {
                DisplayCategoryIcon(menu.GetCategory(index), Line2);
            }

        }

    }



}










