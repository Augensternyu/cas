package org.apereo.cas.configuration.model.core;

import org.apereo.cas.configuration.support.RequiresModule;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

/**
 * Configuration properties class for host.
 *
 * @author Dmitriy Kopylenko
 * @since 5.0.0
 */
@RequiresModule(name = "cas-server-core", automated = true)
@Getter
@Setter
@Accessors(chain = true)
public class CasServerHostProperties implements Serializable {

    @Serial
    private static final long serialVersionUID = 8624916460241033347L;

    /**
     * Name of the networking host configured to run CAS server.
     * A CAS host is automatically appended to the ticket ids generated by CAS.
     * If none is specified, one is automatically detected and used by CAS.
     */
    private String name;
}
