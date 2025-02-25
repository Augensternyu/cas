package org.apereo.cas.configuration.model.support.interrupt;

import org.apereo.cas.configuration.model.SpringResourceProperties;
import org.apereo.cas.configuration.support.RequiresModule;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serial;

/**
 * This is {@link GroovyInterruptProperties}.
 *
 * @author Misagh Moayyed
 * @since 6.4.0
 */
@RequiresModule(name = "cas-server-support-interrupt-webflow")
@Getter
@Setter
@Accessors(chain = true)
public class GroovyInterruptProperties extends SpringResourceProperties {
    @Serial
    private static final long serialVersionUID = 8079027843747126082L;
}
