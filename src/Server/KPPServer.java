package Server; // �������� � ������ ������

import java.io.BufferedReader; // ������ ����������� ���������
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import Client.Error_db;
import Client.Error_login;

public class KPPServer // ����������� �������
{
	public static int portID = 4301; // ������ ����, ������� ��� ����� �������
	
	private static ArrayList streams; // ������� ���������, ��� ������ ������ ���� ������, ������� ����������� � �����
	private static Connection c; // ���������� ����� ��� ��
	private static Statement st; // ���������� ��� ��, ��� ������� ������
	private static PrintWriter writer; // ����� ������ 
	private static ArrayList<String> list = new ArrayList<String>(); // ���������-������ ������ ������
	private static int number = 0;
	
//������������ � �� 
private static void setDB() 
{
	String url = "jdbc:mysql://localhost:3306/KPPMessanger"; // ����� �� + ����, ������� ��� �������
	String login = "root";
	String pass = "root";
	
	
	try 
	{
		Class.forName("com.mysql.jdbc.Driver"); // ��������� �������
		c=DriverManager.getConnection(url, login, pass); // ���������� ������� �������������� � ��
		st = c.createStatement(); // ��������� ������ ��������� �������
	} 
	catch (SQLException e)  // ��������� ������ �������� � ��
	{
		System.out.println("Error 37! Magic problems with dedication users message(SQL). I am in server!");
		e.printStackTrace(); // ������ ����� �� ������
	}
	catch (ClassNotFoundException e) 
	{
		System.out.println("Error 42! Magic problems with dedication users message(classNotFound). I am in server!");
		e.printStackTrace(); // ��� ������ �������� ������������ ��� �����
	}
	
}
	
	private static class Listener implements Runnable // ������� ���. ����� ��������� �������
	{
		BufferedReader reader; // ����� ��������� ���� � �������
		PrintWriter pw; // ���������� ���� � ������
		
		Listener(Socket sock, PrintWriter pw) // ����������� �� ����� ������� + ����� ������
		{
			this.pw = pw;
				try
				{
					InputStreamReader is = new InputStreamReader(sock.getInputStream());
					reader = new BufferedReader(is); // ������� ����� ����������� ���������� �������� ���������
				} 
				catch (IOException e) 
				{
					System.out.println("Error 62! Magic problems with dedication users message(IO). I am in server!");
				}
		}
		@Override
		public void run() // ������������ ����� run ��� ������, ������� ����������� �� ���������� Runnable
		{
			try 
			{
				String message; // ���� ����� ���������� �������� ��������	
				while( (message=reader.readLine()) != null )
					{
						if(message.equals("keySecretKPPMessanger11")) // ���� msg = ���������� �����
						{
							streams.remove(pw); // ������� ������ ��� �������� ���� �������������
							streams.trimToSize(); // ������������ ������� ���������, ����� �������� ������ �������
						}
						else
						{
							if(message.startsWith("spEcialForUser11")) // ���� ����� ����, �� ��� ��
							{
								System.out.println("Private: " + message); // ������� �� ����� ,��� ������
								spreadPrivate(message); // �����, ����������� ������ ����������
							}
							else
							{
							System.out.println(message);
							spreadInChat(message); // �����, ����������� ���� ����������
							}
						}
					}
			} 
			catch (IOException e)  // ������ �����-�����
			{
				System.out.println("Error 95! Magic problems with dedication users message(IO). I am in server!");
			}
		}
	}

	private static void spreadInChat(String msg)
	{
		int cut = msg.indexOf(' ') + 1;
		String login = msg.substring(0, cut-2); // � ��������� �������� ������� �� ������
		String text = msg.substring(cut);
		String hello = login + ": I have connected!"; // ��������� �� �������� �����
		String exit = login + ": I have disconnected!";
		String logout = login + ": I have logged out.";
		//save(login,msg);
		
		java.util.Iterator<PrintWriter> iter = streams.iterator(); // ���������� ���� ������������� ���������
		
		while(iter.hasNext()) // ���� ���� ��� - ������
		{
			try
			{
				if(msg.equals(hello)) // ���� ����������, �� ��������� ������������ � ������ �������������
				{
					list.add(login); // �� �����
					writer.println("forlist" + list.get( list.size()-1 ));
					writer.flush();
					for(int i=0; i<list.size()-1; i++)
					{
						writer.println("forlist" + list.get( i )); // � �������� ����
						writer.flush(); // ����������� �����, �����������, ����� ����� ����������� �������������
					}
	
				}
				
				if(msg.equals(exit)) // ���� ���������, �� ������� ������������ �� �������
				{
					for(int i=0; i<list.size(); i++)
					{
						if(list.get(i).equals(login))
						{
							writer.println("fromlist" + list.get( i )); // �� ��������
							writer.flush();
						}
					}
					list.remove(login); // �� �����
					list.trimToSize(); // ������� ��������� �� ������ �������� ����� ��������
				}
				
				if(msg.equals(logout)) // ���� ����� ����, �� ���� ������ ������������ ��������
				{
					for(int i=0; i<list.size(); i++)
					{
						if(list.get(i).equals(login))
						{
							writer.println("fromlist" + list.get( i )); // � ��������
							writer.flush();
						}
					}
					list.remove(login); // � �����
					list.trimToSize(); // ������� ��������� ��� ����������� ��������
				}
				
				
				if(!text.equals("")) // ���� ��������� �� ������ � ���� ������� ����
				{

					writer = iter.next(); // ���������� ���� �������������
					//writer.println("forlist" + list.get());
					writer.println(msg); // � ���� ���������� ������
					writer.flush();
				}
				else
				{
					writer = iter.next(); // ���� ��������� ������ - �� �������� ������, ���� ������
					writer.flush();
				}
			}
			catch(Exception e)
			{
				System.out.println("Error 182! Iterator is dumb. Correct it! I am in server!");
			}
		}
	}
	
