package ms.gamerecommender.app.config;

import lombok.val;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

@Configuration
public class ThymeleafConfiguration {

    @Bean
    @Scope("singleton")
    public ITemplateResolver getTemplateResolver() {
        val templateResolver = new ClassLoaderTemplateResolver();

        templateResolver.setPrefix("templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode("HTML5");
        templateResolver.setCharacterEncoding("UTF-8");
        templateResolver.setCacheable(false);

        return templateResolver;
    }


    @Bean
    @Scope("singleton")
    public ITemplateEngine getTemplateEngine() {
        val engine = new TemplateEngine();

        engine.setTemplateResolver(getTemplateResolver());
        engine.addDialect(new Java8TimeDialect());

        return engine;
    }

}
