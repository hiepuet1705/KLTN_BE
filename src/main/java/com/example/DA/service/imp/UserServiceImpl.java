package com.example.DA.service.imp;


import com.example.DA.dto.UserDTOResponse;
import com.example.DA.exception.VerificationException;
import com.example.DA.model.OTP;
import com.example.DA.model.User;
import com.example.DA.repo.OTPRepository;
import com.example.DA.repo.UserRepository;
import com.example.DA.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    OTPRepository otpRepository;

    private final Random random = new Random();

    private String generateVerificationCode() {
        return String.format("%06d", random.nextInt(1000000)); // Generates a 6-digit code
    }

    public void sendVerificationEmail(String email) {
        // Tạo mã xác thực ngẫu nhiên (6 chữ số)
        String verificationCode = generateVerificationCode();

        // Tính thời gian hết hạn (ví dụ: 5 phút kể từ thời điểm hiện tại)
        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(5);

        // Xóa mã cũ (nếu có) trước khi tạo mã mới
        otpRepository.deleteByEmail(email);

        // Lưu mã OTP mới vào cơ sở dữ liệu
        OTP otp = new OTP();
        otp.setEmail(email);
        otp.setCode(verificationCode);
        otp.setExpiryTime(expiryTime);
        otpRepository.save(otp);

        // Tạo và gửi email xác thực
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Verification Code");
        message.setText("Your verification code is: " + verificationCode);

        try {
            mailSender.send(message);  // Gửi email
            System.out.println("Verification email sent to: " + email);
        } catch (Exception e) {
            System.err.println("Failed to send email: " + e.getMessage());
            throw new RuntimeException("Error sending verification email", e);
        }
    }

    @Override
    public UserDTOResponse getUserById(Integer id) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new RuntimeException("User not found with id: " + id));

        UserDTOResponse userDTOResponse = new UserDTOResponse();
        userDTOResponse.setPhone(user.getPhone());
        userDTOResponse.setId(user.getId());
        userDTOResponse.setName(user.getName());
        userDTOResponse.setUsername(user.getUsername());
        userDTOResponse.setEmail(user.getEmail());

        return userDTOResponse;
    }

    // Verify user with code
    @Transactional
    public boolean verifyUser(String email, String code) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new VerificationException("User not found.");
        }

        Optional<OTP> otpRecord = otpRepository.findByEmailAndCode(email, code);
        if (otpRecord.isEmpty()) {
            throw new VerificationException("Invalid OTP code.");
        }

        OTP otp = otpRecord.get();
        if (otp.getExpiryTime().isBefore(LocalDateTime.now())) {
            otpRepository.delete(otp); // Xóa mã OTP hết hạn
            throw new VerificationException("OTP code has expired.");
        }

        user.setIsVerified(1); // Cập nhật trạng thái xác thực
        userRepository.save(user);
        otpRepository.delete(otp); // Xóa mã OTP sau khi sử dụng

        return true;
    }

    @Override
    public UserDTOResponse findUserByPhone(String phone) {
        User user = userRepository.findUserByPhone(phone);
        UserDTOResponse userDTOResponse = new UserDTOResponse();
        userDTOResponse.setPhone(user.getPhone());
        userDTOResponse.setId(user.getId());
        userDTOResponse.setName(user.getName());
        userDTOResponse.setUsername(user.getUsername());
        userDTOResponse.setEmail(user.getEmail());
        return userDTOResponse;
    }

    public List<UserDTOResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(user -> {
                    UserDTOResponse userDTOResponse = new UserDTOResponse();
                    userDTOResponse.setPhone(user.getPhone());
                    userDTOResponse.setId(user.getId());
                    userDTOResponse.setName(user.getName());
                    userDTOResponse.setUsername(user.getUsername());
                    userDTOResponse.setEmail(user.getEmail());
                    return userDTOResponse;
                }).collect(Collectors.toList());
    }
}
