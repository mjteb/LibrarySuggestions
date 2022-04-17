package LibrarySuggestionApplication;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Scanner;

import static LibrarySuggestionApplication.CsvWriter.checkIfAlreadySubmitted;

public class InputSuggestion {

    private final static String MESSAGE_TYPE_COLLECTION = "Please enter the type of collection your suggestion belongs to: AdultBooks, ChildrensBooks, or AudiovisualDocuments";
    private final static String ENTER_TITLE = "Please enter the title";
    private final static String ENTER_AUTHOR = "Please enter the author";
    private final static String ENTER_ISBN = "Please enter the ISBN";
    private final static String ENTER_BARCODE = "Please enter the library member barcode";


    private static LocalDate dateOfSubmission;
    private static String author;
    private static String title;
    private static String isbn;
    private static String libraryMemberCardNumber;
    private static String typeOfLibraryCollection = null;

    private InputSuggestion() {
    }

    public static String startSubmission() throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println(MESSAGE_TYPE_COLLECTION);
        typeOfLibraryCollection = scanner.nextLine();
        System.out.println(ENTER_TITLE);
        title = scanner.nextLine();
        System.out.println(ENTER_AUTHOR);
        author = scanner.nextLine();
        System.out.println(ENTER_ISBN);
        isbn = scanner.nextLine();
        System.out.println(ENTER_BARCODE);
        libraryMemberCardNumber = scanner.nextLine();
        dateOfSubmission = LocalDate.now();
        CsvWriter.setCsvName();
        checkIfAlreadySubmitted();
        scanner.close();

        return typeOfLibraryCollection;
    }

    public static String getAuthor() {
        return author;
    }

    public static String getTitle() {
        return title;
    }

    public static String getIsbn() {
        return isbn;
    }

    public static String getLibraryMemberCardNumber() {
        return libraryMemberCardNumber;
    }

    public static String getTypeOfLibraryCollection() {
        return typeOfLibraryCollection;
    }
}

