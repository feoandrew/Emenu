#ifndef CONSOLEAPPLICATION1_H
#define CONSOLEAPPLICATION1_H
#include <iostream>
#include<string>
#include <fstream>
using namespace std;
class menu
{
public:
    std::string s;
    menu()
    {
        s = "";
    }
    menu(const menu& other)
    {
        this->s = other.s;
    }
    void operator=(const menu& other)
    {
        this->s = other.s;
    }
    friend std::istream& operator>> (ifstream& stream, menu& other)
    {
        std::ifstream fin;
        std::string path = { "menu.txt" };
        fin.open(path);
        if (!fin.is_open())
            std::cout << "Файл не может быть открыт!\n";
        else
        {
            while (!fin.eof())
            {
                //string str = "";
                getline(fin, other.s);
                string str = other.s;
                cout << str << endl;

            }
            fin.close();
        }
        return fin;
    }
};
#endif // CONSOLEAPPLICATION1_H