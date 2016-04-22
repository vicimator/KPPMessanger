package Client; // Работаем в пакете клиента

import java.awt.EventQueue; // Юиюлиотечки
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.JPasswordField;

import java.awt.Toolkit;

public class Change_account extends JFrame {

	private JPanel contentPane; // Объявляем переменные, чтобы те были доступны из любой точки проги
	private JTextField txtLogin;
	
	private static Connection c;
	private static Statement st;
	private JPasswordField txtPassword;
	private static int checks = 0;
	
	//Set DB
	private static void setDB() // Задание метода коннекта к БД, расписано в KPPServer.java
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
			System.out.println("Error 37! Magic problems with dedication users message(SQL). I am in server!");
			e.printStackTrace();
		}
		catch (ClassNotFoundException e) 
		{
			System.out.println("Error 42! Magic problems with dedication users message(classNotFound). I am in server!");
			e.printStackTrace();
		}
		
	}
	
	Change_account() // Конструктор, где добавляем иконку приложения, название и метод отрисовки GUI
	{
		setIconImage(Toolkit.getDefaultToolkit().getImage(Change_account.class.getResource("/Images/logo.jpg")));
		setTitle("Log in");
		initUI();
	}
	
	private void initUI()
	{
		
		try // Отображаем окно так, чтоыб на всех платформах оно выглядело одинаково
		{ // И конечно ловим кучу ошибок, чтоыб прога не понтовалась
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} // Но они в редких случаях на норм машинах выскочят
		catch (ClassNotFoundException e) 
		{
			e.printStackTrace();
			System.out.println("Error 79! Gwt stacked w/ KPP Intarface (ClassNotFound). I'm in loginscreen!");
		} 
		catch (InstantiationException e) 
		{
			e.printStackTrace();
			System.out.println("Error 84! Gwt stacked w/ KPP Intarface(instatiation). I'm in loginscreen!");
		} 
		catch (IllegalAccessException e) 
		{
			e.printStackTrace();
			System.out.println("Error 89! Gwt stacked w/ KPP Intarface(IllegallAccess). I'm in loginscreen!");
		} 
		catch (UnsupportedLookAndFeelException e)
		{
			e.printStackTrace();
			System.out.println("Error 94! Gwt stacked w/ KPP Intarface(GUI). I'm in loginscreen!");
		}
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Закрыть активный прототип окна при выходе
		setSize(280, 180); // Фиксированный размер окна
		setLocationRelativeTo(null); // Появлятся будет всегда посередине экрана
		setVisible(true); // Отображение ставим true
		setResizable(false); // Фиксированный размер, изменить нельзя
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane); // Задаем новую основную панель и ставим ее лэйаут как абсолют
		contentPane.setLayout(null); // То есть элементы распологаются заданием координат окна
		
		JLabel lblLogin = new JLabel("Login:"); // Надпись логин
		lblLogin.setFont(new Font("Walkway Black", Font.PLAIN, 16));
		lblLogin.setBounds(65, 40, 40, 20); // Ее расположение и атрибуты
		contentPane.add(lblLogin);
		
		txtLogin = new JTextField(); // Место, где вводить логин
		txtLogin.setBounds(110, 40, 110, 20);
		contentPane.add(txtLogin);
		txtLogin.setColumns(10);
		
		JLabel lblPassword1 = new JLabel("Password:"); // Надпись пасс
		lblPassword1.setFont(new Font("Walkway Black", Font.PLAIN, 16));
		lblPassword1.setBounds(31, 80, 75, 20); // С добавлением на основную панель и указания координат
		contentPane.add(lblPassword1);
		
		JButton loginButton = new JButton("Log in"); // Кнопка залогиниться
		loginButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent a) 
			{	
				String nickname = txtLogin.getText(); // Берем текст из поля ввода логина
				@SuppressWarnings("deprecation") // И из поля пассаЮ однако тут через этот метод
				String password = txtPassword.getText(); // Потому что поле скрыто по дефолту
				 // Вместо символов пароля ***
				check(nickname, password); // Проверяем на совпадение их с БД
				//login(nickname);
			}
		});
		
		loginButton.setFont(new Font("Tahoma", Font.PLAIN, 12));
		loginButton.setBounds(90, 118, 100, 25); // Добавляем кнопку логина и указываем ее параметры
		contentPane.add(loginButton);
		
		txtPassword = new JPasswordField();
		txtPassword.setBounds(110, 80, 110, 20);
		contentPane.add(txtPassword);
		contentPane.getRootPane().setDefaultButton(loginButton); // Кнопка логина прибинжена на Enter
	}

	//Checking log & pass	
