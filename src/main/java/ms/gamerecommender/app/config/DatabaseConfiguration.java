package ms.gamerecommender.app.config;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Configuration
public class DatabaseConfiguration {
    DataSource mDataSource;

    public DatabaseConfiguration(DataSource mDataSource) {
        this.mDataSource = mDataSource;
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        FileSystemResourceLoader loader = new FileSystemResourceLoader();
        populator.addScript(loader.getResource("./schema/database.sql"));
        populator.execute(this.mDataSource);
    }
}
