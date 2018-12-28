package com.fmi.mpr.hw.chat;

import java.io.*;
import java.net.*;
import java.util.Scanner;

class UDPClient{
	protected static Scanner readFrom;
	protected static BufferedWriter writeTo;

	public static void main(String args[]) throws IOException{

		MulticastSocket clientSocket = new MulticastSocket(8888);
		InetAddress IPAddress = InetAddress.getByName("224.0.0.3");
		clientSocket.joinGroup(IPAddress);
		readFrom = new Scanner(System.in);

		Thread readThread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				 // read the message sent to this client 
					byte[] receiveData = new byte[1024];
					DatagramPacket receivedPacket = new DatagramPacket(receiveData, receiveData.length);
					while(true) {
						try {
							clientSocket.receive(receivedPacket);
							String message = new String(receivedPacket.getData(), receivedPacket.getOffset(), receivedPacket.getLength());
							System.out.println("Received message: " + message);
						}  catch (IOException e) {
							// TODO Auto-generated catch block
							System.out.println("Reading thread can't read");
							e.printStackTrace();
						}
					}
			}
			
		});
		
		Thread writeThread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				 // read the message sent to this client 
                String msg;
                while(true) {
		            try {
						msg = readFrom.nextLine();
						DatagramPacket packet = new DatagramPacket(msg.getBytes(), msg.length(), IPAddress, 8888);
						clientSocket.send(packet);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						System.out.println("Writing thread can't write");
						e.printStackTrace();
					} 
                }
			}
			
		});
		
		readThread.start();
		writeThread.start();	
				
		}
}