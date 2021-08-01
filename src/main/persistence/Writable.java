package persistence;

import org.json.JSONObject;

public interface Writable {

    //Effects: Returns object as a JSON object
    JSONObject toJson();
}
