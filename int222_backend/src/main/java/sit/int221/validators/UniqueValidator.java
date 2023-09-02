package sit.int221.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import sit.int221.repositories.UserRepository;

public class UniqueValidator implements ConstraintValidator<ValidUnique, String> {

    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean isValid(String string, ConstraintValidatorContext constraintValidatorContext) {
        return userRepository.countByUsername(string) == 0 && userRepository.countByEmail(string) == 0 && userRepository.countByName(string) == 0;
    }
}