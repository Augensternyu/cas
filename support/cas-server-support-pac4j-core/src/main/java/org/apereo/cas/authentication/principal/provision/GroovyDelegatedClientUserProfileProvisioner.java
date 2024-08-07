package org.apereo.cas.authentication.principal.provision;

import org.apereo.cas.authentication.Credential;
import org.apereo.cas.authentication.principal.Principal;
import org.apereo.cas.util.scripting.ExecutableCompiledScript;
import org.apereo.cas.util.scripting.ExecutableCompiledScriptFactory;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.pac4j.core.client.BaseClient;
import org.pac4j.core.profile.UserProfile;
import org.springframework.core.io.Resource;

/**
 * This is {@link GroovyDelegatedClientUserProfileProvisioner}.
 *
 * @author Misagh Moayyed
 * @since 6.1.0
 */
@Slf4j
public class GroovyDelegatedClientUserProfileProvisioner extends BaseDelegatedClientUserProfileProvisioner {
    private final ExecutableCompiledScript watchableScript;

    public GroovyDelegatedClientUserProfileProvisioner(final Resource groovyResource) {
        val scriptFactory = ExecutableCompiledScriptFactory.getExecutableCompiledScriptFactory();
        this.watchableScript = scriptFactory.fromResource(groovyResource);
    }

    @Override
    public void execute(final Principal principal, final UserProfile profile,
                        final BaseClient client, final Credential credential) throws Throwable {
        val args = new Object[]{principal, profile, client, LOGGER};
        watchableScript.execute(args, Void.class);
    }
}
