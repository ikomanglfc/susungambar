package com.triyakom.susun.service;

import com.triyakom.susun.model.SusunGambarUser;
import com.triyakom.susun.repo.UserRepo;
import com.triyakom.susun.shorter.Encrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Service
public class UserService {
    public static final String AUTH_COOKIE = "SSN_O";
    private static int MAX_COOKIE_AGE = 365 * 24 * 60 * 60;

    @Autowired
    UserRepo userRepo;

    @Autowired
    Encrypt encrypt;

    //getUser
    public SusunGambarUser getUser(int userId) throws UserException {
        SusunGambarUser user = userRepo.getOne(userId);
        return user;
    }

    public String getName(SusunGambarUser user) {
        if (StringUtils.isEmpty(user.getName())) return user.getMsisdn();
        if (user.getName().trim().length() == 0) return user.getMsisdn();
        return user.getName();
    }

    public SusunGambarUser getUserByMsisdn(String msisdn) {
        return userRepo.getOne(msisdn);
    }

    //updateName
    public void updateName(int userId, String name) {
        userRepo.updateName(userId, name);
    }

    public CookieAuth checkCookie(String cookie) throws UserException {
        try {
            CookieAuth auth = encrypt.decrypt(cookie, CookieAuth.class);
            return auth;
        }catch (Exception ex){
            throw new UserException();
        }
    }

    public SusunGambarUser getUser(String cookie) throws UserException {
        CookieAuth auth = checkCookie(cookie);
        SusunGambarUser user = userRepo.getOne(auth.getUserId());
        return user;
    }

    public void saveCookie(SusunGambarUser user, HttpServletResponse response) {
        CookieAuth cookieAuth = new CookieAuth();
        cookieAuth.setUserId(user.getUserId());
        cookieAuth.setMsisdn(user.getMsisdn());

        String cookieEncrypt = encrypt.encrypt(cookieAuth);
        //save cookie
        Cookie cookie = new Cookie(AUTH_COOKIE, cookieEncrypt);
        cookie.setMaxAge(MAX_COOKIE_AGE);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public void updateFlagTutor(int userId) {
        userRepo.updateTutor(userId, 1);
    }

    public void updateHint(int userId, int i) {
        userRepo.updateHint(userId, i);
    }

    public void logHint(int userId, long sessionId, int number) {
        userRepo.logHint(userId, sessionId, number);
    }

}
