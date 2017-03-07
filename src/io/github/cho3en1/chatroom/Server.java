package io.github.cho3en1.chatroom;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
	private List<MyChannel> all = new ArrayList<MyChannel>();
	
	public static void main(String[] args) {
		new Server().start();
	}
	
	public void start() {
		try {
			ServerSocket server = new ServerSocket(8888);
			while(true) {
				Socket client = server.accept();
				MyChannel channel = new MyChannel(client);
				all.add(channel);
				new Thread(channel).start();;
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private class MyChannel implements Runnable {
		private DataInputStream dis;
		private DataOutputStream dos;
		private boolean isRunning = true;
		
		public MyChannel(Socket client) {
			try {
				dis = new DataInputStream(client.getInputStream());
				dos = new DataOutputStream(client.getOutputStream());
			} catch (IOException e) {
				isRunning = false;
				e.printStackTrace();
			}
		}
		
		private String receive() {
			try {
				return dis.readUTF();
			} catch (IOException e) {
				isRunning = false;
				all.remove(this);
				e.printStackTrace();
			}
			return "";
		}
		
		private void send(String msg) {
			try {
				if(msg==null && msg.equals("")) {
					return;
				}
				dos.writeUTF(msg);
				dos.flush();
			} catch (IOException e) {
				isRunning = false;
				e.printStackTrace();
			}
		}
		
		private void sendOthers() {
			String msg = receive();
			for(MyChannel other:all) {
				if(other==this) {
					continue;
				}
				other.send(msg);
			}
		}
		
		@Override
		public void run() {
			while(isRunning) {
				sendOthers();
			}
		}
		
	}
}
