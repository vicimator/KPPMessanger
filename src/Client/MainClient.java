package Client;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import java.awt.Toolkit;

public class MainClient extends JFrame {
	
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
	final static DefaultListModel listModel = new DefaultListModel();
	private static ArrayList<String> list = new ArrayList<String>();
	private static boolean listCheck = false;
	private JButton btnPrivate;
	
	public static void toList(String nick)
	{
		try
		{
			for(int i=0; i<listModel.size(); i++)
			{
				if(listModel.getElementAt(i).toString().equals(nick)) throw new Exception("Dat nick is already in use!");
			}
	
	        listModel.addElement(nick);
	        int index = listModel.size() - 1;
	        usersList.setSelectedIndex(index);
	        usersList.ensureIndexIsVisible(index);
		}
		catch(Exception e)
		{
			System.out.println("Got it.");
		}
	}
	
	public static void fromList(String nick)
	{
		for(int i=0; i<listModel.size(); i++)
		{
			if(listModel.getElementAt(i).toString().equals(nick)) 
				{
					listModel.removeElementAt(i);
					break;
				}
		}
		
	}
	
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
			System.out.println("Error 33! Connected w/ virtual Web Connection. I am in Client.");
		}	
	}
	
	public MainClient(String name)
	{
		setIconImage(Toolkit.getDefaultToolkit().getImage(MainClient.class.getResource("/Images/logo.jpg")));
		setTitle("KPPMessanger");
		this.nickname = name;
		createWindow();
	}


	private void createWindow() 
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
		setSize(600, 480);
		setMinimumSize(new Dimension(500, 400));
		
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		mntmLogOut = new JMenuItem("Chenge account");
		mntmLogOut.addActionListener(new ActionListener() // LOG OUT
		{
			public void actionPerformed(ActionEvent a) 
			{
				writer.println(nickname + ": I have logged out."); // Отправляем сообщение серверу
				writer.println("keySecretKPPMessanger11");
				writer.flush(); // Перекрываем поток, чтобы сообщение отправилось корректно
				closing = true;
				dispose();
				txtHistory.setText("");
				txtMessage.setText("");
				txtMessage.requestFocus();
				new Change_account();
			}
		});
		mnFile.add(mntmLogOut);
		
		mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) // EXIT
			{
				writer.println(nickname + ": I have disconnected!");
				writer.println("keySecretKPPMessanger11"); // secret key to delete threads
				writer.flush(); // close it for God
				System.exit(0);
			}
		});
		mnFile.add(mntmExit);
		
		mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
		
		mntmAbout = new JMenuItem("About");
		mnHelp.add(mntmAbout);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{0, 450, 0, 150, 0};
		gbl_contentPane.rowHeights = new int[]{15, 245, 0, 20, 200};
		gbl_contentPane.columnWeights = new double[]{1.0, 1.0, 1.0, 0.0, 1.0};
		gbl_contentPane.rowWeights = new double[]{1.0, 1.0, 0.0, 0.0, 0.0};
		contentPane.setLayout(gbl_contentPane);
		
		txtHistory.setFont(new Font("MS Reference Sans Serif", Font.PLAIN, 13));
		txtHistory.setLineWrap(true);
		txtHistory.setWrapStyleWord(true);
		txtHistory.setEditable(false);
		GridBagConstraints gbc_txtHistory = new GridBagConstraints();
		gbc_txtHistory.gridheight = 2;
		gbc_txtHistory.gridwidth = 3;
		gbc_txtHistory.fill = GridBagConstraints.BOTH;
		gbc_txtHistory.insets = new Insets(5, 0, 5, 5);
		gbc_txtHistory.gridx = 0;
		gbc_txtHistory.gridy = 0;
		txtHistory.setLineWrap(true);
		txtHistory.setWrapStyleWord(true);
		JScrollPane scrolltxt = new JScrollPane();
        scrolltxt.setWheelScrollingEnabled(true);
        scrolltxt.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrolltxt.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrolltxt.setViewportView(txtHistory);
		contentPane.add(scrolltxt, gbc_txtHistory);
		
        usersList = new JList(listModel);
        GridBagConstraints gbc_list = new GridBagConstraints();
        gbc_list.gridwidth = 2;
        gbc_list.gridheight = 4;
        gbc_list.insets = new Insets(5, 0, 5, 0);
        gbc_list.fill = GridBagConstraints.BOTH;
        gbc_list.gridx = 3;
        gbc_list.gridy = 0;
        JScrollPane scrolList = new JScrollPane();
        scrolList.setWheelScrollingEnabled(true);
        scrolList.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrolList.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrolList.setViewportView(usersList);
		contentPane.add(scrolList, gbc_list);
       // usersList.setLayoutOrientation(JList.VERTICAL_WRAP);
        //contentPane.add(usersList, gbc_list);
		
        txtMessage = new JTextField();
		txtMessage.setFont(new Font("MS Reference Sans Serif", Font.PLAIN, 13));
		GridBagConstraints gbc_txtMessage = new GridBagConstraints();
		gbc_txtMessage.gridwidth = 3;
		gbc_txtMessage.fill = GridBagConstraints.BOTH;
		gbc_txtMessage.insets = new Insets(0, 0, 5, 5);
		gbc_txtMessage.gridx = 0;
		gbc_txtMessage.gridy = 3;
		contentPane.add(txtMessage, gbc_txtMessage);
		txtMessage.setColumns(10);
		
		btnSend = new JButton("Send");
		btnSend.setSize(20, 60);
		btnSend.addActionListener(new Listener());
		
		GridBagConstraints gbc_btnSend = new GridBagConstraints();
		gbc_btnSend.gridwidth = 2;
		gbc_btnSend.anchor = GridBagConstraints.NORTHWEST;
		gbc_btnSend.insets = new Insets(0, 0, 0, 5);
		gbc_btnSend.gridx = 0;
		gbc_btnSend.gridy = 4;
		gbc_btnSend.ipadx = 30;
		contentPane.add(btnSend, gbc_btnSend);
		
		logged = new JLabel();
		logged.setFont(new Font("MS Reference Sans Serif", Font.PLAIN, 11));
		logged.setText("<html><i>Logged as: " + nickname + "</i></html>");
		GridBagConstraints gbc_logged = new GridBagConstraints();
		gbc_logged.anchor = GridBagConstraints.NORTHEAST;
		gbc_logged.gridwidth = 3;
		gbc_logged.insets = new Insets(0, 0, 0, 5);
		gbc_logged.gridx = 0;
		gbc_logged.gridy = 4;
		gbc_logged.ipadx = 100;
		gbc_logged.ipady = 7;
		contentPane.add(logged, gbc_logged);
		
		virtualNet();
		
		Thread serverObtaining = new Thread(new Obdt());
		serverObtaining.start();
		
		contentPane.getRootPane().setDefaultButton(btnSend);
		
		btnPrivate = new JButton("Private message");
		btnPrivate.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				// THIS IS PRIVATE MESSAGES
			}
		});
		GridBagConstraints gbc_btnPrivate = new GridBagConstraints();
		gbc_btnPrivate.gridwidth = 2;
		gbc_btnPrivate.anchor = GridBagConstraints.NORTH;
		gbc_btnPrivate.insets = new Insets(0, 0, 0, 0);
		gbc_btnPrivate.gridx = 3;
		gbc_btnPrivate.gridy = 4;
		contentPane.add(btnPrivate, gbc_btnPrivate);
		setLocationRelativeTo(null);
		setVisible(true);
		//setLocationByPlatform(true);
		addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				if(!closing)
				{
				writer.println(nickname + ": I have disconnected!");
				writer.println("keySecretKPPMessanger11"); // secret key to delete threads
				writer.flush(); // close it for God
				System.exit(0);
				}
			}
			
			public void windowOpened(WindowEvent e)
			{
				writer.println(nickname+": I have connected!");
				writer.flush();
			}
		});
		//System.out.println(nickname + " " + password);
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
						if(message.startsWith("forlist"))
						{
							String nick = message.substring(7);
							toList(nick);
						}
						else
						{
							if(message.startsWith("fromlist"))
							{
								String nick = message.substring(8);
								fromList(nick);
							}
							else
							{
									txtHistory.append(message + '\n');
							}
						}
					
				}
			}
			catch(Exception e)
			{
				System.out.println("Error 132! Problems w/ server listener. I am in client!");
			}
		}
	}
	
	private static class Listener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			String message;
			if(txtMessage.getText() == null){ message = null; }
			else
			{
			message = nickname + ": " + txtMessage.getText(); // Записываем строку, которую нужно отправить
			writer.println(message); // Отправляем сообщение серверу
			writer.flush(); // Перекрываем поток, чтобы сообщение отправилось корректно
			}
			txtMessage.setText("");
			txtMessage.requestFocus();
		}
		
	}

}
