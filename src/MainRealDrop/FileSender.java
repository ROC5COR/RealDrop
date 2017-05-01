package MainRealDrop;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

import Network.ClientPinger;


public class FileSender {
	private String remoteHost = "";
	private int remotePort = -1;
	
	private Socket socket = null;
	
	public FileSender(String remoteHost, int port){
		this.remoteHost = remoteHost;
		this.remotePort = port;
		
		try {
			InetSocketAddress remoteAddr = new InetSocketAddress(this.remoteHost, this.remotePort);
			this.socket = new Socket();
			this.socket.connect(remoteAddr);
			
			
			System.out.println("Sender connected to Host");
			
			//byte[] dataReceived = new byte[2048];
			//int bytesRead = is.read(dataReceived, 0, dataReceived.length);
			//String data = new String(dataReceived,0,bytesRead);
			
			//data = data.trim();//Delete withespace and \n
			
		} catch (IOException e1) {
			System.out.println("Error in FileSender : "+e1.toString());
		}
		
	}
	public int send(String filePath){
		
		try {
			OutputStream os = socket.getOutputStream();
			
			
			FileInputStream fis = new FileInputStream(new File(filePath));
			int totaSize = fis.available();
			byte[] bufE = new byte[512];
			int charRead = 0;
			int sizeRead = 0;
			
			while ((charRead = fis.read(bufE)) != -1) {
				sizeRead = sizeRead + charRead;//To check the final size read
				os.write(bufE,0,charRead);
				os.flush();//Maybe change, place flush after loop
			}
			
			System.out.println("Bytes read(file)/total : "+sizeRead+"/"+totaSize);
			fis.close();
			socket.close();
		} catch (IOException e) {
			System.out.println("Error in FileSender(send) : "+e.getMessage());
			return -1;
		}
		
		return -1;
	}
}
