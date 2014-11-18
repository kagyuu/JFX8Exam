/* All Rights Reserved, Copyright (C) com.mycompany
 */
package com.mycompany.jfxtemplate.d3;

import com.mycompany.jfxtemplate.core.FXMLDialog;
import com.mycompany.jfxtemplate.core.MyDialog;
import com.mycompany.jfxtemplate.core.SimpleDialogController;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Sphere;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 3Dの練習.
 * @author atsushi
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class D3ExamController extends SimpleDialogController {
    @FXML
    private VBox vbox;

    @FXML
    private Label lblX;

    @FXML
    private Label lblY;
    
    @MyDialog
    private FXMLDialog myStage;

    private PerspectiveCamera camera;

    private double az = 0.0;
    private double el = 0.0;
    private double r = 100.0;
    private double mx = Double.NaN;
    private double my = Double.NaN;

    @FXML
    private void handleButtonAction(ActionEvent event) {
        az = 0.0;
        el = 0.0;
        r = 100.0;
        moveCamera();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // 3D Objects
        Group root = new Group();

        final Box xAxis = new Box(240.0, 1, 1);
        final Box yAxis = new Box(1, 240.0, 1);
        final Box zAxis = new Box(1, 1, 240.0);

        final PhongMaterial redMaterial = new PhongMaterial();
        redMaterial.setDiffuseColor(Color.DARKRED);
        redMaterial.setSpecularColor(Color.RED);

        final PhongMaterial greenMaterial = new PhongMaterial();
        greenMaterial.setDiffuseColor(Color.DARKGREEN);
        greenMaterial.setSpecularColor(Color.GREEN);

        final PhongMaterial blueMaterial = new PhongMaterial();
        blueMaterial.setDiffuseColor(Color.DARKBLUE);
        blueMaterial.setSpecularColor(Color.BLUE);

        xAxis.setMaterial(redMaterial);
        yAxis.setMaterial(greenMaterial);
        zAxis.setMaterial(blueMaterial);

        root.getChildren().addAll(xAxis, yAxis, zAxis);

        for (double th = -10.0 * Math.PI; th < 10.0 * Math.PI; th += Math.PI / 6.0) {
            Sphere sphere = new Sphere(2);
            sphere.setTranslateX(20.0 * Math.sin(th));
            sphere.setTranslateY((th / Math.PI) * 10.0);
            sphere.setTranslateZ(20.0 * Math.cos(th) - 20.0);
            sphere.setMaterial(greenMaterial);
            sphere.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
                System.out.println("緑 Clickされたよ:" + e.toString());
            });

            double th2 = th + Math.PI;
            Sphere sphere2 = new Sphere(2);
            sphere2.setTranslateX(20.0 * Math.sin(th2));
            sphere2.setTranslateY((th / Math.PI) * 10.0);
            sphere2.setTranslateZ(20.0 * Math.cos(th2) - 20.0);
            sphere2.setMaterial(redMaterial);
            sphere2.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
                System.out.println("赤 Clickされたよ" + e.toString());
            });

            root.getChildren().addAll(sphere, sphere2);
        }

        // 透視投影カメラを設定する
        camera = new PerspectiveCamera(true);

        // カメラの位置を (0, 0, -r) にする
        camera.setTranslateZ(-r);
        camera.setTranslateY(0.0);
        camera.setTranslateX(0.0);
        camera.setFarClip(800.0);

        // Sub Scene
        SubScene scene = new SubScene(root, 640, 480, true, SceneAntialiasing.DISABLED);
        scene.setFill(Color.BLACK);
        scene.setCamera(camera);

        Pane pane = new Pane();
        pane.getChildren().add(scene);

        vbox.getChildren().add(pane);

        // Add Event Listener
        pane.setOnMouseReleased(e -> {
            mx = Double.NaN;
            my = Double.NaN;
        });
        pane.setOnMouseDragged(e -> {
            double cx = e.getX();
            double cy = e.getY();
            if (!Double.isNaN(mx) && !Double.isNaN(my)) {
                az += Math.PI * (cx - mx) / 640.0;
                el += Math.PI * (cy - my) / 480.0;
                
                while (az < 0){
                    az += 2 * Math.PI;
                }
                while (az > 2 * Math.PI){
                    az -= 2 * Math.PI;
                }
                while (el < 0){
                    el += 2 * Math.PI;
                }
                while (el > 2 * Math.PI){
                    el -= 2 * Math.PI;
                }

                moveCamera();
            }
            mx = cx;
            my = cy;

        });

        pane.setOnScroll(e -> {
            double delta = e.getDeltaY();
            r += delta > 0 ? -10.0 : 10.0;
            moveCamera();
        });
    }

    private void moveCamera() {
        double x = r * Math.cos(el) * Math.sin(az);
        double y = r * Math.sin(el);
        double z = -r * Math.cos(el) * Math.cos(az);
        camera.setTranslateX(x);
        camera.setTranslateY(y);
        camera.setTranslateZ(z);

        // カメラのローテート
        // Y軸中心に -az ラジアン回転
        Rotation rAz = new Rotation(-az, 0, 1, 0);
        Quaternion qAz = new Quaternion(rAz);
        // Y軸(0,1,0) と カメラ位置(0,0,0)->(x,y,z) との法線ベクトルを中心に、-el ラジアン回転
        Rotation rEl = new Rotation(z > 0 ? el : -el, z, 0, x);
        Quaternion qEl = new Quaternion(rEl);
        // 回転を合成
        Quaternion qM = qAz.multilple(qEl);
        Rotation rM = new Rotation(qM);

        camera.setRotationAxis(new Point3D(rM.a, rM.b, rM.c));
        camera.setRotate(180.0 * rM.t / Math.PI);

        lblX.setText(String.format("[AZ] %02f [EL] %02f", az * 180.0 / Math.PI, el * 180.0 / Math.PI));
        lblY.setText(String.format("[X] %03f [Y] %03f [Z] %03f", x, y, z));
    }
}
