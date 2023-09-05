package sit.int221.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sit.int221.dtos.UpdateUserDTO;
import sit.int221.entities.User;
import sit.int221.repositories.UserRepository;

@Component
public class OnUpdateUniqueValidator implements ConstraintValidator<ValidUniqueOnUpdate, UpdateUserDTO> {
    @Autowired
    private UserRepository userRepository;
    @Override
    public void initialize(ValidUniqueOnUpdate constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(UpdateUserDTO User, ConstraintValidatorContext constraintValidatorContext) {
        boolean isValid = true;
        boolean isUsernameAlreadyExist = userRepository.countByUsername(User.getUsername()) > 0;
        boolean isEmailAlreadyExist = userRepository.countByEmail(User.getEmail()) > 0;
        boolean isNameAlreadyExist = userRepository.countByName(User.getName()) > 0;

        if (!isUsernameAlreadyExist && !isEmailAlreadyExist && !isNameAlreadyExist) {
            return true;
        }

        if (isUsernameAlreadyExist) {
            User user = userRepository.findByUsername(User.getUsername());

            int isEmailAlreadyExistByOtherUser = userRepository.countByEmailAndIdNot(User.getEmail(), user.getId());
            int isNameAlreadyExistByOtherUser = userRepository.countByNameAndIdNot(User.getName(), user.getId());

            if (isEmailAlreadyExistByOtherUser > 0) {
                constraintValidatorContext.disableDefaultConstraintViolation();
                constraintValidatorContext.buildConstraintViolationWithTemplate(constraintValidatorContext.getDefaultConstraintMessageTemplate())
                        .addPropertyNode("email")
                        .addConstraintViolation();
                isValid = false;
            }

            if (isNameAlreadyExistByOtherUser > 0) {
                constraintValidatorContext.disableDefaultConstraintViolation();
                constraintValidatorContext.buildConstraintViolationWithTemplate(constraintValidatorContext.getDefaultConstraintMessageTemplate())
                        .addPropertyNode("name")
                        .addConstraintViolation();
                isValid = false;
            }
        }

        if (isEmailAlreadyExist) {
            User user = userRepository.findByEmail(User.getEmail());

            int isUsernameAlreadyExistByOtherUser = userRepository.countByUsernameAndIdNot(User.getUsername(), user.getId());
            int isNameAlreadyExistByOtherUser = userRepository.countByNameAndIdNot(User.getName(), user.getId());

            if (isUsernameAlreadyExistByOtherUser > 0) {
                constraintValidatorContext.disableDefaultConstraintViolation();
                constraintValidatorContext.buildConstraintViolationWithTemplate(constraintValidatorContext.getDefaultConstraintMessageTemplate())
                        .addPropertyNode("username")
                        .addConstraintViolation();
                isValid = false;
            }

            if (isNameAlreadyExistByOtherUser > 0) {
                constraintValidatorContext.disableDefaultConstraintViolation();
                constraintValidatorContext.buildConstraintViolationWithTemplate(constraintValidatorContext.getDefaultConstraintMessageTemplate())
                        .addPropertyNode("name")
                        .addConstraintViolation();
                isValid = false;
            }
        }

        if (isNameAlreadyExist) {
            User user = userRepository.findByName(User.getName());

            int isUsernameAlreadyExistByOtherUser = userRepository.countByUsernameAndIdNot(User.getUsername(), user.getId());
            int isEmailAlreadyExistByOtherUser = userRepository.countByEmailAndIdNot(User.getEmail(), user.getId());

            if (isUsernameAlreadyExistByOtherUser > 0) {
                constraintValidatorContext.disableDefaultConstraintViolation();
                constraintValidatorContext.buildConstraintViolationWithTemplate(constraintValidatorContext.getDefaultConstraintMessageTemplate())
                        .addPropertyNode("username")
                        .addConstraintViolation();
                isValid = false;
            }

            if (isEmailAlreadyExistByOtherUser > 0) {
                constraintValidatorContext.disableDefaultConstraintViolation();
                constraintValidatorContext.buildConstraintViolationWithTemplate(constraintValidatorContext.getDefaultConstraintMessageTemplate())
                        .addPropertyNode("email")
                        .addConstraintViolation();
                isValid = false;
            }
        }

        return isValid;
    }
}
