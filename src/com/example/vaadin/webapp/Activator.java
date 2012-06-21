package com.example.vaadin.webapp;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javawebparts.session.SessionSize;

import javax.servlet.http.HttpSession;

import com.github.wolfie.refresher.Refresher;
import com.vaadin.Application;
import com.vaadin.terminal.gwt.server.WebApplicationContext;
import com.vaadin.terminal.gwt.server.WebBrowser;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.Window;

@SuppressWarnings("serial")
public class Activator extends Application {

	private static final String CURRENT_SESSION_USERS_COUNT = "Count of users which use this session";

	private SessionSize sessionSize;

	private HttpSession session;

	private WebApplicationContext context;

	private TextArea sessionTextArea;

	private Window secondWindow;

	private String tempDir = System.getProperty("java.io.tmpdir");

	private File sessionOutputFile = new File(tempDir + "/session.ser");

	private File httpSessionOutputFile = new File(tempDir + "/HTTPsession.ser");

	private File webContextOutputFile = new File(tempDir + "/sessionContext.ser");

	@Override
	public void init() {

		// main window
		final Window mainPage = new Window("Main Window");

		context = (WebApplicationContext) getContext();

		session = context.getHttpSession();

		Integer count = (Integer) session.getAttribute(CURRENT_SESSION_USERS_COUNT);
		if (count == null) {
			count = new Integer(1);
		}
		else {
			count = new Integer(count.intValue() + 1);
		}
		session.setAttribute(CURRENT_SESSION_USERS_COUNT, count);

		sessionSize = new SessionSize(session);
		sessionSize.setIgnoreNonSerializable(true);

		sessionTextArea = new TextArea();
		sessionTextArea.setCaption("Session info (updates every second):");
		sessionTextArea.setImmediate(false);
		sessionTextArea.setSizeFull();
		sessionTextArea.setHeight("600px");
		sessionTextArea.setNullSettingAllowed(true);

		final ScheduledExecutorService scheduler =
				Executors.newScheduledThreadPool(1);

		final Runnable sessionInfoUpdater = new Runnable() {
			public void run() {
				sessionTextArea.setValue(getSessionInfoString());
				sessionTextArea.requestRepaint(); // update value
			}
		};

		final ScheduledFuture<?> updaterHandle =
				scheduler.scheduleAtFixedRate(sessionInfoUpdater, 1, 1, TimeUnit.SECONDS);
		// session info will be updated every second

		scheduler.schedule(new Runnable() {
			public void run() {
				updaterHandle.cancel(true);
			}
		}, 60 * 60, TimeUnit.SECONDS);

		mainPage.setName("main");

		MainPageContent composite = new MainPageContent(this);

		mainPage.addComponent(composite);
		setMainWindow(mainPage);

		secondWindow = new Window("Session information");

		secondWindow.setName("session_info");

		secondWindow.addComponent(sessionTextArea);

		sessionTextArea.setStyleName("v-my-session-info-text-area");

		final Refresher refresher = new Refresher();
		refresher.setRefreshInterval(1000);

		secondWindow.addComponent(refresher);

		addWindow(secondWindow);

		// setTheme("runo");
		// setTheme("chameleon");

	}

