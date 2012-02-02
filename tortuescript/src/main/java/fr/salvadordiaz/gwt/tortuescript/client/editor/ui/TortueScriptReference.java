package fr.salvadordiaz.gwt.tortuescript.client.editor.ui;

import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

import fr.salvadordiaz.gwt.tortuescript.client.i18n.LocalizedCommands;
import fr.salvadordiaz.gwt.tortuescript.client.i18n.LocalizedDocumentation;
import fr.salvadordiaz.gwt.tortuescript.client.i18n.Messages;

public class TortueScriptReference extends PopupPanel {

	private static TortueScriptReferenceUiBinder uiBinder = GWT.create(TortueScriptReferenceUiBinder.class);

	interface TortueScriptReferenceUiBinder extends UiBinder<Widget, TortueScriptReference> {
	}

	interface SyntaxTemplate extends SafeHtmlTemplates {
		@Template("<div class=\"page-header\"><h5>{0}</h5></div>{1}. {2}:<pre>{0} {3}</pre>")
		SafeHtml commandSyntax(String name, SafeHtml description, String exampleWord, SafeHtml arguments);
	}

	@UiField
	HeadingElement syntaxTitle;
	@UiField
	Anchor closeButton;
	@UiField
	SpanElement intro;
	@UiField
	Anchor movementsTab, penTab, mathTab, functionsTab;
	@UiField
	HTML movementsPanel, penPanel, mathPanel, functionsPanel;

	private final SyntaxTemplate template = GWT.create(SyntaxTemplate.class);

	private final Messages messages = GWT.create(Messages.class);

	private final LocalizedCommands commands = GWT.create(LocalizedCommands.class);

	private final LocalizedDocumentation documentation = GWT.create(LocalizedDocumentation.class);

	private final Map<Anchor, HTML> panelMap;

	private Anchor activeTab;

	public TortueScriptReference() {
		super(false, false);
		setWidget(uiBinder.createAndBindUi(this));
		syntaxTitle.setInnerText(messages.syntaxTitle());
		intro.setInnerText(documentation.intro());
		movementsTab.setText(messages.movementsTab());
		penTab.setText(messages.penTab());
		mathTab.setText(messages.mathTab());
		functionsTab.setText(messages.functionsTab());
		activeTab = movementsTab;
		panelMap = ImmutableMap.<Anchor, HTML> builder()//
				.put(movementsTab, movementsPanel)//
				.put(penTab, penPanel)//
				.put(mathTab, mathPanel)//
				.put(functionsTab, functionsPanel)//
				.build();
		makeDocs();
	}

	@UiHandler("closeButton")
	void close(ClickEvent event) {
		hide();
	}

	@UiHandler({ "movementsTab", "penTab", "mathTab", "functionsTab" })
	void switchTab(ClickEvent event) {
		activeTab.getElement().getParentElement().removeAttribute("class");
		panelMap.get(activeTab).setVisible(false);
		activeTab = (Anchor) event.getSource();
		activeTab.getElement().getParentElement().setClassName("active");
		panelMap.get(activeTab).setVisible(true);
	}

	private void makeDocs() {
		final String left = commands.leftCommand();
		final String forward = commands.forwardCommand();
		final String repeat = commands.repeatCommand();
		final String ifCommand = commands.ifCommand();
		final String to = commands.toCommand();
		final String end = commands.endCommand();
		SafeHtml movementDocs = new SafeHtmlBuilder()//
				.append(makeDoc(forward, documentation.forwardDescription(), documentation.forwardArgs()))//
				.append(makeDoc(commands.backwardCommand(), documentation.backwardDescription(), documentation.backwardArgs()))//
				.append(makeDoc(left, documentation.leftDescription(), documentation.leftArgs()))//
				.append(makeDoc(commands.rightCommand(), documentation.rightDescription(), documentation.rightArgs()))//
				.append(makeDoc(commands.setXCommand(), documentation.setXDescription(), documentation.setXArgs()))//
				.append(makeDoc(commands.setYCommand(), documentation.setYDescription(), documentation.setYArgs()))//
				.append(makeDoc(commands.homeCommand(), documentation.homeDescription(), SafeHtmlUtils.EMPTY_SAFE_HTML))//
				.toSafeHtml();
		movementsPanel.setHTML(movementDocs);
		SafeHtml penDocs = new SafeHtmlBuilder()//		
				.append(makeDoc(commands.penDownCommand(), documentation.penDownDescription(), SafeHtmlUtils.EMPTY_SAFE_HTML))//
				.append(makeDoc(commands.penUpCommand(), documentation.penUpDescription(), SafeHtmlUtils.EMPTY_SAFE_HTML))//
				.append(makeDoc(commands.penColorCommand(), documentation.penColorDescription(), documentation.penColorArgs(commands.penColorCommand())))//
				.append(makeDoc(commands.drawStringCommand(), documentation.drawStringDescription(), documentation.drawStringArgs()))//
				.append(makeDoc(commands.fontNameCommand(), documentation.fontNameDescription(), documentation.fontNameArgs()))//
				.append(makeDoc(commands.fontSizeCommand(), documentation.fontSizeDescription(), documentation.fontSizeArgs()))//
				.append(makeDoc(commands.fontStyleCommand(), documentation.fontStyleDescription(), documentation.fontStyleArgs()))//
				.append(makeDoc(commands.clearCommand(), documentation.clearDescription(), SafeHtmlUtils.EMPTY_SAFE_HTML))//
				.append(makeDoc(commands.newCommand(), documentation.newDescription(), SafeHtmlUtils.EMPTY_SAFE_HTML))//
				.toSafeHtml();
		penPanel.setHTML(penDocs);
		SafeHtml mathDocs = new SafeHtmlBuilder()//
				.append(makeDoc(commands.sumCommand(), documentation.sumDescription(), documentation.sumArgs()))//
				.append(makeDoc(commands.subtractCommand(), documentation.subtractDescription(), documentation.subtractArgs()))//
				.append(makeDoc(commands.multiplyCommand(), documentation.multiplyDescription(), documentation.multiplyArgs()))//
				.append(makeDoc(commands.divideCommand(), documentation.divideDescription(), documentation.divideArgs()))//
				.toSafeHtml();
		mathPanel.setHTML(mathDocs);
		SafeHtml functionDocs = new SafeHtmlBuilder()//
				.append(makeDoc(commands.makeCommand(), documentation.makeDescription(), documentation.makeArgs()))//
				.append(makeDoc(repeat, documentation.repeateDescription(end, repeat), documentation.repeatArgs(repeat, forward, left, end)))//
				.append(makeDoc(ifCommand, documentation.ifDescription(end, ifCommand), documentation.ifArgs(ifCommand, forward, end)))//
				.append(makeDoc(to, documentation.toDescription(), documentation.toArgs(to, forward, end)))//
				.toSafeHtml();
		functionsPanel.setHTML(functionDocs);
	}

	private SafeHtml makeDoc(String name, SafeHtml description, SafeHtml args) {
		return template.commandSyntax(name, description, messages.example(), args);
	}
}
