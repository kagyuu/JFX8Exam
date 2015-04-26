/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.jfxtemplate;

import javafx.application.Preloader;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *
 * @author atsushi
 */
@Data
@AllArgsConstructor
public class MyPreloaderNotification implements Preloader.PreloaderNotification {
    private String message;
}