private void check(String nickname, String password)
	{
		String checker; // переменные для проверки логина и пасса и активности пользователя и БД
		boolean check_log = false;
		boolean check_pass = false;
		boolean check_online = false;
		setDB(); // Подключаемся к БД
		
		try 
		{
			ResultSet rs = st.executeQuery("SELECT nick FROM `logins`");
			java.sql.ResultSetMetaData md = rs.getMetaData(); // Получаем ланные из таблицы Логинов
			int x = md.getColumnCount(); // а именно ники всех зарегестрированных
			
			while(rs.next()) // Пока есть ники - чекаем их
			{
				for(int i=1; i<=x; i++)
				{
					checker = rs.getString(i); // Получаем строки из запроса
					if(checker.equals(nickname)) // Если равно нику, введенному пользователем в поле ЛОГИН
						{
						check_log = true; // Тогда true
						break; // И сразу идем дальше
						}
				}
			}
			
			rs = st.executeQuery("SELECT pass FROM `logins` WHERE logins.nick=\""+nickname+"\"");
			
			md = rs.getMetaData(); // Теперь похожий запрос, но мы хотим пароль пользвоателя
			x = md.getColumnCount(); // Логин которого совпал с тем, что ввел юхер в поле при попытке залогинится
			
			while(rs.next()) // Пока есть пароли
			{
					checker = rs.getString(x); // Берем их и парсим в строку
					if(checker.equals(password)) check_pass = true; // Если пароли совпали, то true
			}
			
			rs = st.executeQuery("SELECT isOnline FROM `logins` WHERE logins.nick=\""+nickname+"\"");
			
			md = rs.getMetaData(); // Теперь проверяем онлайн ли уже этот юзер
			x = md.getColumnCount(); // Если онлайн, то 1, иначе 0.
			
			while(rs.next()) // Пока есть логины для првоерки
			{
					checks = rs.getInt(x); // Получаем числа пользователей
					if(checks == 1) check_online = true; // Если он уже онлайн, то true
			}
		//c.close();	
		}
		catch (SQLException e) // Возможные ошибки БД
		{
			System.out.println("Error #184! Problems w/ SQL. I'm in log screen.");
			e.printStackTrace();
		}	
		// DESICION
		if(check_log && check_pass) // Если логин совпал и пароль верный
		{
			if(check_online) // Если пользователь уже в сети
			{
				txtPassword.setText(""); // Сбрасываем все введенные данные
				txtLogin.setText("");
				txtLogin.requestFocus(); // Фокус на окне ввода логина
				new Info_online(); // Инфо о том, что юзер с таким ником уже есть в сети
			}
			else
			{ // Иначе
			try 
			{
				checks = 1; // Ставим int переменную в 1
				String SQL = "UPDATE `logins` SET isOnline=1 WHERE nick=\""+nickname+"\"";
				Statement s=c.createStatement(); // Записываем ее в БД
				s.executeUpdate(SQL); // Мол пользователь вошел в онлайн
				c.close(); // Закрываем конекшн
				checks = 0; // Ставим переменную в ноль, на всякий
				login(nickname); // Логиним пользователя, все ок
			} 
			catch (SQLException e) // Возможные ошибки БД
			{
				System.out.println("Error #255! Problems w/ SQL. I'm in log screen.");
				e.printStackTrace();
			}
			}
		}
		else
		{
			txtPassword.setText(""); // Иначе, если шото не совпало, логин или пасс
			txtPassword.requestFocus(); // Скорей всего это пасс, так как логин найдется инфа 100
			new Error_login(); // Мы стираем написанный ранее пасс, фокусируемся на поле ввода пасса
		} // И выдаем окно с ошибкой, чтобы юхер не пугался и попробовал еще раз
}

private void login(String name) // Working w/ ActionListener on Log in button.
{
		dispose(); // Если логинимся, то это окно закрывается
		new MainClient(name); // И открывается окно основного клиента с новым ником
}
	
public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			
			public void run() 
			{
				try 
				{
					Change_account frame = new Change_account(); // Создаем фрейм (прототип окна)
					frame.setVisible(true); // Делаем его видимым
				} 
				catch (Exception e) 
				{
					e.printStackTrace(); // Отчеты о возможных ошибках всех типов
				}
			}
		});
	}
}
