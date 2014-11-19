/* All Rights Reserved, Copyright (C) com.mycompany
 */
package com.mycompany.jfxtemplate.core;

import java.util.HashMap;
import java.util.Map;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.event.Event;
import javafx.scene.input.MouseButton;

import javafx.scene.input.MouseEvent;
import javafx.event.EventDispatchChain;
import javafx.event.EventDispatcher;
import javafx.event.EventHandler;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebEvent;
import javafx.scene.web.WebView;
import netscape.javascript.JSException;
import netscape.javascript.JSObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * WebView Controller.
 *
 * <pre>
 * (1) To use this controller, Write following javascripts :
 * ------------------------------------------------
 * var jBridge = null;
 * function isJBridgeReady() {
 *   return !(jBridge == null);
 * }
 * ------------------------------------------------
 *
 * (2) The jBridge will bind this object. So, you can invoke this object from
 * javascript like followings :
 * ------------------------------------------------
 * function callJavaFX(msg) {
 *   if(isJBridgeReady()) {
 *     jBridge.callback(msg);
 *   } else {
 *     alert(msg);
 *   }
 * }
 * ------------------------------------------------
 * thisJavaObj#callback(String) will be invoked.
 *
 * (3) There are some javascript event handlers on this object.
 *
 *   void handleAlert(WebEvent&lt;String&gt event);
 *     will be called when alert() was called in the javascript.
 *
 *   void handleLoadError(Throwable th);
 *     will be called when some error was occured int the Nashorn javascript
 *     engine.
 *
 *   void onWebViewReady();
 *     will be called when the html was ready. Exactry say, we regard that
 *     isJBridgeReady() javascript function returned true as html ready.
 * * (4) right click make disable at bind().
 * if you want to use right-click on webview, please comment out #4 statements.
 * </pre>
 *
 * @author atsushi
 */
public abstract class WebViewController {

    /**
     * Page state.
     */
    private WebState state = WebState.LOADING;

    /**
     * WebView.
     */
    private WebView pWview;

    /**
     * retry limit of calling isJBridgeReady().
     */
    private static final int RETRY_LIMIT = 1000;

    /**
     * retry interval for calling isJBridgeReady().
     */
    private static final long LOOP_WAIT = 500L;

    /**
     * @return state of html
     */
    public WebState getState() {
        return state;
    }

    /**
     * bind WebView to WebController.
     *
     * @param wview WebView
     */
    public void bind(final WebView wview) {

        pWview = wview;

        final Object thisController = this;

        final Logger logger = LoggerFactory.getLogger(getClass());

        final WebEngine webEngine = wview.getEngine();

        // ----- 1. bind event-handler for javascript:alert()
        webEngine.setOnAlert(new EventHandler<WebEvent<String>>() {
            @Override
            public void handle(final WebEvent<String> event) {
                handleAlert(event);
            }
        });

        // ----- 2.bind event-handler for page load
        Worker<Void> loadWorker = webEngine.getLoadWorker();
        loadWorker.stateProperty().addListener(
            new ChangeListener<Worker.State>() {
                @Override
                public void changed(
                    final ObservableValue<? extends Worker.State> ov,
                    final Worker.State oldValue,
                    final Worker.State newValue) {

                    state = WebState.LOADING;
                    logger.trace("State changed, old: {}, new: {}",
                            oldValue, newValue);
                    if (newValue == Worker.State.SUCCEEDED) {
                        setCallBack(webEngine, thisController, 0);
                    } else if (newValue == Worker.State.FAILED) {
                        logger.error("failed to initialize webview");
                        state = WebState.EXIT_FAIL;
                        handleLoadError(new Exception("failed to initialize webview"));
                    }
                }
            }
        );

        // ----- 3.bind event-hander for page error
        loadWorker.exceptionProperty().addListener(
            new ChangeListener<Throwable>() {
                @Override
                public void changed(
                    final ObservableValue<? extends Throwable> ov,
                    final Throwable oldValue, final Throwable newValue) {
                        logger.error("Web View returns error", newValue);
                }
            }
        );

        // ----- 4.diable right click
        // !!! if you want to use right-click on webview, please comment out followings. !!!
        wview.setEventDispatcher(new EventDispatcher() {
            private final EventDispatcher originalDispatcher = wview.getEventDispatcher();

            @Override
            public Event dispatchEvent(final Event event, final EventDispatchChain tail) {
                if (event instanceof MouseEvent) {
                    MouseEvent mouseEvent = (MouseEvent) event;
                    if (MouseButton.SECONDARY == mouseEvent.getButton()) {
                        mouseEvent.consume();
                    }
                }
                return originalDispatcher.dispatchEvent(event, tail);
            }
        });
    }

