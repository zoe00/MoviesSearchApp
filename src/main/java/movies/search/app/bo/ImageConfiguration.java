package movies.search.app.bo;

public class ImageConfiguration {

    private String[] logo_sizes;
    private String[] poster_sizes;
    private String base_url = "";

    public String[] getLogoSizes() {
        return logo_sizes;
    }

    public void setLogoSizes(String[] logo_sizes) {
        this.logo_sizes = logo_sizes;
    }

    public String[] getPosterSizes() {
        return poster_sizes;
    }

    public void setPosterSizes(String[] poster_sizes) {
        this.poster_sizes = poster_sizes;
    }

    public String getBaseUrl() {
        return base_url;
    }

    public void setBaseUrl(String base_url) {
        this.base_url = base_url;
    }
}
