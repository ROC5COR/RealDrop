package Interfaces;

import MainRealDrop.ClientData;

public interface I_Controller {
	//From GUI to Controller
	void sendFileTo(int id, String filePath);
	void setSenderName(String name);
	
	//From service discovery to controller
	void newClientDiscovered(ClientData data);
	
	//From PortManager to controller
	void askPermissions(String fileName, String fromIpAddr);
}
