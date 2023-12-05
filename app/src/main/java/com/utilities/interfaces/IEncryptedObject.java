package com.utilities.interfaces;

import org.json.JSONException;
import org.json.JSONObject;

public interface IEncryptedObject
{
    String getText();
    String getSalt();
    String getSecret();
    String get6code();
    JSONObject toJsonObject() throws JSONException;
}
