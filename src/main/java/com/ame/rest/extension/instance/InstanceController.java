package com.ame.rest.extension.instance;

import java.util.Map;

import com.ame.rest.exceptions.UnexpectedUserType;
import com.ame.rest.extension.instance.Instance.STATE;
import com.ame.rest.util.dto.InstanceDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(path = "/instance")
@PreAuthorize("isAuthenticated()")
public class InstanceController {

    @Autowired
    private InstanceService service;

    @GetMapping(value = "/")
    @PreAuthorize("hasRole('WRITER')")
    @ResponseBody
    public Iterable<InstanceDTO> getInstances() throws Exception {
        return service.getInstances();
    }

    @PostMapping(value = "/create")
    @PreAuthorize("hasRole('WRITER')")
    @ResponseBody
    public ResponseEntity<String> createInstance(@RequestParam Long extension) {

        try {
            service.CreateInstance(extension);
        } catch (UnexpectedUserType e) {
            return new ResponseEntity<String>(e.getMessage() ,HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<String>("Something went wrong : (", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<String>("Instance created", HttpStatus.OK);
    }

    @GetMapping(value = "/run/{id}")
    @PreAuthorize("permitAll()")
    @ResponseBody
    public String run(@PathVariable Long id) throws Exception{
        return service.runInstance(id);
    }

    @PostMapping(value = "/data/get")
    @PreAuthorize("permitAll()")  //TODO
    @ResponseBody
    public String getData(@RequestBody Map<String,String> request) throws Exception{
        return service.getData(request);
    }

    @PostMapping(value = "/data/set")
    @PreAuthorize("permitAll()") //TODO
    @ResponseBody
    public void setData(@RequestBody Map<String,String> request) throws Exception{
        service.setData(request);
    }

    @PostMapping(value = "/state/set")
    @PreAuthorize("hasRole('WRITER')")
    @ResponseBody
    public void updateState(@RequestParam Long id, @RequestParam String state) throws Exception{
        service.updateState(id,state);
    }
}
