
package githubborrra.service;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/* Класс Окна-Консоли, где 2 метода: 1. выводящий само окно (startWindow)
 * и отправляющий инфу в это окно */

public class ConsoleWindow {

	private static JTextArea textArea; // Area for displaying console output
	private static JTextField inputField; // Field for user input

	/*
	 * мое Консоль-Окно создается в Методе. Поэтому видимо оно и закрывается само,
	 * конда метод заканчивает свою работу
	 */

	/* 1. Метод используется в Методах основного класса № 7 и 8 */

	public static void startWindow(String my) {

		// Create the main frame

		JFrame frame = new JFrame(my);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(620, 400); // ширина и высота соответственно
		frame.setLayout(new BorderLayout());

		// Create a JTextArea for console output

		textArea = new JTextArea();
		textArea.setEditable(false); // Make it non-editable
		JScrollPane scrollPane = new JScrollPane(textArea); // Add scrolling capability
		frame.add(scrollPane, BorderLayout.CENTER);

		// Create a JTextField for user input

		inputField = new JTextField();
		frame.add(inputField, BorderLayout.SOUTH);

		// Add action listener for the input field

		inputField.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				String input = inputField.getText(); // Get text from input field

				if (input.equalsIgnoreCase("end")) {
					
					toConsole("Closing the console...");

					frame.dispose();

					// System.exit(0);

				} else {

					toConsole(input); // Append to the console
				}

				inputField.setText(""); // Clear the input field
			}
		});

		// Set up the frame visibility
		frame.setVisible(true);

	} // конец Метода 1. startWindow

	/* 2. Метод используется в Методах основного класса № 5 и 6 */

	public static void toConsole(String text) {

		textArea.append(text + "\n"); // Append text with a newline

		textArea.setCaretPosition(textArea.getDocument().getLength()); // Scroll to bottom

//        if (text.equals("end")) {
//        	
//        	toConsole("\nClosing the console...");
//        	ServiceMethods.windowShow("the program is closing");
//        	
//        	System.exit(0);
//        	 
//        }

	} // конец метода 2. toConsole

} // конец 4-го Класса ConsoleWindow
