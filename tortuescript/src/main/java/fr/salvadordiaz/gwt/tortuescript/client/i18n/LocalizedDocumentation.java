package fr.salvadordiaz.gwt.tortuescript.client.i18n;

import com.google.gwt.i18n.client.LocalizableResource.DefaultLocale;
import com.google.gwt.i18n.client.LocalizableResource.Generate;
import com.google.gwt.safehtml.shared.SafeHtml;

@Generate(format = "com.google.gwt.i18n.rebind.format.PropertiesFormat")
@DefaultLocale("en")
public interface LocalizedDocumentation extends com.google.gwt.i18n.client.Messages {

	@DefaultMessage("Commands are entered on individual lines. TortueScript commands and variables are not case-sensitive. The following commands are currently supported by TortueScript")
	String intro();
	
	@DefaultMessage("Move forward, producing a line")
	SafeHtml forwardDescription();

	@DefaultMessage("50")
	SafeHtml forwardArgs();

	@DefaultMessage("Move backward, producing a line")
	SafeHtml backwardDescription();

	@DefaultMessage("50")
	SafeHtml backwardArgs();

	@DefaultMessage("Rotate the cursor left by an amount of degrees")
	SafeHtml leftDescription();

	@DefaultMessage("90")
	SafeHtml leftArgs();

	@DefaultMessage("Rotate the cursor right by an amount of degrees")
	SafeHtml rightDescription();

	@DefaultMessage("90")
	SafeHtml rightArgs();

	@DefaultMessage("Move the cursor to the specified X location")
	SafeHtml setXDescription();

	@DefaultMessage("10")
	SafeHtml setXArgs();

	@DefaultMessage("Move the cursor to the specified Y location")
	SafeHtml setYDescription();

	@DefaultMessage("20")
	SafeHtml setYArgs();

	@DefaultMessage("Moves the cursor to the center of the screen")
	SafeHtml homeDescription();

	@DefaultMessage("Causes subsequent drawing commands (forward/backward) to be drawn")
	SafeHtml penDownDescription();

	@DefaultMessage("Causes subsequent drawing commands (forward/backward) to <b>not</b> be drawn")
	SafeHtml penUpDescription();

	@DefaultMessage("Change the pen color. Use a color name or specify red, green, blue, and alpha values")
	SafeHtml penColorDescription();

	@DefaultMessage("GREEN<br/>{0} 128 0 128 255")
	SafeHtml penColorArgs(String commandName);

	@DefaultMessage("Draw the specified string at the current location")
	SafeHtml drawStringDescription();

	@DefaultMessage("Hello World!")
	SafeHtml drawStringArgs();

	@DefaultMessage("Use the specified font name when drawing messages. Accepts css fonts")
	SafeHtml fontNameDescription();

	@DefaultMessage("serif")
	SafeHtml fontNameArgs();

	@DefaultMessage("Use the specified font size when drawing messages")
	SafeHtml fontSizeDescription();

	@DefaultMessage("18")
	SafeHtml fontSizeArgs();

	@DefaultMessage("Use the specified style when drawing messages. Accepts css font styles")
	SafeHtml fontStyleDescription();

	@DefaultMessage("bold")
	SafeHtml fontStyleArgs();

	@DefaultMessage("Clears the screen")
	SafeHtml clearDescription();

	@DefaultMessage("Starts a new drawing")
	SafeHtml newDescription();

	@DefaultMessage("Add two values together and put the result into the specified variable")
	SafeHtml sumDescription();

	@DefaultMessage("mysum = 10 + 20")
	SafeHtml sumArgs();

	@DefaultMessage("Subtract two values and put the result into the specified variable")
	SafeHtml subtractDescription();

	@DefaultMessage("mysubtraction = 30 - 10")
	SafeHtml subtractArgs();

	@DefaultMessage("Multiply two values and put the result into the specified variable")
	SafeHtml multiplyDescription();

	@DefaultMessage("mymult = 2 * 3")
	SafeHtml multiplyArgs();

	@DefaultMessage("Divide two values and put the result into the specified variable")
	SafeHtml divideDescription();

	@DefaultMessage("mydiv = 10 / 2")
	SafeHtml divideArgs();

	@DefaultMessage("Define a variable with the specified value")
	SafeHtml makeDescription();

	@DefaultMessage("myvar = 200")
	SafeHtml makeArgs();

	@DefaultMessage("Repeat all commands until <b>{0} {1}</b> is reached for the specified number of times")
	SafeHtml repeateDescription(String end, String command);

	@DefaultMessage("4<br />{1} 50<br />{2} 90<br />{3} {0}")
	SafeHtml repeatArgs(String repeat, String forward, String left, String end);

	@DefaultMessage("Execute the commands until <b>{0} {1}</b> is reached, if the specified condition is true. Conditions are: = (equals), &lt; (less than), &gt; (greater than), ! (not equal)")
	SafeHtml ifDescription(String end, String command);

	@DefaultMessage("10 &lt; 100<br />{1} 50<br />{2} {0}")
	SafeHtml ifArgs(String ifComm, String forward, String end);

	@DefaultMessage("Define a set of commands that can be called using the specified name")
	SafeHtml toDescription();

	@DefaultMessage("LINE<br/>{1} 30<br/>{2} {0}<br/>LINE")
	SafeHtml toArgs(String to, String forward, String end);
}
