package sit.int221.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import sit.int221.entities.Announcement;
import sit.int221.entities.Category;
import sit.int221.entities.Subscription;
import sit.int221.entities.UnsubscribeToken;
import sit.int221.repositories.SubscriptionRepository;
import sit.int221.repositories.UnsubscribeTokenRepository;
import sit.int221.utils.AnnouncementDisplay;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final CategoryService categoryService;
    private final EmailService emailService;
    private final UnsubscribeTokenRepository unsubscribeTokenRepository;

    public void subscribe(String email, List<Integer> categoryIds) {
        for (Integer categoryId : categoryIds) {
            if (subscriptionRepository.existsByEmailAndCategoryId(email, categoryId)) {
                continue;
            }

            Category category = categoryService.getCategory(categoryId);
            Subscription subscription = new Subscription();
            subscription.setEmail(email);
            subscription.setCategory(category);
            subscriptionRepository.saveAndFlush(subscription);
        }
    }

    @Transactional
    public void unsubscribe(String token, Integer categoryId) {
        String email = verifyUnsubscribeToken(token);
        if (email != null) {
            Subscription subscription = subscriptionRepository.findByEmailAndCategoryId(email, categoryId);
            if (subscription != null) {
                subscriptionRepository.delete(subscription);
            }
        }
    }

    public void sendOtpSubscribeResponseEmail(String email, List<Integer> categoryIds) {
        for (Integer categoryId : categoryIds) {
            Category category = categoryService.getCategory(categoryId);
            String categoryName = category.getCategoryName();
            String text = emailService.getOtpResponseTemplate(categoryName);
            emailService.sendEmail(email, "You successfully subscribed to " + categoryName, text);
        }
    }

    @Async
    public void sendAnnouncementEmail(Integer categoryId, Announcement announcement) {
        List<Subscription> subscriptions = subscriptionRepository.findByCategoryId(categoryId);

        if (announcement.getAnnouncementDisplay().equals(AnnouncementDisplay.Y)) {
            if (announcement.getPublishDate() == null) {
                subscriptions.forEach(subscription -> emailService.sendSubscriberEmail(subscription.getEmail(), announcement));
            } else {
                long delay = Duration.between(ZonedDateTime.now(ZoneId.systemDefault()), announcement.getPublishDate()).toMillis();
                Executors.newSingleThreadScheduledExecutor().schedule(() -> {
                    subscriptions.forEach(subscription -> emailService.sendSubscriberEmail(subscription.getEmail(), announcement));
                }, delay, TimeUnit.MILLISECONDS);
            }
        }
    }

    @Transactional
    public String verifyUnsubscribeToken(String token) {
        UnsubscribeToken unsubscribeToken = unsubscribeTokenRepository.findByToken(token);
        if (unsubscribeToken != null) {
            unsubscribeTokenRepository.deleteByToken(token);
            return unsubscribeToken.getEmail();
        } else {
            return null;
        }
    }
}
