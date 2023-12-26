package com.free;

import java.util.List;

public class AccountImages
{
    private static List<String> accountImagesPath;

    public List<String> getImagePath() {
        return accountImagesPath;
    }
    public void setImagePath(List<String> imagePath) {
        AccountImages.accountImagesPath = imagePath;
    }
}
