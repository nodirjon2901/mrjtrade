package org.example.mrj.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.example.mrj.controller.RequestLoggingFilter;
import org.example.mrj.domain.dto.ApiResponse;
import org.example.mrj.domain.dto.ProductDTO;
import org.example.mrj.domain.entity.CategoryItem;
import org.example.mrj.domain.entity.Characteristic;
import org.example.mrj.domain.entity.Product;
import org.example.mrj.exception.CategoryException;
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
public class ProductService
{
    private final ProductRepository productRepository;
    private final ObjectMapper objectMapper;
    private final PhotoService photoService;
    private final PartnerRepository partnerRepository;
    private final CatalogRepository catalogRepository;
    private final CategoryItemRepository categoryItemRepository;
    private final CharacteristicRepository characterRepo;

    private static final Logger logger = LoggerFactory.getLogger(RequestLoggingFilter.class);

    public ResponseEntity<ApiResponse<Product>> add(String jsonData, List<MultipartFile> gallery)
    {
        ApiResponse<Product> response = new ApiResponse<>();
        try
        {
            Product product = objectMapper.readValue(jsonData, Product.class);
            if (product.getCatalog() != null && product.getCategoryItem() != null)
            {
                logger.info("Product belongs to either catalog or CategoryItem, not both !!!");
                throw new CategoryException("Product belongs to either catalog or CategoryItem, not both !!!");
            }
//            if (mainPhoto != null) product.setMainPhoto(photoService.save(mainPhoto));

            if (product.getPartner() != null)
            {
                if (!partnerRepository.existsById(product.getPartner().getId()))
                {
                    logger.info("Partner not found by id: {}", product.getPartner().getId());
                    throw new NotFoundException("Partner not found by id: " + product.getPartner().getId());
                }
            } else
            {
                logger.info("Partner not given");
                throw new NotFoundException("Partner not given");
            }

            if (product.getCatalog() != null)
            {
                if (!catalogRepository.existsById(product.getCatalog().getId()))
                {
                    logger.info("Catalog not found by id: {}", product.getCatalog().getId());
                    throw new NotFoundException("Catalog not found by id: " + product.getCatalog().getId());
                }
            }

            if (product.getCategoryItem() != null)
            {
//                System.err.println("product2.getCategoryItem() = " + product.getCategoryItem());

                if (!categoryItemRepository.existsById(product.getCategoryItem().getId()))
                {
                    logger.info("CategoryItem not found by id: {}", product.getCategoryItem().getId());
                    throw new NotFoundException("CategoryItem not found by id: " + product.getCategoryItem().getId());
                }

                List<Long> catalogIds = categoryItemRepository.getCatalogIds(product.getCategoryItem().getId());
                if (!catalogIds.isEmpty())
                {
                    logger.info("You send CategoryItem , But this category have {} catalog(s) ,If category-item have catalog(s). You can set only catalog for product", catalogIds.size());
                    throw new CategoryException(String.format("You send CategoryItem , But this category have %s catalog(s) ,If category-item have catalog(s). You can set only catalog for product", catalogIds.size()));
                }

            }

            product.setGallery(new ArrayList<>());
            gallery.forEach(i -> product.getGallery().add(photoService.save(i)));

            Long id = productRepository.save(new Product()).getId();
            product.setId(id);
            product.setSlug(id + "-" + SlugUtil.makeSlug(product.getName()));
            product.getCharacteristics().forEach(i -> i.setProduct(product));

            response.setData(productRepository.save(product));
            response.setMessage("Product added");
            return ResponseEntity.ok(response);
        } catch (JsonProcessingException e)
        {
            response.setMessage("Error parsing json: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    public ResponseEntity<ApiResponse<?>> get(String slug, boolean similar)
    {
        Optional<Product> bySlug = productRepository.findBySlug(slug);
        if (bySlug.isEmpty())
            throw new NotFoundException("Product not found by slug: " + slug);
        Product product = bySlug.get();

        if (similar)
        {
            ApiResponse<List<ProductDTO>> response = new ApiResponse<>();

            if (product.getCatalog() != null && product.getCatalog().getId() != null)
            {
                CategoryItem categoryItem = product.getCatalog().getCategoryItem();
                List<Product> productList = productRepository.findByCategoryItemId(categoryItem.getId());

                response.setData(new ArrayList<>());
                productList.removeIf(i -> i.getId().equals(product.getId()));
                productList.forEach(i -> response.getData().add(new ProductDTO(i)));
                response.setMessage("Similar product");
                return ResponseEntity.ok(response);
            }


            response.setData(new ArrayList<>());
        }

        ApiResponse<Product> response = new ApiResponse<>();
        response.setData(product);
        response.setMessage("Found");
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<ApiResponse<Product>> update(Product newProduct)
    {
        ApiResponse<Product> response = new ApiResponse<>();
        StringBuilder message = new StringBuilder();
        Optional<Product> byId = productRepository.findById(newProduct.getId());
        if (byId.isEmpty())
            throw new NotFoundException("Product not found by id: " + newProduct.getId());

        Product fromDb = byId.get();

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

        if (newProduct.getProfessional() != null)
            fromDb.setProfessional(newProduct.getProfessional());

        if (newProduct.getPartner() != null && newProduct.getPartner().getId() != null)
        {
            if (!partnerRepository.existsById(newProduct.getPartner().getId()))
                throw new NotFoundException("Partner not found by id: " + newProduct.getPartner().getId());

            fromDb.setPartner(partnerRepository.findById(newProduct.getPartner().getId()).get());
        }

        if (newProduct.getCatalog() != null && newProduct.getCategoryItem() != null)
        {
            logger.info("Product belong only catalog-id or CategoryItem-id , Please send either of catalog or categoryItem");
            throw new CategoryException("Product belong only catalog-id or CategoryItem-id , Please send either of catalog or categoryItem");
        }

        if (newProduct.getCatalog() != null && newProduct.getCatalog().getId() != null)
        {
            if (!catalogRepository.existsById(newProduct.getCatalog().getId()))
                throw new NotFoundException("Catalog not found by id: " + newProduct.getCatalog().getId());

            fromDb.setCategoryItem(null);
            fromDb.setCatalog(catalogRepository.findById(newProduct.getCatalog().getId()).get());
        }

        if (newProduct.getCategoryItem() != null && newProduct.getCategoryItem().getId() != null)
        {
            if (!categoryItemRepository.existsById(newProduct.getCategoryItem().getId()))
                throw new NotFoundException("category-item not found by id: " + newProduct.getCategoryItem().getId());

            List<Long> catalogIds = categoryItemRepository.getCatalogIds(newProduct.getCategoryItem().getId());
            if (!catalogIds.isEmpty())
            {
                logger.info("You send CategoryItem , But this category have {} catalog(s) ,If category-item have catalog(s). You can set only catalog for product", catalogIds.size());
                throw new CategoryException(String.format("You send CategoryItem , But this category have %s catalog(s) ,If category-item have catalog(s). You can set only catalog for product", catalogIds.size()));
            }

            fromDb.setCatalog(null);
            fromDb.setCategoryItem(categoryItemRepository.findById(newProduct.getCategoryItem().getId()).get());
        }

        List<Characteristic> newList = newProduct.getCharacteristics();
        //check Characteristic
        if (newList != null && !newList.isEmpty())
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
                            if (newItem.getParameterName() != null)
                                dbItem.setParameterName(newItem.getParameterName());
                        }
                    }
                    //If id given but , both of Description and ParameterName equals null , accordingly delete this Characteristic
                    if (newItem.getDescription() != null && newItem.getParameterName() != null)
                        characterRepo.deleteById(newItem.getId());

                } else //If id not given accordingly this is new and add...
                {
                    newItem.setProduct(fromDb);
                    fromDb.getCharacteristics().add(newItem);
                }
            }
        }


