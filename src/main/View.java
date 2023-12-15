package main;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextPane;
import javax.swing.SwingWorker;

public class View {
	Controller controller = new Controller();

	private JFrame frame;
	private JTextPane textPane;

	/**
	 * Launch the application.
	 */
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

	/**
	 * Create the application.
	 */
	public View() {
		initialize();
		
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 489, 697);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("FILAS NO SERVIDOR");
		lblNewLabel.setBounds(180, 48, 181, 14);
		frame.getContentPane().add(lblNewLabel);
		
		textPane = new JTextPane();
		textPane.setBounds(60, 105, 330, 490);
		frame.getContentPane().add(textPane);
	}
	
	public void threadd() {
		(new SwingWorker<Void, Void>() {
			@Override
			public Void doInBackground() {
				while (true) {
					try {
						Thread.sleep(7000);
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
