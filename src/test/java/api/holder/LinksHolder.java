package api.holder;

public interface LinksHolder {
    String CSV_FILE_PATH = "src/test/java/files/test.csv";
    String XML_FILE_PATH = "src/test/java/files/test.xml";
    String WORD_FILE_PATH = "src/test/java/files/nonvalid.word";
    String REMOVE_ALL_ANIMALS_FILE_PATH =
            "classpath:database/remove-all-animals-from-database.sql";
    String ADD_ANIMALS_TO_ANIMALS_FILE_PATH =
            "classpath:database/add-animals-to-database.sql";
}
