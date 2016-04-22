package Client; // Рабоатем в пакете клиента

import java.awt.EventQueue; // Необходимые библиотечки
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
import javax.swing.border.EmptyBorder;
import java.awt.Toolkit;

public class Register extends JFrame {

	private JPanel contentPane; // Переменные, используемые во всем коде класса
	private JTextField txtLogin;
	private JTextField txtPassword1;
	private JTextField txtPassword2;
	
	private static Connection c;
	private static Statement st;
	
	//Set DB
	private static void setDB() // Задание БД. Подробно в фалйе: KPPServer.java
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
	
	Register() // Конструктор прототипа окна, где добавляем иконку окна, название
	{
		setIconImage(Toolkit.getDefaultToolkit().getImage(Register.class.getResource("/Images/logo.jpg")));
		setTitle("Sign up");
		initUI(); // И запускаем метод отрисовки окна
	}
	
	private void initUI()
	{
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // При закрытии - закрыть прототип окна
		setSize(300, 200); // Фиксированный размер окна
		setLocationRelativeTo(null); // Появление его посередине экрана
		setVisible(true); // Отображение = true
		setResizable(false); // Изменять размер окна нельзя
		contentPane = new JPanel(); // Новая основная панель с отступом 5 пикселей откраев
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null); // Лэйаут ставим абсолют
		
		JLabel lblLogin = new JLabel("Login:"); // Текст ЛОГИН
		lblLogin.setFont(new Font("Walkway Black", Font.PLAIN, 16));
		lblLogin.setBounds(102, 37, 63, 19);
		contentPane.add(lblLogin);
		
		txtLogin = new JTextField(); // Поле ввода логина для регистрации
		txtLogin.setBounds(145, 39, 108, 17);
		contentPane.add(txtLogin);
		txtLogin.setColumns(10);
		
		JLabel lblPassword1 = new JLabel("Password:"); // Текст ПАРОЛЬ
		lblPassword1.setFont(new Font("Walkway Black", Font.PLAIN, 16));
		lblPassword1.setBounds(68, 64, 81, 18);
		contentPane.add(lblPassword1);
		
		txtPassword1 = new JTextField(); // Ввод пароля для регистрации
		txtPassword1.setColumns(10);
		txtPassword1.setBounds(145, 66, 108, 17);
		contentPane.add(txtPassword1);
		
		txtPassword2 = new JTextField(); // Поле2 для ввода пароля (подтверждение)
		txtPassword2.setColumns(10);
		txtPassword2.setBounds(145, 97, 108, 17);
		contentPane.add(txtPassword2);
		
		JLabel lblPassword2 = new JLabel("Confirm password:"); // Текст на подтверждение пароля
		lblPassword2.setFont(new Font("Walkway Black", Font.PLAIN, 16));
		lblPassword2.setBounds(10, 95, 139, 18);
		contentPane.add(lblPassword2);
		
		JButton btnAgree = new JButton("Sign up"); // Кнопка регистрации в системе
		btnAgree.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				setDB(); // Подключаем БД
				String log, pass1, pass2, checker;
				boolean pass = false;
				boolean db = true;
				log = txtLogin.getText(); // Получаем текст с поля ввода логина
				pass1 = txtPassword1.getText(); // Также берем 2 пароля на проверку
				pass2 = txtPassword2.getText();
				
				if(pass1.equals(pass2)) // Если пароли совпадают
				{
					//Checkaem nick v BD
					try 
					{
						ResultSet rs = st.executeQuery("SELECT nick FROM `logins`"); // Запрос в БД
						java.sql.ResultSetMetaData md = rs.getMetaData(); // На все ники системы
						int x = md.getColumnCount();
						
						while(rs.next()) // Пока еще ест ьники - идем дальше
						{
							for(int i=1; i<=x; i++)
							{
								checker = rs.getString(i); // Получаем ники в виде строк и сравниваем
								if(checker.equals(log)) // С тем, чт оввел юхер для регистрации
									{
									db = false; // Если такой уже есть, то перейдем к ошибке
									break;
									}
							}
						}
						
						if(db) // Если ошибки не было - то заносим юзера в БД и говорим, что все ок
						{
							String SQL = "INSERT INTO `kppmessanger`.`logins` (`nick`, `pass`) VALUES('"+log+"', '"+pass1+"')";
							st.executeUpdate(SQL);
							c.close(); // Еще один запрос + закрыть конекшн в конце
							new Info_register(); // Открываем окно, что все ок
							dispose(); // закрываем данный прототип окна
						}
						else
						{
							txtLogin.setText(""); // Если логин уже есть, говорим об этом
							txtLogin.requestFocus(); // Фокус на очищенном поле логина
							new Error_register_log(); // соответствующая ошибка юзеру, чтобы знал
						}
					}
					catch (SQLException ex) // Возможные sql ошибки
					{	
						System.out.println("Error #152! Problems w/ SQL. I'm in log screen.");
						ex.printStackTrace(); // Отчет об ошибке
					}
				}
				else // Если пароли не совпали, тогда очищаем поля, фокус на первом из них
				{
					txtPassword1.setText("");
					txtPassword2.setText("");
					txtPassword1.requestFocus();
					new Error_register_pass(); // Выводим соотвесттвующее сообщение
				}
			}
		});
		
		btnAgree.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnAgree.setBounds(78, 125, 123, 23);
		contentPane.add(btnAgree);
	}
	
public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() { // Запускаем это окно при запуска
			
			public void run() 
			{
				try 
				{
					Register frame = new Register();
					frame.setVisible(true); // Отрисовка и делаем его видимым
				} 
				catch (Exception e) 
				{
					e.printStackTrace(); // Вывод отчета о возможных ошибках
				}
			}
		});
	}
}
