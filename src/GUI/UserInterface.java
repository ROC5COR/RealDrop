package GUI;



import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import com.sun.javafx.iio.ImageLoaderFactory;

import Interfaces.I_ClientView;
import Interfaces.I_Controller;
import Interfaces.I_Model;

public class UserInterface extends JFrame implements ActionListener,I_ClientView{
	
	private I_Controller ctrl = null;
	private I_Model model = null;
	
	private JPanel mainPanel = null;
	private JPanel clientList = null;
	private JTextField userPseudo = null;
	
	public UserInterface(){
        
		this.setTitle("RealDrop");
		this.setSize(300, 300);
		this.setLocationRelativeTo(null);

		//Image image = Toolkit.getDefaultToolkit().getImage(getClass().getResource());
	    //setIconImage(image);
	    try{
	    	Image image = new ImageIcon("/Resources/RealDrop_Icon.png").getImage();
	    	this.setIconImage(image);
	    }catch(Exception e){
	    	System.out.println("Appilcation icon not found");
	    }
		//this.setUndecorated(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		mainPanel = new JPanel(new BorderLayout());
		
		mainPanel.setSize(new Dimension(this.getWidth(), this.getHeight()));
		mainPanel.setBorder(BorderFactory.createLineBorder(Color.RED));
		
		userPseudo = new JTextField();
		userPseudo.addActionListener(this);
		mainPanel.add(userPseudo,BorderLayout.NORTH);
		mainPanel.add(createClientList(),BorderLayout.CENTER);
		
		/**/
		
		
		this.setContentPane(mainPanel);
		this.setVisible(true);
		
		
	}
	
	private JPanel createClientList(){
		/* Create GUI and non GUI for client list management*/
		clientList = new JPanel();
		clientList.setLayout(new BoxLayout(clientList, BoxLayout.Y_AXIS));
		
		System.out.println(mainPanel.getWidth()+","+mainPanel.getHeight());
		clientList.setPreferredSize(new Dimension(mainPanel.getWidth(),mainPanel.getHeight()));
		clientList.setBorder(BorderFactory.createLineBorder(Color.BLUE));
		
		
		
		//JScrollPane listScroller = new JScrollPane(clientListPane);
		//listScroller.setPreferredSize(new Dimension(250, 80));
		//listScroller.setAlignmentX(LEFT_ALIGNMENT);
		
		//TODO Use JScrollPane to display clients list
	    
	    
		return clientList;
	}

	@Override
	public void setController(I_Controller ctrl) {
		this.ctrl = ctrl;
	}

	@Override
	public void triggerSendFile(int id, String filePath) {
		if(this.ctrl == null){
			System.out.println("Error Controller interface in GUI is NULL");
			return;
		}
		ctrl.sendFileTo(id, filePath);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == userPseudo){
			System.out.println("Event : "+e.getID());
			if(ctrl == null){
				System.out.println("Controller interface is NULL in action performed");
			}
			if(!e.getActionCommand().equals("")){
				userPseudo.setFocusable(false);
				ctrl.setSenderName(e.getActionCommand());
			}
		}
		
	}



	@Override
	public void setModel(I_Model model) {
		this.model = model;
	}

	@Override
	public void updateView() {
		if(model == null){
			System.out.println("Error model not set in the view");
			return;
		}
		System.out.println("Updating view");
		this.clientList.removeAll();
		for(int i = 0; i < model.getModelSize(); i++){
			System.out.println("GUI : Adding new client at "+i);
			this.clientList.add(new ClientGUIRepresentation(model.getClientAt(i).getClientName(), i, this));
		}
		
		this.validate();
	}

}

