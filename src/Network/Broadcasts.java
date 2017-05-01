package Network;

import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;

public class Broadcasts
{
    public static String getFirstBroadcastAddress()
    {
        //HashSet<InetAddress> listOfBroadcasts = new HashSet<InetAddress>();
        Enumeration list;
        try {
            list = NetworkInterface.getNetworkInterfaces();

            while(list.hasMoreElements()) {
                NetworkInterface iface = (NetworkInterface) list.nextElement();

                if(iface == null) continue;

                if(!iface.isLoopback() && iface.isUp()) {
                    //System.out.println("Found non-loopback, up interface:" + iface);

                    Iterator it = iface.getInterfaceAddresses().iterator();
                    while (it.hasNext()) {
                        InterfaceAddress address = (InterfaceAddress) it.next();
                        //System.out.println("Found address: " + address);
                        if(address == null) continue;
                        InetAddress broadcast = address.getBroadcast();
                        if(broadcast != null) 
                        {
                            System.out.println("Found broadcast: " + broadcast);
                            
                            //listOfBroadcasts.add(broadcast);
                            return broadcast.getHostAddress();
                        }
                        
                    }
                }
            }
        } catch (SocketException ex) {
            System.err.println("Error while getting network interfaces");
            ex.printStackTrace();
        }
		return "192.168.1.255";

        // return listOfBroadcasts;
    }
}