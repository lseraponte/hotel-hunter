package com.lseraponte.cupidapi.hh.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TranslationUpdater<T, ID, TR> {

    public List<T> updateTranslations(
            List<T> currentList,
            List<T> retrievedList,
            Function<T, ID> idExtractor,
            Function<T, List<TR>> translationExtractor,
            ValuesMerger<TR> translationMerger,
            String languageCode,
            Function<TR, String> languageExtractor) {

        List<T> updatedList = new ArrayList<>();
        Map<ID, T> retrievedMap = retrievedList.stream()
                .collect(Collectors.toMap(idExtractor, Function.identity()));

        for (T currentItem : currentList) {
            ID currentId = idExtractor.apply(currentItem);
            T retrievedItem = retrievedMap.get(currentId);

            if (retrievedItem != null) {
                List<TR> retrievedTranslations = translationExtractor.apply(retrievedItem);
                List<TR> currentTranslations = translationExtractor.apply(currentItem);

                boolean translationUpdated = false;

                for (int i = 0; i < retrievedTranslations.size(); i++) {
                    if (languageCode.equals(languageExtractor.apply(retrievedTranslations.get(i)))) {
                        retrievedTranslations.set(i,
                                translationMerger.merge(currentTranslations.get(0), retrievedTranslations.get(i)));
                        translationUpdated = true;
                        break;
                    }
                }

                if (!translationUpdated) {
                    retrievedTranslations.add(currentTranslations.get(0));
                }

                updatedList.add(retrievedItem);
            } else {
                updatedList.add(currentItem);
            }
        }
        return updatedList;
    }
}
