package com.example.dm_test.service;

import com.example.dm_test.entity.User;
import com.example.dm_test.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
public class LoginService {
    @Autowired
    private UserMapper userMapper;

    public String checkLogin(String username, String password_org)
    {
        User user = userMapper.selectOne(username);
        if (user != null)
        {
            String password = user.getPassword();
            if (password_org.equals(password))
            {
                return "Success";
            }
            else {
                return "Password Incorrect";
            }
        }else {
            return "No User Found!";
        }
    }

    public String getMD5Hash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());

            StringBuilder sb = new StringBuilder();
            for (byte b : messageDigest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