        response.setData(productRepository.save(fromDb));
        response.setMessage("Updated");
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<ApiResponse<?>> delete(Long id)
    {
        ApiResponse<?> response = new ApiResponse<>();
        if (!productRepository.existsById(id))
            throw new NotFoundException("Product not found by id: " + id);

        productRepository.deleteById(id);
        response.setMessage("Deleted");
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<ApiResponse<List<ProductDTO>>> getAll(Long categoryItemId, Long catalogId, String tag, Boolean professional)
    {
        ApiResponse<List<ProductDTO>> response = new ApiResponse<>();
        List<Product> products = new ArrayList<>();

        if (categoryItemId != null && catalogId != null)
        {
            throw new CategoryException("Filter only for either of category-id or catalog-id, not both!!!");
        }

        if (categoryItemId == null && catalogId == null)
        {
            if (tag == null && professional == null)
                products = productRepository.findAll();
            else if (professional == null)
                products = productRepository.findByTagContaining(tag);
            else if (tag == null)
                products = productRepository.findByProfessional(professional);
            else
                products = productRepository.findByTagContainingAndProfessional(tag, professional);

        } else if (catalogId != null)
        {
            if (tag == null)
                products = productRepository.findByCatalogId(catalogId);
            else
                products = productRepository.findByCatalogId(catalogId)
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
                    products = productRepository.findByCatalogIds(catalogIds);
                else
                    products = productRepository.findByCatalogIds(catalogIds)
                            .stream()
                            .filter(product -> product.getTag().stream()
                                    .anyMatch(t -> t.equalsIgnoreCase(tag)))
                            .collect(Collectors.toList());
            } else
            {
                if (tag == null)
                    products = productRepository.findByCategoryItemId(categoryItemId);
                else
                    products = productRepository.findByCategoryItemId(categoryItemId)
                            .stream()
                            .filter(product -> product.getTag().stream()
                                    .anyMatch(t -> t.equalsIgnoreCase(tag)))
                            .collect(Collectors.toList());
            }

        }
        response.setData(new ArrayList<>());
        products.forEach(i -> response.getData().add(new ProductDTO(i)));
        response.setMessage("Found " + products.size() + " product(s)");

        return ResponseEntity.ok(response);
    }

}
