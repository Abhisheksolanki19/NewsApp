package com.shivamsaluja20.android.newsreport;

class News {
    private String sectionName, title, date, time, urls, author;

    News(String sectionName, String title, String date, String time, String urls, String author) {
        this.sectionName = sectionName;
        this.title = title;
        this.time = time;
        this.date = date;
        this.urls = urls;
        this.author = author;
    }

    String getAuthor() {
        return author;
    }

    String getSectionName() {
        return sectionName;
    }


    String getTitle() {
        return title;
    }

    String getDate() {
        return date;
    }


    String getTime() {
        return time;
    }

    String getUrls() {
        return urls;
    }


}
