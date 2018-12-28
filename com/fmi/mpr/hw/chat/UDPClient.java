package com.fmi.mpr.hw.chat;

import java.io.*;
import java.net.*;

class UDPClient implements Runnable{
	public static void main(String args[]){
		
		Thread thread = new Thread(new UDPClient());
		thread.start();
    }

	@Override
	public void run() {
		try(MulticastSocket clientSocket = new MulticastSocket(8888)) {
			InetAddress IPAddress = InetAddress.getByName("224.0.0.3");
			clientSocket.joinGroup(IPAddress);
			byte[] receiveData = new byte[1024];
			while(true) {
				System.out.println("Waiting for salvation... or a message");
				DatagramPacket receivedPacket = new DatagramPacket(receiveData, receiveData.length);
				clientSocket.receive(receivedPacket);
				String message = new String(receivedPacket.getData(), receivedPacket.getOffset(), receivedPacket.getLength());
				System.out.println("Received message: " + message);
			}
		} catch (SocketException e) {
			System.out.println("Could not create a datagram socket for the client.");
			e.printStackTrace();
		} catch (UnknownHostException e) {
			System.out.println("Could not find the given host.");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Could not read from the file descriptor.");
			e.printStackTrace();
		}
	}
}