package MainRealDrop;

import java.util.ArrayList;

import Interfaces.I_Model;

public class ClientDataModel implements I_Model {
	private ArrayList<ClientData> clientList = new ArrayList<>();
	
	public ClientDataModel(){
		
	}
	
	@Override
	public int addClient(ClientData d){
		clientList.add(clientList.size(),d);
		return (clientList.size()-1); //Position of the last item inserted
	}
	@Override
	public void removeClient(int id){
		clientList.remove(id);
	}
	public boolean isPresent(String ip){
		for(ClientData s : this.clientList){
			if(s.getIp().equals(ip)){
				return true;
			}
		}
		return false;
	}

	@Override
	public int getModelSize() {
		return this.clientList.size();
	}

	@Override
	public ClientData getClientAt(int index) {
		return this.clientList.get(index);
	}
	
}
