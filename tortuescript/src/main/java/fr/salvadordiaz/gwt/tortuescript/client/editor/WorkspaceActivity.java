package fr.salvadordiaz.gwt.tortuescript.client.editor;

import static com.google.common.base.Strings.*;
import static com.google.common.collect.Iterables.*;
import static com.google.common.collect.Lists.*;
import static com.google.common.collect.Maps.*;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceChangeRequestEvent;
import com.google.gwt.storage.client.Storage;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

import fr.salvadordiaz.gwt.tortuescript.client.LocalizedCommands;
import fr.salvadordiaz.gwt.tortuescript.client.Messages;

public class WorkspaceActivity extends AbstractActivity {

	private final WorkspaceDisplay workspace;
	private final LocalizedCommands localizedCommands;
	private final Messages messages;

	private final Map<String, Double> userVariables = newHashMap();
	private final Map<String, List<Iterable<String>>> functions = newHashMap();

	private final Splitter lineSplitter = Splitter.on("\n").omitEmptyStrings().trimResults();
	private final Splitter tokenSplitter = Splitter.on(" ");
	private final Joiner stringJoiner = Joiner.on(" ");

	private String programName;

	@Inject
	public WorkspaceActivity(WorkspaceDisplay workspace, LocalizedCommands localizedCommands, Messages messages, EventBus eventBus) {
		this.workspace = workspace;
		this.localizedCommands = localizedCommands;
		this.messages = messages;
		bind(eventBus);
	}

