package Network;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Interfaces.I_Controller;


public class PortManager extends Thread{
	
	private int defaultPort = 30451;
	private static List<Integer> availablePorts = Collections.synchronizedList(new ArrayList<>());
	private static List<Integer> usedPorts = Collections.synchronizedList(new ArrayList<>());
	
	private I_Controller ctrl = null;
	
	public PortManager(I_Controller ctrl){
		this.ctrl = ctrl;
		for(int i = 30452; i < 30462; i++){
			availablePorts.add(new Integer(i));
		}
		System.out.println(availablePorts.size()+" ports available in pool");
		
	}
	
	public int getListenPort(){
		return defaultPort;
	}
	
	
	public void run(){
		//Randomize the port returned in the pool

		ServerSocket socketEcoute;
		try {
			socketEcoute = new ServerSocket();
			socketEcoute.bind(new InetSocketAddress(this.defaultPort));
			System.out.println("Port manager started on : "+this.defaultPort);
			while(true){
				Socket socketConnexion = socketEcoute.accept();
				System.out.println("Someone is connected on PortManager");
				
				new PortManagerThread(socketConnexion,this.ctrl).start();
				
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public int getRemoteTransfertPort(String ip, int remotePort, String fileName){
		Socket socket = new Socket();
		System.out.println("Getting RemoteTransfertPort");
		
		InetSocketAddress addrDest = new InetSocketAddress(ip,remotePort);
		int transferPort = -1;
		
		try {
			socket.connect(addrDest);
			byte[] bufE = new String("TP:?,FILENAME:"+fileName).getBytes();
			
			OutputStream os =  socket.getOutputStream();
			InputStream is = socket.getInputStream();
			os.write(bufE);
			System.out.println("RemoteTransfertPort Request sent");
	
			byte[] bufR = new byte[512];
			int lenBufR = 0;
			
			lenBufR = is.read(bufR);
			if(lenBufR == -1){
				System.out.println("Error while reading response");
			}
				
			String reponse = new String(bufR, 0 ,lenBufR);
			System.out.println("RemoteTransfertPortServer reply : "+reponse);
			
			try{
				transferPort = Integer.parseInt(reponse);
			}
			catch(NumberFormatException e){
				System.out.println("Error while getting port");
				transferPort = -1;
			}
			
			is.close();
			os.close();
			socket.close();
		} catch (IOException e) {
			System.out.println("Error while connecting to the server...");
			e.printStackTrace();
		}		
		
		
		return transferPort;
	}
	
	public static int getPortFromPool(){
		
		//TODO Choose a random port in the pool
		//TODO move ports to userdPort if it's okay

		int port = -1;
		if(availablePorts.size() <= 0){
			return -1;
		}
		port = availablePorts.get(0).intValue();
		
		return port;
	}
	
}
