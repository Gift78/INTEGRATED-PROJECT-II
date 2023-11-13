package sit.int221.services;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import sit.int221.entities.OtpInfo;
import sit.int221.exceptions.InvalidOTPException;
import sit.int221.exceptions.OtpRetryLimitExceededException;
import sit.int221.repositories.OtpInfoRepository;

@Service
@RequiredArgsConstructor
public class OtpService {
    private static final int MAX_OTP_ATTEMPTS = 3;
    private static final int MAX_OTP_ATTEMPTS_WINDOW_MINUTES = 15;
    private static final int OTP_EXPIRY_MINUTES = 5;

    private final OtpInfoRepository otpInfoRepository;
    private final EmailService emailService;
    private final CacheManager cacheManager;

    public String generateOtp(String email) {
        OtpInfo existingOtpInfo = otpInfoRepository.findByEmail(email);

        if (existingOtpInfo != null) {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime lastRequestTime = existingOtpInfo.getGeneratedAt();

            if (getOtpAttempts(email) >= MAX_OTP_ATTEMPTS) {
                if (lastRequestTime.isAfter(now.minusMinutes(MAX_OTP_ATTEMPTS_WINDOW_MINUTES))) {
                    throw new OtpRetryLimitExceededException(
                            "OTP generation limit exceeded. Please try again after some time.");

                } else {
                    // MAX_OTP_ATTEMPTS exceeds but MAX_OTP_ATTEMPTS_WINDOW_MINUTES TIme Over
                    // so reset count and user can get new OTP
                    resetOtpAttempts(email);
                }
            }
        }

        String otp = null;

        if (existingOtpInfo != null) {
            if (isOtpExpired(existingOtpInfo.getGeneratedAt())) {
                otpInfoRepository.delete(existingOtpInfo);
                otp = generateNewOTP(email);
            } else {
                // OTP is valid , return same OTP but reset time
                existingOtpInfo.setGeneratedAt(LocalDateTime.now());
                otp = existingOtpInfo.getOtp();
            }
        } else {
            otp = generateNewOTP(email);
        }

        incrementOtpAttempts(email);

        return otp;
    }

    public CompletableFuture<Boolean> sendOtp(String email, String otp) {
        String subject = "OTP Verification";
        String text = emailService.getOtpTemplate(otp);
        CompletableFuture<Void> emailSendingFuture = emailService.sendEmail(email, subject, text);

        return emailSendingFuture.thenApplyAsync(result -> true)
                .exceptionally(ex -> {
                    ex.printStackTrace();
                    return false;
                });
    }

    private int getOtpAttempts(String email) {
        Cache cache = cacheManager.getCache("otpAttempts");
        Integer attempts = cache.get(email, Integer.class);
        return attempts != null ? attempts : 0;
    }

    private void incrementOtpAttempts(String email) {
        Cache cache = cacheManager.getCache("otpAttempts");
        Integer attempts = cache.get(email, Integer.class);
        if (attempts == null) {
            attempts = 1;
        } else {
            attempts++;
        }
        cache.put(email, attempts);
    }

    private void resetOtpAttempts(String email) {
        Cache cache = cacheManager.getCache("otpAttempts");
        cache.evict(email);
    }

    private String generateNewOTP(String email) {
        Random random = new Random();
        int otpValue = 100_000 + random.nextInt(900_000);
        String otp = String.valueOf(otpValue);

        OtpInfo otpInfo = new OtpInfo();
        otpInfo.setEmail(email);
        otpInfo.setOtp(otp);
        otpInfo.setGeneratedAt(LocalDateTime.now());
        otpInfoRepository.save(otpInfo);
        return otp;
    }

    private boolean isOtpExpired(LocalDateTime otpGeneratedAt) {
        LocalDateTime now = LocalDateTime.now();
        return otpGeneratedAt.isBefore(now.minusMinutes(OTP_EXPIRY_MINUTES));
    }

    public boolean verifyOtp(String email, String otp) {
        OtpInfo otpInfo = otpInfoRepository.findByEmailAndOtp(email, otp);

        if (otpInfo != null) {
            if (isOtpExpired(otpInfo.getGeneratedAt())) {
                otpInfoRepository.delete(otpInfo);
                return false;
            } else {
                otpInfoRepository.delete(otpInfo);
                return true;
            }
        } else {
            throw new InvalidOTPException("Invalid OTP");
        }
    }
}
