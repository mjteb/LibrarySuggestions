package LibrarySuggestionApplication;

import Exception.InvalidEntryException;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

public class CsvWriter {
    private final static String MESSAGE_ENTER_VALID_COLLECTION = "Please enter a valid collection";
    private final static String CSV = ".csv";
    private final static String CSV_COLUMNS = "Isbn,Title,Author,Publication date,Price,Format,Library member card number,Position in line to request document,Date of submission";
    private final static String VIRGULE = ",";
    private final static String ERROR_MEMBER_ALREADY_EXISTS = "Library member already submitted a suggestion for the following document.";

    private static CSVWriter csvWriter;
    private static int positionOfMemberToSuggestTitle;

    private CsvWriter() {
    }

    public static void setCsvName() throws IOException {
        if (InputSuggestion.getTypeOfLibraryCollection().equals(LibraryCollectionsEnum.AdultBooks.toString())
                || InputSuggestion.getTypeOfLibraryCollection().equals(LibraryCollectionsEnum.ChildrensBooks.toString())
                || InputSuggestion.getTypeOfLibraryCollection().equals(LibraryCollectionsEnum.AudiovisualDocuments.toString())) {
            createFile();
        } else {
            throw new InvalidEntryException(MESSAGE_ENTER_VALID_COLLECTION);
        }
    }

    private static void createFile() throws IOException {
        File file = new File(InputSuggestion.getTypeOfLibraryCollection() + CSV);
        if (file.exists()) {
            return;
        } else {
            csvWriter = new CSVWriter(new FileWriter(file, true));
            createHeader();
        }
    }

    private static void createHeader() throws IOException {
        String[] header = (CSV_COLUMNS).split(VIRGULE);
        csvWriter.writeNext(header);
        csvWriter.close();
    }

    public static void checkIfAlreadySubmitted() throws IOException {
        positionOfMemberToSuggestTitle = 0;
        Stream<String> txtFile = Files.lines(Paths.get(InputSuggestion.getTypeOfLibraryCollection() + CSV));
        List<String> checkingIfSuggestionAlreadySubmitted = txtFile.filter(s -> s.contains(InputSuggestion.getIsbn()))
                .toList();
        if (checkingIfSuggestionAlreadySubmitted.isEmpty()) {
            positionOfMemberToSuggestTitle = 1;
            AccessWebsite.launchWebsite(InputSuggestion.getIsbn());
        } else {
            checkIfSuggestionIsBySameUser();
        }
    }

    public static void checkIfSuggestionIsBySameUser() throws IOException {
        Stream<String> txtFile2 = Files.lines(Paths.get(InputSuggestion.getTypeOfLibraryCollection() + CSV));
        boolean submittedBySameUser = txtFile2.filter(s -> s.contains(InputSuggestion.getIsbn()))
                .anyMatch(s -> s.contains(InputSuggestion.getLibraryMemberCardNumber()));
        if (submittedBySameUser) {
            throw new InvalidEntryException(ERROR_MEMBER_ALREADY_EXISTS);
        } else {
            sameSuggestionFromDifferentUser();
        }
    }

    public static void sameSuggestionFromDifferentUser() throws IOException {
        Stream<String> txtFile3 = Files.lines(Paths.get(InputSuggestion.getTypeOfLibraryCollection() + CSV));
        long numberOfRequestsPerTitle = txtFile3.filter(s -> s.contains(InputSuggestion.getIsbn()))
                .count();
        positionOfMemberToSuggestTitle = (int) numberOfRequestsPerTitle + 1;
        AccessWebsite.launchWebsite(InputSuggestion.getIsbn());
    }

    public static void printSuggestion(String price, String publicationDate, String format) throws IOException {
        File file = new File(InputSuggestion.getTypeOfLibraryCollection() + CSV);
        csvWriter = new CSVWriter(new FileWriter(file, true));
        String[] record = {InputSuggestion.getIsbn(), InputSuggestion.getTitle(), InputSuggestion.getAuthor(), publicationDate, price, format, InputSuggestion.getLibraryMemberCardNumber(),
                String.valueOf(positionOfMemberToSuggestTitle), String.valueOf(LocalDate.now())};
        csvWriter.writeNext(record);
        csvWriter.close();
    }
}