	private String getSessionInfoString() {

		Date sessionCreationDateTime = new Date(session.getCreationTime());
		Date sessionLastAccessTime = new Date(session.getLastAccessedTime());

		String sessionInfo = "Session creation time: " + DateFormat.getInstance().format(sessionCreationDateTime)
				+ "\n"
				+ "Session last access time: " + DateFormat.getInstance().format(sessionLastAccessTime) + "\n"
				+ "Session timeout: " + session.getMaxInactiveInterval() + "\n"
				+ "Is Session new: " + session.isNew() + "\n"
				+ "Session Id: " + session.getId() + "\n"
				+ "Session size by javawebparts SessionSize utility: " + sessionSize.getSessionSize() + " bytes.\n"
				+ "Serialized session size (serialized current Application class instance): " + writeSession() + "\n"
				+ "Serialized session context size (serialized current WebApplicationContext class instance): "
				+ writeWebAppContext() + "\n"
				+ "Serialized HTTP session size (serialized current HttpSession instance): " + writeHttpSession()
				+ "\n"
				+ "\nApplications which are belonging to the current session: \n";

		final Collection<Application> applications = context.getApplications();
		int appsCount = 1;
		for (Application application : applications) {
			sessionInfo += appsCount + "). " + application.toString() + " (main window caption: '"
					+ application.getMainWindow().getCaption() + "', is running: " + application.isRunning() + ")\n";
			appsCount++;
		}

		// additional session information:
		@SuppressWarnings("rawtypes")
		Enumeration e = session.getAttributeNames();
		sessionInfo += "\nAdditional session attributes:\n";
		while (e.hasMoreElements()) {
			String name = (String) e.nextElement();
			sessionInfo += name + " --> " + session.getAttribute(name) + "\n";
		}

		sessionInfo += "\n[System messages]: 'Authentication Error' notification enabled: "
				+ getSystemMessages().isAuthenticationErrorNotificationEnabled() + "\n"
				+ "[System messages]: 'Communication Error' notification enabled: "
				+ getSystemMessages().isCommunicationErrorNotificationEnabled() + "\n"
				+ "[System messages]: 'Cookies Disabled' notification enabled: "
				+ getSystemMessages().isCookiesDisabledNotificationEnabled() + "\n"
				+ "[System messages]: 'Internal Error notification' enabled: "
				+ getSystemMessages().isInternalErrorNotificationEnabled() + "\n"
				+ "[System messages]: 'Out Of Sync' notification' enabled: "
				+ getSystemMessages().isOutOfSyncNotificationEnabled() + "\n"
				+ "[System messages]: 'Session Expired' notification enabled: "
				+ getSystemMessages().isSessionExpiredNotificationEnabled();

		sessionInfo += "\n\nSession context base directory: " + context.getBaseDirectory() + "\n";

		sessionInfo += "Session client/browser info: " + getBrowserAgent() + "\n";

		return sessionInfo;
	}

	private String getBrowserAgent() {

		WebBrowser browser = ((WebApplicationContext) getContext()).getBrowser();

		String browserDetails = browser.getBrowserApplication();

		return browserDetails;
	}

	public String writeWebAppContext() {
		String result = "undefined";
		try {
			OutputStream file = new FileOutputStream(webContextOutputFile);
			OutputStream buffer = new BufferedOutputStream(file);
			ObjectOutput output = new ObjectOutputStream(buffer);
			try {
				output.writeObject(context);
			} finally {
				if (output != null) {
					output.close();
				}
			}
		} catch (NotSerializableException e) {
			result = "Sorry, but " + context.toString() + " is not serializable =(";
		} catch (IOException e) {
			e.printStackTrace();
		}
		long byteSize = webContextOutputFile.length();
		result = humanReadableByteCount(byteSize, false);
		return result;
	}

	public String writeHttpSession() {
		try {
			OutputStream file = new FileOutputStream(httpSessionOutputFile);
			OutputStream buffer = new BufferedOutputStream(file);
			ObjectOutput output = new ObjectOutputStream(buffer);
			try {
				output.writeObject(session);
			} finally {
				if (output != null) {
					output.close();
				}
			}
		} catch (NotSerializableException e) {
			// ignore
		} catch (IOException e) {
			e.printStackTrace();
		}
		long byteSize = httpSessionOutputFile.length();
		return humanReadableByteCount(byteSize, false);
	}

	public String writeSession() {
		try {
			OutputStream file = new FileOutputStream(sessionOutputFile);
			OutputStream buffer = new BufferedOutputStream(file);
			ObjectOutput output = new ObjectOutputStream(buffer);
			try {
				output.writeObject(this);
			} finally {
				if (output != null) {
					output.close();
				}
			}
		} catch (NotSerializableException e) {
			// ignore
		} catch (IOException e) {
			e.printStackTrace();
		}
		long byteLength = sessionOutputFile.length();

		return humanReadableByteCount(byteLength, false);
	}

	public static String humanReadableByteCount(long bytes, boolean si) {
		int unit = si ? 1000 : 1024;
		if (bytes < unit) {
			return bytes + " B";
		}
		int exp = (int) (Math.log(bytes) / Math.log(unit));
		String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp - 1) + (si ? "" : "i");
		return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
	}

}