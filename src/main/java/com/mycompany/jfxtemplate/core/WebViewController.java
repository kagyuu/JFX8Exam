/* All Rights Reserved, Copyright (C) com.mycompany
 * この製品は、日本国著作権法及び国際条約により保護されています。
 * この製品の全部または一部を無断で複製した場合、著作権法の侵害となりますので、
 * ご注意ください。
 */
package com.mycompany.jfxtemplate.core;

import java.util.HashMap;
import java.util.Map;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.event.EventHandler;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebEvent;
import javafx.scene.web.WebView;
import netscape.javascript.JSException;
import netscape.javascript.JSObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * WebViewの操作にたいするフレームワーク.
 *
 * <pre>
 * HTML 側には、
 * ------------------------------------------------
 * var jBridge = null;
 * function isJBridgeReady() {
 *   return !(jBridge == null);
 * }
 * ------------------------------------------------
 * を記述してください。jBridge には、このオブジェクトが関連付けられ
 * Javascript 側から、このオブジェクトの public メソッドを実行する
 * ことができます。
 * たとえば、javascript 側から
 * ------------------------------------------------
 * function callJavaFX(msg) {
 *   if(isJBridgeReady()) {
 *     jBridge.callback(msg);
 *   } else {
 *     alert(msg);
 *   }
 * }
 * ------------------------------------------------
 * を呼び出すと、Java 側の WebController#callback(String) が呼び出されます。
 * </pre>
 *
 * @author hondou
 */
public abstract class WebViewController {

    /**
     * ページのロードが終わって、処理を実行できるかのフラグ.
     */
    private WebState state = WebState.LOADING;

    /**
     * WebView.
     */
    private WebView pWview;

    /**
     * Java-Javascript 接続の再試行回数.
     */
    private static final int RETRY_LIMIT = 1000;

    /**
     * WebEngineの初期化終了を待つときに、どれくらいの間隔で終了確認を行うか.
     */
    private static final long LOOP_WAIT = 500L;

    /**
     * ページのロードが終わって、処理を実行できるかを検証します.
     *
     * @return true:アクセス可<br/>false:アクセス不可
     */
    public WebState getState() {
        return state;
    }

    /**
     * WebView と WebController の関連付けを行います.
     *
     * @param wview WebView
     */
    public void bind(final WebView wview) {

        pWview = wview;

        final Object thisController = this;

        final Logger logger = LoggerFactory.getLogger(getClass());

        final WebEngine webEngine = wview.getEngine();

        // ----- 1.javascript:alert() が呼ばれたときのイベントハンドラを設定
        webEngine.setOnAlert(new EventHandler<WebEvent<String>>() {
            @Override
            public void handle(final WebEvent<String> event) {
                handleAlert(event);
            }
        });

        // ----- 2.ページのロード状態を監視するイベントハンドラを設定
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
                                    logger.error("Web View の初期化に失敗しました");
                                    state = WebState.EXIT_FAIL;
                                    handleLoadError(new Exception("HTMLのロードに失敗しました"));
                                }
                            }
                });

        // ----- 3.ページのエラー状態を監視するイベントハンドラを設定
        loadWorker.exceptionProperty().addListener(
                new ChangeListener<Throwable>() {
                    @Override
                    public void changed(
                            final ObservableValue<? extends Throwable> ov,
                            final Throwable oldValue, final Throwable newValue) {
                                logger.error("Web View がエラーを返しました", newValue);
                            }
                });

// このアプリでは WebView 上で右クリックを使う。ブラウザの右クリックメニューの抑止は javasctipt で行う
        // ----- 4.右クリックを握りつぶす
//		wview.setEventDispatcher(new EventDispatcher() {
//			private EventDispatcher originalDispatcher = wview.getEventDispatcher();
//
//			@Override
//			public Event dispatchEvent(final Event event, final EventDispatchChain tail) {
//				if (event instanceof MouseEvent) {
//					MouseEvent mouseEvent = (MouseEvent) event;
//					if (MouseButton.SECONDARY == mouseEvent.getButton()) {
//						mouseEvent.consume();
//					}
//				}
//				return originalDispatcher.dispatchEvent(event, tail);
//			}
//		});
    }

    /**
     * Javascript を呼び出します.
     *
     * @param function function名
     * @param args 引数
     * @return 返値
     */
    public Object callJs(final String function, final Object... args) {
        if (WebState.ACCESSIBLE != state) {
            throw new IllegalStateException("Webviewの初期化が終了していません");
        }
        WebEngine engine = pWview.getEngine();
        JSObject win = (JSObject) engine.executeScript("window");
        return win.call(function, args);
    }

    /**
     * JS-Java通信用の設定.
     *
     * @param engine WebEngine
     * @param callback Callbackオブジェクト(このWebViewController)
     * @param cnt 再試行回数
     */
    private void setCallBack(final WebEngine engine, final Object callback, final int cnt) {
        final Logger logger = LoggerFactory.getLogger(getClass());
        if (cnt > RETRY_LIMIT) {
            state = WebState.EXIT_FAIL;
            logger.error("Web View の初期化に失敗しました");
            handleLoadError(new Exception("Web View の初期化に失敗しました"));
            return;
        }

        javafx.application.Platform.runLater(new Runnable() {
            @Override
            public void run() {

                try {
                    logger.trace("Set Callback Object");
                    JSObject win = (JSObject) engine.executeScript("window");
                    win.setMember("jBridge", callback);

                    // Javascript エンジンが起動して、正常に通信できることを確認する
                    if ((Boolean) engine.executeScript("isJBridgeReady()")) {
                        logger.trace("Set Callback Object Success");
                        state = WebState.ACCESSIBLE;
                        onWebViewReady();
                        return;
                    }

                    logger.trace("Set Callback Object Fail. Retry later");
                    setCallBack(engine, callback, cnt + 1); // <-- ここでの this は、WebViewController ではなく Runnabale

                } catch (JSException ex) {
					// WebView は非同期で動いているので、Javascript エンジンが起動する前に
                    // isJBridgeReady() を呼ぶとエラーになる可能性がある。
                    // 何回か試せば成功する。
                    logger.trace("WebView 上の Javascript に Java への Callback を設定できませんでした。リトライします", ex);
                    setCallBack(engine, callback, cnt + 1); // <-- ここでの this は、WebViewController ではなく Runnabale
                }
            }
        });
    }

    /**
     * コンテンツを読み込みます(完了を待たずに復帰します).
     *
     * @param template テンプレート
     * @param params パラメータ
     */
    public void loadContentAsync(final String template, final Object[]... params) {
        javafx.application.Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Map<Object, Object> map = new HashMap<>();
                for (Object[] param : params) {
                    map.put(param[0], param[1]);
                }
                WebEngine webEngine = pWview.getEngine();
                webEngine.loadContent(MyVelocityUtil.merge(template, map));
            }
        });
    }

    /**
     * コンテンツを読み込みます(完了を待って復帰します).
     *
     * @param template テンプレート
     * @param params パラメータ
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
     * WebView 側から alert() が呼ばれたときに呼ばれます.
     *
     * @param event イベント情報
     */
    public abstract void handleAlert(WebEvent<String> event);

    /**
     * 画面ロード時にエラーが起きた時に呼ばれます.
     *
     * @param th エラー
     */
    public abstract void handleLoadError(Throwable th);

    /**
     * WebViewで描画終了時に呼ばれます.
     */
    public abstract void onWebViewReady();

}
