package view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextPane;
import javax.swing.SwingWorker;

import core.Controller;
import java.awt.Color;

public class View {
	Controller controller = new Controller();

	private JFrame frame;
	private JTextPane textPane;


	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					View window = new View();
					window.frame.setVisible(true);
					window.threadd();
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public View() {
		initialize();
	}

	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 489, 697);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("FILAS NO SERVIDOR");
		lblNewLabel.setBounds(189, 19, 181, 14);
		frame.getContentPane().add(lblNewLabel);
		
		textPane = new JTextPane();
		textPane.setBackground(new Color(192, 192, 192));
		textPane.setEditable(false);
		textPane.setBounds(46, 44, 387, 582);
		frame.getContentPane().add(textPane);
	}
	
	public void threadd() {
		(new SwingWorker<Void, Void>() {
			@Override
			public Void doInBackground() {
				while (true) {
					try {
						Thread.sleep(3000);
						String panelContentQ = controller.getQueuesNames();
						textPane.setText(panelContentQ);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
			}
		}).execute();
	}
}
