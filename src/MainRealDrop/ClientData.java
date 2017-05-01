package MainRealDrop;

public class ClientData {
	private String ip;//IP addr of the remote client
	private int fileManagerPort = -1;// remote port to get data port
	private String clientName = "ND";
	
	public String getClientName() {
		return clientName;
	}
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public int getFileManagerPort() {
		return fileManagerPort;
	}
	public void setFileManagerPort(int fileManagerPort) {
		this.fileManagerPort = fileManagerPort;
	}
	
}
