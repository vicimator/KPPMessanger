package Server; // Работаем в пакете сервер

import java.io.BufferedReader; // Импорт необходимых библиотек
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

public class KPPServer // Констурктор Сервера
{
	public static int portID = 4301; // Задаем порт, который сер будет слушать
	
	private static ArrayList streams; // Создаем коллекцию, где храним потоки всех юзеров, которые конектились к серву
	private static Connection c; // Переменная связи для БД
	private static Statement st; // Переменная для БД, где пишется запрос
	private static PrintWriter writer; // Поток записи 
	private static ArrayList<String> list = new ArrayList<String>(); // Коллекция-список юхеров онлайн
	private static int number = 0;
	
//Коннектиться к БД 
private static void setDB() 
{
	String url = "jdbc:mysql://localhost:3306/KPPMessanger"; // Адрес БД + порт, который она слушает
	String login = "root";
	String pass = "root";
	
	
	try 
	{
		Class.forName("com.mysql.jdbc.Driver"); // Загружаем драйвер
		c=DriverManager.getConnection(url, login, pass); // Переменная коннект присоеденилась к БД
		st = c.createStatement(); // Стэйтмент готова принимать запросы
	} 
	catch (SQLException e)  // Возможные ошибки коннекта к БД
	{
		System.out.println("Error 37! Magic problems with dedication users message(SQL). I am in server!");
		e.printStackTrace(); // Полный отчет об ошибке
	}
	catch (ClassNotFoundException e) 
	{
		System.out.println("Error 42! Magic problems with dedication users message(classNotFound). I am in server!");
		e.printStackTrace(); // Эти ошибки выводить пользователю нет нужды
	}
	
}
	
	private static class Listener implements Runnable // Создаем доп. класс слушателя потоков
	{
		BufferedReader reader; // Будет считывать инфу с потоков
		PrintWriter pw; // Записывать инфу в потоки
		
		Listener(Socket sock, PrintWriter pw) // Конструктор на сокет сервера + поток записи
		{
			this.pw = pw;
				try
				{
					InputStreamReader is = new InputStreamReader(sock.getInputStream());
					reader = new BufferedReader(is); // Входной поток присваеваем локальному читателю сообщенек
				} 
				catch (IOException e) 
				{
					System.out.println("Error 62! Magic problems with dedication users message(IO). I am in server!");
				}
		}
		@Override
		public void run() // Обязательный метод run для класса, который наследуется от интерфейса Runnable
		{
			try 
			{
				String message; // Сюда будем записывать входящие месседжи	
				while( (message=reader.readLine()) != null )
					{
						if(message.equals("keySecretKPPMessanger11")) // Если msg = секретному ключу
						{
							streams.remove(pw); // удаляем райтер при закрытии окна пользователем
							streams.trimToSize(); // Подстраиваем размеры коллекции, чтобы удаление прошло успешно
						}
						else
						{
							if(message.startsWith("spEcialForUser11")) // Если такой ключ, то это ЛС
							{
								System.out.println("Private: " + message); // Выводим на серве ,мол приват
								spreadPrivate(message); // Метод, рассылающий приват сообщеньки
							}
							else
							{
							System.out.println(message);
							spreadInChat(message); // Метод, рассылающий всем сообщеньки
							}
						}
					}
			} 
			catch (IOException e)  // Ошибка ввода-выода
			{
				System.out.println("Error 95! Magic problems with dedication users message(IO). I am in server!");
			}
		}
	}

