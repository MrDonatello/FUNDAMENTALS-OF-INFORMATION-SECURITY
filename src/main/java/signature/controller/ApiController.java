package signature.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import signature.dto.request.AdminDto;
import signature.dto.request.ClientDto;
import signature.dto.request.LoginPasswordDto;
import signature.dto.response.UserDtoResponse;
import signature.exceptions.ServiceException;
import signature.exceptions.StorageFileNotFoundException;
import signature.service.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.stream.Collectors;

@Controller
@RequestMapping
@Profile({"prod", "debug"})
public class ApiController {

    private final AdminService adminService;
    private final UserService userService;
    private final ClientService clientService;
    private final SessionService sessionService;
    private final SettingsService settingsService;
    private final StorageService storageService;

    @Autowired
    public ApiController(AdminService adminService, UserService userService, ClientService clientService, SessionService sessionService, SettingsService settingsService, StorageService storageService) {
        this.userService = userService;
        this.clientService = clientService;
        this.adminService = adminService;
        this.sessionService = sessionService;
        this.settingsService = settingsService;
        this.storageService = storageService;
    }

    @GetMapping("/")
    public String homeRegistration() {
        return "home";
    }

    @PostMapping
    public String login(@ModelAttribute LoginPasswordDto loginPasswordDto, HttpServletResponse response) throws ServiceException {
        UserDtoResponse userDtoResponse = userService.login(loginPasswordDto);
        response.addCookie(sessionService.updateSession(userDtoResponse.getId()));
        switch (userDtoResponse.getUserType()) {
            case "ADMIN":
                return "redirect:/admin";
            case "CLIENT":
                return "redirect:/client";
            default:
                return "redirect: /";
        }
    }

    @GetMapping("formRegistrationAdmin")
    public String formRegistrationAdmin() {
        return "adminRegistration";
    }

    @PostMapping("addAdmin")
    public String addAdmin(@ModelAttribute @Valid AdminDto adminDto, HttpServletResponse response) throws ServiceException {
        UserDtoResponse userDtoResponse = adminService.insert(adminDto);
        response.addCookie(sessionService.createNewSession(userDtoResponse.getId()));
        return "redirect:/admin";
    }

    @GetMapping("/admin")
    public String getAdmin(@CookieValue("JAVASESSIONID") String sessionId) throws ServiceException {
        int id = sessionService.checkSessionId(sessionId);
        userService.checkAdmin(id);
        return "adminForm";
    }

    @PostMapping("/admin")
    public String addDocuments(@RequestParam("file") MultipartFile file, @RequestParam("userId") int userId,
                               @CookieValue("JAVASESSIONID") String sessionId, Model model) throws ServiceException {
        int id = sessionService.checkSessionId(sessionId);
        userService.checkAdmin(id);
        storageService.store(file, userId);
        model.addAttribute("message", "You successfully uploaded: " + file.getOriginalFilename() + " for user with id: " + userId);
        return "adminForm";
    }
////////////////////////////////////////////ADMIN_END/////////////////////////////////////////////////////////////

    @GetMapping("formRegistrationClient")
    public String formRegistrationClient() {
        return "clientRegistration";
    }

    @PostMapping("addClient")
    public String addClient(@ModelAttribute @Valid ClientDto clientDto, HttpServletResponse response) throws ServiceException {
        UserDtoResponse userDtoResponse = clientService.insert(clientDto);
        response.addCookie(sessionService.createNewSession(userDtoResponse.getId()));
        return "redirect:/client";
    }

    @GetMapping("/client")
    public String getClient(Model model, @CookieValue("JAVASESSIONID") String sessionId) throws ServiceException {
        int userId = sessionService.checkSessionId(sessionId);
        userService.checkClient(userId);
        model.addAttribute("files", storageService.loadAllNotSigned(userId).map(
                path -> MvcUriComponentsBuilder.fromMethodName(ApiController.class,
                        "serveFile2", String.valueOf(userId), path.getFileName().toString()).build().toString())
                .collect(Collectors.toList()));
        model.addAttribute("sigDocs", storageService.loadAllSigned(userId).map(
                path -> MvcUriComponentsBuilder.fromMethodName(ApiController.class,
                        "serveFile2", String.valueOf(userId), path.getFileName().toString()).build().toString())
                .collect(Collectors.toList()));
        return "clientForm";
    }

    @GetMapping("/sessions")
    public String logout(@CookieValue("JAVASESSIONID") String sessionId) throws ServiceException {
        sessionService.deleteSession(sessionId);
        return "redirect:/";
    }

    @GetMapping(path = "/files/{id:.+}/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile2(@PathVariable int id, @PathVariable String filename) {

        Resource file = storageService.loadAsResource(filename,id);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }
}
/*
    @GetMapping("/api/accounts")
    public UserDtoResponse infoAccounts(@CookieValue("JAVASESSIONID") String sessionId) throws ServiceException {
        return userService.infoAccounts(sessionService.checkSessionId(sessionId));
    }

    @GetMapping("/api/clients")
    public List<UserDtoResponse> infoClient(@CookieValue("JAVASESSIONID") String sessionId) throws ServiceException {
        userService.checkAdmin(sessionService.checkSessionId(sessionId));
        return adminService.infoClient();
    }
*/
