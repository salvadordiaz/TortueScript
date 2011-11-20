package com.sfeir.gwt.tortugwt.client;


public interface Messages extends com.google.gwt.i18n.client.Messages {

	@DefaultMessage("Execute !")
	String execute();

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

}
