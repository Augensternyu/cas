package org.apereo.cas.multitenancy;

import java.io.Serializable;
import java.util.List;

/**
 * This is {@link TenantDelegatedAuthenticationPolicy}.
 *
 * @author Misagh Moayyed
 * @since 7.2.0
 */
@FunctionalInterface
public interface TenantDelegatedAuthenticationPolicy extends Serializable {
    /**
     * Gets allowed external identity providers for delegation.
     *
     * @return the allowed providers
     */
    List<String> getAllowedProviders();
}
