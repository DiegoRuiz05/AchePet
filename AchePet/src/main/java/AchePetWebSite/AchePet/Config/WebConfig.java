package AchePetWebSite.AchePet.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Mapeia URLs "/imagens/**" para a pasta do seu PC
        registry.addResourceHandler("/imagens/**")
                .addResourceLocations("file:" + System.getProperty("user.home") + "/AchePetUploads/");
    }
}