    /**
     * call Javascript function.
     *
     * @param function javascript function
     * @param args args
     * @return return value from javascript function.
     */
    public Object callJs(final String function, final Object... args) {
        if (WebState.ACCESSIBLE != state) {
            throw new IllegalStateException("Webview is not initialized.");
        }
        WebEngine engine = pWview.getEngine();
        JSObject win = (JSObject) engine.executeScript("window");
        return win.call(function, args);
    }

    /**
     * bind this object to jBridge on javascript.
     *
     * @param engine WebEngine
     * @param callback bind object (this object)
     * @param cnt retry count
     */
    private void setCallBack(final WebEngine engine, final Object callback, final int cnt) {
        final Logger logger = LoggerFactory.getLogger(getClass());
        if (cnt > RETRY_LIMIT) {
            logger.error("failed to initialize webview");
            state = WebState.EXIT_FAIL;
            handleLoadError(new Exception("failed to initialize webview"));
            return;
        }

        javafx.application.Platform.runLater(new Runnable() {
            @Override
            public void run() {

                try {
                    logger.trace("Set Callback Object");
                    JSObject win = (JSObject) engine.executeScript("window");
                    win.setMember("jBridge", callback);

                    // Is jBridge not empty?
                    if ((Boolean) engine.executeScript("isJBridgeReady()")) {
                        logger.trace("Set Callback Object Success");
                        state = WebState.ACCESSIBLE;
                        onWebViewReady();
                        return;
                    }
                    
                    // may be javascript engin wake up but jBridge is not bound.
                    logger.trace("Set Callback Object Fail. Retry later");
                    setCallBack(engine, callback, cnt + 1);

                } catch (JSException ex) {
                    // may be javascript engin not wake up.
                    logger.trace("Set Callback Object Fail. Retry later");
                    setCallBack(engine, callback, cnt + 1);
                }
            }
        });
    }

    /**
     * load contents asynchronusly.
     *
     * @param template template file
     * @param params params
     */
    public void loadContentAsync(final String template, final Object[]... params) {
        javafx.application.Platform.runLater(() -> {
            Map<Object, Object> map = new HashMap<>();
            for (Object[] param : params) {
                map.put(param[0], param[1]);
            }
            WebEngine webEngine = pWview.getEngine();
            webEngine.loadContent(MyVelocityUtil.merge(template, map));
        });
    }

    /**
     * load contents synchronusly.
     *
     * @param template template file
     * @param params params
     */
    public void loadContentSync(final String template, final Object[]... params) {
        loadContentAsync(template, params);
        while (getState() == WebState.LOADING) {
            try {
                Thread.sleep(LOOP_WAIT);
            } catch (InterruptedException e) {
                e = null;
            }
        }
    }

    /**
     * handle javascript:alert().
     *
     * @param event Event
     */
    public abstract void handleAlert(WebEvent<String> event);

    /**
     * handler errors fromm javascript engine.
     *
     * @param th error
     */
    public abstract void handleLoadError(Throwable th);

    /**
     * handler web view trun to ready.
     */
    public abstract void onWebViewReady();

}
