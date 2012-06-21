package com.example.vaadin.webapp;

import com.vaadin.Application;

public class MainPageContentCreator {

    public MainPageContent createMainPage(Application application) {
        MainPageContent mainPageContent = new MainPageContent(application);

        // setup parameters of main page Widgets 
        // and add any data

        return mainPageContent;
    }

}