	public void setPlace(Place place) {
		programName = nullToEmpty(((WorkspacePlace) place).getProgramName()).trim();
		if (!programName.isEmpty()) {
			final String program = Storage.getLocalStorageIfSupported().getItem(programName);
			workspace.getCodeEditor().setText(program);
			execute();
		}
	}

	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		panel.setWidget(workspace);
	}

	private void bind(EventBus eventBus) {
		workspace.getExecuteButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				execute();
			}
		});
		workspace.getSaveButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				save();
			}
		});
		eventBus.addHandler(PlaceChangeRequestEvent.TYPE, new PlaceChangeRequestEvent.Handler() {
			@Override
			public void onPlaceChangeRequest(PlaceChangeRequestEvent event) {
				System.out.println("Place change was requested, TODO: cancel if there are unsaved changes");
			}
		});
	}

	private void save() {
		final String nameToSave = nullToEmpty(workspace.getNameEditor().getText()).trim();
		final String programToSave = nullToEmpty(workspace.getCodeEditor().getText()).trim();
		if (!nameToSave.isEmpty() && !programToSave.isEmpty()) {
			Storage.getLocalStorageIfSupported().setItem(nameToSave, programToSave);
		}
	}

	private void execute() {
		final List<String> lines = ImmutableList.copyOf(lineSplitter.split(workspace.getCodeEditor().getText()));
		final List<Iterable<String>> lineTokens = transform(lines, splitTokens);
		interpret(lineTokens);
	}

	private final Function<String, Iterable<String>> splitTokens = new Function<String, Iterable<String>>() {
		@Override
		public Iterable<String> apply(String input) {
			return tokenSplitter.split(input);
		}
	};

	private void interpret(List<Iterable<String>> linesAsTokens) {
		for (int lineIndex = 0; lineIndex < linesAsTokens.size(); lineIndex++) {
			Iterable<String> lineTokens = linesAsTokens.get(lineIndex);
			String command = getFirst(lineTokens, "");
			{
				if (command.startsWith(";")) {
					// Comment, do nothing
				} else if (command.equalsIgnoreCase(localizedCommands.forwardCommand())) {
					workspace.forward(getValue(getLast(lineTokens)));
				} else if (command.equalsIgnoreCase(localizedCommands.backwardCommand())) {
					workspace.backward(getValue(getLast(lineTokens)));
				} else if (command.equalsIgnoreCase(localizedCommands.leftCommand())) {
					workspace.left(getValue(getLast(lineTokens)));
				} else if (command.equalsIgnoreCase(localizedCommands.rightCommand())) {
					workspace.right(getValue(getLast(lineTokens)));
				} else if (command.equalsIgnoreCase(localizedCommands.setXCommand())) {
					workspace.setX(getValue(getLast(lineTokens)));
				} else if (command.equalsIgnoreCase(localizedCommands.setYCommand())) {
					workspace.setY(getValue(getLast(lineTokens)));
				} else if (command.equalsIgnoreCase(localizedCommands.penDownCommand())) {
					workspace.penDown();
				} else if (command.equalsIgnoreCase(localizedCommands.penUpCommand())) {
					workspace.penUp();
				} else if (command.equalsIgnoreCase(localizedCommands.newCommand())) {
					workspace.newCommand();
				} else if (command.equalsIgnoreCase(localizedCommands.penColorCommand())) {
					if (size(lineTokens) == 2) {
						workspace.penColor(getLast(lineTokens));
					} else {
						int red = getValue(get(lineTokens, 1)).intValue();
						int green = getValue(get(lineTokens, 2)).intValue();
						int blue = getValue(get(lineTokens, 3)).intValue();
						int alpha = getValue(get(lineTokens, 4)).intValue();
						workspace.penColor(red, green, blue, alpha);
					}
				} else if (command.equalsIgnoreCase(localizedCommands.canvasColorCommand())) {
					if (size(lineTokens) == 2) {
						workspace.canvasColor(getLast(lineTokens));
					} else {
						int red = getValue(get(lineTokens, 1)).intValue();
						int green = getValue(get(lineTokens, 2)).intValue();
						int blue = getValue(get(lineTokens, 3)).intValue();
						int alpha = getValue(get(lineTokens, 4)).intValue();
						workspace.canvasColor(red, green, blue, alpha);
					}
				} else if (command.equalsIgnoreCase(localizedCommands.drawStringCommand())) {
					workspace.drawString(stringJoiner.join(skip(lineTokens, 1)));
				} else if (command.equalsIgnoreCase(localizedCommands.fontSizeCommand())) {
					workspace.fontSize(getValue(getLast(lineTokens)).intValue());
				} else if (command.equalsIgnoreCase(localizedCommands.fontStyleCommand())) {
					workspace.fontStyle(getLast(lineTokens));
				} else if (command.equalsIgnoreCase(localizedCommands.fontNameCommand())) {
					workspace.fontName(getLast(lineTokens));
				} else if (command.equalsIgnoreCase(localizedCommands.clearCommand())) {
					workspace.newCommand();
				} else if (command.equalsIgnoreCase(localizedCommands.homeCommand())) {
					workspace.home();
				} else if (command.equalsIgnoreCase(localizedCommands.makeCommand())) {
					userVariables.put(get(lineTokens, 1).toLowerCase(), getValue(get(lineTokens, 3)));
				} else if (command.equalsIgnoreCase(localizedCommands.contentCommand())) {
					String variableName = getLast(lineTokens);
					Window.alert(variableName + " : " + getValue(variableName));
				} else if (command.equalsIgnoreCase(localizedCommands.sumCommand())) {
					handleVariableMath(lineTokens);
				} else if (command.equalsIgnoreCase(localizedCommands.subtractCommand())) {
					handleVariableMath(lineTokens);
				} else if (command.equalsIgnoreCase(localizedCommands.multiplyCommand())) {
					handleVariableMath(lineTokens);
				} else if (command.equalsIgnoreCase(localizedCommands.divideCommand())) {
					handleVariableMath(lineTokens);
				} else if (command.equalsIgnoreCase(localizedCommands.remainderCommand())) {
					handleVariableMath(lineTokens);
				} else if (command.equalsIgnoreCase(localizedCommands.hideCommand())) {
					//				tortueCanvas.hideTurtle();
				} else if (command.equalsIgnoreCase(localizedCommands.showCommand())) {
					//				tortueCanvas.showTurtle();
				} else if (command.equalsIgnoreCase(localizedCommands.repeatCommand())) {
					int unclosedRepeatCommands = 1;//to detect nested repeats
					ImmutableList.Builder<Iterable<String>> linesToRepeat = ImmutableList.builder();
					for (int lineAfterRepeat = lineIndex + 1; lineAfterRepeat < linesAsTokens.size(); lineAfterRepeat++) {
						Iterable<String> lineToRepeat = linesAsTokens.get(lineAfterRepeat);
						if (get(lineToRepeat, 0).equalsIgnoreCase(localizedCommands.repeatCommand())) {
							unclosedRepeatCommands++;
						}
						if (size(lineToRepeat) >= 2//
								&& get(lineToRepeat, 0).equalsIgnoreCase(localizedCommands.endCommand())
								&& get(lineToRepeat, 1).equalsIgnoreCase(localizedCommands.repeatCommand())) {
							unclosedRepeatCommands--;
							if (unclosedRepeatCommands == 0) {
								lineIndex = lineAfterRepeat + 1;
								break;
							}
						}
						linesToRepeat.add(lineToRepeat);
					}
					if (unclosedRepeatCommands != 0) {
						throw new IllegalStateException(messages.unclosedRepeatStatement(lineIndex));
					}
					int repeats = getValue(getLast(lineTokens)).intValue();
					for (int i = 0; i < repeats; i++) {
						interpret(linesToRepeat.build());
					}
				} else if (command.equalsIgnoreCase(localizedCommands.ifCommand())) {
					int unclosedIfs = 1;
					ImmutableList.Builder<Iterable<String>> conditionedLines = ImmutableList.builder();
					for (int lineAfterIf = lineIndex + 1; lineAfterIf < linesAsTokens.size(); lineAfterIf++) {
						Iterable<String> conditionedLine = linesAsTokens.get(lineAfterIf);
						if (get(conditionedLine, 0).equalsIgnoreCase(localizedCommands.ifCommand())) {
							unclosedIfs++;
						}
						if (size(conditionedLine) >= 2//
								&& get(conditionedLine, 0).equalsIgnoreCase(localizedCommands.endCommand())//
								&& get(conditionedLine, 1).equalsIgnoreCase(localizedCommands.ifCommand())) {
							unclosedIfs--;
							if (unclosedIfs == 0) {
								lineIndex = lineAfterIf + 1;
								break;
							}
						}
						conditionedLines.add(conditionedLine);
					}
					if (unclosedIfs != 0) {
						throw new IllegalStateException(messages.unclosedIfStatement(lineIndex));
					}
					if (isConditionTrue(get(lineTokens, 2), getValue(get(lineTokens, 1)), getValue(get(lineTokens, 3)), lineIndex)) {
						interpret(conditionedLines.build());
					}
				} else if (command.equalsIgnoreCase(localizedCommands.toCommand())) {
					boolean endTagFound = false;
					ImmutableList.Builder<Iterable<String>> functionLines = ImmutableList.builder();
					for (int functionLineIndex = lineIndex + 1; functionLineIndex < linesAsTokens.size(); functionLineIndex++) {
						Iterable<String> functionLine = linesAsTokens.get(functionLineIndex);
						if (size(functionLine) >= 2//
								&& get(functionLine, 0).equalsIgnoreCase(localizedCommands.endCommand())//
								&& get(functionLine, 1).equalsIgnoreCase(localizedCommands.toCommand())) {
							endTagFound = true;
							break;
						}
						functionLines.add(functionLine);
					}
					if (!endTagFound) {
						throw new IllegalStateException(messages.unclosedFunctionStatement(lineIndex));
					}
					functions.put(get(lineTokens, 1).toLowerCase(), functionLines.build());
				} else {
					if (functions.containsKey(command.toLowerCase())) {
						interpret(functions.get(command.toLowerCase()));
					} else {
						throw new IllegalStateException(messages.unrecognizedStatement(lineIndex));
					}
				}
			}
		}
	}

	private Double getValue(String token) {
		String lowercaseToken = token.toLowerCase();
		if (userVariables.containsKey(lowercaseToken)) {
			return userVariables.get(lowercaseToken);
		}
		return Double.parseDouble(token);
	}

	private void handleVariableMath(Iterable<String> lineTokens) {
		String variableName = get(lineTokens, 1).toLowerCase();
		Double value1 = getValue(get(lineTokens, 3));
		String mathOperator = get(lineTokens, 4);
		Double value2 = getValue(get(lineTokens, 5));

		if (mathOperator.equals("+")) {
			userVariables.put(variableName, value1 + value2);
		} else if (mathOperator.equals("-")) {
			userVariables.put(variableName, value1 - value2);
		} else if (mathOperator.equals("*")) {
			userVariables.put(variableName, value1 * value2);
		} else if (mathOperator.equals("/")) {
			userVariables.put(variableName, value1 / value2);
		}
	}

	private boolean isConditionTrue(String operator, double left, double right, int lineIndex) {
		if (operator.equals("=")) {
			return left == right;
		}
		if (operator.equals("!")) {
			return left != right;
		}
		if (operator.equals("<")) {
			return left < right;
		}
		if (operator.equals(">")) {
			return left > right;
		}
		throw new IllegalStateException(messages.unrecognizedIfOperator(operator, lineIndex));
	}

}
