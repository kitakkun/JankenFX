module janken {
    requires javafx.controls;
    requires javafx.fxml;

    exports janken.system;
    exports janken.user;
    exports janken.view;
    exports janken.app;
    exports janken.util;
    opens janken.view to javafx.fxml;
    opens janken.app to javafx.fxml;
    exports janken.view.components;
    opens janken.view.components to javafx.fxml;
    exports janken.view.menus;
    opens janken.view.menus to javafx.fxml;
    exports janken.system.datamodel;
    exports janken.system.manager;
    exports janken.system.io;
    exports janken.system.preferences;
    exports janken.system.core;
    exports janken.system.tool;
}