package Client; // Работаем в пакете Клиента

import java.awt.EventQueue; // Необходимые для рабоыт библиотеки

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Toolkit;

public class About extends JFrame {

	private JPanel contentPane;

	public About() // Констурктор один
	{ // Добавляем логотип
		setIconImage(Toolkit.getDefaultToolkit().getImage(About.class.getResource("/Images/logo.jpg")));
		setTitle("About KPPMessanger"); // Задаем название окна
		setResizable(false); // Нельзя изменять размер этого окна
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // При закрытии закрыть только этот прототип окна
		setSize(300, 330); // Фиксированный размер окна
		contentPane = new JPanel(); // Новая панель с отступом от краев 5 пикс. 
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane); // Задаем ее как основную панель
		contentPane.setLayout(null); // Лэйаут у нее абсолютный
		setLocationRelativeTo(null); // Появляется окно будет всегда посередине экрана
		setVisible(true); // Окно будет видно
		
		JLabel image = new JLabel(); // Добавляем имэйдж на лэйбл
		image.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/kppLogo.jpg")));
		image.setBounds(60, 30, 180, 82); // Задаем его расположение
		contentPane.add(image); // Добавляем на основную панель
		
		JLabel desc = new JLabel("<html>\r\nKPPMessanger it's <i>\"easy-to-use\"</i> manager, which includes all common stuff to comunicate. <br /> <br />\r\nThis application was made by <strong>Vindict Team</strong>: <br /> <br />\r\n<strong>Main developer</strong>: Vladyslav Stopin <br />\r\n<strong>Assistance</strong>: Bogdan Gorbatenko <br /> <br />\r\n<strong>GitHub</strong>: KPPMessanger <br /> <br />\r\n<center><i><small>Current version: 0.63</small></i></center>\r\n</html>");
		desc.setBounds(27, 135, 245, 156); // Добавим описание в определенное место на окне
		contentPane.add(desc); // Добавим все на основную панель для отображения
	}
}
