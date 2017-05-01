package gist.telecontrol;

import android.app.Service;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

public class LANBroadcastingThread extends Thread{

    private boolean mFinish;
    private String mName;
    private Service mService;
    private DatagramSocket mSocket;
    private int mCode;

    public LANBroadcastingThread(Service service, String name, int code){
        mService = service;
        mName = name;
        mCode = code;
    }

    public void run(){

        /* Implement this
        switch(mCode){
            case 0: CLIENT
                break;
            case 1: SERVER
                break;
            default: ERROR_MSG
                break;
        }
        */

        if(getMainInterface() == null){
            //Give information about the error
            return;
        }

        InetAddress ipaddr = getMainAddress(getMainInterface().getInetAddresses());

        if(ipaddr == null){
            //Give information about the error
            return;
        }

        InetAddress braddr = getBroadcastAddress(getMainInterface().getInterfaceAddresses(), ipaddr);

        if(braddr == null){
            //Give information about the error
            return;
        }

        byte [] request = new String("REQ: " + mName).getBytes();

        DatagramPacket requestPacket = new DatagramPacket(request, request.length);

        requestPacket.setAddress(braddr);

        requestPacket.setPort(48182);

        try{
            mSocket = new DatagramSocket(48181);
            mSocket.connect(braddr, 48182);
        }
        catch(SocketException se){
            //Give information about the error
            return;
        }

        mFinish = false;

        LANReceivingThread lanReceivingThread = new LANReceivingThread(mService, mSocket, 0);
        lanReceivingThread.start();

        while(!mFinish){

            try{
                mSocket.setBroadcast(true);
                mSocket.send(requestPacket);
            }
            catch(IOException ioe){
                mSocket.close();
                //Give information about the error
                return;
            }

            try{
                Thread.sleep(1000);
            } catch (InterruptedException iex){
                mFinish = true;
            }

        }

        mSocket.close();
    }

    public NetworkInterface getMainInterface(){
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            for(NetworkInterface interf : Collections.list(interfaces)){
                if(getMainAddress(interf.getInetAddresses()) != null &&
                        !getMainAddress(interf.getInetAddresses()).getHostAddress().equals("127.0.0.1"))
                    return interf;
            }
        } catch (SocketException se) {
            return null;
        }
        return null;
    }

    public InetAddress getMainAddress(Enumeration<InetAddress> addresses){
        for(InetAddress address : Collections.list(addresses)){
            if(address instanceof Inet4Address){
                return address;
            }
        }
        return null;
    }

    public InetAddress getBroadcastAddress(List<InterfaceAddress> iaddresses, InetAddress ipaddr){
        for(InterfaceAddress iaddress : iaddresses){
            if(iaddress.getAddress().equals(ipaddr)) return iaddress.getBroadcast();
        }
        return null;
    }

    public void setFinish(boolean finish){
        mFinish = finish;
    }

}