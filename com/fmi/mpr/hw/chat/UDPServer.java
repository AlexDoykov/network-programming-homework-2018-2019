package com.fmi.mpr.hw.chat;

import java.io.*;
import java.net.*;

class UDPServer{
	
	
	public static void sendUDPMessage(DatagramSocket serverSocket, String message, String ip, int port) throws IOException {
			
		InetAddress to = InetAddress.getByName(ip);
		byte[] msg = message.getBytes();
		DatagramPacket packet = new DatagramPacket(msg, msg.length, to, port);
		serverSocket.send(packet);
	}
		
	public static void main(String[] args) throws IOException {
		try(DatagramSocket serverSocket = new DatagramSocket(8888)) {
			byte[] receiveData = new byte[1024];
			while(true) {
				DatagramPacket receivedPacket = new DatagramPacket(receiveData, receiveData.length);
				serverSocket.receive(receivedPacket);
				String message = new String(receivedPacket.getData(), receivedPacket.getOffset(), receivedPacket.getLength());
				sendUDPMessage(serverSocket, message, "224.0.0.3", 8888);
			}
		}
	}
}