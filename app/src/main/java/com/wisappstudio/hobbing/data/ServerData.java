package com.wisappstudio.hobbing.data;

public class ServerData {
    public static String IP = "182.228.83.7";
    public static String LOCALHOST = "192.168.219.110";
    public static String URL = "http://"+IP+"/hobbing/";
    public static String SIGN_UP_URL = URL+"login/sign_up.php";
    public static String SIGN_IN_URL = URL+"login/sign_in.php";
    public static String POST_READ_URL = URL+"post/read.php";

    public static String INNER_POST_READ_URL = URL+"post/inner_read.php";
    public static String INNER_POST_IMAGE_READ_URL = URL + "post/inner_image_read.php";

    public static String PROFILE_IMAGE_DIRECTORY = URL + "image/profile/";
    public static String POST_IMAGE_DIRECTORY = URL + "image/post/";
}
