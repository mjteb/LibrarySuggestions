package LibrarySuggestionApplication;

import java.util.Objects;

public enum LibraryCollections {
    AdultBooks("Adult Books"),
    ChildrensBooks("Childrens Books"),
    AudiovisualDocuments("Audiovisual Documents");

    private String name;

    LibraryCollections(String name) {
        this.name = name;
    }

    public static boolean verifierExistance(final String requete) {
        LibraryCollections resultat = LibraryCollections.valueOf(requete);
        return Objects.nonNull(resultat);
    }


}
