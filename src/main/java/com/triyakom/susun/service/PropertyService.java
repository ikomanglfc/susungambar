package com.triyakom.susun.service;

import com.sun.org.apache.bcel.internal.generic.POP;
import com.triyakom.susun.repo.PropertyRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.HashMap;

/**
 * Hold property load statically or by API
 */
@Service
public class PropertyService {

    @Autowired
    PropertyRepo repo;

    private HashMap<String, Object> property = new HashMap<>();

    @PostConstruct
    public void loadProps(){
        property.put(HOME_BANNER, repo.loadProperty(HOME_BANNER));
        property.put(SNK_BANNER, repo.loadProperty(SNK_BANNER));
        property.put(POPUP_BANNER, repo.loadProperty(POPUP_BANNER));

    }

    public String getHomeBanner(){
        return (String) property.get(HOME_BANNER);
    }

    public String getSnkBanner(){
        return (String) property.get(SNK_BANNER);
    }

    public String getPopupBanner(){
        return (String) property.get(POPUP_BANNER);
    }

    public HashMap<String, Object> getProperty() {
        return property;
    }

    private static final String HOME_BANNER = "HOME_BANNER";
    private static final String SNK_BANNER = "SNK_BANNER";
    private static final String POPUP_BANNER = "POPUP_BANNER";

}
