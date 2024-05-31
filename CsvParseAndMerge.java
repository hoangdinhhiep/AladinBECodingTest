package com.tatoeba;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CsvParseAndMerge {
    public static void readCsvFile(String filePath, Consumer<String[]> consumer) {
        try (Stream<String> lines = Files.lines(Paths.get(filePath))) {
            lines.forEach(line -> {
                String[] split = line.split("\t");
                consumer.accept(split);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        final Map<Integer, Sentence> engSentenceMap = new HashMap<>();
        final Map<Integer, Sentence> vieSentenceMap = new HashMap<>();
//        String sentencesFilePath = "/home/hoangdinh/projects/aladin_becodingtest/sentences.csv";
        String sentencesFilePath = args[0];
        buildSentenceMap(sentencesFilePath, engSentenceMap, vieSentenceMap);

//        String sentenceLinkFilePath = "/home/hoangdinh/projects/aladin_becodingtest/links.csv";
        String sentenceLinkFilePath = args[1];
        final List<SentenceLink> sentenceLinkList = getSentenceList(
                sentenceLinkFilePath,
                engSentenceMap,
                vieSentenceMap);

//        String sentenceAudioFilePath = "/home/hoangdinh/projects/aladin_becodingtest/sentences_with_audio.csv";
        String sentenceAudioFilePath = args[2];
        final Map<Integer, SentenceAudio> engSentenceAudioMap = getSentenceAudioMap(
                sentenceAudioFilePath,
                sentenceLinkList);

//        String resultFilePath = "/home/hoangdinh/projects/aladin_becodingtest/result.csv";
        String resultFilePath = args[3];
        writeToFile(resultFilePath, bw -> {
            final Map<SentenceLink, String> resultMap = buildResultMap(sentenceLinkList, engSentenceMap, vieSentenceMap, engSentenceAudioMap);

            for (String data : resultMap.values()) {
                try {
                    //Writing to the file
                    bw.write(data);
                    bw.newLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private static void writeToFile(String filePath, Consumer<BufferedWriter> consumer) {
        File file;
        BufferedWriter bw = null;
        FileWriter fw = null;

        try {
            file = new File(filePath);
            fw = new FileWriter(file, false);
            bw = new BufferedWriter(fw);

            consumer.accept(bw);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bw != null) {
                    bw.close();
                }
                if (fw != null) {
                    fw.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static Map<SentenceLink, String> buildResultMap(List<SentenceLink> sentenceLinkList,
                                                            Map<Integer, Sentence> engSentenceMap,
                                                            Map<Integer, Sentence> vieSentenceMap,
                                                            Map<Integer, SentenceAudio> engSentenceAudioMap) {
        final Map<SentenceLink, String> resultMap = new HashMap<>();
        sentenceLinkList.forEach(sentenceLink -> {
            if (engSentenceMap.containsKey(sentenceLink.getSentenceId())
                    && vieSentenceMap.containsKey(sentenceLink.getTranslationId())) {
                SentenceAudio sentenceAudio = engSentenceAudioMap.get(sentenceLink.getSentenceId());

                Sentence engSentence = engSentenceMap.get(sentenceLink.getSentenceId());
                Sentence vieSentence = vieSentenceMap.get(sentenceLink.getTranslationId());

                String data = engSentence.getId() + "\t" + engSentence.getText() + "\t" +
                        (sentenceAudio != null && sentenceAudio.getUrl() != null && sentenceAudio.getUrl().startsWith("http")
                                ? sentenceAudio.getUrl() + "/eng/" + engSentence.getId() + ".mp3"
                                : "\\N")
                        + "\t" + vieSentence.getId() + "\t" + vieSentence.getText();
                resultMap.put(sentenceLink, data);
            }
        });

        return resultMap;
    }

    private static List<SentenceLink> getSentenceList(String filePath,
                                                      Map<Integer, Sentence> engSentenceMap,
                                                      Map<Integer, Sentence> vieSentenceMap) {
        final List<SentenceLink> sentenceLinkList = new ArrayList<>();

        readCsvFile(filePath, split -> {
            if (split.length != 2) {
                return;
            }

            int sentenceId = Integer.parseInt(split[0]);
            int transactionId = Integer.parseInt(split[1]);

            if (engSentenceMap.containsKey(sentenceId) && vieSentenceMap.containsKey(transactionId)) {
                sentenceLinkList.add(SentenceLink.SentenceLinkBuilder.aSentenceLink()
                        .withSentenceId(sentenceId)
                        .withTranslationId(transactionId)
                        .build());
            } else if (engSentenceMap.containsKey(transactionId) && vieSentenceMap.containsKey(sentenceId)) {
                sentenceLinkList.add(SentenceLink.SentenceLinkBuilder.aSentenceLink()
                        .withSentenceId(transactionId)
                        .withTranslationId(sentenceId)
                        .build());
            }
        });

        return sentenceLinkList;
    }

    private static Map<Integer, SentenceAudio> getSentenceAudioMap(String filePath,
                                                                   List<SentenceLink> sentenceLinkList) {
        final Map<Integer, SentenceAudio> engSentenceAudioMap = new HashMap<>();
        final Set<Integer> sentenceLinkIdSet = sentenceLinkList.parallelStream()
                .map(SentenceLink::getSentenceId)
                .collect(Collectors.toSet());

        readCsvFile(filePath, split -> {
            if (split.length != 4) {
                return;
            }

            int sentenceId = Integer.parseInt(split[0]);

            if (sentenceLinkIdSet.contains(sentenceId)) {
                engSentenceAudioMap.put(sentenceId,
                        SentenceAudio.SentenceAudioBuilder.aSentenceAudio()
                                .withId(sentenceId)
                                .withUsername(split[1])
                                .withLicense(split[2])
                                .withUrl(split[3])
                                .build());
            }
        });

        return engSentenceAudioMap;
    }

    private static void buildSentenceMap(String filePath,
                                         Map<Integer, Sentence> engSentenceMap,
                                         Map<Integer, Sentence> vieSentenceMap) {
        readCsvFile(filePath, split -> {
            if (split.length != 3) {
                return;
            }

            String lang = split[1];
            if ("eng".equals(lang) || "vie".equals(lang)) {
                Sentence sentence = Sentence.SentenceBuilder.aSentence()
                        .withId(Integer.parseInt(split[0]))
                        .withLang(lang)
                        .withText(split[2])
                        .build();
                if ("eng".equals(lang)) {
                    engSentenceMap.put(sentence.getId(), sentence);
                } else {
                    vieSentenceMap.put(sentence.getId(), sentence);
                }
            }
        });
    }
}

class Sentence {
    private int id;
    private String lang;
    private String text;

    private Sentence() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public static final class SentenceBuilder {
        private int id;
        private String lang;
        private String text;

        private SentenceBuilder() {
        }

        public static SentenceBuilder aSentence() {
            return new SentenceBuilder();
        }

        public SentenceBuilder withId(int id) {
            this.id = id;
            return this;
        }

        public SentenceBuilder withLang(String lang) {
            this.lang = lang;
            return this;
        }

        public SentenceBuilder withText(String text) {
            this.text = text;
            return this;
        }

        public Sentence build() {
            Sentence sentence = new Sentence();
            sentence.setId(id);
            sentence.setLang(lang);
            sentence.setText(text);
            return sentence;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sentence sentence = (Sentence) o;
        return id == sentence.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

class SentenceAudio {
    private int id;
    private String username;
    private String license;
    private String url;

    private SentenceAudio() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SentenceAudio that = (SentenceAudio) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public static final class SentenceAudioBuilder {
        private int id;
        private String username;
        private String license;
        private String url;

        private SentenceAudioBuilder() {
        }

        public static SentenceAudioBuilder aSentenceAudio() {
            return new SentenceAudioBuilder();
        }

        public SentenceAudioBuilder withId(int id) {
            this.id = id;
            return this;
        }

        public SentenceAudioBuilder withUsername(String username) {
            this.username = username;
            return this;
        }

        public SentenceAudioBuilder withLicense(String license) {
            this.license = license;
            return this;
        }

        public SentenceAudioBuilder withUrl(String url) {
            this.url = url;
            return this;
        }

        public SentenceAudio build() {
            SentenceAudio sentenceAudio = new SentenceAudio();
            sentenceAudio.setId(id);
            sentenceAudio.setUsername(username);
            sentenceAudio.setLicense(license);
            sentenceAudio.setUrl(url);
            return sentenceAudio;
        }
    }
}

class SentenceLink {
    private int sentenceId;
    private int translationId;

    private SentenceLink() {
    }

    public int getSentenceId() {
        return sentenceId;
    }

    public void setSentenceId(int sentenceId) {
        this.sentenceId = sentenceId;
    }

    public int getTranslationId() {
        return translationId;
    }

    public void setTranslationId(int translationId) {
        this.translationId = translationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SentenceLink that = (SentenceLink) o;
        return sentenceId == that.sentenceId && translationId == that.translationId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(sentenceId, translationId);
    }

    public static final class SentenceLinkBuilder {
        private int sentenceId;
        private int translationId;

        private SentenceLinkBuilder() {
        }

        public static SentenceLinkBuilder aSentenceLink() {
            return new SentenceLinkBuilder();
        }

        public SentenceLinkBuilder withSentenceId(int sentenceId) {
            this.sentenceId = sentenceId;
            return this;
        }

        public SentenceLinkBuilder withTranslationId(int translationId) {
            this.translationId = translationId;
            return this;
        }

        public SentenceLink build() {
            SentenceLink sentenceLink = new SentenceLink();
            sentenceLink.setSentenceId(sentenceId);
            sentenceLink.setTranslationId(translationId);
            return sentenceLink;
        }
    }
}