package core;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;

import javax.jms.*;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;

public class MiddlewareService {
	ConnectionFactory connectionFactory;
	Connection connection;
	Session session;
	private static String url = ActiveMQConnection.DEFAULT_BROKER_URL;

	
	public void initializeMiddleaware() throws JMSException {
		connectionFactory = new ActiveMQConnectionFactory(url);
		connection = connectionFactory.createConnection();
		connection.start();
		session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		
	}
	
	public MessageProducer createMessageProducer(String queueName) throws JMSException {
		Destination destination = session.createQueue(queueName);
		MessageProducer producer = session.createProducer(destination);
		return producer;
	}
	
	public void deleteMessageProducer(MessageProducer producerOrPublisher) throws JMSException {
		producerOrPublisher.close();
	}
	
	public MessageConsumer createMessageConsumer(String queueName) throws JMSException {
		Destination destination = session.createQueue(queueName);
		MessageConsumer client = session.createConsumer(destination);
		return client;
	}
	
	public void deleteMessageConsumer(MessageConsumer client) throws JMSException {
		client.close();
	}
	
	public TextMessage createTextMessage(String message) throws JMSException {
		TextMessage textMessage = session.createTextMessage(message);
		return textMessage;
	}
	
	public Destination createQueue(String queueName) throws JMSException {
		Destination destination = session.createQueue(queueName);
		MessageProducer producer = session.createProducer(destination);
		producer.close();
		return destination;
	}
	
	public List<String> getMessagesFromQueue(String queueName, MessageConsumer consumer) throws JMSException {
		ActiveMQQueue queue = (ActiveMQQueue) session.createQueue(queueName);
		QueueBrowser browser = session.createBrowser((ActiveMQQueue) queue);
		Enumeration<?> enumeration = browser.getEnumeration();
		List<String> messages = new ArrayList<>();

		while (enumeration.hasMoreElements()) {
			Message message = (Message) consumer.receive();

			if (message instanceof TextMessage) {
				TextMessage textMessage = (TextMessage) message;
				messages.add(textMessage.getText());
			}
			enumeration.nextElement();
		}
		return messages;
	}
	
	public List<String> getQueuesNames() throws JMSException {
		Set<ActiveMQQueue> queues = ((ActiveMQConnection) connection).getDestinationSource().getQueues();
		List<String> queueNames = new ArrayList<>();
		queues.forEach(fila -> {
			String nome = fila.getPhysicalName();
			queueNames.add(nome);

		});
		return queueNames;
	}
}
