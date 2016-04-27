package Client;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

public class Private extends JFrame 
{

	private JPanel contentPane;
	private static JTextField txtMsg;
	private static JTextArea txtHistory;
	private static BufferedReader reader;
	private static PrintWriter writer;
	public static int portID = 4301;
	private static String clientIP = "127.0.0.1"; // 31.170.160.85
	private static String sender = "", reciever = "";
	
	private static void virtualNet()
	{
		try 
		{
			Socket clientSocket = new Socket(clientIP, portID); // Новый сокет на порт 4300
			InputStreamReader is = new InputStreamReader(clientSocket.getInputStream()); // создаем поток
			reader = new BufferedReader(is); // приписываем потоки к переменным
			writer = new PrintWriter(clientSocket.getOutputStream());
		}
		catch (IOException e)
		{
			System.out.println("Error 51! Connected w/ virtual Web Connection. I am in Client.");
		}	
	}
		
	public Private(String pname, String selfName) 
	{
		sender = selfName;
		reciever = pname;
		setIconImage(Toolkit.getDefaultToolkit().getImage(MainClient.class.getResource("/Images/logo.jpg")));
		setTitle("Private message to " + pname);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(450, 300);
		setLocationRelativeTo(null);
		setMinimumSize(new Dimension(390, 250));
		setVisible(true);
		
		virtualNet();
		
		final Thread serverOb = new Thread(new Obdt());
		serverOb.start();
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 300, 130, 20};
		gridBagLayout.rowHeights = new int[]{0,170,15,100, 15};
		gridBagLayout.columnWeights = new double[]{1.0, 1.0};
		gridBagLayout.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		getContentPane().setLayout(gridBagLayout);
		
		txtHistory = new JTextArea();
		txtHistory.setFont(new Font("MS Reference Sans Serif", Font.PLAIN, 13));
		txtHistory.setEditable(false);
		GridBagConstraints gbc_txtHistory = new GridBagConstraints();
		gbc_txtHistory.gridy = 0;
		gbc_txtHistory.gridx = 0;
		gbc_txtHistory.insets = new Insets(0, 0, 10, 0);
		gbc_txtHistory.gridwidth = 3;
		gbc_txtHistory.gridheight = 2;
		gbc_txtHistory.fill = GridBagConstraints.BOTH;
		JScrollPane scrolltxt = new JScrollPane();
        scrolltxt.setWheelScrollingEnabled(true);
        scrolltxt.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrolltxt.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrolltxt.setViewportView(txtHistory);
		getContentPane().add(scrolltxt, gbc_txtHistory);
		
		txtMsg = new JTextField();
		GridBagConstraints gbc_txtMsg = new GridBagConstraints();
		gbc_txtMsg.gridwidth = 2;
		gbc_txtMsg.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtMsg.insets = new Insets(0, 0, 5, 5);
		gbc_txtMsg.gridx = 0;
		gbc_txtMsg.gridy = 2;
		getContentPane().add(txtMsg, gbc_txtMsg);
		txtMsg.setColumns(10);
		
		JButton btnSend = new JButton("Send");
		contentPane.getRootPane().setDefaultButton(btnSend);
		btnSend.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) 
			{
				String message;
				if(txtMsg.getText() == null){ message = null; }
				else
				{
				message = "spEcialForUser11"+ sender + "\\" + reciever + ": " + txtMsg.getText(); // Записываем строку, которую нужно отправить
				writer.println(message); // Отправляем сообщение серверу
				writer.flush(); // Перекрываем поток, чтобы сообщение отправилось корректно
				}
				txtMsg.setText("");
				txtMsg.requestFocus();
			}
		});
		
		GridBagConstraints gbc_btnSend = new GridBagConstraints();
		gbc_btnSend.fill = GridBagConstraints.BOTH;
		gbc_btnSend.insets = new Insets(0, 5, 5, 0);
		gbc_btnSend.gridx = 2;
		gbc_btnSend.gridy = 2;
		gbc_btnSend.ipadx = 65;
		getContentPane().add(btnSend, gbc_btnSend);
		
		JLabel login = new JLabel();
		login.setFont(new Font("MS Reference Sans Serif", Font.PLAIN, 11));
		login.setText("<html><i>You logged as: " + selfName + "</i></html>");
		GridBagConstraints gbc_login = new GridBagConstraints();
		gbc_login.gridwidth = 3;
		gbc_login.fill = GridBagConstraints.HORIZONTAL;
		gbc_login.anchor = GridBagConstraints.NORTH;
		gbc_login.insets = new Insets(0, 0, 5, 5);
		gbc_login.gridx = 0;
		gbc_login.gridy = 3;
		getContentPane().add(login, gbc_login);
		
		addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{

				writer.flush(); // close it for God
				writer.close();
				try 
				{
					reader.close();
				} catch (IOException e1) 
				{
					System.out.println("Error 157! I'm in client(private). Reader closed incorrect!");
					e1.printStackTrace();
				}
				serverOb.interrupt();
			}
		});
	}
	
	private static class Obdt implements Runnable
	{
		@Override
		public void run()
		{
			String message;
			try
			{
				while( (message=reader.readLine()) != null) // Пока сообщение есть, он добавляет его в конец и Enter
				{
					if(message.startsWith("prIvaTeMeSsSaGGGe123"))
					{
						String msgPrivate = message.substring(20);
						txtHistory.append(msgPrivate + '\n');
					}
				}
			}
			catch(Exception e)
			{
				System.out.println("Error 175! Problems w/ server listener. I am in client(private)!");
			}
		}
	}
}
