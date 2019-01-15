package uk.ac.tees.net;

import java.util.HashSet;
import java.util.Set;

/**
 * The key part of the key-value pair where connections are mapped to portal uids
 * and portal uids are mapped to agent uids.
 * 
 * <br />
 * 
 * The {@link #hashCode()} function of this class, used in a {@link HashMap} to 
 * retrieve/store bucket, returns the hashCode of {@link #portalId} since the
 * it is the portal that is mapped to a {@link Connection}.
 *
 * <br />
 * 
 * Override equals and hashCode to adhere to the contract that states if two objects 
 * are equal then their hashCodes must also be equal.
 * 
 * @author Sam Hammersley (q5315908)
 */
public final class ConnectionKey {

	/**
	 * The portal id that is mapped to a connection.
	 */
	private final String portalId;
	
	/**
	 * A set of agent ids that are associated with a portal.
	 */
	private final Set<String> aIds = new HashSet<>();
	
	/**
	 * Constructs a new {@link ConnectionKey} object with the given portal id.
	 * 
	 * @param portalId the portal id this key is for.
	 */
	public ConnectionKey(String portalId) {
		this.portalId = portalId;
	}
	
	/**
	 * Adds an agent id to {@link #aIds} and is then associated with the portal.
	 * 
	 * @param agentId the id to add.
	 */
	public void add(String agentId) {
		aIds.add(agentId);
	}

	/**
	 * Checks if this key is the portal id or if {@link #aIds} contains the specified uid.
	 * 
	 * @param uid the uid to check for.
	 * @return {@code true} if this key contains the specified uid.
	 */
	public boolean contains(String uid) {
		return portalId.equals(uid) || aIds.contains(uid);
	}
	
	@Override
	public int hashCode() {
		return portalId.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof ConnectionKey)) {
			return false;
		}
		
		ConnectionKey other = (ConnectionKey) obj;
		
		return other.portalId.equals(portalId);
	}
	
	@Override
	public String toString() {
		return portalId + ": " + aIds.toString(); 
	}
	
}