package com.fmi.mpr.hw.chat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

public class WriteThread implements Runnable{
	
	private BufferedWriter writeTo;
	private BufferedReader readFrom;
	
	public WriteThread(BufferedWriter writeTo, BufferedReader readFrom){
		this.writeTo = writeTo;
		this.readFrom = readFrom;
	}

	@Override
	public void run() {
		while (true) { 
                String msg;
				try {
					msg = readFrom.readLine();
					writeTo.write(msg); 
				} catch (IOException e) {
					// TODO Auto-generated catch block
					System.out.println("Writing thread can't write");
					e.printStackTrace();
				} 
        } 
		
	}
}
