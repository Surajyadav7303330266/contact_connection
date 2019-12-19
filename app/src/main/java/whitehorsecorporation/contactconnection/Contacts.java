package whitehorsecorporation.contactconnection;

public class Contacts {
    public String name, status, image,sback,ads;

    public Contacts()
    {

    }

    public Contacts(String name, String status, String image,String sback) {
        this.name = name;
        this.status = status;
        this.image = image;
        this.sback = sback;
        this.ads = ads;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getShopback() {
        return sback;
    }

    public void setShopback(String sback) {
        this.sback = sback;
    }

    public String getAds() {
        return ads;
    }

    public void setAds(String ads) {
        this.ads = ads;
    }
}
