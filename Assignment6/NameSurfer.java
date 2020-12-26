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
	private JButton deleteBtn;
	private NameSurferGraph graph;
	private NameSurferDataBase db;
/* Method: init() */
/**
 * This method has the responsibility for reading in the data base
 * and initializing the interactors at the bottom of the window.
 */
	public void init() {
		graph = new NameSurferGraph();
		add(graph);
		db = new NameSurferDataBase(NAMES_DATA_FILE);
		initializeInteractorsAndAddActionListeners();
	}

	/** Initializes interactors which should be present on window */
	private void initializeInteractorsAndAddActionListeners(){
		JLabel label = new JLabel("Name");

		nameInput = new JTextField(TEXT_FIELD_SIZE);
		nameInput.setActionCommand("Graph");
		graphBtn = new JButton("Graph");
		clearBtn = new JButton("Clear");
		deleteBtn = new JButton("Delete");

		add(label, SOUTH);
		add(nameInput, SOUTH);
		add(graphBtn, SOUTH);
		add(deleteBtn, SOUTH);
		add(clearBtn, SOUTH);

		nameInput.addActionListener(this);
		graphBtn.addActionListener(this);
		deleteBtn.addActionListener(this);
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
				if(entry != null) graph.addEntry(entry);
				nameInput.setText("");
			}
			case "Delete" -> {
				NameSurferEntry entry = db.findEntry(nameInput.getText());
				if(entry != null) graph.removeEntry(entry);
				nameInput.setText("");
			}
			case "Clear" -> {
				graph.clear();
			}
		}
	}
}
