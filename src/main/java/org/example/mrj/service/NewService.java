package org.example.mrj.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.mrj.domain.NewnessWrapper;
import org.example.mrj.domain.dto.ApiResponse;
import org.example.mrj.domain.dto.NewDTO;
import org.example.mrj.domain.entity.New;
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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
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

    public ResponseEntity<ApiResponse<New>> create(String strNew, MultipartFile photoFile)
    {
        ApiResponse<New> response = new ApiResponse<>();
        Optional<Integer> maxOrderNum = newRepository.getMaxOrderNum();
        try
        {
            New newness = objectMapper.readValue(strNew, New.class);
            newness.setOrderNum(maxOrderNum.map(num -> num + 1).orElse(1));
            newness.setActive(true);
//            newness.setPhoto(photoService.save(photoFile));
            New save = newRepository.save(newness);
//            String slug = save.getId() + "-" + SlugUtil.makeSlug(save.getTitle());
//            newRepository.updateSlug(slug, save.getId());
//            save.setSlug(slug);
            response.setData(save);
            return ResponseEntity.status(200).body(response);
        } catch (JsonProcessingException e)
        {
            response.setMessage(e.getMessage());
            return ResponseEntity.status(409).body(response);
        }
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
        ApiResponse<New> response = new ApiResponse<>();
        New fromDB = newRepository.findById(newness.getId())
                .orElseThrow(() -> new NotFoundException("New is not found by id: " + newness.getId()));

        if (newness.getHeadOption() != null)
        {
            NewOption newHeadOption = newness.getHeadOption();

            if (newHeadOption.getHeading() != null)
            {
                fromDB.getHeadOption().setHeading(newHeadOption.getHeading());
                fromDB.setSlug(fromDB.getId() + "-" + SlugUtil.makeSlug(newHeadOption.getHeading()));
            }

            if (newHeadOption.getText() != null)
                fromDB.getHeadOption().setText(newHeadOption.getText());
        }

        if (newness.getNewOptions() != null)
        {
            List<NewOption> fromDBOptions = fromDB.getNewOptions();
            List<NewOption> newOptions = newness.getNewOptions();

            for (int i = 0; i < newOptions.size(); i++)
            {
                NewOption newOption = newOptions.get(i);
                if (newOption.getId() != null)
                {
                    for (int j = 0; j < fromDBOptions.size(); j++)
                    {
                        NewOption fromDbOption = fromDBOptions.get(j);
                        if (fromDbOption.getId().equals(newOption.getId()))
                        {
                            if (newOption.getHeading() != null)
                                fromDB.getHeadOption().setHeading(newOption.getHeading());

                            if (newOption.getText() != null)
                                fromDB.getHeadOption().setText(newOption.getText());

                            if (newOption.getHeading() == null && newOption.getText() == null)
                            {
                                deleteNewsOption(newOption.getId());
                                response.setMessage("Deleted news option id : " + newOption.getId() + "\n");
                            }

                            newness.setOrderNum(i + 1);
                            fromDB.setOrderNum(newness.getOrderNum());
                        }
                    }
                } else
                {
                    newOption.setOrderNum(i + 1);
                    fromDB.getNewOptions().add(newOption);
                }
            }

        }

        if (newness.getActive() != null)
            fromDB.setActive(newness.getActive());
        response.setMessage(response.getMessage() + "Updated");
        response.setData(newRepository.save(fromDB));
        return ResponseEntity.status(201).body(response);
    }

    private void deleteNewsOption(Long id)
    {
        if (!newOptionRepository.existsById(id))
            throw new NotFoundException("News option is not found by id: " + id);
        newOptionRepository.deleteById(id);
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
