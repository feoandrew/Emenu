#include "mainwindow.h"
#include "ui_mainwindow.h"
#include"ConsoleApplication1.h"
#include<QTimer>
#include<QMessageBox>
#include "position.h"

using namespace std;

MainWindow::MainWindow(QWidget* parent) : QMainWindow(parent), ui(new Ui::MainWindow) // конструктор
{
    ui->setupUi(this);                                                                              
    ui->tableWidget->setRowCount(rows_in_table);                                                    
    ui->tableWidget->setColumnCount(3);                                                              
    QStringList HorizontalLabels_0, HorizontalLabels_1;                                             
    HorizontalLabels_0 << u8"Ќомер столика";                                                        
    HorizontalLabels_0 << u8"«аказ"; 
    HorizontalLabels_0 << u8"÷ена заказа";
    ui->tableWidget->setHorizontalHeaderLabels(HorizontalLabels_0);      
    ui->tableWidget->horizontalHeader()->setSectionResizeMode(0, QHeaderView::Stretch);
    ui->tableWidget->horizontalHeader()->setSectionResizeMode(1, QHeaderView::Stretch);
    ui->tableWidget->horizontalHeader()->setSectionResizeMode(2, QHeaderView::Stretch);
    for (int x = 0; x < rows_in_table; x++)                                                            //
    {                                                                                                 //
        QTableWidgetItem* empty_item,* empty_item1, * empty_item2;                                   // заполнение всех €чеек таблицы пробелами (" ") 
        empty_item = new QTableWidgetItem(" ");                                                     // 
        empty_item1 = new QTableWidgetItem(" ");                                                   //
        empty_item2 = new QTableWidgetItem(" ");                                                  // 
        ui->tableWidget->setItem(x, 0, empty_item);                                              //
        ui->tableWidget->setItem(x, 1, empty_item1);                                            //
        ui->tableWidget->setItem(x, 2, empty_item2);                                           //
    }                                                                                         //
    QTimer *time = new QTimer(this);
    connect(time,SIGNAL(timeout()),this,SLOT(ActionTimer()));
    time->start(5000);
}
QString decodefromWin1251toU8(string str)                   // решает проблемы с кодировкий
{
    QByteArray encoded;
    encoded = QByteArray::fromStdString(str);
    QTextCodec* codec = QTextCodec::codecForName("windows-1251");
    QString temp = codec->toUnicode(encoded);
    QTextCodec* codec2 = QTextCodec::codecForName("UTF-8");
    QString out = codec2->fromUnicode(temp);
    return out;
}
void MainWindow::ActionTimer()
{ 
    QTableWidgetItem *item;
    while(curr_pos< rows_in_table)
    {
        QTableWidgetItem* item_tb_numder, *item_str_order, * item_price;
        Position ord = Orders.front();
        QString tb_numder = decodefromWin1251toU8(ord.table_numder); 
        QString tb_price;
        if (ord.sum != NULL)
            tb_price = decodefromWin1251toU8(to_string(ord.sum));
        else
            tb_price = decodefromWin1251toU8(" ");
        QString str_order;
        while (!ord.order.empty())
        {
            str_order += decodefromWin1251toU8(ord.order.front());
            ord.order.pop_front();
            if(!ord.order.empty())
                str_order += "\n";
        }
        item_tb_numder = new QTableWidgetItem(tb_numder);        
        item_str_order = new QTableWidgetItem(str_order); 
        item_price = new QTableWidgetItem(tb_price);
        ui->tableWidget->setItem(curr_pos, 0, item_tb_numder);
        ui->tableWidget->setItem(curr_pos, 1, item_str_order);
        ui->tableWidget->setItem(curr_pos, 2, item_price);
        Orders.pop_front();
        if (Orders.empty())
        {
            Orders.push_back(Position(empty_str, empty_order, empty_price));
        }   
        curr_pos++;
    }
}
MainWindow::~MainWindow()
{
    delete ui;
}
void MainWindow::on_pushButton_2_clicked()
{
    QString tmp;
    int tmp_sum;
    QTableWidgetItem* item, * item1_0, * item1_1,  * item1_2,* item2_0, * item2_1, * item2_2;
    for (int m = row; m < rows_in_table - 1; m++)
    {
        tmp = ui->tableWidget->item(m + 1, 0)->text();//
        item = new QTableWidgetItem(tmp);
        ui->tableWidget->setItem(m, 0, item);
        item1_0 = new QTableWidgetItem();
        ui->tableWidget->setItem(m + 1, 0, item1_0);
        tmp = ui->tableWidget->item(m + 1, 1)->text();//
        item = new QTableWidgetItem(tmp);
        ui->tableWidget->setItem(m, 1, item);
        item1_1 = new QTableWidgetItem();
        ui->tableWidget->setItem(m + 1, 1, item1_1);//
        tmp=ui->tableWidget->item(m + 1, 2)->text();
        item = new QTableWidgetItem(tmp);
        ui->tableWidget->setItem(m, 2, item);
        item1_2 = new QTableWidgetItem();
        ui->tableWidget->setItem(m + 1, 2, item1_2);
    }
    curr_pos--;
    if (curr_pos < 0)
        curr_pos = 0;
    item2_0 = new QTableWidgetItem();
    ui->tableWidget->setItem(rows_in_table - 1, 0, item2_0);
    item2_1 = new QTableWidgetItem();
    ui->tableWidget->setItem(rows_in_table - 1, 1, item2_1);
    item2_2 = new QTableWidgetItem();
    ui->tableWidget->setItem(rows_in_table - 1, 2, item2_2);
}
void MainWindow::on_tableWidget_clicked(const QModelIndex& index)
{
    row = index.row();
    column = index.column();
}
void MainWindow::on_pushButton_3_clicked()
{
    QMessageBox::about(this, u8"заказ", ui->tableWidget->currentItem()->text());
}