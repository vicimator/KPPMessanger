package Client;

import java.awt.BasicStroke;
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
    private static JButton deletePanes;
	
	public static void toList(String nick)
	{
		try
		{
			for(int i=0; i<data.size(); i++)
			{
				if(data.get(i).toString().equals(nick)) throw new Exception("Dat nick is already in use!");
			}
			data.add(nick);
	        updateJList();
		}
		catch(Exception e)
		{
			System.out.println("Got it.");
		}
	}
	
	public static void fromList(String nick)
	{
		for(int i=0; i<data.size(); i++)
		{
			if(data.get(i).toString().equals(nick)) 
				{
					data.remove(i);
					updateJList();
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
		setMinimumSize(new Dimension(585, 450));
		
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		setDB();
		
		mntmLogOut = new JMenuItem("Chenge account");
		mntmLogOut.addActionListener(new ActionListener() // LOG OUT
		{
			public void actionPerformed(ActionEvent a) 
			{
				txtHistory.setText("");
				txtMessage.setText("");
				writer.println(nickname + ": I have logged out."); // Отправляем сообщение серверу
				writer.println("keySecretKPPMessanger11");
				writer.flush(); // Перекрываем поток, чтобы сообщение отправилось корректно
				writer.close();
				try
				{
					reader.close();
				} 
				catch (IOException e) 
				{
					System.out.println("Error 190! I'm in client. Reader closed incottrect.");
					e.printStackTrace();
				}
				closing = true;
				serverObtaining.interrupt();
				try 
				{
					String SQL = "UPDATE `logins` SET isOnline=0 WHERE nick=\""+nickname+"\"";
					Statement s = c.createStatement();
					s.executeUpdate(SQL);
					c.close();
				} 
				catch (SQLException e) 
				{
					System.out.println("Error #237! Problems w/ SQL. I'm in main client.");
					e.printStackTrace();
				}

				dispose();
				//txtMessage.requestFocus();
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
				writer.close();
				try 
				{
					reader.close();
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
				System.exit(0);
			}
		});
		mnFile.add(mntmExit);
		
		mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
		
		mntmAbout = new JMenuItem("About");
		mntmAbout.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				new About();
			}
		});
		mnHelp.add(mntmAbout);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{0, 450, 0, 150, 0};
		gbl_contentPane.rowHeights = new int[]{15, 245, 0, 20, 30, 0, 160, 20};
		gbl_contentPane.columnWeights = new double[]{1.0, 1.0, 1.0, 0.0, 1.0};
		gbl_contentPane.rowWeights = new double[]{1.0, 1.0, 0.0, 0.0, 0.0, 1.0};
		contentPane.setLayout(gbl_contentPane);
		
		txtHistory.setFont(new Font("MS Reference Sans Serif", Font.PLAIN, 13));
		txtHistory.setLineWrap(true);
		txtHistory.setWrapStyleWord(true);
		txtHistory.setEditable(false);
		GridBagConstraints gbc_txtHistory = new GridBagConstraints();
		gbc_txtHistory.gridheight = 3;
		gbc_txtHistory.gridwidth = 3;
		gbc_txtHistory.fill = GridBagConstraints.BOTH;
		gbc_txtHistory.insets = new Insets(5, 0, 5, 5);
		gbc_txtHistory.gridx = 0;
		gbc_txtHistory.gridy = 0;
		JScrollPane scrolltxt = new JScrollPane();
        scrolltxt.setWheelScrollingEnabled(true);
        scrolltxt.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrolltxt.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrolltxt.setViewportView(txtHistory);
		contentPane.add(scrolltxt, gbc_txtHistory);
		
        usersList = new JList(new myListModel());
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
		gbc_btnSend.insets = new Insets(0, 0, 5, 5);
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
		gbc_logged.insets = new Insets(0, 0, 5, 5);
		gbc_logged.gridx = 0;
		gbc_logged.gridy = 4;
		gbc_logged.ipadx = 100;
		gbc_logged.ipady = 7;
		contentPane.add(logged, gbc_logged);
		
		
		
		virtualNet();
		
		serverObtaining = new Thread(new Obdt());
		serverObtaining.start();
		
		contentPane.getRootPane().setDefaultButton(btnSend);
		
		btnPrivate = new JButton("Private mode");

		GridBagConstraints gbc_btnPrivate = new GridBagConstraints();
		gbc_btnPrivate.gridwidth = 2;
		gbc_btnPrivate.anchor = GridBagConstraints.NORTH;
		gbc_btnPrivate.insets = new Insets(0, 0, 5, 0);
		gbc_btnPrivate.gridx = 3;
		gbc_btnPrivate.gridy = 4;
		contentPane.add(btnPrivate, gbc_btnPrivate);
		
		privatePane = new JTabbedPane(JTabbedPane.TOP);
		privatePane.setFont(new Font("MS Reference Sans Serif", Font.PLAIN, 13));
		privatePane.setVisible(false);
		GridBagConstraints gbc_privatePane = new GridBagConstraints();
		gbc_privatePane.gridwidth = 3;
		gbc_privatePane.gridheight = 2;
		gbc_privatePane.insets = new Insets(0, 0, 5, 5);
		gbc_privatePane.fill = GridBagConstraints.BOTH;
		gbc_privatePane.gridx = 0;
		gbc_privatePane.gridy = 5;
		contentPane.add(privatePane, gbc_privatePane);
		
		deletePanes = new JButton("Remove pane");
		deletePanes.setVisible(false);
		deletePanes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0)
			{
				int index = privatePane.getSelectedIndex();
				if (index != -1) 
	            {
	            	privatePane.remove(index);
	            }
				privates.remove(index);
				if(index == 0)
				{
				privatePane.setVisible(false);
				deletePanes.setVisible(false);
				}
			}
		});
		GridBagConstraints gbc_deletePanes = new GridBagConstraints();
		gbc_deletePanes.anchor = GridBagConstraints.SOUTH;
		gbc_deletePanes.insets = new Insets(0, 0, 5, 5);
		gbc_deletePanes.gridx = 3;
		gbc_deletePanes.gridy = 6;
		contentPane.add(deletePanes, gbc_deletePanes);

		btnPrivate.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent ae) 
			{
					try
					{
					Object element = usersList.getSelectedValue();
	                String pName = element.toString();
		                if(pName.equals(nickname))
		                {
		                	new Info_private();
		                }
		                else
		                {
		                	boolean iChecker = true;
		                	for(int i=0; i<privates.size();i++)
		                	{
		                		if(privates.get(i).equals(pName))
		                		{
		                			new Info_private_add();
		                			iChecker = false;
		                			break;
		                		}
		                	}
		                	if(iChecker)
		                	{
		                	privates.add(pName);
		                	privatePane.setVisible(true);
		                	deletePanes.setVisible(true);
		                	privatePane.addTab(pName, createPane(pName));
		                	//new Private(pName, nickname);
		                	}
		                }
					}
					catch(RuntimeException e)
					{
						Object element = usersList.getSelectedValue();
		                String pName = element.toString();
						new Error_private();
						for( int i=0; i<privates.size(); i++)
						{
							if(privates.get(i).equals(pName)) privates.remove(i);
						}
					}
			}
		});
		
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
				writer.close();
				try 
				{
					reader.close();
				} catch (IOException e1) 
				{
					System.out.println("Error 399! I'm in client. Reader closed incorrect!");
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
				catch (SQLException exc) 
				{
					System.out.println("Error #237! Problems w/ SQL. I'm in main client.");
					exc.printStackTrace();
				}
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
								if(message.startsWith("prIvaTeMeSsSaGGGe123"+nickname))
								{
									int width = nickname.length();
									String msgPrivate = message.substring(20+width);
									int cut = msgPrivate.indexOf(' ') + 1;
									String login = msgPrivate.substring(0, cut-2);
									//System.out.println("LOGIN IS: " + login);
									boolean iChecks = true;
									for(int i=0; i<privates.size(); i++)
									{
										if(privates.get(i).equals(login))
										{
											txtArea.append(msgPrivate + '\n');
											iChecks = false;
											break;
										}
									}
									if(iChecks)
									{
										privates.add(login);
					                	privatePane.setVisible(true);
					                	deletePanes.setVisible(true);
					                	privatePane.addTab(login, createPane(login));
					                	txtArea.append(msgPrivate + '\n');
									}
									
								}
								else
								{
									if(message.startsWith("spEcIalFORRecieveRR5"))
									{
										String msgPrivate = message.substring(20);
										txtArea.append(msgPrivate + '\n');
									}
									else
									{
										txtHistory.append(message + '\n');
									}
								}
								
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
	
	 private static void updateJList() 
	 {
	        ((myListModel) usersList.getModel()).update();
	    }
	
	private class myListModel extends AbstractListModel<String> 
	{
		 
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
	
	  static JPanel createPane(final String pName) 
	  {
		  	JPanel panel = new JPanel();
			panel.setLayout(new BorderLayout(0, 0));
			
			txtArea = new JTextArea();
			txtArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
			txtArea.setEditable(false);
			txtArea.setLineWrap(true);
			txtArea.setWrapStyleWord(true);
			JScrollPane scroll = new JScrollPane();
	        scroll.setWheelScrollingEnabled(true);
	        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
	        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
	        scroll.setViewportView(txtArea);
			panel.add(scroll, BorderLayout.CENTER);
			
			JPanel panel_1 = new JPanel();
			panel_1.setLayout(new BorderLayout(0, 0));
			panel_1.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
			
			final JTextField txtField = new JTextField();
			panel_1.add(txtField, BorderLayout.CENTER);
			txtField.setColumns(10);
			
			panel.add(panel_1, BorderLayout.SOUTH);
			
			JButton btnNewButton = new JButton("Send");
			btnNewButton.addActionListener(new ActionListener() 
			{
				public void actionPerformed(ActionEvent ae) 
				{
					String message;
					if(txtField.getText() == null){ message = null; }
					else
					{
					message = "spEcialForUser11"+ nickname + "\\" + pName + ": " + txtField.getText(); // Записываем строку, которую нужно отправить
					writer.println(message); // Отправляем сообщение серверу
					writer.flush(); // Перекрываем поток, чтобы сообщение отправилось корректно
					}
					txtField.setText("");
					txtField.requestFocus();
				}
			});
			panel_1.add(btnNewButton, BorderLayout.EAST);
			
		    return panel;
	  }
	  
	  private class TabButton extends JButton implements ActionListener 
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
	 
	    private final static MouseListener buttonMouseListener = new MouseAdapter() {
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
