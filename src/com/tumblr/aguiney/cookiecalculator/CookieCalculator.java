package com.tumblr.aguiney.cookiecalculator;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class CookieCalculator extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	JComboBox<Building> buildingList;
	Building current = Building.CURSOR;
	JTextField[] fields = new JTextField[8];
	JButton calculateButton = new JButton("Calculate");
	JButton clearButton = new JButton("Clear");
	String currentText;
	int buildings = 0;
	long cookies = 0L;
	
	public CookieCalculator() {
		super("Cookie Calculator");
		
		try {
			String fileName = "res/cookie_calc.png";
			java.net.URL url = ClassLoader.getSystemResource(fileName);
			Toolkit kit = Toolkit.getDefaultToolkit();
			Image img = kit.createImage(url);
			setIconImage(img);
		} catch (Exception e) {
			e.printStackTrace();
		}

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JLabel[] labels = new JLabel[8];
		String[] labelNames = {" Cookies Banked:", " Available Quantity:", 
				" # Buildings:", " Cost:", " Sell:", " Refund Amount:", 
				" Target:", " Cookies Needed:"};
		
		buildingList = new JComboBox<Building>(Building.values());
		
		JPanel container = new JPanel(new BorderLayout());
		JPanel comboPanel = new JPanel();
		JPanel buttonPanel = new JPanel();
		JPanel fieldPanel = new JPanel(new GridLayout(4, 2, 20, 20));
		
		buildingList.setSelectedIndex(0);
		buildingList.addActionListener(this);
		comboPanel.add(buildingList);
		
		calculateButton.addActionListener(this);
		clearButton.addActionListener(this);
		buttonPanel.add(calculateButton);
		buttonPanel.add(clearButton);
		
		for (int i = 0; i < fields.length; i++) {
			fields[i] = new JTextField();
			
			if (i % 2 != 0) {
				fields[i].setEditable(false);
			} else {
				fields[i].addActionListener(this);
			}
			
			labels[i] = new JLabel(labelNames[i]);
			labels[i].setLabelFor(fields[i]);
			fieldPanel.add(labels[i]);
			fieldPanel.add(fields[i]);
		}
		container.add(comboPanel, BorderLayout.NORTH);
		container.add(fieldPanel, BorderLayout.CENTER);
		container.add(buttonPanel, BorderLayout.SOUTH);
		add(container);
		setSize(650, 300);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == buildingList) {
			current = (Building) buildingList.getSelectedItem();
		}
		
		if (e.getSource() == calculateButton ||
				e.getSource() instanceof JTextField) {
			try {
				displayCost();
				displayQuantity();
				displayRefund();
				displayCookiesNeeded();
			} catch (NumberFormatException nfe) {
				JOptionPane.showMessageDialog(this, "Error: "
						+ "Invalid number(s)!");
			}
		}
		
		if (e.getSource() == clearButton) {
			clearFields();
		}
	}
	
	/* Displays the amount of buildings that can be purchased given
	 * the amount of banked cookies. */
	public void displayQuantity() {
		if (!fields[0].getText().equals("")) {
			cookies = Long.parseLong(fields[0].getText());
			currentText = String.valueOf(current.quantity(cookies, buildings));
			fields[1].setText(currentText);
		} else {
			fields[0].setText("0");
			fields[1].setText("0");
		}
	}
	
	/* Displays the current cost of the building given the amount
	 * of purchased buildings. */
	public void displayCost() {
		if (!fields[2].getText().equals("")) {
			buildings = Integer.parseInt(fields[2].getText());
			currentText = String.valueOf(current.price(buildings));
			fields[3].setText(currentText);
		} else {
			fields[2].setText("0");
			// Displays the base price of the building.
			fields[3].setText(String.valueOf(current.getBaseCost()));
		}
	}
	
	/* Displays the refund value of "amount" buildings.
	 * If "amount" is greater than "buildings", then the new value for "amount"
	 * becomes the current value of "buildings". */
	public void displayRefund() {
		if (!fields[4].getText().equals("")) {
			int amount = Integer.parseInt(fields[4].getText());
			if (amount > buildings) fields[4].setText(String.valueOf(buildings));
			currentText = String.valueOf(current.refund(amount, buildings));
			fields[5].setText(currentText);
		} else {
			fields[4].setText("0");
			fields[5].setText("0.0");
		}
	}
	
	/* Displays the cumulative price of "target" buildings - the
	 * cumulative price of buildings already owned - banked cookies. */
	public void displayCookiesNeeded() {
		if (!fields[6].getText().equals("")) {
			int target = Integer.parseInt(fields[6].getText());
			double diff = current.cumulativePrice(target) - 
					current.cumulativePrice(buildings);	
			currentText = String.valueOf(diff - cookies);
			fields[7].setText(currentText);
		} else {
			fields[6].setText("0");
			// Calls itself so that it has a meaningful value to display.
			displayCookiesNeeded();
		}
	}
	
	// Self-explanatory.
	public void clearFields() {
		for (int i = 0; i < fields.length; i++) {
			fields[i].setText("");
		}
	}
	
	public static void main(String[] args) {
		CookieCalculator ch = new CookieCalculator();
		ch.setVisible(true);
	}
}