package Network;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

import Interfaces.I_Controller;
import MainRealDrop.ClientData;

public class DiscoveryServiceReceiver extends Thread{
	private DatagramSocket socket;
	private I_Controller ctrl;
	
	private int port = 20451;
	
	private String localIP = "";
	
	public DiscoveryServiceReceiver(I_Controller ctrl){
		this.ctrl = ctrl;
		
		try {
	        socket = new DatagramSocket(null);	  
	        socket.bind(new InetSocketAddress(this.port));
	    } catch (Exception e) {
	        System.err.println("DSR : Connection failed. " + e.getMessage());
	    }
		
		try {
			localIP = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			System.out.println("Error : Unknow local IP (will accept all packets...)");
		}
	}
	
	public void run(){
		System.out.println("Start listening");
		listen();
	}
	
	public void listen() {
	   
		 while (true) {
		    try {
		        byte[] buf = new byte[1024];
		        DatagramPacket packet = new DatagramPacket(buf,buf.length);
		        socket.receive(packet);
		       
		        if(!packet.getAddress().getHostAddress().equals(this.localIP) || true){//Get rid of true
		        
			        String message = new String(buf);
			        ClientData data = new ClientData();
			        
			        data.setClientName(packet.getAddress().getHostName());
			        data.setIp(packet.getAddress().getHostAddress());
			        for(String s : message.split(",")){
			        	String key = s.split(":")[0];
			        	String value = s.split(":")[1];
			        	System.out.println("Key:"+key+", value : "+value);
			        	
			        	if(key.equals("PORT")){
			        		try{
			        			data.setFileManagerPort((int)Long.parseLong(value));
			        		}
			        		catch(NumberFormatException e){
			        			System.out.println("Error whith remote port (not a number) (received :"+value+")");
			        		}
			        	}
			        	else if(key.equals("NAME")){
			        		data.setClientName(value);
			        	}
			        	else{
			        		System.out.println("Unrecognized option : "+key);
			        	}
			        }
			      
			        ctrl.newClientDiscovered(data);
		        
		        }
		        
		    } catch (Exception e) {
		        System.err.println(e.getMessage());
		    }
		}
	        
	}
	
	
	
}
