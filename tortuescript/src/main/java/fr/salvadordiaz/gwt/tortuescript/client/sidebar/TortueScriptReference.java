package fr.salvadordiaz.gwt.tortuescript.client.sidebar;

import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

import fr.salvadordiaz.gwt.tortuescript.client.Messages;

public class TortueScriptReference extends PopupPanel {

	private static TortueScriptReferenceUiBinder uiBinder = GWT.create(TortueScriptReferenceUiBinder.class);

	interface TortueScriptReferenceUiBinder extends UiBinder<Widget, TortueScriptReference> {
	}

	@UiField
	HeadingElement syntaxTitle;
	@UiField
	Button closeButton;
	@UiField
	Anchor movementsTab, penTab, mathTab, functionsTab;
	@UiField
	DivElement movementsPanel, penPanel, mathPanel, functionsPanel;

	private final Map<Anchor, DivElement> panelMap;

	private Anchor activeTab;

	public TortueScriptReference(Messages messages) {
		super(false, false);
		setWidget(uiBinder.createAndBindUi(this));
		syntaxTitle.setInnerText(messages.syntaxTitle());
		movementsTab.setText(messages.movementsTab());
		penTab.setText(messages.penTab());
		mathTab.setText(messages.mathTab());
		functionsTab.setText(messages.functionsTab());
		activeTab = movementsTab;
		panelMap = ImmutableMap.<Anchor, DivElement> builder()//
				.put(movementsTab, movementsPanel)//
				.put(penTab, penPanel)//
				.put(mathTab, mathPanel)//
				.put(functionsTab, functionsPanel)//
				.build();
	}

	@UiHandler("closeButton")
	void close(ClickEvent event){
		hide();
	}
	
	@UiHandler({ "movementsTab", "penTab", "mathTab", "functionsTab" })
	void switchTab(ClickEvent event) {
		activeTab.getElement().getParentElement().removeAttribute("class");
		panelMap.get(activeTab).getStyle().setDisplay(Display.NONE);
		activeTab = (Anchor) event.getSource();
		activeTab.getElement().getParentElement().setClassName("active");
		panelMap.get(activeTab).getStyle().setDisplay(Display.BLOCK);
	}
}
