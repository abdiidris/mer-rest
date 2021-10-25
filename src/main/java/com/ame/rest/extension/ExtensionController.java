package com.ame.rest.extension;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(path = "/extension")
public class ExtensionController{

    @Autowired
    private ExtensionRepository repo;

    @GetMapping(path = "/all")
    @ResponseBody
    public Iterable<Extension> getAll() {
        return repo.findAll();
    }

    @PostMapping(value = "/register")
    @ResponseBody
    public void addExtension(@RequestBody Extension extension){
        repo.save(extension);
    }

    @PostMapping(path = "/run")
    @ResponseBody
    public String run(){
        return "nice";
    }
}
