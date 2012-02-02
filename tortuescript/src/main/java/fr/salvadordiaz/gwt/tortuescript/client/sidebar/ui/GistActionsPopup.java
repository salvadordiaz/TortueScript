package fr.salvadordiaz.gwt.tortuescript.client.sidebar.ui;

import javax.inject.Inject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.safehtml.shared.UriUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import fr.salvadordiaz.gwt.tortuescript.client.i18n.Messages;
import fr.salvadordiaz.gwt.tortuescript.client.model.JsonGist;
import fr.salvadordiaz.gwt.tortuescript.client.sidebar.SidebarActivity.SidebarDisplay.GistActionsDisplay;

public class GistActionsPopup extends PopupPanel implements GistActionsDisplay {

	private static final String ERROR_STYLE = "error";
	private static final String IN = "in";

	private static GistActionsUiBinder uiBinder = GWT.create(GistActionsUiBinder.class);

	interface GistActionsUiBinder extends UiBinder<Widget, GistActionsPopup> {
	}

	@UiField
	HTMLPanel container, infoPanel, sharePanel, emailPanel;
	@UiField
	HasClickHandlers closeButton;
	@UiField
	Element title, mailLabel, authorLabel, shareLegend, helpLabel;
	@UiField
	Anchor gistLink, executeLink;
	@UiField
	TextBox mailTextBox;
	@UiField
	Button shareButton, deleteButton;

	private final Messages messages;
	private JsonGist value;

	@Inject
	public GistActionsPopup(Messages messages) {
		super(true);
		setWidget(uiBinder.createAndBindUi(this));
		this.messages = messages;
		executeLink.setText(messages.execute());
		gistLink.setText(messages.showOnGithub());
		mailLabel.setInnerText(messages.enterYourEmail());
		shareLegend.setInnerText(messages.shareOnGithub());
		shareButton.setText(messages.shareButton());
		helpLabel.setInnerText(messages.emailHelp());
		deleteButton.setText(messages.deleteProgram());
	}

	@UiHandler({ "closeButton", "deleteButton","shareButton" })
	void close(ClickEvent event) {
		hide();
	}

	@UiHandler("mailTextBox")
	void validate(BlurEvent event) {
		final String email = mailTextBox.getValue().trim();
		if (email.isEmpty()) {
			emailPanel.addStyleName(ERROR_STYLE);
			shareButton.setEnabled(false);
		} else {
			emailPanel.removeStyleName(ERROR_STYLE);
			value.setUser(email);
			shareButton.setEnabled(true);
		}
	}

	@Override
	public HasClickHandlers getShareButton() {
		return shareButton;
	}

	@Override
	public HasClickHandlers getDeleteButton() {
		return deleteButton;
	}

	@Override
	public HasValue<String> getEmailBox() {
		return mailTextBox;
	}

	@Override
	public void show() {
		super.show();
		Scheduler.get().scheduleDeferred(fadeCommand);
	}

	private final Scheduler.ScheduledCommand fadeCommand = new Scheduler.ScheduledCommand() {
		@Override
		public void execute() {
			container.addStyleName(IN);
		}
	};

	public void hide(final boolean autoClosed) {
		container.removeStyleName(IN);
		super.hide(autoClosed);
	}

	@Override
	public JsonGist getValue() {
		return value;
	}

	@Override
	public void setValue(JsonGist value) {
		this.value = value;
		final String filename = value.getFile().getFilename();
		title.setInnerText(messages.gistPopupTitle(filename));
		executeLink.setHref("#editor:" + filename);
		final String url = value.getHtmlUrl();
		final boolean hasUrl = url != null;
		infoPanel.setVisible(hasUrl);
		sharePanel.setVisible(!hasUrl);
		if (hasUrl) {
			gistLink.setHref(value.getHtmlUrl());
			gistLink.setVisible(UriUtils.isSafeUri(url));
			authorLabel.setInnerText(messages.author(value.getDescription().getUser()));
		} else {
			mailTextBox.removeStyleName(ERROR_STYLE);
		}
	}

}
