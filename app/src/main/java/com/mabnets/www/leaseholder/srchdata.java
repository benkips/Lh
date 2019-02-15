package com.mabnets.www.leaseholder;

import java.io.Serializable;

public class srchdata implements Serializable {
    private String names;
    private String phone;
    private String lpic;
    private String premise;
    private String county;
    private String town;
    private String image;
    private String description;
    private String photosid;

    public srchdata() {
    }

    public String getNames() {
        return names;
    }

    public String getPhone() {
        return phone;
    }

    public String getLpic() {
        return lpic;
    }

    public String getPremise() {
        return premise;
    }

    public String getCounty() {
        return county;
    }

    public String getTown() {
        return town;
    }

    public String getImage() {
        return image;
    }

    public String getDescription() {
        return description;
    }

    public String getPhotosid() {
        return photosid;
    }
}
