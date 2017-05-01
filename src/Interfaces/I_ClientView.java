package Interfaces;

public interface I_ClientView {
	//void addNewClient(String name, int id);
	
	void setController(I_Controller ctrl);
	void setModel(I_Model model);
	void updateView();
	
	//For lower level of the GUI not the controller
	void triggerSendFile(int id, String filePath);
}
