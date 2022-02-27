package com.ame.rest.extension;

import java.util.List;

import com.ame.rest.exceptions.MissingParameterException;
import com.ame.rest.exceptions.UnexpectedUserType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(path = "/extension")
@PreAuthorize("isAuthenticated()")
public class ExtensionController {

    @Autowired
    private ExtensionService service;

    @GetMapping(value = "/all", produces = "application/json")
    @ResponseBody
    public Iterable<BrowseExtensionDTO> getAll() {
         Iterable<BrowseExtensionDTO> extension = service.findAll();
         return extension;
    }

    @PostMapping(value = "/register")
    @ResponseBody
    @PreAuthorize("hasRole('DEVELOPER')")
    public ResponseEntity<String> registerExtension(@RequestBody Extension extension) {

        try {
            service.registerExtension(extension);
        } catch (UnexpectedUserType | MissingParameterException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<String>("Something went wrong : (", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<String>("Extension registered", HttpStatus.OK);
    }

    @GetMapping(value = "/user")
    @PreAuthorize("hasRole('DEVELOPER')")
    @ResponseBody
    public List<DevelopExtensionDTO> getMyExtensions() throws Exception {
        return service.findByDeveloper();
    }

}
