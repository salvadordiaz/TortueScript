package com.sfeir.gwt.tortugwt.client.sidebar;

import com.google.common.base.Joiner;
import com.sfeir.gwt.tortugwt.client.LocalizedCommands;

public class Examples {

	private final LocalizedCommands commands;
	private final Joiner tokenJoiner = Joiner.on(" ");
	private final Joiner lineJoiner = Joiner.on("\n");

	public Examples(LocalizedCommands commands) {
		this.commands = commands;
	}

	public String getBox() {
		String boxFunctionName = "BOX";
		return createProgram(//
				with(commands.toCommand(), boxFunctionName)//
				, with(commands.repeatCommand(), 4)//
				, with(commands.forwardCommand(), 100)//
				, with(commands.leftCommand(), 90)//
				, with(commands.endCommand(), commands.repeatCommand())//
				, with(commands.endCommand(), commands.toCommand())//
				, with(commands.penColorCommand(), "GREEN")//
				, boxFunctionName);
	}

	public String getFlower() {
		String flowerFunctionName = "FLOWER";
		return createProgram(//
				with(commands.toCommand(), flowerFunctionName)//
				, with(commands.repeatCommand(), 9)//
				, with(commands.rightCommand(), 30)//
				, with(commands.forwardCommand(), 30)//
				, with(commands.leftCommand(), 40)//
				, with(commands.forwardCommand(), 20)//
				, with(commands.leftCommand(), 130)//
				, with(commands.forwardCommand(), 30)//
				, with(commands.leftCommand(), 40)//
				, with(commands.forwardCommand(), 20)//
				, with(commands.leftCommand(), 20)//
				, with(commands.forwardCommand(), 10)//
				, with(commands.leftCommand(), 120)//
				, with(commands.forwardCommand(), 10)//
				, with(commands.endCommand(), commands.repeatCommand())//
				, with(commands.endCommand(), commands.toCommand())//
				, commands.clearCommand()//
				, commands.homeCommand()//
				, flowerFunctionName);
	}

	public String getOtherFlower() {
		String index = "index";
		String color = "color";
		String red = "red";
		String blue = "blue";
		String green = "green";
		String forwardAmount = "forwardamount";
		return createProgram(//
				commands.clearCommand()//
				, commands.homeCommand()//
				, with(commands.makeCommand(), index, "=", 1)//
				, with(commands.makeCommand(), color, "=", 200)//
				, with(commands.repeatCommand(), 500)//
				, with(commands.makeCommand(), red, "=", color)//
				, with(commands.divideCommand(), blue, "=", color, "/", 2)//
				, with(commands.makeCommand(), green, "=", color)//
				, with(commands.penColorCommand(), red, green, blue, 255)//
				, with(commands.sumCommand(), forwardAmount, "=", index, "+", 1)//
				, with(commands.forwardCommand(), forwardAmount)//
				, with(commands.rightCommand(), 70)//
				, with(commands.sumCommand(), index, "=", index, "+", 1)//
				, with(commands.sumCommand(), color, "=", color, "+", 1)//
				, with(commands.ifCommand(), color, ">", 255)//
				, with(commands.makeCommand(), color, "=", 200)//
				, with(commands.endCommand(), commands.ifCommand())//
				, with(commands.endCommand(), commands.repeatCommand()));
	}

	private String with(Object... tokens) {
		return tokenJoiner.join(tokens);
	}

	private String createProgram(String... commands) {
		return lineJoiner.join(commands);
	}
}
