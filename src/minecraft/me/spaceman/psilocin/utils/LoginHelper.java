package me.spaceman.psilocin.utils;

import com.mojang.authlib.Agent;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import net.minecraft.util.Session;

import java.net.Proxy;

public class LoginHelper {

    public static Session login(String username, String password)
    {
        YggdrasilUserAuthentication auth = (YggdrasilUserAuthentication) new YggdrasilAuthenticationService(Proxy.NO_PROXY,  "1").createUserAuthentication(Agent.MINECRAFT);
        auth.setPassword(password);
        auth.setUsername(username);
        password = null;

        try {
            auth.logIn();
        } catch (AuthenticationException e)
        {
            e.printStackTrace();
        }
        if(auth.isLoggedIn()) {
            Session session = new Session(auth.getSelectedProfile().getName(), auth.getSelectedProfile().getId().toString().replace("-", ""), auth.getAuthenticatedToken(), auth.getUserType().getName());
            return session;
        } else {
            return null;
        }
    }

}
