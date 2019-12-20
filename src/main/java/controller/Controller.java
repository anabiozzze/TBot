package controller;

public class Controller {
    private JsonParser jsonResponse;
    private ImgMaker img;
    private String URL;

    public Controller(String URL) {
        this.URL = URL;
    }

    public void getResponse() {
        this.jsonResponse = new JsonParser(URL);
    }

    public void getImg() {
        this.img = new ImgMaker(jsonResponse.getResponse());
        img.makeImg();
    }
}
