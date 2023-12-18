package core;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import javax.jms.*;

import interfaces.Server;

public class Controller implements Server {
	private Registry registry = null;
	private Remote remoteObject = null;
	MiddlewareService middlewareService = new MiddlewareService();

	public Controller() {
		try {
			middlewareService.initializeMiddleaware();

			registry = LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
			remoteObject = UnicastRemoteObject.exportObject(this, 0);
			registry.bind("Server", remoteObject);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void createQueue(String userName) throws RemoteException {
		try {
			middlewareService.createQueue(userName);
		} catch (JMSException e) {
		}

	}

	@Override
	public void saveMessage(String userName, String message) throws RemoteException {
		try {
			MessageProducer producer = middlewareService.createMessageProducer(userName);
			TextMessage textMessage = middlewareService.createTextMessage(message);
			producer.send(textMessage);
			middlewareService.deleteMessageProducer(producer);
		} catch (JMSException e) {
		}

	}

	@Override
	public List<String> getUserMessages(String userName) throws RemoteException {
		List<String> messages = new ArrayList<>();
		try {
			MessageConsumer consumer = middlewareService.createMessageConsumer(userName);
			messages = middlewareService.getMessagesFromQueue(userName, consumer);
			middlewareService.deleteMessageConsumer(consumer);
			return messages;

		} catch (JMSException e) {
			List<String> messagesError = new ArrayList<>();
			return messagesError;
		}
	}

	public String getQueuesNames() {
		try {
			List<String> namesList = middlewareService.getQueuesNames();
			StringBuilder queueNames = new StringBuilder();
			namesList.forEach(queue -> {
				queueNames.append("FILA: " + queue + "\n\n");
			});
			return queueNames.toString();

		} catch (JMSException e) {
			e.printStackTrace();
			return "";
		}
	}
}
