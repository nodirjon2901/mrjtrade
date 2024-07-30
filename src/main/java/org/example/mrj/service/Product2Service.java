package org.example.mrj.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.example.mrj.controller.RequestLoggingFilter;
import org.example.mrj.domain.dto.ApiResponse;
import org.example.mrj.domain.dto.Product2DTO;
import org.example.mrj.domain.entity.*;
import org.example.mrj.exception.NotFoundException;
import org.example.mrj.repository.*;
import org.example.mrj.util.SlugUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor

@Service
public class Product2Service
{
    private final Product2Repository product2Repository;
    private final ObjectMapper objectMapper;
    private final PhotoService photoService;
    private final PartnerRepository partnerRepository;
    private final CatalogRepository catalogRepository;
    private final CategoryItemRepository categoryItemRepository;
    private final CharacteristicRepository characterRepo;

    private static final Logger logger = LoggerFactory.getLogger(RequestLoggingFilter.class);

    public ResponseEntity<ApiResponse<Product2>> add(String jsonData, List<MultipartFile> gallery)
    {
        ApiResponse<Product2> response = new ApiResponse<>();
        try
        {
            Product2 product2 = objectMapper.readValue(jsonData, Product2.class);
            if (product2.getCatalog() != null && product2.getCategoryItem() != null)
            {
                logger.info("Product belongs to either catalog or CategoryItem, not both !!!");
                throw new RuntimeException("Product belongs to either catalog or CategoryItem, not both !!!");
            }
//            if (mainPhoto != null) product2.setMainPhoto(photoService.save(mainPhoto));
            product2.setGallery(new ArrayList<>());
            gallery.forEach(i -> product2.getGallery().add(photoService.save(i)));

            if (product2.getPartner() != null)
            {
                Optional<Partner> partner = partnerRepository.findById(product2.getPartner().getId());
                if (partner.isEmpty())
                {
                    logger.info("Partner not found by id: {}", product2.getPartner().getId());
                    throw new NotFoundException("Partner not found by id: " + product2.getPartner().getId());
                }
            } else
            {
                logger.info("Partner not given");
                throw new RuntimeException("Partner not given");
            }

            if (product2.getCatalog() != null)
            {
                System.err.println("product2.getCatalog() = " + product2.getCatalog());
                Optional<Catalog> catalog = catalogRepository.findById(product2.getCatalog().getId());
                if (catalog.isEmpty())
                {
                    logger.info("Catalog not found by id: {}", product2.getCatalog().getId());
                    throw new NotFoundException("Catalog not found by id: " + product2.getCatalog().getId());
                }
            }

            if (product2.getCategoryItem() != null)
            {
                System.err.println("product2.getCategoryItem() = " + product2.getCategoryItem());
                Optional<CategoryItem> categoryItem = categoryItemRepository.findById(product2.getCategoryItem().getId());
                if (categoryItem.isEmpty())
                {
                    logger.info("CategoryItem not found by id: {}", product2.getCategoryItem().getId());
                    throw new NotFoundException("CategoryItem not found by id: " + product2.getCategoryItem().getId());
                }
            }

            Long id = product2Repository.save(new Product2()).getId();
            product2.setId(id);
            product2.setSlug(id + "-" + SlugUtil.makeSlug(product2.getName()));

            response.setData(product2Repository.save(product2));
            response.setMessage("Product added");
            return ResponseEntity.ok(response);
        } catch (JsonProcessingException e)
        {
            response.setMessage("Error parsing json: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    public ResponseEntity<ApiResponse<Product2>> get(String slug)
    {
        ApiResponse<Product2> response = new ApiResponse<>();
        Optional<Product2> bySlug = product2Repository.findBySlug(slug);
        if (bySlug.isEmpty())
            throw new NotFoundException("Product not found by slug: " + slug);
        response.setData(bySlug.get());
        response.setMessage("Found");
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<ApiResponse<Product2>> update(Product2 newProduct)
    {
        ApiResponse<Product2> response = new ApiResponse<>();
        Optional<Product2> byId = product2Repository.findById(newProduct.getId());
        if (byId.isEmpty())
            throw new NotFoundException("Product not found by id: " + newProduct.getId());

        Product2 fromDb = byId.get();

        if (newProduct.getName() != null)
        {
            fromDb.setName(newProduct.getName());
            fromDb.setSlug(fromDb.getId() + "-" + SlugUtil.makeSlug(newProduct.getName()));
        }

        if (newProduct.getTag() != null)
            fromDb.setTag(newProduct.getTag());

        if (newProduct.getDescription() != null)
            fromDb.setDescription(newProduct.getDescription());

        if (newProduct.getShortDescription() != null)
            fromDb.setShortDescription(newProduct.getShortDescription());

        if (newProduct.getDiscount() != null)
            fromDb.setDiscount(newProduct.getDiscount());

        if (newProduct.getOriginalPrice() != null)
            fromDb.setOriginalPrice(newProduct.getOriginalPrice());

        if (newProduct.getConditions() != null)
            fromDb.setConditions(newProduct.getConditions());

        if (newProduct.getPartner() != null && newProduct.getPartner().getId() != null)
        {
            if (!partnerRepository.existsById(newProduct.getPartner().getId()))
                throw new NotFoundException("Partner not found by id: " + newProduct.getPartner().getId());

            fromDb.setPartner(partnerRepository.findById(newProduct.getPartner().getId()).get());
        }

        if (newProduct.getCatalog() != null && newProduct.getCatalog().getId() != null)
        {
            if (!catalogRepository.existsById(newProduct.getCatalog().getId()))
                throw new NotFoundException("Catalog not found by id: " + newProduct.getCatalog().getId());

            fromDb.setCatalog(catalogRepository.findById(newProduct.getCatalog().getId()).get());
        }

        List<Characteristic> newList = newProduct.getCharacteristics();
        //check Characteristic
        if (!newList.isEmpty())
        {
            for (Characteristic newItem : newList)
            {
                if (newItem.getId() != null)
                {
                    for (Characteristic dbItem : fromDb.getCharacteristics())
                    {
                        if (dbItem.getId().equals(newItem.getId()))
                        {
                            if (newItem.getDescription() != null) dbItem.setDescription(newItem.getDescription());
                            if (newItem.getParameterName() != null) dbItem.setParameterName(newItem.getParameterName());
                        }
                    }
                    //If id given but , both of Description and ParameterName equals null , accordingly delete this Characteristic
                    if (newItem.getDescription() != null && newItem.getParameterName() != null)
                        characterRepo.deleteById(newItem.getId());

                } else //If id not given accordingly this is new and add...
                {
                    fromDb.getCharacteristics().add(newItem);
                }
            }
        }

        response.setData(product2Repository.save(fromDb));
        response.setMessage("Updated");
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<ApiResponse<?>> delete(Long id)
    {
        ApiResponse<?> response = new ApiResponse<>();
        if (!product2Repository.existsById(id))
            throw new NotFoundException("Product not found by id: " + id);

        product2Repository.deleteById(id);
        response.setMessage("Deleted");
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<ApiResponse<List<Product2DTO>>> getAll(Long categoryItemId, Long catalogId, String tag)
    {
        ApiResponse<List<Product2DTO>> response = new ApiResponse<>();
        List<Product2> products = new ArrayList<>();

        if (categoryItemId != null && catalogId != null)
        {
            throw new RuntimeException("Filter only for either of category-id or catalog-id, not both!!!");
        }

        if (categoryItemId == null && catalogId == null)
        {
            if (tag == null)
                products = product2Repository.findAll();
            else
                products = product2Repository.findByTagContaining(tag);
        } else if (catalogId != null)
        {
            if (tag == null)
                products = product2Repository.findByCatalogId(catalogId);
            else
                products = product2Repository.findByCatalogId(catalogId)
                        .stream()
                        .filter(product -> product.getTag().stream()
                                .anyMatch(t -> t.equalsIgnoreCase(tag)))
                        .collect(Collectors.toList());
        } else
        {
            List<Long> catalogIds = categoryItemRepository.getCatalogIds(categoryItemId);

            if (!catalogIds.isEmpty())
            {
                if (tag == null)
                    products = product2Repository.findByCatalogIds(catalogIds);
                else
                    products = product2Repository.findByCatalogIds(catalogIds)
                            .stream()
                            .filter(product -> product.getTag().stream()
                                    .anyMatch(t -> t.equalsIgnoreCase(tag)))
                            .collect(Collectors.toList());
            } else
            {
                if (tag == null)
                    products = product2Repository.findByCategoryItemId(categoryItemId);
                else
                    products = product2Repository.findByCategoryItemId(categoryItemId)
                            .stream()
                            .filter(product -> product.getTag().stream()
                                    .anyMatch(t -> t.equalsIgnoreCase(tag)))
                            .collect(Collectors.toList());
            }

        }
        response.setData(new ArrayList<>());
        products.forEach(i -> response.getData().add(new Product2DTO(i)));
        response.setMessage("Found " + products.size() + " product(s)");

        return ResponseEntity.ok(response);
    }

}
