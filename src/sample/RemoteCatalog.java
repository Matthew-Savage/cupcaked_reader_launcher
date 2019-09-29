package sample;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static sample.CategoryMangaLists.*;

public class RemoteCatalog {

    private static int newestTitleIdent;
    private static ArrayList<Manga> results = new ArrayList<>();
    private static int currentPageNum = 1;
    private static int currentEntryNum = 0;
    //    private static int currentMultiplier = 1;
    private static boolean matchNotFound = true;

    public static void indexTitles() {
        Controller.paneMid.setValue("blank");
        Controller.paneTop.setValue("top_new_1");
        prepareComparisonList();
        do {
            processRemoteEntries();
        } while (matchNotFound);
    }

    private static void prepareComparisonList() {
        populateFiveNewest();
        newestTitleIdent = fiveNewestTitles.get(0).getTitleId();
    }

    private static void populateFiveNewest() {
        ArrayList<Manga> consolidated = MangaConsolidated.list();
        consolidated.sort(Comparator.comparingInt(Manga::getTitleId).reversed());
        determineFiveLatest(consolidated);
    }

    private static void determineFiveLatest(ArrayList<Manga> arrayList) {
        int i = 0;
        while (fiveNewestTitles.size() < 5) {
            boolean notYetPresent = true;
            for (Manga manga : fiveNewestTitles) {
                if (manga.getTitleId() == arrayList.get(i).getTitleId()) {
                    notYetPresent = false;
                    break;
                }
            }
            if (notYetPresent) {
                fiveNewestTitles.add(arrayList.get(i));
            } else {
                i++;
            }
        }
    }

    private static void processRemoteEntries() {
        for (Element remoteEntry : remoteEntries().select(entryVal())) {
            compareRemoteToLocal(remoteEntry);
        }
        tryNextPage();
    }

    private static void compareRemoteToLocal(Element element) {
        if (remoteMangaTitle(element).equals(localMangaTitle())) {
            matchNotFound = false;
            processResults();
        } else {
            results.add(new Manga(remoteMangaTitle(element), remoteMangaWebAddress(element)));
        }
    }

    private static void processResults() {
        if (results.size() == 0) {
            Controller.paneTop.setValue("top_new_3");
        } else {
            Controller.paneTop.setValue("top_new_2");
//            ControllerLoader.update.set("Adding " + results.size() + " new titles ...");
            Collections.reverse(results);
            Database.openConnection(Val.DB_NAME_MANGA.get());
            for (int i = 0; i < results.size(); i++) {
                newestTitleIdent++;
                populateRemainingValues(i, true);
                insertNewRecord(i, Val.DB_TABLE_AVAILABLE.get());
            }
            Database.closeConnection();
        }
    }

    private static void populateRemainingValues(int indexNumber, boolean firstTimeFetched){
        Document document = fetchPageSource(results.get(indexNumber).getWebAddress());
        for (Element details : document.select(detailVal())) {
            results.get(indexNumber).setAuthors(appendPrefix(details.child(1).text()));
            results.get(indexNumber).setStatus(formatStatus(appendPrefix(details.child(2).text())));
            results.get(indexNumber).setGenreTags(appendPrefix(details.child(6).text()));
        }
        results.get(indexNumber).setTitleId(newestTitleIdent);
        document.select("h2 p").remove();
        results.get(indexNumber).setSummary(document.select("div#noidungm").text().replace("'", ""));
        if (firstTimeFetched) {
            RemoteImage.saveLocally(document.select(".manga-info-pic img").first().attr("abs:src"), newestTitleIdent);
            results.get(indexNumber).setTotalChapters(0);
            results.get(indexNumber).setCurrentPage(0);
            results.get(indexNumber).setLastChapterRead(0);
            results.get(indexNumber).setLastChapterDownloaded(0);
            results.get(indexNumber).setNewChapters(0);
            results.get(indexNumber).setFavorite(0);
            updateProgress(indexNumber);
        }
    }

    private static void insertNewRecord(int indexNumber, String tableName) {

        Database.addMangaEntry(tableName, results.get(indexNumber).getTitleId(),
                results.get(indexNumber).getTitle(),
                results.get(indexNumber).getAuthors(),
                results.get(indexNumber).getStatus(),
                results.get(indexNumber).getSummary(),
                results.get(indexNumber).getWebAddress(),
                results.get(indexNumber).getGenreTags(),
                results.get(indexNumber).getTotalChapters(),
                results.get(indexNumber).getCurrentPage(),
                results.get(indexNumber).getLastChapterRead(),
                results.get(indexNumber).getLastChapterDownloaded(),
                results.get(indexNumber).getNewChapters(),
                results.get(indexNumber).getFavorite());
        if (tableName.equals(Val.DB_TABLE_AVAILABLE.get())) {
            notCollectedMangaList.add(results.get(indexNumber));
        } else {
            fiveNewestTitles.add(results.get(indexNumber));
        }

    }

    private static void updateProgress(int indexNumber) {
        Controller.paneMid.setValue("progress/" + (indexNumber * 100) / results.size());
    }

    private static void tryNextPage() {
        currentPageNum++;

        if (currentEntryNum == 4 && currentPageNum == 50) {
            matchNotFound = false;
        }

        if (currentPageNum == 50) {
            results.clear();
            currentEntryNum++;
            currentPageNum = 1;
        }
//        if (currentPageNum == (10 * currentMultiplier)) {
//            currentEntryNum++;
//            currentPageNum = 1;
//            if (currentEntryNum == 5) {
//                if (currentMultiplier == 1) {
//                    ControllerLoader.update.set("Still fetching ...");
//                    currentMultiplier = 10;
//                    currentPageNum = 10;
//                    currentEntryNum = 0;
//                } else if (currentMultiplier == 10) {
//                    ControllerLoader.updatable = false;
//                    matchNotFound = false;
//                }
//            }
//        }
    }

    private static boolean comparisonsExhausted() {
        return currentEntryNum == 5;
    }

    private static Document remoteEntries() {
        return selectionPageUrl();
    }

    private static Document selectionPageUrl() {
        return fetchPageSource(Val.URL_ROOT.get() + "manga_list?type=newest&category=all&state=all&page=" + currentPageNum);
    }

    private static Document fetchPageSource(String webAddress) {
        try {
            return Jsoup.connect(webAddress).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String remoteMangaTitle(Element element) {
        return element.select("h3 a").text().replace("'", "");
    }

    private static String remoteMangaWebAddress(Element element) {
        return element.select("h3 a").first().attr("abs:href");
    }

    private static String localMangaTitle() {
        return fiveNewestTitles.get(currentEntryNum).getTitle();
    }

    private static String appendPrefix(String string) {
        return string.replaceAll(".*: ", "");
    }

    private static String formatStatus(String statusText) {
        if (statusText.contains("Completed") && statusText.length() > 9) {
            return statusText.substring(0, 9);
        } else if (statusText.contains("Ongoing") && statusText.length() > 7) {
            return statusText.substring(0, 7);
        } else {
            return statusText;
        }
    }

    private static String entryVal() {
        return ".list-truyen-item-wrap";
    }

    private static String detailVal() {
        return ".manga-info-text";
    }
}
