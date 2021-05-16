//#include "stdafx.h"
#pragma comment(lib, "ws2_32.lib")
#include <winsock2.h>
#include<string>
#include <iostream>
#include<fstream>
#include<stdio.h>
#include<Windows.h>
#include <sys/stat.h>
#include<filesystem>
namespace fs = std::filesystem;


#pragma warning(disable: 4996)

class ClientReqvest
{
public:
	std::string reqvest;
	std::string content;
	
	ClientReqvest(){


	};
	void Read(char* msg)
	{ 
		char* temp = new char[strlen(msg)];
		int first=0;
		for (unsigned int  i = 0; i < strlen(msg); i++)
		{
			if (msg[i] != '\n')
			{
				temp[i] = msg[i];
			}
			else
			{
				first = i;
				temp[i] = '\0';
				reqvest = temp;
				break;
			}

		}
		first++;
		for (int i = 0; i+first < (strlen(msg)+1); i++)
		{
			if (msg[i+first] != '\n')
			{
				temp[i] = msg[i+first];
			}
			else
			{
				temp[i] = '\0';
				content = temp;
				break;
			}

		}
		delete[] temp;
		
	}

	~ClientReqvest() {
	};





};
void CreateManifestFile()
{
	std::ofstream outfile("Manifest.txt", std::ios::binary);
	
	outfile << "Manifest" << std::endl;
	outfile << "Menu.txt" << std::endl;

	std::string path = "Files";
	for (auto& p : fs::directory_iterator(path))
	{
		
		std::string s = p.path().string();
		bool write = false;
		for (int i = 0; i < s.size(); i++)
		{
			
			if (s[i] == '\\')
			{
				
				 write = true;
			}
			else
			{
				if (write)
					outfile << s[i];
			}
			
			
		}
		outfile << std::endl;
	}
	outfile << "/Updates" << std::endl;
	outfile << "Menu.txt" << std::endl;
	//outfile << "Теплый салат с куриной печенью.txt" << std::endl;
	//outfile << "Штрудель с яблоком и пломбиром.png" << std::endl;
	
		
	outfile << "/END" << std::endl;
	outfile.close();

}


