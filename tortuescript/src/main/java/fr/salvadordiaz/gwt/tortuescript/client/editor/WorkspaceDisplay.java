package fr.salvadordiaz.gwt.tortuescript.client.editor;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.IsWidget;

public interface WorkspaceDisplay extends IsWidget {

	HasText getCodeEditor();

	HasClickHandlers getExecuteButton();

	HasClickHandlers getStopButton();
	
	HasClickHandlers getSaveButton();

	HasText getNameEditor();

	void newCommand();

	void penDown();

	void penUp();

	void home();

	void setX(double newX);

	void setY(double newY);

	void penColor(String stringColor);

	void canvasColor(String stringColor);

	void drawString(String string);

	void fontSize(int fontSize);

	void fontStyle(String fontStyle);

	void fontName(String fontName);

	void turn(double value);

	double getCurrentAngle();

	void updatePosition(double i, double currentAngle);
}
