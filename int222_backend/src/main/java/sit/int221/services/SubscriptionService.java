package sit.int221.services;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import sit.int221.entities.Announcement;
import sit.int221.entities.Category;
import sit.int221.entities.Subscription;
import sit.int221.repositories.SubscriptionRepository;
import sit.int221.utils.AnnouncementDisplay;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final CategoryService categoryService;
    private final EmailService emailService;

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

    public void unsubscribe(String email, List<Integer> categoryIds){
        for (Integer categoryId : categoryIds) {
            Subscription subscription = subscriptionRepository.findByEmailAndCategoryId(email, categoryId);
            if (subscription != null) {
                subscriptionRepository.delete(subscription);
            }
        }
    }

    @Async
    public void sendOtpSubscribeResponseEmail(String email, List<Integer> categoryIds) {
        for (Integer categoryId : categoryIds) {
            Category category = categoryService.getCategory(categoryId);
            String categoryName = category.getCategoryName();
            String text = emailService.getOtpResponseTemplate(categoryName);
            emailService.sendEmail(email, "You successfully subscribed to " + categoryName, text);
        }
    }

    public CompletableFuture<Void> sendAnnouncementEmail(Integer categoryId, Announcement announcement) {
        List<Subscription> subscriptions = subscriptionRepository.findByCategoryId(categoryId);

        if (announcement.getAnnouncementDisplay().equals(AnnouncementDisplay.Y)) {
            if (announcement.getPublishDate() == null) {
                return CompletableFuture.runAsync(() -> {
                    subscriptions.forEach(subscription -> emailService.sendSubscriberEmail(subscription.getEmail(), announcement));
                });
            } else {
                long delay = Duration.between(ZonedDateTime.now(ZoneId.systemDefault()), announcement.getPublishDate()).toMillis();
                Executors.newSingleThreadScheduledExecutor().schedule(() -> {
                    subscriptions.forEach(subscription -> emailService.sendSubscriberEmail(subscription.getEmail(), announcement));
                }, delay, TimeUnit.MILLISECONDS);
                return CompletableFuture.completedFuture(null);
            }
        }

        return CompletableFuture.completedFuture(null);
    }
}
