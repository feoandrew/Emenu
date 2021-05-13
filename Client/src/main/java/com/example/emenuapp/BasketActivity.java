package com.example.emenuapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.autofill.AutofillValue;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import java.util.ArrayList;

public class BasketActivity extends AppCompatActivity {
private LinearLayout MainLinear;
private ArrayList<Integer> LayoutsId;
private ArrayList<Integer> ButtonsId;
private TextView itog;
private  DisplayMetrics metricsB;
private Dialog dialog;
private TextView text , price ,sum;
private int DialogCount, DialogCost;
private int ChangeId;
private Order UserOrder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);
        MainLinear=findViewById(R.id.mainlayout);
        dialog = new Dialog(BasketActivity.this);


        dialog.setTitle("Измените колличество");

        dialog.setContentView(R.layout.dialog);


        text = (TextView) dialog.findViewById(R.id.textView6);
        UserOrder=MenuActivity.UserOrder;
        LayoutsId = new ArrayList<>();
        ButtonsId =  new ArrayList<>();
        TextView subtitle = findViewById(R.id.subtitle);
        ActionBar act = getSupportActionBar();
        if (act != null) {
            act.hide();
        }
        itog=findViewById(R.id.itog);
        price=(TextView) dialog.findViewById(R.id.price);
        sum = (TextView) dialog.findViewById(R.id.sum);
        itog.setText("Cумма Заказа: "+UserOrder.getTotalSum());


        Display display = getWindowManager().getDefaultDisplay();
        metricsB = new DisplayMetrics();
        display.getMetrics(metricsB);
        subtitle.setWidth((metricsB.widthPixels/2));
        DisplayUserOrderIn(MainLinear);
        Button Delete = dialog.findViewById(R.id.DeleteBut);
        Delete.setWidth((metricsB.widthPixels/2));



    }

    public void onClick(View v)
    {
        if(ButtonsId.contains(v.getId()))
        {
            ChangeId=ButtonsId.indexOf(v.getId());
            DialogCount=UserOrder.getPosition(ButtonsId.indexOf(v.getId())).getCount();

            text.setText(""+DialogCount);
             DialogCost=UserOrder.getPosition(ButtonsId.indexOf(v.getId())).getCost();
            sum.setText("Cумма: "+DialogCount* DialogCost);
            String name = UserOrder.getPosition(ButtonsId.indexOf(v.getId())).getName();
            price.setText(name+"\nЦена за 1 шт: "+ DialogCost);
            dialog.show();






        }

    }
    public void onMinus(View v)
    {
        if(DialogCount>1)
            DialogCount--;
        text.setText(""+DialogCount);
        sum.setText("Cумма: "+DialogCount*DialogCost);

    }
    public void onPlus(View v)
    {
        if(DialogCount<100)
        DialogCount++;
        text.setText(""+DialogCount);
        sum.setText("Cумма: "+DialogCount*DialogCost);
    }
    public void onOk(View v)
    {
        UserOrder.getPosition(ChangeId).setCount(DialogCount);
        LinearLayout temp=findViewById(LayoutsId.get(ChangeId));
        temp.removeAllViews();
        ButtonsId.remove(ChangeId);
        DisplayPositionIn(UserOrder.getPosition(ChangeId), temp);


        itog.setText("Cумма Заказа: "+UserOrder.getTotalSum());
        dialog.hide();

    }
    public void onDelete(View v)
    {
        UserOrder.removePosition(ChangeId);
        LinearLayout temp=findViewById(LayoutsId.get(ChangeId));
        temp.removeAllViews();
        MainLinear.removeView(temp);
        LayoutsId.remove(ChangeId);
        ButtonsId.remove(ChangeId);
        //DisplayUserOrderIn(MainLinear);


        itog.setText("Cумма Заказа: "+UserOrder.getTotalSum());

        dialog.hide();
    }

    @Override
    public void onBackPressed() {


    }
    public void BackButton(View v)
    {
        Intent intent = new Intent();
        intent.putExtra("NewOrder", false );
        setResult(RESULT_OK, intent);

        this.finish();


    }
    public void onOrderBut(View v) throws InterruptedException {
        if(UserOrder.getSize()>0)
        NetworkActivity.NETWORK.SendOrder(UserOrder);
        setContentView(R.layout.new_order);


    }
    public void onNewOrderButton(View v)
    {
        Intent intent = new Intent();
        intent.putExtra("NewOrder", true );
        setResult(RESULT_OK, intent);
        this.finish();


    }






    private void DisplayPositionIn(Position pos,LinearLayout linear)
    {

        TextView Text = new TextView(this);
        Text.setLayoutParams(new LinearLayout.LayoutParams((metricsB.widthPixels/2)-20, LinearLayout.LayoutParams.MATCH_PARENT));
        Text.setTextSize(18);
        Text.setTextColor(0xFF000000);
        Text.setText(pos.getName());
        linear.addView(Text);



        Text = new TextView(this);
        Text.setLayoutParams(new LinearLayout.LayoutParams(70, LinearLayout.LayoutParams.MATCH_PARENT));
        Text.setText(" X "+pos.getCount());
        Text.setTextSize(18);
        Text.setGravity(Gravity.CENTER_VERTICAL);
        Text.setTextColor(0xFF000000);
        linear.addView(Text);




        Text = new TextView(this);
        Text.setLayoutParams(new LinearLayout.LayoutParams(100, LinearLayout.LayoutParams.MATCH_PARENT));
        Text.setText(" " +pos.getSum()+" ");
        Text.setTextSize(18);
        Text.setGravity(Gravity.CENTER_VERTICAL);
        Text.setTextColor(0xFF000000);
        linear.addView(Text);



        Button btn = new Button(this);
        btn.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        btn.setText("Изменить");
        btn.setId(View.generateViewId());
        btn.setBackgroundColor(0xFF14B51B);
        btn.setTextColor(0xFFFFFFFF);
        btn.setOnClickListener(this::onClick);
        ButtonsId.add(LayoutsId.indexOf(linear.getId()), btn.getId());
        linear.addView(btn);

    }
    public void DisplayUserOrderIn(LinearLayout linearLayout)
    {
        linearLayout.removeAllViews();
        Position pos;
        for(int i=0; i<UserOrder.getSize(); i++)
        {
            pos=UserOrder.getPosition(i);
            LinearLayout linear = new LinearLayout(this);
            linear.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 100));
            linear.setId(View.generateViewId());
            linear.setOrientation(LinearLayout.HORIZONTAL);

            linear.setDividerDrawable(getResources().getDrawable(R.drawable.divider));
            linear.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
            LayoutsId.add(linear.getId());
            linearLayout.addView(linear);

            DisplayPositionIn(pos, linear);









        }

    }
}