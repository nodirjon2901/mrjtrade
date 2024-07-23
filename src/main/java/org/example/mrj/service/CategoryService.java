package org.example.mrj.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.mrj.domain.dto.ApiResponse;
import org.example.mrj.domain.entity.Catalog;
import org.example.mrj.domain.entity.Category;
import org.example.mrj.domain.entity.CategoryItem;
import org.example.mrj.repository.CategoryItemRepository;
import org.example.mrj.repository.CategoryRepository;
import org.example.mrj.util.SlugUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryService
{

    private final CategoryRepository categoryRepo;

    private final ObjectMapper objectMapper;

    private final PhotoService photoService;
    private final CategoryItemRepository categoryItemRepository;

    public ResponseEntity<ApiResponse<Category>> addItem(String json, MultipartFile photo)
    {
        ApiResponse<Category> response = new ApiResponse<>();

        CategoryItem categoryItem;
        try
        {
            categoryItem = objectMapper.readValue(json, CategoryItem.class);
        } catch (JsonProcessingException e)
        {
            response.setMessage("Error parsing json" + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        categoryItem.setPhoto(photoService.save(photo));
        categoryItem.setSlug(SlugUtil.makeSlug(categoryItem.getTitle()));
        Optional<Integer> maxOrderNum = categoryItemRepository.getMaxOrderNum();
        categoryItem.setOrderNum(maxOrderNum.map(num -> num + 1).orElse(1));
        List<Category> all = categoryRepo.findAll();
        if (all.isEmpty())
        {
            Category category = new Category();
            category.setItemList(List.of(categoryItem));
            categoryRepo.save(category);
            response.setData(category);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }
        Category category = all.get(0);
        if (category.getItemList().isEmpty()) category.setItemList(List.of(categoryItem));
        else category.getItemList().add(categoryItem);
        response.setData(categoryRepo.save(category));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    public ResponseEntity<ApiResponse<Category>> get(Boolean main, Boolean active)
    {
        ApiResponse<Category> response = new ApiResponse<>();
        List<Category> all = categoryRepo.findAll();
        if (all.isEmpty())
        {
            response.setMessage("Category is null , not created yet");
            return ResponseEntity.status(404).body(response);
        }
        Category category = all.get(0);

        List<CategoryItem> categoryItemList = new ArrayList<>();
        for (CategoryItem categoryItem : category.getItemList())
        {
            if (categoryItem.getMain().equals(main) && categoryItem.getActive().equals(active))
                categoryItemList.add(categoryItem);
            else if (main == null && active == null)
            {
                categoryItemList.add(categoryItem);
            }
        }

        response.setMessage("Found");
        response.setData(new Category(category.getId(), category.getHeader(), categoryItemList, category.getAddable()));
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ApiResponse<List<Category>>> findAll()
    {
        ApiResponse<List<Category>> response = new ApiResponse<>();
        response.setData(new ArrayList<>());
        List<Category> all = categoryRepo.findAll();
        all.forEach(equipmentCategory -> response.getData().add(equipmentCategory));
        response.setMessage("Found " + all.size() + " categories");
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ApiResponse<Category>> update(Category newCategory)
    {
        ApiResponse<Category> response = new ApiResponse<>();
        List<Category> all = categoryRepo.findAll();
        if (all.isEmpty())
        {
            response.setMessage("Category is null, not created yet. You can't update it");
            return ResponseEntity.status(404).body(response);
        }
        Category fromDB = all.get(0);
        List<CategoryItem> fromDBItemList = fromDB.getItemList();
        List<CategoryItem> newItemList = newCategory.getItemList();
        int givenOrderNumCount = 0;
        for (CategoryItem newItem : newItemList)
        {
            if (newItem.getId() != null)
            {
                if (newItem.getTitle() != null)
                {
                    for (CategoryItem item : fromDBItemList)
                        if (item.getId().equals(newItem.getId()) && !item.getTitle().equals(newItem.getTitle()))
                        {
                            item.setTitle(newItem.getTitle());
                            item.setSlug(SlugUtil.makeSlug(newItem.getTitle()));
                        }
                }
                if (newItem.getCatalogList() != null)
                {
                    for (CategoryItem item : fromDBItemList)
                        if (item.getId().equals(newItem.getId()))
                        {
                            for (Catalog fromDb : item.getCatalogList())
                            {
                                for (Catalog newItemCatalog : newItem.getCatalogList())
                                {
                                    if (fromDb.getId().equals(newItemCatalog.getId()))
                                        fromDb.setName(newItemCatalog.getName());
                                }
                            }
                        }
                }
                if (newItem.getActive() != null)
                {
                    for (CategoryItem categoryItemFromDB : fromDBItemList)
                        if (categoryItemFromDB.getId().equals(newItem.getId()))
                            categoryItemFromDB.setActive(newItem.getActive());
                }
                if (newItem.getMain() != null)
                {
                    for (CategoryItem categoryItemFromDB : fromDBItemList)
                        if (categoryItemFromDB.getId().equals(newItem.getId()))
                            categoryItemFromDB.setMain(newItem.getMain());
                }
            }
            if (newItem.getOrderNum() != null)
                givenOrderNumCount++;
        }
        //Update order number
        if (givenOrderNumCount > 0 && givenOrderNumCount != fromDBItemList.size())
        {
            response.setMessage("In database have: " + fromDBItemList.size() + " item(s). But you send " + givenOrderNumCount + " order number(s)");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } else if (givenOrderNumCount == fromDBItemList.size() && givenOrderNumCount != 0)
        {
            replaceOrderNum(fromDBItemList, newItemList);
        }


        // Check header text:
        if (newCategory.getHeader() != null)
            fromDB.setHeader(newCategory.getHeader());

        response.setData(categoryRepo.save(fromDB));
        response.setMessage("Updated");
        return ResponseEntity.status(201).body(response);
    }

    private void replaceOrderNum(List<CategoryItem> fromDBItemList, List<CategoryItem> newItemList)
    {
        for (CategoryItem db : fromDBItemList)
        {
            Long dbId = db.getId();
            for (CategoryItem newItem : newItemList)
            {
                if (newItem.getId().equals(dbId))
                {
                    db.setOrderNum(newItem.getOrderNum());
                }
            }
        }
    }

    private void replaceMain(List<CategoryItem> fromDBItemList, Long id, Boolean active)
    {
        for (CategoryItem item : fromDBItemList)
        {
            if (item.getId().equals(id))
            {
                item.setActive(active);
            }
        }
    }

    private void replaceActive(List<CategoryItem> fromDBItemList, Long id, Boolean active)
    {
        for (CategoryItem item : fromDBItemList)
        {
            if (item.getId().equals(id))
                item.setActive(active);
        }
    }

    private void replaceTitle(List<CategoryItem> fromDBItemList, Long id, String title)
    {
        for (CategoryItem item : fromDBItemList)
        {
            if (item.getId().equals(id) && !item.getTitle().equals(title))
            {
                item.setTitle(title);
                item.setSlug(SlugUtil.makeSlug(title));
            }
        }
    }

    private void updateCatalogList(List<CategoryItem> fromDBItemList, Long id, List<Catalog> newCatalogList)
    {
        for (CategoryItem item : fromDBItemList)
        {
            if (item.getId().equals(id))
                item.setCatalogList(newCatalogList);
        }
    }


    public ResponseEntity<ApiResponse<?>> delete(Long id)
    {
        ApiResponse<?> response = new ApiResponse<>();
        if (categoryItemRepository.existsById(id))
        {
            response.setMessage("Category is not found by id: " + id);
            return ResponseEntity.status(404).body(response);
        }
        categoryItemRepository.deleteById(id);
        response.setMessage("Successfully deleted!");
        return ResponseEntity.status(200).body(response);
    }


    public ResponseEntity<ApiResponse<CategoryItem>> getItem(String slug)
    {
        ApiResponse<CategoryItem> response = new ApiResponse<>();
        Optional<CategoryItem> bySlug = categoryItemRepository.findBySlug(slug);
        if (bySlug.isEmpty())
        {
            response.setMessage("Category is not found by slug: " + slug);
            return ResponseEntity.status(404).body(response);
        }
        response.setData(bySlug.get());
        response.setMessage("Found");
        return ResponseEntity.status(200).body(response);
    }

}
