package fr.salvadordiaz.gwt.tortuescript.client.sidebar;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import fr.salvadordiaz.gwt.tortuescript.client.i18n.Messages;
import fr.salvadordiaz.gwt.tortuescript.client.sidebar.SidebarActivity.EmailPromptDisplay;

public class EmailPrompt extends PopupPanel implements EmailPromptDisplay {

	private static final String ERROR_STYLE = "error";

	private static EmailPromptUiBinder uiBinder = GWT.create(EmailPromptUiBinder.class);

	interface EmailPromptUiBinder extends UiBinder<Widget, EmailPrompt> {
	}

	@UiField
	Button closeButton;
	@UiField
	HeadingElement title;
	@UiField
	TextBox textBox;
	@UiField
	Button saveButton;

	private final Messages messages = GWT.create(Messages.class);

	public EmailPrompt() {
		super(false, true);
		setGlassEnabled(true);
		setWidget(uiBinder.createAndBindUi(this));

		title.setInnerText(messages.enterYourEmail());
		saveButton.setText(messages.saveEmail());
	}

	@UiHandler("textBox")
	void validate(BlurEvent event) {
		if (textBox.getValue().trim().isEmpty()) {
			textBox.addStyleName(ERROR_STYLE);
			saveButton.setEnabled(false);
		} else {
			textBox.removeStyleName(ERROR_STYLE);
			saveButton.setEnabled(true);
		}
	}

	@UiHandler("closeButton")
	void close(ClickEvent event) {
		hide();
	}

	@Override
	public HasClickHandlers getSaveButton() {
		return saveButton;
	}

	@Override
	public HasValue<String> getTextbox() {
		return textBox;
	}
}
