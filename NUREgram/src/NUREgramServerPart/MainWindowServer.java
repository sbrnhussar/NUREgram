package NUREgramServerPart;

import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.net.ServerSocket;
import java.awt.event.ActionEvent;

public class MainWindowServer extends JFrame{

	private JTextArea logs_area;
	private JLabel log_label = new JLabel("Server logs:");

	public MainWindowServer() {
		this.setTitle("NUREgram Server");
		this.setBounds(100, 100, 522, 492);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.getContentPane().setLayout(null);
		
		log_label.setBounds(10, 17, 199, 14);
		getContentPane().add(log_label);
		
		JLabel prod_label = new JLabel("Sushko ITCS-16-6 prod.");
		prod_label.setBounds(363, 17, 151, 14);
		getContentPane().add(prod_label);
		
		logs_area = new JTextArea();
		logs_area.setLineWrap(true);
		logs_area.setWrapStyleWord(true);
		logs_area.setEditable(false);
		JScrollPane output_scroll_pane = new JScrollPane(logs_area);
		output_scroll_pane.setBounds(10, 42, 486, 400);
		output_scroll_pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		output_scroll_pane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		this.getContentPane().add(output_scroll_pane);
	}
	
	public void set_caret() {
		logs_area.setCaretPosition(logs_area.getDocument().getLength());
	}
	
	public void set_text(String text) {
		logs_area.setText(logs_area.getText() + text + "\n");
	}
}


