package uk.ac.tees.node.router;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import uk.ac.tees.net.Connection;
import uk.ac.tees.net.ConnectionKey;

public final class ConnectionManager {
	
	/**
	 * Maps portal uid to connection
	 */
	private final Map<ConnectionKey, Connection> cachedConnections = new ConcurrentHashMap<>();

	/**
	 * Gets a connection for an id
	 * 
	 * @param uid the id
	 * @return a {@link Connection}
	 */
	public Optional<Connection> getConnection(String uid) {
		return getConnectionKey(uid).map(cachedConnections::get);
	}

	/**
	 * Gets the whole key of a connection for a uid.
	 * 
	 * @param uid one of the ids associated with the connection.
	 * @return an associated {@link Connection} with the given uid. 
	 */
	public Optional<ConnectionKey> getConnectionKey(String uid) {
		return cachedConnections.keySet().stream().filter(s -> s.contains(uid)).findAny();
	}

	/**
	 * Stores a {@link Connection}.
	 * 
	 * @param portalId the portalId associated with the given {@link Connection}.
	 * @param connection the {@link Connection}.
	 */
	public boolean storeConnection(String portalId, Connection connection) {
		Optional<ConnectionKey> ids = getConnectionKey(portalId);
		
		if (!ids.isPresent()) {
			ConnectionKey key = new ConnectionKey(portalId);
			
			cachedConnections.put(key, connection);
		}
		
		return !ids.isPresent();
	}

	/**
	 * Determines whether the uid is associated with any connections.
	 * 
	 * @param uid the id to check connections for.
	 * @return {@code true} if there is an associated connection.
	 */
	public boolean hasConnection(String uid) {
		return cachedConnections.keySet().stream().anyMatch(s -> s.contains(uid));
	}
}