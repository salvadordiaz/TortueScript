package fr.salvadordiaz.gwt.tortuescript.client.editor.ui;

import com.google.common.base.Joiner;
import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.ClickEvent;
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
import com.google.gwt.user.client.ui.PopupPanel.PositionCallback;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import fr.salvadordiaz.gwt.tortuescript.client.editor.WorkspaceDisplay;
import fr.salvadordiaz.gwt.tortuescript.client.i18n.Messages;

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
	Button syntaxButton;
	@UiField
	Button executeButton;
	@UiField
	Button stopButton;
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
	private double currentRadians;

	private final TortueScriptReference syntaxPopup = new TortueScriptReference();

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
		stopButton.setText(messages.pauseResume());
		saveButton.setText(messages.save());
		syntaxButton.setText(messages.showSyntax());
		//set the initial position to the center of the canvas
		home();
	}

	private final PositionCallback positionCallback = new PositionCallback() {
		@Override
		public void setPosition(int offsetWidth, int offsetHeight) {
			final int left = syntaxButton.getAbsoluteLeft();
			final int top = syntaxButton.getAbsoluteTop() + syntaxButton.getOffsetHeight();
			syntaxPopup.setPopupPosition(left, top);
		}
	};

	@UiHandler({ "syntaxButton" })
	void showSyntax(ClickEvent event) {
		syntaxPopup.setPopupPositionAndShow(positionCallback);
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
		if (nameInput.getValue().trim().isEmpty()) {
			nameInput.addStyleName(ERROR_STYLE);
			saveButton.setEnabled(false);
		} else {
			nameInput.removeStyleName(ERROR_STYLE);
			saveButton.setEnabled(true);
		}
	}

	@Override
	public HasClickHandlers getExecuteButton() {
		return executeButton;
	}

	@Override
	public HasClickHandlers getStopButton() {
		return stopButton;
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

	private void updateCanvas(double radians) {
		frontContext.clearRect(0, 0, canvasWidth, canvasHeight);
		frontContext.drawImage(lineContext.getCanvas(), 0, 0);
		frontContext.save();
		frontContext.translate(currentX, currentY);
		frontContext.rotate(radians);
		frontContext.drawImage(tortueElement, -12, -10);
		frontContext.restore();
	}

	@Override
	public void updatePosition(double length, double radians) {
		double newX = currentX + length * Math.cos(radians);
		double newY = currentY + length * Math.sin(radians);
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
		updateCanvas(radians);
	}

	@Override
	public void turn(double degrees) {
		currentRadians = currentRadians + Math.PI * degrees / 180;
		updateCanvas(currentRadians);
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
	public void home() {
		this.currentX = canvas.getCoordinateSpaceWidth() / 2;
		this.currentY = canvas.getCoordinateSpaceHeight() / 2;
		this.currentRadians = 0;
		lineContext.moveTo(currentX, currentY);
		updatePosition(0, currentRadians);
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
	public void canvasColor(String stringColor) {
		canvas.getElement().getStyle().setBackgroundColor(stringColor);
	}

	@Override
	public void drawString(String string) {
		lineContext.save();
		lineContext.translate(currentX, currentY);
		lineContext.rotate(currentRadians);
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

	@Override
	public double getCurrentAngle() {
		return currentRadians;
	}
}
