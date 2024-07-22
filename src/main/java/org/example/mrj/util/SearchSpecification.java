package org.example.mrj.util;

import org.example.mrj.domain.entity.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class SearchSpecification {

    public Specification<EquipmentCategory> equipmentCategoryContains(String searchTerm) {
        String[] searchTerms = searchTerm.toLowerCase().split(" ");
        return (root, query, criteriaBuilder) -> {
            Specification<EquipmentCategory> spec = null;
            for (String term : searchTerms) {
                Specification<EquipmentCategory> tempSpec = (root1, query1, criteriaBuilder1) -> criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + term + "%")
                );
                if (spec == null) {
                    spec = tempSpec;
                } else {
                    spec = spec.or(tempSpec);
                }
            }
            return Objects.requireNonNull(spec).toPredicate(root, query, criteriaBuilder);
        };
    }

    public Specification<Catalog> catalogContains(String searchTerm) {
        String[] searchTerms = searchTerm.toLowerCase().split(" ");
        return (root, query, criteriaBuilder) -> {
            Specification<Catalog> spec = null;
            for (String term : searchTerms) {
                Specification<Catalog> tempSpec = (root1, query1, criteriaBuilder1) -> criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + term + "%")
                );
                if (spec == null) {
                    spec = tempSpec;
                } else {
                    spec = spec.or(tempSpec);
                }
            }
            return Objects.requireNonNull(spec).toPredicate(root, query, criteriaBuilder);
        };
    }

    public Specification<Product> productContains(String searchTerm) {
        String[] searchTerms = searchTerm.toLowerCase().split(" ");
        return (root, query, criteriaBuilder) -> {
            Specification<Product> spec = null;
            for (String term : searchTerms) {
                Specification<Product> tempSpec = (root1, query1, criteriaBuilder1) -> criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + term + "%"),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("mainDescription")), "%" + term + "%"),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), "%" + term + "%")
                );
                if (spec == null) {
                    spec = tempSpec;
                } else {
                    spec = spec.or(tempSpec);
                }
            }
            return Objects.requireNonNull(spec).toPredicate(root, query, criteriaBuilder);
        };
    }

    public Specification<New> newContains(String searchTerm) {
        String[] searchTerms = searchTerm.toLowerCase().split(" ");
        return (root, query, criteriaBuilder) -> {
            Specification<New> spec = null;
            for (String term : searchTerms) {
                Specification<New> tempSpec = (root1, query1, criteriaBuilder1) -> criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), "%" + term + "%"),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("body")), "%" + term + "%")
                );
                if (spec == null) {
                    spec = tempSpec;
                } else {
                    spec = spec.or(tempSpec);
                }
            }
            return Objects.requireNonNull(spec).toPredicate(root, query, criteriaBuilder);
        };
    }

    public Specification<Event> eventContains(String searchTerm) {
        String[] searchTerms = searchTerm.toLowerCase().split(" ");
        return (root, query, criteriaBuilder) -> {
            Specification<Event> spec = null;
            for (String term : searchTerms) {
                Specification<Event> tempSpec = (root1, query1, criteriaBuilder1) -> criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), "%" + term + "%"),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), "%" + term + "%")
                );
                if (spec == null) {
                    spec = tempSpec;
                } else {
                    spec = spec.or(tempSpec);
                }
            }
            return Objects.requireNonNull(spec).toPredicate(root, query, criteriaBuilder);
        };
    }

    public Specification<Partner> partnerContains(String searchTerm) {
        String[] searchTerms = searchTerm.toLowerCase().split(" ");
        return (root, query, criteriaBuilder) -> {
            Specification<Partner> spec = null;
            for (String term : searchTerms) {
                Specification<Partner> tempSpec = (root1, query1, criteriaBuilder1) -> criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), "%" + term + "%"),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("mainDescription")), "%" + term + "%"),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), "%" + term + "%")
                );
                if (spec == null) {
                    spec = tempSpec;
                } else {
                    spec = spec.or(tempSpec);
                }
            }
            return Objects.requireNonNull(spec).toPredicate(root, query, criteriaBuilder);
        };
    }
}
