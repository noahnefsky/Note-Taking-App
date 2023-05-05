module notes {
    requires kotlin.stdlib;
    requires javafx.controls;
    requires kotlinx.coroutines.core.jvm;
    requires shared;
    requires javafx.web;
    requires kotlinx.serialization.json;
    requires java.net.http;
    requires pdf2dom;
    requires pdfbox;
    requires jdk.jconsole;
    exports notes;
    exports notes.view;
    exports notes.model;
}