int main(int argc, char* argv[]) {
	//WSAStartup
	WSAData wsaData;
	WORD DLLVersion = MAKEWORD(2, 1);
	if (WSAStartup(DLLVersion, &wsaData) != 0) {
		std::cout << "Error" << std::endl;
		exit(1);
	}
	setlocale(LC_ALL, "rus");

	CreateManifestFile();
	SOCKADDR_IN addr;
	int sizeofaddr = sizeof(addr);
	addr.sin_addr.s_addr = inet_addr("192.168.0.105");
	addr.sin_port = htons(24441);
	
	addr.sin_family = AF_INET;

	SOCKET sListen = socket(AF_INET, TCP_NODELAY, NULL);
	bind(sListen, (SOCKADDR*)&addr, sizeof(addr));
	DWORD nonBlocking = 1;
	
	listen(sListen, SOMAXCONN);


	SOCKET newConnection;
	newConnection = accept(sListen, (SOCKADDR*)&addr, &sizeofaddr);


	if (newConnection == 0) {
		std::cout << "Error #2\n";
	}
	else {
		std::cout << "Client Connected!\n";
		char msg[100];
		bool Registered = false;
		std::string Name;
		std::string Password="3241";
		while (newConnection != 0)
		{
			
			ClientReqvest Client;
			
			if(recv(newConnection, msg, sizeof(msg), 0)<=0)
				break;
			
			Client.Read(msg);
			
			std::cout << Client.reqvest<<" ";
			std::cout << Client.content << std::endl;
			
			if (Registered)
			{
				if (Client.reqvest == "DOWNLOAD")
				{
					char filestr[1024] = " ";
					std::string Filename = Client.content;
					if (Filename != "Manifest.txt" && Filename != "Menu.txt")
					{
						Filename = "Files\\" + Filename;
					}
					
					std::ifstream usfile(Filename.c_str(), std::ios::binary);
					





					if (usfile.is_open())
					{
						char answer[] = "SENDING\n";
						send(newConnection, answer, strlen(answer), 0);

						

						struct stat fi;
						stat(Filename.c_str(), &fi);
						std::cout << fi.st_size << std::endl;
						int bytes = fi.st_size;
						//if (fi.st_size % 1024 != 0)
						
							//*bytes=*bytes+1;
						std::string str = std::to_string(bytes);
						char* FILE_SIZE=new char[str.size()+1];
						itoa(bytes, FILE_SIZE, 10);
						int pacs = 0;
						int id = str.size();
						FILE_SIZE[id] = '\n';
						FILE_SIZE[id + 1] = '\0';
						Sleep(500);
						send(newConnection, FILE_SIZE,strlen(FILE_SIZE), 0);
						
						if (recv(newConnection, msg, sizeof(msg), 0) <= 0)
							break;

						Client.Read(msg);

						std::cout << Client.reqvest << " ";
						//std::cout << Client.content << std::endl;
						if (Client.reqvest == "READY")
						{

							do
							{
								usfile.read(filestr, 1024);

								send(newConnection, filestr, sizeof(filestr), 0);
								pacs++;
							} while (!usfile.eof());
							std::cout << pacs << "KB sended" << std::endl;
						}
						//char* file = new char[fi.st_size];
						//usfile.read(file, fi.st_size);
						//if (recv(newConnection, msg, sizeof(msg), 0) <= 0)
						//	break;

						///Client.Read(msg);
						//if (Client.reqvest == "READY")
					//	{
						//	send(newConnection, file, sizeof(file), 0);
							
					//	}
						
						usfile.close();
						
					}
					else
					{
						char answer[] = "FILE NOT FOUND\n";
						send(newConnection, answer, strlen(answer), 0);
						std::cout << answer << std::endl;
					}

					



				}
				else if(Client.reqvest=="OFFICIANT")
				{

					char answer[] = "OFFICIANT CALLED\n";
					send(newConnection, answer, strlen(answer), 0);
				}
				else if (Client.reqvest == "ORDER")
				{
					int size = std::stoi(Client.content);
					
					char answer[] = "ACCEPTING\n";
					send(newConnection, answer, strlen(answer), 0);
					for (int i = 0; i < size; i++)
					{
						recv(newConnection, msg, sizeof(msg), 0);
							
						Client.Read(msg);
						std::cout << Client.reqvest << " ";
						std::cout << Client.content << std::endl;
						char answer[] = "OK\n";
						send(newConnection, answer, strlen(answer), 0);


					}
					if (recv(newConnection, msg, sizeof(msg), 0) <= 0)
						break;

					Client.Read(msg);
					std::cout << Client.reqvest << " ";
					std::cout << Client.content << std::endl;
				
					send(newConnection, answer, strlen(answer), 0);

				}

				
			}
			else
			{
				if (Client.reqvest == "LOGIN")
				{
					
					char answer[] = "PASSWORD\n";
					send(newConnection, answer, strlen(answer), 0);
				}else if(Client.reqvest == "PASSWORD")
				{
					if (Client.content == Password)
					{
						char answer[] = "PASSWORD CORRECT\n";
						Registered = true;
						send(newConnection, answer, strlen(answer), 0);
						std::cout << answer << std::endl;
					}
					else
					{
						char answer[] = "WRONG PASSWORD\n";
						send(newConnection, answer, strlen(answer), 0);
						newConnection = closesocket(newConnection);
						std::cout << answer << std::endl;
					}
				}
				else
				{
					char answer[] = "NOT AUTORIZED\n";
					std::cout << answer << std::endl;
					send(newConnection, answer, strlen(answer), 0);
					newConnection = closesocket(newConnection);
				}
				

			}
			//char answer[] = "Hello_Client";
			//answer[strlen(answer)] =  '\n';



			


			


			


			
		}
	}


	
	newConnection = closesocket(newConnection);
	return 0;
}