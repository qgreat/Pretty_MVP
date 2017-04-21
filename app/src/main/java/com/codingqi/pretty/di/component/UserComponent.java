package me.jessyan.mvparms.pretty.di.component;

import com.codingqi.pretty.di.module.UserModule;
import com.codingqi.pretty.mvp.ui.activity.UserActivity;
import com.jess.arms.di.scope.ActivityScope;

import common.AppComponent;
import dagger.Component;

/**
 * Created by jess on 9/4/16 11:17
 * Contact with jess.yan.effort@gmail.com
 */
@ActivityScope
@Component(modules = UserModule.class,dependencies = AppComponent.class)
public interface UserComponent {
    void inject(UserActivity activity);
}
