package model;

import util.Util;

import java.io.File;
import java.util.LinkedHashMap;

public class PersonFactory {
    public static Person createRandom() {
        FeatureLayer[]layers = Util.getFeatureLayers();
        LinkedHashMap<String, String> selectedFeatures = new LinkedHashMap<>();

        for (FeatureLayer layer : layers) {
            if(layer.getAvailability() >= Util.getRandomInt(100)){

                File layerFile = Util.getRandomFile(
                        Util.getResourcePath("images/person/"+layer.getPath())
                        );

                selectedFeatures.put(layer.getPath(), layerFile.getName());
            }
        }

        Person person = new Person();
        person.setSelectedFeatures(selectedFeatures);

        return person;
    }
}
