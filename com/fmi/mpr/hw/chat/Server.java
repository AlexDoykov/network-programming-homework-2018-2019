package com.fmi.mpr.hw.chat;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class Server extends Thread {
	private DatagramSocket socket;
	private boolean running;
	private byte[] buff = new byte[1024];
	
	public Server(int port) throws SocketException {
		socket = new DatagramSocket(port);
	}
	
	public void run() {
		running = true;
		while(running) {
			DatagramPacket packet = new DatagramPacket(buff, buff.length);
			System.out.println("RUNNING");
			try {
				socket.receive(packet);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			InetAddress address = packet.getAddress();
			int port = packet.getPort();
			packet = new DatagramPacket(buff, buff.length, address, port);
			String received = new String(packet.getData(), 0, packet.getLength());
			if(received.equals("Exit")) {
				running = false;
				continue;
			}
			try {
				socket.send(packet);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		socket.close();
	}
}
	