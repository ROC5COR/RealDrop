package GUI;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Interfaces.I_ClientView;
import MainRealDrop.FileDrop;
import MainRealDrop.FileDrop.Listener;

public class ClientGUIRepresentation extends JPanel{
	
	private JLabel label = new JLabel("NULL");
	private int id = -1;
	private I_ClientView view = null;
	
	public ClientGUIRepresentation(String text, int id, I_ClientView view){
		this.id = id;
		this.view = view;
		
		label.setText(text);
		this.add(label);
		
		this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		this.setPreferredSize(new Dimension(300, 10));
		
		
		//Drop management
		new FileDrop( System.out, this, new FileDrop.Listener()
        {   public void filesDropped( java.io.File[] files )
            {   for( int i = 0; i < files.length; i++ )
                {   try
                    {   //text.append( files[i].getCanonicalPath() + "\n" );
                		System.out.println("File : "+files[i].getCanonicalPath());
                		
                		sendFileTrigger(files[i].getCanonicalPath());
                    }   // end try
                    catch( java.io.IOException e ) {
                    	System.out.println("Error : "+e.toString());
                    }
                }   // end for: through each dropped file
            }   // end filesDropped
        }); // end FileDrop.Listener
	}

	public void sendFileTrigger(String filePath){
		view.triggerSendFile(this.id, filePath);
	}
}
