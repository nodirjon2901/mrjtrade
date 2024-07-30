package org.example.mrj.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.mrj.controller.RequestLoggingFilter;
import org.example.mrj.domain.dto.ApiResponse;
import org.example.mrj.domain.dto.CategoryItemDTO;
import org.example.mrj.domain.entity.Catalog;
import org.example.mrj.domain.entity.Category;
import org.example.mrj.domain.entity.CategoryItem;
import org.example.mrj.exception.NoUniqueNameException;
import org.example.mrj.repository.CatalogRepository;
import org.example.mrj.repository.CategoryItemRepository;
import org.example.mrj.repository.CategoryRepository;
import org.example.mrj.repository.Product2Repository;
import org.example.mrj.util.SlugUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final CatalogRepository catalogRepository;
    private final Product2Repository product2Repository;

    private static final Logger logger = LoggerFactory.getLogger(RequestLoggingFilter.class);

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
//        Optional<Integer> maxOrderNum = categoryItemRepository.getMaxOrderNum();
//        categoryItem.setOrderNum(maxOrderNum.map(num -> num + 1).orElse(1));
        List<Category> all = categoryRepo.findAll();
        Category category = new Category();

        if (all.isEmpty())
        {
            category.setItems(List.of(categoryItem));
            response.setMessage("First category saved");
        } else
        {
            if (categoryItemRepository.existsByTitleEqualsIgnoreCase(categoryItem.getTitle()))
            {
                logger.info("Name '{}' already exists", categoryItem.getTitle());
                throw new NoUniqueNameException("Name '" + categoryItem.getTitle() + "' already exists");
            }
            category = all.get(0);

            category.getItems().add(categoryItem);
            categoryItem.setCategory(category);

            response.setMessage("Category added");
        }
        categoryItemRepository.save(categoryItem);
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
        for (CategoryItem categoryItem : category.getItems())
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
        List<CategoryItem> fromDBItemList = fromDB.getItems();
        List<CategoryItem> newItemList = newCategory.getItems();
        if (newItemList != null)
            for (CategoryItem newItem : newItemList)
            {
                if (newItem.getId() != null)
                {
                    //Check title
                    if (newItem.getTitle() != null)
                    {
                        for (CategoryItem itemDB : fromDBItemList)
                            if (itemDB.getId().equals(newItem.getId()) && !itemDB.getTitle().equals(newItem.getTitle()))
                            {
                                if (categoryItemRepository.existsByTitleEqualsIgnoreCase(newItem.getTitle()))
                                    throw new NoUniqueNameException("Name '" + newItem.getTitle() + "' already exists");
                                itemDB.setTitle(newItem.getTitle());
                                itemDB.setSlug(SlugUtil.makeSlug(newItem.getTitle()));
                            }
                    }

                    //Check catalog list
                    if (newItem.getCatalogList() != null)
                    {
                        for (CategoryItem fromDbItem : fromDBItemList)
                        {
                            if (fromDbItem.getId().equals(newItem.getId()))
                            {
                                for (Catalog newItemCatalog : newItem.getCatalogList())
                                {
                                    for (Catalog fromDb : fromDbItem.getCatalogList())
                                    {
                                        //if given id found from database....
                                        if (newItemCatalog.getId() != null && fromDb.getId().equals(newItemCatalog.getId()))
                                        {
                                            //if new name given update else not given, delete this catalog
                                            if (newItemCatalog.getName() != null)
                                                fromDb.setName(newItemCatalog.getName());
                                            else
                                            {
                                                List<Long> catalogIds = categoryItemRepository.getCatalogIds(newItemCatalog.getId());

                                                Integer countByCatalogIds = product2Repository.findCountByCatalogIds(catalogIds);
                                                if (countByCatalogIds > 0)
                                                {
                                                    logger.info("You are trying to delete a category-item. But inside of this category-item's CATALOG have {} product(s). Please delete first this product(s) or replace CATALOG of this product", countByCatalogIds);
                                                    throw new RuntimeException("You are trying to delete a category-item. But inside of this category-item's CATALOG have product(s). Please delete first this product(s) or replace CATALOG of this product");
                                                }
                                                catalogRepository.deleteById(newItemCatalog.getId());
                                            }
                                        }
                                    }
                                    //if catalog haven't id and name is not null add to database
                                    if (newItemCatalog.getId() == null && newItemCatalog.getName() != null)
                                    {
                                        newItemCatalog.setCategoryItem(fromDbItem);
                                        fromDbItem.getCatalogList().add(newItemCatalog);
                                    }
                                }
                            }
                        }
                    }

                    //Check active
                    if (newItem.getActive() != null)
                    {
                        for (CategoryItem categoryItemFromDB : fromDBItemList)
                            if (categoryItemFromDB.getId().equals(newItem.getId()))
                                categoryItemFromDB.setActive(newItem.getActive());
                    }

                    //Check main
                    if (newItem.getMain() != null)
                    {
                        for (CategoryItem categoryItemFromDB : fromDBItemList)
                            if (categoryItemFromDB.getId().equals(newItem.getId()))
                                categoryItemFromDB.setMain(newItem.getMain());
                    }
                }
            }

        // Check header text:\
        if (newCategory.getHeader() != null)
            fromDB.setHeader(newCategory.getHeader());

        response.setData(categoryRepo.save(fromDB));
        response.setMessage("Updated");
        return ResponseEntity.status(201).body(response);
    }

/*    private void replaceOrderNum(List<CategoryItem> fromDBItemList, List<CategoryItem> newItemList)
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
    }*/

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
        if (!categoryItemRepository.existsById(id))
        {
            response.setMessage("Category item is not found by id: " + id);
            return ResponseEntity.status(404).body(response);
        }

        if (product2Repository.existsByCategoryItemId(id))
        {
            logger.info("You are trying to delete a category-item. But inside of this category-item have product(s). Please delete first this product(s) or replace category-item of this product");
            throw new RuntimeException("You are trying to delete a category-item. But inside of this category-item have product(s). Please delete first this product(s) or replace category-item of this product");
        }

        List<Long> catalogIds = categoryItemRepository.getCatalogIds(id);

        Integer countByCatalogIds = product2Repository.findCountByCatalogIds(catalogIds);
        if (countByCatalogIds > 0)
        {
            logger.info("You are trying to delete a category-item. But inside of this category-item's CATALOG have {} product(s). Please delete first this product(s) or replace CATALOG of this product", countByCatalogIds);
            throw new RuntimeException("You are trying to delete a category-item. But inside of this category-item's CATALOG have product(s). Please delete first this product(s) or replace CATALOG of this product");
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

    public ResponseEntity<ApiResponse<List<CategoryItemDTO>>> getNameList()
    {
        ApiResponse<List<CategoryItemDTO>> response = new ApiResponse<>();
        List<CategoryItem> all = categoryItemRepository.findAll();
        response.setData(new ArrayList<>());
        all.forEach(i -> response.getData().add(new CategoryItemDTO(i)));
        response.setMessage("Found " + all.size() + " category name(s)");
        return ResponseEntity.status(200).body(response);
    }
}
