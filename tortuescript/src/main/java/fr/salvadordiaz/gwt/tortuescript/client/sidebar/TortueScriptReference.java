package fr.salvadordiaz.gwt.tortuescript.client.sidebar;

import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import fr.salvadordiaz.gwt.tortuescript.client.Messages;

public class TortueScriptReference extends Composite {

	private static TortueScriptReferenceUiBinder uiBinder = GWT.create(TortueScriptReferenceUiBinder.class);

	interface TortueScriptReferenceUiBinder extends UiBinder<Widget, TortueScriptReference> {
	}

	@UiField
	HeadingElement syntaxTitle;
	@UiField
	Anchor movementsTab, penTab, mathTab, functionsTab;
	@UiField
	DivElement movementsPanel, penPanel, mathPanel, functionsPanel;

	private final Map<Anchor, DivElement> panelMap;

	private Anchor activeTab;

	public TortueScriptReference(Messages messages) {
		initWidget(uiBinder.createAndBindUi(this));
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

	@UiHandler({ "movementsTab", "penTab", "mathTab", "functionsTab" })
	void switchTab(ClickEvent event) {
		activeTab.getElement().getParentElement().removeAttribute("class");
		panelMap.get(activeTab).getStyle().setDisplay(Display.NONE);
		activeTab = (Anchor) event.getSource();
		activeTab.getElement().getParentElement().setClassName("active");
		panelMap.get(activeTab).getStyle().setDisplay(Display.BLOCK);
	}

	public void setPosition(Widget widget) {
		getElement().setAttribute("visibility", "hidden");
		setVisible(true);
		int left = widget.getAbsoluteLeft() + widget.getOffsetWidth();
		int top = widget.getAbsoluteTop() + widget.getOffsetHeight() / 2 - getOffsetHeight() / 2;
		getElement().getStyle().setLeft(left, Unit.PX);
		getElement().getStyle().setTop(top, Unit.PX);
		setVisible(false);
		getElement().removeAttribute("visibility");
	}

	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		getElement().getStyle().setDisplay(visible ? Display.BLOCK : Display.NONE);
	}
}
