/*
 * File: NameSurferGraph.java
 * ---------------------------
 * This class represents the canvas on which the graph of
 * names is drawn. This class is responsible for updating
 * (redrawing) the graphs whenever the list of entries changes or the window is resized.
 */

import acm.graphics.*;
import java.awt.event.*;
import java.util.*;
import java.awt.*;

public class NameSurferGraph extends GCanvas
	implements NameSurferConstants, ComponentListener {

	private Hashtable<NameSurferEntry, Color> entries;
	private static int graphCount = 0;
	/**
	* Creates a new NameSurferGraph object that displays the data.
	*/
	public NameSurferGraph() {
		addComponentListener(this);
		entries = new Hashtable<>();
	}
	
	/**
	* Clears the list of name surfer entries stored inside this class.
	*/
	public void clear() {
		entries = new Hashtable<>();
		graphCount = 0;
		update();
	}
	
	/* Method: addEntry(entry) */
	/**
	* Adds a new NameSurferEntry to the list of entries on the display.
	* Note that this method does not actually draw the graph, but
	* simply stores the entry; the graph is drawn by calling update.
	*/
	public void addEntry(NameSurferEntry entry) {
		entries.put(entry, getColor());
		graphCount++;
		update();
	}

	/**
	* Updates the display image by deleting all the graphical objects
	* from the canvas and then reassembling the display according to
	* the list of entries. Your application must call update after
	* calling either clear or addEntry; update is also called whenever
	* the size of the canvas changes.
	*/
	public void update() {
		removeAll();
		drawHorizontalLines();
		drawVerticalLinesAndAddYearLabels();
		drawGraphs();
	}

	private void drawGraphs() {
		for(int i=0; i<entries.size(); i++){
			for (int j = 0; j < NDECADES; j++) {
				NameSurferEntry entry = (NameSurferEntry)(entries.keySet().toArray()[i]);

				if(j == NDECADES - 1){
					drawGraphAndLabel(j, entry, null);
				}else{
					drawGraphAndLabel(j, entry, entry.getRank(j+1));
				}
			}
		}
	}

	private void drawGraphAndLabel(int decadeIndex, NameSurferEntry entry, Integer nextRank){
		double currentY, nextY;
		int rank = entry.getRank(decadeIndex);

		if(rank == 0){
			currentY = getHeight() - GRAPH_MARGIN_SIZE;
		}else{
			currentY = GRAPH_MARGIN_SIZE + rank * (getHeight() - 2.0*GRAPH_MARGIN_SIZE)/MAX_RANK;
		}

		if(nextRank != null){
			if(nextRank == 0){
				nextY = getHeight() - GRAPH_MARGIN_SIZE - LABEL_TEXT_MARGIN;
			}else{
				nextY = GRAPH_MARGIN_SIZE + nextRank * (getHeight() - 2.0*GRAPH_MARGIN_SIZE)/MAX_RANK;
			}

			if(rank != 0 || nextRank != 0) drawGraph(decadeIndex, currentY, nextY, entries.get(entry));
		}
		drawNameLabel(decadeIndex, entry, currentY);
	}

	private void drawNameLabel(int decadeIndex, NameSurferEntry entry, double y){
		String name = entry.getName();
		int rank = entry.getRank(decadeIndex);

		String labelText = name + (rank == 0 ? " *" : " " + rank);
		GLabel label = new GLabel(labelText);
		label.setColor(entries.get(entry));
		y += rank == 0 ? 0 : label.getAscent();
		add(label,decadeIndex * getWidth()*1.0/NDECADES + LABEL_TEXT_MARGIN, y - LABEL_TEXT_MARGIN);
	}

	private void drawGraph(int decadeIndex, double startY, double endY, Color color){
		GLine line = new GLine(
				decadeIndex * getWidth()*1.0/NDECADES,
				startY,
				(decadeIndex+1) * getWidth()*1.0/NDECADES,
				endY);
		line.setColor(color);
		add(line);
	}

	private Color getColor(){
		switch (graphCount%4){
			case 0 -> {
				return Color.BLACK;
			}
			case 1 -> {
				return Color.RED;
			}
			case 2 -> {
				return Color.BLUE;
			}
			default -> {
				return Color.GREEN;
			}
		}
	}

	private void drawVerticalLinesAndAddYearLabels(){
		for(int i=0; i<=NDECADES; i++){
			double lineX = i * getWidth()*1.0/NDECADES;
			double labelX = (i-1) * getWidth()*1.0/NDECADES + LABEL_TEXT_MARGIN;
			add(new GLine(lineX, 0, lineX, getHeight()));
			add(new GLabel(""+(START_DECADE+10*(i-1))), labelX, getHeight() - LABEL_TEXT_MARGIN);
		}
	}
	
	private void drawHorizontalLines(){
		GLine topLine = new GLine(0, GRAPH_MARGIN_SIZE, getWidth(), GRAPH_MARGIN_SIZE);
		GLine bottomLine = new GLine(0, getHeight()-GRAPH_MARGIN_SIZE, getWidth(), getHeight()-GRAPH_MARGIN_SIZE);
		add(topLine);
		add(bottomLine);
	}
	
	
	/* Implementation of the ComponentListener interface */
	public void componentHidden(ComponentEvent e) { }
	public void componentMoved(ComponentEvent e) { }
	public void componentResized(ComponentEvent e) { update(); }
	public void componentShown(ComponentEvent e) { }
}
