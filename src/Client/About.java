package Client;

import java.awt.EventQueue;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Toolkit;

public class About extends JFrame {

	private JPanel contentPane;

	public About() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(About.class.getResource("/Images/logo.jpg")));
		setTitle("About KPPMessanger");
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(300, 330);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel image = new JLabel();
		image.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/kppLogo.jpg")));
		image.setBounds(60, 40, 180, 82);
		contentPane.add(image);
		
		JLabel desc = new JLabel("<html>\r\nKPPMessanger it's <i>\"easy-to-use\"</i> manager, which includes all common stuff to comunicate. <br /> <br />\r\nThis application was made by <strong>Vindict Team</strong>: <br /> <br />\r\n<strong>Main developer</strong>: Vladyslav Stopin <br />\r\n<strong>Assistance</strong>: Bogdan Gorbatenko <br /> <br />\r\n<center><i><small>Current version: 0.3</small></i></center>\r\n</html>");
		desc.setBounds(27, 145, 245, 125);
		contentPane.add(desc);
	}
	
public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					About frame = new About();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
}
}
