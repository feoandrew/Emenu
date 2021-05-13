package com.example.emenuapp;

import java.util.ArrayList;
import java.util.List;

class Position
{
    Position(String name, int count, int cost)
    {
        Name=name;
        Count = count;
        Cost = cost;
    }
    private String Name;
    private int Count=0;
    private int Cost=0;

    public int getSum()
    {
        return Cost*Count;
    }
    public int getCount(){ return Count;}
    public void setCount(int count)
    {
        Count=count;
    }

    public String getName()
    {
        return Name;
    }
    public void increase(int count)
    {
        Count+=count;
    }
    public void decrease(int count)
    {
        Count-=count;
    }
    public int getCost()
    {
        return Cost;
    }

}

public class Order {


   private ArrayList<Position> order;
   private int TotalSum=0;


    public Order()
    {
        order=new ArrayList<Position>();
    }
    public Position getPosition(int index)
    {
        return order.get(index);
    }
    public int getSize()
    {
        return order.size();
    }

    public int getTotalSum()
    {

        return TotalSum;
    }
    public void put(String name, int count, int cost)
    {
        boolean contains=false;
        if(count>0) {
            TotalSum+=count*cost;
            for (Position pos:order)
            {
                if(pos.getName().compareTo(name)==0)
                {
                    contains=true;
                    pos.increase(count);
                }

            }
            if(!contains)
            order.add(new Position(name, count, cost));
        }

    }
    public void removePosition(int Id)
    {
        order.remove(Id);
        TotalSum=0;
        for(Position pos:order)
        {
            TotalSum+=pos.getSum();
        }
    }
}
