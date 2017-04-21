package com.codingqi.pretty.di.component;

import com.codingqi.pretty.di.module.GirlModule;
import com.codingqi.pretty.mvp.ui.activity.GirlActivity;
import com.jess.arms.di.scope.ActivityScope;

import common.AppComponent;
import dagger.Component;

/**
 * Created by jess on 9/4/16 11:17
 * Contact with jess.yan.effort@gmail.com
 */
@ActivityScope
@Component(modules = GirlModule.class,dependencies = AppComponent.class)
public interface GirlComponent {
    void inject(GirlActivity activity);
}
