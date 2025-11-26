package AchePetWebSite.AchePet.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${achepet.upload.dir}")
    private String uploadDir;

    @Value("${achepet.upload.dir.perdido}")
    private String uploadDirPerdido;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        // Caminho absoluto no sistema de arquivos para pets de adoção
        Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();

        // Caminho absoluto no sistema de arquivos para pets perdidos
        Path uploadPathPerdido = Paths.get(uploadDirPerdido).toAbsolutePath().normalize();

        // Garante que termina com /
        String uploadLocation = uploadPath.toString().replace("\\", "/");
        if (!uploadLocation.endsWith("/")) {
            uploadLocation += "/";
        }

        String uploadLocationPerdido = uploadPathPerdido.toString().replace("\\", "/");
        if (!uploadLocationPerdido.endsWith("/")) {
            uploadLocationPerdido += "/";
        }

        // mapa URL pública "/uploads/pets/**" → pasta física uploads/pets/
        registry.addResourceHandler("/uploads/pets/**")
                .addResourceLocations("file:///" + uploadLocation)
                .setCachePeriod(3600);

        // mapa URL pública "/uploads/pets-perdidos/**" → pasta física uploads/pets-perdidos/
        registry.addResourceHandler("/uploads/pets-perdidos/**")
                .addResourceLocations("file:///" + uploadLocationPerdido)
                .setCachePeriod(3600);
    }
}
