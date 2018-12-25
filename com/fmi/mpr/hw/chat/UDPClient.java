package com.fmi.mpr.hw.chat;

import java.io.*;
import java.net.*;

class UDPClient{
	public static void main(String args[]){
		BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
		try(DatagramSocket clientSocket = new DatagramSocket()) {
			InetAddress IPAddress = InetAddress.getByName("localhost");
			byte[] sendData = new byte[1024];
			byte[] receiveData = new byte[1024];
			String sentence = inFromUser.readLine();
			sendData = sentence.getBytes();
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9876);
			clientSocket.send(sendPacket);
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			clientSocket.receive(receivePacket);
			String modifiedSentence = new String(receivePacket.getData());
			System.out.println("FROM SERVER:" + modifiedSentence);
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