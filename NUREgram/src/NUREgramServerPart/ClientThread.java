package NUREgramServerPart;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;


public class ClientThread extends Thread{
	private static ClientThreadList thread_list = new ClientThreadList();
	private int index;
	private static int autoincrement_index = 0;
	private Socket client;
	private DataOutputStream server_output;
	private DataInputStream server_input;
	private String nickname;
	private MainWindowServer main_window;
	
	ClientThread(Socket socket, MainWindowServer set_window){
		client = socket;
		main_window = set_window;
		nickname = "";
		index = autoincrement_index++;
	}
		
	public String get_nickname() {
		return nickname;
	}
	
	public void add_thread() {
		thread_list.add(this);
	}
	
	public void del_thread() {
		thread_list.delete(this);
	}
	
	public int get_index() {
		return index;
	}
	
	void send(String message) {
		try{
			server_output.writeUTF(message);
		}
		catch (IOException e)
		{
			e.printStackTrace();
        }
	}
	
	public void run() {
		try {
			server_output = new DataOutputStream(client.getOutputStream());
			server_input = new DataInputStream(client.getInputStream());
			
			String client_message = server_input.readUTF();
			main_window.set_text("New thread created for connected client. Nickname - " + client_message);
			nickname = client_message;
			
			while(true) {
				client_message = server_input.readUTF();
				
				if(client_message.compareTo("who_is_online?") == 0) {
					main_window.set_text(index + " thread(" + nickname + ") ask - " + client_message);
					String answer = "take_online;";
					if(thread_list.get_number() > 1) {
						for(int i = 0; i < thread_list.get_number() - 1; i++) {
							answer += thread_list.get_thread(i).get_nickname() + ",";
						}
						answer += thread_list.get_thread(thread_list.get_number() - 1).get_nickname();
					}
					else if(thread_list.get_number() == 1){
						answer += thread_list.get_thread(0).get_nickname();
					}
					main_window.set_text("Online list for " + index + " thread(" + nickname + ") - " + answer);
					server_output.writeUTF(answer);
					server_output.flush();
				}
				else {
					for(int i = 0; i < thread_list.get_number(); i++) {
						if((client_message.split(";", 2)[0]).compareTo(thread_list.get_thread(i).get_nickname()) == 0) {	
							main_window.set_text(index + "-th thread(" + nickname + "), send to " + thread_list.get_thread(i).get_index() + "-th thread(" + thread_list.get_thread(i).get_nickname() + ") - " + client_message.split(";", 2)[1]);
							(thread_list.get_thread(i)).send(nickname + ": " + client_message.split(";", 2)[1]);
							for(int j = 0; j < thread_list.get_number(); j++) {
								if(nickname.compareTo(thread_list.get_thread(j).get_nickname()) == 0) {
									(thread_list.get_thread(j)).send("You to " + client_message.split(";", 2)[0]+  ": " + client_message.split(";", 2)[1]);
								}
							}
						}
						else if((client_message.split(";", 2)[0]).compareTo("Saved Messages") == 0 && nickname.compareTo(thread_list.get_thread(i).get_nickname()) == 0) {
							main_window.set_text(index + "-th thread(" + nickname + "), send to himself, to him "  + thread_list.get_thread(i).get_index() + "-th thread - " + client_message.split(";", 2)[1]);
							(thread_list.get_thread(i)).send("Saved Messages: " + client_message.split(";", 2)[1]);
						}
					}
				}
				main_window.set_caret();
			}
		}
		catch (UnknownHostException e1)
		{
			//e1.printStackTrace();
		}
		catch (IOException e2)
		{
			del_thread();
		}
	}
}
