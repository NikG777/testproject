package com.nikita.testproject.service;

import com.nikita.testproject.entities.PasswordEntity;
import com.nikita.testproject.entities.RoleEntity;
import com.nikita.testproject.entities.UserEntity;
import com.nikita.testproject.exception.UserServiceException;
import com.nikita.testproject.repo.PasswordEntityRepository;
import com.nikita.testproject.repo.RoleEntityRepository;
import com.nikita.testproject.repo.UserEntityRepository;
import org.aspectj.weaver.patterns.IToken;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


import java.io.IOException;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserEntityRepository userEntityRepository;
    @Autowired
    private RoleEntityRepository roleEntityRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private PasswordEntityRepository passwordEntityRepository;
    @Autowired
    private MailSender mailSender;

    public UserEntity saveUser(UserEntity userEntity) {
        try {
            RoleEntity userRole = roleEntityRepository.findByName("ROLE_USER");
            userEntity.setRoleEntity(userRole);
            userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
            userEntity.setActivationCode(UUID.randomUUID().toString());
            //email sending
            if (!StringUtils.isEmpty(userEntity.getEmail())) {
                String message = String.format("Привет, %s! \n" +
                        "Ваша ссылка активации: http://188.244.5.33:8080/activate/" + userEntity.getActivationCode(), userEntity.getLogin());

                mailSender.send(userEntity.getEmail(), "Код активации", message);
            }
            return userEntityRepository.save(userEntity);
        } catch (NullPointerException e) {
            throw  new UserServiceException("Not found",e);
        }
    }
    public void update (UserEntity userEntity)
    {
        try {
            userEntityRepository.save(userEntity);
        }
        catch (NullPointerException e) {throw new NullPointerException("UserMust be not null");}
    }
    public UserEntity findByLogin(String login) {
        try {
            return userEntityRepository.findByLogin(login);
        }catch (NullPointerException e){
            throw new UserServiceException("Not found");
        }
    }
    public UserEntity findByEmail(String email) {
        try{
        return userEntityRepository.findByEmail(email);
        }catch (NullPointerException e){
        throw new UserServiceException("Not found");
    }
    }

    public UserEntity findByLoginAndPassword(String login, String password) {
        try{
        UserEntity userEntity = findByLogin(login);
        if (userEntity != null) {
            if (passwordEncoder.matches(password, userEntity.getPassword())) {
                return userEntity;
            }
        }
        return null;
        }catch (NullPointerException e){
        throw new UserServiceException("Not found");
    }
    }

    public boolean activateUser(String code) {
        try{
       UserEntity user = userEntityRepository.findByActivationCode(code);
        if(user == null) return false;
        user.setActivationCode(null);
        userEntityRepository.save(user);
        return true;
        }catch (NullPointerException e){
        throw new UserServiceException("Not found");
    }
    }

    public boolean resetUserPassword(String email)
    {
        try {
            UserEntity user = userEntityRepository.findByEmail(email);
            Date date = new Date();

            PasswordEntity passwordEntity = new PasswordEntity();
            passwordEntity.setToken(UUID.randomUUID().toString());
            passwordEntity.setDate(date);

            user.setPasswordEntity(passwordEntity);

            if (!StringUtils.isEmpty(user.getEmail())) {
                String message = String.format("Privet, %s! \n" +
                        "Welcome. http://localhost:8080/password/reset/%s", user.getPasswordEntity().getToken(), user.getLogin());

                mailSender.send(user.getEmail(), "Reset code", message);
            }
            userEntityRepository.save(user);
        }catch (NullPointerException e) {
            throw new UserServiceException("Dont find user",e);
        }
        return true;
    }
    public boolean updateUserPassword(String token, String password)
    {
        try{
        UserEntity user = userEntityRepository.findUserEntityByPasswordEntity_Token(token);
        if(user == null) return false;
        Period period = Period.between(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), user.getPasswordEntity().getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        PasswordEntity passwordEntity = passwordEntityRepository.findByToken(token);
        if(period.getDays() > 1)
        {
            user.setPassword(passwordEncoder.encode(password));
            passwordEntityRepository.delete(passwordEntity);
            passwordEntity.getUser_table().setPasswordEntity(null);
            return false;
        }

        user.setPassword(passwordEncoder.encode(password));
        passwordEntity.getUser_table().setPasswordEntity(null);
        userEntityRepository.save(user);
        passwordEntityRepository.delete(passwordEntity);
        return true;
         }catch (NullPointerException e){
        throw new UserServiceException("Not found");
    }

    }


}