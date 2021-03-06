import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

/**
 * 
 * @className MailFrame
 * @class CSCI 150
 * @programmer Anthony Rabon
 * @lastRevised 10-10-16
 *
 *This class is used to create the Graphical User Interface and all of the components that are seen in the window
 *such as the buttons and text fields that interact with the MailSystem class
 */

	public class MailFrame extends JFrame{
		
		//final integer values that will be used to set the size of the window
		private final int FRAME_WIDTH = 400;
		private final int FRAME_HEIGHT = 400;
		
		//creates variables that will be used in the GUI
		MailSystem mailSys;
		private String userName;
		private JPanel headerPanel;
		private JPanel optionPanel;
		private JScrollPane messagePane;
		private JPanel innerMessagePanel;
		private JButton loginLogoutButton;
		private JButton writeButton;
		private JButton inboxButton;
		private JButton sentButton;
		private JLabel helloLabel;
		private JTextArea messageField;
		
		/**
		 * Constructor that initializes all of the variables and components used in the GUI
		 */
		public MailFrame() {
			userName = "";//sets userName to nothing since noone will be initially logged in
			mailSys = new MailSystem();//creates MailSystem object to deliver messages, and get messages
			this.setSize(FRAME_WIDTH, FRAME_HEIGHT);
			//creates buttons that allow the user to interact with the program and mailsystem
			loginLogoutButton = new JButton("Login");
			writeButton = new JButton("Write Message");
			inboxButton = new JButton("Inbox");
			sentButton = new JButton("Outbox");
			helloLabel = new JLabel("Hello");
			//panels used to in-case multiple objects such as labels, buttons
			headerPanel = new JPanel();
			optionPanel = new JPanel();
			createTextField();//creates a text field that will display the users messages inbox/outbox
			messagePane = new JScrollPane(messageField);//make the textfield created above scrollable
			innerMessagePanel = new JPanel();
			addListeners();//add listeners to all of the buttons so that they can be interacted with
			//add items to each panel and lays out object within each panel 
			headerPanel.setLayout(new BorderLayout());
			helloLabel.setHorizontalAlignment(helloLabel.CENTER);
			headerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
			headerPanel.add(helloLabel, BorderLayout.CENTER);
			headerPanel.add(loginLogoutButton, BorderLayout.EAST);
			optionPanel.add(inboxButton);
			optionPanel.add(sentButton);
			optionPanel.add(writeButton);
			headerPanel.add(optionPanel, BorderLayout.PAGE_END);
			innerMessagePanel.setLayout(new BorderLayout());
			innerMessagePanel.setBorder(new EmptyBorder(10, 10, 10, 10));
			innerMessagePanel.add(messagePane);
			//add each of the panels containing objects to the main window
			this.add(headerPanel, BorderLayout.NORTH);
			this.add(innerMessagePanel, BorderLayout.CENTER);
		}
		
		/**
		 * Used to create the text field that displays the user's messages
		 */
		private void createTextField() {
			final int COLUMNS = 15;
			final int ROWS = 15;
			messageField = new JTextArea(ROWS, COLUMNS);
			messageField.setEditable(false);
			messageField.setText("Login to view messages.");
			}
		
		/**
		 * @className LoginLogoutListener
		 *
		 *Inner class that executes the code within the class anytime the Login/Logout button is clicked to either logout a user or login a user
		 */
		public class LoginLogoutListener implements ActionListener{
	
				public void actionPerformed(ActionEvent event) {
					//if the button says login then it will prompt the user for them to login and set username to what the user entered and sets the button
					//to say logout
					if(loginLogoutButton.getText().equals("Login")) {
						userName = getUserInput("Enter your username to log in.");
						if(!userName.equals("")) {
							helloLabel.setText("Hello, " + userName);
							loginLogoutButton.setText("Logout");
						}
					}
					//The button says logout and will set username back to the empty string and sets the button to say login
					else {
						userName = "";
						helloLabel.setText("Hello");
						messageField.setText("Login to view messages.");
						loginLogoutButton.setText("Login");
					}
				}	
			}
		
		/**
		 * @className LoginLogoutListener
		 *
		 *Inner class that executes the code within the class anytime the writeMessage button is clicked to allow the user to write a message to another
		 *user as long as someone is logged in
		 */
		public class WriteListener implements ActionListener{

			public void actionPerformed(ActionEvent event) {
				//if nobody is logged in will display an error message
				if(userName.equals("")) {
					notLoggedIn();
				}
				//else it will ask who to send the message to and the user will write the message line by line until an empty line is entered
				//then deliver that message to the user's inbox
				else {
					String recipientName = getUserInput("Who would you like to send this message to?");
					if(!recipientName.equals("")) {
						Message m = new Message(userName, recipientName);
						String appendText = "";
						do {
							appendText = getUserInput("Enter the body of the message to send or nothing to stop writing to the message.");
							if(!appendText.equals("")) {
								m.append(appendText);
							}
						}while(!appendText.equals(""));
						mailSys.deliver(m);
						messageField.setText("Your message has been delivered to " + recipientName + ".");
					}
				}
			}
		}
		
		/**
		 * @className InboxListener
		 *
		 *Inner class that executes the code within the class anytime the inbox button is clicked and will display the user's inbox
		 */
		public class InboxListener implements ActionListener{

			public void actionPerformed(ActionEvent event) {
				//if nobody is logged in will display error message
				if(userName.equals("")) {
					notLoggedIn();
				}
				//otherwise set the text in the message field to what is returned by the print messages function
				else {
					messageField.setText(mailSys.printMessages(userName, "inbox"));
				}
			}
		}
		
		/**
		 * @className SentListener
		 *
		 *Inner class that executes the code within the class anytime the sent button is clicked and will display the user's outbox
		 */
		public class SentListener implements ActionListener{

			public void actionPerformed(ActionEvent event) {
				//if nobody is logged in will display error message
				if(userName.equals("")) {
					notLoggedIn();
				}
				//otherwise set the text in the message field to what is returned by the print messages function
				else {
					messageField.setText(mailSys.printMessages(userName, "outbox"));
				}
			}
		}
		
		/**
		 * function that will add listeners to all buttons in the GUI
		 */
		private void addListeners() {
			loginLogoutButton.addActionListener(new LoginLogoutListener());
			writeButton.addActionListener(new WriteListener());
			inboxButton.addActionListener(new InboxListener());
			sentButton.addActionListener(new SentListener());
		}
		
		/**
		 * function that will display an error message because no user is logged in
		 */
		private void notLoggedIn() {
			final JPanel errorPanel = new JPanel();
			JOptionPane.showMessageDialog(errorPanel, "Error: You need to login first", "Noone Logged In", JOptionPane.ERROR_MESSAGE);
		}
		
		/**
		 * 
		 * @param s String s that is a prompt displayed to the user
		 * @return the string the user entered
		 * 
		 * Displays a window with a input box for the user to enter information into
		 */
		private String getUserInput(String s) {
			final JPanel inputPanel = new JPanel();
			String userInput = JOptionPane.showInputDialog(inputPanel, s);
			if(userInput == null ) {
				return "";
			}
			else {
				return userInput;
			}
		}
		
	}
