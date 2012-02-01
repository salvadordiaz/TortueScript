package fr.salvadordiaz.gwt.tortuescript.client.i18n;

import com.google.gwt.i18n.client.Constants;
import com.google.gwt.i18n.client.LocalizableResource.DefaultLocale;
import com.google.gwt.i18n.client.LocalizableResource.Generate;

@Generate(format = "com.google.gwt.i18n.rebind.format.PropertiesFormat")
@DefaultLocale("en")
public interface LocalizedCommands extends Constants {

	@DefaultStringValue("box")
	String boxExample();

	@DefaultStringValue("flower")
	String flowerExample();

	@DefaultStringValue("bigflower")
	String bigFlowerExample();

	@DefaultStringValue("pendown")
	String penDownCommand();

	@DefaultStringValue("penup")
	String penUpCommand();

	@DefaultStringValue("forward")
	String forwardCommand();

	@DefaultStringValue("backward")
	String backwardCommand();

	@DefaultStringValue("left")
	String leftCommand();

	@DefaultStringValue("right")
	String rightCommand();

	@DefaultStringValue("setX")
	String setXCommand();

	@DefaultStringValue("setY")
	String setYCommand();

	@DefaultStringValue("new")
	String newCommand();

	@DefaultStringValue("pencolor")
	String penColorCommand();

	@DefaultStringValue("canvascolor")
	String canvasColorCommand();

	@DefaultStringValue("drawstring")
	String drawStringCommand();

	@DefaultStringValue("fontsize")
	String fontSizeCommand();

	@DefaultStringValue("fontstyle")
	String fontStyleCommand();

	@DefaultStringValue("fontname")
	String fontNameCommand();

	@DefaultStringValue("pause")
	String pauseCommand();

	@DefaultStringValue("clear")
	String clearCommand();

	@DefaultStringValue("home")
	String homeCommand();

	@DefaultStringValue("hide")
	String hideCommand();

	@DefaultStringValue("show")
	String showCommand();

	@DefaultStringValue("make")
	String makeCommand();

	@DefaultStringValue("content")
	String contentCommand();

	@DefaultStringValue("sum")
	String sumCommand();

	@DefaultStringValue("subtract")
	String subtractCommand();

	@DefaultStringValue("multiply")
	String multiplyCommand();

	@DefaultStringValue("divide")
	String divideCommand();

	@DefaultStringValue("remainder")
	String remainderCommand();

	@DefaultStringValue("repeat")
	String repeatCommand();

	@DefaultStringValue("if")
	String ifCommand();

	@DefaultStringValue("to")
	String toCommand();

	@DefaultStringValue("end")
	String endCommand();
}
