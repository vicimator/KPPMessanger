package Client;

import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.UIManager;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Error_private extends JFrame 
{
	
	private Icon warnIcon = UIManager.getIcon("OptionPane.warningIcon");
	
	Error_private() 
	{
		initGUI();
	}
	
	private void initGUI()
	{
		
		setTitle("Error");
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(280, 160);
		setLocationRelativeTo(null);
		setVisible(true);
		getContentPane().setLayout(null);
		
		JLabel msgLabel = new JLabel();
		msgLabel.setFont(new Font("Book Antiqua", Font.PLAIN, 13));
		msgLabel.setText("<html> Error occurred because of chosen nickname. System can't recognize it, please, return and choose again.</html>");
		msgLabel.setBounds(47, 11, 217, 85);
		getContentPane().add(msgLabel);
		
		JButton btnAgree = new JButton("Got it");
		btnAgree.setFont(new Font("Tahoma", Font.PLAIN, 13));
		btnAgree.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent a) 
			{
				dispose();
			}
		});
		btnAgree.setBounds(160, 87, 104, 34);
		getContentPane().add(btnAgree);
		
		JLabel warnLabel = new JLabel();
		warnLabel.setBounds(10, 21, 32, 44);
		warnLabel.setIcon(warnIcon);
		getContentPane().add(warnLabel);

	}
	
public static void main(String[] args) 
{
		EventQueue.invokeLater(new Runnable(){
			
			public void run() 
			{
				try 
				{
					Error_private frame = new Error_private();
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
