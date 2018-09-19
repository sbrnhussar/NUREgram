package NUREgramServerPart;

public class ClientThreadList {
	private int number;
	private ClientThread[] list;
	ClientThreadList(){
		number = 0;
	}
	
	public void add(ClientThread new_thread) {
		number++;
		ClientThread[] buff_list = new ClientThread[number];
		buff_list[number - 1] = new_thread;
		for (int i = 0; i < number - 1; i++)
		{
			buff_list[i] = list[i];
		}
		list = buff_list;
		new_thread.start();
	}
	
	public void delete(ClientThread del_thread) {
		int index = 0;
		for(int i = 0; i < number; i++) {
			if(list[i].get_index() == del_thread.get_index()) {
				index = i;
			}
		}
		
		ClientThread[] buff_list = new ClientThread[number - 1];
		for (int i = 0; i < index; i++)
		{
			buff_list[i] = list[i];
		}
		for (int i = index; i < number - 1; i++)
		{
			buff_list[i] = list[i + 1];
		}
		number--;
		list[number].interrupt();
		list = buff_list;
	}
	
	public ClientThread get_thread(int index) {
		if(list[index] != null) {
			return list[index];
		}
		else {
			return null;
		}
	}
	
	public int get_number() {
		return number;
	}
}
