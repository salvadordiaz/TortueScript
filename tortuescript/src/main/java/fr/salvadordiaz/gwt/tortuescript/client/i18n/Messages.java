package fr.salvadordiaz.gwt.tortuescript.client.i18n;

import com.google.gwt.i18n.client.LocalizableResource.Generate;

@Generate(format = "com.google.gwt.i18n.rebind.format.PropertiesFormat")
public interface Messages extends com.google.gwt.i18n.client.Messages {

	/*Sidebar*/

	@DefaultMessage("Try some examples")
	String examples();

	@DefaultMessage("Your drawings")
	String savedItems();

	@DefaultMessage("You don''t have any saved drawings")
	String noSavedItems();

	@DefaultMessage("Delete all saved drawings")
	String clearSavedItems();


	@DefaultMessage("TortueScript reference syntax")
	String syntaxTitle();

	/*Workspace*/

	@DefaultMessage("Execute !")
	String execute();

	@DefaultMessage("Save program as : ")
	String save();

	@DefaultMessage("Show syntax")
	String showSyntax();
	
	@DefaultMessage("Example")
	String example();

	/*Errors*/

	@DefaultMessage("Unclosed REPEAT statement (opened at line {0})")
	String unclosedRepeatStatement(int lineIndex);

	@DefaultMessage("Unclosed IF statement (opened at line {0})")
	String unclosedIfStatement(int lineIndex);

	@DefaultMessage("Unrecognized comparison operator ( \"{0}\" ) in IF statement at line {1}")
	String unrecognizedIfOperator(String operator, int lineIndex);

	@DefaultMessage("Unclosed TO statement (opened at line {0})")
	String unclosedFunctionStatement(int lineIndex);

	@DefaultMessage("Unrecognized statement at line {0}")
	String unrecognizedStatement(int lineIndex);

	@DefaultMessage("Movements")
	String movementsTab();

	@DefaultMessage("Pen")
	String penTab();

	@DefaultMessage("Math")
	String mathTab();

	@DefaultMessage("Functions")
	String functionsTab();

}
