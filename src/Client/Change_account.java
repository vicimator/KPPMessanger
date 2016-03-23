package Client;

import java.awt.EventQueue;
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

public class Change_account extends JFrame {

	private JPanel contentPane;
	private JTextField txtLogin;
	
	private static Connection c;
	private static Statement st;
	private JPasswordField txtPassword;
	
	//Set DB
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
			System.out.println("Error 37! Magic problems with dedication users message(SQL). I am in server!");
			e.printStackTrace();
		}
		catch (ClassNotFoundException e) 
		{
			System.out.println("Error 42! Magic problems with dedication users message(classNotFound). I am in server!");
			e.printStackTrace();
		}
		
	}
	
	Change_account() 
	{
		setTitle("Log in");
		initUI();
	}
	
	private void initUI()
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
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(280, 180);
		setLocationRelativeTo(null);
		setVisible(true);
		setResizable(false);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblLogin = new JLabel("Login:");
		lblLogin.setFont(new Font("Walkway Black", Font.PLAIN, 16));
		lblLogin.setBounds(65, 40, 40, 20);
		contentPane.add(lblLogin);
		
		txtLogin = new JTextField();
		txtLogin.setBounds(110, 40, 110, 20);
		contentPane.add(txtLogin);
		txtLogin.setColumns(10);
		
		JLabel lblPassword1 = new JLabel("Password:");
		lblPassword1.setFont(new Font("Walkway Black", Font.PLAIN, 16));
		lblPassword1.setBounds(31, 80, 75, 20);
		contentPane.add(lblPassword1);
		
		JButton loginButton = new JButton("Log in");
		loginButton.addActionListener(new ActionListener()
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
		
		loginButton.setFont(new Font("Tahoma", Font.PLAIN, 12));
		loginButton.setBounds(90, 118, 100, 25);
		contentPane.add(loginButton);
		
		txtPassword = new JPasswordField();
		txtPassword.setBounds(110, 80, 110, 20);
		contentPane.add(txtPassword);
		contentPane.getRootPane().setDefaultButton(loginButton);
	}

	//Checking log & pass	
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
			System.out.println("Error #184! Problems w/ SQL. I'm in log screen.");
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
			
			public void run() 
			{
				try 
				{
					Change_account frame = new Change_account();
					frame.setVisible(true);
				} 
				catch (Exception e) 
				{
					e.printStackTrace();
				}
			}
		});
	}
}
