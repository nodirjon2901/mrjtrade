package org.example.mrj.util;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.example.mrj.domain.entity.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class SearchSpecification {

    public Specification<CategoryItem> equipmentCategoryContains(String searchTerm) {
        String[] searchTerms = searchTerm.toLowerCase().split(" ");
        return (root, query, criteriaBuilder) -> {
            Specification<CategoryItem> spec = null;
            for (String term : searchTerms) {
                Specification<CategoryItem> tempSpec = (root1, query1, criteriaBuilder1) -> criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), "%" + term + "%")
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

    public Specification<Product2> productContains(String searchTerm) {
        String[] searchTerms = searchTerm.toLowerCase().split(" ");
        return (root, query, criteriaBuilder) -> {
            Specification<Product2> spec = null;
            for (String term : searchTerms) {
                Specification<Product2> tempSpec = (root1, query1, criteriaBuilder1) -> criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + term + "%"),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("shortDescription")), "%" + term + "%"),
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
            // Join with NewHeadOption and NewOption entities
            Join<New, NewHeadOption> headJoin = root.join("head", JoinType.LEFT);
            Join<New, NewOption> optionJoin = root.join("newOptions", JoinType.LEFT);

            Specification<New> spec = null;

            for (String term : searchTerms) {
                Specification<New> tempSpec = (root1, query1, criteriaBuilder1) -> criteriaBuilder.or(
                        // Search in NewHeadOption title and body
                        criteriaBuilder.like(criteriaBuilder.lower(headJoin.get("title")), "%" + term + "%"),
                        criteriaBuilder.like(criteriaBuilder.lower(headJoin.get("body")), "%" + term + "%"),
                        // Search in NewOption heading and text
                        criteriaBuilder.like(criteriaBuilder.lower(optionJoin.get("heading")), "%" + term + "%"),
                        criteriaBuilder.like(criteriaBuilder.lower(optionJoin.get("text")), "%" + term + "%")
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
