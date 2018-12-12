package uk.ac.tees;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

import javax.net.SocketFactory;

import uk.ac.tees.net.Connection;
import uk.ac.tees.net.NetworkConstants;
import uk.ac.tees.net.message.StringMessage;
import uk.ac.tees.net.util.SocketUtility;

public class Client {

	public static void main(String[] args) {
		Socket socket = SocketUtility.createSocket(SocketFactory.getDefault(), "localhost", NetworkConstants.SERVER_PORT);

		try (Scanner scanner = new Scanner(System.in); Connection connection = new Connection(socket)) {

			while (scanner.hasNextLine()) {
				String message = scanner.nextLine();
				connection.write(new StringMessage(message));
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}