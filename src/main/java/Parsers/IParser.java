package Parsers;

import Entities.Mission;

public interface IParser {
    Mission parse(String file) throws Exception;
}
