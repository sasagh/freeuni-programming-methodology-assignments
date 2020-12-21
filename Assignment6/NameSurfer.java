/*
 * File: NameSurfer.java
 * ---------------------
 * When it is finished, this program will implements the viewer for
 * the baby-name database described in the assignment handout.
 */

import acm.program.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.*;

public class NameSurfer extends ConsoleProgram implements NameSurferConstants {
	private JTextField nameInput;
	private JButton graphBtn;
	private JButton clearBtn;
/* Method: init() */
/**
 * This method has the responsibility for reading in the data base
 * and initializing the interactors at the bottom of the window.
 */
	public void init() {
		initializeObjectsAndAddActionListeners();
		NameSurferDataBase db = new NameSurferDataBase("names-data.txt");

		println(db.findEntry("sAlVadoR").toString());
		println(db.findEntry("Samantha").toString());
	}

	private void initializeObjectsAndAddActionListeners(){
		JLabel label = new JLabel("Name");
		add(label, SOUTH);
		nameInput = new JTextField(20);
		nameInput.setActionCommand("Graph");
		add(nameInput, SOUTH);
		graphBtn = new JButton("Graph");
		add(graphBtn, SOUTH);
		clearBtn = new JButton("Clear");
		add(clearBtn, SOUTH);

		nameInput.addActionListener(this);
		graphBtn.addActionListener(this);
		clearBtn.addActionListener(this);
	}

/* Method: actionPerformed(e) */
/**
 * This class is responsible for detecting when the buttons are
 * clicked, so you will have to define a method to respond to
 * button actions.
 */
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
			case "Graph" -> {
				println("Graph " + nameInput.getText());
				nameInput.setText("");
			}
			case "Clear" -> println("Clear");
		}
	}
}
