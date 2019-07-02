package signature.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import signature.exceptions.StorageFileNotFoundException;
import signature.service.StorageService;

import java.io.IOException;
import java.util.stream.Collectors;

@Controller
public class ControllerApi {

    private final StorageService storageService;

    @Autowired
    public ControllerApi(StorageService storageService) {
        this.storageService = storageService;
    }

  /*  @GetMapping("/")
    public String listUploadedFiles(Model model) throws IOException {
        model.addAttribute("files", storageService.loadAllNotSigned("1").map(
                path -> MvcUriComponentsBuilder.fromMethodName(ControllerApi.class,
                        "serveFile2", "1", path.getFileName().toString()).build().toString())
                .collect(Collectors.toList()));

        return "clientForm";
    }*/


    /*@GetMapping("/home")
    public String list(Model model) throws IOException {

        model.addAttribute("files", storageService.loadAll("1").map(
                path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
                        "serveFile", path.getFileName().toString()).build().toString())
                .collect(Collectors.toList()));

        return "uploadForm";
    }*/

   /* @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

        Resource file = storageService.loadAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }
*/
    /*@GetMapping(path = "/files/{id:.+}/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile2(@PathVariable String id ,@PathVariable String filename) {

        Resource file = storageService.loadAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @PostMapping("/")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
            RedirectAttributes redirectAttributes) {

        storageService.store(file);
        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded " + file.getOriginalFilename() + "!");

        return "redirect:/";
    }*/

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

}
