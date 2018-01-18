package org.apereo.cas.metadata;

import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apereo.cas.configuration.CasConfigurationProperties;
import org.jooq.lambda.Unchecked;
import org.springframework.boot.configurationmetadata.ConfigurationMetadataProperty;
import org.springframework.boot.configurationmetadata.ConfigurationMetadataRepository;
import org.springframework.boot.configurationmetadata.ConfigurationMetadataRepositoryJsonBuilder;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.InputStream;
import java.util.Arrays;

/**
 * This is {@link CasConfigurationMetadataRepository}.
 *
 * @author Misagh Moayyed
 * @author Dmitriy Kopylenko
 * @since 5.2.0
 */
@Slf4j
@Getter
public class CasConfigurationMetadataRepository {
    private final ConfigurationMetadataRepository repository;

    public CasConfigurationMetadataRepository() {
        this("classpath*:META-INF/spring-configuration-metadata.json");
    }

    /**
     * Instantiates a new Cas configuration metadata repository.
     * Scans the context looking for spring configuration metadata
     * resources and then loads them all into a repository instance.
     *
     * @param resource the resource
     */
    @SneakyThrows
    public CasConfigurationMetadataRepository(final String resource) {
        final Resource[] resources = new PathMatchingResourcePatternResolver().getResources(resource);
        final ConfigurationMetadataRepositoryJsonBuilder builder = ConfigurationMetadataRepositoryJsonBuilder.create();
        Arrays.stream(resources).forEach(Unchecked.consumer(r -> {
            try (InputStream in = r.getInputStream()) {
                builder.withJsonResource(in);
            }
        }));
        repository = builder.build();
    }
    
    /**
     * Gets property group id.
     *
     * @param prop the prop
     * @return the property group id
     */
    public static String getPropertyGroupId(final ConfigurationMetadataProperty prop) {
        if (isCasProperty(prop)) {
            return StringUtils.substringBeforeLast(prop.getName(), ".");
        }
        return StringUtils.substringBeforeLast(prop.getId(), ".");
    }

    /**
     * Is cas property ?.
     *
     * @param prop the prop
     * @return the boolean
     */
    public static boolean isCasProperty(final ConfigurationMetadataProperty prop) {
        return prop.getName().startsWith(CasConfigurationProperties.PREFIX.concat("."));
    }
}
