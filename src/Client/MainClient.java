package Client; // Рабоатем в пакете клиента

import java.awt.BasicStroke; // Импорт необходимых библиотек
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.AbstractListModel;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicButtonUI;

public class MainClient extends JFrame {
	// Описание необходимых переменных программы, доступных из любой ее точки (в рамках файла класса и наследников)
	private static Thread serverObtaining;
	private static final long serialVersionUID = 1L;
	public static int portID = 4301;
	private static String clientIP = "127.0.0.1"; // 31.170.160.85
	private JPanel contentPane;
	private static String nickname;
	private static JTextField txtMessage;
	private JButton btnSend;
	private static JLabel logged;
	private static BufferedReader reader;
	private static PrintWriter writer;
	private static  JTextArea txtHistory = new  JTextArea();
	private JMenuBar menuBar;
	private JMenu mnFile;
	private JMenuItem mntmLogOut;
	private JMenuItem mntmExit;
	private JMenu mnHelp;
	private JMenuItem mntmAbout;
	private static boolean closing = false;
	private static JList usersList;
	//final static DefaultListModel listModel = new DefaultListModel();
	private static  List<String> data = new ArrayList<String>();
	private static boolean listCheck = false;
	private JButton btnPrivate;
	private static JTabbedPane privatePane;
	private static List<String> privates = new ArrayList<String>();
	private static JTextArea txtArea;
	private static Connection c;
	private static Statement st;
	
	public static void toList(String nick) // Метод добавление пользователя в список тех, кто онлайн
	{
		try
		{
			for(int i=0; i<data.size(); i++) // Если ник уде используется - создаем исключение и не добавляем
			{
				if(data.get(i).toString().equals(nick)) throw new Exception("Dat nick is already in use!");
			}
			data.add(nick); // Иначе добавим ник в коллекцию-список
	        updateJList(); // И обновим список
		}
		catch(Exception e) // Ловим исключения
		{
			System.out.println("Got it.");
		}
	}
	
	public static void fromList(String nick) // Метод удаления из списка тех, кто ушел в оффлайн
	{
		for(int i=0; i<data.size(); i++)
		{
			if(data.get(i).toString().equals(nick))  // Если находим ник этого юзера
				{
					data.remove(i); // удаляем его из коллекции-списка
					updateJList(); // Обновляем список на клиенте
				}
		}
		
	}
	
	private static void virtualNet() // создания виртуальной сети, чтобы обзатсья с сервером
	{
		try 
		{
			Socket clientSocket = new Socket(clientIP, portID); // Новый сокет на порт 4300
			InputStreamReader is = new InputStreamReader(clientSocket.getInputStream()); // создаем поток ввода
			reader = new BufferedReader(is); // приписываем потоки к переменным (чтение/запись)
			writer = new PrintWriter(clientSocket.getOutputStream());
		}
		catch (IOException e) // Ошибка ввода-вывода, ловим, если есть
		{
			System.out.println("Error 33! Connected w/ virtual Web Connection. I am in Client.");
		}	
	}
	
	private static void setDB() // Коннект к БД. Подробно в файле: KPPServer.java
	{
		String url = "jdbc:mysql://localhost:3306/KPPMessanger";
		String login = "root";
		String pass = "root";
		
		
		try 
		{
			Class.forName("com.mysql.jdbc.Driver");
			c=DriverManager.getConnection(url, login, pass);
			st = c.createStatement();
		} 
		catch (SQLException e) 
		{
			new Error_db();
			System.out.println("Error 148! Magic problems with dedication users message(SQL). I am in client!");
			e.printStackTrace();
		}
		catch (ClassNotFoundException e) 
		{
			System.out.println("Error 42! Magic problems with dedication users message(classNotFound). I am in server!");
			e.printStackTrace();
		}
		
	}
	
	public MainClient(String name) // Конструктор окна. Добавляем иконочку, название
	{
		setIconImage(Toolkit.getDefaultToolkit().getImage(MainClient.class.getResource("/Images/logo.jpg")));
		setTitle("KPPMessanger");
		this.nickname = name; // Говорим, что ник из поля логина - юзер, использующий это конкретное окно клиента
		createWindow(); // Отрисовываем это окно
	}


