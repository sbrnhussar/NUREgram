package NUREgramClientPart;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import java.util.Arrays;
import javax.swing.JTextArea;
import javax.swing.JButton;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import java.util.stream.Collectors;

public class MainWindowClient extends JFrame{

	private JTextArea output_area;
	private JTextField input_field;
	private String my_nickname;
	private Socket socket;
	private JTable client_table;

	MainWindowClient(Socket set_socket, String set_nickname) {
		socket = set_socket;
		my_nickname = set_nickname;
		
		JLabel prod_label = new JLabel("Sushko ITCS-16-6 prod.");
		prod_label.setBounds(168, 11, 147, 14);
		getContentPane().add(prod_label);
		
		this.setTitle("NUREgram");
		
		this.setBounds(100, 100, 499, 388);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.getContentPane().setLayout(null);
		
		client_table = new JTable();
		client_table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	    JScrollPane table_scroll_pane = new JScrollPane(client_table);
	    table_scroll_pane.setAutoscrolls(true);
	    table_scroll_pane.setBounds(340, 21, 132, 256);
	    client_table.setPreferredScrollableViewportSize(new Dimension(150, 100));
	    getContentPane().add(table_scroll_pane);
		
		input_field = new JTextField();
		input_field.setBounds(10, 315, 320, 23);
		this.getContentPane().add(input_field);
		input_field.setColumns(10);
		
		JButton send_button = new JButton("Send");
		send_button.setBounds(340, 315, 132, 23);
		this.getRootPane().setDefaultButton(send_button);
		this.getContentPane().add(send_button);
		
		output_area = new JTextArea();
		output_area.setLineWrap(true);
		output_area.setWrapStyleWord(true);
		output_area.setEditable(false);
		JScrollPane output_scroll_pane = new JScrollPane(output_area);
		//output_scroll_pane.setAutoscrolls(true);
		output_scroll_pane.setBounds(10, 48, 320, 256);
		output_scroll_pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		output_scroll_pane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		this.getContentPane().add(output_scroll_pane);
		
		JLabel nickname_label = new JLabel("Your nickname: " + my_nickname);
		nickname_label.setBounds(10, 21, 208, 14);
		getContentPane().add(nickname_label);
		
		JButton update_button = new JButton("Refresh");
		update_button.setBounds(340, 281, 132, 23);
		update_button.addActionListener(new UpdateActionListener());
		getContentPane().add(update_button);
		send_button.addActionListener(new SendActionListener());
	}
		
	private class SendActionListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			if(client_table.getSelectedRow() != -1 && ("").compareTo(input_field.getText()) != 0) {
				String recepient = (String) client_table.getValueAt(client_table.getSelectedRow(), 0);
				String input = input_field.getText();
				input_field.setText("");
				String message = recepient + ";" + input;
				
				try {
					DataOutputStream output = new DataOutputStream(socket.getOutputStream());
					
					output.writeUTF(message);
					output.flush();
				}
				catch (UnknownHostException e1)
				{
					//e1.printStackTrace();
				}
				catch (IOException e2)
				{
					//e2.printStackTrace();
				}
			}
		}
	}
	
	public void set_caret() {
		output_area.setCaretPosition(output_area.getDocument().getLength());
	}
	
	private class UpdateActionListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			try {
				DataOutputStream output = new DataOutputStream(socket.getOutputStream());
				
				output.writeUTF("who_is_online?");
				output.flush();
			}
			catch (UnknownHostException e1)
			{
				//e1.printStackTrace();
			}
			catch (IOException e2)
			{
				//e2.printStackTrace();
			}
		}
	}
	
	public void set_output_area(String text) {
		output_area.setText(output_area.getText() + text + "\n");
	}
	
	public void set_clients(String list){
		String[] str_list = list.split(",");
		for(int i = 0; i < str_list.length; i++) {
			if(my_nickname.compareTo(str_list[i]) == 0) {
				str_list[i] = "Saved Messages";
			}
		}
		List<String> client_buff = Arrays.asList(str_list);
		
		List<String> client_list = client_buff.stream().distinct().collect(Collectors.toList());
		
	    client_table.setModel(new ClientTableModel(client_list));
	}
	
	private class ClientTableModel extends AbstractTableModel
	{
	    private final String[] columnTitles = {"Online users"};
	    private final List<String> list;

	    public ClientTableModel( List<String> list ) {
	        this.list=list;
	    }

	    @Override
	    public int getColumnCount(){
	        return 1;
	    }

	    @Override
	    public String getColumnName( int column ) {
	        return columnTitles[column];
	    }

	    @Override
	    public int getRowCount(){
	        return list.size();
	    }

	    @Override
	    public Object getValueAt(int row, int column)
	    {
	    	String client_row = list.get(row);
	    	return (client_row);
	    }
	}
}