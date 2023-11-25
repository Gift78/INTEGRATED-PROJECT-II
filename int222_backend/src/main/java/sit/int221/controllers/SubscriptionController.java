package sit.int221.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sit.int221.dtos.OtpRequestDTO;
import sit.int221.dtos.OtpVerificationRequest;
import sit.int221.exceptions.OtpRetryLimitExceededException;
import sit.int221.services.OtpService;
import sit.int221.services.SubscriptionService;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/subscription")
@CrossOrigin(origins = {"http://localhost:5173", "https://intproj22.sit.kmutt.ac.th", "http://127.0.0.1:5173"})
@RequiredArgsConstructor
public class SubscriptionController {
    private final OtpService otpService;
    private final SubscriptionService subscriptionService;

    @PostMapping("/generate-otp")
    public ResponseEntity<?> generateOtp(@RequestBody OtpRequestDTO otpRequest) {
        try {
            String otp = otpService.generateOtp(otpRequest.getEmail());
            CompletableFuture<Boolean> emailSendingFuture = otpService.sendOtp(otpRequest.getEmail(), otp);
            boolean isEmailSent = emailSendingFuture.get();

            if (isEmailSent) {
                return ResponseEntity.ok(Map.of("message", "OTP sent successfully"));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Failed to send OTP"));
            }
        } catch (InterruptedException | ExecutionException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Failed to send OTP"));
        } catch (OtpRetryLimitExceededException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "OTP generation limit exceeded. Please try again after some time."));
        }
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody OtpVerificationRequest otpRequest) {
        boolean isOtpValid = otpService.verifyOtp(otpRequest.getEmail(), otpRequest.getOtp());

        if (isOtpValid) {
            subscriptionService.subscribe(otpRequest.getEmail(), otpRequest.getCategoryIds());

            CompletableFuture.runAsync(() ->
                    subscriptionService.sendOtpSubscribeResponseEmail(otpRequest.getEmail(), otpRequest.getCategoryIds()));
            return ResponseEntity.ok(Map.of("message", "OTP verified successfully"));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Invalid OTP"));
        }
    }

    @PostMapping("/unsubscribe")
    public ResponseEntity<?> unsubscribe(@RequestBody Map<String, Object> request) {
        String token = (String) request.get("token");
        Integer categoryId = (Integer) request.get("categoryId");
        String email = subscriptionService.verifyUnsubscribeToken(token);
        if (email != null) {
            subscriptionService.unsubscribe(email, categoryId);
            return ResponseEntity.ok(Map.of("message", "Unsubscribed successfully"));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Invalid or expired token"));
        }
    }

    @PostMapping("/unsubscribe/generate-token")
    public ResponseEntity<?> generateUnsubscribeToken(@RequestParam String email) {
        String token = subscriptionService.generateUnsubscribeToken(email);
        return ResponseEntity.ok(Map.of("token", token));
    }
}
