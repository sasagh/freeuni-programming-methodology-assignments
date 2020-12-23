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

public class NameSurfer extends Program implements NameSurferConstants {
	private JTextField nameInput;
	private JButton graphBtn;
	private JButton clearBtn;
	private NameSurferGraph graph;
	private NameSurferDataBase db;
/* Method: init() */
/**
 * This method has the responsibility for reading in the data base
 * and initializing the interactors at the bottom of the window.
 */
	public void init() {
		graph = new NameSurferGraph();
		db = new NameSurferDataBase(NAMES_DATA_FILE);
		add(graph);
		initializeObjectsAndAddActionListeners();
	}

	private void initializeObjectsAndAddActionListeners(){
		JLabel label = new JLabel("Name");
		add(label, SOUTH);
		nameInput = new JTextField(15);
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
				NameSurferEntry entry = db.findEntry(nameInput.getText());
				if(entry != null){
					graph.addEntry(entry);
				}
				nameInput.setText("");
			}
			case "Clear" -> {
				graph.clear();
			}
		}
	}
}
