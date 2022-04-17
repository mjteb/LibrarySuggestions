package LibrarySuggestionApplication;

public enum LibraryCollectionsEnum {
    AdultBooks("Adult Books"),
    ChildrensBooks("Childrens Books"),
    AudiovisualDocuments("Audiovisual Documents");

    private String name;

    LibraryCollectionsEnum(final String name) {
        this.name = name;
    }

}
