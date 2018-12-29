package com.fmi.mpr.hw.chat;

import java.io.*;
import java.net.*;
import java.util.Scanner;

class UDPClient{

	private static String username;
	private static final int PORT = 8888;
	private static Scanner readFrom;
	private static InetAddress IPAddress;
	private final static String IP = "224.0.0.3";
	private final static int BUFF_SIZE = 8192;
	
	
	private static Message deserialize(DatagramPacket request) throws IOException, ClassNotFoundException {
		ByteArrayInputStream bais = new ByteArrayInputStream(request.getData());
        ObjectInputStream ois = new ObjectInputStream(bais);
        Message message =  (Message) ois.readObject();
        ois.close();
        return message;
	}
	
	public static void main(String args[]) throws IOException{

		MulticastSocket clientSocket = new MulticastSocket(PORT);
		IPAddress = InetAddress.getByName(IP);
		clientSocket.joinGroup(IPAddress);
		
		readFrom = new Scanner(System.in);
		
		System.out.print("Please choose your username: ");
		username = readFrom.nextLine();
		
		Thread thread = new Thread(new WriteThread(readFrom, username, IPAddress, PORT, clientSocket));
		thread.start();	
		
		while(true) {
			byte[] receiveData = new byte[BUFF_SIZE];
			DatagramPacket receivedPacket = new DatagramPacket(receiveData, receiveData.length);
			while(true) {
				clientSocket.receive(receivedPacket);
				Message receivedMessage;
				try {
					receivedMessage = deserialize(receivedPacket);
					if(!receivedMessage.getName().equals(username)) {
						if(receivedMessage.getType() == Type.TEXT) {
							String toShow = new String(receivedMessage.getContent());
							System.out.println(receivedMessage.getName() + " writes: " + toShow);
						}else {
							save(receivedMessage, receivedMessage.getType());
						}
					}
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
				
		}

	private static void save(Message message, Type type) {
		String fileToWrite = message.getFilename() + "." + message.getFileExtension();

		if(message.isStartTransmission()) {
			try(FileOutputStream writer = new FileOutputStream(fileToWrite)){
			    writer.write(message.getContent());
			    writer.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}else if(message.isEndTransmission()){
			System.out.println(message.getName() + " sends " + message.getType() + " file named " + message.getFilename() + "." + message.getFileExtension());
		}else {
			try(FileOutputStream writer = new FileOutputStream(fileToWrite, true)){
			    writer.write(message.getContent());
			    writer.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}