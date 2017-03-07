package io.github.cho3en1.chatroom;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class Receive implements Runnable {
	private DataInputStream dis;
	private boolean isRunning = true;

	public Receive() {

	}

	public Receive(Socket client) {
		try {
			dis = new DataInputStream(client.getInputStream());
		} catch (IOException e) {
			isRunning = false;
			e.printStackTrace();
		}
	}

	public String receive() {
		String msg = "";
		try {
			msg = dis.readUTF();
		} catch (IOException e) {
			isRunning = false;
			e.printStackTrace();
		}
		return msg;
	}

	@Override
	public void run() {
		while (true) {
			System.out.println(receive());
		}
	}
}