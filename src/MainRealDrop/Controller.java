/*
 * Main class to launch the project
 * 
 * 
 */
package MainRealDrop;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import GUI.FileNotification;
import GUI.UserInterface;
import Interfaces.I_ClientView;
import Interfaces.I_Controller;
import Interfaces.I_Model;
import Network.Broadcasts;
import Network.ClientPinger;
import Network.DiscoveryServiceEmitter;
import Network.DiscoveryServiceReceiver;
import Network.PortManager;

public class Controller implements I_Controller {
	public static void main(String[] args) {
		System.out.println("RealDrop v0.0.25b");
		java.net.URL url = ClassLoader.getSystemResource("/RealDrop/src/Resources/RealDrop_Icon.png");
		Controller ctrl = new Controller();
	}
	
	
	I_Model model = null;
	I_ClientView gui = null;
	
	PortManager pm = null;
	
	private String userPseudo = "";

	public Controller() {
		
		model = new ClientDataModel();
		
		gui = new UserInterface();
		gui.setController(this);
		gui.setModel(model);
		
		pm = new PortManager(this);
		pm.start();
		
		String localMachineName = "ND";
		try {
			localMachineName = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		DiscoveryServiceReceiver drs = new DiscoveryServiceReceiver(this);
		
		if(!this.userPseudo.equals("")){
			localMachineName = userPseudo+" ("+localMachineName+")";
		}
		//TODO change fixed IP
		DiscoveryServiceEmitter dse = new DiscoveryServiceEmitter(Broadcasts.getFirstBroadcastAddress(),"PORT:"+pm.getListenPort()+",NAME:"+localMachineName);
		dse.start();
		drs.start();
		
		//Task to check if each client is online
		final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
		
		executor.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				for(int i = 0; i < model.getModelSize(); i++){
					if(new ClientPinger(model.getClientAt(i).getFileManagerPort())
							.ping(model.getClientAt(i).getIp()) == 1){
						System.out.println("[Taks] Client "+model.getClientAt(i).getIp()+" is online");
					}
					else{
						System.out.println("[Task] Client "+model.getClientAt(i).getIp()+" is offline");
						model.removeClient(i);
						gui.updateView();
					}
				}
				
			}
		}, 5, 10, TimeUnit.SECONDS);
		
			
		System.out.println("Controller : All service started");
		
	}
	
	@Override
	public void sendFileTo(int id, String filePath) {
		System.out.println("Must send "+filePath+" to ID : "+id);
		File file = new File(filePath);
		ClientData clientToSend = model.getClientAt(id);
		if(new ClientPinger(clientToSend.getFileManagerPort()).ping(clientToSend.getIp()) == 1){
			System.out.println("Host is online");
		}
		else{
			System.out.println("Host is offline file not sent");
			model.removeClient(id);
			gui.updateView();
			
			return;
		}
		
		int tp = pm.getRemoteTransfertPort(model.getClientAt(id).getIp(), model.getClientAt(id).getFileManagerPort(),file.getName());
		if(tp < 0){
			System.out.println("Error while sending file to client (maybe offline)");
		}
		else{
			System.out.println("Must send "+filePath+" to "+model.getClientAt(id).getIp()+":"+tp);
			
			FileSender fs = new FileSender(model.getClientAt(id).getIp(), tp);
			fs.send(filePath);
		}
		
	}

	@Override
	public void setSenderName(String name) {
		System.out.println("SenderName : "+name);
		this.userPseudo = name;
	}

	@Override
	public void newClientDiscovered(ClientData data) {
		System.out.println("New client : "+data.getClientName()+", "+data.getIp()+", "+data.getFileManagerPort());
		
		if(!model.isPresent(data.getIp())){
			int id = model.addClient(data);
			System.out.println("Client added to list with id : "+id);
			gui.updateView();
			//gui.addNewClient(data.getClientName(), id);
		}
		
		
		
	}

	@Override
	public void askPermissions(String fileName, String fromIpAddr) {
		System.out.println("Ask permissions to the user ("+fileName+","+fromIpAddr+")");
		//Maybe call GUI directly from Thread to block the Thread but not the entire program
	}

}
