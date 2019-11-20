package signature.controller;

import org.apache.commons.io.IOUtils;
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
import signature.config.StorageProperties;
import signature.dto.request.AdminDto;
import signature.dto.request.ClientDto;
import signature.dto.request.LoginPasswordDto;
import signature.dto.response.UserDtoResponse;
import signature.exceptions.ApiError;
import signature.exceptions.ErrorCode;
import signature.exceptions.ServiceException;
import signature.exceptions.StorageFileNotFoundException;
import signature.service.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.util.ArrayList;
import java.util.List;
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
    private StorageProperties properties;

    @Autowired
    public ApiController(AdminService adminService, UserService userService, ClientService clientService, SessionService sessionService, SettingsService settingsService, StorageService storageService, StorageProperties properties) {
        this.userService = userService;
        this.clientService = clientService;
        this.adminService = adminService;
        this.sessionService = sessionService;
        this.settingsService = settingsService;
        this.storageService = storageService;
        this.properties = properties;
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

    @GetMapping("formRegistrationClient")
    public String formRegistrationClient() {
        return "clientRegistration";
    }

    ///////////////////////////////////////////////////////////////////ADMIN////////////////////////////////////////////
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
        storageService.store(file, userId, Paths.get(properties.getLocationNotSigned()));
        model.addAttribute("message", "You successfully uploaded: " + file.getOriginalFilename() + " for user with id: " + userId);
        return "adminForm";
    }

    ////////////////////////////////////////////CLIENT//////////////////////////////////////////////////////////////////////

    @PostMapping("addClient")
    public String addClient(@ModelAttribute @Valid ClientDto clientDto, @RequestParam("file") MultipartFile file, HttpServletResponse response) throws ServiceException {
        if (!file.isEmpty()) {
            UserDtoResponse userDtoResponse = clientService.insert(clientDto);
            storageService.store(file, userDtoResponse.getId(), Paths.get(properties.getLocationKeys()));
            response.addCookie(sessionService.createNewSession(userDtoResponse.getId()));
        } else {
            List<ApiError> errorList = new ArrayList<>();
            ApiError apiError = new ApiError(ErrorCode.INVALID_PUBLIC_KEY.name(), null, ErrorCode.INVALID_PUBLIC_KEY.getErrorString());
            errorList.add(apiError);
            throw new ServiceException(errorList);
        }
        return "redirect:/client";
    }

    @GetMapping("/client")
    public String getClient(Model model, @CookieValue("JAVASESSIONID") String sessionId) throws ServiceException {
        int userId = sessionService.checkSessionId(sessionId);
        userService.checkClient(userId);
        model.addAttribute("files", storageService.loadAllNotSigned(userId).map(
                path -> MvcUriComponentsBuilder.fromMethodName(ApiController.class,
                        "serveFile", String.valueOf(userId), path.getFileName().toString()).build().toString())
                .collect(Collectors.toList()));
        model.addAttribute("sigDocs", storageService.loadAllSigned(userId).map(
                path -> MvcUriComponentsBuilder.fromMethodName(ApiController.class,
                        "serveFile", String.valueOf(userId), path.getFileName().toString()).build().toString())
                .collect(Collectors.toList()));
        return "clientForm";
    }

    private static Object readKey(final String filePath)
            throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(filePath);
        ObjectInputStream ois = new ObjectInputStream(fis);
        return ois.readObject();
    }

    @PostMapping("/client")
    public String addSigning(@RequestParam MultipartFile file, @CookieValue("JAVASESSIONID") String sessionId, Model model) throws ServiceException, IOException, ClassNotFoundException {
        int id = sessionService.checkSessionId(sessionId);
        userService.checkClient(id);
        PublicKey publicKey = (PublicKey) ApiController.readKey(properties.getLocationKeys() + "/" + id + "/public.key");
        try {
            //поиск и чтение исходного файла на сервере
            FileInputStream fis1 = new FileInputStream(storageService.loadResourceByFile(file, id).getFile());
            String text = IOUtils.toString(fis1, "UTF-8");
            fis1.close();
            // Создание подписи (хэш-функции с RSA)
            Signature signature = Signature.getInstance("SHA256withRSA");
            // Инициализация цифровой подписи открытым ключом
            signature.initVerify(publicKey);
            // Формирование цифровой подписи сообщения с открытым ключом
            signature.update(text.getBytes());
            // Открытие и чтение цифровой подписи сообщения
            FileInputStream fis = new FileInputStream(ApiController.convert(file));
            BufferedInputStream bis = new BufferedInputStream(fis);
            byte[] bytesSignature = new byte[bis.available()];
            bis.read(bytesSignature);
            fis.close();
            // Проверка цифровой подписи
            if (signature.verify(bytesSignature)) {
                //перемещение файла в подписанные
                File file1 = new File(storageService.loadResourceByFile(file, id).getURI());
                Path signPath = Paths.get(properties.getLocationSigned().concat("/" + id));
                File file2 = new File(signPath +"/"+ file1.getName());
                InputStream inStream = new FileInputStream(file1);
                OutputStream outStream = new FileOutputStream(file2);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inStream.read(buffer)) > 0) {
                    outStream.write(buffer, 0, length);
                }
                inStream.close();
                outStream.close();
                file1.delete();
            }
        } catch (IOException | InvalidKeyException | SignatureException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "redirect:/client";
    }

    private static File convert(MultipartFile file) throws IOException {
        File convertFile = new File(file.getOriginalFilename());
        convertFile.createNewFile();
        FileOutputStream fos = new FileOutputStream(convertFile);
        fos.write(file.getBytes());
        fos.close();
        return convertFile;
    }


    @GetMapping("/sessions")
    public String logout(@CookieValue("JAVASESSIONID") String sessionId) throws ServiceException {
        sessionService.deleteSession(sessionId);
        return "redirect:/";
    }

    @GetMapping(path = "/{id:.+}/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable int id, @PathVariable String filename) {

        Resource file = storageService.loadAsResource(filename, id);
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