	private static void spreadPrivate(String msg) // ��������� ��
	{
		String log = msg.substring(16); // ������� �������� �����-���, ����� �������� �������
		int cut = log.indexOf('\\') + 1 ;
		String sender = log.substring(0, cut-1); // �������� �����������
		log = log.substring(cut);
		cut = log.indexOf(' ') + 1 ;
		String reciever = log.substring(0, cut-2); // �������� ����������
		log = log.substring(cut); // �������� ������ ������� -- ������ �� �������� ���������
		
		java.util.Iterator<PrintWriter> iter = streams.iterator(); 
		
		while(iter.hasNext()) // ���� ���� ����������
		{
			try
			{
					if(list.get(number).equals(reciever)) // ���� ��� ����������
					{
						writer = iter.next(); // ���� ������ �� ������
						number++; // ���������� ����� (�����, ����� ��������� ���� � ������ � ��������� ������ ������)
						writer.println("prIvaTeMeSsSaGGGe123"+ reciever + sender + ": " + log); // ������ 
						writer.flush(); // � ������� ������ ��� ����������, ������� �� ������� �������
					}
					else
					{
						if(list.get(number).equals(sender)) // ���� ��� �����������
						{
							writer = iter.next();
							number++;
							writer.println("spEcIalFORRecieveRR5" + sender + ": " + log); // ��� �����������
							writer.flush(); // ������� �� ������� �������, �� ���� � ���, ��� �� ���������� �������
						}
						else
						{
							writer = iter.next(); // ����� ������ ���� ������ �� ������
							number++; // ���� �� ������ ����������
							writer.flush(); // ����������� �������� �����
						}
					}
			}
			catch(Exception ex)
			{
				System.out.println("Error 230! Iterator is dumb. Correct it! I am in server!-privates");
			}
		}
		number = 0; // ���������� �������
	}
	
	//��������� ��������� � �� -- �� ������������ � ������ 0.2 (��������, �� � ������� ����� �����)
	private static void save(String login, String msg) 
	{
		setDB();
		int cut = msg.indexOf(' ') + 1;
		String message = msg.substring(cut);
		String SQL = "INSERT INTO `kppmessanger`.`chat` (`login`, `msg`) VALUES('"+login+"', '"+message+"')";
		try
		{
			st.executeUpdate(SQL);
		} 
		catch (SQLException e) 
		{
			System.out.println("Error 122! SQL execute problems. Correct it!(SQL) I am in server!");
			e.printStackTrace();
		}
	}

	private static void createGUI() 
	{
		//logins[0] = "nikogdaneyuzainicketot";
		streams = new ArrayList<PrintWriter>(); // ������� ��������� �� "���������" ������
		try 
		{
			ServerSocket serverSock = new ServerSocket(portID); // ����� ����� �������
			while(true) // � ����������� ����� ������� ����������� �������
			{
				Socket acceptSock = serverSock.accept(); // ������� ������
				System.out.println("Got a new client."); // ��������, ��� ������ ������������� � �����
				writer = new PrintWriter(acceptSock.getOutputStream()); // ������ ��� ����� �� ������/������
				streams.add(writer); // ��������� � ���������
				
				System.out.println("Number of Threads: " + streams.size()); // ������ ���-�� �������
				Thread serverThread = new Thread(new Listener(acceptSock, writer)); // ������� ����� �����
				serverThread.start(); // ��� ������� ������� � ����� ��� ���������
			}
		} 
		catch (IOException e)  // ������ �� ������ �����-������
		{
			System.out.println("Error 151! Problems w/ Server Socket!(IO) I am in Server.");
			e.printStackTrace();
		}
		
	}
	
	public static void main(String []args)
	{
		createGUI(); // ��������� ������ � main � ��.
	}

}
