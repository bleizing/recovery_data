package com.sae.controller;

import com.sae.models.response.WebResponse;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/download")
@Validated
public class DownloadController {

    public WebResponse<String> downloadFileSQL(
            @PathVariable String fileName
    ){

        return WebResponse.<String>builder().data("").build();
    }
}
