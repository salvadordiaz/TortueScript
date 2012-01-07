package fr.salvadordiaz.gwt.tortuescript.client.editor;

import static com.google.common.base.Strings.*;

import com.google.common.base.Joiner;
import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
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

	interface Tortue extends ClientBundle {
		@Source("tortue.png")
		ImageResource icon();
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
	@UiField
	Tortue tortue;

	private final Context2d lineContext;
	private final Context2d frontContext;
	private final Messages messages;
	private final Joiner fontJoiner = Joiner.on(" ").skipNulls();
	private ImageElement tortueElement;
	private final int canvasWidth;
	private final int canvasHeight;

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
		frontContext = canvas.getContext2d();
		canvasHeight = canvas.getCoordinateSpaceHeight();
		canvasWidth = canvas.getCoordinateSpaceWidth();
		lineContext = getBufferCanvas().getContext2d();
		tortueElement = Document.get().createImageElement();
		tortueElement.setSrc(tortue.icon().getSafeUri().asString());
		messages = GWT.create(Messages.class);
		executeButton.setText(messages.execute());
		saveButton.setText(messages.save());
		//set the initial position to the center of the canvas
		home();
	}

	private Canvas getBufferCanvas() {
		final Canvas bufferCanvas = Canvas.createIfSupported();
		bufferCanvas.setCoordinateSpaceHeight(canvasHeight);
		bufferCanvas.setCoordinateSpaceWidth(canvasWidth);
		return bufferCanvas;
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
		GWT.log("Going " + length + " pixels " + (forward ? "fwd":"bwd"));
		double direction = forward ? 1.0 : -1.0;
		double newX = currentX + direction * length * Math.cos(currentAngle);
		double newY = currentY + length * Math.sin(currentAngle);
		if (penDown) {
			lineContext.beginPath();
			lineContext.setLineWidth(PEN_WIDTH);
			lineContext.moveTo(currentX, currentY);
			lineContext.lineTo(newX, newY);
			lineContext.closePath();
			lineContext.stroke();
		}
		this.currentX = newX;
		this.currentY = newY;
		updateCanvas();
	}

	private void updateCanvas(){
		frontContext.clearRect(0, 0, canvasWidth, canvasHeight);
		frontContext.drawImage(lineContext.getCanvas(), 0, 0);
		frontContext.save();
		frontContext.translate(currentX, currentY);
		frontContext.rotate(currentAngle);
		frontContext.drawImage(tortueElement, -12, -10);
		frontContext.restore();
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
		updateCanvas();
	}

	@Override
	public void right(double angle) {
		currentAngle += Math.PI * angle / 180.0;
		updateCanvas();
	}

	@Override
	public void home() {
		this.currentX = canvas.getCoordinateSpaceWidth() / 2;
		this.currentY = canvas.getCoordinateSpaceHeight() / 2;
		this.currentAngle = 0;
		lineContext.moveTo(currentX, currentY);
		updatePosition(0, true);
	}

	@Override
	public void newCommand() {
		lineContext.clearRect(0, 0, canvas.getCoordinateSpaceWidth(), canvas.getCoordinateSpaceHeight());
		home();
	}

	@Override
	public void penColor(String stringColor) {
		lineContext.save();
		lineContext.setStrokeStyle(CssColor.make(stringColor));
	}

	@Override
	public void penColor(int red, int green, int blue, int alpha) {
		lineContext.save();
		lineContext.setStrokeStyle(CssColor.make("rgba(" + red + "," + green + "," + blue + "," + alpha + ")"));
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
		lineContext.save();
		lineContext.translate(currentX, currentY);
		lineContext.rotate(currentAngle);
		lineContext.strokeText(string, 0, 0);
		lineContext.restore();
		//FIXME: are the transformations used correctly ?
	}

	private void updateFont() {
		lineContext.save();
		lineContext.setFont(fontJoiner.join(fontStyle, fontSize, fontName));
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
