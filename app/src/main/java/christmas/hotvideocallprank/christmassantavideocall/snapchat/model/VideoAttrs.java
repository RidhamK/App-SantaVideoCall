package christmas.hotvideocallprank.christmassantavideocall.snapchat.model;

public class VideoAttrs {
    private String name;
    private String number;
    private String videoName;
    private String uri;
    private String videoPath;

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public boolean isInternal() {
        return isInternal;
    }

    public void setInternal(boolean internal) {
        isInternal = internal;
    }

    private boolean isInternal;
    private int numberOfCalls;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public int getNumberOfCalls() {
        return numberOfCalls;
    }

    public void setNumberOfCalls(int numberOfCalls) {
        this.numberOfCalls = numberOfCalls;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

//    public JSONObject toJSON() {
//        try {
//            JSONObject jsonObject = new JSONObject();
//            jsonObject.put(StorageManager.NAME, getName());
//            jsonObject.put(StorageManager.NUMBER, getNumber());
//            jsonObject.put(StorageManager.NUMBER_OF_CALLS, getNumberOfCalls());
//            jsonObject.put(StorageManager.URL, getUri());
//            return jsonObject;
//        } catch (Exception e) {
//            return null;
//        }
//    }
}
