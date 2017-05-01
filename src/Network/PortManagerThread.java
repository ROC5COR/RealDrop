package Network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import GUI.FileNotification;
import Interfaces.I_Controller;
import MainRealDrop.FileReceiver;

public class PortManagerThread extends Thread{
	
	private Socket client = null;
	private I_Controller ctrl = null;
	
	public PortManagerThread(Socket client, I_Controller ctrl){
		this.client = client;
		this.ctrl = ctrl;
	}
	
	public void run(){
		try{
			byte[] bufR = new byte[2048];
			InputStream is = client.getInputStream();
			OutputStream os = client.getOutputStream();
			
			int lenBufR = is.read(bufR);
			String[] commands = new String(bufR, 0 , lenBufR).split(",");
			System.out.println("["+client.getInetAddress()+"] Say : "+commands);
			System.out.println("Parsing command from the client...");
			
			//commands = commands.trim();
	
			//TP : TransferPort
			String fileName = "Unknow filename";
			boolean portRequest = false;
			boolean pingRequest = false;
			for(String s : commands){//Command parser
				System.out.println("Command received : "+s);
				if(s.split(":")[0].equals("FILENAME")){
					fileName = s.split(":")[1];
				}
				if(s.split(":")[0].equals("TP")){
					if(s.split(":")[1].equals("?")){
						portRequest = true;
					}
				}
				if(s.split(":")[0].equals("PING")){
					pingRequest = true;
					break;
				}
			}
			//TODO filter the fileName to be sure its "conform" (no '/', ',', '|'...)
		
			if(portRequest){
				//TODO Check if user wants to receive file
				FileNotification notif = new FileNotification(fileName);
				notif.start();
				
				try {
					notif.join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("Thread ended");
				if(notif.getReturnValue() == 0){
					System.out.println("User refuse the transfer");
				}
				else if(notif.getReturnValue() == 1){
					System.out.println("User accept the file !!");
				}
				else{
					System.out.println("Error with notification");
				}
				//TODO Check  if FIleReceiver is listening
				//TODO Otherwise choose another random port in the pool
				ctrl.askPermissions(fileName, client.getInetAddress().getHostAddress());
				
				int receptionPort = PortManager.getPortFromPool();
				String localFilePath = System.getProperty("user.home")+"/Downloads/"+fileName;
				
				FileReceiver fr = new FileReceiver(client.getInetAddress().getHostAddress(), receptionPort, localFilePath);

				os.write(new String(""+receptionPort).getBytes());
				
				fr.receive();
				
			}
			else{
				System.out.println("Not a port request");
			}
			
			
			is.close();
			os.close();
			client.close();
		}
		catch (IOException e) {
			
			e.printStackTrace();
		}
	}
	
}
