package sit.int221.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sit.int221.dtos.OtpRequestDTO;
import sit.int221.dtos.OtpVerificationRequest;
import sit.int221.services.OtpService;
import sit.int221.services.SubscriptionService;

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
        String otp = otpService.generateOtp(otpRequest.getEmail());
        CompletableFuture<Boolean> emailSendingFuture = otpService.sendOtp(otpRequest.getEmail(), otp);

        try {
            boolean isEmailSent = emailSendingFuture.get();

            if (isEmailSent) {
                return ResponseEntity.ok("OTP sent successfully");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send OTP");
            }
        } catch (InterruptedException | ExecutionException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send OTP");
        }
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody OtpVerificationRequest otpRequest) {
        boolean isOtpValid = otpService.verifyOtp(otpRequest.getEmail(), otpRequest.getOtp());

        if (isOtpValid) {
            if (otpRequest.getAction().equals("subscribe")) {
                subscriptionService.subscribe(otpRequest.getEmail(), otpRequest.getCategoryIds());

                CompletableFuture.runAsync(() ->
                        subscriptionService.sendOtpSubscribeResponseEmail(otpRequest.getEmail(), otpRequest.getCategoryIds()));
            } else {
                subscriptionService.unsubscribe(otpRequest.getEmail(), otpRequest.getCategoryIds());
            }
            return ResponseEntity.ok("OTP verified successfully");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid OTP");
        }
    }
}
