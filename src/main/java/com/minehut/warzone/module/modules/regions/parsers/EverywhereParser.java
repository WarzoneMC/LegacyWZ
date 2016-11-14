package com.minehut.warzone.module.modules.regions.parsers;

import com.minehut.warzone.module.modules.regions.RegionParser;
import org.jdom2.Element;

public class EverywhereParser extends RegionParser {

    public EverywhereParser(Element element) {
        super(element.getAttributeValue("name") != null ? element.getAttributeValue("name") : element.getAttributeValue("id"));
    }

}
