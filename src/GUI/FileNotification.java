package GUI;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;


public class FileNotification extends Thread{

	private static final long serialVersionUID = -551065545150454282L;
	private JFrame frame = null;
	private int returnValue = -1;
	private String fileName = "";

    public int getReturnValue() {
		return returnValue;
	}

	public void setReturnValue(int returnValue) {
		this.returnValue = returnValue;
	}

	public FileNotification(String fileName) {
		this.fileName = fileName;
	}
	
	@Override
	public void run(){
		String header = "Do you want to accept the file "+fileName +" ?";
		
		frame = new JFrame();
		
		frame.setSize(350,125);
		frame.setUndecorated(true);
		frame.setOpacity((float) 0.75);
		frame.setLayout(new GridBagLayout());
		
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.weightx = 1.0f;
		constraints.weighty = 1.0f;
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.fill = GridBagConstraints.BOTH;
		JLabel headingLabel = new JLabel(header);
		//headingLabel.setIcon(headingIcon); // --- use image icon you want to be as heading image.
		headingLabel.setOpaque(false);
		frame.add(headingLabel, constraints);
		
		
		JButton acceptButton = new JButton(new AbstractAction("Accept") {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				returnValue = 0;
				frame.dispose();
			}
		});
		
		JButton refuseButton = new JButton(new AbstractAction("Refuse") {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				returnValue = 1;
				frame.dispose();
			}
		});
		
		constraints.gridx = 0;
		constraints.gridy++;
		constraints.weightx = 1.0f;
		constraints.weighty = 1.0f;
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.fill = GridBagConstraints.BOTH;
		
		frame.add(acceptButton, constraints);
		
		constraints.gridx++;
		//constraints.gridy++;
		constraints.weightx = 1.0f;
		constraints.weighty = 1.0f;
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.fill = GridBagConstraints.BOTH;
		
		frame.add(refuseButton, constraints);
		
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setVisible(true);
        
    
	}

}