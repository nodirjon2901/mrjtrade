package org.example.mrj.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;
import lombok.RequiredArgsConstructor;
import org.example.mrj.domain.NewnessWrapper;
import org.example.mrj.domain.dto.ApiResponse;
import org.example.mrj.domain.dto.NewDTO;
import org.example.mrj.domain.entity.New;
import org.example.mrj.domain.entity.NewOption;
import org.example.mrj.service.NewService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;

@RestController
@RequestMapping("/news")
@RequiredArgsConstructor
public class NewController {

    private final NewService newService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<New>> createNew(
//            @RequestParam(value = "json") String newness,
            @RequestPart(value = "main-photo") MultipartFile mainPhoto,
            HttpServletRequest request) throws ServletException, IOException
    {
        Collection<Part> parts = request.getParts();

        for (Part part : parts)
        {
            System.err.println("part.getName() = " + part.getName());
            // Check if the part is a file
            if (part.getSubmittedFileName() != null)
            {
                // Cast the part to MultipartFile
                MultipartFile multipartFile = new MultipartFile()
                {
                    @Override
                    public String getName()
                    {
                        return part.getName();
                    }

                    @Override
                    public String getOriginalFilename()
                    {
                        return part.getSubmittedFileName();
                    }

                    @Override
                    public String getContentType()
                    {
                        return part.getContentType();
                    }

                    @Override
                    public boolean isEmpty()
                    {
                        return part.getSize() == 0;
                    }

                    @Override
                    public long getSize()
                    {
                        return part.getSize();
                    }

                    @Override
                    public byte[] getBytes() throws IOException
                    {
                        return part.getInputStream().readAllBytes();
                    }

                    @Override
                    public InputStream getInputStream() throws IOException
                    {
                        return part.getInputStream();
                    }

                    @Override
                    public void transferTo(File dest) throws IOException, IllegalStateException
                    {
                        try (InputStream in = part.getInputStream(); OutputStream out = new FileOutputStream(dest))
                        {
                            in.transferTo(out);
                        }
                    }
                };

                // Handle the file here
                System.err.println("Received file: " + multipartFile.getOriginalFilename());
                System.err.println("Parameter name: " + multipartFile.getName());
            }

//            return newService.create(newness, mainPhoto);
            return null;
        }

        return null;
    }

    @GetMapping("/get/{slug}")
    public ResponseEntity<ApiResponse<New>> findBySlug(
            @PathVariable String slug
    ) {
        return newService.findBySlug(slug);
    }

    @GetMapping("/get-all")
    public ResponseEntity<ApiResponse<List<NewDTO>>> findAll(
            @RequestParam(value = "page",defaultValue = "1") Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "12") Integer size) {
        return newService.findAllByPageNation(page, size);

    }

    @GetMapping("/get-all-other/{newSlug}")
    public ResponseEntity<ApiResponse<List<NewDTO>>> findOtherFourNews(
            @PathVariable String newSlug
    ) {
        return newService.findOtherFourNews(newSlug);
    }

    @PutMapping("/change-order")
    public ResponseEntity<ApiResponse<List<NewDTO>>> changeOrder(
            @RequestBody List<NewnessWrapper> newnessWrapperList
    ) {
        return newService.changeOrder(newnessWrapperList);
    }


    @PutMapping("/update")
    public ResponseEntity<ApiResponse<New>> update(
            @RequestBody New newness) {
        return newService.update(newness);
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<?>> deleteNew(
            @PathVariable Long id) {
        return newService.deleteById(id);
    }


}
