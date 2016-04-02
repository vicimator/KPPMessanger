package Client;

import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import java.awt.Toolkit;


public class LoginScreen extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private static JTextField txtLogin;
	private static JPasswordField txtPassword;
	private static boolean checker = false;

	private static Connection c;
	private static Statement st;
	final static ImageIcon longIcon = new ImageIcon("icon.png");
	// Устанавливает конекшн к БД
private static void setDB() 
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
		txtLogin.setText("");
		txtPassword.setText("");
		txtPassword.requestFocus();
		new Error_db();
		System.out.println("Error 37! Magic problems with dedication users message(SQL). I am in server!");
		e.printStackTrace();
	}
	catch (ClassNotFoundException e) 
	{
		System.out.println("Error 42! Magic problems with dedication users message(classNotFound). I am in server!");
		e.printStackTrace();
	}
	
}
	
LoginScreen()
{
	setIconImage(Toolkit.getDefaultToolkit().getImage(LoginScreen.class.getResource("/Images/logo.jpg")));
		initGUI();	
}

private void initGUI()
{	
	
	try 
	{
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	}
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

	setTitle("KPPMessanger");
	setResizable(false);
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	setSize(300, 350);
	contentPane = new JPanel();
	contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
	setContentPane(contentPane);
	contentPane.setLayout(null);
	setLocationRelativeTo(null);
	
	txtLogin = new JTextField();
	txtLogin.setBounds(90, 80, 120, 25); // Login
	txtLogin.setColumns(10);
	
	JLabel lblLogin = new JLabel("Login");
	lblLogin.setFont(new Font("Walkway Black", Font.PLAIN, 20)); // Login text
	lblLogin.setBounds(90, 47, 50, 25);
	
	JLabel lblPassword = new JLabel("Password");
	lblPassword.setFont(new Font("Walkway Black", Font.PLAIN, 20));	// Pass text
	lblPassword.setBounds(90, 127, 90, 25);	
	
	JButton loginButton = new JButton("Log in");
	loginButton.setFont(new Font("Tahoma", Font.PLAIN, 13));
	loginButton.addActionListener(new ActionListener() // Log in button
	{	
		public void actionPerformed(ActionEvent a) 
		{	
			String nickname = txtLogin.getText();
			@SuppressWarnings("deprecation")
			String password = txtPassword.getText();
			
			check(nickname, password);
			//login(nickname);
		}
	});

	loginButton.setBounds(100, 200, 100, 30);
	
	contentPane.add(lblPassword);
	contentPane.add(lblLogin);
	contentPane.add(txtLogin);
	contentPane.add(loginButton);
	
	contentPane.getRootPane().setDefaultButton(loginButton);
	
	txtPassword = new JPasswordField();
	txtPassword.setBounds(90, 160, 120, 25);
	contentPane.add(txtPassword);
	
	JLabel regLabel = new JLabel();
	regLabel.setText("<html><u>Sign up</u></html>");
	regLabel.addMouseListener(new MouseAdapter() {
		
		@Override
		public void mouseClicked(MouseEvent ev) 
		{
			new Register();
		}
		
		@Override
		public void mouseEntered(MouseEvent e) 
		{
			regLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		}
	});
	regLabel.setFont(new Font("Vani", Font.PLAIN, 17));
	regLabel.setBounds(120, 250, 60, 30);
	contentPane.add(regLabel);
	JLabel lblAuthor = new JLabel("Copyright Vindict Team \u00A9 2016");
	lblAuthor.setBounds(75, 297, 150, 14);
	contentPane.add(lblAuthor);
}
	
	//Чекает данные для логина
private void check(String nickname, String password)
	{
		String checker;
		boolean check_log = false;
		boolean check_pass = false;
		setDB();
		
		try 
		{
			ResultSet rs = st.executeQuery("SELECT nick FROM `logins`");
			java.sql.ResultSetMetaData md = rs.getMetaData();
			int x = md.getColumnCount();
			
			while(rs.next())
			{
				for(int i=1; i<=x; i++)
				{
					checker = rs.getString(i);
					if(checker.equals(nickname)) 
						{
						check_log = true;
						break;
						}
				}
			}
			
			rs = st.executeQuery("SELECT pass FROM `logins` WHERE logins.nick=\""+nickname+"\"");
			
			md = rs.getMetaData();
			x = md.getColumnCount();
			
			while(rs.next())
			{
					checker = rs.getString(x);
					if(checker.equals(password)) check_pass = true;
			}
			
		c.close();	
		}
		catch (SQLException e) 
		{
			System.out.println("Error #175! Problems w/ SQL. I'm in log screen.");
			e.printStackTrace();
		}	
		// DESICION
		if(check_log && check_pass)
		{
			login(nickname);
		}
		else
		{
			txtPassword.setText("");
			txtPassword.requestFocus();
			new Error_login();
		}
	}


	private void login(String name) // Working w/ ActionListener on Log in button.
	{
			dispose();
			new MainClient(name);
	}
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginScreen frame = new LoginScreen();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("Error 75! Problems w/ Loin frame. I'm in LoginScreen!");
				}
			}
		});
	}
}
