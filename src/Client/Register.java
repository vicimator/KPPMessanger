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
import javax.swing.border.EmptyBorder;

public class Register extends JFrame {

	private JPanel contentPane;
	private JTextField txtLogin;
	private JTextField txtPassword1;
	private JTextField txtPassword2;
	
	private static Connection c;
	private static Statement st;
	
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
	
	Register() 
	{
		setTitle("Sign up");
		initUI();
	}
	
	private void initUI()
	{
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(300, 200);
		setLocationRelativeTo(null);
		setVisible(true);
		setResizable(false);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblLogin = new JLabel("Login:");
		lblLogin.setFont(new Font("Walkway Black", Font.PLAIN, 16));
		lblLogin.setBounds(102, 37, 63, 19);
		contentPane.add(lblLogin);
		
		txtLogin = new JTextField();
		txtLogin.setBounds(145, 39, 108, 17);
		contentPane.add(txtLogin);
		txtLogin.setColumns(10);
		
		JLabel lblPassword1 = new JLabel("Password:");
		lblPassword1.setFont(new Font("Walkway Black", Font.PLAIN, 16));
		lblPassword1.setBounds(68, 64, 81, 18);
		contentPane.add(lblPassword1);
		
		txtPassword1 = new JTextField();
		txtPassword1.setColumns(10);
		txtPassword1.setBounds(145, 66, 108, 17);
		contentPane.add(txtPassword1);
		
		txtPassword2 = new JTextField();
		txtPassword2.setColumns(10);
		txtPassword2.setBounds(145, 97, 108, 17);
		contentPane.add(txtPassword2);
		
		JLabel lblPassword2 = new JLabel("Confirm password:");
		lblPassword2.setFont(new Font("Walkway Black", Font.PLAIN, 16));
		lblPassword2.setBounds(10, 95, 139, 18);
		contentPane.add(lblPassword2);
		
		JButton btnAgree = new JButton("Sign up");
		btnAgree.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				setDB();
				String log, pass1, pass2, checker;
				boolean pass = false;
				boolean db = true;
				log = txtLogin.getText();
				pass1 = txtPassword1.getText();
				pass2 = txtPassword2.getText();
				
				if(pass1.equals(pass2))
				{
					//Checkaem
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
								if(checker.equals(log)) 
									{
									db = false;
									break;
									}
							}
						}
						
						if(db)
						{
							String SQL = "INSERT INTO `kppmessanger`.`logins` (`nick`, `pass`) VALUES('"+log+"', '"+pass1+"')";
							st.executeUpdate(SQL);
							c.close();
							dispose();
						}
						else
						{
							txtLogin.setText("");
							txtLogin.requestFocus();
							new Error_register_log();
						}
					}
					catch (SQLException ex) 
					{
						System.out.println("Error #152! Problems w/ SQL. I'm in log screen.");
						ex.printStackTrace();
					}
				}
				else
				{
					txtPassword1.setText("");
					txtPassword2.setText("");
					txtPassword1.requestFocus();
					new Error_register_pass();
				}
			}
		});
		
		btnAgree.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnAgree.setBounds(78, 125, 123, 23);
		contentPane.add(btnAgree);
	}
	
public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			
			public void run() 
			{
				try 
				{
					Register frame = new Register();
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
