package movies.search.app.globals;

import movies.search.app.bo.ImageConfiguration;

public class AppData {

	private static AppData instance = null;
    private ImageConfiguration imageConfig;

	protected AppData() {
		// Exists only to defeat instantiation.
	}

	public static AppData getInstance() {
		if(instance == null) {
			instance = new AppData();
		}
		return instance;
	}

    public ImageConfiguration getImageConfig() {
        return imageConfig;
    }

    public void setImageConfig(ImageConfiguration imageConfig) {
        this.imageConfig = imageConfig;
    }
}
