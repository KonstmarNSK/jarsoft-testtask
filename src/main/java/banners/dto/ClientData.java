package banners.dto;

public class ClientData {
    public final String address;
    public final String userAgent;

    public ClientData(String address, String userAgent) {
        this.address = address;
        this.userAgent = userAgent;
    }
}
