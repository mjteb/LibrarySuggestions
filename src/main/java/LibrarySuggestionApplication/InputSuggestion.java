package LibrarySuggestionApplication;

import com.opencsv.CSVWriter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

import static org.apache.commons.lang3.StringUtils.split;

public class InputSuggestion {
    static CSVWriter csvWriter;
    static BufferedWriter bufferedWriter;
    static LibraryCollections collections;
    static LocalDate dateOfSubmission;
    static String author;
    static String title;
    static String isbn;
    static String libraryMemberCardNumber;
    static int positionOfMemberToSuggestTitle;
    static String typeOfLibraryCollection;


    private InputSuggestion() throws IOException {
    }

    public static void startSubmission() throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter the type of collection your suggestion belongs to: AdultBooks, ChildrensBooks," +
                " or AudiovisualDocuments");
        typeOfLibraryCollection = scanner.nextLine();
        setCsVWriter(typeOfLibraryCollection);
        scanner.close();
    }

    private static void setCsVWriter(final String typeOfLibraryCollection) throws IOException {
        if (typeOfLibraryCollection.equals(LibraryCollections.AdultBooks.toString())
                || typeOfLibraryCollection.equals(LibraryCollections.ChildrensBooks.toString())
                || typeOfLibraryCollection.equals(LibraryCollections.AudiovisualDocuments.toString())) {
            createFile();
        }
        else {
            throw new InvalidEntryException("Please enter a valid collection");
        }
    }

        private static void createFile() throws IOException {
            File file = new File(typeOfLibraryCollection + ".csv");
            if (file.exists()) {
                enterSuggestion();
            } else {
                csvWriter = new CSVWriter(new FileWriter(file, true));
                createHeader();
            }
        }

    public static void createHeader() throws IOException {
        String[] header = ("Isbn,Title,Author,Publication date,Price,Format,Library member card number," +
                "Position in line to request document,Date of submission").split(",");
        csvWriter.writeNext(header);
        csvWriter.close();
        enterSuggestion();
    }

    private static void enterSuggestion() throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter the title");
        title = scanner.nextLine();
        System.out.println("Please enter the author");
        author = scanner.nextLine();
        System.out.println("Please enter the ISBN");
        isbn = scanner.nextLine();
        System.out.println("Please enter the library member barcode");
        libraryMemberCardNumber = scanner.nextLine();
        dateOfSubmission = LocalDate.now();
        checkIfAlreadySubmitted();
        scanner.close();
    }

    public static void checkIfAlreadySubmitted() throws IOException {
        positionOfMemberToSuggestTitle = 0;
        Stream<String> txtFile = Files.lines(Paths.get(typeOfLibraryCollection + ".csv"));
        List<String> checkingIfSuggestionAlreadySubmitted = txtFile.filter(s -> s.contains(isbn))
                .toList();
        if (checkingIfSuggestionAlreadySubmitted.isEmpty()) {
            csvWriter = new CSVWriter(new FileWriter(typeOfLibraryCollection + ".csv", true));
            positionOfMemberToSuggestTitle = 1;
            AccessWebsite.launchWebsite(isbn);
        } else {
            checkIfSuggestionIsBySameUser();
        }
    }

    public static void checkIfSuggestionIsBySameUser() throws IOException {
        Stream<String> txtFile2 = Files.lines(Paths.get(typeOfLibraryCollection + ".csv"));
        boolean submittedBySameUser = txtFile2.filter(s -> s.contains(isbn))
                .anyMatch(s -> s.contains(libraryMemberCardNumber));
        if (submittedBySameUser) {
            librarySuggestionAlreadySubmitted();
        } else {
            sameSuggestionFromDifferentUser();
        }
    }

    public static void sameSuggestionFromDifferentUser () throws IOException {
        Stream<String> txtFile3 = Files.lines(Paths.get(typeOfLibraryCollection + ".csv"));
        long numberOfRequestsPerTitle = txtFile3.filter(s -> s.contains(isbn))
                .count();
        positionOfMemberToSuggestTitle = (int) numberOfRequestsPerTitle + 1;
        AccessWebsite.launchWebsite(isbn);
    }


    public static void librarySuggestionAlreadySubmitted() {
        throw new InvalidEntryException("Library member already submitted a suggestion for the following document.");
    }


    public static void printSuggestion(String price, String publicationDate, String format) throws IOException {
        String[] record = {isbn, title, author, publicationDate, price, format, libraryMemberCardNumber,
                String.valueOf(positionOfMemberToSuggestTitle), String.valueOf(LocalDate.now())};
        csvWriter.writeNext(record);
        csvWriter.close();
    }

}

