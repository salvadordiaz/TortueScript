package fr.salvadordiaz.gwt.tortuescript.client.i18n;

import com.google.gwt.i18n.client.LocalizableResource.Generate;
import com.google.gwt.i18n.client.LocalizableResource.DefaultLocale;;

@Generate(format = "com.google.gwt.i18n.rebind.format.PropertiesFormat")
@DefaultLocale("en")
public interface Messages extends com.google.gwt.i18n.client.Messages {

	/*Sidebar*/

	@DefaultMessage("Your drawings")
	String savedItems();

	@DefaultMessage("Box")
	String boxExample();

	@DefaultMessage("Flower")
	String flowerExample();

	@DefaultMessage("Big Flower")
	String bigFlowerExample();

	/*Workspace*/

	@DefaultMessage("Editor")
	String workspaceHeader();

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

	/*Docs*/

	@DefaultMessage("TortueScript reference syntax")
	String syntaxTitle();

	@DefaultMessage("Movements")
	String movementsTab();

	@DefaultMessage("Pen")
	String penTab();

	@DefaultMessage("Math")
	String mathTab();

	@DefaultMessage("Functions")
	String functionsTab();

	/*Sharing*/
	@DefaultMessage("Program : {0}")
	String gistPopupTitle(String gistName);

	@DefaultMessage("Enter your email")
	String enterYourEmail();

	@DefaultMessage("OK")
	String saveEmail();

	@DefaultMessage("Your program was successfully shared.")
	String programWasShared();

	@DefaultMessage("Error while sharing program.")
	String sharingError();

	@DefaultMessage("Find more programs")
	String loadPrograms();

	/*Search*/

	@DefaultMessage("Search")
	String searchProgramsHeader();

	@DefaultMessage("Programs shared by other users")
	String searchProgramsSubheader();

	@DefaultMessage("User")
	String programTableUser();

	@DefaultMessage("Program")
	String programTableName();

	@DefaultMessage("Language")
	String programTableLocale();

	@DefaultMessage("Unable to search programs.")
	String errorSearchingPrograms();

	@DefaultMessage("Open")
	String open();

	@DefaultMessage("Unable to load program {0} from GitHub.")
	String errorLoadingProgram(String filename);

	@DefaultMessage("Show on GitHub")
	String showOnGithub();

	@DefaultMessage("Shared by {0}")
	String author(String user);

	@DefaultMessage("Share on GitHub !")
	String shareOnGithub();

	@DefaultMessage("Share")
	String shareButton();

	@DefaultMessage("Helps you identify your programs on GitHub.")
	String emailHelp();

	@DefaultMessage("Delete")
	String deleteProgram();

	@DefaultMessage("French")
	String french();

	@DefaultMessage("English")
	String english();

	@DefaultMessage("Pause / Resume")
	String pauseResume();
}