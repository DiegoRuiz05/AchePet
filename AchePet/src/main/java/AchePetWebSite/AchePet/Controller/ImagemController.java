package AchePetWebSite.AchePet.Controller;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/imagens")
@CrossOrigin(origins = "*")
public class ImagemController {

    private static final String IMAGE_UPLOAD_DIR = "C:\\Users\\Diego\\Desktop\\ImagemPets\\";

    @GetMapping("/{nomeImagem}")
    public ResponseEntity<Resource> getImagem(@PathVariable String nomeImagem) {
        try {
            Path caminho = Paths.get(IMAGE_UPLOAD_DIR + nomeImagem);
            Resource resource = new UrlResource(caminho.toUri());

            if (!resource.exists()) {
                return ResponseEntity.notFound().build();
            }

            String contentType = Files.probeContentType(caminho);
            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);

        } catch (MalformedURLException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
