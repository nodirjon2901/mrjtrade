package org.example.mrj.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.mrj.domain.NewnessWrapper;
import org.example.mrj.domain.dto.ApiResponse;
import org.example.mrj.domain.dto.NewDTO;
import org.example.mrj.domain.entity.New;
import org.example.mrj.domain.entity.NewHeadOption;
import org.example.mrj.domain.entity.NewOption;
import org.example.mrj.exception.NotFoundException;
import org.example.mrj.repository.NewOptionRepository;
import org.example.mrj.repository.NewRepository;
import org.example.mrj.util.SlugUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class NewService
{

    private final NewRepository newRepository;

    private final NewOptionRepository newOptionRepository;

    private final PhotoService photoService;

    private final ObjectMapper objectMapper;

    public ResponseEntity<ApiResponse<New>> create(String strNew, MultipartHttpServletRequest request)
    {
        ApiResponse<New> response = new ApiResponse<>();
        Optional<Integer> maxOrderNum = newRepository.getMaxOrderNum();
        try
        {
            New newness = objectMapper.readValue(strNew, New.class);
            newness.setOrderNum(maxOrderNum.map(num -> num + 1).orElse(1));
            newness.setActive(true);
            Iterator<String> fileNames = request.getFileNames();
            while (fileNames.hasNext())
            {
                String key = fileNames.next();
                MultipartFile photo = request.getFile(key);
                setNewsPhoto(key, photo, newness);
            }
            New save = newRepository.save(newness);
            String slug = save.getId() + "-" + SlugUtil.makeSlug(save.getHead().getTitle());
            newRepository.updateSlug(slug, save.getId());
            save.setSlug(slug);
            response.setData(save);

            System.err.println("save.getHead() = " + save.getHead());

            return ResponseEntity.ok().body(response);
        } catch (JsonProcessingException e)
        {
            response.setMessage(e.getMessage());
            return ResponseEntity.status(409).body(response);
        }
    }

    private void setNewsPhoto(String key, MultipartFile photo, New newness)
    {
        if (key.equalsIgnoreCase("main-photo"))
        {
            newness.getHead().setPhoto(photoService.save(photo));
            return;
        }
        int index = Integer.parseInt(key.substring(12)) - 1;
        NewOption newOption = newness.getNewOptions().get(index);
        newOption.setPhoto(photoService.save(photo));
    }

    public ResponseEntity<ApiResponse<New>> findById(Long id)
    {
        ApiResponse<New> response = new ApiResponse<>();
        Optional<New> optionalNew = newRepository.findById(id);
        if (optionalNew.isEmpty())
        {
            response.setMessage("New is not found by id: " + id);
            return ResponseEntity.status(404).body(response);
        }
        New newness = optionalNew.get();
        response.setMessage("Found");
        response.setData(newness);
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ApiResponse<New>> findBySlug(String slug)
    {
        ApiResponse<New> response = new ApiResponse<>();
        Optional<New> optionalNew = newRepository.findBySlug(slug);
        if (optionalNew.isEmpty())
        {
            response.setMessage("New is not found by slug: " + slug);
            return ResponseEntity.status(404).body(response);
        }
        New newness = optionalNew.get();
        response.setMessage("Found");
        response.setData(newness);
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ApiResponse<List<NewDTO>>> findAll()
    {
        ApiResponse<List<NewDTO>> response = new ApiResponse<>();
        List<New> all = newRepository.findAll();
        response.setData(new ArrayList<>());
        all.forEach(newness -> response.getData().add(new NewDTO(newness)));
        response.getData().sort(Comparator.comparing(NewDTO::getOrderNum));
        response.setMessage("Found " + all.size() + " new(s)");
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ApiResponse<List<NewDTO>>> findAllByPageNation(int page, int size)
    {
        ApiResponse<List<NewDTO>> response = new ApiResponse<>();
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<New> all = newRepository.findAll(pageable);
        Page<NewDTO> map = all.map(NewDTO::new);
        response.setData(map.getContent());
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ApiResponse<List<NewDTO>>> findOtherFourNews(String newSlug)
    {
        ApiResponse<List<NewDTO>> response = new ApiResponse<>();
        List<New> all = newRepository.findAllByOrderByIdAsc();
        response.setData(new ArrayList<>());
        all.stream()
                .filter(newness -> !newness.getSlug().equals(newSlug))
                .limit(4).toList()
                .forEach(newness -> response.getData().add(new NewDTO(newness)));
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ApiResponse<New>> update(New newness)
    {
        System.err.println("newness.getHead() = " + newness.getHead());

        ApiResponse<New> response = new ApiResponse<>();
        try
        {
            New fromDB = newRepository.findById(newness.getId())
                    .orElseThrow(() -> new NotFoundException("New is not found by id: " + newness.getId()));

            if (newness.getHead() != null)
            {
                NewHeadOption newHead = newness.getHead();

                if (newHead.getTitle() != null)
                {
                    fromDB.getHead().setTitle(newHead.getTitle());
                    String slug = fromDB.getId() + "-" + SlugUtil.makeSlug(newHead.getTitle());
                    fromDB.setSlug(slug);
                }

                if (newHead.getBody() != null)
                {
                    fromDB.getHead().setBody(newHead.getBody());
                }
            }

            if (newness.getNewOptions() != null)
            {
                List<NewOption> fromDBOptions = fromDB.getNewOptions();
                List<NewOption> newOptions = newness.getNewOptions();

                Map<Long, NewOption> existingOptionsMap = fromDBOptions.stream()
                        .collect(Collectors.toMap(NewOption::getId, option -> option));

                List<NewOption> toRemove = new ArrayList<>();
                for (NewOption newOption : newOptions)
                {
                    if (newOption.getId() != null)
                    {
                        NewOption existingOption = existingOptionsMap.get(newOption.getId());
                        if (newOption.getHeading() == null && newOption.getText() == null)
                        {
                            toRemove.add(existingOption);
                        }
                    }
                }

                fromDBOptions.removeAll(toRemove);
                newOptions.removeAll(toRemove);

                List<NewOption> updatedOptions = new ArrayList<>();

                for (int i = 0; i < newOptions.size(); i++)
                {
                    NewOption newOption = newOptions.get(i);

                    if (newOption.getId() != null)
                    {
                        NewOption existingOption = existingOptionsMap.get(newOption.getId());
                        if (existingOption != null)
                        {
                            if (newOption.getHeading() != null)
                            {
                                existingOption.setHeading(newOption.getHeading());
                            }
                            if (newOption.getText() != null)
                            {
                                existingOption.setText(newOption.getText());
                            }
                            existingOption.setOrderNum(i + 1);
                            updatedOptions.add(existingOption);
                        }
                    } else
                    {
                        newOption.setOrderNum(i + 1);
                        updatedOptions.add(newOption);
                    }
                }

                fromDB.getNewOptions().clear();
                fromDB.getNewOptions().addAll(updatedOptions);
            }

            if (newness.getActive() != null)
            {
                fromDB.setActive(newness.getActive());
            }

            response.setMessage("Updated");
            response.setData(newRepository.save(fromDB));
            return ResponseEntity.status(201).body(response);
        } catch (Exception e)
        {
            response.setMessage("Error: " + e.getMessage());
            return ResponseEntity.status(400).body(response);
        }
    }


    public ResponseEntity<ApiResponse<New>> update2(New newness)
    {

        ApiResponse<New> response = new ApiResponse<>();
        New fromDB = newRepository.findById(newness.getId())
                .orElseThrow(() -> new NotFoundException("New is not found by id: " + newness.getId()));

        if (newness.getHead() != null)
        {
            NewHeadOption newHead = newness.getHead();

            if (newHead.getTitle() != null)
            {
                fromDB.getHead().setTitle(newHead.getTitle());
            }

            if (newHead.getBody() != null)
                fromDB.getHead().setBody(newHead.getBody());
        }

        if (newness.getNewOptions() != null)
        {
            List<NewOption> fromDBOptions = fromDB.getNewOptions();
            List<NewOption> newOptions = newness.getNewOptions();

            for (NewOption newOption : newOptions)
            {

                for (NewOption fromDBOption : fromDBOptions)
                {
                    if (newOption.getId() != null && newOption.getId().equals(fromDBOption.getId()))
                    {
                        if (newOption.getHeading() != null) fromDBOption.setHeading(newOption.getHeading());
                        if (newOption.getText() != null) fromDBOption.setText(newOption.getText());

                        if (newOption.getText() == null && newOption.getHeading() == null)
                        {
                            System.err.println("newOption.getId() = " + newOption.getId());
                            newOptionRepository.deleteee(newOption.getId());
                        }
                    }
                }

                if (newOption.getId() == null)
                {
                    newOption.setNewness(fromDB);
                    fromDBOptions.add(newOption);
                }

            }
        }
        response.setData(newRepository.save(fromDB));
        response.setMessage("Updated");
        return ResponseEntity.status(201).body(response);
    }


    public ResponseEntity<ApiResponse<List<NewDTO>>> changeOrder(List<NewnessWrapper> newnessWrapperList)
    {
        ApiResponse<List<NewDTO>> response = new ApiResponse<>();
        response.setData(new ArrayList<>());
        List<New> dbAll = newRepository.findAll();
        if (dbAll.size() != newnessWrapperList.size())
        {
            response.setMessage("In database have: " + dbAll.size() + " newness. But you send " + newnessWrapperList.size() + " order number(s)");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        for (New db : newRepository.findAll())
        {
            Long dbId = db.getId();
            for (NewnessWrapper newnessWrapper : newnessWrapperList)
            {
                if (newnessWrapper.id().equals(dbId))
                {
                    db.setOrderNum(newnessWrapper.orderNum());
                    response.getData().add(new NewDTO(newRepository.save(db)));
                }
            }
        }
        response.getData().sort(Comparator.comparing(NewDTO::getOrderNum));
        return ResponseEntity.status(201).body(response);
    }

    public ResponseEntity<ApiResponse<?>> deleteById(Long id)
    {
        ApiResponse<?> response = new ApiResponse<>();
        if (newRepository.findById(id).isEmpty())
        {
            response.setMessage("New is not found by id: " + id);
            return ResponseEntity.status(404).body(response);
        }
        newRepository.deleteById(id);
        response.setMessage("Successfully deleted");
        return ResponseEntity.status(200).body(response);
    }
}
