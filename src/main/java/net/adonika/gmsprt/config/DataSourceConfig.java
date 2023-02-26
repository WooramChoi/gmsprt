package net.adonika.gmsprt.config;

import net.adonika.gmsprt.comm.impl.CommRepositoryImpl;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EntityScan(basePackages = {"net.adonika.gmsprt"})
@EnableJpaRepositories(
        basePackages = {"net.adonika.gmsprt"},
        repositoryBaseClass = CommRepositoryImpl.class
)
@EnableJpaAuditing
@EnableTransactionManagement
public class DataSourceConfig {
}
