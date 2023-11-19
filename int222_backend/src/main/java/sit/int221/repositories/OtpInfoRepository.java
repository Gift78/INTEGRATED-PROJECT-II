package sit.int221.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sit.int221.entities.OtpInfo;

public interface OtpInfoRepository extends JpaRepository<OtpInfo, Integer> {
    OtpInfo findByEmail(String email);
    OtpInfo findByEmailAndOtp(String email, String otp);
}
