package org.example.mrj.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.mrj.domain.dto.ApiResponse;
import org.example.mrj.domain.entity.Catalog;
import org.example.mrj.domain.entity.Category;
import org.example.mrj.domain.entity.CategoryItem;
import org.example.mrj.repository.CategoryItemRepository;
import org.example.mrj.repository.CategoryRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryService
{

    private final CategoryRepository categoryRepo;

    private final ObjectMapper objectMapper;

    private final PhotoService photoService;
    private final CategoryItemRepository categoryItemRepository;

    public ResponseEntity<ApiResponse<Category>> addItem(CategoryItem categoryItem)
    {
        ApiResponse<Category> response = new ApiResponse<>();
        List<Category> all = categoryRepo.findAll();
        if (all.isEmpty())
        {
            Category category = new Category();
            category.setItemList(List.of(categoryItem));
            categoryRepo.save(category);
            response.setData(category);
        }
        Category category = all.get(0);
        if (category.getItemList().isEmpty())
            category.setItemList(List.of(categoryItem));
        else
            category.getItemList().add(categoryItem);
        response.setData(categoryRepo.save(category));
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    public ResponseEntity<ApiResponse<Category>> findById(Long id)
    {
        ApiResponse<Category> response = new ApiResponse<>();
        Optional<Category> optionalEquipmentCategory = categoryRepo.findById(id);
        if (optionalEquipmentCategory.isEmpty())
        {
            response.setMessage("Category is not found by id: " + id);
            return ResponseEntity.status(404).body(response);
        }
        Category category = optionalEquipmentCategory.get();
        response.setMessage("Found");
        response.setData(category);
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ApiResponse<Category>> get()
    {
        ApiResponse<Category> response = new ApiResponse<>();
        List<Category> all = categoryRepo.findAll();
        if (all.isEmpty())
        {
            response.setMessage("Category is null , not created yet");
            return ResponseEntity.status(404).body(response);
        }
        Category category = all.get(0);
        response.setMessage("Found");
        response.setData(category);
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
        List<CategoryItem> itemList = newCategory.getItemList();
        boolean allOrderNumberGiven = true;
        for (CategoryItem item : itemList)
        {
            if (item.getTitle() != null) categoryItemRepository.updateTitle(item.getId(), item.getTitle());
            if (item.getSlug() != null) categoryItemRepository.updateSlug(item.getId(), item.getSlug());
            if (item.getCatalogList() != null) updateCatalogList(item.getCatalogList());
            if (item.getActive() != null) categoryItemRepository.updateActive(item.getId(), item.getActive());
            if (item.getMain() != null) categoryItemRepository.updateMain(item.getId(), item.getMain());
            if (item.getOrder() == null) allOrderNumberGiven = false;
        }
        //Update order number
        if (!allOrderNumberGiven) //if not given all order number
        {
            response.setMessage("Not given all order's number. If you change order any number, please send all item's new order number");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        // Fetch existing order values
        List<Integer> existingOrders = categoryItemRepository.findAllExistingOrders();
        Set<Integer> orderSet = new HashSet<>(existingOrders);

        // Start assigning orders from the next available integer
        int newOrder = existingOrders.isEmpty() ? 1 : existingOrders.stream().max(Integer::compareTo).orElse(0) + 1;

        for (CategoryItem item : itemList)
        {
            while (orderSet.contains(newOrder))
            {
                newOrder++;
            }
            item.setOrder(newOrder);
            orderSet.add(newOrder);
        }

        fromDB.setItemList(itemList);
        response.setData(categoryRepo.save(fromDB));
        response.setMessage("Updated");
        return ResponseEntity.status(201).body(response);
    }

    private void updateCatalogList(List<Catalog> newCatalogList)
    {

    }


    public ResponseEntity<ApiResponse<?>> delete(Long id)
    {
        ApiResponse<?> response = new ApiResponse<>();
        if (categoryRepo.findById(id).isEmpty())
        {
            response.setMessage("Category is not found by id: " + id);
            return ResponseEntity.status(404).body(response);
        }
        categoryRepo.deleteById(id);
        response.setMessage("Successfully deleted!");
        return ResponseEntity.status(200).body(response);
    }


}
