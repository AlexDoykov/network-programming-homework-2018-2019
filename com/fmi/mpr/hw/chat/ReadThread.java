package com.fmi.mpr.hw.chat;

import java.io.BufferedReader;
import java.io.IOException;

public class ReadThread implements Runnable{
	
	private BufferedReader readFrom;
	
	
	public ReadThread(BufferedReader readFrom){
		this.readFrom = readFrom;
	}

	@Override
	public void run() {
		while (true) { 
                // read the message sent to this client 
                String msg;
				try {
					msg = readFrom.readLine();
	                System.out.println(msg); 
				} catch (IOException e) {
					// TODO Auto-generated catch block
					System.out.println("Reading thread can't read");
					e.printStackTrace();
				} 
        } 
		
	}

}
