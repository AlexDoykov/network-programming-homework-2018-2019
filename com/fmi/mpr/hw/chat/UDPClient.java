package com.fmi.mpr.hw.chat;

import java.io.*;
import java.net.*;
import java.util.Scanner;

class Message implements Serializable{

	private static final long serialVersionUID = 1L;
	private String name;
	private String content;
	
	public Message(String name, String content) {
		this.name = name;
		this.content = content;
	}
	
	public String getName() {
		return name;
	}
	
	public String getContent() {
		return content;
	}
	
}

class UDPClient{

	private static String username;
	protected static Scanner readFrom;
	protected static BufferedWriter writeTo;
	private static InetAddress IPAddress;
	
	
	static void sendVideo(String path, MulticastSocket clientSocket) {
		File fileToSend = new File(path);
		if(!fileToSend.exists()) {
			System.out.println("File doesn't exist!");
		}
		byte[] buffer = new byte[4096];
		int bytesRead = 0;
		try (FileInputStream fd = new FileInputStream(fileToSend)) {
			while ((bytesRead = fd.read(buffer, 0, 4096)) > 0) {
				DatagramPacket packet = new DatagramPacket(buffer, buffer.length, IPAddress, 8888);
				clientSocket.send(packet);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public static void main(String args[]) throws IOException{

		MulticastSocket clientSocket = new MulticastSocket(8888);
		IPAddress = InetAddress.getByName("224.0.0.3");
		clientSocket.joinGroup(IPAddress);
		readFrom = new Scanner(System.in);
		
		System.out.print("Please choose your username: ");
		username = readFrom.nextLine();
		
		Thread writeThread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				 // read the message sent to this client 
                String msg;
                while(true) {
		            try {
		            	System.out.println("What will you send?\n>>");
		            	String type = readFrom.nextLine();
		            	msg = readFrom.nextLine();
		            	if(type.equals("TEXT")) {
		            		Message message = new Message(username, msg);
		            		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		                    ObjectOutputStream oos = new ObjectOutputStream(baos);
		                    oos.writeObject(message);
		                    byte[] data = baos.toByteArray();
		            		DatagramPacket packet = new DatagramPacket(data, data.length, IPAddress, 8888);
							clientSocket.send(packet);
		            	}else if(type.equals("VIDEO")){
		            		System.out.println("SENDING VIDEO");
		            		sendVideo(msg, clientSocket);
		            	}else {
		            		//sendImage(msg);
		            	}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						System.out.println("Writing thread can't write");
						e.printStackTrace();
					} 
                }
			}
			
		});

		writeThread.start();	
		while(true) {
			byte[] receiveData = new byte[1024];
			DatagramPacket receivedPacket = new DatagramPacket(receiveData, receiveData.length);
			while(true) {
				try {
					clientSocket.receive(receivedPacket);
					ByteArrayInputStream bais = new ByteArrayInputStream(receiveData);
		            ObjectInputStream ois = new ObjectInputStream(bais);
		            Message receivedMessage = (Message) ois.readObject();
		            if(!receivedMessage.getName().equals(username)) {
						System.out.println("Received message: " + receivedMessage.getContent());
		            }
				}  catch (IOException e) {
					// TODO Auto-generated catch block
					System.out.println("Reading thread can't read");
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
				
		}
}