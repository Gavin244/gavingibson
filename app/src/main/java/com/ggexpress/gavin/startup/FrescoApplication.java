package com.ggexpress.gavin.startup;

import android.app.Application;

import com.ggexpress.gavin.cache.ImagePipelineConfigFactory;
import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created by 06peng on 2015/6/24.
 */
public class FrescoApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
    }
}