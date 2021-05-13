#pragma once 
#include <iostream>
#include<string>
#include<list>
using namespace std;
class Position
{
public:
	string table_numder;
	list<string> order;
	int sum;
public:
	Position(const string other_number, list<string> other, int other_sum)
	{
		order = other;
		table_numder = other_number;
		sum = other_sum;
	}
	Position(const Position& other)
	{
		this->table_numder = other.table_numder;
		this->order = other.order;
		this->sum = other.sum;
	}
	Position& operator =(const Position& other)
	{
		while (!order.empty())
			order.pop_back();
		this->table_numder = other.table_numder;
		this->order = other.order;
		this->sum = other.sum;
		return *this;
	}
};