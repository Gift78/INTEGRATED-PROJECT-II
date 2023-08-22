package sit.int221.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sit.int221.repositories.CategoryRepository;

@Component
public class CategoryIdValidator implements ConstraintValidator<ValidCategoryId, Integer> {
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public boolean isValid(Integer categoryId, ConstraintValidatorContext constraintValidatorContext) {
        if (categoryId == null) {
            return true;
        }

        return categoryRepository.findById(categoryId).isPresent();
    }
}
