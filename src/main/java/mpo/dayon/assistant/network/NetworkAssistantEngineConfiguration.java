package mpo.dayon.assistant.network;

import mpo.dayon.common.configuration.Configuration;
import mpo.dayon.common.preference.Preferences;

public class NetworkAssistantEngineConfiguration extends Configuration {
	private static final String PREF_VERSION = "assistant.network.version";

	private static final String PREF_PORT_NUMBER = "assistant.network.portNumber";

	private final int port;

	/**
	 * Default : takes its values from the current preferences.
	 */
	public NetworkAssistantEngineConfiguration() {
		port = Preferences.getPreferences().getIntPreference(PREF_PORT_NUMBER, 8080);
	}

	public NetworkAssistantEngineConfiguration(int port) {
		this.port = port;
	}

	public int getPort() {
		return port;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		final NetworkAssistantEngineConfiguration that = (NetworkAssistantEngineConfiguration) o;

		return port == that.getPort();
	}

	@Override
	public int hashCode() {
		return port;
	}

	/**
	 * @param clear
	 *            allows for clearing properties from previous version
	 */
	@Override
    protected void persist(boolean clear) {
		final Preferences.Props props = new Preferences.Props();
		props.set(PREF_VERSION, String.valueOf(1));
		props.set(PREF_PORT_NUMBER, String.valueOf(port));

		if (clear) // migration support (!)
		{
			props.clear("assistantPortNumber");
		}
		Preferences.getPreferences().update(props); // atomic (!)
	}

}