	private void createWindow() 
	{
		
		try // Говорим,что хотим видеть это окно одинаково на всех устройствах
		{ // Конечно для этого нужно словить кучу исключений при необходимости
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}// В большинстве случаев не ловистя ни одного
		catch (ClassNotFoundException e) 
		{
			e.printStackTrace();
			System.out.println("Error 72! Gwt stacked w/ KPP Intarface (ClassNotFound). I'm in loginscreen!");
		} 
		catch (InstantiationException e) 
		{
			e.printStackTrace();
			System.out.println("Error 77! Gwt stacked w/ KPP Intarface(instatiation). I'm in loginscreen!");
		} 
		catch (IllegalAccessException e) 
		{
			e.printStackTrace();
			System.out.println("Error 82! Gwt stacked w/ KPP Intarface(IllegallAccess). I'm in loginscreen!");
		} 
		catch (UnsupportedLookAndFeelException e)
		{
			e.printStackTrace();
			System.out.println("Error 87! Gwt stacked w/ KPP Intarface(GUI). I'm in loginscreen!");
		}
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Призакрытии окна - закрывает программу
		setSize(600, 480); // Начальный размер (дефолт) задаем такой
		setMinimumSize(new Dimension(585, 450)); // Задаем минимальный размер при масштабировании
		
		menuBar = new JMenuBar(); // Создаем меню
		setJMenuBar(menuBar); // Добавляем на основную панель
		
		mnFile = new JMenu("File"); // Первый пункт назывыаем File
		menuBar.add(mnFile);
		
		setDB(); // Подключаем БД к программе, она ожидает запрсоов теперь
		
		mntmLogOut = new JMenuItem("Chenge account"); // Первый пункт - логаут !!!БЕТА ТЕСТ!!!
		mntmLogOut.addActionListener(new ActionListener() // LOG OUT
		{
			public void actionPerformed(ActionEvent a) 
			{
				txtHistory.setText(""); // Очищаем все поляокна
				txtMessage.setText("");
				writer.println(nickname + ": I have logged out."); // Отправляем сообщение серверу
				writer.println("keySecretKPPMessanger11"); // Секретный ключ, котоырй говорит о том, что пользвоатель уходит в офйлан, дисконнектиться от клиента и сервера.
				writer.flush(); // Перекрываем поток, чтобы сообщение отправилось корректно
				writer.close(); // Закрываем поток ,так как программа закрывается
				try
				{
					reader.close(); // Закрываем поток чтения с сервера
				} 
				catch (IOException e) 
				{
					System.out.println("Error 190! I'm in client. Reader closed incottrect.");
					e.printStackTrace();
				}
				closing = true; // Это для метода, который следит за закрытием окна, а не экземпляра окна, true значит, что мы не вышли, а лишь делаем логаут.
				serverObtaining.interrupt(); // Прерываем поток клиента
				try 
				{
					String SQL = "UPDATE `logins` SET isOnline=0 WHERE nick=\""+nickname+"\"";
					Statement s = c.createStatement(); // Пишем в БД инфу о том, что пользователь более не онлайн
					s.executeUpdate(SQL);
					c.close(); // Закрываем конекшн к БД
				} 
				catch (SQLException e) 
				{
					System.out.println("Error #237! Problems w/ SQL. I'm in main client.");
					e.printStackTrace(); // Ловим возможные ошибки mySQL
				}

				dispose(); // Закрываем экземпляр этого окна
				//txtMessage.requestFocus();
				new Change_account(); // Открываем окно смены аккаунта
			}
		});
		mnFile.add(mntmLogOut); // Добавляем это все на главную панель в меню
		
		mntmExit = new JMenuItem("Exit"); // Создаем пункт меню - выход
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) // EXIT
			{
				writer.println(nickname + ": I have disconnected!"); // Пишем, что пользователь отсоеденился
				writer.println("keySecretKPPMessanger11"); // secret key to delete threads
				writer.flush(); // close it for God
				writer.close(); // Закрываем поток... Все то же , что и выше
				try 
				{
					reader.close(); // Выше то же самое, смотрите комменты там
				} 
				catch (IOException e1) 
				{
					System.out.println("Error 205! I'm in client. Reader didn't close right way.");
					e1.printStackTrace();
				}
				serverObtaining.interrupt();
				try 
				{
					String SQL = "UPDATE `logins` SET isOnline=0 WHERE nick=\""+nickname+"\"";
					Statement s = c.createStatement();
					s.executeUpdate(SQL);
					c.close();
				} 
				catch (SQLException ex) 
				{
					System.out.println("Error #274! Problems w/ SQL. I'm in main client.");
					ex.printStackTrace();
				}
				System.exit(0); // Только на этот раз закрываем программу, а не экземпляр окна.
			}
		});
		mnFile.add(mntmExit); // Добавили его в менюшку.
		
		mnHelp = new JMenu("Help"); // Создаем подраздел Help
		menuBar.add(mnHelp);
		
		mntmAbout = new JMenuItem("About"); // Там пункт "О Приложении"
		mntmAbout.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				new About(); // При нажатии всплывает окно About
			}
		});
		mnHelp.add(mntmAbout); // Добавили в меню
		contentPane = new JPanel(); // Создаем основную панель с отступами 5 пикс по всем сторонам.
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane); // Добавляем ее с лэйаутом GridBagLayout
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{0, 450, 0, 150, 0}; // Массивы для лэйаута по ширине
		gbl_contentPane.rowHeights = new int[]{15, 245, 0, 20, 30, 0, 160, 20}; // И высоте
		// По сути, мы разбиваем окно на сетку и описываем элементы этой сетки, размеры их, чтобы вставлять туда элементы
		gbl_contentPane.columnWeights = new double[]{1.0, 1.0, 1.0, 0.0, 1.0};
		gbl_contentPane.rowWeights = new double[]{1.0, 1.0, 0.0, 0.0, 0.0, 1.0};
		contentPane.setLayout(gbl_contentPane); // Добавляем данный лэйаут
		
		txtHistory.setFont(new Font("MS Reference Sans Serif", Font.PLAIN, 13));
		txtHistory.setLineWrap(true); // Перенос строк включен в окно, где все сообщеньки отображаются
		txtHistory.setWrapStyleWord(true); // Перенос по словам
		txtHistory.setEditable(false); // Нельзя редактировать окно это, только просматривать
		GridBagConstraints gbc_txtHistory = new GridBagConstraints();
		gbc_txtHistory.gridheight = 3; // Добавление элемента при помощи GridBag
		gbc_txtHistory.gridwidth = 3; // Здесь указываем элементы таблицы, которые характеризуют ячейку, куда элемент будет помещен
		gbc_txtHistory.fill = GridBagConstraints.BOTH; // Растяжение элемента на всю ячейку
		gbc_txtHistory.insets = new Insets(5, 0, 5, 5); // Отступы ячейки
		gbc_txtHistory.gridx = 0;
		gbc_txtHistory.gridy = 0;
		JScrollPane scrolltxt = new JScrollPane(); // Добавление скролл бара
        scrolltxt.setWheelScrollingEnabled(true); // Вращение колесиком разрешено
        scrolltxt.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED); // Вертикальный скролл, если надо
        scrolltxt.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER); // Горизонтальный - никогда
        scrolltxt.setViewportView(txtHistory); // Добавляем скролл бар к элементу TextArea, чтобы он там появлялся при необходимости
		contentPane.add(scrolltxt, gbc_txtHistory); // Добавляем элемент+его расположение на основную панель
		
        usersList = new JList(new myListModel()); // Создаем список пользователей онлайн
        GridBagConstraints gbc_list = new GridBagConstraints();
        gbc_list.gridwidth = 2;
        gbc_list.gridheight = 4; // Размещение GridBag, смотри выше для подробной инфы
        gbc_list.insets = new Insets(5, 0, 5, 0);
        gbc_list.fill = GridBagConstraints.BOTH;
        gbc_list.gridx = 3;
        gbc_list.gridy = 0;
        JScrollPane scrolList = new JScrollPane(); // Тоже делаем этому окну скролл бар, как и выше
        scrolList.setWheelScrollingEnabled(true);
        scrolList.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrolList.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrolList.setViewportView(usersList);
		contentPane.add(scrolList, gbc_list); // И добавляем все это дело
       // usersList.setLayoutOrientation(JList.VERTICAL_WRAP);
        //contentPane.add(usersList, gbc_list);
		
        txtMessage = new JTextField(); // То ,куда вводим наши сообщеньки
		txtMessage.setFont(new Font("MS Reference Sans Serif", Font.PLAIN, 13));
		GridBagConstraints gbc_txtMessage = new GridBagConstraints();
		gbc_txtMessage.gridwidth = 3;
		gbc_txtMessage.fill = GridBagConstraints.BOTH;
		gbc_txtMessage.insets = new Insets(0, 0, 5, 5);
		gbc_txtMessage.gridx = 0; // С размещением GridBag
		gbc_txtMessage.gridy = 3;
		contentPane.add(txtMessage, gbc_txtMessage); // И добавляем все это
		txtMessage.setColumns(10);
		
		btnSend = new JButton("Send"); // Кнопка отправления сообщения
		btnSend.setSize(20, 60);
		btnSend.addActionListener(new Listener()); // Где слушатель событий этой кнопки описан ниже
		
		GridBagConstraints gbc_btnSend = new GridBagConstraints();
		gbc_btnSend.gridwidth = 2; // Расположение кнопки при помощи GridBag
		gbc_btnSend.anchor = GridBagConstraints.NORTHWEST;
		gbc_btnSend.insets = new Insets(0, 0, 5, 5);
		gbc_btnSend.gridx = 0;
		gbc_btnSend.gridy = 4;
		gbc_btnSend.ipadx = 30;
		contentPane.add(btnSend, gbc_btnSend); // Добавление е ена экран
		
		logged = new JLabel(); // Добавляем надпись о том, какой пользователь сейчас залогинен
		logged.setFont(new Font("MS Reference Sans Serif", Font.PLAIN, 11));
		logged.setText("<html><i>Logged as: " + nickname + "</i></html>");
		GridBagConstraints gbc_logged = new GridBagConstraints();
		gbc_logged.anchor = GridBagConstraints.NORTHEAST; // Иногда бывает удобно наблюдать, а какой у тебя ник
		gbc_logged.gridwidth = 3; // Чтобы не терятся
		gbc_logged.insets = new Insets(0, 0, 5, 5); // Отступы + размещение при помощи GridBag
		gbc_logged.gridx = 0;
		gbc_logged.gridy = 4;
		gbc_logged.ipadx = 100; // Отступы для GridBag
		gbc_logged.ipady = 7;
		contentPane.add(logged, gbc_logged); // Добавляем элемент на основную панель
		
		
		
		virtualNet(); // Подключаем сеть после отрисовки окна для взаимодейсвтяи с сервером
		// Именно тут сервер нас замечает
		serverObtaining = new Thread(new Obdt()); // Создаем новый поток на клиенте для каждого
		serverObtaining.start(); // уникального клиента и описываем его в методе ниже
		
		contentPane.getRootPane().setDefaultButton(btnSend); // Юиндим Enter на кнопку "Отправить месседж"
		
		btnPrivate = new JButton("Private mode"); // Созаем новую кнопку для ЛС

		GridBagConstraints gbc_btnPrivate = new GridBagConstraints();
		gbc_btnPrivate.gridwidth = 2; // Размещаем кнопку при помощи GridBag
		gbc_btnPrivate.anchor = GridBagConstraints.NORTH;
		gbc_btnPrivate.insets = new Insets(0, 0, 5, 0);
		gbc_btnPrivate.gridx = 3;
		gbc_btnPrivate.gridy = 4;
		contentPane.add(btnPrivate, gbc_btnPrivate); // Добавляем ее на основную панель
		
		privatePane = new JTabbedPane(JTabbedPane.TOP); // Добавляем окно с вкладками для ЛС
		privatePane.setFont(new Font("MS Reference Sans Serif", Font.PLAIN, 13));
		privatePane.setVisible(false); // Сначала его не видно
		GridBagConstraints gbc_privatePane = new GridBagConstraints();
		gbc_privatePane.gridwidth = 3; // Размещаем его при помощи GridBag
		gbc_privatePane.gridheight = 2;
		gbc_privatePane.insets = new Insets(0, 0, 5, 5);
		gbc_privatePane.fill = GridBagConstraints.BOTH;
		gbc_privatePane.gridx = 0;
		gbc_privatePane.gridy = 5;
		contentPane.add(privatePane, gbc_privatePane); // Помещаем его на основную панель

		btnPrivate.addActionListener(new ActionListener() // новый слушатель событий для кнопки ЛС
		{
			public void actionPerformed(ActionEvent ae) 
			{
					try
					{
					Object element = usersList.getSelectedValue(); // Получаем значение выделенного в списке ника
	                String pName = element.toString(); // Берем его значение в переменную pname
		                if(pName.equals(nickname)) // Если юзер выбрал сам себя
		                {
		                	new Info_private(); // Гвоорим, что так делать нехорошо, пусть выберет другого.
		                }
		                else
		                {
		                	boolean iChecker = true; // Иначе ставим нашу галочку в true
		                	for(int i=0; i<privates.size();i++)
		                	{
		                		if(privates.get(i).equals(pName)) // Ищем ник в уже открытых вкладках
		                		{
		                			new Info_private_add();
		                			iChecker = false; // Если он там уже есть, то отменяем галочку
		                			break; // Открываем сообщение о том, что диалог уже открыт и прерываем цикл
		                		}
		                	}
		                	if(iChecker)
		                	{ // Если же диалога еще нет
		                	privates.add(pName); // Добавляем ник юзера в список-коллекцию, что диалог с ним открыт
		                	privatePane.setVisible(true); // Панель отображаем с вкладками
		                	privatePane.addTab(pName, createPane(pName)); // Открываем новую вкладку для диалога
		                	//new Private(pName, nickname);
		                	}
		                }
					}
					catch(RuntimeException e) // Ловим ошибку времени исполнения
					{
						Object element = usersList.getSelectedValue(); // Если таковая есть, то берем значение
		                String pName = element.toString(); // выделенного в списке пользователя (его ник)
						new Error_private(); // Пишем ошибку открытия с ним диалога
						for( int i=0; i<privates.size(); i++)
						{
							if(privates.get(i).equals(pName)) privates.remove(i); // Удаляем ег оиз списка тех, скем диалог открыт
						}
					}
			}
		});
		
		setLocationRelativeTo(null); // помещение окна посередине экрана
		setVisible(true); // Делаем его видимым
		//setLocationByPlatform(true);
		addWindowListener(new WindowAdapter() // Создаем слушатель окна на происходящие события
		{
			public void windowClosing(WindowEvent e) // Действия пр изакрытии окна
			{
				if(!closing) // Если это не лог аут
				{
				writer.println(nickname + ": I have disconnected!"); // пишем сообщение о дисконекте
				writer.println("keySecretKPPMessanger11"); // secret key to delete threads
				writer.flush(); // close it for God
				writer.close(); // Закрываем поток записи при выходе из программы
				try 
				{
					reader.close(); // Закрываем поток чтения сообщений сервера
				} catch (IOException e1) 
				{
					System.out.println("Error 399! I'm in client. Reader closed incorrect!");
					e1.printStackTrace(); // Вывод отчета об ошибках ввода-вывода
				}
				serverObtaining.interrupt(); // Прерываем пользоваетльский поток
				try 
				{
					String SQL = "UPDATE `logins` SET isOnline=0 WHERE nick=\""+nickname+"\"";
					Statement s = c.createStatement(); // Записываем в БД ифно о том, что пользователь более не онлайн
					s.executeUpdate(SQL);
					c.close(); // Закрываем конекшн к БД
				} 
				catch (SQLException exc) 
				{
					System.out.println("Error #237! Problems w/ SQL. I'm in main client.");
					exc.printStackTrace(); // печатаем отчет об ошибке SQL
				}
				System.exit(0); // Выходим их программы
				}
			}
			
			public void windowOpened(WindowEvent e)
			{
				writer.println(nickname+": I have connected!"); // При открытии окна посылаем сообщение на серв
				writer.flush(); // О том, что юзер присоеденился
			}
		});
		//System.out.println(nickname + " " + password);
	}
	
	private static class Obdt implements Runnable // Типа парсер сообщений
	{
		@Override
		public void run() // Обязательный метод run для класса-наследника от интерфйеса Runnable
		{
			String message; // конетйнер для сообщения
			try
			{
				while( (message=reader.readLine()) != null) // Пока сообщение есть, он добавляет его в конец и Enter
				{
						if(message.startsWith("forlist")) // Если сообщение начинается с кодового слова
						{
							String nick = message.substring(7); // Обрезаем эту стркоу, чтобы оставить только ник
							toList(nick); // Добавляем ник в список онлайн юзеров при помощи метода toList
						}
						else
						{
							if(message.startsWith("fromlist")) // Если же с кодового слова такого
							{
								String nick = message.substring(8); // Обрезаем его и отправляем ник	
								fromList(nick); // В метод, который удаляет его из списка онлайн
							}
							else
							{
								if(message.startsWith("prIvaTeMeSsSaGGGe123"+nickname)) // Если же код ЛС + ник наш (как получателя)
								{
									int width = nickname.length();
									String msgPrivate = message.substring(20+width); // Обрезаем кодовое слово + ник наш
									int cut = msgPrivate.indexOf(' ') + 1;
									String login = msgPrivate.substring(0, cut-2); // Получаем логин того, кто отправил
									//System.out.println("LOGIN IS: " + login);
									for(int i=0; i<privates.size(); i++)
									{
										if(privates.get(i).equals(login)) // Если окно с ЛС уже открыто, то
										{
											txtArea.append(msgPrivate + '\n'); // Отображаем его в спец. окне для ЛС
										}
										else
										{ // Чет не пашет, я хз. Вроде все ок, но не. :'(
											privates.add(login); // Иначе, мы открываем это коно
						                	privatePane.setVisible(true); // Делаем все окно с вкладками длс ЛС видимым
						                	privatePane.addTab(login, createPane(login)); // Содаем новую вкладку с ЛС
										}
									}
									
								}
								else
								{
									if(message.startsWith("spEcIalFORRecieveRR5")) // Если мы как отправитель хотим видеть 
									{ // наше сообщение и у себя в окне, то кодовое слово обрезается
										String msgPrivate = message.substring(20);
										txtArea.append(msgPrivate + '\n'); // И сообщение появляется и у нас в окне для ЛС
									}
									else
									{// Если это обычное сообщение, без кодовых слов, то отображаем его в главном окне чата
										txtHistory.append(message + '\n'); 
									}
								}
								
							}
						}
					
				}
			}
			catch(Exception e) // При этом ловим все ошибки возникшие и пишем по ним репорт
			{
				System.out.println("Error 570! Problems w/ server listener. I am in client!");
			}
		}
	}
	
	private static class Listener implements ActionListener // Класс прослушки события
	{
		@Override
		public void actionPerformed(ActionEvent e) // Если событие прошло
		{
			String message;
			if(txtMessage.getText() == null){ message = null; } // И наше сообщение не равно NULL
			else
			{
			message = nickname + ": " + txtMessage.getText(); // Записываем строку, которую нужно отправить
			writer.println(message); // Отправляем сообщение серверу
			writer.flush(); // Перекрываем поток, чтобы сообщение отправилось корректно
			}
			txtMessage.setText(""); // Воле ввода сообщения очищаем
			txtMessage.requestFocus(); // Фокусируемся на нем для возможной отправки след. сообщения
		}
		
	}
	
	 private static void updateJList() // Метод обновления списка онлайн пользователей
	 { // Стырено с форума по ссылке Александра Шевченко
	        ((myListModel) usersList.getModel()).update(); // Метод обновить
	    }
	
	private class myListModel extends AbstractListModel<String> 
	{ // Работает на правах магии
		 
        public void update() {
            fireContentsChanged(this, data.size()-1, 0);
        }
 
        public int getSize() {
            return data.size();
        }
 
        public String getElementAt(int index) {
            return data.get(index);
        }
    }
	
	  static JPanel createPane(String pName) // Метод создания новой вкладки для ЛС
	  {
		  	JPanel panel = new JPanel(); // новая панель с Border лэйаут
			panel.setLayout(new BorderLayout(0, 0));
			
			txtArea = new JTextArea(); // Основное окно отображения сообщений
			txtArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
			txtArea.setEditable(false); // редактирвоать его запрещено, только просматривать
			txtArea.setLineWrap(true); // Перенос строк включен
			txtArea.setWrapStyleWord(true); // Перенос полных слов включен
			JScrollPane scroll = new JScrollPane(); // Добавление скролл бара, как и в основном окне для сообщений
	        scroll.setWheelScrollingEnabled(true);
	        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
	        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
	        scroll.setViewportView(txtArea); // добавляем ег ок нашему окну
			panel.add(scroll, BorderLayout.CENTER); // Добавляем окно со скроллом по центру размещая в BorderLayout
			
			JPanel panel_1 = new JPanel(); // Новая панель и тем же лэйаутом и отступом в 5пикс. сверху
			panel_1.setLayout(new BorderLayout(0, 0));
			panel_1.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
			
			JTextField txtField = new JTextField(); // Поле воода сообщение, размещенное на панели внутренней по центру
			panel_1.add(txtField, BorderLayout.CENTER);
			txtField.setColumns(10);
			
			panel.add(panel_1, BorderLayout.SOUTH);
			
			JButton btnNewButton = new JButton("Send"); // Кнопка отправки сообщения
			btnNewButton.addActionListener(new ActionListener() 
			{
				public void actionPerformed(ActionEvent ae) 
				{
					String message; // Контейнер для строки сообщения
					if(txtField.getText() == null){ message = null; } // Если строка null - пропускаем ее
					else
					{
					message = "spEcialForUser11"+ nickname + "\\" + pName + ": " + txtField.getText(); // Записываем строку, которую нужно отправить с кодом, что жто личное сообщение
					writer.println(message); // Отправляем сообщение серверу
					writer.flush(); // Перекрываем поток, чтобы сообщение отправилось корректно
					}
					txtField.setText(""); // Очищаем поле ввода сообщения
					txtField.requestFocus(); // Оставляем на нем фокус
				}
			});
			panel_1.add(btnNewButton, BorderLayout.EAST); // добавляем кнопку налево в окне
			
		    return panel; // возвращаем собранную панель ЛС в метод сверху. Каждый новый диалог подразумевает создание такого окна.
	  }
	  
	  private class TabButton extends JButton implements ActionListener // НЕ ИСПОЛЬЗУЕТСЯ (создает крестик для закрытия вкладки)
	  {
		 int index = privatePane.getSelectedIndex();
		 
	        public TabButton() 
	        {
	            int size = 17;
	            setPreferredSize(new Dimension(size, size));
	            setToolTipText("Close this tab");
	            //Make the button looks the same for all Laf's
	            setUI(new BasicButtonUI());
	            //Make it transparent
	            setContentAreaFilled(false);
	            //No need to be focusable
	            setFocusable(false);
	            setBorder(BorderFactory.createEtchedBorder());
	            setBorderPainted(false);
	            //Making nice rollover effect
	            //we use the same listener for all buttons
	            addMouseListener(buttonMouseListener);
	            setRolloverEnabled(true);
	            //Close the proper tab by clicking the button
	            addActionListener(this);
	        }
	 
	        public void actionPerformed(ActionEvent e)
	        {
	            if (index != -1) 
	            {
	            	privatePane.remove(index);
	            }
	        }
	 
	        //we don't want to update UI for this button
	        public void updateUI() {
	        }
	 
	        //paint the cross
	        protected void paintComponent(Graphics g) {
	            super.paintComponent(g);
	            Graphics2D g2 = (Graphics2D) g.create();
	            //shift the image for pressed buttons
	            if (getModel().isPressed()) {
	                g2.translate(1, 1);
	            }
	            g2.setStroke(new BasicStroke(2));
	            g2.setColor(Color.BLACK);
	            if (getModel().isRollover()) {
	                g2.setColor(Color.MAGENTA);
	            }
	            int delta = 6;
	            g2.drawLine(delta, delta, getWidth() - delta - 1, getHeight() - delta - 1);
	            g2.drawLine(getWidth() - delta - 1, delta, delta, getHeight() - delta - 1);
	            g2.dispose();
	        }
	    }
	 
	    private final static MouseListener buttonMouseListener = new MouseAdapter() { // НЕ ИСПОЛЬЗУЕТСЯ (метод метода выше)
	        public void mouseEntered(MouseEvent e) {
	            Component component = e.getComponent();
	            if (component instanceof AbstractButton) {
	                AbstractButton button = (AbstractButton) component;
	                button.setBorderPainted(true);
	            }
	        }
	 
	        public void mouseExited(MouseEvent e) {
	            Component component = e.getComponent();
	            if (component instanceof AbstractButton) {
	                AbstractButton button = (AbstractButton) component;
	                button.setBorderPainted(false);
	            }
	        }
	    };
}