	private static void spreadInChat(String msg)
	{
		int cut = msg.indexOf(' ') + 1;
		String login = msg.substring(0, cut-2); // В сообщении отделяем контент от логина
		String text = msg.substring(cut);
		String hello = login + ": I have connected!"; // Проверяем на ключевые фразы
		String exit = login + ": I have disconnected!";
		String logout = login + ": I have logged out.";
		//save(login,msg);
		
		java.util.Iterator<PrintWriter> iter = streams.iterator(); // Перебираем всех пользователей итерацией
		
		while(iter.hasNext()) // Пока есть еще - делаем
		{
			try
			{
				if(msg.equals(hello)) // Если приветсвие, то добавляем пользоваетля в список пользователей
				{
					list.add(login); // На серве
					writer.println("forlist" + list.get( list.size()-1 ));
					writer.flush();
					for(int i=0; i<list.size()-1; i++)
					{
						writer.println("forlist" + list.get( i )); // В клиентах всех
						writer.flush(); // Перекрываем поток, обязательно, иначе будет бесконечное прослушивание
					}
	
				}
				
				if(msg.equals(exit)) // Если прощаение, то удаляем пользователя из списков
				{
					for(int i=0; i<list.size(); i++)
					{
						if(list.get(i).equals(login))
						{
							writer.println("fromlist" + list.get( i )); // На клиентах
							writer.flush();
						}
					}
					list.remove(login); // На серве
					list.trimToSize(); // Сжимаем коллекцию до нужных размеров после удаления
				}
				
				if(msg.equals(logout)) // Если смена акка, то тоже удалям пользователя отовсюду
				{
					for(int i=0; i<list.size(); i++)
					{
						if(list.get(i).equals(login))
						{
							writer.println("fromlist" + list.get( i )); // с клиентов
							writer.flush();
						}
					}
					list.remove(login); // С серва
					list.trimToSize(); // Сжимаем коллекцию для корректного удаления
				}
				
				
				if(!text.equals("")) // Если сообщение не пустое и нету кодовых слов
				{

					writer = iter.next(); // Перебираем всех пользователей
					//writer.println("forlist" + list.get());
					writer.println(msg); // И всем сообщеньку кидаем
					writer.flush();
				}
				else
				{
					writer = iter.next(); // Если сообщение пустое - не передаем ничего, идем дальше
					writer.flush();
				}
			}
			catch(Exception e)
			{
				System.out.println("Error 182! Iterator is dumb. Correct it! I am in server!");
			}
		}
	}
	
	private static void spreadPrivate(String msg) // пересылка ЛС
	{
		String log = msg.substring(16); // Убираем ключевое слово-код, чтобы получить месседж
		int cut = log.indexOf('\\') + 1 ;
		String sender = log.substring(0, cut-1); // Получаем отправителя
		log = log.substring(cut);
		cut = log.indexOf(' ') + 1 ;
		String reciever = log.substring(0, cut-2); // Получаем получателя
		log = log.substring(cut); // Получаем чистый месседж -- работа со строками проведена
		
		java.util.Iterator<PrintWriter> iter = streams.iterator(); 
		
		while(iter.hasNext()) // Пока есть получатели
		{
			try
			{
					if(list.get(number).equals(reciever)) // Если это получатель
					{
						writer = iter.next(); // Идем дольше по списку
						number++; // Прибавляем номер (нужно, чтобы перебрать всех в списке и отправить нужным юзерам)
						writer.println("prIvaTeMeSsSaGGGe123"+ reciever + sender + ": " + log); // меседж 
						writer.flush(); // С кодовым словом для получаетля, парсинг на стороне клиента
					}
					else
					{
						if(list.get(number).equals(sender)) // Если это отправитель
						{
							writer = iter.next();
							number++;
							writer.println("spEcIalFORRecieveRR5" + sender + ": " + log); // Код отправителя
							writer.flush(); // Парсинг на стороне клиента, но суть в том, что мы отображаем месседж
						}
						else
						{
							writer = iter.next(); // Иначе просто идем дальше по списку
							number++; // Пока не найдем совпадения
							writer.flush(); // Обязательно закрывая поток
						}
					}
			}
			catch(Exception ex)
			{
				System.out.println("Error 230! Iterator is dumb. Correct it! I am in server!-privates");
			}
		}
		number = 0; // Сбрасываем счетчик
	}
	
	//Сохраняет переписку в БД -- НЕ ИСПОЛЬЗУЕТСЯ С ВЕРСИИ 0.2 (Работает, мб в будущем будем фичей)
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
		streams = new ArrayList<PrintWriter>(); // создаем коллекцию из "читателей" потока
		try 
		{
			ServerSocket serverSock = new ServerSocket(portID); // Новый сокет сервера
			while(true) // В бесконечном цикле ожидаем подключения клиента
			{
				Socket acceptSock = serverSock.accept(); // Приняли одного
				System.out.println("Got a new client."); // Сообщаем, что клиент присоеденился к серву
				writer = new PrintWriter(acceptSock.getOutputStream()); // Ставим его поток на чтение/запись
				streams.add(writer); // Добавляем в коллекцию
				
				System.out.println("Number of Threads: " + streams.size()); // Чекаем кол-во потоков
				Thread serverThread = new Thread(new Listener(acceptSock, writer)); // Создаем новый поток
				serverThread.start(); // Для каждого клиента и сразу его запускаем
			}
		} 
		catch (IOException e)  // Чекаем на ошибки ввода-вывода
		{
			System.out.println("Error 151! Problems w/ Server Socket!(IO) I am in Server.");
			e.printStackTrace();
		}
		
	}
	
	public static void main(String []args)
	{
		createGUI(); // Запускаем сервер в main и всё.
	}

}
