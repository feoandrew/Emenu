#ifndef MAINWINDOW_H
#define MAINWINDOW_H

#include<QtWidgets/QMainWindow>
#include<string>
#include<list>
#include <QtGui>
#include <QTextCodec>
#include "position.h"
using namespace std;
QT_BEGIN_NAMESPACE
namespace Ui { class MainWindow; }
QT_END_NAMESPACE
class MainWindow : public QMainWindow
{
    Q_OBJECT
public:
    Ui::MainWindow* ui;
    MainWindow(QWidget* parent = nullptr);
    ~MainWindow();
private slots:
    void on_pushButton_2_clicked();
    void on_tableWidget_clicked(const QModelIndex& index);
    void ActionTimer();
    void on_pushButton_3_clicked();
private:
    //Ui::MainWindow* ui;
    string empty_str = " ";
    list<string> empty_order = { " " };
    int row, column, rows_in_table = 10;
    int curr_pos = 0;
    int empty_price = NULL;
    list<Position> Orders;
};
#endif
