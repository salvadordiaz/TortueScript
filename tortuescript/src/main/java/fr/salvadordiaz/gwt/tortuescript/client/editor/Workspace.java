package fr.salvadordiaz.gwt.tortuescript.client.editor;

import static com.google.common.base.Strings.*;

import com.google.common.base.Joiner;
import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import fr.salvadordiaz.gwt.tortuescript.client.Messages;

public class Workspace extends Composite implements WorkspaceDisplay {

	interface WorkspaceUiBinder extends UiBinder<Widget, Workspace> {
	}

	private static final String ERROR_STYLE = "error";
	private static final double PEN_WIDTH = 1;

	@UiField
	Canvas canvas;
	@UiField
	TextArea textArea;
	@UiField
	Button executeButton;
	@UiField
	TextBox nameInput;
	@UiField
	Button saveButton;

	private final Context2d context;
	private final Messages messages;
	private final Joiner fontJoiner = Joiner.on(" ").skipNulls();

	private String fontStyle = null;
	private String fontName = "sans-serif";
	private String fontSize = "8px";
	private boolean penDown = true;
	private double currentX;
	private double currentY;
	private double currentAngle;

	public Workspace() {
		WorkspaceUiBinder uiBinder = GWT.create(WorkspaceUiBinder.class);
		initWidget(uiBinder.createAndBindUi(this));
		context = canvas.getContext2d();
		messages = GWT.create(Messages.class);
		executeButton.setText(messages.execute());
		saveButton.setText(messages.save());
		//set the initial position to the center of the canvas
		home();
	}

	@UiFactory
	Canvas createCanvas() {
		return Canvas.createIfSupported();
	}

	@UiHandler("nameInput")
	void errorIfEmptyName(BlurEvent event) {
		if (nullToEmpty(nameInput.getText()).trim().isEmpty()) {
			nameInput.addStyleName(ERROR_STYLE);
			saveButton.setEnabled(false);
		}
	}

	@UiHandler("nameInput")
	void clearErrorOnFocus(FocusEvent event) {
		nameInput.removeStyleName(ERROR_STYLE);
		saveButton.setEnabled(true);
	}

	private void updatePosition(double length, boolean forward) {
		double direction = forward ? 1.0 : -1.0;
		double newX = currentX + direction * length * Math.cos(currentAngle);
		double newY = currentY + length * Math.sin(currentAngle);
		if (penDown) {
			context.beginPath();
			context.setLineWidth(PEN_WIDTH);
			context.moveTo(currentX, currentY);
			context.lineTo(newX, newY);
			context.closePath();
			context.stroke();
		}
		this.currentX = newX;
		this.currentY = newY;
	}

	@Override
	public HasClickHandlers getExecuteButton() {
		return executeButton;
	}

	@Override
	public HasClickHandlers getSaveButton() {
		return saveButton;
	}

	@Override
	public HasText getCodeEditor() {
		return textArea;
	}

	@Override
	public HasText getNameEditor() {
		return nameInput;
	}

	@Override
	public void penDown() {
		penDown = true;
	}

	@Override
	public void penUp() {
		penDown = false;
	}

	@Override
	public void setX(double newX) {
		currentX = newX;
	}

	@Override
	public void setY(double newY) {
		currentY = newY;
	}

	@Override
	public void forward(final double length) {
		updatePosition(length, true);
	}

	@Override
	public void backward(final double length) {
		updatePosition(length, false);
	}

	@Override
	public void left(double angle) {
		currentAngle -= Math.PI * angle / 180.0;
	}

	@Override
	public void right(double angle) {
		currentAngle += Math.PI * angle / 180.0;
	}

	@Override
	public void home() {
		this.currentX = canvas.getCoordinateSpaceWidth() / 2;
		this.currentY = canvas.getCoordinateSpaceHeight() / 2;
		this.currentAngle = 0;
		context.moveTo(currentX, currentY);
	}

	@Override
	public void newCommand() {
		context.clearRect(0, 0, canvas.getCoordinateSpaceWidth(), canvas.getCoordinateSpaceHeight());
		home();
	}

	@Override
	public void penColor(String stringColor) {
		context.save();
		context.setStrokeStyle(CssColor.make(stringColor));
		//TODO: is the save method used correctly ?
	}

	@Override
	public void penColor(int red, int green, int blue, int alpha) {
		context.save();
		context.setStrokeStyle(CssColor.make("rgba(" + red + "," + green + "," + blue + "," + alpha + ")"));
		//TODO: is the save method used correctly ?
	}

	@Override
	public void canvasColor(String stringColor) {
		//TODO: is it possible ?
	}

	@Override
	public void canvasColor(int red, int green, int blue, int alpha) {
		//TODO: is it possible ?
	}

	@Override
	public void drawString(String string) {
		context.save();
		context.translate(currentX, currentY);
		context.rotate(currentAngle);
		context.strokeText(string, currentX, currentY);
		context.restore();
		//FIXME: are the transformations used correctly ?
	}

	private void updateFont() {
		context.save();
		context.setFont(fontJoiner.join(fontStyle, fontSize, fontName));
	}

	@Override
	public void fontSize(int fontSize) {
		this.fontSize = fontSize + "px";
		updateFont();
	}

	@Override
	public void fontStyle(String fontStyle) {
		this.fontStyle = fontStyle;
		updateFont();
	}

	@Override
	public void fontName(String fontName) {
		this.fontName = fontName;
		updateFont();
	}
}
