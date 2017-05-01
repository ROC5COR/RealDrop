package Interfaces;

import MainRealDrop.ClientData;

public interface I_Model {
	int addClient(ClientData c);
	
	void removeClient(int id);
	
	boolean isPresent(String ip);
	
	int getModelSize();
	
	ClientData getClientAt(int index);
	
}
